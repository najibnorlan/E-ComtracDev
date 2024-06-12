package com.ssm.llp.base.page;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.ezbiz.otcPayment.ListOtcPaymentPage;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.page.ListPartnerRegistrationPanel;
import com.ssm.llp.mod1.page.SearchPartnerPage;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

import freemarker.template.SimpleDate;

public class ViewPaymentReceiptPanel extends SecBasePanel {
	

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "RobCounterSessionService")
	private RobCounterSessionService robCounterSessionService;
	
	public ViewPaymentReceiptPanel() {
		super();
	}

	public ViewPaymentReceiptPanel(String id) {
		super(id);
	}

	public ViewPaymentReceiptPanel(String id, PageParameters param) {
		super(id);

		String transId = param.get("transId").toString();
		String registrationFail = param.get("registrationFail") != null ? param.get("registrationFail").toString() : null;
		if (Parameter.YES_NO_yes.equals(registrationFail)) {
			ssmError(SSMExceptionParam.PAYMENT_SUCCESS_REG_FAIL);
		}
		
		final LlpPaymentTransaction llpPaymentTransaction = ((LlpPaymentTransactionService) getService(LlpPaymentTransactionService.class.getSimpleName()))
				.findById(transId);
		populateDetail(transId,llpPaymentTransaction);
		
		AjaxLink backToListPaymentOTC = new AjaxLink<Void>("backToListPaymentOTC") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
					RobCounterSession sessionObj = (RobCounterSession) SignInSession.get().getAttribute("_currentCheckinSession");
					RobCounterSession robCounterSession = robCounterSessionService.findByUserIdIpAddressOpen(sessionObj.getUserId(), sessionObj.getCounterIpAddress().getIpAddress());
				    setResponsePage(new ListOtcPaymentPage(robCounterSession.getSessionId()));
				
			}
		};
		
		backToListPaymentOTC.setVisible(false);
		backToListPaymentOTC.setOutputMarkupId(true);
		add(backToListPaymentOTC);
		
		if(llpPaymentTransaction.getPaymentMode() != null){
			if(llpPaymentTransaction.getPaymentMode().equalsIgnoreCase(Parameter.PAYMENT_MODE_CASH)){
				backToListPaymentOTC.setVisible(true);
			}
		}
		
	}

	public void populateDetail(String transactionId, LlpPaymentTransaction llpPaymentTransaction) {
		
//		System.out.println("transactionId : " + transactionId);
		if (llpPaymentTransaction != null && StringUtils.isNotEmpty(llpPaymentTransaction.getTransactionId())) {
			LlpPaymentReceipt llpPaymentReceipt = ((LlpPaymentReceiptService) getService(LlpPaymentReceiptService.class.getSimpleName()))
					.find(transactionId);

			if (llpPaymentReceipt.getReceiptNo() != null) {
				
				boolean disableGSTFormat = false;
				
				String dateDisableGst = getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_DATE_SST_RECEIPT_FORMAT);
				if(StringUtils.isNotBlank(dateDisableGst)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						if(llpPaymentReceipt.getCreateDt().after(sdf.parse(dateDisableGst))) {
							disableGSTFormat = true;
						}
					}catch(Exception ex) {
						
					}
				}
				
				
				String initial = "";
				
				if(llpPaymentReceipt.getCounterSessionId() != null){
					initial += llpPaymentReceipt.getCounterSessionId().getBranch();
					initial += "/" + llpPaymentReceipt.getCounterSessionId().getFloorLevel();
					initial += "/" + llpPaymentReceipt.getCounterSessionId().getCounterIpAddress().getCounterName();
					initial += "/" + llpPaymentReceipt.getCounterSessionId().getUserId();
				}
				
				SSMLabel initialNote = new SSMLabel("initialNote", initial);
				initialNote.setOutputMarkupPlaceholderTag(true);
				add(initialNote);
				
				SSMLabel taxInvoice = new SSMLabel("taxInvoiceNo", llpPaymentReceipt.getTaxInvoiceNo());
				taxInvoice.setOutputMarkupId(true);
				taxInvoice.setVisible(false);
				add(taxInvoice);
				add(new SSMLabel("receiptNo", llpPaymentReceipt.getReceiptNo()));
				add(new SSMLabel("cashReceived", llpPaymentReceipt.getCashReceived()));
				add(new SSMLabel("change", llpPaymentReceipt.getBalance()));
				add(new SSMLabel("transId", llpPaymentTransaction.getTransactionId()));
				add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
				add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
				add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));
				add(new SSMLabel("paymentMode", llpPaymentTransaction.getPaymentMode()));
				SSMLabel approvalCode = new SSMLabel("approvalCode", llpPaymentTransaction.getApprovalCode());
				SSMLabel ccNo = new SSMLabel("creditCardNo", llpPaymentTransaction.getPaymentDetail());
				if (StringUtils.isNotBlank(llpPaymentTransaction.getPaymentDetail())) {
					if(llpPaymentTransaction.getPaymentDetail().equalsIgnoreCase(Parameter.PAYMENT_DETAIL_OTC)){
						ccNo.setVisible(false);
						approvalCode.setVisible(false);
						initialNote.setVisible(true);
					}else{
						initialNote.setVisible(false);
						ccNo.setVisible(true);
						approvalCode.setVisible(true);
					}
				} else {
					ccNo.setVisible(false);
				}
				add(ccNo);
				add(approvalCode);
				add(new SSMLabel("requestDate", llpPaymentTransaction.getRequestDt(), "dd/MM/yyyy"));
				
				add(new SSMLabel("totalWithoutGst", llpPaymentTransaction.getAmount() - llpPaymentTransaction.getGstAmt()));
				add(new SSMLabel("totalGst", llpPaymentTransaction.getGstAmt()));
				add(new SSMLabel("amount", llpPaymentTransaction.getAmount()));

				add(new SSMLabel("totalAmount", llpPaymentTransaction.getAmount()));
				
				String labelReceipt = Parameter.PAYMENT_RECEIPT_IS_OFFICIAL_RECEIPT;
				
				
				if(llpPaymentReceipt.getTaxInvoiceNo() != null && !disableGSTFormat){
					labelReceipt = Parameter.PAYMENT_RECEIPT_IS_TAX_INVOICE;
					taxInvoice.setVisible(true);
				}
				
				
				
				add(new SSMLabel("labelReceipt", labelReceipt));
				
				List<LlpPaymentTransactionDetail> paymentItems = ((LlpPaymentTransactionDetailService) getService(LlpPaymentTransactionDetailService.class
						.getSimpleName())).find(llpPaymentTransaction.getTransactionId());
				add(new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
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
				});
				
				
			}
		}
	}
}
