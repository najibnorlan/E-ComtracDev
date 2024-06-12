/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2;

public interface RobFormB2Service extends BaseService<RobFormB2, Long>{
	RobFormB2 findByRobFormBCode(String robFormBCode);
}

