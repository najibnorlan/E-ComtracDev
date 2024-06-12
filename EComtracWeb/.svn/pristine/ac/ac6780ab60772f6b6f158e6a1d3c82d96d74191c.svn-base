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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobFormBDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobFormB1Service;
import com.ssm.ezbiz.service.RobFormB2DetService;
import com.ssm.ezbiz.service.RobFormB2Service;
import com.ssm.ezbiz.service.RobFormB3Service;
import com.ssm.ezbiz.service.RobFormB4Service;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.MD5DigestUtils;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RobFormB4;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.client.SSMInfoClient;
import com.ssm.webis.param.BizProfileDetReq;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessFormACertificateReq;
import com.ssm.webis.param.BusinessFormACertificateResp;
import com.ssm.webis.param.BusinessFormBLodgeReq;
import com.ssm.webis.param.BusinessFormBLodgeResp;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoResp;
import com.ssm.webis.param.InfoParameter;
import com.ssm.webis.param.RobFormB1Param;
import com.ssm.webis.param.RobFormB2Param;
import com.ssm.webis.param.RobFormB3BranchesParam;
import com.ssm.webis.param.RobFormB3Param;
import com.ssm.webis.param.RobFormB4OwnersParam;
import com.ssm.webis.param.RobFormB4Param;
import com.ssm.webis.param.RobFormBPaymentInfoParam;
import com.ssm.webis.param.RobFormBranchInfo;
import com.ssm.webis.param.RobFormOwnerInfo;
import com.ssm.webis.param.SSMInfoException;
import com.ssm.webis.param.SSMInfoGenerateReq;
import com.ssm.webis.param.SSMInfoGenerateResp;
import com.ssm.webis.param.SSMInfoReq;
import com.ssm.webis.param.SSMInfoResp;

@Service
public class RobFormBServiceImpl extends BaseServiceImpl<RobFormB,  String> implements RobFormBService , PaymentInterface{
	
	@Autowired
	RobFormBDao robFormBDao;
	
	@Autowired
	RobFormB3Service robFormB3Service;
	
	@Autowired
	RobFormB4Service robFormB4Service;
	

	@Autowired
	private LlpParametersService llpParametersService;
	
	@Autowired
	private LlpUserProfileService llpUserProfileService;
	
	@Autowired
	private RobFormOwnerVerificationService  robFormOwnerVerificationService;
	
	@Autowired
	private RobFormB1Service robFormB1Service;
	
	@Autowired
	private RobFormB2DetService robFormB2DetService; 
	
	@Autowired
	private RobFormB2Service robFormB2Service;
	
	@Autowired
	private RobFormNotesService robFormNotesService; 
	

	@Autowired
	@Qualifier("LlpUserLogService")
	LlpUserLogService llpUserLogService;

