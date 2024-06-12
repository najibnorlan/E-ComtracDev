/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobUserActivationTraySchedulerService;
import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

@Service
public class RobUserActivationTraySchedulerServiceImpl extends RobSchedulerServiceImpl implements RobUserActivationTraySchedulerService{
	
	@Autowired 
	RobUserActivationTrayService robUserActivationTrayService;

	@Override
	public void unlockProcessTray() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, -30);

				String status[] = new String[]{Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS,Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION};
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.IN, status, false);
				SearchCriteria sc2 = new SearchCriteria("lockBy", SearchCriteria.IS_NOT_NULL);
				sc = new SearchCriteria(sc,SearchCriteria.AND,sc2);
				
				sc = sc.andIfNotNull("lockDt", SearchCriteria.LESS, cal.getTime());
				
				List<RobUserActivationTray> listUser = robUserActivationTrayService.findByCriteria(sc).getList();

				for (int i = 0; i < listUser.size(); i++) {
					RobUserActivationTray activationTray = listUser.get(i);
					try {
						println("unlockProcessTray:" + activationTray.getAppRefNo() );
						robUserActivationTrayService.unlock(activationTray);
					} catch (Exception e) {
						println(e.getMessage());
					}
				}
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;

			} catch (Exception ex) {

				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());

			}
			updateHealthCheckStatus(healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
