package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormABranches;

public interface RobFormABranchesDao  extends BaseDao<RobFormABranches, Long> {
	void deleteNotInId(String robFormACode, long[] idToDelete);
	
	List<RobFormABranches> findByRobFormACode(String formACode);
}

