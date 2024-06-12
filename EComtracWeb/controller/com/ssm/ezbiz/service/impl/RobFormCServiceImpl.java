/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobFormCDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.client.SSMInfoClient;
import com.ssm.webis.param.BizProfileDetReq;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessFormCCmpAmtReq;
import com.ssm.webis.param.BusinessFormCCmpAmtResp;
import com.ssm.webis.param.BusinessFormCLodgeReq;
import com.ssm.webis.param.BusinessFormCLodgeResp;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.webis.param.BusinessInfoResp;
import com.ssm.webis.param.FindBusinessByICNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoResp;
import com.ssm.webis.param.InfoParameter;
import com.ssm.webis.param.RobFormCParam;
import com.ssm.webis.param.RobFormCPaymentInfoParam;
import com.ssm.webis.param.RobFormOwnerInfo;
import com.ssm.webis.param.SSMInfoException;
import com.ssm.webis.param.SSMInfoGenerateReq;
import com.ssm.webis.param.SSMInfoGenerateResp;
import com.ssm.webis.param.SSMInfoReq;
import com.ssm.webis.param.SSMInfoResp;

@Service
public class RobFormCServiceImpl extends BaseServiceImpl<RobFormC, String> implements RobFormCService, PaymentInterface {

	@Autowired
	RobFormCDao robFormCDao;

	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;

	@Autowired
	@Qualifier("LlpFileDataService")
	LlpFileDataService llpFileDataService;

	@Autowired
	@Qualifier("RobFormNotesService")
	RobFormNotesService robFormNotesService;

	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	@Qualifier("RobFormOwnerVerificationService")
	RobFormOwnerVerificationService robFormOwnerVerificationService;

	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;

	@Autowired
	@Qualifier("LlpUserLogService")
	LlpUserLogService llpUserLogService;

	@Override
	public BaseDao getDefaultDao() {
		return robFormCDao;
	}

