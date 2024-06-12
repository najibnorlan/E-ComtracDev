package com.ssm.ezbiz.service;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobParticipantRegTraining;

public interface RobParticipantRegTrainingService extends BaseService<RobParticipantRegTraining, Integer>  {
	
	public List<RobParticipantRegTraining> findParticipantCurrentTrainingBetweenStartDtEndDt(Date startDt, Date endDt, String ic)throws SSMException;

}
