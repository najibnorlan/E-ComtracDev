package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobEBranchTransactionDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobEBranchTransaction;

@Repository
public class RobEBranchTransactionDaoImpl extends BaseDaoImpl<RobEBranchTransaction, Long> implements RobEBranchTransactionDao {

	@Override
	public RobEBranchTransaction findActiveByBrNo(String brNo) {
		String hql = "from "+RobEBranchTransaction.class.getName()
				+" where brNo=? and status='A' ";
		
		List results = getHibernateTemplate().find(hql, brNo);
		
		if(results.size()>0){
			return (RobEBranchTransaction) results.get(0);
		}
		return null;
		
	}

}
