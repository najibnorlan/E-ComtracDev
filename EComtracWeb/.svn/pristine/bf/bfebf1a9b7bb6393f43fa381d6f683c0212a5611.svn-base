package com.ssm.ezbiz.robUserActivationTray;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings("all")
public class SSMFileUploadPanel extends BasePanel{
	
	public SSMFileUploadPanel(String panelId, String fileUploadLableKey) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new SSMFileUploadModel();
            }
        }));
		add(new SSMFileUploadPanelForm("fileUploadPanelForm", getDefaultModel(), fileUploadLableKey ) );
	}
	
	
	private class SSMFileUploadPanelForm extends Form  {
		public LlpFileData llpFileData = new LlpFileData();
		
		public SSMFileUploadPanelForm(String id, IModel m,String fileUploadLableKey ) {
			super(id, m);
			setMultiPart(true);
			setPrefixLabelKey("page.lbl.fileUpload.");
			
			
			final WebMarkupContainer wmcError = new WebMarkupContainer("wmcError");
			wmcError.setOutputMarkupId(true);
			wmcError.setOutputMarkupPlaceholderTag(true);
			wmcError.setPrefixLabelKey(getPrefixLabelKey());
			add(wmcError);
			
			final RepeatingView listError = new RepeatingView("listError");
			listError.setOutputMarkupId(true);
			listError.setVisible(false);
			wmcError.add(listError);
			
			final FileUploadField fileUpload = new FileUploadField("fileUpload" , new PropertyModel(" ", ""));
			fileUpload.setLabelKey(fileUploadLableKey);
			fileUpload.setOutputMarkupId(true);
			fileUpload.setOutputMarkupPlaceholderTag(true);
			add(fileUpload);
			
			
			final SSMLabel fileNameLabel = new SSMLabel("fileNameLabel","");
			fileNameLabel.setOutputMarkupId(true);
			fileNameLabel.setOutputMarkupPlaceholderTag(true);
			add(fileNameLabel);
			
			final SSMAjaxLink fileUploadRemoveLink = generateRemoveLink("fileUploadRemoveLink", fileUpload, fileNameLabel , llpFileData);
			add(fileUploadRemoveLink);
			
			SSMAjaxFormSubmitBehavior fileUploadBehavior = generateUploadBehavior(fileUpload, fileNameLabel, fileUploadRemoveLink, listError, llpFileData);
			fileUpload.add(fileUploadBehavior);
			
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
					
					
//					try {
//						Thread.sleep(20000);
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
	
	class SSMFileUploadModel {
		String fileName;
		byte fileUpload[];
		
	}
}
