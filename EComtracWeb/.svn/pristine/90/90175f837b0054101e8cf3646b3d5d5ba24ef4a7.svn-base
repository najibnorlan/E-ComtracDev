package com.ssm.llp.base.wicket.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.mod1.model.LlpUserProfile;

@SuppressWarnings({ "unchecked", "unchecked", "serial","rawtypes" })
public class SSMDropDownChoice extends DropDownChoice{
	private boolean isReadOnly = false;
	private List<LlpParameters> listCodeType;
	private SSMDropDownChoice child;
//	public SSMDropDownChoice(String id, IModel choices) {
//		super(id, choices);
//		populateOptions(codeType);
//	}
	public SSMDropDownChoice(String id,  PropertyModel propertyModel, String codeType) {
		this(id,propertyModel,codeType,false);
	}
	
	public SSMDropDownChoice(String id,  PropertyModel propertyModel, String codeType, boolean isReq) {
		super(id);
		setModel(propertyModel);
		setNullValid(true);
		setRequired(isReq);
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		listCodeType = parametersService.findByActiveCodeType(codeType);
		populateOptionsByParentCode("");
	}
	
	public SSMDropDownChoice(String id, String codeType) {
		this(id, codeType, false);
	}
	
	public SSMDropDownChoice(String id, String codeType, String parentCode) {
		this(id, codeType, false , parentCode);
	}
	public SSMDropDownChoice(String id, String codeType, boolean isReq, String parentCode) {
		super(id);
		setRequired(isReq);
		setNullValid(true);
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		listCodeType = parametersService.findByActiveCodeType(codeType);
		populateOptionsByParentCode(parentCode);
	}
	public SSMDropDownChoice(String id, String codeType, boolean isReq) {
		super(id);
		setRequired(isReq);
		setNullValid(true);
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		listCodeType = parametersService.findByActiveCodeType(codeType);
		populateOptionsByParentCode("");
	}
	public SSMDropDownChoice(String id, List<LlpParameters> listCodeType) {
		this(id,listCodeType,false);
	}	
	public SSMDropDownChoice(String id, List<LlpParameters> listCodeType, boolean isReq) {
		super(id);
		setRequired(isReq);
		setNullValid(true);
		this.listCodeType = listCodeType;
		populateOptionsByParentCode("");
	}
	public SSMDropDownChoice(String id,  PropertyModel propertyModel, List<LlpParameters> listCodeType) {
		this(id, propertyModel, listCodeType, false);
	}	
	
	public SSMDropDownChoice(String id,  PropertyModel propertyModel, List<LlpParameters> listCodeType, boolean isReq) {
		super(id);
		setRequired(isReq);
		setModel(propertyModel);
		setNullValid(true);
		this.listCodeType = listCodeType;
		populateOptionsByParentCode("");
	}
	
	public SSMDropDownChoice(String id, String codeType,final SSMDropDownChoice child) {
		this(id, codeType, child, false);
	}	
	
	public SSMDropDownChoice(String id, String codeType,final SSMDropDownChoice child, boolean isReq) {
		super(id);
		setRequired(isReq);
		setNullValid(true);
		this.child=child;
		setOutputMarkupId(true);
		child.setOutputMarkupId(true);
		LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		listCodeType = parametersService.findByActiveCodeType(codeType);
		populateOptionsByParentCode("");
		
		
		AjaxFormComponentUpdatingBehavior ajxForm = new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				
				child.populateOptionsByParentCode(getDefaultModelObjectAsString());
				target.add(child);
				
			}
		};
		add(ajxForm);

	}
	

	public void populateOptionsByParentCode(String filterCode){
		
		List<LlpParameters> listNew = new ArrayList<LlpParameters>();
		
		for (int i = 0; i < listCodeType.size(); i++) {
			LlpParameters llpParameters = listCodeType.get(i);
			if(StringUtils.isBlank(llpParameters.getParentCode())){
				listNew.add(llpParameters);
			}else{
				if(llpParameters.getParentCode().equals(filterCode)){
					listNew.add(llpParameters);
				}
			}
		}
		
//		LlpParameters llpParametersSelectOne = new LlpParameters();
//		llpParametersSelectOne.setCode("");
//		llpParametersSelectOne.setCodeDesc("-- Please Select --");
//		listNew.add(0, llpParametersSelectOne);
		
		this.setChoices(listNew);
		IChoiceRenderer renderer = new IChoiceRenderer<Object>() {
			@Override
			public Object getDisplayValue(Object arg0) {
				if(arg0 instanceof LlpParameters){
					return ((LlpParameters)arg0).getCodeDesc();
				}
				return arg0;
			}
			@Override
			public String getIdValue(Object arg0, int arg1) {
				if(arg0 instanceof LlpParameters){
					return ((LlpParameters)arg0).getCode();
				}
				return arg0.toString();
			}
		};
		this.setChoiceRenderer(renderer);
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
//		if(child!=null){
//			String code = (String) getModelObject();
//			System.out.println(getModelValue());
//			child.populateOptionsByParentCode(code);
////			System.out.println(">>"+child.getModelObject());
//		}
	}
	
	@Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if(getFeedbackMessages().size()>0){
        	add(new AttributeAppender("class", new Model("formcomponentReq"), " "));
        }
        if(isRequired()){
        	add(new AttributeAppender("class", new Model("formcomponentReqHigh"), " "));
        }
        if(isReadOnly){
        	setNullValid(false);
        	super.add(new AttributeModifier("readonly", new Model("readonly")));
        	String value = getDefaultModelObjectAsString();
        	if(StringUtils.isNotBlank(value)){
        		String desc = "";
	        	String parentCode = "";
	        	List listNew = new ArrayList();
				for (int i = 0;i < listCodeType.size(); i++) {
					LlpParameters llpParameters = listCodeType.get(i);
					if(llpParameters.getCode().equals(value)){
						listNew.add(llpParameters);
						desc = llpParameters.getCodeDesc();
						parentCode = llpParameters.getParentCode();
						break;
					}
				}
				super.add(new AttributeModifier("style", new Model("width:"+((desc.length()*10)+20)+"px")));
				listCodeType = listNew;
				populateOptionsByParentCode(parentCode);
        	}
        }
        
		if(child!=null){
			String code = (String) getModelObject();
			child.populateOptionsByParentCode(code);
		}
    }
	public AttributeModifier add(AttributeModifier attributeModifier){
		super.add(attributeModifier);
		if("readonly".equals(attributeModifier.getAttribute())){
			isReadOnly = true;
		}
		return attributeModifier;
	}
	
	public List<LlpParameters> getListChild(){
		return listCodeType;
	}
	
	public void resetChild(List<LlpParameters> listParam){
		listCodeType = listParam;
		populateOptionsByParentCode("");
	}
	
	
	public void setReadOnly(boolean isReadOnly){
		
		
		if(isReadOnly){
			this.isReadOnly=true;	
		}else{
			this.isReadOnly = false;
		}
	}

}
