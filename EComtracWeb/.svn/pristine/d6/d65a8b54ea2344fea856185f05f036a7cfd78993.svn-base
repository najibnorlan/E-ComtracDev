package com.ssm.common.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.html.panel.Panel;

import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.WicketUtils;

public class BasePanelMobile extends Panel 
{
    /**
     * Constructor
     */
    public BasePanelMobile(String id)
    {
    	super(id);
    }
    
    /**
     * Constructor
     */
    public BasePanelMobile()
    {
    	super(null);
    }
	
	public BaseService getService(String serviceId){
		return WicketApplication.getServiceNew(serviceId);
	}
	
	
	public List<LlpParameters> getCodeType(String codeType){
		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		return parametersService.findByActiveCodeType(codeType);
	}
	
	public String getIpAddress(){
		
    	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
    	String ipAdd = WicketUtils.getIpAddress(request, getSession());
    	
    	return ipAdd;
	}
}
