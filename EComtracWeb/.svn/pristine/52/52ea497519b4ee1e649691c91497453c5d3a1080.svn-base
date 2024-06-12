package com.ssm.ezbiz.service.impl;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.eghl.EGHLPaymentResponse;
import com.ssm.ezbiz.eghl.EGHLQueryResponse;
import com.ssm.ezbiz.service.EGHLService;
import com.ssm.ezbiz.service.PaymentService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.pg.model.ReturnStatus;
import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.model.StoreData;
import com.ssm.llp.base.pg.model.StoreDataResponse;
import com.ssm.llp.base.pg.ws.SsmPgWSStub;
import com.ssm.llp.base.pg.xsd.Payment;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;

@Service
public class PaymentServiceImpl extends BaseServiceImpl<Object, String> implements PaymentService {
	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	LlpParametersService llpParametersService;

	@Autowired
	LlpUserProfileService llpUserProfileService;

	@Autowired
	LlpRunningNoService llpRunningNoService;

	@Autowired
	@Qualifier("LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Autowired
	@Qualifier("EGHLService")
	EGHLService eghlService;

//	private static Set transactionIdSet = new HashSet();

//	@Override
//	public StoreDataResponse storeData(LlpPaymentTransaction llpPaymentTransaction, List<LlpPaymentTransactionDetail> listPaymentDetails)
//			throws SSMException {
//		StoreDataResponse storeDataResponse = null;
//		// Insert into DB
//
//		String paymentDet = "";
//		double total = 0;
//		double totalGst = 0;
//		if (listPaymentDetails != null && !listPaymentDetails.isEmpty()) {
//			for (Iterator iterator = listPaymentDetails.iterator(); iterator.hasNext();) {
//				LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
//				total += llpPaymentTransactionDetail.getAmount();
//				totalGst += llpPaymentTransactionDetail.getGstAmt();
//			}
//		}
//		DecimalFormat df = new DecimalFormat("#.00");
//		total = Double.valueOf(df.format(total));
//		totalGst = Double.valueOf(df.format(totalGst));
//
//		llpPaymentTransaction.setTransactionType(llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PG_PAY_TRAN_TYPE));
//		llpPaymentTransaction.setRequestDt(new Date());
//		llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_PENDING);
//		llpPaymentTransaction.setAmount(total);
//		llpPaymentTransaction.setGstAmt(totalGst);
//		llpPaymentTransactionService.insert(llpPaymentTransaction);
//
//		if (listPaymentDetails != null && !listPaymentDetails.isEmpty()) {
//			for (Iterator iterator = listPaymentDetails.iterator(); iterator.hasNext();) {
//				LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
//
//				llpPaymentTransactionDetail.setTransactionId(llpPaymentTransaction.getTransactionId());
//				llpPaymentTransactionDetailService.insert(llpPaymentTransactionDetail);
//
//				String itemDesc = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_TYPE, llpPaymentTransactionDetail.getPaymentItem());
//
//				if(StringUtils.isNotBlank(itemDesc) && StringUtils.isBlank(paymentDet)){
//					paymentDet = itemDesc;
//				}
//			}
//		}
//		SsmPgWSStub ssmPgWSStub = null;
//		try{
//			String ssmPgUrl = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PG_WS_URL);
//			ssmPgWSStub = new SsmPgWSStub(ssmPgUrl);
//		}catch (Exception e){
//			throw new SSMException(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//		}
//
//		StoreData storeData = new StoreData();
//
//		storeData.setId(llpPaymentTransaction.getTransactionId());// LyyyyMMddXXXXX
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		storeData.setRequest_date(sdf.format(llpPaymentTransaction.getRequestDt()).toString());// yyyyMMddHHmmss
//		// (20121203131010 for 3
//		// December 2012
//		// 13:10:10)
//		total = total * 100;
//		String strAmt = df.format(total);
//		String[] strAmts = StringUtils.split(strAmt, ".");
//		strAmt = strAmts[0];
//
//		storeData.setAmount(strAmt);// amount
//									// without
//									// (.).
//									// For
//									// example
//									// 505
//		// for RM5.05
//		storeData.setType(llpPaymentTransaction.getTransactionType());// default to 1
//		storeData.setIcPassport(llpPaymentTransaction.getPayerId());
//		storeData.setName(llpPaymentTransaction.getPayerName());
//		storeData.setDetails(paymentDet);
//
//		try{
//			storeDataResponse = ssmPgWSStub.storeData(storeData);
//		}catch (Exception e){
//			throw new SSMException(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//		}
//
//		System.out.println("storeDataResponse >> " + storeDataResponse.get_return());
//		return storeDataResponse;
//	}


	@Override
	public BaseDao getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}

//	public static void main(String[] args) throws Exception{
//		String ssmPgUrl = "http://192.168.3.84/axis2/services/SsmPgServices?wsdl";
//		SsmPgWSStub ssmPgWSStub = new SsmPgWSStub(ssmPgUrl);
//
//		ReturnStatus returnStatus = new ReturnStatus();
//		returnStatus.setId("E2015091500004");
//
//		ReturnStatusResponse response = ssmPgWSStub.returnStatus(returnStatus);
//		System.out.println(">>"+response.get_return().getTransStatus());
//		if (response != null && response.get_return() != null && StringUtils.isNotBlank(response.get_return().getTransStatus()) && (SUCCESS_TRANSACT.equals(response.get_return().getTransStatus()))) {
//
//			if (response.get_return() != null) {
//				Payment payment = response.get_return();
//
//				System.out.println(payment.getTransMode());
//				System.out.println(payment.getTransDetail());
//				System.out.println(payment.getTransApprovalCode());
//			}
//		}
//	}

