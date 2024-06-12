/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.exception.SSMException;

public interface LlpPaymentTransactionService extends BaseService<LlpPaymentTransaction, String>{
	public byte[] generateExcel(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo, String paymentGroup) throws SSMException;

	public byte[] generateGaf(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo, String paymentGroup)throws SSMException;

	public LlpPaymentTransaction findSuccessByAppRefNo(String appRefNo);
	
	List<LlpPaymentTransaction> findPendingByAppRefNo(String appRefNo);
	
	public LlpPaymentTransaction findPendingByTransactionId(String transactionId);
	
	List<LlpPaymentTransaction> findByAppRefNoStatus(String appRefNo,String status);
	
	public void cancelAllOtc(String appRefNo, String transactionId);

	LlpPaymentTransaction findByAppRefNoStatusPaymentMode(String appRefNo, String status, String paymentMode);

	byte[] generateExcelGAFSetup() throws SSMException;

	@Transactional
	void updateExcelGAFSetup(byte[] byteExcel) throws SSMException;

	public void cancelAllPreviosDayOTCPayment();

	public boolean hasPendingOnlineAndSuccessPaymentByAppRefNo(String appRefNo);

	public void cancelPendingOtcByAppRefNo(String appRefNo);

	public LlpPaymentTransaction findDetailByLatestByAppRefNo(String robFormACode);

}

