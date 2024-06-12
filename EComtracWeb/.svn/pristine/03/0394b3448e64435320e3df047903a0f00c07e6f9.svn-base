package com.ssm.ezbiz.otcPayment;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.ViewPaymentReceiptPanel2;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({ "all" })
public class ViewTransactionSummary extends SecBasePage {

	@SpringBean(name = "LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;

	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService;

	public ViewTransactionSummary() {
		super();
	}

	public ViewTransactionSummary(Integer counterSessionId, Boolean isViewReceipt) {
		final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		Integer countTransactions = llpPaymentReceiptService.getCountTransactionByCounterSession(robCounterSession.getSessionId(), null);
		Integer countCreditNote = llpPaymentReceiptService.getCountTransactionByCounterSession(robCounterSession.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
		add(new SSMLabel("countCreditNote", countCreditNote));
		add(new SSMLabel("countTransactions", countTransactions));
		add(new SSMLabel("userId", robCounterSession.getUserId()));
		add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress()
				.getCounterName()));
		add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
		add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
		add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));
		add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(),
				"dd-MM-yyyy hh:mm:ss a"));

		Link balancingButton = new Link("balancingButton") {
			@Override
			public void onClick() {
				
				robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_new);
				robCounterSessionService.update(robCounterSession);
				
				setResponsePage(new CollectionBalancingPage(robCounterSession.getSessionId()));
			}
			
		};
		balancingButton.setOutputMarkupId(true);
		add(balancingButton);
		
		Link back = new Link("back") {
			@Override
			public void onClick() {
				
				setResponsePage(new ListOtcPaymentPage(robCounterSession.getSessionId()));
			}
			
		};
		back.setOutputMarkupId(true);
		add(back);
		
		if(isViewReceipt){
			add(new SSMLabel("checkoutDate", "-"));
			balancingButton.setVisible(false);
			back.setVisible(true);
		}else{
			add(new SSMLabel("checkoutDate", robCounterSession.getCheckoutDate(),
					"dd-MM-yyyy hh:mm:ss a"));
			balancingButton.setVisible(true);
			back.setVisible(false);
		}
		
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId",
				SearchCriteria.EQUAL, robCounterSession.getSessionId());
		populateDetail(sc);
	}

	public void populateDetail(SearchCriteria sc) {
		final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		WebMarkupContainer wmcSummary = new WebMarkupContainer("wmcSummary");
		wmcSummary.setOutputMarkupId(true);
		wmcSummary.setVisible(true);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt",
				SortOrder.ASCENDING, sc, LlpPaymentReceiptService.class);
		final SSMDataView<LlpPaymentReceipt> dataView = new SSMDataView<LlpPaymentReceipt>(
				"sorting", dp) {

			public void populateItem(final Item<LlpPaymentReceipt> item) {
				final LlpPaymentReceipt transaction = item.getModelObject();

				LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService
						.findById(transaction.getTransactionId());

				item.add(new SSMLabel("receiptNo", transaction.getReceiptNo()));
				item.add(new SSMLabel("transId", llpPaymentTransaction
						.getTransactionId()));
				item.add(new SSMLabel("appRefNo", llpPaymentTransaction
						.getAppRefNo()));
				item.add(new SSMLabel("payerName", llpPaymentTransaction
						.getPayerName()));
				item.add(new SSMLabel("isCancel", transaction.getIsCancel()
						.toString(), Parameter.PAYMENT_RECEIPT_ISCANCEL));
				item.add(new SSMLabel("bil", item.getIndex() + 1));

				final ModalWindow viewReceiptPopUp = new ModalWindow("viewReceiptDiv");
				item.add(viewReceiptPopUp);
				
				// Transaction detail modal window
				viewReceiptPopUp.setCookieName("viewReceiptCookies"+llpPaymentTransaction.getTransactionId());
				viewReceiptPopUp.setResizable(true);

				PageParameters param = new PageParameters();
				param.set("transId", llpPaymentTransaction.getTransactionId());
				viewReceiptPopUp.setContent(getRecieptPanel(viewReceiptPopUp.getContentId(),param));

				viewReceiptPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
					@Override
					public boolean onCloseButtonClicked(AjaxRequestTarget target) {
						return true;
					}
				});
			
				item.add(new AjaxLink<Void>("viewReceipt") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						viewReceiptPopUp.show(target);
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

		wmcSummary.add(dataView);
		wmcSummary.add(new SSMPagingNavigator("navigator", dataView));
		wmcSummary.add(new NavigatorLabel("navigatorLabel", dataView));
		add(wmcSummary);

	}

	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.transactionSummary";
		return titleKey;
	}
}
