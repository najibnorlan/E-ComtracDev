/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpSpecialKeywordDao;
import com.ssm.llp.base.common.model.LlpSpecialKeyword;
import com.ssm.llp.base.common.service.LlpSpecialKeywordService;

@Service
public class LlpSpecialKeywordServiceImpl extends BaseServiceImpl<LlpSpecialKeyword,  String> implements LlpSpecialKeywordService{
	@Autowired 
	LlpSpecialKeywordDao llpSpecialKeywordDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpSpecialKeywordDao;
	}

}
