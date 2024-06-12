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

import com.ssm.ezbiz.dao.RobFormB2Dao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2;

@Repository
public class RobFormB2DaoImpl extends BaseDaoImpl<RobFormB2, Long>  implements RobFormB2Dao{

	@Override
	public RobFormB2 findByRobFormBCode(String robFormBCode) {
		String hql = "from "+RobFormB2.class.getName()
				+" where robFormBCode=? ";
		
		List<RobFormB2> result = getHibernateTemplate().find(hql, new String[]{robFormBCode});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
}
