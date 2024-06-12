package com.ssm.llp.mod1.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;

public interface LlpRegistrationService extends BaseService<LlpRegistration, String> {

	LlpRegistration findByIdWithAllInfo(String llpNo);

	void saveInfo(LlpRegistration llpRegistration, String paymentTransId) throws SSMException;
	
	void validateRecord(LlpRegistration llpRegistration) throws SSMException;

	//void emailToPartner(LlpRegistration llpRegistration, boolean isProfileEmail);

	void sendEmailLlpRegistrationSuccess(LlpRegistration llpRegistration);
	
	void validatePartner(LlpRegistration llpRegistration) throws SSMException;
	
	void validateBizCode(LlpRegistration llpRegistration) throws SSMException;
	
	void validateBizCodePopup(int count) throws SSMException;

}