	@Autowired
	@Qualifier("LlpFileDataService")
	LlpFileDataService llpFileDataService;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;

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
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormBDao;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RobFormB findByIdWithData(String robFormBCode) throws SSMException {
		RobFormB robFormB;
		byte[] certByData = null;
		byte[] certBusinessInfo = null;
		byte[] borangBData = null;
		byte[] cmpNoticeData = null;
		try {
			robFormB = robFormBDao.findByIdWithData(robFormBCode);
			
			try {
				borangBData = robFormB.getFormBData().getFileData();
			} catch (Exception e) {
				System.out.println("No borang B Data");
				// e.printStackTrace();
			}
			try {
				certByData = robFormB.getCertFileData().getFileData();
			} catch (Exception e) {
				System.out.println("No Cert B Data");
				// e.printStackTrace();
			}
			if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())) {
				try {
					certBusinessInfo = robFormB.getBusinessInfoData().getFileData();
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("No BusinessInfo B Data");
				}
			}
			if (StringUtils.isNotBlank(robFormB.getCmpNo())) {
				try {
					cmpNoticeData = robFormB.getCmpNoticeFileData().getFileData();
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("No Cmp Data");
				}
			}
		} catch (Exception e) {
			robFormB = robFormBDao.findById(robFormBCode);
		}

		//TO DO form B generation
		
		try {
			if (certByData == null || certByData.length == 0) {
				robFormB = getCertFileFromWS(robFormB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())) {
			try {
				if (certBusinessInfo == null || certBusinessInfo.length == 0) {
					robFormB = getBizInfoFromWS(robFormB);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isNotBlank(robFormB.getCmpNo())) {
			try {
				robFormB = getCmpDataFromWS(robFormB);
			} catch (SSMException e) {
				e.printStackTrace();
			}
		}
		return robFormB;
	}
	
	
	
	
	@Override
	public RobFormB findAllByIdWithWS(String robFormBCode) {
		RobFormB robFormB = findAllById(robFormBCode);
		
		try {
			String url = wSManagementService.getWsUrl("RobClient.getBizProfileDet");
			
			BizProfileDetReq param = new BizProfileDetReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setBrNo(robFormB.getBrNo());
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			
			BizProfileDetResp ssmWsResp = null;//(BizProfileDetResp) getWsObject("ssmWsResp");
			if(ssmWsResp==null) {
				ssmWsResp = RobClient.getBizProfileDet(url, param);
				mapWSStatic.put("ssmWsResp", ssmWsResp);
			}
			
			
			
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			robFormB.setBizProfileDetResp(ssmWsResp);
			
			RobFormOwnerInfo[] cbsOwner = ssmWsResp.getActiveOwnerInfo();
			
			for (int i = 0; i < robFormB.getListRobFormOwnerVerification().size(); i++) {
				RobFormOwnerVerification ownerVer = robFormB.getListRobFormOwnerVerification().get(i);
				for (int j = 0; j < cbsOwner.length; j++) {
					if(ownerVer.getIdNo().equals(cbsOwner[j].getIcNo())) {
						ownerVer.setCbsOwnerInfo(cbsOwner[j]);
						break;
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return robFormB;
	}

	@Override
	public RobFormB findAllById(String robFormBCode) {
		RobFormB robFormB = robFormBDao.findById(robFormBCode);
		
		if(Parameter.YES_NO_yes.equals(robFormB.getIsHasSupportingDoc())){
			LlpFileData suppurtData = llpFileDataService.findById(robFormB.getSupportingDocData().getFileDataId());
			robFormB.setSupportingDocData(suppurtData);
		}
		
		if(Parameter.ROB_FORM_B_STATUS_APPROVED.equals(robFormB.getStatus())){
			
			if(Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo()) ){
				if(robFormB.getBusinessInfoData()==null){
					try {
						robFormB = getBizInfoFromWS(robFormB);
					} catch (SSMException e) {
						e.printStackTrace();
					}
				}
				LlpFileData bizInfoData = llpFileDataService.findById(robFormB.getBusinessInfoData().getFileDataId());
				robFormB.setBusinessInfoData(bizInfoData);
			}
			
			if(robFormB.getCertFileData()==null){
				try {
					robFormB = getCertFileFromWS(robFormB);
				} catch (SSMException e) {
					e.printStackTrace();
				}
			}
			
			if(robFormB.getCertFileData()!=null){
				LlpFileData certFileData = llpFileDataService.findById(robFormB.getCertFileData().getFileDataId());
				robFormB.setCertFileData(certFileData);
			}
			
			
			if(StringUtils.isNotBlank(robFormB.getCmpNo()) ){
				try {
					robFormB = getCmpDataFromWS(robFormB);
				} catch (SSMException e) {
					e.printStackTrace();
				}
				LlpFileData cmpNotice = llpFileDataService.findById(robFormB.getCmpNoticeFileData().getFileDataId());
				robFormB.setCmpNoticeFileData(cmpNotice);
			}
		}
		
		RobFormB1 robFormB1 = robFormB1Service.findByRobFormBCode(robFormB.getRobFormBCode());
		robFormB.setRobFormB1(robFormB1);
		
		RobFormB2 robFormB2 = robFormB2Service.findByRobFormBCode(robFormB.getRobFormBCode());
		robFormB.setRobFormB2(robFormB2);
		List<RobFormB2Det> listRobFormB2Det = robFormB2DetService.findByRobFormBCode(robFormBCode);
		robFormB2.setListRobFormB2Det(listRobFormB2Det);
		
		List<RobFormB3> listRobFormB3 = robFormB3Service.findByRobFormBCode(robFormBCode);
		robFormB.setListRobFormB3(listRobFormB3);
		
		List<RobFormB4> listRobFormB4 = robFormB4Service.findByRobFormBCode(robFormBCode);
		robFormB.setListRobFormB4(listRobFormB4);
		
		
		List<RobFormOwnerVerification> listRobFormOwnerVerification = robFormOwnerVerificationService.findByRobFormCode(robFormBCode);
		robFormB.setListRobFormOwnerVerification(listRobFormOwnerVerification);
		for (int i = 0; i < listRobFormOwnerVerification.size(); i++) {
			RobFormOwnerVerification ownerVer = listRobFormOwnerVerification.get(i);
			LlpUserProfile ezbizUser = llpUserProfileService.findByIdTypeAndIdNo("01", ownerVer.getIdNo());
			if(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER.equals(ownerVer.getStatus())){
				if(ezbizUser!=null){
					if(Parameter.USER_STATUS_active.equals(ezbizUser.getUserStatus())){
						ownerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
					}else{
						ownerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_ACTIVATION);
					}
				}
			}
		}
		
		
		return robFormB;
	}
	
	private RobFormB getCmpDataFromWS(RobFormB robFormB) throws SSMException{
		try {
			String url = wSManagementService.getWsUrl("RobClient.getCompoundNotice");
			
			FindCompoundNoticeByCompoundNoReq param = new FindCompoundNoticeByCompoundNoReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			String brNo = StringUtils.split(robFormB.getBrNo(),"-")[0];
			param.setBrNo(brNo);
			param.setCompoundNumber(robFormB.getCmpNo());
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			
			FindCompoundNoticeByCompoundNoResp compoundNoResp = RobClient.getCompoundNotice(url, param);
			if("00".equals(compoundNoResp.getSuccessCode())){
				LlpFileData cmpFilePdf = new LlpFileData();
				cmpFilePdf.setFileData(compoundNoResp.getBarcodeImage());
				cmpFilePdf.setFileDataType("PDF");
				llpFileDataService.insert(cmpFilePdf);
				robFormB.setCmpNoticeFileData(cmpFilePdf);
				robFormBDao.update(robFormB);
			}else{
				throw new SSMException(compoundNoResp.getSuccessCode()+":"+compoundNoResp.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		}
		return robFormB;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		String url = "http://ezbizwebisdev.ssm.com.my/EZBIZ/services";
		
		BizProfileDetReq param = new BizProfileDetReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setBrNo("002931504");
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

		try {

			BizProfileDetResp ssmWsResp = RobClient.getBizProfileDet(url, param);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RobFormB generateRobDetailFromWs(String brNo)throws SSMException {
		RobFormB robFormB = new RobFormB();
		robFormB.setIsBuyInfo(Parameter.YES_NO_yes);
		
		String url = wSManagementService.getWsUrl("RobClient.getBizProfileDet");
		
		BizProfileDetReq param = new BizProfileDetReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setBrNo(brNo);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

//		try {
			BizProfileDetResp ssmWsResp = null;
			try {
				ssmWsResp = RobClient.getBizProfileDet(url, param);
				if(!"00".equals(ssmWsResp.getSuccessCode())){
					System.out.println(ssmWsResp.getErrorMsg());
					throw new SSMException("Webservice Data Problem. Please try again.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SSMException("Webservice Problem. Please try again.");
			}
			
			
			String hashCode = MD5DigestUtils.hashObject(ssmWsResp.getMainInfo(),ssmWsResp.getActiveBranchInfo(),ssmWsResp.getActiveOwnerInfo());
			System.out.println("Hash:"+hashCode);
			
			
			//Validate CBS DATA
			if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizMainAddrTown())) {
				throw new SSMException("Data Issues, Please Contact SSM (codeMT)");
			}
			
			if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizMainAddrState())) {
				throw new SSMException("Data Issues, Please Contact SSM (codeMS)");
			}
			
			if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizMainAddrPostcode())) {
				throw new SSMException("Data Issues, Please Contact SSM (codeMO)");
			}
			
			if(StringUtils.isNotBlank(ssmWsResp.getMainInfo().getBizPostAddr())){
				if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizPostAddrTown())) {
					throw new SSMException("Data Issues, Please Contact SSM (codePT)");
				}
				
				if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizPostAddrState())) {
					throw new SSMException("Data Issues, Please Contact SSM (codePS)");
				}
				
				if(StringUtils.isBlank(ssmWsResp.getMainInfo().getBizPostAddrPostcode())) {
					throw new SSMException("Data Issues, Please Contact SSM (codePP)");
				}
			}
			
			RobFormOwnerInfo[] owner = ssmWsResp.getActiveOwnerInfo();
			for (int i = 0; i < owner.length; i++) {
				if(StringUtils.isBlank(owner[i].getAddrTown())) {
					throw new SSMException("Data Issues, Please Contact SSM (codeOT)");
				}
				if(StringUtils.isBlank(owner[i].getAddrState())) {
					throw new SSMException("Data Issues, Please Contact SSM (codeOS)");
				}
				if(StringUtils.isBlank(owner[i].getAddrPostcode())) {
					throw new SSMException("Data Issues, Please Contact SSM (codeOP)");
				}
			}
			
			//END Validation  CBS
			
			
			robFormB.setBizProfileDetResp(ssmWsResp);
			robFormB.setHashBizInfo(hashCode);
			
			
			robFormB.setBrNo(ssmWsResp.getMainInfo().getBrNo());
			robFormB.setCheckDigit(ssmWsResp.getMainInfo().getChkDigit());
			robFormB.setBizName(ssmWsResp.getMainInfo().getBizName());
			robFormB.setStatus(ssmWsResp.getMainInfo().getStatus());
			robFormB.setBizExpDt(ssmWsResp.getMainInfo().getBizExpDate());
			robFormB.setBizType(ssmWsResp.getMainInfo().getBizType());
			robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY);
			robFormBDao.insert(robFormB);
			
			
			
			//Prepare B2 List
			if(ssmWsResp.getBizCodeInfo()!=null){
				
				String[] bizCodeInfo = ssmWsResp.getBizCodeInfo();
				List<RobFormB2Det> listRobFormB2Det = new ArrayList<RobFormB2Det>();
				
				Set<String> setBizCode = new HashSet<String>();
				for (int i = 0; i < bizCodeInfo.length; i++) {
					if(setBizCode.contains(bizCodeInfo[i])){//To Check for duplicate
						continue;
					}
					setBizCode.add(bizCodeInfo[i]);
					
					
					String codeDesc = llpParametersService.findByCodeTypeValue(Parameter.ROB_BUSINESS_CODE, bizCodeInfo[i]);
					
					RobFormB2Det robFormB2Det = new RobFormB2Det();
					robFormB2Det.setRobFormBCode(robFormB.getRobFormBCode());
					robFormB2Det.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES);
					robFormB2Det.setBizCode(bizCodeInfo[i]);
					robFormB2Det.setBizCodeDesc(codeDesc);
					robFormB2DetService.insert(robFormB2Det);
					listRobFormB2Det.add(robFormB2Det);
				}
				
				RobFormB2 robFormB2 = new RobFormB2();
				robFormB2.setRobFormBCode(robFormB.getRobFormBCode());

				robFormB2.setListRobFormB2Det(listRobFormB2Det);
				
				robFormB2Service.insert(robFormB2);
				
				robFormB.setRobFormB2(robFormB2);
				
			}
			
			
			//Prepare Branch List
			if(ssmWsResp.getActiveBranchInfo()!=null && ssmWsResp.getActiveBranchInfo().length > 0){
				RobFormBranchInfo[] branchDet = ssmWsResp.getActiveBranchInfo();
				List<RobFormB3> listRobFormB3 = new ArrayList<RobFormB3>();
				for (int i = 0; i < branchDet.length; i++) {
					RobFormB3 robFormB3 = new RobFormB3();
					robFormB3.setRobFormBCode(robFormB.getRobFormBCode());
					robFormB3.setCbsBranchId(branchDet[i].getAddressid());
					robFormB3.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES);
					robFormB3.setAddr(branchDet[i].getAddr());
					robFormB3.setAddr2(branchDet[i].getAddr1());
					robFormB3.setAddr3(branchDet[i].getAddr2());
					robFormB3.setAddrPostcode(branchDet[i].getAddrPostcode());
					robFormB3.setAddrTown(branchDet[i].getAddrTown());
					robFormB3.setAddrState(branchDet[i].getAddrState());
					robFormB3Service.insert(robFormB3);
					listRobFormB3.add(robFormB3);
				}
				robFormB.setListRobFormB3(listRobFormB3);
			}
			
			//Prepare Owner List
			List<RobFormB4> listRobFormB4 = new ArrayList<RobFormB4>();
			List<RobFormOwnerVerification> listRobFormOwnerVerification = new ArrayList<RobFormOwnerVerification>();
			LlpUserProfile llpCurrrentUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			
			int lodgerIdx = -1;
			for (int i = 0; i < owner.length; i++) {
				
				LlpUserProfile ezbizUser = llpUserProfileService.findByIdTypeAndIdNo("01", owner[i].getIcNo());
				
				
				//OwnerB4
				RobFormB4 robFormB4 = new RobFormB4();
				robFormB4.setRobFormBCode(robFormB.getRobFormBCode());
				robFormB4.setCbsOwnerId(owner[i].getOwnerId());
				robFormB4.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES);
				robFormB4.setNewicno(owner[i].getIcNo());
				robFormB4.setName(owner[i].getName());
				robFormB4.setDob(owner[i].getDob());
				robFormB4.setNationality(owner[i].getNationality());
				robFormB4.setCountryoforiginifpr(owner[i].getCountryoforiginifpr());
				robFormB4.setGender(owner[i].getGender());
				robFormB4.setRace(owner[i].getRace());
				robFormB4.setOtherrace(owner[i].getOtherRace());
				robFormB4.setTelNo(owner[i].getTelNo());
				robFormB4.setMobileNo(owner[i].getMobileNo());
				robFormB4.setEmail(owner[i].getEmail());
				robFormB4.setOwnershiptype(owner[i].getOwnershipType());
				robFormB4.setAddr(owner[i].getAddr());
				robFormB4.setAddr2(owner[i].getAddr2());
				robFormB4.setAddr3(owner[i].getAddr3());
				robFormB4.setAddrTown(owner[i].getAddrTown());
				robFormB4.setAddrPostcode(owner[i].getAddrPostcode());
				robFormB4.setAddrState(owner[i].getAddrState());
				
				
				//Owner Verification
				RobFormOwnerVerification ownerVerification = new RobFormOwnerVerification();
				ownerVerification.setCbsOwnerInfo(owner[i]);
				ownerVerification.setName(owner[i].getName());
				ownerVerification.setIdNo(owner[i].getIcNo());
				ownerVerification.setIdType("01");
				ownerVerification.setRobFormCode(robFormB.getRobFormBCode());
				ownerVerification.setRobFormType(Parameter.ROB_FORM_TYPE_B);
				ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND);//by default
				
				if(ezbizUser!=null){
					robFormB4.setEzbizLoginName(ezbizUser.getLoginId());
					robFormB4.setEzbizUserRefNo(ezbizUser.getUserRefNo());
					
					ownerVerification.setUserRefNo(ezbizUser.getUserRefNo());
					ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
					
					if(Parameter.USER_STATUS_active.equals(ezbizUser.getUserStatus())){
						if(llpCurrrentUserProfile.getUserRefNo().equals(ezbizUser.getUserRefNo())){
							ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
							ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_NOT_REQ);
							lodgerIdx = i;
						}else{
							ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
						}
					}
					if(Parameter.USER_STATUS_pending.equals(ezbizUser.getUserStatus())){
						ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_ACTIVATION);
					}
					
				}else{
					ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER);
				}
				
				listRobFormB4.add(robFormB4);
				listRobFormOwnerVerification.add(ownerVerification);
				
			}
			
			if(lodgerIdx>0) {//Sort Lodger on TOP
				Collections.swap(listRobFormB4, 0, lodgerIdx);
				Collections.swap(listRobFormOwnerVerification, 0, lodgerIdx);
			}
			
			
			robFormB4Service.insertAll(listRobFormB4);
			robFormOwnerVerificationService.insertAll(listRobFormOwnerVerification);
			
			robFormB.setListRobFormOwnerVerification(listRobFormOwnerVerification);
			robFormB.setListRobFormB4(listRobFormB4);
			
			return robFormB;
