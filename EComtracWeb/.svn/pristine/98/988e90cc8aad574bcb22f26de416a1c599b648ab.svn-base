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

import com.ssm.ezbiz.dao.RobFormB2DetDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB2Det;

@Repository
public class RobFormB2DetDaoImpl extends BaseDaoImpl<RobFormB2Det, Long>  implements RobFormB2DetDao{

	@Override
	public void deleteExceptId(String robFormBCode, long[] listB2IdNotDelete) {
		String hql = " from " + RobFormB2Det.class.getName() + "  where robFormBCode = ? ";
		List listParam = new ArrayList();
		listParam.add(robFormBCode);
		for (int i = 0; i < listB2IdNotDelete.length; i++) {
			if(i==0){
				hql += " and robFormB2DetId not in ( ";
			}
			if(i>0){
				hql += ",";
			}
			hql += "?";
			listParam.add(listB2IdNotDelete[i]);
		}
		hql += ")"; 
		List list = getHibernateTemplate().find(hql, listParam.toArray());
		if(list.size()>0){
			getHibernateTemplate().deleteAll(list);
		}
	}

	@Override
	public List<RobFormB2Det> findByRobFormBCode(String robFormBCode) {
		String hql = "from "+RobFormB2Det.class.getName()
				+" where robFormBCode=? ";
		
		return getHibernateTemplate().find(hql, new String[]{robFormBCode});
	}
}
