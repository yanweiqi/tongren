package com.ginkgocap.tongren.common.utils;

import java.io.File;
import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.ginkgocap.tongren.common.FileInstance;

/**
 * 将Word文档转换成html字符串的工具类
 * 
 * @author MZULE
 * 
 */
public class Doc2Html {
	private static final Logger logger = LoggerFactory.getLogger(Doc2Html.class);

	public static void main(String[] args) throws Exception {
		convert(args[0], args[1],args[3]);
	}

	/**
	 * 将word文档转换成html文档
	 * 
	 * @param docFile
	 *            需要转换的word文档
	 * @param filepath
	 *            转换之后html的存放路径
	 * @return 转换之后的html文件
	 */
	public static File convert(String docPath, String htmlDir,String htmlName) {
		File docFile=new File(docPath);
		logger.info("begin convert "+docPath+"-->"+htmlDir+"/"+htmlName);
		if(docFile.exists()==false){
			return null;
		}
		File htmlFile = new File(htmlDir, htmlName+ ".html");
		// 创建Openoffice连接
		String url=FileInstance.getValue("openofficeurl", "127.0.0.1:8100");
		logger.info("wille connect to "+url);
		String as[]=url.split(":");
		OpenOfficeConnection con = new SocketOpenOfficeConnection(as[0], Integer.parseInt(as[1]));
		try {
			// 连接
			con.connect();
		} catch (ConnectException e) {
			logger.error("conect to "+url+",failed!",e);
			return null;
			
		}
		// 创建转换器
		try {
			DocumentConverter converter = new OpenOfficeDocumentConverter(con);
			logger.info("htmlFile is :" + htmlFile.getAbsolutePath());
			// 转换文档问html
			converter.convert(docFile, htmlFile);
			logger.info("convert "+docPath+" to "+htmlFile.getAbsolutePath()+",success!");

		} catch (Exception e) {
			logger.error("convert failed "+docPath+","+htmlDir+","+htmlName,e);
			return null;
		} finally {
			con.disconnect();
		}
		return htmlFile;
	}

}
