/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormCDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormC;

@Repository
public class RobFormCDaoImpl extends BaseDaoImpl<RobFormC, String>  implements RobFormCDao{

	@Override
	public RobFormC findByIdWithData(String robFormCCode) {
		// TODO Auto-generated method stub
		Session session = getSession();
		RobFormC  robFormC = (RobFormC) session.load(RobFormC.class, robFormCCode);
		
		Hibernate.initialize(robFormC.getFormCDataId());
		if(Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())){
			Hibernate.initialize(robFormC.getBusinessInfoDataId());
		}
		return robFormC;
	}

	@Override
	public RobFormC findByRobFormCCode(String robFormCCode) {
		// TODO Auto-generated method stub
		String hql = "from "+RobFormC.class.getName()
				+" where robFormCCode=? ";
		
		List<RobFormC> result = getHibernateTemplate().find(hql, new String[]{robFormCCode});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	
	

}
