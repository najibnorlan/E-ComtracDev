package com.ssm.ezbiz.payment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class ViewPaymentTransactionPanel extends SecBasePanel{
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	
	public ViewPaymentTransactionPanel(String id, String appRefNo) {
		super(id);
		
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findDetailByLatestByAppRefNo(appRefNo);
		if(llpPaymentTransaction!=null){
			add(new ListView<LlpPaymentTransactionDetail>("paymentItems", llpPaymentTransaction.getLlpPaymentTransactionDetails()) {
				public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
					final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
					
					item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));
					SSMLabel itemDesc = new SSMLabel("itemDet", paymentItem.getPaymentDet());
					if(StringUtils.isBlank(paymentItem.getPaymentDet())){
						itemDesc.setVisible(false);
					}
					item.add(itemDesc);
					item.add(new SSMLabel("gstCode", paymentItem.getGstCode()));
					item.add(new SSMLabel("index", item.getIndex()+1));
					item.add(new SSMLabel("itemAmountWoutGst", paymentItem.getAmountWithOutGst()));
					
				}
			});
			add(new SSMLabel("totalWithoutGst", llpPaymentTransaction.getAmount() - llpPaymentTransaction.getGstAmt()));
			add(new SSMLabel("totalGst", llpPaymentTransaction.getGstAmt()));
			add(new SSMLabel("totalAmount", llpPaymentTransaction.getAmount()));
		}else{
			SSMLabel dummy = new SSMLabel("paymentItems");
			add(dummy);
			dummy.setVisible(false);
		}
	}
	
	
}
