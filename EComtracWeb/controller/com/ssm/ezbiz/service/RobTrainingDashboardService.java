package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingDashboard;

public interface RobTrainingDashboardService extends BaseService<RobTrainingDashboard, Integer>  {
	
	public RobTrainingDashboard insertData(String type, String year);
	
	public RobTrainingDashboard updateData();
	
	public RobTrainingDashboard checkData(String type, int year);
	
	public RobTrainingDashboard populateData(RobTrainingDashboard robTrainingDashboard,String type, String year, String month);
	
	public RobTrainingDashboard findDashboardData(String type, int year);
	
	public List<RobTrainingDashboard> findListDashboardData(Integer[] year);
//	
}
