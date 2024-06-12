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

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.dao.LlpPaymentTransactionDetailDao;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.ezbiz.model.RobFormAOwner;

@Repository
public class LlpPaymentTransactionDetailDaoImpl extends BaseDaoImpl<LlpPaymentTransactionDetail, Long>  implements LlpPaymentTransactionDetailDao{

	@Override
	public Boolean isHaveSrProduct(String transactionId){
		String hql = "from LlpPaymentTransaction a, LlpPaymentTransactionDetail b, LlpPaymentFee c"
				+ " where b.transactionId = a.transactionId"
				+ " and b.paymentItem = c.paymentCode"
				+ " and b.transactionId = ?"
				+ " and c.gstCode = 'SR'";
		
		List<LlpPaymentTransaction> list = getHibernateTemplate().find(hql, new String[]{transactionId});
		if(list.size() > 0){
			return true;
		}
		return false;
	}
}
