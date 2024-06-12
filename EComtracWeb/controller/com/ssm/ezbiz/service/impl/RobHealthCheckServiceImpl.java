package com.ssm.ezbiz.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobHealthCheckDao;
import com.ssm.ezbiz.service.RobHealthCheckService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobHealthCheck;

@Service
public class RobHealthCheckServiceImpl extends BaseServiceImpl<RobHealthCheck, Long> implements RobHealthCheckService {

	@Autowired
	private RobHealthCheckDao robHealthCheckDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robHealthCheckDao;
	}
	
	@Override
	public List<RobHealthCheck> findByItemType(String type) {
		
		SearchCriteria sc = new SearchCriteria("itemType", SearchCriteria.EQUAL, type);
		sc.addOrderBy("code", SearchCriteria.ASC);
		List<RobHealthCheck> listHealth = findByCriteria(sc).getList();
		
		return listHealth;
	}
	
	@Override
	public RobHealthCheck findbyCode(String code) {
		return robHealthCheckDao.findbyCode(code);
	}

//	@Override
//	public RobHealthCheck findbyMethodName(String methodName) {
//		
//		RobHealthCheck healthCheck = null;
//		SearchCriteria sc = new SearchCriteria("methodName", SearchCriteria.EQUAL, methodName);
//		List<RobHealthCheck> listHealth = findByCriteria(sc).getList();
//		
//		if(listHealth.size() > 0){
//			healthCheck = listHealth.get(0);
//		}else{
//			String[] splitCode = methodName.split("(?=\\p{Lu})");
//			String code = "";
//			Integer index = 0;
//			for(String s : splitCode){
//				if(index != 0){
//					 code += "_";
//				}
//				code += StringUtils.upperCase(s);
//				index++;
//			}
//			healthCheck = new RobHealthCheck();
//			healthCheck.setMethodName(methodName);
//			healthCheck.setItemType(Parameter.SCHEDULER_ITEM_TYPE_scheduler);
//			healthCheck.setStatus(Parameter.HEALTH_CHECK_ok);
//			healthCheck.setCode(code);
//			healthCheck.setTime(1);
//			
//			insert(healthCheck);
//		}
//		return healthCheck;
//	}

	@Override
	public void updateHealthCheckStatus(String methodName, String healthCheckStatus) {
		
		RobHealthCheck healthCheck = null;
		SearchCriteria sc = new SearchCriteria("methodName", SearchCriteria.EQUAL, methodName);
		List<RobHealthCheck> listHealth = findByCriteria(sc).getList();
		
		if(listHealth.size() > 0){
			healthCheck = listHealth.get(0);
			healthCheck.setStatus(healthCheckStatus);
			update(healthCheck);
		}else{
			
			String str = "";
			for (int i = 0; i < methodName.length(); i++) {
				if(i==0){
					str += methodName.charAt(i);
					continue;
				}
				
				if(StringUtils.isAllUpperCase(methodName.charAt(i)+"")){
					str += " ";
				}
				str += methodName.charAt(i);
				
			}
			String code = str.toUpperCase();
			
			healthCheck = new RobHealthCheck();
			healthCheck.setMethodName(methodName);
			healthCheck.setItemType(Parameter.SCHEDULER_ITEM_TYPE_scheduler);
			healthCheck.setStatus(healthCheckStatus);
			healthCheck.setCode(code);
			healthCheck.setTime(1);
			
			insert(healthCheck);
		}
	}
	
}
