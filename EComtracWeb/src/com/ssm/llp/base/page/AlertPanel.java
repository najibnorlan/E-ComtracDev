package com.ssm.llp.base.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import com.ssm.ezbiz.dashboard.DashboardRenewalAlert;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class AlertPanel extends Panel {
	
	private WebMarkupContainer wmcAlert;
	private SSMLabel titleLbl;
	private SSMLabel msgLbl;
	
	
	public AlertPanel(String id) {
		super(id);
		
		wmcAlert = new WebMarkupContainer("wmcAlert");
		wmcAlert.setOutputMarkupId(true);
		wmcAlert.setOutputMarkupPlaceholderTag(true);
		add(wmcAlert);
		
		titleLbl = new SSMLabel("title", "title");
		msgLbl = new SSMLabel("msg", "msg");
		wmcAlert.add(titleLbl);
		wmcAlert.add(msgLbl);
		wmcAlert.add(new SSMLabel("additionalMsg", ""));
	}
//	
//	public AlertPanel(String id, boolean cannotClose) {
//		super(id);
//		
//		wmcAlert = new WebMarkupContainer("wmcAlert");
//		wmcAlert.setOutputMarkupId(true);
//		wmcAlert.setOutputMarkupPlaceholderTag(true);
//		add(wmcAlert);
//		
//		titleLbl = new SSMLabel("title", "title");
//		msgLbl = new SSMLabel("msg", "msg");
//		wmcAlert.add(titleLbl);
//		wmcAlert.add(msgLbl);
//		wmcAlert.add(new SSMLabel("additionalMsg", ""));
//		
//		
//		SSMLabel isWithButton = new SSMLabel("isWithButton", "");
//		wmcAlert.add(isWithButton);
//		
//		if(cannotClose) {
//			isWithButton.setVisible(false);
//		}
//	}
//	
	public void resetAlert(String title, String msg, AjaxRequestTarget target){
		titleLbl.setDefaultModelObject(title);
		msgLbl.setDefaultModelObject(msg);
		SSMLabel lbl = new SSMLabel("additionalMsg", "");
		wmcAlert.replace(lbl);
		target.add(wmcAlert);
	}
	
	public String getWmcAlertId() {
		return wmcAlert.getMarkupId();
	}

	public void resetAlert(String title, String msg, Panel addInfoPanel, AjaxRequestTarget target) {
		titleLbl.setDefaultModelObject(title);
		msgLbl.setDefaultModelObject(msg);
		wmcAlert.replace(addInfoPanel);
		
		target.add(wmcAlert);
	}

}