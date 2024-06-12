package com.ssm.llp.base.common.sec;

import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * 
 * Provides a custom password encoder for {@code spring-security} framework.
 * <p>
 * See under the {@code <authentication-provider>} tag in {@code applicationContext-security.xml}. 
 * 
 * @author yee
 *
 */

@Service("md5PasswordEncoder")
public class Md5PasswordEncoder implements PasswordEncoder {

	public String encodePassword(String rawPassword, Object salt)
			throws DataAccessException
	{
		return PasswordUtil.encrptdPassword(rawPassword, false);
	}

	public boolean isPasswordValid(String encryptedPassword, String rawPassword, Object arg2)
			throws DataAccessException
	{
		return PasswordUtil.authenticatePassword(rawPassword, encryptedPassword);
	}

}
