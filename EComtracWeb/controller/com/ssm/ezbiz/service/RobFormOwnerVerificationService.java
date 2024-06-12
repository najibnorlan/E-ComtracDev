/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;

public interface RobFormOwnerVerificationService extends BaseService<RobFormOwnerVerification, Long>{

	RobFormOwnerVerification findByRobFormCodeAndIdNo(String robFormBCode, String icNo);

	List<RobFormOwnerVerification> getListRobFormOwnerVerification(String robFormCRefNo);

	
	
	
	
	List<Object[]> findAllVerificationList(LlpUserProfile llpUserProfile);


	List<RobFormOwnerVerification> findByRobFormCode(String robFormCode);

	List<Object[]> findAllVerificationFormCList(LlpUserProfile llpUserProfile);

	RobFormOwnerVerification findByFormCodeAndUserRefNo(String robFormCode, String ezbizUserRefNo);
}

