package com.ssm.ezbiz.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

public interface RobTrainingParticipantService extends BaseService<RobTrainingParticipant, Integer>  {
	
	public List<RobTrainingParticipant> findAllParticipantByTrainingIdStatus(Integer trainingId, String[] status, String ic);
	
	public RobTrainingParticipant findByTransactionCodeIcNo(String transactionCode, String icNo);
	
	public RobTrainingParticipant findByTransactionCodeIcNoLodger(String transactionCode, String icNo, String lodgerName);
	
	void deleteNotInId(String transactionCode, long[] idToDelete);
	
	void deleteUsingParticipantId(Integer participantId);
	
	public byte[] generateExcelParticipant(RobTrainingConfig robTrainingConfig, List<RobTrainingParticipant> listParticipant);

	public List<RobTrainingParticipant> findAllEligible();

	public void generateCertificate(RobTrainingParticipant participant);
	
	public Date calculateDob(String idno) throws ParseException;
	
	public Double totalRevenue(String type, String year, String month);
}
