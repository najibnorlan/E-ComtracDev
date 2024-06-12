package com.ssm.llp.base.page;

import java.io.Serializable;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class SSMJSRedirectPage extends WebPage implements Serializable, IAjaxIndicatorAware {
	
	public SSMJSRedirectPage(String jsScript) {
		Label redirectJS = new Label("redirectJS", jsScript);
		redirectJS.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		add(redirectJS);
	}

	@Override
	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}
}
