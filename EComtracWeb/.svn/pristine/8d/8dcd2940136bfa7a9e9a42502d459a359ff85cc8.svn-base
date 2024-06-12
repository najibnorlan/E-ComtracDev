package com.ssm.ezbiz.dashboard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

public class DashboardRenewalAlert extends Panel{

	public DashboardRenewalAlert(String id, List<RobRenewal> listPendingRenewal) {
		super(id);
		
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("brNo", listPendingRenewal);
		final SSMDataView<RobRenewal> dataView = new SSMDataView<RobRenewal>("sorting", dp) {

			protected void populateItem(final Item<RobRenewal> item) {
				final RobRenewal renewal = item.getModelObject();
				item.add(new SSMLabel("refNo", renewal.getTransCode()));
				item.add(new SSMLabel("brNo", renewal.getBrNo() + "-" + renewal.getChkDigit()));
				item.add(new SSMLabel("bizName", renewal.getBizName()));
				item.add(new SSMLabel("status", renewal.getStatus(),Parameter.ROB_RENEWAL_STATUS) );
				
				SSMAjaxLink viewAction = new SSMAjaxLink("viewAction") {
					@Override
					public void onClick(AjaxRequestTarget arg0) {
						setResponsePage(new EditRobRenewalPage(renewal));
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
