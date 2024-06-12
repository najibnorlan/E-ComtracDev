package com.ssm.ezbiz.robformA;


import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import com.ssm.base.common.util.DateUtil;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.page.AfterLoginLlp;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobIncentiveService;
import com.ssm.llp.mod1.service.RobUserOkuService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.ssm.webis.param.BizProfileDetResp;
import com.ssm.webis.param.BlacklistInfoResp;
import com.ssm.webis.param.BusinessFormAOwnerValidResp;
import com.ssm.webis.param.RobFormOwnerInfo;

@SuppressWarnings({ "rawtypes", "unchecked"})
public class EditRobFormAPage extends SecBasePage implements Serializable{

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;	
	
	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;

	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;
	
	@SpringBean(name = "LlpParametersService")
	public LlpParametersService parametersService;
	
	@SpringBean(name = "RobUserOkuService")
	private RobUserOkuService robUserOkuService;
	
	@SpringBean(name = "RobIncentiveService")
	private RobIncentiveService robIncentiveService;
	public EditRobFormAPage(final String robFormARefNo) {
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormA robFormA = robFormAService.findAllById(robFormARefNo);
				return robFormA;
			}
		}));
		init();
		
	}

	public EditRobFormAPage() {
//		branchFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES).getPaymentFee();
//		renewalFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE).getPaymentFee();
//		renewalFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL).getPaymentFee();

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormA robFormA = new RobFormA();
				robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY);
				robFormA.setBizRegPeriod((short) 1);
				robFormA.setBizStartDt(new Date());
				//Prepare Branches

				//Prepare Owners
				LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
				RobFormAOwner robFormAOwner = new RobFormAOwner();
				robFormAOwner.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
				copyFromUserProfileToRobOwner(llpUserProfile, robFormAOwner, true);
				robFormA.getListRobFormAOwner().add(robFormAOwner);
				LocalDate birthdate = new LocalDate(robFormAOwner.getDob());
				Years age = Years.yearsBetween(birthdate, new LocalDate());
				if(age.getYears()<18){
					storeErrorMsgKey("page.lbl.ezbiz.robFormA.robFormAOwners.ownerBelow18",robFormAOwner.getName());
					setResponsePage(AfterLoginLlp.class);
				}
				return robFormA;
			}
		}));
		init();
	}

	private void init() {
		add(new RobFormAForm("form", getDefaultModel()));
	}


	private class RobFormAForm extends Form implements Serializable {
		final WebMarkupContainer wmcFormCode;
		final WebMarkupContainer wmcAddress;
		final WebMarkupContainer wmcBranches;
		final WebMarkupContainer wmcOwners;
		final WebMarkupContainer wmcBizCodeAll;
		final WebMarkupContainer wmcFeeSummaryAll;
		final SSMLabel incentiveTypeLabel;
		final SSMLabel regFeePerYear;
		final SSMLabel regFeeDiscount;
		final SSMLabel totalRegDiscount;
		final SSMLabel regFeeDurationDiscount;
		final SSMLabel businessInfoDiscount;
		final SSMLabel totalBusinessInfoDiscount;
		final SSMLabel businessInfoQuantityDiscount;
		final SSMLabel regFeeDuration;
		final SSMLabel totalRegFee;
		final SSMLabel branchFee;
		final SSMLabel branchFeeDuration;
		final SSMLabel branchFeePerYear;
		final SSMLabel totalBranchFee;
		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		final SSMLabel totalFee;
		final SSMLabel summaryError;
		final SSMLabel bizStartDtWarning;
		final SSMLabel bizNameWarning;
		final SSMLabel uploadErrorLabel;
		final SSMTextField robFormACode;
		final SSMTextArea bizDesc;
		final RepeatingView listError;
		int currentShowId = 0;
		final RobFormA robFormA;
		final SSMAjaxButton previewToPayment,reLodgeFormA, submitVerification ;
		final SSMAjaxCheckBox declarationChkBox;
		final SSMTextField bizName;
		final LlpUserProfile currentLlpUserProfile;
		boolean isQuery = false;
		final SSMTextArea queryAnswer;
		final LlpPaymentFee branchPaymentFee;
		final LlpPaymentFee formAPaymentFeeTrade;
		final LlpPaymentFee formAPaymentFeePersonal;
		final LlpPaymentFee businessInfoPaymentFee;
		final LlpPaymentFee incentivePersonalStudent;
		final LlpPaymentFee incentiveTradeStudent;
		final LlpPaymentFee incentivePersonalOku;
		final LlpPaymentFee incentiveTradeOku;
		final LlpPaymentFee incentiveBizInfoStudent;
		final LlpPaymentFee incentiveBizInfoOku;
		//final SSMTextField formAfileUpload;
		
		final SSMAjaxButton removeSupportingDoc;
		final SSMDownloadLink downloadSupportingDoc;
		
		public RobFormAForm(String id, IModel m) {
			super(id, m);
			boolean isNew = true;
			setAutoCompleteForm(false);
			
			branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
			formAPaymentFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
			formAPaymentFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			incentivePersonalStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT);
			incentiveTradeStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT);
			incentivePersonalOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU);
			incentiveTradeOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_OKU);
			incentiveBizInfoStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT);
			incentiveBizInfoOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_OKU);
			
			currentLlpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			String prefixLabelKey = "page.lbl.ezbiz.robFormA."; //gabung label dgn bawah dan store dlm db full. cth: page.lbl.ezbiz.robFormA.bizMainAddrUrl.. refer juga MarkupContainer utk semanticJS form validation.
			setPrefixLabelKey(prefixLabelKey);
			
			robFormA = (RobFormA) m.getObject();
			
			
			SSMDownloadLink gazettedLink = new SSMDownloadLink("gazettedLink", "GAZETTED_NAME");
			add(gazettedLink);
			
			
			SSMTextArea notes = new SSMTextArea("notes",Model.of(""));
			notes.setVisible(false);
			notes.setReadOnly(true);
			add(notes);
			
			if(Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())){
				if(Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormA.getStatus())){
					isQuery = true;
				}
				RobFormNotes formNotes = robFormA.getListRobFormNotes().get(robFormA.getListRobFormNotes().size()-1);
				notes.setDefaultModelObject(formNotes.getNotes());
				notes.setVisible(true);
				if(StringUtils.isNotBlank(formNotes.getNotesAnswer())){
					robFormA.setQueryAnswer(formNotes.getNotesAnswer());
				}
			}
			
			SSMTextField status = new SSMTextField("status", Model.of(""));
			status.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_FORM_A_STATUS, robFormA.getStatus()));
			status.setReadOnly(true);
			add(status);
			
			
			
			wmcFormCode = new WebMarkupContainer("wmcFormCode");
			wmcFormCode.setOutputMarkupId(true);
			wmcFormCode.setOutputMarkupPlaceholderTag(true);
			wmcFormCode.setPrefixLabelKey(prefixLabelKey);
			add(wmcFormCode);
			
			robFormACode = new SSMTextField("robFormACode");
			robFormACode.setReadOnly(true);
			robFormACode.setOutputMarkupId(true);
			wmcFormCode.add(robFormACode);
			
			
			if(StringUtils.isBlank(robFormA.getRobFormACode())){
				wmcFormCode.setVisible(false);
			}else{
				wmcFormCode.setVisible(true);
			}
			
			final Label notyJs = new Label("notyJs", Model.of(""));
			notyJs.setEscapeModelStrings(false);
			notyJs.setOutputMarkupId(true);
			notyJs.setOutputMarkupPlaceholderTag(true);
			add(notyJs);
			
			SSMAjaxFormSubmitBehavior bizNameOnchange = new SSMAjaxFormSubmitBehavior("onblur", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					String alert = "";
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
//					System.out.println(">>"+robFormAForm.getBizName()+"<<");
					Boolean noError = true;
					
//					bizNameWarning.setDefaultModelObject("");
					if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
//						List<SSMException> listException = robFormAService.validateName(robFormAForm.getBizName());
//						if(listException.size()>0){
//							String errorDesc = "";
//							for (int i = 0; i < listException.size(); i++) {
//								listError.add(new SSMLabel(listError.newChildId() ,listException.get(i).getMessage()));
//								errorDesc+=listException.get(i).getMessage()+"\n";
//							}
////								resolve("page.lbl.ezbiz.robFormA.nameInvalid", robFormAForm.getBizName())
//							System.out.println(errorDesc);
//							if(StringUtils.isNotBlank(errorDesc)){
//								bizNameWarning.setDefaultModelObject(errorDesc+"</br>");
////								alert = generateNotyAlert( "reservedName.page.symbol", Parameter.ALERT_TYPE_error, target);
//							}
//							noError = false;
//						}
						
					}else{
						if(!currentLlpUserProfile.getName().equals(robFormAForm.getBizName())){
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.personalMustUserLoggerName", robFormAForm.getBizName())));
							alert = generateNotyAlert(resolve("page.lbl.ezbiz.robFormA.personalMustUserLoggerName", robFormAForm.getBizName()), Parameter.ALERT_TYPE_error, target);
							noError = false;
						}
					}
					
					if(noError){
						recalculateFee(target, robFormAForm);
					}
					
					notyJs.setDefaultModelObject(alert);
					if(target == null){
						add(notyJs);
					}else{
						replace(notyJs);
						target.add(notyJs);
					}
					target.add(bizNameWarning);
				}
			};
			
			bizName = new SSMTextField("bizName");
			bizName.add(bizNameOnchange);
			bizName.setShouldTrim(false);
			add(bizName);
//			
			final WebMarkupContainer dateWmc = new WebMarkupContainer("dateWmc");
			dateWmc.setOutputMarkupId(true);
			dateWmc.setOutputMarkupPlaceholderTag(true);
			dateWmc.setPrefixLabelKey(prefixLabelKey);
			add(dateWmc);
			
			final WebMarkupContainer dummyDateWmc = new WebMarkupContainer("dummyDateWmc");
			dummyDateWmc.setOutputMarkupId(true);
			dummyDateWmc.setVisible(false);
			dummyDateWmc.setOutputMarkupPlaceholderTag(true);
			dummyDateWmc.setPrefixLabelKey(prefixLabelKey);
			add(dummyDateWmc);
			
			final SSMDateTextField bizPartnershipAgreementDate = new SSMDateTextField("bizPartnershipAgreementDate");
			bizPartnershipAgreementDate.setOutputMarkupId(true);
			bizPartnershipAgreementDate.setOutputMarkupPlaceholderTag(true);
			dateWmc.add(bizPartnershipAgreementDate);
			
			final SSMTextField dateDummy = new SSMTextField("dateDummy", new PropertyModel(" ", ""));
			dateDummy.setReadOnly(true);
			dummyDateWmc.add(dateDummy);
			
			SSMAjaxFormSubmitBehavior nameTypeOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					if(Parameter.ROB_NAME_TYPE_PERSONAL.equals(robFormAForm.getNameType())){
						bizName.setDefaultModelObject(currentLlpUserProfile.getName());
						bizName.setReadOnly(true);
						robFormAForm.setBizPartnershipAgreementDate(null);
						robFormA.setBizPartnershipAgreementDate(null);
						dateWmc.setVisible(false);
						dummyDateWmc.setVisible(true);
					}else{
						bizName.setReadOnly(false);
						bizName.setDefaultModelObject("");
						dateWmc.setVisible(true);
						dummyDateWmc.setVisible(false);
					}
					target.add(bizName);
					target.add(dateWmc);
					target.add(dummyDateWmc);
					recalculateFee(target, robFormAForm);
				}
			};
			
			SSMAjaxFormSubmitBehavior bizRegPeriodOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					recalculateFee(target, robFormAForm);
				}
			};
			
			SSMTextField nameTypeDesc = new SSMTextField("nameTypeDesc", Model.of(""));
			nameTypeDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_NAME_TYPE, robFormA.getNameType()));
			nameTypeDesc.setVisible(false);
			nameTypeDesc.setReadOnly(true);
			add(nameTypeDesc);
			
			SSMDropDownChoice nameType = new SSMDropDownChoice("nameType", Parameter.ROB_NAME_TYPE);
			if(isQuery){
				nameType.setVisible(false);
				nameTypeDesc.setVisible(true);
			}else{
				nameType.add(nameTypeOnchange);
			}
			add(nameType);
			
			
			SSMTextField isBuyInfoDesc = new SSMTextField("isBuyInfoDesc", Model.of(""));
			isBuyInfoDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormA.isBuyInfo()));
			isBuyInfoDesc.setVisible(false);
			isBuyInfoDesc.setReadOnly(true);
			add(isBuyInfoDesc);
			
			SSMAjaxFormSubmitBehavior isBuyInfoOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					recalculateFee(target, robFormAForm);
				}
			};
			
			SSMDropDownChoice isBuyInfo = new SSMDropDownChoice("isBuyInfo", Parameter.YES_NO);
			isBuyInfo.add(isBuyInfoOnchange);
			if(isQuery){
				isBuyInfo.setVisible(false);
				isBuyInfoDesc.setVisible(true);
			}
			add(isBuyInfo);
			
			SSMAjaxFormSubmitBehavior bizStartDtOnBlur = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					recalculateFee(target, robFormAForm);
				}
			};
			
			String msg = "";
