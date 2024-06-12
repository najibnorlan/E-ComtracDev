package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.ws.RobBusinessSummaryInfo;

public interface RobRenewalService extends BaseService<RobRenewal, String> {
	public RobRenewal findByIdWithData(String id)throws SSMException;
	
	public BusinessInfo findBusinessByRegNoWS(String regNo, String chkDigit) throws SSMException;
	
	public BusinessInfo findBusinessByRegNoWS(String regNo) throws SSMException;

	List<BusinessInfo> findListRobRenewalByIcWS(String icNo) throws SSMException;

	void lodgeRobFormA1(RobRenewal robRenewal) throws SSMException;

	public RobRenewal getCertDataFromWS(RobRenewal robRenewal) throws SSMException;

	public List<RobBusinessSummaryInfo> findAllBizDetailByIcWS(String idNo)throws Exception;

}
