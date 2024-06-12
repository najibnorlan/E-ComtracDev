package com.ssm.llp.base.page;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import com.ssm.ezbiz.service.PaymentService;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;

public class DownloadFile extends WebPage implements Serializable {
	
	@SpringBean(name = "LlpFileUploadService")
	private LlpFileUploadService llpFileUploadService;
	
	
	public DownloadFile(PageParameters param) {
		String fileId=param.get("fileId").toString();
		
		final LlpFileUpload llpFileUpload = llpFileUploadService.findByFileCode(fileId);
		if(llpFileUpload.getFileData()!=null){
//			OutputStream outputStream = null;
//			try {
//				WebResponse response = (WebResponse) getResponse();
//				response.setHeader("Content-disposition", "attachment; filename="+llpFileUpload.getFileName());
////									response.setContentType("application/vnd.ms-excel");
//				outputStream = response.getOutputStream();
//				outputStream.write(llpFileUpload.getFileData());
//				outputStream.flush();
//			} catch (Exception e) {
//			} finally {
//				if (outputStream != null) {
//					try {
//						outputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
			
			
			IResourceStream resourceStream = new AbstractResourceStreamWriter() {
				@Override
				public void write(OutputStream output) {
					try {
						output.write(llpFileUpload.getFileData());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public String getContentType() {
					return llpFileUpload.getFileType();
				}
			};

			ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream).setFileName(llpFileUpload.getFileName());
			rs.setCacheDuration(Duration.NONE);
			getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);
			
		}
	}
}
