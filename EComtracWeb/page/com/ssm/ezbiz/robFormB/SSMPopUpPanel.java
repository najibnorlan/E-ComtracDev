package com.ssm.ezbiz.robFormB;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB4;	

@SuppressWarnings("all")
public class SSMPopUpPanel extends SecBasePanel {
	private WebMarkupContainer wmcPopUp;
	private SSMLabel titleLbl;
	private SSMLabel msgLbl;
	
	public SSMPopUpPanel(String panelId, String headerTitleKey) {
		super(panelId);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
//		wmcPopUp = new WebMarkupContainer("wmcPopUp");
//		wmcPopUp.setOutputMarkupId(true);
//		wmcPopUp.setOutputMarkupPlaceholderTag(true);
//		add(wmcPopUp);
		
		titleLbl = new SSMLabel("headerTitle", resolve(headerTitleKey));
		add(titleLbl);
//		wmcPopUp.add(msgLbl);
//		wmcPopUp.add(new SSMLabel("additionalMsg", ""));
//		add(new AttributeModifier("class", "ui modal"));
//		add(new AttributeModifier("style", "z-index:999"));
		
		
//		class="ui modal" ="z-index:999"
	}

	public void showPanel(AjaxRequestTarget target) {
		System.out.println("ModalId:"+this.getMarkupId());
		
		
		String jScript = "showModalParentID('"+this.getMarkupId()+"');";
		jScript = "setTimeout(function() { "+jScript+"; }, 1000); ";//Delay bcoz update model then show
		
		
		target.prependJavaScript(jScript);
	}
	
	public void resetAndShowContent(String title, String msg, AjaxRequestTarget target){
		titleLbl.setDefaultModelObject(title);
		msgLbl.setDefaultModelObject(msg);
		SSMLabel lbl = new SSMLabel("additionalMsg", "");
		wmcPopUp.replace(lbl);
		target.add( wmcPopUp);
	}
	
}