//			if(robFormA.getBizStartDt()!=null && robFormA.getBizStartDt().before(c30DayBefore.getTime())){
//				msg = bizDateCompoundAlert;
//			}
			bizStartDtWarning= new SSMLabel("bizStartDtWarning",msg);
			bizStartDtWarning.setOutputMarkupId(true);
			bizStartDtWarning.setOutputMarkupPlaceholderTag(true);
			bizStartDtWarning.setEscapeModelStrings(false);
			add(bizStartDtWarning);
			
			bizNameWarning= new SSMLabel("bizNameWarning","");
			bizNameWarning.setOutputMarkupId(true);
			bizNameWarning.setOutputMarkupPlaceholderTag(true);
			bizNameWarning.setEscapeModelStrings(false);
			add(bizNameWarning);
			
			
			SSMDateTextField bizStartDt = new SSMDateTextField("bizStartDt");
			bizStartDt.add(bizStartDtOnBlur);
			add(bizStartDt);
			

			final SSMDropDownChoice bizRegPeriod = new SSMDropDownChoice("bizRegPeriod", Parameter.ROB_RENEWAL_YEAR);
			add(bizRegPeriod);
			
			
			SSMTextField bizRegPeriodDesc = new SSMTextField("bizRegPeriodDesc", Model.of(""));
			bizRegPeriodDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_RENEWAL_YEAR, String.valueOf(robFormA.getBizRegPeriod())));
			bizRegPeriodDesc.setVisible(false);
			bizRegPeriodDesc.setReadOnly(true);
			add(bizRegPeriodDesc);
			
			if(isQuery){
				bizRegPeriod.setVisible(false);
				bizRegPeriodDesc.setVisible(true);
			}else{
				bizRegPeriod.add(bizRegPeriodOnchange);
			}
			
			wmcAddress = new WebMarkupContainer("wmcAddress");
			wmcAddress.setPrefixLabelKey(prefixLabelKey);
			wmcAddress.setOutputMarkupId(true);
			wmcAddress.setOutputMarkupPlaceholderTag(true);
			add(wmcAddress);
			
			final SSMTextField bizMainAddr = new SSMTextField("bizMainAddr");
			wmcAddress.add(bizMainAddr);
			final SSMTextField bizMainAddr2 = new SSMTextField("bizMainAddr2");
			bizMainAddr2.setNoLabel();
			wmcAddress.add(bizMainAddr2);
			final SSMTextField bizMainAddr3 = new SSMTextField("bizMainAddr3");
			wmcAddress.add(bizMainAddr3);
			bizMainAddr3.setNoLabel();
			final SSMTextField bizMainAddrPostcode= new SSMTextField("bizMainAddrPostcode");
			wmcAddress.add(bizMainAddrPostcode);

//			final SSMTextField bizMainAddrTown= new SSMTextField("bizMainAddrTown");
//			wmcAddress.add(bizMainAddrTown);
			
//			final SSMDropDownChoice bizMainAddrState = new SSMDropDownChoice("bizMainAddrState", Parameter.ROB_ALLOW_REG_STATE);
//			bizMainAddrState.setNoLabel();
//			wmcAddress.add(bizMainAddrState);
			
			WicketUtils.generatePostcodeTownState(wmcAddress, bizMainAddrPostcode, robFormA, "bizMainAddrPostcode", "bizMainAddrTown", "bizMainAddrState", true  );
			
			
			final SSMTextField bizMainAddrTelNo= new SSMTextField("bizMainAddrTelNo");
			wmcAddress.add(bizMainAddrTelNo);
			final SSMTextField bizMainAddrMobileNo= new SSMTextField("bizMainAddrMobileNo");
			wmcAddress.add(bizMainAddrMobileNo);
			final SSMTextField bizMainAddrEmail= new SSMTextField("bizMainAddrEmail");
			bizMainAddrEmail.setUpperCase(false);
			wmcAddress.add(bizMainAddrEmail);
			final SSMTextField bizMainAddrUrl = new SSMTextField("bizMainAddrUrl");
			bizMainAddrUrl.setUpperCase(false);
			wmcAddress.add(bizMainAddrUrl);
			
			final SSMTextField bizPostAddr= new SSMTextField("bizPostAddr");
			wmcAddress.add(bizPostAddr);
			final SSMTextField bizPostAddr2= new SSMTextField("bizPostAddr2");
			bizPostAddr2.setNoLabel();
			wmcAddress.add(bizPostAddr2);
			final SSMTextField bizPostAddr3= new SSMTextField("bizPostAddr3");
			bizPostAddr3.setNoLabel();
			wmcAddress.add(bizPostAddr3);
//			final SSMTextField bizPostAddrTown= new SSMTextField("bizPostAddrTown");
//			wmcAddress.add(bizPostAddrTown);
//			
//			final SSMDropDownChoice bizPostAddrState = new SSMDropDownChoice("bizPostAddrState", Parameter.CBS_ROB_STATE);
//			wmcAddress.add(bizPostAddrState);
			
			final SSMTextField bizPostAddrPostcode= new SSMTextField("bizPostAddrPostcode");
			wmcAddress.add(bizPostAddrPostcode);
			
			WicketUtils.generatePostcodeTownState(wmcAddress, bizPostAddrPostcode, robFormA, "bizPostAddrPostcode", "bizPostAddrTown", "bizPostAddrState"  );
			
			
			
			
			final SSMTextField bizPostAddrTelNo= new SSMTextField("bizPostAddrTelNo");
			wmcAddress.add(bizPostAddrTelNo);
			final SSMTextField bizPostAddrMobileNo= new SSMTextField("bizPostAddrMobileNo");
			wmcAddress.add(bizPostAddrMobileNo);
			final SSMTextField bizPostAddrEmail= new SSMTextField("bizPostAddrEmail");
			bizPostAddrEmail.setUpperCase(false);
			wmcAddress.add(bizPostAddrEmail);
			
			
			SSMAjaxButton copyFromMainAddr = new SSMAjaxButton("copyFromMainAddr") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					bizPostAddr.setDefaultModelObject(bizMainAddr.getValue());
					bizPostAddr2.setDefaultModelObject(bizMainAddr2.getValue());
					bizPostAddr3.setDefaultModelObject(bizMainAddr3.getValue());
					
					SSMDropDownChoice bizMainAddrTownTmp = (SSMDropDownChoice) form.get("wmcAddress:bizMainAddrTownTmp");
					SSMTextField bizMainAddrStateDesc = (SSMTextField) form.get("wmcAddress:bizMainAddrStateDesc");
					SSMDropDownChoice bizPostAddrTownTmp = (SSMDropDownChoice) form.get("wmcAddress:bizPostAddrTownTmp");
					SSMTextField bizPostAddrStateDesc = (SSMTextField) form.get("wmcAddress:bizPostAddrStateDesc");
					
					bizPostAddrPostcode.setDefaultModelObject(bizMainAddrPostcode.getValue());
					bizPostAddrTownTmp.resetChild(bizMainAddrTownTmp.getListChild());
					bizPostAddrTownTmp.setDefaultModelObject(bizMainAddrTownTmp.getValue());
					bizPostAddrStateDesc.setDefaultModelObject(bizMainAddrStateDesc.getValue());
					
					bizPostAddrTelNo.setDefaultModelObject(bizMainAddrTelNo.getValue());
					bizPostAddrMobileNo.setDefaultModelObject(bizMainAddrMobileNo.getValue());
					bizPostAddrEmail.setDefaultModelObject(bizMainAddrEmail.getValue());
					
					
					target.add(wmcAddress);
				}
			};
			copyFromMainAddr.setDefaultFormProcessing(false);
			wmcAddress.add(copyFromMainAddr);
			
			SSMAjaxButton copyFromProfileAddr = new SSMAjaxButton("copyFromProfileAddr") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					List<LlpParameters> listParameters = parametersService.findListTownByPostcode(currentLlpUserProfile.getPostcode() , true);
					
					
					String cbsState = getCodeTypeWithValue(Parameter.STATE_CODE_TO_CBS_CODE, currentLlpUserProfile.getState());
					String stateDesc = getCodeTypeWithValue(Parameter.CBS_ROB_STATE, cbsState);
					
					
					String postTownStateCode = currentLlpUserProfile.getPostcode()+":"+currentLlpUserProfile.getCity().toUpperCase()+":"+cbsState;
					
					
					bizMainAddr.setDefaultModelObject(currentLlpUserProfile.getAdd1());
					bizMainAddr2.setDefaultModelObject(currentLlpUserProfile.getAdd2());
					bizMainAddr3.setDefaultModelObject(currentLlpUserProfile.getAdd3());
					bizMainAddrPostcode.setDefaultModelObject(currentLlpUserProfile.getPostcode());
					bizMainAddrMobileNo.setDefaultModelObject(currentLlpUserProfile.getHpNo());

					
					
					SSMDropDownChoice bizMainAddrTownTmp =  (SSMDropDownChoice) form.get("wmcAddress:bizMainAddrTownTmp");
					bizMainAddrTownTmp.resetChild(listParameters);
					bizMainAddrTownTmp.setDefaultModelObject(postTownStateCode);
					
					boolean townExist = false;
					for (int i = 0; i < listParameters.size(); i++) {
						if(listParameters.get(i).getCode().equals(postTownStateCode)){
							townExist = true;
							break;
						}
					}
					if(!townExist){
						stateDesc = "";
					}
					form.get("wmcAddress:bizMainAddrStateDesc").setDefaultModelObject(stateDesc);
					robFormA.setBizMainAddrState(stateDesc);
					
					target.add(wmcAddress);
				}
			};
			copyFromProfileAddr.setDefaultFormProcessing(false);
			wmcAddress.add(copyFromProfileAddr);
		
			SSMTextField isIncubatorDesc = new SSMTextField("isIncubatorDesc", Model.of(""));
			isIncubatorDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormA.isIncubator()));
			isIncubatorDesc.setVisible(false);
			isIncubatorDesc.setReadOnly(true);
			wmcAddress.add(isIncubatorDesc);
			
