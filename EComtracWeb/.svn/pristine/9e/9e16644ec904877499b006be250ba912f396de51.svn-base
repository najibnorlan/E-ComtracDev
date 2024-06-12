package com.ssm.llp.mod1.page;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.web.util.UriUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserTnc;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserTncService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class UserTncPage extends BasePage{
	
	@SpringBean(name = "RobUserTncService")
	private RobUserTncService robUserTncService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpParametersService")
	public LlpParametersService parametersService;
	
	@SpringBean(name = "LlpFileUploadService")
	public LlpFileUploadService fileUploadService;
	
	@SpringBean(name = "mailService")
	MailService mailService;
	
	
	public UserTncPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
		protected Object load() {
			return new RobUserTnc();
			}
		}));
		init();
	}
	
	private void init() {
		add(new UserTncForm("form", getDefaultModel()));
	}
	
	
	private class UserTncForm extends Form implements Serializable {
		
		LlpUserProfile llpUserProfile = new LlpUserProfile();
		RobUserTnc robUserTnc = new RobUserTnc();
		RobUserTnc robUserTncTmp = new RobUserTnc();
		
		String tncType = Parameter.TNC_TYPE_USER_COMTRAC; //for comtrac user
		String name="", userRefNo="", idNo="", loginId="", userStatus= "";
		SSMLabel userTncLbl = new SSMLabel("userTnc","");
		SSMLabel downloadTncLbl = new SSMLabel("downloadTnc","");
		
		public UserTncForm(String id, IModel m) {
			super(id, m);
			String prefixLbl = "page.lbl.userComtracTnc."; //for comtrac user
			setPrefixLabelKey(prefixLbl); //utk html Label ie. "myKadNoLabel".
			robUserTnc = (RobUserTnc) m.getObject();
			
			//public
			if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { //LlpUserEnviroment=external user(public), InternalUserEnviroment=internal(bo).
				llpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile(); //get profile from environment after public login
			}else {
				llpUserProfile = (LlpUserProfile) llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName()); //search from db if null or internal officer login etc
			}
			
			if(llpUserProfile!=null) { //akan null jika public tak login.
				userRefNo = llpUserProfile.getUserRefNo();
				name = llpUserProfile.getName();
				idNo = llpUserProfile.getIdNo();
				loginId = llpUserProfile.getLoginId();
				userStatus = llpUserProfile.getUserStatus();
				
				userTncLbl = new SSMLabel("userTnc", resolve(prefixLbl+"afterLogin", name, idNo, loginId));
			} 
			else {
				//downloadTncLbl = new SSMLabel("downloadTnc", new StringResourceModel("pal.declaration.userTnc", this, null));
				//downloadTncLbl = new SSMLabel("downloadTnc", resolve("pal.declaration.userTnc"));
				downloadTncLbl = new SSMLabel("downloadTnc", resolve(prefixLbl+"afterLogin", " "," "," "));
			}
			
			add(userTncLbl); //tnc wording..
			add(downloadTncLbl); //for internal officer n if not login.
			
			
			
//			add(new Button("exitBtn") { //exit ezbiz
//				public void onSubmit() {
//					SignInSession session = (SignInSession)getSession();
//					session.invalidate();
//					setResponsePage(HomePage.class);
//				}
//			}.setDefaultFormProcessing(false));
			
//			SSMAjaxLink saveTnc = new SSMAjaxLink("saveBtn") { //agree to tnc
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				robUserTnc = robUserTncService.findActiveUserTnc(userRefNo, loginId, tncType);
//		    	if(robUserTnc==null) {
//		    		robUserTnc = new RobUserTnc();
//		    		robUserTnc.setUserRefNo(llpUserProfile.getUserRefNo());
//		    		robUserTnc.setIdRegUser(llpUserProfile.getIdRegUser());
//		    		robUserTnc.setLoginId(llpUserProfile.getLoginId());
//		    		robUserTnc.setIdNo(llpUserProfile.getIdNo());
//		    		robUserTnc.setTncType(Parameter.TNC_TYPE_USER_REGISTRATION); 
//		    		robUserTnc.setIsAgree(Parameter.TNC_IS_AGREE_yes);
//		    		robUserTnc.setAgreementDt(new Date());
//		    		robUserTnc.setAgreementStatus(Parameter.TNC_AGREEMENT_STATUS_active);
//		    		robUserTnc.setRemarks(null);
//		    		robUserTncService.insert(robUserTnc);
//		    		setResponsePage(GuidelinePage.class);
//		    	}
//			}
//		};
//		saveTnc.setOutputMarkupId(true);
//		saveTnc.setOutputMarkupPlaceholderTag(true);
//		saveTnc.setEnabled(robUserTnc.getDeclarationChkBox()); //onload default false (as per data object).
//		add(saveTnc);
			
			
			
			final SSMTextField myKadNo = new SSMTextField("myKadNo", Model.of(""));
			//myKadNo.setNoLabel(); //bypass JSValidation for html label..
			add(myKadNo);
			
			SSMAjaxFormSubmitBehavior myKadNoOnBlur = new SSMAjaxFormSubmitBehavior("onblur", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					robUserTncTmp = (RobUserTnc) getForm().getDefaultModelObject(); //dummy
					myKadNo.add(new NumberValidator()); //numeric only
					myKadNo.add(StringValidator.maximumLength(12));
				}
			};
			myKadNo.add(myKadNoOnBlur);
			
			
			
			final WebMarkupContainer remarksLblWmc = new WebMarkupContainer("remarksLblWmc");
			remarksLblWmc.setOutputMarkupId(true);
			remarksLblWmc.setOutputMarkupPlaceholderTag(true);
			remarksLblWmc.setVisible(false);
			add(remarksLblWmc);
			
			final SSMLabel remarks = new SSMLabel("remarks","");
			remarks.setDefaultModelObject(""); //initialize
			remarksLblWmc.add(remarks);
			
			
			final String jsValidationApprove = "jsValidationApprove";
			String mainFieldToValidateApprove[] = new String[]{ "myKadNo"};
			String mainFieldToValidateRulesApprove[] = new String[]{ "empty"};
			setSemanticJSValidation(this, jsValidationApprove, mainFieldToValidateApprove, mainFieldToValidateRulesApprove);
			
			
	    	SSMAjaxLink exitTnc = new SSMAjaxLink("exitBtn") { //exit comtrac
				@Override
				public void onClick(AjaxRequestTarget target) {
					SignInSession session = (SignInSession)getSession();
					session.invalidate();
					setResponsePage(HomePage.class);
				}
	    	};
	    	exitTnc.setOutputMarkupId(true);
	    	exitTnc.setOutputMarkupPlaceholderTag(true);
			add(exitTnc);
			
			
			final SSMAjaxButton saveTnc = new SSMAjaxButton("saveBtn", jsValidationApprove) { //agree to tnc
				@Override
				protected void onSubmit(AjaxRequestTarget request, Form form) {
					robUserTnc = robUserTncService.findActiveUserTnc(userRefNo, loginId, tncType);
					//robUserTncTmp = (RobUserTnc) getForm().getDefaultModelObject();
			    	if(robUserTnc==null) {
			    		robUserTnc = new RobUserTnc();
			    		robUserTnc.setUserRefNo(llpUserProfile.getUserRefNo());
			    		robUserTnc.setIdRegUser(llpUserProfile.getIdRegUser());
			    		robUserTnc.setLoginId(llpUserProfile.getLoginId());
			    		robUserTnc.setIdNo(llpUserProfile.getIdNo());
			    		robUserTnc.setTncType(Parameter.TNC_TYPE_USER_COMTRAC); 
			    		robUserTnc.setIsAgree(Parameter.TNC_IS_AGREE_yes);
			    		robUserTnc.setAgreementDt(new Date());
			    		robUserTnc.setAgreementStatus(Parameter.TNC_AGREEMENT_STATUS_active);
			    		robUserTnc.setRemarks(remarks.getDefaultModelObjectAsString());
			    		robUserTncService.insert(robUserTnc);
			    		
			    		mailService.sendMail(llpUserProfile.getEmail(), "email.notification.userComtracTnc.subject", llpUserProfile.getIdNo(),
			    		 "email.notification.userComtracTnc.body",llpUserProfile.getName(),llpUserProfile.getUserRefNo(),llpUserProfile.getIdNo(),llpUserProfile.getLoginId());
			    		
			    		String msj = resolve(getForm().getPrefixLabelKey()+"receivedAgreement", robUserTnc.getIdNo(), name, robUserTnc.getLoginId());
						storeSuccessMsg(msj);
						
						setResponsePage(GuidelinePage.class);
			    	}
				}
			};
			saveTnc.setOutputMarkupId(true);
			saveTnc.setOutputMarkupPlaceholderTag(true);
			saveTnc.setEnabled(robUserTnc.getDeclarationChkBox()); //onload default false (as per data object).
			add(saveTnc);
			
			
			//checkbox label
			final SSMLabel declarationChkBoxLabel = new SSMLabel("declarationChkBoxLabel",resolve(prefixLbl+"userDeclaration.checkboxLbl"));
			declarationChkBoxLabel.setEscapeModelStrings(false); 
			declarationChkBoxLabel.setOutputMarkupId(true);  
			
			SSMAjaxCheckBox declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robUserTnc, "declarationChkBox")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) { //tick checkbox
						if((StringUtils.isNotBlank(myKadNo.getValue())) && (myKadNo.getModelObject().equals(idNo))){
//							System.out.println("idNo= "+idNo);
//							System.out.println("myKadNo.getValue()= "+myKadNo.getValue());
//							System.out.println("myKadNo.getModelObject()= "+myKadNo.getModelObject());
							remarks.add(new AttributeAppender("style", "color:green; font-weight:bold"));
							remarks.setDefaultModelObject(resolve(prefixLbl+"userDeclaration.remarks", name, idNo, loginId));
							remarksLblWmc.setVisible(true);
							//robUserTnc.setRemarks(remarks.getDefaultModelObjectAsString());
							robUserTnc.setDeclarationChkBox(true);
						}
						else {
							robUserTnc.setDeclarationChkBox(false);
							remarks.setDefaultModelObject("");
							remarksLblWmc.setVisible(false);
							myKadNo.setDefaultModelObject("");
							String jsAlert = generateAjaxErrorAlertScript(resolve(prefixLbl+"errorMyKadValidationTitle"), resolve(prefixLbl+"errorMyKadValidationDesc"));
							target.prependJavaScript(jsAlert);
							target.prependJavaScript("$('#" + getMarkupId() + "').prop('checked',false);"); //untick checkbox again.
						}
					}
					else{ //not tick checkbox
						robUserTnc.setDeclarationChkBox(false);
						remarks.setDefaultModelObject("");
						remarksLblWmc.setVisible(false);
					}
					
					saveTnc.setEnabled(robUserTnc.getDeclarationChkBox()); //button save enable depends on tick checkbox
					remarksLblWmc.setVisible(robUserTnc.getDeclarationChkBox()); //depends on checkbox
					target.add(saveTnc);
					target.add(remarks);
					target.add(remarksLblWmc);
					target.add(myKadNo);
				}
			};
			remarks.setOutputMarkupId(true); //resolve error
			remarks.setOutputMarkupPlaceholderTag(true);

			
			
