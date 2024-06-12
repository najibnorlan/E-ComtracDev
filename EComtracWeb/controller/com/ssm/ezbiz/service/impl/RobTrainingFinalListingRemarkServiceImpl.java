package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobTrainingFinalListingRemarkDao;
import com.ssm.ezbiz.service.RobTrainingFinalListingRemarkService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListing;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListingRemark;

@Service
public class RobTrainingFinalListingRemarkServiceImpl extends BaseServiceImpl<RobTrainingFinalListingRemark, Integer>
		implements RobTrainingFinalListingRemarkService {

	@Autowired
	RobTrainingFinalListingRemarkDao robTrainingFinalListingRemarkDao;

	@Override
	public BaseDao getDefaultDao() {
		return robTrainingFinalListingRemarkDao;
	}

	@Override
	public List findByFinalListingRefNo(String finalListingRefNo) {
		return robTrainingFinalListingRemarkDao.findByFinalListingRefNo(finalListingRefNo);
	}

	@Override
	public RobTrainingFinalListingRemark updateInsertAll(RobTrainingFinalListingRemark robTrainingFinalListingRemark) {

		if (StringUtils.isBlank(robTrainingFinalListingRemark.getFinalListingRefNo())) {
			insert(robTrainingFinalListingRemark);
		} else {
			update(robTrainingFinalListingRemark);
		}
		return robTrainingFinalListingRemark;

	}
	
	@Override
	public RobTrainingFinalListingRemark findExistingFinalListingRefNo(String finalListingRefNo) {

		SearchCriteria sc = new SearchCriteria("finalListingRefNo", SearchCriteria.EQUAL, finalListingRefNo);
		List<RobTrainingFinalListingRemark> finalListingRemarkExist = findByCriteria(sc).getList();
		if (finalListingRemarkExist.size() > 0) {
			return finalListingRemarkExist.get(0);
		}

		return null;
	}
}
