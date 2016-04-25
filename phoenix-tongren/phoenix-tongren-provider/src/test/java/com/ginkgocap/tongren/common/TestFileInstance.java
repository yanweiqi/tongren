package com.ginkgocap.tongren.common;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class TestFileInstance extends SpringContextTestCase{
	
	@Test
	public void testGetVal(){
		logger.info(FileInstance.REMOVE_FILE_ROOT_PATH);
		logger.info(FileInstance.FILE_ENCODING);
		logger.info(FileInstance.FTP_USER_NAME);// FTP 用户名(测试环境)
		logger.info(FileInstance.FTP_PASSWORD);// FTP 密码(测试环境)
		logger.info(FileInstance.FTP_IP);//  FTP 地址
		logger.info(FileInstance.FTP_URL);// FTP_URL 地址url
		logger.info(FileInstance.FTP_PORT);// FTP 端口
		logger.info(FileInstance.ENCODING);//全文编码格式
		logger.info(FileInstance.DOWNLOAD_PATH);//文件下载本地地址
		logger.info(FileInstance.BIG_IMG_WEIGHT);//大图片宽度
		logger.info(FileInstance.BIG_IMG_HEIGHT);//大图片高度
		logger.info(FileInstance.MID_IMG_WEIGHT);//中图片宽度
		logger.info(FileInstance.MID_IMG_HEIGHT);//中图片高度
		logger.info(FileInstance.SM_IMG_WEIGHT);//小图片宽度
		logger.info(FileInstance.SM_IMG_HEIGHT);//小图片高度
		logger.info(FileInstance.MM_IMG_WEIGHT);//极小图片宽度
		logger.info(FileInstance.MM_IMG_HEIGHT);//极小图片高度
		logger.info(FileInstance.IMG_MAX_SIZE);//图片上传大小最大值2M
		logger.info(FileInstance.IMG_MAX_HEIGHT);//图片上传大小最大高度
		logger.info(FileInstance.IMG_MAX_WIDTH);//图片上传大小最大宽度
		logger.info(FileInstance.FTP_WEB);//web url头
		logger.info(FileInstance.FTP_FULL_URL);//文件上传后的ULR头
	}

}
