package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobTrainingParticipantDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

@Repository
public class RobTrainingParticipantDaoImpl extends BaseDaoImpl<RobTrainingParticipant, Integer>
		implements RobTrainingParticipantDao {

	public List<RobTrainingParticipant> findAllParticipantByTrainingIdStatus(Integer trainingId, String status,
			String ic) {

		String hql = "select a from " + RobTrainingParticipant.class.getName() + " a"
				+ " join fetch a.robTrainingTransaction b" + " join fetch b.trainingId c" + " where c.trainingId = ?";

		if (ic != null) {
			hql += " and a.icNo = '" + ic + "'";

		}

		if (status != null) {
			hql += " and b.status IN (" + status + ")" + " order by a.name asc";
		}

		return getHibernateTemplate().find(hql, trainingId);
	}

	@Override
	public void deleteNotInId(String transactionCode, long[] idToDelete) {

		String hql = "delete RobTrainingParticipant" + " where robTrainingTransaction ="
				+ " (select b FROM RobTrainingTransaction b WHERE b.transactionCode = ?)";
		for (int i = 0; i < idToDelete.length; i++) {
			if (i == 0) {
				hql += " and participantId not in (";
			}
			if (i > 0) {
				hql += ",";
			}
			hql += "?";
		}
		hql += ")";
		Query query = getSession().createQuery(hql);
		query.setString(0, transactionCode);
		for (int i = 0; i < idToDelete.length; i++) {
			query.setLong(i + 1, idToDelete[i]);
		}
		query.executeUpdate();
	}

	@Override
	public void deleteUsingParticipantId(Integer participantId) {
		String hql = "delete RobTrainingParticipant" + " where participantId = ?";

		Query query = getSession().createQuery(hql);
		query.setInteger(0, participantId);
		query.executeUpdate();
	}

	@Override
	public RobTrainingParticipant findParticipantByTrainingId(Integer trainingId, String icNo) {

		String hql = "select a from " + RobTrainingParticipant.class.getName() + " a"
				+ " join fetch a.robTrainingTransaction b" + " join fetch b.trainingId c"
				+ " where c.trainingId = ? and a.icNo = ?";

		List<RobTrainingParticipant> list = getHibernateTemplate().find(hql, new Object[] { trainingId, icNo });
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<RobTrainingParticipant> findAllEligible() {
		// TODO Auto-generated method stub
		String hql = "select a from " + RobTrainingParticipant.class.getName() + " a"
				+ " join fetch a.robTrainingTransaction b" + " join fetch b.trainingId c"
//		        + " where a.isEligible = ? and a.fileId is null ";
				+ " where a.isEligible = ? and a.isAttend = ? and a.fileId is null ";

//		return getHibernateTemplate().find(hql, Parameter.YES_NO_yes);
		return getHibernateTemplate().find(hql, Parameter.YES_NO_yes, Parameter.YES_NO_yes);
	}

	@Override
	public Double totalRevenue(String type, String year, String month) {

		String isRefund = "N";
		String status = "PS";
		String paymentChannel = "UNPAID";

		ArrayList<String> param = new ArrayList<String>();
		String hql = null;
		double totalRevenue = 0.0;

		hql = "select a from " + RobTrainingParticipant.class.getName() + " a"
				+ " join fetch a.robTrainingTransaction b join fetch b.trainingId c"
				+ " where a.isRefund =? and b.status =? and b.paymentChannel !=?"
				+ " and to_char(c.trainingStartDt, '%Y')=? and to_char(c.trainingStartDt, '%m')=?";

		param.add(isRefund);
		param.add(status);
		param.add(paymentChannel);
		param.add(year);
		param.add(month);
		String[] paramArray = new String[param.size()];
		param.toArray(paramArray);

		List<RobTrainingParticipant> result = getHibernateTemplate().find(hql, paramArray);

		if (result.size() > 0) {
			return totalRevenue = result.stream().mapToDouble(RobTrainingParticipant::getAmount) // Extract the value
																									// property
					.sum();

		}

		return totalRevenue;
	}

}
