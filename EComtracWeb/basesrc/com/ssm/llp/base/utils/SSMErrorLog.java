package com.ssm.llp.base.utils;

import java.io.Serializable;
import java.util.Date;

import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

public class SSMErrorLog implements Serializable{
	public String errorId;
	public String errorMsg;
	public String userId;
	public Date logTime;
	
	public SSMErrorLog() {
		if(UserEnvironmentHelper.getUserenvironment() != null ){
			userId = UserEnvironmentHelper.getLoginName();
		}
		logTime  = new Date();
		errorId  =System.currentTimeMillis()+"";
	}
	
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}	
	
	
}
