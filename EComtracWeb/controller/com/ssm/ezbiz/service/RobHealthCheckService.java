package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobHealthCheck;

public interface RobHealthCheckService extends BaseService<RobHealthCheck, Long> {

	public List<RobHealthCheck> findByItemType(String type);
	
	public RobHealthCheck findbyCode(String code);
	
//	public RobHealthCheck findbyMethodName(String methodName);

	public void updateHealthCheckStatus(String methodName, String healthCheckStatus);
	
}
