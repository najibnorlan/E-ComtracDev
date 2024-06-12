package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormB1Panel extends RobFormBBasePanel{
	
	
	public EditRobFormB1Panel(String panelId, final RobFormB robFormB) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	RobFormB1 robFormB1 = robFormB.getRobFormB1();
            	if(robFormB1==null){
            		robFormB1 = new RobFormB1();
            		robFormB1.setRobFormBCode(robFormB.getRobFormBCode());
            		robFormB.setRobFormB1(robFormB1);
            	}
            	return robFormB1;
            }
        }));
		add(new RobFormB1Form("robFormB1Form",getDefaultModel(), robFormB));
	}
	
	private class RobFormB1Form extends Form implements Serializable {
		
		final RobFormB1 robFormB1;
		public RobFormB1Form(String id, IModel m,final RobFormB robFormB) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormB1.");
			setAutoCompleteForm(false);
			robFormB1 = (RobFormB1) m.getObject();
			
			String currentMainAddress = robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddr().toUpperCase();
			if(StringUtils.isNotBlank(robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddr2())){
				currentMainAddress+="\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddr2().toUpperCase();
			}
			if(StringUtils.isNotBlank(robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddr3())){
				currentMainAddress+="\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddr3().toUpperCase();
			}
			currentMainAddress += "\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddrPostcode()+" "+robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddrTown().toUpperCase();
			currentMainAddress += "\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormB.getBizProfileDetResp().getMainInfo().getBizMainAddrState()) ;
			MultiLineLabel currentMainAddressLabel = new MultiLineLabel("currentMainAddress", currentMainAddress);
			add(currentMainAddressLabel);
			
			String currentPostAddress = currentMainAddress;// By Default Same with main
			if(StringUtils.isNotBlank(robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr())){
				currentPostAddress = robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr().toUpperCase();
				if(StringUtils.isNotBlank(robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr2())){
					currentPostAddress+="\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr2().toUpperCase();
				}
				if(StringUtils.isNotBlank(robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr3())){
					currentPostAddress+="\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddr3().toUpperCase();
				}
				currentPostAddress += "\n"+robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddrPostcode()+" "+robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddrTown().toUpperCase();
				currentPostAddress += "\n"+getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormB.getBizProfileDetResp().getMainInfo().getBizPostAddrState()).toUpperCase() ;
			}
			
			MultiLineLabel currentPostAddressLabel = new MultiLineLabel("currentPostAddress", currentPostAddress);
			add(currentPostAddressLabel);
			
			
			
			final WebMarkupContainer wmcB1 = new WebMarkupContainer("wmcB1");
			wmcB1.setPrefixLabelKey("page.lbl.ezbiz.robFormB1.");
			wmcB1.setOutputMarkupId(true);
			wmcB1.setOutputMarkupPlaceholderTag(true);
			add(wmcB1);
			
			if(!robFormB.getIsB1()){
				wmcB1.setVisible(false);
			}
			
			SSMAjaxCheckBox isB1 = new SSMAjaxCheckBox("isB1", new PropertyModel(robFormB, "isB1") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (String.valueOf(true).equals(getValue())) {
						robFormB.setIsB1(true);
						wmcB1.setVisible(true);
					}else{
						robFormB.setIsB1(false);
						wmcB1.setVisible(false);
					}
					arg0.add(wmcB1);
				}
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
			        String confirmTitle = resolve("page.lbl.ezbiz.robFormB1.confirmResetB1Title");
			        String confirmDesc = resolve("page.lbl.ezbiz.robFormB1.confirmResetB1Desc");		
			        AjaxCallListener ajaxCallListener = generateAjaxConfirm(this, confirmTitle, confirmDesc, true);
			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			    }
			};
			add(isB1);
			isB1.setEnabled(false);
			if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
				isB1.setEnabled(true);
			}
			
			final SSMDateTextField b1AmmendmendDt = new SSMDateTextField("b1AmmendmendDt");
