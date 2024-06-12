package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssm.ezbiz.dao.RobParticipantRegTrainingDao;
import com.ssm.ezbiz.service.RobParticipantRegTrainingService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobParticipantRegTraining;

@Service
public class RobParticipantRegTrainingServiceImpl extends BaseServiceImpl<RobParticipantRegTraining, Integer> implements RobParticipantRegTrainingService{
	
	@Autowired
	RobParticipantRegTrainingDao robParticipantRegTrainingDao;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return robParticipantRegTrainingDao;
	}
	
	
	@Override
	public List<RobParticipantRegTraining> findParticipantCurrentTrainingBetweenStartDtEndDt(Date startDt, Date endDt, String ic)throws SSMException{
		return robParticipantRegTrainingDao.getParticipantCurrentTrainingByStartDtEndDt(startDt, endDt, ic);
	}


}
