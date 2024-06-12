package com.ssm.llp.base.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.ssm.ezbiz.eghl.EGHLPaymentRequest;
import com.ssm.ezbiz.eghl.EGHLPaymentResponse;
import com.ssm.ezbiz.otcPayment.ViewOtcSlip;
import com.ssm.ezbiz.service.CmpTransactionService;
import com.ssm.ezbiz.service.EGHLService;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.PaymentService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.ezbiz.service.RobTrainingReprintcertService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.CmpInfo;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;

@SuppressWarnings({ "all" })
public class PaymentDetailPage extends SecBasePage {
	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;

	@SpringBean(name = "PaymentService")
	private PaymentService paymentService;

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;

	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "CmpTransactionService")
	private CmpTransactionService cmpTransactionService;

	@SpringBean(name = "EGHLService")
	private EGHLService eghlService;

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;

	@SpringBean(name = "RobTrainingTransactionService")
	private RobTrainingTransactionService robTrainingTransactionService;
	
	@SpringBean(name = "RobTrainingReprintcertService")
	private RobTrainingReprintcertService robTrainingReprintcertService;	

	@SpringBean(name = "SupplyInfoTransHdrService")
	public SupplyInfoTransHdrService supplyInfoTransHdrService;

//	@SpringBean(name = "RmsDiscountService")
//	RmsDiscountService rmsDiscountService;

	public PaymentDetailPage() {
		initDefaultModel();
		init();
	}

	public PaymentDetailPage(String appRefNo, String serviceName, Serializable obj,
			List<LlpPaymentTransactionDetail> paymentItems) {
		initDefaultModel();

		String paymentId = paymentService.getSuccessPaymentIdByAppRefNo(appRefNo);
		System.out.println("p id : " + paymentId);
		if (StringUtils.isNotBlank(paymentId)) {
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

			throw new RestartResponseAtInterceptPageException(PaymentReceiptPage.class, paramToSend);

		}

		SignInSession.get().setAttribute("_appRefNo", appRefNo);
		SignInSession.get().setAttribute("_obj", obj);
		SignInSession.get().setAttribute("_paymentDetails", (Serializable) paymentItems);
		SignInSession.get().setAttribute("_serviceName", serviceName);

		init();
	}

	public PaymentDetailPage(PageParameters param) {
		initDefaultModel();
		init();

		StringValue refId = param.get("id");

		try {
			if (refId != null) {
				Object obj = SignInSession.get().getAttribute("_obj");
				String sessionTransId = (String) SignInSession.get().getAttribute("_transactionId");
				String serviceName = (String) SignInSession.get().getAttribute("_serviceName");
				System.out.println("SESSION ID = " + sessionTransId + " >>>>>  URL ID = " + refId);
				// Check session ref id with url pass id to avoid url injection
				if (StringUtils.isNotBlank(sessionTransId) && sessionTransId.equals(refId.toString())) {
					StringValue hashValue = getRequest().getRequestParameters().getParameterValue("HashValue");
					EGHLPaymentResponse paymentResponse = null;
					if (hashValue != null && StringUtils.isNotBlank(hashValue.toString())) {
						paymentResponse = new EGHLPaymentResponse(getRequest().getRequestParameters());
					}

					paymentService.paymentSuccess(refId.toString(), paymentResponse);

					// IS TRANSACTION
					String registrationFail = Parameter.YES_NO_no;

					LlpPaymentTransaction paymentTrans = llpPaymentTransactionService.findById(refId.toString());
					if (Parameter.PAYMENT_STATUS_SUCCESS.equals(paymentTrans.getStatus())) {
						PaymentInterface paymentInterface = (PaymentInterface) getService(serviceName);
						try {
							paymentInterface.sucessPayment(obj, refId.toString());
						} catch (SSMException e) {
							e.printStackTrace();
							ssmError(e);
							registrationFail = Parameter.YES_NO_yes;
						}

						try {
							paymentInterface.sucessNotification(obj, refId.toString());
						} catch (SSMException e) {
							e.printStackTrace();
							registrationFail = Parameter.YES_NO_yes;
						}

						PageParameters paramToSend = new PageParameters();
						paramToSend.set("transId", refId.toString());
						paramToSend.set("registrationFail", registrationFail);

						SignInSession.get().removeAttribute("_paymentDetails");
						SignInSession.get().removeAttribute("_obj");
						SignInSession.get().removeAttribute("_transactionId");
						SignInSession.get().removeAttribute("_serviceName");

						setResponsePage(PaymentReceiptPage.class, paramToSend);
					} else {
						ssmError(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
					}
				} else {
					ssmError(SSMExceptionParam.PAYMENT_TRANS_INVALID);
				}
			} else {
				ssmError(SSMExceptionParam.PAYMENT_ID_INVALID);
			}
		} catch (SSMException e) {
			ssmError(e);
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.isNotBlank(e.getMessage())) {
				ssmError(e.getMessage());
			} else {
				ssmError(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
			}

		} finally {
//			initDefaultModel();
		}

	}

	private void initDefaultModel() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				LlpPaymentTransaction paymentTransaction = new LlpPaymentTransaction();

				return paymentTransaction;
			}
		}));
	}

	private void updateStatus(Object obj, LlpPaymentTransaction llpPaymentTransaction, String paymentType) {

		String regTransactionStatus = null;
		String robFormAStatus = null;
		String robFormBStatus = null;
		String robFormCStatus = null;
		String robRenewalStatus = null;
		String cmpInfoStatus = null;
		String comtractStatus = null;
		String supplyInfoStatus = null;

		if (paymentType.equalsIgnoreCase("payOnline")) {
			regTransactionStatus = Parameter.REG_TRANSACTION_STATUS_pending_payment;
			robFormAStatus = Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT;
			robFormBStatus = Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT;
			robFormCStatus = Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT;
			robRenewalStatus = Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT;
			cmpInfoStatus = Parameter.CMP_PAYMENT_PENDING_PAYMENT;
			comtractStatus = Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment;
			supplyInfoStatus = Parameter.SUPPLY_INFO_TRANS_STATUS_PENDING_PAYMENT;

		} else {
			regTransactionStatus = Parameter.REG_TRANSACTION_STATUS_otc;
			robFormAStatus = Parameter.ROB_FORM_A_STATUS_OTC;
			robFormBStatus = Parameter.ROB_FORM_B_STATUS_OTC;
			robFormCStatus = Parameter.ROB_FORM_C_STATUS_OTC;
			robRenewalStatus = Parameter.ROB_RENEWAL_STATUS_OTC;
			cmpInfoStatus = Parameter.CMP_PAYMENT_OTC;
			comtractStatus = "OTC";
			supplyInfoStatus = Parameter.SUPPLY_INFO_TRANS_STATUS_OTC;
		}

		String refid = llpPaymentTransaction.getTransactionId();

		String processBranch = Parameter.ROB_AGENCY_BRANCH_CODE;
		String ipAddr = getIpAddress();

		String ip3Segment = ipAddr;
		if (ipAddr.contains(".")) {
			ip3Segment = ipAddr.substring(0, ipAddr.lastIndexOf("."));
		}
		String branchByIp = llpParametersService.findByCodeTypeValue(Parameter.IP_SEGMENT_BY_BRANCH, ip3Segment);
//		System.out.println("ipAddr:"+ipAddr);
//		System.out.println("ip3Segment:"+ip3Segment);
//		System.out.println("branchByIp:"+branchByIp);
		if (StringUtils.isNotBlank(branchByIp)) {
			String isChecklodgeByIpClient = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG,
					Parameter.LLP_CONFIG_IS_LODGE_BY_IP_CLIENT);
			if (Parameter.YES_NO_yes.equals(isChecklodgeByIpClient)) {
				processBranch = branchByIp;
			}
			try {
				BeanUtils.setProperty(obj, "isFromSSMPc", Parameter.YES_NO_yes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (obj != null && obj instanceof LlpRegistration) {
			LlpRegistration llpRegistration = (LlpRegistration) obj;
			LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
			llpRegTransaction.setPaymentTransactionId(refid);
			llpRegTransaction.setStatus(regTransactionStatus);
			llpRegTransaction.setLlpRegistration(llpRegistration);
			llpRegTransactionService.update(llpRegTransaction);
		}

		if (obj != null && obj instanceof RobRenewal) {
			RobRenewal robRenewal = (RobRenewal) obj;
			robRenewal.setStatus(robRenewalStatus);
			robRenewal.setProsessingBranch(processBranch);
			robRenewalService.update(robRenewal);
		}

		if (obj != null && obj instanceof RobFormA) {
			RobFormA robFormA = (RobFormA) obj;
			robFormA.setStatus(robFormAStatus);
			robFormA.setProsessingBranch(processBranch);
			robFormAService.update(robFormA);
		}

		if (obj != null && obj instanceof RobFormB) {
			RobFormB robFormB = (RobFormB) obj;
			robFormB.setStatus(robFormBStatus);
			robFormB.setProsessingBranch(processBranch);
			robFormBService.update(robFormB);
		}

		if (obj != null && obj instanceof CmpInfo) {
			CmpInfo cmpInfo = (CmpInfo) obj;
			CmpTransaction cmpTransaction = cmpInfo.getCmpTransaction();
			cmpTransaction.setStatus(cmpInfoStatus);
			cmpTransactionService.update(cmpTransaction);
		}

		if (obj != null && obj instanceof RobFormC) {
			RobFormC robFormC = (RobFormC) obj;
			robFormC.setStatus(robFormCStatus);
			robFormC.setProsessingBranch(processBranch);
			robFormCService.update(robFormC);
		}

		if (obj != null && obj instanceof RobTrainingTransaction) {
			RobTrainingTransaction robTrainingTransaction = (RobTrainingTransaction) obj;
			robTrainingTransaction.setStatus(comtractStatus);
			robTrainingTransactionService.update(robTrainingTransaction);
		}

		if (obj != null && obj instanceof RobTrainingReprintcert) {
			RobTrainingReprintcert robTrainingReprintcert = (RobTrainingReprintcert) obj;
			robTrainingReprintcert.setStatus(comtractStatus);
			robTrainingReprintcertService.update(robTrainingReprintcert);
		}
		
		if (obj != null && obj instanceof SupplyInfoTransHdr) {
			SupplyInfoTransHdr pcertForm = (SupplyInfoTransHdr) obj;
			pcertForm.setStatus(supplyInfoStatus);
			supplyInfoTransHdrService.update(pcertForm);

			getSession().removeAttribute("cartInfoHdr_");
		}

		if (paymentType.equalsIgnoreCase("payOnline")) {
			// Strore transaction id for checking
			SignInSession.get().setAttribute("_transactionId", refid);
			String ssmPgLandPage = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG,
					Parameter.LLP_CONFIG_PG_LANDING_PAGE);
			String url = ssmPgLandPage + refid;
//			setResponsePage( new RedirectPage(url));

//			if(!UserEnvironmentHelper.isInternalUser()){
//				LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
//				if(profile!=null){
//					profile.getEmail();
//				}
//			}

			String ipAddress = getIpAddress();

			LlpUserProfile profile = llpUserProfileService
					.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			EGHLPaymentRequest eghlSaleReq = eghlService.prepareSaleRequest(refid, llpPaymentTransaction.getAppRefNo(),
					"SSM EzBiz Payment", llpPaymentTransaction.getAmount(), ipAddress,
					llpPaymentTransaction.getPayerName(), profile.getEmail(), profile.getHpNo());

			setResponsePage(new EGHLRedirectPage(eghlSaleReq));
		} else {
			SignInSession.get().setAttribute("_transactionId", refid);
			setResponsePage(ViewOtcSlip.class);
		}

	}

	private void init() {
		add(new PaymentForm("form", getDefaultModel()));
	}

	private class PaymentForm extends Form {

		public PaymentForm(String id, IModel m) {
			super(id, m);

			final Object obj = (Object) SignInSession.get().getAttribute("_obj");
			final String appRefNo = (String) SignInSession.get().getAttribute("_appRefNo");
			final List<LlpPaymentTransactionDetail> paymentItems = (List<LlpPaymentTransactionDetail>) SignInSession
					.get().getAttribute("_paymentDetails");

			DecimalFormat df = new DecimalFormat("#.00");
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal totalGst = BigDecimal.ZERO;

			if (paymentItems != null && !paymentItems.isEmpty()) {

//				rmsDiscountService.recalculateDiscount(paymentItems);

				for (Iterator iterator = paymentItems.iterator(); iterator.hasNext();) {
					LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator
							.next();

					LlpPaymentFee fee = ((LlpPaymentFeeService) getService(LlpPaymentFeeService.class.getSimpleName()))
							.findById(llpPaymentTransactionDetail.getPaymentItem());

					if (llpPaymentTransactionDetail.getAmount() == 0) {
						double baseFee = fee.getPaymentFee() * llpPaymentTransactionDetail.getQuantity();
						llpPaymentTransactionDetail.setAmount(baseFee);
						if (Parameter.PAYMENT_GST_CODE_SR.equals(fee.getGstCode())) {
							double gstAmt = (baseFee * getGSTRate(fee.getGstCode()));
							double feeWithGst = baseFee + gstAmt;
							llpPaymentTransactionDetail.setAmount(feeWithGst);
							llpPaymentTransactionDetail.setGstAmt(gstAmt);
						}
					}
					llpPaymentTransactionDetail.setGstCode(fee.getGstCode());

					total = total.add(BigDecimal.valueOf(llpPaymentTransactionDetail.getAmount()));
					totalGst = totalGst.add(BigDecimal.valueOf(llpPaymentTransactionDetail.getGstAmt()));
//					System.out.println("total : " + total);
				}
			} else {
				ssmError(SSMExceptionParam.PAYMENT_ITEMS_INVALID);
			}
			BigDecimal totalWithoutGst = total.subtract(totalGst);

			SSMTextField payerName = new SSMTextField("payerName");
			payerName.setRequired(true);
			payerName.setLabelKey("paymentdetails.page.comtrac.payerName");
			add(payerName);

			SSMTextField payerId = new SSMTextField("payerId");
			payerId.setRequired(true);
			payerId.setLabelKey("paymentdetails.page.comtrac.payerId");
			add(payerId);

			SSMTextArea payerAddr = new SSMTextArea("payerAddr");
			payerAddr.setRequired(true);
			payerAddr.setLabelKey("paymentdetails.page.comtrac.payerAddr");
			add(payerAddr);

			add(new SSMLabel("totalWithoutGst", totalWithoutGst.doubleValue()));
			add(new SSMLabel("totalGst", totalGst.doubleValue()));
			add(new SSMLabel("amount", total.doubleValue()));
			add(new SSMLabel("totalAll", total.doubleValue()));

			add(new ListView<LlpPaymentTransactionDetail>("paymentItems", paymentItems) {
				public void populateItem(final ListItem<LlpPaymentTransactionDetail> item) {
					final LlpPaymentTransactionDetail paymentItem = item.getModelObject();
					// LlpPaymentFee fee = ((LlpPaymentFeeService)
					// getService(LlpPaymentFeeService.class
					// .getSimpleName())).findById(paymentItem
					// .getPaymentItem());
					// paymentItem.setAmount(fee.getPaymentFee());

					item.add(new SSMLabel("itemDesc", paymentItem.getPaymentItem(), Parameter.PAYMENT_TYPE));

					SSMLabel itemDesc = new SSMLabel("itemDet", paymentItem.getPaymentDet());
					if (StringUtils.isBlank(paymentItem.getPaymentDet())) {
						itemDesc.setVisible(false);
					}
					item.add(itemDesc);
					item.add(new SSMLabel("itemQuantity", String.valueOf(paymentItem.getQuantity())));
					item.add(new SSMLabel("itemAmountWoutGst", paymentItem.getAmountWithOutGst()));
					item.add(new SSMLabel("itemGstAmt", paymentItem.getGstAmt()));
					item.add(new SSMLabel("itemAmount", paymentItem.getAmount()));
				}
			});

			Button pay = new Button("pay") {
				@Override
				public void onSubmit() {
					LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) getForm().getModelObject();
					llpPaymentTransaction.setAppRefNo(appRefNo);
					Boolean noError = true;

					// check if previous payment ada status 'P', can't resubmit online payment
					List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService
							.findByAppRefNoStatus(appRefNo, Parameter.PAYMENT_STATUS_PENDING);
					if (listPayment.size() > 0) {
						noError = false;
						ssmError(SSMExceptionParam.PAYMENT_PENDING_EXIST);
					}

					// check status S
					LlpPaymentTransaction transactionSuccess = llpPaymentTransactionService
							.findSuccessByAppRefNo(appRefNo);
					if (transactionSuccess != null) {
						// redirect ke receipt page
						PageParameters paramToSend = new PageParameters();
						paramToSend.set("transId", transactionSuccess.getTransactionId());
						paramToSend.set("registrationFail", Parameter.YES_NO_no);

						SignInSession.get().removeAttribute("_paymentDetails");
						SignInSession.get().removeAttribute("_obj");
						SignInSession.get().removeAttribute("_transactionId");
						SignInSession.get().removeAttribute("_serviceName");
						noError = false;
						setResponsePage(PaymentReceiptPage.class, paramToSend);

					}

					if (noError) {
						try {

							llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName()))
									.processPayment(llpPaymentTransaction, paymentItems);

							if (StringUtils.isNotBlank(llpPaymentTransaction.getTransactionId())) {

								llpPaymentTransactionService.cancelAllOtc(appRefNo,
										llpPaymentTransaction.getTransactionId());
								updateStatus(obj, llpPaymentTransaction, "payOnline");

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
//					FeedbackPanel feedbackPanel =  ((PaymentDetailPage)getPage()).getFeedbackPanel();
//		        	feedbackPanel.getFeedbackMessages().clear();
//		        	target.add(feedbackPanel);
				}
			};
			add(pay);
			pay.setOutputMarkupId(true);

			Button payOtc = new Button("payOtc") {
				public void onSubmit() {
					LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) getForm().getModelObject();
					llpPaymentTransaction.setAppRefNo(appRefNo);
					llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_CASH);
					llpPaymentTransaction.setPaymentDetail(Parameter.PAYMENT_DETAIL_OTC);
					Boolean noError = true;

					// check if previous payment ada status 'P', can't resubmit online payment
					List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService
							.findByAppRefNoStatus(appRefNo, Parameter.PAYMENT_STATUS_PENDING);
					if (listPayment.size() > 0) {
						noError = false;
						ssmError(SSMExceptionParam.PAYMENT_PENDING_EXIST);
					}

					// check status S
					LlpPaymentTransaction transactionSuccess = llpPaymentTransactionService
							.findSuccessByAppRefNo(appRefNo);
					if (transactionSuccess != null) {
						// redirect ke receipt page
						PageParameters paramToSend = new PageParameters();
						paramToSend.set("transId", transactionSuccess.getTransactionId());
						paramToSend.set("registrationFail", Parameter.YES_NO_no);

						SignInSession.get().removeAttribute("_paymentDetails");
						SignInSession.get().removeAttribute("_obj");
						SignInSession.get().removeAttribute("_transactionId");
						SignInSession.get().removeAttribute("_serviceName");
						noError = false;
						setResponsePage(PaymentReceiptPage.class, paramToSend);
					}

					if (noError) {
						try {

							llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName()))
									.processPayment(llpPaymentTransaction, paymentItems);

							if (StringUtils.isNotBlank(llpPaymentTransaction.getTransactionId())) {
								llpPaymentTransactionService.cancelAllOtc(appRefNo,
										llpPaymentTransaction.getTransactionId());
								updateStatus(obj, llpPaymentTransaction, "payOtc");
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

				}
			};
			payOtc.setOutputMarkupPlaceholderTag(true);
			payOtc.setVisible(true);

			LlpParameters parameter = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG,
					Parameter.PAYMENT_CONFIG_ALLOW_OTC_PAYMENT);
			if (Parameter.YES_NO_no.equals(parameter.getCodeDesc())) {
				payOtc.setVisible(false);
			}
			add(payOtc);

			if (obj instanceof RobTrainingTransaction) {
				payOtc.setVisible(false);
			}
			if (obj instanceof RobTrainingReprintcert) {
				payOtc.setVisible(false);
			}

			add(new Button("cancel") {
				public void onSubmit() {
//					setResponsePage(ViewListLlpReservedNames.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "Payment Page";
	}
}
