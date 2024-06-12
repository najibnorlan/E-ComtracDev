package com.ssm.llp.mod1.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.LlpBusinessCodeLinkDao;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.model.LlpPartnerLink;


@Repository
public class LlpBusinessCodeLinkDaoImpl extends BaseDaoImpl<LlpBusinessCodeLink, Long> implements LlpBusinessCodeLinkDao {

	@Override
	public List<LlpBusinessCodeLink> findByLlpNo(String llpNo) {
		String hql = "from "+LlpBusinessCodeLink.class.getName()
				+" where llpNo=? "
				+" and status='A' "
				+" order by idBusinessCodeLink asc " ;
		
		return getHibernateTemplate().find(hql,llpNo);
	}
}

