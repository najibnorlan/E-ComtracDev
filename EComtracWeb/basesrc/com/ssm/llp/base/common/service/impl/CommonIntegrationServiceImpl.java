package com.ssm.llp.base.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.integrasi.ws.processor.MwServiceRestProcessor;
import com.ssm.integrasi.ws.request.GeneralRestRequest;
import com.ssm.integrasi.ws.request.GenerateQRCodeTextReq;
import com.ssm.integrasi.ws.request.RequestRestHeader;
import com.ssm.integrasi.ws.response.GenerateQRCodeTextResp;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.CommonIntegrationService;
import com.ssm.llp.base.common.service.LlpParametersService;

@Service
public class CommonIntegrationServiceImpl implements CommonIntegrationService {
	private HttpClient client;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
	@Autowired
	LlpParametersService llpParametersService;
	

	@Override
	public String generateQRCodeText(GenerateQRCodeTextReq req) {
		MwServiceRestProcessor restProcessor = new MwServiceRestProcessor();

		String urlMethod = llpParametersService.findByCodeTypeValue(Parameter.MW_URL, Parameter.MW_URL_METHOD_QR_GENERATE);
		String username = llpParametersService.findByCodeTypeValue(Parameter.MW_ACCESS, Parameter.MW_ACCESS_USER_NAME);
		String password = llpParametersService.findByCodeTypeValue(Parameter.MW_ACCESS, Parameter.MW_ACCESS_PWD);
		
		restProcessor.setHostEndPoint(urlMethod);
		restProcessor.setUserName(username);
		restProcessor.setPassword(password);
		
//		req = new GenerateQRCodeTextReq();
//		req.setEntityNo("002935518-T");
//		req.setEntityNoNew("201903042113");
//		req.setEntityType("ROB");
//		req.setVersion("2");
		
		RequestRestHeader reqHeader = new RequestRestHeader();
		reqHeader.setCustomerId(username);
		reqHeader.setCustomerReferenceNo(req.getEntityNo());

    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String dtFormat = sdf.format(new Date());
		reqHeader.setCustomerRequestDate(dtFormat);
		
		GeneralRestRequest<GenerateQRCodeTextReq> restReq = new GeneralRestRequest<>();
		restReq.setHeader(reqHeader);
		restReq.setRequest(req);
		try {
			GenerateQRCodeTextResp res = restProcessor.generateQRCodeText(restReq);
			
			if("00".equals(res.getResponse().getSuccessCode()))  {
				return res.getResponse().getText();
			}else {
				System.err.println("QR ErrorMsg >> "+res.getResponse().getErrorMsg());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	public static void main(String[] args) {
//		RequestRestHeader reqHeader = new RequestRestHeader();
//		reqHeader.setCustomerId(mwUserName);
//		reqHeader.setCustomerReferenceNo("TEST");
//
//    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//    	String dtFormat = sdf.format(new Date());
//		reqHeader.setCustomerRequestDate(dtFormat);
		
//		MwServiceRestProcessor restProcessor = new MwServiceRestProcessor();
//		restProcessor.setHostEndPoint("http://integrasidev.ssm.com.my/qrServiceRest/generateQRCodeText/1");
//		restProcessor.setUserName("EZBIZ");
//		restProcessor.setPassword("5617722");
//		
//		GenerateQRCodeTextReq req = new GenerateQRCodeTextReq();
//		req.setEntityNo("002935518-T");
//		req.setEntityNoNew("201903042113");
//		req.setEntityType("ROB");
//		req.setVersion("2");
//		
//		GeneralRestRequest<GenerateQRCodeTextReq> restReq = new GeneralRestRequest<>();
//		restReq.setHeader(getRequestHeader());
//		restReq.setRequest(req);
//		
//		GenerateQRCodeTextResp res = restProcessor.generateQRCodeText(restReq);
//		
//		System.out.println("Text >> "+res.getResponse().getText());
	}
	
	
	

}
