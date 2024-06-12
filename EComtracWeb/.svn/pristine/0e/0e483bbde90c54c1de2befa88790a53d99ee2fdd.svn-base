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
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

import com.ssm.controller.token.TokenDataModel;
import com.ssm.ezbiz.service.ExtUserPairingInfoService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.sec.SecUtils;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;
import com.ssm.webis.param.UamUserInfo;
/**
 * Session class for signin example. Holds and authenticates users.
 * 
 * @author Jonathan Locke
 */
public final class SignInSession extends AuthenticatedWebSession
{
    /** Trivial user representation */
    private String user;
    private String loginType;
    private String loginSource;
    private boolean isNoNeedPassword = false;
    private Form signInForm;
    
    /**
     * Constructor
     * 
     * @param request
     */
    public SignInSession(Request request)
    {
        super(request);
    }
    
    

    /**
     * Checks the given username and password, returning a User object if if the username and
     * password identify a valid user.
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if the user was authenticated
     */
    @Override
    public final boolean authenticate(final String username, final String password)
    {
    	WSUamClientService wsUamClientService = (WSUamClientService) WicketApplication.getBean(WSUamClientService.class.getSimpleName());
    	if(Parameter.LOGIN_TYPE_normal.equals(loginType)){
	    	LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceNew(LlpUserProfileService.class.getSimpleName());
	        try {
	        	LlpUserProfile llpUserProfile = null;// llpUserProfileService.getProfileLogin(username, password);
	        	if(isNoNeedPassword){
	        		llpUserProfile = llpUserProfileService.findProfileInfoByUserId(username);
	        	}else{
	        		llpUserProfile = llpUserProfileService.getProfileLogin(username, password);
	        	}
	        	if(llpUserProfile!=null) {
					LlpUserEnviroment enviroment = new LlpUserEnviroment(llpUserProfile);
					if(Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus())) {
						RobUserOkuService robUserOkuService = (RobUserOkuService) WicketApplication.getServiceNew(RobUserOkuService.class.getSimpleName());
						RobUserOku robUserOku = robUserOkuService.findOkuByUserRefNo(llpUserProfile.getUserRefNo());
						if(robUserOku!=null){
							enviroment.setRobUserOku(robUserOku); //set oku into user profile session
						}
					}
		        	UserEnvironmentHelper.setUserEnvironment(enviroment);
		        	setAttribute("UserEnvironment", enviroment);
		        	
		        	generateLogData(enviroment);
					return true;
	        	}
			} catch (SSMException e) {
				ssmError(e);
			} catch (Exception e) {
				e.printStackTrace();
				ssmError(new SSMException("System Error"));
			}
	        
    	}

	    if(Parameter.LOGIN_TYPE_internal.equals(loginType)){
            try {
            	if(!SecUtils.loginNTLM(username, password)){
        			throw new SSMException(SSMExceptionParam.USERNAME_NOT_MATCH);
        		}
            	
            	UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(username);
            	
            	//Temp
//            	UamUserProfile uamUserProfile = new UamUserProfile();
//            	uamUserProfile.setVchuserid("zamzam");
//            	uamUserProfile.setVchusername("MOHD ZAM ZAM");
//            	uamUserProfile.setVchemail("zamzam@ssm.com.my");
            	
            	if(uamUserInfo!=null) {
//            		String defaultBranch=null;
//            		try {
//        				defaultBranch =uamUserProfileService.getDefaultBranch(uamUserProfile.getVchuserid());
//        			} catch (Exception e) {
//        			}
            		InternalUserEnviroment enviroment = new InternalUserEnviroment(uamUserInfo);
	            	UserEnvironmentHelper.setUserEnvironment(enviroment);
	            	setAttribute("UserEnvironment", enviroment);
	            	generateLogData(enviroment);
	    			return true;
            	}
    		} catch (SSMException e) {
    			ssmError(e);
    		} catch (Exception e) {
    			e.printStackTrace();
				ssmError(new SSMException("System Error"));
			}
            
		}
	    
	    if(Parameter.LOGIN_TYPE_mykad.equals(loginType)){
//        	UamUserProfileService uamUserProfileService = (UamUserProfileService) WicketApplication.getServiceNew(UamUserProfileService.class.getSimpleName());
            try {
            	if(!SecUtils.loginNTLM(username, password)){
        			throw new SSMException(SSMExceptionParam.USERNAME_NOT_MATCH);
        		}
            	UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(username);
//            	//Temp
//            	UamUserProfile uamUserProfile = new UamUserProfile();
//            	uamUserProfile.setVchuserid("zamzam");
//            	uamUserProfile.setVchusername("MOHD ZAM ZAM");
//            	uamUserProfile.setVchemail("zamzam@ssm.com.my");
            	
            	if(uamUserInfo!=null) {
//            		String defaultBranch=null;
//            		try {
//        				defaultBranch = uamUserProfileService.getDefaultBranch(uamUserProfile.getVchuserid());
//        			} catch (Exception e) {
//        			}
	            	InternalUserEnviroment enviroment = new InternalUserEnviroment(uamUserInfo , Parameter.LOGIN_TYPE_mykad);
	            	UserEnvironmentHelper.setUserEnvironment(enviroment);
	            	setAttribute("UserEnvironment", enviroment);
	            	generateLogData(enviroment);
	    			return true;
            	}
    		} catch (SSMException e) {
    			ssmError(e);
    		} catch (Exception e) {
    			e.printStackTrace();
				ssmError(new SSMException("System Error"));
			}
		}
	    
