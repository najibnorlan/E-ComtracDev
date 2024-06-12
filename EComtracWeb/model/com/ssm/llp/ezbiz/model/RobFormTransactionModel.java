package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class RobFormTransactionModel implements Serializable,Comparable<RobFormTransactionModel> {

	private String appRefNo;
	private String brNo;
	private String bizName;
	private String status;
	private Date updateDt;
	private Date createDt;
	private String formType;
	private Object formObject;

	private String brNoNBizName;

	public RobFormTransactionModel(String appRefNo, String brNo, String bizName, String statusDesc, Date createDt, Date updateDt, String formType, Object formObject) {
		this.appRefNo = appRefNo;
		this.brNo = brNo;
		this.bizName = bizName;
		this.status = statusDesc;
		this.updateDt = updateDt;
		this.createDt = createDt;
		this.formType = formType;
		this.formObject = formObject;
	}

	public String getAppRefNo() {
		return appRefNo;
	}

	public void setAppRefNo(String appRefNo) {
		this.appRefNo = appRefNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getBrNo() {
		return brNo;
	}

	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBrNoNBizName() {
		if (StringUtils.isNotBlank(brNo)) {
			return getBrNo() + "\n" + getBizName();
		}
		return getBizName();
	}

	public Object getFormObject() {
		return formObject;
	}

	public void setFormObject(Object formObject) {
		this.formObject = formObject;
	}

	@Override
	public int compareTo(RobFormTransactionModel model) {
		if(this.getUpdateDt().after(model.getUpdateDt()) ) {
			return -1;
		}
		return 0;
	}
	
	
}