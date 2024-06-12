package com.ssm.common.mobile;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;

public class ESearchMainPageMobileBasePage extends BasePageMobile {
	
	
	public ESearchMainPageMobileBasePage(String entityType, String entityNo) {
		if(StringUtils.isBlank(entityType)){
    		entityType=Parameter.ENTITY_TYPE_ROB;
    	}
		add(new ESearchMobilePanel("eSearchMobilePanel",entityType,entityNo));
	}
	
	public ESearchMainPageMobileBasePage() {
		add(new ESearchMobilePanel("eSearchMobilePanel"));
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.esearch";
		return titleKey;
	}

	@Override
	public String getLogoImage() {
		return "images/esearch.png";
	}

}
