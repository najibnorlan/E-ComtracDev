package com.ssm.llp.base.wicket;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.page.WicketApplication;

public class SSMDownloadLink extends Link{

	public static final String TYPE_PDF = "application/pdf";
	public static final String TYPE_EXCEL = "application/vnd.ms-excel";
	public static final String TYPE_TXT = "application/txt";
	
	private byte[] byteData;
	private String fileName;
	private String contentType;
	private String fileAttachCode;
	public SSMDownloadLink(String id,byte[] byteData, String fileName) {
		super(id);
		this.byteData=byteData;
		this.fileName=fileName;
	}
	
	public SSMDownloadLink(String id, byte[] byteData, String fileName, String contentType) {
		super(id);
		this.byteData=byteData;
		this.fileName=fileName;
		this.contentType=contentType;
	}
	
	public SSMDownloadLink(String id) {
		super(id);
	}
	
	public void setDownloadData(String fileName, String contentType, byte[] byteData){
		this.byteData = byteData;
		this.contentType= contentType;
		this.fileName = fileName;
	}
	public SSMDownloadLink(String id, String fileAttachCode) {
		super(id);
		this.fileAttachCode = fileAttachCode;
		LlpFileUploadService llpFileUploadService = (LlpFileUploadService) WicketApplication.getServiceNew(LlpFileUploadService.class.getSimpleName());
		LlpFileUpload llpFileUpload = llpFileUploadService.findByFileCode(fileAttachCode);
		if(llpFileUpload!=null){
			this.fileName=llpFileUpload.getFileName();
			this.contentType=llpFileUpload.getFileType();
		}
	}

	public SSMDownloadLink() {
		super(""+System.currentTimeMillis());
	}

	@Override
	public void onClick() {
		IResourceStream resourceStream = new AbstractResourceStreamWriter() {
		      @Override 
		      public void write(OutputStream output) {
		    	  try {
		    		  if(StringUtils.isNotBlank(fileAttachCode)){
		    			  LlpFileUploadService llpFileUploadService = (LlpFileUploadService) WicketApplication.getServiceNew(LlpFileUploadService.class.getSimpleName());
			    		  LlpFileUpload llpFileUpload = llpFileUploadService.findByFileCode(fileAttachCode);
			    	      byteData=llpFileUpload.getFileData();
		    		  }
		    		
					output.write(byteData);
				} catch (IOException e) {
					e.printStackTrace();
				}
		      }

		      @Override
		      public String getContentType() {                        
		        return contentType;
		      }
		};
		
		
		ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream).setFileName(fileName);
		rs.setCacheDuration(Duration.NONE);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);

	}
	
	public boolean isHasFile(){
		if(StringUtils.isNotBlank(fileName)){
			return true;
		}
		return false;
	}

	public byte[] getByteData() {
		return byteData;
	}


}