//			final SSMTextField incubaterStateDesc = new SSMTextField("incubaterStateDesc", new PropertyModel(getCodeTypeWithValue(Parameter.CBS_ROB_STATE, "B"), ""));
//			incubaterStateDesc.setVisible(false);
//			incubaterStateDesc.setReadOnly(true);
//			incubaterStateDesc.setLabelReq(false);
//			wmcAddress.add(incubaterStateDesc);
			
			
			SSMAjaxFormSubmitBehavior incentiveOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Form<?> form = getForm();
					RobFormA robFormAForm = (RobFormA) form.getDefaultModelObject();
					if(robFormAForm.getIncentive() != null){
						if(robFormAForm.getRobFormACode() != null){
							updateAll(target,form,"");
						}else{
							recalculateFee(target, robFormAForm);
						}
					}
				}
			};
			
			SSMDropDownChoice incentive =  new SSMDropDownChoice("incentive", Parameter.ROB_FORM_A_INCENTIVE);
			incentive.setLabelKey("page.lbl.ezbiz.robFormA.incentive");
			add(incentive);
			
			SSMTextField incentiveDesc = new SSMTextField("incentiveDesc", Model.of(""));
			String incentiveVal = getCodeTypeWithValue(Parameter.ROB_FORM_A_INCENTIVE, String.valueOf(Parameter.ROB_FORM_A_INCENTIVE_no));
			if(robFormA.getIncentive() != null){
				incentiveVal = getCodeTypeWithValue(Parameter.ROB_FORM_A_INCENTIVE, String.valueOf(robFormA.getIncentive()));
			}
			add(incentiveDesc);
			
			incentiveDesc.setDefaultModelObject(incentiveVal);
			incentiveDesc.setVisible(false);
			incentiveDesc.setReadOnly(true);
			incentiveDesc.setLabelKey("page.lbl.ezbiz.robFormA.incentiveDesc");
			
			LlpParameters incentiveEnable = llpParametersService.findParameter(Parameter.PAYMENT_CONFIG, Parameter.PAYMENT_CONFIG_ALLOW_INCENTIVE);
			
			if(Parameter.YES_NO_no.equals(incentiveEnable.getCodeDesc())){
				incentive.setVisible(false);
				incentiveDesc.setVisible(false);
			}else{
				if(isQuery){
					incentive.setVisible(false);
					incentiveDesc.setVisible(true);
				}else{
					incentive.add(incentiveOnchange);
				}
			}
			
			if(Parameter.YES_NO_yes.equals(robFormA.isIncubator())){
				bizMainAddr.setReadOnly(true);
				bizMainAddr2.setReadOnly(true);
				bizMainAddr3.setReadOnly(true);
				bizMainAddrPostcode.setReadOnly(true);
//				incubaterStateDesc.setVisible(true);
			}
			
			final SSMDropDownChoice isIncubator = new SSMDropDownChoice("isIncubator", Parameter.YES_NO);
			SSMAjaxFormSubmitBehavior isIncubatorOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					
					
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					if(Parameter.YES_NO_yes.equals(isIncubator.getValue())){
						
						String addr1 = resolve("page.lbl.ezbiz.robFormA.incubatorAddr1");
						String addr2 = resolve("page.lbl.ezbiz.robFormA.incubatorAddr2");
						String addr3 = resolve("page.lbl.ezbiz.robFormA.incubatorAddr3");
						String postcode = resolve("page.lbl.ezbiz.robFormA.incubatorPostcode");
						String state = resolve("page.lbl.ezbiz.robFormA.incubatorState");
						String town = resolve("page.lbl.ezbiz.robFormA.incubatorTown");
						
						List listParameters = parametersService.findListTownByPostcode(postcode , true);
						
						String postTownStateCode = postcode+":"+town.toUpperCase()+":"+state;
						
						bizMainAddr.setDefaultModelObject(addr1);
						bizMainAddr2.setDefaultModelObject(addr2);
						bizMainAddr3.setDefaultModelObject(addr3);
						bizMainAddrPostcode.setDefaultModelObject(postcode);

						
						SSMDropDownChoice bizMainAddrTownTmp = (SSMDropDownChoice) get("wmcAddress:bizMainAddrTownTmp");
						bizMainAddrTownTmp.resetChild(listParameters);
						bizMainAddrTownTmp.setDefaultModelObject(postTownStateCode);
						get("wmcAddress:bizMainAddrStateDesc").setDefaultModelObject(getCodeTypeWithValue(Parameter.CBS_ROB_STATE, state));
						
						
						
						robFormA.setBizMainAddrState(resolve("page.lbl.ezbiz.robFormA.incubatorState"));
						robFormAForm.setBizMainAddrState(resolve("page.lbl.ezbiz.robFormA.incubatorState"));
						
						bizMainAddr.setReadOnly(true);
						bizMainAddr2.setReadOnly(true);
						bizMainAddr3.setReadOnly(true);
						bizMainAddrPostcode.setReadOnly(true);
						
					}else{
						bizMainAddr.setDefaultModelObject("");
						bizMainAddr2.setDefaultModelObject("");
						bizMainAddr3.setDefaultModelObject("");
						bizMainAddrPostcode.setDefaultModelObject("");

						
						SSMDropDownChoice bizMainAddrTownTmp = (SSMDropDownChoice) get("wmcAddress:bizMainAddrTownTmp");
						bizMainAddrTownTmp.resetChild(new ArrayList());
						bizMainAddrTownTmp.setDefaultModelObject("");
						get("wmcAddress:bizMainAddrStateDesc").setDefaultModelObject("");
						
						
						
						bizMainAddr.setReadOnly(false);
						bizMainAddr2.setReadOnly(false);
						bizMainAddr3.setReadOnly(false);
						bizMainAddrPostcode.setReadOnly(false);
					}
//						else{
//						if(robFormA.getBizMainAddr() != null){
//							bizMainAddr.setDefaultModelObject(robFormA.getBizMainAddr());
//							bizPostAddr.setDefaultModelObject(robFormA.getBizPostAddr());
//						}else{
//							bizMainAddr.setDefaultModelObject("");
//							bizPostAddr.setDefaultModelObject("");
//						}
//						
//						if(robFormA.getBizMainAddr2() != null){
//							bizMainAddr2.setDefaultModelObject(robFormA.getBizMainAddr2());
//							bizPostAddr2.setDefaultModelObject(robFormA.getBizPostAddr2());
//						}else{
//							bizMainAddr2.setDefaultModelObject("");
//							bizPostAddr2.setDefaultModelObject("");
//						}
//						
//						if(robFormA.getBizMainAddr3() != null){
//							bizMainAddr3.setDefaultModelObject(robFormA.getBizMainAddr3());
//							bizPostAddr3.setDefaultModelObject(robFormA.getBizPostAddr3());
//						}else{
//							bizMainAddr3.setDefaultModelObject("");
//							bizPostAddr3.setDefaultModelObject("");
//						}
//						
//						if(robFormA.getBizMainAddrTown() != null){
//							bizMainAddrTown.setDefaultModelObject(robFormA.getBizMainAddrTown());
//							bizPostAddrTown.setDefaultModelObject(robFormA.getBizPostAddrTown());
//						}else{
//							bizMainAddrTown.setDefaultModelObject("");
//							bizPostAddrTown.setDefaultModelObject("");
//						}
//						
//						if(robFormA.getBizMainAddrPostcode() != null){
//							bizMainAddrPostcode.setDefaultModelObject(robFormA.getBizMainAddrPostcode());
//							bizPostAddrPostcode.setDefaultModelObject(robFormA.getBizPostAddrPostcode());
//						}else{
//							bizMainAddrPostcode.setDefaultModelObject("");
//							bizPostAddrPostcode.setDefaultModelObject("");
//						}
//						
//						if(robFormA.getBizMainAddrState() != null){
//							bizMainAddrState.setDefaultModelObject(robFormA.getBizMainAddrState());
//							bizPostAddrState.setDefaultModelObject(robFormA.getBizPostAddrState());
//						}else{
//							bizMainAddrState.setDefaultModelObject("");
//							bizPostAddrState.setDefaultModelObject("");
//						}
//
//						
//
//						bizMainAddr.setDefaultModelObject("");
//						bizPostAddr.setDefaultModelObject("");
//						bizMainAddr2.setDefaultModelObject("");
//						bizPostAddr2.setDefaultModelObject("");
//						bizMainAddr3.setDefaultModelObject("");
//						bizPostAddr3.setDefaultModelObject("");
//						bizMainAddrTown.setDefaultModelObject("");
//						bizPostAddrTown.setDefaultModelObject("");
//						bizMainAddrPostcode.setDefaultModelObject("");
//						bizPostAddrPostcode.setDefaultModelObject("");
//						bizMainAddrState.setDefaultModelObject("");
//						bizPostAddrState.setDefaultModelObject("");
//						
//						bizMainAddr.setReadOnly(false);
//						bizMainAddr2.setReadOnly(false);
//						bizMainAddr3.setReadOnly(false);
//						bizMainAddrTown.setReadOnly(false);
//						bizMainAddrPostcode.setReadOnly(false);
//						
//						bizMainAddrState.setVisible(true);
//						incubaterStateDesc.setVisible(false);
//						bizMainAddrState.setDefaultModelObject(getCodeTypeWithValue(Parameter.CBS_ROB_STATE, resolve("page.lbl.ezbiz.robFormA.incubatorState")));
//
//					}
					
					
//					LG 03, Ground Floor, SME Technopreneur Centre 3, Block 3740, Persiaran Apec, Cyber 8, 63000 Cyberjaya, Selangor 
					
					target.add(wmcAddress);
					recalculateFee(target, robFormAForm);
				}
			};
			
			//formAfileUpload = new SSMTextField("formAfileUpload");
			//formAfileUpload.add(isIncubatorOnchange);
			//add(formAfileUpload);
			
			
			isIncubator.add(isIncubatorOnchange);
			if(isQuery){
				isIncubator.setVisible(true);
				isIncubatorDesc.setVisible(false);
			}
			wmcAddress.add(isIncubator);

			
			//Branches
			final WebMarkupContainer wmcBranchesAll = new WebMarkupContainer("wmcBranchesAll");
			wmcBranchesAll.setOutputMarkupId(true);
			wmcBranchesAll.setOutputMarkupPlaceholderTag(true);
			add(wmcBranchesAll);
			
			wmcBranches = new WebMarkupContainer("wmcBranches");
			wmcBranches.setOutputMarkupId(true);
			wmcBranches.setOutputMarkupPlaceholderTag(true);
			wmcBranchesAll.add(wmcBranches);
			
			final EditRobFormABranchPanel branchPanel = new EditRobFormABranchPanel("editRobFormABranchPanel", wmcBranches, robFormA);
			branchPanel.setOutputMarkupId(true);
			branchPanel.setOutputMarkupPlaceholderTag(true);
			
			wmcBranchesAll.add(branchPanel);
			
			final List listBranches = robFormA.getListRobFormABranches();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
			final SSMDataView<RobFormABranches> dataView = new SSMDataView<RobFormABranches>("sortingRobFormABranch", dp) {

				protected void populateItem(final Item<RobFormABranches> item) {
					RobFormABranches robFormABranches = item.getModelObject();

					item.add(new SSMLabel("branchNo", item.getIndex()+1));
					String address = robFormABranches.getAddr();
					if(StringUtils.isNotBlank(robFormABranches.getAddr2())){
						address += "\n"+robFormABranches.getAddr2();
					}
					if(StringUtils.isNotBlank(robFormABranches.getAddr3())){
						address += "\n"+robFormABranches.getAddr3();
					}
					address += "\n"+robFormABranches.getAddrPostcode()+" "+robFormABranches.getAddrTown();
					address = address +"\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormABranches.getAddrState()) ;
					address = address.toUpperCase();
					if(StringUtils.isNotBlank(robFormABranches.getAddrUrl())){
						address += "\n"+robFormABranches.getAddrUrl();
					}
					
					item.add(new MultiLineLabel("branchAddress", address));

					item.add(new AjaxLink("editBranch", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							branchPanel.setModel(item.getIndex());
							target.add(branchPanel);
						}
					});
					SSMAjaxLink delete = new SSMAjaxLink("deleteBranch", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABranches>)wmcBranches.get("sortingRobFormABranch")).getDataProvider();
							List<RobFormABranches> list = dpProvider.getListResult();
							list.remove(item.getIndex());
							dpProvider.resetView(list);
							target.add(wmcBranches);
						}
					};
					delete.setConfirmQuestion(resolve("page.confirm.deleteBranch"));
					item.add(delete);
					if(isQuery){
						delete.setVisible(false);
					}

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}

			};

			wmcBranches.add(dataView);
			wmcBranches.add(new SSMPagingNavigator("navigatorRobFormABranch", dataView));
			wmcBranches.add(new NavigatorLabel("navigatorLabelRobFormABranch", dataView));
			
			//Owners
			final WebMarkupContainer wmcOwnersAll = new WebMarkupContainer("wmcOwnersAll");
			wmcOwnersAll.setPrefixLabelKey(prefixLabelKey+"robFormAOwners.");
			wmcOwnersAll.setOutputMarkupId(true);
			wmcOwnersAll.setOutputMarkupPlaceholderTag(true);
			add(wmcOwnersAll);
			
			final SSMTextField newIcNoForOwners = new SSMTextField("newIcNoForOwners");
			wmcOwnersAll.add(newIcNoForOwners);
			
			final SSMLabel newIcNoForOwnersError = new SSMLabel("newIcNoForOwnersError","");
			newIcNoForOwnersError.setEscapeModelStrings(true);
			newIcNoForOwnersError.setVisible(false);
			//ui basic red pointing prompt label transition visible
			wmcOwnersAll.add(newIcNoForOwnersError);
			
			
			wmcOwners = new WebMarkupContainer("wmcOwners");
			wmcOwners.setOutputMarkupId(true);
			wmcOwners.setOutputMarkupPlaceholderTag(true);
			wmcOwnersAll.add(wmcOwners);
			

			final ModalWindow editOwnerPopUp = new ModalWindow("editOwnerPopUp");
			editOwnerPopUp.setHeightUnit("px"); 
			editOwnerPopUp.setInitialHeight(700);
			add(editOwnerPopUp);
			
			final SSMSessionSortableDataProvider dpOwners = new SSMSessionSortableDataProvider("", robFormA.getListRobFormAOwner());
			
			final SSMDataView<RobFormAOwner> dataViewOwners = new SSMDataView<RobFormAOwner>("sortingRobFormAOwners", dpOwners) {

				protected void populateItem(final Item<RobFormAOwner> item) {
					final RobFormAOwner robFormAOwnerSelected = (RobFormAOwner) item.getModelObject();

					item.add(new SSMLabel("ownerNo", item.getIndex()+1));
					item.add(new SSMLabel("name", robFormAOwnerSelected.getName()));
					item.add(new SSMLabel("idNo", robFormAOwnerSelected.getNewicno()));
					
					SSMLabel veriStatus = new SSMLabel("verificationStatus", robFormAOwnerSelected.getVerificationStatus(), Parameter.ROB_OWNER_VERI_STATUS);
					item.add(veriStatus);

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
					
					AjaxLink editOwner = new AjaxLink("editOwner", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
							
							editOwnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new EditRobFormAOwnerPanel(  robFormAOwnerSelected, editOwnerPopUp, item.getIndex());// edit record
								}
							});
							
							editOwnerPopUp.show(target);
						}
					};
					item.add(editOwner);
					
					SSMAjaxLink deleteOwner = new SSMAjaxLink("deleteOwner", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							
							listFormRobAOwners.remove(item.getIndex());
							
							dpProvider.resetView(listFormRobAOwners);
							target.add(wmcOwners);
							
							getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
							
						}
					};
					item.add(deleteOwner);
					
					deleteOwner.setConfirmQuestion(resolve("page.confirm.deleteOwner"));
					if(UserEnvironmentHelper.getLoginName().equals(robFormAOwnerSelected.getEzbizLoginName())){
						deleteOwner.setVisible(false);
					}else{
						if(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED.equals(robFormAOwnerSelected.getVerificationStatus())){
//							editOwner.setVisible(false);
						}
					}
					
					if(Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robFormAOwnerSelected.getVerificationStatus())){
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
					if(Parameter.ROB_OWNER_VERI_STATUS_PENDING_ADD_UPDATE.equals(robFormAOwnerSelected.getVerificationStatus())){
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
					
				}

			};

			wmcOwners.add(dataViewOwners);
			wmcOwners.add(new SSMPagingNavigator("navigatorRobFormAOwners", dataViewOwners));
			wmcOwners.add(new NavigatorLabel("navigatorLabelRobFormAOwners", dataViewOwners));
			
			editOwnerPopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
					List<RobFormAOwner> listFormRobAOwners = (List<RobFormAOwner>) getSession().getAttribute("listRobFormAOwners_");
					dpProvider.resetView(listFormRobAOwners);
					
					robFormA.setNewIcNoForOwners("");
