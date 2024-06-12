package com.ssm.llp.jaxws;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.webis.param.UamUserInfo;

public class SSMValidatorHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {

		SOAPMessage soapMsg = context.getMessage();
		try {
			LlpParametersService llpParametersService = (LlpParametersService) WicketApplication.getServiceObject(LlpParametersService.class
					.getSimpleName());
			Map<String, String> listValidIp = llpParametersService.findAllCodeTypeAsMap(Parameter.MYKAD_CONFIG_IP);
			String isCheckIp = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_FILTER_MYKAD_IP);
			
			MessageContext mctx = context;
			HttpServletRequest req = (HttpServletRequest) mctx.get(MessageContext.SERVLET_REQUEST);
			String ipAddress = req.getRemoteAddr();
			
			if(!Parameter.YES_NO_no.equals(isCheckIp)){
				if(!listValidIp.containsKey(ipAddress) && !"127.0.0.1".equals(ipAddress)){
					throw new SSMException("Invalid Address Register:"+ipAddress);
				}
			}
			
			

			// get detail from request headers
			Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
			List userList = (List) http_headers.get("Username");
			List passList = (List) http_headers.get("Password");
			List agentIdList = (List) http_headers.get("agentId");

			String username = "";
			String password = "";
			String agentId = "";

			if (userList != null) {
				username = userList.get(0).toString();
			}

			if (passList != null) {
				password = passList.get(0).toString();
			}

			if (agentIdList != null) {
				agentId = agentIdList.get(0).toString();
			}

			String mykadPass = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_MYKAD_PWD);
			if (!("mykad".equals(username)) || !(mykadPass.equals(password))) {
				throw new Exception("Invalid Username & Password");
			}

			if (StringUtils.isBlank(agentId)) {
				throw new Exception(SSMExceptionParam.USER_PROFILE_ID_NO_EXIST + ":" + agentId);
			}
			
			WSUamClientService wsUamClientService = (WSUamClientService) WicketApplication.getBean(WSUamClientService.class.getSimpleName());
			
			UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(agentId);
//			UamUserProfile uamUserProfile = uamUserProfileService.findById(agentId);
//			if (uamUserProfile == null) {
//				throw new Exception(SSMExceptionParam.PROFILE_NOT_FOUND + ":" + agentId);
//			}
//
//			List<UamFunction> listFunction = uamUserProfileService.findAllEzBizMenu(agentId);
//			if (listFunction.size() == 0) {
//				throw new SSMException(SSMExceptionParam.NO_ACCESS_ROLE + ":" + agentId);
//			}

			initUserEnv("SSM:" + agentId, ipAddress, uamUserInfo);

		} catch (Exception e) {
			generateSOAPErrMessage(soapMsg, e.getMessage());
		}

		return true;
	}

	public void initUserEnv(String userid, String ipAddress, UamUserInfo uamUserInfo) {
		System.out.println(userid+":"+ipAddress);
//		String defaultBranch=null;
//		try {
//			UamUserProfileService uamUserProfileService = (UamUserProfileService) WicketApplication.getServiceObject(UamUserProfileService.class
//					.getSimpleName());
//			defaultBranch =uamUserProfileService.getDefaultBranch(userid);
//		} catch (Exception e) {
//		}
		InternalUserEnviroment enviroment = new InternalUserEnviroment(userid, ipAddress, "", "SSMMYKAD", uamUserInfo.getDefaultBranch());
		UserEnvironmentHelper.setUserEnvironment(enviroment);
	}

	private String getHTTPHeader(Map<String, List<String>> headers, String header) {
		String str = "";
		List<String> contentType = null;
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String name = entry.getKey();
			if (name.equalsIgnoreCase(header)) {
				contentType = entry.getValue();
			}
		}

		if (contentType != null) {
			StringBuffer strBuf = new StringBuffer();
			for (String type : contentType) {
				strBuf.append(type);
			}
			return strBuf.toString();
		}

		return null;
	}

	private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
		System.err.println(reason);
		try {
			SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
			SOAPFault soapFault = soapBody.addFault();
			soapFault.setFaultString(reason);
			throw new SOAPFaultException(soapFault);
		} catch (SOAPException e) {

		}
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(MessageContext context) {
		UserEnvironmentHelper.setUserEnvironment(null);
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
