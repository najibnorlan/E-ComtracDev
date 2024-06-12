package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormTypeStatReportDao;
import com.ssm.ezbiz.service.RobFormTypeStatReportService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.UsageReportService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormTypeStatReport;

@Service
public class RobFormTypeStatReportServiceImpl extends BaseServiceImpl<RobFormTypeStatReport, Integer> implements RobFormTypeStatReportService{

	@Autowired
	RobFormTypeStatReportDao robFormTypeStatReportDao;
	
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
		
		Integer result = robFormTypeStatReportDao.getValue(year, month, status, type);
		if(result != null){
			return result;
		}else{
			generateStatistic(year, month, status, type);
			return getStatisticValue(year, month, status, type, false);
		}
	}
	
	private void generateStatistic(String year, String month, String status, String type) {
		
		List<String> robFormAStatus = getStatusDropdownList();
		List<LlpParameters> formList =llpParametersService.getFormTypeWithCompound();
		
		for(String i : robFormAStatus){
			RobFormTypeStatReport statReport = new RobFormTypeStatReport();
			statReport.setStatMonth(month);
			statReport.setStatYear(year);
			for(LlpParameters j : formList){
				Integer result = usageReportService.countRobByCriteria(month, null, j.getCode(), i, year);
				if(Parameter.ROB_FORM_TYPE_A.equals(j.getCode())){
					statReport.setFormA(result);
				}else if(Parameter.ROB_FORM_TYPE_RENEWAL.equals(j.getCode())){
					statReport.setFormA1(result);
				}else if(Parameter.ROB_FORM_TYPE_B.equals(j.getCode())){
					statReport.setFormB(result);
				}else if(Parameter.ROB_FORM_TYPE_C.equals(j.getCode())){
					statReport.setFormC(result);
				}else if("CMP".equals(j.getCode())){
					statReport.setCompound(result);
				}
			}
			statReport.setStatus(i);
			insert(statReport);
		}
		
		return;
	}

	private void updateStatistic(String year, String month) {
		
		List<LlpParameters> formList =llpParametersService.getFormTypeWithCompound();
		List<RobFormTypeStatReport> statReports = getByMonthAndYear(year, month);
		
		for(RobFormTypeStatReport i : statReports){
			for(LlpParameters j : formList){
				Integer result = usageReportService.countRobByCriteria(month, null, j.getCode(), i.getStatus(), year);
				if(Parameter.ROB_FORM_TYPE_A.equals(j.getCode())){
					i.setFormA(result);
				}else if(Parameter.ROB_FORM_TYPE_RENEWAL.equals(j.getCode())){
					i.setFormA1(result);
				}else if(Parameter.ROB_FORM_TYPE_B.equals(j.getCode())){
					i.setFormB(result);
				}else if(Parameter.ROB_FORM_TYPE_C.equals(j.getCode())){
					i.setFormC(result);
				}else if("CMP".equals(j.getCode())){
					i.setCompound(result);
				}
			}
			update(i);
		}
		
	}
	
	private List<RobFormTypeStatReport> getByMonthAndYear(String year, String month){
		
		SearchCriteria sc = new SearchCriteria("statYear", SearchCriteria.EQUAL, year);
		sc = sc.andIfNotNull("statMonth", SearchCriteria.EQUAL, month);
		
		List<RobFormTypeStatReport> list = findByCriteria(sc).getList();
		
		return list;
	}
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormTypeStatReportDao;
	}

	@Override
	public List<String> getStatusDropdownList() {
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY);
		list.add(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT);
		list.add(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
		list.add(Parameter.ROB_FORM_A_STATUS_IN_PROCESS);
		list.add(Parameter.ROB_FORM_A_STATUS_APPROVED);
		list.add(Parameter.ROB_FORM_A_STATUS_REJECT);
		list.add(Parameter.ROB_FORM_A_STATUS_QUERY);
		list.add(Parameter.ROB_FORM_A_STATUS_CANCEL);
		
		return list;
	}

	
}
