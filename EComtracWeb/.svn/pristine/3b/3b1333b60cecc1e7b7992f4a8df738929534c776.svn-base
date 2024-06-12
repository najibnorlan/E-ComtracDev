package com.ssm.ezbiz.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobPaymentCreditNoteDao;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;
import com.ssm.llp.ezbiz.model.RobRenewal;

@Service
public class RobPaymentCreditNoteServiceImpl extends BaseServiceImpl<RobPaymentCreditNote, String> implements RobPaymentCreditNoteService{

	@Autowired
	RobPaymentCreditNoteDao robPaymentCreditNoteDao;
	
	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;
	
	@Autowired
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;
	
	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	@Autowired
	RobFormAService robFormAService;
	
	@Autowired
	RobFormBService robFormBService;
	
	@Autowired
	RobFormCService robFormCService;
	
	@Autowired
	RobRenewalService robRenewalService;
//	
//	@Resource(name="creditNotesReversalService")
//	Map<String, String> creditNotesReversalService;
	
	@Override
	public BaseDao getDefaultDao() {
		return robPaymentCreditNoteDao;
	}

	@Override
	public RobPaymentCreditNote processCreditNote(LlpPaymentReceipt llpPaymentReceipt){
		DecimalFormat df = new DecimalFormat("-###.00");
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(llpPaymentReceipt.getTransactionId());
		List<LlpPaymentTransactionDetail> detailList = llpPaymentTransactionDetailService.find(llpPaymentTransaction.getTransactionId());
		
		LlpPaymentTransaction transaction = new LlpPaymentTransaction();
		transaction = llpPaymentTransaction;
		transaction.setPaymentMode(Parameter.PAYMENT_MODE_CN);
		transaction.setRequestDt(new Date());
		transaction.setAmount(Double.valueOf(df.format(llpPaymentTransaction.getAmount())));
		transaction.setPaymentDetail(null);
		
		llpPaymentTransactionService.insert(transaction);
		
		for(LlpPaymentTransactionDetail i : detailList){
			LlpPaymentTransactionDetail detail = new LlpPaymentTransactionDetail();
			detail.setTransactionId(transaction.getTransactionId());
			detail.setAmount(Double.valueOf(df.format(i.getAmount())));
			detail.setPaymentItem(i.getPaymentItem());
			detail.setQuantity(i.getQuantity());
			detail.setPaymentDet(i.getPaymentDet());
			detail.setGstAmt(Double.valueOf(df.format(i.getGstAmt())));
			detail.setGstCode(i.getGstCode());
			llpPaymentTransactionDetailService.insert(detail);
		}
		
		RobPaymentCreditNote paymentCreditNote = new RobPaymentCreditNote();
		paymentCreditNote.setAmount(llpPaymentReceipt.getTotalAmount());
		paymentCreditNote.setCounterSessionId(llpPaymentReceipt.getCounterSessionId());
		paymentCreditNote.setPaymentReceiptNo(llpPaymentReceipt);
		paymentCreditNote.setCnTransactionNo(transaction.getTransactionId());
		insert(paymentCreditNote);
		
		llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
		llpPaymentReceiptService.update(llpPaymentReceipt);
		
		
		//reverse transaction. set to cancel
		
//		///*******
//		
//		for (Iterator iterator = creditNotesReversalService.keySet().iterator(); iterator.hasNext();) {
//			String serviceRefNoPrefix = (String) iterator.next();
//			if(llpPaymentTransaction.getAppRefNo().startsWith(serviceRefNoPrefix)) {
//				String serviceId = creditNotesReversalService.get(serviceRefNoPrefix);
//				PaymentInterface paymentInterface = (PaymentInterface) WicketApplication.getBean(serviceId);
//				paymentInterface.creditNoteReversal(llpPaymentTransaction.getAppRefNo());
//			}
//		}
//		
//		
//		
//		String serviceId = creditNotesReversalService.get("EB-A");
//		if(StringUtils.isNotBlank(serviceId)) {
//			PaymentInterface ps = (PaymentInterface) WicketApplication.getBean(serviceId);
//			ps.creditNoteReversal(llpPaymentTransaction.getAppRefNo());
//			System.out.println(ps);
//		}
		
		///*******
		
		if(llpPaymentTransaction.getAppRefNo().startsWith("EB-A")){
			RobFormA formA = robFormAService.findById(llpPaymentTransaction.getAppRefNo());
			formA.setStatus(Parameter.ROB_FORM_A_STATUS_CANCEL);
			formA.setApproveRejectNotes("Credit Note");
			robFormAService.update(formA);
			
		}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B")){
			RobFormB formB = robFormBService.findById(llpPaymentTransaction.getAppRefNo());
			formB.setStatus(Parameter.ROB_FORM_B_STATUS_CANCEL);
			formB.setApproveRejectNotes("Credit Note");
			robFormBService.update(formB);
			
		}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C2")){//need to distinch with Compound
			RobFormC formC = robFormCService.findById(llpPaymentTransaction.getAppRefNo());
			formC.setStatus(Parameter.ROB_FORM_C_STATUS_CANCEL);
			formC.setApproveRejectNotes("Credit Note");
			robFormCService.update(formC);
			
		}else if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
			RobRenewal robRenewal = robRenewalService.findById(llpPaymentTransaction.getAppRefNo());
			robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_CANCEL);
			robRenewal.setApproveRejectNotes("Credit Note");
			robRenewalService.update(robRenewal);
			
		}
		
		return paymentCreditNote;
	}
	
	public RobPaymentCreditNote findByTransactionId(String transactionId){
		
		SearchCriteria sc = new SearchCriteria("cnTransactionNo", SearchCriteria.EQUAL, transactionId);
		List<RobPaymentCreditNote> creditNote = findByCriteria(sc).getList();
		
		if(creditNote.size() > 0){
			return creditNote.get(0);
		}
		
		return null;
	}
}
