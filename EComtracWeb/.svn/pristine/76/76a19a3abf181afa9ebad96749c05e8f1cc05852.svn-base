package com.ssm.ezbiz.robFormB;

import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Param;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.OwnersValidationListRobFormAPage;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class OwnersValidationListRobFormBPage extends SecBasePage {
	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;

	public OwnersValidationListRobFormBPage() {

		final WebMarkupContainer wmcOwnerVerification = new WebMarkupContainer("wmcOwnerVerification");
		wmcOwnerVerification.setOutputMarkupId(true);
		wmcOwnerVerification.setVisible(true);

		final LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());


		List list = robFormOwnerVerificationService.findAllVerificationList(llpUserProfile);
		
		final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", list);

		final SSMDataView<Object[]> dataView = new SSMDataView<Object[]>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<Object[]> item) {
				Object[] obj = item.getModelObject();
				final RobFormOwnerVerification robFormOwnerVerification = (RobFormOwnerVerification) obj[0];
				final RobFormB robFormB = (RobFormB) obj[1];

				item.add(new SSMLabel("robFormBCode", robFormOwnerVerification.getRobFormCode()));
				item.add(new SSMLabel("brNo", robFormB.getBrNo() + "-" + robFormB.getCheckDigit()));
				item.add(new SSMLabel("name", robFormB.getBizName()));

				robFormOwnerVerificationService.findByRobFormCodeAndIdNo(robFormOwnerVerification.getRobFormCode(), llpUserProfile.getIdNo());

				item.add(new SSMLabel("status", robFormB.getStatus(), Parameter.ROB_FORM_B_STATUS));
				item.add(new SSMLabel("updateDt", robFormOwnerVerification.getUpdateDt(), "dd/MM/yyyy hh:mm:ss a"));

				SSMLabel veriStatus = new SSMLabel("verificationStatus", robFormOwnerVerification.getStatus(), Parameter.ROB_OWNER_B_C_VERI_STATUS);
				item.add(veriStatus);

				if (Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION.equals(robFormOwnerVerification.getStatus())) {
					String styleAttr = "color: red;";
					veriStatus.add(new AttributeModifier("style", styleAttr));
				}

				String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormB.robFormBOwners.confirmAccept");
				SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormOwnerVerification>) wmcOwnerVerification
								.get("sorting")).getDataProvider();
						List<RobFormOwnerVerification> robFormOwnerVerifications = dpProvider.getListResult();

						robFormOwnerVerification.setStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
						robFormOwnerVerificationService.update(robFormOwnerVerification);
						robFormBService.sendEmailPartnerAccept(robFormB, robFormOwnerVerification);

						dpProvider.resetView(robFormOwnerVerifications);

						target.add(wmcOwnerVerification);

					}

				};
				acceptOwners.setConfirmQuestion(confirmAcceptQuestion);
				acceptOwners.setVisible(false);
				item.add(acceptOwners);

				String rejectOwnerQuestion = resolve("page.lbl.ezbiz.robFormB.robFormBOwners.confirmRemove");
				SSMAjaxLink rejectOwners = new SSMAjaxLink("rejectOwners") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormOwnerVerification>) wmcOwnerVerification
								.get("sorting")).getDataProvider();
						List<RobFormOwnerVerification> roboFormOwnerVerifications = dpProvider.getListResult();

						robFormOwnerVerification.setStatus(Parameter.ROB_OWNER_VERI_STATUS_REJECT);
						robFormBService.sendEmailPartnerReject(robFormB, robFormOwnerVerification);
						robFormOwnerVerificationService.update(robFormOwnerVerification);

						dpProvider.resetView(roboFormOwnerVerifications);

						target.add(wmcOwnerVerification);
//						setResponsePage(new OwnersValidationListRobFormBPage());
						
					}
				};
				rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
				rejectOwners.setVisible(false);
				item.add(rejectOwners);

				if (Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())
						&& robFormOwnerVerification.getIdNo().equals(llpUserProfile.getIdNo())) {
					if (!Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED.equals(robFormOwnerVerification.getStatus())) {
						acceptOwners.setVisible(true);
					}
					rejectOwners.setVisible(true);
				}

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						setResponsePage(new ViewRobFormBPage(robFormOwnerVerification.getRobFormCode(), getPage()));
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

		// add(dataView);
		// add(new SSMPagingNavigator("navigator", dataView));
		// add(new NavigatorLabel("navigatorLabel", dataView));

		wmcOwnerVerification.add(dataView);
		wmcOwnerVerification.add(new SSMPagingNavigator("navigator", dataView));
		wmcOwnerVerification.add(new NavigatorLabel("navigatorLabel", dataView));
		add(wmcOwnerVerification);
	}

	public SearchCriteria getSearchCriteria() {
		LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());

		robFormOwnerVerificationService.findAllVerificationList(llpUserProfile);
		
//		SearchCriteria sc = new SearchCriteria("ownerVer.robFormCode", SearchCriteria.EQUAL, "robFormB.robFormBCode");
//		sc = new SearchCriteria("ownerVer.userRefNo", SearchCriteria.EQUAL, llpUserProfile.getUserRefNo());
//		sc = SearchCriteria.andIfNotNull(sc, "ownerVer.formType", SearchCriteria.EQUAL, Parameter.ROB_FORM_TYPE_B);
//		sc = SearchCriteria.andIfInNotNull(sc, "ownerVer.status", new String[]{Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION,Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT,Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED}, false);
//		
//		
//		SearchCriteria sc1 = new SearchCriteria("secUser.userlvl", SearchCriteria.EQUAL, "LEVEL1"); 
//		
//		MultiEntitySearchCriteria meSc = new MultiEntitySearchCriteria(); 
//		meSc.addReturnField("ownerVer");
////		meSc.setDistinctReturnFields(true); 
//		meSc.setMainClassName(RobFormOwnerVerification.class.getName());
//		meSc.setMainAliasName("ownerVer"); 
//		meSc.addAssociatedClass(RobFormB.class.getName(), MultiEntitySearchCriteria.ENTITY_JOIN_TYPE_INNER, "robFormB"); 
//		meSc.
//		meSc.setSearchCriteria(sc);
//
//		List list = robFormOwnerVerificationService.findByCriteria(meSc).getList();
//		
		
		return null;

	}

}