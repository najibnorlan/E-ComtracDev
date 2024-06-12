package com.ssm.llp.jaxws;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.crypto.SealedObject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang.StringUtils;

import com.ssm.ezbiz.service.SSMMyKadModelService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.mykad.ws.MyKadProfileParam;
import com.ssm.mykad.ws.WsMyKadReq;
import com.ssm.webis.param.UamUserInfo;
import com.ssm.ws.WsDes;

@WebService(name = "WSMykadReaderService")
@HandlerChain(file = "handler-chain.xml")
public class WSMykadReaderServiceImpl implements WSMykadReaderService {
	@Resource
	WebServiceContext wsctx;

	@Override
	@WebMethod
	public String getHelloWorld() {
		return "Hello World JAX-WS on " + System.currentTimeMillis();// TODO
																		// Auto-generated
																		// method
																		// stub
	}

	@Override
	@WebMethod
	public String[] getUserStatusNMaskEmail(String icNo) throws Exception {
		try {
			LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceObject(LlpUserProfileService.class
					.getSimpleName());
			LlpUserProfile llpUserProfile = llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, icNo);
			if (llpUserProfile != null) {
				String maskEmail = "";
				String email = llpUserProfile.getEmail();
				Matcher m = Pattern.compile("\\w{3}(.*)@").matcher(email);
				if (m.find()) {
					String s = m.group(1);
					char[] c = new char[s.length()];
					Arrays.fill(c, '*');
					maskEmail = email.replace(s, String.valueOf(c));
				}
				return new String[] { llpUserProfile.getUserStatus(), maskEmail };
			}
			return null;
		} catch (Exception e) {
			throw e;
		}

	}
	

	@Override
	@WebMethod
	public boolean registerByMyKad(@WebParam WsMyKadReq myKadReq) throws Exception {

		try {

			WSUamClientService wsUamClientService = (WSUamClientService) WicketApplication.getBean(WSUamClientService.class.getSimpleName());
			
			LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceObject(LlpUserProfileService.class
					.getSimpleName());
			MailService mailService = (MailService) WicketApplication.getServiceObject(MailService.class.getSimpleName());
			LlpParametersService llpParametersService = (LlpParametersService) WicketApplication.getServiceObject(LlpParametersService.class
					.getSimpleName());

			Map myKadMapping = llpParametersService.findAllCodeTypeAsMap(Parameter.MY_KAD_STATE_MAPPING);
			
			
			String pass = myKadReq.getAdUser() + myKadReq.getReqRefNo();
			MyKadProfileParam myKadProfileParam = null;
			try {
				SealedObject sealedObject = (SealedObject) WsDes.byteToObject(myKadReq.getProfileObject());
				myKadProfileParam = (MyKadProfileParam) WsDes.decrypt(pass, sealedObject);
			} catch (Exception e) {
				throw new Exception("Invalid Profile data");
			}
			if (myKadProfileParam == null) {
				throw new Exception("Invalid Profile data null");
			}
			SSMMyKadModelService myKadModelService = (SSMMyKadModelService) WicketApplication.getServiceObject(SSMMyKadModelService.class
					.getSimpleName());
			SSMMyKadModel myKadModel = new SSMMyKadModel();
			myKadModel.copyFromMyKadWSParameter(myKadProfileParam);
			
			MessageContext mctx = wsctx.getMessageContext();
			HttpServletRequest req = (HttpServletRequest) mctx.get(MessageContext.SERVLET_REQUEST);
			String ipAddress = req.getRemoteAddr();
			myKadModel.setIpAddress(ipAddress);
			
			myKadModelService.insert(myKadModel);
			
			if(StringUtils.isBlank(myKadModel.getEzBizLoginId())){//Mean thumbprint only
				return true;
			}
			

			LlpUserProfile llpUserProfile = llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, myKadProfileParam.getIc());
			
			List<LlpParameters> listRace = llpParametersService.findByActiveCodeType(Parameter.RACE);
			if (llpUserProfile == null) {
				if (StringUtils.isNotBlank(myKadProfileParam.getEzBizEmail()) && llpUserProfileService.isEmailExist(myKadProfileParam.getEzBizEmail())) {
					throw new Exception("Email already been use");
				}

				if (llpUserProfileService.findProfileInfoByUserId(myKadProfileParam.getEzBizLoginId()) != null) {
					throw new Exception("Login Id already been use");
				}
				
				List<LlpParameters> listState = llpParametersService.findByActiveCodeType(Parameter.STATE_CODE);

				llpUserProfile = new LlpUserProfile();
				
				llpUserProfile.setLoginId(myKadProfileParam.getEzBizLoginId());
				llpUserProfile.setName(myKadProfileParam.getAvailableName());
				llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
				llpUserProfile.setUserType(Parameter.USER_TYPE_complianceOfficer);
				
				if(StringUtils.isNotBlank(myKadProfileParam.getEzBizPassword())){
					llpUserProfile.setUserPwd(myKadProfileParam.getEzBizPassword());
				}else{
					llpUserProfile.setUserPwd(generatePassword());
				}
				
				llpUserProfile.setReconfirmPassword(llpUserProfile.getUserPwd());
				llpUserProfile.setEmail(myKadProfileParam.getEzBizEmail());
				llpUserProfile.setReTypeEmail(llpUserProfile.getEmail());
				llpUserProfile.setIdType(Parameter.ID_TYPE_newic);
				llpUserProfile.setIdNo(myKadProfileParam.getIc());
				if(myKadProfileParam.getCitizenship().indexOf("WARGANEGARA")!=-1){
					llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_CITIZEN);
					llpUserProfile.setIcColour("B");
				}else if(myKadProfileParam.getCitizenship().toUpperCase().indexOf("PEMASTAUTIN TETAP")!=-1){
					llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_PERMANENT_RESIDENT);
					llpUserProfile.setIcColour("R");
				}else if(myKadProfileParam.getCitizenship().toUpperCase().indexOf("PEMAST. SEMENTARA")!=-1){
					llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_TEMP_RESIDENT);
					llpUserProfile.setIcColour("G");
					throw new Exception("Temporary Resident not allow to register");
				}else{
					llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_FOREIGNERS);
					llpUserProfile.setIcColour("N");
					throw new Exception("Foreigners not allow to register: "+myKadProfileParam.getCitizenship());
				}
				
				
				llpUserProfile.setGender(myKadProfileParam.getGender());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				llpUserProfile.setDob(sdf.parse(myKadProfileParam.getDob()));
				llpUserProfile.setHpNo(myKadProfileParam.getEzBizPhoneNo());
				llpUserProfile.setAdd1(myKadProfileParam.getEzBizAddress1());
				llpUserProfile.setAdd2(myKadProfileParam.getEzBizAddress2());
				llpUserProfile.setAdd3(myKadProfileParam.getEzBizAddress3());
				llpUserProfile.setCity(myKadProfileParam.getEzBizCity());
				llpUserProfile.setPostcode(myKadProfileParam.getEzBizPostcode());
				
				String myKadStateCode = myKadProfileParam.getEzBizState().toUpperCase().trim();
				if(myKadStateCode.indexOf("KL")!=-1 || myKadStateCode.indexOf("LUMPUR")!=-1){
					myKadStateCode = "KUALA LUMPUR";
				}
				if(myKadStateCode.indexOf("LABUAN")!=-1){
					myKadStateCode = "LABUAN";
				}
				if(myKadStateCode.indexOf("PUTRAJAYA")!=-1){
					myKadStateCode = "PUTRAJAYA";
				}
				
				for (int i = 0; i < listState.size(); i++) {
					String stateDesc = listState.get(i).getCodeDesc().toUpperCase().trim();
					
					if (stateDesc.indexOf(myKadStateCode) != -1) {
						llpUserProfile.setState(listState.get(i).getCode());
						break;
					}
				}

				String raceCode = Parameter.RACE_OTHERS;
				boolean canMapRace = false;
				for (int i = 0; i < listRace.size(); i++) {
					if (listRace.get(i).getCodeDesc().toLowerCase().indexOf(myKadProfileParam.getRace().trim().toLowerCase()) != -1) {
						raceCode = listRace.get(i).getCode();
						canMapRace = true;
						break;
					}
				}
				
				if(canMapRace){
					llpUserProfile.setRace(raceCode);
				}else{
					llpUserProfile.setRace(Parameter.RACE_OTHERS);
					llpUserProfile.setOthersRace(myKadProfileParam.getRace());
				}

				// Insert new user
				llpUserProfileService.insertNewLlpUserProfile(llpUserProfile);
				try {
					mailService.sendMail(llpUserProfile.getEmail(), "email.notification.user.register.subject", llpUserProfile.getUserRefNo(),
							"email.notification.user.register.body", llpUserProfile.getName(), llpUserProfile.getUserRefNo(),
							llpUserProfile.getLoginId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// Update for Approval
				if (myKadProfileParam.isThumbPrintSuccess()) {
					llpUserProfile.setUserStatus(Parameter.USER_STATUS_active);
					llpUserProfile.setApproveBy("SSM:" + myKadProfileParam.getAdUser());
					
					try {
						UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(myKadProfileParam.getAdUser());
						llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
					} catch (Exception e) {
					}
					
					llpUserProfile.setApproveDt(new Date());
					llpUserProfile.setApproveChannel(Parameter.USER_APPROVED_CHANNEL_SSMMYKAD);
					llpUserProfileService.update(llpUserProfile);
					try {
						mailService.sendMail(llpUserProfile.getEmail(), "email.notification.user.success.active.subject",
								llpUserProfile.getUserRefNo(), "email.notification.user.success.active.body", llpUserProfile.getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				if (llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_active)) {
					throw new Exception("No need to register.User already register and active using id " + llpUserProfile.getLoginId());
				}
				if (!llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_pending)) {
					throw new Exception("User status is not Pending Verification. Current Status: " + llpUserProfile.getUserStatus());
				}
				if (!myKadProfileParam.isThumbPrintSuccess()) {
					throw new Exception("Please do thumbprint verification or go verify at SSM Counter!!");
				}
				
				//Comment auto overide
//				if (!llpUserProfile.getName().equals(myKadProfileParam.getAvailableName())) {
//					throw new Exception("Name not match.User already register and using different name:" + llpUserProfile.getName());
//				}
				
				llpUserProfile.setName(myKadProfileParam.getAvailableName());//overide from mykad
				llpUserProfile.setRace(myKadProfileParam.getRace());//overide from mykad
				llpUserProfile.setGender(myKadProfileParam.getGender()); //overide from mykad
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				llpUserProfile.setDob(sdf.parse(myKadProfileParam.getDob())); //overide from mykad
				String raceCode = Parameter.RACE_OTHERS;
				for (int i = 0; i < listRace.size(); i++) {
					if (listRace.get(i).getCodeDesc().toLowerCase().indexOf(myKadProfileParam.getRace().trim().toLowerCase()) != -1) {
						raceCode = listRace.get(i).getCode();
						break;
					}
				}
				llpUserProfile.setRace(raceCode);//overide from mykad
				
				
				
				llpUserProfile.setUserStatus(Parameter.USER_STATUS_active);
				llpUserProfile.setApproveBy("SSM:" +myKadReq.getAdUser());
				try {
					
					UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(myKadProfileParam.getAdUser());
					llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
					
				} catch (Exception e) {
				}
				llpUserProfile.setApproveDt(new Date());
				llpUserProfile.setApproveChannel(Parameter.USER_APPROVED_CHANNEL_SSMMYKAD);

				llpUserProfileService.update(llpUserProfile);
				String email = llpUserProfile.getEmail();
				mailService.sendMail(email, "email.notification.user.success.active.subject", llpUserProfile.getUserRefNo(),
						"email.notification.user.success.active.body", llpUserProfile.getName());
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String generatePassword() {
		// TODO Auto-generated method stub
		char[] values1 = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z' };
		char[] values3 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		String out1 = "";
		String out2 = "";

		String password;
		Random rand = new Random();

		for (int i = 0; i < 5; i++) {
			int idx = rand.nextInt(values1.length);
			out1 += values1[idx];
		}

		for (int i = 0; i < 3; i++) {
			int idx = rand.nextInt(values3.length);
			out2 += values3[idx];
		}

		password = out1.concat(out2);
		return password;

	}

}
