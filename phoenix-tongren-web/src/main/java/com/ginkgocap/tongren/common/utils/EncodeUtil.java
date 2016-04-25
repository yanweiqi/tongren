package com.ginkgocap.tongren.common.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EncodeUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(EncodeUtil.class);

	// 加密
	public static String encrypt(String value) {
		int hashIterations = 5000;
		RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();
		String salt = saltGenerator.nextBytes().toHex();
		return new Sha256Hash(value, salt, hashIterations).toHex();
	}

	// 使用给定的salt加密
	public static String encryptBySalt(String value, String salt) {
		int hashIterations = 5000;
		RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();
		return new Sha256Hash(value, salt, hashIterations).toHex();
	}

	public static String getRandomSalt() {
		RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();
		return saltGenerator.nextBytes().toHex();
	}


	// 字符串转字节数组
	public static byte[] toByte(String source, String encoding) {
		try {
			return source.getBytes();
		} catch (Exception e) {
			logger.error("字符串 " + source + " 转成字节数组错误  " + e.toString());
		}
		return null;
	}

	// 使用AES 128位加密生成串
	public static String aesEncrypt(String src, byte[] key) {
//		try {
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");//linux系统需要指定
//			secureRandom.setSeed(key);  
//			kgen.init(128, secureRandom);
//			SecretKey secretKey = kgen.generateKey();
//			byte[] enCodeFormat = secretKey.getEncoded();
//			SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
//			Cipher cipher = Cipher.getInstance("AES");
//			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//			byte[] encrypted = cipher.doFinal(toByte(src));
//			return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
//		} catch (Exception e) {
//			logger.error("加密用户信息出错 " + e.toString());
//		}
		return null;
	}

	// 使用AES 128位解密
	public static String aesDecrypt(String src, byte[] key) {
		try {
//			byte[] content = new BASE64Decoder().decodeBuffer(src);// 先用base64解密
			byte[] content = Base64.decode(src);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key);  
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);// 初始化
			byte[] result = cipher.doFinal(content);
			String originalString = new String(result);
			return originalString;
		} catch (Exception e) {
			logger.error("解密用户信息出错 " + e.toString());
		}
		return null;
	}
	
	//字符串转成十六进制字符串
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String sc = Integer.toHexString(ch);
			str = str + sc;
		}
		return str;
	}
	
}
