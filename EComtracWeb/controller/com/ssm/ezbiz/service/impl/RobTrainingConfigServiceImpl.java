package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.comtrac.SelectComtracTraining;
import com.ssm.ezbiz.comtrac.SelectCorporateTraining;
import com.ssm.ezbiz.dao.RobTrainingConfigDao;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;

@Service
public class RobTrainingConfigServiceImpl extends BaseServiceImpl<RobTrainingConfig, Integer> implements RobTrainingConfigService{

	@Autowired
	RobTrainingConfigDao robTrainingConfigDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robTrainingConfigDao;
	}

	public List<RobTrainingConfig> getAvailableTraining(){
		return robTrainingConfigDao.getAvailableTraining();
	}
	
	public List<RobTrainingConfig> getAvailableTrainingCorp() {
		return robTrainingConfigDao.getAvailableTrainingCorp();
	}
	
	@Override
	public void update(RobTrainingConfig entity) {
		super.update(entity);
		SelectComtracTraining.resetAvailableSeat(entity);
		SelectCorporateTraining.resetAvailableSeat(entity);
	}
	
	@Override
	public RobTrainingConfig findByTrainingCode(String trainingCode) {

		SearchCriteria sc = new SearchCriteria("trainingCode", SearchCriteria.EQUAL, trainingCode);
		List<RobTrainingConfig> trainingCodeExist = findByCriteria(sc).getList();
		if (trainingCodeExist.size() > 0) {
			return trainingCodeExist.get(0);
		}

		return null;
	}

	@Override
	public Integer totalPax(String type, String year, String month) {

		return robTrainingConfigDao.totalPax(type, year, month);
	}
}
