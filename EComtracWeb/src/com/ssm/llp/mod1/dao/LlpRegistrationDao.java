package com.ssm.llp.mod1.dao;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.mod1.model.LlpRegistration;

public interface LlpRegistrationDao  extends BaseDao<LlpRegistration, String> {

	LlpRegistration findByIdWithAllInfo(String llpNo);

}
