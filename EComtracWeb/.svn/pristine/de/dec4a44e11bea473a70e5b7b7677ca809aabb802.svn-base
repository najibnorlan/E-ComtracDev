package com.ssm.llp.mod1.page;

import java.util.Date;

import com.ssm.llp.base.page.SecBasePage;

public class LlpUserProfileBasePage extends SecBasePage {
	
	
	public LlpUserProfileBasePage(String userRefNo, String idType, String idNo,String name, String userStatus, Date dtFrom, Date dtTo, Date dtApproveTo, Date dtApproveFrom, String approveChannel, String approveBy, String email) {
		add(new SearchLlpUserProfilePanel("searchLlpUserProfilePanel",userRefNo,idType,idNo,name,userStatus, dtFrom, dtTo, dtApproveTo, dtApproveFrom, approveChannel, approveBy, email));
	}
	
	public LlpUserProfileBasePage() {
		add(new SearchLlpUserProfilePanel("searchLlpUserProfilePanel"));
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.listuserprofile";
		return titleKey;
	}

}
