package com.ssm.llp.jaxws;

public class Wsdl2Java {
	public static void main(String[] args) {
		String filePath = "D:/workspaces/NewFramework/EzBizWeb/client/SSMPortalServices.wsdl";
		Wsdl2Java wsdl2Java = new Wsdl2Java();
		wsdl2Java.main(new String[]{filePath});
		
	}
}
