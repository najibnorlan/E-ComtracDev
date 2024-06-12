package com.ssm.ezbiz.myCardTransaction;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class ReadMyKadProcessingPanel extends Panel{

	
	private WebMarkupContainer wmcAlert;
	private SSMLabel titleLbl;
	private SSMLabel descriptionLbl;
	private SSMLabel msgLbl;
	private Image image;
	
	public ReadMyKadProcessingPanel(String id) {
		super(id);
		
		wmcAlert = new WebMarkupContainer("wmcAlert");
		wmcAlert.setOutputMarkupId(true);
		wmcAlert.setOutputMarkupPlaceholderTag(true);
		add(wmcAlert);
		
		titleLbl = new SSMLabel("title", resolve("alert.readMyKad"));
		wmcAlert.add(titleLbl);
		
		descriptionLbl = new SSMLabel("descriptionLbl", resolve("page.ssm.ezbiz.myCardDetailPage.insertMyKad"));
		descriptionLbl.setOutputMarkupId(true);
		descriptionLbl.setOutputMarkupPlaceholderTag(true);
		
		wmcAlert.add(descriptionLbl);
		
		
		image = new Image("img", "");
		image.add(new AttributeModifier("src", getContextPath()+"/images/insertSmardCard.gif"));
		
		
		wmcAlert.add(image);
	}
	
	
	private String getContextPath() {
		HttpServletRequest  request=(HttpServletRequest)getRequestCycle().getRequest().getContainerRequest();
		return request.getContextPath();
	}


	public void resetData(String desc, String srcImage, AjaxRequestTarget target){
		image.add(new AttributeModifier("src", getContextPath()+"/"+srcImage));
		descriptionLbl.setDefaultModelObject(desc);
		
		target.add(descriptionLbl);
		target.add(wmcAlert);
	}
	
	public void resetAlert(String title, AjaxRequestTarget target){
		titleLbl.setDefaultModelObject(title);
		target.add(wmcAlert);
	}
	
	public String getWmcAlertId() {
		return wmcAlert.getMarkupId();
	}

	
}
