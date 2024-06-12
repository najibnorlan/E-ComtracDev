package com.ssm.supplyinfo;

import java.util.Calendar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;


@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ListOrderPage extends SecBasePage{

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	public ListOrderPage() {

		SearchCriteria sc = new SearchCriteria("ownerBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		sc.andIfNotNull("updateDt", SearchCriteria.GREATER_EQUAL, cal.getTime());
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, SupplyInfoTransHdrService.class);
		
		final SSMDataView<SupplyInfoTransHdr> dataView = new SSMDataView<SupplyInfoTransHdr>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<SupplyInfoTransHdr> item) {
				final SupplyInfoTransHdr supplyInfoTransHdr = item.getModelObject();
				
				item.add(new SSMLabel("bil", item.getIndex()+1));
				item.add(new SSMLabel("transCode", supplyInfoTransHdr.getTransCode()));
				item.add(new SSMLabel("updateDt", supplyInfoTransHdr.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("status", supplyInfoTransHdr.getStatus(), Parameter.SUPPLY_INFO_TRANS_STATUS));
				
				
				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						setResponsePage(new DetailOrderPage(supplyInfoTransHdr.getTransCode()));
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

		dataView.setItemsPerPage(20L);

		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	@Override
	public String getPageTitle() {
		return "com.ssm.supplyInfo.listOrder";
	}
}
