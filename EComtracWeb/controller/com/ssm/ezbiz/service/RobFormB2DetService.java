/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobFormB2Det;

public interface RobFormB2DetService extends BaseService<RobFormB2Det, Long>{

	void deleteExceptId(String robFormBCode, long[] listIdNotDelete);
	
	List<RobFormB2Det> findByRobFormBCode(String robFormBCode);

}

