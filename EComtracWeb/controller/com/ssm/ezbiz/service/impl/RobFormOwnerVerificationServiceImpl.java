/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormOwnerVerificationDao;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;

@Service
public class RobFormOwnerVerificationServiceImpl extends BaseServiceImpl<RobFormOwnerVerification,  Long> implements RobFormOwnerVerificationService{
	@Autowired 
	RobFormOwnerVerificationDao robFormOwnerVerificationDao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormOwnerVerificationDao;
	}



	@Override
	public RobFormOwnerVerification findByRobFormCodeAndIdNo(
			String robFormCode, String idNo) {
		// TODO Auto-generated method stub
		return robFormOwnerVerificationDao.findByRobFormCodeAndIdNo(robFormCode, idNo);
	}



	@Override
	public List<RobFormOwnerVerification> getListRobFormOwnerVerification(
			String robFormCRefNo) {
		return robFormOwnerVerificationDao.getListRobFormOwnerVerification(robFormCRefNo);
	}


	@Override
	public List<Object[]> findAllVerificationList(LlpUserProfile llpUserProfile) {
		return robFormOwnerVerificationDao.findAllVerificationList(llpUserProfile);
	}

	@Override
	public List<RobFormOwnerVerification> findByRobFormCode(String robFormCode) {
		return robFormOwnerVerificationDao.findByRobFormCode(robFormCode);
	}

	
	@Override
	public List<Object[]> findAllVerificationFormCList(LlpUserProfile llpUserProfile) {
		return robFormOwnerVerificationDao.findAllVerificationFormCList(llpUserProfile);
	}



	@Override
	public RobFormOwnerVerification findByFormCodeAndUserRefNo(String robFormCode, String ezbizUserRefNo) {
		return robFormOwnerVerificationDao.findByFormCodeAndUserRefNo(robFormCode, ezbizUserRefNo);
	}
}
