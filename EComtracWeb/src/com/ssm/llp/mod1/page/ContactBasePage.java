package com.ssm.llp.mod1.page;

import com.ssm.llp.base.page.BasePage;

public class ContactBasePage extends BasePage {
	public ContactBasePage() {
		// add(new StyleSheetReference("stylesheet", ContactBasePage.class,
		// "styles.css"));
		add(new SearchPanel("searchPanel"));
		// add(new SSMLabel("label2", "This is in the subclass Page2"));
	}

	@Override
	public String getPageTitle() {
		return null;
	}
}
