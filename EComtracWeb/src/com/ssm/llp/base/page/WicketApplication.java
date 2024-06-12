package com.ssm.llp.base.page;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.core.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.page.CouldNotLockPageException;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.PageRequestHandlerTracker;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ssm.common.mobile.HomePageMobile;
import com.ssm.common.mobile.MainPageMobile;
import com.ssm.ezbiz.errorlog.MyInternalErrorPage;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpLocaleMessage;
import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpLocaleMessageService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.utils.SSMDebugStream;
import com.ssm.llp.base.utils.SSMErrorStream;
import com.ssm.llp.mod1.page.Page2;
import com.ssm.llp.wicket.SSMResourceLoader;
import com.ssm.supplyinfo.SupplyInfoMainPage;

/**
 * Application object for your web application.
 * <p/>
 * If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication {
	public static boolean PROXY_ON = true;
	private SSMResourceLoader ssmResourceLoader;
	
	private static LlpLocaleMessageService llpLocaleMessageService;
	public static final String THEME_SUPPLY_INFO = "SUPPLY_INFO";
	public static final String THEME_EZBIZ = "EZBIZ";
	
	public static final String SELECTED_THEME = THEME_EZBIZ;
	
	public Class getHomePage() {
		String userAgent = WebSession.get().getClientInfo().getUserAgent();
		if(isMobileBrowser(userAgent)){
			return MainPageMobile.class;
		}
		if(SELECTED_THEME.equals(THEME_SUPPLY_INFO)) {
			return SupplyInfoMainPage.class;
		}
        return HomePage.class;
    }

    private ApplicationContext getContext() {
        return WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
    }
    
    public static Object getBean(String serviceId) {
        return get().getContext().getBean(serviceId);
    }
    
    public static BaseService getServiceNew(String serviceId) {
        return (BaseService) get().getContext().getBean(serviceId);
    }
    
    public static Object getServiceObject(String serviceId) {
        return get().getContext().getBean(serviceId);
    }
    

    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }
    
    public static SSMResourceLoader getResourceLoader() {
        return get().getSsmResourceLoader();
    }
    
    public static String resolve(String key, String... params){
    	String keyValue = key + " : NOT DEFINED in properties files";
    	try {
    		Localizer localizer = WicketApplication.get().getResourceSettings().getLocalizer();
    		keyValue = localizer.getString(key, null);
		} catch (Exception e) {
			LlpLocaleMessage llpLocaleMessage = llpLocaleMessageService.findByKeyNLocale(key, null);
			if(llpLocaleMessage!=null){
				keyValue = llpLocaleMessage.getMsg();
			}
		}
    	
    	
    	
    	try {
    		
			if(params != null && params.length>0){
				for (int i = 0; i < params.length; i++) {
					keyValue = StringUtils.replace(keyValue, "${param"+i+"}", params[i]);
				}
			}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
		
		return keyValue;
    }
    
   
    protected void init() {
        super.init();
        
        SpringComponentInjector springInjector = new SpringComponentInjector(this);
        super.getComponentInstantiationListeners().add(springInjector);
//        getPageSettings().addComponentResolver(new SSMWicketMessageResolver()) ;
        llpLocaleMessageService = (LlpLocaleMessageService) WicketApplication.getServiceNew(LlpLocaleMessageService.class.getSimpleName());
        
//        get().getResourceSettings().getLocalizer().gets
        getRequestCycleListeners().add(new PageRequestHandlerTracker());

        getResourceSettings().setResourceStreamLocator(new CustomResourceStreamLocator());
        
        mountPage("/MainPageMobile", MainPageMobile.class);
        mountPage("/SamlHomePage", SamlHomePage.class);
//        mountPage("/HomePageMobile", HomePageMobile.class);
        mountPage("/myssmonline", MySSMOnline.class);
        mountPage("/PaymentDetail", PaymentDetailPage.class);
        mountPage("/LlpCluster", LlpCluster.class);
        mountPage("/Page2", Page2.class);
        mountPage("/fileAttach", DownloadFile.class);
        mountPage("/extInterface", ExtInterface.class);
        
        ssmResourceLoader = new SSMResourceLoader();
        getResourceSettings().getStringResourceLoaders().add(ssmResourceLoader); 
        getRequestCycleSettings().setTimeout(Duration.minutes(3));
        
        getRequestCycleListeners().add(new IRequestCycleListener() {
			
			@Override
			public void onUrlMapped(RequestCycle arg0, IRequestHandler arg1, Url arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRequestHandlerScheduled(RequestCycle arg0,
					IRequestHandler arg1) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onRequestHandlerResolved(RequestCycle arg0, IRequestHandler arg1) {
				SignInSession session = (SignInSession) SignInSession.get();
				try {
					UserEnvironment ue = (UserEnvironment) session.getAttribute("UserEnvironment");
					UserEnvironmentHelper.setUserEnvironment(ue);
				} catch (Exception e) {
						
				}
			}
			
			@Override
			public void onRequestHandlerExecuted(RequestCycle arg0, IRequestHandler arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onExceptionRequestHandlerResolved(RequestCycle arg0,
					IRequestHandler arg1, Exception arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public IRequestHandler onException(RequestCycle arg0, Exception arg1) {
				HttpSession session = ((ServletWebRequest)arg0.getRequest()).getContainerRequest().getSession();
				session.setAttribute("errorExp", arg1);
				if(arg1 instanceof CouldNotLockPageException || arg1 instanceof PageExpiredException) {
					return new RenderPageRequestHandler(new PageProvider(new MyInternalErrorPage(arg1)));
				}
				return null;
			}
			
			@Override
			public void onEndRequest(RequestCycle arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDetach(RequestCycle arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBeginRequest(RequestCycle arg0) {
				
			}
		});
        
        SSMDebugStream.activate();
        SSMErrorStream.activate();
//        try {
//        	String beanName[] = getContext().getBeanDefinitionNames();
//        	System.out.println("BeanName:");
//            for (int i = 0; i < beanName.length; i++) {
//				System.out.println(beanName[i]);
//			}
//        }
//        catch (Exception e) {
//            // expect an exception here if we try to create an existing table and bury it.
//        }
        
        
        // Register the authorization strategy
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy()
        {
            @Override
            public boolean isActionAuthorized(Component component, Action action)
            {
                // authorize everything
                return true;
            }

            @Override
            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
                Class<T> componentClass)
            {
                // Check if the new Page requires authentication (implements the marker interface)
                if (AuthenticatedWebPage.class.isAssignableFrom(componentClass))
                {
                    // Is user signed in?
                    if (((SignInSession)Session.get()).isSignedIn())
                    {
                        return true;
                    }

                    // Intercept the request, but remember the target for later.
                    // Invoke Component.continueToOriginalDestination() after successful logon to
                    // continue with the target remembered.
                    
                    String userAgent = WebSession.get().getClientInfo().getUserAgent();
                    
                    if(isMobileBrowser(userAgent)){
                    	throw new RestartResponseAtInterceptPageException(HomePageMobile.class);
                    }
                    
                    throw new RestartResponseAtInterceptPageException(getHomePage());
                }

                // okay to proceed
                return true;
            }
        });
        
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");//-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2
        checkIfProxyReqired();
        
        getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
    }
    
    private void checkIfProxyReqired() {
		try {
			LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
	        
			String proxyHost = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PROXY_URL);
			int proxyPort = Integer.parseInt(parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PROXY_PORT)); //your proxy server port
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);
			 
			URLConnection urlConn = null;
			BufferedReader reader = null;
			String response = "";
			String output = "";
			
			String urlOverHttps = "https://www.google.com";
			URL url = new URL(urlOverHttps);
			
			//Pass the Proxy instance defined above, to the openConnection() method
			urlConn = url.openConnection(httpProxy); 
			urlConn.connect();
			reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			response = reader.readLine();
			while (response!=null) {
			    output+= response;
			    response = reader.readLine();
			}   
			
		} catch (Exception e) {
			PROXY_ON = false;
		}
	}

	boolean isMobileBrowser(String userAgent){
    	if(WebSession.get().getAttribute("isMobile")!=null){
    		if(Parameter.YES_NO_yes.equals(WebSession.get().getAttribute("isMobile"))){
    			return true;
    		}
    	}
//    	String mobileList = "Android,webOS,iPhone,iPad,iPod,BlackBerry,Windows Phone";
//    	String mobile[] = StringUtils.split(mobileList,",");
//    	
//    	for (int i = 0; i < mobile.length; i++) {
//			if(StringUtils.contains(userAgent, mobile[i])){
//				return true;
//			}
//		}
    	
    	return false;
    }
    
    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(Request, Response)
     */
    @Override
    public Session newSession(Request request, Response response)
    {
        return new SignInSession(request);
    }

	public SSMResourceLoader getSsmResourceLoader() {
		return ssmResourceLoader;
	}
	
	public double getGSTRate(String gstCode) {
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		return Double.valueOf(parametersService.findByCodeTypeValue(Parameter.PAYMENT_GST_CODE, gstCode));
	}
	
	
	
	
	
	private final class CustomResourceStreamLocator extends ResourceStreamLocator
    {
        /**
         * @see org.apache.wicket.core.util.resource.locator.ResourceStreamLocator#locate(java.lang.Class,
         *      java.lang.String)
         */
        @Override
        public IResourceStream locate(Class<?> clazz, String path)
        {

            String location;
            
            if ( path.indexOf("com/ssm/llp/base/page/BasePage.html")!=-1)
            {
            	
            	System.out.println("SELECTED_THEME"+":"+SELECTED_THEME);
            	if(SELECTED_THEME.equals(THEME_SUPPLY_INFO)) {
                    location = "/com/ssm/common/v2/BasePageSupplyInfo.html";
                    
                    URL url;
                    try
                    {
                        url = getServletContext().getResource(location);
                        if (url != null)
                        {
                            return new UrlResourceStream(url);
                        }
                    }
                    catch (MalformedURLException e)
                    {
                        throw new WicketRuntimeException(e);
                    }
            	}
            	
            }
            

            // resource not found; fall back on class loading
            return super.locate(clazz, path);
        }

    }
	
}
