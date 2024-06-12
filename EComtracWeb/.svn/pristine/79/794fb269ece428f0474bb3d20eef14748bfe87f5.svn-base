package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormABizCode;

public interface RobFormABizCodeDao  extends BaseDao<RobFormABizCode, Long> {
	void deleteNotInId(String robFormACode, long[] idToDelete);

	List<RobFormABizCode> findByRobFormACode(String formACode);
}

