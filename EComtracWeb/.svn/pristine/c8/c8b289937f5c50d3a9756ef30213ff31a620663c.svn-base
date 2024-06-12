package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

public interface RobTrainingParticipantDao extends BaseDao<RobTrainingParticipant, Integer>{
	
	public List<RobTrainingParticipant> findAllParticipantByTrainingIdStatus(Integer trainingId, String status, String ic);

	void deleteNotInId(String transactionCode, long[] idToDelete);
	
	public void deleteUsingParticipantId(Integer participantId);
	
	public RobTrainingParticipant findParticipantByTrainingId(Integer trainingId, String icNo);

	public List<RobTrainingParticipant> findAllEligible();
	
	public Double totalRevenue(String type, String year, String month);
}
