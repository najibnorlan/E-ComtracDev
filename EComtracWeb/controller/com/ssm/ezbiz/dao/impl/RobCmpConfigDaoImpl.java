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

import com.ssm.ezbiz.dao.RobCmpConfigDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobCmpConfig;
import com.ssm.llp.ezbiz.model.RobFormB1;

@Repository
public class RobCmpConfigDaoImpl extends BaseDaoImpl<RobCmpConfig, Long>  implements RobCmpConfigDao{

	@Override
	public double findCmpAmtByDelayDay(String formCode, int delayDay) {
		String hql = "from "+RobCmpConfig.class.getName()
				+" where formType=? and dayDurationFrom <= ? and dayDurationTo >= ?";
		
		List<RobCmpConfig> result = getHibernateTemplate().find(hql, new Object[]{formCode, delayDay, delayDay});
		if(result.size()>0){
			return result.get(0).getCmpAmount();
		}
		
		return 0;
	}
}
