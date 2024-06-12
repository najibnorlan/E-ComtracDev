package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormTransactionDao;
import com.ssm.ezbiz.service.RobFormTransactionService;
import com.ssm.llp.ezbiz.model.RobFormTransactionModel;

@Service
public class RobFormTransactionServiceImpl implements RobFormTransactionService {
	
	@Autowired
	RobFormTransactionDao robFormTransactionDao;

	@Override
	public List<RobFormTransactionModel> findLatestTransactionByLoginId(String loginName) {
		return robFormTransactionDao.findLatestTransactionByLoginId(loginName);
	}

	@Override
	public List<RobFormTransactionModel> findPendingTransaction(String brNo) {
		return robFormTransactionDao.findPendingTransaction(brNo);
	}

}
