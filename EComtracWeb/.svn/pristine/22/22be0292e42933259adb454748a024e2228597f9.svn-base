package com.ssm.llp.base.page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.CollectionBalancingPage;
import com.ssm.ezbiz.otcPayment.ViewCollectionTotalAmount;
import com.ssm.ezbiz.otcPayment.ViewTransactionSummary;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.LoginInternalPanel.LoginInternalPanelForm;
import com.ssm.llp.base.page.LoginInternalPanel.SignInInternalForm;
import com.ssm.llp.base.utils.GoogleReCapthaUtils;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMCaptchaResource;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.AfterLoginInternal;
import com.ssm.llp.mod1.page.ViewOkuRegistrationPage;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "all" })
public class SecondLevelLoginPage extends BaseFrame {

	@SpringBean(name = "LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	public SecondLevelLoginPage(final Page parentPage,
			final ModalWindow window, final String searchStr) {

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						SecondLevelLoginModel secondLevelLoginModel = new SecondLevelLoginModel();
						secondLevelLoginModel
								.setLoginType(Parameter.LOGIN_TYPE_second_level);
						secondLevelLoginModel.setParam(searchStr);
						secondLevelLoginModel.setParentPage(parentPage);
						return secondLevelLoginModel;
					}
				}));
		add(new LoginForm("loginForm", getDefaultModel(),window));

	}

	public final class LoginForm extends Form implements Serializable {
		public LoginForm(final String id,IModel m, final ModalWindow window)
        {
            super(id, m);
            final SecondLevelLoginModel loginModel = (SecondLevelLoginModel) m.getObject();
            SSMTextField username = new SSMTextField("username");
            username.setRequired(false);
            username.setLabelKey("login.panel.username");
            username.setUpperCase(false);
            add(username);
            
            SSMPasswordTextField password = new SSMPasswordTextField("password");
            password.setLabelKey("login.panel.password");
            password.setRequired(false);
            add(password);
            
            SSMAjaxButton signIn = new SSMAjaxButton("signIn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					getSession().removeAttribute("_approveby");
					getSession().removeAttribute("_processSuccess");
					
					SecondLevelLoginModel secondLevelLoginModel = (SecondLevelLoginModel)form.getModelObject();
					
					boolean isNoError = true;
					boolean isStatusPending = true;
					
					if(secondLevelLoginModel.getPassword() == null){
						isNoError = false;
						ssmError(SSMExceptionParam.PASSWORD_IS_BLANK);		   
					}
					
					
					if(secondLevelLoginModel.getUsername() == null){
						isNoError = false;
						ssmError(SSMExceptionParam.USERNAME_IS_BLANK);		   
					
					} else {	
						if(UserEnvironmentHelper.getLoginName().equals("SSM:" + secondLevelLoginModel.getUsername())){	//jika sama loginId		
							isNoError = false;
							String sameUserIdTwoLayer = getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_USER_VERIFY_2ND_LAYER);
							if(StringUtils.isNotBlank(sameUserIdTwoLayer)){
								List<String> userList = Arrays.asList(StringUtils.split(sameUserIdTwoLayer,","));
									if((userList.contains(UserEnvironmentHelper.getLoginName())) && !(loginModel.getParentPage() instanceof CollectionBalancingPage)){
										if(!(loginModel.getParentPage() instanceof ViewOkuRegistrationPage)) { //for oku no need to check status!
										isStatusPending = llpUserProfileService.checkIsAllowedStatusByUserRefNo(secondLevelLoginModel.getParam(), Parameter.USER_STATUS_pending);
										} if (isStatusPending){ //only status P (pending approval) can proceed with same loginId
												isNoError=true;		
											}else {
												ssmError(SSMExceptionParam.USER_STATUS_NOT_ALLOWED);
											}
										
//										System.out.println("loginModel.getParam: " +loginModel.getParam());
//										System.out.println("secondLevelLoginModel.getParam: "+secondLevelLoginModel.getParam());
										
									}else {
										ssmError(SSMExceptionParam.LOGIN_SECOND_LEVEL_SAME_STAF);
									}
							}else {
								ssmError(SSMExceptionParam.LIST_ALLOWED_USER_BLANK); 
							}
					   }
				    }
					
					
					if(isNoError){
			        	
			        	SignInSession session = new SignInSession(getRequest());
			            session.setLoginType(Parameter.LOGIN_TYPE_second_level);
			            session.setSignInForm(form);
			            // Sign the user in
			            if (session.authenticate(secondLevelLoginModel.getUsername(), secondLevelLoginModel.getPassword())){
//			                LlpUserLog userLog = new LlpUserLog();
//			                userLog.setLoginId("SSM:" + secondLevelLoginModel.getUsername());
//			                userLog.setLoginTime(new Date());
//			                
//			                String ipAddress= getIpAddress();
//			                userLog.setIpAddress(ipAddress);
//			                llpUserLogService.insert(userLog);
			                
			                if(loginModel.getParentPage() instanceof CollectionBalancingPage){
			                  setResponsePage(new ViewCollectionTotalAmount(Integer.parseInt(loginModel.getParam())));
			                }
			                
			                //if(loginModel.getParentPage() instanceof SyncLlpUserProfilePage){
			                	getSession().setAttribute("_approveby", secondLevelLoginModel.getUsername());
			                	window.close(target);
//				                }
//			                if(loginModel.getParentPage() instanceof UserApprovalPage){
//			                	getSession().setAttribute("_processSuccess", Boolean.TRUE);
//			                	window.close(target);
//			                }
			            }else{
			            	ssmError(SSMExceptionParam.USERNAME_NOT_MATCH);
			            }
					}
		        	FeedbackPanel feedbackPanel =  ((SecondLevelLoginPage)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
		        	
				}
				
			};
			add(signIn);
            
        }

		@Override
		protected void onError() {
			// TODO Auto-generated method stub
			super.onError();
		}

		/**
		 * @return
		 */
		private SignInSession getMySession() {
			return (SignInSession) getSession();
		}

	}

	public final class SecondLevelLoginModel implements Serializable {
		private String username;
		private String password;
		private String loginType;
		private Page parentPage;
		private String param;

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

		public Page getParentPage() {
			return parentPage;
		}

		public void setParentPage(Page parentPage) {
			this.parentPage = parentPage;
		}

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}

	@Override
	public String getPageTitle() {
		return null;
	}
}
