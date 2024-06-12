package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobFormABranches;

public interface RobFormABranchesService extends BaseService<RobFormABranches, Long> {
	void deleteNotInId(String robFormACode, long[] idToDelete);

	List<RobFormABranches> findByRobFormACode(String formACode);
}
