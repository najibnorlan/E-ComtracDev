package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormABranchesDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;

@Repository
public class RobFormABranchesDaoImpl extends BaseDaoImpl<RobFormABranches, Long> implements RobFormABranchesDao {

	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		
		String hql = " delete " + RobFormABranches.class.getName() + "  where robFormACode = ? ";
		for (int i = 0; i < idToDelete.length; i++) {
			if(i==0){
				hql += " and robFormABranchesId not in ( ";
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
	public List<RobFormABranches> findByRobFormACode(String formACode) {
		String hql = "from "+RobFormABranches.class.getName()
				+" where robFormACode=?  ";
		
		return getHibernateTemplate().find(hql, formACode);
	}


}
