/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormB2DetDao;
import com.ssm.ezbiz.service.RobFormB2DetService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormB2Det;

@Service
public class RobFormB2DetServiceImpl extends BaseServiceImpl<RobFormB2Det,  Long> implements RobFormB2DetService{
	@Autowired 
	RobFormB2DetDao robFormB2DetDao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormB2DetDao;
	}

	@Override
	public void deleteExceptId(String robFormBCode, long[] listIdNotDelete) {
		robFormB2DetDao.deleteExceptId(robFormBCode, listIdNotDelete);
	}

	@Override
	public List<RobFormB2Det> findByRobFormBCode(String robFormBCode) {
		return robFormB2DetDao.findByRobFormBCode(robFormBCode);
	}

}
