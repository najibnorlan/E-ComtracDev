package com.ssm.controller.token;

import java.io.Serializable;
import java.util.Date;

public class TokenDataModel implements Serializable {
	private String iss;
	private Date iat;
	private Date exp;
	private Date expired;
	private String nric;
	private String vli;
	private String operation;
	private String refNo;
	private String brNo;
	
	
	
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public Date getIat() {
		return iat;
	}
	public void setIat(Date iat) {
		this.iat = iat;
	}
	public Date getExp() {
		return exp;
	}
	public void setExp(Date exp) {
		this.exp = exp;
	}
	public Date getExpired() {
		return expired;
	}
	public void setExpired(Date expired) {
		this.expired = expired;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getVli() {
		return vli;
	}
	public void setVli(String vli) {
		this.vli = vli;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getBrNo() {
		return brNo;
	}
	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}
}
