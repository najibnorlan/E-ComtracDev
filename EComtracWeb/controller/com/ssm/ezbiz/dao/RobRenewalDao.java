package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobRenewal;

public interface RobRenewalDao  extends BaseDao<RobRenewal, String> {

	RobRenewal findByIdWithData(String id);

}

