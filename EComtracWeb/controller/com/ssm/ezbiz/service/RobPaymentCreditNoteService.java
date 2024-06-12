package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;

public interface RobPaymentCreditNoteService extends BaseService<RobPaymentCreditNote, String>{

	public RobPaymentCreditNote processCreditNote(LlpPaymentReceipt llpPaymentReceipt);
	
	public RobPaymentCreditNote findByTransactionId(String transactionId);
}
