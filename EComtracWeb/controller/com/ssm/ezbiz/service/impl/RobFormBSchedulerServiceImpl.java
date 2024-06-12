package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobFormBSchedulerService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessFormBStatusReq;
import com.ssm.webis.param.BusinessFormBStatusResp;

@Service
public class RobFormBSchedulerServiceImpl extends RobSchedulerServiceImpl implements RobFormBSchedulerService {

	@Autowired
	RobFormBService robFormBService;
	
	public void runFormBAllStatusCheck() {
		try {
			checkRobFormBStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processBStatusPendingPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processBStatusPaymentSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void checkRobFormBStatus() {// TO get status and update
										// all in process
										// transaction
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_B_STATUS_IN_PROCESS);
				List<RobFormB> listFormB = robFormBService.findByCriteria(sc).getList();

				for (int i = 0; i < listFormB.size(); i++) {
					RobFormB robFormB = listFormB.get(i);
					println(this.getClass() + ":checkRobFormBStatus:" + robFormB.getRobFormBCode());
					try {
						BusinessFormBStatusResp resp = getRobFormBStatusWs(robFormB.getRobFormBCode());
						if (!"00".equals(resp.getSuccessCode())) {
							if (resp.getErrorMsg().indexOf("Cannot find form") != -1) {
								robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_CANCEL_INVALID);
								robFormB.setApproveRejectDt(new Date());
								robFormB.setApproveRejectNotes("Cannot get Form In CBS");
								robFormBService.update(robFormB);
							}
						} else {
							updateRobFormStatus(robFormB, resp);
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

	public void processBStatusPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobFormB> listRobFormB = robFormBService.findByCriteria(sc).getList();
				for (int i = 0; i < listRobFormB.size(); i++) {
					RobFormB robFormB = listRobFormB.get(i);
					String appRefNo = robFormB.getRobFormBCode();
					println("processBStatusPendingPayment:" + appRefNo);
					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						robFormB.setPaymentDt(paymentSuccessTrans.getCreateDt());
						robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS);
						robFormBService.update(robFormB);
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								if (Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT.equals(robFormB.getStatus())) {
									robFormB.setPaymentDt(paymentTransaction.getCreateDt());
									robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS);
									robFormBService.update(robFormB);
								}
							}
						} catch (Exception e1) {
							println("processBStatusPendingPayment:" + appRefNo + "" + e1.getMessage());
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

	public void processBStatusPaymentSuccess() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS);
				List<RobFormB> listRobFormB = robFormBService.findByCriteria(sc).getList();

				for (int i = 0; i < listRobFormB.size(); i++) {
					RobFormB robFormB = listRobFormB.get(i);
					try {
						// println("processBStatusPaymentSuccess:" +
						// robFormB.getRobFormBCode());
						BusinessFormBStatusResp resp = getRobFormBStatusWs(robFormB.getRobFormBCode());
						if (resp != null) {
							if (StringUtils.isNotBlank(resp.getStatus())) {
								updateRobFormStatus(robFormB, resp);
							} else {
								robFormB = robFormBService.findAllByIdWithWS(robFormB.getRobFormBCode());
								robFormBService.lodgeFormBWs(robFormB);
							}
						}

					} catch (Exception e) {
						println(robFormB.getRobFormBCode() + ":" + e.getMessage());
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

	private BusinessFormBStatusResp getRobFormBStatusWs(String formBRefNo) {
		try {
			String url = wSManagementService.getWsUrl("RobClient.getStatusFormB");

			BusinessFormBStatusReq param = new BusinessFormBStatusReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("FBSC" + System.currentTimeMillis());
			param.setFormBRefNo(formBRefNo);

			BusinessFormBStatusResp resp = RobClient.getStatusFormB(url, param);

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
