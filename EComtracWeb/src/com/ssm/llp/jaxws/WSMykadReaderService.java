package com.ssm.llp.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.ssm.mykad.ws.WsMyKadReq;

@WebService
//@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) 
public interface WSMykadReaderService {
	String getHelloWorld();

	boolean registerByMyKad(WsMyKadReq req) throws Exception;

	String[] getUserStatusNMaskEmail(String icNo) throws Exception;
}