//			b1AmmendmendDt.setsasaset readonly on query
			wmcB1.add(b1AmmendmendDt);
			
//			final SSMLabel b1AmmendmendDtWarning = new SSMLabel("b1AmmendmendDtWarning","");
//			b1AmmendmendDtWarning.setOutputMarkupId(true);
//			b1AmmendmendDtWarning.setOutputMarkupPlaceholderTag(true);
//			b1AmmendmendDtWarning.setEscapeModelStrings(false);
//			wmcB1.add(b1AmmendmendDtWarning);
//			
//			
//			SSMAjaxFormSubmitBehavior b1AmmendmendDtOnChange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target) {
//					b1AmmendmendDtWarning.setDefaultModelObject("yohohoho");
//					target.add(b1AmmendmendDtWarning);
//				}
//			};
//			b1AmmendmendDt.add(b1AmmendmendDtOnChange);
			
			final SSMTextField bizMainAddr = new SSMTextField("bizMainAddr");
			wmcB1.add(bizMainAddr);
			
			final SSMTextField bizMainAddr2 = new SSMTextField("bizMainAddr2");
			bizMainAddr2.setNoLabel();
			wmcB1.add(bizMainAddr2);
			final SSMTextField bizMainAddr3 = new SSMTextField("bizMainAddr3");
			wmcB1.add(bizMainAddr3);
			bizMainAddr3.setNoLabel();

			
			final SSMTextField bizMainAddrPostcode= new SSMTextField("bizMainAddrPostcode");
			wmcB1.add(bizMainAddrPostcode);

			
			WicketUtils.generatePostcodeTownState(wmcB1, bizMainAddrPostcode, robFormB1, "bizMainAddrPostcode","bizMainAddrTown","bizMainAddrState" ,true );
			
			
			final SSMTextField bizMainAddrTelNo= new SSMTextField("bizMainAddrTelNo");
			wmcB1.add(bizMainAddrTelNo);
			final SSMTextField bizMainAddrMobileNo= new SSMTextField("bizMainAddrMobileNo");
			wmcB1.add(bizMainAddrMobileNo);
			final SSMTextField bizMainAddrEmail= new SSMTextField("bizMainAddrEmail");
			bizMainAddrEmail.setUpperCase(false);
			wmcB1.add(bizMainAddrEmail);
			final SSMTextField bizMainAddrUrl= new SSMTextField("bizMainAddrUrl");
			bizMainAddrUrl.setUpperCase(false);
			wmcB1.add(bizMainAddrUrl);
			
			final SSMTextField bizPostAddr= new SSMTextField("bizPostAddr");
			bizPostAddr.setOutputMarkupId(true);
			wmcB1.add(bizPostAddr);
			final SSMTextField bizPostAddr2= new SSMTextField("bizPostAddr2");
			bizPostAddr2.setOutputMarkupId(true);
			bizPostAddr2.setNoLabel();
			wmcB1.add(bizPostAddr2);
			final SSMTextField bizPostAddr3= new SSMTextField("bizPostAddr3");
			bizPostAddr3.setOutputMarkupId(true);
			bizPostAddr3.setNoLabel();
			wmcB1.add(bizPostAddr3);
			
			final SSMTextField bizPostAddrPostcode= new SSMTextField("bizPostAddrPostcode");
			bizPostAddrPostcode.setOutputMarkupId(true);
			wmcB1.add(bizPostAddrPostcode);
			
//			final SSMTextField bizPostAddrTown= new SSMTextField("bizPostAddrTown");
//			bizPostAddrTown.setOutputMarkupId(true);
//			wmcB1.add(bizPostAddrTown);
			
