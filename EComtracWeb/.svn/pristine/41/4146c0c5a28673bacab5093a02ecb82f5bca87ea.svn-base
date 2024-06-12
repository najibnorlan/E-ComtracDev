package com.ssm.llp.mod1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.mod1.dao.NameSearchDao;
import com.ssm.llp.mod1.page.NameSearchPage;

@Service
public class NameSearchServiceImpl extends BaseServiceImpl<NameSearchPage, Long> {

	@Autowired
	NameSearchDao nameSearchDao;

	@Override
	public BaseDao getDefaultDao() {
		return nameSearchDao;
	}

}
