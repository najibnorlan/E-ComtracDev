package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormABizCodeDao;
import com.ssm.ezbiz.service.RobFormABizCodeService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormABizCode;

@Service
public class RobFormABizCodeServiceImpl extends BaseServiceImpl<RobFormABizCode, Long> implements RobFormABizCodeService {

	@Autowired
	RobFormABizCodeDao robFormABizCodeDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormABizCodeDao;
	}

	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		robFormABizCodeDao.deleteNotInId(robFormACode, idToDelete);
	}

	@Override
	public List<RobFormABizCode> findByRobFormACode(String formACode) {
		return robFormABizCodeDao.findByRobFormACode(formACode);
	}
	
	
}