package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.ParamLocale;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ListPartnerRegistrationPanel extends SecBasePanel {
	

	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	

	public ListPartnerRegistrationPanel(String panelId) {
		super(panelId);
		final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");

		// add new partner - popup window
		final ModalWindow searchPartnerPopUp = new ModalWindow("searchPartnerPopUp");
		add(searchPartnerPopUp);

		List<LlpPartnerLink> getListLlpPartnerLink = new ArrayList<LlpPartnerLink>(llpRegistration.getLlpPartnerLinks());

		List<LlpPartnerLink> newListLlpPartnerLink = new ArrayList<LlpPartnerLink>();

		final LlpPartnerLink llpPartnerLinkCO = getComplianceOfficer(getListLlpPartnerLink);
		
		LlpPartnerLink llpPartnerLink = new LlpPartnerLink();

		for (int i = 0; i < getListLlpPartnerLink.size(); i++) {
			llpPartnerLink = getListLlpPartnerLink.get(i);
			if (Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())) {
				continue; // loop next record
			}
			if(Parameter.PARTNER_LINK_STATUS_active.equals(llpPartnerLink.getLinkStatus())){
			newListLlpPartnerLink.add(llpPartnerLink); //to populate at partner listing
			}
		}

		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider(newListLlpPartnerLink);
		final SSMDataView<LlpPartnerLink> dataView = new SSMDataView<LlpPartnerLink>("sorting", dp) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<LlpPartnerLink> item) { // populate list of partner
				LlpPartnerLink partner = item.getModelObject();
				item.add(new SSMLabel("idNo", partner.getLlpUserProfile().getIdNo()));
				item.add(new SSMLabel("name", partner.getLlpUserProfile().getName()));
				//item.add(new SSMLabel("gender", partner.getLlpUserProfile().getGender()));
				//item.add(new SSMLabel("type", partner.getType()));
				item.add(new SSMLabel("userStatus", partner.getLinkStatus(), Parameter.PARTNER_LINK_STATUS));
				item.add(new SSMLabel("appointmentDate", partner.getAppointmentDate()));
				item.add(new SSMLabel("createDate", partner.getCreateDt()));

				
				SSMLink deleteLink = new SSMLink("delete", item.getDefaultModel(),true) {
					public void onClick() {
						final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
						List<LlpPartnerLink> listLlpPartnerLink = llpRegistration.getLlpPartnerLinks();

						LlpPartnerLink partnerLink = (LlpPartnerLink) getModelObject();
						if (partnerLink.getIdPartnerLink() == 0) { // if record not exist in db
							for (int i = 0; i < listLlpPartnerLink.size(); i++) {
								LlpPartnerLink partnerLinkTemp = listLlpPartnerLink.get(i);
								if (Parameter.USER_TYPE_partner.equals(partnerLinkTemp.getType())) { // to avoid delete CO who is also become PT in the session
									if (partnerLink.getLlpUserProfile().getIdNo() == (partnerLinkTemp.getLlpUserProfile().getIdNo())) {
										listLlpPartnerLink.remove(i);
										break;
									}
								}
							}
						} else { // if record exist in db / if maintenance
							for (int i = 0; i < listLlpPartnerLink.size(); i++) {
								LlpPartnerLink partnerLinkTmp = listLlpPartnerLink.get(i);
								 if (partnerLink.getIdPartnerLink() == (partnerLinkTmp.getIdPartnerLink())) {
									partnerLinkTmp.setLinkStatus(Parameter.PARTNER_LINK_STATUS_inactive);
								 }
							}
						}

						System.out.println("TOTAL LIST:" + listLlpPartnerLink.size());
						setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.PARTNER_LINK_PANEL));
					}
				};
				item.add(deleteLink);
				

				AjaxLink editLink = new AjaxLink("ajaxEdit", item.getDefaultModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						final LlpPartnerLink partnerLink = (LlpPartnerLink) getModelObject();
						searchPartnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
							@Override
							public Page createPage() {
								return new SearchPartnerPage(ListPartnerRegistrationPanel.this.getPage(), partnerLink, new ModalWindow("searchPartnerPopUp")); // edit record
							}
						});
						searchPartnerPopUp.show(target);
					}
				};
				item.add(editLink); //add into form
				
				
				String editLinkLabel = "[Edit]", deleteLinkLabel = "[Delete]";
				if(!UserEnvironmentHelper.isInternalUser()){ //if public = VIEW
					if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
						editLinkLabel = "[View]";
						deleteLinkLabel = "";
					}
				}

				editLink.add(new SSMLabel("editLabel",editLinkLabel)); //add into hyper-link editLink
				deleteLink.add(new SSMLabel("deleteLabel",deleteLinkLabel));
				
				
				if(StringUtils.isBlank(llpPartnerLinkCO.getLlpUserProfile().getLicenseMbrNo())){
					if(partner.getLlpUserProfile()!=null && partner.getLlpUserProfile().getUserRefNo()!=null && partner.getLlpUserProfile().getUserRefNo().equals(llpPartnerLinkCO.getLlpUserProfile().getUserRefNo())){////if not professional member.mandatory to become partner.cannot delete
//						deleteLink.setVisible(false);
						deleteLink.setEnabled(false);
						deleteLink.addOrReplaceOnClick(ParamLocale.ACTION_ALERT_CO_MUST_OWNER);
						deleteLink.addOrReplaceTitle(ParamLocale.ACTION_INFO_CO_MUST_OWNER);
					}
				}
				
				

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

		searchPartnerPopUp.setCookieName("modal-4");
		searchPartnerPopUp.setResizable(true);
		searchPartnerPopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			@Override
			public void onClose(AjaxRequestTarget target) {
				setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.PARTNER_LINK_PANEL));
			}
		});

		searchPartnerPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		
		AjaxLink searchPartnerBtn = new AjaxLink<Void>("searchPartnerBtn") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				searchPartnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
					@Override
					public Page createPage() {
						return new SearchPartnerPage(ListPartnerRegistrationPanel.this.getPage(), searchPartnerPopUp, null); // get data from DB and insert into session
					}
				});
				searchPartnerPopUp.show(target);
			}
		};
		add(searchPartnerBtn);
		
		
		
		
		SSMLink cancel = new SSMLink("cancel") {
			@Override
			public void onClick() {
				setResponsePage(ListLlpRegistration.class);
				
			}	
		};
		add(cancel);
		
		
		SSMLink save = new SSMLink("save") { //save and continue
			@Override
			public void onClick() {
				
				LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
				LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
				try {
					llpRegistrationService.validatePartner(llpRegistration);
					
					//Save Transaction To DB
					if(StringUtils.isBlank(llpRegistration.getLlpNo())){
						LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
						llpRegTransaction.setLlpRegistration(llpRegistration);
						llpRegTransactionService.update(llpRegTransaction);
						getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
					}
					
					setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.BUSINESS_LINK_PANEL));
				} catch (SSMException e) {
					ssmError(e);
				}
				
			}	
		};
		add(save);
		
		
		if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm
			if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
				searchPartnerBtn.setVisible(false);
				save.setVisible(false);
				//cancel.setVisible(false);
			}
		}


	}
	
	public LlpPartnerLink  getComplianceOfficer(List<LlpPartnerLink> listLlpPartnerLink){
		for (int i = 0; i < listLlpPartnerLink.size(); i++) {
			LlpPartnerLink llpPartnerLink = listLlpPartnerLink.get(i);
			if (Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())) {
				return llpPartnerLink;
			}
		}
		return null;
	}
}