//			Url url = ((WebRequest)RequestCycle.get().getRequest()).getUrl();
//			String fullUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(url);
//			String absoluteUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse("../../fileAttach?fileId=PORTAL_TERM_CONDITION"));
			
//			SSMDownloadLink portalTermLink = new SSMDownloadLink("portalTermLink", "PORTAL_TERM_CONDITION");
//			add(portalTermLink);
//			SSMDownloadLink protocalElodgementLink = new SSMDownloadLink("protocalElodgementLink", "PROTOCOL_ELODGEMENT");
//			add(protocalElodgementLink);
			
			LlpFileUpload tncPDF = fileUploadService.findByFileCode("PORTAL_TERM_CONDITION_COMTRAC");
			LlpFileUpload pelPDF = fileUploadService.findByFileCode("PROTOCOL_ELODGEMENT_COMTRAC");
			
			if((getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_USER_TNC_COMTRAC).equals(Parameter.YES_NO_yes)) && (UserEnvironmentHelper.getUserenvironment() != null)
					&& (tncPDF!=null && pelPDF!=null)){
				try {
					String pdfPath = generatePDFLink(tncPDF, pelPDF);
					RedirectPage page = new RedirectPage (pdfPath);
					InlineFrame frameComponent = new InlineFrame("frameComponent", page);  
					add(frameComponent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				SSMLabel frameComponent = new SSMLabel("frameComponent","");
				add(frameComponent); 
				frameComponent.setVisible(false);
			}
			
			
			String enableUserTnc = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_USER_TNC_COMTRAC);
			boolean hasTncAgreement = robUserTncService.hasAgreeTnc(userRefNo, loginId, tncType);
			
//			if((!Parameter.USER_STATUS_active.equals(userStatus)) || (!Parameter.YES_NO_yes.equals(enableUserTnc))) { //hide semua jika userstatus P atau turnoff config tnc.
			if((llpUserProfile==null) || !Parameter.YES_NO_yes.equals(enableUserTnc)) { //for comtrac ada user status P (kerana tak mandatory verify ic).
				userTncLbl.setVisible(false);
				exitTnc.setVisible(false);
				saveTnc.setVisible(false);
				declarationChkBoxLabel.setVisible(false);
				declarationChkBox.setVisible(false);
				myKadNo.setVisible(false);
				remarksLblWmc.setVisible(false);
			}
//			if(Parameter.USER_STATUS_active.equals(userStatus) && hasTncAgreement) { //hide button..after accept tnc..
			if(hasTncAgreement) { //hide button..after accept tnc..for comtrac ada user status P (kerana tak mandatory verify ic).
				exitTnc.setVisible(false);//button
				saveTnc.setVisible(false);//button
				declarationChkBoxLabel.setVisible(false);
				declarationChkBox.setVisible(false);
				myKadNo.setVisible(false);
				//remarksLblWmc.setVisible(true);
			}
			
			add(declarationChkBoxLabel);
			add(declarationChkBox);
			
		}
		
		
		private String generatePDFLink(LlpFileUpload tncPDF, LlpFileUpload pelPDF) throws Exception{
			String tncPDF_updDt = new SimpleDateFormat("yyyyMMddHHmmss").format(tncPDF.getUpdateDt());
			String pelPDF_updDt = new SimpleDateFormat("yyyyMMddHHmmss").format(pelPDF.getUpdateDt());

			String version = tncPDF_updDt+"_"+pelPDF_updDt; //to create file version..
			//String pdfFileName = "TNCnPEL-"+version+".pdf";
			String pdfFileName = resolve("page.lbl.userComtracTnc.pdfFileName")+"-"+version+".pdf";
			
			ServletContext servletContext = WebApplication.get().getServletContext(); //get path (url)
			String pdfRealPath = servletContext.getRealPath("/");
			if(pdfRealPath.endsWith(File.separator)) { //file separator "\"
				pdfRealPath+="download"+File.separator +pdfFileName; //put pdf in folder ie. EzBizWeb/download
			}else {
				pdfRealPath+=File.separator+"download"+File.separator+pdfFileName;
			}
			
			File filePdfTmp = new File(pdfRealPath);
			if(!filePdfTmp.exists()) { //if first time, file not been created yet in the download folder..
				Document mergedDocument = new Document(PageSize.A4, 0, 0, 0, 0);
				PdfSmartCopy pdfMerge = new PdfSmartCopy(mergedDocument, new FileOutputStream(filePdfTmp));
				
				mergedDocument.open();
				mergeFileData(tncPDF, pdfMerge);
				mergeFileData(pelPDF, pdfMerge);
				mergedDocument.close();
			}
			
			//String pdfRelativePath = "download/"+pdfFileName; //store pdf in EzBizWeb/download
			//System.out.println("iframepdf absolute path="+pdfRealPath);
			//System.out.println("iframepdf relative path="+pdfRelativePath);
			//return pdfRelativePath;
			
			String pdfDocSrc = getContextPath()+"/ViewerJS/?"; //masuk ke folder ViewerJS
			pdfDocSrc+=	"entityName="+UriUtils.encodePath(resolve(getPrefixLabelKey()+"iFrame.header"), "UTF-8")+"&";	//encode header frame name utk space etc..
			pdfDocSrc+=	"canPrint=N"+"&"; //set no printing
			pdfDocSrc+="#../download/"+pdfFileName; //location file pdf lepas # (keluar folder ViewerJS dan masuk ke folder yg store pdf).
			
			
			//String pdfDocSrc = getContextPath()+"/download/"+pdfFileName; //tak boleh view PDF kat mobile phone.
			
			return pdfDocSrc;
		}
		
		
		public void mergeFileData(LlpFileUpload fileUpload,PdfSmartCopy pdfMerge) throws Exception{	
			
			if(fileUpload.getFileType().toLowerCase().indexOf("pdf")!=-1) {
				PdfReader reader = new PdfReader(fileUpload.getFileData());
				pdfMerge.addDocument(reader);
				reader.close();
			}else {
				PdfReader reader = new PdfReader(generatePDFByteFromImage(fileUpload.getFileData())); //generate PDf jika fileType bukan pdf.
				pdfMerge.addDocument(reader);
				reader.close();
			}
		}
		
		public byte[] generatePDFByteFromImage(byte byteDataImage[]) throws Exception{
			final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

			final Document imageDocument = new Document(PageSize.A4, 0, 0, 0, 0);
			float DOC_WIDTH = imageDocument.getPageSize().getWidth();
			float DOC_HEIGHT = imageDocument.getPageSize().getHeight();
			PdfWriter.getInstance(imageDocument, byteStream);

			imageDocument.open();
			imageDocument.newPage();
			// Create single page with the dimensions as source image and no
			// margins:
			Image image = Image.getInstance(byteDataImage);

			float width = image.getWidth();
			float scaleWidth= (DOC_WIDTH/width)  ;
			
			float height = image.getHeight();
			float scaleHeight= (DOC_HEIGHT/height) ;
			
			float scale = scaleWidth;
			if(scaleHeight<scaleWidth) {
				scale=scaleHeight;
			}
			
			image.scaleAbsolute(width*scale, height*scale);
			
			imageDocument.newPage();
			imageDocument.add(image);
			image.setAbsolutePosition(0f, 0f);
			imageDocument.close();

			// Copy PDF document with only one page carrying the image:
			byte[] bytePdf = byteStream.toByteArray();
			byteStream.close();
			
			return bytePdf;
		}
		
	}
	
	
	public String getPageTitle() {
		String titleKey = "page.lbl.userComtracTnc.pageTitle";
		return titleKey;
	}

}