	@Override
	@Transactional
	public LlpPaymentTransaction checkAndUpdatePaymentStatus(LlpPaymentTransaction llpPaymentTransaction)throws SSMException{
		String tranId = llpPaymentTransaction.getTransactionId();
//		if(transactionIdSet.contains(tranId)){
//			System.err.println("Payment Already process for :"+tranId+ " please try again later!!");
//			return llpPaymentTransaction;
//		}
//		transactionIdSet.add(tranId);
		try {
			//Control 5 minit after Payment for scheduler
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -10);
			if(llpPaymentTransaction.getCreateDt().after(cal.getTime())){// To early to check
				System.err.println("Early Check:"+llpPaymentTransaction.getTransactionId());
				return llpPaymentTransaction;
			}
			if(Parameter.PAYMENT_DETAIL_OTC.equals(llpPaymentTransaction.getPaymentDetail())){
				return llpPaymentTransaction;
			}
			EGHLQueryResponse response = eghlService.getPaymentStatus(llpPaymentTransaction.getTransactionId(), llpPaymentTransaction.getAmount());
			if(response!=null && StringUtils.isNotBlank(response.getTxnStatus())){
				if(EGHL_TRANSACT_SUCCESS.equals(response.getTxnStatus())){

					llpPaymentTransaction.setPaymentMode(response.getPymtMethod());
					llpPaymentTransaction.setPaymentDetail(response.getIssuingBank());
					llpPaymentTransaction.setApprovalCode(response.getAuthCode());

					llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_SUCCESS);
					llpPaymentTransactionService.update(llpPaymentTransaction);

					LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
					llpPaymentReceipt.setCashReceived(llpPaymentTransaction.getAmount());
					llpPaymentReceipt.setTotalAmount(llpPaymentTransaction.getAmount());
					llpPaymentReceipt.setBalance(Double.valueOf(0));
					llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
					llpPaymentReceipt.setTransactionId(llpPaymentTransaction.getTransactionId());


					if(llpPaymentTransactionDetailService.isHaveSrProduct(llpPaymentTransaction.getTransactionId()) == true){
						String taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO, Parameter.INVOICE_FIELDCODE, null, null, "000000000", new Date());
						llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
					}
					llpPaymentReceiptService.insert(llpPaymentReceipt);

					LlpUserProfile user  = llpUserProfileService.findById(llpPaymentTransaction.getCreateBy());
					if(user!=null){
						String email = user.getEmail();

						if (!StringUtils.isEmpty(email)) {
							DecimalFormat df = new DecimalFormat("#0.00");
							mailService.sendMail(email, "email.notification.payment.success.subject", llpPaymentReceipt.getReceiptNo(), "email.notification.payment.success.body",
									llpPaymentReceipt.getReceiptNo(), df.format(llpPaymentTransaction.getAmount()),
									llpPaymentTransaction.getPaymentMode(), LlpDateUtils.formatDate(llpPaymentTransaction.getRequestDt()));
						}
					}
					return llpPaymentTransaction;
				}else if(EGHL_TRANSACT_PENDING.equals(response.getTxnStatus())){
					llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_PENDING);
				}else{

					if(response.getTxnMessage()!=null && "Invalid Input".equals(response.getTxnMessage().trim())){
						llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_PENDING);
					}else{
						llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_FAIL);
					}

				}
				llpPaymentTransaction.setRemark(response.getTxnStatus()+":"+response.getTxnExists()+":\n"+response.getTxnMessage()+":\n"+response.getQueryDesc());
				llpPaymentTransactionService.update(llpPaymentTransaction);
			}
		} catch (Exception e) {
//			if(transactionIdSet.contains(tranId)){
//				transactionIdSet.remove(tranId);
//			}
			throw new SSMException(e);
		}

		return llpPaymentTransaction;
	}

