package com.ssm.llp.base.common.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.LlpParametersDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.mod1.model.LlpUserProfile;

@Repository
public class LlpParametersDaoImpl extends BaseDaoImpl<LlpParameters, Long> implements LlpParametersDao, Serializable{

	@Override
	public List<LlpParameters> findByActiveNonActiveCodeType(String codeType) {
		
		String hql = "from "+LlpParameters.class.getName()
				+" where codeType=? order by idParameter asc ";
		
		return getHibernateTemplate().find(hql,codeType);
	}

	

	@Override
	public List<LlpParameters> findAllCodeTypeAsParameters() {
		String hql = "select distinct p.codeType from "+LlpParameters.class.getName()+" p order by p.codeType ";
		List listResult= getHibernateTemplate().find(hql);
		List<LlpParameters> listParameter = new ArrayList<LlpParameters>();
		for (int i = 0; i < listResult.size(); i++) {
			String codeType = (String) listResult.get(i);
			LlpParameters llpParameters = new LlpParameters();
			llpParameters.setCode(codeType);
			llpParameters.setCodeDesc(codeType);
			listParameter.add(llpParameters);
		}
		return listParameter;
	}



	@Override
	public List<LlpParameters> findByActiveCodeType(String codeType) {
		String hql = "from "+LlpParameters.class.getName()
				+" where codeType=? and status='A' order by seq ,codeDesc asc ";
		
		return getHibernateTemplate().find(hql,codeType);
	}



	@Override
	public void deleteAllByCodeType(String codeType) {
		String hql = " delete " + LlpParameters.class.getName() + "  where codeType = ? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, codeType);
		query.executeUpdate();
	}



	@Override
	public List<LlpParameters> findByActiveCodeType(String codeType, String orderBy) {
		String hql = "from "+LlpParameters.class.getName()
				+" where codeType=? and status='A' order by "+orderBy+" asc ";
		
		return getHibernateTemplate().find(hql,codeType);
	}
}
