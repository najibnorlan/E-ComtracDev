/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.supplyinfo.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.supplyinfo.dao.SupplyInfoTransHdrDao;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;

@Repository
public class SupplyInfoTransHdrDaoImpl extends BaseDaoImpl<SupplyInfoTransHdr, String> implements SupplyInfoTransHdrDao {

	@Override
	public SupplyInfoTransHdr findAllById(String transCode) {

		try {
			Session session = getSession();
			SupplyInfoTransHdr hdr = (SupplyInfoTransHdr) session.load(SupplyInfoTransHdr.class, transCode);
			Hibernate.initialize(hdr.getListSupplyInfoTransDtl());
			if (hdr.getListSupplyInfoTransDtl() == null) {
				hdr.setListSupplyInfoTransDtl(new ArrayList());
			}
			return hdr;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public SupplyInfoTransHdr findLatestDraffHdr(String userId) {

		String hql = "from " + SupplyInfoTransHdr.class.getName() + " where ownerBy=? and status=? order by updateDt desc ";

		List<SupplyInfoTransHdr> result = getHibernateTemplate().find(hql, new String[] { userId, Parameter.SUPPLY_INFO_TRANS_STATUS_DATA_ENTRY });

		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	

	@Override
	public void deleteCookiesCart(SupplyInfoTransHdr cartHdr) {
		
		delete(cartHdr);
		String hql = " delete " + SupplyInfoTransDtl.class.getName() + "  where hdrTransCode = ? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, cartHdr.getTransCode());
		query.executeUpdate();
	}
}
