package com.ssm.llp.mod1.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.RobUserOku;


public interface RobUserOkuService extends BaseService<RobUserOku, String> {

	public RobUserOku findOkuByRefNo(String okuRefNo) ;
	
	public RobUserOku findOkuByUserRefNo(String userRefNo);
	
	public RobUserOku findOkuByIdTypeAndIdNo(String idType, String idNo);//userProfile.idType userProfile.idNo
	
	public RobUserOku findOkuByUserRefNoWithData(String userRefNo);
	
	public RobUserOku findOkuByUserRefNoAndIdRegUser(String userRefNo, long idRegUser); //belum guna
	
	public List<RobUserOku> getListRobUserOku(String userRefNo, String okuRegStatus); //belum guna
	
	public void updatePreviousRecordStatus(RobUserOku robUserOku) throws SSMException;
	
	public RobUserOku insertUpdateOkuApplication(RobUserOku robUserOku, String cmd);
	
	public void updateOkuApproval(RobUserOku robUserOku);
	
	public void sendEmailOku(RobUserOku robUserOku, String process);
	
	public void discardApplication(RobUserOku robUserOku);

	public RobUserOku findOkuByRefNoWithData(String okuRefNo);
}
