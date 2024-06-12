package com.ssm.llp.mod1.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.mod1.dao.RobIncentiveDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.service.RobIncentiveService;

@Service
public class RobIncentiveServiceImpl extends BaseServiceImpl<RobIncentive, String> implements RobIncentiveService {
	
	@Autowired
	RobIncentiveDao robIncentiveDao;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return robIncentiveDao;
	}


	@Override
	public RobIncentive getLastIncentiveByNewIcNo(String newIc, String incentiveType){
		// TODO Auto-generated method stub

		RobIncentive robIncentive = robIncentiveDao.getLastIncentiveApprovedDate(newIc, incentiveType);
		return robIncentive;
	}
	
	@Override
	public List<RobIncentive> findListIncentiveByRobFormCode(String robFormCode){
		return robIncentiveDao.findListIncentiveByRobFormCode(robFormCode);
	}
	
	@Override
	public String checkLastApplicationStatus(String newIc, String incentiveType, String formType) {
		return robIncentiveDao.checkLastApplicationStatus(newIc, incentiveType, formType);
	}
	
 }
	