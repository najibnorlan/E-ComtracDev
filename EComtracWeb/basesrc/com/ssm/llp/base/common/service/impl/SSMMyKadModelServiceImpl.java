package com.ssm.llp.base.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.SSMMyKadModelDao;
import com.ssm.ezbiz.service.SSMMyKadModelService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.param.UamUserInfo;

@Service
public class SSMMyKadModelServiceImpl extends BaseServiceImpl<SSMMyKadModel,  Long> implements SSMMyKadModelService{
	@Autowired 
	SSMMyKadModelDao ssmMyKadModelDao;
	
	@Autowired 
	LlpUserProfileService llpUserProfileService;
	
	@Autowired 
	LlpParametersService llpParametersService;
	
	@Autowired 
	MailService mailService;

	@Autowired
	@Qualifier("WSUamClientService")
	private WSUamClientService wSUamClientService;
	
	@Override
	public BaseDao getDefaultDao() {
		return ssmMyKadModelDao;
	}

	@Override
	public LlpUserProfile registerOrActivateUser(SSMMyKadModel mykadModel) throws Exception {
		
		insert(mykadModel);
		
		LlpUserProfile llpUserProfile = llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, mykadModel.getMykadNo());
		
		List<LlpParameters> listRace = llpParametersService.findByActiveCodeType(Parameter.RACE);

		String adUser = StringUtils.remove(mykadModel.getAdUserRequester() ,"SSM:" );
		
		if (llpUserProfile == null) {
			if (StringUtils.isNotBlank(mykadModel.getEzBizEmail()) && llpUserProfileService.isEmailExist(mykadModel.getEzBizEmail())) {
				throw new Exception("Email already been use");
			}

			if (llpUserProfileService.findProfileInfoByUserId(mykadModel.getEzBizLoginId()) != null) {
				throw new Exception("Login Id already been use");
			}
			
			
			
			llpUserProfile = new LlpUserProfile();
			
			llpUserProfile.setLoginId(mykadModel.getEzBizLoginId());
			llpUserProfile.setName(mykadModel.getAvailableName());
			llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
			llpUserProfile.setUserType(Parameter.USER_TYPE_complianceOfficer);
			
			llpUserProfile.setUserPwd(mykadModel.getEzBizPassword());
			
			llpUserProfile.setReconfirmPassword(llpUserProfile.getUserPwd());
			llpUserProfile.setEmail(mykadModel.getEzBizEmail());
			llpUserProfile.setReTypeEmail(llpUserProfile.getEmail());
			llpUserProfile.setIdType(Parameter.ID_TYPE_newic);
			llpUserProfile.setIdNo(mykadModel.getMykadNo());
			if(mykadModel.getCitizenship().indexOf("WARGANEGARA")!=-1){
				llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_CITIZEN);
				llpUserProfile.setIcColour("B");
			}else if(mykadModel.getCitizenship().toUpperCase().indexOf("PEMASTAUTIN TETAP")!=-1){
				llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_PERMANENT_RESIDENT);
				llpUserProfile.setIcColour("R");
			}else if(mykadModel.getCitizenship().toUpperCase().indexOf("PEMAST. SEMENTARA")!=-1){
				llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_TEMP_RESIDENT);
				llpUserProfile.setIcColour("G");
				throw new Exception("Temporary Resident not allow to register");
			}else{
				llpUserProfile.setNationality(Parameter.NATIONALITY_TYPE_FOREIGNERS);
				llpUserProfile.setIcColour("N");
				throw new Exception("Foreigners not allow to register: "+mykadModel.getCitizenship());
			}
			
			
			llpUserProfile.setGender(mykadModel.getGender());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			llpUserProfile.setDob(mykadModel.getDob());
			llpUserProfile.setHpNo(mykadModel.getEzBizPhoneNo());
			llpUserProfile.setAdd1(mykadModel.getEzBizAddress1());
			llpUserProfile.setAdd2(mykadModel.getEzBizAddress2());
			llpUserProfile.setAdd3(mykadModel.getEzBizAddress3());
			llpUserProfile.setCity(mykadModel.getEzBizCity());
			llpUserProfile.setPostcode(mykadModel.getEzBizPostcode());
			
			List<LlpParameters> list = llpParametersService.findByActiveCodeType(Parameter.STATE_CODE_TO_CBS_CODE);
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getCodeDesc().equals(mykadModel.getEzBizState())) {
					llpUserProfile.setState(list.get(i).getCode());
					break;
				}
			}
			
