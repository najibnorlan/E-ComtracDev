package com.ssm.llp.mod1.service;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobSyncProfileAudit;
import com.ssm.llp.mod1.model.LlpUserProfile;

public interface LlpUserProfileService extends BaseService<LlpUserProfile, String> {
	LlpUserProfile getProfileLogin(String userId, String password) throws SSMException;

	public LlpUserProfile findProfileInfoByUserId(String userId) ;
	
	public List<LlpUserProfile> findProfileInfo() ;

	public LlpUserProfile generateForgetpassword(String userId, String mykad) throws SSMException;

	public void insertNewLlpUserProfile(LlpUserProfile llpUserProfile) throws SSMException;

	public void updateLlpUserProfile(LlpUserProfile llpUserProfile, LlpUserProfile llpUserProfileNew) throws SSMException;

	LlpUserProfile findByIdTypeAndIdNo(String idType, String idNo);

	LlpUserProfile findByIdTypeAndIdNoWithNamePortion(String idType, String idNo, String name) throws SSMException;

	public void validateInsertPersonForPartner(LlpUserProfile entity, boolean isRecordExist) throws SSMException;

	public void validateBlacklistedPersonForPartner(String idType, String idNo) throws SSMException;
	
	public boolean checkBlacklist(String idType, String idNo) throws SSMException;
	
	public LlpUserProfile findProfileInfoByUserIdNo(String idNo);

	public String generatePassword();
	
	public String findCBSOwnerNameByIcWS(String icNo) throws SSMException;
	
	public void updateProfileInfoByWS(RobSyncProfileAudit robSyncProfileAudit) throws SSMException;

	boolean isEmailExist(String ezBizEmail);

	void updateStatus(String loginId, String userStatus , String remarks, Date deceasedDt) throws SSMException;
	
	boolean isPasswordUseByOthers(String userPwd, String loginId);
	
	
	public void changeUserPasswordByPublic(String loginName, String password, String newPassword1, String newPassword2) throws SSMException;
	
	public void changeUserPasswordByOfficer(String loginName, String newPassword) throws SSMException;
	
	public LlpUserProfile findLatestActiveUserByIdNo(String idNo);
	
	public LlpUserProfile findLatestProfileByUserRefNo(String userRefNo);

	boolean checkIsAllowedStatusByUserRefNo(String userRefNo, String status);
}