//					target.add(newIcNoForOwners);
					target.add(wmcOwners);
				}
			});
			
			
			
			final String showOwnerValidationJS = "showOwnerValidation";
			String showOwnerValidationField[] = new String[]{"newIcNoForOwners"};
			String showOwnerValidationFieldRules[] = new String[]{"empty"};
			setSemanticJSValidation(this, showOwnerValidationJS, showOwnerValidationField,showOwnerValidationFieldRules);
			
			SSMAjaxButton showOwnersPanel = new SSMAjaxButton("showOwnersPanel", showOwnerValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					String newIc =  newIcNoForOwners.getInput();
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
					List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
					
					if(listFormRobAOwners.size() > 20){
						String ownerMore20 = getLocaleMsg("page.lbl.ezbiz.robFormA.partnerCannotMore20");
						newIcNoForOwnersError.setDefaultModelObject(ownerMore20);
						newIcNoForOwnersError.setVisible(true);
						target.add(wmcOwnersAll);
						return;
					}
					
					for (int i = 0; i < listFormRobAOwners.size(); i++) {
						if(newIc.equals(listFormRobAOwners.get(i).getNewicno())){
							String ownerAlreadyAddMsg = getLocaleMsg("page.lbl.ezbiz.robFormA.robFormAOwners.ownerAlreadyAdd",newIc);
							newIcNoForOwnersError.setDefaultModelObject(ownerAlreadyAddMsg);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcOwnersAll);
							return;
						}
					}
					
					LlpUserProfile llpUserProfile =llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, newIc);
					if(llpUserProfile==null){
						String fillICMsg = getLocaleMsg("page.lbl.ezbiz.robFormA.robFormAOwners.mustRegisterEzBiz",newIc);
						newIcNoForOwnersError.setDefaultModelObject(fillICMsg);
						newIcNoForOwnersError.setVisible(true);
						target.add(wmcOwnersAll);
						return;
					}else{
						if(Parameter.USER_STATUS_deceased.equals(llpUserProfile.getUserStatus())) {
							String fillICMsg = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.userAlreadyDeceased",newIc);
							newIcNoForOwnersError.setDefaultModelObject(fillICMsg);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcOwnersAll);
							return;
						}
						
						final RobFormAOwner robFormAOwner = new RobFormAOwner();
						copyFromUserProfileToRobOwner(llpUserProfile, robFormAOwner , false);
						LocalDate birthdate = new LocalDate(robFormAOwner.getDob());
						Years age = Years.yearsBetween(birthdate, new LocalDate());
						if(age.getYears()<18){
							String ownerBelow18 = getLocaleMsg("page.lbl.ezbiz.robFormA.robFormAOwners.ownerBelow18", robFormAOwner.getName());
							newIcNoForOwnersError.setDefaultModelObject(ownerBelow18);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcOwnersAll);
							return;
						}else{
							RobUserOku robUserOkuPartner = robUserOkuService.findOkuByUserRefNo(llpUserProfile.getUserRefNo());
							if(robUserOkuPartner!=null && Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOkuPartner.getOkuRegStatus())) {
								robFormAOwner.setName(robFormAOwner.getName()+"  ( "+Parameter.ROB_FORM_A_INCENTIVE_oku +" )" ); //set name <user> (OKU) at popup label.. 
							}
							editOwnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new EditRobFormAOwnerPanel(  robFormAOwner, editOwnerPopUp);// edit record
								}
							});
							
							getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
							editOwnerPopUp.show(target);
						}
					}
						
					
				}

			};
			showOwnersPanel.setDefaultFormProcessing(false);
			wmcOwnersAll.add(showOwnersPanel);
			
			if(isQuery){
				showOwnersPanel.setVisible(false);
			}
			
			//Biz Code
			wmcBizCodeAll = new WebMarkupContainer("wmcBizCodeAll");
			wmcBizCodeAll.setPrefixLabelKey(prefixLabelKey+"robFormABizCode.");
			wmcBizCodeAll.setOutputMarkupId(true);
			wmcBizCodeAll.setOutputMarkupPlaceholderTag(true);
			add(wmcBizCodeAll);
			
			bizDesc = new SSMTextArea("bizDesc");
			bizDesc.setOutputMarkupId(true);
			wmcBizCodeAll.add(bizDesc);
			
			if(StringUtils.isNotBlank(robFormA.getBizDesc())){
				bizDesc.setDefaultModelObject(robFormA.getBizDesc());
			}
			
			final ModalWindow editBizCodePopUp = new ModalWindow("editBizCodePopUp");
			editBizCodePopUp.setWidthUnit("px"); 
			editBizCodePopUp.setHeightUnit("px"); 
			editBizCodePopUp.setInitialWidth(800);
			editBizCodePopUp.setInitialHeight(500);

			add(editBizCodePopUp);
			
			editBizCodePopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
					List<RobFormABizCode> listFormRobABizCode = (List<RobFormABizCode>) getSession().getAttribute("listRobFormABizCode_");
					
					dpProvider.resetView(listFormRobABizCode);
					
					target.add(wmcBizCodeAll);
				}
			});
			
			final SSMSessionSortableDataProvider dpBizCode = new SSMSessionSortableDataProvider("", robFormA.getListRobFormABizCode());
			final SSMDataView<RobFormABizCode> dataViewBizCode = new SSMDataView<RobFormABizCode>("sortingRobFormABizCode", dpBizCode) {

				protected void populateItem(final Item<RobFormABizCode> item) {
					RobFormABizCode robFormABizCode = item.getModelObject();

					item.add(new SSMLabel("bizCodeNo", item.getIndex()+1));
					item.add(new SSMLabel("bizCode", robFormABizCode.getBizCode()));
					item.add(new MultiLineLabel("bizCodeDesc", robFormABizCode.getBizCodeDesc()));
					
					SSMAjaxLink deleteBizCode = new SSMAjaxLink("deleteBizCode", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
							List<RobFormABizCode> list = dpProvider.getListResult();
							list.remove(item.getIndex());
							dpBizCode.resetView(list);
							target.add(wmcBizCodeAll);
						}
					};
					item.add(deleteBizCode);
					deleteBizCode.setConfirmQuestion(resolve("page.confirm.deleteBizCode"));
					
					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));

				}

			};
			wmcBizCodeAll.add(dataViewBizCode);
			wmcBizCodeAll.add(new SSMPagingNavigator("navigatorRobFormABizCode", dataViewBizCode));
			wmcBizCodeAll.add(new NavigatorLabel("navigatorLabelRobFormABizCode", dataViewBizCode));
			
			
			SSMAjaxButton showBizCodePanel = new SSMAjaxButton("showBizCodePanel") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
					List<RobFormABizCode> listFormRobABizCode = dpProvider.getListResult();
					editBizCodePopUp.setPageCreator(new ModalWindow.PageCreator() {
						@Override
						public Page createPage() {
							return new EditRobFormABizCodePanel(  null, editBizCodePopUp);// edit record
						}
					});
					
					getSession().setAttribute("listRobFormABizCode_", (Serializable) listFormRobABizCode);
					editBizCodePopUp.show(target);
						
				}
			};
			showBizCodePanel.setDefaultFormProcessing(false);
			wmcBizCodeAll.add(showBizCodePanel);
			//End of Biz Code
			
			
			//Fee Summary	
			wmcFeeSummaryAll = new WebMarkupContainer("wmcFeeSummaryAll");
			wmcFeeSummaryAll.setPrefixLabelKey(prefixLabelKey+"feeSummary.");
			wmcFeeSummaryAll.setOutputMarkupId(true);
			wmcFeeSummaryAll.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummaryAll);
			
			
			queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(false);
			wmcFeeSummaryAll.add(queryAnswer);
			
			uploadErrorLabel = new SSMLabel("uploadErrorLabel","");
			uploadErrorLabel.setEscapeModelStrings(false);
			wmcFeeSummaryAll.add(uploadErrorLabel);
			
			FileUploadField fileUpload = new FileUploadField("formAfileUpload");
			fileUpload.setRequired(false);
			wmcFeeSummaryAll.add(fileUpload);
			
			SSMAjaxButton uploadSuppDoc = new SSMAjaxButton("uploadSuppDoc") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormA robFormATmp = (RobFormA) form.getDefaultModelObject();
					boolean isError = false;
					if(robFormATmp.getformAfileUpload()!=null){
						if(robFormATmp.getformAfileUpload().get(0).getBytes().length>3145728){
							uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormA.exceedUploadSize"));
							isError = true;
						}else{
							try {
								ByteArrayInputStream bais = new ByteArrayInputStream(robFormATmp.getformAfileUpload().get(0).getBytes());
								PDDocument document = PDDocument.load(bais);
								document.close();
							} catch (Exception e) {
								uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormA.notInPDF"));
								isError = true;
							}
						}
						
						if(!isError){
							LlpFileData supportingDoc = new LlpFileData();
							supportingDoc.setFileData(robFormATmp.getformAfileUpload().get(0).getBytes());
							supportingDoc.setFileDataType("PDF");//,robFormBSummaryModel.getFileUploadTmp().get(0).getBytes());
							llpFileDataService.insert(supportingDoc);
							
							robFormA.setSupportingDocData(supportingDoc);
							robFormA.setIsHasSupportingDoc(Parameter.YES_NO_yes);
							
							robFormATmp.setSupportingDocData(supportingDoc);
							robFormATmp.setIsHasSupportingDoc(Parameter.YES_NO_yes);
							
							robFormAService.insertUpdateAll(robFormA);
							
							//System.out.println("(1) robFormA robFormACode: "+robFormA.getRobFormACode());
							//System.out.println("(2) robFormATmp robFormACode: "+robFormATmp.getRobFormACode()); //null jika new business (1st time). 
							
							//recalculateFee(target, robFormATmp); //fix insentif STU can apply more than 1 (robFormATmp.RobFormACode null).
							recalculateFee(target, robFormA);
						}
						
						target.add(wmcFeeSummaryAll);
					}

					
				}
			};
			wmcFeeSummaryAll.add(uploadSuppDoc);
			
			downloadSupportingDoc = new SSMDownloadLink("downloadSupportingDoc");
			downloadSupportingDoc.setVisible(false);
			wmcFeeSummaryAll.add(downloadSupportingDoc); 
			
			removeSupportingDoc = new SSMAjaxButton("removeSupportingDoc") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					LlpFileData suppDoc = robFormA.getSupportingDocData();
					if(suppDoc!=null){
						llpFileDataService.delete(suppDoc);
						robFormA.setIsHasSupportingDoc(Parameter.YES_NO_no);
						robFormA.setSupportingDocData(null);
						robFormAService.insertUpdateAll(robFormA);
						recalculateFee(target, robFormA);
					}
					
				}
			};
			removeSupportingDoc.setVisible(false);
			wmcFeeSummaryAll.add(removeSupportingDoc);
			if(Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
				removeSupportingDoc.setVisible(true);
				downloadSupportingDoc.setDownloadData("SUPPORTING.pdf", "application/pdf", robFormA.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
			}
			
					
			declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robFormA, "declarationChkBox") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (String.valueOf(true).equals(getValue())) {
						robFormA.setDeclarationChkBox(true);
					} else {
						robFormA.setDeclarationChkBox(false);
					}
					if(previewToPayment.isVisible()){
						previewToPayment.setEnabled(robFormA.getDeclarationChkBox());
						arg0.add(previewToPayment);
					}
					if(submitVerification.isVisible()){
						submitVerification.setEnabled(robFormA.getDeclarationChkBox());
						arg0.add(submitVerification);
					}
					if(reLodgeFormA.isVisible()){
						reLodgeFormA.setEnabled(robFormA.getDeclarationChkBox());
						arg0.add(reLodgeFormA);
					}
					
				}
			};
			wmcFeeSummaryAll.add(declarationChkBox);
			
			
			
			if(isQuery || Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())){
				queryAnswer.setVisible(true);
			}
			
			regFeePerYear = new SSMLabel("regFeePerYear","");
			wmcFeeSummaryAll.add(regFeePerYear);
			regFeeDuration = new SSMLabel("regFeeDuration","");
			wmcFeeSummaryAll.add(regFeeDuration);
			
			regFeeDiscount = new SSMLabel("regFeeDiscount","");
			regFeeDiscount.setOutputMarkupId(true);
			regFeeDiscount.setVisible(false);
			wmcFeeSummaryAll.add(regFeeDiscount);
			totalRegDiscount = new SSMLabel("totalRegDiscount","");
			wmcFeeSummaryAll.add(totalRegDiscount);
			regFeeDurationDiscount = new SSMLabel("regFeeDurationDiscount","");
			wmcFeeSummaryAll.add(regFeeDurationDiscount);
			incentiveTypeLabel = new SSMLabel("incentiveTypeLabel","");
			wmcFeeSummaryAll.add(incentiveTypeLabel);
			
			businessInfoDiscount = new SSMLabel("businessInfoDiscount","");
			businessInfoDiscount.setOutputMarkupId(true);
			businessInfoDiscount.setVisible(false);
			wmcFeeSummaryAll.add(businessInfoDiscount);
			totalBusinessInfoDiscount = new SSMLabel("totalBusinessInfoDiscount","");
			wmcFeeSummaryAll.add(totalBusinessInfoDiscount);
			businessInfoQuantityDiscount = new SSMLabel("businessInfoQuantityDiscount","");
			wmcFeeSummaryAll.add(businessInfoQuantityDiscount);
			
			totalRegFee = new SSMLabel("totalRegFee","");
			wmcFeeSummaryAll.add(totalRegFee);
			
			branchFee = new SSMLabel("branchFee","");
