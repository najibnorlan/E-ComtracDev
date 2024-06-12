package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.webis.client.UamClient;
import com.ssm.webis.param.UamUserInfo;
import com.ssm.webis.param.UamUserInfoResp;

@Service
public class WSUamClientServiceImpl implements WSUamClientService {

	@Autowired
	WSManagementService wSManagementService;

	@Override
	public UamUserInfo findCBSProfileWithEzbizRole(String userId) throws Exception{
		
		String url = wSManagementService.getWsUrl("UamClient.findCBSProfileWithEzbizRole");
		
		UamUserInfoResp resp = UamClient.findCBSProfileWithEzbizRole(url, userId);
		if("00".equals(resp.getSuccessCode())){
			return resp.getUamUserInfo();
		}else{
			throw new SSMException(resp.getErrorMsg());
		}
	}
}
