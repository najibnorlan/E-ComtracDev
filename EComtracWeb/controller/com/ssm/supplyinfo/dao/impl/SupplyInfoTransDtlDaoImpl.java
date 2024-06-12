/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.supplyinfo.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.supplyinfo.dao.SupplyInfoTransDtlDao;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;

@Repository
public class SupplyInfoTransDtlDaoImpl extends BaseDaoImpl<SupplyInfoTransDtl, Long>  implements SupplyInfoTransDtlDao{

	@Override
	public void deleteByHdrCode(String transCode) {
		String hql = " delete " + SupplyInfoTransDtl.class.getName() + "  where hdrTransCode = ? ";
		
		Query query = getSession().createQuery(hql);
		query.setString(0, transCode);
		query.executeUpdate();
		
		
	}
}
