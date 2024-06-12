package com.ssm.common.mobile;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
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
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.page.AfterLoginInternal;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LoginInternalPanelMobile extends BasePanelMobile 
{
    
	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	/**
     * Constructor
     */
    public LoginInternalPanelMobile(String id)
    {
    	
    	super(id);
    	setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
                LoginInternalPanelForm loginPanelForm = new LoginInternalPanelForm();
                loginPanelForm.setLoginType(Parameter.LOGIN_TYPE_internal);
                return loginPanelForm;
            }
        }));
    	 add(new SignInInternalForm("signInInternalForm",getDefaultModel()));
        

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
            add(username);
            
            add(new SSMPasswordTextField("password"));
            
            SSMTextField captchaWord = new SSMTextField("captchaWord");
            captchaWord.setRequired(true);
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
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        public final void onSubmit()
        {
        	LoginInternalPanelForm loginInternalPanelForm = (LoginInternalPanelForm)this.getModelObject();
        	if (!captchaResource.getCaptchaWord().equals(loginInternalPanelForm.getCaptchaWord().toUpperCase())){
        		ssmError(SSMExceptionParam.CAPTCHA_ERROR);
        	}else {
        		SignInSession session = getMySession();
                session.setLoginType(Parameter.LOGIN_TYPE_internal);
                session.setSignInForm(this);
                // Sign the user in
                if (session.signIn(loginInternalPanelForm.getUsername(), loginInternalPanelForm.getPassword()))
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
                    setResponsePage(AfterLoginInternal.class);
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