	@Autowired
	@Qualifier("LlpPaymentTransactionDetailService")
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	@Autowired
	@Qualifier("LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	@Qualifier("LlpFileUploadService")
	LlpFileUploadService llpFileUploadService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Transactional
	@Override
	public void relodgeRobFormC(RobFormC robFormC) throws SSMException {
		RobFormNotes formNotes = robFormC.getListRobFormNotes().get(robFormC.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormC.getQueryAnswer());

		robFormNotesService.update(formNotes);
		reLodgeFormCWs(robFormC);

	}

	private void reLodgeFormCWs(RobFormC robFormC) throws SSMException {
		// TODO Auto-generated method stub
		String url = wSManagementService.getWsUrl("RobClient.relodgeFormC");
		BusinessFormCLodgeReq reqParam = prepareFormCParamData(robFormC);

		RobFormNotes formNotes = robFormC.getListRobFormNotes().get(robFormC.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormC.getQueryAnswer());
		reqParam.getFormCParam().setNotes(formNotes.getNotes());
		reqParam.getFormCParam().setNotesAnswer(formNotes.getNotesAnswer());

		try {
			BusinessFormCLodgeResp resp = RobClient.relodgeFormC(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_IN_PROCESS);
				robFormC.setResubmitDt(new Date());
				robFormCDao.update(robFormC);
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}

	private BusinessFormCLodgeReq prepareFormCParamData(RobFormC robFormC) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		BusinessFormCLodgeReq param = new BusinessFormCLodgeReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		param.setReqRefNo(robFormC.getRobFormCCode());

		
		// Payment Info
		String processBranch = Parameter.ROB_AGENCY_BRANCH_CODE; 
		LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormC.getRobFormCCode());
		if (paymentTransaction != null) {
			LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
			String recieptNo = paymentReceipt.getReceiptNo();
			RobFormCPaymentInfoParam paramPayment = new RobFormCPaymentInfoParam();
			paramPayment.setPaymentAmt(robFormC.getTotalAmt());
			paramPayment.setBusinessInfoAmt(robFormC.getBizInfoAmt());
			if (recieptNo != null) {
				paramPayment.setInvoiceNo(recieptNo);
				paramPayment.setPaymentDate(paymentTransaction.getCreateDt());
			} else {
				paramPayment.setInvoiceNo(recieptNo);
				paramPayment.setPaymentDate(new Date());
			}
			paramPayment.setCmpAmt(robFormC.getCmpAmt());
			param.setRobFormCPaymentInfoParam(paramPayment);
			
			if(paymentReceipt.getCounterSessionId()!=null){
				processBranch = paymentReceipt.getCounterSessionId().getBranch();
				robFormC.setProsessingBranch(processBranch);
				robFormCDao.update(robFormC);
			}
		}
		
		if(StringUtils.isBlank(robFormC.getProsessingBranch())){
			robFormC.setProsessingBranch(processBranch);
			robFormCDao.update(robFormC);
		}
		param.setAgencyBranchCode(robFormC.getProsessingBranch());
		param.setIsFromSSMPc(robFormC.getIsFromSSMPc());
		// Param Info
		RobFormCParam formCParam = new RobFormCParam();
		formCParam.setBranchCode(Parameter.EZBIZ_BRANCH_CODE);
		formCParam.setFormCRefNo(robFormC.getRobFormCCode());
		formCParam.setApplicantName(userProfile.getName());
		formCParam.setApplicantIC(userProfile.getIdNo());
		formCParam.setApplicantICColor("");
		String addr = userProfile.getAdd1();
		if (StringUtils.isNotBlank(userProfile.getAdd2())) {
			addr += "\n" + userProfile.getAdd2();
		}
		if (StringUtils.isNotBlank(userProfile.getAdd3())) {
			addr += "\n" + userProfile.getAdd3();
		}
		formCParam.setApplicantAddress(addr);
		formCParam.setApplicantTown(userProfile.getCity());
		formCParam.setApplicantPostcode(userProfile.getPostcode());
		formCParam.setApplicantState(userProfile.getState());
		formCParam.setApplicantEmail(userProfile.getEmail());
		formCParam.setApplicantTelNo(userProfile.getHpNo());
		formCParam.setApplicantFaxNo(userProfile.getFaxNo());

		formCParam.setBizRegNo(robFormC.getBrNo());
		formCParam.setCheckDigit(robFormC.getCheckDigit());
		formCParam.setCourtDate(robFormC.getCourtOrderDt());
		formCParam.setTerminationDate(robFormC.getTerminationDt());
		formCParam.setDeceasedDate(robFormC.getDeceasedDt());
		formCParam.setCmpAmount(robFormC.getCmpAmt());
		formCParam.setIsPayCmp(robFormC.getIsPayCmp());
		formCParam.setOtherReason(robFormC.getReasonOthers());
		formCParam.setReasonType(robFormC.getReasonType());
		// formCParam.setFormCRefNo(robFormC.getRobFormCCode());

		// TODO Auto-generated method stub
		param.setFormCParam(formCParam);
		return param;
	}

