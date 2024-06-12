/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssm.llp.base.page;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.page.CouldNotLockPageException;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.controller.token.TokenController;
import com.ssm.controller.token.TokenDataModel;
import com.ssm.ezbiz.dashboard.DashboardPage;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMCaptchaResource;
import com.ssm.llp.base.wicket.component.SSMForm;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.AfterLoginLlp;
import com.ssm.llp.mod1.page.GenerateLlpUserProfilePage;
import com.ssm.llp.mod1.page.GuidelinePage;
import com.ssm.llp.mod1.page.UserTncPage;
import com.ssm.llp.mod1.page.VerificationLlpUserProfilePage;
import com.ssm.llp.mod1.service.RobUserTncService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public final class LoginPanel extends BasePanel 
{
    
	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	@SpringBean(name = "RobUserTncService")
	private RobUserTncService robUserTncService;
	
	/**
     * Constructor
     */
    public LoginPanel(String id)
    {
    	
    	super(id);
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
//    	private String test;
    	
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
//		public String getTest() {
//			return test;
//		}
//		public void setTest(String test) {
//			this.test = test;
//		}
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
            
//            SSMDropDownChoice choice = new SSMDropDownChoice("test", Parameter.YES_NO);
//            choice.setRequired(true);
//            add(choice);
//            SSMRadioChoice loginType = new SSMRadioChoice("loginType", Parameter.LOGIN_TYPE );
//            add(loginType);
            
            add(new Link("newRegistration") {
				public void onClick() {
					  setResponsePage(VerificationLlpUserProfilePage.class);
				}
			});
          
           add(new Link("generatePassword") {
                public void onClick() {
                	setResponsePage(GenerateLlpUserProfilePage.class);
                }
            });
            
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
        		session.setNoNeedPassword(false);
        		boolean isExtIface = Parameter.LOGIN_TYPE_interface.equals(session.getLoginType());
        		if(!isExtIface) {
        			session.setLoginType(loginPanelForm.getLoginType());
        		}
                session.setSignInForm(this);
                // Sign the user in
                
                try {
                	if (session.signIn(loginPanelForm.getUsername(), loginPanelForm.getPassword()))
                    {
//                        continueToOriginalDestination();
//                    	LlpUserLog userLog = new LlpUserLog();
//                    	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
//                    	userLog.setLoginTime(new Date());
//                    
//                        String ipAddress= getIpAddress();
//                        userLog.setIpAddress(ipAddress);
//                        
//                        llpUserLogService.insert(userLog);
//                        UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
                        
                        
                        if(isExtIface) {
                        	TokenDataModel tdm = (TokenDataModel)session.getAttribute(ExtInterface.EXT_TOKEN_DATA_MODEL);
                        	
    		                LlpUserProfile userProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
    		                String pairSuccessUrl = "";
    		                boolean tokenGenerateSuccess = false;
    		                try {
                        		TokenController tc = new TokenController();
                        		pairSuccessUrl = tc.generatePairingSuccessJS(userProfile.getLoginId(), tdm.getNric());
                        		tokenGenerateSuccess = true;
    						} catch (Exception e) {
    							e.printStackTrace();
    							setResponsePage(new ExtInterface("Pair Success cannot route ro mampu token issues"));
    						}
//                        	System.out.println(pairSuccessUrl);
                        	if(tokenGenerateSuccess) {
                        		setResponsePage( new SSMJSRedirectPage(pairSuccessUrl));
                        	}
                        	
                        }else {
                        	LlpUserEnviroment userProfile = (LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment();
        					String userRefNo = userProfile.getLlpUserProfile().getUserRefNo();
        					String loginId = userProfile.getLoginName();
        					//String userStatus = userProfile.getLlpUserProfile().getUserStatus(); //check e-comtrac perlu status approved (verified user) ??
                        	boolean hasTncAgreement = robUserTncService.hasAgreeTnc(userRefNo, loginId, Parameter.TNC_TYPE_USER_COMTRAC);
                        	String enableUserTnc = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_USER_TNC_COMTRAC);
                        	String enableDashboard = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_DASHBOARD);
                        	
                        	if(Parameter.YES_NO_yes.equals(enableUserTnc) && (!hasTncAgreement)) { 
                        		//	&& Parameter.USER_STATUS_active.equals(userStatus)) { //if enable TNC, without TNC record n userStatus approved.
                          		setResponsePage(UserTncPage.class);
                          	}else {
                          		if(Parameter.YES_NO_yes.equals(enableDashboard) ) {
                          			setResponsePage(DashboardPage.class);
                          		}else {
                          			setResponsePage(GuidelinePage.class);
                          		}
                          	}
                        }  
                    }
				} catch (CouldNotLockPageException e) {
					System.err.println("Session Invalidate send to login page");
					session.invalidate();
					setResponsePage(HomePage.class);
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
