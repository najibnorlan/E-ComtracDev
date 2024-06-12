package com.ssm.ezbiz.dao;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.mod1.model.BranchSummaryReportModel;

public interface UsageReportDao extends BaseDao<UsageReportDao, String>{

	Integer countRobByCriteria(String month, String bizType, String formType, String status, String year);
	
	BranchSummaryReportModel findBranchSummaryReportData(String paymentCode, String branch, Date dateFrom, Date dateTo);

	List<Object> getMonthYearCreateDt();

}
