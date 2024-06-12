package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobTrainingParticipantDao;
import com.ssm.ezbiz.dao.RobTrainingReprintcertDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;

@Repository
public class RobTrainingReprintcertDaoImpl extends BaseDaoImpl<RobTrainingReprintcert, Integer> implements RobTrainingReprintcertDao{
	
	@Override
	public List<RobTrainingReprintcert> findAllPendingGenerateCert() {
		// TODO Auto-generated method stub
		String hql = "select a from " + RobTrainingReprintcert.class.getName() + " a"
		         + " where a.fileId is null "	
		         + " and a.status = ? "
		         + " order by a.createDt asc";
		return getHibernateTemplate().find(hql, Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
	}	
}
