package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobEBranchTransaction;

public interface RobEBranchTransactionService extends BaseService<RobEBranchTransaction, Long>{

	RobEBranchTransaction findActiveByBrNo(String brNo);
}
