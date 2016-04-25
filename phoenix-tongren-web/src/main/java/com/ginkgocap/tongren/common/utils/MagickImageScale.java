/*
 * 文件名： MagickImageScale.java
 * 创建日期： 2015年10月15日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.common.utils;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月15日
 */
public class MagickImageScale {
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
     * @throws MagickException
     */
    public static void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight) throws IOException, MagickException {
        ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
        MagickImage image = new MagickImage(info);
        // 计算缩小宽高
        Dimension dim = image.getDimension();
        int width = (int) dim.getWidth();
        int height = (int) dim.getHeight();
        int zoomWidth;
        int zoomHeight;
        if ((float) width / height > (float) boxWidth / boxHeight) {
            zoomWidth = boxWidth;
            zoomHeight = Math.round((float) boxWidth * height / width);
        } else {
            zoomWidth = Math.round((float) boxHeight * width / height);
            zoomHeight = boxHeight;
        }
        // 缩小
        MagickImage scaled = image.scaleImage(zoomWidth, zoomHeight);
        // 输出
        scaled.setFileName(destFile.getAbsolutePath());
        scaled.writeImage(info);
        scaled.destroyImages();
    }
}
