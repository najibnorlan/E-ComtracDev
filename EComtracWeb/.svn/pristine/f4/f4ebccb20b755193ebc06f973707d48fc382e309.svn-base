package com.ssm.supplyinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.supplyinfo.dao.SupplyInfoTransDtlDao;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.service.SupplyInfoTransDtlService;

@Service
public class SupplyInfoTransDtlServiceImpl extends BaseServiceImpl<SupplyInfoTransDtl, Long>implements SupplyInfoTransDtlService{
	
	@Autowired
	private SupplyInfoTransDtlDao supplyInfoTransDtlDao;
	
	
	@Override 
	public  BaseDao getDefaultDao() {
		return supplyInfoTransDtlDao;
	}


	@Override
	public void deleteByHdrCode(String transCode) {
		supplyInfoTransDtlDao.deleteByHdrCode(transCode);
	}

}
