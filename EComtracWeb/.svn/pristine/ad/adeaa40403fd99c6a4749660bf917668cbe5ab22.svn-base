package com.ssm.llp.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
//@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) 
public interface WSQRValidationService {
	
	@WebMethod
	String resolveQRCode(String qrText)throws Exception;
	
	void HelloWorld()throws Exception;
}