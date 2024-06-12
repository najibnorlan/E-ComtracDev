/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpPaymentFeeDao;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;

@Service
public class LlpPaymentFeeServiceImpl extends BaseServiceImpl<LlpPaymentFee,  String> implements LlpPaymentFeeService{
	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired 
	LlpPaymentFeeDao LlpPaymentFeeDao;

	@Override
	public BaseDao getDefaultDao() {
		return LlpPaymentFeeDao;
	}

	@Override
	public void insertWithParameter(LlpPaymentFee entity, String desc) {
		LlpParameters llpParameters = new LlpParameters();
			
		llpParameters.setCode(entity.getPaymentCode());
		llpParameters.setCodeDesc(desc);
		llpParameters.setCodeType("PAYMENT_TYPE");
		llpParameters.setStatus("A");

		
		llpParametersService.insert(llpParameters);
		insert(entity);
		
	}

	@Override
	public void updateWithParameter(LlpPaymentFee entity, String desc) {
	
		LlpParameters llpParameters = new LlpParameters();
		llpParameters =  llpParametersService.findParameter(Parameter.PAYMENT_TYPE, entity.getPaymentCode());
		
		llpParameters.setCode(entity.getPaymentCode());
		llpParameters.setCodeDesc(desc);
		llpParameters.setCodeType("PAYMENT_TYPE");
		llpParameters.setStatus("A");
		
		llpParametersService.update(llpParameters);
		update(entity);
		
	}
	
	public void deletePaymentFee(LlpPaymentFee entity) {

		LlpParameters llpParameters = new LlpParameters();
		llpParameters =  llpParametersService.findParameter(Parameter.PAYMENT_TYPE, entity.getPaymentCode());
		
		llpParametersService.delete(llpParameters);
		delete(entity);
		
	}

}
