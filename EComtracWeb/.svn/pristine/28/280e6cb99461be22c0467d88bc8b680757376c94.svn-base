/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.common.dao.LlpUserLogDao;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.service.LlpUserLogService;

@Service
public class LlpUserLogServiceImpl extends BaseServiceImpl<LlpUserLog,  Long> implements LlpUserLogService{
	@Autowired 
	LlpUserLogDao llpUserLogDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpUserLogDao;
	}

	@Override
	public LlpUserLog findLastLogin(String userId) {
		return llpUserLogDao.findLastLogin(userId);
	}

}
