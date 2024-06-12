package com.ssm.llp.mod1.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.mod1.model.LlpUserProfile;

public interface LlpUserProfileDao  extends BaseDao<LlpUserProfile, String> {

	LlpUserProfile findByUserId(String userId);
	
	List<LlpUserProfile> findAllUsers();

	boolean checkStatusIdTypeAndIdNo(String idType, String idNo);

	LlpUserProfile findByIdTypeAndIdNo(String idType,String idNo);

	//boolean checkStatusIdTypeAndIdNo(LlpUserProfile llpUserProfile);

	boolean validateIdNoUponUpdate(LlpUserProfile llpUserProfile);

	boolean checkEmail(String email);

	boolean checkLoginId(String loginId, String userRefNo);

	boolean checkEmailUpdate(String userRefNo, String email);

	List<LlpUserProfile> getListLlpUserProfile(String idType,String idNo,String name, String userStatus);

	boolean validateIdNoUpdate(LlpUserProfile llpUserProfile);

	//SearchResult findByCriteriaUserProfile(SearchCriteria sc);;
	
	public LlpUserProfile findProfileInfoByUserIdNo(String idNo);

	boolean isPasswordUseByOthers(String userPwd, String loginId);

	LlpUserProfile findLatestActiveUserByIdNo(String idNo);

	LlpUserProfile findLatestProfileByUserRefNo(String userRefNo);

	boolean checkIsAllowedStatusByUserRefNo(String userRefNo, String status);


}

