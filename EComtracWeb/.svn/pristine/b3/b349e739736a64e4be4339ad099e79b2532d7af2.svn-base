package com.ssm.ezbiz.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormAOwnerDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormAOwner;

@Repository
public class RobFormAOwnerDaoImpl extends BaseDaoImpl<RobFormAOwner, Long> implements RobFormAOwnerDao {
	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		
		String hql = " delete " + RobFormAOwner.class.getName() + "  where robFormACode = ? ";
		for (int i = 0; i < idToDelete.length; i++) {
			if(i==0){
				hql += " and robFormAOwnerId not in ( ";
			}
			if(i>0){
				hql += ",";
			}
			hql += "?";
		}
		hql += ")"; 
		Query query = getSession().createQuery(hql);
		query.setString(0, robFormACode);
		for (int i = 0; i < idToDelete.length; i++) {
			query.setLong(i+1, idToDelete[i]);
		}
		query.executeUpdate();
	}

	@Override
	public List<RobFormAOwner> findByRobFormACode(String formACode) {
		String hql = "from "+RobFormAOwner.class.getName()
				+" where robFormACode=?  ";
		
		return getHibernateTemplate().find(hql, formACode);
	}
	
	@Override
	public List<RobFormAOwner> findByIcNoInStatus(String icNo, String status){
		 
		String hql = "select a from RobFormAOwner as a, RobFormA as b where"
				+ " a.robFormACode = b.robFormACode AND"
				+ " a.newicno=? AND b.status IN ('" + status + "')";
		
		List<RobFormAOwner> result = getHibernateTemplate().find(hql, new String[]{icNo});
		
		return result;
		
	}

	@Override
	public RobFormAOwner findByRobFormACodeAndIcNo(String robFormACode, String icNo) {
		String hql = "from "+RobFormAOwner.class.getName()
				+" where robFormACode=? and newicno=? ";
		
		List<RobFormAOwner> result = getHibernateTemplate().find(hql, new String[]{robFormACode ,icNo});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public String getVerificationStatus(String robFormACode, String icNo) {
		
		String status = "PV";
		String hql = "from "+RobFormAOwner.class.getName()
				+" where robFormACode=? and newicno=? ";
		
		if(!StringUtils.isBlank(robFormACode)){
			
			List listParam = new ArrayList();
			listParam.add(robFormACode);
			listParam.add(icNo);
			
			List<RobFormAOwner> result = getHibernateTemplate().find(hql, new String[]{robFormACode ,icNo});
			
			if(result.size()>0){
				RobFormAOwner robFormAOwner = result.get(0);
				
				if(robFormAOwner != null){
					status = robFormAOwner.getVerificationStatus();
				}
			}
		}
	
		
		return status;
	
	}

	@Override
	public List<RobFormAOwner> findOwnerNotInId(String robFormACode, long[] idToDelete) {
		String hql = " from " + RobFormAOwner.class.getName() + "  where robFormACode = ? ";
		List listParam = new ArrayList();
		listParam.add(robFormACode);
		for (int i = 0; i < idToDelete.length; i++) {
			if(i==0){
				hql += " and robFormAOwnerId not in ( ";
			}
			if(i>0){
				hql += ",";
			}
			hql += "?";
			listParam.add(idToDelete[i]);
		}
		hql += ")"; 
		
		return getHibernateTemplate().find(hql, listParam.toArray());
	}
	
	@Override
	public List<RobFormAOwner> checkIfAlreadyUseIncentiveFormA(String icNo, String incentive, String currentFormACode) {
		
		List<RobFormAOwner> result = new ArrayList<RobFormAOwner>();
		Calendar cal = Calendar.getInstance();
		Integer curYear = cal.get(Calendar.YEAR);
		
		String hql = "from RobFormAOwner as a, RobFormA as b where"
				+ " a.robFormACode = b.robFormACode AND"
				+ " a.newicno=? AND b.status not in ('R','C') AND b.incentive=? AND a.robFormACode IS NOT NULL AND a.robFormACode<>?";
		
		if(incentive.equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)){
			hql += " AND to_char(b.submitDt, '%Y') = " + curYear.toString();
//			hql += " AND DATE_FORMAT(b.submitDt, '%Y') = " + curYear.toString();//MYSQL
		}
		List listResult =  getHibernateTemplate().find(hql, new String[]{icNo ,incentive,currentFormACode});
		for (int i = 0; i < listResult.size(); i++) {
			Object obj[] = (Object[]) listResult.get(i);
			result.add((RobFormAOwner) obj[0]);
		}
		return result;
	}
	
	@Override
	public List<RobFormAOwner> checkIfAlreadyUseIncentiveFormA1(String icNo, String incentive, String currentRefCode) {
		Calendar cal = Calendar.getInstance();
		Integer curYear = cal.get(Calendar.YEAR);
		List<RobFormAOwner> result = new ArrayList<RobFormAOwner>();
		String hql = "from RobFormAOwner as a, RobFormA as b, RobRenewal c where"
				+ " a.robFormACode = b.robFormACode AND"
				+ " b.brNo = c.brNo||'-'||c.chkDigit AND"
				+ " a.newicno=? AND b.status='A' AND c.renewalIncentive=? AND"
				+ " c.status='S'"
				+ " AND c.transCode<>? "
				+ " AND to_char(c.createDt, '%Y') = " + curYear.toString();
//				+ " AND DATE_FORMAT(c.createDt, '%Y') = " + curYear.toString();//MYSQL			
		
		List listResult =   getHibernateTemplate().find(hql, new String[]{icNo ,incentive,currentRefCode});
		for (int i = 0; i < listResult.size(); i++) {
			Object obj[] = (Object[]) listResult.get(i);
			result.add((RobFormAOwner) obj[0]);
		}
		return result;
	}
}
