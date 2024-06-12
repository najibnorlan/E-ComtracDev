package com.ssm.controller.token;

public class MB_UnauthorizedException extends Exception {

	  private static final long serialVersionUID = 1L;

	  private String error;
	  private int statusCode;
	  
	  public MB_UnauthorizedException(String error, int statusCode) {
		this.error = error;
		this.statusCode = statusCode;
	  }	  	  
	  
	  public String getError() {
	    return error;
	  }
	  
	  public int getStatusCode() {
		return statusCode;
	  }
		  
	  public void seStatusCode(int statusCode) {
	    this.statusCode = statusCode;
	  }
} 
