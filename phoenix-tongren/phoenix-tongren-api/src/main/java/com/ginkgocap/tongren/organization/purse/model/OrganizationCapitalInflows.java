package com.ginkgocap.tongren.organization.purse.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织资金流入
 * @author yanweiqi
 *
 */
public class OrganizationCapitalInflows implements Serializable {


	private static final long serialVersionUID = 8241872497522894074L;

	private long id;
	private long payerId;
	private long payerOrganizationId;
	private double payAmount;
	private String payTitle;
	private Timestamp payTime;
	private String content;
	private int payWays;
	private String payAccountId;
	private Timestamp toAccountTime;
	private int status;
	private boolean isInvoice;
	private long organizationCapitalId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPayerId() {
		return payerId;
	}
	public void setPayerId(long payerId) {
		this.payerId = payerId;
	}
	public long getPayerOrganizationId() {
		return payerOrganizationId;
	}
	public void setPayerOrganizationId(long payerOrganizationId) {
		this.payerOrganizationId = payerOrganizationId;
	}
	public double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}
	public String getPayTitle() {
		return payTitle;
	}
	public void setPayTitle(String payTitle) {
		this.payTitle = payTitle;
	}
	public Timestamp getPayTime() {
		return payTime;
	}
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPayWays() {
		return payWays;
	}
	public void setPayWays(int payWays) {
		this.payWays = payWays;
	}
	public String getPayAccountId() {
		return payAccountId;
	}
	public void setPayAccountId(String payAccountId) {
		this.payAccountId = payAccountId;
	}
	public Timestamp getToAccountTime() {
		return toAccountTime;
	}
	public void setToAccountTime(Timestamp toAccountTime) {
		this.toAccountTime = toAccountTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isInvoice() {
		return isInvoice;
	}
	public void setInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}
	public long getOrganizationCapitalId() {
		return organizationCapitalId;
	}
	public void setOrganizationCapitalId(long organizationCapitalId) {
		this.organizationCapitalId = organizationCapitalId;
	}
	
	
}
