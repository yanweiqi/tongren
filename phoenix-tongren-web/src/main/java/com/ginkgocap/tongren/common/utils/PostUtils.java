package com.ginkgocap.tongren.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostUtils {
	private static final Logger logger = LoggerFactory.getLogger(PostUtils.class);

	/**
	 * 上传附件到指定的url
	 * @param localPath
	 * @param url
	 */
	public static String uploadFile(String localPath,String subName, String url) {
		CloseableHttpClient httpclient =HttpClients.createDefault();
		try {
			logger.info("begin upload "+localPath+","+subName+","+url);
			HttpPost httppost = new HttpPost(url);
			File file = new File(localPath);
			if (file.exists() == false) {
				logger.info("not found file " + localPath);
				return null;
			}
			FileBody fbody = new FileBody(file);
		

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", fbody);
			if(subName!=null){
				reqEntity.addPart("subName", new StringBody(subName));
			}
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				String body = EntityUtils.toString(resEntity);
				logger.info("upload success "+localPath+","+body);
				return body;
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
		return null;
	}
	
	public static String downloadFile(String url,String localDir) throws Exception{
		CloseableHttpClient httpclient =HttpClients.createDefault();
		HttpGet httpGet =new HttpGet(url);
		HttpResponse response= httpclient.execute(httpGet);
		logger.info("response status: " + response.getStatusLine());
		Header headr=response.getLastHeader("Content-Disposition");
		if(headr!=null&&headr.getValue().contains("attachment")){
			
			try {
				String localFile=localDir+File.separator+"file_"+(System.currentTimeMillis()+"").substring(5)+".zip";
				FileOutputStream fos=new FileOutputStream(localFile);
				InputStream is=response.getEntity().getContent();
				byte[] bs=new byte[1024];
				int len=is.read(bs);
				while(len>0){
					fos.write(bs, 0, len);
					len=is.read(bs);
				}
				fos.close();
				is.close();
				logger.info("has download "+localFile);
				return localFile;
			} catch (Exception e) {
				logger.error("download failed "+url+","+localDir,e);
			}
			
		}
		return null;
	}
	public static void main(String[] args) {
//		String downLoadUrl=uploadFile("D:/tmp/word2pdf/ZJXL_HYBDS_Hadoop系统安装配置说明书.doc","213123123123","http://localhost:5060/onlineview/upload.do");
		//logger.info("url is "+downLoadUrl);
		try {
			String path=downloadFile("http://localhost:5060/onlineview/download.do?p=43F9F966775888339F62B7105785B130EAC4503EF11E195E5804360F9BFFCBF7F0EA2E7FAEA7A872","d://tmp//img");
			logger.info("path is "+path);
			ZipUtil.unZipFiles(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
