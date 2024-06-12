package com.ssm.ezbiz.mydata;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.ezbiz.model.RobFormA;

public class MyDataRequest {
	public String email;
	public String clientid = "2";
	public String username ;
	public String entityNo ;
	public String checkDigit;
	public String entityName;
	public String id;
	public static final String CTC_CERT = "3";
	
	public MyDataRequest(RobFormA robFormA,String productId){
		email = UserEnvironmentHelper.getUserenvironment().getEmail();
		username = UserEnvironmentHelper.getLoginName();
//		entityNo = StringUtils.split(robFormA.getBrNo(),"-")[0];
//		checkDigit = StringUtils.split(robFormA.getBrNo(),"-")[1];
//		entityName = robFormA.getBizName();
		entityNo = "IP0300375";
		checkDigit = "H";
		entityName = "GEMILANG PASIR TRADING";
		id = productId;//for ctc cert
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEntityNo() {
		return entityNo;
	}
	public void setEntityNo(String entityNo) {
		this.entityNo = entityNo;
	}
	public String getCheckDigit() {
		return checkDigit;
	}
	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
