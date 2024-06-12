package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobTrainingConfigDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

@Repository
public class RobTrainingConfigDaoImpl extends BaseDaoImpl<RobTrainingConfig, Integer> implements RobTrainingConfigDao {

	@Override
	public List<RobTrainingConfig> getAvailableTraining() {
//		
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String hql = "select a from RobTrainingConfig a";

		if (!UserEnvironmentHelper.isInternalUser()) {
			hql += " where a.standardFee > 0 ";
			hql += " and a.specialFee > 0 ";
			hql += " and a.trainingStartTime != 'N/A' ";
			hql += " and a.trainingEndTime != 'N/A' ";
			hql += " and a.isActive = 'T' "; // T=true..
			hql += " and date(a.trainingStartDt) > today ";
			hql += " and a.trainingStartTime != 'N/A' ";
			hql += " and a.trainingEndTime != 'N/A' ";
		}
		hql += " order by a.trainingListSeq, a.trainingStartDt asc ";
		// hql += " order by a.trainingStartDt asc ";
		// hql += " order by a.trainingId desc ";

		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<RobTrainingConfig> getAvailableTrainingCorp() {
		String hql = "select a from RobTrainingConfig a";

		if (!UserEnvironmentHelper.isInternalUser()) {
			hql += " where a.standardFee = '0' ";
			hql += " and a.specialFee = '0' ";
			hql += " and a.isActive = 'T' "; // T=true..
			hql += " and date(a.trainingStartDt) > today ";
		}
		hql += " order by a.trainingListSeq, a.trainingStartDt asc ";
		// hql += " order by a.trainingStartDt asc ";
		// hql += " order by a.trainingId desc ";

		return getHibernateTemplate().find(hql);
	}

	@Override
	public Integer totalPax(String type, String year, String month) {

		String isRefund = "N";
		String status = "PS";
		String paymentChannel = "UNPAID";

		ArrayList<String> param = new ArrayList<String>();
		String hql = null;
		int results = 0;

		if ("PAX".equals(type)) {
			hql = "SELECT COUNT(a.participantId) " + "FROM " + RobTrainingParticipant.class.getName() + " a "
					+ "JOIN a.robTrainingTransaction b " + "JOIN b.trainingId c " + "WHERE a.isRefund = ? "
					+ "AND b.status = ? " + "AND b.paymentChannel != ? "
					+ "AND to_char(c.trainingStartDt, '%Y')=? and to_char(c.trainingStartDt, '%m')=?";

			param.add(isRefund);
			param.add(status);
			param.add(paymentChannel);
		} else if ("TOTAL_PAX".equals(type)) {
			hql = "select sum(a.maxPax) " + " from RobTrainingConfig a "
					+ " where to_char(a.trainingStartDt, '%Y')=? and to_char(a.trainingStartDt, '%m')=?";
		} else if ("PAST_PROGRAM".equals(type)) {
			hql = "select count(a.trainingId) " + " from RobTrainingConfig a "
					+ " where to_char(a.trainingStartDt, '%Y')=? and to_char(a.trainingStartDt, '%m')=? and date(a.trainingStartDt) <= today";
		} else if ("FUTURE_PROGRAM".equals(type)) {
			hql = "select count(a.trainingId) " + " from RobTrainingConfig a "
					+ " where to_char(a.trainingStartDt, '%Y')=? and to_char(a.trainingStartDt, '%m')=? and date(a.trainingStartDt) > today";
		}

		param.add(year);
		param.add(month);
		String[] paramArray = new String[param.size()];
		param.toArray(paramArray);

		List<Long> result = getHibernateTemplate().find(hql, paramArray);

		if (result.size() > 0) {
			if (result.get(0) != null) {
				return result.get(0).intValue();
			}
		}

		return results;
	}
}
