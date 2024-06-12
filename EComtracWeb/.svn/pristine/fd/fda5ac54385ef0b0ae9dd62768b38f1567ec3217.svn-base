package com.ssm.common.mobile;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.LoginPanel.SignInForm;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMCaptchaResource;
import com.ssm.llp.base.wicket.component.SSMForm;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.page.AfterLoginLlp;
import com.ssm.llp.mod1.page.GenerateLlpUserProfilePage;
import com.ssm.llp.mod1.page.VerificationLlpUserProfilePage;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class MainPageMobile extends BasePageMobile{
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	public MainPageMobile() {
		String isHideAds =llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_HIDE_ADS);
		if(Parameter.YES_NO_yes.equals(isHideAds)){
			AjaxEventBehavior event = new AjaxEventBehavior("onload") {
			    @Override
			    protected void onEvent(final AjaxRequestTarget target) {
			        target.appendJavaScript("sendHideAdsParent();");
			    }
			};
			add(event);
		}
		
		BookmarkablePageLink ezBizLink = new BookmarkablePageLink("ezBizLink", HomePageMobile.class);
		add(ezBizLink);
		
		BookmarkablePageLink eSearchLink = new BookmarkablePageLink("eSearchLink", ESearchMainPageMobile.class);
		add(eSearchLink);
	}

	@Override
	public String getPageTitle() {
		String titleKey = "page.title.ssmanywhere";
		return titleKey;
	}
	
}
