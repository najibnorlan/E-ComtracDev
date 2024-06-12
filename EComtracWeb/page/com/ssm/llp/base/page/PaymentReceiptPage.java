package com.ssm.llp.base.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;

public class PaymentReceiptPage extends SecBasePage {
//	public PaymentReceiptPage() {
//		add(new ViewPaymentReceiptPanel2("viewPaymentReceipt"));
//	}
	
	public PaymentReceiptPage(PageParameters param) {
//		String transId = param.get("transId").toString();
//		boolean isSSTFormat = false;
//		if(StringUtils.isNotBlank(transId)) {
//			LlpPaymentReceipt llpPaymentReceipt = ((LlpPaymentReceiptService) getService(LlpPaymentReceiptService.class.getSimpleName())).find(transId);
//			String dateSSTFormat = getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_DATE_SST_RECEIPT_FORMAT);
//			
//			if(StringUtils.isNotBlank(dateSSTFormat)) {
//				try {
//					Date sstDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateSSTFormat);
//					if(!llpPaymentReceipt.getCreateDt().before(sstDate)) {
//						isSSTFormat = true;
//					}
//				} catch (ParseException e) {
//				}
//			}
//			
//		}
//		if(isSSTFormat) {
//			add(new ViewPaymentReceiptSSTPanel("viewPaymentReceipt",param));
//		}else {
//			add(new ViewPaymentReceiptPanel2("viewPaymentReceipt",param));
//		}
		
		add(getRecieptPanel(param));
		
	}
	
}
