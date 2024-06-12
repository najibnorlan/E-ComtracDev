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
import com.ssm.llp.ezbiz.model.RobCmpConfig;

public interface RobCmpConfigService extends BaseService<RobCmpConfig, Long>{

	double findCmpAmtByDelayDay(String formCode, int delayDay);
}

