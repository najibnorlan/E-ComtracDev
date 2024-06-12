/**
 * DataServiceFaultException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v9  Built on : Jun 17, 2013 (12:52:35 PDT)
 */

package com.ssm.client.ssmportal;

public class DataServiceFaultException extends java.lang.Exception {

	private static final long serialVersionUID = 1446045200067L;

	private SSMPortalServicesStub.DataServiceFault faultMessage;

	public DataServiceFaultException() {
		super("DataServiceFaultException");
	}

	public DataServiceFaultException(java.lang.String s) {
		super(s);
	}

	public DataServiceFaultException(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	public DataServiceFaultException(java.lang.Throwable cause) {
		super(cause);
	}

	public void setFaultMessage(SSMPortalServicesStub.DataServiceFault msg) {
		faultMessage = msg;
	}

	public SSMPortalServicesStub.DataServiceFault getFaultMessage() {
		return faultMessage;
	}
}
