package com.ssm.llp.base.wicket.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.ConversionException;
import org.dom4j.rule.Mode;

public class SSMTextField<T> extends TextField<T>{

	private boolean isReadOnly = false;
	private boolean isUpperCase = true;
	private String labelKey;
	private Integer maxLength = null ;
	
	public SSMTextField(String id) {
		super(id);
	}
	
	private void initMaxLength() {
		maxLength=0;
		try {
			Class cl = getForm().getDefaultModelObject().getClass();
			String id = StringUtils.substring(getId(), 0,1).toUpperCase()+StringUtils.substring(getId(), 1);
			Integer length = cl.getDeclaredMethod("get"+id, null).getAnnotation(Column.class).length();
			if(length!=null){
				maxLength = length;
			}
		} catch (Exception e) {
		}
	}

	public SSMTextField(String id, Model model) {
		super(id, model);
	}
	
	public SSMTextField(String id, PropertyModel propertyModel) {
		super(id,propertyModel);
	}
	
	public SSMTextField(String id, boolean isReq) {
		super(id);
		setRequired(isReq);
	}
	
	public SSMTextField(String id, PropertyModel propertyModel, boolean isReq) {
		super(id,propertyModel);
		setRequired(isReq);
	}
	
	public void setMaxLength(int length) {
		maxLength = length;
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
		}else{
			List<AttributeModifier> list = super.getBehaviors(AttributeModifier.class);
	        List<AttributeModifier> list2 = new ArrayList<AttributeModifier>();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getAttribute().equals("readonly")){
					list2.add(list.get(i));
				}
			}
			for (int i = 0; i < list2.size(); i++) {
				try {
					super.remove(list2.get(i));
				} catch (Exception e) {
				}
			}
		}
		
        if(isUpperCase){
        	add(new AttributeAppender("style", new Model("text-transform:uppercase;"), " "));
        	add(new AttributeAppender("onblur", new Model("this.value=this.value.toUpperCase();"), " "));
        }
        
        if(maxLength==null){
        	initMaxLength();
        }
        if(maxLength>0){
        	add(new AttributeModifier("maxlength", new Model(maxLength) ));
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
		this.isReadOnly = isReadOnly;
	}
	
	public void setUpperCase(boolean isUpperCase) {
		this.isUpperCase = isUpperCase;
	}
	
	public void setLabel(String labelKey) {
		this.labelKey = labelKey;
	}
	
}
