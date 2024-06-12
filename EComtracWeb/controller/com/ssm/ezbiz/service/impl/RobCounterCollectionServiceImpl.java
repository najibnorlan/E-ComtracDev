package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobCounterCollectionDao;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobCounterCollection;

@Service
public class RobCounterCollectionServiceImpl extends BaseServiceImpl<RobCounterCollection, Integer> implements RobCounterCollectionService{

	@Autowired
	RobCounterCollectionDao robCounterCollectionDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robCounterCollectionDao;
	}
	
	@Override
	public RobCounterCollection findByIpAddress(String ipAddress){
		SearchCriteria sc = new SearchCriteria("ipAddress", SearchCriteria.EQUAL, ipAddress).andIfNotNull("isActive", SearchCriteria.EQUAL, Parameter.YES_NO_yes);
		List<RobCounterCollection> counter = findByCriteria(sc).getList();
		if(counter.size() > 0){
			return counter.get(0);
		}
		return null;
	}

}
