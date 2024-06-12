package com.ssm.llp.base.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.RobPaymentCardInfoDao;
import com.ssm.llp.base.common.service.RobPaymentCardInfoService;
import com.ssm.llp.ezbiz.model.RobPaymentCardInfo;

@Service
public class RobPaymentCardInfoServiceImpl extends BaseServiceImpl<RobPaymentCardInfo,  String> implements RobPaymentCardInfoService{

	@Autowired 
	RobPaymentCardInfoDao robPaymentCardInfoDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robPaymentCardInfoDao;
	}

}
