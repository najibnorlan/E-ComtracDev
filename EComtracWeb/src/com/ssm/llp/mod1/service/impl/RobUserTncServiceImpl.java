package com.ssm.llp.mod1.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.mod1.dao.RobUserTncDao;
import com.ssm.llp.mod1.model.RobUserTnc;
import com.ssm.llp.mod1.service.RobUserTncService;

@Service
public class RobUserTncServiceImpl extends BaseServiceImpl<RobUserTnc, String> implements RobUserTncService {
	
	@Autowired
	RobUserTncDao robUserTncDao;

	@Override
	public BaseDao getDefaultDao() {
		return robUserTncDao;
	}


	@Override
	public RobUserTnc findActiveUserTnc(String userRefNo, String loginId, String tncType) {
		return robUserTncDao.getActiveUserTnc(userRefNo, loginId, tncType);
	}
	
	@Override
	public boolean hasAgreeTnc(String userRefNo, String loginId, String tncType) {
		return robUserTncDao.hasAgreeTnc(userRefNo, loginId, tncType);
	}

 }
	