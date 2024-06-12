package com.ssm.llp.mod1.page;

import java.util.Date;

import com.ssm.llp.base.page.SecBasePage;

public class LlpPaymentTransactionBasePage extends SecBasePage {
	public LlpPaymentTransactionBasePage(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo, String paymentGroup) {
		add(new SearchLlpPaymentTransaction("searchLlpPaymentTransaction", searchFromDt, searchToDt, paymentMode, transactionId, status, refNo, paymentGroup));
	}

	public LlpPaymentTransactionBasePage() {
		add(new SearchLlpPaymentTransaction("searchLlpPaymentTransaction"));
	}
}
