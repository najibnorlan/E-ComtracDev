package com.ssm.llp.base.page;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.value.ValueMap;
import org.hibernate.LazyInitializationException;
import com.cooldatasoft.common.MenuItem;
import com.ssm.common.v2.InternalLoginPanel;
import com.ssm.common.v2.LoginPanel;
import com.ssm.common.v2.SignUpPanel;
import com.ssm.ezbiz.biztrust.ListBiztrustPage;
import com.ssm.ezbiz.cmp.SearchCmpHistoryPage;
import com.ssm.ezbiz.comtrac.FinalListingPage;
import com.ssm.ezbiz.comtrac.ListComtracTraining;
import com.ssm.ezbiz.comtrac.LpoApprovalTray;
import com.ssm.ezbiz.comtrac.SearchComtracList;
import com.ssm.ezbiz.comtrac.RobTrainingReprintcertSearch;
import com.ssm.ezbiz.comtrac.SelectComtracTraining;
import com.ssm.ezbiz.comtrac.SelectCorporateTraining;
import com.ssm.ezbiz.comtrac.SelectLPOPayment;
import com.ssm.ezbiz.comtrac.TabComtracPage;
import com.ssm.ezbiz.counter.ListCheckInCounter;
import com.ssm.ezbiz.dashboard.DashboardInternalPage;
import com.ssm.ezbiz.dashboard.DashboardPage;
import com.ssm.ezbiz.eghl.PaymentCheckWithEghl;
import com.ssm.ezbiz.emailBlast.MailBlaster;
import com.ssm.ezbiz.errorlog.SearchErrorLogCheckPage;
import com.ssm.ezbiz.healthCheck.HealthCheckPage;
import com.ssm.ezbiz.incentive.ListIncentiveVerification;
import com.ssm.ezbiz.myCardTransaction.ReadMyKadPage;
import com.ssm.ezbiz.myCardTransaction.SearchMyCardList;
import com.ssm.ezbiz.otcPayment.ListCollectionCounter;
import com.ssm.ezbiz.otcPayment.ListCollectionVerification;
import com.ssm.ezbiz.otcPayment.ListCounterBankinSlip;
import com.ssm.ezbiz.otcPayment.SearchBankinSlip;
import com.ssm.ezbiz.otcPayment.UserCheckinPage;
import com.ssm.ezbiz.report.BankInSlip;
import com.ssm.ezbiz.report.BranchSummaryReport;
import com.ssm.ezbiz.report.MonthlyBankInSlip;
import com.ssm.ezbiz.report.SearchCreditNote;
import com.ssm.ezbiz.report.SearchPaymentReceipt;
import com.ssm.ezbiz.robFormB.OwnersValidationListRobFormBPage;
import com.ssm.ezbiz.robFormB.SearchRobFormB;
import com.ssm.ezbiz.robFormB.SelectBizRobFormBPage;
import com.ssm.ezbiz.robFormB.TabRobFormBPage;
import com.ssm.ezbiz.robFormC.ListRobFormCPage;
import com.ssm.ezbiz.robFormC.ListRobFormCTransactionPage;
import com.ssm.ezbiz.robFormC.OwnersValidationListRobFormCPage;
import com.ssm.ezbiz.robFormC.SearchRobFormC;
import com.ssm.ezbiz.robUserActivationTray.SearchRobUserActivationTrayPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.OwnersValidationListRobFormAPage;
import com.ssm.ezbiz.robformA.SearchRobFormA;
import com.ssm.ezbiz.robformA.SearchRobFormA1Renewal;
import com.ssm.ezbiz.robformA.TabRobFormAPage;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.ezbiz.usageReport.FormTypeReport;
import com.ssm.ezbiz.usageReport.MonthlyReport;
import com.ssm.ezbiz.usageReport.NameTypeReport;
import com.ssm.ezbiz.usageReport.RobFormAStatistic;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.EditLlpUserProfilePasswordPage;
import com.ssm.llp.mod1.page.GuidelinePage;
import com.ssm.llp.mod1.page.ListLlpEmailLogPage;
import com.ssm.llp.mod1.page.ListLlpSpecialKeyword;
import com.ssm.llp.mod1.page.ListLlpUserProfilePage;
import com.ssm.llp.mod1.page.ListPaymentTransactionPage;
import com.ssm.llp.mod1.page.RobTrainingReprintcertList;
import com.ssm.llp.mod1.page.ListRobUserOkuSearchPage;
import com.ssm.llp.mod1.page.ListRobUserOkuWorkingTrayPage;
import com.ssm.llp.mod1.page.LlpPaymentFeePage;
import com.ssm.llp.mod1.page.UserTncPage;
import com.ssm.llp.mod1.page.ViewLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserTncService;
import com.ssm.llp.page.robRenewal.ListRobRenewalPage;
import com.ssm.llp.page.robRenewal.ListRobRenewalTransactionsPage;
import com.ssm.llp.page.robRenewal.SearchBizForRenew;
import com.ssm.supplyinfo.ListOrderPage;
import com.ssm.supplyinfo.PreviewCartPage;
import com.ssm.supplyinfo.SearchSupplyInfoTransaction;
import com.ssm.supplyinfo.SupplyInfoMainPage;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransDtlService;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;

public abstract class BasePage extends WebPage implements Serializable {// , IAjaxIndicatorAware {

