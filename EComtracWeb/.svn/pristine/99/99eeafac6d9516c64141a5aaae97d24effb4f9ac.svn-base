package com.ssm.llp.mod1.page;

import org.apache.wicket.validation.CompoundValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

public class UsernameValidator extends CompoundValidator<String> {


	private static final long serialVersionUID = 1L;
	 
	public UsernameValidator() {
 
		add(StringValidator.lengthBetween(5, 40));
		add(new PatternValidator("[A-Z0-9_-]+"));
		
 
	}


}
