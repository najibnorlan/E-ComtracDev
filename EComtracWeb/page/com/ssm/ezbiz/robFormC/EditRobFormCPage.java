package com.ssm.ezbiz.robFormC;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.base.common.util.DateUtil;
import com.ssm.ezbiz.robFormB.TabRobFormBPage;
import com.ssm.ezbiz.robFormB.ViewRobFormBPage;
import com.ssm.ezbiz.service.RobCmpConfigService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.ws.RobBusinessSummaryInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormCPage extends SecBasePage {
	protected static final AjaxRequestTarget AjaxRequestTarget = null;

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	private DecimalFormat currencyDecFormat = new DecimalFormat("#0.00");

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;

	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	@SpringBean(name = "RobCmpConfigService")
	private RobCmpConfigService robCmpConfigService;
	
	

	public EditRobFormCPage(final RobBusinessSummaryInfo robBusinessSummaryInfo) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormC robFormC = new RobFormC();
				try {
					robFormC = robFormCService.generateRobDetailFromWs(robBusinessSummaryInfo.getBrNo());
				} catch (SSMException e1) {
					e1.printStackTrace();
				}

				getSession().setAttribute("robFormC", robFormC);
				robFormC.setBizName(robBusinessSummaryInfo.getBizName());
				robFormC.setBrNo(robBusinessSummaryInfo.getBrNo());
				robFormC.setCheckDigit(robBusinessSummaryInfo.getChkDigit());
				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY);

				return robFormC;
			}
		}));
		init();
	}
	
	public EditRobFormCPage(final BusinessInfo businesInfo) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormC robFormC = new RobFormC();
				try {
					robFormC = robFormCService.generateRobDetailFromWs(businesInfo.getBrNo());
				} catch (SSMException e1) {
					e1.printStackTrace();
				}

				getSession().setAttribute("robFormC", robFormC);
				robFormC.setBizName(businesInfo.getBizName());
				robFormC.setBrNo(businesInfo.getBrNo());
				robFormC.setCheckDigit(businesInfo.getChkDigit());
				robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY);

				return robFormC;
			}
		}));
		init();
	}

	public EditRobFormCPage(final String robFormCRefNo) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormC robFormC = robFormCService.findAllById(robFormCRefNo);
				return robFormC;
			}
		}));
		init();
	}

	public EditRobFormCPage(RobFormC robFormC) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormC robFormC = new RobFormC();
				return robFormC;
			}
		}));
		init();

		// TODO Auto-generated constructor stub
	}

	private void init() {
		add(new RobFormCForm("form", getDefaultModel()));
	}

	private class RobFormCForm extends Form implements Serializable {

		// final WebMarkupContainer wmcCourtOrder;
		// final WebMarkupContainer wmcDeceasedDt;
		final WebMarkupContainer wmcformCfileUpload;
		final SSMLabel terminationDtWarning;
		RepeatingView listError;
		final WebMarkupContainer wmcFeeSummaryAll;
		final SSMAjaxButton submitPayment, reLodgeFormC;
		final SSMAjaxButton saveDraftFormC;
		final SSMAjaxCheckBox declarationChkBox;

		final WebMarkupContainer wmccompoundFormC;
		boolean isQuery;

		final SSMLabel cmpFormCFee;
		final SSMLabel cmpFormCFeeQuantity;
		final SSMLabel totalCmpFormCFee;

		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		final SSMLabel cmpAmt;

		final SSMLabel totalFee;
		final SSMLabel summaryError;

		final LlpPaymentFee cmpFee;
		final LlpPaymentFee businessInfoPaymentFee;

		final RobFormC robFormC;

		final SSMTextField robFormCCode;
		final SSMAjaxCheckBox isPayCmp;
		final SSMLabel uploadErrorLabel;

		public RobFormCForm(String id, IModel m) {
			super(id, m);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			cmpFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_C_COMPOUND);

			String prefixLabelKey = "page.lbl.ezbiz.robFormC.";
			setPrefixLabelKey(prefixLabelKey);

			robFormC = (RobFormC) m.getObject();

			SSMLabel brNo = new SSMLabel("brNo", robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
			add(brNo);

			SSMLabel bizName = new SSMLabel("bizName", robFormC.getBizName());
			add(bizName);

			listError = new RepeatingView("listError");
			listError.setVisible(false);
			add(listError);

			robFormCCode = new SSMTextField("robFormCCode");
			robFormCCode.setReadOnly(true);
			robFormCCode.setOutputMarkupId(true);
			add(robFormCCode);

			SSMDownloadLink downloadAttachmentCForm = new SSMDownloadLink("downloadAttachmentCForm");

			if (robFormC.getSupportingDocId() == null) {
				downloadAttachmentCForm.setVisible(false);
			} else {
				LlpFileData fileDataNew = new LlpFileData();
				fileDataNew = llpFileDataService.findById(robFormC.getSupportingDocId());
				downloadAttachmentCForm.setDownloadData(robFormC.getBrNo() + "_BORANG_C.pdf", "application/pdf", fileDataNew.getFileData());
				downloadAttachmentCForm.setVisible(true);
				
			}
			add(downloadAttachmentCForm);

			wmcformCfileUpload = new WebMarkupContainer("wmcformCfileUpload");
			wmcformCfileUpload.setPrefixLabelKey(prefixLabelKey);
			wmcformCfileUpload.setOutputMarkupId(true);
			wmcformCfileUpload.setOutputMarkupPlaceholderTag(true);
			wmcformCfileUpload.setVisible(false);
			add(wmcformCfileUpload);

			wmccompoundFormC = new WebMarkupContainer("wmccompoundFormC");
			wmccompoundFormC.setPrefixLabelKey(prefixLabelKey);
			wmccompoundFormC.setOutputMarkupId(true);
			wmccompoundFormC.setOutputMarkupPlaceholderTag(true);
			wmccompoundFormC.setVisible(false);
			add(wmccompoundFormC);

			// Fee Summary
			wmcFeeSummaryAll = new WebMarkupContainer("wmcFeeSummaryAll");
			wmcFeeSummaryAll.setPrefixLabelKey(prefixLabelKey + "feeSummary.");
			wmcFeeSummaryAll.setOutputMarkupId(true);
			wmcFeeSummaryAll.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummaryAll);

			SSMTextArea queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(false);
			wmcFeeSummaryAll.add(queryAnswer);

			SSMTextArea notes = new SSMTextArea("notes", Model.of(""));
			notes.setVisible(false);
			notes.setReadOnly(true);
			wmcFeeSummaryAll.add(notes);

			if (Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())) {
				isQuery = true;
				RobFormNotes formNotes = robFormC.getListRobFormNotes().get(robFormC.getListRobFormNotes().size() - 1);
				notes.setDefaultModelObject(formNotes.getNotes());
				notes.setVisible(true);
				if (StringUtils.isNotBlank(formNotes.getNotesAnswer())) {
					robFormC.setQueryAnswer(formNotes.getNotesAnswer());
				}
			} else {
				isQuery = false;
			}

			cmpFormCFee = new SSMLabel("cmpFormCFee", "");
			add(cmpFormCFee);
			wmcFeeSummaryAll.add(cmpFormCFee);

			cmpFormCFeeQuantity = new SSMLabel("cmpFormCFeeQuantity", "");
			add(cmpFormCFeeQuantity);
			wmcFeeSummaryAll.add(cmpFormCFeeQuantity);

			totalCmpFormCFee = new SSMLabel("totalCmpFormCFee", "");
			add(totalCmpFormCFee);
			wmcFeeSummaryAll.add(totalCmpFormCFee);

			bisnessInfoFee = new SSMLabel("bisnessInfoFee", "");
			add(bisnessInfoFee);
			wmcFeeSummaryAll.add(bisnessInfoFee);

			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity", "");
			add(bisnessInfoFeeQuantity);
			wmcFeeSummaryAll.add(bisnessInfoFeeQuantity);

			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee", "");
			add(totalBisnessInfoFee);
			wmcFeeSummaryAll.add(totalBisnessInfoFee);

			totalFee = new SSMLabel("totalFee", "");
			add(totalFee);
			wmcFeeSummaryAll.add(totalFee);

			summaryError = new SSMLabel("summaryError", "");
			summaryError.setEscapeModelStrings(true);
			add(summaryError);

			listError = new RepeatingView("listError");
			listError.setVisible(false);
			wmcFeeSummaryAll.add(listError);

			final SSMDropDownChoice reasonType = new SSMDropDownChoice("reasonType", Parameter.ROB_FORM_C_REASON);

			add(reasonType);

			FileUploadField fileUploadTmp = new FileUploadField("fileUploadTmp");
			add(fileUploadTmp);
			setMultiPart(true);
			
			OnChangeAjaxBehavior fileUploadOnchange = new OnChangeAjaxBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