//			final SSMDropDownChoice bizPostAddrState = new SSMDropDownChoice("bizPostAddrState", Parameter.CBS_ROB_STATE);
//			bizPostAddrState.setOutputMarkupId(true);
//			wmcB1.add(bizPostAddrState);
			
			WicketUtils.generatePostcodeTownState(wmcB1, bizPostAddrPostcode, robFormB1, "bizPostAddrPostcode","bizPostAddrTown","bizPostAddrState"  );
			
			
			final SSMTextField bizPostAddrTelNo= new SSMTextField("bizPostAddrTelNo");
			bizPostAddrTelNo.setOutputMarkupId(true);
			wmcB1.add(bizPostAddrTelNo);
			final SSMTextField bizPostAddrMobileNo= new SSMTextField("bizPostAddrMobileNo");
			bizPostAddrMobileNo.setOutputMarkupId(true);
			wmcB1.add(bizPostAddrMobileNo);
			final SSMTextField bizPostAddrEmail= new SSMTextField("bizPostAddrEmail");
			bizPostAddrEmail.setOutputMarkupId(true);
			bizPostAddrEmail.setUpperCase(false);
			wmcB1.add(bizPostAddrEmail);
			
			SSMAjaxButton copyFromMainAddr = new SSMAjaxButton("copyFromMainAddr") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					RobFormB1 robFormB1 = (RobFormB1) form.getDefaultModelObject();
					bizPostAddr.setDefaultModelObject(bizMainAddr.getValue());
					bizPostAddr2.setDefaultModelObject(bizMainAddr2.getValue());
					bizPostAddr3.setDefaultModelObject(bizMainAddr3.getValue());
					
					
					SSMDropDownChoice bizMainAddrTownTmp = (SSMDropDownChoice) wmcB1.get("bizMainAddrTownTmp");
					SSMTextField bizMainAddrStateDesc = (SSMTextField) wmcB1.get("bizMainAddrStateDesc");
					SSMDropDownChoice bizPostAddrTownTmp = (SSMDropDownChoice) wmcB1.get("bizPostAddrTownTmp");
					SSMTextField bizPostAddrStateDesc = (SSMTextField) wmcB1.get("bizPostAddrStateDesc");
					
					bizPostAddrPostcode.setDefaultModelObject(bizMainAddrPostcode.getValue());
					bizPostAddrTownTmp.resetChild(bizMainAddrTownTmp.getListChild());
					bizPostAddrTownTmp.setDefaultModelObject(bizMainAddrTownTmp.getValue());
					bizPostAddrStateDesc.setDefaultModelObject(bizMainAddrStateDesc.getValue());
					
					bizPostAddrPostcode.setDefaultModelObject(bizMainAddrPostcode.getValue());
					bizPostAddrTelNo.setDefaultModelObject(bizMainAddrTelNo.getValue());
					bizPostAddrMobileNo.setDefaultModelObject(bizMainAddrMobileNo.getValue());
					bizPostAddrEmail.setDefaultModelObject(bizMainAddrEmail.getValue());
					
					robFormB1.setBizPostAddrTown(robFormB1.getBizMainAddrTown());
					robFormB1.setBizPostAddrState(robFormB1.getBizMainAddrState());
					
					target.add(wmcB1);
				}
			};
			copyFromMainAddr.setDefaultFormProcessing(false);
			wmcB1.add(copyFromMainAddr);
			
			final String b1ValidationJS = "b1Validation";
			String mainFieldToValidate[] = new String[]{"b1AmmendmendDt","bizMainAddr","bizMainAddrTownTmp","bizMainAddrPostcode","bizMainAddrStateDesc","bizPostAddr","bizPostAddrTownTmp","bizPostAddrStateDesc","bizPostAddrPostcode","bizMainAddrTelNo","bizMainAddrMobileNo","bizPostAddrTelNo","bizPostAddrMobileNo","bizPostAddrEmail","bizMainAddrEmail","bizMainAddrUrl"};
			String mainFieldToValidateRules[] = new String[]{"empty","empty","empty","exactLengthNumber[5]","empty","empty","empty","empty","exactLengthNumber[5]","isNotReqMinLengthNumber[10]","minLengthNumber[10]","isNotReqMinLengthNumber[10]","isNotReqMinLengthNumber[10]","isNotReqEmail","isNotReqEmail","isNotReqUrl"};
			setSemanticJSValidation(this, b1ValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			//Main Address
			SSMAjaxButton b1Next = new SSMAjaxButton("b1Next", b1ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormB1 robFormB1Form = (RobFormB1) form.getDefaultModelObject();

					if(robFormB1.compareToCurrentAddress(robFormB.getBizProfileDetResp().getMainInfo())){
						
					}
					hideAndShowSegment(target, 1);
				}
			};
			add(b1Next);
			
			setOutputMarkupId(true);
			generateDiscardButton(this, robFormB);

		}
