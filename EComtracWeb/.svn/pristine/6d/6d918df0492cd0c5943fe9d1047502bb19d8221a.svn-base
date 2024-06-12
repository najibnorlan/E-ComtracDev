package com.ssm.llp.mod1.page;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;

@SuppressWarnings({ "all" })
public class EditOkuRegistrationPage extends SecBasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobUserOkuService")
	private RobUserOkuService robUserOkuService;
	
	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	
	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;
	
	//public
	public EditOkuRegistrationPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobUserOku robUserOku = new RobUserOku();
				LlpUserProfile llpUserProfile = new LlpUserProfile();
				try {
					if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { //LlpUserEnviroment=external user(public), InternalUserEnviroment=internal(bo), UserEnvironmentTemp=?? 
						llpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile(); //get profile from environment after public login
					}else {
						llpUserProfile = (LlpUserProfile) llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName()); //search from db
					}
					
					if(llpUserProfile!=null) {
						RobUserOku robUserOkuTmp = (RobUserOku) robUserOkuService.findOkuByUserRefNoWithData(llpUserProfile.getUserRefNo()); //find latest record
						if(robUserOkuTmp!=null) {
							robUserOku = (RobUserOku) robUserOkuTmp;
						}
						robUserOku.setUserProfile(llpUserProfile);
						robUserOku.setIdRegUser(llpUserProfile.getIdRegUser());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return robUserOku;
			}
		}));

		init();
	}
	
	
	//officer internal
	public EditOkuRegistrationPage(final RobUserOku robUserOku) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
