package com.ssm.ezbiz.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.ComtracSchedulerService;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingDashboardService;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.ezbiz.service.RobTrainingReprintcertService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class ComtracSchedulerServiceImpl extends RobSchedulerServiceImpl implements ComtracSchedulerService {

	@Autowired
	RobTrainingTransactionService robTrainingTransactionService;

	@Autowired
	RobTrainingConfigService robTrainingConfigService;

	@Autowired
	RobTrainingParticipantService robTrainingParticipantService;
	
	@Autowired
	RobTrainingReprintcertService robTrainingReprintcertService;
	
	@Autowired
	RobTrainingDashboardService robTrainingDashboardService;
	
	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	
	public void cancelComtracDataEntry() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.COMTRAC_TRANSACTION_STATUS_data_entry);
			List<RobTrainingTransaction> trainingTransactions = robTrainingTransactionService.findByCriteria(sc).getList();
			if (trainingTransactions.size() == 0) {
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			}
			for (RobTrainingTransaction transaction : trainingTransactions) {
				try {

					RobTrainingConfig robTrainingConfig = transaction.getTrainingId();
					Calendar calTrans = Calendar.getInstance();
					calTrans.setTime(transaction.getUpdateDt());
					calTrans.add(Calendar.MINUTE, 15);

					Calendar calNow = Calendar.getInstance();
					calNow.setTime(new Date());

					println("transaction : " + transaction.getTransactionCode() + " | now : " + new Date() + " | update + 15min : "
							+ calTrans.getTime());
					if (calTrans.before(calNow)) {
						transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
						robTrainingTransactionService.update(transaction);

						robTrainingConfig.setCurrentPax(robTrainingConfig.getCurrentPax() - transaction.getTotalPax());
						robTrainingConfigService.update(robTrainingConfig);
					}
					healthCheckStatus = Parameter.HEALTH_CHECK_ok;
				} catch (Exception ex) {
					println(ex.getMessage());
					healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				}
			}

			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void cancelComtracPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment);
				List<RobTrainingTransaction> trainingTransactions = robTrainingTransactionService.findByCriteria(sc).getList();

				if (trainingTransactions.size() == 0) {
					healthCheckStatus = Parameter.HEALTH_CHECK_ok;
				}

				String[] status = { Parameter.PAYMENT_STATUS_PENDING, Parameter.PAYMENT_STATUS_SUCCESS };
				for (RobTrainingTransaction transaction : trainingTransactions) {
					try {
						RobTrainingConfig robTrainingConfig = transaction.getTrainingId();
						SearchCriteria sc2 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, transaction.getTransactionCode());
						sc2 = sc2.andIfInNotNull("status", status, false);
						sc2.addOrderBy("createDt", SearchCriteria.DESC);

						List<LlpPaymentTransaction> paymentTransactions = llpPaymentTransactionService.findByCriteria(sc2).getList();

						if (paymentTransactions.size() == 0) {
							SearchCriteria sc3 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, transaction.getTransactionCode());
							sc3.addOrderBy("createDt", SearchCriteria.DESC);

							List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService.findByCriteria(sc3).getList();
							if (listPayment.size() > 0) {
								LlpPaymentTransaction payment = listPayment.get(0);

								Calendar calTrans = Calendar.getInstance();
								calTrans.setTime(payment.getCreateDt());
								calTrans.add(Calendar.MINUTE, 30);

								Calendar calNow = Calendar.getInstance();
								calNow.setTime(new Date());

								if (calTrans.before(calNow)) {
									transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
									robTrainingTransactionService.update(transaction);

									robTrainingConfig.setCurrentPax(robTrainingConfig.getCurrentPax() - transaction.getTotalPax());
									robTrainingConfigService.update(robTrainingConfig);
								}
							} else {
								Calendar calTrans = Calendar.getInstance();
								calTrans.setTime(transaction.getUpdateDt());
								calTrans.add(Calendar.MINUTE, 30);

								Calendar calNow = Calendar.getInstance();
								calNow.setTime(new Date());
								if (calTrans.before(calNow)) {
									transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
									robTrainingTransactionService.update(transaction);

									robTrainingConfig.setCurrentPax(robTrainingConfig.getCurrentPax() - transaction.getTotalPax());
									robTrainingConfigService.update(robTrainingConfig);
								}
							}
						}
						healthCheckStatus = Parameter.HEALTH_CHECK_ok;
					} catch (Exception e) {
						healthCheckStatus = Parameter.HEALTH_CHECK_fail;
					}
				}

			} catch (Exception ex) {

				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());
			}

			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void processComtracPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobTrainingTransaction> transactionList = robTrainingTransactionService.findByCriteria(sc).getList();
				for (int i = 0; i < transactionList.size(); i++) {
					RobTrainingTransaction transaction = transactionList.get(i);
					String appRefNo = transaction.getTransactionCode();

					println("processComtracPendingPayment:" + appRefNo);

					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
						robTrainingTransactionService.update(transaction);

						robTrainingTransactionService.sendEmailConfirmation(transaction.getTransactionCode());
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								if (Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment.equals(transaction.getStatus())) {
									transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
									robTrainingTransactionService.update(transaction);

									robTrainingTransactionService.sendEmailConfirmation(transaction.getTransactionCode());
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
	
	public void processComtracPrintCertificatePendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				
				String lodgerName = UserEnvironmentHelper.getLoginName();
				
				
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<RobTrainingReprintcert> transactionList = robTrainingReprintcertService.findByCriteria(sc).getList();
				
				for (int i = 0; i < transactionList.size(); i++) {
					RobTrainingReprintcert transaction = transactionList.get(i);
					String appRefNo = transaction.getCertRefNo();
					String cpIcNo = transaction.getTransactionCode()+"/"+transaction.getIcNo();
					Calendar calTrans = Calendar.getInstance();
					calTrans.setTime(transaction.getUpdateDt());
					calTrans.add(Calendar.MINUTE, 15);
					Calendar calNow = Calendar.getInstance();

					println("processComtracPrintCertificatePendingPayment:" + appRefNo);

					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (calTrans.before(calNow)) {
					if (paymentSuccessTrans != null) {
						transaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
						robTrainingReprintcertService.update(transaction);
						
						LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(transaction.getLodgerName());
						mailService.sendMail(llpUserProfile.getEmail(), "email.notification.ecomtrac.reprintCertOrders.subject", appRefNo, "email.notification.ecomtrac.reprintCertOrders.body", transaction.getTrainingName(),transaction.getTrainingCode(),cpIcNo,appRefNo);
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
	
	@Override
	public void generateCertificate() {
		
		String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		try {
		
			if (!checkIsRunScheduler()) {
				return;
			}
			

			
			List<RobTrainingParticipant> listParticipant = robTrainingParticipantService.findAllEligible(); 
			
			for (RobTrainingParticipant participant : listParticipant) {
				try {

					robTrainingParticipantService.generateCertificate(participant);
					
					
					
				} catch (Exception ex) {
					println(ex.getMessage());
					
				}
			}

			
			healthCheckStatus = Parameter.HEALTH_CHECK_ok;
		} catch (Exception e) {
			e.printStackTrace();
			healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		}

		updateHealthCheckStatus( healthCheckStatus);
	}
	
public void generateReprintCertificate() {
		
		String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		try {
		
			if (!checkIsRunScheduler()) {
				return;
			}
			

			
			List<RobTrainingReprintcert> listRobTrainingReprintcert = robTrainingReprintcertService.findAllPendingGenerateCert(); 
			
			for (RobTrainingReprintcert reprintcert : listRobTrainingReprintcert) {
				Calendar calTrans = Calendar.getInstance();
				calTrans.setTime(reprintcert.getUpdateDt());
				calTrans.add(Calendar.MINUTE, 15);
				Calendar calNow = Calendar.getInstance();
				try {
					
					if (calTrans.before(calNow)) {
					robTrainingReprintcertService.generateCertificate(reprintcert);
					}
					
					
					
				} catch (Exception ex) {
					println(ex.getMessage());
					
				}
			}

			
			healthCheckStatus = Parameter.HEALTH_CHECK_ok;
		} catch (Exception e) {
			e.printStackTrace();
			healthCheckStatus = Parameter.HEALTH_CHECK_fail;
		}

		updateHealthCheckStatus( healthCheckStatus);
	}

	public void cancelReprintCertificateDataEntry() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
	
			SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.COMTRAC_TRANSACTION_STATUS_data_entry);
			List<RobTrainingReprintcert> robTrainingReprintcert = robTrainingReprintcertService.findByCriteria(sc).getList();
			if (robTrainingReprintcert.size() == 0) {
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			}
			for (RobTrainingReprintcert reprintcert : robTrainingReprintcert) {
				try {
	
					Calendar calTrans = Calendar.getInstance();
					calTrans.setTime(reprintcert.getUpdateDt());
					calTrans.add(Calendar.MINUTE, 15);
	
					Calendar calNow = Calendar.getInstance();
					calNow.setTime(new Date());
	
					println("reprintcert : " + reprintcert.getCertRefNo()+ new Date() + " | update + 15min : "
							+ calTrans.getTime());
					if (calTrans.before(calNow)) {
						reprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
						robTrainingReprintcertService.update(reprintcert);
					}
					healthCheckStatus = Parameter.HEALTH_CHECK_ok;
				} catch (Exception ex) {
					println(ex.getMessage());
					healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				}
			}
	
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public void cancelReprintCertificateDataEntryPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment);
				List<RobTrainingReprintcert> robTrainingReprintcert = robTrainingReprintcertService.findByCriteria(sc).getList();
	
				if (robTrainingReprintcert.size() == 0) {
					healthCheckStatus = Parameter.HEALTH_CHECK_ok;
				}
	
				String[] status = { Parameter.PAYMENT_STATUS_PENDING, Parameter.PAYMENT_STATUS_SUCCESS };
				for (RobTrainingReprintcert reprintcert : robTrainingReprintcert) {
					try {
						SearchCriteria sc2 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, reprintcert.getCertRefNo());
						sc2 = sc2.andIfInNotNull("status", status, false);
						sc2.addOrderBy("createDt", SearchCriteria.DESC);
	
						List<LlpPaymentTransaction> paymentTransactions = llpPaymentTransactionService.findByCriteria(sc2).getList();
	
						if (paymentTransactions.size() == 0) {
							SearchCriteria sc3 = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, reprintcert.getCertRefNo());
							sc3.addOrderBy("createDt", SearchCriteria.DESC);
	
							List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService.findByCriteria(sc3).getList();
							if (listPayment.size() > 0) {
								LlpPaymentTransaction payment = listPayment.get(0);
	
								Calendar calTrans = Calendar.getInstance();
								calTrans.setTime(payment.getCreateDt());
								calTrans.add(Calendar.MINUTE, 30);
	
								Calendar calNow = Calendar.getInstance();
								calNow.setTime(new Date());
	
								if (calTrans.before(calNow)) {
									reprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
									robTrainingReprintcertService.update(reprintcert);
	
								}
							} else {
								Calendar calTrans = Calendar.getInstance();
								calTrans.setTime(reprintcert.getUpdateDt());
								calTrans.add(Calendar.MINUTE, 30);
	
								Calendar calNow = Calendar.getInstance();
								calNow.setTime(new Date());
								if (calTrans.before(calNow)) {
									reprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cancel);
									robTrainingReprintcertService.update(reprintcert);

								}
							}
						}
						healthCheckStatus = Parameter.HEALTH_CHECK_ok;
					} catch (Exception e) {
						healthCheckStatus = Parameter.HEALTH_CHECK_fail;
					}
				}
	
			} catch (Exception ex) {
	
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(ex.getMessage());
			}
	
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
public void insertUpdateDashboardData() {
		
	String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
	try {
	
		if (!checkIsRunScheduler()) {
			return;
		}
		
		robTrainingDashboardService.updateData();
		
		healthCheckStatus = Parameter.HEALTH_CHECK_ok;
	} catch (Exception e) {
		e.printStackTrace();
		healthCheckStatus = Parameter.HEALTH_CHECK_fail;
	}

	updateHealthCheckStatus( healthCheckStatus);

}
}
