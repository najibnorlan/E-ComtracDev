package com.ssm.llp.mod1.page;

import com.ssm.llp.base.page.BasePage;

public class PaymentFeeBasePage extends BasePage {
	public PaymentFeeBasePage() {
		// add(new StyleSheetReference("stylesheet", ContactBasePage.class,
		// "styles.css"));
		add(new AddPaymentFeePanel("addPaymentFeePanel"));
		// add(new SSMLabel("label2", "This is in the subclass Page2"));
	}

	@Override
	public String getPageTitle() {
		return "page.title.payment.configuration";
	}
}

