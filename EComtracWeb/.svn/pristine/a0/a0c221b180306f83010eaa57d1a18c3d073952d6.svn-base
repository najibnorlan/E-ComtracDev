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

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.webis.param.BusinessInfo;

public interface RobFormCService extends BaseService<RobFormC, String>{
	
	void lodgeFormCWs(RobFormC robFormC) throws SSMException;
	
	void relodgeRobFormC(RobFormC robFormC) throws SSMException;

	RobFormC generateRobDetailFromWs(String brNo) throws SSMException;

	double generateFormCCmpAmtFromWs(Date terminationDt, Date date)throws SSMException;

	void insertUpdateAll(RobFormC robFormC);

	RobFormC findAllById(String robFormCRefNo);

	RobFormB findByRobFormCCode(String robFormCode);

	void sendEmailPartnerAccept(RobFormC robFormC,RobFormOwnerVerification robFormOwnerVerification);

	void sendEmailFormCInQuery(RobFormC robFormC);

	void sendEmailFormCInReject(RobFormC robFormC);
	
	void sendEmailFormCInProcess(RobFormC robFormC);

	void sendEmailFormCInApproved(RobFormC robFormC);

	void sendEmailToAllPartner(RobFormC robFormC);

	void UpdateAll(RobFormC robFormC);

	RobFormC findByIdWithData(String robFormCCode) throws SSMException;

	RobFormC generateBorangCForm(RobFormC robFormC);

	void sendEmailPartnerReject(RobFormC robFormC,
			RobFormOwnerVerification robFormOwnerVerification);

	List<BusinessInfo> findListRobActiveByIcWS(String icNo) throws SSMException;

	List<RobFormC> findPendingApplication(String brNo);

	void discardApp(RobFormC robFormC)throws SSMException;

	void lodgeFormCWoPayment(RobFormC robFormC);

	
	
	
}

