package com.ssm.ezbiz.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobFormA1SchedulerService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.webis.client.RobClient;
import com.ssm.webis.param.BusinessFormA1StatusReq;
import com.ssm.webis.param.BusinessFormA1StatusResp;

@Service
public class RobFormA1SchedulerServiceImpl extends RobSchedulerServiceImpl implements RobFormA1SchedulerService {

	@Autowired
	RobRenewalService robRenewalService;
	
	public void runFormA1AllStatusCheck() {
		try {
			processA1StatusPendingPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processA1StatusPaymentSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void processA1StatusPaymentSuccess() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			Locale malayLoc = new Locale("ms", "MY");
			DateFormat endDtFormat = new SimpleDateFormat("dd MMMM yyyy", malayLoc);

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
				List<RobRenewal> listRobRenewal = robRenewalService.findByCriteria(sc).getList();

				for (int i = 0; i < listRobRenewal.size(); i++) {
					RobRenewal robRenewal = listRobRenewal.get(i);
					try {
						BusinessFormA1StatusResp resp = getRobFormA1StatusWs(robRenewal.getTransCode());
						if (resp != null) {
							if (Parameter.CBS_ROB_FORM_A1_STATUS_APPROVED.equals(resp.getStatus())) {
								robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_SUCCESS);
								robRenewal.setCmpNo(resp.getCompoundNo());
								robRenewal.setNewExpDate(endDtFormat.parse(resp.getNewEndDate()));
								robRenewal.setApproveRejectDt(new Date());
								robRenewalService.update(robRenewal);

								try {
									robRenewal = robRenewalService.getCertDataFromWS(robRenewal);
								} catch (Exception e) {
									println("Error:" + robRenewal.getTransCode());
									println(e.getMessage());
								}

							}
							if (Parameter.CBS_ROB_FORM_A1_STATUS_REJECT.equals(resp.getStatus())) {
								robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_FAIL);
								robRenewal.setApproveRejectDt(new Date());
								robRenewalService.update(robRenewal);
							}
						} else {
							robRenewalService.lodgeRobFormA1(robRenewal);
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
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processA1StatusPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobRenewal> listRobRenewal = robRenewalService.findByCriteria(sc).getList();
				for (int i = 0; i < listRobRenewal.size(); i++) {
					RobRenewal robRenewal = listRobRenewal.get(i);
					String appRefNo = robRenewal.getTransCode();
					println("processA1StatusPendingPayment:" + robRenewal.getTransCode());
					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
						robRenewal.setPaymentDt(paymentSuccessTrans.getCreateDt());
						robRenewalService.update(robRenewal);
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								if (Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT.equals(robRenewal.getStatus())) {
									robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS);
									robRenewal.setPaymentDt(paymentTransaction.getCreateDt());
									robRenewalService.update(robRenewal);
								}
							}
							if (Parameter.PAYMENT_STATUS_FAIL.equals(paymentTransaction.getStatus())) {
								if(!DateUtils.isSameDay(robRenewal.getCreateDt(), paymentTransaction.getUpdateDt())) {
									robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_CANCEL);
									robRenewalService.update(robRenewal);
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
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void cancelA1FormExceed1DayDE() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_DATA_ENTRY);

				List<RobRenewal> listRobRenewal = robRenewalService.findByCriteria(sc).getList();
				for (int i = 0; i < listRobRenewal.size(); i++) {
					RobRenewal robRenewal = listRobRenewal.get(i);

					Calendar calAdd = Calendar.getInstance();
					calAdd.setTime(robRenewal.getCreateDt());
					calAdd.add(Calendar.DATE, 1);

					Calendar calNow = Calendar.getInstance();
					calNow.setTime(new Date());

					if (calNow.after(calAdd)) {
						robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_CANCEL);
						robRenewalService.update(robRenewal);
					}

				}
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			} catch (Exception ex) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());

			}
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void cancelA1PendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT);
				List<RobRenewal> robRenewals = robRenewalService.findByCriteria(sc).getList();

				String[] status = { Parameter.PAYMENT_STATUS_PENDING, Parameter.PAYMENT_STATUS_SUCCESS };
				Integer index = 0;
				for (RobRenewal renewal : robRenewals) {

					SearchCriteria sc2 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, renewal.getTransCode());
					sc2 = sc2.andIfInNotNull("status", status, false);
					sc2.addOrderBy("createDt", SearchCriteria.DESC);

					List<LlpPaymentTransaction> paymentTransactions = llpPaymentTransactionService.findByCriteria(sc2).getList();

					if (paymentTransactions.size() == 0) {

						SearchCriteria sc3 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, renewal.getTransCode());
						sc3.addOrderBy("createDt", SearchCriteria.DESC);

						LlpPaymentTransaction payment = (LlpPaymentTransaction) llpPaymentTransactionService.findByCriteria(sc3).getList().get(0);
						println("renewal : " + renewal.getBizName() + " | ref  " + renewal.getTransCode() + " | transaction : " + payment.getStatus());
						Calendar calTrans = Calendar.getInstance();
						calTrans.setTime(payment.getCreateDt());
						calTrans.add(Calendar.MINUTE, 30);

						Calendar calNow = Calendar.getInstance();
						calNow.setTime(new Date());

						if (calTrans.before(calNow)) {
							renewal.setStatus(Parameter.ROB_RENEWAL_STATUS_CANCEL);
							robRenewalService.update(renewal);
						}
					} else {

					}
				}
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;

			} catch (Exception ex) {

				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());

			}
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private BusinessFormA1StatusResp getRobFormA1StatusWs(String formA1RefNo) {
		try {

			String url = wSManagementService.getWsUrl("RobClient.getStatusFormA1");

			BusinessFormA1StatusReq param = new BusinessFormA1StatusReq();
			param.setAgencyId(Parameter.ROB_AGENCY_ID);
			param.setAgencyBranchCode(Parameter.ROB_AGENCY_BRANCH_CODE);
			param.setReqRefNo("FA1SC" + System.currentTimeMillis());
			param.setFormA1RefNo(formA1RefNo);

			BusinessFormA1StatusResp resp = RobClient.getStatusFormA1(url, param);

			if ("00".equals(resp.getSuccessCode())) {
				if(StringUtils.isNotBlank(resp.getStatus())){
					resp.setStatus(resp.getStatus().trim());
				}
				return resp;
			} else {
				println(resp.getSuccessCode() + ":" + resp.getErrorMsg());
			}
		} catch (Exception e) {
			println(e.getMessage());
		}
		return null;
	}

}
