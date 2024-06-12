package com.ssm.llp.base.page;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpAppsvrNode;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpAppsvrNodeService;
import com.ssm.llp.base.common.service.SSMDistributedService;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMTextField;

public class LlpCluster extends WebPage implements Serializable {
	@SpringBean(name="LlpAppsvrNodeService")
	private LlpAppsvrNodeService llpAppsvrNodeService;
	
	private static String callerId;
	public LlpCluster(){
		HttpServletRequest  request=(HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
        String ipAddress= WicketUtils.getIpAddress(request, getSession());
        
        //Process Request
        String serviceId = request.getParameter("serviceId");
        if(StringUtils.isNotBlank(serviceId)){
        	String param = request.getParameter("param");
//        	SSMDistributedService ssmDistributedService =  (SSMDistributedService) WicketApplication.getServiceObject(serviceId);
//        	ssmDistributedService.processDistributed(param);
        }
        
	}
	
}
