package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpFileDataDao;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.service.LlpFileDataService;

@Service
public class LlpFileDataServiceImpl extends BaseServiceImpl<LlpFileData, Long> implements LlpFileDataService {

	@Autowired
	LlpFileDataDao llpFileDataDao;
	

	@Override
	public BaseDao getDefaultDao() {
		return llpFileDataDao;
	}
	
}