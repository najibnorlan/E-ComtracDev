package com.ssm.integrasi.ws.response;

import java.io.Serializable;

public class ResponseRestHeader implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerId;
	
	private String customerReferenceNo;
	
	private String  customerRequestDate;
	
	private String requestTimestamp;
	
	private String responseTimestamp;
	
	private Integer recordCount;
	
	private String errorCode;
	
	private String errorMessage;
	
	private String hostErrorCode;
	
	private String hostErrorMessage;
	
	private String operationName;
	
	
	
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

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public String getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(String responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHostErrorCode() {
		return hostErrorCode;
	}

	public void setHostErrorCode(String hostErrorCode) {
		this.hostErrorCode = hostErrorCode;
	}

	public String getHostErrorMessage() {
		return hostErrorMessage;
	}

	public void setHostErrorMessage(String hostErrorMessage) {
		this.hostErrorMessage = hostErrorMessage;
	}

}
