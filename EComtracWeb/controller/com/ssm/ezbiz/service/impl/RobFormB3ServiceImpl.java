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

import com.ssm.ezbiz.dao.RobFormB3Dao;
import com.ssm.ezbiz.service.RobFormB3Service;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormB3;

@Service
public class RobFormB3ServiceImpl extends BaseServiceImpl<RobFormB3,  Long> implements RobFormB3Service{
	@Autowired 
	RobFormB3Dao robFormB3Dao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormB3Dao;
	}

	@Override
	public void deleteExceptId(String robFormBCode, long[] listB3IdNotDelete) {
		robFormB3Dao.deleteExceptId(robFormBCode,listB3IdNotDelete);
	}

	@Override
	public List<RobFormB3> findByRobFormBCode(String robFormBCode) {
		return robFormB3Dao.findByRobFormBCode(robFormBCode);
	}

}
