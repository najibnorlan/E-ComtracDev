package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobFormCSchedulerService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessFormCStatusReq;
import com.ssm.webis.param.BusinessFormCStatusResp;

@Service
public class RobFormCSchedulerServiceImpl extends RobSchedulerServiceImpl implements RobFormCSchedulerService {

	@Autowired
	RobFormCService robFormCService;
	
	public void runFormCAllStatusCheck() {
		try {
			checkRobFormCStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processCStatusPendingPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processCStatusPaymentSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processCStatusWithoutPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkRobFormCStatus() {// TO get status and update all in
										// process transaction
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_C_STATUS_IN_PROCESS);
				List<RobFormC> listFormC = robFormCService.findByCriteria(sc).getList();

				for (int i = 0; i < listFormC.size(); i++) {
					RobFormC robFormC = listFormC.get(i);
					try {
						BusinessFormCStatusResp resp = getRobFormCStatusWs(robFormC.getRobFormCCode());

						if (!"00".equals(resp.getSuccessCode())) {
							if (resp.getErrorMsg().indexOf("Cannot find form") != -1) {
								robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_CANCEL_INVALID);
								robFormC.setApproveRejectDt(new Date());
								robFormC.setApproveRejectNotes("Cannot get Form In CBS");
								robFormCService.update(robFormC);
							}
						} else {
							
								updateRobFormStatus(robFormC, resp);
							
						}

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


	public void processCStatusPaymentSuccess() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS);
				List<RobFormC> listRobFormC = robFormCService.findByCriteria(sc).getList();

				for (int i = 0; i < listRobFormC.size(); i++) {
					RobFormC robFormC = listRobFormC.get(i);
					if (robFormC.getRobFormCCode().indexOf("006") != -1) {
						println("Sasa");
					}
					try {
						BusinessFormCStatusResp resp = getRobFormCStatusWs(robFormC.getRobFormCCode());
						if (resp != null) {
							if (StringUtils.isNotBlank(resp.getStatus())) {
								updateRobFormStatus(robFormC, resp);
							} else {
								robFormC = robFormCService.findAllById(robFormC.getRobFormCCode());
								robFormCService.lodgeFormCWs(robFormC);
							}
						}

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

	public void processCStatusWithoutPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT);
				List<RobFormC> listRobFormC = robFormCService.findByCriteria(sc).getList();

				for (int i = 0; i < listRobFormC.size(); i++) {
					RobFormC robFormC = listRobFormC.get(i);
					try {
						BusinessFormCStatusResp resp = getRobFormCStatusWs(robFormC.getRobFormCCode());
						if (resp != null) {
							if (StringUtils.isNotBlank(resp.getStatus())) {
								updateRobFormStatus(robFormC, resp);
							} else {
								robFormC = robFormCService.findAllById(robFormC.getRobFormCCode());
								robFormCService.lodgeFormCWs(robFormC);
							}
						}

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

	public void processCStatusPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobFormC> listRobFormC = robFormCService.findByCriteria(sc).getList();
				for (int i = 0; i < listRobFormC.size(); i++) {
					RobFormC robFormC = listRobFormC.get(i);
					String appRefNo = robFormC.getRobFormCCode();
					// println("processAStatusPendingPayment:"+appRefNo);
					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						robFormC.setPaymentDt(paymentSuccessTrans.getCreateDt());
						robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS);
						robFormCService.update(robFormC);
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								if (Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT.equals(robFormC.getStatus())) {
									robFormC.setPaymentDt(paymentTransaction.getCreateDt());
									robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS);
									robFormCService.update(robFormC);
								}
							}
						} catch (Exception e) {
							println(e.getMessage());
						}
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


	private BusinessFormCStatusResp getRobFormCStatusWs(String formCRefNo) {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getStatusFormC");

			BusinessFormCStatusReq param = new BusinessFormCStatusReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("FCSC" + System.currentTimeMillis());
			param.setFormCRefNo(formCRefNo);

			BusinessFormCStatusResp resp = RobClient.getStatusFormC(url, param);

			if (resp != null) {
				if ("00".equals(resp.getSuccessCode())) {
					if (StringUtils.isNotBlank(resp.getStatus())) {
						resp.setStatus(resp.getStatus().trim());
					}
				} else {
					println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
				}
				return resp;
			}
		} catch (Exception e) {
			println(e.getMessage());
		}
		return null;
	}
}
