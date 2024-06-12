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
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;


public interface RobFormBService extends BaseService<RobFormB, String>{

	RobFormB generateRobDetailFromWs(String brNo)throws SSMException;
	
	public RobFormB findByRobFormBCode(String robFormBCode);
	
	
	public RobFormB insertUpdateAll(RobFormB robFormB);
	
	void sendEmailPartnerAccept(RobFormB robFormB, RobFormOwnerVerification partner);
	
	void sendEmailToAllPartner(RobFormB robFormB);

	RobFormB findAllByIdWithWS(String robFormBCode);

	RobFormB findAllById(String robFormBCode);

	void sendEmailFormBInProcess(RobFormB robFormB);

	void sendEmailFormBInQuery(RobFormB robFormB);

	void sendEmailFormBInReject(RobFormB robFormB);

	void sendEmailFormBInApproved(RobFormB robFormB);

	void reLodgeFormB(RobFormB robFormB)throws SSMException;

	RobFormB getBizInfoFromWS(RobFormB robFormB)throws SSMException;

	void lodgeFormBWs(RobFormB robFormB)throws SSMException ;

	void sendEmailPartnerReject(RobFormB robFormB, RobFormOwnerVerification partner);
	
	boolean isBizInfoHashValid(RobFormB formB)throws SSMException;

	List findPendingApplication(String brNo);

	void updateFormBackToDraff(RobFormB robFormB) throws SSMException;

	RobFormB findByIdWithData(String robFormBCode) throws SSMException;
	

	void updateStatusApproved(RobFormB robFormB, String approveRejectBy, String approveRejectbranch, Date approveRejectDt, String approveRejectNotes);
}

