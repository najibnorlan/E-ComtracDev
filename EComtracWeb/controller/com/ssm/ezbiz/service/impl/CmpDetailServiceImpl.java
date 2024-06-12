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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.CmpDetailDao;
import com.ssm.ezbiz.service.CmpDetailService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpDetail;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.webis.client.CompoundClient;
import com.ssm.webis.param.CompoundDetailListReq;
import com.ssm.webis.param.CompoundDetailListResp;
import com.ssm.webis.param.CompoundListReq;
import com.ssm.webis.param.CompoundListResp;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.WSManagementService;

@Service
public class CmpDetailServiceImpl extends BaseServiceImpl<CmpDetail,  Long> implements CmpDetailService{
	@Autowired 
	CmpDetailDao cmpDetailDao;
	
	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;


	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;
	
	@Override
	public BaseDao getDefaultDao() {
		return cmpDetailDao;
	}

	@Override
	public List<CmpDetail> findCmpDetailListWS(String cmpNo, String type) throws SSMException {
		String url = wSManagementService.getWsUrl("CompoundClient.findCompoundDetailList");
		
		CompoundDetailListReq param = new CompoundDetailListReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setCmpNo(cmpNo);
		param.setType(type);

		List<CmpDetail> listCmpDetail = null;
		try {

			CompoundDetailListResp ssmWsResp = CompoundClient.findCompoundDetailList(url, param);
			if(!"00".equals(ssmWsResp.getSuccessCode())){
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
			com.ssm.webis.param.CmpDetail[] arrayCmpDetail = ssmWsResp.getListCompound();
			
			if(arrayCmpDetail != null){
				listCmpDetail = new ArrayList<CmpDetail>();
				for (int i = 0; i < arrayCmpDetail.length; i++) {
					com.ssm.webis.param.CmpDetail cmpDetailWs = arrayCmpDetail[i];
					CmpDetail cmpDetail = new CmpDetail();
					cmpDetail.setCmpSectionAmt(cmpDetailWs.getCmpAmt());
					cmpDetail.setCmpSectionCode(cmpDetailWs.getSectionCode());
					cmpDetail.setCmpSectionOffence(cmpDetailWs.getOffence());
					
					listCmpDetail.add(cmpDetail);
				}
			}

		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}

		return listCmpDetail;
	}

}
