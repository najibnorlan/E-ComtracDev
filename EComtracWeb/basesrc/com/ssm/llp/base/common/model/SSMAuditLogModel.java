package com.ssm.llp.base.common.model;

import java.io.Serializable;
import java.util.List;

public class SSMAuditLogModel implements Serializable{
	private List<String> listColumnName;
	private List<List<Object>> listObjectValue;
	
	public SSMAuditLogModel(List<String> listColumnName , List<List<Object>> listObjectValue) {
		this.listColumnName = listColumnName ;
		this.listObjectValue=listObjectValue;
	}
	public List<String> getListColumnName() {
		return listColumnName;
	}
	public void setListColumnName(List<String> listColumnName) {
		this.listColumnName = listColumnName;
	}
	public List<List<Object>> getListObjectValue() {
		return listObjectValue;
	}
	public void setListObjectValue(List<List<Object>> listObjectValue) {
		this.listObjectValue = listObjectValue;
	}
	
}
