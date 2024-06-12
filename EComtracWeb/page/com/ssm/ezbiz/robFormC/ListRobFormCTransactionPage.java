package com.ssm.ezbiz.robFormC;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.ssm.ezbiz.robFormB.ListRobFormBPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ListRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormC;
@SuppressWarnings(value="all")

public class ListRobFormCTransactionPage extends SecBasePage {
	
	public ListRobFormCTransactionPage() {
		setOutputMarkupId(true);
		SearchBizForFormCPage draftPanel = new SearchBizForFormCPage("draftPanel",new String[]{Parameter.ROB_FORM_C_STATUS_OTC,Parameter.ROB_FORM_C_STATUS_DATA_ENTRY,Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT});
		//SSMLabel draftPanel = new SSMLabel("draftPanel");
		draftPanel.setOutputMarkupId(true);
		add(draftPanel);
				
		SSMLabel inProcessDummy = new SSMLabel("inProcessPanel");
		inProcessDummy.setOutputMarkupId(true);
		add(inProcessDummy);
		
		SSMLabel queryPanel = new SSMLabel("queryPanel");
		queryPanel.setOutputMarkupId(true);
		add(queryPanel);
		
		SSMLabel approvedRejectPanel = new SSMLabel("approvedRejectPanel");
		approvedRejectPanel.setOutputMarkupId(true);
		add(approvedRejectPanel);
		
	

		SSMAjaxLink draftLink = new SSMAjaxLink<Void>("draftLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SearchBizForFormCPage panel = new SearchBizForFormCPage("draftPanel",new String[]{Parameter.ROB_FORM_C_STATUS_OTC,Parameter.ROB_FORM_C_STATUS_DATA_ENTRY,Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT});

				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(draftLink);
		
		
		SSMAjaxLink inProcessLink = new SSMAjaxLink<Void>("inProcessLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SearchBizForFormCPage panel = new SearchBizForFormCPage("inProcessPanel",new String[]{Parameter.ROB_FORM_C_STATUS_IN_PROCESS,Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(inProcessLink);
		
		SSMAjaxLink queryLink = new SSMAjaxLink<Void>("queryLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SearchBizForFormCPage panel = new SearchBizForFormCPage("queryPanel",new String[]{Parameter.ROB_FORM_C_STATUS_QUERY});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(queryLink);
		
		SSMAjaxLink approvedRejectLink = new SSMAjaxLink<Void>("approvedRejectLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SearchBizForFormCPage panel = new SearchBizForFormCPage("approvedRejectPanel",new String[]{Parameter.ROB_FORM_C_STATUS_REJECT,Parameter.ROB_FORM_C_STATUS_APPROVED});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(approvedRejectLink);
	}
		


}