	@SpringBean(name = "LlpUserLogService")
	private LlpUserLogService llpUserLogService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "LlpParametersService")
	public LlpParametersService llpParametersService;

	@SpringBean(name = "RobCounterCollectionService")
	private RobCounterCollectionService robCounterCollectionService;

	@SpringBean(name = "SupplyInfoTransHdrService")
	public SupplyInfoTransHdrService supplyInfoTransHdrService;

	@SpringBean(name = "SupplyInfoTransDtlService")
	public SupplyInfoTransDtlService supplyInfoTransDtlService;

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	@SpringBean(name = "RobUserTncService")
	private RobUserTncService robUserTncService;

	@SpringBean(name = "applicationType")
	private String applicationType;

	public final String SUCCESS_MSG = "SUCCESS_MSG";
	public final String ERROR_MSG = "ERROR_MSG";

	private WebMarkupContainer wmc;
	private SSMMessagesFeedbackPanel feedbackPanel;

	public final SSMLabel cartTotal = new SSMLabel("cartTotal", "0");
	public final WebMarkupContainer wmcMainMenu = new WebMarkupContainer("wmcMainMenu");
	public Image bannerImage = new Image("banner", "");

	// public final BookmarkablePageLink privacyPolicyLink = new
	// BookmarkablePageLink("privacyPolicy", PrivacyPolicy.class);
	public final BookmarkablePageLink securityPolicyLink = new BookmarkablePageLink("securityPolicy",
			SecurityPolicy.class);
	public final BookmarkablePageLink guidelinePageLink = new BookmarkablePageLink("guidelinePage",
			GuidelinePage.class);
	public final BookmarkablePageLink tncLink = new BookmarkablePageLink("userTnC", UserTncPage.class);

	public BasePage() {
		String path = getMarkup().getMarkupResourceStream().getBaseMarkup().locationAsString();

		if (WicketApplication.SELECTED_THEME.equals(WicketApplication.THEME_SUPPLY_INFO)) {
			initThemeV2();
		} else {
			initThemeV1();
		}

//		if(path.indexOf("/v2/")!=-1) {
////			new com.ssm.common.v2.BasePage(this);
//			
//
//			
//			SSMAjaxLink toCart = new SSMAjaxLink("toCart") {
//				@Override
//				public void onClick(AjaxRequestTarget arg0) {
//					setResponsePage(PreviewCartPage.class);
//				}
//			};
//			add(toCart);
//		}else {
//			initThemeV1();
//		}

	}

	public void initThemeV2() {

		// Ni tuk page title
//		String myPageTitle = "";
//		if (getPageTitle() != null)
//			myPageTitle = getPageTitle();
//		else
//			myPageTitle = "page.title.default";

		SSMLabel transparentCssClassStart = new SSMLabel("transparentCssClassStart", "");
		SSMLabel transparentCssClassEnd = new SSMLabel("transparentCssClassEnd", "");

		if (!displayAnimatedBackground()) {
			transparentCssClassStart.setDefaultModelObject("<div class=\"ui attached segment\">");
			transparentCssClassEnd.setDefaultModelObject("</div>");
		}

		transparentCssClassStart.setRenderBodyOnly(true);
		transparentCssClassEnd.setRenderBodyOnly(true);

		add(transparentCssClassStart);
		add(transparentCssClassEnd);

		String myPageTitle = getPageTitle();
		if (StringUtils.isBlank(myPageTitle)) {
			myPageTitle = this.getClass().getName();
		}

		add(new SSMLabel("pageTitle", new StringResourceModel(myPageTitle, this, null)));

		SSMLabel animatedBackgroud = new SSMLabel("animatedBackgroud", "");
		add(animatedBackgroud);
		if (!displayAnimatedBackground()) {
			animatedBackgroud.setVisible(false);
		}
		add(new SSMLabel("secondSegment", ""));
		add(new SSMLabel("thirdSegment", ""));
		add(new LoginPanel("loginPanel"));
		add(new InternalLoginPanel("internalLoginPanel"));
		add(new SignUpPanel("signUpPanel"));

		WebMarkupContainer floatSeg = new WebMarkupContainer("floatSeg");
		floatSeg.setOutputMarkupId(true);
		add(floatSeg);

		if (!displayFloatingCart()) {
			floatSeg.setVisible(false);
		}

		List<SupplyInfoTransDtl> list = getCartHdr().getListSupplyInfoTransDtl();

		if (list != null && list.size() > 0) {
			cartTotal.setDefaultModelObject(new String(list.size() + ""));
		}
		cartTotal.setOutputMarkupId(true);
		add(cartTotal);

		SSMAjaxLink toCart = new SSMAjaxLink("toCart") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				setResponsePage(PreviewCartPage.class);
			}
		};
		floatSeg.add(toCart);

		WebMarkupContainer wmcLogin = new WebMarkupContainer("wmcLogin");
		wmcLogin.setOutputMarkupId(true);
		wmcLogin.setVisible(false);
		add(wmcLogin);

		WebMarkupContainer wmcNotLogin = new WebMarkupContainer("wmcNotLogin");
		wmcNotLogin.setOutputMarkupId(true);
		wmcNotLogin.setVisible(false);
		add(wmcNotLogin);

		WebMarkupContainer wmcNotLoginBlackBar = new WebMarkupContainer("wmcNotLoginBlackBar");
		wmcNotLoginBlackBar.setOutputMarkupId(true);
		wmcNotLoginBlackBar.setVisible(false);
		add(wmcNotLoginBlackBar);

		WebMarkupContainer wmcLoginBlackBar = new WebMarkupContainer("wmcLoginBlackBar");
		wmcLoginBlackBar.setOutputMarkupId(true);
		wmcLoginBlackBar.setVisible(false);
		add(wmcLoginBlackBar);

		if (UserEnvironmentHelper.getUserenvironment() != null) {
			String welcomeMsgId = "base.welcome";
			ValueMap map = new ValueMap();
			map.put("fullName", UserEnvironmentHelper.getUserenvironment().getFullName());
			StringResourceModel labelModel = new StringResourceModel(welcomeMsgId, this, new Model<ValueMap>(map));
			wmcLoginBlackBar.add(new SSMLabel(welcomeMsgId, labelModel));
			wmcLogin.add(new SSMLabel(welcomeMsgId, labelModel));

			Link linkSignOut = new Link("signout") {
				@Override
				public void onClick() {
					getSession().invalidate();
					LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment()
							.getAttribute("UserLog");
					llpUserLog.setLogoutTime(new Date());
					llpUserLogService.update(llpUserLog);

					UserEnvironmentHelper.setUserEnvironment(null);
					setResponsePage(HomePage.class);
				}
			};
			wmcLogin.add(new Link("signout") {
				@Override
				public void onClick() {
					getSession().invalidate();
					LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment()
							.getAttribute("UserLog");
					llpUserLog.setLogoutTime(new Date());
					llpUserLogService.update(llpUserLog);

					UserEnvironmentHelper.setUserEnvironment(null);
					setResponsePage(SupplyInfoMainPage.class);
				}
			});
			wmcLoginBlackBar.add(new Link("signout") {
				@Override
				public void onClick() {
					getSession().invalidate();
					LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment()
							.getAttribute("UserLog");
					llpUserLog.setLogoutTime(new Date());
					llpUserLogService.update(llpUserLog);

					UserEnvironmentHelper.setUserEnvironment(null);
					setResponsePage(SupplyInfoMainPage.class);
				}
			});

			wmcLogin.setVisible(true);
			wmcLoginBlackBar.setVisible(true);

			SSMLabel ifNotActivateBase = new SSMLabel("ifNotActivateBase",
					resolve("com.ssm.supplyInfo.ifNotActivateBase"));
			ifNotActivateBase.setVisible(false);
			wmcLogin.add(ifNotActivateBase);

			if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
				if (!Parameter.USER_STATUS_active
						.equals(((LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile()
								.getUserStatus())) {
					ifNotActivateBase.setVisible(true);
				}
			}

			/// maybe session clean after login..so kena reset balik
			if (getSession().getAttribute("isMergeWithCookies") == null
					|| Parameter.YES_NO_no.equals(getSession().getAttribute("isMergeWithCookies"))) {
				getSession().setAttribute("isMergeWithCookies", Parameter.YES_NO_yes);

				SupplyInfoTransHdr latestGenHdr = supplyInfoTransHdrService
						.genAndMergeLatestCart(UserEnvironmentHelper.getLoginName(), getCartHdr());

				getSession().setAttribute("cartInfoHdr_", (Serializable) latestGenHdr);

				if (((WebRequest) getRequest()).getCookie("cartInfoHdrID") != null) {
					Cookie cartInfoHdrCookie = ((WebRequest) getRequest()).getCookie("cartInfoHdrID");
					((WebResponse) getResponse()).clearCookie(cartInfoHdrCookie);

				}

				cartTotal.setDefaultModelObject(new String(latestGenHdr.getListSupplyInfoTransDtl().size() + ""));

			}

		} else {
			wmcNotLogin.setVisible(true);
			wmcNotLoginBlackBar.setVisible(true);
		}

		BookmarkablePageLink myOrders = new BookmarkablePageLink("myOrders", ListOrderPage.class);
		myOrders.setOutputMarkupId(true);
		myOrders.setVisible(false);
		add(myOrders);

		BookmarkablePageLink searchOrder = new BookmarkablePageLink("searchOrder", SearchSupplyInfoTransaction.class);
		searchOrder.setOutputMarkupId(true);
		searchOrder.setVisible(false);
		add(searchOrder);

		BookmarkablePageLink editLlpUserProfilePasswordPage = new BookmarkablePageLink("editLlpUserProfilePasswordPage",
				EditLlpUserProfilePasswordPage.class);
		editLlpUserProfilePasswordPage.setVisible(false);
		add(editLlpUserProfilePasswordPage);

		if (UserEnvironmentHelper.getUserenvironment() != null) {
			if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
				myOrders.setVisible(true);
				editLlpUserProfilePasswordPage.setVisible(true);
			} else if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
				searchOrder.setVisible(true);
			}

		}

		generateMenu();

	}

	private void generateMenu() {
		Link linkSignOut = new Link("signout") {
			@Override
			public void onClick() {
				getSession().invalidate();
				LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment().getAttribute("UserLog");
				llpUserLog.setLogoutTime(new Date());
				llpUserLogService.update(llpUserLog);

				UserEnvironmentHelper.setUserEnvironment(null);
				setResponsePage(HomePage.class);
			}
		};
		add(new BookmarkablePageLink("homePage1", SupplyInfoMainPage.class));
		add(new BookmarkablePageLink("homePage2", SupplyInfoMainPage.class));
		add(new BookmarkablePageLink("homePage3", SupplyInfoMainPage.class));

	}

	public void initThemeV1() {

		long duration = System.currentTimeMillis();
		LlpUserProfile llpUserProfile = null;// llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
//		LlpUserEnviroment llpUserEnviroment = (LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment();
		boolean isMykadChannel = false;

		if (UserEnvironmentHelper.getUserenvironment() != null) {
			if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
				llpUserProfile = ((LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
			} else if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
				if (Parameter.LOGIN_TYPE_mykad.equals((UserEnvironmentHelper.getUserenvironment()).getChannel())) {
					isMykadChannel = true;
				}
			}
		}

		// Ni tuk page title
		String myPageTitle = "";
		if (getPageTitle() != null)
			myPageTitle = getPageTitle();
		else
			myPageTitle = "page.title.default.comtrac";

		add(new SSMLabel("pageTitle", new StringResourceModel(myPageTitle, this, null)));

		// Ni tuk footer
//		add(privacyPolicyLink);
		add(securityPolicyLink);
		add(guidelinePageLink);
		add(tncLink);

		SSMLabel ipAdd = new SSMLabel("ipAdd", getIpAddress());
		ipAdd.setOutputMarkupId(true);
		ipAdd.setOutputMarkupPlaceholderTag(true);
		ipAdd.setVisible(false);
		if (UserEnvironmentHelper.isInternalUser()) {
			ipAdd.setVisible(true);
		}

		add(ipAdd);

		HttpServletRequest request = (HttpServletRequest) getRequestCycle().getRequest().getContainerRequest();

		String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG,
				Parameter.LLP_CONFIG_deployment_type);

		String uri = "";
		String banner = "images/bannerecomtrac.png";

		try {
			uri = getUrl(request);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		if (!StringUtils.isBlank(uri)) {
			if (uri.contains(Parameter.URL_TYPE_dev) || uri.contains(Parameter.URL_TYPE_local)) {
				banner = "images/bannerecomtrac.png";
			} else if (uri.contains(Parameter.URL_TYPE_stg)) {
				banner = "images/bannerecomtrac.png";
			}
		}
		bannerImage = new Image("banner", new ContextRelativeResource(banner));
		add(bannerImage);

		Link linkSignOut = new Link("signout") {
			@Override
			public void onClick() {
				getSession().invalidate();
				LlpUserLog llpUserLog = (LlpUserLog) UserEnvironmentHelper.getUserenvironment().getAttribute("UserLog");
				if (llpUserLog != null) {
					llpUserLog.setLogoutTime(new Date());
					llpUserLogService.update(llpUserLog);
				}
				UserEnvironmentHelper.setUserEnvironment(null);
				setResponsePage(HomePage.class);
			}
		};

		Link linkClose = new Link("close") {
			@Override
			public void onClick() {

			}
		};
		linkClose.add(new AttributeAppender("onclick", "closeWindows();"));

		wmc = new WebMarkupContainer("isLogin");
		wmc.add(linkClose);
		wmc.add(linkSignOut);

		add(wmc);

		SignInSession signInSession = (SignInSession) getSession();
		boolean isExtIface = false;
		if (null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
			linkClose.setVisible(true);
			linkSignOut.setVisible(false);
		} else {
			linkClose.setVisible(false);
			linkSignOut.setVisible(true);
		}

		if (null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
			isExtIface = true;
		}

		SSMLabel isFromExternal = new SSMLabel("isFromExternal", "");
		add(isFromExternal);

		if (isExtIface) {
			isFromExternal.setVisible(false);
		}

		feedbackPanel = new SSMMessagesFeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		List listSuccessMessage = (List) getSession().getAttribute(SUCCESS_MSG);
		if (listSuccessMessage != null) {
			for (int i = 0; i < listSuccessMessage.size(); i++) {
				String msg = (String) listSuccessMessage.get(i);
				success(msg);
			}
			getSession().removeAttribute(SUCCESS_MSG);
		}

		List listErrorMessage = (List) getSession().getAttribute(ERROR_MSG);
		if (listErrorMessage != null) {
			for (int i = 0; i < listErrorMessage.size(); i++) {
				String msg = (String) listErrorMessage.get(i);
				error(msg);
			}
			getSession().removeAttribute(ERROR_MSG);
		}

//		List<MenuItem> primaryMenuList = buildMenu();
//		WebMarkupContainer wmcMainMenu =  new WebMarkupContainer("wmcMainMenu");
		add(wmcMainMenu);

		WebMarkupContainer wmcComtracPublic = new WebMarkupContainer("wmcComtracPublic");
		wmcComtracPublic.setOutputMarkupId(true);
		wmcComtracPublic.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic);

		WebMarkupContainer wmcComtracPublic2 = new WebMarkupContainer("wmcComtracPublic2");
		wmcComtracPublic2.setOutputMarkupId(true);
		wmcComtracPublic2.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic2);

		WebMarkupContainer wmcComtracPublic3 = new WebMarkupContainer("wmcComtracPublic3");
		wmcComtracPublic3.setOutputMarkupId(true);
		wmcComtracPublic3.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic3);

		WebMarkupContainer wmcComtracPublic4 = new WebMarkupContainer("wmcComtracPublic4");
		wmcComtracPublic4.setOutputMarkupId(true);
		wmcComtracPublic4.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic4);

		WebMarkupContainer wmcComtracPublic5 = new WebMarkupContainer("wmcComtracPublic5");
		wmcComtracPublic5.setOutputMarkupId(true);
		wmcComtracPublic5.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic5);

		WebMarkupContainer wmcComtracPublic6 = new WebMarkupContainer("wmcComtracPublic6");
		wmcComtracPublic6.setOutputMarkupId(true);
		wmcComtracPublic6.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic6);

		WebMarkupContainer wmcComtracPublic7 = new WebMarkupContainer("wmcComtracPublic7");
		wmcComtracPublic7.setOutputMarkupId(true);
		wmcComtracPublic7.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic7);
		
		WebMarkupContainer wmcComtracPublic8 = new WebMarkupContainer("wmcComtracPublic8");
		wmcComtracPublic8.setOutputMarkupId(true);
		wmcComtracPublic8.setVisible(false);
		wmcMainMenu.add(wmcComtracPublic8);

		
		SSMLabel newReg = new SSMLabel("newReg", resolve("menu.comtrac.newReg"));
		newReg.setVisible(false);
		wmcMainMenu.add(newReg);

		WebMarkupContainer wmcComtracInternal = new WebMarkupContainer("wmcComtracInternal");
		wmcComtracInternal.setOutputMarkupId(true);
		wmcComtracInternal.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal);

		WebMarkupContainer wmcComtracInternal2 = new WebMarkupContainer("wmcComtracInternal2");
		wmcComtracInternal2.setOutputMarkupId(true);
		wmcComtracInternal2.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal2);

		WebMarkupContainer wmcComtracInternal3 = new WebMarkupContainer("wmcComtracInternal3");
		wmcComtracInternal3.setOutputMarkupId(true);
		wmcComtracInternal3.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal3);

		WebMarkupContainer wmcComtracInternal4 = new WebMarkupContainer("wmcComtracInternal4");
		wmcComtracInternal4.setOutputMarkupId(true);
		wmcComtracInternal4.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal4);
		
		WebMarkupContainer wmcComtracInternal5 = new WebMarkupContainer("wmcComtracInternal5");
		wmcComtracInternal5.setOutputMarkupId(true);
		wmcComtracInternal5.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal5);
		
		WebMarkupContainer wmcComtracInternal6 = new WebMarkupContainer("wmcComtracInternal6");
		wmcComtracInternal6.setOutputMarkupId(true);
		wmcComtracInternal6.setVisible(false);
		wmcMainMenu.add(wmcComtracInternal6);

		Class homeClass = HomePage.class;
		if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
			homeClass = DashboardPage.class;
		}
		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
			if (isMykadChannel) {
				homeClass = ReadMyKadPage.class;
			} else {
				homeClass = DashboardInternalPage.class;
			}
		}
