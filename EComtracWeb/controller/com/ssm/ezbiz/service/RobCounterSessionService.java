package com.ssm.ezbiz.service;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobCounterSession;

public interface RobCounterSessionService extends BaseService<RobCounterSession, Integer>{
	public RobCounterSession findByIpAddressStatusNDE(String ipAddress);
	
	public RobCounterSession findByUserIdIpAddressOpen(String userId, String ipAddress);

	public SearchCriteria searchCriteriaTemplate(String branch, Date checkoutDate, String floor, String status);

	public Double getTotalAmountByCounterList(List<RobCounterSession> counterList);
	
	public List<Object> getBranchAndFloorNoBankSlip(String branchCode, String floor, String date, String isApprove);
}
