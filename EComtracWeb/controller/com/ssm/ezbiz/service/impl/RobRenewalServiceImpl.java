package com.ssm.ezbiz.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobRenewalDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.dao.RobIncentiveDao;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.client.SSMInfoClient;
import com.ssm.webis.exception.SSMBusinessException;
import com.ssm.webis.param.BizOwnerInfo;
import com.ssm.webis.param.BizProfileDetReq;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessFormA1LodgeReq;
import com.ssm.webis.param.BusinessFormA1LodgeResp;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.webis.param.BusinessInfoResp;
import com.ssm.webis.param.CertificateBusinessInfoReq;
import com.ssm.webis.param.CertificateBusinessInfoResp;
import com.ssm.webis.param.FindBusinessByBrNoReq;
import com.ssm.webis.param.FindBusinessByICNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoReq;
import com.ssm.webis.param.FindCompoundNoticeByCompoundNoResp;
import com.ssm.webis.param.FindListDetailBizByIDReq;
import com.ssm.webis.param.FindListDetailBizByIDResp;
import com.ssm.webis.param.InfoParameter;
import com.ssm.webis.param.RobBizDetailInfo;
import com.ssm.webis.param.SSMInfoException;
import com.ssm.webis.param.SSMInfoGenerateReq;
import com.ssm.webis.param.SSMInfoGenerateResp;
import com.ssm.webis.param.SSMInfoReq;
import com.ssm.webis.param.SSMInfoResp;
import com.ssm.webis.param.TransactionStatus;
import com.ssm.ws.RobBusinessSummaryInfo;

@Service
public class RobRenewalServiceImpl extends BaseServiceImpl<RobRenewal, String> implements RobRenewalService,PaymentInterface {

	@Autowired
	RobRenewalDao robRenewalDao;
	
	@Autowired
	RobIncentiveDao robIncentiveDao;
	
	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService  llpPaymentTransactionService;
	
	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("LlpFileDataService")
	LlpFileDataService llpFileDataService;
	
