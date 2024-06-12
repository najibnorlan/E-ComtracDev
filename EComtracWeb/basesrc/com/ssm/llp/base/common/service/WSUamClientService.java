package com.ssm.llp.base.common.service;

import com.ssm.webis.param.UamUserInfo;

public interface WSUamClientService {

	UamUserInfo findCBSProfileWithEzbizRole(String cbsUserId)throws Exception;
	
}
