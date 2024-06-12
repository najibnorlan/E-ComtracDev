package com.ssm.llp.base.page;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;


public abstract class BaseFrame extends WebPage{
	
	public final FeedbackPanel feedbackPanel;
	
	@SpringBean(name = "LlpParametersService")
	public LlpParametersService parametersService;
	
	public BaseFrame() {
		
//		//Ni tuk page title
//		String myPageTitle = "";
//		if (getPageTitle() != null)
//			myPageTitle = getPageTitle();
//		else
//			myPageTitle = "page.title.default";
//		
//		add(new SSMLabel("pageTitle", new StringResourceModel(myPageTitle, this, null)));
		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
	}
	




	public BaseService getService(String serviceId){
		return WicketApplication.getServiceNew(serviceId);
	}
	
	
	public List getCodeType(String codeType){
		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		return parametersService.findByActiveCodeType(codeType);
	}
	
	public String getCodeTypeWithValue(String codeType, String value) {
		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		String codeValue = parametersService.findByCodeTypeValue(codeType, value);
		if(codeValue == null){
			return value;
		}else{
			return codeValue;
		}
	}
	
	public abstract String getPageTitle();





	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}
	
	public String getIpAddress(){
		
    	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
    	String ipAdd = WicketUtils.getIpAddress(request,getSession());
    	
    	return ipAdd;
	}
	

}
