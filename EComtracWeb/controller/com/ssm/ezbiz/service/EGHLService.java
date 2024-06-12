package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.ezbiz.eghl.EGHLPaymentRequest;
import com.ssm.ezbiz.eghl.EGHLQueryResponse;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.model.StoreDataResponse;

public interface EGHLService {

	EGHLPaymentRequest prepareSaleRequest(String paymentId, String transactionNo, String paymentDesc, double amount, String custIp, String custName,
			String custEmail, String custPhone);

	EGHLQueryResponse getPaymentStatus(String paymentId, double amount) throws SSMException;

	List<EGHLQueryResponse> getStatusByAppRefNo(String appRefNo);
}
