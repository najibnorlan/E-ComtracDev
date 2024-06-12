package com.ssm.common.v2;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.html.panel.Panel;

import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class BasePanel extends Panel{

	
	private SSMLabel ssmLabel;
	
	public BasePanel(String id) {
		super(id);
	}
    
    /**
     * Constructor
     */
    public BasePanel()
    {
    	super(null);
    	ssmLabel = new SSMLabel("errorLbl");
    }

    

	public String getIpAddress(){
		
    	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
    	String ipAdd = WicketUtils.getIpAddress(request, getSession());
    	
    	
    	return ipAdd;
	}
}
