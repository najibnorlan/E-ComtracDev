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

import com.ssm.ezbiz.dao.RobFormB1Dao;
import com.ssm.ezbiz.service.RobFormB1Service;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormB1;

@Service
public class RobFormB1ServiceImpl extends BaseServiceImpl<RobFormB1,  Long> implements RobFormB1Service{
	@Autowired 
	RobFormB1Dao robFormB1Dao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormB1Dao;
	}

	@Override
	public RobFormB1 findByRobFormBCode(String robFormBCode) {
		return robFormB1Dao.findByRobFormBCode(robFormBCode);
	}

}