//	@Override
//	public ReturnStatusResponse checkPaymentStatus(String id, double amount) throws SSMException {
//
//		//Implement eGhl CheckHere
//		try {
//			EGHLQueryResponse queryResponse = eghlService.getPaymentStatus(id, amount);
//
//			return returnStatusResponse;
//		} catch (Exception e) {
//			throw new SSMException(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//		}
//
//
//
////		System.out.println("checkPaymentStatus >> " + id);
////		ReturnStatusResponse returnStatusResponse = null;
////
////		try{
////			String ssmPgUrl = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PG_WS_URL);
////			SsmPgWSStub ssmPgWSStub = new SsmPgWSStub(ssmPgUrl);
////
////			ReturnStatus returnStatus = new ReturnStatus();
////			returnStatus.setId(id);
////
////
////			returnStatusResponse = ssmPgWSStub.returnStatus(returnStatus);
////
////			//Always bypass
//////			returnStatusResponse = new ReturnStatusResponse();
//////			returnStatusResponse.set_return(new Payment());
//////			returnStatusResponse.get_return().setTransStatus(SUCCESS_TRANSACT);
//////			returnStatusResponse.get_return().setTransMode("credit card");
//////			returnStatusResponse.get_return().setTransMode("TEST");
////		}catch(Exception e){
////			throw new SSMException(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
////		}
////
////		return returnStatusResponse;
//	}

	@Override
	@Transactional
	public void paymentSuccess(String id, EGHLPaymentResponse eghlPaymentResponse) throws SSMException {
//		if(transactionIdSet.contains(id)){
//			System.out.println("Payment Already process for :"+id+ " please try again later!!");
//			return;
//		}
		try {
//			transactionIdSet.add(id);
			if (id != null) {
				LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(id);
				if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
					System.out.println("Payment Already Success for : " + id);
					LlpPaymentReceipt receipt = llpPaymentReceiptService.find(llpPaymentTransaction.getTransactionId());

					if(StringUtils.isBlank(receipt.getReceiptNo())){
						System.out.println("Status Success but no receipt found for : " + id);
						LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
						llpPaymentReceipt.setCashReceived(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setTotalAmount(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setBalance(Double.valueOf(0));
						llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
						llpPaymentReceipt.setTransactionId(llpPaymentTransaction.getTransactionId());

						if(llpPaymentTransactionDetailService.isHaveSrProduct(llpPaymentTransaction.getTransactionId()) == true){
							String taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO, Parameter.INVOICE_FIELDCODE, null, null, "000000000", new Date());
							llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
						}
						llpPaymentReceiptService.insert(llpPaymentReceipt);
					}

					return;
				}
				if(Parameter.PAYMENT_DETAIL_OTC.equals(llpPaymentTransaction.getPaymentDetail())){
					System.out.println("THIS IS OTC PAYMENT :"+id);
					throw new SSMException("THIS IS OTC PAYMENT :"+id);
//					return ;
				}

//				ReturnStatusResponse response = checkPaymentStatus(id);
//				ReturnStatusResponse response = new ReturnStatusResponse();
//				response.set_return(new Payment());
//				response.get_return().setTransStatus(SUCCESS_TRANSACT);
//				response.get_return().setTransMode("credit card");
//				response.get_return().setTransMode("XXXXXXXXXXXX3859");
//				response.get_return().setTransMode("TEST");

				EGHLQueryResponse response = null;
				if(eghlPaymentResponse!=null){
					String hashId = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_HASH_ID);
					if(eghlPaymentResponse.isHashingValid(hashId)){
						//Convert Payment Response become query response
						response = new EGHLQueryResponse(eghlPaymentResponse);
					}
				}
				if(response==null){
					response = eghlService.getPaymentStatus(id, llpPaymentTransaction.getAmount());
				}
				if(response!=null && StringUtils.isNotBlank(response.getTxnStatus())){
//					System.out.println("RES >> "+id+":"+response.getTxnStatus());
					if(EGHL_TRANSACT_SUCCESS.equals(response.getTxnStatus())){
//						if (response.get_return() != null) {
//							Payment payment = response.get_return();
//							if("CC".equals(response.getPymtMethod())){
//								llpPaymentTransaction.setPaymentMode("credit card");
//							}else if("DD".equals(response.getPymtMethod())){
//								llpPaymentTransaction.setPaymentMode("fpx");
//							}else{
//								llpPaymentTransaction.setPaymentMode(response.getPymtMethod());
//							}
							llpPaymentTransaction.setPaymentMode(response.getPymtMethod());
							llpPaymentTransaction.setPaymentDetail(response.getIssuingBank());
							llpPaymentTransaction.setApprovalCode(response.getAuthCode());
//						}

						llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_SUCCESS);
						llpPaymentTransactionService.update(llpPaymentTransaction);

						LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
						llpPaymentReceipt.setCashReceived(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setTotalAmount(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setBalance(Double.valueOf(0));
						llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
						llpPaymentReceipt.setTransactionId(llpPaymentTransaction.getTransactionId());

						if(llpPaymentTransactionDetailService.isHaveSrProduct(llpPaymentTransaction.getTransactionId()) == true){
							String taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO, Parameter.INVOICE_FIELDCODE, null, null, "000000000", new Date());
							llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
						}
						llpPaymentReceiptService.insert(llpPaymentReceipt);

						String email = UserEnvironmentHelper.getUserenvironment().getEmail();

						if (!StringUtils.isEmpty(email)) {
							DecimalFormat df = new DecimalFormat("#0.00");
							mailService.sendMail(email, "email.notification.payment.success.subject", llpPaymentReceipt.getReceiptNo(), "email.notification.payment.success.body",
									llpPaymentReceipt.getReceiptNo(), df.format(llpPaymentTransaction.getAmount()),
									llpPaymentTransaction.getPaymentMode(), LlpDateUtils.formatDate(llpPaymentTransaction.getRequestDt()));
						}
					}else if(EGHL_TRANSACT_PENDING.equals(response.getTxnStatus())){
						failTransact(id, SSMExceptionParam.PAYMENT_RESPONSE_PENDING);

					}else{
						if(response.getTxnMessage()!=null && "Invalid Input".equals(response.getTxnMessage().trim())){
							failTransact(id, SSMExceptionParam.PAYMENT_RESPONSE_PENDING);
						}else{
							llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_FAIL);
							llpPaymentTransactionService.update(llpPaymentTransaction);
							failTransact(id, SSMExceptionParam.PAYMENT_RESPONSE_FAIL, response.getTxnStatus()+":"+response.getQueryDesc()+":\n"+response.getTxnMessage());
						}
					}
				}else{
					failTransact(id, SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
				}

			} else {
				failTransact(id, SSMExceptionParam.PAYMENT_ID_INVALID);
			}
//			if(transactionIdSet.contains(id)){
//				transactionIdSet.remove(id);
//			}
		} catch (Exception e) {
			e.printStackTrace();
//			if(StringUtils.isNotBlank(e.getMessage())){
//				failTransact(id, e.getMessage());
//			}else{
//				failTransact(id, SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//			}
//			if(transactionIdSet.contains(id)){
//				transactionIdSet.remove(id);
//			}
			throw new SSMException(e);
		}
	}

	private void failTransact(String id, String errMsg) throws SSMException {
		failTransact(id, errMsg, "");
	}
	private void failTransact(String id, String errMsg, String errFailDesc) throws SSMException {
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(id);

		if (llpPaymentTransaction != null) {
			if(SSMExceptionParam.PAYMENT_RESPONSE_PENDING.equals(errMsg)){
				llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_PENDING);
			}else{
				if(Parameter.PAYMENT_DETAIL_OTC.equals(llpPaymentTransaction.getPaymentDetail())){
					llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_CANCEL);
				}else{
					llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_FAIL);
					llpPaymentTransaction.setRemark(errFailDesc);
				}

			}

			try {
				llpPaymentTransactionService.update(llpPaymentTransaction);
			} catch (Exception e) {
				throw new SSMException(e);
			}

		}
		System.err.println(errMsg);
		throw new SSMException(errMsg);
	}

	@Override
	public LlpPaymentTransaction processPayment(LlpPaymentTransaction llpPaymentTransaction, List<LlpPaymentTransactionDetail> listPaymentDetails) throws SSMException {

		String paymentDet = "";
		double total = 0;
		double totalGst = 0;
		if (listPaymentDetails != null && !listPaymentDetails.isEmpty()) {
			for (Iterator iterator = listPaymentDetails.iterator(); iterator.hasNext();) {
				LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
				total += llpPaymentTransactionDetail.getAmount();
				totalGst += llpPaymentTransactionDetail.getGstAmt();
			}
		}
		DecimalFormat df = new DecimalFormat("#.00");
		total = Double.valueOf(df.format(total));
		totalGst = Double.valueOf(df.format(totalGst));

		llpPaymentTransaction.setTransactionType(llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PG_PAY_TRAN_TYPE));
		llpPaymentTransaction.setRequestDt(new Date());
		llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_PENDING);
		llpPaymentTransaction.setAmount(total);
		llpPaymentTransaction.setGstAmt(totalGst);
		llpPaymentTransactionService.insert(llpPaymentTransaction);

		if (listPaymentDetails != null && !listPaymentDetails.isEmpty()) {
			for (Iterator iterator = listPaymentDetails.iterator(); iterator.hasNext();) {
				LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();

				llpPaymentTransactionDetail.setTransactionId(llpPaymentTransaction.getTransactionId());
				llpPaymentTransactionDetailService.insert(llpPaymentTransactionDetail);

				String itemDesc = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_TYPE, llpPaymentTransactionDetail.getPaymentItem());

				if(StringUtils.isNotBlank(itemDesc) && StringUtils.isBlank(paymentDet)){
					paymentDet = itemDesc;
				}
			}
		}

		return llpPaymentTransaction;


