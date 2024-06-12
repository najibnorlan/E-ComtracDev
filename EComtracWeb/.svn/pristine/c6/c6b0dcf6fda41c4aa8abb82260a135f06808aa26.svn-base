package com.ssm.llp.base.wicket.component;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class SSMDateTextField extends DateTextField {

	private DatePicker datePicker;
	private boolean isReadOnly = false;
	
	
	public SSMDateTextField(String id, String datePattern, boolean isReq) {
		this(id, datePattern, true , isReq);
	}	
	
	public SSMDateTextField(String id, String datePattern) {
		this(id, datePattern, true , false);
	}
	
	public SSMDateTextField(String id) {
		this(id, "dd-MMM-yyyy", true, false);
	}

	public SSMDateTextField(String id, boolean isReq) {
		this(id, "dd-MMM-yyyy", true, isReq);
	}

	public SSMDateTextField(String id, String datePattern, boolean withDatePicker, boolean isReq) {
		super(id, datePattern);
		setRequired(isReq);
		
		datePicker = new DatePicker()
        {
            @Override
            protected String getAdditionalJavaScript()
            {
                return "${calendar}.cfg.setProperty(\"navigator\",true,false); ${calendar}.render();";
            }
        };
        datePicker.setShowOnFieldClick(false);
        datePicker.setAutoHide(true);
        add(datePicker);
	}
	
	public SSMDateTextField(String id, IModel<Date> model) {
		this(id, model, "dd-MMM-yyyy", false);
	}
	
	public SSMDateTextField(String id, IModel<Date> model, String datePattern) {
		this(id, model, datePattern, false);
	}
	public SSMDateTextField(String id, IModel<Date> model, String datePattern, boolean isReq) {
		super(id,model,datePattern);
		setRequired(isReq);
		datePicker = new DatePicker()
        {
            @Override
            protected String getAdditionalJavaScript()
            {
                return "${calendar}.cfg.setProperty(\"navigator\",true,false); ${calendar}.render();";
            }
        };
        datePicker.setShowOnFieldClick(false);
        datePicker.setAutoHide(true);
        add(datePicker);
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
        	try {
        		remove(datePicker);
			} catch (Exception e) {
				
			}
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
