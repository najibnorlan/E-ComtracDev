package com.ssm.llp.base.page;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

public class BasePanel extends Panel 
{

	private final String SUCCESS_MSG = "SUCCESS_MSG";
	private final String ERROR_MSG = "ERROR_MSG";
	
	@SpringBean(name = "LlpParametersService")
	public LlpParametersService parametersService;
    /**
     * Constructor
     */
    public BasePanel(String id)
    {
    	super(id);
    }
    
    /**
     * Constructor
     */
    public BasePanel()
    {
    	super(null);
    }
	
	public BaseService getService(String serviceId){
		return WicketApplication.getServiceNew(serviceId);
	}
	
	
	public List<LlpParameters> getCodeType(String codeType){
//		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		return parametersService.findByActiveCodeType(codeType);
	}
	

	public String getCodeTypeWithValue(String codeType, String value) {
//		LlpParametersService parametersService = (LlpParametersService) getService(LlpParametersService.class.getSimpleName());
		String codeValue = parametersService.findByCodeTypeValue(codeType, value);
		if(codeValue == null){
			return value;
		}else{
			return codeValue;
		}
	}
	
	public void storeSuccessMsg(String msg){
		List listSuccessMessage = (List) getSession().getAttribute(SUCCESS_MSG);
		if(listSuccessMessage==null){
			listSuccessMessage = new ArrayList();
		}
		listSuccessMessage.add(msg);
		getSession().setAttribute(SUCCESS_MSG,(Serializable) listSuccessMessage);
	}
	
	public void storeSuccessMsgKey(String key){
		String msg = resolve(key);
		storeSuccessMsg(msg);
	}
	
	
	public void storeErrorMsg(String msg){
		List listErrorMessage = (List) getSession().getAttribute(ERROR_MSG);
		if(listErrorMessage==null){
			listErrorMessage = new ArrayList();
		}
		listErrorMessage.add(msg);
		getSession().setAttribute(ERROR_MSG,(Serializable) listErrorMessage);
	}
	
	
	public String getIpAddress(){
		
    	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
    	String ipAdd = WicketUtils.getIpAddress(request, getSession());
    	
    	
    	return ipAdd;
	}
	public double getGSTRate(String gstCode) {
		return Double.valueOf(getCodeTypeWithValue(Parameter.PAYMENT_GST_CODE, gstCode));
	}

	public AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc) {
		return generateAjaxConfirm(component, confirmTitle, confirmDesc, false);
	}
	
	public AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc , boolean confirmOnlyOnUntick ) {
		return WicketUtils.generateAjaxConfirm(component, confirmTitle, confirmDesc, confirmOnlyOnUntick);
	}
	
	
	public LlpFileData validateAndGenerateFileData(FileUploadField fileUpload, RepeatingView listError) {
		return validateAndGenerateFileData(fileUpload, listError, false);
	}
	public LlpFileData validateAndGenerateFileData(FileUploadField fileUpload, RepeatingView listError, boolean isNullDontValidate) {
		String label = resolve(fileUpload.getLabelKey());
		
		if(fileUpload.getFileUpload()==null  ) {
			if(isNullDontValidate) {
				return null;
			}
			listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.noAttachment", label)));
			return null;
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
			
			if(fileExt.endsWith("pdf")) {
				try {
					ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
					PDDocument document = PDDocument.load(bais);
					document.close();
					fileType=fileUpload.getFileUpload().getContentType();
				} catch (Exception e) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.notInPDF", label)));
					formatError= true;
				}
			}else {
				try {
					if(ImageIO.read(new ByteArrayInputStream(byteData))==null) {
						throw new Exception();
					}
					fileType=fileUpload.getFileUpload().getContentType();
				} catch (Exception e) {
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.err.notInImage", label)));
					formatError= true;
				}
			}
		}
		if(!formatError) {
			LlpFileData fileData = new LlpFileData();
			fileData.setFileData(byteData);
			fileData.setFileDataType(fileType);
			return fileData;
		}
		return null;
	}
}