//		wmcMainMenu.add(new BookmarkablePageLink("selfservice", SelfServiceRegistration.class));

		wmcMainMenu.add(new BookmarkablePageLink("Home", homeClass));

//		BookmarkablePageLink internalSignIn = new BookmarkablePageLink("Internal", HomeInternalPage.class);
//		wmcMainMenu.add(internalSignIn);
//		internalSignIn.setVisible(false);
//		if (UserEnvironmentHelper.getUserenvironment() == null) {
//			internalSignIn.setVisible(true);
//		}

		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
//			generateMenuInternal(primaryMenuList);
		}

		SSMLabel activateAccRenew = new SSMLabel("activateAccRenew", resolve("menu.myBiz.pleaseActivateAccount"));
		activateAccRenew.setVisible(false);
		wmcMainMenu.add(activateAccRenew);

		SSMLabel activateAccA = new SSMLabel("activateAccA", resolve("menu.myBiz.pleaseActivateAccount"));
		activateAccA.setVisible(false);
		wmcMainMenu.add(activateAccA);

		SSMLabel activateAccB = new SSMLabel("activateAccB", resolve("menu.myBiz.pleaseActivateAccount"));
		activateAccB.setVisible(false);
		wmcMainMenu.add(activateAccB);

		SSMLabel activateAccC = new SSMLabel("activateAccC", resolve("menu.myBiz.pleaseActivateAccount"));
		activateAccC.setVisible(false);
		wmcMainMenu.add(activateAccC);

		SSMLabel activateAccCmp = new SSMLabel("activateAccCmp", resolve("menu.myBiz.pleaseActivateAccount"));
		activateAccCmp.setVisible(false);
		wmcMainMenu.add(activateAccCmp);

		String ipAddress = getIpAddress();

		String enableDashboard = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG,
				Parameter.LLP_CONFIG_ENABLE_DASHBOARD);

		BookmarkablePageLink editLlpUserProfilePasswordPage = new BookmarkablePageLink("editLlpUserProfilePasswordPage",
				EditLlpUserProfilePasswordPage.class);
		editLlpUserProfilePasswordPage.setVisible(false);
		wmcMainMenu.add(editLlpUserProfilePasswordPage);
		
		BookmarkablePageLink reprintCert = new BookmarkablePageLink("reprintCert",
				EditLlpUserProfilePasswordPage.class);
		reprintCert.setVisible(false);
		wmcMainMenu.add(reprintCert);

		if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) { // menu public
			editLlpUserProfilePasswordPage.setVisible(true);
			reprintCert.setVisible(true);
			wmcComtracPublic.setVisible(true);
			wmcComtracPublic2.setVisible(true);
			wmcComtracPublic3.setVisible(true);
			wmcComtracPublic5.setVisible(true);
			wmcComtracPublic7.setVisible(true);
			wmcComtracPublic8.setVisible(true);
			newReg.setVisible(true);

			BookmarkablePageLink listRobRenewalPage = new BookmarkablePageLink("listRobRenewalPage",
					ListRobRenewalPage.class);
			wmcMainMenu.add(listRobRenewalPage);

			BookmarkablePageLink searchBizForRenew = new BookmarkablePageLink("searchBizForRenew",
					SearchBizForRenew.class);
			wmcMainMenu.add(searchBizForRenew);

			BookmarkablePageLink listRobRenewalTransactionsPage = new BookmarkablePageLink(
					"listRobRenewalTransactionsPage", ListRobRenewalTransactionsPage.class);
			wmcMainMenu.add(listRobRenewalTransactionsPage);

			// ROB FORM A
			BookmarkablePageLink editRobFormAPage = new BookmarkablePageLink("editRobFormAPage",
					EditRobFormAPage.class);
			wmcMainMenu.add(editRobFormAPage);

			BookmarkablePageLink tabRobFormAPage = new BookmarkablePageLink("tabRobFormAPage", TabRobFormAPage.class);
			wmcMainMenu.add(tabRobFormAPage);

			BookmarkablePageLink ownersValidationListRobFormAPage = new BookmarkablePageLink(
					"ownersValidationListRobFormAPage", OwnersValidationListRobFormAPage.class);
			wmcMainMenu.add(ownersValidationListRobFormAPage);

			// ROB FORM B
			BookmarkablePageLink selectBizRobFormBPage = new BookmarkablePageLink("selectBizRobFormBPage",
					SelectBizRobFormBPage.class);// SelectBizRobFormBPage EditRobFormBPage
			wmcMainMenu.add(selectBizRobFormBPage);

			BookmarkablePageLink tabRobFormBPage = new BookmarkablePageLink("tabRobFormBPage", TabRobFormBPage.class);
			wmcMainMenu.add(tabRobFormBPage);

			BookmarkablePageLink ownersValidationListRobFormBPage = new BookmarkablePageLink(
					"ownersValidationListRobFormBPage", OwnersValidationListRobFormBPage.class);
			wmcMainMenu.add(ownersValidationListRobFormBPage);

			// ROB FORM C
			BookmarkablePageLink listRobFormCPage = new BookmarkablePageLink("listRobFormCPage",
					ListRobFormCPage.class);
			wmcMainMenu.add(listRobFormCPage);

			BookmarkablePageLink listRobFormCTransactionPage = new BookmarkablePageLink("listRobFormCTransactionPage",
					ListRobFormCTransactionPage.class);
			wmcMainMenu.add(listRobFormCTransactionPage);

			BookmarkablePageLink ownersValidationListRobFormCPage = new BookmarkablePageLink(
					"ownersValidationListRobFormCPage", OwnersValidationListRobFormCPage.class);
			wmcMainMenu.add(ownersValidationListRobFormCPage);

			// COMTRAC

			BookmarkablePageLink selectComtracTraining = new BookmarkablePageLink("selectComtracTraining",
					SelectComtracTraining.class);
			wmcComtracPublic.add(selectComtracTraining);

			BookmarkablePageLink tabComtracPage = new BookmarkablePageLink("tabComtracPage", TabComtracPage.class);
			wmcComtracPublic2.add(tabComtracPage);

			BookmarkablePageLink selectCorporateTraining = new BookmarkablePageLink("selectCorporateTraining",
					SelectCorporateTraining.class);
			wmcComtracPublic3.add(selectCorporateTraining);
			
			BookmarkablePageLink selectReprintCert = new BookmarkablePageLink("selectReprintCert",
					RobTrainingReprintcertSearch.class);
			wmcComtracPublic7.add(selectReprintCert);
			
			BookmarkablePageLink reprintCertOrders = new BookmarkablePageLink("reprintCertOrders",
					RobTrainingReprintcertList.class);
			wmcComtracPublic8.add(reprintCertOrders);

			BookmarkablePageLink selectLPOPayment = new BookmarkablePageLink("selectLPOPayment",
					SelectLPOPayment.class);
			wmcComtracPublic5.add(selectLPOPayment);

			BookmarkablePageLink listPaymentTransactionPage = new BookmarkablePageLink("listPaymentTransactionPage",
					ListPaymentTransactionPage.class);
			wmcMainMenu.add(listPaymentTransactionPage);

			BookmarkablePageLink viewLlpUserProfilePage = new BookmarkablePageLink("viewLlpUserProfilePage",
					ViewLlpUserProfilePage.class);
			wmcMainMenu.add(viewLlpUserProfilePage);

			// Other Services
			BookmarkablePageLink biztrustPage = new BookmarkablePageLink("biztrustPage", ListBiztrustPage.class);
			wmcMainMenu.add(biztrustPage);

			if (Parameter.YES_NO_yes.equals(
					getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_FILTER_USER_LIST_COMTRAC))) {
				wmcComtracPublic.setVisible(false);
				wmcComtracPublic2.setVisible(false);
				wmcComtracPublic3.setVisible(false);
				wmcComtracPublic5.setVisible(false);
				wmcComtracPublic7.setVisible(false);
				wmcComtracPublic8.setVisible(false);
				String userFilterComtrac = getCodeTypeWithValue(Parameter.LLP_CONFIG,
						Parameter.LLP_CONFIG_FILTER_USER_LIST_COMTRAC);
				if (StringUtils.isNotBlank(userFilterComtrac)) {
					List<String> userList = Arrays.asList(StringUtils.split(userFilterComtrac, ","));
					if (userList.contains(UserEnvironmentHelper.getLoginName())) {
						wmcComtracPublic.setVisible(true);
						wmcComtracPublic2.setVisible(true);
						wmcComtracPublic3.setVisible(true);
						wmcComtracPublic5.setVisible(false);
						wmcComtracPublic7.setVisible(false);
						wmcComtracPublic8.setVisible(false);
					}
				}
			}

			if (Parameter.YES_NO_yes.equals(enableDashboard)) {
				listRobRenewalPage.setVisible(false);
				editRobFormAPage.setVisible(false);
				selectBizRobFormBPage.setVisible(false);
				listRobFormCPage.setVisible(false);
			} else {
				listRobRenewalPage.setVisible(true);
				editRobFormAPage.setVisible(true);
				selectBizRobFormBPage.setVisible(true);
				listRobFormCPage.setVisible(true);
			}

			if (Parameter.USER_STATUS_pending.equals(llpUserProfile.getUserStatus())) {
				listRobRenewalPage.setVisible(false);
				searchBizForRenew.setVisible(false);
				listRobRenewalTransactionsPage.setVisible(false);

				editRobFormAPage.setVisible(false);
				tabRobFormAPage.setVisible(false);
				ownersValidationListRobFormAPage.setVisible(false);

				selectBizRobFormBPage.setVisible(false);
				tabRobFormBPage.setVisible(false);
				ownersValidationListRobFormBPage.setVisible(false);

				listRobFormCPage.setVisible(false);
				listRobFormCTransactionPage.setVisible(false);
				ownersValidationListRobFormCPage.setVisible(false);

			}