//			wmcFeeSummaryAll.add(branchFee);
			branchFeeDuration  = new SSMLabel("branchFeeDuration","");
			wmcFeeSummaryAll.add(branchFeeDuration);
			branchFeePerYear = new SSMLabel("branchFeePerYear","");
			wmcFeeSummaryAll.add(branchFeePerYear);
			totalBranchFee = new SSMLabel("totalBranchFee","");
			wmcFeeSummaryAll.add(totalBranchFee);
			
			bisnessInfoFee = new SSMLabel("bisnessInfoFee","");
			wmcFeeSummaryAll.add(bisnessInfoFee);
			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity","");
			wmcFeeSummaryAll.add(bisnessInfoFeeQuantity);
			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee","");
			wmcFeeSummaryAll.add(totalBisnessInfoFee);
			
			totalFee = new SSMLabel("totalFee","");
			wmcFeeSummaryAll.add(totalFee);
			
			summaryError = new SSMLabel("summaryError","");
			summaryError.setEscapeModelStrings(true);
			
//			summaryError.setVisible(false);
			//ui basic red pointing prompt label transition visible
//			wmcFeeSummaryAll.add(summaryError);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			wmcFeeSummaryAll.add(listError); 
			
			final WebMarkupContainer wmcAddressStep = new WebMarkupContainer("wmcAddressStep");
			wmcAddressStep.setOutputMarkupId(true);
			wmcAddressStep.setOutputMarkupPlaceholderTag(true);
			wmcAddressStep.add(new AttributeModifier("class", new Model("active step")));
			add(wmcAddressStep);
			final WebMarkupContainer wmcBranchesStep = new WebMarkupContainer("wmcBranchesStep");
			wmcBranchesStep.setOutputMarkupId(true);
			wmcBranchesStep.setOutputMarkupPlaceholderTag(true);
			wmcBranchesStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcBranchesStep);
			final WebMarkupContainer wmcOwnersStep = new WebMarkupContainer("wmcOwnersStep");
			wmcOwnersStep.setOutputMarkupId(true);
			wmcOwnersStep.setOutputMarkupPlaceholderTag(true);
			wmcOwnersStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcOwnersStep);
			final WebMarkupContainer wmcBizCodeStep = new WebMarkupContainer("wmcBizCodeStep");
			wmcBizCodeStep.setOutputMarkupId(true);
			wmcBizCodeStep.setOutputMarkupPlaceholderTag(true);
			wmcBizCodeStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcBizCodeStep);
			final WebMarkupContainer wmcFeeSummaryStep = new WebMarkupContainer("wmcFeeSummaryStep");
			wmcFeeSummaryStep.setOutputMarkupId(true);
			wmcFeeSummaryStep.setOutputMarkupPlaceholderTag(true);
			wmcFeeSummaryStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcFeeSummaryStep);
			
			final WebMarkupContainer segmentLeftMenu[] = new WebMarkupContainer[]{wmcAddressStep, wmcBranchesStep, wmcBizCodeStep, wmcOwnersStep,wmcFeeSummaryStep}; 
			final WebMarkupContainer segmentContainer[] = new WebMarkupContainer[]{wmcAddress, wmcBranchesAll, wmcBizCodeAll, wmcOwnersAll, wmcFeeSummaryAll}; 
			
			final String mainValidationJS = "mainValidation";
			String mainFieldToValidate[] = new String[]{"nameType","bizName","bizStartDt","bizRegPeriod","isIncubator","isOnlineSeller","bizMainAddr","bizMainAddrTownTmp","bizMainAddrPostcode","bizMainAddrStateDesc","bizPostAddr","bizPostAddrTownTmp","bizPostAddrStateDesc","bizPostAddrPostcode","bizMainAddrTelNo","bizMainAddrMobileNo","bizPostAddrTelNo","bizPostAddrMobileNo","bizPostAddrEmail","bizMainAddrEmail","bizMainAddrUrl"};
			String mainFieldToValidateRules[] = new String[]{"empty","empty","empty","empty","empty","empty","empty","empty","exactLengthNumber[5]","empty","empty","empty","empty","exactLengthNumber[5]","isNotReqMinLengthNumber[10]","minLengthNumber[10]","isNotReqMinLengthNumber[10]","isNotReqMinLengthNumber[10]","isNotReqEmail","isNotReqEmail","isNotReqUrl"}; //isNotReqUrl means set field ie. url tak mandatory. Refer form.js dan semantic.js utk validation url/email.
			setSemanticJSValidation(this, mainValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			
			final String bizCodeValidationJS = "bizCodeValidation";
			String bizCodeFieldToValidate[] = new String[]{"nameType","bizName","bizStartDt","bizRegPeriod","bizDesc"};
			String bizCodeFieldToValidateRules[] = new String[]{"empty","empty","empty","empty","empty"};
			setSemanticJSValidation(this, "bizCodeValidation", bizCodeFieldToValidate, bizCodeFieldToValidateRules);
			
			final String reLodgeFormAValidationJS = "reLodgeFormAValidation";
			String reLodgeFormAValidation[] = new String[]{"queryAnswer"};
			String reLodgeFormAValidationRules[] = new String[]{"empty"};
			setSemanticJSValidation(wmcFeeSummaryAll, reLodgeFormAValidationJS, reLodgeFormAValidation, reLodgeFormAValidationRules);
			
			
			
			//Show Hide Panel
			wmcAddressStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 0, segmentLeftMenu, segmentContainer);
				}
			});
			wmcBranchesStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 1, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcOwnersStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 2, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcBizCodeStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcFeeSummaryStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			});
			//0				1				2				3			4
			//wmcAddress, wmcBranchesAll, wmcBizCodeAll, wmcOwnersAll, wmcFeeSummaryAll
			
			//Main Address
			SSMAjaxButton mainNext = new SSMAjaxButton("mainNext", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target,form,"");
					hideAndShowSegment(target, 1, segmentLeftMenu ,segmentContainer);
				}
			};
			wmcAddress.add(mainNext);
			
			//Branches
			SSMAjaxButton branchesPrevious = new SSMAjaxButton("branchesPrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form,"");
					hideAndShowSegment(target, 0, segmentLeftMenu, segmentContainer);
				}

			};
			wmcBranchesAll.add(branchesPrevious);
			
			SSMAjaxButton branchesNext = new SSMAjaxButton("branchesNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form, "");
					hideAndShowSegment(target, 2, segmentLeftMenu, segmentContainer);
				}
			};
			wmcBranchesAll.add(branchesNext);
			
			
			//Biz Code
			SSMAjaxButton bizCodePrevious = new SSMAjaxButton("bizCodePrevious", bizCodeValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form, "");
					hideAndShowSegment(target, 1, segmentLeftMenu, segmentContainer);
				}
			};
			wmcBizCodeAll.add(bizCodePrevious);
			
			SSMAjaxButton bizCodeNext = new SSMAjaxButton("bizCodeNext", bizCodeValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form, "");
					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			};
			wmcBizCodeAll.add(bizCodeNext);
			
			
			//Owners
			SSMAjaxButton ownersPrevious = new SSMAjaxButton("ownersPrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form, "");
					hideAndShowSegment(target, 2, segmentLeftMenu ,segmentContainer);
				}
			};
			wmcOwnersAll.add(ownersPrevious);

			SSMAjaxButton ownersNext = new SSMAjaxButton("ownersNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					updateAll(target, form, "owner");
					hideAndShowSegment(target, 4, segmentLeftMenu, segmentContainer);
				}
			};
			wmcOwnersAll.add(ownersNext);
			//Fee Summary
			SSMAjaxButton feeSummaryPrevious = new SSMAjaxButton("feeSummaryPrevious", bizCodeValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					updateAll(target, form, "owner");
					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			};
			wmcFeeSummaryAll.add(feeSummaryPrevious);
			
			
			SSMAjaxFormSubmitBehavior isOnlineSellerOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormA robFormAForm = (RobFormA) getForm().getDefaultModelObject();
					
					boolean hasChanges = false;
					SSMSessionSortableDataProvider dpProviderBizCode = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
					List<RobFormABizCode> listRobFormABizCode = dpProviderBizCode.getListResult();
					String[] internetCode = StringUtils.split(getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_FORM_A_INTERNET_CODE),",");
					Set<String> setInternetCode = new HashSet<String>(Arrays.asList(internetCode));
					
					if(Parameter.YES_NO_yes.equals(robFormAForm.getIsOnlineSeller())){
						
						
						for (int i = 0; i < listRobFormABizCode.size(); i++) {
							if(setInternetCode.contains(listRobFormABizCode.get(i).getBizCode())){
								setInternetCode.remove(listRobFormABizCode.get(i).getBizCode());
							}
							if(setInternetCode.isEmpty()){
								break;
							}
						}
						
						for (Iterator iterator = setInternetCode.iterator(); iterator.hasNext();) {
							String bizCode = (String) iterator.next();
							LlpParameters internetParameter = llpParametersService.findParameter(Parameter.ROB_BUSINESS_CODE, bizCode);
							listRobFormABizCode.add(new RobFormABizCode(internetParameter.getCode(),internetParameter.getCodeDesc()));
							hasChanges = true;
						}
						
					}else{
						
						List<RobFormABizCode> listToRemove = new ArrayList();
						
						for (int i = 0; i < listRobFormABizCode.size(); i++) {
							if(setInternetCode.contains(listRobFormABizCode.get(i).getBizCode())){
								listToRemove.add(listRobFormABizCode.get(i));
							}
						}
						
						for (int i = 0; i < listToRemove.size(); i++) {
							listRobFormABizCode.remove(listToRemove.get(i));
							hasChanges = true;
						}
						
						
					}
					if(hasChanges){
						dpProviderBizCode.resetView(listRobFormABizCode);
						target.add(wmcBizCodeAll);
						hideAndShowSegment(target, 0, segmentLeftMenu, segmentContainer);
					}
					recalculateFee(target, robFormAForm);
					
				}
			};
			
			SSMDropDownChoice isOnlineSeller =  new SSMDropDownChoice("isOnlineSeller", Parameter.YES_NO);
			isOnlineSeller.setLabelKey("page.lbl.ezbiz.robFormA.isOnlineSeller");
			isOnlineSeller.add(isOnlineSellerOnchange);
			if(isQuery){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
				isOnlineSeller.setVisible(true);
			}
			wmcAddress.add(isOnlineSeller);
			
			previewToPayment = new SSMAjaxButton("previewToPayment", bizCodeValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
					
					updateAll(target, form, "");
					

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())){
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
					}else{//PERSONAL
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL );
					}
					
					paymentItem.setQuantity(robFormA.getBizRegPeriod());
					paymentItem.setAmount(robFormA.getRegistrationAmt());
					paymentItem.setPaymentDet(robFormA.getBizName());
					listPaymentItems.add(paymentItem);

					if (robFormA.getBranchesAmt()>0) {
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
						paymentItem2.setQuantity(robFormA.getBizRegPeriod());
						paymentItem2.setPaymentDet(robFormA.getListRobFormABranches().size()+" ");
						paymentItem2.setAmount(robFormA.getBranchesAmt());
						listPaymentItems.add(paymentItem2);
					}
					
					if(Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormA.getBusinessInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()* getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}

