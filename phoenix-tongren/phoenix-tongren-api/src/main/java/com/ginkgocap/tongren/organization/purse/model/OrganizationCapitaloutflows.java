package com.ginkgocap.tongren.organization.purse.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织资金流出
 * @author yanweiqi
 *
 */
public class OrganizationCapitaloutflows implements Serializable {

	private static final long serialVersionUID = -4220416511940331719L;
	private long id;
	private long drawerId;
	private long drawerOrganizationId;
	private double withdrayAmount;
	private String withdrawTitle;
	private Timestamp withdrawTime;
	private String withdrawContent;
	private int withdrawWays;
	private int withdrawStatus;
	private long organizationCapitalId;
	private String receiveAccount;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDrawerId() {
		return drawerId;
	}
	public void setDrawerId(long drawerId) {
		this.drawerId = drawerId;
	}
	public long getDrawerOrganizationId() {
		return drawerOrganizationId;
	}
	public void setDrawerOrganizationId(long drawerOrganizationId) {
		this.drawerOrganizationId = drawerOrganizationId;
	}
	public double getWithdrayAmount() {
		return withdrayAmount;
	}
	public void setWithdrayAmount(double withdrayAmount) {
		this.withdrayAmount = withdrayAmount;
	}
	public String getWithdrawTitle() {
		return withdrawTitle;
	}
	public void setWithdrawTitle(String withdrawTitle) {
		this.withdrawTitle = withdrawTitle;
	}
	public Timestamp getWithdrawTime() {
		return withdrawTime;
	}
	public void setWithdrawTime(Timestamp withdrawTime) {
		this.withdrawTime = withdrawTime;
	}
	public String getWithdrawContent() {
		return withdrawContent;
	}
	public void setWithdrawContent(String withdrawContent) {
		this.withdrawContent = withdrawContent;
	}
	public int getWithdrawWays() {
		return withdrawWays;
	}
	public void setWithdrawWays(int withdrawWays) {
		this.withdrawWays = withdrawWays;
	}
	public int getWithdrawStatus() {
		return withdrawStatus;
	}
	public void setWithdrawStatus(int withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}
	public long getOrganizationCapitalId() {
		return organizationCapitalId;
	}
	public void setOrganizationCapitalId(long organizationCapitalId) {
		this.organizationCapitalId = organizationCapitalId;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
}