//			else if (Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus())) { //for tnc.. Comtrac tak perlu verify ic ???
			String userRefNo = llpUserProfile.getUserRefNo();
			String loginId = llpUserProfile.getLoginId();
			String tncType = Parameter.TNC_TYPE_USER_COMTRAC;
			String enableUserTnc = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG,
					Parameter.LLP_CONFIG_ENABLE_USER_TNC_COMTRAC);
			boolean hasTncAgreement = robUserTncService.hasAgreeTnc(userRefNo, loginId, tncType);

			if (Parameter.YES_NO_yes.equals(enableUserTnc) && (!hasTncAgreement)) { // if enable TNC and No TNC record.
				selectComtracTraining.setVisible(false);
				tabComtracPage.setVisible(false);
				listPaymentTransactionPage.setVisible(false);
				viewLlpUserProfilePage.setVisible(false); // to hide profile (IC number)
				editLlpUserProfilePasswordPage.setVisible(false);
				reprintCert.setVisible(false);
				selectCorporateTraining.setVisible(false);
				selectLPOPayment.setVisible(false);
				newReg.setVisible(false);
			} else {
				selectComtracTraining.setVisible(true);
				tabComtracPage.setVisible(true);
				listPaymentTransactionPage.setVisible(true);
				viewLlpUserProfilePage.setVisible(true); // to hide profile (IC number)
				editLlpUserProfilePasswordPage.setVisible(true);
				reprintCert.setVisible(true);
				selectCorporateTraining.setVisible(true);
				selectLPOPayment.setVisible(true);
				newReg.setVisible(true);
			}
