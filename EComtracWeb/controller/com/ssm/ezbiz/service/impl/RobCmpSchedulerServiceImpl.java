package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.CmpMasterService;
import com.ssm.ezbiz.service.CmpTransactionService;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobCmpSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpInfo;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.webis.param.CompoundStatusResp;

@Service
public class RobCmpSchedulerServiceImpl  extends RobSchedulerServiceImpl implements RobCmpSchedulerService{
	
	@Autowired
	CmpMasterService cmpMasterService;

	@Autowired
	CmpTransactionService cmpTransactionService;

	public void runFormCmpAllStatusCheck() {
		try {
			processCmpStatusPaymentSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			processCmpStatusPendingPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processCmpStatusPaymentSuccess() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.CMP_PAYMENT_PAYMENT_SUCCESS);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<CmpTransaction> listCmpTrans = cmpTransactionService.findByCriteria(sc).getList();
				for (int i = 0; i < listCmpTrans.size(); i++) {
					CmpTransaction cmpTransaction = listCmpTrans.get(i);
					String appRefNo = cmpTransaction.getCmpTransactionCode();

					println("processCmpStatusPaymentSuccess:" + appRefNo);

					try {

						CompoundStatusResp resp = cmpTransactionService.getCmpStatusWS(cmpTransaction);
						if (resp != null) {
							if (Parameter.YES_NO_yes.equals(resp.getIsAllStatusPaid())) {
								cmpTransaction.setStatus(Parameter.CMP_PAYMENT_CMP_CBS_PAID);
								cmpTransactionService.update(cmpTransaction);
							} else {
								cmpTransactionService.lodgeCompoundPaymentWS(cmpTransaction);
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
			updateHealthCheckStatus( healthCheckStatus);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processCmpStatusPendingPayment() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.CMP_PAYMENT_PENDING_PAYMENT);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<CmpTransaction> listCmpTrans = cmpTransactionService.findByCriteria(sc).getList();
				for (int i = 0; i < listCmpTrans.size(); i++) {
					CmpTransaction cmpTransaction = listCmpTrans.get(i);
					String appRefNo = cmpTransaction.getCmpTransactionCode();

					println("processCmpStatusPendingPayment:" + appRefNo);
					LlpPaymentTransaction paymentSuccessTrans = llpPaymentTransactionService.findSuccessByAppRefNo(appRefNo);
					if (paymentSuccessTrans != null) {
						PaymentInterface paymentInterface = (PaymentInterface) cmpTransactionService;
						try {
							// List<CmpMaster> cmpMasterList =
							// cmpMasterService.findByTransCode(cmpTransaction.getCmpTransactionCode());
							// CmpInfo cmpInfo = new CmpInfo();
							// cmpInfo.setCmpTransaction(cmpTransaction);
							// cmpInfo.setListCmpMaster(cmpMasterList);
							//
							// paymentInterface.sucessPayment(cmpInfo,
							// paymentSuccessTrans.getTransactionId());

							cmpTransaction.setPaymentDt(paymentSuccessTrans.getCreateDt());
							cmpTransaction.setStatus(Parameter.CMP_PAYMENT_PAYMENT_SUCCESS);
							cmpTransactionService.update(cmpTransaction);

							try {
								cmpTransactionService.lodgeCompoundPaymentWS(cmpTransaction);
							} catch (Exception e) {
								// println(e.getMessage());
							}
						} catch (Exception e) {
							println(e.getMessage());
						}
						continue;
					}

					List<LlpPaymentTransaction> listPaymentTransactions = llpPaymentTransactionService.findPendingByAppRefNo(appRefNo);
					for (int j = 0; j < listPaymentTransactions.size(); j++) {
						LlpPaymentTransaction paymentTransaction = listPaymentTransactions.get(j);
						try {
							paymentTransaction = paymentService.checkAndUpdatePaymentStatus(paymentTransaction);
							if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTransaction.getStatus())) {
								PaymentInterface paymentInterface = (PaymentInterface) cmpTransactionService;
								try {
									List<CmpMaster> cmpMasterList = cmpMasterService.findByTransCode(cmpTransaction.getCmpTransactionCode());
									CmpInfo cmpInfo = new CmpInfo();
									cmpInfo.setCmpTransaction(cmpTransaction);
									cmpInfo.setListCmpMaster(cmpMasterList);
									paymentInterface.sucessPayment(cmpInfo, paymentTransaction.getTransactionId());

								} catch (SSMException e) {
								}
								break;
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
			updateHealthCheckStatus( healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
