package com.ssm.ezbiz.robFormC;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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

import com.ssm.ezbiz.payment.ViewPaymentTransactionPanel;
import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
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
public class ViewRobFormCPage extends SecBasePage{
	
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

	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	public ViewRobFormCPage(final String robFormCCode, Page fromPage) {
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
		final SSMAjaxButton submitPayment;
		
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
			cmpFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL); //guna ker tak ni ???
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
			
			String reason=getCodeTypeWithValue(Parameter.ROB_FORM_C_REASON, robFormC.getReasonType());
			
			SSMTextField reasonType = new SSMTextField("reasonType", new PropertyModel(reason, ""));
			reasonType.setReadOnly(true);
			add(reasonType);
			
			
			
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			add(listError);
			
			robFormCCode = new SSMTextField("robFormCCode");
			robFormCCode.setReadOnly(true);
			robFormCCode.setOutputMarkupId(true);
			add(robFormCCode);
			
		
			wmcformCfileUpload = new WebMarkupContainer("wmcformCfileUpload");
			wmcformCfileUpload.setPrefixLabelKey(prefixLabelKey);
			wmcformCfileUpload.setOutputMarkupId(true);
			wmcformCfileUpload.setOutputMarkupPlaceholderTag(true);
			wmcformCfileUpload.setVisible(false);
			add(wmcformCfileUpload);
			
