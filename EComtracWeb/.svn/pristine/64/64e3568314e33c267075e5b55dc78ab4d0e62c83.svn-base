package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormTypeStatReportDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormTypeStatReport;

@Repository
public class RobFormTypeStatReportDaoImpl extends BaseDaoImpl<RobFormTypeStatReport, Integer> implements RobFormTypeStatReportDao {

	@Override
	public Integer getValue(String year, String month, String status, String type) {
		String findType = "";
		if(Parameter.ROB_FORM_TYPE_A.equals(type)){
			findType = "formA";
		}else if(Parameter.ROB_FORM_TYPE_RENEWAL.equals(type)){
			findType = "formA1";
		}else if(Parameter.ROB_FORM_TYPE_B.equals(type)){
			findType = "formB";
		}else if(Parameter.ROB_FORM_TYPE_C.equals(type)){
			findType = "formC";
		}else if("CMP".equals(type)){
			findType = "compound";
		}else{
			return null;
		}
		
		ArrayList<String> param = new ArrayList<String>();
		
		String hql = "select sum(" + findType + ") " +
				" from " + RobFormTypeStatReport.class.getName() +
				" where statYear=? and statMonth=?";
				param.add(year);
				param.add(month);
		
		if(!status.equalsIgnoreCase("all")){
			hql += " and status=?";
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
