package com.ssm.supplyinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.supplyinfo.dao.SupplyInfoFileDataDao;
import com.ssm.supplyinfo.model.SupplyInfoFileData;
import com.ssm.supplyinfo.service.SupplyInfoFileDataService;

@Service
public class SupplyInfoFileDataServiceImpl extends BaseServiceImpl<SupplyInfoFileData, Long> implements SupplyInfoFileDataService{
	
	@Autowired
	SupplyInfoFileDataDao supplyInfoFileDataDao;
	
	
	@Override 
	public  BaseDao getDefaultDao() {
		return supplyInfoFileDataDao;
	}

}
