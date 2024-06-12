package com.ssm.llp.page.supplyinfo;


import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;


@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class BuyLlpSupplyInfoBasePage extends SecBasePage {
	
	
	public BuyLlpSupplyInfoBasePage() { //empty constructor for payment link
		getSession().removeAttribute("llpSupplyInfoDetList_");
		LlpSupplyInfoItemPanel llpSupplyInfoItemPanel = new LlpSupplyInfoItemPanel("LlpSupplyInfoItemPanel");
		add(new LlpSupplyInfoLlpSearchPanel("LlpSupplyInfoLlpSearchPanel",llpSupplyInfoItemPanel));
		
		add(llpSupplyInfoItemPanel);
	}
}
