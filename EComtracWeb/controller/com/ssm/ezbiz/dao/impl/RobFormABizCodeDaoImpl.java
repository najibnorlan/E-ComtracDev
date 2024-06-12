package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormABizCodeDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;

@Repository
public class RobFormABizCodeDaoImpl extends BaseDaoImpl<RobFormABizCode, Long> implements RobFormABizCodeDao {
	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		
		String hql = " delete " + RobFormABizCode.class.getName() + "  where robFormACode = ? ";
		for (int i = 0; i < idToDelete.length; i++) {
			if(i==0){
				hql += " and robFormABizCodeId not in ( ";
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
	public List<RobFormABizCode> findByRobFormACode(String formACode) {
		String hql = "from "+RobFormABizCode.class.getName()
				+" where robFormACode=?  ";
		
		return getHibernateTemplate().find(hql, formACode);
	}
}
