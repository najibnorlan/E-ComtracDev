package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;
import com.ssm.llp.mod1.model.LlpUserProfile;

public interface SSMMyKadModelService extends BaseService<SSMMyKadModel, Long>{

	LlpUserProfile registerOrActivateUser(SSMMyKadModel myKadModel) throws Exception;

}
