package com.ssm.ezbiz.robFormC;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormC;
@SuppressWarnings(value="all")
public class ListRobFormCStatusPage extends SecBasePanel {
	
	@SpringBean(name = "RobFormCService")
	private RobFormAService robFormCService;
	
	public ListRobFormCStatusPage(String id, String tabStatus){
		super(id);
		init(new String[]{tabStatus});
	}
	public ListRobFormCStatusPage(String id, String tabStatusArr[]){
		super(id);
		init(tabStatusArr);
	}
	
	public void init(String tabStatusArr[]) {
 		SearchCriteria sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
 		sc = sc.andIfInNotNull("status", tabStatusArr, false );
 		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormCService.class);
		
		final SSMDataView<RobFormC> dataView = new SSMDataView<RobFormC>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobFormC> item) {
				final RobFormC robFormC = item.getModelObject();
		        
				item.add(new SSMLabel("robFormACode", robFormC.getRobFormCCode()));
				item.add(new SSMLabel("bizName", robFormC.getBizName()));
				item.add(new SSMLabel("status", robFormC.getStatus(), Parameter.ROB_FORM_A_STATUS ));
				item.add(new SSMLabel("updateDt", robFormC.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						RobFormC robFormC = item.getModelObject();
						if(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus())|| Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
							setResponsePage(new EditRobFormCPage(robFormC));
						}else{
						//	setResponsePage(new ViewRobFormCPage(robFormA.getRobFormACode(), getPage()));
						}
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
	}
	
	
	public SearchCriteria getSearchCriteria() {
		SearchCriteria sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
		return sc;
	}

}