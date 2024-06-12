package com.ssm.ezbiz.robFormC;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ViewRobFormCDetailPage extends SecBasePage{
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	private DecimalFormat currencyDecFormat = new DecimalFormat("#0.00");
	
	
	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	
	
	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;
	
	
	@SpringBean(name="LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	
	public ViewRobFormCDetailPage(final String robFormCCode, Page fromPage) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormC robFormC = robFormCService.findAllById(robFormCCode);
				return robFormC;
			}
		}));
		init(fromPage);
		// TODO Auto-generated constructor stub
	}

	

	private void init(Page fromPage) {
		add(new RobFormCForm("form", getDefaultModel(), fromPage));
	}
	
	private class RobFormCForm extends Form implements Serializable {
		
		final WebMarkupContainer wmcformCfileUpload;

		RepeatingView listError;
		final WebMarkupContainer wmcFeeSummaryAll;
		final SSMAjaxButton submitPayment,reLodgeFormC;
		final SSMAjaxButton saveDraftFormC;
		
		
		//final WebMarkupContainer wmccompoundFormC;
		boolean isQuery;
		
		final SSMLabel cmpFormCFee;
		final SSMLabel cmpFormCFeeQuantity;
		final SSMLabel totalCmpFormCFee;
		
		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		
		final SSMLabel totalFee;
		final SSMLabel summaryError;
		
		final LlpPaymentFee cmpFee;
		final LlpPaymentFee businessInfoPaymentFee;
		
		final RobFormC robFormC;
		
		
		final SSMTextField robFormCCode;
		final Page fromPage;
		
		final LlpUserProfile currentLlpUserProfile;
		
		public RobFormCForm(String id, IModel m, Page fPage) {
			super(id, m);
			this.fromPage = fPage;
			cmpFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			currentLlpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			String prefixLabelKey = "page.lbl.ezbiz.robFormC.";
			setPrefixLabelKey(prefixLabelKey);

			
			robFormC = (RobFormC) m.getObject();

			
			
			SSMLabel brNo = new SSMLabel("brNo", robFormC.getBrNo() + "-"+ robFormC.getCheckDigit());
			add(brNo);

			SSMLabel bizName = new SSMLabel("bizName", robFormC.getBizName());
			add(bizName);

			String terminationDtStr = "";
			if (robFormC.getTerminationDt() != null) {
				terminationDtStr = sdf.format(robFormC.getTerminationDt());
			}
			SSMTextField terminationDt = new SSMTextField("terminationDt",new PropertyModel(terminationDtStr, ""));
			terminationDt.setReadOnly(true);
			add(terminationDt);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			add(listError);
			
			robFormCCode = new SSMTextField("robFormCCode");
			robFormCCode.setReadOnly(true);
			robFormCCode.setOutputMarkupId(true);
			add(robFormCCode);
			
			
//			wmcCourtOrder = new WebMarkupContainer("wmcCourtOrder");
//			wmcCourtOrder.setPrefixLabelKey(prefixLabelKey);
//			wmcCourtOrder.setOutputMarkupId(true);
//			wmcCourtOrder.setOutputMarkupPlaceholderTag(true);
//			wmcCourtOrder.setVisible(false);
//			add(wmcCourtOrder);
//			
//			
//			wmcDeceasedDt = new WebMarkupContainer("wmcDeceasedDt");
//			wmcDeceasedDt.setPrefixLabelKey(prefixLabelKey);
//			wmcDeceasedDt.setOutputMarkupId(true);
//			wmcDeceasedDt.setOutputMarkupPlaceholderTag(true);
//			wmcDeceasedDt.setVisible(false);
//			add(wmcDeceasedDt);
			
			
			wmcformCfileUpload = new WebMarkupContainer("wmcformCfileUpload");
			wmcformCfileUpload.setPrefixLabelKey(prefixLabelKey);
			wmcformCfileUpload.setOutputMarkupId(true);
			wmcformCfileUpload.setOutputMarkupPlaceholderTag(true);
			wmcformCfileUpload.setVisible(false);
			add(wmcformCfileUpload);
			
			
//			wmccompoundFormC = new WebMarkupContainer("wmccompoundFormC");
//			wmccompoundFormC.setPrefixLabelKey(prefixLabelKey);
//			wmccompoundFormC.setOutputMarkupId(true);
//			wmccompoundFormC.setOutputMarkupPlaceholderTag(true);
//			wmccompoundFormC.setVisible(false);
//			add(wmccompoundFormC);
//			
			
			//Fee Summary	
			wmcFeeSummaryAll = new WebMarkupContainer("wmcFeeSummaryAll");
			wmcFeeSummaryAll.setPrefixLabelKey(prefixLabelKey+"feeSummary.");
			wmcFeeSummaryAll.setOutputMarkupId(true);
			wmcFeeSummaryAll.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummaryAll);
			
			
			
			SSMTextArea	queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(false);
			wmcFeeSummaryAll.add(queryAnswer);
			
			SSMTextArea notes = new SSMTextArea("notes",Model.of(""));
			notes.setVisible(false);
			notes.setReadOnly(true);
			wmcFeeSummaryAll.add(notes);
			
			if(Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
				isQuery = true;
				RobFormNotes formNotes = robFormC.getListRobFormNotes().get(robFormC.getListRobFormNotes().size()-1);
				notes.setDefaultModelObject(formNotes.getNotes());
				notes.setVisible(true);
				if(StringUtils.isNotBlank(formNotes.getNotesAnswer())){
					robFormC.setQueryAnswer(formNotes.getNotesAnswer());
				}
			}else{
				isQuery = false;
			}
			
			
			cmpFormCFee = new SSMLabel("cmpFormCFee","");
			add(cmpFormCFee);
			wmcFeeSummaryAll.add(cmpFormCFee);
			
			cmpFormCFeeQuantity = new SSMLabel("cmpFormCFeeQuantity","");
			add(cmpFormCFeeQuantity);
			wmcFeeSummaryAll.add(cmpFormCFeeQuantity);
			
		
			totalCmpFormCFee = new SSMLabel("totalCmpFormCFee","");
			add(totalCmpFormCFee);
			wmcFeeSummaryAll.add(totalCmpFormCFee);
			
			bisnessInfoFee = new SSMLabel("bisnessInfoFee","");
			add(bisnessInfoFee);
			wmcFeeSummaryAll.add(bisnessInfoFee);
			
			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity","");
			add(bisnessInfoFeeQuantity);
			wmcFeeSummaryAll.add(bisnessInfoFeeQuantity);
			
			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee","");
			add(totalBisnessInfoFee);
			wmcFeeSummaryAll.add(totalBisnessInfoFee);
			
			
			totalFee = new SSMLabel("totalFee","");
			add(totalFee);
			wmcFeeSummaryAll.add(totalFee);
			
			summaryError = new SSMLabel("summaryError","");
			summaryError.setEscapeModelStrings(true);
			add(summaryError);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			wmcFeeSummaryAll.add(listError);
			
		
			
			final String mainValidationJS = "mainValidation";
			String mainFieldToValidate[] = new String[]{"reasonType","terminationDt,"};
			String mainFieldToValidateRules[] = new String[]{"empty","empty"};
			setSemanticJSValidation(this, mainValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			final String reLodgeFormCValidationJS = "reLodgeFormCValidation";
			String reLodgeFormCValidation[] = new String[]{"queryAnswer"};
			String reLodgeFormCValidationRules[] = new String[]{"empty"};
			setSemanticJSValidation(wmcFeeSummaryAll, reLodgeFormCValidationJS, reLodgeFormCValidation, reLodgeFormCValidationRules);
			
			
			SSMAjaxFormSubmitBehavior isReasonTypeOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
					
//					if(Parameter.ROB_FORM_C_REASON_P.equals(robFormCForm.getReasonType())){
//						wmcCourtOrder.setVisible(true);
//						wmcDeceasedDt.setVisible(false);
//						//wmcformCfileUpload.setVisible(false);
//						//wmccompoundFormC.setVisible(true);
//					}else if((Parameter.ROB_FORM_C_REASON_M).equals(robFormCForm.getReasonType())){
//						wmcCourtOrder.setVisible(false);
//						wmcDeceasedDt.setVisible(true);
//						//wmcformCfileUpload.setVisible(true);
//						//wmccompoundFormC.setVisible(true);
//					}else if((Parameter.ROB_FORM_C_REASON_M).equals(robFormCForm.getReasonType())){
//						wmcCourtOrder.setVisible(false);
//						wmcDeceasedDt.setVisible(false);
//						//wmcformCfileUpload.setVisible(true);
//					}else{
//						wmcCourtOrder.setVisible(false);
//						wmcDeceasedDt.setVisible(false);
//						//wmcformCfileUpload.setVisible(false);
//						//wmccompoundFormC.setVisible(true);
//					}
					
					//target.add(wmcCourtOrder);
					//target.add(wmcDeceasedDt);
					//target.add(wmcformCfileUpload);
					//target.add(wmccompoundFormC);
				}
			};
			
			

			final SSMDropDownChoice reasonType = new SSMDropDownChoice("reasonType", Parameter.ROB_FORM_C_REASON);
			reasonType.setReadOnly(true);
			add(reasonType);

			SSMTextField reasonTypeDesc = new SSMTextField("reasonTypeDesc",Model.of(""));
			reasonTypeDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormC.getReasonType()));
			reasonTypeDesc.setVisible(false);
			reasonTypeDesc.setReadOnly(true);
			add(reasonTypeDesc);
			
			/*
			final SSMDropDownChoice reasonType = new SSMDropDownChoice("reasonType", Parameter.ROB_FORM_C_REASON);
			reasonType.add(isReasonTypeOnchange);
			add(reasonType);
			
			SSMDateTextField courtOrderDt = new SSMDateTextField("courtOrderDt");
			wmcCourtOrder.add(courtOrderDt);
			
			SSMDateTextField deceasedDt = new SSMDateTextField("deceasedDt");
			wmcDeceasedDt.add(deceasedDt);
			
			final SSMTextArea reasonOthers = new SSMTextArea("reasonOthers");
			add(reasonOthers);
			
			
			SSMTextField isBuyInfoDesc = new SSMTextField("isBuyInfoDesc", Model.of(""));
			isBuyInfoDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormC.getIsBuyInfo()));
			isBuyInfoDesc.setVisible(false);
			isBuyInfoDesc.setReadOnly(true);
			add(isBuyInfoDesc);
			
			SSMAjaxFormSubmitBehavior isBuyInfoOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
					recalculateFeeFormC(target, robFormCForm);
				}
			};
			
			SSMDropDownChoice isBuyInfo = new SSMDropDownChoice("isBuyInfo", Parameter.YES_NO);
			isBuyInfo.setOutputMarkupId(true);
			isBuyInfo.setOutputMarkupPlaceholderTag(true);
			isBuyInfo.setEscapeModelStrings(false);
			isBuyInfo.add(isBuyInfoOnchange);
			if(isQuery){
				isBuyInfo.setVisible(false);
				isBuyInfoDesc.setVisible(true);
			}
			add(isBuyInfo);
*/
			/*
			final SSMTextArea reasonOthers = new SSMTextArea("reasonOthers");
			reasonOthers.setReadOnly(true);
			add(reasonOthers);

			String courtOrderDtStr = "";
			if (robFormC.getCourtOrderDt() != null) {
				courtOrderDtStr = sdf.format(robFormC.getCourtOrderDt());
			}
			/*
			SSMTextField courtOrderDt = new SSMTextField("courtOrderDt", new PropertyModel(courtOrderDtStr, ""));
			courtOrderDt.setReadOnly(true);
			wmcCourtOrder.add(courtOrderDt);

			String deceasedDtStr = "";
			if (robFormC.getDeceasedDt() != null) {
				deceasedDtStr = sdf.format(robFormC.getDeceasedDt());
			}
			SSMTextField deceasedDt = new SSMTextField("deceasedDt",new PropertyModel(deceasedDtStr, ""));
			deceasedDt.setReadOnly(true);
			wmcDeceasedDt.add(deceasedDt);
			
			if(Parameter.ROB_FORM_C_REASON_P.equals(robFormC.getReasonType())){
				wmcCourtOrder.setVisible(true);
				wmcDeceasedDt.setVisible(false);
			}else if((Parameter.ROB_FORM_C_REASON_M).equals(robFormC.getReasonType())){
				wmcCourtOrder.setVisible(false);
				wmcDeceasedDt.setVisible(true);
			}else{
				wmcCourtOrder.setVisible(false);
				wmcDeceasedDt.setVisible(false);
			}
			
			*/
			
			String isBuyInfoDesc = getCodeTypeWithValue(Parameter.YES_NO,robFormC.getIsBuyInfo());
			SSMTextField isBuyInfo = new SSMTextField("isBuyInfo",new PropertyModel(isBuyInfoDesc, ""));
			isBuyInfo.setReadOnly(true);
			add(isBuyInfo);

			
			FileUploadField fileUploadTmp = new FileUploadField("fileUploadTmp");
			add(fileUploadTmp);
			setMultiPart(true);
			
			
			SSMDownloadLink downloadAttachmentCForm = new SSMDownloadLink("downloadAttachmentCForm");
			add(downloadAttachmentCForm);
			
			if(robFormC.getSupportingDocId()==null){
				downloadAttachmentCForm.setVisible(false);
			}else{
				LlpFileData fileDataNew = new LlpFileData();
				fileDataNew = llpFileDataService.findById(robFormC.getSupportingDocId());
				downloadAttachmentCForm.setDownloadData(robFormC.getBrNo() + "_BORANG_C.pdf", "application/pdf", fileDataNew.getFileData());
				downloadAttachmentCForm.setVisible(true);
			}
			
			final EditRobFormCOwnerVerificationPanel cOwnerVerificationPanel = new EditRobFormCOwnerVerificationPanel("cOwnerVerificationPanel", robFormC);
			cOwnerVerificationPanel.setOutputMarkupId(true);
			cOwnerVerificationPanel.setOutputMarkupPlaceholderTag(true);
			add(cOwnerVerificationPanel);
			
		
			cOwnerVerificationPanel.setAllPanel(cOwnerVerificationPanel);
		
			
			String hideAllJs =  "";
			hideAllJs +=  "$('#"+cOwnerVerificationPanel.getMarkupId()+"').hide();"; 
		
	
			Label hideAllLbl = new Label("hideAllLbl", hideAllJs);
			hideAllLbl.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		    add(hideAllLbl);
			

		    final SSMLabel cmpAmt = new SSMLabel("cmpAmt");
		    add(cmpAmt);
		   // wmccompoundFormC.add(cmpAmt);
	
			
			SSMAjaxFormSubmitBehavior terminationDtOnBlur = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
					recalculateFeeFormC(target, robFormCForm);
				}
			};
			
			/*
			SSMDateTextField terminationDt = new SSMDateTextField("terminationDt");
			terminationDt.setOutputMarkupId(true);
			terminationDt.setOutputMarkupPlaceholderTag(true);
			terminationDt.setEscapeModelStrings(false);
			terminationDt.add(terminationDtOnBlur);
			add(terminationDt);
			
			String msg = "";

			terminationDtWarning= new SSMLabel("terminationDtWarning",msg);
			terminationDtWarning.setOutputMarkupId(true);
			terminationDtWarning.setOutputMarkupPlaceholderTag(true);
			terminationDtWarning.setEscapeModelStrings(false);
			add(terminationDtWarning);
			
		*/
			
	
			boolean isPayCmpBool = false;
			if(Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp())){
				isPayCmpBool=true;
			}
			SSMAjaxCheckBox isPayCmp = new SSMAjaxCheckBox("isPayCmp", new PropertyModel(robFormC, "isPayCmp") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
				
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
						
						if (String.valueOf(true).equals(robFormCForm.getIsPayCmp())) {
							robFormCForm.setIsBuyInfo(Parameter.YES_NO_yes);
						} else {
							robFormCForm.setIsBuyInfo(Parameter.YES_NO_no);
						}

						recalculateFeeFormC(target, robFormCForm);
					}
			};

			add(isPayCmp);
			//isPayCmp.setOutputMarkupId(true);
			//wmccompoundFormC.add(isPayCmp);
			

			
			
			
			boolean checkVerification = checkVerificationOwner(robFormC.getListRobFormOwnerVerification());
		/*	
			declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robFormC, "declarationChkBox") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (String.valueOf(true).equals(getValue())) {
						robFormC.setDeclarationChkBox(true);
					} else {
						robFormC.setDeclarationChkBox(false);
					}
					
					if(submitPayment.isVisible()){
						submitPayment.setEnabled(robFormC.getDeclarationChkBox());
				        arg0.add(submitPayment);	
					}
						
					
					if(reLodgeFormC.isVisible()){
						reLodgeFormC.setEnabled(robFormC.getDeclarationChkBox());
						arg0.add(reLodgeFormC);
					}
					
				}

				
			};
			
			System.out.println("checkVerification"+checkVerification);
			
			if(checkVerification){
				declarationChkBox.setEnabled(true);
			}else{
				declarationChkBox.setEnabled(false);
			}
			wmcFeeSummaryAll.add(declarationChkBox);
			
			*/
			
			if(isQuery){
				queryAnswer.setVisible(true);
			}
			
			
			saveDraftFormC = new SSMAjaxButton("saveDraftFormC", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					System.out.println("list Error:"+listError.size());
					
					if(listError.size()>0){
						return;
					}
					
					try {
						updateAll(target, form);
					} catch (SSMException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp())&& robFormC.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
						paymentItem1.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_C_COMPOUND);
						paymentItem1.setQuantity(1);
						paymentItem1.setPaymentDet(robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
						paymentItem1.setAmount(robFormC.getCmpAmt());
						listPaymentItems.add(paymentItem1);
					}
					
					
					if(Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormC.getBizInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()*getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}
					storeSuccessMsgKey("page.lbl.ezbiz.formC.successSaveDraft");
					setResponsePage(new ListRobFormCTransactionPage());
				}
			};
			saveDraftFormC.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(saveDraftFormC);
			
			
			
			submitPayment = new SSMAjaxButton("submitPayment", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					System.out.println("list Error:"+listError.size());
					if(listError.size()>0){
					return;
					}
					
					try {
						updateAll(target, form);
					} catch (SSMException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp())&& robFormC.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
						paymentItem1.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_C_COMPOUND);
						paymentItem1.setQuantity(1);
						paymentItem1.setPaymentDet(robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
						paymentItem1.setAmount(robFormC.getCmpAmt());
						listPaymentItems.add(paymentItem1);
					}
					
					
					if(Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormC.getBizInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()*getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}

					
					if(listPaymentItems.size()>0){
						setResponsePage(new PaymentDetailPage(robFormC.getRobFormCCode(), RobFormAService.class.getSimpleName(), robFormC,listPaymentItems));
					}else{
						storeSuccessMsgKey("page.lbl.ezbiz.formC.successSubmit");
						setResponsePage(new ListRobFormCTransactionPage());
					}
				}
			};
			submitPayment.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(submitPayment);
			
		
			
			
			reLodgeFormC = new SSMAjaxButton("reLodgeFormC", reLodgeFormCValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
					
					try {
						updateAll(target, form);
						
						robFormCService.relodgeRobFormC(robFormC);
						storeSuccessMsgKey("page.lbl.ezbiz.formC.successRelodge");
						setResponsePage(new ListRobFormCTransactionPage());
					} catch (Exception e) {
						ssmError(e.getMessage());
						FeedbackPanel feedbackPanel =  ((EditRobFormCPage)getPage()).getFeedbackPanel();
						target.add(feedbackPanel);
						
						String scroll = "\n$.scrollTo(document.getElementById('"+getFeedbackPanel().getMarkupId()+"'),100);\n";
						target.appendJavaScript(scroll);
					}
				}
			};
			reLodgeFormC.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(reLodgeFormC);
			
		/*	
			wmcFeeSummaryAll.add(new Button("done") {
				public void onSubmit() {
					if (("Done").equals(robFormC.getPage())) {
						setResponsePage(ListRobFormCPage.class);
					} else {
						setResponsePage(SearchRobFormC.class);
					}
				}
			}.setDefaultFormProcessing(false));
			
			*/
			
			SSMAjaxButton backButton = new SSMAjaxButton("backButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						setResponsePage(fromPage);
				}
			};
			wmcFeeSummaryAll.add(backButton);
			
			
			
			if(isQuery && checkVerification){
				submitPayment.setVisible(false);
				reLodgeFormC.setVisible(true);
				saveDraftFormC.setVisible(false);
			}else if(!isQuery && !checkVerification){
				reLodgeFormC.setVisible(false);
				submitPayment.setVisible(false);
				saveDraftFormC.setVisible(false);
			}else{
				submitPayment.setVisible(false);
				reLodgeFormC.setVisible(false);
				saveDraftFormC.setVisible(false);
			}
			
			/*
			wmcFeeSummaryAll.add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListRobFormCPage.class);
				}
			}.setDefaultFormProcessing(false));
			*/
			
			SSMDownloadLink downloadBusinessInfo = new SSMDownloadLink("downloadBusinessInfo");
			downloadBusinessInfo.setVisible(false);
			wmcFeeSummaryAll.add(downloadBusinessInfo);
			
			SSMDownloadLink downloadBorangCForm = new SSMDownloadLink("downloadBorangCForm");
			downloadBorangCForm.setVisible(false);
			wmcFeeSummaryAll.add(downloadBorangCForm);
			
			
			if(Parameter.ROB_FORM_C_STATUS_APPROVED.equals(robFormC.getStatus())){
				RobFormC robFormCTmp = null;
				try {
					robFormCTmp = robFormCService.findByIdWithData(robFormC.getRobFormCCode());
					LlpFileData fileDataNew = new LlpFileData();
					fileDataNew = llpFileDataService.findById(robFormCTmp.getFormCDataId());
					downloadBorangCForm.setDownloadData(robFormCTmp.getBrNo() + "_BORANG_C.pdf", "application/pdf", fileDataNew.getFileData());
					downloadBorangCForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())){
					try {
						LlpFileData fileDataNew = new LlpFileData();
						fileDataNew = llpFileDataService.findById(robFormCTmp.getBusinessInfoDataId());
						downloadBusinessInfo.setDownloadData(robFormCTmp.getBrNo() + "_BIS_INFO.pdf", "application/pdf", fileDataNew.getFileData());
						downloadBusinessInfo.setVisible(true);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			recalculateFeeFormC(null, robFormC);
		}

		protected void recalculateFeeFormC(AjaxRequestTarget target, RobFormC robFormCForm) {

			double gstAmt = 0;
			double businessInfoAmt = 0;
		

				double totalFeeDouble = 0;
				
				
				double bisnessInfoFeeDouble = 0.00;
				int bisnessInfoFeeQuantityInt = 0;
				double totalBisnessInfoFeeDouble = 0;
				
				System.out.println("robFormCForm.getIsBuyInfo():"+robFormCForm.getIsBuyInfo());
			
				if(Parameter.YES_NO_yes.equals(robFormCForm.getIsBuyInfo())){
					bisnessInfoFeeQuantityInt = 1;
					bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
					totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
					if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
						totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
						gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
					}
					businessInfoAmt = businessInfoPaymentFee.getPaymentFee()+gstAmt;
				}
				
				
				
				
				double totalCmpFeeDouble = 0;
				int cmpFeeQuantityInt = 0;
				double cmpFeeDouble =0.00;
				System.out.println("Test IS PAY CMP:"+robFormCForm.getIsPayCmp());
				
				
				if (Parameter.YES_NO_yes.equals(robFormCForm.getIsPayCmp())) {
					cmpFeeDouble = robFormCForm.getCmpAmt();
					cmpFeeQuantityInt = 1;
					totalCmpFeeDouble = cmpFeeDouble;
				}
				
				totalFeeDouble =  + totalBisnessInfoFeeDouble +totalCmpFeeDouble;
				
				
				DecimalFormat df = new DecimalFormat("#0.00");
				
				
				bisnessInfoFee.setDefaultModelObject(df.format(businessInfoAmt));
				bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
				totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
				
				
				cmpFormCFee.setDefaultModelObject(df.format(cmpFeeDouble));
				cmpFormCFeeQuantity.setDefaultModelObject(String.valueOf(cmpFeeQuantityInt));
				totalCmpFormCFee.setDefaultModelObject(df.format(totalCmpFeeDouble));
				
				
				totalFee.setDefaultModelObject(df.format(totalFeeDouble));
				System.out.println("list Error:"+listError.size());
				
				listError.removeAll();
				robFormCForm.setTotalAmt(totalFeeDouble);
			
				
				if(listError.size()>0){
					listError.setVisible(true);
					//declarationChkBox.setVisible(false);
					submitPayment.setEnabled(false);
					reLodgeFormC.setEnabled(false);
				}else{
					listError.setVisible(false);
					//declarationChkBox.setVisible(true);
					submitPayment.setEnabled(false);
					reLodgeFormC.setEnabled(false);
				}			
				if(target!=null){
					target.add(wmcFeeSummaryAll);
				}
		
	}
	
		protected void updateAll(AjaxRequestTarget target, Form<?> form) throws SSMException {
			FeedbackPanel feedbackPanel =  ((EditRobFormCPage)getPage()).getFeedbackPanel();
			feedbackPanel.getFeedbackMessages().clear();
			target.add(feedbackPanel);
			
			
			RobFormC robFormCForm = (RobFormC) form.getDefaultModelObject();

			if(robFormCForm.getFileUploadTmp().size()>0){
				FileUpload fileUploadNew =  robFormCForm.getFileUploadTmp().get(0);

				Long supportingDocIdNew = insertFileUpload(fileUploadNew, robFormCForm);
				robFormC.setSupportingDocId(supportingDocIdNew);

				if (robFormC.getSupportingDocId()==0) {
					robFormC.setIsHasSupportingDoc(Parameter.YES_NO_no);
				}else{
					robFormC.setIsHasSupportingDoc(Parameter.YES_NO_yes);
				}
				
			}
			
			robFormC.copyFrom(robFormCForm);
			recalculateFeeFormC(target, robFormC);
			boolean isNew = false;
			if(StringUtils.isBlank(robFormC.getRobFormCCode())){
				isNew = true;
			}
			
			robFormCService.insertUpdateAll(robFormC);
			form.setDefaultModelObject(robFormC);
			
			
			if(isNew){
				robFormCCode.setDefaultModelObject(robFormC.getRobFormCCode());
				target.add(robFormCCode);
			}
		}
		
	}
	


	@Override
	public String getPageTitle() {
		return "page.title.mybiz.bizCloseDetail";
	}





	public Long insertFileUpload(FileUpload fileUploadNew, RobFormC robFormCForm) {
		
		LlpFileData fileData = new LlpFileData();
		if(fileUploadNew!=null){
			fileData.setFileData(fileUploadNew.getBytes());
			fileData.setFileDataType(fileUploadNew.getContentType());
		}
		
		if((Parameter.YES_NO_yes).equalsIgnoreCase(robFormCForm.getIsHasSupportingDoc())){
			llpFileDataService.update(fileData);
		}else{
			llpFileDataService.insert(fileData);
		}
			
		return fileData.getFileDataId();
	
	}
	
	public boolean checkVerificationOwner(List<RobFormOwnerVerification> listRobFormOwnerVerification) {
		// TODO Auto-generated method stub
		
		boolean checkVerification = true;
		for(int i=0; i<listRobFormOwnerVerification.size(); i++){
			RobFormOwnerVerification robFormOwnerVerification = (RobFormOwnerVerification) listRobFormOwnerVerification.get(i);
			
			System.out.println("robFormOwnerVerification.getStatus():"+robFormOwnerVerification.getStatus());
			
			if(!robFormOwnerVerification.getStatus().equals(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED)){
				checkVerification=false;
				return checkVerification;
			}
		}
		
		return checkVerification;
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
}