//		StoreDataResponse storeDataResponse = storeData(llpPaymentTransaction, transactionDetails);
//
//		if (storeDataResponse != null) {
//			String ret = storeDataResponse.get_return();
//			System.out.println("storeDataResponse >> " + ret);
//
//			if (SUCCESS_STORE_DATA.equals(ret)) {
//				return SUCCESS_STORE_DATA;
//			} else {
//				failTransact(llpPaymentTransaction.getTransactionId(), SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//			}
//
//		} else {
//			failTransact(llpPaymentTransaction.getTransactionId(), SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
//		}
//
//		return null;
	}

	@Override
	public String getSuccessPaymentIdByAppRefNo(String appRefNo) {

		SearchCriteria scSuccess = new SearchCriteria("appRefNo",SearchCriteria.EQUAL,appRefNo);
		scSuccess = scSuccess.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.PAYMENT_STATUS_SUCCESS);
		scSuccess.addOrderBy("createDt", SearchCriteria.ASC);

		List<LlpPaymentTransaction> listPaymentSuccess = llpPaymentTransactionService.findByCriteria(scSuccess).getList();
		if(listPaymentSuccess.size()>0){
			return listPaymentSuccess.get(0).getTransactionId();
		}


		SearchCriteria scPending = new SearchCriteria("appRefNo",SearchCriteria.EQUAL,appRefNo);
		scPending = scPending.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.PAYMENT_STATUS_PENDING);
		scPending.addOrderBy("createDt", SearchCriteria.ASC);


		List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService.findByCriteria(scPending).getList();
		for (Iterator iterator = listPayment.iterator(); iterator.hasNext();) {
			LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) iterator.next();
			String paymentId = llpPaymentTransaction.getTransactionId();
			try {
				paymentSuccess(paymentId, null);
				return paymentId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
