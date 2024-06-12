package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobRenewalDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.ezbiz.model.RobRenewal;

@Repository
public class RobRenewalDaoImpl extends BaseDaoImpl<RobRenewal, String> implements RobRenewalDao {

	@Override
	public RobRenewal findByIdWithData(String id) {
		RobRenewal robRenewal = findById(id);
		return robRenewal;
	}

}
