package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.dashboard.DashboardRenewalAlert;
import com.ssm.ezbiz.robFormB.TabRobFormBPage;
import com.ssm.ezbiz.robFormC.ListRobFormCTransactionPage;
import com.ssm.ezbiz.robformA.TabRobFormAPage;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.common.service.RobPosTerminalTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.hibernate.LlpIdGenerator;
import com.ssm.llp.base.page.AlertPanel;
import com.ssm.llp.base.page.PaymentReceiptPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobPosTerminalTransaction;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.ssm.ssmlocalsvr.POSTerminalUtils;
import com.sun.tools.ws.wscompile.Options.Target;

@SuppressWarnings({ "all" })
public class ViewOtcPaymentPage extends SecBasePage{
	
	@SpringBean(name = "LlpPaymentReceiptService")
	private LlpPaymentReceiptService llpPaymentReceiptService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;
	
	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	
	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "LlpRunningNoService")
	private LlpRunningNoService llpRunningNoService;
	
	@SpringBean(name = "RobPosTerminalTransactionService")
	private RobPosTerminalTransactionService robPosTerminalTransactionService;
	
	
	public ViewOtcPaymentPage(final String transactionId) {
		final DecimalFormat df = new DecimalFormat("#0.00");
		final LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) getService(
				LlpPaymentTransactionService.class.getSimpleName())
				.findById(transactionId);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				PaymentReceivedModel paymentReceivedModel = new PaymentReceivedModel();
				paymentReceivedModel.setTotalAmount(Double.valueOf(df.format(llpPaymentTransaction.getAmount())));
				paymentReceivedModel.setBalance(Double.valueOf("0"));
				paymentReceivedModel.setTransactionId(llpPaymentTransaction.getTransactionId());
				return paymentReceivedModel;
			}
		}));
		init();
		
		populateDetail(llpPaymentTransaction);
	}
	
	private void init() {

//		final AlertPanel alertPanel = new AlertPanel("alert", true);
//		add(alertPanel);
		add(new ViewOtcPaymentForm("form", getDefaultModel() ));
	}

	private class ViewOtcPaymentForm extends Form implements Serializable {
		public ViewOtcPaymentForm(String id, IModel m) {
			super(id, m);
			
			String isAllowCreditCardStr = getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.IS_ALLOW_CREDIT_CARD);
			boolean isAllowCreditCard = false;
			if(Parameter.YES_NO_yes.equals(isAllowCreditCardStr)) {
				isAllowCreditCard = true;
			}else {
				isAllowCreditCard = false;
			}
			
//			final WebMarkupContainer wmcCreditCardTabMenu = new WebMarkupContainer("wmcCreditCardTabMenu");
//			wmcCreditCardTabMenu.setPrefixLabelKey("page.lbl.ezbiz.otcPaymentPage.");
//			wmcCreditCardTabMenu.setOutputMarkupId(true);
//			wmcCreditCardTabMenu.setOutputMarkupPlaceholderTag(true);
//			wmcCreditCardTabMenu.setVisible(isAllowCreditCard);
//			add(wmcCreditCardTabMenu);
			
			final CreditCardProcessingPanel creditCardProcessingPanel = new CreditCardProcessingPanel("creditCardProcessingPanel");
			add(creditCardProcessingPanel);
			
			
			final PaymentReceivedModel paymentReceivedModel = (PaymentReceivedModel) m.getObject();
			final RobCounterSession counterSession = (RobCounterSession) SignInSession.get().getAttribute("_currentCheckinSession");
			
			
			final HiddenField hiddenInput = new HiddenField("hiddenInput");
			hiddenInput.setOutputMarkupId(true);
			add(hiddenInput);
			
			AjaxFormComponentUpdatingBehavior behavior = new AjaxFormComponentUpdatingBehavior("onchange") {
				
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					PaymentReceivedModel paymentReceivedModel = (PaymentReceivedModel) getDefaultModelObject();
					String hexResponseMsg = hiddenInput.getValue();
					hexResponseMsg = StringUtils.remove(hexResponseMsg, "<END>");
					String error = "Please Try Again!!";
					String jScript="";
					try {
						List listResponse = WicketUtils.getSSMSvrResponse(hexResponseMsg);
						
						String responseStatus = (String) listResponse.get(0);
						
						if("00".equals(responseStatus)) {
							String cmd = (String) listResponse.get(1);
							byte byteResponse[] = (byte[]) listResponse.get(2);
							
							RobPosTerminalTransaction posTerminalTransaction = (RobPosTerminalTransaction) SignInSession.get().getAttribute("_currentPOSTerminalInSession");
							robPosTerminalTransactionService.processAndUpdate(cmd, byteResponse, posTerminalTransaction);
							String terminalMachineStatus = posTerminalTransaction.getStatus();
							
							if("00".equals(terminalMachineStatus)) {
								jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('success');";
								
//								LlpPaymentTransaction llpPaymentTransaction = llpPaymentReceiptService.receivePaymentGenerateReceiptForIntegrationCardPayment(posTerminalTransaction);
//								if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
//									try {
//										llpPaymentReceiptService.lodgeWSAfterRecieptGenerate(llpPaymentTransaction.getTransactionId());
//									} catch (Exception e) {
//										System.err.println("Cannot Lodge WS:"+llpPaymentTransaction.getTransactionId());
//										e.printStackTrace();
//									}
//								}
//								PageParameters paramToSend = new PageParameters();
//								paramToSend.set("transId", llpPaymentTransaction.getTransactionId());
//								paramToSend.set("registrationFail", Parameter.YES_NO_no);
//								SignInSession.get().removeAttribute("_paymentDetails");
//								SignInSession.get().removeAttribute("_obj");
//								SignInSession.get().removeAttribute("_transactionId");
//								SignInSession.get().removeAttribute("_serviceName");
//								
//								setResponsePage(PaymentReceiptPage.class, paramToSend);
							}else if("BT".equals(terminalMachineStatus)) {
								jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('The transaction timed out from Bank’s end');";
							}else if("CT".equals(terminalMachineStatus)) {
								jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('The transaction timed out from Terminal’s end/Terminal Cancel Transaction');";
							}else if("CD".equals(terminalMachineStatus)) {
								jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('Invalid Check Digit');";
							}else{
								jScript = "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('"+error+"');";
							}

							
						
						}else if("S1".equals(responseStatus)) {
							jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('Port busy');";
						}else if("S2".equals(responseStatus)) {
							jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('Port not found');";
						}else if("S3".equals(responseStatus)) {
							jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('Waiting to long');";
						}else {
							jScript += "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('"+responseStatus+"');";
						}
						
					} catch (Exception e) {
						jScript = "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal('hide');alert('"+error+"');";
					}

					arg0.appendJavaScript(jScript);
				}
			};
			
			hiddenInput.add(behavior);
			
			
			
			/*hiddenInput.add(new AjaxEventBehavior("change") {

			    @Override
			    protected void onEvent(final AjaxRequestTarget target) {
			        // Execute any code you like and respond with an ajax response 
			    	PaymentReceivedModel paymentReceivedModel2 = (PaymentReceivedModel) getDefaultModelObject();
			    	hiddenInput.modelChanged();
			    	System.out.println(hiddenInput.getValue());
			    	System.out.println(paymentReceivedModel2.getHiddenInput());
			    	System.out.println(paymentReceivedModel.getHiddenInput());
//			        	target.appendJavaScript("alert('Hidden Input Change Event Behaviour!');");
			        	
			        }
			});
			*/
			
			final DecimalFormat df = new DecimalFormat("#0.00");
			SSMLabel totalToPaid = new SSMLabel("totalToPaid", paymentReceivedModel.getTotalAmount());
			totalToPaid.setOutputMarkupId(true);
			add(totalToPaid);
			
			final SSMLabel balance = new SSMLabel("balance", paymentReceivedModel.getBalance());
			balance.setOutputMarkupId(true);
			add(balance);
			
			SSMTextField cashReceived = new SSMTextField("cashReceived");
			add(cashReceived);
			
			Link cancel = new Link("cancel") {
				@Override
				public void onClick() {
					setResponsePage(new ListOtcPaymentPage(counterSession.getSessionId()));
				}
				
			};
			add(cancel);
			
			
			
			final SSMAjaxButton payCreditCard = new SSMAjaxButton("payCreditCard") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					PaymentReceivedModel paymentReceivedModel = (PaymentReceivedModel) form.getDefaultModelObject();
					Boolean noError = true;
					
					if(noError){
						
						LlpPaymentTransaction llpPaymentTransaction = null;
						
						LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService.find(paymentReceivedModel.getTransactionId());
						
						if(llpPaymentReceipt.getReceiptNo() != null){
							llpPaymentTransaction = llpPaymentTransactionService.findById(llpPaymentReceipt.getTransactionId()) ;
						}else{							
							//Here call credit card machine
							try {
								
								RobPosTerminalTransaction robPosTerminalTransaction = new RobPosTerminalTransaction("020",counterSession.getCounterIpAddress().getRobCounterCollectionId(), counterSession.getSessionId(),  paymentReceivedModel.getTransactionId(), paymentReceivedModel.getTotalAmount());
								robPosTerminalTransactionService.insert(robPosTerminalTransaction);
								SignInSession.get().setAttribute("_currentPOSTerminalInSession", robPosTerminalTransaction);
								
								String deviceName[] = "VX520,Prolific".split(",");
								String deviceNameParam = getCodeTypeWithValue(Parameter.PAYMENT_CONFIG, Parameter.PAYMENT_CONFIG_EMV_DEVICE_NAME);
								if(StringUtils.isNotBlank(deviceNameParam)) {
									deviceName = deviceNameParam.split(",");
								}
								String cmdToSend = POSTerminalUtils.prepareSale(deviceName ,robPosTerminalTransaction.getTransAmount(), robPosTerminalTransaction.getCounterCollectionId());
								String jScript = "$('#"+creditCardProcessingPanel.getWmcAlertId()+"').modal({ offset: 0,closable : false }).modal('show'); connect('"+cmdToSend+"','"+creditCardProcessingPanel.getWmcAlertId()+"');";
								target.appendJavaScript(jScript);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
						
					}
					FeedbackPanel feedbackPanel =  ((ViewOtcPaymentPage)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
					
				}
				
			};
			payCreditCard.setOutputMarkupPlaceholderTag(true);
			payCreditCard.setOutputMarkupId(true);
			payCreditCard.setEnabled(true);
			add(payCreditCard);	
			
			
			
			
			final SSMAjaxButton pay = new SSMAjaxButton("pay") {//Cash Payment
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					PaymentReceivedModel paymentReceivedModel = (PaymentReceivedModel) form.getDefaultModelObject();
					Boolean noError = true;
					
					if(paymentReceivedModel.getCashReceived() == null){
						noError = false;
						ssmError(SSMExceptionParam.OTC_PAYMENT_CASH_RECEIVED_ISBLANK);
					}else{
						if(paymentReceivedModel.getCashReceived() < paymentReceivedModel.getTotalAmount()){
							noError = false;
							ssmError(SSMExceptionParam.OTC_PAYMENT_RECEIVED_GREATER_TOTAL);
						}
					}
					
					if(noError){
						
						LlpPaymentTransaction llpPaymentTransaction = null;
						
						LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService.find(paymentReceivedModel.getTransactionId());
						
						if(llpPaymentReceipt.getReceiptNo() != null){
							llpPaymentTransaction = llpPaymentTransactionService.findById(llpPaymentReceipt.getTransactionId()) ;
						}else{
							llpPaymentTransaction = llpPaymentReceiptService.receivePaymentGenerateReceiptForCash(paymentReceivedModel.getTransactionId(), counterSession.getSessionId(), paymentReceivedModel.getTotalAmount(),  paymentReceivedModel.getCashReceived());
							if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
								try {
									llpPaymentReceiptService.lodgeWSAfterRecieptGenerate(llpPaymentTransaction.getTransactionId());
								} catch (Exception e) {
									System.err.println("Cannot Lodge WS:"+llpPaymentTransaction.getTransactionId());
									e.printStackTrace();
								}
								
							}
						}
						
						PageParameters paramToSend = new PageParameters();
						paramToSend.set("transId", llpPaymentTransaction.getTransactionId());
						paramToSend.set("registrationFail", Parameter.YES_NO_no);
						
						SignInSession.get().removeAttribute("_paymentDetails");
						SignInSession.get().removeAttribute("_obj");
						SignInSession.get().removeAttribute("_transactionId");
						SignInSession.get().removeAttribute("_serviceName");
						
						setResponsePage(PaymentReceiptPage.class, paramToSend);
					}
					FeedbackPanel feedbackPanel =  ((ViewOtcPaymentPage)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
					
				}
				
			};
			pay.setOutputMarkupPlaceholderTag(true);
			pay.setOutputMarkupId(true);
			pay.setEnabled(true);
			add(pay);	
			
			SSMAjaxFormSubmitBehavior cashReceivedOnChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					PaymentReceivedModel receivedModel = (PaymentReceivedModel) getForm().getDefaultModelObject();
					if(receivedModel.getCashReceived() != null){
						pay.setEnabled(true);
						target.add(pay);
						balance.setDefaultModelObject(df.format(receivedModel.getCashReceived() - paymentReceivedModel.getTotalAmount()));
						paymentReceivedModel.setCashReceived(receivedModel.getCashReceived());
					}else{
						balance.setDefaultModelObject(df.format(Double.valueOf("0")));
					}
					target.add(balance);
				}
			};
			cashReceived.add(cashReceivedOnChange);
			
			AjaxEventBehavior test = new AjaxEventBehavior("onclick") {
			    @Override
			    protected void onEvent(AjaxRequestTarget target) {
			    	pay.setEnabled(false);
					target.add(pay);
			   }
			};
			pay.add(test);
			
			
			
			
			
			SSMDropDownChoice paymentCardBank = new SSMDropDownChoice("paymentCardBank", Parameter.PAYMENT_CARD_BANK);
			paymentCardBank.setLabelKey("page.lbl.ezbiz.otcPaymentPage.paymentCardBank");
			add(paymentCardBank);
			
			SSMDropDownChoice paymentCardType = new SSMDropDownChoice("paymentCardType", Parameter.PAYMENT_CARD_TYPE);
			paymentCardType.setLabelKey("page.lbl.ezbiz.otcPaymentPage.paymentCardType");
			add(paymentCardType);
			
			SSMTextField paymentCardApprovalCode = new SSMTextField("paymentCardApprovalCode");
			paymentCardApprovalCode.setLabelKey("page.lbl.ezbiz.otcPaymentPage.paymentCardApprovalCode");
			add(paymentCardApprovalCode);
			
			final String manualPayCardValidationJS = "manualPayCardValidationJS";
			String mainFieldToValidate[] = new String[]{"paymentCardBank","paymentCardType","paymentCardApprovalCode"};
			String mainFieldToValidateRules[] = new String[]{"empty","empty","minLengthNumber[6]"};
			setSemanticJSValidation(this, manualPayCardValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			
			final SSMAjaxButton manualPayCard = new SSMAjaxButton("manualPayCard", manualPayCardValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					PaymentReceivedModel paymentReceivedModel = (PaymentReceivedModel) form.getDefaultModelObject();
					LlpPaymentTransaction llpPaymentTransaction = null;
					LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService.find(paymentReceivedModel.getTransactionId());
					
					if(llpPaymentReceipt.getReceiptNo() != null){
						llpPaymentTransaction = llpPaymentTransactionService.findById(llpPaymentReceipt.getTransactionId()) ;
					}else{
						llpPaymentTransaction = llpPaymentReceiptService.receivePaymentGenerateReceiptForManualCardPayment(paymentReceivedModel.getTransactionId(), counterSession.getSessionId(), paymentReceivedModel.getTotalAmount(), paymentReceivedModel.getTotalAmount(), paymentReceivedModel.getPaymentCardApprovalCode(), paymentReceivedModel.getPaymentCardType(), paymentReceivedModel.getPaymentCardBank());
						if(Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())){
							try {
								llpPaymentReceiptService.lodgeWSAfterRecieptGenerate(llpPaymentTransaction.getTransactionId());
							} catch (Exception e) {
								System.err.println("Cannot Lodge WS:"+llpPaymentTransaction.getTransactionId());
								e.printStackTrace();
							}
							
						}
					}
					
					PageParameters paramToSend = new PageParameters();
					paramToSend.set("transId", llpPaymentTransaction.getTransactionId());
					paramToSend.set("registrationFail", Parameter.YES_NO_no);
					
					SignInSession.get().removeAttribute("_paymentDetails");
					SignInSession.get().removeAttribute("_obj");
					SignInSession.get().removeAttribute("_transactionId");
					SignInSession.get().removeAttribute("_serviceName");
					
					setResponsePage(PaymentReceiptPage.class, paramToSend);
					
				}
				
			};
			manualPayCard.setOutputMarkupPlaceholderTag(true);
			manualPayCard.setOutputMarkupId(true);
			manualPayCard.setEnabled(true);
			add(manualPayCard);
			

			

			String jsScript ="";
			if(!isAllowCreditCard) {
				jsScript = "document.getElementById('creditCardTab').style.visibility = \"hidden\"; ";
			}
			 
			
			Label jsScriptLable = new Label("otcJsScript", jsScript);
			jsScriptLable.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		    add(jsScriptLable);
		    
			
			
		}
		
		
	}
	
	public void populateDetail(LlpPaymentTransaction llpPaymentTransaction) {
		

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
		
		add(new SSMLabel("transId",llpPaymentTransaction.getTransactionId()));
		add(new SSMLabel("appRefNo", llpPaymentTransaction.getAppRefNo()));
		add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
		add(new MultiLineLabel("payerAddr", llpPaymentTransaction.getPayerAddr()));

	}
	
	public class PaymentReceivedModel implements Serializable{
		public Double cashReceived;
		public Double balance;
		public Double totalAmount;
		public String transactionId;
		public String hiddenInput="";
		public String showHideManualFieldInput;
		
		public String paymentCardApprovalCode;
		public String paymentCardType;
		public String paymentCardBank;
		
		public Double getCashReceived() {
			return cashReceived;
		}
		public void setCashReceived(Double cashReceived) {
			this.cashReceived = cashReceived;
		}
		public Double getBalance() {
			return balance;
		}
		public void setBalance(Double balance) {
			this.balance = balance;
		}
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
		public String getHiddenInput() {
			return hiddenInput;
		}
		public void setHiddenInput(String hiddenInput) {
			this.hiddenInput = hiddenInput;
		}
		public String getPaymentCardType() {
			return paymentCardType;
		}
		public void setPaymentCardType(String paymentCardType) {
			this.paymentCardType = paymentCardType;
		}
		public String getPaymentCardBank() {
			return paymentCardBank;
		}
		public void setPaymentCardBank(String paymentCardBank) {
			this.paymentCardBank = paymentCardBank;
		}
		public String getPaymentCardApprovalCode() {
			return paymentCardApprovalCode;
		}
		public void setPaymentCardApprovalCode(String paymentCardApprovalCode) {
			this.paymentCardApprovalCode = paymentCardApprovalCode;
		}
		public String getShowHideManualFieldInput() {
			return showHideManualFieldInput;
		}
		public void setShowHideManualFieldInput(String showHideManualFieldInput) {
			this.showHideManualFieldInput = showHideManualFieldInput;
		}
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.transactionDetails";
		return titleKey;
	}
	
	
}
