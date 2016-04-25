package com.ginkgocap.tongren.common.model;

import java.io.Serializable;

/**
 * 
 * @author hanxifa
 *
 */
public class JmsMsgBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	Project_Publish_Success(1,"发布成功",21),     //项目发布
    Project_Publish_Discussed(2,"屏蔽发布",22),   //项目屏蔽、项目删除
    Project_Publish_Expired(3,"发布已过期",23),   //项目过期
    Project_Publish_Complect(4,"项目完成",24),    //项目完成
    Project_Publish_Abort(5,"项目放弃",25),       //项目放弃
    Project_Publish_Delay(6,"项目延期",26),       //项目延期
    Project_Publish_Expired_Unfinished(7,"项目过期未完成",27), //项目过期未完成
    Project_Publish_Inprogress(8,"项目进行中",28),               //项目进行中
    Project_Publish_Resumit(9,"重新发布项目",29),                //重新发布项目
	 * 增加组织:organization,1
	 * 解散组织:organization,2
	 */
	
	//消息的大类，比如项目，组织 
	private String opType; 
	
	//操作类型，比如增加， 删除，修改等
	private int operator;
	
	
	private Object content;//消息内容
	
	private long id;//消息 id，每个消息唯一


	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}
	

}
