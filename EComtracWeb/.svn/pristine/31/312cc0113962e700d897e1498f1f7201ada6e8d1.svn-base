package com.ssm.llp.base.page;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;


@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class LLPFileAttachmentPage extends SecBasePage {

	@SpringBean(name="LlpFileUploadService")
	private LlpFileUploadService llpFileUploadService;
	
	public LLPFileAttachmentPage() {
		add(new FileAttachmentForm("fileAttachmentForm"));
	}
	
	private class FileAttachmentForm extends Form {

		private String fileCode;
		private String fileName;
		private WebMarkupContainer wmc;
		private SSMDataView dataView;
		private SSMSortableDataProvider dp;
		
		public FileAttachmentForm(String name) {
			super(name);
			populateTable();
			SSMTextField tf = new SSMTextField("fileCode", new PropertyModel(this, "fileCode"));
			tf.setLabelKey("llpFileAttach.page.fileCode");
			add(tf);
			
			SSMTextField msgSearchTf = new SSMTextField("fileName", new PropertyModel(this, "fileName"));
			msgSearchTf.setLabelKey("llpFileAttach.page.fileName");
			add(msgSearchTf);
			
			add(new BookmarkablePageLink("addFileAttachment", EditLLPFileAttachmentPage.class));
			
			AjaxButton searchButton = new AjaxButton("ajaxSubmitSearch") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(getFileCode())){
						sc = SearchCriteria.andIfNotNull(sc, "fileCode", SearchCriteria.LIKE, getFileCode()+"%");
					}
					if(StringUtils.isNotBlank(getFileName())){
						sc = SearchCriteria.andIfNotNull(sc, "fileName", SearchCriteria.LIKE, "%"+getFileName()+"%");
					}
					dp.setSc(sc);
					target.add(wmc);
					FeedbackPanel feedbackPanel =  ((LLPFileAttachmentPage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
				}

			};
			add(searchButton);
		}

		public String getFileCode() {
			return fileCode;
		}

		public void setFileCode(String fileCode) {
			this.fileCode = fileCode;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		private void populateTable() {
			SearchCriteria sc = null;
			
			dp = new SSMSortableDataProvider("fileCode", sc, LlpFileUploadService.class);
			dataView = new SSMDataView<LlpFileUpload>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpFileUpload> item) {
					LlpFileUpload llpFileUpload = item.getModelObject();
					
					item.add(new SSMLabel("fileCode", llpFileUpload.getFileCode()));
					item.add(new SSMLabel("fileName", llpFileUpload.getFileName()));
					item.add(new SSMLabel("fileDesc", llpFileUpload.getFileDesc()));
					item.add(new SSMLabel("fileStatus", llpFileUpload.getFileStatus(), Parameter.FILE_STATUS));
					
					item.add(new SSMLink("edit", item.getDefaultModel()) {
						public void onClick() {
							LlpFileUpload llpFileUpload = item.getModelObject();
							setResponsePage(new EditLLPFileAttachmentPage(llpFileUpload.getFileUploadId()));
						}
					});
//					item.add(new SSMLink("delete", item.getDefaultModel(), true) {
//						public void onClick() {
//							LlpFileUpload llpFileUpload = item.getModelObject();
//							llpFileUploadService.softDelete(llpFileUpload);
//							setResponsePage(ListContacts.class);
//						}
//					});
//					
					item.add(new SSMLink("download", item.getDefaultModel()) {
						public void onClick() {
							LlpFileUpload llpFileUpload = item.getModelObject();
							
							if(llpFileUpload.getFileData()!=null){
								OutputStream outputStream = null;
								try {
									WebResponse response = (WebResponse) getResponse();
									response.setHeader("Content-disposition", "attachment; filename="+llpFileUpload.getFileName());
		//									response.setContentType("application/vnd.ms-excel");
									outputStream = response.getOutputStream();
									outputStream.write(llpFileUpload.getFileData());
									outputStream.flush();
								} catch (Exception e) {
									// TODO: handle exception
								} finally {
									if (outputStream != null) {
										try {
											outputStream.close();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
							}
						}
					});
					
					

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};
			dataView.setItemsPerPage(20L);

			dataView.setOutputMarkupId(true);

			SSMPagingNavigator navigator = new SSMPagingNavigator("navigator", dataView);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);
//			wmc.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

			add(wmc);
		}
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.file.attachment";
	}
}
