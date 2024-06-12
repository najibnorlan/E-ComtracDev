package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.ezbiz.eghl.EGHLPaymentResponse;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.model.StoreDataResponse;

public interface PaymentService {
	public static String EGHL_TRANSACT_SUCCESS = "0";
	public static String EGHL_TRANSACT_FAIL = "1";
	public static String EGHL_TRANSACT_PENDING = "2";

	public static String SUCCESS_TRANSACT = "S";
	public static String FAIL_TRANSACT = "F";
	public static String PENDING_TRANSACT = "1";
	public static String SUCCESS_STORE_DATA = "00";


//	public ReturnStatusResponse checkPaymentStatus(String id, double amount) throws Exception;

	public void paymentSuccess(String id, EGHLPaymentResponse paymentResponse)throws SSMException;

	public LlpPaymentTransaction processPayment(LlpPaymentTransaction llpPaymentTransaction,
			List<LlpPaymentTransactionDetail> listPaymentDetails)
			throws SSMException;

	public String getSuccessPaymentIdByAppRefNo(String appRefNo);

	LlpPaymentTransaction checkAndUpdatePaymentStatus(LlpPaymentTransaction llpPaymentTransaction) throws SSMException;

}
