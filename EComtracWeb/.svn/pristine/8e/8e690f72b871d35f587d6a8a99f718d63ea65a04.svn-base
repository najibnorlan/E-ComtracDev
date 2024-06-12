/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface RobFormOwnerVerificationDao extends BaseDao<RobFormOwnerVerification, Long> {
	
	RobFormOwnerVerification findByRobFormCodeAndIdNo(String robFormCode, String idNo);

	List<RobFormOwnerVerification> getListRobFormOwnerVerification(
			String robFormCRefNo);

	List<Object[]> findAllVerificationList(LlpUserProfile llpUserProfile);

	List<RobFormOwnerVerification> findByRobFormCode(String robFormCode);

	List<Object[]> findAllVerificationFormCList(LlpUserProfile llpUserProfile);

	RobFormOwnerVerification findByFormCodeAndUserRefNo(String robFormCode, String ezbizUserRefNo);
}
