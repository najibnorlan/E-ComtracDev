package com.ssm.llp.base.wicket.component;

import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.model.IModel;

public abstract class SSMAjaxCheckBox extends AjaxCheckBox{

	public SSMAjaxCheckBox(String id) {
		this(id, false);
	}
	
	public SSMAjaxCheckBox(String id,boolean isReq) {
		super(id);
		setRequired(isReq);
	}
	
	public SSMAjaxCheckBox(final String id, final IModel<Boolean> model)
	{
		this(id, model, false);
	}
	
	public SSMAjaxCheckBox(final String id, final IModel<Boolean> model, boolean isReq)
	{
		super(id, model);
		setRequired(isReq);
	}

}
