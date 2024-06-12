package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ViewOtcPaymentPage.PaymentReceivedModel;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SecondLevelLoginPage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "all" })
public class ListOtcPaymentPage extends SecBasePage {

	@SpringBean(name = "RobCounterSessionService")
	private RobCounterSessionService robCounterSessionService;

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;

	@SpringBean(name = "LlpPaymentReceiptService")
	private LlpPaymentReceiptService llpPaymentReceiptService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	public ListOtcPaymentPage() {
		super();
	}

	public void init() {
		add(new ListOtcPaymentForm("form",
				(IModel<ListOtcPaymentModel>) getDefaultModel()));
	}

	public ListOtcPaymentPage(Integer sessionId) {

		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				sessionId);

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						ListOtcPaymentModel listOtcPaymentModel = new ListOtcPaymentModel();
						listOtcPaymentModel
								.setRobCounterSession(robCounterSession);
						return listOtcPaymentModel;
					}
				}));

		init();
	}

	private class ListOtcPaymentForm extends Form implements Serializable {
		public ListOtcPaymentForm(String id, IModel<ListOtcPaymentModel> m) {
			super(id, m);
			ListOtcPaymentModel listOtcPaymentModel = m.getObject();
			RobCounterSession sessionObj = (RobCounterSession) SignInSession
					.get().getAttribute("_currentCheckinSession");

			if (sessionObj == null) {
				setResponsePage(UserCheckinPage.class);
			}
			
			Date curDate = new Date();
			Date start = null;
			Date end = null;
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				start = pars.parse(form.format(curDate) + " 00:00:00");
				end = pars.parse(form.format(curDate) + " 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String userId = sessionObj.getUserId();
			String ipAddress = sessionObj.getCounterIpAddress().getIpAddress();
			final RobCounterSession robCounterSession = listOtcPaymentModel
					.getRobCounterSession();

			DecimalFormat df = new DecimalFormat("#,###,##0.00");
			
			final Integer countTransactions = llpPaymentReceiptService
					.getCountTransactionByCounterSession(robCounterSession
							.getSessionId(), null);

			Integer countCreditNote = llpPaymentReceiptService
					.getCountTransactionByCounterSession(robCounterSession
							.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
			
			Integer countActivity = countTransactions + countCreditNote;
			
			SSMAjaxFormSubmitBehavior onchangeTransactionId = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					ListOtcPaymentModel otcPaymentModel = (ListOtcPaymentModel) getForm().getDefaultModelObject();
					if(otcPaymentModel.getTransactionId() != null && otcPaymentModel.getTransactionId().length() == 14){
						SearchCriteria sc = generateScTemplate(otcPaymentModel);
						populateTable(sc, target);
					}
				}
				
				public String getAjaxIndicatorMarkupId() {
					return null;
				}
			};
			
			SSMTextField transactionId = new SSMTextField("transactionId");
			transactionId.setLabelKey("page.lbl.ezbiz.otcPaymentPage.transactionId");
			transactionId.add(onchangeTransactionId);
			add(transactionId);

			SSMTextField appRefNo = new SSMTextField("appRefNo");
			appRefNo.setLabelKey("page.lbl.ezbiz.otcPaymentPage.appRefNo");
			add(appRefNo);

			SSMTextField icNo = new SSMTextField("icNo");
			icNo.setLabelKey("page.lbl.ezbiz.otcPaymentPage.icNo");
			add(icNo);
			
			SSMDateTextField date = new SSMDateTextField("date");
			date.setLabelKey("page.lbl.ezbiz.otcPaymentPage.date");
			add(date);
			
			add(new SSMLabel("countActivity", countActivity));
			add(new SSMLabel("countCreditNote", countCreditNote));
			add(new SSMLabel("countTransactions", countTransactions));
			add(new SSMLabel("userId", robCounterSession.getUserId()));
			add(new SSMLabel("counterName", robCounterSession
					.getCounterIpAddress().getCounterName()));
			add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
			add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));
			add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
			add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(),
					"dd-MM-yyyy hh:mm:ss a"));
			
			LlpParameters param = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG, Parameter.SEARCH_BY_DATE_PAYMENT);
			if(Parameter.YES_NO_no.equals(param.getCodeDesc())){
				date.setVisible(false);
			}

			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListOtcPaymentModel searchModelForm = (ListOtcPaymentModel) form
							.getDefaultModelObject();
					
					SearchCriteria sc = null;
					
					if(searchModelForm.getTransactionId() == null && searchModelForm.getAppRefNo() == null 
							&& searchModelForm.getIcNo() == null && searchModelForm.getDate() == null){
						ssmError(SSMExceptionParam.NEED_AT_LEAST_ONE_INPUT);
						
					}else{
						sc = generateScTemplate(searchModelForm);
					}
					
					SSMSortableDataProvider dp = populateTable(sc, target);
					long size = dp.size();
					 
					FeedbackPanel feedbackPanel =  ((ListOtcPaymentPage)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
			};
			add(search);
			populateTable(null, null);

			SSMAjaxLink viewReceipt = new SSMAjaxLink("viewReceipt") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					setResponsePage(new ViewTransactionSummary(robCounterSession.getSessionId(), true));
				}
			};
			add(viewReceipt);
			
			SSMAjaxLink checkout = new SSMAjaxLink("checkout") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					if(countTransactions == 0){
						RobCounterSession session = robCounterSessionService.findById(robCounterSession.getSessionId());
						robCounterSessionService.delete(session);
						SignInSession.get().removeAttribute("_currentCheckinSession");
						
						setResponsePage(UserCheckinPage.class);
					}else{
						robCounterSession.setIsOpen(Parameter.COLLECTION_COUNTER_IS_OPEN_no);
						robCounterSession.setCheckoutDate(new java.util.Date());
						robCounterSession.setCheckoutBy(UserEnvironmentHelper.getLoginName());
						robCounterSessionService.update(robCounterSession);

						SignInSession.get().removeAttribute("_currentCheckinSession");
						setResponsePage(new ViewTransactionSummary(robCounterSession.getSessionId(), false));
					}
				}

			};
			add(checkout);
			
			SSMAjaxLink creditNote = new SSMAjaxLink("creditNote") {
				@Override
				public void onClick(AjaxRequestTarget target){
					setResponsePage(new ListOtcReceipt(robCounterSession.getSessionId()));
				}
			};
			add(creditNote);
		}
	}
	
	public SearchCriteria generateScTemplate(ListOtcPaymentModel searchModelForm){
		
		SearchCriteria sc = null;
		
		if (StringUtils.isNotBlank(searchModelForm
				.getTransactionId())) {
			sc = new SearchCriteria("transactionId",
					SearchCriteria.EQUAL,
					searchModelForm.getTransactionId());
		}
		if (StringUtils.isNotBlank(searchModelForm.getAppRefNo())) {
			if (sc == null) {
				sc = new SearchCriteria("appRefNo",
						SearchCriteria.EQUAL,
						searchModelForm.getAppRefNo());
			} else {
				sc = sc.andIfNotNull("appRefNo",
						SearchCriteria.EQUAL,
						searchModelForm.getAppRefNo());
			}
		}
		if (StringUtils.isNotBlank(searchModelForm.getIcNo())) {
			if (sc == null) {
				sc = new SearchCriteria("payerId",
						SearchCriteria.EQUAL,
						searchModelForm.getIcNo());
			} else {
				sc = sc.andIfNotNull("payerId",
						SearchCriteria.EQUAL,
						searchModelForm.getIcNo());
			}
		}
		
		if (searchModelForm.getDate() != null) {
			SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (sc == null) {
				try {
					sc = new SearchCriteria("requestDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(searchModelForm.getDate()) + " 00:00:00"));
					sc = sc.andIfNotNull("requestDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(searchModelForm.getDate()) + " 23:59:59"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					sc = sc.andIfNotNull("requestDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(searchModelForm.getDate()) + " 00:00:00"));
					sc = sc.andIfNotNull("requestDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(searchModelForm.getDate()) + " 23:59:59"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (sc == null) {
			sc = new SearchCriteria("paymentMode",
					SearchCriteria.EQUAL,
					Parameter.PAYMENT_MODE_CASH);
		} else {
			sc = sc.andIfNotNull("paymentMode",
					SearchCriteria.EQUAL,
					Parameter.PAYMENT_MODE_CASH);
		}

//		sc = sc.andIfNotNull("status", SearchCriteria.EQUAL,
//				Parameter.PAYMENT_STATUS_PENDING);
		
		return sc;
	}

	public SSMSortableDataProvider populateTable(SearchCriteria sc, AjaxRequestTarget target) {
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer(
				"wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt",
				SortOrder.DESCENDING, sc, LlpPaymentTransactionService.class);
		
		final SSMDataView<LlpPaymentTransaction> dataView = new SSMDataView<LlpPaymentTransaction>(
				"sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpPaymentTransaction> item) {
				final LlpPaymentTransaction llpPaymentTransaction = item
						.getModelObject();

				item.add(new SSMLabel("transactionId", llpPaymentTransaction
						.getTransactionId()));
				item.add(new SSMLabel("payerId", llpPaymentTransaction
						.getPayerId()));
				item.add(new SSMLabel("payerName", llpPaymentTransaction
						.getPayerName()));
				item.add(new SSMLabel("amount", llpPaymentTransaction
						.getAmount()));
				item.add(new SSMLabel("appRefNo", llpPaymentTransaction
						.getAppRefNo()));
				item.add(new SSMLabel("requestDt", llpPaymentTransaction
						.getRequestDt(), "dd/MM/yyyy hh:mm:ss a"));

				
				Link makePaymentLink = new Link("makePayment", item.getDefaultModel()) {
					public void onClick() {
						LlpPaymentTransaction llpPaymentTransaction = item
								.getModelObject();

						//check if dah buat payment ke belum
						LlpPaymentReceipt receipt = llpPaymentReceiptService.find(llpPaymentTransaction.getTransactionId());
						System.out.println("receipt : " + receipt.getTransactionId());
						if(receipt.getTransactionId() != null){
							RobCounterSession sessionObj = (RobCounterSession) SignInSession
									.get().getAttribute("_currentCheckinSession");
							setResponsePage(new ListOtcPaymentPage(sessionObj.getSessionId()));
						}else{
							setResponsePage(new ViewOtcPaymentPage(
									llpPaymentTransaction.getTransactionId()));
						}
						
					}
				};
				item.add(makePaymentLink);

				SSMLabel paymentNotPending = new SSMLabel("paymentNotPending", resolve("page.lbl.ezbiz.otcPaymentPage.notPending"));
				item.add(paymentNotPending);
				
				
				boolean showMakePayment = false;
				if(Parameter.PAYMENT_STATUS_PENDING.equals(llpPaymentTransaction.getStatus())){
					if(DateUtils.isSameDay(llpPaymentTransaction.getCreateDt(), new Date())){
						showMakePayment = true;
					}else{
						paymentNotPending.setDefaultModelObject(resolve("page.lbl.ezbiz.otcPaymentPage.paymentExpired"));
					}
				}else if(Parameter.PAYMENT_STATUS_CANCEL.equals(llpPaymentTransaction.getStatus())){
					paymentNotPending.setDefaultModelObject(resolve("page.lbl.ezbiz.otcPaymentPage.paymentCancel"));
				}
				else if(Parameter.PAYMENT_STATUS_FAIL.equals(llpPaymentTransaction.getStatus())){
					paymentNotPending.setDefaultModelObject(resolve("page.lbl.ezbiz.otcPaymentPage.paymentFail"));
				}else if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
					paymentNotPending.setDefaultModelObject(resolve("page.lbl.ezbiz.otcPaymentPage.paymentAlreadyPaid"));
				}
				
				if(showMakePayment){
					makePaymentLink.setVisible(true);
					paymentNotPending.setVisible(false);
				}else{
					makePaymentLink.setVisible(false);
					paymentNotPending.setVisible(true);
				}
				
				
				

				item.add(AttributeModifier.replace("class",
						new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even"
										: "odd";
							}
						}));
			}
		};

		dataView.setItemsPerPage(20L);

		wmcSearchResult.add(dataView);
		wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
		wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));
		if (target == null) {
			add(wmcSearchResult);
		} else {
			replace(wmcSearchResult);
			target.add(wmcSearchResult);
		}
		return dp;
	}

	private class ListOtcPaymentModel implements Serializable {
		private String transactionId;
		private String appRefNo;
		private String icNo;
		private Date date;
		private RobCounterSession robCounterSession;

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getAppRefNo() {
			return appRefNo;
		}

		public void setAppRefNo(String appRefNo) {
			this.appRefNo = appRefNo;
		}

		public String getIcNo() {
			return icNo;
		}

		public void setIcNo(String icNo) {
			this.icNo = icNo;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public RobCounterSession getRobCounterSession() {
			return robCounterSession;
		}

		public void setRobCounterSession(RobCounterSession robCounterSession) {
			this.robCounterSession = robCounterSession;
		}

	}

	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.ListPayment";
		return titleKey;
	}
}