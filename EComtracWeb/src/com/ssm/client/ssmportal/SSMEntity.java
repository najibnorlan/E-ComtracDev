package com.ssm.client.ssmportal;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class SSMEntity implements Serializable{
	private String entityType;
	private String entityNo;
	private String entityCheckDigit;
	private String entityName;
	private String entityStatus;
	private String[] entityGsts = new String[0];
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
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityStatus() {
		return entityStatus;
	}
	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}
	public String[] getEntityGsts() {
		return entityGsts;
	}
	public void setEntityGsts(String[] entityGsts) {
		this.entityGsts = entityGsts;
	}
	public String getFullEntityNo(){
		if(StringUtils.isNotBlank(getEntityCheckDigit())){
			return getEntityNo()+"-"+getEntityCheckDigit();
		}else{
			return getEntityNo();
		}
	}
	public void print(){
		System.out.println("Entity No: "+getFullEntityNo());
		System.out.println("Entity Name: "+getEntityName());
		System.out.println("Entity Status: "+getEntityStatus());
		System.out.println("Entity GST :");
		for (int i = 0; i < entityGsts.length; i++) {
			System.out.println("\t"+entityGsts[i]);
		}
		
	}
	
	public String getEntityCheckDigit() {
		return entityCheckDigit;
	}
	public void setEntityCheckDigit(String entityCheckDigit) {
		this.entityCheckDigit = entityCheckDigit;
	}
}
