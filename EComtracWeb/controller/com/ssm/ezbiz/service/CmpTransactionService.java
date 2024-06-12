/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;


import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpInfo;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.webis.param.CompoundPaymentResp;
import com.ssm.webis.param.CompoundStatusResp;


public interface CmpTransactionService extends BaseService<CmpTransaction, String>{

	public List<CmpTransaction> findByDate(Date fromDt, Date toDt, String cmpNo, String updateBy, String status, String page) throws SSMException;
//	public CompoundPaymentResp lodgeCompoundPaymentWS(CmpInfo cmpInfo, LlpPaymentReceipt receipt) throws SSMException;
	public CompoundPaymentResp lodgeCompoundPaymentWS(CmpTransaction cmpTransaction, LlpPaymentReceipt receipt) throws SSMException;
	public CompoundStatusResp getCmpStatusWS(CmpTransaction cmpTransaction) throws SSMException;
	public void lodgeCompoundPaymentWS(CmpTransaction cmpTransaction)throws SSMException;

}

