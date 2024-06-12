package com.ssm.llp.mod1.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.ExtUserPairingInfoService;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobSyncProfileAuditService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.sec.MD5DigestUtils;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.RocBlacklistService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobSyncProfileAudit;
import com.ssm.llp.mod1.dao.LlpUserProfileDao;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessFindOwnerReq;
import com.ssm.webis.param.BusinessFindOwnerResp;
import com.ssm.webis.param.BusinessUpdateOwnerReq;
import com.ssm.webis.param.BusinessUpdateOwnerResp;
import com.ssm.webis.param.UamUserInfo;

@Service
public class LlpUserProfileServiceImpl extends BaseServiceImpl<LlpUserProfile, String> implements LlpUserProfileService {
	@Autowired
	LlpUserProfileDao llpUserProfileDao;
	
	@Autowired
	RobFormAOwnerService robFormAOwnerService;
	
	@Autowired
	RobSyncProfileAuditService robSyncProfileAuditService;

	@Override
	public BaseDao getDefaultDao() {
		return llpUserProfileDao;
	}

	@Autowired
	RocBlacklistService rocBlacklistService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;
	
	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;
	

	@Autowired
	@Qualifier("WSUamClientService")
	private WSUamClientService wSUamClientService;
	

	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	ExtUserPairingInfoService extUserPairingInfoService; 

	
	@Override
	public LlpUserProfile getProfileLogin(String userId, String password) throws SSMException {
		LlpUserProfile llpUserProfile = llpUserProfileDao.findByUserId(userId);
		if (llpUserProfile == null) {
			throw new SSMException(SSMExceptionParam.USERNAME_NOT_MATCH);
		}
//		if (!(Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus()))) {
//			throw new SSMException(SSMExceptionParam.NOT_ACTIVATE, llpUserProfile.getLoginId());
//		}
		if (!MD5DigestUtils.isPasswordValid(llpUserProfile.getUserPwd(), password)) {
			throw new SSMException(SSMExceptionParam.USERNAME_NOT_MATCH);
		}
		
		if(Parameter.USER_STATUS_deactive.equals(llpUserProfile.getUserStatus())){ //deactivate user
			throw new SSMException(SSMExceptionParam.USER_DEACTIVATE);
		}
		
		if(Parameter.USER_STATUS_suspend.equals(llpUserProfile.getUserStatus())){ //suspended user
			throw new SSMException(SSMExceptionParam.USER_SUSPENDED);
		}
		
		if(Parameter.USER_STATUS_deceased.equals(llpUserProfile.getUserStatus())){ //deceased user
			throw new SSMException(SSMExceptionParam.USER_DECEASED);
		}
		return llpUserProfile;
	}

	@Override
	@Transactional
	public void update(LlpUserProfile entity) {
		super.update(entity);
	}

	/*
	 * @Transactional public void insert(LlpUserProfile entity) {
	 * entity.setUserPwd(MD5DigestUtils.encrypt(entity.getUserPwd()));
	 * 
	 * boolean chechkStatus = rocBlacklistService.checkStatusIdTypeAndIdNo(
	 * entity.getIdType(), entity.getIdNo());
	 * 
	 * System.out.println("chechkStatus" + chechkStatus); if (!chechkStatus) {
	 * super.insert(entity); mailService.sendMail(entity.getEmail(),
	 * "User Profile already Created",
	 * "Congrulations! Your User Profile already Created.Your User Id :" +
	 * entity
	 * .getLoginId()+"For ID Activation, Please Come To the nearest SSM's office."
	 * ); } else { try { throw new SSMException("Id No Already Blacklisted."); }
	 * catch (SSMException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } }
	 */
	@Override
	public LlpUserProfile findProfileInfoByUserId(String userId) {
		LlpUserProfile llpUserProfile = llpUserProfileDao.findByUserId(userId);
		return llpUserProfile;
	}
	
	@Override
	public List<LlpUserProfile> findProfileInfo() {
		List<LlpUserProfile> llpUserProfile = llpUserProfileDao.findAllUsers();
		return llpUserProfile;
	}
	
