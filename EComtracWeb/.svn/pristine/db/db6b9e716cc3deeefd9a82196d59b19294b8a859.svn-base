/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.CmpMasterDao;
import com.ssm.ezbiz.service.CmpMasterService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.webis.client.CompoundClient;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.webis.param.CompoundListReq;
import com.ssm.webis.param.CompoundListResp;

@Service
public class CmpMasterServiceImpl extends BaseServiceImpl<CmpMaster,  Long> implements CmpMasterService{
	@Autowired 
	CmpMasterDao cmpMasterDao;
	
	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;

	@Override
	public BaseDao getDefaultDao() {
		return cmpMasterDao;
	}

	@Override
	public List<CmpMaster> findCmpListWS(String icNo, String entityType, String entityNo) throws SSMException {
		String url = wSManagementService.getWsUrl("CompoundClient.findCompoundList");
		CompoundListReq param = new CompoundListReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setICNo(icNo);
		param.setEntityType(entityType);
		param.setEntityNo(entityNo);

		List<CmpMaster> listCmpMaster = null;
		try {

			CompoundListResp ssmWsResp = CompoundClient.findCompoundList(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			com.ssm.webis.param.CmpMaster[] arrayCmpMaster = ssmWsResp.getListCompound();
			
			if(arrayCmpMaster != null){
				listCmpMaster = new ArrayList<CmpMaster>();
				for (int i = 0; i < arrayCmpMaster.length; i++) {
					com.ssm.webis.param.CmpMaster cmpMasterWs = arrayCmpMaster[i];
					CmpMaster cmpMaster = new CmpMaster();
					cmpMaster.setCmpAmt(cmpMasterWs.getTotalAmt());
					cmpMaster.setCmpMasterKeyCode(cmpMasterWs.getCmpMasterKeyCode());
					cmpMaster.setCmpNo(cmpMasterWs.getCmpNo());
					cmpMaster.setCmpStatus(cmpMasterWs.getCmpStatus());
					cmpMaster.setEntityNo(cmpMasterWs.getEntityNo());
					cmpMaster.setEntityNoCheckDigit("");
					cmpMaster.setEntityType(cmpMasterWs.getEntityType());
					cmpMaster.setCmpType(cmpMasterWs.getCmpType());
					cmpMaster.setEntityName(cmpMasterWs.getEntityName());
					cmpMaster.setEntity_name_reg(cmpMasterWs.getRegName());
					cmpMaster.setEntityNoCheckDigit(cmpMasterWs.getRegNo());
					listCmpMaster.add(cmpMaster);
				}
			}

		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}

		return listCmpMaster;
	}

	@Override
	public List<CmpMaster> findByTransCode(String transCode)
			throws SSMException {
		return cmpMasterDao.findByTransCode(transCode);
	}

}
