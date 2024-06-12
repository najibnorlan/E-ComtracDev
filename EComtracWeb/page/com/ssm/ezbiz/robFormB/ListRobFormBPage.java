package com.ssm.ezbiz.robFormB;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
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
import com.ssm.llp.ezbiz.model.RobFormB;

public class ListRobFormBPage extends SecBasePanel{
	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	public ListRobFormBPage(String id, String tabStatus){
		super(id);
		init(new String[]{tabStatus});
	}
	public ListRobFormBPage(String id, String tabStatusArr[]){
		super(id);
		init(tabStatusArr);
	}
	
	public void init(String tabStatusArr[]) {
 		SearchCriteria sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
 		sc = sc.andIfInNotNull("status", tabStatusArr, false );
 		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormBService.class);
		
		final SSMDataView<RobFormB> dataView = new SSMDataView<RobFormB>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobFormB> item) {
				final RobFormB robFormB = item.getModelObject();
		        
				item.add(new SSMLabel("robFormBCode", robFormB.getRobFormBCode()));
				item.add(new SSMLabel("bizName", robFormB.getBizName()));
				item.add(new SSMLabel("status", robFormB.getStatus(), Parameter.ROB_FORM_B_STATUS ));
				item.add(new SSMLabel("updateDt", robFormB.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						RobFormB robFormBTmp = item.getModelObject();
						if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormBTmp.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
							setResponsePage(new EditRobFormBPage(robFormBTmp.getRobFormBCode()));
						}else{
							setResponsePage(new ViewRobFormBPage(robFormBTmp.getRobFormBCode(), getPage()));
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
