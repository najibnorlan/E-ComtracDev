package com.ssm.integrasi.ws.request;

import java.io.Serializable;

public class GenerateQRCodeTextReq implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String version;
	String entityNo;
	String entityNoNew;
	String entityType;
	
	 public GenerateQRCodeTextReq() {
	    }
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEntityNoNew() {
		return entityNoNew;
	}

	public void setEntityNoNew(String entityNoNew) {
		this.entityNoNew = entityNoNew;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityNo() {
		return entityNo;
	}

	public void setEntityNo(String entityNo) {
		this.entityNo = entityNo;
	}
	
	
}
