package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

/**
 * <p>
 * User: Nick Heudecker
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LlpBusinessCodePanel extends BasePanel {


	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	
	// public LlpBusinessCodePanel(String id,String llpNo) {
	public LlpBusinessCodePanel(String panelId) {
		super(panelId);
		final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");

		List<LlpBusinessCodeLink> listResult = new ArrayList<LlpBusinessCodeLink>(llpRegistration.getLlpBusinessCodeLink());
		List<LlpBusinessCodeLink> listResultNew = new ArrayList<LlpBusinessCodeLink>();
		for (int i = 0; i < listResult.size(); i++) {
			LlpBusinessCodeLink businessCodeLink = listResult.get(i);
			if (Parameter.BUSINESS_CODE_STATUS_active.equals(businessCodeLink.getStatus())) {
				listResultNew.add(businessCodeLink);
			}
		}
		
		//Save Transaction To DB
		if(StringUtils.isBlank(llpRegistration.getLlpNo())){
			LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
			llpRegTransaction.setLlpRegistration(llpRegistration);
			llpRegTransactionService.update(llpRegTransaction);
			getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
		}

		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider(listResultNew);

		final SSMDataView<LlpBusinessCodeLink> dataView = new SSMDataView<LlpBusinessCodeLink>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpBusinessCodeLink> item) {
				LlpBusinessCodeLink codeLink = item.getModelObject();
				// item.add(new SSMLabel("idBusinessCodeLink",
				// codeLink.getIdBusinessCodeLink()));
				item.add(new SSMLabel("businessCode", codeLink.getBusinessCode()));
				item.add(new SSMLabel("businessCodeDesc", codeLink.getRocBusinessObjectCode().getVchbusinessdesceng()));
				item.add(new SSMLabel("status", codeLink.getStatus(),Parameter.BUSINESS_CODE_STATUS));
				item.add(new SSMLabel("createDt", codeLink.getCreateDt()));

				
				SSMLink deleteLink = new SSMLink("delete", item.getDefaultModel(),true) {
					public void onClick() {
						final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
						List<LlpBusinessCodeLink> listBusinessCodeLink = llpRegistration.getLlpBusinessCodeLink();

						LlpBusinessCodeLink codeLink = (LlpBusinessCodeLink) getModelObject();
						if (codeLink.getIdBusinessCodeLink() == 0) {// Mean
																	// newly add
							for (int i = 0; i < listBusinessCodeLink.size(); i++) {
								LlpBusinessCodeLink codeLinkTmp = listBusinessCodeLink.get(i);
								if (codeLink.getBusinessCode().equals(codeLinkTmp.getBusinessCode())) {
									listBusinessCodeLink.remove(i);
									break;
								}
							}
						} else {// Mean in DB LIST
							for (int i = 0; i < listBusinessCodeLink.size(); i++) {
								LlpBusinessCodeLink codeLinkTmp = listBusinessCodeLink.get(i);
								if (codeLink.getBusinessCode().equals(codeLinkTmp.getBusinessCode())) {
									codeLinkTmp.setStatus(Parameter.BUSINESS_CODE_STATUS_inactive);
								}
							}
						}
						// codeLink.setStatus(Parameter.BUSINESS_CODE_STATUS_inactive);
						// getService(LlpBusinessCodeLinkService.class.getSimpleName()).update(codeLink);
						setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.BUSINESS_LINK_PANEL));
					}
				};
				item.add(deleteLink);
				
				String deleteLinkLabel = "[Delete]";
				if(!UserEnvironmentHelper.isInternalUser()){ //if public = VIEW
					if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
						deleteLinkLabel = "";
					}
				}
				deleteLink.add(new SSMLabel("deleteLabel",deleteLinkLabel));
				

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		dataView.setItemsPerPage(10L);

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));

		/*
		 * First modal window
		 */

		final ModalWindow searchBusinessCodePopUp = new ModalWindow("searchBusinessCodePopUp");
		add(searchBusinessCodePopUp);

		searchBusinessCodePopUp.setCookieName("modal-4");
		searchBusinessCodePopUp.setResizable(true);

		searchBusinessCodePopUp.setPageCreator(new ModalWindow.PageCreator() {
			@Override
			public Page createPage() {
				return new SearchBusinessCodePage(LlpBusinessCodePanel.this.getPage(), searchBusinessCodePopUp, "");
			}
		});
		searchBusinessCodePopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			@Override
			public void onClose(AjaxRequestTarget target) {
				setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.BUSINESS_LINK_PANEL));
			}
		});

		searchBusinessCodePopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		// add(new AjaxButton("searchBusinessCodeBtn")
		// {
		// @Override
		// protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		// searchBusinessCodePopUp.show(target);
		// }
		//
		// });

		
		AjaxLink searchBusinessCodeBtn = new AjaxLink<Void>("searchBusinessCodeBtn") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				searchBusinessCodePopUp.show(target);
			}
		};
		add(searchBusinessCodeBtn);
		
		
		
		SSMLink updateInfo = new SSMLink("updateInfo") { //button for internal user (ssm officer) only
			@Override
			public void onClick() {
				LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
				LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
				
				try {
					llpRegistrationService.saveInfo(llpRegistration,"");
					setResponsePage(ListLlpRegistration.class);
				} catch (SSMException e) {
					ssmError(e);
				}
				
			}
		};
		add(updateInfo);
		
		
		
		
		final SSMLink lodgeLink = new SSMLink("lodgeDoc") { //button for public user only
			@Override
			public void onClick() {
				LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
				LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
				
				try {
					
					
					llpRegistrationService.validateRecord(llpRegistration);
					
					//Save Transaction To DB
//					boolean alreadyPaid=false;
					LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
//					if(llpRegTransaction!=null){
//						if(Parameter.REG_TRANSACTION_STATUS_data_entry.equals(llpRegTransaction.getStatus())){
//							llpRegTransaction.setStatus(Parameter.REG_TRANSACTION_STATUS_pending_payment);
//						}else if(Parameter.REG_TRANSACTION_STATUS_payment_success.equals(llpRegTransaction.getStatus())){
//							alreadyPaid=true;
//						}
//						
//						
//						llpRegTransaction.setLlpRegistration(llpRegistration);
//						llpRegTransactionService.update(llpRegTransaction);
//						getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
//					}
//					
//					if(!alreadyPaid){
						List<LlpPaymentTransactionDetail> listPayment = new ArrayList<LlpPaymentTransactionDetail>();
						LlpPaymentTransactionDetail paymentLlpReg = new LlpPaymentTransactionDetail();
						paymentLlpReg.setPaymentItem(Parameter.PAYMENT_LLP_REGISTRATION);
						paymentLlpReg.setQuantity(1); //default one item
						paymentLlpReg.setPaymentDet(llpRegistration.getLlpName());
						listPayment.add(paymentLlpReg);
						setResponsePage(new PaymentDetailPage(String.valueOf(llpRegTransaction.getRegTransactionId()), LlpRegistrationService.class.getSimpleName(), llpRegistration, listPayment));
//					}else{
//						try {
//							llpRegistrationService.saveInfo(llpRegistration,"");
//							setResponsePage(ListLlpRegistration.class);
//						} catch (SSMException e) {
//							ssmError(e);
//						}
//					}
					
					
					
//					if(!UserEnvironmentHelper.isInternalUser()){//with payment
						
						//payment RM 500
//						List<LlpPaymentTransactionDetail> listPayment = new ArrayList<LlpPaymentTransactionDetail>();
//						LlpPaymentTransactionDetail paymentLlpReg = new LlpPaymentTransactionDetail();
//						paymentLlpReg.setPaymentItem(Parameter.PAYMENT_LLP_REGISTRATION);
//						paymentLlpReg.setQuantity(1); //default one item
//						paymentLlpReg.setPaymentDet(llpRegistration.getLlpName());
//						listPayment.add(paymentLlpReg);
						
						//CA or LAW need to add RM 30
			/*			if(StringUtils.isNotBlank(llpRegistration.getLlpReservedName().getProfBodyType())){
						if((Parameter.PROF_BODY_TYPE_ca.equals(llpRegistration.getLlpReservedName().getProfBodyType()))||
								(Parameter.PROF_BODY_TYPE_law.equals(llpRegistration.getLlpReservedName().getProfBodyType()))){
							LlpPaymentTransactionDetail paymentNs = new LlpPaymentTransactionDetail();
							paymentNs.setPaymentItem(Parameter.PAYMENT_RESERVED_NAME);
							paymentNs.setQuantity(1);
							listPayment.add(paymentNs);
						}
						} */
						
						//if success payment and then save into DB after payment is in the LlpRegistrationBasePage
//						setResponsePage(new PaymentDetailPage(LlpRegistrationBasePage.class, llpRegistration, listPayment));

//					}else{ //no payment needed ie. maintenance (internal)
//						
//						llpRegistrationService.saveInfo(llpRegistration,"");
//						setResponsePage(ListLlpRegistration.class);
//					}

				} catch (SSMException e) {
					ssmError(e);
				}
			}
			
		};
		lodgeLink.setEnabled(false);
		lodgeLink.setOutputMarkupId(true);
		add(lodgeLink);
		
		
		AjaxCheckBox declareChk = new AjaxCheckBox("declareChk", new PropertyModel(llpRegistration, "declareChk")) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (String.valueOf(true).equals(getValue())) {
					lodgeLink.setEnabled(true);
				} else {
					lodgeLink.setEnabled(false);
				}
				
				if(lodgeLink.isVisible())
					target.add(lodgeLink);
			}
			
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				if (String.valueOf(true).equals(getValue())) {
					lodgeLink.setEnabled(true);
				} else {
					lodgeLink.setEnabled(false);
				}
			}
			
		};
		add(declareChk);
		//declareChk.setVisible(false);
		
		
		SSMLink cancel = new SSMLink("cancel") {
			@Override
			public void onClick() {
				setResponsePage(ListLlpRegistration.class);
				
			}	
		};
		add(cancel);
		
		
		
		
		if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm (public user)
			updateInfo.setVisible(false); //button for officer only
			declareChk.setVisible(true);
			if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
				lodgeLink.setVisible(false);
				declareChk.setVisible(false);
				searchBusinessCodeBtn.setVisible(false);
				//cancel.setVisible(false);
				
			}
		}
		else{ //if officer
			updateInfo.setVisible(true); //button for officer only
			declareChk.setVisible(false);
			lodgeLink.setVisible(false);
		}
	}

}
