package com.ssm.llp.mod1.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.LlpPartnerLinkDao;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpReservedName;


@Repository
public class LlpPartnerLinkDaoImpl extends BaseDaoImpl<LlpPartnerLink, Long> implements LlpPartnerLinkDao {

	@Override
	public List<LlpPartnerLink> findByLlpNo(String llpNo) {
		String hql = "from "+LlpPartnerLink.class.getName()
				+" where llpNo=? "
				+" and linkStatus='A' "
				+" order by idPartnerLink desc " ;
		
		return getHibernateTemplate().find(hql,llpNo);
	}
}

