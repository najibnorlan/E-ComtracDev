package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;

public interface RobTrainingConfigDao extends BaseDao<RobTrainingConfig, Integer>{
	
	public List<RobTrainingConfig> getAvailableTraining();

	public List<RobTrainingConfig> getAvailableTrainingCorp();
	
	public Integer totalPax(String type, String year, String month);
}
