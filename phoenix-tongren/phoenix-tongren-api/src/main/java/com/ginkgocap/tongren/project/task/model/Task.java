package com.ginkgocap.tongren.project.task.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.tree.Node;
import com.ginkgocap.tongren.common.util.DateUtil;

/**
 * 项目任务表
 * @author Administrator
 *
 */

@Entity
@Table(name="tb_project_task")
public class Task extends Node implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private long id;//主键
	
	private long projectUndertakenId;//承接项目id
	
	private String title;//名称
	
	private int cycle;//任务周期,单位天
	
	private Timestamp startTime;//起始时间
	
	private Timestamp endTime;//结束时间
	
	private Timestamp createTime;//创建时间
	
	private long taskPid;//父任务id
		
	private long createId;//创建人id
	
	private int taskStatus;//0 准备中 1 已开始 2 已完成 3 已过期 6已驳回 9(无效)已删除
	
	private float progress;//0-100 范围内的浮点数，不能超过这个范围
	
	private long organizationId;//此任务对应项目的所属组织id
	
	private String taskDescription;//
	
	private int taskType;//0 项目任务 1 组织任务
	
	private String attachId;//附件id
	
	private Date partitionDate;//日期分区字段
	
	private long lastRemindTime;//任务最后一次提醒时间

	@Id
	@GeneratedValue(generator = "TaskID")
	@GenericGenerator(name = "TaskID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "TaskID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name="project_undertaken_id")
	public long getProjectUndertakenId() {
		return projectUndertakenId;
	}

	public void setProjectUndertakenId(long projectUndertakenId) {
		this.projectUndertakenId = projectUndertakenId;
	}

	@Column(name="title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="cycle")
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	@Column(name="start_time")
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name="end_time")
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Column(name="task_pid")
	public long getTaskPid() {
		return taskPid;
	}

	public void setTaskPid(long taskPid) {
		this.taskPid = taskPid;
	}

	@Column(name="create_id")
	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	@Column(name="task_status")
	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.partitionDate=DateUtil.trunck(createTime);
		this.createTime = createTime;
	}

	@Column(name="progress")
	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Column(name="task_description")
	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	@Override
	@Transient
	public long getPid() {
		
		return taskPid;
	}
	
	@Column(name="task_type")
	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	@Column(name="attach_id")
	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	@Column(name="last_remind_time")
	public long getLastRemindTime() {
		return lastRemindTime;
	}

	public void setLastRemindTime(Long lastRemindTime) {
		if(lastRemindTime==null){
			lastRemindTime=0l;
		}
		this.lastRemindTime = lastRemindTime;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", projectUndertakenId=" + projectUndertakenId + ", title=" + title + ", cycle=" + cycle + ", startTime="
				+ startTime + ", endTime=" + endTime + ", createTime=" + createTime + ", taskPid=" + taskPid + ", createId=" + createId
				+ ", taskStatus=" + taskStatus + ", progress=" + progress + ", organizationId=" + organizationId + ", taskDescription="
				+ taskDescription + ", taskType=" + taskType + ", attachId=" + attachId + "]";
	}

	@Column(name="partition_date")
	public Date getPartitionDate() {
		return partitionDate;
	}

	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}

	@Transient
	public String getQueryDate() {
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMM");
		return sf.format(this.createTime);
	}

}
