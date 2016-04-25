package com.ginkgocap.tongren.project.system.code;

/**
 * 项目任务操作code
 * @author liweichao
 *
 */
public enum TaskCode {
	
	
	DEFER("01","延期项目"),
	CHILD("02","创建子任务"),
	ALLOT("03","分配任务"),
	START("04","开始任务"),
	FINISH("05","完成任务"),
	SUBDOC("06","提交文档"),
	EXIT_TASK("07","退回任务"),
	RETRY("08","重发组织任务");
	private String code;
	private String message;
	
	 private TaskCode(String code, String message) {
	        this.code = code;
	        this.message = message;
	    }
	    
	    public String getCode() {
	        return code;
	    }
	    
	    public void setCode(String code) {
	        this.code = code;
	    }
	    
	    public String getMessage() {
	        return message;
	    }
	    
	    public void setMessage(String message) {
	        this.message = message;
	    }
}
