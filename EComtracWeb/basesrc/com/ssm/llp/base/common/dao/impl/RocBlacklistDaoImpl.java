/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.RocBlacklistDao;
import com.ssm.llp.ezbiz.model.RocBlacklist;

@Repository
public class RocBlacklistDaoImpl extends RocBaseDaoImpl<RocBlacklist, Long>  implements RocBlacklistDao{

	@Override
	public boolean checkStatusIdTypeAndIdNo(String idType, String idNo) {
		// TODO Auto-generated method stub
		String hql = " from " + RocBlacklist.class.getName() + " where vchentitytype=? and "
				+ "vchentityno=? and vchblackliststatus='01' ";

		//System.out.println("idType"+idType);
		List list = getHibernateTemplate().find(hql, new Object[] {idType, idNo});

		//System.out.println("hql"+hql);
		if (list.size() > 0) {
			RocBlacklist blacklist = (RocBlacklist) list.get(0);
			return true;
		}

		return false;
		
	}
	
}
