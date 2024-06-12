package com.ssm.ezbiz.dao;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobHealthCheck;

public interface RobHealthCheckDao extends BaseDao<RobHealthCheck, Long> {
	
	public RobHealthCheck findbyCode(String code);
	
}