//			List<LlpParameters> listState = llpParametersService.findByActiveCodeType(Parameter.STATE_CODE);
//			String myKadStateCode = mykadModel.getEzBizState().toUpperCase().trim();
//			if(myKadStateCode.indexOf("KL")!=-1 || myKadStateCode.indexOf("LUMPUR")!=-1){
//				myKadStateCode = "KUALA LUMPUR";
//			}
//			if(myKadStateCode.indexOf("LABUAN")!=-1){
//				myKadStateCode = "LABUAN";
//			}
//			if(myKadStateCode.indexOf("PUTRAJAYA")!=-1){
//				myKadStateCode = "PUTRAJAYA";
//			}
//			
//			for (int i = 0; i < listState.size(); i++) {
//				String stateDesc = listState.get(i).getCodeDesc().toUpperCase().trim();
//				
//				if (stateDesc.indexOf(myKadStateCode) != -1) {
//					llpUserProfile.setState(listState.get(i).getCode());
//					break;
//				}
//			}
			

			String raceCode = Parameter.RACE_OTHERS;
			boolean canMapRace = false;
			for (int i = 0; i < listRace.size(); i++) {
				if (listRace.get(i).getCodeDesc().toLowerCase().indexOf(mykadModel.getRace().trim().toLowerCase()) != -1) {
					raceCode = listRace.get(i).getCode();
					canMapRace = true;
					break;
				}
			}
			
			if(canMapRace){
				llpUserProfile.setRace(raceCode);
			}else{
				llpUserProfile.setRace(Parameter.RACE_OTHERS);
				llpUserProfile.setOthersRace(mykadModel.getRace());
			}
			

			
			if (Parameter.YES_NO_yes.equals(mykadModel.getThumbPrintSuccess()) ) {
				// Insert new user
				llpUserProfileService.insertNewLlpUserProfile(llpUserProfile);
				
				llpUserProfile.setUserStatus(Parameter.USER_STATUS_active);
				llpUserProfile.setApproveBy(mykadModel.getAdUserRequester());
				
				try {
					UamUserInfo uamUserInfo = wSUamClientService.findCBSProfileWithEzbizRole(adUser);
					llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
				} catch (Exception e) {
				}
				
				llpUserProfile.setApproveDt(new Date());
				llpUserProfile.setApproveChannel(Parameter.USER_APPROVED_CHANNEL_SSMMYKADWEB);
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
			if (llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_deactive) || llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_suspend) || llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_deceased)) {
				throw new Exception("Cannot Activate User "+ llpUserProfile.getLoginId() +" with type "+llpParametersService.findByCodeTypeValue(Parameter.USER_STATUS, llpUserProfile.getUserStatus()) );
			}
			
			if (!Parameter.YES_NO_yes.equals(mykadModel.getThumbPrintSuccess()) ) {
				throw new Exception("Please do thumbprint verification or go verify at SSM Counter!!");
			}
			
			llpUserProfile.setName(mykadModel.getAvailableName());//overide from mykad
			llpUserProfile.setRace(mykadModel.getRace());//overide from mykad
			llpUserProfile.setGender(mykadModel.getGender()); //overide from mykad
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			llpUserProfile.setDob(mykadModel.getDob()); //overide from mykad
			String raceCode = Parameter.RACE_OTHERS;
			for (int i = 0; i < listRace.size(); i++) {
				if (listRace.get(i).getCodeDesc().toLowerCase().indexOf(mykadModel.getRace().trim().toLowerCase()) != -1) {
					raceCode = listRace.get(i).getCode();
					break;
				}
			}
			llpUserProfile.setRace(raceCode);//overide from mykad
			
			llpUserProfile.setUserStatus(Parameter.USER_STATUS_active);
			llpUserProfile.setApproveBy(mykadModel.getAdUserRequester());
			try {

				UamUserInfo uamUserInfo = wSUamClientService.findCBSProfileWithEzbizRole(adUser);
				llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
			} catch (Exception e) {
			}
			llpUserProfile.setApproveDt(new Date());
			llpUserProfile.setApproveChannel(Parameter.USER_APPROVED_CHANNEL_SSMMYKADWEB);

			llpUserProfileService.update(llpUserProfile);
			String email = llpUserProfile.getEmail();
			mailService.sendMail(email, "email.notification.user.success.active.subject", llpUserProfile.getUserRefNo(),
					"email.notification.user.success.active.body", llpUserProfile.getName());
		}
		return llpUserProfile;
	}

}
