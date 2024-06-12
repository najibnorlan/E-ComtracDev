package com.ssm.llp.base.page;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.page.AfterLoginInternal;
import com.ssm.llp.mod1.page.AfterLoginLlp;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class MyKadLogin extends WebPage implements Serializable, IAjaxIndicatorAware{
	
	private SSMMessagesFeedbackPanel feedbackPanel;
	 
	public MyKadLogin() {
		
		if(UserEnvironmentHelper.getUserenvironment() != null){
			if(UserEnvironmentHelper.isInternalUser()){
				throw new RestartResponseAtInterceptPageException(AfterLoginInternal.class);
			}else{
				throw new RestartResponseAtInterceptPageException(AfterLoginLlp.class);
			}
		}
		
		HttpServletRequest  request=(HttpServletRequest)getRequestCycle().getRequest().getContainerRequest();
		
		String uri = "";
		String text = "";
		
		SSMLabel serverType = new SSMLabel("serverType", "");
		serverType.setOutputMarkupPlaceholderTag(true);
		serverType.setOutputMarkupId(true);
		serverType.setVisible(false);
		
		add(serverType);
		
		try {
			uri =  getUrl(request);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		if(!StringUtils.isBlank(uri)){
			if(uri.contains(Parameter.URL_TYPE_dev) || uri.contains(Parameter.URL_TYPE_local)){
				text = "DEVELOPMENT ENVIRONMENT";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);
			}else if(uri.contains(Parameter.URL_TYPE_stg)){
				text = "STAGING ENVIRONMENT";
				serverType.setDefaultModelObject(text);
				serverType.setVisible(true);
			}
		}
		
		/*add(new LoginPanel("loginPanel"));		*/
		add(new LoginMyKadPanel("loginMyKadPanel"));	
		feedbackPanel = new SSMMessagesFeedbackPanel("feedback");
 		feedbackPanel.setOutputMarkupId(true);
 		add(feedbackPanel);
 		
 		// Ni tuk footer
		add(new BookmarkablePageLink("privacyPolicy", PrivacyPolicy.class));
		add(new BookmarkablePageLink("securityPolicy", SecurityPolicy.class));
		add(new BookmarkablePageLink("homePage", HomePage.class));
	}
	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	@Override
	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}
	
	public String getUrl(HttpServletRequest request) throws ServletException {
		String url = request.getRequestURL().toString();
		return url;
	}
}
