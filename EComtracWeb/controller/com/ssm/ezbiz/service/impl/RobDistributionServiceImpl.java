package com.ssm.ezbiz.service.impl;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobDistributionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessDistributionReq;
import com.ssm.webis.param.BusinessDistributionResp;

@Service
public class RobDistributionServiceImpl extends RobSchedulerServiceImpl implements RobDistributionService {
	

	@Autowired
	RobFormAService robFormAService;
	
	
	public static void main(String[] args) throws Exception{
		String url = "http://ezbizwebisss.ssm.com.my/EZBIZ/services";
		BusinessDistributionReq param = new BusinessDistributionReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
		param.setReqRefNo("FASC" + System.currentTimeMillis());
		int poolHQ = 30;
		param.setHqPool(poolHQ);
		
		String todayName = new SimpleDateFormat("EEEE").format(new Date()).toUpperCase();
		String distibutionConfigToday = "MA:100,NS:100,CA:100,IP:100,PG:100,KT:100,AS:100,TR:100";
		
		param.setStateDistConfig(distibutionConfigToday);
		BusinessDistributionResp resp = RobClient.distributeEzBizApplication(url, param);
		if (resp != null) {
			if ("00".equals(resp.getSuccessCode())) {
				String listUpdatedTrans[] = resp.getListDistributionResult();
				if(listUpdatedTrans!=null){
					for (int i = 0; i < listUpdatedTrans.length; i++) {
						String br = listUpdatedTrans[i].split(":")[0];
						String paramRefNo = listUpdatedTrans[i].split(":")[1];
						System.out.println(br);
						System.out.println(paramRefNo);
						System.out.println();
					}
//					
//					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("d:/returnnew.obj"));
//					oos.writeObject(listUpdatedTrans);
//					oos.close();
				}
			} else {
				System.out.println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		}
	}
	
	@Override
	public void runDistribution() {
		
		String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			
			String url = wSManagementService.getWsUrl("RobClient.distributeEzBizApplication");
			BusinessDistributionReq param = new BusinessDistributionReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("FASC" + System.currentTimeMillis());
			int poolHQ = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_HQ_POOL));
			param.setHqPool(poolHQ);
			
			String todayName = new SimpleDateFormat("EEEE").format(new Date()).toUpperCase();
			String distibutionConfigToday = llpParametersService.findByCodeTypeValue(Parameter.DISTRIBUTION_CONFIG, todayName);
			
			if(StringUtils.isNotBlank(distibutionConfigToday)){
				param.setStateDistConfig(distibutionConfigToday);
				BusinessDistributionResp resp = RobClient.distributeEzBizApplication(url, param);
				if (resp != null) {
					if ("00".equals(resp.getSuccessCode())) {
						String listUpdatedTrans[] = resp.getListDistributionResult();
						if(listUpdatedTrans!=null){
							robFormAService.updateBaseListDistribution(listUpdatedTrans);
						}
					} else {
						System.out.println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
					}
				}
			}else{
				System.out.println("No distribution formula for :"+todayName);
			}
			healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			
		} catch (Exception e) {
			println(e.getMessage());
			healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		}
		
		updateHealthCheckStatus(healthCheckStatus);
	}

}
