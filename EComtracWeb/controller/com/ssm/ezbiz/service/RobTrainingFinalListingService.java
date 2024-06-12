package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListing;

public interface RobTrainingFinalListingService extends BaseService<RobTrainingFinalListing, Integer> {

	public RobTrainingFinalListing updateInsertAll(RobTrainingFinalListing robTrainingFinalListing);

	public RobTrainingFinalListing findByFinalListingRefNo(String findByFinalListingRefNo);

	public RobTrainingFinalListing findByTrainingCode(String trainingCode);

	void sendEmailRefundComtrac(String transactionCode);
	
	void sendEmailListingApprove(String trainingCode);
}