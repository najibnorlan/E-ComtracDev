package com.ssm.llp.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

public class WicketUtils implements Serializable{
	
	public static String getIpAddress(HttpServletRequest request, Session session){
		String ipAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.isBlank(ipAddress)){
        	ipAddress=request.getRemoteAddr();
        }
        
        LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
        
        String isGetIpByJS = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_GET_IP_BY_JS);
        if(Parameter.YES_NO_yes.equals(isGetIpByJS)){
            String listIpNotAllowed = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_LIST_SVR_IP_ADDR);
        	if(listIpNotAllowed!=null && listIpNotAllowed.indexOf(ipAddress)!=-1){//mean use proxy or others
        		String localIp = (String) session.getAttribute(HomePage.CLIENT_LOCAL_IP_ADDR);
        		if(StringUtils.isNotBlank(localIp)){
        			ipAddress = localIp;
        		}
        	}
        }
        
    	
        
        
        return ipAddress;
	}
	
	public static void generatePostcodeTownState(final WebMarkupContainer container, final SSMTextField postcodeTf, final Object addressObject, final String postcodeFieldName, final String townFieldName, final String stateFieldName ) {
		
		generatePostcodeTownState(container, postcodeTf, addressObject, postcodeFieldName, townFieldName, stateFieldName  , false );
	}
	
	public static void generatePostcodeTownState(final WebMarkupContainer container, final SSMTextField postcodeTf, final Object addressObject, final String postcodeFieldName, final String townFieldName, final String stateFieldName, final boolean isFilterAllowState ) {
		try {
			final LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
			
			final String stateDescFieldName=stateFieldName+"Desc";
			final String townTmpFieldName = townFieldName+"Tmp";
			
			final SSMTextField stateDesc = new SSMTextField( stateDescFieldName,new PropertyModel("" , ""));
			stateDesc.setOutputMarkupId(true);
			stateDesc.setOutputMarkupPlaceholderTag(true);
			stateDesc.setReadOnly(true);
			container.addOrReplace(stateDesc);
			
			List<LlpParameters> listParameters = new ArrayList<LlpParameters>();
			
			String townTmpFieldNameValue = "";
			String town = BeanUtils.getProperty(addressObject, townFieldName);
			if(StringUtils.isBlank(town)){
				listParameters.add(new LlpParameters("", "--Fill in Postcode First--"));
			}else{
				listParameters = parametersService.findListTownByPostcode(BeanUtils.getProperty(addressObject, postcodeFieldName),isFilterAllowState);
				String townCode = BeanUtils.getProperty(addressObject, postcodeFieldName)+":"+BeanUtils.getProperty(addressObject, townFieldName)+":"+BeanUtils.getProperty(addressObject, stateFieldName);
				for (int i = 0; i < listParameters.size(); i++) {
					if(townCode.equals(listParameters.get(i).getCode())){
						townTmpFieldNameValue = townCode;
						stateDesc.setDefaultModelObject(parametersService.findByCodeTypeValue(Parameter.CBS_ROB_STATE, BeanUtils.getProperty(addressObject, stateFieldName)));
						break;
					}
				}
			}
			
			
			final SSMDropDownChoice addrTownDD = new SSMDropDownChoice(townTmpFieldName, new PropertyModel(townTmpFieldNameValue, ""), listParameters);
			addrTownDD.setOutputMarkupId(true);
			addrTownDD.setOutputMarkupPlaceholderTag(true);
			container.addOrReplace(addrTownDD);
			
			
			OnChangeAjaxBehavior postCodeOnchange = new OnChangeAjaxBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
			
//			SSMAjaxFormSubmitBehavior postCodeOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//					@Override
//					protected void onSubmit(AjaxRequestTarget target) {	

					try {
						
//						System.out.println(addressObjectDef);
//						Object a = addressObjectDef;
//						Object addressObject = addressObjectDef;//(Object) getForm().getDefaultModelObject();
//						System.out.println("Sdasdas");
						
						String postcode = (String) getFormComponent().getDefaultModelObject();
//						Object addressObject = (Object) getFormComponent().getForm().getDefaultModelObject();
						
						
//						String postcode = Be3anUtils.getProperty(addressObject, postcodeFieldName);
						
						List<LlpParameters> listTown2 = parametersService.findListTownByPostcode(postcode, false);
						List<LlpParameters> listTown = parametersService.findListTownByPostcode(postcode, isFilterAllowState);
						
						
//						for (Iterator iterator = target.getComponents().iterator(); iterator.hasNext();) {
//							Component	 comp = (Component) iterator.next();
//							System.out.println(">>"+comp.getId());
//						}
						
//						target.
//						Object obj1 = addrTownDD;
//						SSMDropDownChoice obj2 = (SSMDropDownChoice) getFormComponent().getForm().get("addrTownTmp");
//						Object obj3 = getComponent().findParent(Form.class).get("addrTownTmp");
//						System.out.println(obj1);
//						System.out.println(addrTownDD.getMarkupId()+"<><>");
						
						
//						obj1 = new SSMDropDownChoice(townTmpFieldName, new PropertyModel("addrTownTmp", ""), listTown);
//						addrTownDD.setOutputMarkupId(true);
//						addrTownDD.setOutputMarkupPlaceholderTag(true);
//						container.addOrReplace(addrTownDD);
						
						
						addrTownDD.resetChild(listTown);
						stateDesc.setDefaultModelObject("");
						container.addOrReplace(addrTownDD);
						

//						String script = "alert('hihi');$('"+stateDesc.getMarkupId()+"').val('kambing')";
//						System.out.println("script:"+script);
//						target.prependJavaScript(script);
						
						
						BeanUtils.setProperty(addressObject, townFieldName, null);
						BeanUtils.setProperty(addressObject, stateFieldName, null);
						
						if(listTown.size()==1){
							String postTownState = listTown.get(0).getCode();
							String state = StringUtils.split(postTownState,":")[2];
							String town = StringUtils.split(postTownState,":")[1];
							BeanUtils.setProperty(addressObject, townFieldName, town);
							BeanUtils.setProperty(addressObject, stateFieldName, state);
							addrTownDD.setDefaultModelObject(postTownState);
							stateDesc.setDefaultModelObject(parametersService.findByCodeTypeValue(Parameter.CBS_ROB_STATE, state));
						}else{
							if(isFilterAllowState && listTown.size()==0 && listTown2.size()>=1){
								String alertTitle = WicketApplication.resolve("postcode.sabahSarawak.alertTitle");
								String alertDesc = WicketApplication.resolve("postcode.sabahSarawak.alertDesc");
								String jsScript = WicketUtils.generateAjaxErrorAlertScript(alertTitle, alertDesc);
								target.prependJavaScript(jsScript);
							}
						}
						target.add(addrTownDD);
						target.add(stateDesc);
//						target.add(container);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			};
			postcodeTf.add(postCodeOnchange);
						
			OnChangeAjaxBehavior townOnchange = new OnChangeAjaxBehavior() {
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
//					
//			SSMAjaxFormSubmitBehavior townOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target) {	

					try {

//						Object addressObject = (Object) getFormComponent().getForm().getDefaultModelObject();
						String postTownState = getFormComponent().getValue();
						
//						Object addressObject = (Object) getFormComponent().getForm().getDefaultModelObject();
////						Object addressObject = (Object) getForm().getDefaultModelObject();
////						Object addressObject = addressObjectDef;
//						String postTownState = addrTownDD.getValue();
						
						if(StringUtils.isNotBlank(postTownState)&&postTownState.indexOf(":")!=-1){
							String state = StringUtils.split(postTownState,":")[2];
							String town = StringUtils.split(postTownState,":")[1];
							
							BeanUtils.setProperty(addressObject, townFieldName, town);
							BeanUtils.setProperty(addressObject, stateFieldName, state);
							
							stateDesc.setDefaultModelObject(parametersService.findByCodeTypeValue(Parameter.CBS_ROB_STATE, state));
						}else{
							BeanUtils.setProperty(addressObject, townFieldName, null);
							BeanUtils.setProperty(addressObject, stateFieldName, null);
							stateDesc.setDefaultModelObject("");
						}
//						target.add(container);
						target.add(stateDesc);
						target.add(addrTownDD);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			};
			addrTownDD.add(townOnchange);
			

//			SSMAjaxFormSubmitBehavior postCodeOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target) {
//					
//					try {
////						System.out.println(addressObjectDef);
////						Object a = addressObjectDef;
//						Object addressObject = addressObjectDef;//(Object) getForm().getDefaultModelObject();
//						String postcode = BeanUtils.getProperty(addressObjectDef, postcodeFieldName);
//						
//
//						List<LlpParameters> listTown2 = parametersService.findListTownByPostcode(postcode, false);
//						List<LlpParameters> listTown = parametersService.findListTownByPostcode(postcode, isFilterAllowState);
//						
//						addrTownDD.resetChild(listTown);
//						stateDesc.setDefaultModelObject("");
//						
//						BeanUtils.setProperty(addressObject, townFieldName, null);
//						BeanUtils.setProperty(addressObject, stateFieldName, null);
//						
//						
//						if(listTown.size()==1){
//							String postTownState = listTown.get(0).getCode();
//							String state = StringUtils.split(postTownState,":")[2];
//							String town = StringUtils.split(postTownState,":")[1];
//							BeanUtils.setProperty(addressObject, townFieldName, town);
//							BeanUtils.setProperty(addressObject, stateFieldName, state);
//							addrTownDD.setDefaultModelObject(postTownState);
//							stateDesc.setDefaultModelObject(parametersService.findByCodeTypeValue(Parameter.CBS_ROB_STATE, state));
//						}else{
//							if(isFilterAllowState && listTown.size()==0 && listTown2.size()>=1){
//								String alertTitle = WicketApplication.resolve("postcode.sabahSarawak.alertTitle");
//								String alertDesc = WicketApplication.resolve("postcode.sabahSarawak.alertDesc");
//								String jsScript = WicketUtils.generateAjaxErrorAlertScript(alertTitle, alertDesc);
//								target.prependJavaScript(jsScript);
//							}
//						}
////						target.add(addrTownDD);
////						target.add(stateDesc);
//						target.add(container);
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}
//			};
//			postcodeTf.add(postCodeOnchange);

			
//			SSMAjaxFormSubmitBehavior townOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target) {
//					try {
////						Object addressObject = (Object) getForm().getDefaultModelObject();
//						Object addressObject = addressObjectDef;
//						String postTownState = addrTownDD.getValue();
//						if(StringUtils.isNotBlank(postTownState)&&postTownState.indexOf(":")!=-1){
//							String state = StringUtils.split(postTownState,":")[2];
//							String town = StringUtils.split(postTownState,":")[1];
//							
//							BeanUtils.setProperty(addressObject, townFieldName, town);
//							BeanUtils.setProperty(addressObject, stateFieldName, state);
//							
//							stateDesc.setDefaultModelObject(parametersService.findByCodeTypeValue(Parameter.CBS_ROB_STATE, state));
//						}else{
//							BeanUtils.setProperty(addressObject, townFieldName, null);
//							BeanUtils.setProperty(addressObject, stateFieldName, null);
//							stateDesc.setDefaultModelObject("");
//						}
//						target.add(container);
////						target.add(stateDesc);
////						target.add(addrTownDD);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}
//				
//				@Override
//				protected void onError(AjaxRequestTarget target) {
//					super.onError(target);
//					System.out.println("ERROR");
//				}
//				
//				@Override
//				protected void onEvent(AjaxRequestTarget target) {
//					// TODO Auto-generated method stub
//					super.onEvent(target);
//					System.out.println("EVENT");
//				}
//			};
//			
//			addrTownDD.add(townOnchange);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc) {
		return generateAjaxConfirm(component, confirmTitle, confirmDesc, false);
	}
	public static AjaxCallListener generateAjaxConfirm(Component component, String confirmTitle, String confirmDesc, boolean confirmOnlyOnUntick) {
		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		String str = "";
        str +=" var isCheck = document.getElementById('"+component.getMarkupId()+"').checked; ";
        
        if(component instanceof AjaxCheckBox && confirmOnlyOnUntick){
        str +=" if(!isCheck){ ";
        }
        str +=" 	var isOk = (jQuery.data( "+component.getMarkupId()+", 'isOk' ) == 'OK'); ";
        str +=" 	var confirmTitle = '"+confirmTitle+"'; ";
        str +=" 	var confirmDesc = '"+confirmDesc+"'; ";
        str +="		jQuery.removeData( "+component.getMarkupId()+", 'isOk'); ";
        str +=" 	if(!isOk){ ";	
        
        if(component instanceof AjaxCheckBox && confirmOnlyOnUntick){
        str +="				document.getElementById('"+component.getMarkupId()+"').checked=true;";
        }
        str +="				alertify.confirm(confirmTitle, confirmDesc, ";
		str +="						function(){ ";
		str +="							jQuery.data( "+component.getMarkupId()+", 'isOk', 'OK' ); ";
		str +=" 						$('#" + component.getMarkupId() + "').trigger('click'); ";
		str +=" 						alertify.success('Ok'); ";
		str +="						},";
		str +="						function(){ ";
		str +=" 						alertify.error('Cancel'); ";
		if(component instanceof AjaxCheckBox && confirmOnlyOnUntick){
		str +="							document.getElementById('"+component.getMarkupId()+"').checked=true;";
		}
		str +="						}";
		str +="				); ";
        str +=" 	} else { ";	
        str +=" 		return true; ";	
        str +=" 	} ";
        
        if(component instanceof AjaxCheckBox && confirmOnlyOnUntick){
        str +=" } else {";
        str +=" 	return true; ";			
        str +=" } ";
        }
        
        str +=" return false;";
        ajaxCallListener.onPrecondition( str );
        return ajaxCallListener;
	}
	
	public static AjaxCallListener generateAjaxErrorAlert(String alertTitle, String alertDesc) {
		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		
		String str = "alertify.alert('"+alertTitle+"', '"+alertDesc+"!', function(){ alertify.error('"+alertDesc+"'); });";
        str +=" return false;";
        ajaxCallListener.onPrecondition( str );
        return ajaxCallListener;
	}
	
	public static String generateAjaxErrorAlertScript(String alertTitle, String alertDesc) {
		String str = "alertify.alert('"+alertTitle+"', '"+alertDesc+"!', function(){ alertify.error('"+alertDesc+"'); });";
        return str;
	}
	
	public static String generateAjaxSuccessAlertScript(String alertTitle, String alertDesc ) {
		return generateAjaxSuccessAlertScript(alertTitle,alertDesc,null);
	}
	public static String generateAjaxSuccessAlertScript(String alertTitle, String alertDesc , String addScriptAfterClose) {
		
		if(StringUtils.isNotBlank(alertTitle)) {
			alertTitle = StringEscapeUtils.escapeJavaScript(alertTitle);
		}
		if(StringUtils.isNotBlank(alertDesc)) {
			alertDesc =  StringEscapeUtils.escapeJavaScript(alertDesc);
		}
		
		if(StringUtils.isNotBlank(addScriptAfterClose)) {
			return  "alertify.alert('"+alertTitle+"', '"+alertDesc+"!', function(){ alertify.success('Ok'); }).set({onshow:null, onclose:function(){ "+addScriptAfterClose+" }});";
		}
		
        return "alertify.alert('"+alertTitle+"', '"+alertDesc+"', function(){ alertify.success('Ok'); });";
	}
	
	
	public static List getSSMSvrResponse(String hexResponseMsg) throws Exception{
		
		boolean isNormalStr = false;
		if(hexResponseMsg.indexOf("<END2>")!=-1) {
			isNormalStr = true;
			hexResponseMsg = StringUtils.remove(hexResponseMsg, "<END2>");
		}else {
			hexResponseMsg = StringUtils.remove(hexResponseMsg, "<END>");
		}
		
		
		List listResponse = new ArrayList();
		
		if(isNormalStr) {
			byte byteResponse[] = Hex.decodeHex(hexResponseMsg.toCharArray());
			String responseStr[] = new String(byteResponse).split(":");
			listResponse.add(responseStr[0]);
			listResponse.add(responseStr[1]);
		}else {
		
			byte byteResponse[] = Hex.decodeHex(hexResponseMsg.toCharArray());
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteResponse));
			
			while(1==1) {
				try {
					listResponse.add(ois.readObject());
				} catch (Exception e) {
					break;
				}
			}
			ois.close();
		}
		
		return listResponse;
	}
	
}
