package com.ssm.llp.base.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class SSMNumberTextField<N extends Number & Comparable<N>> extends NumberTextField<N>{

	private boolean isReadOnly = false;
	
	public SSMNumberTextField(String id) {
		super(id);
		setRequired(false);
	}
	
	public SSMNumberTextField(String id, IModel<N> model) {
		super(id, model);
		setRequired(false);
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
