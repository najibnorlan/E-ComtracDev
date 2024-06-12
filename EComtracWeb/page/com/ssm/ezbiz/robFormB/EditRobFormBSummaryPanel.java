package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.base.common.util.DateUtil;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.service.RobCmpConfigService;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RobFormB4;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.ssm.webis.param.BlacklistInfoResp;
import com.ssm.webis.param.BusinessFormAOwnerValidResp;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormBSummaryPanel extends RobFormBBasePanel{

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	@SpringBean(name = "RobCmpConfigService")
	private RobCmpConfigService robCmpConfigService;
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;
	
	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;
	
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	
	private RobFormBSummaryForm robFormBSummaryForm;
	private final RobFormB robFormB;
	
	public EditRobFormBSummaryPanel(String panelId, final RobFormB robFormB) {
		super(panelId);
		this.robFormB = robFormB;
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new EditRobFormBSummaryModel(robFormB);
            }
        }));
		robFormBSummaryForm = new RobFormBSummaryForm("robFormBSummaryForm",getDefaultModel(),robFormB);
		add(robFormBSummaryForm);
	}
	
	@Override
	public void refreshPanel(AjaxRequestTarget target) {
		super.refreshPanel(target);
		robFormBSummaryForm.recalculateFee(target, robFormB);
	}
	private class RobFormBSummaryForm extends Form implements Serializable {
		
		final EditRobFormBSummaryModel robFormSummaryModel;
		final boolean isQuery;
		final SSMAjaxButton submitPayment, reLodgeFormA;
		final RepeatingView listError;
		final SSMLabel formBFee;
		final SSMLabel totalFormBFee;
		final SSMLabel branchFee;
		final SSMLabel cmpAmtFee;
		final SSMLabel totalCmpAmtFee;
		final SSMLabel branchFeeDuration;
		final SSMLabel branchFeePerYear;
		final SSMLabel totalBranchFee;
		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		final SSMLabel totalFee;
//		final SSMLabel summaryError;
		final WebMarkupContainer wmcFeeSummary;
		final WebMarkupContainer wmcSupportingDoc;
		final WebMarkupContainer wmcListError ;
		final SSMAjaxCheckBox declarationChkBox;
		
		

		final SSMDownloadLink downloadSupportingDoc;
		final SSMAjaxLink removeSupportingDoc;
		final FileUploadField fileUpload;
		final LlpPaymentFee formBCmpPaymentFee;
		final LlpPaymentFee branchPaymentFee;
		final LlpPaymentFee formBPaymentFee;
		final LlpPaymentFee businessInfoPaymentFee;
		
		public RobFormBSummaryForm(String id, IModel m, final RobFormB robFormB) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormBSummary.");
			robFormSummaryModel = (EditRobFormBSummaryModel) m.getObject();
			
			formBPaymentFee  = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_B);
			branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_B_BRANCHES);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			formBCmpPaymentFee  = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_B_CMP);
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			
					
			wmcSupportingDoc = new WebMarkupContainer("wmcSupportingDoc");
			wmcSupportingDoc.setOutputMarkupId(true);
			wmcSupportingDoc.setPrefixLabelKey("page.lbl.ezbiz.robFormBSummary.");
			add(wmcSupportingDoc);
			
			downloadSupportingDoc = new SSMDownloadLink("downloadSupportingDoc");
			wmcSupportingDoc.add(downloadSupportingDoc);
			
			removeSupportingDoc = new SSMAjaxLink("removeSupportingDoc"){

				@Override
				public void onClick(AjaxRequestTarget target) {
					LlpFileData suppDoc = robFormB.getSupportingDocData();
					llpFileDataService.delete(suppDoc);
					robFormB.setIsHasSupportingDoc(Parameter.YES_NO_no);
					robFormB.setSupportingDocData(null);
					robFormBService.update(robFormB);
					recalculateFee(target, robFormB);
				}
				
			};
			removeSupportingDoc.setConfirmQuestion(resolve("page.confirm.robFormB.confirmRemoveSupportingDoc"));
			wmcSupportingDoc.add(removeSupportingDoc);
			
			fileUpload = new FileUploadField("fileUploadTmp");
			fileUpload.setNoLabel();
			wmcSupportingDoc.add(fileUpload);
			
			SSMAjaxButton uploadSuppDoc = new SSMAjaxButton("uploadSuppDoc") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					EditRobFormBSummaryModel robFormBSummaryModel = (EditRobFormBSummaryModel) form.getDefaultModelObject();
					if(robFormBSummaryModel.getFileUploadTmp()!=null){
						LlpFileData supportingDoc = new LlpFileData();
						supportingDoc.setFileData(robFormBSummaryModel.getFileUploadTmp().get(0).getBytes());
						supportingDoc.setFileDataType("PDF");//,robFormBSummaryModel.getFileUploadTmp().get(0).getBytes());
						llpFileDataService.insert(supportingDoc);
						
						robFormB.setSupportingDocData(supportingDoc);
						robFormB.setIsHasSupportingDoc(Parameter.YES_NO_yes);
						
						robFormBService.insertUpdateAll(robFormB);
						hideAndShowSegment(target, 4);//mean show segment summary back
					}

					
				}
			};
			wmcSupportingDoc.add(uploadSuppDoc);
			
			SSMAjaxFormSubmitBehavior isBuyInfoOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					EditRobFormBSummaryModel obj = (EditRobFormBSummaryModel) getForm().getModelObject();
					robFormB.setIsBuyInfo(obj.getIsBuyInfo());
					recalculateFee(target, robFormB);
				}

			};
			
			String noAttachmentLabelValue = "";
			if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				fileUpload.setVisible(true);
				removeSupportingDoc.setVisible(true);
				uploadSuppDoc.setVisible(true);
			}else{
				fileUpload.setVisible(false);
				removeSupportingDoc.setVisible(false);
				uploadSuppDoc.setVisible(false);
				if(robFormB.getSupportingDocData()==null){
					noAttachmentLabelValue = resolve("page.lbl.ezbiz.robFormBSummary.noAttachmentLabel");
				}
			}
			wmcSupportingDoc.add(new SSMLabel("noAttachmentLabel",noAttachmentLabelValue));
			
			SSMDropDownChoice isBuyInfo = new SSMDropDownChoice("isBuyInfo", Parameter.YES_NO);
			isBuyInfo.add(isBuyInfoOnchange);
			add(isBuyInfo);
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isBuyInfo.setEnabled(false);
			}
			
			SSMTextArea queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(isQuery);
			queryAnswer.setOutputMarkupId(true);
			add(queryAnswer);
			
			wmcFeeSummary = new WebMarkupContainer("wmcFeeSummary");
			wmcFeeSummary.setOutputMarkupId(true);
			add(wmcFeeSummary);
			
			formBFee = new SSMLabel("formBFee","");
			wmcFeeSummary.add(formBFee);
			cmpAmtFee  = new SSMLabel("cmpAmtFee","");
			wmcFeeSummary.add(cmpAmtFee);
			totalCmpAmtFee  = new SSMLabel("totalCmpAmtFee","");
			wmcFeeSummary.add(totalCmpAmtFee);
			totalFormBFee = new SSMLabel("totalFormBFee","");
			wmcFeeSummary.add(totalFormBFee);
			branchFee = new SSMLabel("branchFee","");
			branchFeeDuration = new SSMLabel("branchFeeDuration","");
			wmcFeeSummary.add(branchFeeDuration);
			branchFeePerYear = new SSMLabel("branchFeePerYear","");
			wmcFeeSummary.add(branchFeePerYear);
			totalBranchFee = new SSMLabel("totalBranchFee","");
			wmcFeeSummary.add(totalBranchFee);
			bisnessInfoFee = new SSMLabel("bisnessInfoFee","");
			wmcFeeSummary.add(bisnessInfoFee);
			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity","");
			wmcFeeSummary.add(bisnessInfoFeeQuantity);
			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee","");
			wmcFeeSummary.add(totalBisnessInfoFee);
			totalFee = new SSMLabel("totalFee","");
			wmcFeeSummary.add(totalFee);
			
			
