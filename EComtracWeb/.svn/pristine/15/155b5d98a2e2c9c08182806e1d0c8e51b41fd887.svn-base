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

import com.ssm.ezbiz.dao.RobEBranchDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobEBranch;
import com.ssm.llp.ezbiz.model.RobEBranchTransaction;

@Repository
public class RobEBranchDaoImpl extends BaseDaoImpl<RobEBranch, Long>  implements RobEBranchDao{

	@Override
	public List<RobEBranch> findActiveByBrNo(String brNo) {
		String hql = "from "+RobEBranch.class.getName()
				+" where brNo=? and status='A' ";
		
		return getHibernateTemplate().find(hql, brNo);
	}
}
