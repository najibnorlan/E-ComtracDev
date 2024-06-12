package com.ssm.ezbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobSyncProfileAuditDao;
import com.ssm.ezbiz.service.RobSyncProfileAuditService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobSyncProfileAudit;

@Service
public class RobSyncProfileAuditServiceImpl extends BaseServiceImpl<RobSyncProfileAudit, Integer> implements RobSyncProfileAuditService{

	@Autowired
	RobSyncProfileAuditDao robSyncProfileAuditDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robSyncProfileAuditDao;
	}

}
