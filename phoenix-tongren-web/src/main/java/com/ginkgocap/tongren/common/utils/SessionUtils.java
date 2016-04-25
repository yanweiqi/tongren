package com.ginkgocap.tongren.common.utils;

import java.util.Random;
/**
 * @Title: SessionUtils.java
 * @Package com.ginkgocap.ywxt.utils
 * @Description:
 * @author haiyan
 * @date 2015-5-28 下午3:38:54
 */
public class SessionUtils {

	public String genWebSessionId() {
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long currtime = System.currentTimeMillis();
		byte[] b = ("web" + randomCount + currtime + "").getBytes();
		return Encodes.encodeBase64(b);
	}
	
	public static String genAppSessionId(String imei){
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long curtm = System.currentTimeMillis();
		byte[] b = (imei + randomCount + curtm + "").getBytes();
		return Encodes.encodeBase64(b);
	}
}
