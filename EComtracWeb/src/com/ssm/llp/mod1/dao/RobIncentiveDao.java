package com.ssm.llp.mod1.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.RobIncentive;


public interface RobIncentiveDao  extends BaseDao<RobIncentive, String> {
	
	RobIncentive getLastIncentiveApprovedDate(String newIc, String incentiveType);
	
	List<RobIncentive> findListIncentiveByRobFormCode(String robFormCode);
	
	String checkLastApplicationStatus(String newIc, String incentiveType, String formType);
}

