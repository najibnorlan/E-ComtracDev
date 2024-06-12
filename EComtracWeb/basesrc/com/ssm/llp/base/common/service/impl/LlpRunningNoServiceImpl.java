package com.ssm.llp.base.common.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpRunningNoDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpRunningNo;
import com.ssm.llp.base.common.service.LlpRunningNoService;

@Service
public class LlpRunningNoServiceImpl extends BaseServiceImpl<LlpRunningNo, Long> implements LlpRunningNoService{

	@Autowired
	LlpRunningNoDao llpRunningNoDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return llpRunningNoDao;
	}

	@Override
	public synchronized String generateRunningNo(String genCode, String fieldCode,
			String moduleCode, String yearMonthDay, String lastNoPattern, Date date) {

		List<LlpRunningNo> list = new ArrayList<LlpRunningNo>();
		String todayStr = "";
		NumberFormat noFormatter = new DecimalFormat(lastNoPattern);
		
		SearchCriteria sc = new SearchCriteria("genCode", SearchCriteria.EQUAL, genCode);
		if(fieldCode != null){
			sc = sc.andIfNotNull("fieldCode", SearchCriteria.EQUAL, fieldCode);
		}else{
			fieldCode = "";
		}
		
		if(moduleCode != null){
			sc = sc.andIfNotNull("moduleCode", SearchCriteria.EQUAL, moduleCode);
		}else{
			moduleCode = "";
		}
		
		if(yearMonthDay != null){
			SimpleDateFormat sdf = new SimpleDateFormat(yearMonthDay);
			todayStr = sdf.format(date);
			sc = sc.andIfNotNull("yearMonthDay", SearchCriteria.EQUAL, todayStr);
		}
		
		LlpRunningNo llpRunningNo = new LlpRunningNo();
		list = findByCriteria(sc).getList();
		
		String lastNoStr="";
		int lastNo = 1;
		
		if(list.size()>0){
			llpRunningNo = list.get(0); 
			lastNo = Integer.valueOf(llpRunningNo.getLastNo());
			lastNo++;
			lastNoStr = noFormatter.format(lastNo);
			llpRunningNo.setLastNo(lastNoStr);
			update(llpRunningNo);
		}else{
			llpRunningNo.setGenCode(genCode);
			llpRunningNo.setFieldCode(fieldCode);
			llpRunningNo.setModuleCode(moduleCode);
			llpRunningNo.setYearMonthDay(todayStr);
			lastNoStr = noFormatter.format(lastNo);
			llpRunningNo.setLastNo(lastNoStr);
			insert(llpRunningNo);
		}

		return fieldCode+moduleCode+todayStr+lastNoStr;
	}
	
}