//			summaryError = new SSMLabel("summaryError","");
//			summaryError.setEscapeModelStrings(true);
//			add(summaryError);
			
			
			wmcListError = new WebMarkupContainer("wmcListError");
			wmcListError.setOutputMarkupId(true);
			wmcListError.setPrefixLabelKey("page.lbl.ezbiz.robFormBSummary.");
			add(wmcListError);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			listError.setOutputMarkupId(true);
			wmcListError.add(listError);
			
			declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robFormSummaryModel, "declarationChkBox") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					recalculateFee(target, robFormB);
					if (String.valueOf(true).equals(getValue())) {
						robFormSummaryModel.setDeclarationChkBox(true);
					} else {
						robFormSummaryModel.setDeclarationChkBox(false);
					}
					if(submitPayment.isVisible()){
						submitPayment.setEnabled(robFormSummaryModel.getDeclarationChkBox());
						target.add(submitPayment);
					}
					
					if(reLodgeFormA.isVisible()){
						reLodgeFormA.setEnabled(robFormSummaryModel.getDeclarationChkBox());
						target.add(reLodgeFormA);
					}
				}
			};
			declarationChkBox.setOutputMarkupId(true);
			wmcListError.add(declarationChkBox);
			
			submitPayment = new SSMAjaxButton("submitPayment") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					recalculateFee(target, robFormB);
					if(listError.size()>0){
						return;
					}
					if (robFormB.getCmpAmt() > 0) {
						robFormB.setIsPayCmp(Parameter.YES_NO_yes);
					}
					robFormBService.insertUpdateAll(robFormB);
					
					

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_B);
					paymentItem.setQuantity(1);
					paymentItem.setAmount(formBPaymentFee.getPaymentFee());
					paymentItem.setPaymentDet(robFormB.getBizName());
					listPaymentItems.add(paymentItem);
