package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.annotations.Sort;

import com.ssm.ezbiz.otcPayment.ViewOtcPaymentPage.PaymentReceivedModel;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.PaymentReceiptPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({"all"})
public class ViewCreditNoteDetail extends SecBasePage{
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	@SpringBean(name = "RobPaymentCreditNoteService")
	private RobPaymentCreditNoteService robPaymentCreditNoteService;
	
	public ViewCreditNoteDetail(final String receiptNo) {
		final DecimalFormat df = new DecimalFormat("#0.00");
		final LlpPaymentReceipt llpPaymentReceipt = (LlpPaymentReceipt) getService(LlpPaymentReceiptService.class.getSimpleName()).findById(receiptNo);
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(llpPaymentReceipt.getTransactionId());
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				CreditNoteFormModel creditNoteFormModel = new CreditNoteFormModel();
				creditNoteFormModel.setLlpPaymentReceipt(llpPaymentReceipt);
				return creditNoteFormModel;
			}
		}));
		init();
		
		populateDetail(llpPaymentTransaction, llpPaymentReceipt);
	}
	
	private void init() {
		add(new ViewCreditNoteDetailForm("form", getDefaultModel()));
	}

	private class ViewCreditNoteDetailForm extends Form implements Serializable {
		public ViewCreditNoteDetailForm(String id, IModel m) {
			super(id, m);
			
			final CreditNoteFormModel creditNoteFormModel = (CreditNoteFormModel) m.getObject();
			final RobCounterSession counterSession = (RobCounterSession) SignInSession.get().getAttribute("_currentCheckinSession");
			
			SSMTextField username = new SSMTextField("username");
			username.setLabelKey("page.lbl.ezbiz.creditNoteDetail.username");
			username.setUpperCase(false);
			username.setRequired(false);
			add(username);
			
			SSMPasswordTextField password = new SSMPasswordTextField("password");
			password.setLabelKey("page.lbl.ezbiz.creditNoteDetail.password");
			password.setRequired(false);
			add(password);
			
			SSMTextArea reason = new SSMTextArea("reason");
			reason.setLabelKey("page.lbl.ezbiz.creditNoteDetail.reason");
			reason.setRequired(false);
			add(reason);
			
			Link cancel = new Link("cancel") {
				@Override
				public void onClick() {
					setResponsePage(new ListOtcReceipt(counterSession.getSessionId()));
				}
				
			};
			add(cancel);
			
			SSMAjaxButton submit = new SSMAjaxButton("submit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					CreditNoteFormModel formModel = (CreditNoteFormModel) form.getDefaultModelObject();
					boolean isNoError = true;
					if(formModel.getUsername() == null){
						isNoError = false;
						ssmError(SSMExceptionParam.USERNAME_IS_BLANK);		   
					}else{
						if(UserEnvironmentHelper.getLoginName().equals("SSM:" + formModel.getUsername())){
							isNoError = false;
							ssmError(SSMExceptionParam.LOGIN_SECOND_LEVEL_SAME_STAF);	
						}
					}
					
					if(formModel.getPassword() == null){
						isNoError = false;
						ssmError(SSMExceptionParam.PASSWORD_IS_BLANK);		   
					}
					
					if(formModel.getReason() == null){
						isNoError = false;
						ssmError(SSMExceptionParam.REASON_IS_BLANK);		   
					}
					
					
					if(isNoError){
			        	System.out.println("masuk");
			        	SignInSession session = new SignInSession(getRequest());
			            session.setLoginType(Parameter.LOGIN_TYPE_second_level);
			            session.setSignInForm(form);
			            // Sign the user in
			            if (session.authenticate(formModel.getUsername(), formModel.getPassword())){
			            	String loginId= "SSM:" + formModel.getUsername();
//			                LlpUserLog userLog = new LlpUserLog();
//			                userLog.setLoginId(loginId);
//			                userLog.setLoginTime(new Date());
//			                
//			                String ipAddress= getIpAddress();
//			                userLog.setIpAddress(ipAddress);
//			                llpUserLogService.insert(userLog);
			                
			                RobPaymentCreditNote creditNote = robPaymentCreditNoteService.processCreditNote(creditNoteFormModel.getLlpPaymentReceipt());
			                creditNote.setReason(formModel.getReason());
			                creditNote.setApproveBy(loginId);
			                creditNote.setApproveDt(new Date());
			                robPaymentCreditNoteService.update(creditNote);
			                
			                setResponsePage(new ViewCreditNoteSlip(creditNote));
			            }else{
			            	ssmError(SSMExceptionParam.USERNAME_NOT_MATCH);
			            }
					}
					
					FeedbackPanel feedbackPanel =  ((ViewCreditNoteDetail)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
					
				}
				
			};
			add(submit);			

		}
		
//		@Override
//		protected void onError() {
//			// TODO Auto-generated method stub
//			super.onError();
//		}
//
//		/**
//		 * @return
//		 */
//		private SignInSession getMySession() {
//			return (SignInSession) getSession();
//		}
		
	}
	
	public void populateDetail(LlpPaymentTransaction llpPaymentTransaction, LlpPaymentReceipt llpPaymentReceipt) {
		

		List<LlpPaymentTransactionDetail> paymentItems = ((LlpPaymentTransactionDetailService) getService(LlpPaymentTransactionDetailService.class
				.getSimpleName())).find(llpPaymentTransaction.getTransactionId());
		
		add(new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
			public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
				final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
				item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));
				SSMLabel itemDesc = new SSMLabel("itemDet", paymentItem.getPaymentDet());
				if(StringUtils.isBlank(paymentItem.getPaymentDet())){
					itemDesc.setVisible(false);
				}
				item.add(itemDesc);
				item.add(new SSMLabel("itemAmountWoutGst", paymentItem.getAmountWithOutGst()));
				item.add(new SSMLabel("itemGstAmt", paymentItem.getGstAmt()));
				item.add(new SSMLabel("itemAmount", paymentItem.getAmount())); 
				item.add(new SSMLabel("bil",item.getIndex()+1)); 
			}
		});
		
		
		add(new SSMLabel("totalToBePaid", llpPaymentTransaction.getAmount()));
		add(new SSMLabel("receiptNo",llpPaymentReceipt.getReceiptNo()));
		add(new SSMLabel("transId",llpPaymentTransaction.getTransactionId()));
		add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
		add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
		add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));

	}
	
	public class CreditNoteFormModel implements Serializable{
		private String reason;
		private String username;
		private String password;
		private LlpPaymentReceipt llpPaymentReceipt;
		
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public LlpPaymentReceipt getLlpPaymentReceipt() {
			return llpPaymentReceipt;
		}
		public void setLlpPaymentReceipt(LlpPaymentReceipt llpPaymentReceipt) {
			this.llpPaymentReceipt = llpPaymentReceipt;
		}
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.CreditNoteTitle";
		return titleKey;
	}
}
