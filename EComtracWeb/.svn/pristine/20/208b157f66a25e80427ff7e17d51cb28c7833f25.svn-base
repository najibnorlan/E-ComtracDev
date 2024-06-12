package com.ssm.llp.base.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class SSMCheckBox extends CheckBox{

	private boolean isReadOnly = false;
	
	public SSMCheckBox(String id, IModel<Boolean> model) {
		super(id, model);
	}
	
	@Override
	protected boolean wantOnSelectionChangedNotifications() {
		System.out.println("WTF!!");
		return super.wantOnSelectionChangedNotifications();
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
