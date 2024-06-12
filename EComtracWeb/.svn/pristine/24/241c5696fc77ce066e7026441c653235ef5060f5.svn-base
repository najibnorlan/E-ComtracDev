package com.ssm.llp.mod1.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.mod1.dao.LlpPartnerLinkDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.service.LlpPartnerLinkService;

@Service
public class LlpPartnerLinkServiceImpl extends BaseServiceImpl<LlpPartnerLink, Long> implements LlpPartnerLinkService {

	@Autowired
	LlpPartnerLinkDao llpPartnerLinkDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpPartnerLinkDao;
	}

	@Override
	public List<LlpPartnerLink> findByLlpNo(String llpNo) {
		return llpPartnerLinkDao.findByLlpNo(llpNo);
	}

}