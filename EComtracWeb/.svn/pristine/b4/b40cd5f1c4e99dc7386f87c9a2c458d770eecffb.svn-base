package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.ExtUserPairingInfoDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;

@Repository
public class ExtUserPairingInfoDaoImpl extends BaseDaoImpl<ExtUserPairingInfo, Long>  implements ExtUserPairingInfoDao{

	@Override
	public ExtUserPairingInfo findByLatestSessionByEzBizId(String ezbizId) {
		String hql = "from "+ExtUserPairingInfo.class.getName()
				+" where ezbizId=? order by updateDt desc";
		
		List<ExtUserPairingInfo> list = getHibernateTemplate().find(hql, ezbizId);
		
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
