package com.ssm.llp.base.wicket.component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;

public class SSMLabel extends Label{

	SimpleDateFormat defaultDf = new SimpleDateFormat("dd/MM/yyyy");
	String decimalFormat;
	
	public SSMLabel(String id) {
		super(id);
		initLabel();
	}

	private void initLabel() {
		setEscapeModelStrings(false);
	}

	public SSMLabel(String id, String value, String codeType) {
		super(id,value);
		initLabel();
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		String strValue = parametersService.findByCodeTypeValue(codeType, value);
		setDefaultModelObject(strValue);
	}
	
	public SSMLabel(String id, double value) {
		this(id, value, "#0.00");
		initLabel();
	}
	
	public SSMLabel(String id, double value, String decimalFormat) {
		super(id,"");
		initLabel();
		DecimalFormat df = new DecimalFormat(decimalFormat);
		
		setDefaultModelObject(df.format(value));
	}
	
	public SSMLabel(String id, String value) {
		super(id,value);
		initLabel();
	}
	
	public SSMLabel(String id, Long value) {
		super(id,String.valueOf(value));
		initLabel();
	}
	
	public SSMLabel(String id, long value) {
		super(id,String.valueOf(value));
		initLabel();
	}
	
	public SSMLabel(String id, Date value) {
		this(id,value,null);
		initLabel();
	}
	
	public SSMLabel(String id, Date value, String dateFormat) {
		super(id,"");
		initLabel();
		if(value!=null){
			String dtStr = "";
			if(StringUtils.isBlank(dateFormat)){
				dtStr = defaultDf.format(value);
			}else{
				SimpleDateFormat df= new SimpleDateFormat(dateFormat);
				dtStr = df.format(value);
			}
			setDefaultModelObject(dtStr);
		}
	}

	public SSMLabel(String welcomeMsgId, StringResourceModel labelModel) {
		super(welcomeMsgId, labelModel);
	}
	
	public SSMLabel(String id, Object objValue) {
		super(id,"");
		initLabel();
		
		if(objValue!=null){
			if(objValue instanceof Date){
				SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				setDefaultModelObject(df.format(objValue));
			}else{
				setDefaultModelObject(String.valueOf(objValue));
			}
		}
		
		
		
	}

	public SSMLabel addStyle(String style) {
		AttributeModifier styleAM = new AttributeModifier( "style", style);
		try {
			remove(styleAM);//avoid duplication
		} catch (Exception e) {
		}
		add(styleAM);
		return this;
	}
	
}
