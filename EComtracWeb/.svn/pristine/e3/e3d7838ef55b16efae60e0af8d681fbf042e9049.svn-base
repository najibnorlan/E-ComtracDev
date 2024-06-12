package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormAStatisticDao;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormAStatisticService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormAStatReport;

@Service
public class RobFormAStatisticServiceImpl extends BaseServiceImpl<RobFormAStatReport, Integer> implements RobFormAStatisticService{

	@Autowired
	RobFormAStatisticDao robFormAStatisticDao; 
	
	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	RobFormAService robFormAService;
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormAStatisticDao;
	}

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
		
		Integer result = robFormAStatisticDao.getValue(year, month, status, type);
		if(result != null){
			return result;
		}else{
			generateStatistic(year, month, status, type);
			return getStatisticValue(year, month, status, type, false);
		}
	}

	private void generateStatistic(String year, String month, String status, String type) {
		
		List<String> list = new ArrayList<String>();
		list.add("ONLINE SELLER");
		list.add("INCUBATOR");
		list.add("STUDENT");
		list.add("OKU");
		
		List<LlpParameters> robFormAStatus = llpParametersService.findByActiveCodeType(Parameter.ROB_FORM_A_STATUS);
		
		
		for(LlpParameters i : robFormAStatus){
			RobFormAStatReport statReport = new RobFormAStatReport();
			statReport.setStatMonth(month);
			statReport.setStatYear(year);
			for(String j : list){
				Integer result = robFormAService.countFormAByTypeAndStatus(j, i.getCode(), year, month);
				if(j.equalsIgnoreCase("ONLINE SELLER")){
					statReport.setOnlineSeller(result);
				}else if(j.equalsIgnoreCase("INCUBATOR")){
					statReport.setIncubator(result);
				}else if(j.equalsIgnoreCase("STUDENT")){
					statReport.setStudent(result);
				}else if(j.equalsIgnoreCase("OKU")){
					statReport.setOku(result);
				}
			}
			statReport.setFormAStatus(i.getCode());
			statReport.setStatTotal(0);
			insert(statReport);
		}
		
		return;
	}

	private void updateStatistic(String year, String month) {
		
		List<String> list = new ArrayList<String>();
		list.add("ONLINE SELLER");
		list.add("INCUBATOR");
		list.add("STUDENT");
		list.add("OKU");
		
		List<RobFormAStatReport> statReports = getByMonthAndYear(year, month);
		
		for(RobFormAStatReport i : statReports){
			for(String j : list){
				Integer result = robFormAService.countFormAByTypeAndStatus(j, i.getFormAStatus(), year, month);
				if(j.equalsIgnoreCase("ONLINE SELLER")){
					i.setOnlineSeller(result);
				}else if(j.equalsIgnoreCase("INCUBATOR")){
					i.setIncubator(result);
				}else if(j.equalsIgnoreCase("STUDENT")){
					i.setStudent(result);
				}else if(j.equalsIgnoreCase("OKU")){
					i.setOku(result);
				}
			}
			update(i);
		}
		
	}
	
	private List<RobFormAStatReport> getByMonthAndYear(String year, String month){
		
		SearchCriteria sc = new SearchCriteria("statYear", SearchCriteria.EQUAL, year);
		sc = sc.andIfNotNull("statMonth", SearchCriteria.EQUAL, month);
		
		List<RobFormAStatReport> list = findByCriteria(sc).getList();
		
		return list;
	}

}