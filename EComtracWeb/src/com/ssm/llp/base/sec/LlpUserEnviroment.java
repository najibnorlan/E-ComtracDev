/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.sec;

import java.io.Serializable;
import java.security.Permission;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;

/**
 * Used by the force change password page. This is set because the user is not login yet, but with
 * the user name, use can perform the change password
 *
 * @author hhf
 * @version 1.0
 */
public class LlpUserEnviroment implements UserEnvironment,Serializable {
    /** User login name*/
    private String userName;
    
    /** User login name*/
    private String fullName;

    /** User Ip Address*/
    private String ipAddress;

    /** Channel type*/
    private String channelType;

    /** Session ID*/
    private String sessionId;
    
    /** User login name*/
    private String email;

    /** map to store object related to user */
    private Map userObject = new HashMap();
    
    private String userRefNo;
    
    private LlpUserProfile llpUserProfile;
    
    private RobUserOku robUserOku;

    public LlpUserProfile getLlpUserProfile() {
		return llpUserProfile;
	}


	public void setLlpUserProfile(LlpUserProfile llpUserProfile) {
		this.llpUserProfile = llpUserProfile;
	}


	public RobUserOku getRobUserOku() {
		return robUserOku;
	}


	public void setRobUserOku(RobUserOku robUserOku) {
		this.robUserOku = robUserOku;
	}


	/**
     * Creates a new SimpleUserEnviroment object.
     * can only be created by UserEnvironemntHelper
     *
     * @param usrName User name
     * @param ip IP Address
     * @param session Session Key
     * @param channel Channel Type
     */
    public LlpUserEnviroment(String usrName, String ip, String session, String channel) {
        this.userName = usrName;
        this.ipAddress = ip;
        this.channelType = channel;
        this.sessionId = session;
    }
    
    
    public LlpUserEnviroment(LlpUserProfile llpUserProfile) {
    	this.userRefNo = llpUserProfile.getUserRefNo();
    	this.userName = llpUserProfile.getLoginId();
    	this.fullName = llpUserProfile.getName();
    	this.email = llpUserProfile.getEmail();
    	this.llpUserProfile = llpUserProfile;
//    	userObject.put("userDetail", llpUserProfile);
    }	
    

    /**
     * @see com.ssm.common.sec.UserEnvironment#getLoginContext()
     */
    public LoginContext getLoginContext() {
        return null;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getSubject()
     */
    public Subject getSubject() {
        return null;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getLoginName()
     */
    public String getLoginName() {
        return userName;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getFullName()
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getGroups()
     */
    public Principal[] getGroups() {
        return null;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getChannel()
     */
    public String getChannel() {
        return channelType;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getSessionId()
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getClientIp()
     */
    public String getClientIp() {
        return ipAddress;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getPermission(java.lang.String)
     */
    public Permission getPermission(String role) {
        return null;
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment #setPermission(java.lang.String,
     *      java.security.Permission)
     */
    public void setPermission(String role, Permission permission) {
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#getAttribute(java.lang.String)
     */
    public Object getAttribute(String key) {
        return this.userObject.get(key);
    }

    /**
     * @see com.ssm.common.sec.UserEnvironment#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String key, Object object) {
        this.userObject.put(key, object);
    }

	@Override
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getUserRefNo() {
		return userRefNo;
	}


	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}


	@Override
	public int getUserId() {
		return 0;
	}
}
