package com.ssm.ezbiz.otcPayment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.MultiLineLabel;

import com.ssm.ezbiz.robFormB.TabRobFormBPage;
import com.ssm.ezbiz.robFormC.ListRobFormCTransactionPage;
import com.ssm.ezbiz.robformA.TabRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings({ "all" })
public class ViewOtcSlip extends SecBasePage{

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	public ViewOtcSlip() {

		String transId = SignInSession.get().getAttribute("_transactionId").toString();
		
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
		
		AjaxLink goToRespectiveIndex = new AjaxLink<Void>("goToRespectiveIndex") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				if(llpPaymentTransaction.getAppRefNo().startsWith("EB-A")){
				    setResponsePage(TabRobFormAPage.class);
				}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B")){
					setResponsePage(TabRobFormBPage.class);
				}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C")){
					setResponsePage(ListRobFormCTransactionPage.class);
				}
				
			}
		};
		goToRespectiveIndex.setVisible(false);
		goToRespectiveIndex.setOutputMarkupId(true);
		add(goToRespectiveIndex);
		
		if(llpPaymentTransaction!=null && StringUtils.isNotBlank(llpPaymentTransaction.getAppRefNo()) && !llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
			SignInSession signInSession = (SignInSession)getSession();
			if(null != getSession() && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
				goToRespectiveIndex.setVisible(false);
			}else {
				goToRespectiveIndex.setVisible(true);
			}	
		}else{
			goToRespectiveIndex.setVisible(false);
		}
		
		
	}
	
	public void populateDetail(String transactionId, LlpPaymentTransaction llpPaymentTransaction) {
		

		if (llpPaymentTransaction != null && StringUtils.isNotEmpty(llpPaymentTransaction.getTransactionId())) {
			
			
				add(new SSMLabel("transId", llpPaymentTransaction.getTransactionId()));
				add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
				add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
				add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));
				add(new SSMLabel("totalWithoutGst", llpPaymentTransaction.getAmount() - llpPaymentTransaction.getGstAmt()));
				add(new SSMLabel("totalGst", llpPaymentTransaction.getGstAmt()));
				add(new SSMLabel("amount", llpPaymentTransaction.getAmount()));
				add(new SSMLabel("barcodeText", "*"+llpPaymentTransaction.getTransactionId()+"*"));//need to put * for scanner recognition
				
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				String payBefore = resolve("otcpayment.page.otcslip.payBefore", sdf.format(new Date()));
				
				add(new SSMLabel("payBefore", payBefore));
		}
	}
}
