package com.ginkgocap.tongren.common.util;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 识别txt文件的编码格式
 * @author hanxifa
 *
 */
public class FileEncodeUtil {

	/**
	 * 识别一个文本文件是gbk还是utf-8
	 * @return
	 */
	public static String getFileEncode(String filePath){
		FileInputStream fi=null;
		String encode=null;
		try {
			fi=new FileInputStream(filePath);
			byte[] buffer=new byte[10];
			int readLen=fi.read(buffer);
			if(readLen<=0){
				encode="gbk"; 
				return encode; 
			}
				//微软在windows平台下用自带的notepad.exe生成UTF-8编码的文本文件时会在文件开头加入三个字节的
				//BOM（byte order mark）EF BB BF
			if(readLen>3){
				if(buffer[0]==-17&&buffer[1]==-69&&buffer[2]==-65){
					encode="utf-8";
					return encode; 
				}
			}
			boolean isHead=true;
			int encodeLen=0;//编码长度
			while(readLen>0){
				for(int i=0;i<readLen;i++){
					String str=toBinaryString(buffer[i]);
					if(isHead){
						isHead=false;
						if(str.charAt(0)=='0'){
							encodeLen=0;
							isHead=true;
						}else if(str.startsWith("110")){
							encodeLen=1;
						}else if(str.startsWith("1110")){
							encodeLen=2;
						}else if(str.startsWith("11110")){
							encodeLen=3;
						}else if(str.startsWith("111110")){
							encodeLen=4;
						}else if(str.startsWith("1111110")){
							encodeLen=5;
						}else{
							return "gbk";
						}
					}else{
						if(str.startsWith("10")){
							encodeLen--;
							if(encodeLen==0){
								isHead=true;
							}
						}else{
							return "gbk";
						}
					}
					
				}
				readLen=fi.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fi.close();
			} catch (IOException e) {
			}
		}
		
		return "utf-8";
	}
	
	private static String toBinaryString(byte x){
		String str=Integer.toBinaryString(x);
		if(str.length()<8){
			for(int k=8-str.length();k>0;k--){
				str='0'+str;
			}
		}else if(str.length()>8){
			str=str.substring(str.length()-8);
		}
		return str;
	}
	public static void main(String[] args) {
	//	System.out.println(getFileEncode("d:\\tmp\\《大秦帝国》孙皓晖.txt"));
		///System.out.println(getFileEncode("d:\\tmp\\b.txt"));
	System.out.println(toBinaryString((byte) -3));

	}
}
