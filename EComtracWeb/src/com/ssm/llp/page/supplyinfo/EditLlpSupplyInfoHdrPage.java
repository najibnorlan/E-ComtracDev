package com.ssm.llp.page.supplyinfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class EditLlpSupplyInfoHdrPage extends SecBasePage{
	
	@SpringBean(name="LlpSupplyInfoHdrService")
	private LlpSupplyInfoHdrService llpSupplyInfoHdrService;
	
	@SpringBean(name="LlpSupplyInfoDtlService")
	private LlpSupplyInfoDtlService llpSupplyInfoDtlService;
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	public EditLlpSupplyInfoHdrPage(final String hdrCode) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpSupplyInfoHdrService.findById(hdrCode);
			}
		}));
		
		add(new LlpSupplyInfoHdrForm("form", getDefaultModel()));
		
	}
	
	
	private class LlpSupplyInfoHdrForm extends Form {
		double totalProfileAmt = 0.0;
		double totalCertAmt = 0.0;
		double totalAmt = 0.0;
		
		public LlpSupplyInfoHdrForm(String id, IModel m) {
			super(id, m);
			final LlpSupplyInfoHdr llpSupplyInfoHdr = (LlpSupplyInfoHdr) getDefaultModelObject();
			final List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = llpSupplyInfoDtlService.findByHdrCode(llpSupplyInfoHdr.getHdrCode());
			add(new LlpSupplyInfoDetailPanel("LlpSupplyInfoDetailPanel", llpSupplyInfoHdr.getHdrCode()));
			
			SSMLabel hdrCode = new SSMLabel("hdrCode", llpSupplyInfoHdr.getHdrCode());
			add(hdrCode);

			SSMLabel hdrStatus = new SSMLabel("hdrStatus", llpSupplyInfoHdr.getHdrStatus(), Parameter.SUPPLY_INFO_HDR_STATUS);
			add(hdrStatus);
			
			SSMLabel totalProfileAmtLbl = new SSMLabel("totalProfileAmt", llpSupplyInfoHdr.getTotalProfileAmt());
			SSMLabel totalCertAmtLbl = new SSMLabel("totalCertAmt", llpSupplyInfoHdr.getTotalCertAmt());
			SSMLabel totalAmtLbl = new SSMLabel("totalAmt", llpSupplyInfoHdr.getTotalAmt());
			add(totalProfileAmtLbl);
			add(totalCertAmtLbl);
			add(totalAmtLbl);
			
			if(Parameter.SUPPLY_INFO_HDR_STATUS_pending_payment.equals(llpSupplyInfoHdr.getHdrStatus())){
				double profileAmt = llpPaymentFeeService.findById(Parameter.PAYMENT_LLP_PROFILE).getPaymentFee();
				double certAmt = llpPaymentFeeService.findById(Parameter.PAYMENT_LLP_CERT).getPaymentFee();
				totalProfileAmt = 0.0;
				totalCertAmt = 0.0;
				totalAmt = 0.0;
				
				for (int i = 0;listLlpSupplyInfoDtl!=null &&  i < listLlpSupplyInfoDtl.size(); i++) {
					LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
					if (dtl.getIsProfileSelected()) {
						totalProfileAmt += profileAmt;
					}
					if (dtl.getIsCertSelected()) {
						totalCertAmt += certAmt;
					}
				}
				DecimalFormat df = new DecimalFormat("#0.00");
				totalAmt = totalProfileAmt + totalCertAmt;
				totalProfileAmtLbl.setDefaultModelObject(df.format(totalProfileAmt));
				totalCertAmtLbl.setDefaultModelObject(df.format(totalCertAmt));
				totalAmtLbl.setDefaultModelObject(df.format(totalAmt));
				
			}
			
			
			
			SSMLabel expiredDt = new SSMLabel("expiredDt", "");
			expiredDt.setVisible(false);
			add(expiredDt);
			if(Parameter.SUPPLY_INFO_HDR_STATUS_complete.equals(llpSupplyInfoHdr.getHdrStatus())){
				SimpleDateFormat defaultDf= new SimpleDateFormat("dd/MM/yyyy");
				expiredDt.setDefaultModelObject(defaultDf.format(llpSupplyInfoHdr.getExpiredDt()));
				expiredDt.setVisible(true);
			}
			
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListLlpSupplyInfoHdrPage.class);
				}
			}.setDefaultFormProcessing(false));
			
			
			AjaxButton repayment = new AjaxButton("repayment") {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if (listLlpSupplyInfoDtl.size() > 0) {

						List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
						List<LlpPaymentTransactionDetail> listPaymentItemsCertOnly = new ArrayList<LlpPaymentTransactionDetail>();

						for (int i = 0; i < listLlpSupplyInfoDtl.size(); i++) {
							LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
							if (dtl.getIsProfileSelected()) {
								LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
								paymentItem.setPaymentItem(Parameter.PAYMENT_LLP_PROFILE);
								paymentItem.setQuantity(1);
								paymentItem.setPaymentDet(dtl.getEntityNo() + "\t" + dtl.getEntityName());
								listPaymentItems.add(paymentItem);
							}
							if (dtl.getIsCertSelected()) {
								LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
								paymentItem.setPaymentItem(Parameter.PAYMENT_LLP_CERT);
								paymentItem.setQuantity(1);
								paymentItem.setPaymentDet(dtl.getEntityNo() + "\t" + dtl.getEntityName());
								listPaymentItemsCertOnly.add(paymentItem);
							}
						}

						if (listPaymentItemsCertOnly.size() > 0) {
							listPaymentItems.addAll(listPaymentItemsCertOnly);
						}
						
						llpSupplyInfoHdr.setTotalProfileAmt(totalProfileAmt);
						llpSupplyInfoHdr.setTotalCertAmt(totalCertAmt);
						llpSupplyInfoHdr.setTotalAmt(totalAmt);
						llpSupplyInfoHdrService.update(llpSupplyInfoHdr);
						
						setResponsePage(new PaymentDetailPage(llpSupplyInfoHdr.getHdrCode(), LlpSupplyInfoHdrService.class.getSimpleName(), llpSupplyInfoHdr, listPaymentItems));
					}

				}
			};
			add(repayment);
			repayment.setVisible(false);
			if(Parameter.SUPPLY_INFO_HDR_STATUS_pending_payment.equals(llpSupplyInfoHdr.getHdrStatus())){
				repayment.setVisible(true);
			}
		}
		
	}
	
	

}
