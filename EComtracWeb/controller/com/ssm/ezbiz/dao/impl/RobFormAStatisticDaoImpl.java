package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormAStatisticDao;
import com.ssm.ezbiz.service.RobFormAStatisticService;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormAStatReport;

@Repository
public class RobFormAStatisticDaoImpl extends BaseDaoImpl<RobFormAStatReport, Integer> implements RobFormAStatisticDao{

	@Override
	public Integer getValue(String year, String month, String status, String type) {
		
		String findType = "";
		if(type.equalsIgnoreCase("ONLINE SELLER")){
			findType = "onlineSeller";
		}else if(type.equalsIgnoreCase("INCUBATOR")){
			findType = "incubator";
		}else if(type.equalsIgnoreCase("STUDENT")){
			findType = "student";
		}else if(type.equalsIgnoreCase("OKU")){
			findType = "oku";
		}else{
			return null;
		}
		
		ArrayList<String> param = new ArrayList<String>();
		
		String hql = "select sum(" + findType + ") " +
				" from " + RobFormAStatReport.class.getName() +
				" where statYear=? and statMonth=?";
				param.add(year);
				param.add(month);
		
		if(!status.equalsIgnoreCase("all")){
			hql += " and formAStatus=?";
			param.add(status);
		}
		
			
		String[] paramArray = new String[ param.size() ];
		param.toArray( paramArray );
			
		List<Long> result = getHibernateTemplate().find(hql, paramArray);
		if (result.size() > 0) {
			if (result.get(0) != null) {
				return result.get(0).intValue();
			}
		}
		
		return null;
	}

}
