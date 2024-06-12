package com.ssm.ezbiz.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobTrainingDashboardDao;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingDashboardService;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobTrainingDashboard;


@Service
public class RobTrainingDashboardServiceImpl extends BaseServiceImpl<RobTrainingDashboard, Integer>
		implements RobTrainingDashboardService{
	@Autowired
	RobTrainingDashboardDao robTrainingDashboardDao;
	
	@Autowired
	RobTrainingConfigService robTrainingConfigService;
	
	@Autowired
	RobTrainingParticipantService robTrainingParticipantService;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return robTrainingDashboardDao;
	}
	
	@Override
	public RobTrainingDashboard findDashboardData(String type, int year) {
		
		SearchCriteria sc = new SearchCriteria("type", SearchCriteria.EQUAL, type);
		               sc = sc.andIfNotNull("year", SearchCriteria.EQUAL, year);
		List<RobTrainingDashboard> transactions = findByCriteria(sc).getList();
		if (transactions.size() > 0) {
			return transactions.get(0);
		}

		return null;
	}
	
	@Override
	public List<RobTrainingDashboard> findListDashboardData(Integer[] year) {
		List<RobTrainingDashboard> list = new ArrayList<RobTrainingDashboard>();
		
		SearchCriteria sc = new SearchCriteria("year", SearchCriteria.IN);
		               sc.setValues(year);
	    
	    return findByCriteria(sc).getList();
	}
	
	@Override
	public RobTrainingDashboard insertData(String type, String year) {
		int intyears = Integer.parseInt(year);
		Date currDate = new Date();
		
		RobTrainingDashboard robTrainingDashboard = new RobTrainingDashboard();
		
		robTrainingDashboard.setParam1(0);
		robTrainingDashboard.setParam2(0);
		robTrainingDashboard.setParam3(0);
		robTrainingDashboard.setParam4(0);
		robTrainingDashboard.setParam5(0);
		robTrainingDashboard.setParam6(0);
		robTrainingDashboard.setParam7(0);
		robTrainingDashboard.setParam8(0);
		robTrainingDashboard.setParam9(0);
		robTrainingDashboard.setParam10(0);
		robTrainingDashboard.setParam11(0);
		robTrainingDashboard.setParam12(0);
		robTrainingDashboard.setType(type);
		robTrainingDashboard.setStatus("D");
		robTrainingDashboard.setYear(intyears);
		robTrainingDashboard.setTransDt(currDate);
		insert(robTrainingDashboard);
		
		return robTrainingDashboard;

	}
	
	@Override
	public RobTrainingDashboard updateData() {
		
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
		SimpleDateFormat formatDate1 = new SimpleDateFormat("MM");
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		int curryear = calendar.get(Calendar.YEAR);
		calendar.setTime(calendar.getTime());
		Date startDate = null;
		Date endDate = null;
		
		RobTrainingDashboard robTrainingDashboard = new RobTrainingDashboard();
		
		String[] listType = new String[]{"PAST_PROGRAM","FUTURE_PROGRAM","PAX", "TOTAL_PAX","REVENUE"}; 
		Integer[] listYear = new Integer[]{curryear,curryear-1};
		
		for (int j = 0; j < listYear.length; j++) {

		for (int i = 0; i < listType.length; i++) {

				robTrainingDashboard = checkData(listType[i], listYear[j]);
				
				if(robTrainingDashboard == null) {
					robTrainingDashboard = insertData(listType[i], Integer.toString(listYear[j]));
				}
//				System.out.println("robTrainingDashboard = "+robTrainingDashboard.getStatus()+"robTrainingDashboard = "+robTrainingDashboard.getYear());
				if((robTrainingDashboard.getYear() == curryear) || ("D".equals(robTrainingDashboard.getStatus()) && robTrainingDashboard.getYear() == curryear-1)) {
					for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
						
						calendar.set(Calendar.MONTH, month);
			            calendar.set(Calendar.YEAR, listYear[j]);
			            
			            String year = Integer.toString(listYear[j]);
			            String months = formatDate1.format(calendar.getTime());
			            
						robTrainingDashboard = populateData(robTrainingDashboard,listType[i],year,months);
						update(robTrainingDashboard);
					
					}
				}
			
		}
		
		}
		
		return robTrainingDashboard;


	}
	
	
	@Override
	public RobTrainingDashboard checkData(String type, int year) {
						
		SearchCriteria sc = new SearchCriteria("type", SearchCriteria.EQUAL, type);
        sc = sc.andIfNotNull("year", SearchCriteria.EQUAL, year);
		List<RobTrainingDashboard> transactions = findByCriteria(sc).getList();
		if (transactions.size() > 0) {
		return transactions.get(0);
		}

		return null;

	}
	
	
	@Override
	public RobTrainingDashboard populateData(RobTrainingDashboard robTrainingDashboard, String type, String year, String month) {
		
		
		double dtotalSum = 0;
		int ttotalSum = 0;
		int totalSum = 0;
		
		if("PAST_PROGRAM".equals(type) || "FUTURE_PROGRAM".equals(type) || "PAX".equals(type) || "TOTAL_PAX".equals(type)) {
			
			ttotalSum = robTrainingConfigService.totalPax(type, year, month);			
			totalSum = (int) ttotalSum;
			
		}else if("REVENUE".equals(type)) {
			
			dtotalSum = robTrainingParticipantService.totalRevenue(type, year, month);
			totalSum = (int) dtotalSum;
			
		}
		
//		System.out.println("year ="+year+"month ="+month+"Month ="+month+" Type ="+type+" totalSum ="+totalSum+" ttotalSum ="+ttotalSum);
		if("01".equals(month)) {
			robTrainingDashboard.setParam1(totalSum);
		}else if("02".equals(month)) {
			robTrainingDashboard.setParam2(totalSum);
		}else if("03".equals(month)) {
			robTrainingDashboard.setParam3(totalSum);
		}else if("04".equals(month)) {
			robTrainingDashboard.setParam4(totalSum);
		}else if("05".equals(month)) {
			robTrainingDashboard.setParam5(totalSum);
		}else if("06".equals(month)) {
			robTrainingDashboard.setParam6(totalSum);
		}else if("07".equals(month)) {
			robTrainingDashboard.setParam7(totalSum);
		}else if("08".equals(month)) {
			robTrainingDashboard.setParam8(totalSum);
		}else if("09".equals(month)) {
			robTrainingDashboard.setParam9(totalSum);
		}else if("10".equals(month)) {
			robTrainingDashboard.setParam10(totalSum);
		}else if("11".equals(month)) {
			robTrainingDashboard.setParam11(totalSum);
		}else if("12".equals(month)) {
			robTrainingDashboard.setParam12(totalSum);
		}
		robTrainingDashboard.setStatus("A");
		
		return robTrainingDashboard;


	}
	
}