//		} catch (Exception fault) {
//			System.out.println(fault.getMessage());
//			throw new SSMException(fault);
//		}
	}
	

	@Override
	public RobFormB findByRobFormBCode(String robFormBCode) {
		return robFormBDao.findByRobFormBCode(robFormBCode);
	}

	@Override
	@Transactional
	public RobFormB insertUpdateAll(RobFormB robFormB) {
		
		String robFormBCode = robFormB.getRobFormBCode();
		robFormBDao.update(robFormB);
		
		if(robFormB.getIsB1()){
			RobFormB1 robFormB1 = robFormB.getRobFormB1();
			if(robFormB1.getRobFormB1Id()==0){
				robFormB1Service.insert(robFormB1);
			}else{
				robFormB1Service.update(robFormB1);
			}
		}else{
			RobFormB1 robFormB1 = robFormB.getRobFormB1();
			if(robFormB1!=null){
				robFormB1Service.delete(robFormB1);
			}
			robFormB1 = new RobFormB1();
			robFormB1.setRobFormBCode(robFormB.getRobFormBCode());
    		robFormB.setRobFormB1(robFormB1);
		}
		
		//RObForm B2
		RobFormB2 robFormB2 = robFormB.getRobFormB2();
		if(!robFormB.getIsB2()){
			robFormB2.setBizDesc("");
			robFormB2.setB2AmmendmendDt(null);
		}
		robFormB2Service.update(robFormB2);
		List<RobFormB2Det> listB2 = robFormB2.getListRobFormB2Det();
		long[] listB2IdNotDelete = new long[listB2.size()];
		for (int i = 0; i < listB2.size(); i++) {
			RobFormB2Det b2Det = listB2.get(i);
			if(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW.equals(b2Det.getAmmendmentType())){
				b2Det.setRobFormBCode(robFormBCode);
				robFormB2DetService.insert(b2Det);
			}else{
				robFormB2DetService.update(b2Det);
			}
			listB2IdNotDelete[i]= b2Det.getRobFormB2DetId();
		}
		robFormB2DetService.deleteExceptId(robFormBCode, listB2IdNotDelete);
		
		
		//RobFormB3
		List<RobFormB3> listB3 = robFormB.getListRobFormB3();
		long[] listB3IdNotDelete = new long[0];
		if(listB3!=null){
			listB3IdNotDelete = new long[listB3.size()];
			for (int i = 0; i < listB3.size(); i++) {
				RobFormB3 b3Form = listB3.get(i);
				if(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NEW.equals(b3Form.getAmmendmentType())){
					b3Form.setRobFormBCode(robFormBCode);
					robFormB3Service.insert(b3Form);
				}else{
					robFormB3Service.update(b3Form);
				}
				listB3IdNotDelete[i]= b3Form.getRobFormB3Id();
			}
		}
		robFormB3Service.deleteExceptId(robFormBCode, listB3IdNotDelete);
		
		
		//RobFormB4
		List listOwnerVerification = robFormB.getListRobFormOwnerVerification();
		List<RobFormB4> listB4 = robFormB.getListRobFormB4();
		long[] listB4IdNotDelete1 = new long[0];
		Set<String> setVerificationNotToDelete = new HashSet<String>();
		
		
		Set<String> deceasedOwnersByEzbizUserRefNo = new HashSet<String>();
		if(listB4!=null){
			listB4IdNotDelete1 = new long[listB4.size()];
			for (int i = 0; i < listB4.size(); i++) {
				RobFormB4 b4Form = listB4.get(i);
				if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NEW.equals(b4Form.getAmmendmentType()) && b4Form.getRobFormB4Id()==0 ){
					b4Form.setRobFormBCode(robFormBCode);
					robFormB4Service.insert(b4Form);
					
					
					
					RobFormOwnerVerification prevOwnerVerification = robFormOwnerVerificationService.findByFormCodeAndUserRefNo( robFormB.getRobFormBCode(), b4Form.getEzbizUserRefNo());
					if(prevOwnerVerification!=null) {//Delete Previos before insert
						robFormOwnerVerificationService.delete(prevOwnerVerification);
						for (Iterator iterator = listOwnerVerification.iterator(); iterator.hasNext();) {
							RobFormOwnerVerification ownerVerification = (RobFormOwnerVerification) iterator.next();
							if(prevOwnerVerification.getUserRefNo().equals(ownerVerification.getUserRefNo())) {
								iterator.remove();
								break;
							}
						}
					}
					
					
					RobFormOwnerVerification ownerVerification = new RobFormOwnerVerification();
					ownerVerification.setUserRefNo(b4Form.getEzbizUserRefNo());
					ownerVerification.setName(b4Form.getName());
					ownerVerification.setIdNo(b4Form.getNewicno());
					ownerVerification.setIdType("01");
					ownerVerification.setRobFormCode(robFormB.getRobFormBCode());
					ownerVerification.setRobFormType(Parameter.ROB_FORM_TYPE_B);
					ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND);//by default
					ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
					robFormOwnerVerificationService.insert(ownerVerification);
					
					listOwnerVerification.add(ownerVerification);
				}else{
					if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(b4Form.getAmmendmentType())) {
						deceasedOwnersByEzbizUserRefNo.add(b4Form.getEzbizUserRefNo());
					}
					
					robFormB4Service.update(b4Form);
				}
				listB4IdNotDelete1[i]= b4Form.getRobFormB4Id();
				setVerificationNotToDelete.add(b4Form.getEzbizUserRefNo());
			}
		}
		robFormB4Service.deleteExceptId(robFormBCode, listB4IdNotDelete1);
		
		boolean queryBeforeOwnerChangesExist = false;
		if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus()) && (listB4==null || listB4.size()==0) ) {
			//All Query before owner changes
		}else {
			for (Iterator iterator = listOwnerVerification.iterator(); iterator.hasNext();) {
				RobFormOwnerVerification ownerVerification = (RobFormOwnerVerification) iterator.next();
				if(!setVerificationNotToDelete.contains(ownerVerification.getUserRefNo())) {
					robFormOwnerVerificationService.delete(ownerVerification);
					iterator.remove();
					continue;
				}
				if(deceasedOwnersByEzbizUserRefNo.contains(ownerVerification.getUserRefNo())) {
					ownerVerification.setStatus(Parameter.ROB_OWNER_VERI_STATUS_AUTO_VERIFIED_DECEASED);
					robFormOwnerVerificationService.update(ownerVerification);
				}
				
			}
		}
		
		
		
		
		return robFormB;
	}

	


	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobFormB robFormBTmp = (RobFormB) obj;
		RobFormB robFormB = robFormBDao.findById(robFormBTmp.getRobFormBCode());
		obj = robFormB;
