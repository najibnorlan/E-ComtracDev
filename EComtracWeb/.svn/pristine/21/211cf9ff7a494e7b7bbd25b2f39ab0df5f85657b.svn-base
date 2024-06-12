package com.ssm.llp.mod1.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.RobIncentive;



public interface RobIncentiveService extends BaseService<RobIncentive, String> {

	public RobIncentive getLastIncentiveByNewIcNo(String newIc, String incentiveType); //approved only
	
	public List<RobIncentive> findListIncentiveByRobFormCode(String robFormCode); //scheduler
	
	public String checkLastApplicationStatus(String newIc, String incentiveType, String formType); //all status by form
}
