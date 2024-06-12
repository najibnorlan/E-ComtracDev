package com.ssm.llp.base.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.service.ContactService;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class EditLLPFileAttachmentPage extends SecBasePage{
	
	@SpringBean(name="LlpFileUploadService")
	private LlpFileUploadService llpFileUploadService;
	
	public EditLLPFileAttachmentPage(final long fileUploadId) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpFileUploadService.findById(fileUploadId);
			}
		}));
		init();
	}
	
	public EditLLPFileAttachmentPage(){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpFileUpload();
			}
		}));
		init();
	}
	
	private void init() {
		add(new LlpFileUploadForm("form", getDefaultModel()));
	}
	
	private class LlpFileUploadForm extends Form {

		FileUploadField fileUpload;
		public LlpFileUploadForm(String id, IModel m) {
			super(id, m);
			LlpFileUpload llpFileUpload = (LlpFileUpload) m.getObject();
			boolean isNew = true;
			if (llpFileUpload.getFileUploadId()>0) {
				isNew = false;
			}
			// set this form to multipart mode (always needed for uploads!)
            setMultiPart(true);
			
			SSMTextField fileCode = new SSMTextField("fileCode");
			fileCode.setRequired(true);
			fileCode.setReadOnly(!isNew);
			fileCode.setLabelKey("llpFileAttach.page.fileCode");
			add(fileCode);

			SSMTextField fileName = new SSMTextField("fileName");
			fileName.setRequired(true);
			fileName.setLabelKey("llpFileAttach.page.fileName");
			fileName.setVisible(!isNew);
			fileName.setReadOnly(!isNew);
			add(fileName);

			SSMTextArea fileDesc = new SSMTextArea("fileDesc");
			fileDesc.setRequired(true);
			fileDesc.setLabelKey("llpFileAttach.page.fileDesc");
			add(fileDesc);
			
			FileUploadField fileUpload = new FileUploadField("fileUploadTmp");
			fileUpload.setRequired(isNew);
			add(fileUpload);


			Button save =new Button("save") {
				public void onSubmit() {
					LlpFileUpload fileUpload = (LlpFileUpload) getForm().getModelObject();
					

					if(fileUpload.getFileUploadTmp()!=null){
						FileUpload upload = fileUpload.getFileUploadTmp().get(0);
						fileUpload.setFileData(upload.getBytes());
						fileUpload.setFileName(upload.getClientFileName());
						fileUpload.setFileType(upload.getContentType());
					}
					if (fileUpload.getFileUploadId()==0) {
						getService(LlpFileUploadService.class.getSimpleName()).insert(fileUpload);
					} else {
						getService(LlpFileUploadService.class.getSimpleName()).update(fileUpload);
					}
					ssmSuccess("success.update");
					setResponsePage(LLPFileAttachmentPage.class);
				}
			};
			add(save);
			
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(LLPFileAttachmentPage.class);
				}
			}.setDefaultFormProcessing(false));
			
			
			SSMRadioChoice gender = new SSMRadioChoice("fileStatus", Parameter.FILE_STATUS);
			gender.setLabelKey("llpFileAttach.page.fileStatus");
			add(gender);
		}
		public FileUploadField getFileUpload() {
			return fileUpload;
		}
		public void setFileUpload(FileUploadField fileUpload) {
			this.fileUpload = fileUpload;
		}
	}

}
