/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
 */
public interface LlpPaymentReceiptDao extends
		BaseDao<LlpPaymentReceipt, String> {

	public Integer getCountTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel);

	public Double getTotalTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel);

	public List<LlpPaymentReceipt> getAllTransactionByCounterSession(
			Integer counterSessionId, Integer receiptIsCancel);
	
	public LlpPaymentReceipt getReceiptNoByTransactionID(String transactionId);
}
