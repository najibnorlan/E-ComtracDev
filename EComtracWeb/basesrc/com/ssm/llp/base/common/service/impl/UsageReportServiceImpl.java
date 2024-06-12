package com.ssm.llp.base.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.UsageReportDao;
import com.ssm.llp.base.common.service.UsageReportService;
import com.ssm.llp.mod1.model.BranchSummaryReportModel;

@Service
public class UsageReportServiceImpl implements UsageReportService{

	@Autowired
	UsageReportDao usageReportDao;
	
	@Override
	public Integer countRobByCriteria(String month, String bizType, String formType, String status, String year){
		return usageReportDao.countRobByCriteria(month, bizType, formType, status, year);
	}
	
	@Override
	public BranchSummaryReportModel findBranchSummaryReportData(String paymentCode, String branch, Date dateFrom, Date dateTo) {
		return usageReportDao.findBranchSummaryReportData(paymentCode, branch, dateFrom, dateTo);
	}

	@Override
	public List<Object> getMonthYearCreateDt() {		
		return usageReportDao.getMonthYearCreateDt();
	}
}
