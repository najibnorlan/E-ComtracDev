package com.ssm.ezbiz.dao;

import java.util.Date;
import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobParticipantRegTraining;

public interface RobParticipantRegTrainingDao extends BaseDao<RobParticipantRegTraining, Integer>{
	
	public List<RobParticipantRegTraining> getParticipantCurrentTrainingByStartDtEndDt(Date startDt, Date endDt, String ic);
}