	@Override
	public LlpUserProfile generateForgetpassword(String userId, String myKadNo) throws SSMException {
		if(StringUtils.isBlank(userId) && StringUtils.isBlank(myKadNo) ){
			throw new SSMException(SSMExceptionParam.USER_PROFILE_GENERATE_PASSWORD_EMPTY_ID_AND_MYKAD);
		}
		LlpUserProfile llpUserProfile = null;
		if(StringUtils.isNotBlank(userId)){
			llpUserProfile = llpUserProfileDao.findByUserId(userId);
			if(llpUserProfile==null){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NOT_FOUND, userId);
			}
		}else{
			llpUserProfile = llpUserProfileDao.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, myKadNo);
			if(llpUserProfile==null){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_MYKAD_NOT_FOUND, myKadNo);
			}
		}
		
		String password = generatePassword();
		llpUserProfile.setUserPwd(MD5DigestUtils.encrypt(password));
		if(StringUtils.isBlank(llpUserProfile.getIcColour())){
			llpUserProfile.setIcColour(Parameter.NRIC_COLOUR_none);
		}
		llpUserProfile.setIsTempPassword(Parameter.YES_NO_yes);

		String email = llpUserProfile.getEmail();

		if (!StringUtils.isEmpty(email)) {
			try {
				mailService.sendImmediately(email, "email.notification.user.generatePassword.subject", llpUserProfile.getUserRefNo(),
						"email.notification.user.generatePassword.body", llpUserProfile.getName(), password, llpUserProfile.getLoginId());
			} catch (Exception e) {
				throw new SSMException(SSMExceptionParam.EMAIL_PROBLEM);
			}
		}else {
			throw new SSMException(SSMExceptionParam.EMAIL_NULL);
		}
		
		//If Send Password Ok then update Pasword
		super.update(llpUserProfile);
		
		extUserPairingInfoService.resetStatus(llpUserProfile.getLoginId(),Parameter.EXT_USER_PAIRING_STATUS_RESET_BY_USER);
		
		return llpUserProfile;
	}

	@Override
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

	@Override
	@Transactional
	public void changeUserPasswordByPublic(String loginName, String oldPassword, String newPassword1, String newPassword2) throws SSMException {
		// TODO Auto-generated method stub

		LlpUserProfile llpUserProfile = llpUserProfileDao.findByUserId(loginName);
		
		if(isPasswordUseByOthers(newPassword1, loginName)){
			throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_PASSWORD_NOT_ALLOW);
		}

		if (!MD5DigestUtils.isPasswordValid(llpUserProfile.getUserPwd(), oldPassword)) {
			throw new SSMException(SSMExceptionParam.USER_PROFILE_OLD_PASSWORD_NOT_MATCH);
		} else if (!(newPassword1.equals(newPassword2))) {
			throw new SSMException(SSMExceptionParam.USER_PROFILE_NEW_PASSWORD_NOT_MATCH);
		} else {
			
			llpUserProfile.setUserPwd(MD5DigestUtils.encrypt(newPassword2));
			llpUserProfile.setIsTempPassword(Parameter.YES_NO_no);
			llpUserProfileDao.update(llpUserProfile);
			
			extUserPairingInfoService.resetStatus(llpUserProfile.getLoginId(),Parameter.EXT_USER_PAIRING_STATUS_RESET_BY_USER);
		}

	}
	
	@Override
	@Transactional
	public void changeUserPasswordByOfficer(String loginName, String newPassword) throws SSMException {

		LlpUserProfile llpUserProfile = llpUserProfileDao.findByUserId(loginName);
		
		
		if(isPasswordUseByOthers(MD5DigestUtils.encrypt(newPassword), loginName)){
			throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_PASSWORD_NOT_ALLOW);
		}

		llpUserProfile.setUserPwd(MD5DigestUtils.encrypt(newPassword));
		llpUserProfile.setIsTempPassword(Parameter.YES_NO_yes);
		llpUserProfile.setRemarks("Change Password by:"+UserEnvironmentHelper.getLoginName());
		
		llpUserProfileDao.update(llpUserProfile);
		
		extUserPairingInfoService.resetStatus(llpUserProfile.getLoginId(),Parameter.EXT_USER_PAIRING_STATUS_RESET_BY_OFFICER);

	}
	
	

	@Override
	@Transactional
	public void insertNewLlpUserProfile(LlpUserProfile entity) throws SSMException {

		if (!(entity.getUserPwd().equals(entity.getReconfirmPassword()))) {
			//throw new SSMException("New Password Not Match");
			throw new SSMException(SSMExceptionParam.USER_PROFILE_NEW_PASSWORD_NOT_MATCH);
		}else if(!(entity.getEmail().equals(entity.getReTypeEmail()))) {
			throw new SSMException(SSMExceptionParam.USER_PROFILE_EMAIL_NOT_MATCH);
		}else{
			
			entity.setUserPwd(MD5DigestUtils.encrypt(entity.getUserPwd()));
			

			String idtype = mappingIdType(entity.getIdType());

			java.util.Date After18YearOld = null;

			boolean is18above18 = false;
			Calendar exp = Calendar.getInstance();
			exp.setTime(new Date());
			exp.setLenient(true);
			exp.add(Calendar.YEAR, -18);
			After18YearOld = exp.getTime();
			
			boolean checkExpiredDate = true;

			boolean checksizeIc = true;
			if (entity.getIdType().equals(Parameter.ID_TYPE_newic)) {
				checksizeIc = validateICSize(entity.getIdType(), entity.getIdNo());
			}
			boolean checkIDlogin = true;
			if (After18YearOld.after(entity.getDob())) {
				is18above18 = true;
			}
			if (StringUtils.isNotBlank(entity.getLoginId())) {
				checkIDlogin = validateLoginId(entity.getLoginId(), entity.getUserRefNo());
			}

			boolean checkIdNo = validateIdNo(entity.getIdType(), entity.getIdNo());
			checkIDlogin = validateLoginId(entity.getLoginId(), entity.getUserRefNo());
			boolean checkEmail = validateEmail(entity.getEmail());
			boolean chechkStatus = false;//rocBlacklistService.checkStatusIdTypeAndIdNo(idtype, entity.getIdNo());
			if(entity.getIdExpiredDt()!=null){
				checkExpiredDate = checkExpiredDate(entity.getIdExpiredDt());
			}
			
			
			if(Parameter.NATIONALITY_TYPE_CITIZEN.equals(entity.getNationality()) ){
				//allow
			}else if(Parameter.NATIONALITY_TYPE_PERMANENT_RESIDENT.equals(entity.getNationality()) ){
				//allow
			}else if(Parameter.NATIONALITY_TYPE_TEMP_RESIDENT.equals(entity.getNationality()) ){
				throw new SSMException("Temporary Resident not allow to register");
			}else{
				throw new SSMException("Foreigners not allow to register: "+entity.getNationality());
			}	
			
			
			
			if(isPasswordUseByOthers(entity.getUserPwd(), entity.getLoginId())){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_PASSWORD_NOT_ALLOW);
			}
			
			
			if (checksizeIc) {
				if (!checkIDlogin) {
					if (!checkEmail) {
						if (!checkIdNo) {
							if (is18above18) {
								if(checkExpiredDate){
								if (!chechkStatus) {
										insert(entity);

//										email will be shoot if no problem
//										String email = entity.getEmail();
//										if(!StringUtils.isEmpty(email)){
//													mailService.sendMail(email, "email.notification.user.register.subject", entity.getUserRefNo(),"email.notification.user.register.body",entity.getName(),entity.getUserRefNo());
//										}
								} else {
									throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_BLACKLIST);

								}
								} else {
									throw new SSMException(SSMExceptionParam.USER_PROFILE_LICIENSE_EXPIRED);
								}
								
							} else {
								throw new SSMException(SSMExceptionParam.USER_PROFILE_IS_18_YEARS_OLD);
							}

						} else {
							throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_EXIST);
						}

					} else {
						throw new SSMException(SSMExceptionParam.USER_PROFILE_EMAIL_EXIST);
					}
				} else {
					throw new SSMException(SSMExceptionParam.USER_PROFILE_LOGIN_ID_EXIST);
				}

			} else {
				throw new SSMException(SSMExceptionParam.USER_PROFILE_IC_WRONG_FORMAT);
			}
		}

	}

	private boolean validateEmail(String email) {
		// TODO Auto-generated method stub
		boolean emailExist = llpUserProfileDao.checkEmail(email);
		return emailExist;
	}

	private boolean validateLoginId(String loginId, String userRefNo) {
		// TODO Auto-generated method stub
		boolean loginIDExist = llpUserProfileDao.checkLoginId(loginId, userRefNo);
		return loginIDExist;
	}

	private boolean validateIdNo(String idType, String idNo) {
		boolean idNoExist = llpUserProfileDao.checkStatusIdTypeAndIdNo(idType, idNo);
		return idNoExist;
	}

	private boolean validateIdNoUponUpdate(LlpUserProfile llpUserProfile) {
		boolean idNoExist = llpUserProfileDao.validateIdNoUponUpdate(llpUserProfile);
		return idNoExist;
	}

	private String mappingIdType(String idType) {

		String idTypeNew = "";
		if (idType.equals(Parameter.ID_TYPE_newic)) {
			idTypeNew = Parameter.ROC_BLACKLIST_TYPE_newIc;
		} else if (idType.equals(Parameter.ID_TYPE_oldic)) {
			idTypeNew = Parameter.ROC_BLACKLIST_TYPE_oldIc;
		} else if (idType.equals(Parameter.ID_TYPE_passport)) {
			idTypeNew = Parameter.ROC_BLACKLIST_TYPE_passport;
		} else if (idType.equals(Parameter.ID_TYPE_police)) {
			idTypeNew = Parameter.ROC_BLACKLIST_TYPE_policeid;
		}
		return idTypeNew;
	}

	@Override
	@Transactional
	public void updateStatus(String loginId, String userStatus , String remarks, Date deceasedDt) throws SSMException {
		
		LlpUserProfile llpUserProfile = findProfileInfoByUserId(loginId);
		
		
		String previosStatus = llpUserProfile.getUserStatus();
		
		llpUserProfile.setUserStatus(userStatus);
		llpUserProfile.setRemarks(remarks);
		
		String email = llpUserProfile.getEmail();
		
		if(StringUtils.isNotBlank(email) && Parameter.USER_STATUS_active.equals(userStatus) && (Parameter.USER_STATUS_pending.equals(previosStatus)) ){
			llpUserProfile.setApproveBy(UserEnvironmentHelper.getLoginName());
			try {
				String cbsUserId = StringUtils.remove(UserEnvironmentHelper.getLoginName(),"SSM:");
//				String branchCode = uamUserProfileService.getDefaultBranch(cbsUserId);
				UamUserInfo uamUserInfo = wSUamClientService.findCBSProfileWithEzbizRole(cbsUserId);
				
				llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
			} catch (Exception e) {
			}
			llpUserProfile.setApproveDt(new Date());
			llpUserProfile.setApproveChannel(Parameter.USER_APPROVED_CHANNEL_COUNTER);
			
			mailService.sendMail(llpUserProfile.getEmail(), "email.notification.user.success.active.subject", llpUserProfile.getUserRefNo(),"email.notification.user.success.active.body",llpUserProfile.getName());
		}
		if( Parameter.USER_STATUS_deceased.equals(userStatus) ){
			if(deceasedDt==null) {
				throw new SSMException(SSMExceptionParam.USER_STATUS_DECEASED_MUST_HAVE_DECEASED_DT);
			}
			llpUserProfile.setDeceasedDt(deceasedDt);
			
		}else {
			llpUserProfile.setDeceasedDt(null);
		}
		
		
		if(!Parameter.USER_STATUS_active.equals(userStatus)  ) {
			extUserPairingInfoService.resetStatus(llpUserProfile.getLoginId(), userStatus);
		}
		
		update(llpUserProfile);
	}	

	@Override
	@Transactional
	public void updateLlpUserProfile(LlpUserProfile llpUserProfile, LlpUserProfile newLlpUserProfile) throws SSMException {

		// LlpUserProfile llpUserProfileNew
		// =llpUserProfileDao.findByUserId(llpUserProfile.getLoginId()); //
		// compare status - change status- send email
		String idtype = mappingIdType(llpUserProfile.getIdType());
		boolean checkIdNo = true;
		boolean checksizeIc = true;
		boolean checkExpiredDate = true;
		
		
		boolean is18above = check18above(llpUserProfile);
		checkIdNo = validateIdNoUponUpdate(llpUserProfile);
		boolean chechkStatus = false;//no need to check blacklist rocBlacklistService.checkStatusIdTypeAndIdNo(idtype, llpUserProfile.getIdNo());
		boolean checkEmail = validateEmailUpdate(llpUserProfile.getUserRefNo(), llpUserProfile.getEmail());
		boolean checkIDlogin = false;
		if (StringUtils.isBlank(newLlpUserProfile.getLoginId())) {
			
			if(StringUtils.isNotBlank(llpUserProfile.getLoginId())){
			checkIDlogin = validateLoginId(llpUserProfile.getLoginId(), llpUserProfile.getUserRefNo());
			}
			if(StringUtils.isNotBlank(llpUserProfile.getUserPwd())){
			llpUserProfile.setUserPwd(MD5DigestUtils.encrypt(llpUserProfile.getUserPwd()));
			}
		}
		if (llpUserProfile.getIdType().equals(Parameter.ID_TYPE_newic)) {
			checksizeIc = validateICSize(llpUserProfile.getIdType(), llpUserProfile.getIdNo());
		}

		if(llpUserProfile.getIdExpiredDt()!=null){
			checkExpiredDate = checkExpiredDate(llpUserProfile.getIdExpiredDt());
		}
		

		if (checksizeIc) {
			if (!checkIDlogin) {
				if (!checkIdNo) {
					if (!checkEmail) {
						if (is18above) {
							if (!chechkStatus) {
								if(checkExpiredDate){
									update(llpUserProfile);
									
									
									List<RobFormAOwner> owners = robFormAOwnerService.findByIcNoInStatus(llpUserProfile.getIdNo(), new String[]{Parameter.ROB_FORM_A_STATUS_DATA_ENTRY,Parameter.ROB_FORM_A_STATUS_QUERY});
									for(RobFormAOwner owner : owners){
										owner.setName(llpUserProfile.getName());
										owner.setNewicno(llpUserProfile.getIdNo());
										
										robFormAOwnerService.update(owner);
									}
									
								if (StringUtils.isBlank(newLlpUserProfile.getLoginId()) && StringUtils.isNotBlank(llpUserProfile.getLoginId()) && (newLlpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_active) || llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_pending) || llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_deactive))) {
									String email = llpUserProfile.getEmail();
									mailService.sendMail(email, "email.notification.user.register.subject", llpUserProfile.getUserRefNo(),"email.notification.user.register.body",llpUserProfile.getName(),llpUserProfile.getUserRefNo(),llpUserProfile.getLoginId());
									
								}
								if (Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus())
										&& (newLlpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_pending) || newLlpUserProfile
												.getUserStatus().equals(Parameter.USER_STATUS_deactive))) {
									String email = llpUserProfile.getEmail();
									llpUserProfile.setApproveBy(UserEnvironmentHelper.getLoginName());
									try {
										String cbsUserId = StringUtils.remove(UserEnvironmentHelper.getLoginName(),"SSM:");
//										String branchCode = uamUserProfileService.getDefaultBranch(cbsUserId);
										UamUserInfo uamUserInfo = wSUamClientService.findCBSProfileWithEzbizRole(cbsUserId);
										llpUserProfile.setApproveByBranch(uamUserInfo.getDefaultBranch());
									} catch (Exception e) {
									}
									llpUserProfile.setApproveDt(new Date());
									update(llpUserProfile);
									if(!StringUtils.isEmpty(email)){
										mailService.sendMail(email, "email.notification.user.success.active.subject", llpUserProfile.getUserRefNo(),"email.notification.user.success.active.body",llpUserProfile.getName());
								}
//									
								}
								
								if (Parameter.USER_STATUS_deactive.equals(llpUserProfile.getUserStatus())
										&& (newLlpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_pending) || newLlpUserProfile
												.getUserStatus().equals(Parameter.USER_STATUS_active))) {
									
										String email = llpUserProfile.getEmail();
									
									if(!StringUtils.isEmpty(email)){
										SimpleMailMessage smm = new SimpleMailMessage();
										smm.setSubject("/");
										
										String bodyEmail = "Dear Sir/Madam"+" "+llpUserProfile.getName()+",\n"+
												"<br><br><br>"+
												"Please be informed that your account on LLP System has now been deactivated"+".\n"+
												"<br><br><br>"+
												"Should you have any queries or require any further information please contact us at 603-2299 5500. We welcome any comments or suggestions you may have on how we can improve our services"+".\n"+
												"<br><br>"+
												"Thank you for using our services"+"\n"+
												"<br><br><br>"+
												"Yours sincerely,"+"\n"+
												"<br>"+
												"Admin"+
												"<br><br>"+
												"(c)SSM - All rights reserved.";
												mailService.sendMail(email, "Your Account has been Deactivated", newLlpUserProfile.getUserRefNo(), bodyEmail);
								}	
							}
								} else {
									throw new SSMException(SSMExceptionParam.USER_PROFILE_LICIENSE_EXPIRED);
									

								}
							} else {
								throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_BLACKLIST);
								//throw new SSMException("This ID No is in the blacklist's list.Please Refer SSM Office");

							}// TODO Auto-generated method stub

						} else {
							throw new SSMException(SSMExceptionParam.USER_PROFILE_IS_18_YEARS_OLD);
							//throw new SSMException("Registration only for age above 18 years old.");
						}
					} else {
						throw new SSMException(SSMExceptionParam.USER_PROFILE_EMAIL_EXIST);
						//throw new SSMException("The Email already Exist.");
					}
				} else {
					//throw new SSMException("The ID No already Exist.");
					throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_EXIST);
				}

			} else {
				//throw new SSMException("Sorry. The Login ID Already Exist.");
				throw new SSMException(SSMExceptionParam.USER_PROFILE_LOGIN_ID_EXIST);
			}

		} else {
			//throw new SSMException("Please Key in IC Number format - 801230141234");
			throw new SSMException(SSMExceptionParam.USER_PROFILE_IC_WRONG_FORMAT);
		}
	}

	private boolean checkExpiredDate(Date idExpiredDt) {
		Calendar cal = Calendar.getInstance();
		Date dateCurrent = cal.getTime();
		
		if(idExpiredDt.before(dateCurrent)){
			return false;
		}
		return true;
	}

	private boolean validateICSize(String idType, String idNo) {

		boolean size = true;
		// TODO Auto-generated method stub
		if (Parameter.ID_TYPE_newic.equals(idType)) {
			if (idNo.length() > 12 || idNo.length() < 12) {
				size = false;
			}
		}
		return size;
	}

	private boolean validateEmailUpdate(String userRefNo, String email) {
		// TODO Auto-generated method stub
		boolean emailExist = llpUserProfileDao.checkEmailUpdate(userRefNo, email);
		return emailExist;
	}

	private boolean check18above(LlpUserProfile llpUserProfile) {
		// TODO Auto-generated method stub
		java.util.Date After18YearOld = null;

		boolean is18above = false;
		Calendar exp = Calendar.getInstance();
		exp.setTime(new Date());
		exp.setLenient(true);
		exp.add(Calendar.YEAR, -18);
		After18YearOld = exp.getTime();

		if (After18YearOld.after(llpUserProfile.getDob())) {
			is18above = true;
		}
		return is18above;
	}

	@Override
	public LlpUserProfile findByIdTypeAndIdNo(String idType, String idNo) {
		// TODO Auto-generated method stub

		LlpUserProfile llpUserProfile = llpUserProfileDao.findByIdTypeAndIdNo(idType, idNo);
		return llpUserProfile;
	}

	@Override
	public LlpUserProfile findByIdTypeAndIdNoWithNamePortion(String idType, String idNo, String name) throws SSMException {

		LlpUserProfile llpUserProfile = findByIdTypeAndIdNo(idType, idNo);
		
		

		if (llpUserProfile != null) {
			if (llpUserProfile.getName().toUpperCase().indexOf(name.toUpperCase()) != -1) { // if found																			
				return llpUserProfile;
			} else { // if nama not equal
				//throw new SSMException("Name and Id does not match!!");
				throw new SSMException(SSMExceptionParam.USER_PROFILE_NAME_ID_NOT_MATCH);
			}
		}

		return llpUserProfile; // if null

	}
	
	@Override
	public void validateBlacklistedPersonForPartner(String type, String no) throws SSMException {
		String idtype = mappingIdType(type);
		boolean chechkStatus = rocBlacklistService.checkStatusIdTypeAndIdNo(idtype, no); //if found = true
		if (chechkStatus) {
			throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_BLACKLIST);
		}
		
	}
	
	@Override
	public void validateInsertPersonForPartner(LlpUserProfile entity, boolean isRecordExist) throws SSMException {

			validateBlacklistedPersonForPartner(entity.getIdType(), entity.getIdNo());

			java.util.Date After18YearOld = null;

			boolean is18above18 = false;
			Calendar exp = Calendar.getInstance();
			exp.setTime(new Date());
			exp.setLenient(true);
			exp.add(Calendar.YEAR, -18);
			After18YearOld = exp.getTime();
			
			if (After18YearOld.after(entity.getDob())) {
				is18above18 = true;
			}
			
			if(!is18above18){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_IS_18_YEARS_OLD);
			}

			//already cater in search page (ID type and number non editable)
