package com.ssm.ezbiz.dao;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;

public interface ExtUserPairingInfoDao extends BaseDao<ExtUserPairingInfo, Long> {

	ExtUserPairingInfo findByLatestSessionByEzBizId(String ezbizId);

}
