/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpPaymentTransactionDetailDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;

@Service
public class LlpPaymentTransactionDetailServiceImpl extends BaseServiceImpl<LlpPaymentTransactionDetail,  Long> implements LlpPaymentTransactionDetailService{
	@Autowired 
	LlpPaymentTransactionDetailDao llpPaymentTransactionDetailDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpPaymentTransactionDetailDao;
	}

	@Override
	public List<LlpPaymentTransactionDetail> find(String transactionId){
		List<LlpPaymentTransactionDetail> list = new ArrayList<LlpPaymentTransactionDetail>();
		SearchCriteria sc = new SearchCriteria("transactionId",SearchCriteria.EQUAL,transactionId);
		SearchResult sr = findByCriteria(sc);
		
		if(sr != null){
			list = sr.getList();
		}
		
		return list;
	}

	@Override
	public List<LlpPaymentTransactionDetail> findByTransactionIdAndPaymentItem(String transactionId, String paymentType) {
		List<LlpPaymentTransactionDetail> list = new ArrayList<LlpPaymentTransactionDetail>();
		SearchCriteria sc1 = new SearchCriteria("transactionId",SearchCriteria.EQUAL,transactionId);
		SearchCriteria sc2 = new SearchCriteria("paymentItem", SearchCriteria.EQUAL, paymentType);
		SearchResult sr = findByCriteria(new SearchCriteria(sc1, SearchCriteria.AND, sc2));
		
		if(sr != null){
			list = sr.getList();
		}
		
		return list;
	}
	
	@Override
	public Boolean isHaveSrProduct(String transactionId){
		return llpPaymentTransactionDetailDao.isHaveSrProduct(transactionId);
	}

}
