package com.ssm.llp.mod1.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.mod1.model.RobUserOku;

public interface RobUserOkuDao  extends BaseDao<RobUserOku, String> {
	
	RobUserOku findLatestOkuByRefNo(String okuRefNo);
	
	RobUserOku findLatestOkuByUserRefNo(String userRefNo);
	
	RobUserOku findLatestOkuByIdTypeAndIdNo(String idType, String idNo); //userProfile.idType userProfile.idNo
	
	RobUserOku findLatestOkuByUserRefNoAndIdRegUser(String userRefNo, long idRegUser);
	
	List<RobUserOku> getListRobUserOku(String userRefNo, String okuRegStatus);

	RobUserOku findLatestOkuByUserRefNoWithData(String userRefNo);

	RobUserOku findOkuByRefNoWithData(String okuRefNo);


}

