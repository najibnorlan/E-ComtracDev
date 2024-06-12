package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessFormAOwnerValidResp;

public interface RobFormAService extends BaseService<RobFormA, String> {
	
	public void insertUpdateAll(RobFormA robFormA);

	public RobFormA findAllById(String robFormARefNo);

//	public boolean checkBusinessNameWs(String bizNameStr)throws SSMException;

	public RobFormA findByIdWithData(String robFormACode)throws SSMException;

	public RobFormA getFormACertDataFromWS(RobFormA robFormA) throws SSMException;
	
	BusinessFormAOwnerValidResp isOwnerValidWithIc(String name, String icNo) throws SSMException;
	
	BizProfileDetResp getBusinessProfileDetailWs(String brNo) throws SSMException;
	
	public void insertRobIncentiveFormA(RobFormA robFormA) throws SSMException; 

	public void reLodgeFormA(RobFormA robFormA)throws SSMException;

	void lodgeFormAWs(RobFormA robFormA) throws SSMException;
	
	public RobFormA generateBorangAForm(RobFormA robFormA);
	
	void sendEmailFormAIncentiveVerifiedNoPayment(RobFormA robFormA);
	
	void sendEmailFormAIncentiveVerifiedPayment(RobFormA robFormA);
	
	void sendEmailFormAIncentiveReject(RobFormA robFormA);
	
	void sendEmailFormAIncentiveQuery(RobFormA robFormA);

	void sendEmailFormAInProcess(RobFormA robFormA) ;
	
	void sendEmailFormAInQuery(RobFormA robFormA) ;
	
	void sendEmailFormAInReject(RobFormA robFormA) ;
	
	void sendEmailFormAInApproved(RobFormA robFormA) ;
	
	void sendEmailAddAsPartner(RobFormA robFormA, RobFormAOwner partner);
	
	void sendEmailPartnerAccept(RobFormA robFormA, RobFormAOwner partner);
	
	void sendEmailPartnerReject(RobFormA robFormA, RobFormAOwner partner);
	
	public RobFormA checkUserUsingIncentive(String userId);
	
	public Integer countFormAByTypeAndStatus(String type, String status, String year, String month);

	public void cancelPendingPaymentMoreThanXDays(Integer noOfDays);

	public RobFormA getFormABusinessInfoDataFromWS(RobFormA robFormA) throws SSMException;

	List<SSMException> validateName(String bizNameStr, boolean checkWithCBS);

	public void updateFormBackToDraff(RobFormA robFormA) throws SSMException ;

	public void cancelDraftMoreThanXDays(Integer noOfDays);

	public void updateBaseListDistribution(String[] listUpdatedTrans);

}