//			boolean checksizeIc = true;
//			if (entity.getIdType().equals(Parameter.ID_TYPE_newic)) {
//				checksizeIc = validateICSize(entity.getIdType(), entity.getIdNo());
//			}
			

			//only check for new person (for table llp_user_profile)
			if(!isRecordExist){
			boolean checkIdNo = validateIdNo(entity.getIdType(), entity.getIdNo()); //if found = true
			boolean checkEmail = validateEmail(entity.getEmail()); //if found = true, userRefNo = null
			
			if(checkIdNo){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_ID_NO_EXIST);
			}
			
			if(checkEmail){
				throw new SSMException(SSMExceptionParam.USER_PROFILE_EMAIL_EXIST);
			}
			
			}
	}

	@Override
	public boolean checkBlacklist(String idType, String idNo) throws SSMException {
		// TODO Auto-generated method stub
		String idtype = mappingIdType(idType);
		boolean chechkStatus = rocBlacklistService.checkStatusIdTypeAndIdNo(idtype, idNo); //if found = true
		
		return chechkStatus;
	}
	
	@Override
	@Transactional
	public LlpUserProfile findProfileInfoByUserIdNo(String idNo) {
		LlpUserProfile llpUserProfile = llpUserProfileDao.findProfileInfoByUserIdNo(idNo);
		return llpUserProfile;
	}

	@Override
	public String findCBSOwnerNameByIcWS(String icNo) throws SSMException { 
		
		String url = wSManagementService.getWsUrl("RobClient.findOwnerByIC");
		BusinessFindOwnerReq param = new BusinessFindOwnerReq();
		param.setIcNo(icNo);
		
		try {

			BusinessFindOwnerResp ssmWsResp = RobClient.findOwnerByIC(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			return ssmWsResp.getName();
		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}
		
	}
	
	@Override
	@Transactional
	public void updateProfileInfoByWS(RobSyncProfileAudit robSyncProfileAudit) throws SSMException { 

		robSyncProfileAuditService.insert(robSyncProfileAudit);
		
		String url = wSManagementService.getWsUrl("RobClient.updateOwnerByIc");
		BusinessUpdateOwnerReq param = new BusinessUpdateOwnerReq();
		param.setIcNo(robSyncProfileAudit.getIcNo());
		param.setNameToChange(robSyncProfileAudit.getUpdatedName());
		param.setRemarks(robSyncProfileAudit.getRemarks());
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		
		try {

			BusinessUpdateOwnerResp ssmWsResp = RobClient.updateOwnerNameByIc(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}

		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}
		
	}

	@Override
	public boolean isEmailExist(String ezBizEmail) {
		return llpUserProfileDao.checkEmail(ezBizEmail);
	}

	@Override
	public boolean isPasswordUseByOthers(String md5Passwd, String loginId) {
		String isAllowUseSamePass = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_ALLOW_USE_SAME_PASS);
		if(Parameter.YES_NO_yes.equals(isAllowUseSamePass)) {//mean no need to check
			return false;
		}
		return llpUserProfileDao.isPasswordUseByOthers(md5Passwd, loginId);
	}

	@Override
	public LlpUserProfile findLatestActiveUserByIdNo(String idNo) {
		return llpUserProfileDao.findLatestActiveUserByIdNo(idNo);
	}
	
	@Override
	public LlpUserProfile findLatestProfileByUserRefNo(String userRefNo) {
		return llpUserProfileDao.findLatestProfileByUserRefNo(userRefNo);
	}
	
	@Override
	public boolean checkIsAllowedStatusByUserRefNo(String userRefNo, String status) {
		return llpUserProfileDao.checkIsAllowedStatusByUserRefNo(userRefNo, status);
	}
	
}