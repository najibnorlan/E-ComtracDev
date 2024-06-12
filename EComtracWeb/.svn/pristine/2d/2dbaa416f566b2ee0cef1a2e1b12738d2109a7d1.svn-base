/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.sec;

import com.ssm.llp.base.sec.InternalUserEnviroment;

/**
 * Used to store the UserEnvironment under the current thread. In the server environment, multiple
 * thread will be able to store multiple instance of UserEnvironment
 *
 * @author hhf
 * @version 1.0
 */
public final class UserEnvironmentHelper {
    /** System user, used when performing system function like suspend user after n fail login */
    public static final String SYSTEM_USER = "sys";

    /** This thread local will store the User Environment Information to the hook */
    private static ThreadLocal t = new ThreadLocal();

    /**
     * Prevent Cretaion of new UserEnvironmentHelper object.
     */
    private UserEnvironmentHelper() {
    }

    /**
     * get the UserEnvironment from the hook
     *
     * @return user enviroment if found, or null if not found
     */
    public static UserEnvironment getUserenvironment() {
        return (UserEnvironment) t.get();
    }

    /**
     * get the login user name
     *
     * @return login user name if found, else null
     */
    public static String getLoginName() {
        UserEnvironment ue = getUserenvironment();

        if (ue == null) {
            return "system";
        }

        return ue.getLoginName();
    }
    
    
    /**
     * set the UserEnvironment to the hook
     *
     * @param ue get the UserEnvironment from the hook
     */
    public static void setUserEnvironment(UserEnvironment ue) {
        t.set(ue);
    }
    

    /**
     * set the UserEnvironment to the hook
     *
     * @param ue get the UserEnvironment from the hook
     */
    public static boolean isInternalUser() {
        if(getUserenvironment()!=null && getUserenvironment() instanceof InternalUserEnviroment){
        	return true;
        }
        return false;
    }

    /**
     * Used by the force change password page. This is set because the user is not login yet, but
     * with the user name, use can perform the change password
     *
     * @param loginName Login name
     * @param ip Address address
     * @param sessionId Session ID
     */
    /*public static void initWebUser(String loginName, String ip, String sessionId) {
        SimpleUserEnviroment ue = new SimpleUserEnviroment(loginName, ip, sessionId, "web");
        setUserEnvironment(ue);
    }*/
   
}
