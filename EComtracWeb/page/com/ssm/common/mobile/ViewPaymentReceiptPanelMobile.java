package com.ssm.common.mobile;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

public class ViewPaymentReceiptPanelMobile extends SecBasePanelMobile {
	

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	public ViewPaymentReceiptPanelMobile() {
		super();
	}

	public ViewPaymentReceiptPanelMobile(String id) {
		super(id);
	}

	public ViewPaymentReceiptPanelMobile(String id, PageParameters param) {
		super(id);

		String transId = param.get("transId").toString();
		String registrationFail = param.get("registrationFail") != null ? param.get("registrationFail").toString() : null;
		if (Parameter.YES_NO_yes.equals(registrationFail)) {
			ssmError(SSMExceptionParam.PAYMENT_SUCCESS_REG_FAIL);
		}
		
		final LlpPaymentTransaction llpPaymentTransaction = ((LlpPaymentTransactionService) getService(LlpPaymentTransactionService.class.getSimpleName()))
				.findById(transId);
		populateDetail(transId,llpPaymentTransaction);
		
		
		AjaxLink goToTransactionPage = new AjaxLink<Void>("goToTransactionPage") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
					RobRenewal robRenewal = robRenewalService.findById(llpPaymentTransaction.getAppRefNo());
				    setResponsePage(new EditRobRenewalPage(robRenewal));
				}
				
			}
		};
		goToTransactionPage.setVisible(false);
		goToTransactionPage.setOutputMarkupId(true);
		add(goToTransactionPage);
		
		if(llpPaymentTransaction!=null && StringUtils.isNotBlank(llpPaymentTransaction.getAppRefNo()) && llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
			goToTransactionPage.setVisible(true);
		}
	}

	public void populateDetail(String transactionId, LlpPaymentTransaction llpPaymentTransaction) {
		

		if (llpPaymentTransaction != null && StringUtils.isNotEmpty(llpPaymentTransaction.getTransactionId())) {
			LlpPaymentReceipt llpPaymentReceipt = ((LlpPaymentReceiptService) getService(LlpPaymentReceiptService.class.getSimpleName()))
					.find(transactionId);

			if (llpPaymentReceipt != null) {
				add(new SSMLabel("receiptNo", llpPaymentReceipt.getReceiptNo()));
				add(new SSMLabel("transId", llpPaymentTransaction.getTransactionId()));
				add(new SSMLabel("approvalCode", llpPaymentTransaction.getApprovalCode()));
				add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
				add(new SSMLabel("paymentMode", llpPaymentTransaction.getPaymentMode()));

				SSMLabel ccNo = new SSMLabel("creditCardNo", llpPaymentTransaction.getPaymentDetail());
				if (StringUtils.isNotBlank(llpPaymentTransaction.getPaymentDetail())) {
					ccNo.setVisible(true);
				} else {
					ccNo.setVisible(false);
				}
				add(ccNo);
				add(new SSMLabel("requestDate", llpPaymentTransaction.getRequestDt(), "dd/MM/yyyy"));
				add(new SSMLabel("totalAmount", llpPaymentTransaction.getAmount()));

				List<LlpPaymentTransactionDetail> paymentItems = ((LlpPaymentTransactionDetailService) getService(LlpPaymentTransactionDetailService.class
						.getSimpleName())).find(llpPaymentTransaction.getTransactionId());
				add(new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
					public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
						final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
						item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));
						item.add(new SSMLabel("itemAmount", paymentItem.getAmount()));
						item.add(new SSMLabel("itemDet", paymentItem.getPaymentDet()));
					}
				});
				
				
			}
		}
	}
}
