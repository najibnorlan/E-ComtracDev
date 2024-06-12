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

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormB1Dao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;

@Repository
public class RobFormB1DaoImpl extends BaseDaoImpl<RobFormB1, Long>  implements RobFormB1Dao{

	@Override
	public RobFormB1 findByRobFormBCode(String robFormBCode) {
		String hql = "from "+RobFormB1.class.getName()
				+" where robFormBCode=? ";
		
		List<RobFormB1> result = getHibernateTemplate().find(hql, new String[]{robFormBCode});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
}