//				//getSession().setAttribute("robUserOku_", robUserOkuSearchPage);
//				//RobUserOku robUserOku = (RobUserOku) getSession().getAttribute("robUserOku_");
//				RobUserOku robUserOku = robUserOkuSearchPage;
//				if(robUserOku.getDocDataId()!=null) {
//				//	Hibernate.initialize(robUserOku.getDocDataId()); //di dataobject fetch.lazy
//					robUserOku = (RobUserOku) robUserOkuService.initializeOkuWithDataId(robUserOkuSearchPage); //initialize fetch.LAZY
//				}
				return robUserOku;
			}
		}));
		
		init();
	}
	
	
	//reload page
	public EditOkuRegistrationPage(final RobUserOku robUserOku, String msg) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return robUserOku;
			}
		}));
		
		if(StringUtils.isNotBlank(msg)) {
			if(msg.contains("error")) {
				ssmError(msg);
			}else {
				ssmSuccess(msg);
			}
		}
		
		init();
	}
	

	private void init() {
		add(new EditOkuRegistrationForm("form", (IModel<RobUserOku>)getDefaultModel()));
	}


	private class EditOkuRegistrationForm extends Form implements Serializable{
	
		boolean isQuery,isCurrentStatusApproved=false;
		
		public EditOkuRegistrationForm(String id, IModel<RobUserOku> m) {
			super(id, m);
			
			final WebMarkupContainer wmcUserProfileOku;
			final SSMLabel uploadErrorLabel, declarationChkBoxLabel, discardAppChkBoxLabel;
			final SSMAjaxCheckBox declarationChkBox;
			final SSMAjaxButton removeSuppDocOkuBtn, uploadOkuSuppDocBtn, saveBtn, reSubmitAppBtn, discardAppBtn;
			final Button cancelBtn;
			SSMDownloadLink downloadSuppDocOku;
			SSMTextArea queryAnswer, notes;
			SSMLabel remarks, approveBy,okuRegStatus;
			String prefixLabelKey;
			SSMTextField okuCardNo;
			SSMDropDownChoice okuCategory;
			FileUploadField userOkuFileUpload;

			
			final RobUserOku robUserOku = (RobUserOku) m.getObject();
			final RobUserOku robUserOkuCurrent = (RobUserOku) m.getObject();
			
			prefixLabelKey = "page.lbl.user.profile.oku."; 
			//setPrefixLabelKey(prefixLabelKey); //add to form
			setMultiPart(true); //avoid error multiple click button
			
			
			wmcUserProfileOku = new WebMarkupContainer("wmcUserProfileOku");
			wmcUserProfileOku.setOutputMarkupPlaceholderTag(true);
			wmcUserProfileOku.setOutputMarkupId(true);
			wmcUserProfileOku.setPrefixLabelKey(prefixLabelKey);  //add to webmarkupcontainer
			add(wmcUserProfileOku);
			
			
			//semantic tak validate label (sbb bukan input)
			String userOkuJS = "userOku";
			String userOkuFieldToValidate[] = new String[]{"okuCategory","okuCardNo","userOkuFileUpload","queryAnswer"}; 
			String userOkuFieldToValidateRules[] = new String[]{"isReqOkuCategory","isReqMaxLengthNumber[50]","empty","empty"};//Refer form.js dan semantic.js utk validation  
			setSemanticJSValidation(this, userOkuJS, userOkuFieldToValidate, userOkuFieldToValidateRules);
			
			
			//from table rob_user_profile (label add to form, input add to wmc)
			add(new SSMLabel("userRefNo", robUserOku.getUserProfile().getUserRefNo())); 
			add(new SSMLabel("name", robUserOku.getUserProfile().getName()));
			add(new SSMLabel("idType", robUserOku.getUserProfile().getIdType(),Parameter.ID_TYPE));
			add(new SSMLabel("idNo" , robUserOku.getUserProfile().getIdNo()));
			add(new SSMLabel("nationality" , robUserOku.getUserProfile().getNationality(), Parameter.NATIONALITY_TYPE));
			add(new SSMLabel("gender" , robUserOku.getUserProfile().getGender(), Parameter.GENDER));
			add(new SSMLabel("race" , robUserOku.getUserProfile().getRace(),Parameter.RACE));
			
			
			//from table rob_user_oku
			add(new SSMLabel("okuRefNo", robUserOku.getOkuRefNo()));
			
			//add(new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(),Parameter.OKU_REGISTRATION_STATUS));
			add(okuRegStatus = new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(),Parameter.OKU_REGISTRATION_STATUS));
			if(Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:green;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_PENDING.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:blue;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:blue;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_REJECT.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:red;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:red;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_CANCEL.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_WITHDRAW.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}
			
		/*	add(new SSMLabel("applicationDt", robUserOku.getApplicationDt()));
			add(new SSMLabel("approvalDt", robUserOku.getApprovalDt()));

			//tak jd..check semula hide label
			add(approveBy = new SSMLabel("approveBy", robUserOku.getApproveBy()));	
			add(remarks = new SSMLabel("remarks", robUserOku.getRemarks()));
			
			if(!UserEnvironmentHelper.isInternalUser()){
				remarks.setVisible(false);
				approveBy.setVisible(false);
			}
		*/	
			okuCardNo = new SSMTextField("okuCardNo"); //"okuCardNo" "okuCategory" nama kena sama data object RobUserOku utk auto-populate.
			okuCardNo.setRequired(true);
			okuCardNo.add(StringValidator.maximumLength(50));
			wmcUserProfileOku.add(okuCardNo);
			
			okuCategory = new SSMDropDownChoice("okuCategory", Parameter.OKU_CATEGORY);
			okuCategory.setRequired(true);
			wmcUserProfileOku.add(okuCategory);
			
			
			//remove oku attachment
			removeSuppDocOkuBtn = new SSMAjaxButton("removeSuppDocOkuBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					LlpFileData suppDocOku = robUserOku.getDocDataId();
					if(suppDocOku!=null){
						//llpFileDataService.delete(suppDocOku);
						robUserOku.setDocDataId(null);
						robUserOku.setIsHasSupportingDoc(Parameter.YES_NO_no);
						setResponsePage(new EditOkuRegistrationPage(robUserOku,"page.lbl.user.profile.oku.successRemoveSuppDocOku")); //reload
					}
				}
			};
			removeSuppDocOkuBtn.setVisible(false);
			if(robUserOku.getDocDataId()!=null) {
				removeSuppDocOkuBtn.setVisible(true);	
			}
			wmcUserProfileOku.add(removeSuppDocOkuBtn);
			
			
			//download oku attachment
			downloadSuppDocOku = new SSMDownloadLink("downloadSuppDocOku");
			downloadSuppDocOku.setVisible(false);
			if(robUserOku.getDocDataId()!=null) {
				downloadSuppDocOku.setDownloadData(robUserOku.getUserProfile().getIdNo()+"_OKU_DOCUMENT.pdf", "application/pdf", robUserOku.getDocDataId().getFileData());
				downloadSuppDocOku.setVisible(true);
			}
			wmcUserProfileOku.add(downloadSuppDocOku);
			
			
			//upload oku attachment---------------------------------------------------
			uploadErrorLabel = new SSMLabel("uploadErrorLabel","");
			uploadErrorLabel.setEscapeModelStrings(false); 
			uploadErrorLabel.setOutputMarkupId(true);  
			wmcUserProfileOku.add(uploadErrorLabel);
			
			userOkuFileUpload = new FileUploadField("userOkuFileUpload"); //choose file upload
			userOkuFileUpload.setRequired(true);
			userOkuFileUpload.setEnabled(true);
			if(robUserOku.getDocDataId()!=null) {
			userOkuFileUpload.setEnabled(false);
			}
			wmcUserProfileOku.add(userOkuFileUpload);
			
			uploadOkuSuppDocBtn = new SSMAjaxButton("uploadOkuSuppDocBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobUserOku robUserOkuTmp = (RobUserOku) form.getModelObject();
					boolean isError = false;
					if(robUserOkuTmp.getUserOkuFileUpload()!=null){
						if(robUserOkuTmp.getUserOkuFileUpload().get(0).getBytes().length > Parameter.MAX_UPLOAD_SIZE){
							uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.user.profile.oku.error.exceedUploadSize"));
							isError = true;
						}else{
							try {
								ByteArrayInputStream bais = new ByteArrayInputStream(robUserOkuTmp.getUserOkuFileUpload().get(0).getBytes());
								PDDocument document = PDDocument.load(bais);
								document.close();
							} catch (Exception e) {
								uploadErrorLabel.setDefaultModelObject(resolve("page.lbl.user.profile.oku.error.notInPDF"));
								isError = true;
							}
						}
						
						if(!isError){
							LlpFileData okuSupportingDoc = new LlpFileData();
							okuSupportingDoc.setFileData(robUserOkuTmp.getUserOkuFileUpload().get(0).getBytes());
							okuSupportingDoc.setFileDataType("PDF");
							llpFileDataService.insert(okuSupportingDoc);
							robUserOkuTmp.setDocDataId(okuSupportingDoc);
							robUserOkuTmp.setIsHasSupportingDoc(Parameter.YES_NO_yes);
							setResponsePage(new EditOkuRegistrationPage(robUserOkuTmp,"page.lbl.user.profile.oku.successUploadOkuSuppDoc")); //reload
							}
						}

					if(isError){
						target.add(uploadErrorLabel);
					}
				}
			 };
			//uploadOkuSuppDoc.setVisible(true); //test if initial reload=true / if by default visible true..
			if(robUserOku.getDocDataId()!=null) {
				uploadOkuSuppDocBtn.setVisible(false);
			}
			wmcUserProfileOku.add(uploadOkuSuppDocBtn);
			
			
		//officer kueri (display)-----------------------------------------------------
			notes = new SSMTextArea("notes",Model.of(""));
			notes.setVisible(false);
			notes.setReadOnly(true);
			
			//public answer query
			queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(false);
			
			if(Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus())) {
				isQuery = true;
				//populate display
				if (robUserOku.getListRobFormNotes().size()>0) {
				RobFormNotes formNotes = robUserOku.getListRobFormNotes().get(robUserOku.getListRobFormNotes().size()-1);
				notes.setDefaultModelObject(formNotes.getNotes());
				notes.setVisible(true);
					if(StringUtils.isNotBlank(formNotes.getNotesAnswer())){
						robUserOku.setQueryAnswer(formNotes.getNotesAnswer());
					}
				}
			queryAnswer.setVisible(true);
			}
			
			wmcUserProfileOku.add(notes);
			wmcUserProfileOku.add(queryAnswer);
			
			
			reSubmitAppBtn = new SSMAjaxButton("reSubmitAppBtn") { //answer query
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			//reSubmitAppBtn = new Button("reSubmitAppBtn") {
			//	 public void onSubmit() {
					RobUserOku model = (RobUserOku) getForm().getModelObject(); //getModelObject=getDefaultModelObject = currently input.
					if(model.getDocDataId()!=null) {
						if((isQuery) && (robUserOku.getListRobFormNotes().size()>0)) {
							RobFormNotes formNotesAnswer = robUserOku.getListRobFormNotes().get(robUserOku.getListRobFormNotes().size()-1);
							formNotesAnswer.setNotesAnswer(model.getQueryAnswer());
							robFormNotesService.update(formNotesAnswer);
							}
						LlpFileData updateOkuSupportingDoc = robUserOku.getDocDataId();
						llpFileDataService.update(updateOkuSupportingDoc);
						model.setIsHasSupportingDoc(Parameter.YES_NO_yes);
						model.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_PENDING);
						RobUserOku newModel = robUserOkuService.insertUpdateOkuApplication(model,"updateOku");
						setResponsePage(new ViewOkuRegistrationPage(newModel,null));
					}else {	
//						setResponsePage(new EditOkuRegistrationPage(model,SSMExceptionParam.LLP_REG_ATTACHMENT_NOT_FOUND)); //reload
						String errorScript = WicketUtils.generateAjaxErrorAlertScript("Attachment Not Found", resolve(SSMExceptionParam.LLP_REG_ATTACHMENT_NOT_FOUND));
		        		target.prependJavaScript(errorScript);
		        		return;
					}
				}
			};
			reSubmitAppBtn.setOutputMarkupId(true); //ajax error.. jika ada visible elok letak kedua2 ni..
			reSubmitAppBtn.setOutputMarkupPlaceholderTag(true); 
			reSubmitAppBtn.setVisible(false);
			if (isQuery) {
				reSubmitAppBtn.setVisible(true);
			}
			reSubmitAppBtn.setEnabled(robUserOku.getDeclarationChkBox()); //default false (di data object)
			add(reSubmitAppBtn);
			
		//----------------------------------query (end)------------------------------	
			
			if (Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus())) {
				isCurrentStatusApproved = true;
			}
			saveBtn = new SSMAjaxButton("saveBtn",userOkuJS){//semantic tak jd..
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobUserOku model = (RobUserOku) getForm().getModelObject();
					if(model.getDocDataId()!=null) { //attachment
						if (StringUtils.isNotBlank(model.getOkuRefNo())) { //check if not first time insert..
							try {
								robUserOkuService.updatePreviousRecordStatus(robUserOkuCurrent); //update existing record
							} catch (SSMException e) {
								e.printStackTrace();
								ssmError(e); //status Q,B not allowed to change (button dah control hide!).
								//String js = "alert('"+resolve(e.getMessage())+"');";
								//target.appendJavaScript(js);
								//target.prependJavaScript(js);
								String errorScript = WicketUtils.generateAjaxErrorAlertScript("Status Error", resolve(e.getMessage()));
				        		target.prependJavaScript(errorScript);
								return;
							}
						}
						LlpFileData insertOkuSupportingDoc = robUserOku.getDocDataId();
						llpFileDataService.insert(insertOkuSupportingDoc);
						model.setIsHasSupportingDoc(Parameter.YES_NO_yes);
						model.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_PENDING);
						RobUserOku newModel = robUserOkuService.insertUpdateOkuApplication(model, "insertOku");
						setResponsePage(new ViewOkuRegistrationPage(newModel,null));
					}else {	
						//String js = "alert('"+SSMExceptionParam.LLP_REG_ATTACHMENT_NOT_FOUND+"');";
						//target.appendJavaScript(js);
						//target.prependJavaScript(js);
						String errorScript = WicketUtils.generateAjaxErrorAlertScript("Attachment Not Found", resolve(SSMExceptionParam.LLP_REG_ATTACHMENT_NOT_FOUND));
		        		target.prependJavaScript(errorScript);
		        		//setResponsePage(new EditOkuRegistrationPage(model,SSMExceptionParam.LLP_REG_ATTACHMENT_NOT_FOUND)); //reload (tak jadi lak lepas ajax confirm)
						return;
					}
					
					//ni utk apa??
