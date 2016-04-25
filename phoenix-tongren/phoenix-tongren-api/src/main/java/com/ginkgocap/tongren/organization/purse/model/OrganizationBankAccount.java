package com.ginkgocap.tongren.organization.purse.model;

import java.io.Serializable;

/**
 * 组织银行账号
 * @author yanweiqi
 *
 */
public class OrganizationBankAccount implements Serializable {

	private static final long serialVersionUID = -2806123121914616425L;

	private long id;
	private long capitalId;
	private String name;
	private String bankNumber;
	private int cardType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCapitalId() {
		return capitalId;
	}
	public void setCapitalId(long capitalId) {
		this.capitalId = capitalId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	
}
