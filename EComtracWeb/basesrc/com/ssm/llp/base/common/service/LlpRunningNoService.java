package com.ssm.llp.base.common.service;

import java.util.Date;

import com.ssm.llp.base.common.model.LlpRunningNo;

public interface LlpRunningNoService extends BaseService<LlpRunningNo, Long>{

	public String generateRunningNo(String genCode, String fieldCode, String moduleCode, String yearMonthDay, String lastNoPattern, Date date);
	
}
