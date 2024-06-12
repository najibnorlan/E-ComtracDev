package com.ssm.llp.mod1.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.RobUserTncDao;
import com.ssm.llp.mod1.model.RobUserTnc;

@Repository
public class RobUserTncDaoImpl extends BaseDaoImpl<RobUserTnc, String> implements RobUserTncDao {
	
	@Override
	public RobUserTnc getActiveUserTnc(String userRefNo, String loginId, String tncType) {
		String hql = "from " + RobUserTnc.class.getName() + " where userRefNo=? "
				+ "and loginId=? and tncType=? "
				+ "and isAgree = '"+Parameter.TNC_IS_AGREE_yes
				+ "' and agreementStatus = '"+Parameter.TNC_AGREEMENT_STATUS_active
				+ "'";
		
				List<RobUserTnc> listResult = getHibernateTemplate().find(hql, new Object[] { userRefNo, loginId, tncType });
				//System.out.println("\nSQL getActiveUserTnc ----> "+hql);
				if (listResult.size() > 0) {
					return listResult.get(0);
				}
				return null;
	}
	
	@Override
	public boolean hasAgreeTnc(String userRefNo, String loginId, String tncType) { //get count..
		
		String hql2 = "SELECT count(userRefNo)" + " FROM " + RobUserTnc.class.getName()
				+ " WHERE userRefNo=? and loginId=? and tncType=? "
				+ "and isAgree = '"+Parameter.TNC_IS_AGREE_yes
				+ "' and agreementStatus = '"+Parameter.TNC_AGREEMENT_STATUS_active
				+ "'";

		List list = getHibernateTemplate().find(hql2,userRefNo,loginId, tncType);
	//	System.out.println("\nSQL hasAgreeTnc ----> "+hql2);
		if((Long)list.get(0)>0) {
			return true;
		}
		return false;
	} 
	
}
