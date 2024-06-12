package com.ssm.ezbiz.robFormB;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class TabRobFormBPage extends SecBasePage{
	
	public TabRobFormBPage() {
		setOutputMarkupId(true);
		ListRobFormBPage formBdraftPanel = new ListRobFormBPage("draftPanel",new String[]{Parameter.ROB_FORM_B_STATUS_OTC, Parameter.ROB_FORM_B_STATUS_DATA_ENTRY,Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT});
		formBdraftPanel.setOutputMarkupId(true);
		add(formBdraftPanel);	
		
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
				ListRobFormBPage panel = new ListRobFormBPage("draftPanel",new String[]{Parameter.ROB_FORM_B_STATUS_OTC, Parameter.ROB_FORM_B_STATUS_DATA_ENTRY,Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(draftLink);
		
		SSMAjaxLink inProcessLink = new SSMAjaxLink<Void>("inProcessLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormBPage panel = new ListRobFormBPage("inProcessPanel",Parameter.ROB_FORM_B_STATUS_IN_PROCESS);
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(inProcessLink);
		
		SSMAjaxLink queryLink = new SSMAjaxLink<Void>("queryLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormBPage panel = new ListRobFormBPage("queryPanel",new String[]{Parameter.ROB_FORM_B_STATUS_QUERY});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(queryLink);
		
		SSMAjaxLink approvedRejectLink = new SSMAjaxLink<Void>("approvedRejectLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormBPage panel = new ListRobFormBPage("approvedRejectPanel",new String[]{Parameter.ROB_FORM_B_STATUS_REJECT,Parameter.ROB_FORM_B_STATUS_APPROVED});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(approvedRejectLink);
	}
}