//					String submitType = "saveDraftFormC";
					listError.removeAll();
					listError.setVisible(false);
					target.add(wmcFeeSummaryAll);
					System.out.println("SDSDS");
				}
			};
			fileUploadTmp.add(fileUploadOnchange);
			
			
			uploadErrorLabel = new SSMLabel("uploadErrorLabel","");
			uploadErrorLabel.setEscapeModelStrings(false);
			uploadErrorLabel.setOutputMarkupId(true); 
			add(uploadErrorLabel);
			
			SSMAjaxButton uploadSuppDoc = new SSMAjaxButton("uploadSuppDoc") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormC robFormCTmp = (RobFormC) form.getDefaultModelObject();
					boolean isError = false;
					updateAll(target, form, "saveDraftFormC");
//					
//					if(robFormCTmp.getFileUploadTmp()!=null){
//						if(robFormCTmp.getFileUploadTmp().get(0).getBytes().length>3145728){
//							uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormA.exceedUploadSize"));
//							isError = true;
//						}else{
//							try {
//								ByteArrayInputStream bais = new ByteArrayInputStream(robFormCTmp.getFileUploadTmp().get(0).getBytes());
//								PDDocument document = PDDocument.load(bais);
//								document.close();
//							} catch (Exception e) {
//								uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormA.notInPDF"));
//								isError = true;
//							}
//						}
//						
//						if(!isError){
//							LlpFileData supportingDoc = new LlpFileData();
//							supportingDoc.setFileData(robFormCTmp.getformAfileUpload().get(0).getBytes());
//							supportingDoc.setFileDataType("PDF");
//							llpFileDataService.insert(supportingDoc);
//							
//							robFormA.setSupportingDocData(supportingDoc);
//							robFormA.setIsHasSupportingDoc(Parameter.YES_NO_yes);
//							
//							robFormCTmp.setSupportingDocData(supportingDoc);
//							robFormCTmp.setIsHasSupportingDoc(Parameter.YES_NO_yes);
//							
//							
//							
//						}
						
						target.add(wmcFeeSummaryAll);
					}

					
				};
