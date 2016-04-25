package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 项目完成交付
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project_delivery")
public class Delivery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//主键
	
	private long delivererId;//交付人ID
	
	private long deliveryOrganizationId;//交付组织ID
	
	private long projectUndertakenId;//项目承接ID
	
	private Timestamp deliverTime;//交付时间
	
	private int status;//0按期完成、1延期完成
	
	private long createUserId;//项目创建人ID
	
	private long createOrganizationId;//项目创建组织ID
	@Id
	@GeneratedValue(generator = "DeliveryId")
	@GenericGenerator(name = "DeliveryId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "DeliveryId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "deliverer_id")
	public long getDelivererId() {
		return delivererId;
	}

	public void setDelivererId(long delivererId) {
		this.delivererId = delivererId;
	}
	@Column(name = "delivery_organization_id")
	public long getDeliveryOrganizationId() {
		return deliveryOrganizationId;
	}

	public void setDeliveryOrganizationId(long deliveryOrganizationId) {
		this.deliveryOrganizationId = deliveryOrganizationId;
	}
	@Column(name = "project_undertaken_id")
	public long getProjectUndertakenId() {
		return projectUndertakenId;
	}

	public void setProjectUndertakenId(long projectUndertakenId) {
		this.projectUndertakenId = projectUndertakenId;
	}
	@Column(name = "deliver_time")
	public Timestamp getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Timestamp deliverTime) {
		this.deliverTime = deliverTime;
	}
	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name = "create_user_id")
	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "create_organization_id")
	public long getCreateOrganizationId() {
		return createOrganizationId;
	}

	public void setCreateOrganizationId(long createOrganizationId) {
		this.createOrganizationId = createOrganizationId;
	}
}
