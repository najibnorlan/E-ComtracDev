package com.ssm.llp.base.common.sec;

import java.security.Permission;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

public class UserEnvironmentTemp implements UserEnvironment{
	
	private String loginName;
	private String fullName;
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public Object getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public Principal[] getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginContext getLoginContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLoginName() {
		return loginName;
	}

	@Override
	public Permission getPermission(String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAttribute(String key, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermission(String role, Permission permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

}
