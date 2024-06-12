package com.ssm.ezbiz.service.impl;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobEmailSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpEmailLog;
import com.ssm.llp.base.common.service.LlpEmailLogService;

@Service
public class RobEmailSchedulerServiceImpl extends RobSchedulerServiceImpl implements RobEmailSchedulerService{
	

	@Autowired
	LlpEmailLogService llpEmailLogService;
	
	
	public void sendingEmailStatusPending() {
		try {
			String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_deployment_type);
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.EMAIL_STATUS_pending);
				List<LlpEmailLog> listEmail = llpEmailLogService.findByCriteria(sc).getList();

				for (LlpEmailLog email : listEmail) {
					try {
						boolean sendEmail = true;
						if(!Parameter.DEPLOYMENT_TYPE_production.equals(depType)){
							sendEmail = false;
							if(email.getEmailTo().indexOf("@ssm.com.my")!=-1){//only send if ssmm email
								sendEmail = true;
							}else {
								System.err.println("Not Send Email for Non Production:"+email.getEmailTo());
							}
						}
						if(sendEmail){
							mailService.resendEmail(email);
						}
						email.setStatus(Parameter.EMAIL_STATUS_success);
						llpEmailLogService.update(email);
					} catch (Exception e) {
						boolean retrySending = false;
						try {
							if(e instanceof MailException) {
								MailException mEx = (MailException) e;
								if(mEx.contains(UnknownHostException.class)) {
									retrySending = true;
								}
							}
						} catch (Exception e2) {
						}
						if(!retrySending) {
							email.setStatus(Parameter.EMAIL_STATUS_fail);
							email.setRemark(e.getMessage());
							llpEmailLogService.update(email);
						}
					}

				}
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			} catch (Exception ex) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			}

			updateHealthCheckStatus( healthCheckStatus);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
