package com.ssm.ezbiz.robUserActivationTray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.ServletContext;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.LazyInitializationException;
import org.springframework.web.util.UriUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class ProcessRobUserActivationTrayPage extends BasePage{
	

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;
	
	
	public ProcessRobUserActivationTrayPage(final RobUserActivationTray activationTray) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return activationTray;
			}
		}));
		add(new ProcessForm("form", getDefaultModel()));
	}

	
	public ProcessRobUserActivationTrayPage(final RobUserActivationTray activationTray, Page page) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return activationTray;
			}
		}));
		add(new ProcessForm("form", getDefaultModel()));
		
	}


	private class ProcessForm extends Form {

		public ProcessForm(String id, IModel m) {
			super(id, m);
//			setAutoCompleteForm(false);
			setPrefixLabelKey("page.lbl.ezbiz.robUserActivationTray.");
			final RobUserActivationTray activationTray = (RobUserActivationTray) m.getObject();
			
			
			ViewRobUserActivationTrayPanel viewPanel = new ViewRobUserActivationTrayPanel("viewPanel", activationTray);
			add(viewPanel);
			
			final SSMTextArea processNote = new SSMTextArea("processNote", Model.of(""));
			add(processNote);
			
			
			final SSMTextField myKadNo = new SSMTextField("myKadNo", Model.of(""));
			add(myKadNo);

			
			final String jsValidation = "jsValidation";
			String mainFieldToValidate[] = new String[]{ "processNote","myKadNo"};
			String mainFieldToValidateRules[] = new String[]{ "empty","empty"};
			setSemanticJSValidation(this, jsValidation, mainFieldToValidate, mainFieldToValidateRules);
			

			final String jsValidationApprove = "jsValidationApprove";
			String mainFieldToValidateApprove[] = new String[]{ "myKadNo"};
			String mainFieldToValidateRulesApprove[] = new String[]{ "empty"};
			setSemanticJSValidation(this, jsValidationApprove, mainFieldToValidateApprove, mainFieldToValidateRulesApprove);
			
			
			if(getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_SHOW_IMAGE_VIEWER).equals(Parameter.YES_NO_yes)) {
				try {
					String pdfPath = generatePDFLink(activationTray);
					RedirectPage page = new RedirectPage ( pdfPath );
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
			
			
			
			
			final SSMAjaxButton approvedButton = new SSMAjaxButton("approvedButton", jsValidationApprove) {
				@Override
				protected void onSubmit(AjaxRequestTarget request, Form form) {
					
					if(!myKadNo.getInput().equals(activationTray.getLlpUserProfile().getIdNo())) {
						String jsAlert = generateAjaxErrorAlertScript(resolve("page.lbl.ezbiz.robUserActivationTray.errorMyKadValidationTitle"), resolve("page.lbl.ezbiz.robUserActivationTray.errorMyKadValidationDesc"));
						request.prependJavaScript(jsAlert);
					}else {
						String remarkStr  = "";
						if(processNote.getDefaultModelObject()!=null) {
							remarkStr = processNote.getDefaultModelObject().toString();
						}
						
						try {	
							robUserActivationTrayService.processApplication(activationTray, Parameter.ACTIVATION_TRAY_STATUS_APPROVED, remarkStr);
							robUserActivationTrayService.sendEmailAfterProcess(activationTray);
							String msj = resolve(getForm().getPrefixLabelKey()+"successApproved", activationTray.getAppRefNo(),  activationTray.getLlpUserProfile().getName() );
							storeSuccessMsg(msj);
						} catch (SSMException ex) {
							storeErrorMsg(resolve(ex.getMessage(), ex.getParam()));
						}
						
						setResponsePage(SearchRobUserActivationTrayPage.class);
					}
					
					
				};
			};
			add(approvedButton);
			
			final SSMAjaxButton queryButton = new SSMAjaxButton("queryButton", jsValidation) {
				@Override
				protected void onSubmit(AjaxRequestTarget request, Form form) {
					String remarkStr = processNote.getDefaultModelObject().toString();
					
					try {	
						robUserActivationTrayService.processApplication(activationTray, Parameter.ACTIVATION_TRAY_STATUS_QUERY, remarkStr);
						robUserActivationTrayService.sendEmailAfterProcess(activationTray);
						String msj = resolve(getForm().getPrefixLabelKey()+"successQuery", activationTray.getAppRefNo(),  activationTray.getLlpUserProfile().getName() );
						storeSuccessMsg(msj);
					} catch (SSMException ex) {
						storeErrorMsg(resolve(ex.getMessage(), ex.getParam()));
					}
					setResponsePage(SearchRobUserActivationTrayPage.class);
				};
			};
			add(queryButton);
			
			
			SSMAjaxButton rejectButton = new SSMAjaxButton("rejectButton", jsValidation) {
				@Override
				protected void onSubmit(AjaxRequestTarget request, Form form) {
					String remarkStr = processNote.getDefaultModelObject().toString();
					
					try {
						robUserActivationTrayService.processApplication(activationTray, Parameter.ACTIVATION_TRAY_STATUS_REJECT, remarkStr);
						robUserActivationTrayService.sendEmailAfterProcess(activationTray);
						String msj = resolve(getForm().getPrefixLabelKey()+"successReject", activationTray.getAppRefNo(),  activationTray.getLlpUserProfile().getName() );
						storeSuccessMsg(msj);
					} catch (SSMException ex) {
						storeErrorMsg(resolve(ex.getMessage(), ex.getParam()));
					}
					
					setResponsePage(SearchRobUserActivationTrayPage.class);
				};
				
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {

					super.updateAjaxAttributes( attributes );
					String alertTitle = WicketApplication.resolve(getForm().getPrefixLabelKey()+"confirmTitleReject");
					String alertDesc = WicketApplication.resolve(getForm().getPrefixLabelKey()+"confirmDescReject");
			        
					AjaxCallListener ajaxCallListener = WicketUtils.generateAjaxConfirm(this, alertTitle, alertDesc);
					attributes.getAjaxCallListeners().add( ajaxCallListener );
			       
			    }
			};
			add(rejectButton);
			
			
			SSMAjaxButton fromPageButton = new SSMAjaxButton("fromPageButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					setResponsePage(SearchRobUserActivationTrayPage.class);
				}
				
			};
			
			add(fromPageButton);
		}

		private String generatePDFLink(RobUserActivationTray activationTray) throws Exception{
			
			
			long fileIdHash = activationTray.getMykadDocId().getFileDataId();
			fileIdHash += activationTray.getSelfieDocId().getFileDataId();
			fileIdHash += activationTray.getSupportingDocId().getFileDataId();
			
			
			
			ServletContext servletContext = WebApplication.get().getServletContext();
			
			
			

			String pdfFileFileName = activationTray.getAppRefNo()+"-"+fileIdHash+".pdf";
			
			String pdfRealPath = servletContext.getRealPath("/");
			if(pdfRealPath.endsWith(File.separator)) {
				pdfRealPath+="tmpPdf"+File.separator +pdfFileFileName;
			}else {
				pdfRealPath+=File.separator+"tmpPdf"+File.separator+pdfFileFileName;
			}
			
			
			
//			System.out.println("RealPath:"+pdfRealPath);
			
			String pdfDocSrc = getContextPath()+"/ViewerJS/?";
			pdfDocSrc+=	"entityName="+UriUtils.encodePath(activationTray.getAppRefNo(), "UTF-8")+"&";	
			pdfDocSrc+=	"canPrint=N"+"&";
			pdfDocSrc+="#../tmpPdf/"+pdfFileFileName;
			
			File filePdfTmp = new File(pdfRealPath);
			if(!filePdfTmp.exists()) {
			
				LlpFileData mykadFileData = null;
				try {
					fileIdHash = activationTray.getMykadDocId().getFileDataId();
					activationTray.getMykadDocId().getFileData();
				} catch (LazyInitializationException e) {
					mykadFileData = llpFileDataService.findById(activationTray.getMykadDocId().getFileDataId());
				}
				
				LlpFileData selfieFileData = null;
				try {
					fileIdHash += activationTray.getSelfieDocId().getFileDataId();
					activationTray.getSelfieDocId().getFileData();
				} catch (LazyInitializationException e) {
					selfieFileData = llpFileDataService.findById(activationTray.getSelfieDocId().getFileDataId());
				}
				
				LlpFileData suppDocFileData = null;
				try {
					fileIdHash += activationTray.getSupportingDocId().getFileDataId();
					activationTray.getSupportingDocId().getFileData();
				} catch (LazyInitializationException e) {
					suppDocFileData = llpFileDataService.findById(activationTray.getSupportingDocId().getFileDataId());
				}
				
	
				Document mergedDocument = new Document(PageSize.A4, 0, 0, 0, 0);
				PdfSmartCopy pdfMerge = new PdfSmartCopy(mergedDocument, new FileOutputStream(filePdfTmp));
				
				mergedDocument.open();
				
				
				mergeFileData(mykadFileData, pdfMerge);
				mergeFileData(selfieFileData, pdfMerge);
				mergeFileData(suppDocFileData, pdfMerge);
				
				mergedDocument.close();
			
			}
			return pdfDocSrc;
		}
		
		public void mergeFileData(LlpFileData fileData,PdfSmartCopy pdfMerge) throws Exception{
			
			if(fileData.getFileDataType().toLowerCase().indexOf("pdf")!=-1) {
				PdfReader reader = new PdfReader(fileData.getFileData());
				pdfMerge.addDocument(reader);
				reader.close();
			}else {
				PdfReader reader = new PdfReader(generatePDFByteFromImage(fileData.getFileData()));
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
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
}