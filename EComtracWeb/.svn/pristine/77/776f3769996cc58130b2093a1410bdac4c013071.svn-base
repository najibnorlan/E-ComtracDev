package com.ssm.llp.base.wicket.component;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;

import com.ssm.llp.base.utils.WicketUtils;

public abstract class SSMAjaxButton extends AjaxButton{

	private String validationJs;
	private String confirmQuestion;
	public SSMAjaxButton(String id) {
		super(id);
	}
	
	public SSMAjaxButton(String id, String validationJs) {
		super(id);
		this.validationJs = validationJs;
	}
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes); 
		if(StringUtils.isNotBlank(validationJs)){
			 AjaxCallListener listener = new AjaxCallListener(); 
		     listener.onPrecondition("return "+validationJs+"();"); 
		     attributes.getAjaxCallListeners().add(listener); 
		}
		if(StringUtils.isNotBlank(confirmQuestion)){
//			AjaxCallListener ajaxCallListener = new AjaxCallListener();
//			ajaxCallListener.onPrecondition( "return confirm('"+confirmQuestion+"');" );
//			attributes.getAjaxCallListeners().add( ajaxCallListener );
			
			String defaultConfirmDesc = resolve("default.confirmAlertDesc");
			AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, confirmQuestion, defaultConfirmDesc);
			attributes.getAjaxCallListeners().add( ajaxCallListener );
			
		}
	}

	public void setConfirmQuestion(String confirmQuestion) {
		this.confirmQuestion = confirmQuestion;
	}
}
