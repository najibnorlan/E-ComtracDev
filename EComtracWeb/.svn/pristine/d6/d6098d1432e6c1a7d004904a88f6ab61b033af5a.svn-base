package com.ssm.supplyinfo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;



@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class PreviewCartPage extends BasePage{
	

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	public PreviewCartPage() {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				PreviewCartPageModel previewCartPageModel = new PreviewCartPageModel();
				return previewCartPageModel;
			}
		}));
		add(new PreviewCartPageForm("form", (IModel<PreviewCartPageModel>) getDefaultModel()));
	}

	private class PreviewCartPageForm extends Form implements Serializable {

		final SSMLabel totalLabel,taxLabel;
		final SSMSessionSortableDataProvider dp;
		final SSMAjaxButton checkoutButton;
		final WebMarkupContainer wmcSummaryPrice;
		public PreviewCartPageForm(String id, IModel<PreviewCartPageModel> model) {
			super(id, model);
			final PreviewCartPageModel searchModel = model.getObject();
			

			List<SupplyInfoTransDtl> listSupplyInfoTransDtl = getCartHdr().getListSupplyInfoTransDtl();
			

			wmcSummaryPrice = new WebMarkupContainer("wmcSummaryPrice");
			wmcSummaryPrice.setOutputMarkupId(true);
			wmcSummaryPrice.setOutputMarkupPlaceholderTag(true);
			add(wmcSummaryPrice);
			
			totalLabel = new SSMLabel("totalLabel", getCartHdr().getTotalAmt());
			taxLabel = new SSMLabel("taxLabel", getCartHdr().getTotalAmt());
			
			wmcSummaryPrice.add(totalLabel);
			wmcSummaryPrice.add(taxLabel);
			
			resetWmcSummaryPrice(null);
			
			dp = new SSMSessionSortableDataProvider("idNo", listSupplyInfoTransDtl);
			final SSMDataView<SupplyInfoTransDtl> dataView = new SSMDataView<SupplyInfoTransDtl>("sorting", dp) {

				protected void populateItem(final Item<SupplyInfoTransDtl> item) {
					final SupplyInfoTransDtl supplyInfo = item.getModelObject();
					item.add(new SSMLabel("bil", item.getIndex()+1));
					item.add(new SSMLabel("entityNo", supplyInfo.getEntityNo()));
					item.add(new SSMLabel("entityName", supplyInfo.getEntityName()));
					item.add(new SSMLabel("prodDesc", supplyInfo.getProdCode() , Parameter.SUPPLY_INFO_PROD_TYPE_PERSONAL));
					item.add(new SSMLabel("amt", supplyInfo.getAmt()));
					
					SSMDropDownChoice prodLocale = new SSMDropDownChoice("prodLocale", new PropertyModel(supplyInfo, "prodLocale"), Parameter.SUPPLY_INFO_PROD_LOCALE);
					prodLocale.setOutputMarkupId(true);
					add(prodLocale);
					
					prodLocale.setDefaultModelObject(supplyInfo.getProdLocale());
					item.add(prodLocale );
					
					OnChangeAjaxBehavior prodLocaleOnchange = new OnChangeAjaxBehavior() {
						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							String selectionLocale = ((SSMDropDownChoice)getComponent()).getValue();
							
							Set<String> hashCartCode = new HashSet<String>();
							List<SupplyInfoTransDtl> listDtl = getCartHdr().getListSupplyInfoTransDtl();
							
							for (int i = 0; i < listDtl.size(); i++) {
								SupplyInfoTransDtl dtlTmp = listDtl.get(i);
								if(dtlTmp.getTransDtlId()!=supplyInfo.getTransDtlId()) {
									hashCartCode.add(dtlTmp.getEntityType()+dtlTmp.getEntityNo()+dtlTmp.getProdCode()+dtlTmp.getProdLocale());
								}
							}
							
							if(hashCartCode.contains(supplyInfo.getEntityType()+supplyInfo.getEntityNo()+supplyInfo.getProdCode()+selectionLocale) ){
								if(Parameter.SUPPLY_INFO_PROD_LOCALE_BM.equals(selectionLocale)) {
									((SSMDropDownChoice)getComponent()).setDefaultModelObject(Parameter.SUPPLY_INFO_PROD_LOCALE_ENG);
								}else {
									((SSMDropDownChoice)getComponent()).setDefaultModelObject(Parameter.SUPPLY_INFO_PROD_LOCALE_BM);
								}
								String errorScript = WicketUtils.generateAjaxErrorAlertScript("Error", "Product Already Exist");
				        		target.prependJavaScript(errorScript);
							}
							
							
							target.add(getComponent());
							
						}
					};
					prodLocale.add(prodLocaleOnchange);
					
					SSMAjaxLink removeAction = new SSMAjaxLink("remove") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							removeFromCart(supplyInfo , target);
							refreshCartList(target);
						}
					};
					item.add(removeAction);
				}
			};
			add(dataView);
			dataView.setItemsPerPage(100L);
