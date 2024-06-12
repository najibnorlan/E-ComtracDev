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

import com.ssm.ezbiz.dao.CmpMasterDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.ezbiz.model.CmpMaster;

@Repository
public class CmpMasterDaoImpl extends BaseDaoImpl<CmpMaster, Long>  implements CmpMasterDao{

	@Override
	public List<CmpMaster> findByTransCode(String transCode) {
		String hql = "from "+CmpMaster.class.getName()
				+" where cmpTransactionCode = ?";
		
		List<CmpMaster> result= getHibernateTemplate().find(hql, new String[]{transCode});
		if(result!= null && result.size()>0){
			return result;
		}
		return null;
	}
}
