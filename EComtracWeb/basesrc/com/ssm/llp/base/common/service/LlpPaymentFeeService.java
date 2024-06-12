/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service;

import com.ssm.llp.base.common.model.LlpPaymentFee;

public interface LlpPaymentFeeService extends BaseService<LlpPaymentFee, String>{
	public void insertWithParameter(LlpPaymentFee entity, String desc);
	
	public void updateWithParameter(LlpPaymentFee entity, String desc);
	
	public void deletePaymentFee(LlpPaymentFee entity);
}

