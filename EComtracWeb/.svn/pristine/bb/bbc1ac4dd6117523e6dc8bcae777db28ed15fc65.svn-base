package com.ssm.integrasi.ws.processor;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ssm.integrasi.ws.client.ClientRestService;
import com.ssm.integrasi.ws.request.GeneralRestRequest;
import com.ssm.integrasi.ws.request.GenerateQRCodeTextReq;
import com.ssm.integrasi.ws.response.GenerateQRCodeTextResp;
import com.ssm.integrasi.ws.response.ResponseRestHeader;
import com.ssm.llp.base.utils.TokenGenerator;

public class MwServiceRestProcessor {
	
	private String hostEndPoint;
	private String userName;
	private String password;

	public String getHostEndPoint() {
		return hostEndPoint;
	}

	public void setHostEndPoint(String hostEndPoint) {
		this.hostEndPoint = hostEndPoint;
	}
	
	private String sendRestRequest(GeneralRestRequest requestData, String operationName) {
		String returnData = new String();
		ClientRestService restCall = new ClientRestService();
		String jsonData= new String();
		ObjectMapper mapper = new ObjectMapper();
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("operationName");
		FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
		
		try {
        	jsonData = mapper.writer(filters).writeValueAsString(requestData);
//	        System.out.println("jsonData > "+jsonData);
        } catch (Exception e) {
			e.printStackTrace();
		}	
		
		returnData = restCall.callPost(getHostEndPoint(), jsonData, getMWToken());
//		System.out.println("getHostEndPoint() > "+getHostEndPoint());
//		System.out.println("operationName > "+operationName);
//		System.out.println("returnData > "+returnData);
		return returnData;
		
	}
	
	private ResponseRestHeader setHeaderOnError(GeneralRestRequest requestData,Exception error) {
		ResponseRestHeader responseRestHeader = new ResponseRestHeader();
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("");
		FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
		ObjectMapper mapper = new ObjectMapper();
		error.printStackTrace();
		try {
			responseRestHeader = mapper.readValue(mapper.writer(filters).writeValueAsString(requestData.getHeader()), ResponseRestHeader.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		responseRestHeader.setRecordCount(0);
		responseRestHeader.setErrorCode("11");
		if(error.toString().contains("ConnectException") || error.toString().contains("HTTP Status 404")) {
			responseRestHeader.setErrorMessage("Connection error");
		}else if(error.toString().contains("UnrecognizedPropertyException") || error.toString().contains("The request sent by the client was syntactically incorrect")){
			responseRestHeader.setErrorMessage("Invalid request parameter");
		}else {
			responseRestHeader.setErrorMessage(error.toString());
		}
		
		
		return responseRestHeader;
	}
	
	public String getMWToken() {
		TokenGenerator tokGen = new TokenGenerator();
		
		String mwUserName = getUserName(); //llpParametersService.findByCodeTypeValue(SSMParameter.MW_ACCESS, SSMParameter.MW_ACCESS_USER_NAME);
		String mwPwd = getPassword();//llpParametersService.findByCodeTypeValue(SSMParameter.MW_ACCESS, SSMParameter.MW_ACCESS_PWD);
		  
		String token = tokGen.generateTokenForAuth(mwUserName, mwPwd);
//		System.out.println(">>USERNAME : "+mwUserName+ " >>PWD : "+mwPwd+" >>TOKEN : "+token);
		  
		return token;
    }
	
	public GenerateQRCodeTextResp generateQRCodeText(GeneralRestRequest<GenerateQRCodeTextReq> requestData) {
		GenerateQRCodeTextResp generateQRCodeTextResp = new GenerateQRCodeTextResp();
		String operationName = "generateQRCodeText";
		String restResponse = sendRestRequest(requestData,operationName);
		ObjectMapper mapper = new ObjectMapper();

		try {	
			
            String jsonInString = restResponse;
            generateQRCodeTextResp = mapper.readValue(jsonInString, GenerateQRCodeTextResp.class);
            
            
        } catch (IOException e) {
        	
        	generateQRCodeTextResp.setHeader(setHeaderOnError(requestData,e));
        }

		return generateQRCodeTextResp;
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
