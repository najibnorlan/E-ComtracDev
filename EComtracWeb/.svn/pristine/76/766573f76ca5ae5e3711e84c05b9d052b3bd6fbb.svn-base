package com.ssm.llp.base.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.dashboard.DashboardInternalPage;
import com.ssm.ezbiz.myCardTransaction.ReadMyKadPage;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMCaptchaResource;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LoginInternalPanel extends BasePanel 
{
    
	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	/**
     * Constructor
     */
    public LoginInternalPanel(String id)
    {
    	
    	super(id);
    	setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
                LoginInternalPanelForm loginPanelForm = new LoginInternalPanelForm();
                loginPanelForm.setLoginType(Parameter.LOGIN_TYPE_internal);
                return loginPanelForm;
            }
        }));
    	
    	String uri = "";
    	HttpServletRequest  request=(HttpServletRequest)getRequestCycle().getRequest().getContainerRequest();
    	try {
			uri =   request.getRequestURL().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	SignInInternalForm form = new SignInInternalForm("signInInternalForm",getDefaultModel());
    	add(form);
    	
    	if(uri.contains("ezbiz.ssm.com.my")) {
    		form.setVisible(false);
    		setResponsePage(HomePage.class);
    	}
    	

    }
    public final class LoginInternalPanelForm implements Serializable{
    	private String username;
    	private String password;
    	private String loginType;
    	private String captchaWord;
    	
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getLoginType() {
			return loginType;
		}
		public void setLoginType(String loginType) {
			this.loginType = loginType;
		}
		public String getCaptchaWord() {
			return captchaWord;
		}
		public void setCaptchaWord(String captchaWord) {
			this.captchaWord = captchaWord;
		}
    }
    
    /**
     * Sign in form
     */
    public final class SignInInternalForm extends Form implements Serializable
    {
    	private final SSMCaptchaResource captchaResource;
    	private NonCachingImage img;
        /**
         * Constructor
         * 
         * @param id
         *            id of the form component
         */
        public SignInInternalForm(final String id,IModel m)
        {
            super(id, m);
            captchaResource = new SSMCaptchaResource();
            img = new NonCachingImage("captchaImage", captchaResource);
            img.setOutputMarkupId(true);
            add(img);
            
            // Attach textfield components that edit properties map model
            SSMTextField username = new SSMTextField("username");
            username.setRequired(true);
            username.setUpperCase(false);
            username.setLabelKey("login.panel.username");
            add(username);
            
            SSMPasswordTextField password = new SSMPasswordTextField("password");
            password.setRequired(true);
            password.setLabelKey("login.panel.password");
            add(password);
            
            final SSMTextField captchaWord = new SSMTextField("captchaWord");
            captchaWord.setRequired(true);
            captchaWord.setLabelKey("login.panel.captcha");
            add(captchaWord);
            
           /* 
            SSMRadioChoice loginType = new SSMRadioChoice("loginType", Parameter.LOGIN_TYPE );
            add(loginType);
            
            add(new Link("newRegistration") {
				public void onClick() {
					  setResponsePage(VerificationLlpUserProfilePage.class);
				}
			});
          


            add(new Link("generatePassword") {
                public void onClick() {
                	setResponsePage(GenerateLlpUserProfilePage.class);
                }
            });*/
            
            add(new AjaxLink<Void>("refreshCaptcha"){
    			@Override
    			public void onClick(AjaxRequestTarget target)
    			{
    				captchaResource.redraw();
    				target.add(img);
    			}
    		});
            
//            add(new BookmarkablePageLink("myKadLogin", MyKadLogin.class));
            
            
            
            
            final String myKadLoginValidationJS = "myKadLoginValidation";
			String mainFieldToValidate[] = new String[]{"username","password"};
			String mainFieldToValidateRules[] = new String[]{"empty","empty"};
			setSemanticJSValidation(this, myKadLoginValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			
            
            final SSMAjaxButton myKadLogin = new SSMAjaxButton("myKadLogin",myKadLoginValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					loginChecking(target, form, Parameter.LOGIN_TYPE_mykad, ReadMyKadPage.class);
					
//					LoginInternalPanelForm loginInternalPanelForm = (LoginInternalPanelForm) form.getModelObject();
//					
//					if (!captchaResource.getCaptchaWord().equals(loginInternalPanelForm.getCaptchaWord().toUpperCase())){
//						String alertDesc = resolve(SSMExceptionParam.CAPTCHA_ERROR);
//		        		target.prependJavaScript("alert('"+alertDesc+"');");
//		        	}else {
//		        		SignInSession session = getMySession();
//		                session.setLoginType(Parameter.LOGIN_TYPE_mykad);
//		                session.setSignInForm(form);
//		                
//		                // Sign the user in
//		                if (session.signIn(loginInternalPanelForm.getUsername(), loginInternalPanelForm.getPassword()))
//		                {
//		                    HttpSession httpSession = ((ServletWebRequest)getRequestCycle().getRequest()).getContainerRequest().getSession();
//		                    httpSession.setMaxInactiveInterval(6*60*60);//60jam
//		                    setResponsePage(ReadMyKadPage.class);
//		                }
//		        	}
//					captchaResource.redraw();
//					target.add(img);
				}
			};
//			add(myKadLogin);
			
			final SSMAjaxButton ezbizBoLogin = new SSMAjaxButton("ezbizBoLogin",myKadLoginValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					loginChecking(target, form, Parameter.LOGIN_TYPE_internal, DashboardInternalPage.class);
//					LoginInternalPanelForm loginInternalPanelForm = (LoginInternalPanelForm) form.getModelObject();
//					
//					if (!captchaResource.getCaptchaWord().equals(loginInternalPanelForm.getCaptchaWord().toUpperCase())){
//						String alertDesc = resolve(SSMExceptionParam.CAPTCHA_ERROR);
//		        		target.prependJavaScript("alert('"+alertDesc+"');");
//		        	}else {
//		        		SignInSession session = getMySession();
//		                session.setLoginType(Parameter.LOGIN_TYPE_internal);
//		                session.setSignInForm(form);
//		                // Sign the user in
//		                if (session.signIn(loginInternalPanelForm.getUsername(), loginInternalPanelForm.getPassword()))
//		                {
////		                    continueToOriginalDestination();
////		                	LlpUserLog userLog = new LlpUserLog();
////		                	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
////		                	userLog.setLoginTime(new Date());
////		                
////		                    String ipAddress= getIpAddress();
////		                    
////		                    userLog.setIpAddress(ipAddress);
////		                    
////		                    llpUserLogService.insert(userLog);
////		                    UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
//		                    setResponsePage(DashboardInternalPage.class);
//		                }
//		        	}
//					captchaResource.redraw();
//					target.add(img);
				}
			};
			add(ezbizBoLogin);
        }
        
        
        public void loginChecking(AjaxRequestTarget target, Form<?> form, String loginType, Class afterSuccessLoginPage) {
        	LoginInternalPanelForm loginInternalPanelForm = (LoginInternalPanelForm) form.getModelObject();
        	
        	if (!captchaResource.getCaptchaWord().equals(loginInternalPanelForm.getCaptchaWord().toUpperCase())){
				ssmError(SSMExceptionParam.CAPTCHA_ERROR);
        	}else {
        		SignInSession session = getMySession();
                session.setLoginType(loginType);
                session.setSignInForm(form);
                // Sign the user in
                if (session.signIn(loginInternalPanelForm.getUsername(), loginInternalPanelForm.getPassword()))
                {
                    setResponsePage(afterSuccessLoginPage);
                }
        	}
			captchaResource.redraw();
			target.add(img);
			if(getPage() instanceof HomePage) {
				target.add(((HomePage)getPage()).getFeedbackPanel());
			}else {
				target.add(((SsmLogin)getPage()).getFeedbackPanel());
			}
        }
        
        
        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
