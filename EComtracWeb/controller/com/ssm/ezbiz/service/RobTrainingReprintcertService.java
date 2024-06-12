package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;

public interface RobTrainingReprintcertService extends BaseService<RobTrainingReprintcert, Integer>  {
	
	public RobTrainingReprintcert updateInsertAll(RobTrainingReprintcert robTrainingReprintcert);
	
	public RobTrainingReprintcert findBycertRefNo(String certRefNo);
	
	public RobTrainingReprintcert findDrafTransaction(String transactionCode, String icNo, String lodgerId);
	
	public List<RobTrainingReprintcert> findAllPendingGenerateCert();
	
	public void generateCertificate(RobTrainingReprintcert robTrainingParticipant);
	
	void sendEmailConfirmation(String certRefNo);
	
}
