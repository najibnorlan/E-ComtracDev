package com.ssm.ezbiz.dashboard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.robFormB.ViewRobFormBPage;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobRenewal;

public class DashboardFormBAlert extends Panel{

	public DashboardFormBAlert(String id, List<RobFormB> listPendingTrans) {
		super(id);
		
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("brNo", listPendingTrans);
		final SSMDataView<RobFormB> dataView = new SSMDataView<RobFormB>("sorting", dp) {

			protected void populateItem(final Item<RobFormB> item) {
				final RobFormB robFormB = item.getModelObject();
				item.add(new SSMLabel("refNo", robFormB.getRobFormBCode()));
				item.add(new SSMLabel("brNo", robFormB.getBrNo() + "-" + robFormB.getCheckDigit()));
				item.add(new SSMLabel("bizName", robFormB.getBizName()));
				item.add(new SSMLabel("status", robFormB.getStatus(),Parameter.ROB_FORM_B_STATUS) );
				
				SSMAjaxLink viewAction = new SSMAjaxLink("viewAction") {
					@Override
					public void onClick(AjaxRequestTarget arg0) {
						if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
							setResponsePage(new EditRobFormBPage(robFormB.getRobFormBCode()));
						}else{
							setResponsePage(new ViewRobFormBPage(robFormB.getRobFormBCode(), getPage()));
						}
					}
				};
				item.add(viewAction);
			}
		};
		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}
	
}