//			}

//			BookmarkablePageLink searchBusinessForFormCPage = new BookmarkablePageLink("searchBusinessForFormCPage", SearchBusinessForFormCPage.class);
//			wmcMainMenu.add(searchBusinessForFormCPage);
		}
		BookmarkablePageLink listLlpUserProfilePage = new BookmarkablePageLink("listLlpUserProfilePage",
				ListLlpUserProfilePage.class);
		listLlpUserProfilePage.setVisible(false);
		wmcMainMenu.add(listLlpUserProfilePage);

		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
			if (isMykadChannel) {
				System.out.println(this.getClass().getName());
				if (!this.getClass().getName().equals(ReadMyKadPage.class.getName())
						&& !this.getClass().getName().equals(GuidelinePage.class.getName())) {
					setResponsePage(ReadMyKadPage.class);
				}
			} else {

				BookmarkablePageLink userVerificationTray = new BookmarkablePageLink("userVerificationTray",
						SearchRobUserActivationTrayPage.class);
				userVerificationTray.setVisible(true);
				wmcMainMenu.add(userVerificationTray);

				RobCounterCollection counterCollection = robCounterCollectionService.findByIpAddress(ipAddress);
				listLlpUserProfilePage.setVisible(true);
				wmcComtracInternal.setVisible(true);
				wmcComtracInternal2.setVisible(true);
				wmcComtracInternal3.setVisible(true);
				wmcComtracInternal4.setVisible(true);
				wmcComtracInternal5.setVisible(true);
				wmcComtracInternal6.setVisible(true);

				BookmarkablePageLink selectComtracTrainingInternal = new BookmarkablePageLink(
						"selectComtracTrainingInternal", SelectComtracTraining.class);
				wmcComtracInternal.add(selectComtracTrainingInternal);

				BookmarkablePageLink tabComtracPage = new BookmarkablePageLink("tabComtracPageInternal",
						TabComtracPage.class);
				wmcComtracInternal2.add(tabComtracPage);

				BookmarkablePageLink listComtracTraining = new BookmarkablePageLink("listComtracTraining",
						ListComtracTraining.class);
				wmcComtracInternal3.add(listComtracTraining);

				BookmarkablePageLink lpoApprovalTray = new BookmarkablePageLink("lpoApprovalTray",
						LpoApprovalTray.class);
				wmcComtracInternal4.add(lpoApprovalTray);
				
				BookmarkablePageLink reprintCertTray = new BookmarkablePageLink("reprintCertTray",
						RobTrainingReprintcertList.class);
				wmcComtracInternal5.add(reprintCertTray);
				
				BookmarkablePageLink finalListingTray = new BookmarkablePageLink("finalListingTray",
						FinalListingPage.class);
				wmcComtracInternal6.add(finalListingTray);

				BookmarkablePageLink myCardTransaction = new BookmarkablePageLink("myCardTransaction",
						SearchMyCardList.class);
				wmcMainMenu.add(myCardTransaction);

				BookmarkablePageLink searchPaymentReceipt = new BookmarkablePageLink("searchPaymentReceipt",
						SearchPaymentReceipt.class);
				wmcMainMenu.add(searchPaymentReceipt);

				BookmarkablePageLink creditNoteReport = new BookmarkablePageLink("creditNoteReport",
						SearchCreditNote.class);
				wmcMainMenu.add(creditNoteReport);

				BookmarkablePageLink branchSummaryReport = new BookmarkablePageLink("branchSummaryReport",
						BranchSummaryReport.class);
				wmcMainMenu.add(branchSummaryReport);

				BookmarkablePageLink bankinSlipReport = new BookmarkablePageLink("bankinSlipReport", BankInSlip.class);
				wmcMainMenu.add(bankinSlipReport);

				BookmarkablePageLink monthlyBankInSlipReport = new BookmarkablePageLink("monthlyBankInSlipReport",
						MonthlyBankInSlip.class);
				wmcMainMenu.add(monthlyBankInSlipReport);

				BookmarkablePageLink robFormAStatistic = new BookmarkablePageLink("robFormAStatistic",
						RobFormAStatistic.class);
				robFormAStatistic.setOutputMarkupId(true);
				wmcMainMenu.add(robFormAStatistic);

				BookmarkablePageLink emailBlast = new BookmarkablePageLink("emailBlast", MailBlaster.class);
				emailBlast.setOutputMarkupId(true);
				wmcMainMenu.add(emailBlast);

				BookmarkablePageLink listPaymentTransactionPage = new BookmarkablePageLink("listPaymentTransactionPage",
						ListPaymentTransactionPage.class);
				wmcMainMenu.add(listPaymentTransactionPage);

				BookmarkablePageLink searchComtrac = new BookmarkablePageLink("searchComtrac", SearchComtracList.class);
				wmcMainMenu.add(searchComtrac);

				BookmarkablePageLink llpLocaleMessagePage = new BookmarkablePageLink("llpLocaleMessagePage",
						LlpLocaleMessagePage.class);
				wmcMainMenu.add(llpLocaleMessagePage);

				BookmarkablePageLink healthCheckPage = new BookmarkablePageLink("healthCheckPage",
						HealthCheckPage.class);
				healthCheckPage.setOutputMarkupId(true);
				wmcMainMenu.add(healthCheckPage);

				BookmarkablePageLink llpPaymentFeePage = new BookmarkablePageLink("llpPaymentFeePage",
						LlpPaymentFeePage.class);
				wmcMainMenu.add(llpPaymentFeePage);

				BookmarkablePageLink listLlpEmailLogPage = new BookmarkablePageLink("listLlpEmailLogPage",
						ListLlpEmailLogPage.class);
				wmcMainMenu.add(listLlpEmailLogPage);

				BookmarkablePageLink lLPFileAttachmentPage = new BookmarkablePageLink("lLPFileAttachmentPage",
						LLPFileAttachmentPage.class);
				wmcMainMenu.add(lLPFileAttachmentPage);

				BookmarkablePageLink llpParameterPage = new BookmarkablePageLink("llpParameterPage",
						LlpParameterPage.class);
				wmcMainMenu.add(llpParameterPage);

				BookmarkablePageLink gafConfigPage = new BookmarkablePageLink("gafConfigPage", GafConfigPage.class);
				wmcMainMenu.add(gafConfigPage);

				BookmarkablePageLink listCheckInCounter = new BookmarkablePageLink("listCheckInCounter",
						ListCheckInCounter.class);
				wmcMainMenu.add(listCheckInCounter);

				BookmarkablePageLink paymentCheckWithEghl = new BookmarkablePageLink("paymentCheckWithEghl",
						PaymentCheckWithEghl.class);
				wmcMainMenu.add(paymentCheckWithEghl);

				BookmarkablePageLink searchErrorLogCheckPage = new BookmarkablePageLink("searchErrorLogCheckPage",
						SearchErrorLogCheckPage.class);
				wmcMainMenu.add(searchErrorLogCheckPage);

				BookmarkablePageLink searchSSMAuditLogPage = new BookmarkablePageLink("searchSSMAuditLogPage",
						SearchSSMAuditLogPage.class);
				wmcMainMenu.add(searchSSMAuditLogPage);

				BookmarkablePageLink listCollectionCounter = new BookmarkablePageLink("listCollectionCounter",
						ListCollectionCounter.class);
				wmcMainMenu.add(listCollectionCounter);

				BookmarkablePageLink incentiveVerification = new BookmarkablePageLink("incentiveVerification",
						ListIncentiveVerification.class);
				incentiveVerification.setOutputMarkupId(true);
				wmcMainMenu.add(incentiveVerification);

				BookmarkablePageLink userCheckinPage = new BookmarkablePageLink("userCheckinPage",
						UserCheckinPage.class);
				userCheckinPage.setOutputMarkupPlaceholderTag(true);
				if (counterCollection != null) {
					userCheckinPage.setVisible(true);
				} else {
					userCheckinPage.setVisible(false);
				}
				wmcMainMenu.add(userCheckinPage);

				BookmarkablePageLink listCounterBankinSlip = new BookmarkablePageLink("listCounterBankinSlip",
						ListCounterBankinSlip.class);
				wmcMainMenu.add(listCounterBankinSlip);

				BookmarkablePageLink listCollectionVerification = new BookmarkablePageLink("listCollectionVerification",
						ListCollectionVerification.class);
				wmcMainMenu.add(listCollectionVerification);

				BookmarkablePageLink searchBankinSlip = new BookmarkablePageLink("searchBankinSlip",
						SearchBankinSlip.class);
				wmcMainMenu.add(searchBankinSlip);

				BookmarkablePageLink monthlyReport = new BookmarkablePageLink("monthlyReport", MonthlyReport.class);
				monthlyReport.setOutputMarkupId(true);
				wmcMainMenu.add(monthlyReport);

				BookmarkablePageLink nameTypeReport = new BookmarkablePageLink("nameTypeReport", NameTypeReport.class);
				nameTypeReport.setOutputMarkupId(true);
				wmcMainMenu.add(nameTypeReport);

				BookmarkablePageLink formTypeReport = new BookmarkablePageLink("formTypeReport", FormTypeReport.class);
				formTypeReport.setOutputMarkupId(true);
				wmcMainMenu.add(formTypeReport);

				BookmarkablePageLink searchRobFormA1Renewal = new BookmarkablePageLink("searchRobFormA1Renewal",
						SearchRobFormA1Renewal.class);
				wmcMainMenu.add(searchRobFormA1Renewal);

				BookmarkablePageLink searchRobFormA = new BookmarkablePageLink("searchRobFormA", SearchRobFormA.class);
				wmcMainMenu.add(searchRobFormA);

				BookmarkablePageLink searchRobFormB = new BookmarkablePageLink("searchRobFormB", SearchRobFormB.class);
				wmcMainMenu.add(searchRobFormB);

				BookmarkablePageLink searchRobFormC = new BookmarkablePageLink("searchRobFormC", SearchRobFormC.class);
				wmcMainMenu.add(searchRobFormC);

				BookmarkablePageLink SearchCmpDetails = new BookmarkablePageLink("SearchCmpDetails",
						SearchCmpHistoryPage.class);
				wmcMainMenu.add(SearchCmpDetails);

				BookmarkablePageLink readMyKadPage = new BookmarkablePageLink("readMyKadPage", ReadMyKadPage.class);
				wmcMainMenu.add(readMyKadPage);

				BookmarkablePageLink listRobUserOkuWorkingTrayPage = new BookmarkablePageLink(
						"listRobUserOkuWorkingTrayPage", ListRobUserOkuWorkingTrayPage.class);
				wmcMainMenu.add(listRobUserOkuWorkingTrayPage);

				BookmarkablePageLink listRobUserOkuSearchPage = new BookmarkablePageLink("listRobUserOkuSearchPage",
						ListRobUserOkuSearchPage.class);
				wmcMainMenu.add(listRobUserOkuSearchPage);

				listCheckInCounter.setVisible(true);
				if (Parameter.YES_NO_yes.equals(getCodeTypeWithValue(Parameter.LLP_CONFIG,
						Parameter.LLP_CONFIG_IS_FILTER_USER_LIST_SYS_ADMIN))) {
					boolean visibleType = false;
					llpLocaleMessagePage.setVisible(visibleType);
					llpPaymentFeePage.setVisible(visibleType);
					listLlpEmailLogPage.setVisible(visibleType);
					lLPFileAttachmentPage.setVisible(visibleType);
					llpParameterPage.setVisible(visibleType);
					gafConfigPage.setVisible(visibleType);
					healthCheckPage.setVisible(visibleType);
					emailBlast.setVisible(visibleType);
					listCollectionCounter.setVisible(visibleType);
					paymentCheckWithEghl.setVisible(visibleType);
					searchErrorLogCheckPage.setVisible(visibleType);
					searchSSMAuditLogPage.setVisible(visibleType);

					formTypeReport.setVisible(visibleType);
					nameTypeReport.setVisible(visibleType);
					robFormAStatistic.setVisible(visibleType);

					myCardTransaction.setVisible(visibleType);

					String userFilterComtrac = getCodeTypeWithValue(Parameter.LLP_CONFIG,
							Parameter.LLP_CONFIG_USER_LIST_SYS_ADMIN);
					if (StringUtils.isNotBlank(userFilterComtrac)) {
						List<String> userList = Arrays.asList(StringUtils.split(userFilterComtrac, ","));
						if (userList.contains(UserEnvironmentHelper.getLoginName())) {
							visibleType = true;
							llpLocaleMessagePage.setVisible(visibleType);
							llpPaymentFeePage.setVisible(visibleType);
							listLlpEmailLogPage.setVisible(visibleType);
							lLPFileAttachmentPage.setVisible(visibleType);
							llpParameterPage.setVisible(visibleType);
							gafConfigPage.setVisible(visibleType);
							healthCheckPage.setVisible(visibleType);
							emailBlast.setVisible(visibleType);
							listCollectionCounter.setVisible(visibleType);
							paymentCheckWithEghl.setVisible(visibleType);
							searchErrorLogCheckPage.setVisible(visibleType);
							searchSSMAuditLogPage.setVisible(visibleType);

							formTypeReport.setVisible(visibleType);
							nameTypeReport.setVisible(visibleType);
							robFormAStatistic.setVisible(visibleType);

							myCardTransaction.setVisible(visibleType);
						}
					}

					String powerUserCategory = getCodeTypeWithValue(Parameter.LLP_CONFIG,
							Parameter.LLP_CONFIG_LIST_SYS_POWER_USER);
					if (StringUtils.isNotBlank(powerUserCategory)) {
						List<String> userList = Arrays.asList(StringUtils.split(powerUserCategory, ","));
						if (userList.contains(UserEnvironmentHelper.getLoginName())) {
							visibleType = true;
							myCardTransaction.setVisible(visibleType);
							paymentCheckWithEghl.setVisible(visibleType);
						}
					}
				}

			}
		}
		wmcMainMenu.add(new BookmarkablePageLink("guidelinePage", GuidelinePage.class));

		duration = System.currentTimeMillis() - duration;
		setIfLoginPart(duration);
	}

	public void setIfLoginPart(long duration) {
		String welcomeMsgId = "base.welcome";
		ValueMap map = new ValueMap();
		if (UserEnvironmentHelper.getUserenvironment() == null) {
			map.put("fullName", "" + " in " + duration);
			SSMLabel welcomeLbl = new SSMLabel(welcomeMsgId, "");
			wmc.add(welcomeLbl);
			wmc.setVisible(false);
		} else {
			map.put("fullName", UserEnvironmentHelper.getUserenvironment().getFullName() + " in " + duration);
			StringResourceModel labelModel = new StringResourceModel(welcomeMsgId, this, new Model<ValueMap>(map));
			SSMLabel welcomeLbl = new SSMLabel(welcomeMsgId, labelModel);
			wmc.add(welcomeLbl);
			wmc.setVisible(true);
		}

	}

	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public String getLocaleMsg(String key) {
		return new StringResourceModel(key, this, null).getString();
	}

	public String getLocaleMsg(String key, String... params) {
		StringResourceModel labelModel = null;
		if (params != null && params.length > 0) {
			ValueMap map = new ValueMap();
			for (int i = 0; i < params.length; i++) {
				map.put("param" + i, params[i]);
			}
			labelModel = new StringResourceModel(key, this, new Model<ValueMap>(map));
		} else {
			labelModel = new StringResourceModel(key, this, null);
		}

		String keyValue = key;// + " : NOT DEFINED in properties files";
		try {
			keyValue = labelModel.getString();
		} catch (Exception e) {

		}
		return keyValue;
	}

	private void generateMenuInternal(List<MenuItem> primaryMenuList) {
		// USER REGISTRATION
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
////		MenuItem menuLlpProductInfo = new MenuItem("LLP Profile", LlpProductclass);
////		primaryMenuList.add(menuLlpProductInfo);
//		
//		// LLP Supply Info
//		MenuItem menuLlpSupplyinfo = new MenuItem("Supply Info");
//		primaryMenuList.add(menuLlpSupplyinfo);
//
//		List<MenuItem> subMenuSupplyinfo = new ArrayList<MenuItem>();
//		MenuItem menuBuySupplyInfo = new MenuItem("LLP Profile", LlpProductclass);
////		MenuItem menuListSupplyInfo = new MenuItem("List Supply Info Transaction", ListLlpSupplyInfoHdrPage.class);
//		subMenuSupplyinfo.add(menuBuySupplyInfo);
////		subMenuSupplyinfo.add(menuListSupplyInfo);
//		menuLlpSupplyinfo.setSubMenuItemList(subMenuSupplyinfo);
//		
//		//*********************************************************************************************************************************

		// PAYMENT
		MenuItem menuPayment = new MenuItem("Payment");
		primaryMenuList.add(menuPayment);

		List<MenuItem> subMenuPayment = new ArrayList<MenuItem>();
		MenuItem subMenuPaymentTransaction = new MenuItem("Payment Transaction Report",
				ListPaymentTransactionPage.class);
		MenuItem subMenuCollectionCounter = new MenuItem("Collection Counter", UserCheckinPage.class);
		MenuItem subMenuCollectionVerification = new MenuItem("Collection Verification",
				ListCollectionVerification.class);
		subMenuPayment.add(subMenuPaymentTransaction);
		subMenuPayment.add(subMenuCollectionCounter);
		menuPayment.setSubMenuItemList(subMenuPayment);

		// *********************************************************************************************************************************

		// Control Panel
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

		// ROB MENU
		MenuItem menuBizReg = new MenuItem("MyBiz Services");
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
		LlpParametersService parametersService = (LlpParametersService) getService(
				LlpParametersService.class.getSimpleName());
		return parametersService.findByActiveCodeType(codeType);
	}

	public String getCodeTypeWithValue(String codeType, String value) {
		LlpParametersService parametersService = (LlpParametersService) getService(
				LlpParametersService.class.getSimpleName());
		String codeValue = parametersService.findByCodeTypeValue(codeType, value);
		if (codeValue == null) {
			return value;
		} else {
			return codeValue;
		}
	}

	public abstract String getPageTitle();

	public List<Integer> getDropdownYear() {
		List<Integer> year = new ArrayList<Integer>();
		Integer start = Calendar.getInstance().get(Calendar.YEAR);
		Integer end = start - Parameter.YEAR_GAP;
		if (end < 2016) {
			end = 2016;
		}
		for (Integer i = start; i >= end; i--) {
			year.add(i);
		}

		return year;
	}

	public String getIpAddress() {
		HttpServletRequest request = (HttpServletRequest) getRequestCycle().getRequest().getContainerRequest();
		return WicketUtils.getIpAddress(request, getSession());
	}

	public String getContextPath() {
		HttpServletRequest request = (HttpServletRequest) getRequestCycle().getRequest().getContainerRequest();
		return request.getContextPath();
	}

	public void storeSuccessMsg(String msg) {
		List listSuccessMessage = (List) getSession().getAttribute(SUCCESS_MSG);
		if (listSuccessMessage == null) {
			listSuccessMessage = new ArrayList();
		}
		listSuccessMessage.add(msg);
		getSession().setAttribute(SUCCESS_MSG, (Serializable) listSuccessMessage);
	}

	public void storeSuccessMsgKey(String key) {
		String msg = resolve(key);
		storeSuccessMsg(msg);
	}

	public void storeErrorMsg(String msg) {
		List listErrorMessage = (List) getSession().getAttribute(ERROR_MSG);
		if (listErrorMessage == null) {
			listErrorMessage = new ArrayList();
		}
		listErrorMessage.add(msg);
		getSession().setAttribute(ERROR_MSG, (Serializable) listErrorMessage);
	}

	public void storeErrorMsgKey(String key, String... param) {
		String msg = resolve(key, param);
		storeErrorMsg(msg);
	}

	public String generateNotyAlert(String alertText, String alertType, AjaxRequestTarget target) {

		String cssClass = "";
		String text = "";
		String header = "";

		if (alertType == null) {
			alertType = "alert";
		}

		if (alertText == null) {
			alertText = "Test Notification";
		}

		if (Parameter.ALERT_TYPE_alert.equalsIgnoreCase(alertType)) {
			cssClass = "fa fa-info text-alert";
			header = "<b>ALERT!</b><br>";
		} else if (Parameter.ALERT_TYPE_success.equalsIgnoreCase(alertType)) {
			cssClass = "fa fa-check-circle text-success";
			header = "<b>SUCCESS!</b><br>";
		} else if (Parameter.ALERT_TYPE_warning.equalsIgnoreCase(alertType)) {
			cssClass = "fa fa-exclamation-circle text-warning";
			header = "<b>WARNING!</b><br>";
		} else if (Parameter.ALERT_TYPE_error.equalsIgnoreCase(alertType)) {
			cssClass = "fa fa-exclamation-circle text-error";
			header = "<b>ERROR!</b><br>";
		} else if (Parameter.ALERT_TYPE_info.equalsIgnoreCase(alertType)) {
			cssClass = "fa fa-info-circle text-info";
			header = "<b>INFO!</b><br>";
		}

		alertText = header + resolve(alertText);

		text = "<div class=\"activity-item\" style=\"font-size: 14px\"> <i class=\"" + cssClass
				+ "\"></i> <div class=\"activity\">" + alertText + "</div></div>";

		String js = "$(document).ready(function () {";
		js += "noty({";
		js += "type: '" + alertType + "',";
		js += "text: '" + text + "',";
		js += "dismissQueue: true, progressBar : true, timeout : 5000, layout : 'topRight', closeWith : ['click'], theme : 'defaultTheme', maxVisible  : 10,";
		js += "animation   : {";
		js += "open  : 'animated bounceInRight',";
		js += "close : 'animated bounceOutRight',";
		js += "easing: 'swing',";
		js += "speed : 500";
		js += "}});});";

		return js;
	}

	public String getUrl(HttpServletRequest request) throws ServletException {
		String url = request.getRequestURL().toString();
		return url;
	}

