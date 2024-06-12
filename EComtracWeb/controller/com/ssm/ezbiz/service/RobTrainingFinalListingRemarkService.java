package com.ssm.ezbiz.service;

import java.util.List;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListing;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListingRemark;

public interface RobTrainingFinalListingRemarkService extends BaseService<RobTrainingFinalListingRemark, Integer> {

	public List findByFinalListingRefNo(String finalListingRefNo);

	public RobTrainingFinalListingRemark updateInsertAll(RobTrainingFinalListingRemark robTrainingFinalListingRemark);
	
	public  RobTrainingFinalListingRemark findExistingFinalListingRefNo(String finalListingRefNo);

}
