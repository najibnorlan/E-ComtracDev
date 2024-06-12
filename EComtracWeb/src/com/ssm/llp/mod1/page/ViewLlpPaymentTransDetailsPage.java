package com.ssm.llp.mod1.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class ViewLlpPaymentTransDetailsPage extends BaseFrame {

	public ViewLlpPaymentTransDetailsPage(final Page parentPage, final ModalWindow window, String searchStr) {
//		System.out.println("searchStr >> " + searchStr);

		SearchCriteria sc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, searchStr);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("transactionId", sc, LlpPaymentTransactionDetailService.class);

		final SSMDataView<LlpPaymentTransactionDetail> dataView = new SSMDataView<LlpPaymentTransactionDetail>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<LlpPaymentTransactionDetail> item) {
				final LlpPaymentTransactionDetail llpPaymentTransactionDet = item.getModelObject();
				item.add(new SSMLabel("paymentItem", llpPaymentTransactionDet.getPaymentItem(), Parameter.PAYMENT_TYPE));
				item.add(new SSMLabel("quantity", llpPaymentTransactionDet.getQuantity()));
				item.add(new SSMLabel("amount", llpPaymentTransactionDet.getAmount()));
				item.add(new SSMLabel("detail", llpPaymentTransactionDet.getPaymentDet()));

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
		add(new OrderByBorder("orderByItem", "paymentItem", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}