	@Override
	public void lodgeFormCWs(RobFormC robFormC) throws SSMException {
		// TODO Auto-generated method stub
		Locale malayLoc = new Locale("ms", "MY");
		DateFormat endDtFormat = new SimpleDateFormat("dd MMMM yyyy", malayLoc);

		String url = wSManagementService.getWsUrl("RobClient.lodgeFormC");
		BusinessFormCLodgeReq reqParam = prepareFormCParamData(robFormC);

		try {
			BusinessFormCLodgeResp resp = RobClient.lodgeFormC(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormC.setCmpNo(resp.getCmpNo());
				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_IN_PROCESS);
				robFormC.setSubmitDt(new Date());
				robFormCDao.update(robFormC);
				sendEmailFormCInProcess(robFormC);
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}

	@Override
	public RobFormC generateRobDetailFromWs(String brNo) throws SSMException {
		RobFormC robFormC = new RobFormC();

		String url = wSManagementService.getWsUrl("RobClient.getBizProfileDet");

		BizProfileDetReq param = new BizProfileDetReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setBrNo(brNo);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

		try {

			BizProfileDetResp ssmWsResp = RobClient.getBizProfileDet(url, param);

			if (!"00".equals(ssmWsResp.getSuccessCode())) {
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			robFormC.setBizProfileDetResp(ssmWsResp);
			robFormC.setBrNo(ssmWsResp.getMainInfo().getBrNo());
			robFormC.setCheckDigit(ssmWsResp.getMainInfo().getChkDigit());
			robFormC.setBizName(ssmWsResp.getMainInfo().getBizName());
			robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY);
			robFormC.setTerminationDt(new Date());
			// robFormCDao.insert(robFormC);

			// Prepare Owner List
			List<RobFormOwnerVerification> listRobFormOwnerVerification = new ArrayList<RobFormOwnerVerification>();
			LlpUserProfile llpCurrrentUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			RobFormOwnerInfo[] owner = robFormC.getBizProfileDetResp().getActiveOwnerInfo();
			for (int i = 0; i < owner.length; i++) {
				String ownerId = owner[i].getIcNo();
				LlpUserProfile ezbizUser = llpUserProfileService.findByIdTypeAndIdNo("01", owner[i].getIcNo());

				RobFormOwnerVerification ownerVerification = new RobFormOwnerVerification();
				ownerVerification.setIdNo(owner[i].getIcNo());
				ownerVerification.setIdType("01");
				ownerVerification.setName(owner[i].getName());
				ownerVerification.setRobFormCode(robFormC.getRobFormCCode());
				ownerVerification.setRobFormType(Parameter.ROB_FORM_TYPE_C);
				ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND);// by
																								// default

				if (ezbizUser != null) {
					ownerVerification.setUserRefNo(ezbizUser.getUserRefNo());
					if (Parameter.USER_STATUS_active.equals(ezbizUser.getUserStatus())) {
						if (llpCurrrentUserProfile.getUserRefNo().equals(ezbizUser.getUserRefNo())) {
							ownerVerification.setStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
							ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_NOT_REQ);// by
																											// default
						} else {
							ownerVerification.setStatus(Parameter.ROB_OWNER_VERI_STATUS_PENDING);
						}
					} else {
						ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_ACTIVATION);
					}
				} else {
					ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER);
				}
				listRobFormOwnerVerification.add(ownerVerification);

			}
			robFormC.setListRobFormOwnerVerification(listRobFormOwnerVerification);

			return robFormC;
		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}

	}

	@Override
	public double generateFormCCmpAmtFromWs(Date terminationDt, Date eventDate) throws SSMException {
		// TODO Auto-generated method stub
		double cmpAmount = 0.00;
		String url = wSManagementService.getWsUrl("RobClient.getCmpAmtFormC");

		BusinessFormCCmpAmtReq formCCmpAmtReq = new BusinessFormCCmpAmtReq();
		formCCmpAmtReq.setAgencyId(Parameter.ROB_AGENCY_ID);
		formCCmpAmtReq.setFormCode(Parameter.ROB_FORM_C_CMP_FORM_CODE_C);
		formCCmpAmtReq.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		formCCmpAmtReq.setTerminationDate(terminationDt);
		formCCmpAmtReq.setEventDate(eventDate);

		try {
			BusinessFormCCmpAmtResp ssmWsRes = RobClient.getCmpAmtFormC(url, formCCmpAmtReq);

			if (!"00".equals(ssmWsRes.getSuccessCode())) {
				throw new SSMException(ssmWsRes.getErrorMsg());
			}

			cmpAmount = ssmWsRes.getCmpAmount();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cmpAmount;
	}

	@Override
	@Transactional
	public void insertUpdateAll(RobFormC robFormC) {

		if (StringUtils.isBlank(robFormC.getRobFormCCode())) {
			RobFormOwnerInfo[] owner = robFormC.getBizProfileDetResp().getActiveOwnerInfo();
			robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY);
//			if (robFormC.getTotalAmt() <= 0 && owner.length == 1) {
//				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT);
//			}
			insert(robFormC);

			List<RobFormOwnerVerification> listRobFormOwnerVerification = robFormC.getListRobFormOwnerVerification();
			for (int i = 0; i < listRobFormOwnerVerification.size(); i++) {
				RobFormOwnerVerification ownerVerification = listRobFormOwnerVerification.get(i);
				ownerVerification.setRobFormCode(robFormC.getRobFormCCode());

				robFormOwnerVerificationService.insert(ownerVerification);
			}
			robFormC.setListRobFormOwnerVerification(listRobFormOwnerVerification);

		} else {

			boolean checkVerification = true;

			ArrayList listRobFormOwnerVerification = (ArrayList) robFormC.getListRobFormOwnerVerification();

			for (int i = 0; i < listRobFormOwnerVerification.size(); i++) {
				RobFormOwnerVerification robFormOwnerVerification = (RobFormOwnerVerification) listRobFormOwnerVerification.get(i);
				if (!robFormOwnerVerification.getStatus().equals(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED)) {
					checkVerification = false;
				}
			}

//			if (checkVerification && robFormC.getTotalAmt() <= 0) {
//				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT);
//			} else {
			robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY);
//			}

			update(robFormC);

		}

	}

	@Override
	@Transactional
	public void UpdateAll(RobFormC robFormC) {
		update(robFormC);
	}

	@Override
	public void sendEmailToAllPartner(RobFormC robFormC) {
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());

		List<RobFormOwnerVerification> listOwnerVeri = robFormC.getListRobFormOwnerVerification();
		for (int i = 0; i < listOwnerVeri.size(); i++) {
			RobFormOwnerVerification ownerVer = listOwnerVeri.get(i);
			if (Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION.equals(ownerVer.getStatus())) {
				if (Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND.equals(ownerVer.getEmailStatus())) {
					LlpUserProfile partner = llpUserProfileService.findById(ownerVer.getUserRefNo());
					sendEmailNotifyPartner(robFormC, lodgerUser, partner);
					ownerVer.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_SEND);
					robFormOwnerVerificationService.update(ownerVer);
				}
			}
		}
	}

	public void sendEmailNotifyPartner(RobFormC robFormC, LlpUserProfile lodgerUser, LlpUserProfile partner) {
		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		mailService.sendMail(partner.getEmail(), "email.notification.robFormC.notifyPartner.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.notifyPartner.body",
				robFormC.getRobFormCCode(), robFormC.getBizName(), partner.getName(), lodgerUser.getName(), robFormC.getBrNo(),
				parser.format(new Date()));
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobFormC robFormCTmp = (RobFormC) obj;
		RobFormC robFormC = robFormCDao.findById(robFormCTmp.getRobFormCCode());
		obj = robFormC;
		System.out.println("SUCCESS PAYMENT: ");
		if (Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT.equals(robFormC.getStatus())) {
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
			robFormC.setPaymentDt(paymentTransaction.getCreateDt());

			robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS);
			robFormCDao.update(robFormC);
		}
	}

	@Override
	@Transactional
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		RobFormC robFormCTmp = (RobFormC) obj;
		RobFormC robFormC = findAllById(robFormCTmp.getRobFormCCode());
		obj = robFormC;
		lodgeFormCWs(robFormC);
	}

	@Override
	public RobFormC findAllById(String robFormCRefNo) {
		RobFormC robFormC = robFormCDao.findById(robFormCRefNo);

		List<RobFormOwnerVerification> listRobFormOwnerVerification = robFormOwnerVerificationService.getListRobFormOwnerVerification(robFormCRefNo);
		robFormC.setListRobFormOwnerVerification(listRobFormOwnerVerification);

		List<RobFormOwnerVerification> listOwnerVeri = robFormC.getListRobFormOwnerVerification();

		for (int i = 0; i < listOwnerVeri.size(); i++) {
			RobFormOwnerVerification ownerVer = listOwnerVeri.get(i);
			if (Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER.equals(ownerVer.getStatus())) {

				if (Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND.equals(ownerVer.getEmailStatus())) {
					if (ownerVer.getUserRefNo() == null) {
						LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
						LlpUserProfile partner = llpUserProfileService.findByIdTypeAndIdNo("01", ownerVer.getIdNo());
						if (partner != null) {
							ownerVer.setUserRefNo(partner.getUserRefNo());
							ownerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
							sendEmailNotifyPartner(robFormC, lodgerUser, partner);
							ownerVer.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_SEND);
							robFormOwnerVerificationService.update(ownerVer);
						}
					}

				}
			}
		}

		return robFormC;
	}

	@Override
	public RobFormB findByRobFormCCode(String robFormCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendEmailPartnerAccept(RobFormC robFormC, RobFormOwnerVerification robFormOwnerVerification) {
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());

		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");

		mailService.sendMail(lodgerUser.getEmail(), "email.notification.robFormC.partnerAccept.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.partnerAccept.body",
				robFormC.getRobFormCCode(), robFormC.getBizName(), lodgerUser.getName(), robFormOwnerVerification.getName(), robFormC.getBrNo(),
				parser.format(new Date()));

	}

	@Override
	public void sendEmailFormCInQuery(RobFormC robFormC) {
		// TODO Auto-generated method stub

		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormC.query.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.query.body", robFormC.getRobFormCCode(),
				robFormC.getBizName(), robFormC.getBrNo(), parser.format(new Date()));
	}

	@Override
	public void sendEmailFormCInReject(RobFormC robFormC) {
		// TODO Auto-generated method stub
		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormC.reject.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.reject.body", robFormC.getRobFormCCode(),
				robFormC.getBizName(), robFormC.getBrNo(), parser.format(new Date()));
	}

	@Override
	public void sendEmailFormCInApproved(RobFormC robFormC) {
		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormC.approved.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.approved.body", robFormC.getRobFormCCode(),
				robFormC.getBizName(), robFormC.getBrNo(), parser.format(new Date()));

	}

	@Override
	public void sendEmailFormCInProcess(RobFormC robFormC) {
		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormC.inProcess.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.inProcess.body", robFormC.getRobFormCCode(),
				robFormC.getBizName(), robFormC.getBrNo(), parser.format(new Date()));

	}

	@Override
	public void sendEmailPartnerReject(RobFormC robFormC, RobFormOwnerVerification robFormOwnerVerification) {
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());
		DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
		mailService.sendMail(lodgerUser.getEmail(), "email.notification.robFormC.partnerReject.subject",
				robFormC.getRobFormCCode() + ":" + robFormC.getBizName(), "email.notification.robFormC.partnerReject.body",
				robFormC.getRobFormCCode(), robFormC.getBizName(), lodgerUser.getName(), robFormOwnerVerification.getName(), robFormC.getBrNo(),
				parser.format(new Date()));

	}

	private RobFormC getFormCBusinessInfoDataFromWS(RobFormC robFormC) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.generateInfo");
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormC.getRobFormCCode());
			List<LlpPaymentTransactionDetail> listPaymentDetail = llpPaymentTransactionDetailService.findByTransactionIdAndPaymentItem(
					paymentTransaction.getTransactionId(), Parameter.PAYMENT_TYPE_BUSINESS_INFO);

			LlpPaymentTransactionDetail paymentDetail = listPaymentDetail.get(0);

			LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
			String recieptNo = paymentReceipt.getReceiptNo();
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setTotalAmount(BigDecimal.valueOf(paymentDetail.getAmountWithOutGst()));
			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormC.getRobFormCCode());
			param.setGstAmount(BigDecimal.valueOf(paymentDetail.getGstAmt()));

			List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();

			SSMInfoReq infoReq = new SSMInfoReq();
			infoReq.setEntityNo(robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
			infoReq.setEntityType(InfoParameter.ENTITY_TYPE_ROB);
			infoReq.setInfoType(InfoParameter.ROB_INFO);
			infoReq.setLanguage(InfoParameter.LANG_BM);
			infoReq.setQuantity(1);
			ssmInfoReqList.add(infoReq);
			param.setSSMInfoListReq(ssmInfoReqList.toArray(new SSMInfoReq[0]));

			SSMInfoGenerateResp certResponse = SSMInfoClient.generateInfo(url, param);
			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				LlpFileData bisInfoFileData = new LlpFileData();
				bisInfoFileData.setFileData(bizInfo[0].getPdfInfo());
				bisInfoFileData.setFileDataType("PDF");
				llpFileDataService.insert(bisInfoFileData);
				robFormC.setBusinessInfoDataId(bisInfoFileData.getFileDataId());
				update(robFormC);
			} else if (SSMInfoException.INVOICE_INVALID_CODE.equals(certResponse.getSuccessCode())) {
				return getRegenerateFormCBusinessInfoDataFromWS(robFormC, recieptNo);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormC;
	}

	private RobFormC getRegenerateFormCBusinessInfoDataFromWS(RobFormC robFormC, String recieptNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.reprintInfo");
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormC.getRobFormCCode());

			SSMInfoGenerateResp certResponse = SSMInfoClient.reprintInfo(url, param);

			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				LlpFileData bisInfoFileData = new LlpFileData();
				bisInfoFileData.setFileData(bizInfo[0].getPdfInfo());
				bisInfoFileData.setFileDataType("PDF");
				llpFileDataService.insert(bisInfoFileData);
				robFormC.setBusinessInfoDataId(bisInfoFileData.getFileDataId());
				update(robFormC);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormC;
	}

	@Override
	@Transactional(readOnly = true)
	public RobFormC findByIdWithData(String robFormCCode) throws SSMException {
		RobFormC robFormC;
		byte[] certBusinessInfo = null;
		byte[] cmpCData = null;
		byte[] borangCData = null;
		try {
			robFormC = robFormCDao.findById(robFormCCode);

			try {
				LlpFileData llpFileData = new LlpFileData();
				llpFileData = llpFileDataService.findById(robFormC.getFormCDataId());
				borangCData = llpFileData.getFileData();
			} catch (Exception e) {
				System.out.println("No borang C Data");
			}

			if (Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())) {
				try {
					LlpFileData llpFileData = new LlpFileData();
					llpFileData = llpFileDataService.findById(robFormC.getBusinessInfoDataId());
					certBusinessInfo = llpFileData.getFileData();
				} catch (Exception e) {
					System.out.println("No BusinessInfo C Data");
				}
			}

			if (robFormC.getCmpAmt() > 0) {
				try {
					LlpFileData llpFileData = new LlpFileData();
					llpFileData = llpFileDataService.findById(robFormC.getCmpDataId());
					cmpCData = llpFileData.getFileData();
				} catch (Exception e) {
					System.out.println("No Cmp C Data");
				}
			}

		} catch (Exception e) {
			robFormC = robFormCDao.findById(robFormCCode);
		}

		try {
			if (borangCData == null || borangCData.length == 0) {
				robFormC = generateBorangCForm(robFormC);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())) {
			try {
				if (certBusinessInfo == null || certBusinessInfo.length == 0) {
					robFormC = getFormCBusinessInfoDataFromWS(robFormC);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (robFormC.getCmpAmt() > 0) {
			try {
				if (cmpCData == null || cmpCData.length == 0) {
					robFormC = getFormCCompoundDataFromWS(robFormC);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return robFormC;
	}

	private RobFormC getFormCCompoundDataFromWS(RobFormC robFormC) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getCompoundFormCNotice");

			FindCompoundNoticeByCompoundNoReq param = new FindCompoundNoticeByCompoundNoReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setBrNo(robFormC.getBrNo());
			param.setCompoundNumber(robFormC.getCmpNo());

			FindCompoundNoticeByCompoundNoResp compoundNoResp = RobClient.getCompoundFormCNotice(url, param);

			if ("00".equals(compoundNoResp.getSuccessCode())) {
				LlpFileData cmpFilePdf = new LlpFileData();
				cmpFilePdf.setFileData(compoundNoResp.getBarcodeImage());
				cmpFilePdf.setFileDataType("PDF");
				llpFileDataService.insert(cmpFilePdf);
				robFormC.setCmpDataId(cmpFilePdf.getFileDataId());
				update(robFormC);
			} else {
				throw new SSMException(compoundNoResp.getSuccessCode() + ":" + compoundNoResp.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		}
		return robFormC;

	}

	@Override
	public RobFormC generateBorangCForm(RobFormC robFormC) {

		LlpFileUpload fileUpload = llpFileUploadService.findByFileCode(Parameter.ROB_C_FORM_TEMPLATE);
		LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robFormC.getCreateBy());

		List<RobFormOwnerVerification> listRobFormOwnerVerification = robFormOwnerVerificationService.getListRobFormOwnerVerification(robFormC
				.getRobFormCCode());

		Map mapData = new HashMap();
		mapData.put("llpProfile", profile);
		mapData.put("robFormC", robFormC);
		mapData.put("listRobFormOwnerVerification", listRobFormOwnerVerification);

		try {
			byte bytePdfBorangC[] = LLPOdtUtils.generatePdf(fileUpload.getFileData(), mapData);
			if (bytePdfBorangC.length > 0) {
				LlpFileData formCPdf = new LlpFileData();
				formCPdf.setFileData(bytePdfBorangC);
				formCPdf.setFileDataType("PDF");
				llpFileDataService.insert(formCPdf);
				robFormC.setFormCDataId(formCPdf.getFileDataId());
				update(robFormC);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return robFormC;
	}

	@Override
	public List<BusinessInfo> findListRobActiveByIcWS(String icNo) throws SSMException {

		String url = wSManagementService.getWsUrl("RobClient.findActiveBusinessByICNo");

		FindBusinessByICNoReq param = new FindBusinessByICNoReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setIcNo(icNo);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

		List<BusinessInfo> listBizInfo = null;
		try {

			BusinessInfoResp ssmWsResp = RobClient.findActiveBusinessByICNo(url, param);
			if (!"00".equals(ssmWsResp.getSuccessCode())) {
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			BusinessInfo[] arrayBizInfo = (BusinessInfo[]) ssmWsResp.getListBusinessInfo();

			listBizInfo = Arrays.asList(arrayBizInfo);

		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}

		return listBizInfo;

	}

	@Override
	public List<RobFormC> findPendingApplication(String brNo) {
		
		String[] statusNeeded = {Parameter.ROB_FORM_C_STATUS_DATA_ENTRY, Parameter.ROB_FORM_C_STATUS_IN_PROCESS, Parameter.ROB_FORM_C_STATUS_OTC,Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS
				,Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT,Parameter.ROB_FORM_C_STATUS_QUERY};
		
		SearchCriteria sc = new SearchCriteria("status", SearchCriteria.IN);
		sc.setValues(statusNeeded);
		SearchCriteria sc2 = new SearchCriteria("brNo", SearchCriteria.EQUAL, brNo);
		SearchCriteria sc3 = new SearchCriteria(sc, SearchCriteria.AND, sc2);
		
		return findByCriteria(sc3).getList();
	}

	@Transactional
	@Override
	public void discardApp(RobFormC robFormC) throws SSMException {
		String statusToCheck[] = new String[]{Parameter.ROB_FORM_C_STATUS_QUERY,Parameter.ROB_FORM_C_STATUS_APPROVED,Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_C_STATUS_REJECT,Parameter.ROB_FORM_C_STATUS_IN_PROCESS};
		Set<String> setStatus = new HashSet<String>();
		setStatus.addAll(Arrays.asList(statusToCheck));
		if(setStatus.contains(robFormC.getStatus())){
			throw new SSMException("error.robFormC.statusNotAllowDiscard",robFormC.getStatus());
		}
		boolean hasPendingNSuccess = llpPaymentTransactionService.hasPendingOnlineAndSuccessPaymentByAppRefNo(robFormC.getRobFormCCode());
		if(!hasPendingNSuccess){
			llpPaymentTransactionService.cancelPendingOtcByAppRefNo(robFormC.getRobFormCCode());
			robFormC.setStatus(Parameter.ROB_FORM_B_STATUS_DISCARD);
			update(robFormC);
		}else{
			throw new SSMException("error.robFormC.hasPendingOrSuccess");
		}
	}

	@Override
	public void lodgeFormCWoPayment(RobFormC robFormC) {
		robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT);
		update(robFormC);
		try {
			lodgeFormCWs(robFormC);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