//			add(uploadSuppDoc);

			final EditRobFormCOwnerVerificationPanel cOwnerVerificationPanel = new EditRobFormCOwnerVerificationPanel("cOwnerVerificationPanel",
					robFormC);
			cOwnerVerificationPanel.setOutputMarkupId(true);
			cOwnerVerificationPanel.setOutputMarkupPlaceholderTag(true);
			add(cOwnerVerificationPanel);

			cOwnerVerificationPanel.setAllPanel(cOwnerVerificationPanel);

			String hideAllJs = "";
			hideAllJs += "$('#" + cOwnerVerificationPanel.getMarkupId() + "').hide();";

			Label hideAllLbl = new Label("hideAllLbl", hideAllJs);
			hideAllLbl.setEscapeModelStrings(false); // do not HTML escape
														// JavaScript code
			add(hideAllLbl);

			cmpAmt = new SSMLabel("cmpAmt");
			wmccompoundFormC.add(cmpAmt);

			SSMAjaxFormSubmitBehavior terminationDtOnBlur = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
					robFormC.setIsPayCmp(Parameter.YES_NO_no);
					robFormCForm.setIsPayCmp(Parameter.YES_NO_no);
					robFormC.setTerminationDt(robFormCForm.getTerminationDt());

					isPayCmp.setDefaultModelObject(false);//untick

					String submitType = "onChangeCal";
					recalculateFeeFormC(target, robFormC, submitType);
				}
			};

			SSMDateTextField terminationDt = new SSMDateTextField("terminationDt");
			terminationDt.setOutputMarkupId(true);
			terminationDt.setOutputMarkupPlaceholderTag(true);
			terminationDt.setEscapeModelStrings(false);
			if (isQuery) {
				terminationDt.setReadOnly(true);
				terminationDt.setEnabled(false);
			} else {
				terminationDt.add(terminationDtOnBlur);
			}
			add(terminationDt);

			String msg = "";

			terminationDtWarning = new SSMLabel("terminationDtWarning", msg);
			terminationDtWarning.setOutputMarkupId(true);
			terminationDtWarning.setOutputMarkupPlaceholderTag(true);
			terminationDtWarning.setEscapeModelStrings(false);
			add(terminationDtWarning);

			boolean isPayCmpBool = false;
			if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp())) {
				isPayCmpBool = true;
			}
			isPayCmp = new SSMAjaxCheckBox("isPayCmp", new PropertyModel(isPayCmpBool, "")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {

					if (String.valueOf(true).equals(getValue())) {
						robFormC.setIsPayCmp(Parameter.YES_NO_yes);
					} else {
						robFormC.setIsPayCmp(Parameter.YES_NO_no);
					}
					String submitType = null;
					recalculateFeeFormC(target, robFormC, submitType);
				}
			};

			if (isQuery) {
				isPayCmp.setEnabled(false);
			}
			isPayCmp.setOutputMarkupId(true);
			wmccompoundFormC.add(isPayCmp);

			SSMAjaxFormSubmitBehavior isBuyInfoOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
					robFormC.setIsBuyInfo(robFormCForm.getIsBuyInfo());

					String submitType = null;
					recalculateFeeFormC(target, robFormC, submitType);
				}
			};

			SSMTextField isBuyInfoDesc = new SSMTextField("isBuyInfoDesc", Model.of(""));
			isBuyInfoDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormC.getIsBuyInfo()));
			isBuyInfoDesc.setVisible(false);
			isBuyInfoDesc.setReadOnly(true);
			add(isBuyInfoDesc);

			SSMDropDownChoice isBuyInfo = new SSMDropDownChoice("isBuyInfo", Parameter.YES_NO);
			isBuyInfo.setOutputMarkupId(true);
			isBuyInfo.setOutputMarkupPlaceholderTag(true);
			isBuyInfo.setEscapeModelStrings(false);
			isBuyInfo.add(isBuyInfoOnchange);
			if (isQuery) {
				isBuyInfo.setVisible(false);
				isBuyInfoDesc.setVisible(true);
			}
			add(isBuyInfo);

			boolean checkVerification = checkVerificationOwner(robFormC.getListRobFormOwnerVerification());

			declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robFormC, "declarationChkBox")) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (String.valueOf(true).equals(getValue())) {
						robFormC.setDeclarationChkBox(true);
					} else {
						robFormC.setDeclarationChkBox(false);
					}

					if (submitPayment.isVisible()) {
						submitPayment.setEnabled(robFormC.getDeclarationChkBox());
						arg0.add(submitPayment);
					}

					if (reLodgeFormC.isVisible()) {
						reLodgeFormC.setEnabled(robFormC.getDeclarationChkBox());
						arg0.add(reLodgeFormC);
					}

				}
			};

			if (checkVerification) {
				declarationChkBox.setEnabled(true);
			} else {
				declarationChkBox.setEnabled(false);
			}
			wmcFeeSummaryAll.add(declarationChkBox);

			if (isQuery) {
				queryAnswer.setVisible(true);
			}

			final String mainValidationJS = "mainValidation";
			String mainFieldToValidate[] = new String[] { "reasonType", "terminationDt" };
			String mainFieldToValidateRules[] = new String[] { "empty", "empty" };
			setSemanticJSValidation(this, mainValidationJS, mainFieldToValidate, mainFieldToValidateRules);

			final String reLodgeFormCValidationJS = "reLodgeFormCValidation";
			String reLodgeFormCValidation[] = new String[] { "queryAnswer" };
			String reLodgeFormCValidationRules[] = new String[] { "empty" };
			setSemanticJSValidation(wmcFeeSummaryAll, reLodgeFormCValidationJS, reLodgeFormCValidation, reLodgeFormCValidationRules);

			saveDraftFormC = new SSMAjaxButton("saveDraftFormC", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					RobFormC robFormCForm = (RobFormC) form.getDefaultModelObject();

					String submitType = "saveDraftFormC";
					robFormC.copyFrom(robFormCForm);
					recalculateFeeFormC(target, robFormC, submitType);

					if (listError.size() > 0) {
						return;
					}

					updateAll(target, form, submitType);

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();

					if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp()) && robFormC.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
						paymentItem1.setPaymentItem(Parameter.ROB_FORM_C_CMP);
						paymentItem1.setQuantity(1);
						paymentItem1.setPaymentDet(robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
						paymentItem1.setAmount(robFormC.getCmpAmt());
						listPaymentItems.add(paymentItem1);
					}

					if (Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())) {
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormC.getBizInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}

						listPaymentItems.add(paymentItemBisInfo);
					}
					storeSuccessMsgKey("page.lbl.ezbiz.formC.successSaveDraft");
