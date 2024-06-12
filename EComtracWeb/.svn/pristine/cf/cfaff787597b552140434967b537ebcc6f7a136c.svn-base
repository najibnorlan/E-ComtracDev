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
import com.ssm.llp.base.common.dao.RocBusinessObjectCodeDao;
import com.ssm.llp.base.common.service.RocBusinessObjectCodeService;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;

@Service
public class RocBusinessObjectCodeServiceImpl extends BaseServiceImpl<RocBusinessObjectCode,  String> implements RocBusinessObjectCodeService{
	@Autowired 
	RocBusinessObjectCodeDao rocBusinessObjectCodeDao;

	@Override
	public BaseDao getDefaultDao() {
		return rocBusinessObjectCodeDao;
	}

}
