package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobNameTypeStatReportDao;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobNameTypeStatReportService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.UsageReportService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormAStatReport;
import com.ssm.llp.ezbiz.model.RobNameTypeStatReport;

@Service
public class RobNameTypeStatReportServiceImpl extends BaseServiceImpl<RobNameTypeStatReport, Integer> implements RobNameTypeStatReportService{

	@Autowired
	RobNameTypeStatReportDao robNameTypeStatReportDao;
	
	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	UsageReportService usageReportService;
	
	@Override
	public Integer getStatisticValue(String year, String month, String status, String type, Boolean updateData) {
		
		//plus 1 as Calendar.MONTH return JAN as 0
		Integer curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		Integer curYear = Calendar.getInstance().get(Calendar.YEAR);
		Integer yearInt = Integer.parseInt(year);
		Integer monthInt = Integer.parseInt(month);
		
		if(updateData){
			if(yearInt.equals(curYear) && monthInt.equals(curMonth)){
				updateStatistic(year, month);
			}
		}
		
		Integer result = robNameTypeStatReportDao.getValue(year, month, status, type);
		if(result != null){
			return result;
		}else{
			generateStatistic(year, month, status, type);
			return getStatisticValue(year, month, status, type, false);
		}
	}
	
	private void generateStatistic(String year, String month, String status, String type) {
		
		List<LlpParameters> robFormAStatus = llpParametersService.findByActiveCodeType(Parameter.ROB_FORM_A_STATUS);
		List<LlpParameters> nameList = llpParametersService.findByActiveCodeType(Parameter.ROB_NAME_TYPE);
		
		for(LlpParameters i : robFormAStatus){
			RobNameTypeStatReport statReport = new RobNameTypeStatReport();
			statReport.setStatMonth(month);
			statReport.setStatYear(year);
			for(LlpParameters j : nameList){
				Integer result = usageReportService.countRobByCriteria(month, j.getCode(), null, i.getCode(), year);
				if(Parameter.ROB_NAME_TYPE_PERSONAL.equals(j.getCode())){
					statReport.setPersonalName(result);
				}else if(Parameter.ROB_NAME_TYPE_TRADE.equals(j.getCode())){
					statReport.setTradeName(result);
				}
			}
			statReport.setStatus(i.getCode());
			insert(statReport);
		}
		
		return;
	}

	private void updateStatistic(String year, String month) {
		
		List<LlpParameters> nameList = llpParametersService.findByActiveCodeType(Parameter.ROB_NAME_TYPE);
		List<RobNameTypeStatReport> statReports = getByMonthAndYear(year, month);
		
		for(RobNameTypeStatReport i : statReports){
			for(LlpParameters j : nameList){
				Integer result = usageReportService.countRobByCriteria(month, j.getCode(), null, i.getStatus(), year);
				if(Parameter.ROB_NAME_TYPE_PERSONAL.equals(j.getCode())){
					i.setPersonalName(result);
				}else if(Parameter.ROB_NAME_TYPE_TRADE.equals(j.getCode())){
					i.setTradeName(result);
				}
			}
			update(i);
		}
		
	}
	
	private List<RobNameTypeStatReport> getByMonthAndYear(String year, String month){
		
		SearchCriteria sc = new SearchCriteria("statYear", SearchCriteria.EQUAL, year);
		sc = sc.andIfNotNull("statMonth", SearchCriteria.EQUAL, month);
		
		List<RobNameTypeStatReport> list = findByCriteria(sc).getList();
		
		return list;
	}
	
	@Override
	public BaseDao getDefaultDao() {
		return robNameTypeStatReportDao;
	}

}