//
					if (robFormB.getBranchesAmt()>0) {
//						double branchFeeDouble = branchPaymentFee.getPaymentFee();//Perbranch
//						double branchFeePerYearDouble = branchFeeDouble * robFormB.getNewBranchCount();
//						double totalBranchFeeDouble = branchFeePerYearDouble * robFormB.getBalanceYear();
						
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_B_BRANCHES);
						paymentItem2.setQuantity(robFormB.getBalanceYear());
						paymentItem2.setPaymentDet(robFormB.getNewBranchCount()+" ");
						paymentItem2.setAmount(robFormB.getBranchesAmt());
						listPaymentItems.add(paymentItem2);
					}
//					
					
					if (robFormB.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentCmp = new LlpPaymentTransactionDetail();
						paymentCmp.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_B_CMP);
						paymentCmp.setQuantity(1);
						paymentCmp.setPaymentDet(robFormB.getBrNo() + "-" + robFormB.getCheckDigit());
						paymentCmp.setAmount(robFormB.getCmpAmt());
						listPaymentItems.add(paymentCmp);
					}
					
					
					if(robFormB.getBizInfoAmt()>0){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormB.getBizInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()*getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}

					setResponsePage(new PaymentDetailPage(robFormB.getRobFormBCode(), RobFormBService.class.getSimpleName(), robFormB,
							listPaymentItems));
				}
			};
			submitPayment.setOutputMarkupId(true);
			submitPayment.setEnabled(false);
			add(submitPayment);
			
			

			final String reLodgeFormBValidationJS = "reLodgeFormBValidation";
			String reLodgeFormBValidation[] = new String[]{"queryAnswer"};
			String reLodgeFormBValidationRules[] = new String[]{"empty"};
			setSemanticJSValidation(this, reLodgeFormBValidationJS, reLodgeFormBValidation, reLodgeFormBValidationRules);
			
			reLodgeFormA = new SSMAjaxButton("reLodgeFormA", reLodgeFormBValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
					
					try {
						EditRobFormBSummaryModel model = (EditRobFormBSummaryModel) form.getDefaultModelObject();
						
						robFormB.setQueryAnswer(model.getQueryAnswer());
						robFormBService.insertUpdateAll(robFormB);
						robFormBService.reLodgeFormB(robFormB);
						
						
						storeSuccessMsgKey("page.lbl.ezbiz.successRelodge");
						
						
						SignInSession signInSession = (SignInSession)getSession();
						if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
							setResponsePage(new ViewRobFormBPage(robFormB.getRobFormBCode(), getPage()));
						}else {
							setResponsePage(new TabRobFormBPage());
						}
						
						
					} catch (Exception e) {
						ssmError(e.getMessage());
						FeedbackPanel feedbackPanel =  ((EditRobFormAPage)getPage()).getFeedbackPanel();
						target.add(feedbackPanel);
						
//						String scroll = "\n$.scrollTo(document.getElementById('"+getFeedbackPanel().getMarkupId()+"'),100);\n";
//						target.appendJavaScript(scroll);
					}
				}
			};
			reLodgeFormA.setOutputMarkupId(true);
			add(reLodgeFormA);
			
			if(isQuery){
				submitPayment.setVisible(false);
				reLodgeFormA.setVisible(true);
			}else{
				reLodgeFormA.setVisible(false);
				submitPayment.setVisible(true);
			}
			if(!Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) && !Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				reLodgeFormA.setVisible(false);
				submitPayment.setVisible(false);
			}
			
			SSMAjaxButton bSummaryPrev = new SSMAjaxButton("bSummaryPrev") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 4);//mean show segment 3 - B1
				}
			};
			add(bSummaryPrev);
			
			
			recalculateFee(null, robFormB);
			generateDiscardButton(this, robFormB);
		}
		
		private void recalculateFee(AjaxRequestTarget target, RobFormB robFormB) {
			

			listError.removeAll();
			double totalFormBFeeDouble = 0;
			
			double totalFeeDouble = 0;
			double formBFeeDouble = formBPaymentFee.getPaymentFee(); //Perchanges
			totalFormBFeeDouble = formBFeeDouble * 1;
			
			double branchFeeDouble = branchPaymentFee.getPaymentFee();//Perbranch
			double branchFeePerYearDouble = branchFeeDouble * robFormB.getNewBranchCount();
			double totalBranchFeeDouble = branchFeePerYearDouble * robFormB.getBalanceYear();
			
			double bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
			int bisnessInfoFeeQuantityInt = 0;
			double totalBisnessInfoFeeDouble = 0;
			
			double gstAmt = 0;
			
			if(Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())){
				bisnessInfoFeeQuantityInt = 1;
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
			}
			
			
			//Todo check cmp here
