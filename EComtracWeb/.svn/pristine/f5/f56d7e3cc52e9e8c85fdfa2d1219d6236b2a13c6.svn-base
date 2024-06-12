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

import com.ssm.ezbiz.dao.RobFormB3Dao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;

@Repository
public class RobFormB3DaoImpl extends BaseDaoImpl<RobFormB3, Long>  implements RobFormB3Dao{

	@Override
	public void deleteExceptId(String robFormBCode, long[] listB3IdNotDelete) {
		String hql = " from " + RobFormB3.class.getName() + "  where robFormBCode = ? ";
		List listParam = new ArrayList();
		listParam.add(robFormBCode);
		for (int i = 0; i < listB3IdNotDelete.length; i++) {
			if(i==0){
				hql += " and robFormB3Id not in ( ";
			}
			if(i>0){
				hql += ",";
			}
			hql += "?";
			listParam.add(listB3IdNotDelete[i]);
		}
		hql += ")"; 
		List list = getHibernateTemplate().find(hql, listParam.toArray());
		if(list.size()>0){
			getHibernateTemplate().deleteAll(list);
		}
	}

	@Override
	public List<RobFormB3>  findByRobFormBCode(String robFormBCode) {
		String hql = "from "+RobFormB3.class.getName()
				+" where robFormBCode=? ";
		
		return getHibernateTemplate().find(hql, new String[]{robFormBCode});
	}
}
