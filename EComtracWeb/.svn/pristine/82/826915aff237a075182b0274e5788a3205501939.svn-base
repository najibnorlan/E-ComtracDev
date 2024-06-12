/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormB2Dao;
import com.ssm.ezbiz.service.RobFormB2Service;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormB2;

@Service
public class RobFormB2ServiceImpl extends BaseServiceImpl<RobFormB2,  Long> implements RobFormB2Service{
	@Autowired 
	RobFormB2Dao robFormB2Dao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormB2Dao;
	}

	@Override
	public RobFormB2 findByRobFormBCode(String robFormBCode) {
		return robFormB2Dao.findByRobFormBCode(robFormBCode);
	}

}
