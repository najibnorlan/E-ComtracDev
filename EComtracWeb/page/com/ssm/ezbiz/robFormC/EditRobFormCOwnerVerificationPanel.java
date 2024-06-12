package com.ssm.ezbiz.robFormC;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.OwnersValidationListRobFormAPage;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormCOwnerVerificationPanel extends RobFormCBasePanel{
	

	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;
	

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	
	
	public EditRobFormCOwnerVerificationPanel(String panelId, final RobFormC robFormC) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	
            	return new RobFormC();
            }
        }));
		add(new RobFormCOwnerVerificationPanelForm("robFormCOwnerVerificationPanelForm", getDefaultModel(), robFormC));
	}
	
	private class RobFormCOwnerVerificationPanelForm extends Form implements Serializable {
		final RobFormC robFormC;
		final boolean isQuery;
		
		public RobFormCOwnerVerificationPanelForm(String id,final IModel m,final RobFormC robFormC) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormC.");
			
			if(Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			this.robFormC = robFormC;
			final RobFormCOwnerVerificationPanelForm panelForm  = this;
			
		
            final WebMarkupContainer wmcOwnerVerification = new WebMarkupContainer("wmcOwnerVerification");
            wmcOwnerVerification.setOutputMarkupId(true);
            wmcOwnerVerification.setOutputMarkupPlaceholderTag(true);
			add(wmcOwnerVerification);
            
			final List listOwner = robFormC.getListRobFormOwnerVerification();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listOwner);
			final SSMDataView<RobFormOwnerVerification> dataView = new SSMDataView<RobFormOwnerVerification>("sortingRobFormCOwnerVeri", dp) {

				protected void populateItem(final Item<RobFormOwnerVerification> item) {
					final RobFormOwnerVerification robFormOwnerVer = item.getModelObject();
					
					item.add(new SSMLabel("ownerNo", item.getIndex()+1));
					item.add(new SSMLabel("name", robFormOwnerVer.getName()));
					item.add(new SSMLabel("idNo", robFormOwnerVer.getIdNo()));
					SSMLabel veriStatus = new SSMLabel("status", robFormOwnerVer.getStatus(), Parameter.ROB_OWNER_B_C_VERI_STATUS);
					item.add(veriStatus);
					
//					SSMLabel emailStatus = new SSMLabel("emailStatus", robFormOwnerVer.getEmailStatus(), Parameter.ROB_OWNER_VER_EMAIL_STATUS);
//					item.add(emailStatus);

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
					
					
					
					String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormC.robFormCOwners.confirmAccept");
					SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
//							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>) wmcOwners
//									.get("sortingRobFormAOwner")).getDataProvider();
//							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							
							List<RobFormOwnerVerification> robFormOwnerVerifications = dp.getListResult();

							robFormOwnerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
							robFormOwnerVerificationService.update(robFormOwnerVer);
							robFormCService.sendEmailPartnerAccept(robFormC, robFormOwnerVer);

							dp.resetView(robFormOwnerVerifications);

							target.add(wmcOwnerVerification);
						}

					};
					
					acceptOwners.setConfirmQuestion(confirmAcceptQuestion);
					acceptOwners.setVisible(false);
					item.add(acceptOwners);

					
					String rejectOwnerQuestion = resolve("page.lbl.ezbiz.robFormC.robFormCOwners.confirmReject");
					SSMAjaxLink rejectOwners = new SSMAjaxLink("rejectOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							List<RobFormOwnerVerification> roboFormOwnerVerifications = dp.getListResult();

							robFormOwnerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT);
							robFormOwnerVerificationService.update(robFormOwnerVer);
							robFormCService.sendEmailPartnerReject(robFormC, robFormOwnerVer);
							
							dp.resetView(roboFormOwnerVerifications);
							
							target.add(wmcOwnerVerification);
						}
					};
					rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
					rejectOwners.setVisible(false);
					item.add(rejectOwners);
					
					
					if (!UserEnvironmentHelper.isInternalUser()) {
			            final LlpUserProfile currentLlpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
						if ( (Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus())||Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT.equals(robFormC.getStatus()))  && robFormOwnerVer.getIdNo().equals(currentLlpUserProfile.getIdNo())) {
							if (!Parameter.ROB_OWNER_VERI_STATUS_VERIFIED.equals(robFormOwnerVer.getStatus())) {
								acceptOwners.setVisible(true);
							}
							rejectOwners.setVisible(true);
						}
					}

					if (Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robFormOwnerVer.getStatus())) {
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
				}

			};
			
			
			wmcOwnerVerification.add(dataView);
			wmcOwnerVerification.add(new SSMPagingNavigator("navigatorRobFormCOwnerVeri", dataView));
			wmcOwnerVerification.add(new NavigatorLabel("navigatorLabelRobFormCOwnerVeri", dataView));
			
			SSMAjaxButton sendNotification = new SSMAjaxButton("sendNotification") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
				}
			};
			
			/*
			SSMAjaxButton cOwnerVerPrev = new SSMAjaxButton("cOwnerVerPrev") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					//hideAndShowSegment(target, 2);//mean show segment 3 - B1
				}
			};
			add(cOwnerVerPrev);
			
			
			SSMAjaxButton cOwnerVerNext = new SSMAjaxButton("cOwnerVerNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					//hideAndShowSegment(target, 4);//mean show segment 3 - B3
				}
			};
			add(cOwnerVerNext);
			*/
		}
	}
}