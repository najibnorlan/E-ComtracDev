/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.model.LlpLocaleMessage;

public interface LlpLocaleMessageService extends BaseService<LlpLocaleMessage, Long>{

	LlpLocaleMessage findByKeyNLocale(String key, String localeStr);
}

