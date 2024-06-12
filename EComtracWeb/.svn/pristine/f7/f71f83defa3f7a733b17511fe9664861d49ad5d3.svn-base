package com.ssm.llp.mod1.page;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.CompoundValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class NumberValidator extends Behavior implements IValidator<String>{

	
	private static final long serialVersionUID = 1L;
	private final Pattern numberPattern;
	private String componentId;

	private final String NUMBER_PATTERN  = "[0-9]+";

//	public NumberValidator() {
//		//add(StringValidator.lengthBetween(5, 40));
//		add(new PatternValidator("[0-9]+"));
//	}
	public NumberValidator() {
		numberPattern = Pattern.compile(NUMBER_PATTERN);
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		final String password = validatable.getValue();
		if (numberPattern.matcher(password).matches() == false) {
			error(validatable, "must.number");
		}
	}
	
	@Override
	public void beforeRender(Component component) {
		super.beforeRender(component);
		if(component!=null ){
			componentId = component.getId();
			if(component instanceof FormComponent){
				FormComponent formComponent = (FormComponent) component;
				if(formComponent.getLabel()!=null){
					componentId = formComponent.getLabel().getObject();
				}
			}
		}
	}
	private void error(IValidatable<String> validatable, String errorKey) {
		ValidationError error = new ValidationError();
		error.addKey(getClass().getSimpleName() + "." + errorKey);
		ValueMap map = new ValueMap();
		if(StringUtils.isNotBlank(componentId)){
			map.put("param0", componentId);
		}else{
			map.put("param0", "");
		}
		
		error.setVariables(map);
		validatable.error(error);
	}


}
