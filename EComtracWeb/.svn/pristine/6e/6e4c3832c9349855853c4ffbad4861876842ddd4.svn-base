package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormAOwner;

public interface RobFormAOwnerDao  extends BaseDao<RobFormAOwner, Long> {
	void deleteNotInId(String robFormACode, long[] idToDelete);
	
	List<RobFormAOwner> findByRobFormACode(String formACode);

	RobFormAOwner findByRobFormACodeAndIcNo(String robFormACode, String icNo);
	
	String getVerificationStatus(String robFormACode, String icNo);

	List<RobFormAOwner> findOwnerNotInId(String robFormACode, long[] id);
	
	public List<RobFormAOwner> checkIfAlreadyUseIncentiveFormA(String icNo, String incentive, String currentFormACode);
	
	public List<RobFormAOwner> checkIfAlreadyUseIncentiveFormA1(String icNo, String incentive, String currentRefCode);

	public List<RobFormAOwner> findByIcNoInStatus(String icNo, String status);
}

