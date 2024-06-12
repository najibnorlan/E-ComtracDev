/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormB4Dao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormB4;

@Repository
public class RobFormB4DaoImpl extends BaseDaoImpl<RobFormB4, Long>  implements RobFormB4Dao{

	@Override
	public List<RobFormB4>  findByRobFormBCode(String robFormBCode) {
		
		String hql = "from "+RobFormB4.class.getName() +" where robFormBCode=? ";
		return getHibernateTemplate().find(hql,robFormBCode);
	}
	
	@Override
	public void deleteExceptId(String robFormBCode, long[] listB4IdNotDelete) {
		String hql = " from " + RobFormB4.class.getName() + "  where robFormBCode = ? ";
		List listParam = new ArrayList();
		listParam.add(robFormBCode);
		for (int i = 0; i < listB4IdNotDelete.length; i++) {
			if(i==0){
				hql += " and robFormB4Id not in ( ";
			}
			if(i>0){
				hql += ",";
			}
			hql += "?";
			listParam.add(listB4IdNotDelete[i]);
		}
		hql += ")"; 
		List list = getHibernateTemplate().find(hql, listParam.toArray());
		if(list.size()>0){
			getHibernateTemplate().deleteAll(list);
		}
	}
}
