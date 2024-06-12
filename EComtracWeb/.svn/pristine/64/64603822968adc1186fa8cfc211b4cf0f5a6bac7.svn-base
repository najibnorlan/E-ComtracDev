package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;

public interface RobTrainingConfigService extends BaseService<RobTrainingConfig, Integer> {
	
	public List<RobTrainingConfig> getAvailableTraining();
	
	public List<RobTrainingConfig> getAvailableTrainingCorp();
	
	public Integer totalPax(String type, String year, String month);
	
	@Override
	public void update(RobTrainingConfig entity);

	public RobTrainingConfig findByTrainingCode(String string);
}