//		private void generatePostcodeTownState2(final WebMarkupContainer container, final SSMTextField postcodeTf, Object addressObject, final String postcodeFieldName, final String townFieldName, final String stateFieldName ) {
//			try {
//				
//				
//				String stateDescFieldName=stateFieldName+"Desc";
//				String townTmpFieldName = townFieldName+"Tmp";
//				
//				final SSMTextField stateDesc = new SSMTextField( stateDescFieldName,new PropertyModel("" , ""));
//				stateDesc.setReadOnly(true);
//				container.add(stateDesc);
//				
//				List<LlpParameters> listParameters = new ArrayList<LlpParameters>();
//				
//				String townTmpFieldNameValue = "";
//				String town = BeanUtils.getProperty(addressObject, townFieldName);
//				if(StringUtils.isBlank(town)){
//					listParameters.add(new LlpParameters("", "--Fill in Postcode First--"));
//				}else{
//					listParameters = parametersService.findListTownByPostcode(BeanUtils.getProperty(addressObject, postcodeFieldName));
//					String townCode = BeanUtils.getProperty(addressObject, postcodeFieldName)+":"+BeanUtils.getProperty(addressObject, townFieldName)+":"+BeanUtils.getProperty(addressObject, stateFieldName);
//					for (int i = 0; i < listParameters.size(); i++) {
//						if(townCode.equals(listParameters.get(i).getCode())){
//							townTmpFieldNameValue = townCode;
//							stateDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, BeanUtils.getProperty(addressObject, stateFieldName)));
//							break;
//						}
//					}
//				}
//				
//				
//				final SSMDropDownChoice addrTownDD = new SSMDropDownChoice(townTmpFieldName, new PropertyModel(townTmpFieldNameValue, ""), listParameters);
//				container.add(addrTownDD);
//				
//				SSMAjaxFormSubmitBehavior postCodeOnchange = new SSMAjaxFormSubmitBehavior("onblur", true) {
//					@Override
//					protected void onSubmit(AjaxRequestTarget target) {
//						
//						try {
//							Object addressObject = (Object) getForm().getDefaultModelObject();
//							String postcode = BeanUtils.getProperty(addressObject, postcodeFieldName);
//							List listTown = parametersService.findListTownByPostcode(postcode);
//							addrTownDD.resetChild(listTown);
//							stateDesc.setDefaultModelObject("");
//							
//							BeanUtils.setProperty(addressObject, townFieldName, null);
//							BeanUtils.setProperty(addressObject, stateFieldName, null);
//							
//							target.add(container);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//					}
//				};
//				postcodeTf.add(postCodeOnchange);
//				
//				
//				SSMAjaxFormSubmitBehavior townOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
//					@Override
//					protected void onSubmit(AjaxRequestTarget target) {
//						try {
//							Object addressObject = (Object) getForm().getDefaultModelObject();
//							String postTownState = addrTownDD.getValue();
//							if(StringUtils.isNotBlank(postTownState)&&postTownState.indexOf(":")!=-1){
//								String state = StringUtils.split(postTownState,":")[2];
//								String town = StringUtils.split(postTownState,":")[1];
//								
//								BeanUtils.setProperty(addressObject, townFieldName, town);
//								BeanUtils.setProperty(addressObject, stateFieldName, state);
//								
//								stateDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, state));
//							}else{
//								BeanUtils.setProperty(addressObject, townFieldName, null);
//								BeanUtils.setProperty(addressObject, stateFieldName, null);
//								stateDesc.setDefaultModelObject("");
//							}
//							target.add(container);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//					}
//				};
//				addrTownDD.add(townOnchange);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			
//		}
		
	}

}