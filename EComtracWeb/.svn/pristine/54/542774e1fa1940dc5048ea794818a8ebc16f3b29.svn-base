package com.ssm.ezbiz.robformA;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormA;
@SuppressWarnings(value="all")
public class ListRobFormAPage extends SecBasePanel {
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;
	
	public ListRobFormAPage(String id, String tabStatus){
		super(id);
		init(id, new String[]{tabStatus});
	}
	public ListRobFormAPage(String id, String tabStatusArr[]){
		super(id);
		init(id, tabStatusArr);
	}
	
	public void init(String id, String tabStatusArr[]) {
 		SearchCriteria sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
 		sc = sc.andIfInNotNull("status", tabStatusArr, false );
 		
 		String incentive[] = {Parameter.ROB_FORM_A_INCENTIVE_oku, Parameter.ROB_FORM_A_INCENTIVE_student};
 		
 		if(id.equalsIgnoreCase("incentivePanel")){
 			sc = sc.andIfInNotNull("incentive", incentive, false);
 		}
 		
 		if(id.equalsIgnoreCase("draftPanel")){
 			sc = sc.andIfNotInNotNull("incentive", incentive, false);
 		}
		SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormAService.class);
		
		final SSMDataView<RobFormA> dataView = new SSMDataView<RobFormA>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobFormA> item) {
				final RobFormA robFormA = item.getModelObject();
		        
				item.add(new SSMLabel("robFormACode", robFormA.getRobFormACode()));
				item.add(new SSMLabel("bizName", robFormA.getBizName()));
				item.add(new SSMLabel("status", robFormA.getStatus(), Parameter.ROB_FORM_A_STATUS ));
				item.add(new SSMLabel("updateDt", robFormA.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						RobFormA robFormA = item.getModelObject();
						if(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(robFormA.getStatus())|| Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())){
							setResponsePage(new EditRobFormAPage(robFormA.getRobFormACode()));
						}else{
							setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), getPage()));
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