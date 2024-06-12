package com.ssm.ezbiz.robformA;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class TabRobFormAPage extends SecBasePage{
	
	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;
	
	public TabRobFormAPage() {
		setOutputMarkupId(true);
		ListRobFormAPage formAdraftPanel = new ListRobFormAPage("draftPanel",new String[]{Parameter.ROB_FORM_A_STATUS_DATA_ENTRY, Parameter.ROB_FORM_A_STATUS_OTC, Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT});
		formAdraftPanel.setOutputMarkupId(true);
		add(formAdraftPanel);	
		
		SSMLabel inProcessDummy = new SSMLabel("inProcessPanel");
		inProcessDummy.setOutputMarkupId(true);
		add(inProcessDummy);
		
		SSMLabel queryPanel = new SSMLabel("queryPanel");
		queryPanel.setOutputMarkupId(true);
		add(queryPanel);
		
		SSMLabel approvedRejectPanel = new SSMLabel("approvedRejectPanel");
		approvedRejectPanel.setOutputMarkupId(true);
		add(approvedRejectPanel);
		
		SSMLabel cancelPanel = new SSMLabel("cancelPanel");
		cancelPanel.setOutputMarkupId(true);
		add(cancelPanel);
		
		SSMLabel incentivePanel = new SSMLabel("incentivePanel");
		incentivePanel.setOutputMarkupId(true);
		add(incentivePanel);
		
	
		SSMAjaxLink draftLink = new SSMAjaxLink<Void>("draftLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("draftPanel",new String[]{Parameter.ROB_FORM_A_STATUS_DATA_ENTRY,  Parameter.ROB_FORM_A_STATUS_OTC, Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(draftLink);
		
		SSMAjaxLink inProcessLink = new SSMAjaxLink<Void>("inProcessLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("inProcessPanel",Parameter.ROB_FORM_A_STATUS_IN_PROCESS);
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(inProcessLink);
		
		SSMAjaxLink queryLink = new SSMAjaxLink<Void>("queryLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("queryPanel",new String[]{Parameter.ROB_FORM_A_STATUS_QUERY});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(queryLink);
		
		SSMAjaxLink approvedRejectLink = new SSMAjaxLink<Void>("approvedRejectLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("approvedRejectPanel",new String[]{Parameter.ROB_FORM_A_STATUS_REJECT,Parameter.ROB_FORM_A_STATUS_APPROVED});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(approvedRejectLink);
		
		SSMAjaxLink cancelLink = new SSMAjaxLink<Void>("cancelLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("cancelPanel",new String[]{Parameter.ROB_FORM_A_STATUS_CANCEL});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		add(cancelLink);
		
		SSMAjaxLink incentiveLink = new SSMAjaxLink<Void>("incentiveLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				ListRobFormAPage panel = new ListRobFormAPage("incentivePanel",new String[]{Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY, Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION, Parameter.ROB_FORM_A_STATUS_DATA_ENTRY, Parameter.ROB_FORM_A_STATUS_OTC, Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS,Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT});
				panel.setOutputMarkupId(true);
				getPage().replace(panel);
				target.add(panel);
			}
		};
		incentiveLink.setOutputMarkupId(true);
		add(incentiveLink);
		
		LlpParameters incentiveEnable = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG, Parameter.PAYMENT_CONFIG_ALLOW_INCENTIVE);
		if(Parameter.YES_NO_no.equals(incentiveEnable.getCodeDesc())){
			incentivePanel.setVisible(false);
			incentiveLink.setVisible(false);
		}else{
			incentivePanel.setVisible(true);
			incentiveLink.setVisible(true);
		}
	}
}
