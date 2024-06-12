package com.ssm.llp.mod1.page;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

//LlpRegistrationListPage = search panel      
public class ListLlpRegistration extends LlpRegistrationListPage { // LlpRegistrationBasePage{
	

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ListLlpRegistration(PageParameters params) {
		
		final String llpName = params.get("searchString") != null ? params.get("searchString").toString() : null;
		final String llpNo = params.get("llpNo") != null ? params.get("llpNo").toString() : null;
		
		

		
																					// llpName
																					// as
																					// per
																					// data
																					// object
		SSMSortableDataProvider dp;
		SearchCriteria sc = null;
		if(StringUtils.isNotBlank(llpName)){
			sc = SearchCriteria.andIfNotNull(sc , "llpName", SearchCriteria.LIKE, llpName + "%"); 
		}
		if(StringUtils.isNotBlank(llpNo)){
			sc = SearchCriteria.andIfNotNull(sc, "llpNo", SearchCriteria.EQUAL, llpNo);
		}
		if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm
			sc = SearchCriteria.andIfNotNull(sc, "lodgeBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
		}
		
		dp = new SSMSortableDataProvider("idLlp", sc, LlpRegistrationService.class);	
		
		final SSMDataView<LlpRegistration> dataView = new SSMDataView<LlpRegistration>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpRegistration> item) {
				LlpRegistration llpReg = item.getModelObject();

				item.add(new SSMLabel("idLlp", String.valueOf(llpReg.getIdLlp())));
				item.add(new SSMLabel("llpNo", llpReg.getLlpNo()));
				item.add(new SSMLabel("llpName", llpReg.getLlpName()));
				item.add(new SSMLabel("llpStatus", llpReg.getLlpStatus(), Parameter.LLP_STATUS));

				// item.add(new SmartLinkLabel("email",contact.getEmail()));
				
				SSMLink editLink = new SSMLink("edit", item.getDefaultModel()) {
					public void onClick() {
						LlpRegistration l = (LlpRegistration) getModelObject();
						setResponsePage(new LlpRegistrationBasePage(l.getLlpNo()));
					}
				};
				item.add(editLink);
				String linkLabel = "";
				if(!UserEnvironmentHelper.isInternalUser()){ //if public = VIEW
					linkLabel = "[View]";
				}
				else{
					linkLabel = "[Edit]"; //if officer = EDIT
				}
				editLink.add(new SSMLabel("editLabel",linkLabel));
				
				
//				item.add(new Link("delete", item.getDefaultModel()) {
//					public void onClick() {
//						LlpRegistration l = (LlpRegistration) getModelObject();
//						getService(LlpRegistrationService.class.getSimpleName()).delete(l);
//						setResponsePage(ListLlpRegistration.class);
//					}
//				});
				
				

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		add(new OrderByBorder("orderByLlpName", "llpName", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByLlpStatus", "llpStatus", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	public void sortingPage(List<LlpRegistration> listResult) {

	}
	
	public String getPageTitle() {
		String titleKey = "page.title.registration.list";
		return titleKey;
	}

}