//					SignInSession signInSession = (SignInSession)getSession();
//					if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
//						ssmError("Discard C");
//						setResponsePage(new ExtInterface());
//					}else {
//						setResponsePage(ListRobFormCTransactionPage.class);
//					}
				}
				
				//popup ajax confirm
				protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
					if(isCurrentStatusApproved) {
			        super.updateAjaxAttributes( attributes );
			        String defaultConfirmTitle = resolve("page.lbl.user.profile.oku.ajaxPopup.confirmProceed"); //do u want to proceed?
			        String defaultConfirmDesc = resolve("page.lbl.user.profile.oku.ajaxPopup.confirmProceedApproveStatus"); //  Current status is Approve. This will change the status to pending and may need approval again.
			        
					AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, defaultConfirmTitle, defaultConfirmDesc);
					attributes.getAjaxCallListeners().add( ajaxCallListener );
			    }
			  }
			};
			
			saveBtn.setOutputMarkupId(true);
			saveBtn.setOutputMarkupPlaceholderTag(true);
		//	save.setVisible(true); //test if initial reload page = true
			if((isQuery) || (isCurrentStatusApproved) || Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) { //if status query/revoke/approve hide button
				saveBtn.setVisible(false);
			}
			saveBtn.setEnabled(robUserOku.getDeclarationChkBox()); //default false (di data object)
			add(saveBtn);
			
			//------------------------------------------------------------------------------
				
			//declaration wording label
			declarationChkBoxLabel = new SSMLabel("declarationChkBoxLabel",resolve("declaration.tick.declare"));
			declarationChkBoxLabel.setEscapeModelStrings(false); 
			declarationChkBoxLabel.setOutputMarkupId(true);  
			
			declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robUserOku, "declarationChkBox")) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (String.valueOf(true).equals(getValue())) {
						robUserOku.setDeclarationChkBox(true);
					} else {
						robUserOku.setDeclarationChkBox(false);
					}
					
					saveBtn.setEnabled(robUserOku.getDeclarationChkBox()); //button save enable depends on tick checkbox
					reSubmitAppBtn.setEnabled(robUserOku.getDeclarationChkBox()); //button resubmit (query)
					arg0.add(saveBtn);
					arg0.add(reSubmitAppBtn);
				}
			};
			
			if((isCurrentStatusApproved) || (Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus()))) {
				declarationChkBoxLabel.setVisible(false);
				declarationChkBox.setVisible(false);
			}
			add(declarationChkBoxLabel); //label takleh wmc validation semanticJS ??
			add(declarationChkBox);
			

			cancelBtn = new Button("cancelBtn") {
				public void onSubmit() {
				if(UserEnvironmentHelper.isInternalUser()){
						//setResponsePage(ListRobUserOkuSearchPage.class);
						setResponsePage(ListRobUserOkuWorkingTrayPage.class);
				}else {
						setResponsePage(GuidelinePage.class);	
				  }
				}
			}.setDefaultFormProcessing(false);
			add(cancelBtn);
			
			
			//-----------------public can withdraw application (if status approve only)-----------------------
			final SSMLabel remarkDiscardAppLbl = new SSMLabel("remarkDiscardAppLbl", resolve("page.lbl.user.profile.oku.userDiscardReason")); //text area label
			remarkDiscardAppLbl.setOutputMarkupId(true);
			remarkDiscardAppLbl.setOutputMarkupPlaceholderTag(true);
			remarkDiscardAppLbl.setVisible(false);
			add(remarkDiscardAppLbl);
			
			final SSMTextArea remarkDiscardApp = new SSMTextArea("remarkDiscardApp", Model.of("")); //text area input
			remarkDiscardApp.setOutputMarkupId(true);
			remarkDiscardApp.setOutputMarkupPlaceholderTag(true);
			remarkDiscardApp.setVisible(false);
			remarkDiscardApp.setRequired(true);
			add(remarkDiscardApp);
			
			
			discardAppBtn = new SSMAjaxButton("discardAppBtn") { //public withdraw from the approved application (ie. telah pulih oku)
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobUserOku modelD = (RobUserOku) getForm().getModelObject(); //getModelObject=getDefaultModelObject = currently input.
					if(isCurrentStatusApproved){
					modelD.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_WITHDRAW); //discard by public
					modelD.setRemarks(StringUtils.isNotBlank(remarkDiscardApp.getDefaultModelObject().toString())?
							remarkDiscardApp.getDefaultModelObject().toString():""); //public put remark
					
					robUserOkuService.discardApplication(modelD);
					}
					setResponsePage(new ViewOkuRegistrationPage(modelD,null));
				}
				
				//popup ajax confirm
				protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
					if(isCurrentStatusApproved) {
			        super.updateAjaxAttributes( attributes );
			        String defaultConfirmTitle = resolve("page.lbl.user.profile.oku.ajaxPopup.confirmProceed"); //do u want to proceed?
			        String defaultConfirmDesc = resolve("page.lbl.user.profile.oku.ajaxPopup.confirmWithdrawApproveStatus"); //  Current status is Approve.
			        
					AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, defaultConfirmTitle, defaultConfirmDesc);
					attributes.getAjaxCallListeners().add( ajaxCallListener );
				}
			  }
			};
			discardAppBtn.setOutputMarkupId(true); 
			discardAppBtn.setOutputMarkupPlaceholderTag(true); 
			discardAppBtn.setVisible(false);
			add(discardAppBtn);
			
			
			//checkbox wording label
			discardAppChkBoxLabel = new SSMLabel("discardAppChkBoxLabel",resolve("page.lbl.user.profile.oku.tickToWithdraw"));
			discardAppChkBoxLabel.setEscapeModelStrings(false); 
			discardAppChkBoxLabel.setOutputMarkupId(true);  
			discardAppChkBoxLabel.setVisible(false);
			
			SSMAjaxCheckBox discardAppChkBox = new SSMAjaxCheckBox("discardAppChkBox", new PropertyModel(robUserOku, "discardAppChkBox")) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg1) {
					if (String.valueOf(true).equals(getValue())) {
						robUserOku.setDiscardAppChkBox(true);
					} else {
						robUserOku.setDiscardAppChkBox(false);
					}
					
					remarkDiscardAppLbl.setVisible(robUserOku.getDiscardAppChkBox());
					remarkDiscardApp.setVisible(robUserOku.getDiscardAppChkBox());
					discardAppBtn.setVisible(robUserOku.getDiscardAppChkBox()); //button depends on tick checkbox
					arg1.add(remarkDiscardAppLbl);
					arg1.add(remarkDiscardApp);
					arg1.add(discardAppBtn);
				}
			};
			discardAppChkBox.setVisible(false);
			
			if(isCurrentStatusApproved) {
				discardAppChkBoxLabel.setVisible(true);
				discardAppChkBox.setVisible(true);
			}
			add(discardAppChkBoxLabel);
			add(discardAppChkBox);
			
			
//			Button print = new Button("print") {
//				public void onSubmit() {
//					LlpUserProfile llpUserProfile = (LlpUserProfile) getForm().getModelObject();
//					setResponsePage(new LlpUserProfileEmailNotification(llpUserProfile));
//				}
//			};
//			add(print);
//			print.setVisible(true); // ada
//
//			if (!(Parameter.USER_STATUS_active.equals(llpUserProfileNew.getUserStatus()))) {
//				print.setVisible(false);
//			}
			
		}
	}
		
	public String getPageTitle() {
		String titleKey = "page.lbl.user.profile.oku.titleEdit";
		return titleKey;
	}
}

