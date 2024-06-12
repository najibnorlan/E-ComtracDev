package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.ezbiz.comtrac.SearchComtracList.SearchComtracModel;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

public interface RobTrainingTransactionDao extends BaseDao<RobTrainingTransaction, Integer>{

	RobTrainingTransaction findByTransactionCodeWithParticipant(String transactionCode);
	
	public List<RobTrainingTransaction> searchComtractTransactions(SearchComtracModel searchComtracModel);

}
