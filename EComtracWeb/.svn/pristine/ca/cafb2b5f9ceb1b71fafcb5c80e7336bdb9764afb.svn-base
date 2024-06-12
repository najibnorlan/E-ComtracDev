package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormADao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormA;

@Repository
public class RobFormADaoImpl extends BaseDaoImpl<RobFormA, String> implements RobFormADao {

	// @Override
	// public RobFormA findAllById(String robFormARefNo) {
	// RobFormA robFormA = findById(robFormARefNo);
	// if(robFormA!=null){
	// List list = robFormA.getListRobFormABizCode();
	// robFormA.setListRobFormABizCode(list);
	//
	// List list2 =robFormA.getListRobFormABranches();
	// robFormA.setListRobFormABranches(list2);
	//
	// List list3 =robFormA.getListRobFormAOwner();
	// robFormA.setListRobFormAOwner(list3);
	//
	// }
	// return robFormA;
	// }

	@Override
	public RobFormA findByIdWithData(String id) {
//		RobFormA robFormA  = findById(id);
//		System.out.println(robFormA.getFormAData().getFileDataId());
		Session session = getSession();
		RobFormA robFormA = (RobFormA) session.load(RobFormA.class, id);

		
//		Query q = getSession().createQuery("from "+RobFormA.class.getName() +" where robFormACode=? ");
//	    q.setFirstResult(0); // modify this to adjust paging
//	    q.setMaxResults(1);
//	    q.setString(0, id);
//	    RobFormA robFormA = (RobFormA) q.list().get(0);

		Hibernate.initialize(robFormA.getFormAData());
		Hibernate.initialize(robFormA.getCertFileData());
		
		if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
				Hibernate.initialize(robFormA.getBusinessInfoData());
		}
		if (StringUtils.isNotBlank(robFormA.getCompoundNo())) {
				Hibernate.initialize(robFormA.getCmpNoticeFileData());
		}
//		System.out.println("robFormA.getCertFileData():"+robFormA.getCertFileData().getFileData().length);	
		return robFormA;
	}

	@Override
	public Integer countFormAByTypeAndStatus(String type, String status, String year, String month) {

		ArrayList<String> param = new ArrayList<String>();
		String hql = "from " + RobFormA.class.getName() + " where status=?";
		if (Parameter.ROB_FORM_A_STATUS_APPROVED.equals(status) || Parameter.ROB_FORM_A_STATUS_REJECT.equals(status)) {
			hql += " and to_char(approveRejectDt, '%Y')=? and to_char(approveRejectDt, '%m')=?";
//			hql += " and DATE_FORMAT(approveRejectDt, '%Y')=? and DATE_FORMAT(approveRejectDt, '%m')=?";//MYSQL
		} else {
			hql += " and to_char(createDt, '%Y')=? and to_char(createDt, '%m')=?";
//			hql += " and DATE_FORMAT(createDt, '%Y')=? and DATE_FORMAT(createDt, '%m')=?";//MYSQL
		}
		
		param.add(status);
		param.add(year);
		param.add(month);
		if (type.equalsIgnoreCase("ONLINE SELLER")) {
			hql += " and isOnlineSeller=?";
			param.add(Parameter.YES_NO_yes);
		} else if (type.equalsIgnoreCase("INCUBATOR")) {
			hql += " and is_incubator=?";
			param.add(Parameter.YES_NO_yes);
		} else if (type.equalsIgnoreCase("STUDENT")) {
			hql += " and incentive=?";
			param.add(Parameter.ROB_FORM_A_INCENTIVE_student);
		} else if (type.equalsIgnoreCase("OKU")) {
			hql += " and incentive=?";
			param.add(Parameter.ROB_FORM_A_INCENTIVE_oku);
		}

		System.out.println("hsql : " + hql + " | status : " + status);
		String[] paramArray = new String[param.size()];
		param.toArray(paramArray);

		List results = getHibernateTemplate().find(hql, paramArray);

		return results.size();
	}

	@Override
	public void cancelPendingPaymentMoreThanXDays(Integer noOfDays) {

		String sqlGetFormCodeToCancel = " 		select app_ref_no from rob_payment_transaction where app_ref_no in( "
				+ " 			select rob_form_a_code from rob_form_a  " + " 			where status='PP' and date(update_dt)< current - "+noOfDays.intValue()+" units day "
				+ " 		)and status not in ('P','S') ";
		SQLQuery query = getSession().createSQLQuery(sqlGetFormCodeToCancel);
		List list = query.list();

		if (list.size() > 0) {
			String sql = " update rob_form_a "
					+ " set status='C', update_by='system', update_dt=current, approve_reject_notes='cancel because more then "+noOfDays.intValue()+" days pending payment' "
					+ " where rob_form_a_code in( ";
			for (int i = 0; i < list.size(); i++) {
				if (i != 0) {
					sql += ",";
				}
				sql += "?";
			}
			sql += " ) and status='PP' ";
			SQLQuery queryUpdate = getSession().createSQLQuery(sql);
			for (int i = 0; i < list.size(); i++) {
				queryUpdate.setParameter(i, list.get(i));
			}

			int update = queryUpdate.executeUpdate();
			System.out.println("cancelPendingPaymentMoreThan"+noOfDays.intValue()+"Days Update: " + update + " records");
			
		}
	}

	@Override
	public void cancelDraftMoreThanXDays(Integer noOfDays) {
		String sql = " update rob_form_a "
				+ " set status='C', update_by='auto_cancel', update_dt=current, approve_reject_notes='cancel because more then "+noOfDays.intValue()+" days draff' "
				+ " where status in ('OTC','PP','DE') and date(update_dt) < current - "+noOfDays.intValue()+" units day ";

		SQLQuery queryUpdate = getSession().createSQLQuery(sql);
		
		int update = queryUpdate.executeUpdate();
		System.out.println("cancelPendingPaymentMoreThan"+noOfDays.intValue()+"Days Update: " + update + " records");
		
	}

	@Override
	public void updateBaseListDistribution(String[] listUpdatedTrans) {
		for (int i = 0; i < listUpdatedTrans.length; i++) {
			String br = listUpdatedTrans[i].split(":")[0];
			System.out.println("Branch:"+br);
			String paramRefNo = listUpdatedTrans[i].split(":")[1];
			String sql = " update rob_form_a "
					+ " set prosessing_branch=?, update_by='sysDist' "
					+ " where status='IP' and rob_form_a_code in ("+paramRefNo+") ";
			SQLQuery queryUpdate = getSession().createSQLQuery(sql);
			queryUpdate.setString(0, br);
			int update = queryUpdate.executeUpdate();
			System.out.println("updateBaseListDistribution:" + update + " records");
		}
		
	}
}
