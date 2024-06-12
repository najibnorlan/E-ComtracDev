package com.ssm.llp.base.wicket.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import com.ssm.llp.base.common.ParamLocale;

public abstract class SSMLink<T> extends Link<T>{
	boolean confirmAlert = false;
	
	public SSMLink(String id, IModel<T> model) {
		super(id, model);
	}
	
	public SSMLink(String id, IModel<T> model, boolean confirmAlert) {
		super(id, model);
		this.confirmAlert = confirmAlert;
		if(confirmAlert){
			addOrReplaceOnClick(ParamLocale.ACTION_CONFIRM_DELETE_RECORD);
		}
	}

	public SSMLink(String id){
		super(id);
	}
			
	public SSMLink(String id, boolean confirmAlert) {
		super(id);
		this.confirmAlert = confirmAlert;
		if(confirmAlert){
			addOrReplaceOnClick(ParamLocale.ACTION_CONFIRM_DELETE_RECORD);
		}
	}

}
