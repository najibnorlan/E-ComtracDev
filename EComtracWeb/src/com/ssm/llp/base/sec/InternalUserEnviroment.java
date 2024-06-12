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
import com.ssm.webis.param.UamUserInfo;

/**
 * Used by the force change password page. This is set because the user is not login yet, but with
 * the user name, use can perform the change password
 *
 * @author hhf
 * @version 1.0
 */
public class InternalUserEnviroment implements UserEnvironment, Serializable {
    /** User login name*/
    private String userName;
    
    /** User full name*/
    private String fullName;

    /** User Ip Address*/
    private String ipAddress;

    /** Channel type*/
    private String channelType;

    /** Session ID*/
    private String sessionId;
    
    /** User login name*/
    private String email;
    
    private String defaultBranch;

    /** map to store object related to user */
    private Map userObject = new HashMap();

    /**
     * Creates a new SimpleUserEnviroment object.
     * can only be created by UserEnvironemntHelper
     *
     * @param usrName User name
     * @param ip IP Address
     * @param session Session Key
     * @param channel Channel Type
     */
    public InternalUserEnviroment(String usrName, String ip, String session, String channel, String defaultBranch) {
        this.userName = usrName;
        this.ipAddress = ip;
        this.channelType = channel;
        this.sessionId = session;
        this.defaultBranch=defaultBranch;
    }
    
    public InternalUserEnviroment(UamUserInfo uamUserInfo) {
    	
    	this.userName = "SSM:"+uamUserInfo.getVchuserid();
    	this.fullName = uamUserInfo.getVchusername();
    	this.email = uamUserInfo.getVchemail();
    	this.defaultBranch = uamUserInfo.getDefaultBranch();
    	userObject.put("uamUserInfo", uamUserInfo);
    	
    }	
    
    public InternalUserEnviroment(UamUserInfo uamUserInfo, String channel) {
    	
    	this.userName = "SSM:"+uamUserInfo.getVchuserid();
    	this.fullName = uamUserInfo.getVchusername();
    	this.email = uamUserInfo.getVchemail();
    	userObject.put("uamUserInfo", uamUserInfo);
    	this.channelType = channel;
    	this.defaultBranch=uamUserInfo.getDefaultBranch();
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
     * @see com.ssm.common.sec.UserEnvironment#getUserId()
     */
    public int getUserId() {
        return 0;
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

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getDefaultBranch() {
		return defaultBranch;
	}
}
