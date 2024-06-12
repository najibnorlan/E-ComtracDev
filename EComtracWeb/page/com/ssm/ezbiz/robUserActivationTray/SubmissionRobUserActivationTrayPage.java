package com.ssm.ezbiz.robUserActivationTray;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

public class SubmissionRobUserActivationTrayPage extends SecBasePage implements Serializable{
	private static final long serialVersionUID = 1L;
	

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;
	
	
	public SubmissionRobUserActivationTrayPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				LlpUserProfile llpUserProfile = ((LlpUserEnviroment ) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
				RobUserActivationTray activationTray = new RobUserActivationTray();
				activationTray.setUserRefNo(llpUserProfile.getUserRefNo());
				activationTray.setLlpUserProfile(llpUserProfile);
				activationTray.setStatus(Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS);
				return activationTray;
			}
		}));
		add(new RobUserActivationTraySubmissionForm("form", getDefaultModel()));
		
	}

	
	public SubmissionRobUserActivationTrayPage(final RobUserActivationTray activationTray) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return activationTray;
			}
		}));
		add(new RobUserActivationTraySubmissionForm("form", getDefaultModel()));
	}


	private class RobUserActivationTraySubmissionForm extends Form implements Serializable {
		
		public final LlpFileData myKadDoc = new LlpFileData();
		public final LlpFileData selfieDoc = new LlpFileData();
		public final LlpFileData supportingDoc = new LlpFileData();
		
		
		public RobUserActivationTraySubmissionForm(String id, IModel m) {
			super(id, m);
			setAutoCompleteForm(false);
			setMultiPart(true);
			setPrefixLabelKey("page.lbl.ezbiz.robUserActivationTray.");
			
			RobUserActivationTray activationTray = (RobUserActivationTray) m.getObject();
			final LlpUserProfile llpUserProfile = activationTray.getLlpUserProfile();

			
			boolean isQuery = false;
			if(Parameter.ACTIVATION_TRAY_STATUS_QUERY.equals(activationTray.getStatus()) ) {
				 isQuery = true;
			}
			
			
			final WebMarkupContainer wmc = new WebMarkupContainer("preWmc");
			wmc.setOutputMarkupId(true);
			wmc.setOutputMarkupPlaceholderTag(true);
			wmc.setPrefixLabelKey(getPrefixLabelKey());
			add(wmc);
			
			final RepeatingView listError = new RepeatingView("listError");
			listError.setOutputMarkupId(true);
			listError.setVisible(false);
			wmc.add(listError);
			
			add(new SSMLabel("userRefNo",llpUserProfile.getUserRefNo()));
			add(new SSMLabel("loginId",llpUserProfile.getLoginId()));
			add(new SSMLabel("name",llpUserProfile.getName()));
			add(new SSMLabel("idNo", llpUserProfile.getIdNo()));
			add(new SSMLabel("idType", llpUserProfile.getIdType(), Parameter.ID_TYPE_FOR_CMP_OFFICER));
			add(new SSMLabel("icColour", llpUserProfile.getIcColour(), Parameter.NRIC_COLOUR));
			add(new SSMLabel("nationality", llpUserProfile.getNationality(), Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER));
			
			
			
			String ownerAddr = llpUserProfile.getAdd1();
			if (StringUtils.isNotBlank(llpUserProfile.getAdd2())) {
				ownerAddr +=  "\n"+ llpUserProfile.getAdd2();
			}
			if (StringUtils.isNotBlank(llpUserProfile.getAdd3())) {
				ownerAddr += "\n"+llpUserProfile.getAdd3() ;
			}

			ownerAddr += "\n" + llpUserProfile.getPostcode() + " " + llpUserProfile.getCity().toUpperCase();
			ownerAddr += "\n" + getCodeTypeWithValue(Parameter.STATE_CODE, llpUserProfile.getState());
			
			
			add(new MultiLineLabel("ownersAddress", ownerAddr));
			
			
			final FileUploadField myKadDocFileUpload = generateFileUploadField("mykadDoc", myKadDoc, listError);
			
			final FileUploadField fileUploadSelfieDoc = generateFileUploadField("selfieDoc", selfieDoc, listError);

			final FileUploadField fileUploadSupportingDoc = generateFileUploadField("supportingDoc", supportingDoc, listError);
			
			
//			SSMFileUploadPanel fileUploadPanel = new SSMFileUploadPanel("mykadDocFileUploadPanel", getPrefixLabelKey()+ "mykadDocFileUpload");
//			add(fileUploadPanel);
			
			final SSMTextArea queryAnswer = new SSMTextArea("queryAnswer", Model.of(""));
			queryAnswer.setNoLabel();
			
			final String b2ValidationJS = "formVerificationJS";
			String bizCodeFieldToValidate[] = new String[]{"dummy"};
			String bizCodeFieldToValidateRules[] = new String[]{"empty"};
			
			if(isQuery) {
				bizCodeFieldToValidate = new String[]{"queryAnswer"};
				bizCodeFieldToValidateRules = new String[]{"empty"};
			}
			
			setSemanticJSValidation(this, b2ValidationJS, bizCodeFieldToValidate, bizCodeFieldToValidateRules);
			
			SSMAjaxButton submitButton = new SSMAjaxButton("submitButton", b2ValidationJS) {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					listError.removeAll();
					RobUserActivationTray activationTray = (RobUserActivationTray) form.getModelObject();
					
					
					if(Parameter.ACTIVATION_TRAY_STATUS_QUERY.equals(activationTray.getStatus()) ) {
						
						if(listError.size()==0) {
			        		robUserActivationTrayService.reSubmit(activationTray, queryAnswer.getValue(), myKadDoc, selfieDoc, supportingDoc);
			        	}
						
					}else {
						boolean hasPendingApplication = robUserActivationTrayService.hasPendingApplication(llpUserProfile.getUserRefNo());
						
						if(hasPendingApplication) {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robUserActivationTray.duplicateApplication")));
						}else {
						
							if(myKadDoc.getFileData()==null) {
								listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.noAttachment", resolve(myKadDocFileUpload.getLabelKey()))));
							}
							
							if(selfieDoc.getFileData()==null) {
								listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.noAttachment", resolve(fileUploadSelfieDoc.getLabelKey()))));
							}
							
							if(supportingDoc.getFileData()==null) {
								listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.noAttachment", resolve(fileUploadSupportingDoc.getLabelKey()))));
							}
							
							if(listError.size()==0) {
								robUserActivationTrayService.submit(activationTray,myKadDoc,selfieDoc,supportingDoc);
				        	}
						}
					}
					
					if(listError.size()>0) {
		        		listError.setVisible(true);
		        		target.add(wmc);
		        		target.prependJavaScript("$('#" + getMarkupId() + "').prop('disabled',false);");
		        	}else {
		        		this.setEnabled(false);
		        		setResponsePage(ListRobUserActivationTrayPage.class);
		        	}

				}
				
				@Override
	            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	                super.updateAjaxAttributes(attributes);
	                attributes.getAjaxCallListeners().add(new AjaxCallListener()
	                    .onBefore("$('#" + getMarkupId() + "').prop('disabled',true);"));
	            }

			};
			add(submitButton);
			

			
			
			final WebMarkupContainer wmcQuery = new WebMarkupContainer("wmcQuery");
			wmcQuery.setPrefixLabelKey(getPrefixLabelKey());
			wmcQuery.setAutoCompleteForm(false);	
			wmcQuery.setOutputMarkupId(true);
			wmcQuery.setOutputMarkupPlaceholderTag(true);
			wmcQuery.setVisible(false);
			add(wmcQuery);		
			
			if(isQuery) {
				wmcQuery.setVisible(true);
				
//				SSMLink downlodMykadDoc = generateDownloadLink("downlodMykadDoc", activationTray.getMykadDocId(), "MyKad");
//				add(downlodMykadDoc);
//
//				SSMLink downlodSelfieDoc = generateDownloadLink("downlodSelfieDoc", activationTray.getSelfieDocId(), "SelfieDoc");
//				add(downlodSelfieDoc);
//
//				SSMLink downlodSupportingDoc = generateDownloadLink("downlodSupportingDoc", activationTray.getSupportingDocId(), "SupportingDoc");
//				add(downlodSupportingDoc);
				
				wmcQuery.add(queryAnswer);
				
				RobFormNotes formNotes = activationTray.getListRobFormNotes().get(activationTray.getListRobFormNotes().size() - 1);
				MultiLineLabel queryText = new MultiLineLabel("queryText",  formNotes.getNotes());
				wmcQuery.add(queryText);
				
				
			}
			
		}

		private FileUploadField generateFileUploadField(String fileUploadId, LlpFileData llpFileData,  final RepeatingView listError) {

			final FileUploadField fileUpload = new FileUploadField(fileUploadId+"FileUpload" , new PropertyModel(" ", ""));
			fileUpload.setOutputMarkupId(true);
			fileUpload.setOutputMarkupPlaceholderTag(true);
			add(fileUpload);
			
			
			final SSMLabel labelAfterUpload = new SSMLabel(fileUploadId+"LabelAfterUpload","");
			labelAfterUpload.setOutputMarkupId(true);
			labelAfterUpload.setOutputMarkupPlaceholderTag(true);
			add(labelAfterUpload);
			
			final SSMAjaxLink removeLink = generateRemoveLink(fileUploadId+"RemoveLink", fileUpload, labelAfterUpload , llpFileData);
			add(removeLink);
			
			
			SSMAjaxFormSubmitBehavior fileUploadBehavior = generateUploadBehavior(fileUpload, labelAfterUpload, removeLink, listError, llpFileData);
			fileUpload.add(fileUploadBehavior);
			
			return fileUpload;
			
		}

		private SSMAjaxFormSubmitBehavior generateUploadBehavior(final FileUploadField fileUpload,final SSMLabel labelAfterUpload,final SSMAjaxLink removeLink, final RepeatingView listError ,final  LlpFileData llpFileData) {
			SSMAjaxFormSubmitBehavior fileUploadBehavior = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					listError.removeAll();
					if(StringUtils.isBlank(fileUpload.getInput())) {
						return;
					}
					
					validateAndGenerateFileData(llpFileData, fileUpload, listError);
					
					if(listError.size()>0) {
						fileUpload.setDefaultModelObject("");
		        		listError.setVisible(true);
		        		target.add(listError.getParent());
		        		target.add(fileUpload);
		        	}else {
		        		listError.setVisible(false);
		        		target.add(listError.getParent());
		        		
						fileUpload.setVisible(false);
						
						removeLink.setVisible(true);
						labelAfterUpload.setVisible(true);
						labelAfterUpload.setDefaultModelObject(fileUpload.getInput());
						
						fileUpload.setDefaultModelObject("");
						
						
						target.add(fileUpload);
						target.add(removeLink);
						target.add(labelAfterUpload);
		        	}
					
					//Generate random int value from 50 to 100 
//				    int max = 5;
//				    int min = 15;
//				      
//				    int random_int = (int)(Math.random() * (max - min + 1) + min);
//				    System.out.println("Random Sleep:"+random_int);
//					try {
//						Thread.sleep(random_int*1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					
				}
			};
			return fileUploadBehavior;
		}

		private SSMAjaxLink generateRemoveLink(String linkId, final FileUploadField fileUpload, final SSMLabel labelAfterUpload, final LlpFileData fileData) {

			final SSMAjaxLink ssmAjaxLink = new SSMAjaxLink(linkId) {
				@Override
				public void onClick(AjaxRequestTarget target) {
					fileUpload.setVisible(true);
					
					
					fileData.setFileData(null);
					
					this.setVisible(false);
					labelAfterUpload.setDefaultModelObject("");
					labelAfterUpload.setVisible(false);
					
					
					target.add(fileUpload);
					target.add(this);
					target.add(labelAfterUpload);
				}
			};
			ssmAjaxLink.setOutputMarkupId(true);
			ssmAjaxLink.setOutputMarkupPlaceholderTag(true);
			ssmAjaxLink.setVisible(false);
			
			return ssmAjaxLink;
		}
		
		
		
		public void validateAndGenerateFileData(LlpFileData llpFileData, FileUploadField fileUpload, RepeatingView listError) {
			String label = resolve(fileUpload.getLabelKey());
			
			if(fileUpload.getFileUpload()==null  ) {
				llpFileData.setFileData(null);
				llpFileData.setFileDataType(null);
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.noAttachment", label)));
				return;
			}
			
			byte[] byteData = fileUpload.getFileUpload().getBytes();
			
			boolean formatError= false;
			
			String splitExt[] = StringUtils.split(fileUpload.getInput(),".");
			String fileExt = splitExt[splitExt.length-1];
			String fileType="";
			if(byteData.length>3145728){
				listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.exceedUploadSize", label)));
				formatError= true;
			}else{
				try {
					ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
					if(fileExt.endsWith("pdf")) {
						try {
							PDDocument document = PDDocument.load(bais);
							document.close();
							fileType=fileUpload.getFileUpload().getContentType();
						} catch (Exception e) {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.notInPDF", label)));
							formatError= true;
						}
					}else {
						try {
							if(ImageIO.read(bais)==null) {
								throw new Exception();
							}
							fileType=fileUpload.getFileUpload().getContentType();
						} catch (Exception e) {
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.notInImage", label)));
							formatError= true;
						}
					}
					bais.close();
				} catch (Exception e) {
					
				}
				
			}
			if(!formatError) {
				llpFileData.setFileData(byteData);
				llpFileData.setFileDataType(fileType);
			}else {
				llpFileData.setFileData(null);
				llpFileData.setFileDataType(null);
			}
			
		}
		
		
		
	}
	
	
	
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
}