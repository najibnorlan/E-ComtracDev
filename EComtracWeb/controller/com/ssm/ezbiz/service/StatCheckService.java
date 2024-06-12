package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.webis.param.BusinessInfo;

public interface StatCheckService extends BaseService<RobRenewal, String> {
	public RobRenewal findByIdWithData(String id)throws SSMException;
	
	public BusinessInfo findBusinessByRegNoWS(String regNo, String chkDigit) throws SSMException;

	List<BusinessInfo> findListRobRenewalByIcWS(String icNo) throws SSMException;

	void lodgeRobFormA1(RobRenewal robRenewal) throws SSMException;
}
