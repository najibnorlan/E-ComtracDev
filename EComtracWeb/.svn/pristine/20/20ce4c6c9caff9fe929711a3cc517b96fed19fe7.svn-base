package com.ssm.llp.mod1.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.RobIncentiveDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.RobIncentive;


@SuppressWarnings("unchecked")
@Repository
public class RobIncentiveDaoImpl extends BaseDaoImpl<RobIncentive, String> implements RobIncentiveDao {

	@Override
	public RobIncentive getLastIncentiveApprovedDate(String newIc, String incentiveType) {
		
		//yang status approve sahaja
		String hql = "from " + RobIncentive.class.getName() + " where idNo=? and incentiveType=? and applicationStatus in ('A','S') order by incentiveApprovalDt desc";

		List<RobIncentive> listResult = getHibernateTemplate().find(hql, new Object[] { newIc, incentiveType });
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	
	@Override
	public List<RobIncentive> findListIncentiveByRobFormCode(String robFormCode) { 
		
		//return ownerOku tunggal dan pkongsian
		String hql = "from "+RobIncentive.class.getName() +" where robFormCode=?";
				
		List<RobIncentive> listResult = getHibernateTemplate().find(hql, robFormCode);
		if (listResult.size() > 0) {
			return listResult;
		}
		return null;
	}
	
	@Override
	public String checkLastApplicationStatus(String newIc, String incentiveType, String formType) {
		
		String hql = "from " + RobIncentive.class.getName() + " where idNo=? and incentiveType=? and incentiveForm=? order by updateDt desc";

		List<RobIncentive> listResult = getHibernateTemplate().find(hql, new Object[] { newIc, incentiveType, formType });
		if (listResult.size() > 0) {
			return listResult.get(0).getApplicationStatus() +"_"+ listResult.get(0).getRobFormCode();
		}
		return "";	
	}
	
}
