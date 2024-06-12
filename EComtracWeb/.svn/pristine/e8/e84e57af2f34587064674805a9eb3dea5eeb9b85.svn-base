package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;
import com.ssm.llp.ezbiz.model.RobRenewal;

@SuppressWarnings({"all"})
public class ListOtcReceipt  extends SecBasePage{

	@SpringBean(name = "RobPaymentCreditNoteService")
	RobPaymentCreditNoteService robPaymentCreditNoteService;
	
	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;
	
	@SpringBean(name = "RobFormAService")
	RobFormAService robFormAService;
	
	@SpringBean(name = "RobFormBService")
	RobFormBService robFormBService ;
	
	@SpringBean(name = "RobFormCService")
	RobFormCService robFormCService;
	
	@SpringBean(name = "RobRenewalService")
	RobRenewalService robRenewalService;
	
	public ListOtcReceipt(Integer counterSessionId) {
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						OtcReceiptModel otcReceiptModel = new OtcReceiptModel();
						otcReceiptModel.setRobCounterSession(robCounterSession);
						return otcReceiptModel;
					}
				}));
		
		add(new ListOtcReceiptForm("form",(IModel<OtcReceiptModel>) getDefaultModel()));
	}
	
	private class ListOtcReceiptForm extends Form implements Serializable {
		public ListOtcReceiptForm(String id, IModel<OtcReceiptModel> m) {
			super(id, m);
			final OtcReceiptModel otcReceiptModel = m.getObject();
			
			SSMTextField receiptNo = new SSMTextField("receiptNo");
			receiptNo.setLabelKey("page.lbl.ezbiz.otcPaymentPage.receiptNo");
			receiptNo.setOutputMarkupId(true);
			add(receiptNo);

			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					OtcReceiptModel searchModelForm = (OtcReceiptModel) form
							.getDefaultModelObject();
					if(searchModelForm.getReceiptNo() == null){
						ssmError("Receipt No is required!");
					}else{
						SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId", SearchCriteria.EQUAL, otcReceiptModel.getRobCounterSession().getSessionId())
						.andIfNotNull("isCancel", SearchCriteria.EQUAL, Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
						if (StringUtils.isNotBlank(searchModelForm
								.getReceiptNo())) {
							sc = sc.andIfNotNull("receiptNo", SearchCriteria.EQUAL, searchModelForm.getReceiptNo());
						}
						
						populateTable(sc, target);
					}
					FeedbackPanel feedbackPanel =  ((ListOtcReceipt)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
			};
			add(search);
			populateTable(null, null);
			
			
			SSMAjaxLink back = new SSMAjaxLink("back") {
				@Override
				public void onClick(AjaxRequestTarget target){
					setResponsePage(new ListOtcPaymentPage(otcReceiptModel.getRobCounterSession().getSessionId()));
				}
			};
			add(back);
		}
	}
	
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target) {
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer(
				"wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt",
				SortOrder.DESCENDING, sc, LlpPaymentReceiptService.class);
		final SSMDataView<LlpPaymentReceipt> dataView = new SSMDataView<LlpPaymentReceipt>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(final Item<LlpPaymentReceipt> item) {
				final LlpPaymentReceipt llpPaymentReceipt = item.getModelObject();
				final WebMarkupContainer wmcIssue = new WebMarkupContainer("wmcIssue");
				wmcIssue.setOutputMarkupPlaceholderTag(true);
				wmcIssue.setVisible(true);
				
				
				LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) 
						getService(LlpPaymentTransactionService.class.getSimpleName()).findById(llpPaymentReceipt.getTransactionId());
				
				item.add(new SSMLabel("paymentReceiptNo", llpPaymentReceipt.getReceiptNo()));
				item.add(new SSMLabel("transactionId", llpPaymentReceipt.getTransactionId()));
				item.add(new SSMLabel("payerId", llpPaymentTransaction
						.getPayerId()));
				item.add(new SSMLabel("payerName", llpPaymentTransaction
						.getPayerName()));
				item.add(new SSMLabel("amount", llpPaymentTransaction
						.getAmount()));
				item.add(new SSMLabel("appRefNo", llpPaymentTransaction
						.getAppRefNo()));
				item.add(new SSMLabel("paymentDate", llpPaymentReceipt.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));

				SSMAjaxLink issueCreditNot = new SSMAjaxLink("issueCreditNote", item.getDefaultModel()) {
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new ViewCreditNoteDetail(llpPaymentReceipt.getReceiptNo()));
					}
				};
				issueCreditNot.setOutputMarkupPlaceholderTag(true);
				issueCreditNot.setEnabled(false);
				if(enableCreditNote(llpPaymentTransaction)){
					System.out.println("masuk");
					issueCreditNot.setEnabled(true);
				}
				
				wmcIssue.add(issueCreditNot);
				item.add(wmcIssue);
				
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
	}
	
	public boolean enableCreditNote(LlpPaymentTransaction llpPaymentTransaction){
		Boolean enable = true;
		LlpParameters parameters = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG, Parameter.PAYMENT_CONFIG_IS_CHECKING_STATUS_CN);
		String status = null;
		if(Parameter.YES_NO_yes.equals(parameters.getCodeDesc())){
			if(llpPaymentTransaction.getAppRefNo().startsWith("EB-A")){
				RobFormA formA = robFormAService.findById(llpPaymentTransaction.getAppRefNo());
				status = formA.getStatus();
			}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-B")){
				RobFormB formB = robFormBService.findById(llpPaymentTransaction.getAppRefNo());
				status = formB.getStatus();
			}else if (llpPaymentTransaction.getAppRefNo().startsWith("EB-C")){
				RobFormC formC = robFormCService.findById(llpPaymentTransaction.getAppRefNo());
				status = formC.getStatus();
			}else if(llpPaymentTransaction.getAppRefNo().startsWith("ROB_RENEW")){
				RobRenewal robRenewal = robRenewalService.findById(llpPaymentTransaction.getAppRefNo());
				status = robRenewal.getStatus();
			}
			
			System.out.println("status : " + status);
			if(!status.equalsIgnoreCase("R")){
				enable = false;
			}
		}
		
		System.out.println("enable : " + enable);
		return enable;
	}
	
	public class OtcReceiptModel implements Serializable{
		private String receiptNo;
		private RobCounterSession robCounterSession;
		
		public String getReceiptNo() {
			return receiptNo;
		}
		public void setReceiptNo(String receiptNo) {
			this.receiptNo = receiptNo;
		}
		public RobCounterSession getRobCounterSession() {
			return robCounterSession;
		}
		public void setRobCounterSession(RobCounterSession robCounterSession) {
			this.robCounterSession = robCounterSession;
		}
		
		
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.CreditNoteTitle";
		return titleKey;
	}
}