//			add(new SSMPagingNavigator("navigator", dataView));
//			add(new NavigatorLabel("navigatorLabel", dataView));
			
			
			
			WebMarkupContainer wmcProceedToPay = new WebMarkupContainer("wmcProceedToPay");
			wmcProceedToPay.setOutputMarkupId(true);
			wmcProceedToPay.setOutputMarkupPlaceholderTag(true);
			wmcProceedToPay.setVisible(false);
			add(wmcProceedToPay);
			
			
			
			checkoutButton = new SSMAjaxButton("checkoutButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					recalculateAndupdateHeaderAmt();
					SupplyInfoTransHdr hdr = getCartHdr();
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
					
//					getSession().setAttribute("cartInfoHdr_",(Serializable) hdr);
					setResponsePage(new PaymentDetailPage(hdr.getTransCode(), SupplyInfoTransHdrService.class.getSimpleName(), hdr, listPaymentItems));
					
				}
			};
			checkoutButton.setOutputMarkupId(true);
			checkoutButton.setOutputMarkupPlaceholderTag(true);
			checkoutButton.setEnabled(false);
			wmcProceedToPay.add(checkoutButton);
			
			
			final SSMAjaxCheckBox declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel("", "") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						checkoutButton.setEnabled(true);
					} else {
						checkoutButton.setEnabled(false);
					}
					target.add(checkoutButton);
				}
			};
			declarationChkBox.setOutputMarkupId(true);
			wmcProceedToPay.add(declarationChkBox);
			
			final SSMAjaxButton emptyCart = new SSMAjaxButton("emptyCart") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					removeAllFromCart(target);
					refreshCartList(target);
					
					target.add(checkoutButton);
				}
			};
			wmcProceedToPay.add(emptyCart);
			
			
			SSMLabel ifNotLogIn = new SSMLabel("ifNotLogIn",resolve("com.ssm.supplyInfo.previewCartPage.ifNotLogin"));
			ifNotLogIn.setVisible(false);
			add(ifNotLogIn);
			

			
			SSMLabel ifNotActivate = new SSMLabel("ifNotActivate",resolve("com.ssm.supplyInfo.previewCartPage.ifNotActivate"));
			ifNotActivate.setVisible(false);
			add(ifNotActivate);
			
			
			if(UserEnvironmentHelper.getUserenvironment() == null ) {
				ifNotLogIn.setVisible(true);
			}else {
				if(Parameter.USER_STATUS_active.equals(((LlpUserEnviroment ) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile().getUserStatus())) {
					wmcProceedToPay.setVisible(true);
				}else {
					ifNotActivate.setVisible(true);
				}
			}

			if(listSupplyInfoTransDtl.size()==0) {
				checkoutButton.setVisible(false);
				declarationChkBox.setVisible(false);
			}
			
		}
		private void refreshCartList(AjaxRequestTarget target) {
			resetWmcSummaryPrice(target);
			
			List list = getCartHdr().getListSupplyInfoTransDtl();
			dp.resetView(list);
			
			if(list.size()==0) {
				checkoutButton.setVisible(false);
			}
			
			target.add(getRootForm());
			
		}
		
		
		private void resetWmcSummaryPrice(AjaxRequestTarget target) {
			SupplyInfoTransHdr hdr = getCartHdr();
			DecimalFormat df = new DecimalFormat("#0.00");
			
			totalLabel.setDefaultModelObject(df.format(hdr.getTotalAmt()));
			taxLabel.setDefaultModelObject(df.format(hdr.getTaxAmt()));
			if(target!=null) {
				target.add(wmcSummaryPrice);
			}
		}
	}
	
	private class PreviewCartPageModel implements Serializable {
		String prodLocale;

		public String getProdLocale() {
			return prodLocale;
		}

		public void setProdLocale(String prodLocale) {
			this.prodLocale = prodLocale;
		}
	}
	
	@Override
	public boolean displayAnimatedBackground() {
		return false;
	}
	
	@Override
	public boolean displayFloatingCart() {
		return false;
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "com.ssm.supplyInfo.previewCard";
	}
}
