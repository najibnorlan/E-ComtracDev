package com.ssm.llp.base.page;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class HomeInternalPage extends BasePage{
	
	public HomeInternalPage() {
		add(new LoginInternalPanel("loginInternalPanel"));
	}

	@Override
	public String getPageTitle() {
		String titleKey = "page.title.homepage.comtrac";
		return titleKey;
	}
}
