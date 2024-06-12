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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormBDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;

@Repository
public class RobFormBDaoImpl extends BaseDaoImpl<RobFormB, String>  implements RobFormBDao{
	
	@Override
	public RobFormB findByRobFormBCode(String robFormBCode) {
		String hql = "from "+RobFormB.class.getName()
				+" where robFormBCode=? ";
		
		List<RobFormB> result = getHibernateTemplate().find(hql, new String[]{robFormBCode});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	@Override
	public RobFormB findByIdWithData(String robFormBCode) {
		Session session = getSession();
		RobFormB robFormB = (RobFormB) session.load(RobFormB.class, robFormBCode);

		Hibernate.initialize(robFormB.getFormBData());
		Hibernate.initialize(robFormB.getCertFileData());
		
		if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())) {
			Hibernate.initialize(robFormB.getBusinessInfoData());
		}
		if (StringUtils.isNotBlank(robFormB.getCmpNo())) {
			Hibernate.initialize(robFormB.getCmpNoticeFileData());
		}
		return robFormB;
	}
}
