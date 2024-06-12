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
import com.ssm.llp.base.common.dao.RocBlacklistDao;
import com.ssm.llp.base.common.service.RocBlacklistService;
import com.ssm.llp.ezbiz.model.RocBlacklist;

@Service
public class RocBlacklistServiceImpl extends BaseServiceImpl<RocBlacklist,  Long> implements RocBlacklistService{
	@Autowired 
	RocBlacklistDao rocBlacklistDao;

	@Override
	public BaseDao getDefaultDao() {
		return rocBlacklistDao;
	}

	@Override
	public boolean checkStatusIdTypeAndIdNo(String idType, String idNo) {
	
		boolean idNoStatus= rocBlacklistDao.checkStatusIdTypeAndIdNo(idType,idNo);
		return idNoStatus;
	
	}

}
