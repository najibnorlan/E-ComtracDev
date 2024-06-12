package com.ssm.ezbiz.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.RobPaymentSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

@Service
public class RobPaymentSchedulerServiceImpl extends RobSchedulerServiceImpl implements RobPaymentSchedulerService{
	
	
	public void generatePaymentReceiptNumber() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				String[] vals = { Parameter.COMTRAC_PAYMENT_MODE_CC, Parameter.COMTRAC_PAYMENT_MODE_DD };

				SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.PAYMENT_STATUS_SUCCESS).andIfInNotNull(
						"paymentMode", vals, false);
				sc.addOrderBy("createDt", SearchCriteria.ASC);

				List<LlpPaymentTransaction> paymentList = llpPaymentTransactionService.findByCriteria(sc).getList();

				for (int i = 0; i < paymentList.size(); i++) {

					LlpPaymentTransaction transaction = paymentList.get(i);
					String transactionId = transaction.getTransactionId();

					LlpPaymentReceipt checkTransId = llpPaymentReceiptService.getReceiptNoByTransactionID(transactionId);

					if (checkTransId == null) {
						println("transaction id = " + transactionId);

						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:m:s a");
						LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
						String receiptNo = llpRunningNoService.generateRunningNo(Parameter.RECEIPT_RUNNING_NO, Parameter.RECEIPT_FIELDCODE, null,
								"yyyyMMdd", "000000", transaction.getCreateDt());
						llpPaymentReceipt.setReceiptNo(receiptNo);
						llpPaymentReceipt.setCashReceived(transaction.getAmount());
						llpPaymentReceipt.setTotalAmount(transaction.getAmount());
						llpPaymentReceipt.setBalance(Double.valueOf(0));
						llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
						llpPaymentReceipt.setTransactionId(transaction.getTransactionId());
						llpPaymentReceipt.setRemarks("Receipt generate manually on " + dateFormat.format(new Date()) + " by "
								+ UserEnvironmentHelper.getLoginName());
						llpPaymentReceipt.setCreateDt(transaction.getCreateDt());
						llpPaymentReceipt.setUpdateDt(transaction.getCreateDt());

						if (llpPaymentTransactionDetailService.isHaveSrProduct(transaction.getTransactionId()) == true) {
							String taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO, Parameter.INVOICE_FIELDCODE,
									null, null, "000000000", new Date());
							llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
						}
						llpPaymentReceiptService.insert(llpPaymentReceipt);
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
	
	public void cancelAllOTCPaymentNitely() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				llpPaymentTransactionService.cancelAllPreviosDayOTCPayment();
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