//		System.out.println("SUCCESS PAYMENT");
		
		if (Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT.equals(robFormB.getStatus())) {
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
			robFormB.setPaymentDt(paymentTransaction.getCreateDt());
			
			robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS);
			robFormBDao.update(robFormB);
		}
	}


	@Override
	@Transactional
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
//		System.out.println("SUCCESS NOTIFICATION");
		RobFormB robFormBTmp = (RobFormB) obj;
		RobFormB robFormB = findAllByIdWithWS(robFormBTmp.getRobFormBCode());
		obj = robFormB;
		lodgeFormBWs(robFormB);
	}

	@Override
	@Transactional
	public void lodgeFormBWs(RobFormB robFormB) throws SSMException {
		String url = wSManagementService.getWsUrl("RobClient.lodgeFormB");
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormB.getRobFormBCode());
		if(llpPaymentTransaction==null){
			throw new SSMException("No Success payment ,Cannot Lodge B:"+robFormB.getRobFormBCode());
		}
		
		BusinessFormBLodgeReq reqParam = prepareFormBParamData(robFormB);

		try {
			BusinessFormBLodgeResp resp = RobClient.lodgeFormB(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_IN_PROCESS);//Update to inproces first
				robFormB.setSubmitDt(new Date());
				robFormBDao.update(robFormB);
				if(Parameter.CBS_ROB_FORM_B_STATUS_APPROVED.equals(resp.getCbsStatus())){
					updateStatusApproved(robFormB, Parameter.ROB_AGENCY_ID,reqParam.getAgencyBranchCode(),new Date(),"" );
					sendEmailFormBInApproved(robFormB);
				}else{
					sendEmailFormBInProcess(robFormB);//Only send if not auto approved
				}
				
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public void updateStatusApproved(RobFormB robFormB, String approveRejectBy, String approveRejectbranch, Date approveRejectDt, String approveRejectNotes) {
		robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_APPROVED);
		robFormB.setApproveRejectDt(approveRejectDt);
		robFormB.setApproveRejectNotes(approveRejectNotes);
		robFormB.setApproveRejectBranch(approveRejectbranch);
		robFormB.setApproveRejectBy(approveRejectBy);
		robFormBDao.update(robFormB);
		
		String userDeactiveRemark = "page.lbl.ezbiz.robFormB4.userDeactiveRemark";
		
		if(robFormB.getIsB4()) {
			List<RobFormB4> listRobFormB4 = robFormB.getListRobFormB4();
			if(listRobFormB4==null || listRobFormB4.size()==0) {
				listRobFormB4 = robFormB4Service.findByRobFormBCode(robFormB.getRobFormBCode());
			}
			
			for (int i = 0; i < listRobFormB4.size(); i++) {
				RobFormB4 formB4 = (RobFormB4) listRobFormB4.get(i);
				if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(formB4.getAmmendmentType())) {
					String remarkNote = WicketApplication.resolve(userDeactiveRemark, robFormB.getRobFormBCode());
					
					try {
						llpUserProfileService.updateStatus(formB4.getEzbizLoginName(), Parameter.USER_STATUS_deceased, remarkNote, formB4.getAmmendmentDate());
					} catch (SSMException e) {
						e.printStackTrace();
					}
					
				}
			}
			
		}
	}


	@Override
	@Transactional
	public void reLodgeFormB(RobFormB robFormB) throws SSMException {
		RobFormNotes formNotes = robFormB.getListRobFormNotes().get(robFormB.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormB.getQueryAnswer());
		robFormNotesService.update(formNotes);
		reLodgeFormBWs(robFormB);
	}
	
	
	@Transactional
	public void reLodgeFormBWs(RobFormB robFormB) throws SSMException {
		String url = wSManagementService.getWsUrl("RobClient.reLodgeFormB");
		BusinessFormBLodgeReq reqParam = prepareFormBParamData(robFormB);

		RobFormNotes formNotes = robFormB.getListRobFormNotes().get(robFormB.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(robFormB.getQueryAnswer());
		reqParam.setNotes(formNotes.getNotes());
		reqParam.setNotesAnswer(formNotes.getNotesAnswer());

		try {
			BusinessFormBLodgeResp resp = RobClient.reLodgeFormB(url, reqParam);
			if ("00".equals(resp.getSuccessCode())) {
				robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_IN_PROCESS);
				robFormB.setResubmitDt(new Date());
				robFormBDao.update(robFormB);
			} else {
				throw new SSMException(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
	}
	
	private BusinessFormBLodgeReq prepareFormBParamData(RobFormB robFormB) {
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		BusinessFormBLodgeReq param = new BusinessFormBLodgeReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		param.setReqRefNo(robFormB.getRobFormBCode());

		
		// Payment Info
		LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormB.getRobFormBCode());
		LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
		String recieptNo = paymentReceipt.getReceiptNo();
		
		String processBranch = Parameter.ROB_AGENCY_BRANCH_CODE;
		if(paymentReceipt.getCounterSessionId()!=null){
			processBranch = paymentReceipt.getCounterSessionId().getBranch();
			robFormB.setProsessingBranch(processBranch);
			robFormBDao.update(robFormB);
		}
		
		if(StringUtils.isBlank(robFormB.getProsessingBranch())){
			robFormB.setProsessingBranch(processBranch);
			robFormBDao.update(robFormB);
		}
		
		param.setAgencyBranchCode(robFormB.getProsessingBranch());
		param.setIsFromSSMPc(robFormB.getIsFromSSMPc());
		
		RobFormBPaymentInfoParam paramPayment = new RobFormBPaymentInfoParam();
		paramPayment.setTotalAmt(robFormB.getTotalAmt());
		paramPayment.setBizInfoAmt(robFormB.getBizInfoAmt());
		paramPayment.setBranchesAmt(robFormB.getBranchesAmt());
		paramPayment.setInvoiceNo(recieptNo);
		paramPayment.setPaymentDate(paymentTransaction.getCreateDt());
		
		paramPayment.setCmpAmt(robFormB.getCmpAmt());
		//To DO
//		paramPayment.setIsPayCmp(robFormB.getIsPayCmp());
		
		param.setRobFormBPaymentInfoParam(paramPayment);

		// Param Info
		param.setBrNo(robFormB.getBrNo());
		param.setFormBRefNo(robFormB.getRobFormBCode());
		param.setBranchCode(Parameter.EZBIZ_BRANCH_CODE);//MB
		param.setApplicantName(userProfile.getName());
		param.setApplicantIC(userProfile.getIdNo());
		param.setApplicantICColor("");
		String addr = userProfile.getAdd1();
		if (StringUtils.isNotBlank(userProfile.getAdd2())) {
			addr += "\n" + userProfile.getAdd2();
		}
		if (StringUtils.isNotBlank(userProfile.getAdd3())) {
			addr += "\n" + userProfile.getAdd3();
		}
		param.setApplicantAddress(addr);
		param.setApplicantTown(userProfile.getCity());
		param.setApplicantPostcode(userProfile.getPostcode());
		param.setApplicantState(userProfile.getState());
		param.setApplicantEmail(userProfile.getEmail());
		param.setApplicantTelNo(userProfile.getHpNo());
		param.setApplicantFaxNo(userProfile.getFaxNo());

		
		//If B1
		if(robFormB.getIsB1()){
			RobFormB1 formB1 = robFormB.getRobFormB1();
			RobFormB1Param b1Param = new RobFormB1Param();
			b1Param.setBizAmendmentDate(formB1.getB1AmmendmendDt());
			
			b1Param.setBizMainAddr(formB1.getBizMainAddr());
			b1Param.setBizMainAddr2(formB1.getBizMainAddr2());
			b1Param.setBizMainAddr3(formB1.getBizMainAddr3());
			b1Param.setBizMainAddrTown(formB1.getBizMainAddrTown());
			b1Param.setBizMainAddrPostcode(formB1.getBizMainAddrPostcode());
			b1Param.setBizMainAddrState(formB1.getBizMainAddrState());
			b1Param.setBizMainAddrMobileNo(formB1.getBizMainAddrMobileNo());
			b1Param.setBizMainAddrTelNo(formB1.getBizMainAddrTelNo());
			b1Param.setBizMainAddrEmail(formB1.getBizMainAddrEmail());
			b1Param.setBizMainAddrUrl(formB1.getBizMainAddrUrl());
			
			b1Param.setIsMainAddSamePostAddr("N");

			b1Param.setBizPostAddr(formB1.getBizPostAddr());
			b1Param.setBizPostAddr2(formB1.getBizPostAddr2());
			b1Param.setBizPostAddr3(formB1.getBizPostAddr3());
			b1Param.setBizPostAddrTown(formB1.getBizPostAddrTown());
			b1Param.setBizPostAddrPostcode(formB1.getBizPostAddrPostcode());
			b1Param.setBizPostAddrState(formB1.getBizPostAddrState());
			b1Param.setBizPostAddrMobileNo(formB1.getBizPostAddrMobileNo());
			b1Param.setBizPostAddrTelNo(formB1.getBizPostAddrTelNo());
			b1Param.setBizPostAddrEmail(formB1.getBizPostAddrEmail());
			
			param.setFormB1Param(b1Param);
		}
		
		// Business Code
		if(robFormB.getIsB2()){
			RobFormB2 formB2 = robFormB.getRobFormB2();
			RobFormB2Param b2Param = new RobFormB2Param();
			b2Param.setBizAmendmentDate(formB2.getB2AmmendmendDt());
			b2Param.setBizDesc(formB2.getBizDesc());
			
			List<RobFormB2Det> listRobFormB2Det = formB2.getListRobFormB2Det();
			
			List<String> listCode = new ArrayList<String>();
			for (int i = 0; i < listRobFormB2Det.size(); i++) {
				if(!Parameter.ROB_FORM_B2_AMENDMENT_TYPE_REMOVE.equals(listRobFormB2Det.get(i).getAmmendmentType())){
					listCode.add(listRobFormB2Det.get(i).getBizCode());
				}
			}
			b2Param.setRobFormB2BusinessCodeParamList(listCode.toArray(new String[0]));
			
			param.setFormB2Param(b2Param);
		}
		
		// Rob Branches
		if(robFormB.getIsB3()){
			
			RobFormB3Param robFormB3Param = new RobFormB3Param();
			robFormB3Param.setBizAmendmentDate(robFormB.getB3AmmendmendDt());
			robFormB3Param.setTotalNewBranch(robFormB.getNewBranchCount());
			
			List<RobFormB3> listRobFormB3 = robFormB.getListRobFormB3();
			RobFormB3BranchesParam[] robBranch = new RobFormB3BranchesParam[listRobFormB3.size()];
			for (int i = 0; i < listRobFormB3.size(); i++) {
				RobFormB3 robFormB3 = listRobFormB3.get(i);
				
				robBranch[i] = new RobFormB3BranchesParam();
				if(robFormB3.getCbsBranchId()!=null){
					robBranch[i].setAddressid(robFormB3.getCbsBranchId().intValue());
				}
				robBranch[i].setAmendmentType(robFormB3.getAmmendmentType());
				robBranch[i].setAddr(robFormB3.getAddr());
				robBranch[i].setAddr1(robFormB3.getAddr2());
				robBranch[i].setAddr2(robFormB3.getAddr3());
				robBranch[i].setAddrPostcode(robFormB3.getAddrPostcode());
				robBranch[i].setAddrState(robFormB3.getAddrState());
				robBranch[i].setAddrTown(robFormB3.getAddrTown());
				robBranch[i].setAddrUrl(robFormB3.getAddrUrl());
			}
			robFormB3Param.setRobFormB3BranchesParamList(robBranch);
			
			param.setFormB3Param(robFormB3Param);
		}
		
		// Rob Owner
		if(robFormB.getIsB4() && robFormB.getB4AmmendmendDt()!=null){
			
			RobFormB4Param robFormB4Param = new RobFormB4Param();
			robFormB4Param.setBizAmendmentDate(robFormB.getB4AmmendmendDt());
			
			int totalNewOwner = 0 ;
			
			List<RobFormB4> listRobFormB4 = robFormB.getListRobFormB4();
			RobFormB4OwnersParam[] robFormB4OwnersParamList = new RobFormB4OwnersParam[listRobFormB4.size()];
			for (int i = 0; i < listRobFormB4.size(); i++) {
				RobFormB4 robFormB4 = listRobFormB4.get(i);
				if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NEW.equals(robFormB4.getAmmendmentType())){
					totalNewOwner++;
				}
				
				robFormB4OwnersParamList[i] = new RobFormB4OwnersParam();
				robFormB4OwnersParamList[i].setName(robFormB4.getName());
				robFormB4OwnersParamList[i].setDob(robFormB4.getDob());
				robFormB4OwnersParamList[i].setNationality(robFormB4.getNationality());
				robFormB4OwnersParamList[i].setCountryOfOriginIfPR(robFormB4.getCountryoforiginifpr());
				robFormB4OwnersParamList[i].setOldIcNo(robFormB4.getOldicno());
				robFormB4OwnersParamList[i].setNewIcNo(robFormB4.getNewicno());
				robFormB4OwnersParamList[i].setIcColor(robFormB4.getIccolor());
				robFormB4OwnersParamList[i].setGender(robFormB4.getGender());
				robFormB4OwnersParamList[i].setRace(robFormB4.getRace());
				robFormB4OwnersParamList[i].setOtherRace(robFormB4.getOtherrace());
				robFormB4OwnersParamList[i].setAddr(robFormB4.getAddr());
				robFormB4OwnersParamList[i].setAddr2(robFormB4.getAddr2());
				robFormB4OwnersParamList[i].setAddr3(robFormB4.getAddr3());
				robFormB4OwnersParamList[i].setAddrTown(robFormB4.getAddrTown());
				robFormB4OwnersParamList[i].setAddrPostcode(robFormB4.getAddrPostcode());
				robFormB4OwnersParamList[i].setAddrState(robFormB4.getAddrState());
				robFormB4OwnersParamList[i].setTelNo(robFormB4.getTelNo());
				robFormB4OwnersParamList[i].setMobileNo(robFormB4.getMobileNo());
				robFormB4OwnersParamList[i].setEmail(robFormB4.getEmail());
				robFormB4OwnersParamList[i].setOwnershipType(robFormB4.getOwnershiptype());
				robFormB4OwnersParamList[i].setAmendmentType(robFormB4.getAmmendmentType());
				robFormB4OwnersParamList[i].setAmendmentDate(robFormB4.getAmmendmentDate());
				if(robFormB4.getCbsOwnerId()!=null) {
					robFormB4OwnersParamList[i].setCbsOwnerId(robFormB4.getCbsOwnerId().intValue());
				}
				
				
			}
			robFormB4Param.setTotalNewOwners(totalNewOwner);
			robFormB4Param.setRobFormB4OwnersParamList(robFormB4OwnersParamList);
			
			param.setFormB4Param(robFormB4Param);
		}
		
		return param;
	}

	private RobFormB getCertFileFromWS(RobFormB robFormB)throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getFormACertificate");

			BusinessFormACertificateReq certificateBusinessInfoReq = new BusinessFormACertificateReq();
			certificateBusinessInfoReq.setAgencyId(Parameter.ROB_AGENCY_ID);
			String brNo = StringUtils.split(robFormB.getBrNo(), "-")[0];
			certificateBusinessInfoReq.setBrNo(brNo);// br without check digit
			certificateBusinessInfoReq.setFormARefNo(robFormB.getRobFormBCode());

			BusinessFormACertificateResp certResponse = RobClient.getFormACertificate(url, certificateBusinessInfoReq);
			if ("00".equals(certResponse.getSuccessCode())) {
				LlpFileData certFileData = new LlpFileData();
				certFileData.setFileData(certResponse.getCertImage());
				certFileData.setFileDataType("PDF");
				llpFileDataService.insert(certFileData);
				robFormB.setCertFileData(certFileData);
				update(robFormB);

			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormB;
	}
	
	@Override
	public RobFormB getBizInfoFromWS(RobFormB robFormB) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.generateInfo");
			
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robFormB.getRobFormBCode());
			List<LlpPaymentTransactionDetail> listPaymentDetail = llpPaymentTransactionDetailService.findByTransactionIdAndPaymentItem(paymentTransaction.getTransactionId(), Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			
			LlpPaymentTransactionDetail paymentDetail = listPaymentDetail.get(0);
			
			LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
			String recieptNo = paymentReceipt.getReceiptNo();
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setTotalAmount(BigDecimal.valueOf(paymentDetail.getAmountWithOutGst()));
			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormB.getRobFormBCode());

			String bizInfoGstRate = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GST_CODE, paymentDetail.getGstCode());
			param.setGstAmount(new BigDecimal(0));
			if(StringUtils.isNotBlank(bizInfoGstRate)){
				double gstAmt = paymentDetail.getAmountWithOutGst() * Double.valueOf(bizInfoGstRate);
				param.setGstAmount(BigDecimal.valueOf(gstAmt));
			}

			List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();

			SSMInfoReq infoReq = new SSMInfoReq();
			infoReq.setEntityNo(robFormB.getBrNo()+"-"+robFormB.getCheckDigit());//must past with checkdigit
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
				robFormB.setBusinessInfoData(bisInfoFileData);
				update(robFormB);
			} else if (SSMInfoException.INVOICE_INVALID_CODE.equals(certResponse.getSuccessCode())) {
				return getRegenerateBusinessInfoDataFromWS(robFormB, recieptNo);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormB;
	}
	
	private RobFormB getRegenerateBusinessInfoDataFromWS(RobFormB robFormB, String recieptNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.reprintInfo");
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(robFormB.getRobFormBCode());
			
			SSMInfoGenerateResp certResponse = SSMInfoClient.reprintInfo(url, param);
			
			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				LlpFileData bisInfoFileData = new LlpFileData();
				bisInfoFileData.setFileData(bizInfo[0].getPdfInfo());
				bisInfoFileData.setFileDataType("PDF");
				llpFileDataService.insert(bisInfoFileData);
				robFormB.setBusinessInfoData(bisInfoFileData);
				update(robFormB);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robFormB;
	}


	@Override
	public void sendEmailToAllPartner(RobFormB robFormB)  {
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		
		List<RobFormOwnerVerification> listOwnerVeri = robFormB.getListRobFormOwnerVerification();
		for (int i = 0; i < listOwnerVeri.size(); i++) {
			RobFormOwnerVerification ownerVer = listOwnerVeri.get(i);
			if(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION.equals(ownerVer.getStatus())){
				if(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND.equals(ownerVer.getEmailStatus())){
					LlpUserProfile partner = llpUserProfileService.findById(ownerVer.getUserRefNo());
					sendEmailNotifyPartner(robFormB, lodgerUser, partner);
					
					ownerVer.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_SEND);
					robFormOwnerVerificationService.update(ownerVer);
				}
			}
			if(Parameter.ROB_OWNER_VERI_STATUS_AUTO_VERIFIED_DECEASED.equals(ownerVer.getStatus())){
				if(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND.equals(ownerVer.getEmailStatus())){
					LlpUserProfile partner = llpUserProfileService.findById(ownerVer.getUserRefNo());
					sendEmailNotifyPartnerDeceased(robFormB, lodgerUser, partner);
					
					ownerVer.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_SEND);
					robFormOwnerVerificationService.update(ownerVer);
				}
			}
		}
	}
	
	public String getFormBChangesType(RobFormB robFormB) {
		String changesType = "";
		
		Map<String,String> mapCodeType = llpParametersService.findAllCodeTypeAsMap(Parameter.ROB_FORM_B_TYPE);
		if(robFormB.getIsB1()){
			changesType+=mapCodeType.get(Parameter.ROB_FORM_B_TYPE_B1);
		}
		if(robFormB.getIsB2()){
			if(StringUtils.isNotBlank(changesType)){
				changesType+=", ";
			}
			changesType+=mapCodeType.get(Parameter.ROB_FORM_B_TYPE_B2);
		}
		if(robFormB.getIsB3()){
			if(StringUtils.isNotBlank(changesType)){
				changesType+=", ";
			}
			changesType+=mapCodeType.get(Parameter.ROB_FORM_B_TYPE_B3);
		}
		if(robFormB.getIsB4()){
			if(StringUtils.isNotBlank(changesType)){
				changesType+=", ";
			}
			changesType+=mapCodeType.get(Parameter.ROB_FORM_B_TYPE_B4);
		}
		
		
		return changesType;
	}
	
	
	public void sendEmailNotifyPartner(RobFormB robFormB, LlpUserProfile lodgerUser, LlpUserProfile partner) {
		String changesType = getFormBChangesType(robFormB);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		
		mailService.sendMail(partner.getEmail(), "email.notification.robFormB.notifyPartner.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.notifyPartner.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, partner.getName(), lodgerUser.getName(), applicationDate);
	}
	
	public void sendEmailNotifyPartnerDeceased(RobFormB robFormB, LlpUserProfile lodgerUser, LlpUserProfile partner) {
		String changesType = getFormBChangesType(robFormB);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		
		mailService.sendMail(partner.getEmail(), "email.notification.robFormB.notifyPartnerDeceased.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.notifyPartnerDeceased.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, partner.getName(), lodgerUser.getName(), applicationDate);
	}
	
	

	@Override
	public void sendEmailPartnerAccept(RobFormB robFormB, RobFormOwnerVerification partner) {
		String changesType = getFormBChangesType(robFormB);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		
		mailService.sendMail(lodgerUser.getEmail(), "email.notification.robFormB.partnerAccept.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.partnerAccept.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, partner.getName(), lodgerUser.getName(), applicationDate);
	}

	@Override
	public void sendEmailPartnerReject(RobFormB robFormB, RobFormOwnerVerification partner) {
		String changesType = getFormBChangesType(robFormB);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		LlpUserProfile lodgerUser = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		mailService.sendMail(lodgerUser.getEmail(), "email.notification.robFormB.partnerReject.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.partnerReject.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, partner.getName(), lodgerUser.getName(), applicationDate);
	}
	
	
	@Override
	public void sendEmailFormBInProcess(RobFormB robFormB) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		String changesType = getFormBChangesType(robFormB);
		
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormB.inProcess.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.inProcess.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, applicationDate);
	}
	
	@Override
	public void sendEmailFormBInQuery(RobFormB robFormB) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		String changesType = getFormBChangesType(robFormB);
		
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormB.query.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.query.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, applicationDate);
		
	}

	@Override
	public void sendEmailFormBInApproved(RobFormB robFormB) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		String changesType = getFormBChangesType(robFormB);
		
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormB.approved.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.approved.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, applicationDate);
	}
	
	@Override
	public void sendEmailFormBInReject(RobFormB robFormB) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		String applicationDate = sdf.format(robFormB.getCreateDt());
		String changesType = getFormBChangesType(robFormB);
		
		LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(robFormB.getCreateBy());
		mailService.sendMail(userProfile.getEmail(), "email.notification.robFormB.reject.subject", robFormB.getRobFormBCode()+":"+robFormB.getBizName(), "email.notification.robFormB.reject.body",
				robFormB.getRobFormBCode(), robFormB.getBrNoWithCheckDigit(), robFormB.getBizName(), changesType, applicationDate);
	}

	@Override
	public boolean isBizInfoHashValid(RobFormB robFormB) throws SSMException{
		String url = wSManagementService.getWsUrl("RobClient.getBizProfileDet");
		
		//No need to check if not fetch previosly
		if(StringUtils.isBlank(robFormB.getHashBizInfo())){
			return true;
		}
		
		BizProfileDetReq param = new BizProfileDetReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setBrNo(robFormB.getBrNo());//+"-"+robFormB.getCheckDigit());
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		try {
			BizProfileDetResp ssmWsResp = RobClient.getBizProfileDet(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			if(robFormB.getIsB2()) {//For B2 no need to check
				return true;
			}
			
			String hashCode1 = MD5DigestUtils.hashObject(ssmWsResp.getMainInfo(),ssmWsResp.getBizCodeInfo(),ssmWsResp.getActiveBranchInfo(),ssmWsResp.getActiveOwnerInfo());
			if(hashCode1.equals(robFormB.getHashBizInfo())){//Case because previos hashcode
				return true;
			}
			
			String hashCode = MD5DigestUtils.hashObject(ssmWsResp.getMainInfo(),ssmWsResp.getActiveBranchInfo(),ssmWsResp.getActiveOwnerInfo());
			if(hashCode.equals(robFormB.getHashBizInfo())){
				return true;
			}
		} catch (Exception fault) {
			throw new SSMException(fault);
		}
			
		return false;
	}

	@Override
	public List findPendingApplication(String brNo) {
		String[] statusNeeded = {Parameter.ROB_FORM_B_STATUS_DATA_ENTRY, Parameter.ROB_FORM_B_STATUS_IN_PROCESS, Parameter.ROB_FORM_B_STATUS_OTC,Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS
				,Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT,Parameter.ROB_FORM_B_STATUS_QUERY};
		SearchCriteria sc = new SearchCriteria("status", SearchCriteria.IN);
		sc.setValues(statusNeeded);
		SearchCriteria sc2 = new SearchCriteria("brNo", SearchCriteria.EQUAL, brNo);
		SearchCriteria sc3 = new SearchCriteria(sc, SearchCriteria.AND, sc2);
		
		return findByCriteria(sc3).getList();
	}

	@Override
	@Transactional
	public void updateFormBackToDraff(RobFormB robFormB)throws SSMException {
		
		boolean hasPendingNSuccess = llpPaymentTransactionService.hasPendingOnlineAndSuccessPaymentByAppRefNo(robFormB.getRobFormBCode());
		if(!hasPendingNSuccess){
			llpPaymentTransactionService.cancelPendingOtcByAppRefNo(robFormB.getRobFormBCode());
			robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY);
			update(robFormB);
		}else{
			throw new SSMException("error.robFormB.hasPendingOrSuccess");
		}
	}




	
	public static Map<String, Object> mapWSStatic = new HashMap<String, Object>();
	private Object getWsObject(String wsCacheParam) {
		return mapWSStatic.get(wsCacheParam);
	}

}
