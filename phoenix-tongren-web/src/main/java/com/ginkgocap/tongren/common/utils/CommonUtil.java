package com.ginkgocap.tongren.common.utils;


import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class CommonUtil {

	public static final String JTMOBILE_SERVER_ROOT = ResourceBundle.getBundle("application").getString("jtmobileserver.root");
	static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 获取本机地址，格式为：http://www.gingont.com:4445/，或者为:http://192.168.101.41:4445/
	 * @param request
	 * @return
	 */
	public static String getServerDomainPath(HttpServletRequest request) {
		if (StringUtils.isNotBlank(JTMOBILE_SERVER_ROOT) && JTMOBILE_SERVER_ROOT.startsWith("http") && JTMOBILE_SERVER_ROOT.endsWith("/"))
			return JTMOBILE_SERVER_ROOT;
		if (null == request)
			throw new RuntimeException("传入参数不正确");
		StringBuilder link = new StringBuilder();
		link.append(request.getScheme()).append("://").append(request.getServerName());
		if ((request.getScheme().equals("http") && request.getServerPort() != 80)
				|| (request.getScheme().equals("https")
				&& request.getServerPort() != 443)) {
			link.append(':');
			link.append(request.getServerPort());
		}
		link.append(request.getContextPath());
		link.append("/");
		return link.toString();
	}
	
	/**
	 * web端的JavaScript对长整型的数值无法处理，因此需要转换为字符串
	 */
	public static GsonBuilder createGsonBuilder() {
		GsonBuilder gBuilder = new GsonBuilder().disableHtmlEscaping();
		if (CommonUtil.getRequestIsFromWebFlag() == false) {
			return gBuilder;//不是web端过来的请求，则长整型数值json后不加引号
		}
		gBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(Number.class, new TypeAdapter<Number>() {
			@Override
			public Number read(JsonReader in) throws IOException {
				JsonToken jsonToken = in.peek();
				switch (jsonToken) {
					case NULL:
						in.nextNull();
						return null;
					case NUMBER:
						return new LazilyParsedNumber(in.nextString());
					default:
						throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
				}
			}

			@Override
			public void write(JsonWriter out, Number value) throws IOException {
				if (value != null && (value instanceof Long || value instanceof BigInteger))
					out.value(value.toString());
				out.value(value);
			}
		}));
		gBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigInteger.class, new TypeAdapter<BigInteger>() {
			@Override
			public BigInteger read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				try {
					return new BigInteger(in.nextString());
				} catch (NumberFormatException e) {
					throw new JsonSyntaxException(e);
				}
			}

			@Override
			public void write(JsonWriter out, BigInteger value) throws IOException {
				if (null == value)
					out.value(value);
				out.value(value.toString());
			}
		}));
		gBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		return gBuilder;
	}
	
	/**
	 * web端的JavaScript对长整型的数值无法处理，因此需要转换为字符串
	 */
	public static Gson createGson() {
		Gson gson = createGsonBuilder().create();
		return gson;
	}

	public static long getLongFromJSONObject(JSONObject jsonObject, String key) {
		String value = jsonObject.getString(key);
		if (StringUtils.isBlank(value) || StringUtils.isNumeric(value) == false)
			return 0L;
		return Long.valueOf(value);
	}
	
	public static long optLongFromJSONObject(JSONObject jsonObject, String key) {
		String value = jsonObject.optString(key);
		long result = 0L;
		if (StringUtils.isBlank(value))
			return result;
		try {
			return Long.valueOf(value.trim());
		} catch (Exception e) {
			logger.error("convert string [ " + value.trim() + " ] into long error:" + e);
			return result;
		}
	}

	private static final ThreadLocal<Boolean> REQUEST_IS_FROM_WEB = new ThreadLocal<Boolean>(){
		protected Boolean initialValue() {
			return false;
		}
	};
	public static void setRequestIsFromWebFlag(boolean requestIsFromWeb) {
		REQUEST_IS_FROM_WEB.set(requestIsFromWeb);
	}
	public static boolean getRequestIsFromWebFlag() {
		return REQUEST_IS_FROM_WEB.get();
	}
	
	 /**
     * 判断对象是否为null或空
     * @param obj
     * return IOException
     */
	public static boolean isNullOrEmpty(Object obj){
        if (obj == null)  
            return true;  
  
        if (obj instanceof CharSequence)  
            return ((CharSequence) obj).length() == 0;  
  
        if (obj instanceof Collection)  
            return ((Collection) obj).isEmpty();  
  
        if (obj instanceof Map)  
            return ((Map) obj).isEmpty();  
  
        if (obj instanceof Object[]) {  
            Object[] object = (Object[]) obj;  
            if (object.length == 0) {  
                return true;  
            }  
            boolean empty = true;  
            for (int i = 0; i < object.length; i++) {  
                if (!isNullOrEmpty(object[i])) {  
                    empty = false;  
                    break;  
                }  
            }  
            return empty;  
        }  
        return false;  
    }
	
    /**
     * 功能描述：去掉特殊字符
     * @param source
     * @return dest
     */
    public static String replaceSpecial(String source) {
        String dest = "";
        if (source != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(source);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