//					setResponsePage(new ListRobFormCTransactionPage());
					SignInSession signInSession = (SignInSession)getSession();
					if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
						ssmError("Form C Success Save");
						setResponsePage(new ExtInterface());
					}else {
						setResponsePage(new ListRobFormCTransactionPage());
					}
				}
			};
			saveDraftFormC.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(saveDraftFormC);

			submitPayment = new SSMAjaxButton("submitPayment", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					RobFormC robFormCForm = (RobFormC) form.getDefaultModelObject();
					String submitType = "saveDraftFormC";
					updateAll(target, form, submitType);
					
					if (listError.size() > 0) {
						return;
					}

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();

					if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp()) && robFormC.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
						paymentItem1.setPaymentItem(Parameter.ROB_FORM_C_CMP);
						paymentItem1.setQuantity(1);
						paymentItem1.setPaymentDet(robFormC.getBrNo() + "-" + robFormC.getCheckDigit());
						paymentItem1.setAmount(robFormC.getCmpAmt());
						listPaymentItems.add(paymentItem1);
					}

					if (Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())) {
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormC.getBizInfoAmt());
						System.out.println("robFormC.getBizInfoAmt()" + robFormC.getBizInfoAmt());
						System.out.println("paymentItemBisInfo" + paymentItemBisInfo.getAmount());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}

						listPaymentItems.add(paymentItemBisInfo);
					}

					if (listPaymentItems.size() > 0) {
						setResponsePage(new PaymentDetailPage(robFormC.getRobFormCCode(), RobFormCService.class.getSimpleName(), robFormC,
								listPaymentItems));
					} else {
						robFormCService.lodgeFormCWoPayment(robFormC);
						storeSuccessMsgKey("page.lbl.ezbiz.formC.successSubmit");
//						setResponsePage(new ListRobFormCTransactionPage());
						SignInSession signInSession = (SignInSession)getSession();
						if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
							ssmError("Form C Success Submit");
							setResponsePage(new ExtInterface());
						}else {
							setResponsePage(new ListRobFormCTransactionPage());
						}
					}
				}
			};
			submitPayment.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(submitPayment);

			reLodgeFormC = new SSMAjaxButton("reLodgeFormC", reLodgeFormCValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if (listError.size() > 0) {
						return;
					}

					try {
						updateAll(target, form, null);
						robFormCService.relodgeRobFormC(robFormC);
						storeSuccessMsgKey("page.lbl.ezbiz.formC.successRelodge");
						
						SignInSession signInSession = (SignInSession)getSession();
						if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
							ssmError("Relodge C Success");
							setResponsePage(new ExtInterface());
						}else {
							setResponsePage(new ListRobFormCTransactionPage());
						}
					} catch (Exception e) {
						ssmError(e.getMessage());
						FeedbackPanel feedbackPanel = ((EditRobFormCPage) getPage()).getFeedbackPanel();
						target.add(feedbackPanel);

						String scroll = "\n$.scrollTo(document.getElementById('" + getFeedbackPanel().getMarkupId() + "'),100);\n";
						target.appendJavaScript(scroll);
					}
				}
			};
			reLodgeFormC.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(reLodgeFormC);

			if (isQuery && checkVerification) {
				submitPayment.setVisible(false);
				reLodgeFormC.setVisible(true);
				saveDraftFormC.setVisible(false);
			} else if (!isQuery && !checkVerification) {
				reLodgeFormC.setVisible(false);
				submitPayment.setVisible(false);
				saveDraftFormC.setVisible(true);
			} else {
				submitPayment.setVisible(true);
				reLodgeFormC.setVisible(false);
				saveDraftFormC.setVisible(false);
			}


			SSMAjaxButton discard = new SSMAjaxButton("discardApp") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(StringUtils.isNotEmpty(robFormC.getRobFormCCode())) {
						robFormC.setStatus(Parameter.ROB_FORM_C_STATUS_DISCARD);
						robFormCService.update(robFormC);
					}
					
					SignInSession signInSession = (SignInSession)getSession();
					if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
						ssmError("Discard C");
						setResponsePage(new ExtInterface());
					}else {
						setResponsePage(ListRobFormCTransactionPage.class);
					}
				}
				protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
