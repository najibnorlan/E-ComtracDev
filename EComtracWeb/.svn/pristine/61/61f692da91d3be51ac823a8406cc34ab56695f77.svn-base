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

import com.ssm.ezbiz.dao.RobBusinessDao;
import com.ssm.ezbiz.service.RobBusinessService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobBusiness;

@Service
public class RobBusinessServiceImpl extends BaseServiceImpl<RobBusiness,  String> implements RobBusinessService{
	@Autowired 
	RobBusinessDao robBusinessDao;

	@Override
	public BaseDao getDefaultDao() {
		return robBusinessDao;
	}

}
