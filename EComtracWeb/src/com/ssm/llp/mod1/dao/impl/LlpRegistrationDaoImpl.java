package com.ssm.llp.mod1.dao.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.LlpRegistrationDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;


@Repository
public class LlpRegistrationDaoImpl extends BaseDaoImpl<LlpRegistration, String> implements LlpRegistrationDao {

	@Override
	public LlpRegistration findByIdWithAllInfo(String llpNo) {
		LlpRegistration llpRegistration = findById(llpNo);
		
		return llpRegistration;
	}
}
