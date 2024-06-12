package com.ssm.llp.jaxws;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(name = "WSQRValidationService")
@HandlerChain(file = "handler-chain.xml")
public class WSQRValidationServiceImpl implements WSQRValidationService {
	@Resource
	WebServiceContext wsctx;


	@WebMethod
	@Override
	public String resolveQRCode(String qrText) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(qrText);
		return "yeah:"+qrText;
	}


	@WebMethod
	@Override
	public void HelloWorld() throws Exception {
		System.out.println("Hello!!");
	}


}
