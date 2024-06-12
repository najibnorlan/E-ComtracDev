package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormAOwnerDao;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.webis.client.SSMInfoClient;
import com.ssm.webis.param.*;

@Service
public class RobFormAOwnerServiceImpl extends BaseServiceImpl<RobFormAOwner, Long> implements RobFormAOwnerService {

	@Autowired
	RobFormAOwnerDao robFormAOwnerDao;
	
	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormAOwnerDao;
	}
	@Override
	public void deleteNotInId(String robFormACode, long[] idToDelete) {
		robFormAOwnerDao.deleteNotInId(robFormACode, idToDelete);
	}
	@Override
	public List<RobFormAOwner> findByRobFormACode(String formACode) {
		return robFormAOwnerDao.findByRobFormACode(formACode);
	}
	@Override
	public RobFormAOwner findByRobFormACodeAndIcNo(String robFormACode, String icNo) {
		return robFormAOwnerDao.findByRobFormACodeAndIcNo(robFormACode, icNo);
	}
	@Override
	public String getVerificationStatus(String robFormACode, String icNo){
		return robFormAOwnerDao.getVerificationStatus(robFormACode, icNo);
	}
	@Override
	public List<RobFormAOwner> findOwnerNotInId(String robFormACode, long[] id) {
		// TODO Auto-generated method stub
		return robFormAOwnerDao.findOwnerNotInId(robFormACode, id);
	}
	
	@Override
	public List<RobFormAOwner> findByIcNoInStatus(String icNo, String[] status){
		
		String s = "";
		Integer index = 0;
		
		for(String i : status){
			if(index != 0){
				s += ",";
			}
			
			s += i;
		}
		 return robFormAOwnerDao.findByIcNoInStatus(icNo, s);
	}
	
	@Override
	public void checkIfAlreadyUseIncentive(List<RobFormAOwner> listRobFormAOwner, String incentive, String currentFormACode)throws SSMException{
		
		
		
		
		String incentiveUseBy = "";
		for (int i = 0; i < listRobFormAOwner.size(); i++) {
			String icNo = listRobFormAOwner.get(i).getNewicno();
			List<RobFormAOwner> result = new ArrayList<RobFormAOwner>(); 
			if(incentive.equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_student)){
				//if student, check incentive usage in rob_form_a
				result = robFormAOwnerDao.checkIfAlreadyUseIncentiveFormA(icNo,  incentive, currentFormACode);	
			}else if(incentive.equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)){
				//if oku, check incentive usage in rob_form_a and rob_form_a_renewal
				result = robFormAOwnerDao.checkIfAlreadyUseIncentiveFormA(icNo,  incentive, currentFormACode);
				
				if(result.size() == 0){
					result = robFormAOwnerDao.checkIfAlreadyUseIncentiveFormA1(icNo,  incentive, currentFormACode);
				}
				
			}
			if(result.size()>0){
				for (int j = 0; j < result.size(); j++) {
					incentiveUseBy+=result.get(j).getName()+" for application "+result.get(j).getRobFormACode()+"\n";
				}
			}
		}
		if(StringUtils.isNotBlank(incentiveUseBy)){
			throw new SSMException(incentiveUseBy);
		}
		
	}
	@Override
	public BlacklistInfoResp getBlacklistInfoByICNoWS(String idNo, String idType, String entityType) throws SSMException {
		String url = wSManagementService.getWsUrl("SSMInfoClient.getBlacklistInfoByICNo");
		BlacklistInfoReq param = new BlacklistInfoReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setEntityType(entityType);
		param.setIdType(idType);
		param.setIdNo(idNo);
		
		try {
			BlacklistInfoResp resp = SSMInfoClient.getBlacklistInfoByICNo(url,param);
			if(Parameter.WEBIS_SUCCESS_CODE.equals(resp.getSuccessCode())) {
				return resp;
			}else {
				System.err.println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
				throw new SSMException(resp.getSuccessCode(), resp.getErrorMsg());
			}	
		} catch (Exception e) {
			throw new SSMException(e);
		}
	}
	
}

