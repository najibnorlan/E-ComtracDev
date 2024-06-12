/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.dao.LlpPaymentReceiptDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;


@Repository
public class LlpPaymentReceiptDaoImpl extends BaseDaoImpl<LlpPaymentReceipt, String>  implements LlpPaymentReceiptDao{
	
	@Override
	public Integer getCountTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId", SearchCriteria.EQUAL, counterSessionId).andIfNotNull("isCancel", SearchCriteria.EQUAL, receiptIsCancel);
		List<LlpPaymentReceipt> transactions = findByCriteria(sc).getList();
		
		return transactions.size();
	}
	
	@Override
	public Double getTotalTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId", SearchCriteria.EQUAL, counterSessionId).andIfNotNull("isCancel", SearchCriteria.EQUAL, receiptIsCancel);
		List<LlpPaymentReceipt> transactions = findByCriteria(sc).getList();
		
		BigDecimal jumlah = BigDecimal.ZERO;
		for(LlpPaymentReceipt i : transactions){
			jumlah = jumlah.add(BigDecimal.valueOf(i.getTotalAmount())) ;
		}
		
		return jumlah.doubleValue();
	}
	
	@Override
	public List<LlpPaymentReceipt> getAllTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId", SearchCriteria.EQUAL, counterSessionId).andIfNotNull("isCancel", SearchCriteria.EQUAL, receiptIsCancel);
		List<LlpPaymentReceipt> transactions = findByCriteria(sc).getList();
		
		return transactions;
	}
	
	@Override
	public LlpPaymentReceipt getReceiptNoByTransactionID(String transactionId){
		
		String hql = " from "+LlpPaymentReceipt.class.getName()
				+" where transactionId = ? order by createDt asc ";
				
		List<LlpPaymentReceipt> list = getHibernateTemplate().find(hql, new String[]{transactionId});
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
}
