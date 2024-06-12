package com.ssm.llp.base.wicket.component;

import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.IValueMap;

import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;

@SuppressWarnings({ "unchecked", "unchecked", "serial","rawtypes" })
public class SSMRadioChoice extends RadioChoice {

	private boolean isReadOnly = false;
	public SSMRadioChoice(String id, IModel choices, String codeType) {
		super(id, choices);
		setSuffix("");
		populateOptions(codeType);
	}



	public SSMRadioChoice(String id, String codeType) {
		super(id);
		setSuffix("");
		populateOptions(codeType);
	}
	
	

	private void populateOptions(String codeType){
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		List listCombo = parametersService.findByActiveCodeType(codeType);
		this.setChoices(listCombo);
		IChoiceRenderer renderer = new IChoiceRenderer<Object>() {
			@Override
			public Object getDisplayValue(Object arg0) {
				
				if(arg0 instanceof LlpParameters){
					return ((LlpParameters)arg0).getCodeDesc();
				}
				return arg0;
			}
			@Override
			public String getIdValue(Object arg0, int arg1) {
				if(arg0 instanceof LlpParameters){
					return ((LlpParameters)arg0).getCode();
				}
				return arg0.toString();
			}
			
		};
		this.setChoiceRenderer(renderer);
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
