package com.ssm.ezbiz.otcPayment;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.PaymentReceiptPage;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;

@SuppressWarnings({"all"})
public class ViewCreditNoteSlipPanel extends SecBasePanel{
	
	@SpringBean(name = "RobPaymentCreditNoteService")
	RobPaymentCreditNoteService robPaymentCreditNoteService;
	
	public ViewCreditNoteSlipPanel(String id, RobPaymentCreditNote paymentCreditNote){
		super(id);
		RobPaymentCreditNote creditNote = (RobPaymentCreditNote) getService(RobPaymentCreditNoteService.class.getSimpleName())
				.findById(paymentCreditNote.getCreditNoteNo());
		
		populateDetail(creditNote);
	}
	
	public ViewCreditNoteSlipPanel(String id, String transactionId){
		super(id);
		RobPaymentCreditNote creditNote = robPaymentCreditNoteService.findByTransactionId(transactionId);
		
		populateDetail(creditNote);
	}
	
	public void populateDetail(RobPaymentCreditNote paymentCreditNote) {
		
		final LlpPaymentReceipt paymentReceipt = (LlpPaymentReceipt) getService(LlpPaymentReceiptService.class.getSimpleName())
				.findById(paymentCreditNote.getPaymentReceiptNo().getReceiptNo());
		
		final LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) getService(LlpPaymentTransactionService.class.getSimpleName())
				.findById(paymentReceipt.getTransactionId());
		
		SSMLabel taxInvoice = new SSMLabel("taxInvoiceNo", paymentReceipt.getTaxInvoiceNo());
		taxInvoice.setOutputMarkupId(true);
		taxInvoice.setVisible(false);
		add(taxInvoice);
		if(llpPaymentTransaction.getGstAmt() > 0){
			taxInvoice.setVisible(true);
		}
		
		String initial = "";
		initial += paymentCreditNote.getCounterSessionId().getBranch();
		initial += "/" + paymentCreditNote.getCounterSessionId().getFloorLevel();
		initial += "/" + paymentCreditNote.getCounterSessionId().getCounterIpAddress().getCounterName();
		initial += "/" + paymentCreditNote.getCounterSessionId().getUserId();
		
		SSMLabel initialNote = new SSMLabel("initialNote", initial);
		initialNote.setOutputMarkupPlaceholderTag(true);
		add(initialNote);
		
		add(new SSMLabel("receiptNo", paymentReceipt.getReceiptNo()));
		add(new SSMLabel("creditNoteNo", paymentCreditNote.getCreditNoteNo()));
		add(new SSMLabel("transId", paymentCreditNote.getCnTransactionNo()));
		add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
		add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
		add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));
		add(new SSMLabel("totalWithoutGst", llpPaymentTransaction.getAmount() - llpPaymentTransaction.getGstAmt()));
		add(new SSMLabel("totalGst", llpPaymentTransaction.getGstAmt()));
		add(new SSMLabel("amount", llpPaymentTransaction.getAmount()));
		add(new SSMLabel("paymentMode", llpPaymentTransaction.getPaymentMode()));
		add(new SSMLabel("paymentDate", paymentReceipt.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
		add(new SSMLabel("totalAmount", llpPaymentTransaction.getAmount()));
		
		List<LlpPaymentTransactionDetail> paymentItems = ((LlpPaymentTransactionDetailService) getService(LlpPaymentTransactionDetailService.class
				.getSimpleName())).find(llpPaymentTransaction.getTransactionId());
		
		ListView view =  new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
			public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
				final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
				item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));
				SSMLabel itemDesc = new SSMLabel("itemDet", paymentItem.getPaymentDet());
				if(StringUtils.isBlank(paymentItem.getPaymentDet())){
					itemDesc.setVisible(false);
				}
				item.add(itemDesc);
				item.add(new SSMLabel("itemAmountWoutGst", paymentItem.getAmountWithOutGst()));
				item.add(new SSMLabel("itemGstAmt", paymentItem.getGstAmt()));
				item.add(new SSMLabel("itemAmount", paymentItem.getAmount())); 
			}
		};
		add(view);
		
		AjaxLink done = new AjaxLink<Void>("done") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				    setResponsePage(new ListOtcReceipt(paymentReceipt.getCounterSessionId().getSessionId()));
				
			}
		};
		
		done.setOutputMarkupId(true);
		add(done);
		
		AjaxLink goToReceipt = new AjaxLink<Void>("goToReceipt") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				PageParameters paramToSend = new PageParameters();
				paramToSend.set("transId", llpPaymentTransaction.getTransactionId());
				paramToSend.set("registrationFail", Parameter.YES_NO_no);
				
				setResponsePage(PaymentReceiptPage.class, paramToSend);
				
			}
		};
		
		goToReceipt.setOutputMarkupId(true);
		add(goToReceipt);
	}
}
