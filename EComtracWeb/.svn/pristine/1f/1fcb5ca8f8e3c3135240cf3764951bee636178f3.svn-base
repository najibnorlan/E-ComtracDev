package com.ssm.ezbiz.service;

import com.ssm.llp.base.exception.SSMException;

public interface PaymentInterface {
	/*
	 * Need to implement this method when payment success
	 * 1. Need to save/update object
	 * 2. Need to call LlpPaymentTransactionService.update 
	 */
	public abstract void sucessPayment(Object obj, String paymentTransId)throws SSMException;
	
	public abstract void sucessNotification(Object obj, String paymentTransId)throws SSMException;
}
