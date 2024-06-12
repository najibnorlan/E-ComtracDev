package com.ssm.ezbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.ExtUserPairingInfoDao;
import com.ssm.ezbiz.service.ExtUserPairingInfoService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;

@Service
public class ExtUserPairingInfoServiceImpl extends BaseServiceImpl<ExtUserPairingInfo, Long> implements ExtUserPairingInfoService {

	@Autowired
	ExtUserPairingInfoDao extUserPairingInfoDao;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return extUserPairingInfoDao;
	}


	@Override
	public ExtUserPairingInfo findByLatestSessionByEzBizId(String ezbizId) {
		return extUserPairingInfoDao.findByLatestSessionByEzBizId(ezbizId);
	}


	@Override
	public void resetStatus(String loginId,String pairingStatus) {
		
		ExtUserPairingInfo userPairingInfo = findByLatestSessionByEzBizId(loginId);
		if(userPairingInfo!=null) {
			userPairingInfo.setStatus(pairingStatus);
			extUserPairingInfoDao.update(userPairingInfo);
		}
		
	}
	

}
