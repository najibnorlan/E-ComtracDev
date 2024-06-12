package com.ssm.llp.mod1.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class LlpPaymentFeePage extends PaymentFeeBasePage {

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public LlpPaymentFeePage() {
//		StringValue searchString = params.get("searchString");// .toString();
//		String strParam = "";
//		if (searchString != null && searchString.toString() != null) {
//			strParam = searchString.toString();
//		}


	
//	SearchCriteria sc = new SearchCriteria("paymentCode", "LIKE", strParam + "%");
		SearchCriteria sc = new SearchCriteria();
	SSMSortableDataProvider dp = new SSMSortableDataProvider("paymentCode", sc, LlpPaymentFeeService.class);
	
	final SSMDataView<LlpPaymentFee> dataView = new SSMDataView<LlpPaymentFee>("sorting", dp) {

		protected void populateItem(final Item<LlpPaymentFee> item) {
			LlpPaymentFee llpPaymentFee = item.getModelObject();
		
			String desc = ((LlpParametersService)getService(LlpParametersService.class.getSimpleName())).findByCodeTypeValue(Parameter.PAYMENT_TYPE, llpPaymentFee.getPaymentCode());
//			String desc = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_TYPE,llpPaymentFee.getPaymentCode());
								
			// item.add(new ActionPanel("actions", item.getModel()));
			item.add(new SSMLabel("paymentCode", String.valueOf(llpPaymentFee.getPaymentCode())));
			//item.add(new SSMLabel("paymentDesc", llpPaymentFee.getPaymentCode(), Parameter.PAYMENT_TYPE));
			item.add(new SSMLabel("paymentDesc", desc));
			item.add(new SSMLabel("paymentFee", String.valueOf(llpPaymentFee.getPaymentFee())));
			item.add(new SSMLabel("gstCode", String.valueOf(llpPaymentFee.getGstCode())));
			item.add(new SSMLabel("status", llpPaymentFee.getStatus()));
		
			item.add(new Link("edit", item.getDefaultModel()) {
				public void onClick() {					
					LlpPaymentFee lp = (LlpPaymentFee) getModelObject();
					//getService(LlpPaymentFeeService.class.getSimpleName()).update(lp);// bru tambah
					setResponsePage(new AddPaymentCode(lp.getPaymentCode()));
				}
			});
			item.add(new Link("delete", item.getDefaultModel()) {
				public void onClick() {
					LlpPaymentFee lp = (LlpPaymentFee) getModelObject();
					llpPaymentFeeService.deletePaymentFee(lp);
				}
			});

			item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return (item.getIndex() % 2 == 1) ? "even" : "odd";
				}
			}));
			
		}
		
	};
	dataView.setItemsPerPage(50L);
	dataView.setOutputMarkupId(true);

	
	add(dataView);
	add(new SSMPagingNavigator("navigator", dataView));
	add(new NavigatorLabel("navigatorLabel", dataView));
}
}