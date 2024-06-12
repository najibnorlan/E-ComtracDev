package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.webis.param.BlacklistInfoResp;

public interface RobFormAOwnerService extends BaseService<RobFormAOwner, Long> {
	void deleteNotInId(String robFormACode, long[] idToDelete);
	
	List<RobFormAOwner> findOwnerNotInId(String robFormACode, long[] id);
	
	List<RobFormAOwner> findByRobFormACode(String formACode);

	RobFormAOwner findByRobFormACodeAndIcNo(String robFormACode, String icNo);
	
	public String getVerificationStatus(String robFormACode, String icNo);
	
	public void checkIfAlreadyUseIncentive(List<RobFormAOwner> listRobFormAOwner, String incentive, String currentFormACode)throws SSMException;

	public List<RobFormAOwner> findByIcNoInStatus(String icNo, String[] status);
	
	//check blacklist
	public BlacklistInfoResp getBlacklistInfoByICNoWS (String idNo, String idType, String entityType) throws SSMException;
}
