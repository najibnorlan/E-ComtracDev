package com.ssm.ezbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobEBranchTransactionDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobEBranchTransactionService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobEBranchTransaction;

@Service
public class RobEBranchTransactionServiceImpl extends BaseServiceImpl<RobEBranchTransaction, Long> implements RobEBranchTransactionService, PaymentInterface {

	@Autowired
	RobEBranchTransactionDao robEBranchTransactionDao;

	@Override
	public BaseDao getDefaultDao() {
		return robEBranchTransactionDao;
	}

	@Override
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		
	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		
	}

	@Override
	public RobEBranchTransaction findActiveByBrNo(String brNo) {
		return robEBranchTransactionDao.findActiveByBrNo(brNo);
	}

}