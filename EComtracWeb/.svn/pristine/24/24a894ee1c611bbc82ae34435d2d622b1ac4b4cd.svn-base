/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.CmpTransactionService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpPaymentReceiptDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.common.service.RobPaymentCardInfoService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobPaymentCardInfo;
import com.ssm.llp.ezbiz.model.RobPosTerminalTransaction;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

@Service
public class LlpPaymentReceiptServiceImpl extends BaseServiceImpl<LlpPaymentReceipt,  String> implements LlpPaymentReceiptService{
	@Autowired 
	LlpPaymentReceiptDao llpPaymentReceiptDao;

	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;
	
	@Autowired 
	RobCounterSessionService robCounterSessionService;
	
	@Autowired
	LlpRunningNoService llpRunningNoService;
	
	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	RobFormAService robFormAService;
	
	@Autowired
	RobFormBService robFormBService;
	
	@Autowired
	RobFormCService robFormCService;
	
	@Autowired
	RobRenewalService robRenewalService;
	
	@Autowired
	CmpTransactionService cmpTransactionService;
	
	@Autowired
	RobTrainingTransactionService robTrainingTransactionService;
	
	@Autowired
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService; 
	
	
	@Autowired
	RobPaymentCardInfoService robPaymentCardInfoService;
	
	@Override
	public BaseDao getDefaultDao() {
		return llpPaymentReceiptDao;
	}

	@Override
	public LlpPaymentReceipt find(String transactionId) {
		LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
		
		SearchCriteria sc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);
		
		SearchResult sr = findByCriteria(sc);
		
		if(sr != null && !sr.getList().isEmpty()){
			llpPaymentReceipt = (LlpPaymentReceipt) sr.getList().get(0);
		}
		