			//Fee Summary	
			wmcFeeSummaryAll = new WebMarkupContainer("wmcFeeSummaryAll");
			wmcFeeSummaryAll.setPrefixLabelKey(prefixLabelKey+"feeSummary.");
			wmcFeeSummaryAll.setOutputMarkupId(true);
			wmcFeeSummaryAll.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummaryAll);
			
			final ViewPaymentTransactionPanel viewPaymentTransactionPanel = new ViewPaymentTransactionPanel("viewPaymentTransactionPanel", robFormC.getRobFormCCode());
			viewPaymentTransactionPanel.setOutputMarkupId(true);
			viewPaymentTransactionPanel.setOutputMarkupPlaceholderTag(true);
			wmcFeeSummaryAll.add(viewPaymentTransactionPanel);
			
			
			
			
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
			
			String isBuyInfoDesc = getCodeTypeWithValue(Parameter.YES_NO,robFormC.getIsBuyInfo());
			SSMTextField isBuyInfo = new SSMTextField("isBuyInfo",new PropertyModel(isBuyInfoDesc, ""));
			isBuyInfo.setReadOnly(true);
			add(isBuyInfo);

			
			SSMDownloadLink downloadAttachmentCForm = new SSMDownloadLink("downloadAttachmentCForm");
			
			if(robFormC.getSupportingDocId()==null){
				downloadAttachmentCForm.setVisible(false);
			}else{
				LlpFileData fileDataNew = new LlpFileData();
				fileDataNew = llpFileDataService.findById(robFormC.getSupportingDocId());
				downloadAttachmentCForm.setDownloadData(robFormC.getBrNo() + "_BORANG_C.pdf", "application/pdf", fileDataNew.getFileData());
				downloadAttachmentCForm.setVisible(true);
			}
			add(downloadAttachmentCForm);
			
			
			
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
	
			
		    boolean isPayCmpBool = false;
			if(Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp())){
				isPayCmpBool=true;
			}
			
			
			SSMAjaxCheckBox	isPayCmp = new SSMAjaxCheckBox("isPayCmp", new PropertyModel(isPayCmpBool, "") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
				
					RobFormC robFormCForm = (RobFormC) getForm().getDefaultModelObject();
						
						if (String.valueOf(true).equals(robFormCForm.getIsPayCmp())) {
							robFormCForm.setIsBuyInfo(Parameter.YES_NO_yes);
						} else {
							robFormCForm.setIsBuyInfo(Parameter.YES_NO_no);
						}

					
					}
			};
            isPayCmp.setEnabled(false);
			add(isPayCmp);
		

			submitPayment = new SSMAjaxButton("submitPayment", mainValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					if (Parameter.YES_NO_yes.equals(robFormC.getIsPayCmp()) && robFormC.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
						paymentItem1.setPaymentItem(Parameter.ROB_FORM_C_CMP);
						paymentItem1.setQuantity(1);
						paymentItem1.setPaymentDet(robFormC.getBrNo() + "-"
								+ robFormC.getCheckDigit());
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
						setResponsePage(new PaymentDetailPage(robFormC.getRobFormCCode(), RobFormCService.class.getSimpleName(), robFormC,listPaymentItems));
					}else{
						storeSuccessMsgKey("page.lbl.ezbiz.formC.successSubmit");
						setResponsePage(new ListRobFormCTransactionPage());
					}
				}
			};
			
			
			submitPayment.setVisible(false);
			submitPayment.setOutputMarkupId(true);
			wmcFeeSummaryAll.add(submitPayment);
			if(Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT.equals(robFormC.getStatus())){
				submitPayment.setVisible(true);
			}
			
			SSMDownloadLink downloadBusinessInfo = new SSMDownloadLink("downloadBusinessInfo");
			downloadBusinessInfo.setVisible(false);
			wmcFeeSummaryAll.add(downloadBusinessInfo);
			
			SSMDownloadLink downloadBorangCForm = new SSMDownloadLink("downloadBorangCForm");
			downloadBorangCForm.setVisible(false);
			wmcFeeSummaryAll.add(downloadBorangCForm);
			
			
			SSMDownloadLink downloadCompound = new SSMDownloadLink("downloadCmp");
			downloadCompound.setVisible(false);
			wmcFeeSummaryAll.add(downloadCompound);
			
			
			List listRobFormNotes = robFormNotesService.findByFormCode(robFormC.getRobFormCCode());
			
			ListView<RobFormNotes> listQueryView = new ListView("listQueryView", listRobFormNotes) {
				@Override
				protected void populateItem(ListItem item) {
					RobFormNotes robormNotes = (RobFormNotes) item.getModelObject();
					item.add(new Label("queryBy", robormNotes.getQueryBy()));
					item.add(new MultiLineLabel("notes", robormNotes.getNotes()));
					item.add(new MultiLineLabel("notesAnswer", robormNotes.getNotesAnswer()));
					Date updateDt = null;
					if(robormNotes.getUpdateDt()!=null && !robormNotes.getUpdateDt().equals(robormNotes.getCreateDt())){
						updateDt = robormNotes.getUpdateDt();
					}
					item.add(new SSMLabel("createDt", robormNotes.getCreateDt(),"dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", updateDt,"dd/MM/yyyy hh:mm:ss a"));
				}
			    
			};
			add(listQueryView);
			listQueryView.setVisible(false);
			
			if(UserEnvironmentHelper.isInternalUser()){
				listQueryView.setVisible(true);
			}
			
			
			SSMAjaxButton discardApp = new SSMAjaxButton("discardApp") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						robFormCService.discardApp(robFormC);
						setResponsePage(ListRobFormCTransactionPage.class);
					} catch (SSMException e) {
						ssmError(e);
						e.printStackTrace();
						String js = "alert('"+e.getMessage()+"');";
						target.appendJavaScript(js);
					}
				}
			};
			discardApp.setVisible(false);
			wmcFeeSummaryAll.add(discardApp);
			
			if(Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT.equals(robFormC.getStatus()) || Parameter.ROB_FORM_C_STATUS_OTC.equals(robFormC.getStatus())){
				boolean hasPendingOnline = llpPaymentTransactionService.hasPendingOnlineAndSuccessPaymentByAppRefNo(robFormC.getRobFormCCode());
				if(!hasPendingOnline){
					discardApp.setVisible(true);
				}
			}
			
			
			SSMLabel downloadRule = new SSMLabel("downloadRule", new StringResourceModel("page.title.mybiz.editNoteFormA", this, null));
			add(downloadRule);
			downloadRule.setVisible(false);
				
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
				
				if(robFormC.getCmpAmt()>0){
					try {
						LlpFileData fileDataNew = new LlpFileData();
						fileDataNew = llpFileDataService.findById(robFormCTmp.getCmpDataId());
						downloadCompound.setDownloadData(robFormCTmp.getBrNo() + "_CMP.pdf", "application/pdf", fileDataNew.getFileData());
						downloadCompound.setVisible(true);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				int downValidDays = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG, Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS)); //download 14 hari sama renewal?
				
				// downValidDays = downValidDays + 1;
				Calendar cal = Calendar.getInstance();
				//Date generateCert = robFormC.getCreateDt();
				Date generateCert = robFormC.getUpdateDt();
				
				cal.setTime(generateCert);
				
				cal.add(Calendar.DATE, downValidDays);
				
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				String value = resolve("page.title.mybiz.editNoteFormC",  String.valueOf(downValidDays), sdf2.format(cal.getTime()));
				SSMLabel ssmLabel = new SSMLabel("downloadRule", value);
				replace(ssmLabel);
				ssmLabel.setVisible(true);
				
				if(UserEnvironmentHelper.isInternalUser()){
					downloadBorangCForm.setVisible(true);
					if(Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo())){
						downloadBusinessInfo.setVisible(true);
					}
				}else{
					if(new Date().after(cal.getTime())){
						downloadBorangCForm.setVisible(false);
						downloadBusinessInfo.setVisible(false);
						
					}
				}
				
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
