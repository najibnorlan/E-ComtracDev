package com.ssm.mykad.ws;

import java.net.URL;

import javax.xml.namespace.QName;

import com.ssm.llp.jaxws.WSMykadReaderService;

public class MyKadClient {
	 public static void main(String[] args) throws Exception{
		 final URL url = new URL("http://localhost:8080/EzBizWeb/ws/mykad.ws?wsdl");
		      // Specify the qualified name
		      final QName name = new QName("http://jaxws.llp.ssm.com/", "WSMykadReaderServiceImplService");
		      // Create the service
		      final javax.xml.ws.Service service = javax.xml.ws.Service.create(url, name);
		      // Get the port
		      final WSMykadReaderService simple = service.getPort(WSMykadReaderService.class);
		      // Call an operation
		      simple.registerByMyKad(new WsMyKadReq());
	    }
}