//	@Override
//	public String getAjaxIndicatorMarkupId() {
//		String busyViel= getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_BUSY_VEIL);
//		if(Parameter.YES_NO_yes.equals(busyViel)) {
//			return "ajaxveil2123";
//		}
//		return "noajaxviel";
//		
//	}

	public double getGSTRate(String gstCode) {
		return Double.valueOf(getCodeTypeWithValue(Parameter.PAYMENT_GST_CODE, gstCode));
	}

	public AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc) {
		return generateAjaxConfirm(component, confirmTitle, confirmDesc, false);
	}

	public AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc,
			boolean confirmOnlyOnUntick) {
		return WicketUtils.generateAjaxConfirm(component, confirmTitle, confirmDesc, confirmOnlyOnUntick);
	}

	public AjaxCallListener generateAjaxErrorAlert(String alertTitle, String alertDesc) {
		return WicketUtils.generateAjaxErrorAlert(alertTitle, alertDesc);
	}

	public String generateAjaxErrorAlertScript(String alertTitle, String alertDesc) {
		return WicketUtils.generateAjaxErrorAlertScript(alertTitle, alertDesc);
	}

	public String generateAjaxSuccessAlertScript(String alertTitle, String alertDesc) {
		return WicketUtils.generateAjaxSuccessAlertScript(alertTitle, alertDesc);
	}

	public String generateAjaxSuccessAlertScript(String alertTitle, String alertDesc, String addScriptAfterClose) {
		return WicketUtils.generateAjaxSuccessAlertScript(alertTitle, alertDesc, addScriptAfterClose);
	}

	public Panel getRecieptPanel(PageParameters param) {
		return getRecieptPanel("viewPaymentReceipt", param);
	}

	public Panel getRecieptPanel(String id, PageParameters param) {
		String transId = param.get("transId").toString();
		boolean isSSTFormat = false;
		if (StringUtils.isNotBlank(transId)) {
			LlpPaymentReceipt llpPaymentReceipt = ((LlpPaymentReceiptService) getService(
					LlpPaymentReceiptService.class.getSimpleName())).find(transId);
			String dateSSTFormat = getCodeTypeWithValue(Parameter.LLP_CONFIG,
					Parameter.LLP_CONFIG_DATE_SST_RECEIPT_FORMAT);

			if (StringUtils.isNotBlank(dateSSTFormat)) {
				try {
					Date sstDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateSSTFormat);

					if (!llpPaymentReceipt.getCreateDt().before(sstDate)) {
						isSSTFormat = true;
					}
				} catch (Exception e) {
				}
			}

		}

		Panel outputPanel = null;

		if (isSSTFormat) {
			outputPanel = (new ViewPaymentReceiptSSTPanel(id, param));
		} else {
			outputPanel = (new ViewPaymentReceiptPanel2(id, param));
		}

		if (outputPanel == null) {
			String temp = "";
		}

		return outputPanel;
	}

	public boolean displayFloatingCart() {
		return true;
	}

	public boolean addToCart(String entityType, String entityNo, String entityName, String prodType, String prodCode,
			double price, String prodLocale, AjaxRequestTarget target) {

		synchronized (getSession()) {
			SupplyInfoTransHdr hdr = getCartHdr();
			List<SupplyInfoTransDtl> list = hdr.getListSupplyInfoTransDtl();

			if (StringUtils.isBlank(hdr.getTransCode())) {
				supplyInfoTransHdrService.insert(hdr);

				Cookie languageCookie = new Cookie("cartInfoHdrID", hdr.getTransCode());
				((WebResponse) getResponse()).addCookie(languageCookie);

			}

			Set<String> hashCartCode = new HashSet<String>();

			for (int i = 0; i < list.size(); i++) {
				SupplyInfoTransDtl dtlTmp = list.get(i);
				hashCartCode.add(
						dtlTmp.getEntityType() + dtlTmp.getEntityNo() + dtlTmp.getProdCode() + dtlTmp.getProdLocale());
			}

			boolean hasInsert = false;
			if (Parameter.SUPPLY_INFO_PROD_LOCALE_BOTH.equals(prodLocale)) {
				SupplyInfoTransDtl dtl = new SupplyInfoTransDtl(entityType, entityNo, entityName, prodType, prodCode,
						price, Parameter.SUPPLY_INFO_PROD_LOCALE_ENG);
				dtl.setHdrTransCode(hdr.getTransCode());

				if (!hashCartCode.contains(entityType + entityNo + prodCode + Parameter.SUPPLY_INFO_PROD_LOCALE_ENG)) {
					supplyInfoTransDtlService.insert(dtl);
					list.add(dtl);
					hasInsert = true;
				}
				SupplyInfoTransDtl dtl2 = new SupplyInfoTransDtl(entityType, entityNo, entityName, prodType, prodCode,
						price, Parameter.SUPPLY_INFO_PROD_LOCALE_BM);
				dtl2.setHdrTransCode(hdr.getTransCode());

				if (!hashCartCode.contains(entityType + entityNo + prodCode + Parameter.SUPPLY_INFO_PROD_LOCALE_BM)) {
					supplyInfoTransDtlService.insert(dtl2);
					list.add(dtl2);
					hasInsert = true;
				}

			} else {
				SupplyInfoTransDtl dtl = new SupplyInfoTransDtl(entityType, entityNo, entityName, prodType, prodCode,
						price, prodLocale);
				dtl.setHdrTransCode(hdr.getTransCode());

				if (!hashCartCode.contains(entityType + entityNo + prodCode + prodLocale)) {
					supplyInfoTransDtlService.insert(dtl);
					list.add(dtl);
					hasInsert = true;
				}
			}

			getSession().setAttribute("cartInfoHdr_", (Serializable) getCartHdr());

			cartTotal.setDefaultModelObject(new String(list.size() + ""));
			target.add(cartTotal);
			recalculateAndupdateHeaderAmt();

			return hasInsert;
		}
	}

	public void recalculateAndupdateHeaderAmt() {
		SupplyInfoTransHdr hdr = getCartHdr();
		List<SupplyInfoTransDtl> listDtl = hdr.getListSupplyInfoTransDtl();

		double taxAmt = 0, totalAmt = 0;
		for (int i = 0; i < listDtl.size(); i++) {
			totalAmt += listDtl.get(i).getAmt();
			taxAmt += listDtl.get(i).getTaxAmt();
		}
		hdr.setTotalAmt(totalAmt);
		hdr.setTaxAmt(taxAmt);

		supplyInfoTransHdrService.update(hdr);

	}

	public void removeAllFromCart(AjaxRequestTarget target) {

		synchronized (getSession()) {

			SupplyInfoTransHdr infoTransHdr = getCartHdr();
			List<SupplyInfoTransDtl> list = infoTransHdr.getListSupplyInfoTransDtl();

			supplyInfoTransDtlService.deleteByHdrCode(getCartHdr().getTransCode());
			list.clear();
			getSession().setAttribute("cartInfoHdr_", (Serializable) infoTransHdr);

			cartTotal.setDefaultModelObject(new String(list.size() + ""));
			target.add(cartTotal);

			recalculateAndupdateHeaderAmt();
		}
	}

	public void removeFromCart(SupplyInfoTransDtl dtlToBeRemove, AjaxRequestTarget target) {

		synchronized (getSession()) {
			SupplyInfoTransHdr infoTransHdr = getCartHdr();
			List<SupplyInfoTransDtl> list = infoTransHdr.getListSupplyInfoTransDtl();

			supplyInfoTransDtlService.delete(dtlToBeRemove);

			list.remove(dtlToBeRemove);

			getSession().setAttribute("cartInfoHdr_", (Serializable) infoTransHdr);

			cartTotal.setDefaultModelObject(new String(list.size() + ""));
			target.add(cartTotal);

			recalculateAndupdateHeaderAmt();
		}
	}

	public SupplyInfoTransHdr getCartHdr() {

		SupplyInfoTransHdr hdr = (SupplyInfoTransHdr) getSession().getAttribute("cartInfoHdr_");
		if (hdr == null) {

			if (((WebRequest) getRequest()).getCookie("cartInfoHdrID") != null) {
				String cartInfoHdrID = ((WebRequest) getRequest()).getCookie("cartInfoHdrID").getValue();

				SupplyInfoTransHdr hdrTmp = supplyInfoTransHdrService.findAllById(cartInfoHdrID);
				if (hdrTmp != null && "system".equals(hdrTmp.getOwnerBy())) {
					hdr = hdrTmp;
				}
			}

			if (hdr == null) {
				hdr = new SupplyInfoTransHdr();
				hdr.setStatus(Parameter.SUPPLY_INFO_TRANS_STATUS_DATA_ENTRY);
				hdr.setOwnerBy(UserEnvironmentHelper.getLoginName());
				hdr.setListSupplyInfoTransDtl(new ArrayList<SupplyInfoTransDtl>());
			}
		}

		getSession().setAttribute("cartInfoHdr_", (Serializable) hdr);
		return hdr;

	}

	public boolean displayAnimatedBackground() {
		return false;
	}

	public LlpFileData validateAndGenerateFileData(FileUploadField fileUpload, RepeatingView listError) {
		return validateAndGenerateFileData(fileUpload, listError, false);
	}

	public LlpFileData validateAndGenerateFileData(FileUploadField fileUpload, RepeatingView listError,
			boolean isNullDontValidate) {
		String label = resolve(fileUpload.getLabelKey());

		if (fileUpload.getFileUpload() == null) {
			if (isNullDontValidate) {
				return null;
			}
			listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.err.noAttachment", label)));
			return null;
		}

		byte[] byteData = fileUpload.getFileUpload().getBytes();

		boolean formatError = false;

		String splitExt[] = StringUtils.split(fileUpload.getInput(), ".");
		String fileExt = splitExt[splitExt.length - 1];
		String fileType = "";
		if (byteData.length > 3145728) {
			listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.err.exceedUploadSize", label)));
			formatError = true;
		} else {

			if (fileExt.endsWith("pdf")) {
				try {
					ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
					PDDocument document = PDDocument.load(bais);
					document.close();
					fileType = fileUpload.getFileUpload().getContentType();
				} catch (Exception e) {
					listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.err.notInPDF", label)));
					formatError = true;
				}
			} else {
				try {
					if (ImageIO.read(new ByteArrayInputStream(byteData)) == null) {
						throw new Exception();
					}
					fileType = fileUpload.getFileUpload().getContentType();
				} catch (Exception e) {
					listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.err.notInImage", label)));
					formatError = true;
				}
			}
		}
		if (!formatError) {
			LlpFileData fileData = new LlpFileData();
			fileData.setFileData(byteData);
			fileData.setFileDataType(fileType);
			return fileData;
		}
		return null;
	}

	public SSMLink generateDownloadLink(String downloadId, final LlpFileData llpFileData, final String fileName) {
		return new SSMLink(downloadId) {
			@Override
			public void onClick() {
				LlpFileData fileDataTmp = null;
				try {
					llpFileData.getFileData();
					fileDataTmp = llpFileData;
				} catch (LazyInitializationException e) {
					fileDataTmp = llpFileDataService.findById(llpFileData.getFileDataId());

				}

				final byte byteData[] = fileDataTmp.getFileData();
				final String contentType = fileDataTmp.getFileDataType();

				IResourceStream resourceStream = new AbstractResourceStreamWriter() {
					@Override
					public void write(OutputStream output) {
						try {
							output.write(byteData);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public String getContentType() {
						return contentType;
					}
				};
				String fileExt = "";

				MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
				try {
					MimeType type = allTypes.forName(contentType);
					fileExt = type.getExtension();

				} catch (Exception e) {

				}

				if (StringUtils.isBlank(fileExt)) {
					if (contentType.toLowerCase().indexOf("pdf") != -1) {
						fileExt = ".pdf";
					}
					if (contentType.toLowerCase().indexOf("image") != -1) {
						fileExt = "." + StringUtils.split(contentType, "/")[1];
					}
					if (contentType.toLowerCase().indexOf("text") != -1) {
						fileExt = ".txt";
					}
				}

				ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream)
						.setFileName(fileName + fileExt);
				rs.setCacheDuration(Duration.NONE);
				getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);
			}
		};
	}

}
