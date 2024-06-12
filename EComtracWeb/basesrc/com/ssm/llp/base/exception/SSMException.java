package com.ssm.llp.base.exception;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.base.page.WicketApplication;

public class SSMException extends Exception{
	private String expKey;
	private String param[];
	
	
	public SSMException(Exception ex) {
		super(ex);
		this.expKey=ex.getMessage();
	}
	public SSMException(String expKey) {
		super(expKey);
		this.expKey = expKey;
	}
	public SSMException(String expKey,String... param){
		super(expKey);
		this.expKey = expKey;
		this.param = param;
	}

	public String getExpKey() {
		return expKey;
	}

	public void setExpKey(String expKey) {
		this.expKey = expKey;
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}
	
	@Override
	public String getMessage() {
		if(expKey.indexOf(".")==-1) {
			String paramStr ="";
			if(param!=null && param.length > 0) {
				paramStr = StringUtils.join(param," ");
			}
			return expKey+" "+paramStr;
		}
		return WicketApplication.resolve(expKey,param);
	}
}
