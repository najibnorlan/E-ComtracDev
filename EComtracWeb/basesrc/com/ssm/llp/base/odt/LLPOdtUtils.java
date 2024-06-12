package com.ssm.llp.base.odt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssm.common.oo.OOConvUtils;
import com.ssm.common.oo.ws.client.OOConverterClient;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobBusiness;
import com.ssm.llp.mod1.model.LlpRegistration;

public class LLPOdtUtils {
	private static OOConvUtils convUtils = new OOConvUtils();
	static{
//		OOConverterClient.END_POINT="http://cbsprint.ssm.com.my/SSMPrintSvr/services/OOService";
		OOConverterClient.END_POINT="http://coreprint.ssm4u.com.my/services/OOService";
	}
	
	public static void main(String[] args) throws Exception{
		Map mapData = new HashMap();
		List bsnRenewalList = new ArrayList();
		LlpRegistration llpRegistration = new LlpRegistration();
		llpRegistration.setLlpName("LLP TERBARU");
		llpRegistration.setBussinessStateCode("MY");
		
		RobBusiness business = new RobBusiness();
		business.setVchname("TEST 123");
		business.setVchregistrationnumber("001234x2");
		business.setDtcreatedate(new Date());
	
		
		bsnRenewalList.add(business);
		bsnRenewalList.add(business);
		bsnRenewalList.add(business);
		bsnRenewalList.add(business);
		
		mapData.put("bsnRenewalList", bsnRenewalList);
		mapData.put("llpRegistration", llpRegistration);
		
		File templateFile = new File("resources/test1odt.odt");
		byte bytePdfContent[] = generatePdf(templateFile, mapData);
		FileOutputStream fos = new FileOutputStream(new File("c:/test1.pdf"));
		fos.write(bytePdfContent);
		fos.close();
		System.out.println("Done");
	}
	
	public static byte[] generatePdf(File templateFile, Map mapData){
		try {
			
			FileInputStream fis = new FileInputStream(templateFile);
			OOConvUtils.GENERATE_LOCAL = "Y";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mapData.put("CodeResolver",new CodeResolver());
			mapData.put("NumberConverter", new NumberConverter());
			
			convUtils.resolveAndConvert(fis, baos, mapData, "pdf");
			fis.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] generatePdf(byte[] templateFile, Map mapData){
		try {
			
			ByteArrayInputStream bais = new ByteArrayInputStream(templateFile);
			OOConvUtils.GENERATE_LOCAL = "Y";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mapData.put("CodeResolver",new CodeResolver());
			mapData.put("NumberConverter", new NumberConverter());
			
			convUtils.resolveAndConvert(bais, baos, mapData, "pdf");
			bais.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] generatePdf(String templateCode, Map mapData){
		try {
			LlpFileUploadService llpFileUploadService = (LlpFileUploadService) WicketApplication.getServiceNew(LlpFileUploadService.class.getSimpleName());
			LlpFileUpload llpFileUpload = llpFileUploadService.findByFileCode(templateCode);
			mapData.put("CodeResolver",new CodeResolver());
			mapData.put("NumberConverter", new NumberConverter());
			OOConvUtils.GENERATE_LOCAL = "Y";
			InputStream fis = new ByteArrayInputStream(llpFileUpload.getFileData());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			convUtils.resolveAndConvert(fis, baos, mapData, "pdf");
			
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
