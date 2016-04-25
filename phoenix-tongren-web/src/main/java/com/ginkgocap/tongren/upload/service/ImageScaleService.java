/*
 * 文件名： ImageScaleService.java
 * 创建日期： 2015年10月15日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.upload.service;

import java.io.File;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月15日
 */
public interface ImageScaleService {
	/**
     * 缩小图片
     * 
     * @param srcFile
     *            原图片
     * @param destFile
     *            目标图片
     * @throws IOException
     */
    void resizeFix(File srcFile, File destFile) throws Exception;
    
    /**
     * 缩小图片
     * 
     * @param srcFile
     *            原图片
     * @param destFile
     *            目标图片
     * @param boxWidth
     *            缩略图最大宽度
     * @param boxHeight
     *            缩略图最大高度
     * @throws IOException
     */
    void resizeFix(File srcFile, File destFile, int width, int height) throws Exception;
}
