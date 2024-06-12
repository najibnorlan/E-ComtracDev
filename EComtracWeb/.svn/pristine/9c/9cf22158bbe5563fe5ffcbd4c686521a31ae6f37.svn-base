package com.ssm.ezbiz.dao;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormA;

public interface RobFormADao  extends BaseDao<RobFormA, String> {

	RobFormA findByIdWithData(String robFormACode);

	public Integer countFormAByTypeAndStatus(String type, String status, String year, String month);


	void cancelPendingPaymentMoreThanXDays(Integer noOfDays);

	void cancelDraftMoreThanXDays(Integer noOfDays);

	void updateBaseListDistribution(String[] listUpdatedTrans);
	
//	public RobFormA findAllById(String robFormARefNo);

}

