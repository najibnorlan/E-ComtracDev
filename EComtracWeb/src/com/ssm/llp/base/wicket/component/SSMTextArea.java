package com.ssm.llp.base.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class SSMTextArea<T> extends TextArea<T>{
	
	private boolean isReadOnly = false;
	private boolean isUpperCase = true;
	

	public SSMTextArea(String id) {
		this(id, false);
	}
	
	public SSMTextArea(String id, boolean isReq) {
		super(id);
		setRequired(isReq);
	}
	
	public SSMTextArea(String id, IModel<T> model) {
		this(id, model, false);
	}	
	public SSMTextArea(String id, IModel<T> model, boolean isReq) {
		super(id, model);
		setRequired(isReq);
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
		if(isReadOnly){
			super.add(new AttributeModifier("readonly", new Model("readonly")));
		}
		if(isUpperCase){
        	add(new AttributeAppender("style", new Model("text-transform:uppercase;"), " "));
        	add(new AttributeAppender("onblur", new Model("this.value=this.value.toUpperCase();"), " "));
        }
	}
	
	public AttributeModifier add(AttributeModifier attributeModifier){
		if("readonly".equals(attributeModifier.getAttribute())){
			isReadOnly = true;
		}else{
			super.add(attributeModifier);
		}
		return attributeModifier;
	}
	
	public void setReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}
	
	public void setUpperCase(boolean isUpperCase) {
		this.isUpperCase = isUpperCase;
	}
}
