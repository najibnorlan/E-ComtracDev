package com.ssm.llp.mod1.page;

import com.ssm.llp.base.page.SecBasePage;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NameSearchPage extends SecBasePage {

	public NameSearchPage(String regType,String profBodyType, String conversionType, String isProceedToLLP) {
		add(new SearchLlpName("searchLlpName", null, regType, profBodyType, conversionType, isProceedToLLP));
	}

}