package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.ezbiz.dao.RobCounterBalancingDao;
import com.ssm.ezbiz.service.RobCounterBalancingService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobCounterBalancing;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@Service
public class RobCounterBalancingServiceImpl extends BaseServiceImpl<RobCounterBalancing, Integer> implements RobCounterBalancingService{

	@Autowired
	RobCounterBalancingDao robCounterBalancingDao;
	
	@Autowired
	RobCounterSessionService robCounterSessionService;
	
	@Override
	public BaseDao getDefaultDao() {
		return robCounterBalancingDao;
	}

	@Transactional
	@Override
	public void generateBalancingSlip(List<RobCounterBalancing> listBalancing, RobCounterSession robCounterSession) {
		
		for (Iterator iterator = listBalancing.iterator(); iterator.hasNext();) {
			RobCounterBalancing robCounterBalancing = (RobCounterBalancing) iterator.next();
			robCounterBalancingDao.insert(robCounterBalancing);
		}
		
		robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_data_entry);
		robCounterSession.setBalancingBy(UserEnvironmentHelper.getLoginName());
		robCounterSession.setBalancingDt(new Date());
		robCounterSessionService.update(robCounterSession);
	}
	
}