		return llpPaymentReceipt;
	}
	
	@Override
	public Integer getCountTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		return llpPaymentReceiptDao.getCountTransactionByCounterSession(counterSessionId, receiptIsCancel);
	}
	
	@Override
	public Double getTotalTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		return llpPaymentReceiptDao.getTotalTransactionByCounterSession(counterSessionId, receiptIsCancel);
	}
	
	@Override
	public List<LlpPaymentReceipt> getAllTransactionByCounterSession(Integer counterSessionId, Integer receiptIsCancel){
		return llpPaymentReceiptDao.getAllTransactionByCounterSession(counterSessionId, receiptIsCancel);
	}
	
	
	//Call after success payment OTC payment
	@Transactional
	@Override
	public void lodgeWSAfterRecieptGenerate(final String transactionId)throws SSMException{
		
		String isUsingThread = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_OTC_PAYMENT_USING_THREAD);
		if(Parameter.YES_NO_yes.equals(isUsingThread)){
			TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
			theExecutor.execute(new Runnable() {
	            @Override
	            public void run () {
	            	try {
	            		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(transactionId);
	            		if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
	            			String appRefNo = llpPaymentTransaction.getAppRefNo();
	            			if (llpPaymentTransaction.getAppRefNo().startsWith("EB-A2")) {
	            					RobFormA robFormA = robFormAService.findAllById(appRefNo);
	            					robFormAService.lodgeFormAWs(robFormA);
	            			} else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B2")) {
	            					RobFormB robFormB = robFormBService.findAllById(appRefNo);
	            					robFormBService.lodgeFormBWs(robFormB);
	            			}else if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
	            					RobRenewal robRenewal = robRenewalService.findById(appRefNo);
	            					robRenewalService.lodgeRobFormA1(robRenewal);
	            			}else if(llpPaymentTransaction.getAppRefNo().startsWith("EB-CMP2")){
	            				LlpPaymentReceipt llpPaymentReceipt = find(transactionId);
	            				CmpTransaction cmpTransaction = cmpTransactionService.findById(appRefNo);
	            				cmpTransactionService.lodgeCompoundPaymentWS(cmpTransaction, llpPaymentReceipt);
	            			}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C2")) {
	            				RobFormC robFormC = robFormCService.findById(appRefNo);
	            				robFormCService.lodgeFormCWs(robFormC);
	            			}
	            		}
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        });
		}else{
			LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(transactionId);
    		if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
    			String appRefNo = llpPaymentTransaction.getAppRefNo();
    			if (llpPaymentTransaction.getAppRefNo().startsWith("EB-A2")) {
    					RobFormA robFormA = robFormAService.findAllById(appRefNo);
    					robFormAService.lodgeFormAWs(robFormA);
    			} else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B2")) {
    					RobFormB robFormB = robFormBService.findAllById(appRefNo);
    					robFormBService.lodgeFormBWs(robFormB);
    			}else if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
    					RobRenewal robRenewal = robRenewalService.findById(appRefNo);
    					robRenewalService.lodgeRobFormA1(robRenewal);
    			}else if(llpPaymentTransaction.getAppRefNo().startsWith("EB-CMP2")){
    				LlpPaymentReceipt llpPaymentReceipt = find(transactionId);
    				CmpTransaction cmpTransaction = cmpTransactionService.findById(appRefNo);
    				cmpTransactionService.lodgeCompoundPaymentWS(cmpTransaction, llpPaymentReceipt);
    			}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C2")) {
    				RobFormC robFormC = robFormCService.findById(appRefNo);
    				robFormCService.lodgeFormCWs(robFormC);
    			}
    		}
		}
	}
	
	//This is for OTC payment using integration card
	@Transactional
	@Override
	public LlpPaymentTransaction receivePaymentGenerateReceiptForIntegrationCardPayment(RobPosTerminalTransaction posTerminalTransaction){
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(posTerminalTransaction.getPaymentTransactionId());
		
		
		llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_OTC_CARD);
		llpPaymentTransaction.setPaymentDetail(Parameter.PAYMENT_DETAIL_OTC);
		llpPaymentTransaction.setApprovalCode(posTerminalTransaction.getApprovalCode());
		
		
		String binNo = posTerminalTransaction.getCreditCardNo().substring(0,6);
		RobPaymentCardInfo cardInfo = robPaymentCardInfoService.findById(binNo);
		String issuerBank="";
		if(cardInfo!=null) {
			if(StringUtils.isNotEmpty(cardInfo.getCardIssuerCode())) {
				issuerBank = cardInfo.getCardIssuerCode();
			}else {
				issuerBank = cardInfo.getCardIssuer();
			}
		}
		
		LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
		llpPaymentReceipt.setPaymentCardType(posTerminalTransaction.getCreditCardType());
		llpPaymentReceipt.setPaymentCardBank(issuerBank);
		
		
		return receivePaymentGenerateReceiptCommon(llpPaymentReceipt, llpPaymentTransaction, posTerminalTransaction.getCounterSessionId(), posTerminalTransaction.getTransAmount(), posTerminalTransaction.getTransAmount());
	}
	

	//This is for OTC payment using manual card
	@Transactional
	@Override
	public LlpPaymentTransaction receivePaymentGenerateReceiptForManualCardPayment(String transactionId, Integer counterSessionId, Double totalAmount, Double cashReceived, String approvalCode, String cardType, String cardBank){
		
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(transactionId);
		llpPaymentTransaction.setApprovalCode(approvalCode);
		llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_OTC_CARD_MANUAL);
		
		LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
		llpPaymentReceipt.setPaymentCardType(cardType);
		llpPaymentReceipt.setPaymentCardBank(cardBank);
		
		return receivePaymentGenerateReceiptCommon( llpPaymentReceipt, llpPaymentTransaction, counterSessionId, totalAmount, cashReceived);
	}
	
	//This is for OTC payment using cash
	@Transactional
	@Override
	public LlpPaymentTransaction receivePaymentGenerateReceiptForCash(String transactionId, Integer counterSessionId, Double totalAmount, Double cashReceived){
		
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(transactionId);
		return receivePaymentGenerateReceiptCommon( new LlpPaymentReceipt(), llpPaymentTransaction, counterSessionId, totalAmount, cashReceived);
	}
	
	//This is for OTC payment
	@Transactional
	public LlpPaymentTransaction receivePaymentGenerateReceiptCommon(LlpPaymentReceipt llpPaymentReceipt, LlpPaymentTransaction llpPaymentTransaction, Integer counterSessionId, Double totalAmount, Double cashReceived){
		
		
		RobCounterSession robCounterSession = null;
		if(counterSessionId != null){
			robCounterSession = robCounterSessionService.findById(counterSessionId);
		}
		String taxInvoiceNo = null;
		
		
		llpPaymentReceipt.setCounterSessionId(robCounterSession);
		llpPaymentReceipt.setTotalAmount(totalAmount);
		llpPaymentReceipt.setCashReceived(cashReceived);
		llpPaymentReceipt.setBalance(cashReceived - totalAmount);
		llpPaymentReceipt.setTransactionId(llpPaymentTransaction.getTransactionId());
		llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		
		if(llpPaymentTransactionDetailService.isHaveSrProduct(llpPaymentTransaction.getTransactionId()) == true){
			taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO, Parameter.INVOICE_FIELDCODE, null, null, "000000000", new Date());
			llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
		}
		insert(llpPaymentReceipt);
		
		llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_SUCCESS);
		llpPaymentTransactionService.update(llpPaymentTransaction);
		
		String appRefNo = llpPaymentTransaction.getAppRefNo();
		
		
		if (llpPaymentTransaction.getAppRefNo().startsWith("EB-A2")) {
			RobFormA robFormA = robFormAService.findById(appRefNo);
			robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
			robFormA.setPaymentDt(new Date());
			robFormA.setIsFromSSMPc(Parameter.YES_NO_yes);
			robFormAService.update(robFormA);
		} else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B2")) {
			RobFormB robFormB = robFormBService.findById(appRefNo);
			robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS);
			robFormB.setPaymentDt(new Date());
			robFormB.setIsFromSSMPc(Parameter.YES_NO_yes);
			robFormBService.update(robFormB);
			
		}else if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
			
			RobRenewal robRenewal = robRenewalService.findById(appRefNo);
			robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
			robRenewal.setPaymentDt(new Date());
			robRenewal.setIsFromSSMPc(Parameter.YES_NO_yes);
			robRenewalService.update(robRenewal);
			
		}else if(llpPaymentTransaction.getAppRefNo().startsWith("EB-CMP2")){
			CmpTransaction cmpTransaction = cmpTransactionService.findById(appRefNo);
			cmpTransaction.setStatus(Parameter.CMP_PAYMENT_PAYMENT_SUCCESS);
			cmpTransactionService.update(cmpTransaction);
			
		}else if(llpPaymentTransaction.getAppRefNo().startsWith("CP2")){
			RobTrainingTransaction robTrainingTransaction = robTrainingTransactionService.findByTransactionCode(appRefNo);
			robTrainingTransaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
			robTrainingTransactionService.update(robTrainingTransaction);
		}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C2")) {
			RobFormC robFormC = robFormCService.findById(appRefNo);
			robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS);
			robFormC.setPaymentDt(new Date());
			robFormC.setIsFromSSMPc(Parameter.YES_NO_yes);
			robFormCService.update(robFormC);
		}
		
		return llpPaymentTransaction;
	}
	
	@Override
	public LlpPaymentReceipt getReceiptNoByTransactionID(String transactionId){
		return llpPaymentReceiptDao.getReceiptNoByTransactionID(transactionId);
	}

}
