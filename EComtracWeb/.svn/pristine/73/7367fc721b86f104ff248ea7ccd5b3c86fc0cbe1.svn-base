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
import com.ssm.llp.base.common.service.RocIncorporationService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RocIncorporation;
import com.ssm.llp.base.common.dao.RocIncorporationDao;

@Service
public class RocIncorporationServiceImpl extends BaseServiceImpl<RocIncorporation,  String> implements RocIncorporationService{
	@Autowired 
	RocIncorporationDao rocIncorporationDao;

	@Override
	public BaseDao getDefaultDao() {
		return rocIncorporationDao;
	}

}
