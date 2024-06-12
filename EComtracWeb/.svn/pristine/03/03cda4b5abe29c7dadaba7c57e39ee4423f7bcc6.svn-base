package com.ssm.ezbiz.otcPayment;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

public class CreditCardProcessingPanel extends Panel{

	
	private WebMarkupContainer wmcAlert;
	private SSMLabel titleLbl;
	private SSMLabel msgLbl;
	
	public CreditCardProcessingPanel(String id) {
		super(id);
		
		wmcAlert = new WebMarkupContainer("wmcAlert");
		wmcAlert.setOutputMarkupId(true);
		wmcAlert.setOutputMarkupPlaceholderTag(true);
		add(wmcAlert);
		
		titleLbl = new SSMLabel("title", resolve("alert.creditCardTitle"));
		wmcAlert.add(titleLbl);
		
	}
	
	public void resetAlert(String title, AjaxRequestTarget target){
		titleLbl.setDefaultModelObject(title);
		target.add(wmcAlert);
	}
	
	public String getWmcAlertId() {
		return wmcAlert.getMarkupId();
	}

	
}
