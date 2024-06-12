package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.ezbiz.comtrac.SearchComtracList.SearchComtracModel;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

public interface RobTrainingTransactionService extends BaseService<RobTrainingTransaction, Integer>{

	public void updateInsertAll(RobTrainingTransaction robTrainingTransaction);
	
	public RobTrainingTransaction findByTransactionCode(String transactionCode);
	
	public RobTrainingTransaction findByTransactionCodeWithParticipant(String transactionCode);

	void sendEmailConfirmation(String transactionCode);
	
	void sendEmailLpoInProcess(String transactionCode);
	
	void sendEmailLpoApproved(String transactionCode);
	
	void sendEmailLpoRejected(String transactionCode);
	
	public Integer countParticipantByStatusAndTrainingId(Integer trainingId, String[] status);
	
	public List<RobTrainingTransaction> searchComtractTransactions(SearchComtracModel searchComtracModel);
}
