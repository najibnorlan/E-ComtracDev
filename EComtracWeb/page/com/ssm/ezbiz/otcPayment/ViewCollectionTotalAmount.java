package com.ssm.ezbiz.otcPayment;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class ViewCollectionTotalAmount extends BaseFrame{
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	public ViewCollectionTotalAmount(Integer counterSessionId){
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		Integer countTransactions = llpPaymentReceiptService
				.getCountTransactionByCounterSession(robCounterSession
						.getSessionId(), null);
		final Double sumTransactions = llpPaymentReceiptService
				.getTotalTransactionByCounterSession(robCounterSession
						.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		
		Integer countCreditNote = llpPaymentReceiptService
				.getCountTransactionByCounterSession(robCounterSession
						.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
		
		add(new SSMLabel("countCreditNote", countCreditNote));
		add(new SSMLabel("countTransactions", countTransactions));
		add(new SSMLabel("sumTransactions", sumTransactions));
	}
	
	@Override
	public String getPageTitle() {
		return null;
	}
}

	
