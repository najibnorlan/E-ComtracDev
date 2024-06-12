package com.ssm.ezbiz.dao.impl;

import java.io.Serializable;

import com.ssm.llp.base.common.code.DbType;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;


public abstract class RobBaseDaoImpl<T, ID extends Serializable> extends BaseDaoImpl<T, ID>{
	@Override
	public String getDbType() {
		return DbType.ROB;
	}
	
}
