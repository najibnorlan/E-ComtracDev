package com.ssm.llp.base.common.dao.impl;

import java.io.Serializable;

import com.ssm.llp.base.common.code.DbType;


public abstract class RocBaseDaoImpl<T, ID extends Serializable> extends BaseDaoImpl<T, ID>{
	@Override
	public String getDbType() {
		return DbType.ROC;
	}
	
}
