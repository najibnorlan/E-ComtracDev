package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobHealthCheckDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobHealthCheck;

@Repository
public class RobHealthCheckDaoImpl extends BaseDaoImpl<RobHealthCheck, Long> implements RobHealthCheckDao {
	
	@Override
	public RobHealthCheck findbyCode(String code) {
		
		String hql = "from " + RobHealthCheck.class.getName() + " where code = ? ";
		List<Object> list = getHibernateTemplate().find(hql, code);
		
		if(list.size() > 0) {
			RobHealthCheck rhc = (RobHealthCheck) list.get(0); 
			return rhc;
		} else {
			return null;
		}
	}
	
}
