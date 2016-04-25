package com.ginkgocap.tongren.project.task.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 任务进度表
 * @author Administrator
 *
 */
public class TaskProgress implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//主键
	
	private long assignTaskId;//
	
	private Timestamp startTime;//开始时间
	
	private Timestamp costingTime;//耗时天
	
	private Timestamp completeTime;//完成时间
	
	private float completeProcess;//完成进度

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAssignTaskId() {
		return assignTaskId;
	}

	public void setAssignTaskId(long assignTaskId) {
		this.assignTaskId = assignTaskId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getCostingTime() {
		return costingTime;
	}

	public void setCostingTime(Timestamp costingTime) {
		this.costingTime = costingTime;
	}

	public Timestamp getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

	public float getCompleteProcess() {
		return completeProcess;
	}

	public void setCompleteProcess(float completeProcess) {
		this.completeProcess = completeProcess;
	}
	
	
}
