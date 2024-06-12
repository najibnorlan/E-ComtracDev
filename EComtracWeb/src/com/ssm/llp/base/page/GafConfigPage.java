package com.ssm.llp.base.page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;

@SuppressWarnings({"all"})
public class GafConfigPage extends SecBasePage{
	

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	public GafConfigPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpFileUpload();
			}
		}));
		GafConfigPageForm form = new GafConfigPageForm("form", getDefaultModel());
		add(form);
		
	}
	
	
//	@Override
//	public String getAjaxIndicatorMarkupId() {
//		return null;
//	}
	
	private class GafConfigPageForm extends Form {

		FileUploadField fileUpload;
		public GafConfigPageForm(String id, IModel m) {
			super(id, m);
			LlpFileUpload llpFileUpload = (LlpFileUpload) m.getObject();

			// set this form to multipart mode (always needed for uploads!)
            setMultiPart(true);
			
			FileUploadField fileUpload = new FileUploadField("fileUploadTmp");
			add(fileUpload);
			
			final Component feedback = new FeedbackPanel("feedbackPanel").setOutputMarkupId(true);
	        add(feedback);
	        
			add(new UploadProgressBar("progress", this, fileUpload));
			
			SSMDownloadLink download = new SSMDownloadLink("download");
	        try {
				byte[] gafSetupByte = llpPaymentTransactionService.generateExcelGAFSetup();
				download.setDownloadData("GAF_SETUP.xls", "application/vnd.ms-excel", gafSetupByte);
			} catch (Exception e) {
				ssmError(new SSMException(e));
			}
	        add(download);

			Button save =new Button("save") {
				public void onSubmit() {
					LlpFileUpload fileUpload = (LlpFileUpload) getForm().getModelObject();
					if(fileUpload.getFileUploadTmp()!=null){
						FileUpload upload = fileUpload.getFileUploadTmp().get(0);
						try {
							llpPaymentTransactionService.updateExcelGAFSetup(upload.getBytes());
							ssmSuccess("success.update");
						} catch (Exception e) {
							ssmError(new SSMException(e));
						}
					}
					setResponsePage(GafConfigPage.class);
				}
			};
//			add(save);
			
			SSMAjaxButton uploadDoc = new SSMAjaxButton("save") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					LlpFileUpload fileUpload = (LlpFileUpload) getForm().getModelObject();
					if(fileUpload.getFileUploadTmp()!=null){
						FileUpload upload = fileUpload.getFileUploadTmp().get(0);
						try {
							llpPaymentTransactionService.updateExcelGAFSetup(upload.getBytes());
							ssmSuccess("success.update");
						} catch (Exception e) {
							ssmError(new SSMException(e));
						}
						target.add(feedback);
					}					
				}
			};
			add(uploadDoc);
			
		}
		public FileUploadField getFileUpload() {
			return fileUpload;
		}
		public void setFileUpload(FileUploadField fileUpload) {
			this.fileUpload = fileUpload;
		}
	}
}
