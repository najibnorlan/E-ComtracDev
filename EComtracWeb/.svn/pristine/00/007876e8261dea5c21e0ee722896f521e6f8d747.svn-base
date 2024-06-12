package com.ssm.llp.base.sec;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpLocaleMessageService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

public class SecUtils {
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void main(String[] args) throws Exception{
		System.out.println(loginNTLM("azira", "password"));
	}
	
	public static boolean loginNTLM(final String userName, final String password)  {

		boolean userAuthenticated = false;
		String authenticationMessage = "";
		String domainController = "ssm.com.my";
		try {
			LlpParametersService llpParametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
			String adUrl = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_AD_URL);
			if(StringUtils.isNotBlank(adUrl)){
				domainController = adUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			UniAddress dc = UniAddress.getByName(domainController, true);
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("SSM", userName, password);
			SmbSession.logon(dc, auth);
			
			userAuthenticated = true;
		} catch (SmbAuthException sae) {
			sae.printStackTrace();
			authenticationMessage = "SmbAuthException : " + sae.getMessage();
		} catch (UnknownHostException uhe) {
			authenticationMessage = "UnknownHostException : " + uhe.getMessage();
		} catch (SmbException smbe) {
			smbe.printStackTrace();
			
			System.out.println(smbe.getNtStatus());
			
			authenticationMessage = "SmbException : " + smbe.getMessage();
		}
		
		if (userAuthenticated){
			System.out.println("Authenticated Success : " + authenticationMessage);
		}
		return userAuthenticated;
//		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String pass="rahsia";
}
