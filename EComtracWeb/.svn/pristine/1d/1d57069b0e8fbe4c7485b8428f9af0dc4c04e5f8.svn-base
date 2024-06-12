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

import com.ssm.ezbiz.dao.RobEBranchDao;
import com.ssm.ezbiz.service.RobEBranchService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobEBranch;
import com.ssm.llp.ezbiz.model.RobEBranchTransaction;

@Service
public class RobEBranchServiceImpl extends BaseServiceImpl<RobEBranch,  Long> implements RobEBranchService{
	@Autowired 
	RobEBranchDao robEBranchDao;

	@Override
	public BaseDao getDefaultDao() {
		return robEBranchDao;
	}

	@Override
	public List<RobEBranch> findActiveByBrNo(String brNo) {
		return robEBranchDao.findActiveByBrNo(brNo);
	}

}
