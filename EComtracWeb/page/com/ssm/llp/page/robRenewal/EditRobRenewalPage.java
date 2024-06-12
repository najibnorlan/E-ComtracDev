package com.ssm.llp.page.robRenewal;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.base.common.code.CommonConstant;
import com.ssm.ezbiz.service.PaymentService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.MD5DigestUtils;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobIncentiveService;
import com.ssm.llp.mod1.service.RobUserOkuService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.ssm.webis.param.BizOwnerInfo;
import com.ssm.webis.param.BusinessInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobRenewalPage extends SecBasePage {

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "LlpPaymentReceiptService")
	private LlpPaymentReceiptService llpPaymentReceiptService;
	
	@SpringBean(name = "RobUserOkuService")
	private RobUserOkuService robUserOkuService;
	
	@SpringBean(name = "RobIncentiveService")
	private RobIncentiveService robIncentiveService;
	

	private double renewalFeeTrade = 0;
	private double renewalFeePersonal = 0;
	private double branchFee = 0;
	private DecimalFormat currencyDecFormat = new DecimalFormat("#0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String reason = null;
	private String reason2 = null;
	private double incentiveDiscount = 0;
	private double totalFeeDouble = 0;
	private String incentiveType = "";	
	private String incentiveAmtLbl="0";
	private DecimalFormat dfNegative = new DecimalFormat("-#0.00");
	private Boolean isOwnerAndRegOku = false;
	private Boolean canRenewA1Incentive = false;
	private String isLodgerOwner = "";
	private Boolean isLodgerOku = false;

	public EditRobRenewalPage(final BusinessInfo businesInfo) {
		branchFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES).getPaymentFee();
		renewalFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE).getPaymentFee();
		renewalFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL).getPaymentFee();

		Boolean noError = true;
		
		if(businesInfo!=null) {
		isLodgerOwner = checkIsLodgerOwner(businesInfo); // Y/N
		}
		
		if(isExistStatusPSPP(businesInfo.getBrNo())){
			noError = false;
		}
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobRenewal robRenewal = new RobRenewal();
				robRenewal.setBizName(businesInfo.getBizName());
				robRenewal.setBizType(businesInfo.getBizType());
				robRenewal.setBranchCount(businesInfo.getBranchCount());
				robRenewal.setBrNo(businesInfo.getBrNo());
				robRenewal.setChkDigit(businesInfo.getChkDigit());
				robRenewal.setCmpAmt(businesInfo.getCmpAmount());
				robRenewal.setBranchCount(businesInfo.getBranchCount());
				robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_DATA_ENTRY);
				if(robRenewal.getCmpAmt()>0){
					robRenewal.setPaidCmp(true);
				}
				try {
					robRenewal.setExpDate(sdf.parse(businesInfo.getEndDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				robRenewal.setYearRenew(1);
				
				return robRenewal;
			}
		}));
		init(noError);
	}

	public EditRobRenewalPage(final RobRenewal renewal) {
		branchFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES).getPaymentFee();
		renewalFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE).getPaymentFee();
		renewalFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL).getPaymentFee();
		
		Boolean isValid = true;
		//check data integrity for status pending payment
		if(Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT.equals(renewal.getStatus()) || Parameter.ROB_RENEWAL_STATUS_OTC.equals(renewal.getStatus())){
			isValid = isValidRenewal(renewal);
		}
		
		if(!isValid){
			
			LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findByAppRefNoStatusPaymentMode(renewal.getTransCode(), Parameter.PAYMENT_STATUS_PENDING, Parameter.PAYMENT_MODE_CASH);
			if(llpPaymentTransaction != null){
				llpPaymentTransaction.setStatus(Parameter.PAYMENT_STATUS_CANCEL);
				llpPaymentTransactionService.update(llpPaymentTransaction);
			}
			
			renewal.setStatus(Parameter.ROB_RENEWAL_STATUS_CANCEL);
			renewal.setApproveRejectNotes(reason + " " + reason2);
			renewal.setApproveRejectDt(new Date());
			robRenewalService.update(renewal);
		}
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return renewal;
			}
		}));
		init(isValid);
	}

	private void init(Boolean isValid) {
		add(new RobRenewalForm("form", getDefaultModel(), isValid));
	}

	private class RobRenewalForm extends Form implements Serializable {
		final LlpPaymentFee businessInfoPaymentFee;
		final LlpPaymentFee incentivePersonalOku;
		final LlpPaymentFee incentiveTradeOku;
		//final LlpPaymentFee incentiveBizInfoOku;
		
		RepeatingView listError;
		BusinessInfo bizInfo = null;
		final RobRenewal robRenewal;
		
		public RobRenewalForm(String id, IModel m, Boolean isValid) {
			super(id, m);
			
			final WebMarkupContainer errorDataInfoWmc = new WebMarkupContainer("errorDataInfoWmc");
			errorDataInfoWmc.setOutputMarkupId(true);
			errorDataInfoWmc.setOutputMarkupPlaceholderTag(true);
			errorDataInfoWmc.setVisible(false);
			add(errorDataInfoWmc);
			
			SSMLabel error = new SSMLabel("error", "");
			error.setOutputMarkupId(true);
			errorDataInfoWmc.add(error);
			
			if(!isValid){
				error.setDefaultModelObject(reason);
				errorDataInfoWmc.setVisible(true);
			}
			
			listError = new RepeatingView("listError");
			listError.setOutputMarkupId(true);
			listError.setOutputMarkupPlaceholderTag(true);
			listError.setVisible(false);

			WebMarkupContainer incentiveOkuWmc = new WebMarkupContainer("incentiveOkuWmc");
			incentiveOkuWmc.setOutputMarkupId(true);
			incentiveOkuWmc.setOutputMarkupPlaceholderTag(true);
			add(incentiveOkuWmc);
			
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			incentivePersonalOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU);
			incentiveTradeOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_OKU);
			
			robRenewal = (RobRenewal) m.getObject();
			
			if (Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())) {
				incentiveDiscount = incentiveTradeOku.getPaymentFee();
				incentiveType = Parameter.PAYMENT_INCENTIVE_TRADE_OKU;
			} else {
				incentiveDiscount = incentivePersonalOku.getPaymentFee();
				incentiveType = Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU;
			}
			
			int daysToAllowRenewBeforeEndDate = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG,
					Parameter.ROB_RENEWAL_CONFIG_DAYS_ALLOW_RENEW_BEFORE_EXPIRY));
			Calendar calBizExpired = Calendar.getInstance();
			calBizExpired.setTime(robRenewal.getExpDate());

			Calendar calDateNow = Calendar.getInstance();
			calDateNow.setTime(new Date());
			calDateNow.add(Calendar.DATE, daysToAllowRenewBeforeEndDate); //ie.only can renew with OKU Incentive within 30 DAYS from biz enddate.
			
			if (calBizExpired.before(calDateNow)) {
				canRenewA1Incentive = true;
			}
			
			//tier validations for performance - on form load.
			if(canRenewA1Incentive) { //1st tier -check within ie. 30 days expiry date..hide dropdown incentive.
				
				if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { 
				RobUserOku robUserOku = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getRobUserOku();
					if ((robUserOku!=null) && (Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus()))) {
						isLodgerOku = true;
					}
				}
				
				if ((!Parameter.YES_NO_no.equalsIgnoreCase(isLodgerOwner)) && (isLodgerOku)) { //additional filter for performance..if isLodgerOwner blank search owner under bizInfo.
				  bizInfo = getBusinessInfoWS(robRenewal);
					if(bizInfo!=null) {
						isOwnerAndRegOku = checkIsOwnerAndRegisteredOku(bizInfo); //2nd tier -check isOwner and All registered OKU..hide dropdown incentive.
					} 
						if (isOwnerAndRegOku) {
							listError = validateIncentiveOku(robRenewal,bizInfo); //3rd tier (final) -validate entitle incentive..enable dropdown incentive.
							errorDataInfoWmc.add(listError); 
						} 
				}
			}
			
			if(!Parameter.ROB_RENEWAL_STATUS_CANCEL.equals(robRenewal.getStatus()) &&  !Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus()) && !Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS.equals(robRenewal.getStatus())){
				recalculateRenewal(robRenewal);
			}
			

			SSMLabel transCode = new SSMLabel("transCode", robRenewal.getTransCode());
			add(transCode);
			
			SSMLabel brNo = new SSMLabel("brNo", robRenewal.getBrNo() + "-" + robRenewal.getChkDigit());
			add(brNo);

			SSMLabel bizName = new SSMLabel("bizName", robRenewal.getBizName());
			add(bizName);

			SSMLabel bizType = new SSMLabel("bizType", robRenewal.getBizType(), Parameter.ROB_BIZ_TYPE);
			add(bizType);
			
			SSMLabel expDate = new SSMLabel("expDate", robRenewal.getExpDate());
			add(expDate);
			
			SSMLabel updateDt = new SSMLabel("updateDt", robRenewal.getUpdateDt());
			add(updateDt);
			
			SSMLabel status = new SSMLabel("status", robRenewal.getStatus(), Parameter.ROB_RENEWAL_STATUS);
			add(status);
			
			SSMLabel newExpDate = new SSMLabel("newExpDate", robRenewal.getNewExpDate());
			add(newExpDate);

			SSMLabel totalBranch = new SSMLabel("branchCount", robRenewal.getBranchCount());
			add(totalBranch);
			
			if(Parameter.ROB_FORM_A1_INCENTIVE_oku.equals(robRenewal.getRenewalIncentive())){
				incentiveAmtLbl = dfNegative.format(incentiveDiscount) +" ("+incentiveType+")";
			}
			final SSMLabel incentiveAmt = new SSMLabel("incentiveAmt", incentiveAmtLbl);
			incentiveAmt.setOutputMarkupId(true);
			add(incentiveAmt);

			final SSMLabel total = new SSMLabel("totalAmt", robRenewal.getTotalAmt());
			total.setOutputMarkupId(true);
			add(total);

			SSMLabel cmpAmt = new SSMLabel("cmpAmt", robRenewal.getCmpAmt());
			add(cmpAmt);
			
			SSMLabel downloadRule = new SSMLabel("downloadRule", new StringResourceModel("page.title.mybiz.editNote", this, null));
			add(downloadRule);
			downloadRule.setVisible(false);
			
			
			SSMRadioChoice yearRenew = new SSMRadioChoice("yearRenew", Parameter.ROB_RENEWAL_YEAR);
			yearRenew.add(new AjaxFormChoiceComponentUpdatingBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getModelObject();
						robRenewal.setYearRenew(obj.getYearRenew());
						
						double totalFee = recalculateRenewal(robRenewal);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						arg0.add(total);
					}
				}
			});
			yearRenew.setOutputMarkupId(true);
			add(yearRenew);

			AjaxCheckBox isPaidCmp = new AjaxCheckBox("isPaidCmp", new PropertyModel<Boolean>(getDefaultModel(), "isPaidCmp")) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getForm().getModelObject();
						robRenewal.setPaidCmp(obj.isPaidCmp());

						double totalFee = recalculateRenewal(robRenewal);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						arg0.add(total);
					}

				}
			};

			isPaidCmp.setOutputMarkupId(true);
			add(isPaidCmp);
			
			SSMAjaxFormSubmitBehavior isBuyInfoOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget arg0) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getForm().getModelObject();
						robRenewal.setBuyInfo(obj.isBuyInfo());
						
						double totalFee = recalculateRenewal(obj);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						arg0.add(total);
					}
				}
			};
			
			SSMDropDownChoice isBuyInfo = new SSMDropDownChoice("isBuyInfo", Parameter.YES_NO);
			isBuyInfo.add(isBuyInfoOnchange);
			add(isBuyInfo);
			
			SSMTextField isBuyInfoDesc = new SSMTextField("isBuyInfoDesc", Model.of(""));
			isBuyInfoDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robRenewal.isBuyInfo()));
			isBuyInfoDesc.setVisible(false);
			isBuyInfoDesc.setReadOnly(true);
			add(isBuyInfoDesc);

			final Button saveButton = new Button("save") {
				public void onSubmit() {
					totalFeeDouble = recalculateRenewal(robRenewal);

					double renewalFee = 0;
					if (Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())) {
						renewalFee = renewalFeeTrade;
					} else {
						renewalFee = renewalFeePersonal;
					}
					double totalRenewalFee = robRenewal.getYearRenew() * renewalFee;
					double totalBranchFee = robRenewal.getBranchCount() * branchFee * robRenewal.getYearRenew();
					double totalCmpFee = robRenewal.getCmpAmt();


					if(StringUtils.isBlank(robRenewal.getTransCode())){
						robRenewal.setCreateBy(UserEnvironmentHelper.getLoginName());
						robRenewal.setCreateDt(new Date());
						robRenewalService.insert(robRenewal );
					}

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					if(Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())){
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE);
					}else{//PERSONAL
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL);
					}
					
					paymentItem.setQuantity(robRenewal.getYearRenew());
					paymentItem.setAmount(totalRenewalFee);
					paymentItem.setPaymentDet(robRenewal.getBrNo() + "-" + robRenewal.getChkDigit() + "\t"
							+ robRenewal.getBizName());
					listPaymentItems.add(paymentItem);

					if (robRenewal.getBranchCount() > 0) {
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES);
						paymentItem2.setQuantity(robRenewal.getYearRenew());
						paymentItem2.setPaymentDet(robRenewal.getBranchCount()+"");
						paymentItem2.setAmount(totalBranchFee);
						listPaymentItems.add(paymentItem2);
					}

					if (robRenewal.isPaidCmp() && robRenewal.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem3 = new LlpPaymentTransactionDetail();
						paymentItem3.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_COMPOUND);
						paymentItem3.setQuantity(1);
						paymentItem3.setPaymentDet(robRenewal.getBrNo() + "-" + robRenewal.getChkDigit());
						paymentItem3.setAmount(totalCmpFee);
						listPaymentItems.add(paymentItem3);
					}
					if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robRenewal.getBusinessInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}else{
						robRenewal.setBusinessInfoAmt(0);
					}
					
					//incentive OKU discount
					if(Parameter.ROB_FORM_A1_INCENTIVE_oku.equals(robRenewal.getRenewalIncentive())){
						LlpPaymentTransactionDetail paymentRegDisc = new LlpPaymentTransactionDetail();
						paymentRegDisc.setPaymentItem(incentiveType);
						paymentRegDisc.setQuantity(robRenewal.getYearRenew());
						paymentRegDisc.setAmount(Double.valueOf(dfNegative.format(incentiveDiscount)));
						paymentRegDisc.setPaymentDet("RENEWAL OF BUSINESS");
						listPaymentItems.add(paymentRegDisc);
					}
					
					//tambah utk fix gaf <null> pada kod gst (SR/OR)
					for (Iterator iterator = listPaymentItems.iterator(); iterator.hasNext();) {
						LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
						LlpPaymentFee fee = ((LlpPaymentFeeService) getService(LlpPaymentFeeService.class.getSimpleName()))
								.findById(llpPaymentTransactionDetail.getPaymentItem());
						
						llpPaymentTransactionDetail.setGstCode(fee.getGstCode());
					}
					
					if(totalFeeDouble==0) {
						setNoPaymentTransaction(listPaymentItems);
					}

					setResponsePage(new PaymentDetailPage(robRenewal.getTransCode(), RobRenewalService.class.getSimpleName(), robRenewal,
							listPaymentItems));
				}
			};
			add(saveButton);
			saveButton.setOutputMarkupId(true);
			saveButton.setEnabled(robRenewal.isDeclareChk());
			saveButton.setLabelKey("page.title.mybiz.payment");
			
			final Button cancelBtn = new Button("cancel") {
				public void onSubmit() {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						setResponsePage(ListRobRenewalPage.class);
					} else {
						setResponsePage(ListRobRenewalTransactionsPage.class);
					}
				}
			}.setDefaultFormProcessing(false);
			
			add(cancelBtn);
			cancelBtn.setOutputMarkupId(true);
			SignInSession signInSession = (SignInSession)getSession();
			if(null != getSession() && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
				cancelBtn.setVisible(false);
			}else {
				cancelBtn.setVisible(true);
			}			
			
			Button downloadCertificateButton = new Button("downloadCertificateButton") {
				public void onSubmit() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_CERT.pdf", robRenewalFull.getCertFileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
			};
			
			downloadCertificateButton.setVisible(false);
			add(downloadCertificateButton);
			
			Button downloadCmpButton = new Button("downloadCmpButton") {
				public void onSubmit() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_CMP.pdf", robRenewalFull.getCmpFileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
				
			};
			
			downloadCmpButton.setVisible(false);
			add(downloadCmpButton);
			
			Button downloadA1FormButton = new Button("downloadA1FormButton") {
				public void onSubmit() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_A1.pdf", robRenewalFull.getFormA1FileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
			};
			
			downloadA1FormButton.setVisible(false);
			add(downloadA1FormButton);
			
			Button downloadBusinessInfoFormButton = new Button("downloadBusinessInfoFormButton") {
				public void onSubmit() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_BUSINESS_INFO.pdf", robRenewalFull.getBusinessInfoData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
			};
			
			downloadBusinessInfoFormButton.setVisible(false);
			add(downloadBusinessInfoFormButton);
			
			
			final AjaxCheckBox declareChk= new AjaxCheckBox("declareChk", new PropertyModel(robRenewal, "declareChk")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						saveButton.setEnabled(true);
						robRenewal.setDeclareChk(true);
					} else {
						robRenewal.setDeclareChk(false);
						saveButton.setEnabled(false);
					}
					target.add(saveButton);
				}
			};
			declareChk.setVisible(true);
			add(declareChk);
			
			
			SSMAjaxFormSubmitBehavior renewalIncentiveOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getForm().getModelObject(); //user current input
						robRenewal.setRenewalIncentive(obj.getRenewalIncentive());
						
						double totalFee = recalculateRenewal(obj);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						target.add(total);
						
						listError.setVisible(false);
						saveButton.setOutputMarkupPlaceholderTag(true);
						saveButton.setVisible(true);
						declareChk.setOutputMarkupPlaceholderTag(true);
						declareChk.setVisible(true);
						errorDataInfoWmc.setVisible(false);
						
						incentiveAmtLbl = "0";
						if(Parameter.ROB_FORM_A1_INCENTIVE_oku.equals(obj.getRenewalIncentive())){
							incentiveAmtLbl = dfNegative.format(incentiveDiscount) +" ("+incentiveType+")";
							
							if(listError.size()>0) {
								String js = "$(\"html, body\").animate({ scrollTop: 0 }, 600);";
								listError.setVisible(true);
								saveButton.setVisible(false);
								declareChk.setVisible(false);
								errorDataInfoWmc.setVisible(true);
								errorDataInfoWmc.setEscapeModelStrings(true);
								target.appendJavaScript(js);
							}
						} incentiveAmt.setDefaultModelObject(incentiveAmtLbl);
						
						target.add(incentiveAmt);
						target.add(saveButton);
						target.add(declareChk);
						target.add(errorDataInfoWmc);
					}
				}
				
			};
			
			SSMDropDownChoice renewalIncentive =  new SSMDropDownChoice("renewalIncentive", Parameter.ROB_FORM_A1_INCENTIVE); //nama perlu sama data object..
			renewalIncentive.setLabelKey("page.lbl.ezbiz.robFormA1.incentive");
			
			SSMTextField renewalIncentiveDesc = new SSMTextField("renewalIncentiveDesc", Model.of(""));
			String renewalIncentiveVal = getCodeTypeWithValue(Parameter.ROB_FORM_A1_INCENTIVE, String.valueOf(Parameter.ROB_FORM_A1_INCENTIVE_no));
			if(robRenewal.getRenewalIncentive()!=null) {
				renewalIncentiveVal = getCodeTypeWithValue(Parameter.ROB_FORM_A1_INCENTIVE, String.valueOf(robRenewal.getRenewalIncentive()));
			}
			
			renewalIncentiveDesc.setDefaultModelObject(renewalIncentiveVal);
			renewalIncentiveDesc.setVisible(false);
			renewalIncentiveDesc.setReadOnly(true);
			renewalIncentiveDesc.setLabelKey("page.lbl.ezbiz.robFormA1.incentiveDesc");
			
			LlpParameters incentiveEnable = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG, Parameter.PAYMENT_CONFIG_ALLOW_INCENTIVE);
			
			if(Parameter.YES_NO_no.equals(incentiveEnable.getCodeDesc())){
				incentiveOkuWmc.setVisible(false);
				incentiveAmt.setVisible(false);
			}else{
				renewalIncentive.add(renewalIncentiveOnchange);
			}	
			
			incentiveOkuWmc.add(renewalIncentive);
			incentiveOkuWmc.add(renewalIncentiveDesc);

			if(!UserEnvironmentHelper.isInternalUser()) { //jika public
				if(!isOwnerAndRegOku) {
					incentiveOkuWmc.setVisible(false);
					incentiveAmt.setVisible(false);
				}
			}
			
			if (StringUtils.isNotBlank(robRenewal.getTransCode())) {
				yearRenew.setEnabled(false);
				isPaidCmp.setEnabled(false);
				isBuyInfo.setVisible(false);
				isBuyInfoDesc.setVisible(true);
				renewalIncentive.setVisible(false); //dropdown
				renewalIncentiveDesc.setVisible(true); //text
			} 
			if(Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus())){
				saveButton.setVisible(false);
				downloadRule.setVisible(true);
				declareChk.setVisible(false);
				
				int downValidDays = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG, Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS));
				Calendar cal = Calendar.getInstance();
				cal.setTime(robRenewal.getUpdateDt());
				cal.add(Calendar.DATE, downValidDays);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				String value = resolve("page.title.mybiz.editNote", sdf.format(cal.getTime()));
				SSMLabel ssmLabel = new SSMLabel("downloadRule", value);
				replace(ssmLabel);
				ssmLabel.setVisible(true);
				if(UserEnvironmentHelper.isInternalUser()){
					downloadA1FormButton.setVisible(true);
					downloadCertificateButton.setVisible(true);
					if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
						downloadBusinessInfoFormButton.setVisible(true);
					}
				}else{
					
					if(new Date().before(cal.getTime())){
						downloadCertificateButton.setVisible(true);
						if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
							downloadBusinessInfoFormButton.setVisible(true);
						}
					}
				}
				if(StringUtils.isNotBlank(robRenewal.getCmpNo())){
					downloadCmpButton.setVisible(true);
				}		
				
				
			}else if(Parameter.ROB_RENEWAL_STATUS_CANCEL.equals(robRenewal.getStatus())){
				saveButton.setVisible(false);
			} else{
				saveButton.setVisible(true);
				if(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS.equals(robRenewal.getStatus())){
					saveButton.setLabelKey("page.title.mybiz.resubmit");
				}
			}

			if(!isValid){
				yearRenew.setEnabled(false);
				isPaidCmp.setEnabled(false);
				isBuyInfo.setVisible(false);
				renewalIncentive.setVisible(false); //dropdown hide
				saveButton.setVisible(false);
			}
			setOutputMarkupId(true);
		}
		public double recalculateRenewal(RobRenewal robRenewal) {
			double totalAmt = 0;
			double gstAmt = 0;
			double renewalFee = 0;
			double businessInfoAmt = 0;
			if (Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())) {
				renewalFee = renewalFeeTrade;
			} else {
				renewalFee = renewalFeePersonal;
			}
			
			totalAmt = (robRenewal.getYearRenew() * renewalFee)
					+ (robRenewal.getBranchCount() * branchFee * robRenewal.getYearRenew());
			if (robRenewal.isPaidCmp()) {
				totalAmt += robRenewal.getCmpAmt();
			}
			
			if(Parameter.ROB_FORM_A1_INCENTIVE_oku.equals(robRenewal.getRenewalIncentive())){
				totalAmt -= incentiveDiscount;
			}
			
			if(Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())){
				totalAmt += businessInfoPaymentFee.getPaymentFee();
				
				if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
					totalAmt += businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
				businessInfoAmt = businessInfoPaymentFee.getPaymentFee()+gstAmt;
			}
			robRenewal.setBusinessInfoAmt(businessInfoAmt);
			robRenewal.setGstAmt(gstAmt);
			totalAmt = totalAmt * 1.00;
			robRenewal.setTotalAmt(totalAmt);
			return totalAmt;

		}
		
		
		private BusinessInfo getBusinessInfoWS(RobRenewal robRenewal) {
			try {
				BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(robRenewal.getBrNo(), robRenewal.getChkDigit());
				if (businessInfo!=null) {
					if(!(CommonConstant.NO.equals(businessInfo.getCanRenew()))){
						return businessInfo;
					  } 
					} 
				} catch (SSMException e) {
				error(e);
				}
			return null;
		}
		
		
		private RepeatingView validateIncentiveOku(RobRenewal robRenewal, BusinessInfo businessInfo) {
			listError.removeAll(); //clear
			
				BizOwnerInfo[] ownerList  = businessInfo.getListOwner();
				for (int i = 0; i < ownerList.length; i++) {
				String newIcNo = ownerList[i].getIcNo();
				String ownerName = ownerList[i].getVchname();
			
				//Check dalam table rob_incentive..check jika ada form A in process (A1 auto-approved)..incase ada partner lain submit formA incentive oku..
				String lastStatusAndAppRefNo = robIncentiveService.checkLastApplicationStatus(newIcNo, Parameter.ROB_FORM_A_INCENTIVE_oku, Parameter.ROB_FORM_TYPE_A);
				if((StringUtils.isNotBlank(lastStatusAndAppRefNo))) {
					String lastStatus = StringUtils.split(lastStatusAndAppRefNo,"_")[0];
					String formCode = StringUtils.split(lastStatusAndAppRefNo,"_")[1];
					if(Parameter.ROB_FORM_A_STATUS_IN_PROCESS.equalsIgnoreCase(lastStatus)||Parameter.ROB_FORM_A_STATUS_QUERY.equalsIgnoreCase(lastStatus)) {
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA1.incentive.oku.previousApplicationInProcess", ownerName, formCode, newIcNo)));
					}
				}
				
				//jika jumpa previous incentive (cari status brNo dan ownerLink di cbs guna webis)
				RobIncentive robIncentive = robIncentiveService.getLastIncentiveByNewIcNo(newIcNo, Parameter.ROB_FORM_A1_INCENTIVE_oku); //will return either approved A or A1
				if(robIncentive!=null) {
					try {
						BusinessInfo businessInfoPartner = robRenewalService.findBusinessByRegNoWS(robIncentive.getBrNo());
						
						if(businessInfoPartner!=null) { //renewalService only return active biz!..
						boolean canRenew = false;
						if(Parameter.CBS_BUSINESS_STATUS_EXPIRED.equals(businessInfoPartner.getBizStatus())){
							Calendar calBizExp = Calendar.getInstance();
						try {	
							calBizExp.setTime(sdf.parse(businessInfoPartner.getEndDate()));
						} catch (Exception e) {
							e.printStackTrace();
						}
							calBizExp.add(Calendar.MONTH, 12); //to check within 12 months still can renew.
							Calendar calNow = Calendar.getInstance();
							calNow.setTime(new Date());
							if (calNow.before(calBizExp)) {
								canRenew = true;
							}
						}
						
						//Jika existing business(lama) dah terminate atau luput melebihi 12 bulan, bole proceed incentive utk biz yg lain.
						if((Parameter.CBS_BUSINESS_STATUS_ACTIVE.equals(businessInfoPartner.getBizStatus()))||(canRenew)){
							
							//check business status OKU (all owner oku) atau tidak..
							boolean isAllOwnerOku = true;
							
							BizOwnerInfo[] activeOwnerOku = businessInfoPartner.getListOwner(); //jika oku add partner non-oku kat B4, kuota oku akan release semula.
							for (int m = 0; m < activeOwnerOku.length; m++) {
								//jika all owner OKU kuota tak release, jika ada non-oku kuota incentive release.
								RobUserOku robUserOkuExistingOwner = robUserOkuService.findOkuByIdTypeAndIdNo(Parameter.ID_TYPE_newic, activeOwnerOku[m].getIcNo());
								if (robUserOkuExistingOwner!=null) {
									if((Parameter.OKU_REGISTRATION_STATUS_WITHDRAW.equals(robUserOkuExistingOwner.getOkuRegStatus()))||
											(Parameter.OKU_REGISTRATION_STATUS_REJECT.equals(robUserOkuExistingOwner.getOkuRegStatus()))||
												(Parameter.OKU_REGISTRATION_STATUS_CANCEL.equals(robUserOkuExistingOwner.getOkuRegStatus()))) {
										isAllOwnerOku = false; //ada partner bukan OKU
										break; //found non-oku break loop..
									}
								}else {
									RobIncentive robIncentiveExistingOwner = robIncentiveService.getLastIncentiveByNewIcNo(activeOwnerOku[m].getIcNo(), Parameter.ROB_FORM_A_INCENTIVE_oku);
									if(robIncentiveExistingOwner==null) {
										isAllOwnerOku = false; //ada partner bukan oku (tak jumpa ic dalam data migration)
									}
								}
							}
							
						//validation form A1
							if(isAllOwnerOku) { //ALL owner oku
								String bizNameOku = businessInfoPartner.getBizName();
								if (!(robIncentive.getBrNo().equals(robRenewal.getBrNo()))){ //jika BizNo existing dlm robincentive tidak sama BizNo yg apply A1..
									listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA1.incentive.oku.bizStatusOkuAlreadyExist", 
											bizNameOku, robIncentive.getBrNo(), robIncentive.getCheckDigit(), robIncentive.getIdNo())));
								}
							}
					  } //if biz active can renew
					 } //if businessInfoPartner not null
					} catch (SSMException e) {
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.getBizProfileDetFailDueToWs")));
					}
				} //if robincentive not null
			  } //loop
				
			return listError;
		}
		
		private Boolean checkIsOwnerAndRegisteredOku(BusinessInfo bizInfo) {
			boolean isOwner=false;
			boolean isAllRegOku=true;
			
			try {
				LlpUserProfile llpUserProfile = new LlpUserProfile();
				if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { //external user(public) 
					llpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile(); //get profile from environment after public login
				}else {
					llpUserProfile = (LlpUserProfile) llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName()); //search from db if null or internal officer login etc
				}
				
				BizOwnerInfo[] ownerList  = bizInfo.getListOwner();
				for (int i = 0; i < ownerList.length; i++) {
					String newIcNo = ownerList[i].getIcNo();
					if(newIcNo.equals(llpUserProfile.getIdNo())) {
						isOwner = true;
					}
					RobUserOku robUserOku = robUserOkuService.findOkuByIdTypeAndIdNo(Parameter.ID_TYPE_newic, newIcNo);
					if (robUserOku!=null) {
						if(!(Parameter.USER_STATUS_active.equals(robUserOku.getUserProfile().getUserStatus()))&&
								!(Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus()))) {
							isAllRegOku=false;
						}
					}else {
						isAllRegOku=false;
					}
				}
				
				if(isOwner && isAllRegOku) {
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		private void setNoPaymentTransaction(List<LlpPaymentTransactionDetail> listPaymentItems) {
			//set robPaymentTransaction and robPaymentTransactionDetail..
			
			LlpPaymentTransaction llpPaymentTransaction = new LlpPaymentTransaction();
			LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robRenewal.getCreateBy());
			if (profile != null) {
				llpPaymentTransaction.setPayerName(profile.getName());
				llpPaymentTransaction.setPayerId(profile.getIdNo());
				String address = profile.getAdd1();
				if (StringUtils.isNotBlank(profile.getAdd2())) {
					address += "\n" + profile.getAdd2();
				}
				if (StringUtils.isNotBlank(profile.getAdd3())) {
					address += "\n" + profile.getAdd3();
				}
				address += "\n" + profile.getPostcode() + " " + profile.getCity();
				address = address + "\n" + getCodeTypeWithValue(Parameter.STATE_CODE, profile.getState());
				llpPaymentTransaction.setPayerAddr(address);
			}
			llpPaymentTransaction.setAppRefNo(robRenewal.getTransCode());
			llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_incentive);
			
			try {
				llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName())).processPayment(
						llpPaymentTransaction, listPaymentItems);
			} catch (SSMException e) {
				e.printStackTrace();
			}

			llpPaymentReceiptService.receivePaymentGenerateReceiptForCash(llpPaymentTransaction.getTransactionId(), null, totalFeeDouble,
					totalFeeDouble);
			
		}
		
	}
	
	public void generateDownload(String fileName, final byte[] byteData){
		
		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(byteData);
			}
		};

		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}
	
	private Boolean isValidRenewal(RobRenewal renewal){
		Boolean isValid = true;
		try {
			BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(renewal.getBrNo(), renewal.getChkDigit());
			
			if(businessInfo != null){
				if(CommonConstant.NO.equals(businessInfo.getCanRenew())){
					System.out.println("renewal transaction: " + renewal.getTransCode() + " cannot renew!");
					reason = resolve("error.renewal.datanotvalid");
					reason2 = "(blacklist)";
					return false;
				}
			}else{
				reason = resolve("error.renewal.datanotvalid");
				reason2 = "(renew after 12 months from expired date)";
				return false;
			}
			
			
			String transactionData = StringUtils.replace(sdf.format(renewal.getExpDate()) + renewal.getBizName() + renewal.getBizType() + renewal.getBranchCount(), " ", "");
			String businessData = StringUtils.replace(businessInfo.getEndDate() + businessInfo.getBizName() + businessInfo.getBizType() + renewal.getBranchCount(), " ", "");
			String md5TransactionData = MD5DigestUtils.encrypt(transactionData);
			String md5BusinessData = MD5DigestUtils.encrypt(businessData);
			
			System.out.println("ezbiz : " + transactionData + " | cbs : " + businessData);
			if(md5TransactionData.equals(md5BusinessData)){
				isValid = true;
			}else{
				reason = resolve("error.renewal.datanotvalid");
				reason2 = "(data not sync with cbs)";
				isValid = false;
			}
			
			System.out.println("renewal transaction: " + renewal.getTransCode() + " | isValidHashing? : " + isValid);
		}catch (SSMException e) {
			error(e);
		}
		
		return isValid;
		
	}
	
	private String checkIsLodgerOwner(BusinessInfo bizInfo) {
		isLodgerOwner = Parameter.YES_NO_no;
		try {
			LlpUserProfile llpUserProfile = new LlpUserProfile();
			if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { //external user(public) 
				llpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile(); //get profile from environment after public login
			}else {
				llpUserProfile = (LlpUserProfile) llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName()); //search from db if null or internal officer login etc
			}
			
			BizOwnerInfo[] ownerList  = bizInfo.getListOwner();
			for (int i = 0; i < ownerList.length; i++) {
				String newIcNo = ownerList[i].getIcNo();
				if(newIcNo.equals(llpUserProfile.getIdNo())) {
					isLodgerOwner = Parameter.YES_NO_yes;
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isLodgerOwner;
	}
	
	private Boolean isExistStatusPSPP(String brNo){
		
		String[] status = {Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT, Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS};
		SearchCriteria sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, brNo);
		sc = sc.andIfInNotNull("status", status, false);
		
		List<RobRenewal> renewalList = robRenewalService.findByCriteria(sc).getList();
		
		if(renewalList.size() > 0){
			reason = resolve("error.renewal.dataAlreadyExist");
			return true;
		}
		return false;
	}


	@Override
	public String getPageTitle() {
		return "page.title.mybiz.renewalDetail";
	}

}
