package com.ssm.llp.base.page;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.axis2.databinding.types.soapencoding.DateTime;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.informix.lang.Decimal;
import com.ssm.ezbiz.otcPayment.ListOtcPaymentPage;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

@SuppressWarnings({"all"})
public class ViewPaymentReceiptSSTPanel extends SecBasePanel{
	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "RobCounterSessionService")
	private RobCounterSessionService robCounterSessionService;
//	
//	@SpringBean(name = "LlpPaymentFeeService")
//	LlpPaymentFeeService llpPaymentFeeService;
	
	@SpringBean(name = "RobTrainingTransactionService")
	RobTrainingTransactionService robTrainingTransactionService;
	
	public ViewPaymentReceiptSSTPanel() {
		super();
	}

	public ViewPaymentReceiptSSTPanel(String id) {
		super(id);
	}
	
	public ViewPaymentReceiptSSTPanel(String id, PageParameters param) {
		super(id);

		String transId = param.get("transId").toString();
		String registrationFail = param.get("registrationFail") != null ? param.get("registrationFail").toString() : null;
		if (Parameter.YES_NO_yes.equals(registrationFail)) {
			ssmError(SSMExceptionParam.PAYMENT_SUCCESS_REG_FAIL);
		}
		
		String contextPath = WicketApplication.get().getServletContext().getServletContextName();
		if(StringUtils.isNotBlank(contextPath)) {
			contextPath = "/"+contextPath;
		}
		String js = "function CallPrint(strid) {";
		js += "var prtContent = document.getElementById(strid);";
		js += "var WinPrint = window.open('', '','left=0,top=0,width=800,height=600,toolbar=0,scrollbars=0,status=0');";
		js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/semantic/semantic.min.css \"> <link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/css/styles.css \" /> <link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/semantic/components/form.min.css \">');";
		js += "WinPrint.document.write('<style>body{padding:0px 20px 0px 20px; margin: 0px}td{font-family:\"Tahoma\",\"LucidaSans\",\"Arial\",\"Helvetica\",\"Sans-serif\",\"sans\";font-size:9pt;padding:2px;}table#itemsTable{border:0.5px solid black;}table#itemsTable td{padding:4px;}table#gstSummary td{padding-right:50px;}</style>');";
		js += "WinPrint.document.write('<style>@font-face { font-family: \"BarcodeC39\"; src:url(" + contextPath + "/css/BarcodeC39.otf\") format(\"TrueType\");}</style>');";
		js += "WinPrint.document.write(prtContent.innerHTML);";
		js += "WinPrint.document.close();";
		js += "WinPrint.focus();";
		js += "setTimeout(initprint, 1000);";
		js += "function initprint(){WinPrint.print();WinPrint.close();}}";
		
		Label jsScript = new Label("jsScript", js);
		jsScript.setEscapeModelStrings(false);
		jsScript.setOutputMarkupId(true);
		
		add(jsScript);
		
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
				
				SSMLabel comtracTrainingName = new SSMLabel("comtracTrainingName", "");
				comtracTrainingName.setOutputMarkupId(true);
				add(comtracTrainingName);
				
				if(llpPaymentTransaction.getAppRefNo().startsWith("CP")){
					RobTrainingTransaction trainingTransaction = robTrainingTransactionService.findByTransactionCode(llpPaymentTransaction.getAppRefNo());
					comtracTrainingName.setDefaultModelObject(trainingTransaction.getTrainingId().getTrainingName());
					comtracTrainingName.setVisible(true);
				}else{
					comtracTrainingName.setVisible(false);
				}
				
				
				
				SSMLabel taxInvoice = new SSMLabel("taxInvoiceNo", llpPaymentReceipt.getTaxInvoiceNo());
				taxInvoice.setOutputMarkupId(true);
				taxInvoice.setVisible(false);
				add(taxInvoice);
				add(new SSMLabel("receiptNo", llpPaymentReceipt.getReceiptNo()));
				add(new SSMLabel("cashReceived", String.format("%.2f", llpPaymentReceipt.getCashReceived())));
				add(new SSMLabel("change", String.format("%.2f", llpPaymentReceipt.getBalance())));
				add(new SSMLabel("transId", llpPaymentTransaction.getTransactionId()));
				add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
				add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
				add(new SSMLabel("payerId", llpPaymentTransaction.getPayerId()));
				add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));
				add(new SSMLabel("paymentMode", llpPaymentTransaction.getPaymentMode()));
				add(new SSMLabel("receiptBarcode", "*" + llpPaymentReceipt.getReceiptNo() + "*"));
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
				add(new SSMLabel("requestDate", llpPaymentTransaction.getRequestDt(), "dd/MM/yyyy hh:mm:ss a"));
				
				
				add(new SSMLabel("totalWithoutGst", llpPaymentTransaction.getAmount() - llpPaymentTransaction.getGstAmt()));
				add(new SSMLabel("totalGst", llpPaymentTransaction.getGstAmt()));
//
				add(new SSMLabel("totalAmount", llpPaymentTransaction.getAmount()));
				
				String labelReceipt = Parameter.PAYMENT_RECEIPT_IS_OFFICIAL_RECEIPT;
				if(llpPaymentReceipt.getTaxInvoiceNo() != null){
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
						item.add(new SSMLabel("productType", "")); //paymentItem.getGstCode()
						item.add(new SSMLabel("index", item.getIndex()+1));
						item.add(new SSMLabel("itemAmountWoutGst", paymentItem.getAmountWithOutGst()));
						
						
					}
				});
				
				BigDecimal totalAmountSr = BigDecimal.ZERO;
				BigDecimal totalAmountOS = BigDecimal.ZERO;
				BigDecimal totalTaxSr = BigDecimal.ZERO;
				BigDecimal totalTaxOS = BigDecimal.ZERO;
				
				for(LlpPaymentTransactionDetail i : paymentItems){
					
					if(Parameter.PAYMENT_GST_CODE_SR.equalsIgnoreCase(i.getGstCode())){
						totalAmountSr = totalAmountSr.add(new BigDecimal(i.getAmountWithOutGst()));
						totalTaxSr = totalTaxSr.add(new BigDecimal(i.getGstAmt()));
					}else{
						totalAmountOS = totalAmountOS.add(new BigDecimal(i.getAmountWithOutGst()));
						totalTaxOS = totalTaxOS.add(new BigDecimal(i.getGstAmt()));
					}
				}
				
				DecimalFormat df = new DecimalFormat("#,###,##0.00");
				add(new SSMLabel("totalAmountSr", df.format(totalAmountSr)));
				add(new SSMLabel("totalAmountOS", df.format(totalAmountOS)));
				add(new SSMLabel("totalTaxSr", df.format(totalTaxSr)));
				add(new SSMLabel("totalTaxOS", df.format(totalTaxOS)));
			}
		}
	}
}
