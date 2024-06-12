package com.ssm.llp.base.page;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.ssm.ezbiz.service.ComtracSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.page.AfterLoginInternal;
import com.ssm.llp.mod1.page.GuidelinePage;
import com.ssm.llp.mod1.page.UserTncPage;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class HomePage extends WebPage implements Serializable { // , IAjaxIndicatorAware {

	@SpringBean(name = "ComtracSchedulerService")
	private ComtracSchedulerService comtracSchedulerService;

	private SSMMessagesFeedbackPanel feedbackPanel;

	public static final String CLIENT_LOCAL_IP_ADDR = "CLIENT_LOCAL_IP_ADDR";

	@SpringBean(name = "LlpFileUploadService")
	private LlpFileUploadService llpFileUploadService;

	public HomePage() {
		this(false);
	}

	public HomePage(boolean isExternalIDPairing) {
		SignInSession signInSession = (SignInSession) getSession();
		boolean isExtIface = false;
		if (null != getSession() && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
			isExtIface = true;
		}
		// Tutup comtracSchedulerService ni bila buat testing
		comtracSchedulerService.generateCertificate();

		DynamicImageResource bannerImageResource = new DynamicImageResource() {

			@Override
			protected byte[] getImageData(Attributes arg0) {
				LlpFileUpload fileUpload = llpFileUploadService.findByFileCode("ECOMTRAC_BANNER");
				return fileUpload.getFileData();
			}
		};
		NonCachingImage bannerImage = new NonCachingImage("bannerImage", bannerImageResource);
		bannerImage.setOutputMarkupId(true);
		add(bannerImage);

		if (UserEnvironmentHelper.getUserenvironment() != null) {
			// IF Login Type Mampu clear signIn Session

			if (UserEnvironmentHelper.isInternalUser()) {
				throw new RestartResponseAtInterceptPageException(AfterLoginInternal.class);
			} else {
				if (isExtIface) {
					// redirectToInterceptPage(new ExtInterface("already
					// pair"));
					// return;
				} else {
					throw new RestartResponseAtInterceptPageException(GuidelinePage.class);
				}
			}
		}
		// bannerImage
		SSMLabel isFromExternal = new SSMLabel("isFromExternal");
		add(isFromExternal);

		if (isExtIface) {
			isFromExternal.setVisible(false);
		}

		HttpServletRequest request = (HttpServletRequest) getRequestCycle().getRequest().getContainerRequest();

		String uri = "";
		String text = "";

		SSMLabel serverType = new SSMLabel("serverType", "");
		serverType.setOutputMarkupPlaceholderTag(true);
		serverType.setOutputMarkupId(true);
		serverType.setVisible(false);

		add(serverType);

		try {
			uri = getUrl(request);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		BookmarkablePageLink internalLogin = new BookmarkablePageLink("ssmLogin", SsmLogin.class);
		add(internalLogin);
		internalLogin.setVisible(false);
		internalLogin.setOutputMarkupId(true);
		internalLogin.setOutputMarkupPlaceholderTag(true);

		// BookmarkablePageLink myKadLogin = new
		// BookmarkablePageLink("myKadLogin", MyKadLogin.class);
		// add(myKadLogin);

		if (!StringUtils.isBlank(uri)) {
			if (uri.contains(Parameter.URL_TYPE_dev) || uri.contains(Parameter.URL_TYPE_local) || uri.contains("192")
					|| uri.contains(Parameter.URL_TYPE_dev_comtrac)) {
				text = "DEVELOPMENT ENVIRONMENT";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);

				internalLogin.setVisible(true);
				add(new LoginPanel("loginPanel"));
			} else if (uri.contains(Parameter.URL_TYPE_stg)) {
				text = "STAGING ENVIRONMENT";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);

				internalLogin.setVisible(true);
				add(new LoginPanel("loginPanel"));

			} else if (uri.contains("e-comtrac")) {
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);

				internalLogin.setVisible(true);
				add(new LoginPanel("loginPanel"));
			} else if (uri.contains("ezbizbo.ssm.com.my")) {
				text = "FOR BACK OFFICE USE ONLY";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);
				add(new LoginInternalPanel("loginPanel"));
			} else if (uri.contains("ezbizsh.ssm.com.my") || uri.contains("10.7.34.231")) {
				text = "FOR BACK OFFICE USE ONLY";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);
				add(new LoginPanel("loginPanel"));
			} else {
				add(new LoginPanel("loginPanel"));
			}
		} else {
			add(new LoginPanel("loginPanel"));
		}

		/* add(new LoginInternalPanel("loginInternalPanel")); */
		feedbackPanel = new SSMMessagesFeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		// Ni tuk footer
//		add(new BookmarkablePageLink("privacyPolicy", PrivacyPolicy.class));
		add(new BookmarkablePageLink("userTnC", UserTncPage.class));
		add(new BookmarkablePageLink("securityPolicy", SecurityPolicy.class));

		// Ni tuk footer

		// SSMLink setCallFromMampu = new SSMLink("setCallFromMampu") {
		// @Override
		// public void onClick() {
		// SignInSession signInSession = (SignInSession)getSession();
		// signInSession.setLoginType(Parameter.LOGIN_TYPE_interface);
		//
		// setResponsePage(new HomePage(true));
		// }
		// };
		// add(setCallFromMampu);
		//
		//
		// SSMLink invalidateSession = new SSMLink("invalidateSession") {
		// @Override
		// public void onClick() {
		// getSession().invalidate();
		// }
		// };
		// add(invalidateSession);

		final AbstractDefaultAjaxBehavior setIpBehavior = new AbstractDefaultAjaxBehavior() {
			@Override
			protected void respond(AjaxRequestTarget target) {

				SignInSession signInSession = (SignInSession) getSession();
				boolean isExtIface = false;
				if (null != getSession() && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
					isExtIface = true;
				}

				final StringValue ipAddress = RequestCycle.get().getRequest().getQueryParameters()
						.getParameterValue("ipAddress");
				StringValue isPopUp = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("isPopUp");

//				System.out.println("IPAddress : " + ipAddress.toString());
//				System.out.println("isPopUp : " + isPopUp.toString());

				HttpSession session = ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest()
						.getSession();
				session.setAttribute(CLIENT_LOCAL_IP_ADDR, ipAddress.toString());
				getSession().setAttribute(CLIENT_LOCAL_IP_ADDR, ipAddress.toString());

				if (isExtIface) {
					if (!"true".equals(isPopUp.toString())) {
						System.out.println("Session Invalidate");
						getSession().invalidate();
						setResponsePage(HomePage.class);
					} else {
						// Check If Already Login if already push to alreadyPair
						// redirectToInterceptPage(new ExtInterface("Invalid
						// Page"));
						// return;
					}
				}

			}

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.render(
						JavaScriptHeaderItem
								.forScript(
										String.format("setSumb=%s",
												getCallbackFunction(CallbackParameter.explicit("ipAddress"),
														CallbackParameter.explicit("isPopUp"))),
										"CallFromJavascriptBehavior"));
			}
		};

		add(setIpBehavior);

	}

	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}

	public String getUrl(HttpServletRequest request) throws ServletException {
		String url = request.getRequestURL().toString();
		return url;
	}

}
