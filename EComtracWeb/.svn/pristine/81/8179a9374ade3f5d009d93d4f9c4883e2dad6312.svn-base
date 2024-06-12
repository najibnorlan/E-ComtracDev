package com.ssm.common.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.cooldatasoft.common.MenuItem;
import com.cooldatasoft.horizontal.dropdown.multiLevelCss.MultiLevelCssMenu;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.page.HomeInternalPage;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.LLPFileAttachmentPage;
import com.ssm.llp.base.page.LlpLocaleMessagePage;
import com.ssm.llp.base.page.LlpParameterPage;
import com.ssm.llp.base.page.PrivacyPolicy;
import com.ssm.llp.base.page.SecurityPolicy;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.page.AfterLoginInternal;
import com.ssm.llp.mod1.page.AfterLoginLlp;
import com.ssm.llp.mod1.page.EditLlpUserProfilePasswordPage;
import com.ssm.llp.mod1.page.ListLlpEmailLogPage;
import com.ssm.llp.mod1.page.ListLlpSpecialKeyword;
import com.ssm.llp.mod1.page.ListLlpUserProfilePage;
import com.ssm.llp.mod1.page.ListPaymentTransactionPage;
import com.ssm.llp.mod1.page.LlpPaymentFeePage;
import com.ssm.llp.mod1.page.ViewLlpUserProfilePage;
import com.ssm.llp.page.robRenewal.ListRobRenewalPage;
import com.ssm.llp.page.robRenewal.ListRobRenewalTransactionsPage;
import com.ssm.llp.page.robRenewal.SearchBizForRenew;

public abstract class BasePageMobile extends WebPage implements Serializable {

	@SpringBean(name = "LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	private WebMarkupContainer wmc;
	private FeedbackPanel feedbackPanel;
	public BasePageMobile() {
		WebSession.get().setAttribute("isMobile", Parameter.YES_NO_yes);
		
		String myPageTitle = "";
		if (getPageTitle() != null)
			myPageTitle = getPageTitle();
		else
			myPageTitle = "page.title.default";
		add(new SSMLabel("pageTitle", new StringResourceModel(myPageTitle, this, null)));
		
		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_deployment_type);
//		add(new Image("banner", new ContextRelativeResource("images/Banner"+depType+"Mobile.jpg")));
//		add(new Image("logoImg", new ContextRelativeResource(getLogoImage())));
		add(new Image("logoImg", new Model<String>(getLogoImage())));
		Link linkSignOut = new Link("signout") {
			@Override
			public void onClick() {
				getSession().invalidate();
				LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment().getAttribute("UserLog");
				llpUserLog.setLogoutTime(new Date());
				llpUserLogService.update(llpUserLog);

				setResponsePage(HomePageMobile.class);
			}
		};
//		wmc = new WebMarkupContainer("isLogin");
//		wmc.add(linkSignOut);
		linkSignOut.setVisible(false);
		add(linkSignOut);
		
//		System.out.println(UserEnvironmentHelper.getUserenvironment());
		if (UserEnvironmentHelper.getUserenvironment() != null) {
			linkSignOut.setVisible(true);
		}
		BookmarkablePageLink ezBizhomePageMobile = new BookmarkablePageLink("ezBizhomePageMobile", HomePageMobile.class);
		add(ezBizhomePageMobile);
		
		BookmarkablePageLink eSearchMainPageMobile = new BookmarkablePageLink("eSearchMainPageMobile", ESearchMainPageMobile.class);
		add(eSearchMainPageMobile);
		
		BookmarkablePageLink rssFeedMainPageMobile = new BookmarkablePageLink("rssFeedMainPageMobile", RssFeedMainPageMobile.class);
		add(rssFeedMainPageMobile);
		
		BookmarkablePageLink linkOwnBizRenewal = new BookmarkablePageLink("ownBizRenewal", ListRobRenewalPageMobile.class);
		add(linkOwnBizRenewal);
		
		BookmarkablePageLink myRenewalTransaction = new BookmarkablePageLink("myRenewalTransaction", ListRobRenewalTransactionsPageMobile.class);
		add(myRenewalTransaction);
	}
	public void BasePageMobile1() {
		
		// Ni tuk page title
		String myPageTitle = "";
		if (getPageTitle() != null)
			myPageTitle = getPageTitle();
		else
			myPageTitle = "page.title.default";

		add(new SSMLabel("pageTitle", new StringResourceModel(myPageTitle, this, null)));

		// Ni tuk footer
		add(new BookmarkablePageLink("privacyPolicy", PrivacyPolicy.class));
		add(new BookmarkablePageLink("securityPolicy", SecurityPolicy.class));

//		add(new Link("english") {
//			@Override
//			public void onClick() {
//				getSession().setLocale(Locale.ENGLISH);
//			}
//		});
//
//		add(new Link("malay") {
//			@Override
//			public void onClick() {
//				getSession().setLocale(new Locale("ms", "MY"));
//			}
//		});
		String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_deployment_type);
		add(new Image("banner", new ContextRelativeResource("images/Banner"+depType+".jpg")));
		
