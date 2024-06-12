/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.model.LlpPaymentTransaction;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface LlpPaymentTransactionDao extends BaseDao<LlpPaymentTransaction, String> {

	LlpPaymentTransaction findSuccessByAppRefNo(String appRefNo);

	List<LlpPaymentTransaction> findPendingByAppRefNo(String appRefNo);
	
	public LlpPaymentTransaction findPendingByTransactionId(String transactionId);

	List<LlpPaymentTransaction> findByAppRefNoStatus(String appRefNo,
			String status);

	List<LlpPaymentTransaction> findDetailPaymentForGaf(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status,
			String refNo, String paymentGroupPrefix);

	LlpPaymentTransaction findByAppRefNoStatusPaymentMode(String appRefNo, String status, String paymentMode);

	void cancelAllPreviosDayOTCPayment();

	boolean hasPendingOnlineAndSuccessPaymentByAppRefNo(String appRefNo);

	void cancelPendingOtcByAppRefNo(String appRefNo);

}
