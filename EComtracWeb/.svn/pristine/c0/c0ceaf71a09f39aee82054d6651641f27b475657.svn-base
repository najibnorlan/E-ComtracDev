package com.ssm.llp.mod1.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.RobUserTnc;


public interface RobUserTncService extends BaseService<RobUserTnc, String> {
	
	public RobUserTnc findActiveUserTnc(String userRefNo, String loginId, String tncType);
	
	boolean hasAgreeTnc(String userRefNo, String loginId, String tncType);
}
