package com.ssm.llp.base.wicket.component;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.ssm.llp.base.utils.WicketUtils;

public abstract class SSMAjaxLink<T> extends AjaxLink<T>{
	private boolean confirmAlert;
	private String labelKey;
	private String confirmQuestion;
	
	public SSMAjaxLink(String id) {
		super(id);
		confirmAlert = false;
	}
	public SSMAjaxLink(String id, IModel<T> model) {
		super(id, model);
		confirmAlert = false;
	}
	
	public SSMAjaxLink(String id, boolean confirmAlert) {
		super(id);
		this.confirmAlert = confirmAlert;
	}
	
	public SSMAjaxLink(String id, IModel<T> model, boolean confirmAlert) {
		super(id, model);
		this.confirmAlert = confirmAlert;
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes )
	{
		super.updateAjaxAttributes( attributes );
		if(StringUtils.isNotBlank(confirmQuestion)){
//			AjaxCallListener ajaxCallListener = new AjaxCallListener();
//			ajaxCallListener.onPrecondition( "return confirm('"+confirmQuestion+"');" );
			String defaultConfirmDesc = resolve("default.confirmAlertDesc");
			AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, confirmQuestion, defaultConfirmDesc);
			attributes.getAjaxCallListeners().add( ajaxCallListener );
		}else{
			if(confirmAlert){
//				AjaxCallListener ajaxCallListener = new AjaxCallListener();
//				ajaxCallListener.onPrecondition( "return confirm('Confirm ?');" );
				String defaultConfirmTitle = resolve("default.confirmAlertTitle");
				String defaultConfirmDesc = resolve("default.confirmAlertDesc");
				
				AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, defaultConfirmTitle, defaultConfirmDesc);
				attributes.getAjaxCallListeners().add( ajaxCallListener );
			}
		}
		
	}
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(StringUtils.isNotBlank(labelKey)){
        	String value = resolve(labelKey);
    		String lblId = getId()+"Label";
    		if(get(lblId)==null){
    			SSMLabel ssmLabel = new SSMLabel( lblId, value);
    			add(ssmLabel);
    		}
        }
	}
	
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
	public void setConfirmQuestion(String confirmQuestion) {
		this.confirmQuestion = confirmQuestion;
	}


}
