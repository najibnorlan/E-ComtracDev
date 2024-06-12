package com.ssm.ezbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.TestZamZamDao;
import com.ssm.ezbiz.service.TestZamZamService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.TestZamZam;


@Service
public class TestZamZamServiceImpl extends BaseServiceImpl<TestZamZam,  Long> implements TestZamZamService{

	@Autowired 
	private TestZamZamDao testZamZamDao;

	@Override
	public BaseDao getDefaultDao() {
		return testZamZamDao;
	}

}
