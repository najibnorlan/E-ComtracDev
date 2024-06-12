package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobFormASchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessFormAStatusReq;
import com.ssm.webis.param.BusinessFormAStatusResp;

@Service
public class RobFormASchedulerServiceImpl extends RobSchedulerServiceImpl implements RobFormASchedulerService {


	public void runFormAAllStatusCheck() {
		try {
			checkRobFormAStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processAStatusPendingPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processAStatusPaymentSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void checkRobFormANotGenerateFormA() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc1 = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_APPROVED);
				SearchCriteria sc2 = new SearchCriteria("formAData", SearchCriteria.IS_NULL);
				
				SearchCriteria sc = new SearchCriteria(sc1, SearchCriteria.AND, sc2);
				
				List<RobFormA> listFormA = robFormAService.findByCriteria(sc).getList();
				for (int i = 0; i < listFormA.size(); i++) {
					try {
						robFormAService.generateBorangAForm(listFormA.get(i));
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
	
	public void checkRobFormAStatus() {// TO get status and update
										// all in process
										// transaction
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_IN_PROCESS);
				List<RobFormA> listFormA = robFormAService.findByCriteria(sc).getList();

				for (int i = 0; i < listFormA.size(); i++) {
					RobFormA robFormA = listFormA.get(i);
					println(this.getClass() + ":checkRobFormAStatus:" + robFormA.getRobFormACode());
					try {
						BusinessFormAStatusResp resp = getRobFormAStatusWs(robFormA.getRobFormACode());
						if (!"00".equals(resp.getSuccessCode())) {
							if (resp.getErrorMsg().indexOf("Cannot find form") != -1) {
								robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_CANCEL_INVALID);
								robFormA.setApproveRejectDt(new Date());
								robFormA.setApproveRejectNotes("Cannot get Form In CBS");
								robFormAService.update(robFormA);
							}
						}else{
							updateRobFormStatus(robFormA, resp);
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

	public void processAStatusPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobFormA> listRobFormA = robFormAService.findByCriteria(sc).getList();
				for (int i = 0; i < listRobFormA.size(); i++) {
					RobFormA robFormA = listRobFormA.get(i);
					String appRefNo = robFormA.getRobFormACode();
					println("processAStatusPendingPayment:" + appRefNo);
					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
						robFormA.setPaymentDt(paymentSuccessTrans.getCreateDt());
						robFormAService.update(robFormA);
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								if (Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT.equals(robFormA.getStatus())) {
									robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
									robFormA.setPaymentDt(paymentTransaction.getCreateDt());
									robFormAService.update(robFormA);
								}
							}
						} catch (Exception e1) {
							println(e1.getMessage());
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

	public void processAStatusPaymentSuccess() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS);
				List<RobFormA> listRobFormA = robFormAService.findByCriteria(sc).getList();

				for (int i = 0; i < listRobFormA.size(); i++) {
					RobFormA robFormA = listRobFormA.get(i);
					try {
						println("processAStatusPaymentSuccess:" + robFormA.getRobFormACode());
						BusinessFormAStatusResp resp = getRobFormAStatusWs(robFormA.getRobFormACode());
						if (resp != null) {
							if (StringUtils.isNotBlank(resp.getStatus())) {
								updateRobFormStatus(robFormA, resp);
							} else {
								robFormA = robFormAService.findAllById(robFormA.getRobFormACode());
								robFormAService.lodgeFormAWs(robFormA);
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

	/*public void cancelAPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				String cancelDraffDaysStr = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_CANCEL_A_DRAFT_DAYS);
				Integer cancelDraffDays = 5;
				if(StringUtils.isBlank(cancelDraffDaysStr)){
					cancelDraffDays = Integer.parseInt(cancelDraffDaysStr);
				}
				
				robFormAService.cancelPendingPaymentMoreThanXDays(cancelDraffDays);
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			} catch (Exception ex) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());

			}
			updateHealthCheckStatus(healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public void cancelADraff() {//Draft
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				String cancelDraffDaysStr = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_CANCEL_A_DRAFT_DAYS);
				Integer cancelDraffDays = 5;
				if(StringUtils.isBlank(cancelDraffDaysStr)){
					cancelDraffDays = Integer.parseInt(cancelDraffDaysStr);
				}
				
				robFormAService.cancelDraftMoreThanXDays(cancelDraffDays);
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


	//
	public static void main(String[] args) throws Exception {
		RobFormASchedulerServiceImpl impl = new RobFormASchedulerServiceImpl();
		 BusinessFormAStatusResp s = impl.getRobFormAStatusWs("EB-A2019010500162");
		 System.out.println(">>"+s.getStatusNotes()+"<<");
		 System.out.println(">>"+s.getStatus()+"<<");
		 System.out.println(">>"+s.getProcessBy()+"<<");
		 System.out.println(">>"+s.getProcessBranch()+"<<");
	}

	private BusinessFormAStatusResp getRobFormAStatusWs(String formARefNo) {
		try {
//			String url = "http://webissec.ssm.com.my/EZBIZ/services";
//			String url = "http://ezbizwebis.ssm.com.my/EZBIZ/services";		
			String url = wSManagementService.getWsUrl("RobClient.getStatusFormA");
			BusinessFormAStatusReq param = new BusinessFormAStatusReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("FASC" + System.currentTimeMillis());
			param.setFormARefNo(formARefNo);

			BusinessFormAStatusResp resp = RobClient.getStatusFormA(url, param);

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
