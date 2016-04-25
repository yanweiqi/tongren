/*
 * 文件名： ImageScaleServiceImpl.java
 * 创建日期： 2015年10月15日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.upload.service.impl;

import java.io.File;

import magick.Magick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ginkgocap.tongren.common.utils.AverageImageScale;
import com.ginkgocap.tongren.common.utils.MagickImageScale;
import com.ginkgocap.tongren.upload.service.ImageScaleService;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月15日
 */
public class ImageScaleServiceImpl implements ImageScaleService {
	
	private boolean isMagick = false;
    private boolean tryMagick = true;

    private int boxWidth = 100;
    private int boxHeight = 100;
    
    private static final Log log = LogFactory.getLog(ImageScaleServiceImpl.class);
	
    /**
     * 检查是否安装magick
     */
    public void init() {
        if (tryMagick) {
            try {
                System.setProperty("jmagick.systemclassloader", "no");

                new Magick();
                log.info("using jmagick");
                isMagick = true;
            } catch (Throwable e) {
                log.warn("load jmagick fail, use java image scale. message:" + e.getMessage(), e);
                isMagick = false;
            }
        } else {
            log.info("jmagick is disabled.");
            isMagick = false;
        }
    }
    
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.upload.service.ImageScaleService#resizeFix(java.io.File, java.io.File)
	 */
	@Override
	public void resizeFix(File srcFile, File destFile) throws Exception {
		resizeFix(srcFile, destFile, boxWidth, boxHeight);

	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.upload.service.ImageScaleService#resizeFix(java.io.File, java.io.File, int, int)
	 */
	@Override
	public void resizeFix(File srcFile, File destFile, int width, int height)
			throws Exception {
		if (isMagick) {
            MagickImageScale.resizeFix(srcFile, destFile, width, height);
        } else {
            AverageImageScale.resizeFix(srcFile, destFile, width, height);
        }

	}

}