//					setResponsePage(new PaymentDetailPage(robFormA.getRobFormACode(), RobFormAService.class.getSimpleName(), robFormA,
//							listPaymentItems));
					robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT);
					robFormAService.update(robFormA);
					setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), this.getPage()) );
//							listPaymentItems));
				}
			};
			previewToPayment.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(previewToPayment);
			
			submitVerification = new SSMAjaxButton("submitVerification", bizCodeValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
					
					updateAll(target, form, "");
					
					if(Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())){
						RobFormNotes formNotes = robFormA.getListRobFormNotes().get(robFormA.getListRobFormNotes().size() - 1);
						formNotes.setNotesAnswer(robFormA.getQueryAnswer());
						robFormNotesService.update(formNotes);
					}
					
					robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION);
					robFormAService.update(robFormA);
//
					setResponsePage(TabRobFormAPage.class);
				}
			};
			submitVerification.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(submitVerification);
			
			reLodgeFormA = new SSMAjaxButton("reLodgeFormA", reLodgeFormAValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
					
					try {
						updateAll(target, form, "");
						
						robFormAService.reLodgeFormA(robFormA);
						
						storeSuccessMsgKey("page.lbl.ezbiz.successRelodge");
						
						SignInSession signInSession = (SignInSession)getSession();
						
						if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
							setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), getPage()));
						}else {
							setResponsePage(new TabRobFormAPage());
						}						
						
					} catch (Exception e) {
						ssmError(e.getMessage());
						FeedbackPanel feedbackPanel =  ((EditRobFormAPage)getPage()).getFeedbackPanel();
						target.add(feedbackPanel);
						
						String scroll = "\n$.scrollTo(document.getElementById('"+getFeedbackPanel().getMarkupId()+"'),100);\n";
						target.appendJavaScript(scroll);
					}
				}
			};
			reLodgeFormA.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(reLodgeFormA);
			
			if(isQuery){
				previewToPayment.setVisible(false);
				reLodgeFormA.setVisible(true);
			}else{
				reLodgeFormA.setVisible(false);
				previewToPayment.setVisible(true);
			}
			
			String hideAllJs = "";
			
			for (int i = 1; i < segmentContainer.length; i++) {
				String js = "$('#"+segmentContainer[i].getMarkupId()+"').hide();"; 
				hideAllJs+=js;
			}
			Label hideAllLbl = new Label("hideAllLbl", hideAllJs);
			hideAllLbl.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		    add(hideAllLbl);
			
			
			setOutputMarkupId(true);
			setOutputMarkupPlaceholderTag(true);
		}
		
		
		
		protected void updateAll(AjaxRequestTarget target, Form<?> form, String category) {
			FeedbackPanel feedbackPanel =  ((EditRobFormAPage)getPage()).getFeedbackPanel();
			feedbackPanel.getFeedbackMessages().clear();
			target.add(feedbackPanel);
			
			RobFormA robFormAForm = (RobFormA) form.getDefaultModelObject();
			if(Parameter.YES_NO_yes.equals(robFormAForm.isIncubator())){
				robFormAForm.setBizMainAddrState(resolve("page.lbl.ezbiz.robFormA.incubatorState"));
			}
			
			robFormA.copyFrom(robFormAForm);
			recalculateFee(target, robFormA);
			
			
			boolean isNew = false;
			if(StringUtils.isBlank(robFormA.getRobFormACode())){
				isNew = true;
			}
			
				// ADDED 12/04/2018 -------------------------
				if(!isNew){
					
					List<RobFormAOwner> ownerLs = robFormA.getListRobFormAOwner();
				//	List<RobFormAOwner> ownerInDbLs = robFormAOwnerService.findByRobFormACode(robFormA.getRobFormACode());
					
//					Integer totalOwnerInSession = ownerLs.size();
//					Integer totalOwnerInDatabase = ownerInDbLs.size();
					
//					System.out.println("record total in session >>>>>>>> "+ownerLs.size());
//					System.out.println("record total in database >>>>>>>> "+ownerInDbLs.size());
					
					List<RobFormAOwner> newOwnerLs = new ArrayList<RobFormAOwner>();
					
//					if(totalOwnerInSession == totalOwnerInDatabase){
					
						for(RobFormAOwner owner :ownerLs){
						
						String status = robFormAOwnerService.getVerificationStatus(owner.getRobFormACode(), owner.getNewicno());
						RobFormAOwner newOwner = new RobFormAOwner();
						newOwner = owner;
						newOwner.setVerificationStatus(status);
						
						newOwnerLs.add(newOwner);
						}
						
//					} else {
//						for(RobFormAOwner owner :ownerInDbLs){
//							
//							String status = robFormAOwnerService.getVerificationStatus(owner.getRobFormACode(), owner.getNewicno());
//							RobFormAOwner newOwner = new RobFormAOwner();
//							newOwner = owner;
//							newOwner.setVerificationStatus(status);
//							
//							newOwnerLs.add(newOwner);
//							}
					
//					
					robFormA.setListRobFormAOwner(newOwnerLs);
				
					
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
					
					dpProvider.resetView(newOwnerLs);
					target.add(wmcOwners);
					getSession().setAttribute("listRobFormAOwners_", (Serializable) newOwnerLs);
					
				}

				// END ---------------------------
			
			
			robFormAService.insertUpdateAll(robFormA);
			form.setDefaultModelObject(robFormA);
			if(isNew){
				robFormACode.setDefaultModelObject(robFormA.getRobFormACode());
				target.add(robFormACode);
			}
		}



		protected void recalculateFee(AjaxRequestTarget target, RobFormA robFormAForm) {
			
			String postcodeTownState = get("wmcAddress:bizMainAddrTownTmp").getDefaultModelObjectAsString();
			if(StringUtils.isNotBlank(postcodeTownState)&&postcodeTownState.indexOf(":")!=-1){
				robFormAForm.setBizMainAddrTown(StringUtils.split(postcodeTownState,":")[1]);
				robFormAForm.setBizMainAddrState(StringUtils.split(postcodeTownState,":")[2]);
			}
			String postcodeTownStatePost = get("wmcAddress:bizPostAddrTownTmp").getDefaultModelObjectAsString();
			if(StringUtils.isNotBlank(postcodeTownStatePost)&&postcodeTownStatePost.indexOf(":")!=-1){
				robFormAForm.setBizPostAddrTown(StringUtils.split(postcodeTownStatePost,":")[1]);
				robFormAForm.setBizPostAddrState(StringUtils.split(postcodeTownStatePost,":")[2]);
			}
			
			uploadErrorLabel.setDefaultModelObject("");
			if(isQuery){
				robFormAForm.setNameType(robFormA.getNameType());
				robFormAForm.setBizRegPeriod(robFormA.getBizRegPeriod());
				robFormAForm.setBuyInfo(robFormA.isBuyInfo());
//				robFormAForm.setIncubator(robFormA.isIncubator());
			}
			
			SSMSessionSortableDataProvider dpProviderBranch = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABranches>)wmcBranches.get("sortingRobFormABranch")).getDataProvider();
			List<RobFormABranches> listBranches = dpProviderBranch.getListResult();
			
			SSMSessionSortableDataProvider dpProviderOwners = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
			List<RobFormAOwner> listRobFormAOwner = dpProviderOwners.getListResult();
			
			SSMSessionSortableDataProvider dpProviderBizCode = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
			List<RobFormABizCode> listRobFormABizCode = dpProviderBizCode.getListResult();
			
			robFormAForm.setListRobFormABranches(listBranches);
			robFormAForm.setListRobFormAOwner(listRobFormAOwner);
			robFormAForm.setListRobFormABizCode(listRobFormABizCode);
			robFormA.setListRobFormABranches(listBranches);
			robFormA.setListRobFormAOwner(listRobFormAOwner);
			robFormA.setListRobFormABizCode(listRobFormABizCode);

			
			double totalFeeDouble = 0;
			double regFeePerYearDouble = formAPaymentFeePersonal.getPaymentFee(); 
			
			if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
				regFeePerYearDouble = formAPaymentFeeTrade.getPaymentFee();
			}
			
			double totalRegFeeDouble = regFeePerYearDouble * robFormAForm.getBizRegPeriod();
			double branchFeeDouble = branchPaymentFee.getPaymentFee();
			double branchFeePerYearDouble = branchFeeDouble * robFormAForm.getListRobFormABranches().size();
			double totalBranchFeeDouble = branchFeePerYearDouble * robFormAForm.getBizRegPeriod();
			
			double bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
			int bisnessInfoFeeQuantityInt = 0;
			double totalBisnessInfoFeeDouble = 0;
			
			double gstAmt = 0;
			
			double totalDiscount = 0;
			double totalRegDisc = 0;
			double totalBizInfoDiscount = 0;
			
			double regDiscount = 0;
			double bizInfoDiscount = 0;
			double gstAmountDiscount = 0;
			String gstCode = "";
			
			if(robFormAForm.getIncentive() != null && !robFormAForm.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no) ){
				
				//register discount
				if(robFormAForm.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_student)){
					regDiscount = incentivePersonalStudent.getPaymentFee();
					if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
						regDiscount = incentiveTradeStudent.getPaymentFee();
					}
					
					bizInfoDiscount = incentiveBizInfoStudent.getPaymentFee();
					gstCode = incentiveBizInfoStudent.getGstCode();
					
				}else if(robFormAForm.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)){
					regDiscount = incentivePersonalOku.getPaymentFee();
					if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
						regDiscount = incentiveTradeOku.getPaymentFee();
					}
					
					bizInfoDiscount = incentiveBizInfoOku.getPaymentFee();
					gstCode = incentiveBizInfoOku.getGstCode();
				}
				
				totalRegDisc = regDiscount;
				
				//biz info discount
				if(Parameter.YES_NO_yes.equals(robFormAForm.isBuyInfo())){
					bisnessInfoFeeQuantityInt = 1;
					totalBizInfoDiscount = bizInfoDiscount;
					if(Parameter.PAYMENT_GST_CODE_SR.equalsIgnoreCase(gstCode)){
						totalBizInfoDiscount += bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
						gstAmountDiscount += (bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
					}
					
					if(totalBizInfoDiscount != 0){
						businessInfoDiscount.setVisible(true);
					}else{
						businessInfoDiscount.setVisible(false);
					}
				}
				
				totalDiscount = totalRegDisc + totalBizInfoDiscount;
				
				if(isQuery){
					submitVerification.setVisible(false);
					reLodgeFormA.setVisible(true);
				}else{
					reLodgeFormA.setVisible(false);
					submitVerification.setVisible(true);
				}
				
				previewToPayment.setVisible(false);
				regFeeDiscount.setVisible(true);
				
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equals(robFormAForm.getIncentive())){ //if oku bypass submit for verification (button)
					submitVerification.setVisible(false);
					previewToPayment.setVisible(true);
				}
				
			}else{
				if(isQuery){
					previewToPayment.setVisible(false);
					reLodgeFormA.setVisible(true);
				}else{
					reLodgeFormA.setVisible(false);
					previewToPayment.setVisible(true);
				}
				
				submitVerification.setVisible(false);
				regFeeDiscount.setVisible(false);
			}
			
			if(Parameter.YES_NO_yes.equals(robFormAForm.isBuyInfo())){
				bisnessInfoFeeQuantityInt = 1;
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
			}
			
			totalFeeDouble = totalRegFeeDouble +  totalBranchFeeDouble + totalBisnessInfoFeeDouble - totalDiscount;
			
			DecimalFormat df = new DecimalFormat("#0.00");
			DecimalFormat dfNegative = new DecimalFormat("-#0.00");
			robFormAForm.setRegistrationAmt(totalRegFeeDouble - totalRegDisc);
			robFormAForm.setBranchesAmt(totalBranchFeeDouble);
			robFormAForm.setBusinessInfoAmt(totalBisnessInfoFeeDouble - totalBizInfoDiscount);
			robFormAForm.setGstAmt(gstAmt - gstAmountDiscount);
			robFormAForm.setTotalAmt(totalFeeDouble);
			
			incentiveTypeLabel.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_FORM_A_INCENTIVE, robFormAForm.getIncentive()));
			regFeeDiscount.setDefaultModelObject(dfNegative.format(regDiscount));
			regFeeDurationDiscount.setDefaultModelObject(1);
			totalRegDiscount.setDefaultModelObject(dfNegative.format(totalRegDisc));
			
			businessInfoDiscount.setDefaultModelObject(dfNegative.format(totalBizInfoDiscount));
			totalBusinessInfoDiscount.setDefaultModelObject(dfNegative.format(totalBizInfoDiscount));
			businessInfoQuantityDiscount.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
			
			regFeePerYear.setDefaultModelObject(df.format(regFeePerYearDouble));
			regFeeDuration.setDefaultModelObject(robFormAForm.getBizRegPeriod());
			totalRegFee.setDefaultModelObject(df.format(totalRegFeeDouble));
			
			branchFee.setDefaultModelObject(df.format(branchFeeDouble));
			branchFeeDuration.setDefaultModelObject(robFormAForm.getBizRegPeriod());
			branchFeePerYear.setDefaultModelObject(df.format(branchFeePerYearDouble));
			totalBranchFee.setDefaultModelObject(df.format(totalBranchFeeDouble));
			
			bisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
			totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			
			totalFee.setDefaultModelObject(df.format(totalFeeDouble));
			
			listError.removeAll();
			
			//Check Name
			bizNameWarning.setDefaultModelObject("");
			if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
				
				boolean checkWithCBS = false;
				if(currentShowId==4 || currentShowId==3){//mean screen 
					checkWithCBS = true;
				}
				
				List<SSMException> listException = robFormAService.validateName(robFormAForm.getBizName(), checkWithCBS);
				if(listException.size()>0){
					String errorDesc = "";
					for (int i = 0; i < listException.size(); i++) {
						listError.add(new SSMLabel(listError.newChildId() ,listException.get(i).getMessage()));
						errorDesc+=listException.get(i).getMessage()+"</br>";
					}
					if(StringUtils.isNotBlank(errorDesc)){
						bizNameWarning.setDefaultModelObject(errorDesc+"</br>");
//						alert = generateNotyAlert( "reservedName.page.symbol", Parameter.ALERT_TYPE_error, target);
					}
				}
				
			}else{
				if(!currentLlpUserProfile.getName().equals(robFormAForm.getBizName())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.personalMustUserLoggerName", robFormAForm.getBizName())));
				}
				if(listRobFormAOwner.size()>1){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.personalCanOnlyHaveOneOwner")));
				}
			}
			
			//Check Registration Date
			if(robFormAForm.getBizStartDt()!=null){
				bizStartDtWarning.setDefaultModelObject("");
				if(robFormAForm.getBizStartDt().after(new Date())){
					String futureDtError =resolve("page.lbl.ezbiz.robFormA.bizDateCannotFuture");
					listError.add(new SSMLabel(listError.newChildId() , futureDtError));
					bizStartDtWarning.setDefaultModelObject(futureDtError);
				}else{
					final String bizDateCompoundAlert = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.bizDateCompoundAlert");
					final Calendar c30DayBefore = Calendar.getInstance();
					c30DayBefore.setTime(DateUtil.getCurrentDate());
					c30DayBefore.add(Calendar.DATE, -31);
					if(robFormAForm.getBizStartDt().before(c30DayBefore.getTime())){
						
						final String bizDateMore1YearNeedAttachment = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.bizDateMore1YearNeedAttachment");
						final Calendar c1YearBefore = Calendar.getInstance();
						c1YearBefore.setTime(DateUtil.getCurrentDate());
						c1YearBefore.add(Calendar.YEAR, -1);
						if(robFormAForm.getBizStartDt().before(c1YearBefore.getTime())){//More 1 Year
							bizStartDtWarning.setDefaultModelObject(bizDateCompoundAlert+"\n"+bizDateMore1YearNeedAttachment);
							if(!Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
								listError.add(new SSMLabel(listError.newChildId() ,bizDateMore1YearNeedAttachment));			
							}
						}else{//30 days only
							bizStartDtWarning.setDefaultModelObject(bizDateCompoundAlert);
						}
						
					}
					
				}
				target.add(bizStartDtWarning);
				target.add(bizNameWarning);
			}
			
			//Check partner verification
			for (int i = 0; i < listRobFormAOwner.size(); i++) {
				RobFormAOwner formAOwner = listRobFormAOwner.get(i);
				if(Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(formAOwner.getVerificationStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.notVerify", formAOwner.getName())));
				}
				if(Parameter.ROB_OWNER_VERI_STATUS_PENDING_ADD_UPDATE.equals(formAOwner.getVerificationStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.pendingAddressUpdate", formAOwner.getName())));
				}
				if(currentShowId==4 || currentShowId==3){//mean screen summary
					try {
						BusinessFormAOwnerValidResp resp = robFormAService.isOwnerValidWithIc(formAOwner.getName(),formAOwner.getNewicno());
						if(!resp.isValid()){
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.nameIcNotMatchInCoreSystem", formAOwner.getName())));
						}
					} catch (SSMException e) {
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.partnerValidateFailDueToWS")));
					}
				}
				
				//check formA blacklisted ic number
				try {
					BlacklistInfoResp response = robFormAOwnerService.getBlacklistInfoByICNoWS(formAOwner.getNewicno(), Parameter.ROB_BLACKLIST_TYPE_newIc, Parameter.ENTITY_TYPE_ROB); //03 = newIc
					if(response.getBlacklistStatus()) { //if true = blacklisted
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.robFormAOwners.ownerBlacklist", formAOwner.getName(), formAOwner.getNewicno())));
					}
				} catch (SSMException e) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.partnerValidateFailDueToWS"))); //msg = Webservice Problem!!!
				} 
				
			}
			
			
			//Check Bisness Code
			if(listRobFormABizCode.size()<1){
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.bizCode.notEnough")));
			}else{
				String[] internetCode = StringUtils.split(getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_FORM_A_INTERNET_CODE),",");
				
				boolean hasMoreThenInetCode = false;
				for (int i = 0; i < listRobFormABizCode.size(); i++) {
					boolean isInternetCode = false;
					for (int j = 0; j < internetCode.length; j++) {
						if(internetCode[j].equals(listRobFormABizCode.get(i).getBizCode())){
							isInternetCode=true;
							break;
						}
					}
					if(!isInternetCode){
						hasMoreThenInetCode = true;
						break;
					}
				}
				if(!hasMoreThenInetCode){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.bizCode.notEnoughToSupportActivities")));
				}
			}
			
			//Check is Incubator			
			if(Parameter.YES_NO_yes.equals(robFormAForm.isIncubator())){
				if(!Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.isIncubator.needAttachment")));			
				}
			}
			
			//Check is student incentive
			if(!UserEnvironmentHelper.isInternalUser()){
				if(Parameter.ROB_FORM_A_INCENTIVE_student.equals(robFormAForm.getIncentive())){
					
					try {
						robFormAOwnerService.checkIfAlreadyUseIncentive(listRobFormAOwner, robFormAForm.getIncentive(),robFormAForm.getRobFormACode());
					} catch (Exception e) {
						listError.add(new MultiLineLabel(listError.newChildId() , resolve("page.lbl.ezbiz.robFormA.incentive.oneTimeOnly") +"\n" +e.getMessage()) );
					}
					
//					for (int i = 0; i < listRobFormAOwner.size(); i++) {
//						RobFormAOwner formAOwner = listRobFormAOwner.get(i);
//						List<RobFormAOwner> result = robFormAOwnerService.checkIfAlreadyUseIncentive(formAOwner.getNewicno(), robFormAForm.getIncentive(), robFormAForm.getRobFormACode());
//						if(result.size()>0){
//							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oneTimeOnly") + " | Incentive used by : " + formAOwner.getName() +" for "+result.get(i).getRobFormACode()));
//						}
//					}
					
					if(!Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentiveStudent.needAttachment")));
					}
				}
				
				//Check is OKU incentive
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equals(robFormAForm.getIncentive())){

					if ((currentShowId==3)||(currentShowId==4)) { // if page Fees & Declaration and previous validate oku..for performance..
					//Check all partner registered in rob_user_oku and status approved.
					for (int i = 0; i < listRobFormAOwner.size(); i++) { //LOOP newly apply owner n partner.. 
						RobFormAOwner formAOwnerOku = listRobFormAOwner.get(i);
						LlpUserProfile llpUserProfileOku =llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, formAOwnerOku.getNewicno()); //semua partner mesti sudah daftar ezbiz (status A)
						
						RobUserOku robUserOku = new RobUserOku();
						if(Parameter.USER_STATUS_active.equals(llpUserProfileOku.getUserStatus())){
							robUserOku = robUserOkuService.findOkuByUserRefNo(llpUserProfileOku.getUserRefNo()); //find latest record (maybe ada partner yg tak daftar lg)
						}else {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.userProfileNotActive", formAOwnerOku.getName(), formAOwnerOku.getNewicno())));
						}
						
						if(robUserOku!=null) {
							if (!Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus())) {
								listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.statusNotAllowed", robUserOku.getOkuRegStatus(), formAOwnerOku.getName(), formAOwnerOku.getNewicno())));
							}
						}else {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.notRegister", formAOwnerOku.getName(), formAOwnerOku.getNewicno()))); //newly added partner kene check daftar oku atau tidak.
						}
						
						
						//Check dalam table rob_incentive.
						String lastStatusAndAppRefNo = robIncentiveService.checkLastApplicationStatus(formAOwnerOku.getNewicno(), Parameter.ROB_FORM_A_INCENTIVE_oku, Parameter.ROB_FORM_TYPE_A);
						if((StringUtils.isNotBlank(lastStatusAndAppRefNo))) {
							String lastStatus = StringUtils.split(lastStatusAndAppRefNo,"_")[0];
							String formCode = StringUtils.split(lastStatusAndAppRefNo,"_")[1];
							
							//System.out.println("formCode: "+formCode);
							//System.out.println("robFormAForm.getRobFormACode(): "+robFormAForm.getRobFormACode());
							if (!(formCode.equals(robFormAForm.getRobFormACode()))) {
							if(Parameter.ROB_FORM_A_STATUS_IN_PROCESS.equalsIgnoreCase(lastStatus)||Parameter.ROB_FORM_A_STATUS_QUERY.equalsIgnoreCase(lastStatus)) {
								listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.previousApplicationInProcess", formCode, formAOwnerOku.getNewicno(), formAOwnerOku.getName())));
							}
						  }
						}
						
							//CADANGAN UNTUK ENHANCEMENT-TO CATER PERNAH DPT INSENTIF FORM A LEBIH DARI SEKALI..CHECK SEMULA OWNERSHIP DAN STATUS SETIAP BIZ TERSEBUT..kena loop list table robincentive (semua Form A yg status A)..check ngan user dahulu!..
							//1) search by ic (utk cater data cbs robform+robowner migration dalam table rob_incentive-ada ic tp userRefNo/okuRefNo null).
							RobIncentive robIncentive = robIncentiveService.getLastIncentiveByNewIcNo(formAOwnerOku.getNewicno(), Parameter.ROB_FORM_A_INCENTIVE_oku);
							
							//jumpa previous incentive (cari status brNo dan ownerLink di cbs guna webis)
							if(robIncentive!=null) {
								try {
									BizProfileDetResp bizProfileDetail = robFormAService.getBusinessProfileDetailWs(robIncentive.getBrNo());
									if(bizProfileDetail!=null) {
									boolean canRenew = false;
									if(Parameter.CBS_BUSINESS_STATUS_EXPIRED.equals(bizProfileDetail.getMainInfo().getStatus())){
										Calendar calBizExp = Calendar.getInstance();
										calBizExp.setTime(bizProfileDetail.getMainInfo().getBizExpDate());
										calBizExp.add(Calendar.MONTH, 12); //to check within 12 months can renew

										Calendar calNow = Calendar.getInstance();
										calNow.setTime(new Date());
//										System.out.println("BizExpFate+12month: "+calBizExp);
										
										if (calNow.before(calBizExp)) {
											canRenew = true;
										}
									}
									
									//Jika existing business(lama) dah terminate atau luput melebihi 12 bulan, bole proceed incentive utk biz yg baru.
									if((Parameter.CBS_BUSINESS_STATUS_ACTIVE.equals(bizProfileDetail.getMainInfo().getStatus()))||(canRenew)){
										
										//check business status OKU (all owner oku) atau tidak..
										boolean isAllOwnerOku = true;
										RobFormOwnerInfo[] activeOwnerOku = bizProfileDetail.getActiveOwnerInfo(); //jika oku add partner non-oku kat B4, kuota oku akan release semula.
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
										
									if(isAllOwnerOku) {
									//check status owner dalam robbusinessownerlink, jika status inactive(cbs ownerlink T) boleh proceed biz baru.
										RobFormOwnerInfo[] activeOwner = bizProfileDetail.getActiveOwnerInfo(); 
										for (int j = 0; j < activeOwner.length; j++) {
											if (formAOwnerOku.getNewicno().equals(activeOwner[j].getIcNo())){ //if active owner (form A newBiz compare with existingBiz) 
												listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.currentOkuBizAndOwnerActive", bizProfileDetail.getMainInfo().getBizName(), 
														activeOwner[j].getName(),bizProfileDetail.getMainInfo().getBrNo(),bizProfileDetail.getMainInfo().getChkDigit(),activeOwner[j].getIcNo())));
											}
										}
									}
								  }
								} //bizProfileDetail not null
								} catch (SSMException e) {
									listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.incentive.oku.getBizProfileDetFailDueToWs")));
								}
							}	
					 	}		
				     } // end currentShowId check..		
				  }		
			   }	
			
			
			//Check Partnership Agreement
			if(robFormAForm.getBizPartnershipAgreementDate()!=null){
				if(listRobFormAOwner.size()==1){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnershipAggreement.noNeed")));
				}
			}
			
			if(listRobFormAOwner.size()>20){
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnerCannotMore20")));
			}
			
			if(currentShowId!=4){
				String js = "$('#"+wmcFeeSummaryAll.getMarkupId()+"').hide();"; 
				target.appendJavaScript(js);
			}
			
			if(Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
				removeSupportingDoc.setVisible(true);
				downloadSupportingDoc.setDownloadData("SUPPORTING.pdf", "application/pdf", robFormA.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
			}else{
				removeSupportingDoc.setVisible(false);
				downloadSupportingDoc.setVisible(false);
			}
			
			if(listError.size()>0){
				listError.setVisible(true);
				declarationChkBox.setVisible(false);
				previewToPayment.setEnabled(false);
				submitVerification.setEnabled(false);
				reLodgeFormA.setEnabled(false);
			}else{
				listError.setVisible(false);
				declarationChkBox.setVisible(true);
				previewToPayment.setEnabled(false);
				submitVerification.setEnabled(false);
				reLodgeFormA.setEnabled(false);
			}			
			
			target.add(wmcFeeSummaryAll);
		}

		public WebMarkupContainer getWmcAddress() {
			return wmcAddress;
		}
		
		public final void hideAndShowSegment(AjaxRequestTarget target, int segmentIdToShow, WebMarkupContainer[]  segementLeftMenu, WebMarkupContainer[] segmentContainer){
			
			for (int i = 0; i < segementLeftMenu.length; i++) {
				try {
					List<AttributeModifier> amList = segementLeftMenu[i].getBehaviors(AttributeModifier.class);
					for (int j = 0; j < amList.size(); j++) {
						if(amList.get(j).getAttribute().equals("class")){
							segementLeftMenu[i].remove(amList.get(j));
							break;
						}
					}
				} catch (Exception e) {
				}
				
				if(i == segmentIdToShow){
					segementLeftMenu[i].add(new AttributeModifier("class", new Model("active step")));
				}else{
					segementLeftMenu[i].add(new AttributeModifier("class", new Model("step")));
				}
				target.add(segementLeftMenu[i]);
			}
			
			String js = "var toOpts = { direction: 'right' };";
			for (int i = 0; i < segmentContainer.length; i++) {
				if(i == segmentIdToShow){
					continue;
				}
				js += "$('#"+segmentContainer[i].getMarkupId()+"').hide();"; 
			}
			js += "if($('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').is(':hidden')){";
			js += "$('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').toggle('slide', toOpts, 500).is(':hidden');"; 
			js += "}";
			
			String scroll = "\n$.scrollTo(document.getElementById('"+segmentContainer[segmentIdToShow].getMarkupId()+"'),100);\n";
			js += scroll;
			
			target.appendJavaScript(js);
			currentShowId = segmentIdToShow;
		}
		
		
	}
	
	
	
	private final void copyFromUserProfileToRobOwner(LlpUserProfile llpUserProfile, RobFormAOwner robFormAOwner, boolean copyAddrAlso) {
		robFormAOwner.setEzbizLoginName(llpUserProfile.getLoginId());
		robFormAOwner.setName(llpUserProfile.getName());
		robFormAOwner.setDob(llpUserProfile.getDob());
		robFormAOwner.setNewicno(llpUserProfile.getIdNo());
		robFormAOwner.setGender(llpUserProfile.getGender());
		robFormAOwner.setRace(llpUserProfile.getRace());
		robFormAOwner.setOtherrace(llpUserProfile.getOthersRace());
		robFormAOwner.setNationality(llpUserProfile.getNationality());
		robFormAOwner.setEmail(llpUserProfile.getEmail());
		
		if(copyAddrAlso){
			robFormAOwner.setAddr(llpUserProfile.getAdd1());
			robFormAOwner.setAddr2(llpUserProfile.getAdd2());
			robFormAOwner.setAddr3(llpUserProfile.getAdd3());
			robFormAOwner.setAddrTown(llpUserProfile.getCity());
			robFormAOwner.setAddrPostcode(llpUserProfile.getPostcode());
			
			String cbsState = getCodeTypeWithValue(Parameter.STATE_CODE_TO_CBS_CODE,llpUserProfile.getState());
			robFormAOwner.setAddrState(cbsState);
			
			robFormAOwner.setMobileNo(llpUserProfile.getHpNo());
		}else{
			robFormAOwner.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_PENDING_ADD_UPDATE);
		}
		
		
		
	}
	
	
	@Override
	public String getPageTitle() {
		return "page.title.mybiz.robFormA";
	}

}
