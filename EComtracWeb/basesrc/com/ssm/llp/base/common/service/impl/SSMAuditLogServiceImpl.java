package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.SSMAuditLogDao;
import com.ssm.llp.base.common.model.SSMAuditLogModel;
import com.ssm.llp.base.common.service.SSMAuditLogService;

@Service
public class SSMAuditLogServiceImpl implements SSMAuditLogService{
	
	@Autowired
	SSMAuditLogDao ssmAuditLogDao;
	
	@Override
	public SSMAuditLogModel findAuditLog(String clazzName,Object primaryKey, String filterField) {
		return ssmAuditLogDao.findAuditLog(clazzName,primaryKey,filterField);
	}

}
