package com.ssm.ezbiz.dashboard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

import com.ssm.ezbiz.robFormC.EditRobFormCPage;
import com.ssm.ezbiz.robFormC.ViewRobFormCPage;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

public class DashboardFormCAlert extends Panel{

	public DashboardFormCAlert(String id, List<RobFormC> listPendingTrans) {
		super(id);
		
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("brNo", listPendingTrans);
		final SSMDataView<RobFormC> dataView = new SSMDataView<RobFormC>("sorting", dp) {

			protected void populateItem(final Item<RobFormC> item) {
				final RobFormC robFormC = item.getModelObject();
				item.add(new SSMLabel("refNo", robFormC.getRobFormCCode()));
				item.add(new SSMLabel("brNo", robFormC.getBrNo() + "-" + robFormC.getCheckDigit()));
				item.add(new SSMLabel("bizName", robFormC.getBizName()));
				item.add(new SSMLabel("status", robFormC.getStatus(),Parameter.ROB_FORM_C_STATUS) );
				
				SSMAjaxLink viewAction = new SSMAjaxLink("viewAction") {
					@Override
					public void onClick(AjaxRequestTarget arg0) {
						if(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus())|| Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
							setResponsePage(new EditRobFormCPage(robFormC.getRobFormCCode()));
						}else{
							setResponsePage(new ViewRobFormCPage(robFormC.getRobFormCCode(), getPage()));
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
