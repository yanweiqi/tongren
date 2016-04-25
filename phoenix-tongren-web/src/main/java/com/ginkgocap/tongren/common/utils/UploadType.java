/*
 * 文件名： UploadType.java
 * 创建日期： 2015年10月14日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.common.utils;


 /**
 *  文件上传类型枚举对象
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月14日
 */
public enum UploadType {
	
	TEMP(0, "temp", "临时文件"), 
    IMAGE(1, "images", "图片"), 
    AUDIO(2, "audio", "音频"), 
    OR_PIC(4, "OR_PIC", "组织头像"),
    OBJECT(3,"object","文件");

    private int code;
    private String name;
    private String desc;

    private UploadType(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
