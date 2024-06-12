package com.ssm.common.mobile;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMCaptchaResource;
import com.ssm.llp.base.wicket.component.SSMForm;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class HomePageMobile extends EzBizBasePageMobile{

	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	public HomePageMobile() {
    	setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
                LoginPanelForm loginPanelForm = new LoginPanelForm();
                loginPanelForm.setLoginType(Parameter.LOGIN_TYPE_normal);
                return loginPanelForm;
            }
        }));
        // Add sign-in form to page
        add(new SignInForm("signInForm",getDefaultModel()));
//        PageRequestHandlerTracker.getLastHandler(RequestCycle.get()).
        
	}

	public final class LoginPanelForm implements Serializable{
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
    public final class SignInForm extends SSMForm implements Serializable
    {
    	private final SSMCaptchaResource captchaResource;
    	private NonCachingImage img;
        /**
         * Constructor
         * 
         * @param id
         *            id of the form component
         */
        public SignInForm(final String id,IModel m)
        {
            super(id, m);
            LoginPanelForm loginPanelForm = (LoginPanelForm) m.getObject();
            captchaResource = new SSMCaptchaResource();
            img = new NonCachingImage("captchaImage", captchaResource);
            img.setOutputMarkupId(true);
            add(img);
            
            // Attach textfield components that edit properties map model
            SSMTextField username = new SSMTextField<String>("username");
            username.setRequired(true);
            username.setLabelKey("login.panel.username");
            add(username);
            
            SSMPasswordTextField password = new SSMPasswordTextField("password");
            password.setRequired(true);
            password.setLabelKey("login.panel.password");
            add(password);
            
            SSMTextField captchaWord = new SSMTextField("captchaWord");
            captchaWord.setRequired(true);
            captchaWord.setLabelKey("login.panel.captcha");
            add(captchaWord);
//          
//           add(new Link("generatePassword") {
//                public void onClick() {
//                	setResponsePage(GenerateLlpUserProfilePage.class);
//                }
//            });
//            
            add(new AjaxLink<Void>("refreshCaptcha"){
    			@Override
    			public void onClick(AjaxRequestTarget target)
    			{
    				captchaResource.redraw();
    				target.add(img);
    			}
    		});
            
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        public final void onSubmit()
        {
        	LoginPanelForm loginPanelForm = (LoginPanelForm)this.getModelObject();
        	if (!captchaResource.getCaptchaWord().equals(loginPanelForm.getCaptchaWord().toUpperCase())){
        		ssmError(SSMExceptionParam.CAPTCHA_ERROR);
        	}else {
        		SignInSession session = getMySession();
                session.setLoginType(loginPanelForm.getLoginType());
                session.setSignInForm(this);
                // Sign the user in
                if (session.signIn(loginPanelForm.getUsername(), loginPanelForm.getPassword()))
                {
//                    continueToOriginalDestination();
//                	LlpUserLog userLog = new LlpUserLog();
//                	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
//                	userLog.setLoginTime(new Date());
//                
//                    String ipAddress= getIpAddress();
//                    userLog.setIpAddress(ipAddress);
//                    
//                    llpUserLogService.insert(userLog);
//                    UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
                    setResponsePage(AfterLoginLlpMobile.class);
                }
			}
        	captchaResource.redraw();
        }
       

        /**
         * @return
         */
        private SignInSession getMySession()
        {
            return (SignInSession)getSession();
        }
		
    }
	
}