//        @Override
//        public final void onSubmit()
//        {	
//        	LoginInternalPanelForm loginInternalPanelForm = (LoginInternalPanelForm)this.getModelObject();
//        	
//			boolean isNoError = true;
//			if(StringUtils.isBlank(loginInternalPanelForm.getUsername())){
//				isNoError = false;
//				ssmError(SSMExceptionParam.USERNAME_IS_BLANK);		 
//			} 
//			
//			if(StringUtils.isBlank(loginInternalPanelForm.getPassword())){
//				isNoError = false;
//				ssmError(SSMExceptionParam.PASSWORD_IS_BLANK);		
//			}
//
//        	if (!captchaResource.getCaptchaWord().equals(loginInternalPanelForm.getCaptchaWord().toUpperCase())){
//        		ssmError(SSMExceptionParam.CAPTCHA_ERROR);
//        	}else {
//        		SignInSession session = getMySession();
//                session.setLoginType(Parameter.LOGIN_TYPE_internal);
//                session.setSignInForm(this);
//                // Sign the user in
//                if (session.signIn(loginInternalPanelForm.getUsername(), loginInternalPanelForm.getPassword()))
//                {
////                    continueToOriginalDestination();
////                	LlpUserLog userLog = new LlpUserLog();
////                	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
////                	userLog.setLoginTime(new Date());
////                
////                    String ipAddress= getIpAddress();
////                    
////                    userLog.setIpAddress(ipAddress);
////                    
////                    llpUserLogService.insert(userLog);
////                    UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
//                    setResponsePage(DashboardInternalPage.class);
//                }
//			}
//        	captchaResource.redraw();
//        }
       

        /**
         * @return
         */
        private SignInSession getMySession()
        {
            return (SignInSession)getSession();
        }
		
    }
    
}

