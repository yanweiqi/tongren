package com.ginkgocap.tongren.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class PostUtil {

	private final static Logger logger = LoggerFactory.getLogger(PostUtil.class);
	
	private  String cookie="sessionID=\"d2ViNzI5MDE0NDc3NTE1MDU4NDE=\"";
	private  String baseUrl="http://localhost/";
	private  String downLoadDir=".";

	public static void main(String[] args) {
		PostUtil pt=new PostUtil("sessionID=\""+"d2ViNjc4MzE0NTkxNTY4Nzk1MTg="+"\"", "");
		String url="http://192.168.120.234:3378/knowledge/getKnowledgeRelatedResources.json";
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "2");
		try {
			String str=pt.testURIWithBody(url, params);
			logger.info("str is "+str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PostUtil(String cookie,String baseUrl ){
		this.cookie=cookie;
		this.baseUrl=baseUrl;
	}
	
	public  String testURI(String uri,Map<String,String> content) throws Exception{
		return testURI(uri,JSON.toJSONString(content),false);
	}
	public  String testURIWithBody(String uri,Map<String,String> content) throws Exception{
		logger.info("begin post with "+uri);
		return testURI(uri,JSON.toJSONString(content),true);
	}
	public  String testURI(String uri,Object content,boolean bodyType) throws Exception{
		return testURI(uri,JSON.toJSONString(content),bodyType);
	}
	
	public  String testURI(String uri,String content,boolean bodyType) throws Exception{
		DefaultHttpClient httpclient = new DefaultHttpClient();  
		HttpPost hp=postForm(baseUrl+uri, content,bodyType);
		String str=invoke(httpclient,hp);
		//str=new String(str.getBytes("iso-8859-1"),"utf-8");
		return str;
	}
	
	private  Map<String, String> parseReqFile(String path) throws Exception{
		BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String str=bf.readLine();
		 Map<String, String> map=new HashMap<String, String>();
		while(str!=null){
			if(str.startsWith("#")==false){
				int index=str.indexOf("requestJson=");
				if(index>=0){
					String key=str.substring(0,index-1);
					String val=str.substring(index+"requestJson=".length(),str.length());
					map.put(key, val);
				}
			}
			str=bf.readLine();
		}
		bf.close();
		return map;
	}
	
	/**
	 * 上传附件到指定的url
	 * @param localPath
	 * @param url
	 */
	public void uploadFile(String localPath, String url) {
		CloseableHttpClient httpclient =HttpClients.createDefault();
		try {

			HttpPost httppost = new HttpPost(url);
			File file = new File(localPath);
			if (file.exists() == false) {
				logger.info("not found file " + localPath);
				return;
			}
			FileBody fbody = new FileBody(file);
			StringBody strBody = new StringBody("12123123123123");

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("subName", fbody);
			reqEntity.addPart("file2", strBody);

			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				logger.info("服务器正常响应.....");
//				HttpEntity resEntity = response.getEntity();
//				System.out.println(EntityUtils.toString(resEntity));// httpclient自带的工具类读取返回数据
//				System.out.println(resEntity.getContent());
//				EntityUtils.consume(resEntity);
			}

		} catch (Exception e) {
			logger.error("upload failed! "+localPath+",url:"+url,e);
		} finally {
			try {
				httpclient.close();
			} catch (Exception ignore) {

			}
		}
	}
	
	private  HttpPost postForm(String url, String content,boolean bodyType) {

		HttpPost httpost = new HttpPost(url);
		httpost.addHeader("Cookie", cookie);
		httpost.addHeader("s", "web");
		//httpost.addHeader("sessionID", "d2ViNDM5NzE0NTY0NTA4MDQ3MDE=");
		int index=cookie.indexOf('=');
		String str=cookie.substring(index+2,cookie.length()-1);
		System.out.println("cookie is "+str);
		httpost.addHeader("sessionID", str);
		try {
			HttpEntity entity=null;
			if(bodyType){
				entity=new StringEntity(content, "UTF-8");
			}else{
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("requestJson", content));
				entity=new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			}
			httpost.setEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return httpost;
	}

	private  String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) throws Exception {

		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) throws IOException, IOException {
		logger.info("execute post...");
		HttpResponse response = null;
		response = httpclient.execute(httpost);
		return response;
	}

	private  String paseResponse(HttpResponse response) {
		logger.info("get response from http server..");
		HttpEntity entity = response.getEntity();

		logger.info("response status: " + response.getStatusLine());
		Header headr=response.getLastHeader("Content-Disposition");
		if(headr!=null&&headr.getValue().contains("attachment")){
			String fheader=headr.getValue();
			logger.info("fileName:"+fheader);
			String fn=fheader.substring(fheader.indexOf("filename=")+"filename=".length());
			fn=downLoadDir+File.separator+fn;
			try {
				FileOutputStream fos=new FileOutputStream(fn);
				InputStream is=response.getEntity().getContent();
				byte[] bs=new byte[1024];
				int len=is.read(bs);
				while(len>0){
					fos.write(bs, 0, len);
					len=is.read(bs);
				}
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("has download "+fn);
			return "";
		}

		String body = null;
		try {
			body = EntityUtils.toString(entity);
			logger.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}
}
