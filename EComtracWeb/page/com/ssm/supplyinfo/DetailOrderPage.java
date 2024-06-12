package com.ssm.supplyinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;



@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class DetailOrderPage extends SecBasePage{
	

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;
	
	public DetailOrderPage(String transCode) {
		final SupplyInfoTransHdr hdr =  supplyInfoTransHdrService.findAllById(transCode);
		
		List<SupplyInfoTransDtl> listSupplyInfoTransDtl = hdr.getListSupplyInfoTransDtl();
		
		add(new SSMLabel("totalLabel", hdr.getTotalAmt()));
		add(new SSMLabel("taxLabel", hdr.getTaxAmt()));
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("idNo", listSupplyInfoTransDtl);
		final SSMDataView<SupplyInfoTransDtl> dataView = new SSMDataView<SupplyInfoTransDtl>("sorting", dp) {

			protected void populateItem(final Item<SupplyInfoTransDtl> item) {
				final SupplyInfoTransDtl supplyInfo = item.getModelObject();
				item.add(new SSMLabel("bil", item.getIndex()+1));
				item.add(new SSMLabel("entityNo", supplyInfo.getEntityNo()));
				item.add(new SSMLabel("prodDesc", supplyInfo.getProdCode() , Parameter.SUPPLY_INFO_PROD_TYPE_PERSONAL));
				item.add(new SSMLabel("prodLocale", supplyInfo.getProdLocale() , Parameter.SUPPLY_INFO_PROD_LOCALE));
				item.add(new SSMLabel("amt", supplyInfo.getAmt()));

				SSMAjaxLink download = new SSMAjaxLink("download") {
					@Override
					public void onClick(AjaxRequestTarget target) {
					}
				};
				item.add(download);
			}
		};
		add(dataView);
		dataView.setItemsPerPage(100L);
		
		
		SSMAjaxLink proceedPayment = new SSMAjaxLink("proceedPayment") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				List<SupplyInfoTransDtl> list = hdr.getListSupplyInfoTransDtl();
				
				
				List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
				
				
				for (int i = 0; i < list.size(); i++) {
					SupplyInfoTransDtl dtl = list.get(i);
					LlpPaymentFee llpPaymentFee = llpPaymentFeeService.findById(dtl.getProdCode());
					
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					paymentItem.setPaymentItem(llpPaymentFee.getPaymentCode());
					paymentItem.setQuantity(1);
					paymentItem.setPaymentDet(dtl.getEntityNo());
					paymentItem.setAmount(llpPaymentFee.getPaymentFee());
					paymentItem.setGstCode(llpPaymentFee.getGstCode());
					if(Parameter.PAYMENT_GST_CODE_SR.equals(llpPaymentFee.getGstCode())){
						paymentItem.setGstAmt(llpPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
					}
					
					listPaymentItems.add(paymentItem);
				}
				setResponsePage(new PaymentDetailPage(hdr.getTransCode(), SupplyInfoTransHdrService.class.getSimpleName(), hdr, listPaymentItems));
				
			}
		};
		proceedPayment.setOutputMarkupId(true);
		proceedPayment.setOutputMarkupPlaceholderTag(true);
		proceedPayment.setVisible(false);
		add(proceedPayment);
		
		
		if(Parameter.SUPPLY_INFO_TRANS_STATUS_PENDING_PAYMENT.equals(hdr.getStatus()) || Parameter.SUPPLY_INFO_TRANS_STATUS_DATA_ENTRY.equals(hdr.getStatus()) || Parameter.SUPPLY_INFO_TRANS_STATUS_OTC.equals(hdr.getStatus())) {
			proceedPayment.setVisible(true);
		}
		
		if(Parameter.SUPPLY_INFO_TRANS_STATUS_SUCCESS.equals(hdr.getStatus())) {
			
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findSuccessByAppRefNo(hdr.getTransCode());
			PageParameters param = new PageParameters();
			param.add("transId", paymentTransaction.getTransactionId());
			Panel recieptPanel = getRecieptPanel("recieptPanel",param );
			add(recieptPanel);
		}else {

			SSMLabel dummyResitPanel = new SSMLabel("recieptPanel","");
			dummyResitPanel.setVisible(false);
			add(dummyResitPanel);
		}

	}
	
	@Override
	public String getPageTitle() {
		return "com.ssm.supplyInfo.detailOrderPage";
	}
}
