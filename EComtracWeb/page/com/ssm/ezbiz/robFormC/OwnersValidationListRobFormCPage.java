package com.ssm.ezbiz.robFormC;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings(value="all")
public class OwnersValidationListRobFormCPage extends SecBasePage {

	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	
	public OwnersValidationListRobFormCPage() {
		
		final WebMarkupContainer wmcOwnerVerification = new WebMarkupContainer("wmcOwnerVerification");
		wmcOwnerVerification.setOutputMarkupId(true);
		wmcOwnerVerification.setVisible(true);	
		
	
		final LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());


		List list = robFormOwnerVerificationService.findAllVerificationFormCList(llpUserProfile);
		System.out.println("list : "+list.size());


		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", list);
		final SSMDataView<Object[]> dataView = new SSMDataView<Object[]>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			
			protected void populateItem(final Item<Object[]> item) {
				Object[] obj = item.getModelObject();
				final RobFormOwnerVerification robFormOwnerVerification = (RobFormOwnerVerification) obj[0];
				final RobFormC robFormC = (RobFormC) obj[1];
				
				item.add(new SSMLabel("robFormCCode", robFormOwnerVerification.getRobFormCode()));
				item.add(new SSMLabel("brNo", robFormC.getBrNo() + "-" + robFormC.getCheckDigit()));
				item.add(new SSMLabel("name", robFormC.getBizName()));
				System.out.println("list 3 : "+robFormOwnerVerification.getRobFormCode());
				
				
				
				
				robFormOwnerVerificationService.findByRobFormCodeAndIdNo(robFormOwnerVerification.getRobFormCode(), llpUserProfile.getIdNo());

				item.add(new SSMLabel("status", robFormC.getStatus(), Parameter.ROB_FORM_C_STATUS));
				item.add(new SSMLabel("updateDt", robFormOwnerVerification.getUpdateDt(), "dd/MM/yyyy hh:mm:ss a"));

				SSMLabel veriStatus = new SSMLabel("verificationStatus", robFormOwnerVerification.getStatus(), Parameter.ROB_OWNER_B_C_VERI_STATUS);
				item.add(veriStatus);

				if (Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION.equals(robFormOwnerVerification.getStatus())) {
					String styleAttr = "color: red;";
					veriStatus.add(new AttributeModifier("style", styleAttr));
				}

				
				
				
				
				
				String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormC.robFormCOwners.confirmAccept");
				SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormOwnerVerification>) wmcOwnerVerification.get("sorting")).getDataProvider();
						List<RobFormOwnerVerification> robFormOwnerVerifications = dpProvider.getListResult();

						robFormOwnerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
						robFormOwnerVerificationService.update(robFormOwnerVerification);
						robFormCService.sendEmailPartnerAccept(robFormC, robFormOwnerVerification);

						dpProvider.resetView(robFormOwnerVerifications);

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
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormOwnerVerification>) wmcOwnerVerification
								.get("sorting")).getDataProvider();
						List<RobFormOwnerVerification> roboFormOwnerVerifications = dpProvider.getListResult();

						//roboFormOwnerVerifications.remove(robFormOwnerVerification);

						robFormOwnerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT);
						robFormOwnerVerificationService.update(robFormOwnerVerification);
						robFormCService.sendEmailPartnerReject(robFormC, robFormOwnerVerification);
						dpProvider.resetView(roboFormOwnerVerifications);

						target.add(wmcOwnerVerification);
						setResponsePage(new OwnersValidationListRobFormCPage());
					}
				};
				rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
				rejectOwners.setVisible(false);
				item.add(rejectOwners);

				if ((Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus()) || Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT.equals(robFormC.getStatus()))  && robFormOwnerVerification.getIdNo().equals(llpUserProfile.getIdNo())) {
					if (!Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED.equals(robFormOwnerVerification.getStatus())) {
						acceptOwners.setVisible(true);
					}
					rejectOwners.setVisible(true);
				}

				
				
				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						setResponsePage(new ViewRobFormCDetailPage(robFormOwnerVerification.getRobFormCode(), getPage()));
					}
				});

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}

		};

		dataView.setItemsPerPage(20L);
		wmcOwnerVerification.add(dataView);
		wmcOwnerVerification.add(new SSMPagingNavigator("navigator", dataView));
		wmcOwnerVerification.add(new NavigatorLabel("navigatorLabel", dataView));
		add(wmcOwnerVerification);
	
	}
		
		public SearchCriteria getSearchCriteria() {
			LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());

			robFormOwnerVerificationService.findAllVerificationList(llpUserProfile);
			
			return null;
			/*
			LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());		

			SearchCriteria scOwners = new SearchCriteria("idNo", SearchCriteria.EQUAL, llpUserProfile.getIdNo());
				
			List<RobFormOwnerVerification> listBOwners = robFormOwnerVerificationService.findByCriteria(scOwners).getList();
			
			if(listBOwners.size()>0){
				String robFormBCode[] = new String[listBOwners.size()];
				for (int i = 0; i < listBOwners.size(); i++) {
					robFormBCode[i] = listBOwners.get(i).getRobFormCode();
				}
				SearchCriteria sc1 = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING );
			
				
				return sc1;
			}else{
				return null;
			}	*/	
				
		}
		

}