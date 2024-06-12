/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service;

import java.util.List;

import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobPosTerminalTransaction;

public interface LlpPaymentReceiptService extends BaseService<LlpPaymentReceipt, String>{
	public LlpPaymentReceipt find(String transactionId);
	
	public Integer getCountTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel);
	
	public Double getTotalTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel);
	
	public List<LlpPaymentReceipt> getAllTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel);
	
	
	public LlpPaymentReceipt getReceiptNoByTransactionID(String transactionId);

	public void lodgeWSAfterRecieptGenerate(String transactionId)throws SSMException;

	public LlpPaymentTransaction receivePaymentGenerateReceiptForCash(String transactionId, Integer counterSessionId, Double totalAmount, 
			Double cashReceived);
	
	public LlpPaymentTransaction receivePaymentGenerateReceiptForManualCardPayment(String transactionId, Integer counterSessionId, Double totalAmount, 
			Double cashReceived, String approvalCode, String cardType, String cardBank);
	
	public LlpPaymentTransaction receivePaymentGenerateReceiptForIntegrationCardPayment(RobPosTerminalTransaction posTerminalTransaction)throws SSMException;
}

