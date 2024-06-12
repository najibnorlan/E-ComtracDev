package com.ssm.llp.base.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class SSMPasswordTextField extends PasswordTextField{
	
	private boolean isReadOnly = false;
	public SSMPasswordTextField(String id) {
		super(id);
	}

	@Override
	protected void onInvalid() {
		super.onInvalid();
		add(new AttributeAppender("class", new Model("formcomponentReq"), " "));
		
		
	}
	
	public SSMPasswordTextField(String id, PropertyModel model) {
		super(id, model);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(getFeedbackMessages().size()>0){
        	add(new AttributeAppender("class", new Model("formcomponentReq"), " "));
        }
		if(isRequired()){
        	add(new AttributeAppender("class", new Model("formcomponentReqHigh"), " "));
        }
	}
	
	public AttributeModifier add(AttributeModifier attributeModifier){
		super.add(attributeModifier);
		if("readonly".equals(attributeModifier.getAttribute())){
			isReadOnly = true;
		}
		return attributeModifier;
	}
	
	public void setReadOnly(boolean isReadOnly){
		if(isReadOnly){
			super.add(new AttributeModifier("readonly", new Model("readonly")));
		}else{
			isReadOnly = false;
		}
	}

}
