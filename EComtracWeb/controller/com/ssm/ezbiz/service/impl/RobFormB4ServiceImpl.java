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

import com.ssm.ezbiz.dao.RobFormB4Dao;
import com.ssm.ezbiz.service.RobFormB4Service;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormB4;

@Service
public class RobFormB4ServiceImpl extends BaseServiceImpl<RobFormB4,  Long> implements RobFormB4Service{
	@Autowired 
	RobFormB4Dao robFormB4Dao;

	@Override
	public BaseDao getDefaultDao() {
		return robFormB4Dao;
	}

	@Override
	public List<RobFormB4> findByRobFormBCode(String robFormBCode) {
		return robFormB4Dao.findByRobFormBCode(robFormBCode);
	}

	@Override
	public void deleteExceptId(String robFormBCode, long[] listB4IdNotDelete) {
		robFormB4Dao.deleteExceptId(robFormBCode,listB4IdNotDelete);
	}

}
