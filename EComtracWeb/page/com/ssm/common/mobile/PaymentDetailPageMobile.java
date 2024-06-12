package com.ssm.common.mobile;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.PaymentService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class PaymentDetailPageMobile extends SecBasePageMobile {
	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	@SpringBean(name = "PaymentService")
	private PaymentService paymentService;
	
	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	public PaymentDetailPageMobile() {		
		initDefaultModel();
		init();
	}

	public PaymentDetailPageMobile(String appRefNo, String serviceName, Serializable obj, List<LlpPaymentTransactionDetail> paymentItems) {
		initDefaultModel();		
		
		String paymentId = paymentService.getSuccessPaymentIdByAppRefNo(appRefNo);
		if(StringUtils.isNotBlank(paymentId)){
			String registrationFail = Parameter.YES_NO_no;
			try {
				PaymentInterface paymentInterface = (PaymentInterface) getService(serviceName);
				paymentInterface.sucessPayment(obj, paymentId);
				paymentInterface.sucessNotification(obj, paymentId);
				
			} catch (SSMException e) {
				e.printStackTrace();
				ssmError(e);
				registrationFail = Parameter.YES_NO_yes;
			}
			
			PageParameters paramToSend = new PageParameters();
			paramToSend.set("transId", paymentId);
			paramToSend.set("registrationFail", registrationFail);

			SignInSession.get().removeAttribute("_paymentDetails");
			SignInSession.get().removeAttribute("_obj");
			SignInSession.get().removeAttribute("_transactionId");
			SignInSession.get().removeAttribute("_serviceName");
			
//			setResponsePage(PaymentReceiptPage.class, paramToSend);
			
			throw new RestartResponseAtInterceptPageException(PaymentReceiptPageMobile.class, paramToSend);

			
		}

		SignInSession.get().setAttribute("_appRefNo", appRefNo);
		SignInSession.get().setAttribute("_obj", obj);
		SignInSession.get().setAttribute("_paymentDetails", (Serializable) paymentItems);
		SignInSession.get().setAttribute("_serviceName", serviceName);

		init();
	}

	public PaymentDetailPageMobile(PageParameters param) {
		initDefaultModel();
		init();

		StringValue refId = param.get("id");

		try {
			if (refId != null) {
				Object obj = SignInSession.get().getAttribute("_obj");
				String sessionTransId = (String) SignInSession.get().getAttribute("_transactionId");
				String serviceName = (String) SignInSession.get().getAttribute("_serviceName");
				System.out.println("SESSION ID = "+sessionTransId+" >>>>>  URL ID = "+refId);
				//Check session ref id with url pass id to avoid url injection
				if(StringUtils.isNotBlank(sessionTransId) && sessionTransId.equals(refId.toString())){
					paymentService.paymentSuccess(refId.toString(), null);
					
					
					//IS TRANSACTION
					String registrationFail = Parameter.YES_NO_no;
					try {
						PaymentInterface paymentInterface = (PaymentInterface) getService(serviceName);
						paymentInterface.sucessPayment(obj, refId.toString());
						paymentInterface.sucessNotification(obj, refId.toString());
						
					} catch (SSMException e) {
						e.printStackTrace();
						ssmError(e);
						registrationFail = Parameter.YES_NO_yes;
					}
					
					
					PageParameters paramToSend = new PageParameters();
					paramToSend.set("transId", refId.toString());
					paramToSend.set("registrationFail", registrationFail);

					SignInSession.get().removeAttribute("_paymentDetails");
					SignInSession.get().removeAttribute("_obj");
					SignInSession.get().removeAttribute("_transactionId");
					SignInSession.get().removeAttribute("_serviceName");
					
					setResponsePage(PaymentReceiptPageMobile.class, paramToSend);
				}else{
					ssmError(SSMExceptionParam.PAYMENT_TRANS_INVALID);
				}				
			} else {
				ssmError(SSMExceptionParam.PAYMENT_ID_INVALID);
			}
		} catch (SSMException e) {			
			ssmError(e);
		} catch (Exception e) {
			e.printStackTrace();
			if(StringUtils.isNotBlank(e.getMessage())){
				ssmError(e.getMessage());
			}else{
				ssmError(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
			}
			
		} finally {
//			initDefaultModel();
		}

	}
	
	private void initDefaultModel(){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				LlpPaymentTransaction paymentTransaction = new LlpPaymentTransaction();
				
				if(!UserEnvironmentHelper.isInternalUser()){
					LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
					if(profile!=null){
						paymentTransaction.setPayerName(profile.getName());
						paymentTransaction.setPayerId(profile.getIdNo());
					}
				}
				return paymentTransaction;
			}
		}));
	}

	private void init() {
		add(new PaymentForm("form", getDefaultModel()));
	}

	private class PaymentForm extends Form {

		public PaymentForm(String id, IModel m) {
			super(id, m);

			final Object obj = (Object) SignInSession.get().getAttribute("_obj");
			final String appRefNo = (String) SignInSession.get().getAttribute("_appRefNo");
			final List<LlpPaymentTransactionDetail> paymentItems = (List<LlpPaymentTransactionDetail>) SignInSession.get().getAttribute("_paymentDetails");

			double total = 0;
			if (paymentItems != null && !paymentItems.isEmpty()) {
				for (Iterator iterator = paymentItems.iterator(); iterator.hasNext();) {
					LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
					if(llpPaymentTransactionDetail.getAmount()==0){
						LlpPaymentFee fee = ((LlpPaymentFeeService) getService(LlpPaymentFeeService.class.getSimpleName()))
							.findById(llpPaymentTransactionDetail.getPaymentItem());
						llpPaymentTransactionDetail.setAmount(fee.getPaymentFee());
					}
					total += llpPaymentTransactionDetail.getAmount();
				}
			} else {
				ssmError(SSMExceptionParam.PAYMENT_ITEMS_INVALID);
			}
			
			
			SSMTextField payerName = new SSMTextField("payerName");
			payerName.setRequired(true);
			payerName.setLabelKey("paymentdetails.page.payerName");
			add(payerName);

			SSMTextField payerId = new SSMTextField("payerId");
			payerId.setRequired(true);
			payerId.setLabelKey("paymentdetails.page.payerId");
			add(payerId);

			add(new SSMLabel("amount", total));

			add(new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
				public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
					final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
					// LlpPaymentFee fee = ((LlpPaymentFeeService)
					// getService(LlpPaymentFeeService.class
					// .getSimpleName())).findById(paymentItem
					// .getPaymentItem());
					// paymentItem.setAmount(fee.getPaymentFee());

					item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));
					item.add(new SSMLabel("itemQuantity", String.valueOf(paymentItem.getQuantity())));
					item.add(new SSMLabel("itemAmount", paymentItem.getAmount()));
					item.add(new SSMLabel("itemDet", paymentItem.getPaymentDet()));
				}
			});

			add(new Button("pay") {
				public void onSubmit() {
					LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) getForm().getModelObject();
					llpPaymentTransaction.setAppRefNo(appRefNo);
					try {


						llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName())).processPayment(
								llpPaymentTransaction, paymentItems);

						if (StringUtils.isNotBlank(llpPaymentTransaction.getTransactionId())) {
							String refid = llpPaymentTransaction.getTransactionId();
							
							
							if (obj!=null && obj instanceof LlpRegistration) {
								LlpRegistration llpRegistration = (LlpRegistration) obj;
								LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
								llpRegTransaction.setPaymentTransactionId(refid);
								llpRegTransaction.setStatus(Parameter.REG_TRANSACTION_STATUS_pending_payment);
								llpRegTransaction.setLlpRegistration(llpRegistration);
								llpRegTransactionService.update(llpRegTransaction);
							}
							
							if (obj!=null && obj instanceof RobRenewal) {
								RobRenewal robRenewal = (RobRenewal) obj;
								robRenewal.setStatus("PP");
								robRenewalService.update(robRenewal);
							}
							
							//Strore transaction id for checking
							SignInSession.get().setAttribute("_transactionId", refid);
							String ssmPgLandPage = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PG_LANDING_PAGE);
							String url = ssmPgLandPage + refid;
							setResponsePage(new RedirectPage(url));
							
							
						} else {
							ssmError(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ssmError(e.getMessage());
					} finally {
						SignInSession.get().setAttribute("_paymentTran", llpPaymentTransaction);
					}

				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					//setResponsePage(ViewListLlpReservedNames.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}
}
