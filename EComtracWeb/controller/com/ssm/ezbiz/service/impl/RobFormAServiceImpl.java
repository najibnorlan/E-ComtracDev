package com.ssm.ezbiz.service.impl;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobFormADao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobFormABizCodeService;
import com.ssm.ezbiz.service.RobFormABranchesService;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSpecialKeyword;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpSpecialKeywordService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.dao.RobIncentiveDao;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.client.SSMInfoClient;
import com.ssm.webis.param.BizProfileDetReq;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessFormACertificateReq;
import com.ssm.webis.param.BusinessFormACertificateResp;
import com.ssm.webis.param.BusinessFormALodgeReq;
import com.ssm.webis.param.BusinessFormALodgeResp;
import com.ssm.webis.param.BusinessFormAOwnerValidReq;
import com.ssm.webis.param.BusinessFormAOwnerValidResp;
import com.ssm.webis.param.BusinessNameCheckReq;
import com.ssm.webis.param.BusinessNameCheckResp;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoResp;
import com.ssm.webis.param.InfoParameter;
import com.ssm.webis.param.RobFormABranchesParam;
import com.ssm.webis.param.RobFormAOwnerParam;
import com.ssm.webis.param.RobFormAParam;
import com.ssm.webis.param.RobFormAPaymentInfoParam;
import com.ssm.webis.param.SSMInfoException;
import com.ssm.webis.param.SSMInfoGenerateReq;
import com.ssm.webis.param.SSMInfoGenerateResp;
import com.ssm.webis.param.SSMInfoReq;
import com.ssm.webis.param.SSMInfoResp;

@Service
public class RobFormAServiceImpl extends BaseServiceImpl<RobFormA, String> implements RobFormAService, PaymentInterface {

	@Autowired
	RobFormADao robFormADao;
	
	@Autowired
	RobIncentiveDao robIncentiveDao;

	@Autowired
	@Qualifier("RobFormABranchesService")
	RobFormABranchesService robFormABranchesService;

	@Autowired
	@Qualifier("RobFormAOwnerService")
	RobFormAOwnerService robFormAOwnerService;

	@Autowired
	@Qualifier("RobFormABizCodeService")
	RobFormABizCodeService robFormABizCodeService;

	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	@Qualifier("RobUserOkuService")
	RobUserOkuService robUserOkuService;

	@Autowired
	@Qualifier("LlpFileDataService")
	LlpFileDataService llpFileDataService;

	@Autowired
	@Qualifier("RobFormNotesService")
	RobFormNotesService robFormNotesService;

	@Autowired
	@Qualifier("LlpPaymentFeeService")
	LlpPaymentFeeService llpPaymentFeeService;
	
	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;


	@Autowired
	@Qualifier("LlpPaymentTransactionDetailService")
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	@Autowired
	@Qualifier("LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	@Qualifier("LlpUserLogService")
	LlpUserLogService llpUserLogService;

	@Autowired
	@Qualifier("LlpFileUploadService")
	LlpFileUploadService llpFileUploadService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;

	@Autowired
	@Qualifier("LlpSpecialKeywordService")
	LlpSpecialKeywordService llpSpecialKeywordService;

	@Override
	public BaseDao getDefaultDao() {
		return robFormADao;
	}

	@Override
	@Transactional
	public void insertUpdateAll(RobFormA robFormA) {
		if (robFormA.getBizPartnershipAgreementDate() != null) {
			robFormA.setIsHasPartnershipAgreement(true);
		} else {
			robFormA.setIsHasPartnershipAgreement(false);
		}
		List<RobFormABranches> listABranches = robFormA.getListRobFormABranches();
		List<RobFormAOwner> listAOwner = robFormA.getListRobFormAOwner();
		List<RobFormABizCode> listABizCode = robFormA.getListRobFormABizCode();
		boolean isNew = false;
		if (StringUtils.isBlank(robFormA.getRobFormACode())) {
			insert(robFormA);
			isNew = true;
		} else {
			update(robFormA);
		}

		long setBranchId[] = new long[listABranches.size()];
		for (int i = 0; i < listABranches.size(); i++) {
			RobFormABranches robFormABranches = listABranches.get(i);
			robFormABranches.setRobFormACode(robFormA.getRobFormACode());
			if (robFormABranches.getRobFormABranchesId() == 0) {
				robFormABranchesService.insert(robFormABranches);
			} else {
				robFormABranchesService.update(robFormABranches);
			}
			setBranchId[i] = robFormABranches.getRobFormABranchesId();
		}

		long setOwnerId[] = new long[listAOwner.size()];
		String ownership = Parameter.ROB_OWNERSHIP_SOLE;
		if (listAOwner.size() > 1) {
			ownership = Parameter.ROB_OWNERSHIP_PARTNERSHIP;
		}
		for (int i = 0; i < listAOwner.size(); i++) {
			RobFormAOwner robFormAOwner = listAOwner.get(i);
			robFormAOwner.setRobFormACode(robFormA.getRobFormACode());
			robFormAOwner.setOwnershiptype(ownership);
			if (robFormAOwner.getRobFormAOwnerId() == 0) {
				robFormAOwnerService.insert(robFormAOwner);
				if (!robFormAOwner.getEzbizLoginName().equals(robFormA.getCreateBy())) {
					sendEmailAddAsPartner(robFormA, robFormAOwner);
				}
			} else {
				robFormAOwnerService.update(robFormAOwner);
			}
			setOwnerId[i] = robFormAOwner.getRobFormAOwnerId();
		}

		List<RobFormAOwner> listOwnerToDelete = robFormAOwnerService.findOwnerNotInId(robFormA.getRobFormACode(), setOwnerId);
		for (int i = 0; i < listOwnerToDelete.size(); i++) {
			sendEmailRemoveAsPartner(robFormA, listOwnerToDelete.get(i));
		}

		long setBizCodeId[] = new long[listABizCode.size()];
		for (int i = 0; i < listABizCode.size(); i++) {
			RobFormABizCode robFormABizCode = listABizCode.get(i);
			robFormABizCode.setRobFormACode(robFormA.getRobFormACode());
			if (robFormABizCode.getRobFormABizCodeId() == 0) {
				robFormABizCodeService.insert(robFormABizCode);
			} else {
				robFormABizCodeService.update(robFormABizCode);
			}
			setBizCodeId[i] = robFormABizCode.getRobFormABizCodeId();
		}
		if (!isNew) {
			robFormABranchesService.deleteNotInId(robFormA.getRobFormACode(), setBranchId);
			robFormAOwnerService.deleteNotInId(robFormA.getRobFormACode(), setOwnerId);
			robFormABizCodeService.deleteNotInId(robFormA.getRobFormACode(), setBizCodeId);
		}
	}

	@Override
	public RobFormA findAllById(String robFormARefNo) {
		RobFormA robFormA = robFormADao.findById(robFormARefNo);
		List<RobFormABranches> listBranches = robFormABranchesService.findByRobFormACode(robFormARefNo);
		List<RobFormAOwner> listOwner = robFormAOwnerService.findByRobFormACode(robFormARefNo);
		List<RobFormABizCode> listBizCode = robFormABizCodeService.findByRobFormACode(robFormARefNo);
		robFormA.setListRobFormABranches(listBranches);
		robFormA.setListRobFormAOwner(listOwner);
		robFormA.setListRobFormABizCode(listBizCode);
		if (robFormA.getSupportingDocData() != null) {
			LlpFileData suppDoc = llpFileDataService.findById(robFormA.getSupportingDocData().getFileDataId());
			robFormA.setSupportingDocData(suppDoc);
		}

		return robFormA;
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobFormA robFormATmp = (RobFormA) obj;
		RobFormA robFormA = robFormADao.findById(robFormATmp.getRobFormACode());
		obj = robFormA;
		System.out.println("SUCCESS PAYMENT");
		if (Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT.equals(robFormA.getStatus())) {
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
			robFormA.setPaymentDt(paymentTransaction.getCreateDt());

			robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
			robFormADao.update(robFormA);
		}
	}

	@Override
	@Transactional
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		System.out.println("SUCCESS NOTIFICATION");
		RobFormA robFormATmp = (RobFormA) obj;
		RobFormA robFormA = findAllById(robFormATmp.getRobFormACode());
		obj = robFormA;
		lodgeFormAWs(robFormA);
		
//		if(Parameter.ROB_FORM_A_INCENTIVE_oku.equals(robFormA.getIncentive())){ //to cater pay otc..
//			insertRobIncentiveFormA(robFormA);
//		}
	}

