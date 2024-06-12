package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormABranchesDao;
import com.ssm.ezbiz.service.RobFormABranchesService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormABranches;

@Service
public class RobFormABranchesServiceImpl extends BaseServiceImpl<RobFormABranches, Long> implements RobFormABranchesService {

	@Autowired
	RobFormABranchesDao robFormABranchesDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormABranchesDao;
	}

	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		robFormABranchesDao.deleteNotInId(robFormACode, idToDelete);
	}

	@Override
	public List<RobFormABranches> findByRobFormACode(String formACode) {
		return robFormABranchesDao.findByRobFormACode(formACode);
	}
	
	
}