//			double cmpAmtFeeDouble = 0;
			boolean hasChanges = false;
			Date longestDate = new Date();
			if(robFormB.getIsB1()){
				hasChanges = true;
				if(longestDate.after(robFormB.getRobFormB1().getB1AmmendmendDt())){
					longestDate = robFormB.getRobFormB1().getB1AmmendmendDt();
				}
			}
			if(robFormB.getIsB2()){
				hasChanges = true;
				if(longestDate.after(robFormB.getRobFormB2().getB2AmmendmendDt())){
					longestDate = robFormB.getRobFormB2().getB2AmmendmendDt();
				}
			}
			if(robFormB.getIsB3()){
				hasChanges = true;
				Date b3Date = robFormB.getB3AmmendmendDt();
				if(longestDate.after(b3Date)){
					longestDate = b3Date;
				}
			}
			if(robFormB.getIsB4()){
				if(robFormB.getB4AmmendmendDt()!=null) {
					hasChanges = true;
					if(longestDate.after(robFormB.getB4AmmendmendDt())){
						longestDate = robFormB.getB4AmmendmendDt();
					}
				}else {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b4SelectedWithoutAmendmentData")));
				}
				
				List<RobFormB4> listB4Owner = robFormB.getListRobFormB4();
				boolean hasNoChangeOnly=true;
				for (int i = 0; i < listB4Owner.size(); i++) {
					RobFormB4 formB4 = listB4Owner.get(i);
					if(!Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES.equals(formB4.getAmmendmentType())) {
						hasNoChangeOnly=false;
						if(formB4.getAmmendmentDate()==null) {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.pleaseUpdateb4AmendmentDt",formB4.getName())));
						}
					}
				}
				if(hasNoChangeOnly) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b4SelectedWithoutAmendmentData")));
				}
			}
			
			if(!hasChanges){
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.noChangesDetected")));
			}
			
			String formCode="";
			int delayDay=DateUtil.getDateDifference(longestDate, new Date(), DateUtil.DAY_DIFFERENCE);
			double cmpAmtFeeDouble = robCmpConfigService.findCmpAmtByDelayDay(Parameter.ROB_FORM_TYPE_B, delayDay);
			
			// rule utk kire kompaun..
			
			
			totalFeeDouble = totalFormBFeeDouble +  totalBranchFeeDouble + totalBisnessInfoFeeDouble + cmpAmtFeeDouble;
			
			
			DecimalFormat df = new DecimalFormat("#0.00");
			
			robFormB.setBranchesAmt(totalBranchFeeDouble);
			robFormB.setBizInfoAmt(totalBisnessInfoFeeDouble);
			robFormB.setCmpAmt(cmpAmtFeeDouble);
			robFormB.setGstAmt(gstAmt);
			robFormB.setTotalAmt(totalFeeDouble);
			
			formBFee.setDefaultModelObject(df.format(formBFeeDouble));
			totalFormBFee.setDefaultModelObject(df.format(totalFormBFeeDouble));
			
			branchFee.setDefaultModelObject(df.format(branchFeeDouble));
			branchFeeDuration.setDefaultModelObject(robFormB.getBalanceYear());
			branchFeePerYear.setDefaultModelObject(df.format(branchFeePerYearDouble));
			totalBranchFee.setDefaultModelObject(df.format(totalBranchFeeDouble));
			
			cmpAmtFee.setDefaultModelObject(df.format(cmpAmtFeeDouble));
			totalCmpAmtFee.setDefaultModelObject(df.format(cmpAmtFeeDouble));
			
			bisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
			totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			
			totalFee.setDefaultModelObject(df.format(totalFeeDouble));
			
			
			List<RobFormOwnerVerification> listRobFormOwnerVerification = robFormB.getListRobFormOwnerVerification();
			for (int i = 0; i < listRobFormOwnerVerification.size(); i++) {
				RobFormOwnerVerification robFormOwnerVerification = listRobFormOwnerVerification.get(i);
				if(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER.equals(robFormOwnerVerification.getStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.partner.pendingEzBizRegister", robFormOwnerVerification.getName())));
				}
				if(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_ACTIVATION.equals(robFormOwnerVerification.getStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.partner.pendingEzBizActication", robFormOwnerVerification.getName())));
				}
				if(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION.equals(robFormOwnerVerification.getStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.partner.pendingVerification", robFormOwnerVerification.getName())));
				}
				if(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT.equals(robFormOwnerVerification.getStatus())){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.partner.userReject", robFormOwnerVerification.getName())));
				}
				
				try {
					BusinessFormAOwnerValidResp resp = robFormAService.isOwnerValidWithIc(robFormOwnerVerification.getName(), robFormOwnerVerification.getIdNo());
					if(!resp.isValid()){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.nameIcNotMatchInCoreSystem", robFormOwnerVerification.getName())));
					}
				} catch (SSMException e) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.partnerValidateFailDueToWS")));
				}
				try {
					BlacklistInfoResp response = robFormAOwnerService.getBlacklistInfoByICNoWS(robFormOwnerVerification.getIdNo(), Parameter.ROB_BLACKLIST_TYPE_newIc, Parameter.ENTITY_TYPE_ROB); //03 = newIc
					if(response.getBlacklistStatus()) { //if true = blacklisted
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.robFormAOwners.ownerBlacklist", robFormOwnerVerification.getName(), robFormOwnerVerification.getIdNo() )));
					}
				} catch (SSMException e) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.partnerValidateFailDueToWS"))); //msg = Webservice Problem!!!
				} 
			}
			
			
			
			//Check AmmendmendDate
			Date today = new Date();
			Date stDate = robFormB.getBizProfileDetResp().getMainInfo().getBizStartDate();
			
			if(!isQuery){
				if(robFormB.getIsB1()){
					if(robFormB.getRobFormB1().getB1AmmendmendDt().after(today)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b1.ammendDtCannotFutureDate")));
					}
					
					if(robFormB.getRobFormB1().getB1AmmendmendDt().before(stDate)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b1.ammendDtCannotB4RegDate")));
					}
				}
				if(robFormB.getIsB2()){
					if(robFormB.getRobFormB2().getB2AmmendmendDt().after(today)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b2.ammendDtCannotFutureDate")));
					}
					if(robFormB.getRobFormB2().getB2AmmendmendDt().before(stDate)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b2.ammendDtCannotB4RegDate")));
					}
				}
				if(robFormB.getIsB3()){
					if(robFormB.getB3AmmendmendDt().after(today)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b3.ammendDtCannotFutureDate")));
					}
					if(robFormB.getB3AmmendmendDt().before(stDate)){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b3.ammendDtCannotB4RegDate")));
					}
				}
			}
			
			
			
			//Check Bisness Code
			if(robFormB.getIsB2()){
				List<RobFormB2Det> listB2Det = robFormB.getRobFormB2().getListRobFormB2Det();
				boolean hasB2Code = false;
				for (int i = 0; i < listB2Det.size(); i++) {
					RobFormB2Det b2Det = listB2Det.get(i);
					if(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW.equals(b2Det.getAmmendmentType())){
						hasB2Code=true;
						break;
					}
					if(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NO_CHANGES.equals(b2Det.getAmmendmentType())){
						hasB2Code=true;
						break;
					}
				}
				if(!hasB2Code){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.bizCode.notEnough")));
				}
				String b2Desc = robFormB.getRobFormB2().getBizDesc().toUpperCase();
				String cbsDesc = robFormB.getBizProfileDetResp().getMainInfo().getBizDesc().toUpperCase();
				if(b2Desc.equals(cbsDesc)){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.bizDescNoChanges")));
				}
				
			}
			if(robFormB.getIsB3()){
				List<RobFormB3> listB3 = robFormB.getListRobFormB3();
				boolean hasB3Changes = false;
				for (int i = 0; i < listB3.size(); i++) {
					RobFormB3 b3 = listB3.get(i);
					if(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NEW.equals(b3.getAmmendmentType()) || Parameter.ROB_FORM_B3_AMENDMENT_TYPE_CLOSE.equals(b3.getAmmendmentType())){
						hasB3Changes = true;
						break;
					}
				}
				
				if(!hasB3Changes){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b3NoChanges")));
				}
				
			}
			
			List<RobFormB4> listB4 = robFormB.getListRobFormB4();
			for (int i = 0; listB4!=null && i < listB4.size(); i++) {
				RobFormB4 b4 = listB4.get(i);
				
				if(StringUtils.isNotBlank(b4.getEzbizUserRefNo())) {
					LlpUserProfile llpUserProfile = llpUserProfileService.findById(b4.getEzbizUserRefNo());
					if(Parameter.USER_STATUS_deceased.equals(llpUserProfile.getUserStatus())  && !Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(b4.getAmmendmentType()) ){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB.b4UserAlreadyDeceased",llpUserProfile.getName())));
					}
				}
			}
				
			
			//Check RobSync
			try {
				if(!robFormBService.isBizInfoHashValid(robFormB) && Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
					String robFormBNotSyncStr = resolve("page.lbl.ezbiz.robFormB.notSyncError", robFormB.getRobFormBCode());
					listError.add(new SSMLabel(listError.newChildId() , robFormBNotSyncStr ));
				}
			} catch (Exception e) {
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.notSyncErrorCheckFailDueToWS")));
			}

			if(listError.size()>0){
				listError.setVisible(true);
				declarationChkBox.setVisible(false);
				submitPayment.setEnabled(false);
				reLodgeFormA.setEnabled(false);
			}else{
				listError.setVisible(false);
				declarationChkBox.setVisible(true);
				submitPayment.setEnabled(false);
				reLodgeFormA.setEnabled(false);
			}			
			
			if(Parameter.YES_NO_yes.equals(robFormB.getIsHasSupportingDoc())){
				downloadSupportingDoc.setDownloadData("SUPPORTING.pdf", "application/pdf", robFormB.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
				removeSupportingDoc.setVisible(true);
			}else{
				downloadSupportingDoc.setVisible(false);
				removeSupportingDoc.setVisible(false);
			}

			if(target!=null){
				target.add(wmcSupportingDoc);
				target.add(wmcFeeSummary);
				target.add(wmcListError);
			}
			
		}
		
	}
	
	private class EditRobFormBSummaryModel  implements Serializable{
		private String status;
		private String queryAnswer;
		private Boolean declarationChkBox = Boolean.FALSE;
		private String isBuyInfo;
		private List<FileUpload>  fileUploadTmp;
		
		public EditRobFormBSummaryModel(RobFormB robFormB) {
			isBuyInfo = robFormB.getIsBuyInfo();
//			if(StringUtils.isNotBlank(robFormB.get))// notes answer
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getQueryAnswer() {
			return queryAnswer;
		}

		public void setQueryAnswer(String queryAnswer) {
			this.queryAnswer = queryAnswer;
		}

		public Boolean getDeclarationChkBox() {
			return declarationChkBox;
		}

		public void setDeclarationChkBox(Boolean declarationChkBox) {
			this.declarationChkBox = declarationChkBox;
		}

		public String getIsBuyInfo() {
			return isBuyInfo;
		}

		public void setIsBuyInfo(String isBuyInfo) {
			this.isBuyInfo = isBuyInfo;
		}

		public List<FileUpload> getFileUploadTmp() {
			return fileUploadTmp;
		}

		public void setFileUploadTmp(List<FileUpload> fileUploadTmp) {
			this.fileUploadTmp = fileUploadTmp;
		}

		
	}
}