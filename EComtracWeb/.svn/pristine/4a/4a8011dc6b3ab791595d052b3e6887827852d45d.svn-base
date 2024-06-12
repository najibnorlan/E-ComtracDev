/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.dao.LlpUserLogDao;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpUserLog;

@Repository
public class LlpUserLogDaoImpl extends BaseDaoImpl<LlpUserLog, Long>  implements LlpUserLogDao{

	@Override
	public LlpUserLog findLastLogin(String userId) {
		Query q = getSession().createQuery("from "+LlpUserLog.class.getName() +" where loginId=? order by userLogId desc ");
	    q.setFirstResult(0); // modify this to adjust paging
	    q.setMaxResults(1);
	    q.setString(0, userId);
	    List<LlpUserLog> list = q.list();
	    if(list.size()>0){
	    	return list.get(0);
	    }
		return null;
	}
}
