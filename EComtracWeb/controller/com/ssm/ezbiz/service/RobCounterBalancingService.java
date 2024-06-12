package com.ssm.ezbiz.service;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobCounterBalancing;
import com.ssm.llp.ezbiz.model.RobCounterSession;


public interface RobCounterBalancingService extends BaseService<RobCounterBalancing, Integer> {

	void generateBalancingSlip(List<RobCounterBalancing> listBalancing, RobCounterSession robCounterSession);
}

