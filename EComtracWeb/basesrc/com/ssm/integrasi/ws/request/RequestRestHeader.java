package com.ssm.integrasi.ws.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("myFilter")
public class RequestRestHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String operationName;
	
	private String customerId;
	
	private String customerReferenceNo;
	
	private String customerRequestDate;
	
	

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerReferenceNo() {
		return customerReferenceNo;
	}

	public void setCustomerReferenceNo(String customerReferenceNo) {
		this.customerReferenceNo = customerReferenceNo;
	}

	public String getCustomerRequestDate() {
		return customerRequestDate;
	}

	public void setCustomerRequestDate(String customerRequestDate) {
		this.customerRequestDate = customerRequestDate;
	}

}