//			        AjaxCallListener ajaxCallListener = new AjaxCallListener();
//			        String text = resolve("page.confirm.robFormC.discardApplication");
//			        ajaxCallListener.onPrecondition( "return confirm('" + text + "');" );
//			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			        String defaultConfirmTitle = resolve("page.confirm.robFormC.discardApplicationTitle");
			        String defaultConfirmDesc = resolve("page.confirm.robFormC.discardApplicationDesc");
			        
					AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, defaultConfirmTitle, defaultConfirmDesc);
					attributes.getAjaxCallListeners().add( ajaxCallListener );
			    }
			};
			wmcFeeSummaryAll.add(discard);
			discard.setVisible(false);
			
			if(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus())){
				discard.setVisible(true);
			}
			
			String submitType = null;
			recalculateFeeFormC(null, robFormC, submitType);
		}

		protected void recalculateFeeFormC(AjaxRequestTarget target, RobFormC robFormCForm, String submitType) {

			double gstAmt = 0;
			double businessInfoAmt = 0;

			DecimalFormat df = new DecimalFormat("#0.00");

			listError.removeAll();
			
			if(robFormC.isAttachmentExceedSize()){
				listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.ezbiz.robFormA.exceedUploadSize")));
				robFormC.setAttachmentExceedSize(false);
			}else {
				if(robFormC.isNotPdf()){
					listError.add(new SSMLabel(listError.newChildId(), resolve("page.lbl.ezbiz.robFormA.notInPDF")));
					robFormC.setNotPdf(false);
				}
			}
			
			if (robFormCForm.getTerminationDt() != null) {
				terminationDtWarning.setDefaultModelObject("");
				robFormCForm.setCmpAmt(0.0);
				cmpAmt.setDefaultModelObject(df.format(0.0));
				if (robFormCForm.getTerminationDt().after(new Date())) {
					String futureDtError = resolve("page.lbl.ezbiz.robFormC.terminateDateCannotFuture");
					listError.add(new SSMLabel(listError.newChildId(), futureDtError));
					terminationDtWarning.setDefaultModelObject(futureDtError);
				} else {
					final Calendar c30DayBefore = Calendar.getInstance();
					c30DayBefore.setTime(DateUtil.getCurrentDate());
					c30DayBefore.add(Calendar.DATE, -31);

					robFormCForm.setCmpAmt(0.0);
					cmpAmt.setDefaultModelObject(df.format(0.0));

					if (robFormCForm.getTerminationDt().before(c30DayBefore.getTime())) {

						int delayDay = DateUtil.getDateDifference(robFormCForm.getTerminationDt(), new Date(), DateUtil.DAY_DIFFERENCE);
						double cmpAmtFeeDouble = robCmpConfigService.findCmpAmtByDelayDay(Parameter.ROB_FORM_TYPE_C, delayDay);

						robFormCForm.setCmpAmt(cmpAmtFeeDouble);
						cmpAmt.setDefaultModelObject(df.format(cmpAmtFeeDouble));

						if (robFormCForm.getCmpAmt() >= 0) {
							wmccompoundFormC.setVisible(true);
							if (target != null) {
								target.add(wmccompoundFormC);
							}
						} else {
							wmccompoundFormC.setVisible(true);
							if (target != null) {
								target.add(wmccompoundFormC);
							}
						}
					} else {
						wmccompoundFormC.setVisible(true);
						if (target != null) {
							target.add(wmccompoundFormC);
						}
					}
				}
				if (target != null) {
					wmccompoundFormC.setVisible(true);
					target.add(terminationDtWarning);
					target.add(wmccompoundFormC);

				}

			} else {
				robFormCForm.setCmpAmt(0.00);
				cmpAmt.setDefaultModelObject(df.format(0.00));
				String EmptyDtError = resolve("page.lbl.ezbiz.robFormC.terminateDateCannotEmpty");
				listError.add(new SSMLabel(listError.newChildId(), EmptyDtError));
				terminationDtWarning.setDefaultModelObject(EmptyDtError);
			}

			double totalFeeDouble = 0;
			double bisnessInfoFeeDouble = 0.00;
			int bisnessInfoFeeQuantityInt = 0;
			double totalBisnessInfoFeeDouble = 0;

			if (Parameter.YES_NO_yes.equals(robFormCForm.getIsBuyInfo())) {
				bisnessInfoFeeQuantityInt = 1;
				bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
				businessInfoAmt = businessInfoPaymentFee.getPaymentFee() + gstAmt;
			}

			robFormCForm.setBizInfoAmt(totalBisnessInfoFeeDouble);

			double totalCmpFeeDouble = 0;
			int cmpFeeQuantityInt = 0;
			double cmpFeeDouble = 0.00;

			if (Parameter.YES_NO_yes.equals(robFormCForm.getIsPayCmp()) && robFormCForm.getCmpAmt() > 0) {
				cmpFeeQuantityInt = 1;
				cmpFeeDouble = robFormCForm.getCmpAmt();
				totalCmpFeeDouble = cmpFeeDouble;
			}
			
			else if ("onChangeCal".equals(submitType) && robFormCForm.getCmpAmt() > 0) {  //override terminationDtOnBlur atas
				cmpFeeQuantityInt = 1;
				cmpFeeDouble = robFormCForm.getCmpAmt();
				totalCmpFeeDouble = cmpFeeDouble;
				robFormC.setIsPayCmp(Parameter.YES_NO_yes);
				robFormCForm.setIsPayCmp(Parameter.YES_NO_yes);
				isPayCmp.setDefaultModelObject(true); //checkbox auto tick everytime calendar changed
			}

			totalFeeDouble = +totalBisnessInfoFeeDouble + totalCmpFeeDouble;

			bisnessInfoFee.setDefaultModelObject(df.format(businessInfoAmt));
			bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
			totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));

			cmpFormCFee.setDefaultModelObject(df.format(cmpFeeDouble));
			cmpFormCFeeQuantity.setDefaultModelObject(String.valueOf(cmpFeeQuantityInt));
			totalCmpFormCFee.setDefaultModelObject(df.format(totalCmpFeeDouble));

			totalFee.setDefaultModelObject(df.format(totalFeeDouble));

			robFormCForm.setTotalAmt(totalFeeDouble);

			declarationChkBox.setVisible(false);

			if (("saveDraftFormC").equals(submitType) || ("submitPayment").equals(submitType)) {
				if(!StringUtils.isNotBlank(robFormC.getRobFormCCode())) {//New
					if ((robFormCForm.getFileUploadTmp() == null ) ) {
						String AttachmentEmptyError = resolve("page.lbl.ezbiz.robFormC.attachmentCannotEmpty");
						listError.add(new SSMLabel(listError.newChildId(), AttachmentEmptyError));
						robFormC.setDeclarationChkBox(false);
						declarationChkBox.setVisible(true);
					}
				}
				
			}

			if (listError.size() > 0) {
				listError.setVisible(true);
				submitPayment.setEnabled(false);
				reLodgeFormC.setEnabled(false);
				
				robFormC.setDeclarationChkBox(false);
				declarationChkBox.setVisible(true);
				
			} else {
				listError.setVisible(false);
				declarationChkBox.setVisible(true);
				submitPayment.setEnabled(false);
				reLodgeFormC.setEnabled(false);
				

//				robFormC.setDeclarationChkBox(false);
//				declarationChkBox.setVisible(true);
			}
			if (target != null) {
				target.add(wmcFeeSummaryAll);
				target.add(wmccompoundFormC);
			}

		}

		protected void updateAll(AjaxRequestTarget target, Form<?> form, String submitType) {
			FeedbackPanel feedbackPanel = ((EditRobFormCPage) getPage()).getFeedbackPanel();
			feedbackPanel.getFeedbackMessages().clear();
			target.add(feedbackPanel);
			
			RobFormC robFormCForm = (RobFormC) form.getDefaultModelObject();
			boolean errorFile = false;
			uploadErrorLabel.setDefaultModelObject("");
			
			if (robFormCForm.getFileUploadTmp() != null) {
				FileUpload fileUploadNew = robFormCForm.getFileUploadTmp().get(0);
				
				if(fileUploadNew.getBytes().length>3145728){
					robFormC.setAttachmentExceedSize(true);
					uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormC.exceedUploadSize"));
					errorFile = true;
				}else{
					try {
						robFormC.setAttachmentExceedSize(false);
						ByteArrayInputStream bais = new ByteArrayInputStream(fileUploadNew.getBytes());
						PDDocument document = PDDocument.load(bais);
						document.close();
						robFormC.setNotPdf(false);
						
						Long supportingDocIdNew = insertFileUpload(fileUploadNew, robFormCForm);

						robFormC.setSupportingDocId(supportingDocIdNew);
						if (robFormC.getSupportingDocId() == 0) {
							robFormC.setIsHasSupportingDoc(Parameter.YES_NO_no);
						} else {
							robFormC.setIsHasSupportingDoc(Parameter.YES_NO_yes);
						}
					} catch (Exception e) {
						robFormC.setNotPdf(true);
						errorFile = true;
						uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormC.notInPDF"));
					}
				}
			}
			
			if(errorFile){
				target.add(uploadErrorLabel);
			}
			
			
			robFormC.copyFrom(robFormCForm);

			recalculateFeeFormC(target, robFormC, submitType);
			
			boolean isNew = false;
			if (StringUtils.isBlank(robFormC.getRobFormCCode())) {
				isNew = true;
			}

			robFormCService.insertUpdateAll(robFormC);
			robFormCService.sendEmailToAllPartner(robFormC);
			form.setDefaultModelObject(robFormC);
			if (isNew) {
				robFormCCode.setDefaultModelObject(robFormC.getRobFormCCode());
				target.add(robFormCCode);
			}
		}

		protected void update(AjaxRequestTarget target, Form<?> form) {
			FeedbackPanel feedbackPanel = ((EditRobFormCPage) getPage()).getFeedbackPanel();
			feedbackPanel.getFeedbackMessages().clear();
			target.add(feedbackPanel);

			RobFormC robFormCForm = (RobFormC) form.getDefaultModelObject();

			robFormC.copyFrom(robFormCForm);

			recalculateFeeFormC(target, robFormC, null);
			boolean isNew = false;
			if (StringUtils.isBlank(robFormC.getRobFormCCode())) {
				isNew = true;
			}
			robFormCService.UpdateAll(robFormC);
			form.setDefaultModelObject(robFormC);
			if (isNew) {
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
		if (fileUploadNew != null) {
			fileData.setFileData(fileUploadNew.getBytes());
			fileData.setFileDataType(fileUploadNew.getContentType());
		}

		if ((Parameter.YES_NO_yes).equalsIgnoreCase(robFormCForm.getIsHasSupportingDoc())) {
			LlpFileData fileDataNew = new LlpFileData();
			fileDataNew = llpFileDataService.findById(robFormCForm.getSupportingDocId());
			fileData.setFileDataId(fileDataNew.getFileDataId());
			fileData.setCreateBy(fileDataNew.getCreateBy());
			fileData.setCreateDt(fileDataNew.getCreateDt());
			llpFileDataService.update(fileData);
		} else {
			llpFileDataService.insert(fileData);
		}

		return fileData.getFileDataId();

	}

	public boolean checkVerificationOwner(List<RobFormOwnerVerification> listRobFormOwnerVerification) {
		// TODO Auto-generated method stub

		boolean checkVerification = true;
		for (int i = 0; i < listRobFormOwnerVerification.size(); i++) {
			RobFormOwnerVerification robFormOwnerVerification = (RobFormOwnerVerification) listRobFormOwnerVerification.get(i);

			System.out.println("robFormOwnerVerification.getStatus():" + robFormOwnerVerification.getStatus());

			if (!robFormOwnerVerification.getStatus().equals(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED)) {
				checkVerification = false;
				return checkVerification;
			}
		}

		return checkVerification;
	}

}