	    if(Parameter.LOGIN_TYPE_second_level.equals(loginType)){
//	    	UamUserProfileService uamUserProfileService = (UamUserProfileService) WicketApplication.getServiceNew(UamUserProfileService.class.getSimpleName());
	    	try{
	    		if(!SecUtils.loginNTLM(username, password)){
        			throw new SSMException(SSMExceptionParam.USERNAME_NOT_MATCH);
        		}
	    		UamUserInfo uamUserInfo = wsUamClientService.findCBSProfileWithEzbizRole(username);
	    		if(uamUserInfo!=null) {
	    			return true;
	    		}
	    	}catch(SSMException e){
	    		ssmError(e);
	    	} catch (Exception e) {
	    		e.printStackTrace();
				ssmError(new SSMException("System Error"));
			}
	    }
	    
	    if(Parameter.LOGIN_TYPE_interface.equals(loginType)){
	    	
	    	LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceNew(LlpUserProfileService.class.getSimpleName());
	        try {
	        	LlpUserProfile llpUserProfile = null;// llpUserProfileService.getProfileLogin(username, password);
	        	if(isNoNeedPassword){
	        		llpUserProfile = llpUserProfileService.findProfileInfoByUserId(username);
	        	}else{
	        		llpUserProfile = llpUserProfileService.getProfileLogin(username, password);
	        	}
				if(llpUserProfile!=null) {
					if(!Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus())) {
						if(Parameter.USER_STATUS_pending.equals(llpUserProfile.getUserStatus())) {
							ssmError(new SSMException("extUser.error.pending"));
						}else {
							ssmError(new SSMException("extUser.error.pleasecontactssm"));
						}
						return false;
					}else {
						if(!isNoNeedPassword){
							ExtUserPairingInfoService extUserPairingInfoService = (ExtUserPairingInfoService) WicketApplication.getServiceNew(ExtUserPairingInfoService.class.getSimpleName());
							
							TokenDataModel tdm = (TokenDataModel) getAttribute(ExtInterface.EXT_TOKEN_DATA_MODEL);
							String extToken = (String) getAttribute(ExtInterface.EXT_TOKEN);
							
							if(!tdm.getNric().equals(llpUserProfile.getIdNo())) {
								ssmError(new SSMException("extUser.error.idNotMatchProfile",tdm.getNric()));
								return false;
							}
			        		ExtUserPairingInfo extUserPairing = new ExtUserPairingInfo();
			        		extUserPairing.setExtUserRefNo(tdm.getNric());
			        		extUserPairing.setEzbizId(llpUserProfile.getLoginId());
			        		extUserPairing.setExtToken(extToken);
			        		extUserPairing.setStatus("A");
			        		extUserPairingInfoService.insert(extUserPairing);
						}
						
						LlpUserEnviroment enviroment = new LlpUserEnviroment(llpUserProfile);
						RobUserOkuService robUserOkuService = (RobUserOkuService) WicketApplication.getServiceNew(RobUserOkuService.class.getSimpleName());
		        		RobUserOku robUserOku = robUserOkuService.findOkuByUserRefNo(llpUserProfile.getUserRefNo());
		        		if(robUserOku!=null){
		        			enviroment.setRobUserOku(robUserOku); //set oku into user profile session
		        		}
						UserEnvironmentHelper.setUserEnvironment(enviroment);
						setAttribute("UserEnvironment", enviroment);
						return true;
					}
				}
			} catch (SSMException e) {
				ssmError(e);
			} catch (Exception e) {
				e.printStackTrace();
				ssmError(new SSMException("System Error"));
			}
    	}
	    
        return false;
    }

	void generateLogData(UserEnvironment enviroment) {

		LlpUserLogService llpUserLogService = (LlpUserLogService) WicketApplication.getServiceNew(LlpUserLogService.class.getSimpleName());

		LlpUserLog userLog = new LlpUserLog();
		userLog.setLoginId(enviroment.getLoginName());
		userLog.setLoginTime(new Date());

		
		HttpServletRequest request = (HttpServletRequest)RequestCycle.get().getRequest().getContainerRequest(); 
		String ipAdd = WicketUtils.getIpAddress(request, this);
		userLog.setIpAddress(ipAdd);
		
		
		userLog.setLoginType(getLoginType());
		userLog.setLoginSource(getLoginSource());
		
		
		String reqUrl = request.getRequestURL().toString();
		try {
			reqUrl = StringUtils.split(reqUrl,"//")[1];
			reqUrl = StringUtils.split(reqUrl,"/")[0];
		} catch (Exception e) {
		}
		
		userLog.setReqUrl(reqUrl);

		llpUserLogService.insert(userLog);

		if (!Parameter.LOGIN_TYPE_second_level.equals(loginType)) {
			UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
		}

	}



	/**
     * @return User
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @param user
     *            New user
     */
    public void setUser(final String user)
    {
        this.user = user;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles()
    {
        return null;
    }

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Form getSignInForm() {
		return signInForm;
	}


	public void setSignInForm(Form signInForm) {
		this.signInForm = signInForm;
	}



	public boolean isNoNeedPassword() {
		return isNoNeedPassword;
	}



	public void setNoNeedPassword(boolean isNoNeedPassword) {
		this.isNoNeedPassword = isNoNeedPassword;
	}



	public String getLoginSource() {
		return loginSource;
	}



	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}


}
