package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobCounterSession;

public interface RobCounterBankinSlipService extends BaseService<RobCounterBankinSlip, String>{
	public String generateBankinSlip(ListCollectionVerificationModel listCollectionVerificationModel);

	public RobCounterBankinSlip findByBankinSlipNo(String slipNo);
	
	public byte[] generateTextFile(SearchCriteria sc) throws SSMException;

}