		Link linkSignOut = new Link("signout") {
			@Override
			public void onClick() {
				getSession().invalidate();
				LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment().getAttribute("UserLog");
				llpUserLog.setLogoutTime(new Date());
				llpUserLogService.update(llpUserLog);

				setResponsePage(HomePage.class);
			}
		};

		wmc = new WebMarkupContainer("isLogin");
		wmc.add(linkSignOut);

		add(wmc);
		setIfLoginPart();

		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		List<MenuItem> primaryMenuList = buildMenu();
		add(new MultiLevelCssMenu("multiLevelCssMenu", primaryMenuList));
	}
	
	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public void setIfLoginPart() {
		String welcomeMsgId = "base.welcome";
		ValueMap map = new ValueMap();
		if (UserEnvironmentHelper.getUserenvironment() == null) {
			map.put("fullName", "");
			SSMLabel welcomeLbl = new SSMLabel(welcomeMsgId, "");
			wmc.add(welcomeLbl);
			wmc.setVisible(false);
		} else {
			map.put("fullName", UserEnvironmentHelper.getUserenvironment().getFullName());
			StringResourceModel labelModel = new StringResourceModel(welcomeMsgId, this, new Model<ValueMap>(map));
			SSMLabel welcomeLbl = new SSMLabel(welcomeMsgId, labelModel);
			wmc.add(welcomeLbl);
			wmc.setVisible(true);
		}

	}

	public List<MenuItem> buildMenu() {
		List<MenuItem> primaryMenuList = new ArrayList<MenuItem>();
		
		Class homeClass = HomePage.class;
		if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
			homeClass = AfterLoginLlp.class;
		}
		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
			homeClass = AfterLoginInternal.class;
		}
		MenuItem primaryMenu1 = new MenuItem("Home", homeClass);
		primaryMenuList.add(primaryMenu1);
		
		// MenuItem menuTestPage = new MenuItem("TestPage");
		// primaryMenuList.add(menuTestPage);
		// MenuItem menuListContact = new
		// MenuItem("Contact",ListContacts.class);
		// primaryMenuList.add(menuListContact);
		//
		//
		// List<MenuItem> subMenuTestPage= new ArrayList<MenuItem>();
		// MenuItem menuPage1 = new MenuItem("Page1", Page1.class);
		// MenuItem menuPage2 = new MenuItem("Page2", Page2.class);
		// subMenuTestPage.add(menuPage1);
		// subMenuTestPage.add(menuPage2);
		// menuTestPage.setSubMenuItemList(subMenuTestPage);
		if (UserEnvironmentHelper.getUserenvironment() == null) {
			MenuItem menuSignInInternal = new MenuItem("Internal", HomeInternalPage.class);
			primaryMenuList.add(menuSignInInternal);
		}

		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
			generateMenuInternal(primaryMenuList);
		}
		if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
			generateMenuLlpUser(primaryMenuList);
		}

		return primaryMenuList;

	}

	private void generateMenuLlpUser(List<MenuItem> primaryMenuList) {
		
		//ROB MENU
		//menu.myBiz.title
		MenuItem menuBizReg= new MenuItem(getLocaleMsg("menu.myBiz.services"));//"MyBiz Services"
		primaryMenuList.add(menuBizReg);
		
		List<MenuItem> subMenuBizReg = new ArrayList<MenuItem>();
		MenuItem subMenuBizRegRenewalForOwner = new MenuItem(getLocaleMsg("menu.myBiz.ownBizRenewal"), ListRobRenewalPage.class);
		subMenuBizReg.add(subMenuBizRegRenewalForOwner);
		
		MenuItem subMenuBizRegRenewal3rdParty = new MenuItem(getLocaleMsg("menu.myBiz.selfBizRenewal"), SearchBizForRenew.class);
		subMenuBizReg.add(subMenuBizRegRenewal3rdParty);
		
		MenuItem myRenewalTransaction = new MenuItem(getLocaleMsg("menu.myBiz.myRenewalTransaction"), ListRobRenewalTransactionsPage.class);
		subMenuBizReg.add(myRenewalTransaction);
		
		menuBizReg.setSubMenuItemList(subMenuBizReg);
		
		//*********************************************************************************************************************************
		// LLP My Payment
		MenuItem menuMyPayment = new MenuItem("My Payment");
		primaryMenuList.add(menuMyPayment);
		
		List<MenuItem> subMenuMyLlp = new ArrayList<MenuItem>();
		MenuItem menuMyPaymentHistory = new MenuItem("My Payment History", ListPaymentTransactionPage.class);
		subMenuMyLlp.add(menuMyPaymentHistory);
		menuMyPayment.setSubMenuItemList(subMenuMyLlp);
		
		//*********************************************************************************************************************************
		// LLP User Profile
		MenuItem menuLlpUserProfile = new MenuItem("User Profile");
		primaryMenuList.add(menuLlpUserProfile);

		List<MenuItem> subMenuUserProfile = new ArrayList<MenuItem>();
		MenuItem menuUserProfile = new MenuItem("View User Profile", ViewLlpUserProfilePage.class);
		MenuItem menuChangePasswordUserProfile = new MenuItem("Change Password", EditLlpUserProfilePasswordPage.class);
		subMenuUserProfile.add(menuUserProfile);
		subMenuUserProfile.add(menuChangePasswordUserProfile);
		menuLlpUserProfile.setSubMenuItemList(subMenuUserProfile);

	}

	public String getLocaleMsg(String key){
		return new StringResourceModel(key, this, null).getString();
	}
	private void generateMenuInternal(List<MenuItem> primaryMenuList) {
		//USER REGISTRATION
		MenuItem menuLlpUserRegistration = new MenuItem("User Registration", ListLlpUserProfilePage.class);
		primaryMenuList.add(menuLlpUserRegistration);
//		
//		//*********************************************************************************************************************************
//		
//		//LLP REGISTRATION
//		MenuItem menuLlpRegistration = new MenuItem("LLP Registration", ListLlpRegistration.class);
//		primaryMenuList.add(menuLlpRegistration);
//		
//		//*********************************************************************************************************************************
//		
//		//LLP RESERVED NAME
//		//MenuItem menuLlpReservedName = new MenuItem("LLP Reserved Name");
//		//primaryMenuList.add(menuLlpReservedName);
//		
//		//Sub Menu Llp Reserved Name
//		List<MenuItem> subMenuLlpReservedName = new ArrayList<MenuItem>();
//		
//		Link<Void> linkGeneralNameSearch = new Link<Void>("linkNonProfesional") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null, null, Parameter.YES_NO_no));
//			}
//		};		
//		
//		
//		
//		//MenuItem menuReservedNameNameSearch = new MenuItem("Name Search", linkSearchNameOfficer);
//		//MenuItem menuReservedNameList = new MenuItem("LLP Reserved Name Listing", ListLlpReservedNamesOfficer.class);
//		//MenuItem menuForeignRegistration = new MenuItem("Foreign Registration", linkForeignRegistration);
//		
//		//subMenuLlpReservedName.add(menuReservedNameNameSearch);
//		//subMenuLlpReservedName.add(menuReservedNameList);
//		//subMenuLlpReservedName.add(menuForeignRegistration);
//		//menuLlpReservedName.setSubMenuItemList(subMenuLlpReservedName);
//		
//		//*********************************************************************************************************************************
//
//		//CONVERSION
//		MenuItem menuConversion = new MenuItem("LLP Conversion");
//		primaryMenuList.add(menuConversion);
//		
//		//Sub Menu Conversion
//		List<MenuItem> subMenuConversion = new ArrayList<MenuItem>();
//		
//		Link<Void> linkConvertFromROB = new Link<Void>("linkConvertFromROB") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null, Parameter.CONVERSION_TYPE_rob, Parameter.YES_NO_no));
//			}
//		};
//		MenuItem menuConversionROB = new MenuItem("Conversion From Conventional Partnership", linkConvertFromROB);
//		
//		Link<Void> linkConvertFromROC = new Link<Void>("linkConvertFromROC") {
//			@Override
//			public void onClick() {
//				setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null, Parameter.CONVERSION_TYPE_roc, Parameter.YES_NO_no));
//			}
//		};
//		MenuItem menuConversionROC = new MenuItem("Conversion From Private Company", linkConvertFromROC);
//		
//		subMenuConversion.add(menuConversionROB);
//		subMenuConversion.add(menuConversionROC);
//		menuConversion.setSubMenuItemList(subMenuConversion);
//		
//		//*********************************************RESERVED NAME REGISTRATION INTERNAL**************************************************************************
//		//RESERVED NAME REGISTRATION
//				MenuItem menuReservedName = new MenuItem("Name Reservation");
//				primaryMenuList.add(menuReservedName);
//				
//				//Sub Menu Reserved Name Registration
//				List<MenuItem> subMenuReservedName = new ArrayList<MenuItem>();
//				//Sub Menu Reserved Name Registration : General Registration
//				Link<Void> linkGeneralReservedName = new Link<Void>("linkNonProfesional") {
//					@Override
//					public void onClick() {
//						setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, null, null, Parameter.YES_NO_no));
//					}
//				};
//				MenuItem menuGeneralReservedName = new MenuItem("General Registration", linkGeneralReservedName);
//				
//				//Sub Menu Reserved Name : Professional Practice
//				MenuItem menuProfessionalPracticeReservedName = new MenuItem("Professional Practice Registration");
//				
//				//Sub Menu Reserved Name : Sub Menu Professional Practice
//				List<MenuItem> subMenuProfessionalPracticeReservedName = new ArrayList<MenuItem>();
//				//CS
//				Link<Void> linkMenuProfesionalCsReservedName = new Link<Void>("linkMenuProfesionalCs") {
//					@Override
//					public void onClick() {
//						setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_local, Parameter.PROF_BODY_TYPE_cs, null, Parameter.YES_NO_no));
//					}
//				};
//				MenuItem menuProfessionalPracticeCSReservedName = new MenuItem("Company Secretary", linkMenuProfesionalCsReservedName);
//				//CA
//				Link<Void> linkMenuProfesionalCaReservedName = new Link<Void>("linkCA") {
//					@Override
//					public void onClick() {
//						PageParameters params = new PageParameters();
//						params.set("regType", Parameter.LLP_REG_TYPE_local);
//						params.set("profBodyType", Parameter.PROF_BODY_TYPE_ca);
//						params.set("isProceedToLLP", Parameter.YES_NO_no);
//						setResponsePage(new EditLlpReservedNamePage(params));
//					}
//				};
//				MenuItem menuProfessionalPracticeCAReservedName = new MenuItem("Chartered Accountant", linkMenuProfesionalCaReservedName);
//				//LAW
//				Link<Void> linkMenuProfesionalLawReservedName = new Link<Void>("linkLaw") {
//					@Override
//					public void onClick() {
//						PageParameters params = new PageParameters();
//						params.set("regType", Parameter.LLP_REG_TYPE_local);
//						params.set("profBodyType", Parameter.PROF_BODY_TYPE_law);
//						params.set("isProceedToLLP", Parameter.YES_NO_no);
//						setResponsePage(new EditLlpReservedNamePage(params));
//					}
//				};
//				Link<Void> linkForeignRegistration = new Link<Void>("linkForeignRegistration") {
//					@Override
//					public void onClick() {
//						setResponsePage(new NameSearchPage(Parameter.LLP_REG_TYPE_foreign, null, null, Parameter.YES_NO_no));
//					}
//				};
//				MenuItem menuProfessionalPracticeLAWReservedName = new MenuItem("Advocates & Solicitor", linkMenuProfesionalLawReservedName);
//				
//				MenuItem menuReservedNameList = new MenuItem("LLP Reserved Name Listing", ListLlpReservedNamesOfficer.class);
//				MenuItem menuForeignRegistration = new MenuItem("Foreign Registration", linkForeignRegistration);
//				
//				subMenuProfessionalPracticeReservedName.add(menuProfessionalPracticeCSReservedName);
//				//subMenuProfessionalPracticeReservedName.add(menuProfessionalPracticeCAReservedName);
//				//subMenuProfessionalPracticeReservedName.add(menuProfessionalPracticeLAWReservedName);
//				menuProfessionalPracticeReservedName.setSubMenuItemList(subMenuProfessionalPracticeReservedName);
//				
//				subMenuReservedName.add(menuGeneralReservedName);
//				subMenuReservedName.add(menuProfessionalPracticeReservedName);
//				subMenuReservedName.add(menuForeignRegistration);
//				subMenuReservedName.add(menuReservedNameList);
//				menuReservedName.setSubMenuItemList(subMenuReservedName);
//		
//		
//		//*********************************************************************************************************************************
//		
//		// shrzul - LLP Product Info
////		MenuItem menuLlpProductInfo = new MenuItem("LLP Profile", LlpProductBasePage.class);
////		primaryMenuList.add(menuLlpProductInfo);
//		
//		// LLP Supply Info
//		MenuItem menuLlpSupplyinfo = new MenuItem("Supply Info");
//		primaryMenuList.add(menuLlpSupplyinfo);
//
//		List<MenuItem> subMenuSupplyinfo = new ArrayList<MenuItem>();
//		MenuItem menuBuySupplyInfo = new MenuItem("LLP Profile", LlpProductBasePage.class);
////		MenuItem menuListSupplyInfo = new MenuItem("List Supply Info Transaction", ListLlpSupplyInfoHdrPage.class);
//		subMenuSupplyinfo.add(menuBuySupplyInfo);
////		subMenuSupplyinfo.add(menuListSupplyInfo);
//		menuLlpSupplyinfo.setSubMenuItemList(subMenuSupplyinfo);
//		
//		//*********************************************************************************************************************************
		
		//PAYMENT
		MenuItem menuPayment = new MenuItem("Payment");
		primaryMenuList.add(menuPayment);

		List<MenuItem> subMenuPayment = new ArrayList<MenuItem>();
		MenuItem subMenuPaymentTransaction = new MenuItem("Payment Transaction Report", ListPaymentTransactionPage.class);
		subMenuPayment.add(subMenuPaymentTransaction);
		menuPayment.setSubMenuItemList(subMenuPayment);

		//*********************************************************************************************************************************
		
		//Control Panel
		MenuItem menuPCtrlPanel = new MenuItem("Control Panel");
		primaryMenuList.add(menuPCtrlPanel);
		
		List<MenuItem> subMenuCtrlPanel = new ArrayList<MenuItem>();
		MenuItem subMenuCtrlPanelLocale = new MenuItem("LocaleMessage", LlpLocaleMessagePage.class);
		subMenuCtrlPanel.add(subMenuCtrlPanelLocale);
		
		MenuItem subMenuPaymentConfiguration = new MenuItem("Payment Configuration", LlpPaymentFeePage.class);
		subMenuCtrlPanel.add(subMenuPaymentConfiguration);
		
		MenuItem subMenuEmailLog = new MenuItem("Email Log", ListLlpEmailLogPage.class);
		subMenuCtrlPanel.add(subMenuEmailLog);
		
		MenuItem menuMaintenanceSpecialKeyword = new MenuItem("Keyword Maintenance", ListLlpSpecialKeyword.class);
		subMenuCtrlPanel.add(menuMaintenanceSpecialKeyword);
		
		MenuItem menuLLPFileAttachment = new MenuItem("File Attachment", LLPFileAttachmentPage.class);
		subMenuCtrlPanel.add(menuLLPFileAttachment);
		
		MenuItem menuLLPParameters = new MenuItem("Parameters", LlpParameterPage.class);
		subMenuCtrlPanel.add(menuLLPParameters);
		
		
		menuPCtrlPanel.setSubMenuItemList(subMenuCtrlPanel);
		
		
		
		//ROB MENU
		MenuItem menuBizReg= new MenuItem("MyBiz Services");
		primaryMenuList.add(menuBizReg);
		
		List<MenuItem> subMenuBizReg = new ArrayList<MenuItem>();
		
		MenuItem myRenewalTransaction = new MenuItem("My Renewal Transactions", ListRobRenewalTransactionsPage.class);
		subMenuBizReg.add(myRenewalTransaction);
		
		menuBizReg.setSubMenuItemList(subMenuBizReg);
		
	}

	public BaseService getService(String serviceId) {
		return WicketApplication.getServiceNew(serviceId);
	}

	public List getCodeType(String codeType) {
		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		return parametersService.findByActiveCodeType(codeType);
	}
	
	public String getCodeTypeWithValue(String codeType, String value) {
		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		return parametersService.findByCodeTypeValue(codeType, value);
	}

	public abstract String getPageTitle();
	
	public String getLogoImage(){
		return "images/logoanywhererectangle.png";
	}
	
	
	public String getIpAddress(){
		HttpServletRequest  request=(HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
		return WicketUtils.getIpAddress(request, getSession());
	}

}
