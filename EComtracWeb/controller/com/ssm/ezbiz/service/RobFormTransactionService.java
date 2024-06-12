package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.ezbiz.model.RobFormTransactionModel;

public interface RobFormTransactionService {

	List<RobFormTransactionModel> findLatestTransactionByLoginId(String loginName);

	List<RobFormTransactionModel> findPendingTransaction(String brNo);
	
}
