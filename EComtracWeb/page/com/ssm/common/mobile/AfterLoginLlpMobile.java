package com.ssm.common.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLink;

public class AfterLoginLlpMobile extends SecBasePageMobile{
	public AfterLoginLlpMobile() {
//		SSMLink myRegistrationList = new SSMLink("myRegistrationList") {
//			@Override
//			public void onClick() {
//				setResponsePage(ListLlpRegistration.class);
//			}
//		};
//		
//		add(myRegistrationList);
//		
//		SSMLink myReserveList = new SSMLink("myReserveList") {
//			@Override
//			public void onClick() {
//				setResponsePage(ViewListLlpReservedNames.class);
//			}
//		};
//		
//		add(myReserveList);
//		
//		SSMLink myPaymentList = new SSMLink("myPaymentList") {
//			@Override
//			public void onClick() {
//				setResponsePage(ListPaymentTransactionPage.class);
//			}
//		};
//		
//		add(myPaymentList);
//		
//		SSMLink generalReg = new SSMLink("generalReg") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null , null, Parameter.YES_NO_yes));
//			}
//		};
//		
//		add(generalReg);
//		
//		SSMLink CSReg = new SSMLink("CSReg") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, Parameter.PROF_BODY_TYPE_cs, null, Parameter.YES_NO_yes));
//			}
//		};
//		
//		add(CSReg);
//		
//		SSMLink CAReg = new SSMLink("CAReg") {
//			@Override
//			public void onClick() {
//				PageParameters params = new PageParameters();
//				params.set("regType", Parameter.LLP_REG_TYPE_local);
//				params.set("profBodyType", Parameter.PROF_BODY_TYPE_ca);
//				params.set("isProceedToLLP", Parameter.YES_NO_yes);
//				setResponsePage(new EditLlpReservedNamePage(params));
//				
//			}
//		};
//		
//		add(CAReg);
//		
//		SSMLink LawReg = new SSMLink("LawReg") {
//			@Override
//			public void onClick() {
//				PageParameters params = new PageParameters();
//				params.set("regType", Parameter.LLP_REG_TYPE_local);
//				params.set("profBodyType", Parameter.PROF_BODY_TYPE_law);
//				params.set("isProceedToLLP", Parameter.YES_NO_yes);
//				setResponsePage(new EditLlpReservedNamePage(params));
//				
//			}
//		};
//		
//		add(LawReg);
//		
//		SSMLink generalReserve = new SSMLink("generalReserve") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null, null, Parameter.YES_NO_no));
//				
//			}
//		};
//		
//		add(generalReserve);
//		
//		SSMLink CSReserve = new SSMLink("CSReserve") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, Parameter.PROF_BODY_TYPE_cs, null, Parameter.YES_NO_no));
//				
//			}
//		};
//		
//		add(CSReserve);
//		
//		SSMLink viewUserProfile = new SSMLink("viewUserProfile") {
//			@Override
//			public void onClick() {
//				setResponsePage(ViewLlpUserProfilePage.class);
//				
//			}
//		};
//		
//		add(viewUserProfile);
//		
//		SSMLink changePwd = new SSMLink("changePwd") {
//			@Override
//			public void onClick() {
//				setResponsePage(EditLlpUserProfilePasswordPage.class);
//				
//			}
//		};
//		
//		add(changePwd);
	}
	
	
	
	

	
	public String getPageTitle() {
		String titleKey = "page.title.homepage";
		return titleKey;
	}
}
