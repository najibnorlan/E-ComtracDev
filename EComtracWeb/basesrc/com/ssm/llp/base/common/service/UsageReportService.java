package com.ssm.llp.base.common.service;

import java.util.Date;
import java.util.List;

import com.ssm.llp.mod1.model.BranchSummaryReportModel;

public interface UsageReportService {

	public Integer countRobByCriteria(String month, String bizType, String formType, String status, String year);
	
	public BranchSummaryReportModel findBranchSummaryReportData(String dataType, String branch, Date dateFrom, Date dateTo);
	
	public List<Object> getMonthYearCreateDt();
}
