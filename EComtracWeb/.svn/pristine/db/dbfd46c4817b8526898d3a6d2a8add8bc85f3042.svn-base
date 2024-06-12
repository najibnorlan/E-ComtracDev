package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormNotesDao;
import com.ssm.ezbiz.dao.RobTrainingFinalListingRemarkDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListingRemark;

@Repository
public class RobTrainingFinalListingRemarkDaoImpl extends BaseDaoImpl<RobTrainingFinalListingRemark, Integer>
		implements RobTrainingFinalListingRemarkDao {

	@Override
	public List findByFinalListingRefNo(String finalListingRefNo) {
		String hql = "from " + RobTrainingFinalListingRemark.class.getName()
				+ " where finalListingRefNo=? order by createDt asc";

		return getHibernateTemplate().find(hql, finalListingRefNo);
	}
}
