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

import com.ssm.ezbiz.dao.RobEBranchTransactionDetailDao;
import com.ssm.ezbiz.service.RobEBranchTransactionDetailService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobEBranchTransactionDetail;

@Service
public class RobEBranchTransactionDetailServiceImpl extends BaseServiceImpl<RobEBranchTransactionDetail,  Long> implements RobEBranchTransactionDetailService{
	@Autowired 
	RobEBranchTransactionDetailDao robEbranchTransactionDetailDao;

	@Override
	public BaseDao getDefaultDao() {
		return robEbranchTransactionDetailDao;
	}
}
