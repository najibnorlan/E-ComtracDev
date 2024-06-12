package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;

public interface ExtUserPairingInfoService extends BaseService<ExtUserPairingInfo, Long>{

	ExtUserPairingInfo findByLatestSessionByEzBizId(String ezbizId);

	void resetStatus(String loginId, String pairingStatus);

}
