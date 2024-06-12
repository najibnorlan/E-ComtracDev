package com.ssm.ezbiz.robformA;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class OwnersValidationListRobFormAPage extends SecBasePage {

	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;
	
	public OwnersValidationListRobFormAPage() {
		
		final LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
		
 		SearchCriteria sc = getSearchCriteria();
		SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormAService.class);
		
		final SSMDataView<RobFormA> dataView = new SSMDataView<RobFormA>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobFormA> item) {
				final RobFormA robFormA = item.getModelObject();
		        
				item.add(new SSMLabel("robFormACode", robFormA.getRobFormACode()));
				item.add(new SSMLabel("bizName", robFormA.getBizName()));
				
				RobFormAOwner robOwner = robFormAOwnerService.findByRobFormACodeAndIcNo(robFormA.getRobFormACode(), llpUserProfile.getIdNo());
				
				item.add(new SSMLabel("status", robFormA.getStatus(), Parameter.ROB_FORM_A_STATUS ));
				item.add(new SSMLabel("updateDt", robFormA.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
				
				SSMLabel veriStatus = new SSMLabel("verificationStatus", robOwner.getVerificationStatus(), Parameter.ROB_OWNER_VERI_STATUS);
				item.add(veriStatus);
				
				if(Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robOwner.getVerificationStatus())){
					String styleAttr = "color: red;";
					veriStatus.add(new AttributeModifier("style", styleAttr));
				}

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						RobFormA robFormA = item.getModelObject();
						setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), getPage()));
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


		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
//		add(new RobRenewalForm("form", getDefaultModel()));
	}
		
	public SearchCriteria getSearchCriteria() {
		LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());

		SearchCriteria scOwners = new SearchCriteria("newicno", SearchCriteria.EQUAL, llpUserProfile.getIdNo());
		SearchCriteria scOwners2 = new SearchCriteria("robFormACode", SearchCriteria.IS_NOT_NULL);
		scOwners = new SearchCriteria(scOwners , SearchCriteria.AND, scOwners2);
		
		List<RobFormAOwner> listOwners = robFormAOwnerService.findByCriteria(scOwners).getList();
		if(listOwners.size()>0){
			String robFormACode[] = new String[listOwners.size()];
			for (int i = 0; i < listOwners.size(); i++) {
				robFormACode[i] = listOwners.get(i).getRobFormACode();
			}
			SearchCriteria sc1 = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_DATA_ENTRY );
			SearchCriteria sc2 = new SearchCriteria("createBy", SearchCriteria.NOT_EQUAL, UserEnvironmentHelper.getLoginName());
			SearchCriteria sc3 = new SearchCriteria("robFormACode", SearchCriteria.IN);
			sc3.setValues(robFormACode);
			
			SearchCriteria sc = new SearchCriteria(sc1, SearchCriteria.AND,  sc2);
			sc = new SearchCriteria(sc, SearchCriteria.AND,  sc3);
			
			return sc;
		}else{
			return null;//new SearchCriteria();
		}
		
	}
}