package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobFormABizCode;

public interface RobFormABizCodeService extends BaseService<RobFormABizCode, Long> {
	void deleteNotInId(String robFormACode,long[] idToDelete);
	
	List<RobFormABizCode> findByRobFormACode(String formACode);
}