	@Override
	@Transactional
	public void lodgeFormAWs(RobFormA robFormA) throws SSMException {
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormA.getRobFormACode());
		if (llpPaymentTransaction == null) {
			throw new SSMException("No Success payment ,Cannot Lodge A:" + robFormA.getRobFormACode());
		}

		String url = wSManagementService.getWsUrl("RobClient.lodgeFormA");
		BusinessFormALodgeReq reqParam = prepareFormAParamData(robFormA);

		try {
			BusinessFormALodgeResp resp = RobClient.lodgeFormA(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_IN_PROCESS);
				robFormA.setSubmitDt(new Date());
				robFormADao.update(robFormA);
				
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equals(robFormA.getIncentive())){
					insertRobIncentiveFormA(robFormA); //jika kueri (relodgeFormAWs) tak update sbb lps jwb kueri jd IP semula dan scheduler akan kemaskini semula robIncentive bersama formA..
				}
				sendEmailFormAInProcess(robFormA);
			} else {
//				System.err.println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
				throw new SSMException("err.webis.lodgeFormAWs",resp.getErrorMsg());
			}
		} catch (Exception e) {
//			System.err.println(e.getMessage());
			throw new SSMException("err.webis.lodgeFormAWs",e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public void insertRobIncentiveFormA(RobFormA robFormA) throws SSMException { //insert after lodge to CBSROB
		
		List <RobIncentive> listRobIncentive = new ArrayList<RobIncentive>();
		for (int i = 0; i < robFormA.getListRobFormAOwner().size(); i++) {
			RobIncentive robIncentive = new RobIncentive();
			RobFormAOwner formAOwnerOku = robFormA.getListRobFormAOwner().get(i);

			//LlpUserProfile llpUserProfileOku =llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, formAOwnerOku.getNewicno()); //semua partner mesti sudah daftar ezbiz (status A)
			//RobUserOku robUserOku = robUserOkuService.findOkuByUserRefNo(llpUserProfileOku.getUserRefNo()); //find latest record (semua partner ada sudah bdaftar - validation kat EditRobFormAPage).
			RobUserOku robUserOku = robUserOkuService.findOkuByIdTypeAndIdNo(Parameter.ID_TYPE_newic, formAOwnerOku.getNewicno());
			
			//robIncentive.setUserRefNo(llpUserProfileOku.getUserRefNo());
			robIncentive.setUserRefNo(robUserOku.getUserProfile().getUserRefNo());
			robIncentive.setIdNo(formAOwnerOku.getNewicno());
			robIncentive.setOkuRefNo(robUserOku.getOkuRefNo());
			robIncentive.setIncentiveType(Parameter.ROB_FORM_A_INCENTIVE_oku);
			robIncentive.setIncentiveForm(Parameter.ROB_FORM_TYPE_A);
			robIncentive.setRobFormCode(robFormA.getRobFormACode());
			robIncentive.setIncentiveApplicationDt(robFormA.getSubmitDt());
			robIncentive.setProcessingBranch(robFormA.getProsessingBranch()); //later update  by RobSchedulerServiceImpl (approval branch)
			robIncentive.setBrNo(""); //later scheduler will update after approval
			robIncentive.setCheckDigit("");//later scheduler will update after approval
			robIncentive.setIncentiveApprovalDt(robFormA.getApproveRejectDt());//later scheduler will update after approval
			robIncentive.setApplicationStatus(robFormA.getStatus());//later scheduler will update after approval (current status IP)
			listRobIncentive.add(robIncentive);
			//robIncentiveDao.insert(robIncentive);
		}
		robIncentiveDao.insertAll(listRobIncentive);
		
	}

	private BusinessFormALodgeReq prepareFormAParamData(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());

		LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormA.getRobFormACode());
		LlpPaymentReceipt reciept = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());

		BusinessFormALodgeReq param = new BusinessFormALodgeReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

		String processBranch = Parameter.ROB_AGENCY_BRANCH_CODE;
		if (reciept.getCounterSessionId() != null) {
			processBranch = reciept.getCounterSessionId().getBranch();
			robFormA.setProsessingBranch(processBranch);
			robFormADao.update(robFormA);
		}

		if (StringUtils.isBlank(robFormA.getProsessingBranch())) {
			robFormA.setProsessingBranch(processBranch);
			robFormADao.update(robFormA);
		}

		param.setAgencyBranchCode(robFormA.getProsessingBranch());
		param.setIsFromSSMPc(robFormA.getIsFromSSMPc());
		param.setReqRefNo(robFormA.getRobFormACode());
		
		
		// Payment Info
		RobFormAPaymentInfoParam paramPayment = new RobFormAPaymentInfoParam();
		paramPayment.setPaymentAmt(robFormA.getTotalAmt());
		param.setRobFormAPaymentInfoParam(paramPayment);

		// Param Info
		RobFormAParam formAParam = new RobFormAParam();
		formAParam.setBranchCode(Parameter.EZBIZ_BRANCH_CODE);// MB
		formAParam.setFormARefNo(robFormA.getRobFormACode());
		formAParam.setApplicantName(userProfile.getName());
		formAParam.setApplicantIC(userProfile.getIdNo());
		formAParam.setApplicantICColor("");
		String addr = userProfile.getAdd1();
		if (StringUtils.isNotBlank(userProfile.getAdd2())) {
			addr += "\n" + userProfile.getAdd2();
		}
		if (StringUtils.isNotBlank(userProfile.getAdd3())) {
			addr += "\n" + userProfile.getAdd3();
		}
		formAParam.setApplicantAddress(addr);
		formAParam.setApplicantTown(userProfile.getCity());
		formAParam.setApplicantPostcode(userProfile.getPostcode());
		formAParam.setApplicantState(userProfile.getState());
		formAParam.setApplicantEmail(userProfile.getEmail());
		formAParam.setApplicantTelNo(userProfile.getHpNo());
		formAParam.setApplicantFaxNo(userProfile.getFaxNo());

		formAParam.setNameType(robFormA.getNameType());
		formAParam.setBizName(robFormA.getBizName());
		formAParam.setBizDesc(robFormA.getBizDesc());
		formAParam.setBizStartDt(robFormA.getBizStartDt());
		formAParam.setBizRegPeriod(robFormA.getBizRegPeriod());
		formAParam.setIsPartnershipAgreement("N");
		if (robFormA.getIsHasPartnershipAgreement()) {
			formAParam.setIsPartnershipAgreement("Y");
			formAParam.setBizPartnershipAgreementDate(robFormA.getBizPartnershipAgreementDate());
		}
		formAParam.setBizMainAddr(robFormA.getBizMainAddr());
		formAParam.setBizMainAddr2(robFormA.getBizMainAddr2());
		formAParam.setBizMainAddr3(robFormA.getBizMainAddr3());
		formAParam.setBizMainAddrTown(robFormA.getBizMainAddrTown());
		formAParam.setBizMainAddrPostcode(robFormA.getBizMainAddrPostcode());
		formAParam.setBizMainAddrState(robFormA.getBizMainAddrState());
		formAParam.setBizMainAddrMobileNo(robFormA.getBizMainAddrMobileNo());
		formAParam.setBizMainAddrTelNo(robFormA.getBizMainAddrTelNo());
		formAParam.setBizMainAddrEmail(robFormA.getBizMainAddrEmail());
		formAParam.setBizMainAddrUrl(robFormA.getBizMainAddrUrl());
		formAParam.setIsMainAddSamePostAddr("N");

		formAParam.setBizPostAddr(robFormA.getBizPostAddr());
		formAParam.setBizPostAddr2(robFormA.getBizPostAddr2());
		formAParam.setBizPostAddr3(robFormA.getBizPostAddr3());
		formAParam.setBizPostAddrTown(robFormA.getBizPostAddrTown());
		formAParam.setBizPostAddrPostcode(robFormA.getBizPostAddrPostcode());
		formAParam.setBizPostAddrState(robFormA.getBizPostAddrState());
		formAParam.setBizPostAddrMobileNo(robFormA.getBizPostAddrMobileNo());
		formAParam.setBizPostAddrTelNo(robFormA.getBizPostAddrTelNo());
		formAParam.setBizPostAddrEmail(robFormA.getBizPostAddrEmail());
		formAParam.setIncentive(robFormA.getIncentive());

		// Business Code
		List<RobFormABizCode> listRobFormABizCode = robFormA.getListRobFormABizCode();
		String[] robFormABusinessCodeParamList = new String[listRobFormABizCode.size()];
		for (int i = 0; i < listRobFormABizCode.size(); i++) {
			robFormABusinessCodeParamList[i] = listRobFormABizCode.get(i).getBizCode();
		}
		formAParam.setRobFormABusinessCodeParamList(robFormABusinessCodeParamList);

		// Rob Branches
		List<RobFormABranches> listRobFormABranches = robFormA.getListRobFormABranches();
		RobFormABranchesParam[] robBranch = new RobFormABranchesParam[listRobFormABranches.size()];
		for (int i = 0; i < listRobFormABranches.size(); i++) {
			robBranch[i] = new RobFormABranchesParam();
			robBranch[i].setAddr(listRobFormABranches.get(i).getAddr());
			robBranch[i].setAddr2(listRobFormABranches.get(i).getAddr2());
			robBranch[i].setAddr3(listRobFormABranches.get(i).getAddr3());
			robBranch[i].setAddrPostcode(listRobFormABranches.get(i).getAddrPostcode());
			robBranch[i].setAddrState(listRobFormABranches.get(i).getAddrState());
			robBranch[i].setAddrTown(listRobFormABranches.get(i).getAddrTown());
			robBranch[i].setAddrUrl(listRobFormABranches.get(i).getAddrUrl());
		}
		formAParam.setRobFormABranchesParamList(robBranch);

		// Rob Owner
		Map<String,String> mapNationalityCodeEzbizToCBS = llpParametersService.findActiveCodeTypeAsMap(Parameter.NATIONALITY_TYPE_CBS_MAPPING);
		
		List<RobFormAOwner> listRobFormAOwner = robFormA.getListRobFormAOwner();
		RobFormAOwnerParam[] robOwner = new RobFormAOwnerParam[listRobFormAOwner.size()];
		String ownershipType = "T";
		if (listRobFormAOwner.size() > 1) {
			ownershipType = "U";
		}
		for (int i = 0; i < listRobFormAOwner.size(); i++) {
			robOwner[i] = new RobFormAOwnerParam();
			robOwner[i].setName(listRobFormAOwner.get(i).getName());
			robOwner[i].setGender(listRobFormAOwner.get(i).getGender());
			robOwner[i].setNewIcNo(listRobFormAOwner.get(i).getNewicno());
			robOwner[i].setDob(listRobFormAOwner.get(i).getDob());
			robOwner[i].setAddr(listRobFormAOwner.get(i).getAddr());
			robOwner[i].setAddr2(listRobFormAOwner.get(i).getAddr2());
			robOwner[i].setAddr3(listRobFormAOwner.get(i).getAddr3());
			robOwner[i].setAddrPostcode(listRobFormAOwner.get(i).getAddrPostcode());
			robOwner[i].setAddrTown(listRobFormAOwner.get(i).getAddrTown());
			robOwner[i].setAddrState(listRobFormAOwner.get(i).getAddrState());
			robOwner[i].setAlreadyGetPartnerConcent("Y");
			robOwner[i].setEmail(listRobFormAOwner.get(i).getEmail());
			robOwner[i].setRace(listRobFormAOwner.get(i).getRace());
			robOwner[i].setOtherRace(StringUtils.isNotBlank(listRobFormAOwner.get(i).getOtherrace()) ? listRobFormAOwner.get(i).getOtherrace() : ""); //otherrace not mandatory field
			robOwner[i].setNationality(mapNationalityCodeEzbizToCBS.get(listRobFormAOwner.get(i).getNationality()));
			robOwner[i].setTelNo(listRobFormAOwner.get(i).getTelNo());
			robOwner[i].setMobileNo(listRobFormAOwner.get(i).getMobileNo());
			robOwner[i].setOwnershipType(ownershipType);// PEMILIKAN TUNGGAL T |
														// PERKONGSIAN U
		}
		formAParam.setRobFormAOwnerParamList(robOwner);

		param.setFormAParam(formAParam);
		return param;
	}

	@Override
	public List<SSMException> validateName(String bizNameStr , boolean checkWithCBS) {
		List<SSMException> listException = new ArrayList<SSMException>();
		if (StringUtils.isBlank(bizNameStr)) {
			return listException;
		}

		String mini2Word = bizNameStr.replaceAll("[^ a-zA-Z0-9]", "");
		while(mini2Word.indexOf("  ")!=-1){
			mini2Word = mini2Word.replaceAll("  ", " ");
		}
		String array2Word[] = StringUtils.split(mini2Word," ");
		
		//Check Minimum 2 Word
		if(array2Word.length<2){
			listException.add(new SSMException("reservedName.page.validate.name.minimum2Word"));
		}
		
		//Check Start With 1 Word
//		if(array2Word[0].trim().matches("[a-zA-Z]") && array2Word[0].trim().length()==1){
//			listException.add(new SSMException("reservedName.page.validate.name.firstWordLength1"));
//		}
		
		//Check Word With Number
		for (int i = 0; i < array2Word.length; i++) {
			boolean hasChar = false, hasNo = false;
			if(array2Word[i].length() != array2Word[i].replaceAll("[^a-zA-Z]", "").length()){
				hasChar = true;
			}
			if(array2Word[i].length() != array2Word[i].replaceAll("[^0-9]", "").length()){
				hasNo = true;
			}
			if(hasChar&&hasNo){
				listException.add(new SSMException("reservedName.page.validate.name.wordWithNumber"));
				break;
			}
		}
	
		
				
		//Check Double Space
		if(bizNameStr.indexOf("  ")!=-1){
			listException.add(new SSMException("reservedName.page.validate.name.doubleSpace"));
		}
		
		//Check Double Space
		if(bizNameStr.startsWith(" ")||bizNameStr.endsWith(" ")){
			listException.add(new SSMException("reservedName.page.validate.name.startEndWithSpace", bizNameStr));
		}
		
		//Check Prefix
		List<LlpParameters> listPrefix = llpParametersService.findByActiveCodeType(Parameter.NS_NOT_ALLOW_PREFIX);
		for (int i = 0; i < listPrefix.size(); i++) {
			if(bizNameStr.startsWith(listPrefix.get(i).getCode()+" ")){
				listException.add(new SSMException("reservedName.page.validate.name.prefixNotAllow", listPrefix.get(i).getCodeDesc()));
				break;
			}
		}
		//Check Postfix
		List<LlpParameters> listPostfix = llpParametersService.findByActiveCodeType(Parameter.NS_NOT_ALLOW_POSTFIX);
		for (int i = 0; i < listPostfix.size(); i++) {
			if(bizNameStr.endsWith(" "+listPostfix.get(i).getCode())){
				listException.add(new SSMException("reservedName.page.validate.name.postfixNotAllow", listPostfix.get(i).getCodeDesc()));
				break;
			}
		}
				
		// Check Symbol
		String originalName = bizNameStr;
		int length = originalName.length();
		
		String allowSymbol = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_NS_ALLOWED_SYMBOL);
		String applyName = bizNameStr.replaceAll("[^"+allowSymbol+"A-Za-z0-9 ]", "");
		if (applyName.length() != length) {
			String symbolDetect = "{";
			Set oriCharSet = new HashSet();
			for (int i = 0; i < originalName.length(); i++) {
				oriCharSet.add(originalName.charAt(i));
			}
			Set charSet = new HashSet();
			for (int i = 0; i < applyName.length(); i++) {
				charSet.add(applyName.charAt(i));
			}
			oriCharSet.removeAll(charSet);
			oriCharSet.remove(" ");
			oriCharSet.remove(null);

			for (Iterator iterator = oriCharSet.iterator(); iterator.hasNext();) {
				Character chr = (Character) iterator.next();
				if (StringUtils.isBlank(chr.toString())) {
					continue;
				}
				System.out.println(":" + chr.charValue() + ":");
				symbolDetect += chr;
				if (iterator.hasNext()) {
					symbolDetect += ", ";
				}
			}
			symbolDetect += "}";
			listException.add(new SSMException("reservedName.page.symbol", originalName, symbolDetect));
		}
		
		
		// Check state and country
		List<LlpParameters> listCountry = llpParametersService.findByActiveCodeType(Parameter.COUNTRY_CODE);
		List<LlpParameters> listState = llpParametersService.findByActiveCodeType(Parameter.STATE_CODE);

		for (int i = 0; i < listCountry.size(); i++) {
			LlpParameters llpParameter = listCountry.get(i);
			if (applyName.startsWith(llpParameter.getCodeDesc().toUpperCase())) {
				listException.add(new SSMException("reservedName.page.validate.name.countryName", originalName, llpParameter.getCodeDesc()));
				System.out.println(llpParameter.getCodeDesc());
			}
		}
		for (int i = 0; i < listState.size(); i++) {
			LlpParameters llpParameter = listState.get(i);
			if (applyName.startsWith(llpParameter.getCodeDesc().toUpperCase())) {
				listException.add(new SSMException("reservedName.page.validate.name.stateName", originalName, llpParameter.getCodeDesc()));
				System.out.println(llpParameter.getCodeDesc());
			}
		}

		// Special Gazette Offense
		List<LlpSpecialKeyword> listLlpSpecialKeyword = llpSpecialKeywordService.findByCriteria(new SearchCriteria()).getList();// find
																																// alll

		String offence = "";
		String gazette = "";
		String control = "";
		
		Set<String> offenseSet = new HashSet<String>();
		Set<String> gazetteSet = new HashSet<String>();
		Set<String> controlSet = new HashSet<String>();
		for (int i = 0; i < listLlpSpecialKeyword.size(); i++) {
			LlpSpecialKeyword llpSpecialKeyword = listLlpSpecialKeyword.get(i);
			String keyword = llpSpecialKeyword.getVchkeywords().toUpperCase();
			
			boolean match = false;
			if (applyName.startsWith(keyword)) {
				match = true;
			} else if (applyName.endsWith(keyword)) {
				match = true;
			} else {
				String wordSplit[] = StringUtils.splitByWholeSeparator(applyName, keyword);
				if (wordSplit.length > 1) {
					for (int j = 0; j < wordSplit.length; j++) {
						if (wordSplit[j].startsWith(" ") || wordSplit[j].endsWith(" ")) {
							match = true;
							break;
						}
					}
				}
			}
			if (match) {
				if (Parameter.NAME_SEARCH_TYPE_GAZZETED.equals(llpSpecialKeyword.getChrkeywordtype())) {
					keyword = keyword.trim();
					if(!gazetteSet.contains(keyword)) {
						gazetteSet.add(keyword);
						gazette += keyword + ", ";
					}
				} else if (Parameter.NAME_SEARCH_TYPE_CONTROL.equals(llpSpecialKeyword.getChrkeywordtype())) {
					keyword = keyword.trim();
					if(!controlSet.contains(keyword)) {
						controlSet.add(keyword);
						control += keyword + ", ";
					}
					
				} else if (Parameter.NAME_SEARCH_TYPE_OFFENCE.equals(llpSpecialKeyword.getChrkeywordtype())) {
					keyword = keyword.trim();
					if(!offenseSet.contains(keyword)) {
						offenseSet.add(keyword);
						offence += keyword + ", ";
					}
					
				}
			}
		}
		if (gazette.length() > 0) {
			gazette = "{ " + gazette.substring(0, gazette.length() - 2) + " }";
			listException.add(new SSMException("reservedName.page.gazetteName", originalName, gazette));
		}
		if (control.length() > 0) {
			control = "{ " + control.substring(0, control.length() - 2) + " }";
			listException.add(new SSMException("reservedName.page.controlWord", originalName, control));
		}
		if (offence.length() > 0) {
			offence = "{ " + offence.substring(0, offence.length() - 2) + " }";
			listException.add(new SSMException("reservedName.page.offensiveWord", originalName, offence));
		}
		
		if(listException.size()==0 && checkWithCBS){
			try {
				if(checkBusinessNameWs(bizNameStr)){//Name Exist
					listException.add(new SSMException("page.lbl.ezbiz.robFormA.nameInvalid", bizNameStr) );
				}
			} catch (Exception e) {
				listException.add(new SSMException("page.lbl.ezbiz.robFormA.wsError"));
			}
		}

		return listException;
	}

	private boolean checkBusinessNameWs(String bizNameStr) throws SSMException {

		try {
			String url = wSManagementService.getWsUrl("RobClient.checkBusinessName");

			BusinessNameCheckReq param = new BusinessNameCheckReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("NC" + System.currentTimeMillis());
			param.setBusinessName(bizNameStr);

			BusinessNameCheckResp resp = RobClient.checkBusinessName(url, param);

			if ("00".equals(resp.getSuccessCode())) {
				return resp.isNameValid();
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public RobFormA findByIdWithData(String robFormACode) throws SSMException {
		RobFormA robFormA;
		byte[] certByData = null;
		byte[] certBusinessInfo = null;
		byte[] borangAData = null;
		byte[] cmpNoticeData = null;
		try {
			robFormA = robFormADao.findByIdWithData(robFormACode);
			try {
				borangAData = robFormA.getFormAData().getFileData();
			} catch (Exception e) {
				System.out.println("No borang A Data");
				// e.printStackTrace();
			}
			try {
				certByData = robFormA.getCertFileData().getFileData();
			} catch (Exception e) {
				System.out.println("No Cert A Data");
				// e.printStackTrace();
			}
			if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
				try {
					certBusinessInfo = robFormA.getBusinessInfoData().getFileData();
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("No BusinessInfo A Data");
				}
			}
			if (StringUtils.isNotBlank(robFormA.getCompoundNo())) {
				try {
					cmpNoticeData = robFormA.getCmpNoticeFileData().getFileData();
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("No Cmp Data");
				}
			}
		} catch (Exception e) {
			robFormA = robFormADao.findById(robFormACode);
		}

		try {
			if (borangAData == null || borangAData.length == 0) {
				List<RobFormABranches> listBranches = robFormABranchesService.findByRobFormACode(robFormACode);
				List<RobFormAOwner> listOwner = robFormAOwnerService.findByRobFormACode(robFormACode);
				List<RobFormABizCode> listBizCode = robFormABizCodeService.findByRobFormACode(robFormACode);
				robFormA.setListRobFormABranches(listBranches);
				robFormA.setListRobFormAOwner(listOwner);
				robFormA.setListRobFormABizCode(listBizCode);
				robFormA = generateBorangAForm(robFormA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (certByData == null || certByData.length == 0) {
				robFormA = getFormACertDataFromWS(robFormA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
			try {
				if (certBusinessInfo == null || certBusinessInfo.length == 0) {
					robFormA = getFormABusinessInfoDataFromWS(robFormA);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isNotBlank(robFormA.getCompoundNo())) {
			try {
				robFormA = getCmpDataFromWS(robFormA);
			} catch (SSMException e) {
				e.printStackTrace();
			}
		}
		return robFormA;
	}

	private RobFormA getCmpDataFromWS(RobFormA robFormA) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getCompoundNotice");

			FindCompoundNoticeByCompoundNoReq param = new FindCompoundNoticeByCompoundNoReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			String brNo = StringUtils.split(robFormA.getBrNo(), "-")[0];
			param.setBrNo(brNo);
			param.setCompoundNumber(robFormA.getCompoundNo());
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

			FindCompoundNoticeByCompoundNoResp compoundNoResp = RobClient.getCompoundNotice(url, param);
			if ("00".equals(compoundNoResp.getSuccessCode())) {
				LlpFileData cmpFilePdf = new LlpFileData();
				cmpFilePdf.setFileData(compoundNoResp.getBarcodeImage());
				cmpFilePdf.setFileDataType("PDF");
				llpFileDataService.insert(cmpFilePdf);
				robFormA.setCmpNoticeFileData(cmpFilePdf);
				robFormADao.update(robFormA);

			} else {
				throw new SSMException(compoundNoResp.getSuccessCode() + ":" + compoundNoResp.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		}
		return robFormA;
	}

	@Override
	public RobFormA generateBorangAForm(RobFormA robFormA) {

		LlpFileUpload fileUpload = llpFileUploadService.findByFileCode(Parameter.ROB_A_FORM_TEMPLATE);
		LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());

		Map mapData = new HashMap();
		mapData.put("llpProfile", profile);
		mapData.put("robFormA", robFormA);

		try {
//			 FileInputStream fis = new FileInputStream("D:/workspaces/NewFramework/EzBizWeb/resources/FormA.odt");
//			 byte[] odtFormAData = new byte[fis.available()];
//			 fis.read(odtFormAData);
//			 fis.close();
			// byte bytePdfBorangA[] = LLPOdtUtils.generatePdf(odtFormAData,
			// mapData);
			byte bytePdfBorangA[] = LLPOdtUtils.generatePdf(fileUpload.getFileData(), mapData);
			if (bytePdfBorangA.length > 0) {
				// String file = "d:/borangA1111.pdf";
				// FileOutputStream fos = new FileOutputStream(file);
				// fos.write(bytePdfBorangA);
				// fos.close();
				// try {
				// Process p =
				// Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+file);
				// p.waitFor();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				LlpFileData formAPdf = new LlpFileData();
				formAPdf.setFileData(bytePdfBorangA);
				formAPdf.setFileDataType("PDF");
				llpFileDataService.insert(formAPdf);
				robFormA.setFormAData(formAPdf);
				update(robFormA);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return robFormA;
	}

	public static void main3(String[] args)throws Exception {
//		RobFormA robFormA = new RobFormA();
//		robFormA.setRobFormACode("EB-A2019062500858");
//		robFormA.setBrNo("PG0476742-T");

//		String url = "http://localhost:8082/SSMWEBIS_SEC/services";
//		String url = "http://ezbizwebisdev.ssm.com.my/EZBIZ/services";
//		String url = wSManagementService.getWsUrl("RobClient.getFormACertificate");

//		BusinessFormACertificateReq certificateBusinessInfoReq = new BusinessFormACertificateReq();
//		certificateBusinessInfoReq.setAgencyId(Parameter.ROB_AGENCY_ID);
//		String brNo = StringUtils.split(robFormA.getBrNo(), "-")[0];
//		certificateBusinessInfoReq.setBrNo(brNo);// br without check digit
//		certificateBusinessInfoReq.setFormARefNo(robFormA.getRobFormACode());
//
//		BusinessFormACertificateResp certResponse = RobClient.getFormACertificate(url, certificateBusinessInfoReq);
//		if ("00".equals(certResponse.getSuccessCode())) {
//			LlpFileData certFileData = new LlpFileData();
//			certFileData.setFileData(certResponse.getCertImage());
//			certFileData.setFileDataType("PDF");
//			System.out.println("ss");
//		}
		
//		SSMInfoGenerateReq param = new SSMInfoGenerateReq();
//
//		param.setTotalAmount(BigDecimal.valueOf(70));
//		param.setInvoiceNo("EB20190829000004");
//		param.setAgencyId(Parameter.ROB_AGENCY_ID);
//		param.setReqRefNo("EB-A2019082900004");
//
//		param.setGstAmount(new BigDecimal(0));
//		
//
//		List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();
//
//		SSMInfoReq infoReq = new SSMInfoReq();
//		infoReq.setEntityNo("002931517-T");
//		infoReq.setEntityType(InfoParameter.ENTITY_TYPE_ROB);
//		infoReq.setInfoType(InfoParameter.ROB_INFO);
//		infoReq.setLanguage(InfoParameter.LANG_BM);
//		infoReq.setQuantity(1);
//		ssmInfoReqList.add(infoReq);
//		param.setSSMInfoListReq(ssmInfoReqList.toArray(new SSMInfoReq[0]));
//
//		SSMInfoGenerateResp certResponse = SSMInfoClient.generateInfo(url, param);
//		if ("00".equals(certResponse.getSuccessCode())) {
//			SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
//			System.out.println(bizInfo[0].getPdfInfo().length);
//		}else {
//			System.out.println(certResponse.getErrorMsg());
//		}
//		
//		System.out.println("Done");
		
	}
	@Override
	public  RobFormA getFormACertDataFromWS(RobFormA robFormA) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getFormACertificate");

			BusinessFormACertificateReq certificateBusinessInfoReq = new BusinessFormACertificateReq();
			certificateBusinessInfoReq.setAgencyId(Parameter.ROB_AGENCY_ID);
			String brNo = StringUtils.split(robFormA.getBrNo(), "-")[0];
			certificateBusinessInfoReq.setBrNo(brNo);// br without check digit
			certificateBusinessInfoReq.setFormARefNo(robFormA.getRobFormACode());

			BusinessFormACertificateResp certResponse = RobClient.getFormACertificate(url, certificateBusinessInfoReq);
			if ("00".equals(certResponse.getSuccessCode())) {
				LlpFileData certFileData = new LlpFileData();
				certFileData.setFileData(certResponse.getCertImage());
				certFileData.setFileDataType("PDF");
				llpFileDataService.insert(certFileData);
				robFormA.setCertFileData(certFileData);
				update(robFormA);

			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormA;
	}
	
	public static void main(String[] args) throws Exception{
		SSMInfoGenerateReq param = new SSMInfoGenerateReq();

		param.setTotalAmount(BigDecimal.valueOf(10));
		param.setInvoiceNo("EB20180723001347");
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setReqRefNo("EB-A2018072300923");
		param.setGstAmount(BigDecimal.valueOf(0));
		
//		String bizInfoGstRate = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GST_CODE, paymentDetail.getGstCode());
//		param.setGstAmount(new BigDecimal(0));
//		if(StringUtils.isNotBlank(bizInfoGstRate)){
//			double gstAmt = paymentDetail.getAmountWithOutGst() * Double.valueOf(bizInfoGstRate);
//			param.setGstAmount(BigDecimal.valueOf(gstAmt));
//		}
		
//		String url = "http://swebissec.ssm.com.my/EZBIZ/services";
//		String url = "http://localhost:8082/SSMWEBIS_SEC/services";
		
//		String url = "http://ezbizwebisdev.ssm.com.my/EZBIZ/services";
		String url = "http://10.220.7.174:8080/SSMWEBIS_SEC/services";
		
		List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();

		SSMInfoReq infoReq = new SSMInfoReq();
		infoReq.setEntityNo("002931517-T");
		infoReq.setEntityType(InfoParameter.ENTITY_TYPE_ROB);
		infoReq.setInfoType(InfoParameter.ROB_INFO);
		infoReq.setLanguage(InfoParameter.LANG_BM);
		infoReq.setQuantity(1);
		ssmInfoReqList.add(infoReq);
		param.setSSMInfoListReq(ssmInfoReqList.toArray(new SSMInfoReq[0]));

		SSMInfoGenerateResp certResponse = SSMInfoClient.generateInfo(url, param);
		if ("00".equals(certResponse.getSuccessCode())) {
			SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
			System.out.println("sdsd");
			FileOutputStream fos = new FileOutputStream("d:/out.pdf");
			fos.write(bizInfo[0].getPdfInfo());
			fos.close();
		} else if (SSMInfoException.INVOICE_INVALID_CODE.equals(certResponse.getSuccessCode())) {
//			return getRegenerateFormABusinessInfoDataFromWS(robFormA, recieptNo);
		} else {
//			throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
		}
		System.out.println(certResponse.getErrorMsg());
	}
	
	@Override
	public RobFormA getFormABusinessInfoDataFromWS(RobFormA robFormA) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.generateInfo");
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormA.getRobFormACode());
			List<LlpPaymentTransactionDetail> listPaymentDetail = llpPaymentTransactionDetailService.findByTransactionIdAndPaymentItem(
					paymentTransaction.getTransactionId(), Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			
			LlpPaymentTransactionDetail paymentDetail = listPaymentDetail.get(0);

			LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
			String recieptNo = paymentReceipt.getReceiptNo();
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setTotalAmount(BigDecimal.valueOf(paymentDetail.getAmountWithOutGst()));
			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormA.getRobFormACode());

			String bizInfoGstRate = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GST_CODE, paymentDetail.getGstCode());
			param.setGstAmount(new BigDecimal(0));
			if(StringUtils.isNotBlank(bizInfoGstRate)){
				double gstAmt = paymentDetail.getAmountWithOutGst() * Double.valueOf(bizInfoGstRate);
				param.setGstAmount(BigDecimal.valueOf(gstAmt));
			}
			

			List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();

			SSMInfoReq infoReq = new SSMInfoReq();
			infoReq.setEntityNo(robFormA.getBrNo());
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
				robFormA.setBusinessInfoData(bisInfoFileData);
				update(robFormA);
			} else if (SSMInfoException.INVOICE_INVALID_CODE.equals(certResponse.getSuccessCode())) {
				return getRegenerateFormABusinessInfoDataFromWS(robFormA, recieptNo);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormA;
	}
	
	
	

	private RobFormA getRegenerateFormABusinessInfoDataFromWS(RobFormA robFormA, String recieptNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.reprintInfo");
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormA.getRobFormACode());

			SSMInfoGenerateResp certResponse = SSMInfoClient.reprintInfo(url, param);

			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				LlpFileData bisInfoFileData = new LlpFileData();
				bisInfoFileData.setFileData(bizInfo[0].getPdfInfo());
				bisInfoFileData.setFileDataType("PDF");
				llpFileDataService.insert(bisInfoFileData);
				robFormA.setBusinessInfoData(bisInfoFileData);
				update(robFormA);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormA;
	}

	@Override
	public BusinessFormAOwnerValidResp isOwnerValidWithIc(String name, String icNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.isOwnerValidWithIc");

			BusinessFormAOwnerValidReq businessFormAOwnerValidReq = new BusinessFormAOwnerValidReq();
			businessFormAOwnerValidReq.setAgencyId(Parameter.ROB_AGENCY_ID);
			businessFormAOwnerValidReq.setName(name);
			businessFormAOwnerValidReq.setIcNo(icNo);

			BusinessFormAOwnerValidResp resp = RobClient.isOwnerValidWithIc(url, businessFormAOwnerValidReq);
			if ("00".equals(resp.getSuccessCode())) {
				return resp;
			} else {
				System.err.println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
				throw new SSMException(resp.getSuccessCode(), resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
	@Override
	public BizProfileDetResp getBusinessProfileDetailWs(String brNo) throws SSMException{
		try {
			String url = wSManagementService.getWsUrl("RobClient.getBizProfileDet");
			
			BizProfileDetReq param = new BizProfileDetReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setBrNo(brNo);
			
			BizProfileDetResp bizProfileDet = RobClient.getBizProfileDet(url, param);
			if("00".equals(bizProfileDet.getSuccessCode())){
				return bizProfileDet;
			}else{
				throw new SSMException(bizProfileDet.getErrorMsg());
			}
		
	} catch (Exception e) {
		throw new SSMException(e);
	}
}

	@Transactional
	@Override
	public void reLodgeFormA(RobFormA robFormA) throws SSMException {
		reLodgeFormAWs(robFormA);
		RobFormNotes formNotes = robFormA.getListRobFormNotes().get(robFormA.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormA.getQueryAnswer());
		robFormNotesService.update(formNotes);
	}

	public void reLodgeFormAWs(RobFormA robFormA) throws SSMException {
		String url = wSManagementService.getWsUrl("RobClient.reLodgeFormA");
		BusinessFormALodgeReq reqParam = prepareFormAParamData(robFormA);

		RobFormNotes formNotes = robFormA.getListRobFormNotes().get(robFormA.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormA.getQueryAnswer());
		reqParam.getFormAParam().setNotes(formNotes.getNotes());
		reqParam.getFormAParam().setNotesAnswer(formNotes.getNotesAnswer());

		try {
			BusinessFormALodgeResp resp = RobClient.reLodgeFormA(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_IN_PROCESS);
				robFormA.setReSubmitDt(new Date());
				robFormADao.update(robFormA);
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}
	
	
	@Override
	public void sendEmailFormAInProcess(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.inProcess.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.inProcess.body", robFormA.getRobFormACode(),
				robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAInQuery(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.query.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.query.body", robFormA.getRobFormACode(),
				robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAInReject(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.reject.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.reject.body", robFormA.getRobFormACode(),
				robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAIncentiveVerifiedNoPayment(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.incentiveVerifiedNoPayment.subject", robFormA.getRobFormACode()
				+ ":" + robFormA.getBizName(), "email.notification.robFormA.incentiveVerifiedNoPayment.body", robFormA.getRobFormACode(),
				robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAIncentiveVerifiedPayment(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.incentiveVerifiedNoPayment.subject", robFormA.getRobFormACode()
				+ ":" + robFormA.getBizName(), "email.notification.robFormA.incentiveVerifiedPayment.body", robFormA.getRobFormACode(),
				robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAIncentiveReject(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.incentiveReject.subject", robFormA.getRobFormACode() + ":"
				+ robFormA.getBizName(), "email.notification.robFormA.incentiveReject.body", robFormA.getRobFormACode(), robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAIncentiveQuery(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.incentiveQuery.subject", robFormA.getRobFormACode() + ":"
				+ robFormA.getBizName(), "email.notification.robFormA.incentiveQuery.body", robFormA.getRobFormACode(), robFormA.getBizName());
	}

	@Override
	public void sendEmailFormAInApproved(RobFormA robFormA) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.approved.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.approved.body", robFormA.getRobFormACode(),
				robFormA.getBizName(), robFormA.getBrNo());
	}

	@Override
	public void sendEmailAddAsPartner(RobFormA robFormA, RobFormAOwner partner) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(partner.getEmail(), "email.notification.robFormA.addPartner.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.addPartner.body", robFormA.getRobFormACode(),
				robFormA.getBizName(), partner.getName(), userProfile.getName());
	}

	private void sendEmailRemoveAsPartner(RobFormA robFormA, RobFormAOwner partner) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(partner.getEmail(), "email.notification.robFormA.removePartner.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.remove.body", robFormA.getRobFormACode(),
				robFormA.getBizName(), partner.getName(), userProfile.getName());
	}

	@Override
	public void sendEmailPartnerAccept(RobFormA robFormA, RobFormAOwner partner) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.partnerAccept.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.partnerAccept.body",
				robFormA.getRobFormACode(), robFormA.getBizName(), partner.getName(), userProfile.getName());
	}

	@Override
	public void sendEmailPartnerReject(RobFormA robFormA, RobFormAOwner partner) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormA.partnerReject.subject",
				robFormA.getRobFormACode() + ":" + robFormA.getBizName(), "email.notification.robFormA.partnerReject.body",
				robFormA.getRobFormACode(), robFormA.getBizName(), partner.getName(), userProfile.getName());
	}

	@Override
	public RobFormA checkUserUsingIncentive(String userId) {

		String[] listIncentive = { Parameter.ROB_FORM_A_INCENTIVE_student, Parameter.ROB_FORM_A_INCENTIVE_oku };
		String[] statusToCheck = { Parameter.ROB_FORM_A_STATUS_CANCEL, Parameter.ROB_FORM_A_STATUS_REJECT, Parameter.ROB_FORM_A_STATUS_DATA_ENTRY };

		SearchCriteria sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, userId);
		sc = sc.andIfNotInNotNull("status", statusToCheck, false);
		sc = sc.andIfInNotNull("incentive", listIncentive, false);

		List<RobFormA> robFormAs = findByCriteria(sc).getList();
		if (robFormAs.size() > 0) {
			return robFormAs.get(0);
		}

		return null;
	}

	@Override
	public Integer countFormAByTypeAndStatus(String type, String status, String year, String month) {
		return robFormADao.countFormAByTypeAndStatus(type, status, year, month);
	}

	@Override
	public void updateFormBackToDraff(RobFormA robFormA) throws SSMException {
		boolean hasPendingNSuccess = llpPaymentTransactionService.hasPendingOnlineAndSuccessPaymentByAppRefNo(robFormA.getRobFormACode());
		if(!hasPendingNSuccess){
			llpPaymentTransactionService.cancelPendingOtcByAppRefNo(robFormA.getRobFormACode());
			robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY);
			update(robFormA);
		}else{
			throw new SSMException("error.robFormA.hasPendingOrSuccess");
		}
	}

	@Override
	@Transactional
	public void cancelPendingPaymentMoreThanXDays(Integer noOfDays) {
		robFormADao.cancelPendingPaymentMoreThanXDays(noOfDays);
	}

	@Override
	@Transactional
	public void cancelDraftMoreThanXDays(Integer noOfDays) {
		robFormADao.cancelDraftMoreThanXDays(noOfDays);
	}

	@Override
	public void updateBaseListDistribution(String[] listUpdatedTrans) {
		robFormADao.updateBaseListDistribution(listUpdatedTrans);
	}

}