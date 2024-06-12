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
import com.ssm.llp.base.page.AuthenticatedWebPage;
import com.ssm.llp.base.page.BasePage;
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

public class EzBizBasePageMobile extends BasePageMobile  {
	public EzBizBasePageMobile() {
	}

	public String getPageTitle() {
		return "page.title.ezbiz";
	}
	@Override
	public String getLogoImage() {
		return "images/ezbizMobile.png";
	}
}