	@Autowired
	@Qualifier("LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	@Qualifier("LlpFileUploadService")
	LlpFileUploadService llpFileUploadService;
	
	@Autowired
	@Qualifier("LlpPaymentTransactionDetailService")
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	@Autowired
	@Qualifier("LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;
	

	@Autowired
	@Qualifier("LlpUserLogService")
	LlpUserLogService llpUserLogService;
	
	@Autowired
	@Qualifier("RobUserOkuService")
	RobUserOkuService robUserOkuService;

	@Override
	public BaseDao getDefaultDao() {
		return robRenewalDao;
	}
	
	@Override
	@Transactional
	public void insert(RobRenewal robRenewal) {
		super.insert(robRenewal);
		generateA1Form(robRenewal);
	}
	
	public void generateA1Form(RobRenewal robRenewal){
		System.out.println("GenerateA1"+robRenewal.getTransCode()+":"+robRenewal.getBrNo());
		LlpFileUpload fileUpload = llpFileUploadService.findByFileCode(Parameter.ROB_A1_FORM_TEMPLATE);
		LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robRenewal.getCreateBy());
		
		Map mapData = new HashMap();
		mapData.put("llpProfile", profile);
		mapData.put("robRenewal", robRenewal);
		
		try {
			byte bytePdfBorangA[] = LLPOdtUtils.generatePdf(fileUpload.getFileData(), mapData);
			
			LlpFileData formA1Pdf = new LlpFileData();
			formA1Pdf.setFileData(bytePdfBorangA);
			formA1Pdf.setFileDataType("PDF");
			llpFileDataService.insert(formA1Pdf);
			robRenewal.setFormA1FileData(formA1Pdf);
			update(robRenewal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<BusinessInfo> findListRobRenewalByIcWS(String icNo) throws SSMException {
		String url = wSManagementService.getWsUrl("RobClient.findBusinessByICNo");
		
		FindBusinessByICNoReq param = new FindBusinessByICNoReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setIcNo(icNo);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);

		List<BusinessInfo> listBizInfo = null;
		try {

			BusinessInfoResp ssmWsResp = RobClient.findBusinessByICNo(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				System.out.println(ssmWsResp.getErrorMsg());
				
				if(ssmWsResp.getErrorMsg().indexOf("No row with the given identifier exists: [com.ssm.webis.rob.model.RobownerDB")!=-1  ) {
					throw new SSMException("Owner Data Issues. Please contact SSM");
				}else {
					throw new SSMException(ssmWsResp.getErrorMsg());
				}
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
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobRenewal robRenewalTmp = (RobRenewal) obj;
		RobRenewal robRenewal = robRenewalDao.findById(robRenewalTmp.getTransCode());
		obj = robRenewal;
		if("PP".equals(robRenewal.getStatus())){
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
			robRenewal.setPaymentDt(paymentTransaction.getCreateDt());
			
			robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
			robRenewalDao.update(robRenewal);
		}
	}

	@Override
	@Transactional
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
//		lodge will be trigger by scheduler
		RobRenewal robRenewalTmp = (RobRenewal) obj;
		RobRenewal robRenewal = robRenewalDao.findById(robRenewalTmp.getTransCode());
		obj = robRenewal;
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
		lodgeRobFormA1Ws(robRenewal, llpPaymentTransaction);
	}
	
	@Override
	@Transactional
	public void lodgeRobFormA1(RobRenewal robRenewal)throws SSMException {
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(robRenewal.getTransCode());
		if(llpPaymentTransaction!=null){
//			if(!Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS.equals(robRenewal.getStatus())){
//				robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
//				update(robRenewal);
//			}
			lodgeRobFormA1Ws(robRenewal, llpPaymentTransaction);
		}else{
			throw new SSMException("No Success payment ,Cannot ReLodge A1:"+robRenewal.getTransCode());
		}
	}
	
	private void lodgeRobFormA1Ws(RobRenewal robRenewal, LlpPaymentTransaction llpPaymentTransaction)throws SSMException {
		Locale malayLoc = new Locale("ms", "MY");
		DateFormat endDtFormat = new SimpleDateFormat("dd MMMM yyyy", malayLoc);
		
		String url = wSManagementService.getWsUrl("RobClient.lodgeFormA1");
		
		BusinessFormA1LodgeReq param = new BusinessFormA1LodgeReq();
		
		
		LlpPaymentReceipt reciept = llpPaymentReceiptService.find(llpPaymentTransaction.getTransactionId());
		
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		
		String processBranch = Parameter.ROB_AGENCY_BRANCH_CODE;
		if(reciept.getCounterSessionId()!=null){//Mean pay over OTC
			processBranch = reciept.getCounterSessionId().getBranch();
			robRenewal.setProsessingBranch(processBranch);
			robRenewalDao.update(robRenewal);
		}
		
		if(StringUtils.isBlank(robRenewal.getProsessingBranch())){
			robRenewal.setProsessingBranch(processBranch);
			robRenewalDao.update(robRenewal);
		}
		
		param.setAgencyBranchCode(robRenewal.getProsessingBranch());
		param.setRenewalIncentive(robRenewal.getRenewalIncentive()); //set OKU incentive..
		param.setBranchCode(Parameter.EZBIZ_BRANCH_CODE);
		param.setReqRefNo(robRenewal.getTransCode());
		param.setBrNo(robRenewal.getBrNo());
		param.setCheckDigit(robRenewal.getChkDigit());
		param.setNumberofYears(robRenewal.getYearRenew());
		DecimalFormat df = new DecimalFormat(".00");
		if(robRenewal.isPaidCmp()){
			param.setCmpAmount(robRenewal.getCmpAmt());
			double amt = robRenewal.getTotalAmt()-robRenewal.getBusinessInfoAmt()-robRenewal.getCmpAmt();
			amt = Double.valueOf(df.format(amt));
			param.setAllAmount(amt);
		}else{
			param.setCmpAmount(0);
			double amt = robRenewal.getTotalAmt()-robRenewal.getBusinessInfoAmt();
			amt = Double.valueOf(df.format(amt));
			param.setAllAmount(amt);
		}
		
		param.setPaymentDate(robRenewal.getPaymentDt());
		
		param.setPayerName(llpPaymentTransaction.getPayerName()+"-"+llpPaymentTransaction.getPayerId());
		param.setListOwner(new BizOwnerInfo[0]);
		
		try {
			BusinessFormA1LodgeResp resp = RobClient.lodgeFormA1(url, param);
			if("00".equals(resp.getSuccessCode())){
				robRenewal.setStatus("S");
				try {
					robRenewal.setNewExpDate(endDtFormat.parse(resp.getEndDate()));
					robRenewal.setCmpNo(resp.getCompoundNo());
					robRenewal.setApproveRejectDt(new Date());
				} catch (Exception e) {
					System.out.println("Cannot set end Date or cmp no");
				}
				robRenewalDao.update(robRenewal);
				
				if(Parameter.ROB_FORM_A1_INCENTIVE_oku.equals(robRenewal.getRenewalIncentive())){
					BusinessInfo bizInfo = findBusinessByRegNoWS(robRenewal.getBrNo());
					insertRobIncentiveFormA1(robRenewal, bizInfo);
				}
				
			}else if(TransactionStatus.ERR_REFERENCE_NO_CODE.equals(resp.getSuccessCode())){
				System.out.println(robRenewal.getTransCode()+":"+resp.getErrorMsg());
			}else{
				throw new SSMException(resp.getSuccessCode()+":"+resp.getErrorMsg());
			}
		}  catch (SSMBusinessException e) {
			throw new SSMException(e.getCode());
		}catch (Exception e) {
			throw new SSMException(e.getMessage());
		}
		try {
			getCertDataFromWS(robRenewal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public RobRenewal findByIdWithData(String id)throws SSMException {
		RobRenewal robRenewal;
		byte[] certByData=null;
		byte[] cmpByData=null;
		byte[] formA1Data=null;
		byte[] businessInfoData=null;
		try {
			robRenewal = robRenewalDao.findByIdWithData(id);
			if(robRenewal.getFormA1FileData()!=null && robRenewal.getFormA1FileData().getFileData().length>0){
				formA1Data = robRenewal.getFormA1FileData().getFileData();
			}else{
				generateA1Form(robRenewal);
			}
			
			if(Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus())){
				if(robRenewal.getCertFileData()!=null){
					certByData = robRenewal.getCertFileData().getFileData();
				}
				
				if(StringUtils.isNotBlank(robRenewal.getCmpNo())){
					cmpByData = robRenewal.getCmpFileData().getFileData();
				}
				
				if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
					if(robRenewal.getBusinessInfoData()!=null){
						businessInfoData = robRenewal.getBusinessInfoData().getFileData();
					}
				}
			}
		} catch (Exception e) {
			robRenewal = robRenewalDao.findById(id);
		}
		
		if(Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus())){
			try {
				if(certByData==null || certByData.length==0){
					robRenewal = getCertDataFromWS(robRenewal);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(StringUtils.isNotBlank(robRenewal.getCmpNo())){
					if(cmpByData==null || cmpByData.length==0){
						robRenewal = getCmpDataFromWS(robRenewal);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
					if(businessInfoData==null || businessInfoData.length==0){
						String refNo = robRenewal.getTransCode();
						String brNo = robRenewal.getBrNo()+"-"+robRenewal.getChkDigit();
						
						byte byteInfo[] = getFormA1BusinessInfoDataFromWS(refNo, brNo);
						if(byteInfo!=null && byteInfo.length>0){
							LlpFileData bisInfoFileData = new LlpFileData();
							bisInfoFileData.setFileData(byteInfo);
							bisInfoFileData.setFileDataType("PDF");
							llpFileDataService.insert(bisInfoFileData);
							robRenewal.setBusinessInfoData(bisInfoFileData);
							update(robRenewal);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return robRenewal;
	}
	
	private byte[] getFormA1BusinessInfoDataFromWS(String refNo, String brNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.generateInfo");
			
			
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(refNo);
			List<LlpPaymentTransactionDetail> listPaymentDetail = llpPaymentTransactionDetailService.findByTransactionIdAndPaymentItem(paymentTransaction.getTransactionId(), Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			
			LlpPaymentTransactionDetail paymentDetail = listPaymentDetail.get(0);
			
			LlpPaymentReceipt paymentReceipt = llpPaymentReceiptService.find(paymentTransaction.getTransactionId());
			String recieptNo = paymentReceipt.getReceiptNo();
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setTotalAmount(BigDecimal.valueOf(paymentDetail.getAmountWithOutGst()));
			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(refNo);
			
			String bizInfoGstRate = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GST_CODE, paymentDetail.getGstCode());
			param.setGstAmount(new BigDecimal(0));
			if(StringUtils.isNotBlank(bizInfoGstRate)){
				double gstAmt = paymentDetail.getAmountWithOutGst() * Double.valueOf(bizInfoGstRate);
				param.setGstAmount(BigDecimal.valueOf(gstAmt));
			}

			List<SSMInfoReq> ssmInfoReqList = new ArrayList<SSMInfoReq>();

			SSMInfoReq infoReq = new SSMInfoReq();
			infoReq.setEntityNo(brNo);
			infoReq.setEntityType(InfoParameter.ENTITY_TYPE_ROB);
			infoReq.setInfoType(InfoParameter.ROB_INFO);
			infoReq.setLanguage(InfoParameter.LANG_BM);
			infoReq.setQuantity(1);
			ssmInfoReqList.add(infoReq);
			param.setSSMInfoListReq(ssmInfoReqList.toArray(new SSMInfoReq[0]));

			SSMInfoGenerateResp certResponse = SSMInfoClient.generateInfo(url, param);
			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				return bizInfo[0].getPdfInfo();
				
			} else if (SSMInfoException.INVOICE_INVALID_CODE.equals(certResponse.getSuccessCode())) {
				return getRegenerateFormA1BusinessInfoDataFromWS(refNo, recieptNo);
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
	private byte[] getRegenerateFormA1BusinessInfoDataFromWS(String refNo, String recieptNo) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("SSMInfoClient.reprintInfo");
			SSMInfoGenerateReq param = new SSMInfoGenerateReq();

			param.setInvoiceNo(recieptNo);
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setReqRefNo(refNo);
			
			SSMInfoGenerateResp certResponse = SSMInfoClient.reprintInfo(url, param);
			
			if ("00".equals(certResponse.getSuccessCode())) {
				SSMInfoResp[] bizInfo = certResponse.getSSMInfoListResp();
				return bizInfo[0].getPdfInfo();
			} else {
				throw new SSMException(certResponse.getSuccessCode() + ":" + certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
	private RobRenewal getCmpDataFromWS(RobRenewal robRenewal) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getCompoundNotice");
			
			FindCompoundNoticeByCompoundNoReq param = new FindCompoundNoticeByCompoundNoReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			
			String brNo = StringUtils.split(robRenewal.getBrNo(),"-")[0];
			param.setBrNo(brNo);
			param.setCompoundNumber(robRenewal.getCmpNo());
			
			FindCompoundNoticeByCompoundNoResp compoundNoResp = RobClient.getCompoundNotice(url, param);
			if("00".equals(compoundNoResp.getSuccessCode())){
				LlpFileData cmpFilePdf = new LlpFileData();
				cmpFilePdf.setFileData(compoundNoResp.getBarcodeImage());
				cmpFilePdf.setFileDataType("PDF");
				llpFileDataService.insert(cmpFilePdf);
				robRenewal.setCmpFileData(cmpFilePdf);
				robRenewalDao.update(robRenewal);
			}else{
				throw new SSMException(compoundNoResp.getSuccessCode()+":"+compoundNoResp.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		}
		return robRenewal;
	}
	
	@Override
	public RobRenewal getCertDataFromWS(RobRenewal robRenewal) throws SSMException {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getCertificatePrinted");
			
			CertificateBusinessInfoReq certificateBusinessInfoReq = new CertificateBusinessInfoReq();
			certificateBusinessInfoReq.setAgencyId(Parameter.ROB_AGENCY_ID);
			certificateBusinessInfoReq.setBrNo(robRenewal.getBrNo());
			certificateBusinessInfoReq.setCertificateType("renewal");
			
			CertificateBusinessInfoResp certResponse = RobClient.getCertificatePrinted(url, certificateBusinessInfoReq);
			if("00".equals(certResponse.getSuccessCode())){
				LlpFileData certFileData = new LlpFileData();
				certFileData.setFileData(certResponse.getCertImage());
				certFileData.setFileDataType("PDF");
				llpFileDataService.insert(certFileData);
				robRenewal.setCertFileData(certFileData);
				robRenewalDao.update(robRenewal);
				
			}else{
				throw new SSMException(certResponse.getSuccessCode()+":"+certResponse.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return robRenewal;
	}

	@Override
	public BusinessInfo findBusinessByRegNoWS(String brNo, String chkDigit) throws SSMException{
		String url = wSManagementService.getWsUrl("RobClient.findBusinessByBrNo");
		
		FindBusinessByBrNoReq param = new FindBusinessByBrNoReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		param.setBrNo(brNo);
		
		try {
			BusinessInfoResp response = RobClient.findBusinessByBrNo(url, param);
			if("00".equals(response.getSuccessCode())){
				if(response.getListBusinessInfo().length==0){
					return null;
				}
				BusinessInfo businessInfo = response.getListBusinessInfo()[0];
				if(businessInfo.getChkDigit().equals(chkDigit)){
					return businessInfo;
				}else{
					return null;
				}
			}else{
				throw new SSMException(response.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
	public List<RobBizDetailInfo> findListDetailBizByIDWS(String icNo) throws SSMException{
		String url = wSManagementService.getWsUrl("RobClient.findBusinessByBrNo");
		
		FindListDetailBizByIDReq param = new FindListDetailBizByIDReq();
		param.setAgencyBranchCode("KL");
		param.setAgencyId("EZBIZ");
		param.setIcNo(icNo);
		param.setReqDate(new Date());
		param.setReqRefNo(new Date().toString());
		
		try {
			FindListDetailBizByIDResp response = RobClient.findListDetailBizByID(url, param);
			if("00".equals(response.getSuccessCode())){
				return Arrays.asList(response.getRobBizDetailInfoList());
			}else{
				throw new SSMException(response.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}

	@Override
	public List<RobBusinessSummaryInfo> findAllBizDetailByIcWS(String idNo) throws SSMException{
		
		try {
			List<RobBizDetailInfo> listBiz = findListDetailBizByIDWS(idNo);
			List<RobBusinessSummaryInfo> listBizDet = new ArrayList<RobBusinessSummaryInfo>();
			
			for (int i = 0; i < listBiz.size(); i++) {
				RobBusinessSummaryInfo bInfo = new RobBusinessSummaryInfo();
				bInfo.setBrNo(listBiz.get(i).getBrNo());
				bInfo.setChkDigit(listBiz.get(i).getChkDigit());
				bInfo.setBizName(listBiz.get(i).getBizName());
				bInfo.setBizStatus(listBiz.get(i).getBizStatus());
				bInfo.setBizType(listBiz.get(i).getBizType());
				bInfo.setBranchCount(listBiz.get(i).getBranchCount());
				
				bInfo.setBizDesc(listBiz.get(i).getBizDesc());
				bInfo.setBizStartDate(listBiz.get(i).getBizStartDate());
				bInfo.setBizExpDate(listBiz.get(i).getBizExpDate());
				bInfo.setBizMainAddr(listBiz.get(i).getBizMainAddr());
				bInfo.setBizMainAddr2(listBiz.get(i).getBizMainAddr2());
				bInfo.setBizMainAddr3(listBiz.get(i).getBizMainAddr3());
				bInfo.setBizMainAddrTown(listBiz.get(i).getBizMainAddrTown());
				bInfo.setBizMainAddrPostcode(listBiz.get(i).getBizMainAddrPostcode());
				bInfo.setBizMainAddrState(listBiz.get(i).getBizMainAddrState());
				bInfo.setBizMainAddrTelNo(listBiz.get(i).getBizMainAddrTelNo());
				bInfo.setBizMainAddrMobileNo(listBiz.get(i).getBizMainAddrMobileNo());
				bInfo.setBizMainAddrEmail(listBiz.get(i).getBizMainAddrEmail());
				
				listBizDet.add(bInfo);
			}
			
			
			Collections.sort(listBizDet, new Comparator<RobBusinessSummaryInfo>() {
				  public int compare(RobBusinessSummaryInfo o1, RobBusinessSummaryInfo o2) {
				      return o1.getBizExpDate().compareTo(o2.getBizExpDate());
				  }
				});
			
			return listBizDet;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new SSMException(ex);
		}
		
		
	}

	@Override
	public BusinessInfo findBusinessByRegNoWS(String regNo) throws SSMException {
		String url = wSManagementService.getWsUrl("RobClient.findBusinessByBrNo");
		
		FindBusinessByBrNoReq param = new FindBusinessByBrNoReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		param.setBrNo(regNo);
		
		try {
			BusinessInfoResp response = RobClient.findBusinessByBrNo(url, param);
			if("00".equals(response.getSuccessCode())){
				if(response.getListBusinessInfo().length==0){
					return null;
				}
				BusinessInfo businessInfo = response.getListBusinessInfo()[0];
				return businessInfo;
			}else{
				throw new SSMException(response.getErrorMsg());
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
	//@Override
	//@Transactional
	private void insertRobIncentiveFormA1(RobRenewal robRenewal, BusinessInfo businessInfo) throws SSMException { //insert after lodge to CBSROB
		List <RobIncentive> listRobIncentive = new ArrayList<RobIncentive>();
		for (int i = 0; i < businessInfo.getListOwner().length; i++) {
			RobIncentive robIncentive = new RobIncentive();
			BizOwnerInfo owner = businessInfo.getListOwner()[i];
			RobUserOku robUserOku = robUserOkuService.findOkuByIdTypeAndIdNo(Parameter.ID_TYPE_newic, owner.getIcNo());
			
			robIncentive.setUserRefNo(robUserOku.getUserProfile().getUserRefNo());
			robIncentive.setIdNo(owner.getIcNo());
			robIncentive.setOkuRefNo(robUserOku.getOkuRefNo());
			robIncentive.setIncentiveType(Parameter.ROB_FORM_A1_INCENTIVE_oku);
			robIncentive.setIncentiveForm(Parameter.ROB_FORM_TYPE_RENEWAL);
			robIncentive.setRobFormCode(robRenewal.getTransCode());
			robIncentive.setIncentiveApplicationDt(robRenewal.getPaymentDt()); //rob_renewal update date or create date?
			robIncentive.setProcessingBranch(robRenewal.getProsessingBranch()); 
			robIncentive.setBrNo(robRenewal.getBrNo()); 
			robIncentive.setCheckDigit(robRenewal.getChkDigit());
			robIncentive.setIncentiveApprovalDt(robRenewal.getApproveRejectDt());
			robIncentive.setApplicationStatus(robRenewal.getStatus());
			listRobIncentive.add(robIncentive);
		}
		robIncentiveDao.insertAll(listRobIncentive);
		
	}

}