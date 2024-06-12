/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.sec;

import java.security.Permission;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * This is the interface implemented by services that contain the environment for the logged-in
 * user. A DefaultUserEnvironment object is normally stored in the Context passed to Model
 * objects, and may be used by the Model to provide environment information to other factories and
 * services that it requires. It's purpose is to store information about the current user session
 * (if there is one), including the currently selected database, the ID of the current user, and
 * so forth.
 *
 * @author hhf
 * @version 1.0
 */
public interface UserEnvironment {
    
    /** user's channel */
    String DEFAULT_CHANNEL = "web";
    
    /** map role permission */
    String MAP_ROLE_PERMISSION = "map.role.permission";

    /**
     * The login context in this env.
     *
     * @return The login context
     */
    LoginContext getLoginContext();

    /**
     * The JAAS subject from the login context
     *
     * @return JAAS Subject
     */
    Subject getSubject();

    /**
     * Get the LoginPrincipal
     *
     * @return the login name
     */
    String getLoginName();

    /**
     * Get the User Name
     *
     * @return Full Name of the user
     */
    String getFullName();

    /**
     * Get the User ID
     *
     * @return The UID
     */
    int getUserId();

    /**
     * Get the Group
     *
     * @return The list of groups user belongs to
     */
    Principal[] getGroups();

    /**
     * get the channel
     *
     * @return User's channel. e.g. web, back end interface ...
     */
    String getChannel();

    /**
     * get the client session
     *
     * @return Session ID
     */
    String getSessionId();

    /**
     * get the client IP address
     *
     * @return IP Address
     */
    String getClientIp();
    
    /**
     * get the client Email address
     *
     * @return Email Address
     */
    String getEmail();

    /**
     * get user permission for the particular role
     * @param role a particular resource
     * @return user permission
     */
    Permission getPermission(String role);

    /**
     * set user permission for the particular role
     *
     * @param role a particular resource
     * @param permission Permission given for the resource
     */
    void setPermission(String role, Permission permission);

    /**
     * get the attribute for the user environment
     * @param key unique id for the attribute
     * @return Object related to user, if not found, null will be returned
     */
    Object getAttribute(String key);

    /**
     * set the attribute that belong to the user
     * @param key unique id for the attribute
     * @param object object related to user
     */
    void setAttribute(String key, Object object);
}
