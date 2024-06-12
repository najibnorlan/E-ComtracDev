package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobBusinessService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.RocIncorporationService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobBusiness;
import com.ssm.llp.ezbiz.model.RocIncorporation;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EditLlpReservedNamePage extends SecBasePage  {//implements PaymentInterface
	@SpringBean(name = "LlpReservedNameService")
	private LlpReservedNameService llpReservedNameService;
	
	@SpringBean(name="RocIncorporationService")
	private RocIncorporationService rocIncorporationService;
	
	@SpringBean(name="RobBusinessService")
	private RobBusinessService robBusinessService;
	
	
	public EditLlpReservedNamePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedName();
			}
		}));
		init(false,null);
	}

	public EditLlpReservedNamePage(final String refNo, String conversionType) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpReservedNameService.class.getSimpleName()).findById(refNo);
			}
		}));
		init(false,conversionType);
	}

	public EditLlpReservedNamePage(PageParameters params) {
		final String applyLlpName = params.get("applyLlpName")!=null?params.get("applyLlpName").toString():null;
		final String regType = params.get("regType")!=null?params.get("regType").toString():null;
		final String profBodyType = params.get("profBodyType")!=null?params.get("profBodyType").toString():null;
		final String conversionType = params.get("conversionType")!=null?params.get("conversionType").toString():null;
		final String isProceedToLlpStr = params.get("isProceedToLLP")!=null?params.get("isProceedToLLP").toString():null;
		
		String conversionTypeStr = conversionType;
		boolean isProceedToLlp = false;
		if(Parameter.YES_NO_yes.equals(isProceedToLlpStr)){
			isProceedToLlp = true;
		}

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedName();
			}
		}));


		((LlpReservedName) getDefaultModel().getObject()).setApplyLlpName(applyLlpName);
		((LlpReservedName) getDefaultModel().getObject()).setRegType(regType);
		((LlpReservedName) getDefaultModel().getObject()).setConversionType(conversionTypeStr);
		if(StringUtils.isNotBlank(profBodyType)){
			((LlpReservedName) getDefaultModel().getObject()).setProfBodyType(profBodyType);
		}

		init(isProceedToLlp,conversionTypeStr);
	}

	private void init(boolean isProceedToLlp, String conversionTypeStr) {
		add(new LlpReservedNameForm("form", getDefaultModel(), isProceedToLlp, conversionTypeStr));
		
	}

	private class LlpReservedNameForm extends Form {

		public LlpReservedNameForm(String id, IModel m, final boolean isProceedToLlp, final String conversionTypeStr ) {
			super(id, m);

			final LlpReservedName llpReservedName = (LlpReservedName) m.getObject();
			
			final boolean isOfficer = UserEnvironmentHelper.isInternalUser();
			
			boolean isConvert = false;
			if((Parameter.CONVERSION_TYPE_rob).equals(conversionTypeStr)||(Parameter.CONVERSION_TYPE_roc).equals(conversionTypeStr)){
				isConvert = true;
			}
			
			boolean isForeign = false;
			if((Parameter.LLP_REG_TYPE_foreign).equals(llpReservedName.getRegType())){
				isForeign = true;
			}
			
			boolean isNewReserve = true;
			if (StringUtils.isNotBlank(llpReservedName.getRefNo())) {
				isNewReserve = false;				
			}
			
			boolean isHasLlpNo = false;
			if (StringUtils.isNotBlank(llpReservedName.getLlpNo())) {
				isHasLlpNo = true;				
			}
			
			boolean isProfBody = false;
			if((Parameter.PROF_BODY_TYPE_ca).equals(llpReservedName.getProfBodyType())||(Parameter.PROF_BODY_TYPE_law).equals(llpReservedName.getProfBodyType())||(Parameter.PROF_BODY_TYPE_cs).equals(llpReservedName.getProfBodyType())){
				isProfBody=true;
			}
			
			SSMTextField refNo = new SSMTextField("refNo");
			if (llpReservedName.getRefNo() != null) {
				refNo.setVisible(true);
				refNo.add(new AttributeModifier("readonly", new Model("readonly")));
			} else {
				refNo.setVisible(false);
			}
			refNo.setRequired(true);
			refNo.setLabelKey("reservedName.page.refNo");
			add(refNo);
			
			SSMTextField applyLlpName = new SSMTextField("applyLlpName");
			if(!isOfficer&&!isProfBody&&isProceedToLlp){
				applyLlpName.add(new AttributeModifier("readonly", new Model("readonly")));
			}else if(!isOfficer&&!isProceedToLlp){
				applyLlpName.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			applyLlpName.add(StringValidator.maximumLength(100));
			applyLlpName.setRequired(true);		
			applyLlpName.setLabelKey("reservedName.page.applyLlpName");
			add(applyLlpName);

			SSMTextField llpNo = new SSMTextField("llpNo");
			llpNo.setVisible(false);
			if(!isOfficer&&!isNewReserve){
				llpNo.setVisible(true);
				llpNo.add(new AttributeModifier("readonly", new Model("readonly")));	
			}else if(isOfficer){
				llpNo.setVisible(true);
			}
			llpNo.setLabelKey("reservedName.page.llpNo");
			add(llpNo);

			SSMDateTextField resultDate = new SSMDateTextField("resultDate");
			resultDate.setVisible(false);
			if (!isOfficer&&llpReservedName.getRefNo()!=null){
				resultDate.add(new AttributeModifier("readonly", new Model("readonly")));
				resultDate.setVisible(true);
			}else if (isOfficer&&llpReservedName.getRefNo()!=null){
				resultDate.setVisible(true);
			}
	
			resultDate.setRequired(true);
			resultDate.setLabelKey("reservedName.page.resultDate");
			add(resultDate);

			SSMDateTextField expNameDate = new SSMDateTextField("expNameDate");
			expNameDate.setVisible(false);
			if (!isOfficer&&llpReservedName.getRefNo()!=null){
				expNameDate.add(new AttributeModifier("readonly", new Model("readonly")));
				expNameDate.setVisible(true);
			}else if (isOfficer&&llpReservedName.getRefNo()!=null){
				expNameDate.setVisible(true);
			}
			expNameDate.setRequired(true);
			expNameDate.setLabelKey("reservedName.page.expNameDate");
			add(expNameDate);

			
			SSMLabel regTypeDesc = new SSMLabel("regTypeDesc",llpReservedName.getRegType(), Parameter.LLP_REG_TYPE);
			add(regTypeDesc);
			HiddenField regType = new HiddenField("regType");
			add(regType);


			SSMTextArea clarifySingleLetter = new SSMTextArea("clarifySingleLetter");
			clarifySingleLetter.add(StringValidator.maximumLength(255));
			if(!isOfficer&&!isNewReserve){
				clarifySingleLetter.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			clarifySingleLetter.setLabelKey("reservedName.page.clarifySingleLetter");
			add(clarifySingleLetter);
			
			SSMTextArea clarifyMeaning = new SSMTextArea("clarifyMeaning");
			clarifyMeaning.add(StringValidator.maximumLength(255));
			if(!isOfficer&&!isNewReserve){
				clarifyMeaning.add(new AttributeModifier("readonly", new Model("readonly")));
				//clarifyMeaning.setVisible(false);
			}
			clarifyMeaning.setLabelKey("reservedName.page.clarifyMeaning");
			add(clarifyMeaning);
			
			SSMTextArea officerRemark = new SSMTextArea("officerRemark");
			officerRemark.setVisible(false);
			if(!isOfficer&&llpReservedName.getOfficerRemark()!=null){
				officerRemark.setVisible(true);
				officerRemark.add(new AttributeModifier("readonly", new Model("readonly")));
			}else if(isOfficer){
				officerRemark.setVisible(true);
			}
//			if (llpReservedName.getOfficerRemark() != null) {
//				officerRemark.add(new AttributeModifier("readonly", new Model("readonly")));
//				officerRemark.setVisible(true);
//			}else{
//				officerRemark.setVisible(false);
//			}	
			officerRemark.setLabelKey("reservedName.page.officerRemark");
			add(officerRemark);
			
			
			final SSMTextField conversionType = new SSMTextField("conversionType");
			conversionType.setVisible(isConvert);			
			conversionType.setRequired(isConvert);
			conversionType.setReadOnly(true);
			conversionType.setLabelKey("reservedName.page.conversionType");
			add(conversionType);
			
			
			final SSMTextField conversionName = new SSMTextField("conversionName");
			conversionName.setVisible(isConvert);		
			conversionName.setOutputMarkupId(true);
			conversionName.setRequired(isConvert);
			conversionName.setReadOnly(true);
			conversionName.setLabelKey("reservedName.page.conversionName");
			add(conversionName);
			
			
			final SSMTextField conversionNo = new SSMTextField("conversionNo");
			conversionNo.setVisible(isConvert);
			conversionNo.setRequired(isConvert);	
			conversionNo.setOutputMarkupId(true);
			conversionNo.setReadOnly(isHasLlpNo);
			conversionNo.setLabelKey("reservedName.page.conversionNo");
			add(conversionNo);
			
			AjaxEventBehavior conversionUpdate = new AjaxEventBehavior("onblur"){
				@Override
				protected void onEvent(AjaxRequestTarget target) {
					String name = null;
					if(Parameter.CONVERSION_TYPE_rob.equals(conversionTypeStr)){
						RobBusiness business = robBusinessService.findById(conversionNo.getInput());
						if(business!=null){
							name = business.getVchname();
							if(Parameter.CONVERSION_STATUS_ROB.equals(business.getChrstatus()) ){
								name = "(ALREADY CONVERT TO LLP) "+name;
							}
							
						}
					}else if(Parameter.CONVERSION_TYPE_roc.equals(conversionTypeStr)){
						RocIncorporation rocIncorporation = rocIncorporationService.findById(conversionNo.getInput());
						if(rocIncorporation!=null){
							name = rocIncorporation.getVchcompanyname();
							if(Parameter.CONVERSION_STATUS_ROC.equals(rocIncorporation.getChrstatusofcompany()) ){
								name = "(ALREADY CONVERT TO LLP) "+name;
							}
						}
					}
					if(StringUtils.isNotBlank(name)){
						conversionName.setDefaultModelObject(name);
					}else{
						conversionName.setDefaultModelObject("Invalid No");
					}
					
					target.add(conversionName);
				}
				
			};
			if(!isHasLlpNo){
				conversionNo.add(conversionUpdate);
			}
						
			

			boolean isProfType = false;
			if (StringUtils.isNotBlank(llpReservedName.getProfBodyType())) {
				isProfType = true;
			}
			
			SSMLabel profBodyCodeDesc = new SSMLabel("profBodyTypeDesc", llpReservedName.getProfBodyType() , Parameter.PROF_BODY_TYPE);
			add(profBodyCodeDesc);
			HiddenField profBodyType = new HiddenField("profBodyType");
			profBodyType.setVisible(false);
			if(isProfBody){
				profBodyType.setVisible(true);
			}
			add(profBodyType);		
			
			final SSMTextField profAuthOrg = new SSMTextField("profAuthOrg");
			if(!isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				profAuthOrg.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			//profAuthOrg.setVisible(isProfBody);
			//profAuthOrg.setVisible(true);
			profAuthOrg.setRequired(false);
			profAuthOrg.setEnabled(false);
			profAuthOrg.setOutputMarkupId(true);
			profAuthOrg.setLabelKey("reservedName.page.profAuthOrg");
			add(profAuthOrg);

			final SSMTextField profAuthLetterRefNo = new SSMTextField("profAuthLetterRefNo");
			profAuthLetterRefNo.add(StringValidator.maximumLength(255));
			if(!isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				profAuthLetterRefNo.add(new AttributeModifier("readonly", new Model("readonly")));
			}							
			profAuthLetterRefNo.setRequired(false);
			profAuthLetterRefNo.setEnabled(false);
			profAuthLetterRefNo.setOutputMarkupId(true);
			profAuthLetterRefNo.setLabelKey("reservedName.page.profAuthLetterRefNo");
			add(profAuthLetterRefNo);
			
			final SSMDateTextField profAuthLetterDate = new SSMDateTextField("profAuthLetterDate");
			if (!isOfficer&&llpReservedName.getRefNo()!=null){
				profAuthLetterDate.add(new AttributeModifier("readonly", new Model("readonly")));				
			}
			profAuthLetterDate.setRequired(false);
			profAuthLetterDate.setEnabled(false);
			profAuthLetterDate.setOutputMarkupId(true);
			profAuthLetterDate.setLabelKey("reservedName.page.profAuthLetterDate");
			add(profAuthLetterDate);

			final SSMTextField profLetterPurpose = new SSMTextField("profLetterPurpose");
			profLetterPurpose.add(StringValidator.maximumLength(255));
			if(!isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				profLetterPurpose.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			//profLetterPurpose.setVisible(isProfBody);
			profLetterPurpose.setRequired(false);
			profLetterPurpose.setEnabled(false);
			profLetterPurpose.setOutputMarkupId(true);
			profLetterPurpose.setLabelKey("reservedName.page.profLetterPurpose");
			add(profLetterPurpose);

			final SSMTextField profLetterSign = new SSMTextField("profLetterSign");
			profLetterSign.add(StringValidator.maximumLength(255));
			if(!isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				profLetterSign.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			//profLetterSign.setVisible(isProfBody);
			profLetterSign.setRequired(false);
			profLetterSign.setEnabled(false);
			profLetterSign.setOutputMarkupId(true);
			profLetterSign.setLabelKey("reservedName.page.profLetterSign");
			add(profLetterSign);

			final SSMTextArea profRemark = new SSMTextArea("profRemark");
			profRemark.add(StringValidator.maximumLength(255));
			if(!isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				profRemark.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			//profRemark.setVisible(isProfBody);
			profRemark.setEnabled(false);
			profRemark.setOutputMarkupId(true);
			profRemark.setLabelKey("reservedName.page.profRemark");
			add(profRemark);	
			
						
			AjaxCheckBox authorizationChk= new AjaxCheckBox("authorizationChk", new PropertyModel(llpReservedName, "authorizationChk")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						profAuthOrg.setEnabled(true);
						profAuthOrg.setRequired(true);
						profAuthLetterRefNo.setEnabled(true);
						profAuthLetterRefNo.setRequired(true);
						
						profAuthLetterDate.setEnabled(true);
						profAuthLetterDate.setRequired(true);		
						
						profLetterPurpose.setEnabled(true);
						profLetterPurpose.setRequired(true);
						
						profLetterSign.setEnabled(true);
						profLetterSign.setRequired(true);
						
						profRemark.setEnabled(true);			
						
						
					} else {
						profAuthOrg.setEnabled(false);
						profAuthOrg.setRequired(false);
						
						profAuthLetterRefNo.setEnabled(false);
						profAuthLetterRefNo.setRequired(false);
						
						profAuthLetterDate.setEnabled(false);
						profAuthLetterDate.setRequired(false);
						
						profLetterPurpose.setEnabled(false);
						profLetterPurpose.setRequired(false);
						
						profLetterSign.setEnabled(false);
						profLetterSign.setRequired(false);
						
						profRemark.setEnabled(false);
					}
					
					if(profAuthOrg.isVisible()){
						target.add(profAuthOrg);
						target.add(profAuthLetterRefNo);
						target.add(profAuthLetterDate);
						target.add(profLetterPurpose);
						target.add(profLetterSign);
						target.add(profRemark);
					}
				}
			};
			
			if(isProfBody){
				llpReservedName.setAuthorizationChk(true);
				profAuthOrg.setEnabled(true);
				profAuthOrg.setRequired(true);
								
				profAuthLetterRefNo.setEnabled(true);
				profAuthLetterRefNo.setRequired(true);
								
				profAuthLetterDate.setEnabled(true);
				profAuthLetterDate.setRequired(true);	
								
				profLetterPurpose.setEnabled(true);
				profLetterPurpose.setRequired(true);
								
				profLetterSign.setEnabled(true);
				profLetterSign.setRequired(true);
								
				profRemark.setEnabled(true);
								
				authorizationChk.setEnabled(false);
			}
			
			
			add(authorizationChk);
			
			if(!isOfficer&&llpReservedName.getProfAuthOrg()!=null ){
				llpReservedName.setAuthorizationChk(true);
				authorizationChk.setEnabled(false);
			}else if(!isOfficer&&llpReservedName.getProfAuthOrg()==null&&llpReservedName.getRefNo()!=null ){
				authorizationChk.setVisible(false);
			}
			
			
			SSMTextField lodgeBy = new SSMTextField("lodgeBy");			
			lodgeBy.setVisible(false);
			if(isOfficer){
			lodgeBy.setVisible(true);
			}
			lodgeBy.setLabelKey("reservedName.page.lodgeBy");
			add(lodgeBy);

			
			final Button pay = new Button("pay") {
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();

					if (StringUtils.isBlank(llpReservedName.getRefNo())) {
						llpReservedName.setStatus(Parameter.RESERVE_NAME_STATUS_pending_payment);
						
						//Reserve 1 day only first.. after payment..incresse to 30 days
						Calendar calExp = Calendar.getInstance();
						calExp.add(Calendar.HOUR, 24);
						
//						llpReservedName.setReserveWithPayment(Parameter.YES_NO_yes);
						llpReservedName.setResultDate(new Date());
						llpReservedName.setExpNameDate(calExp.getTime());
						llpReservedName.setLodgeBy(UserEnvironmentHelper.getLoginName());						
						
						llpReservedNameService.insert(llpReservedName);

					}
					
					List<LlpPaymentTransactionDetail> paymentItems = new ArrayList<LlpPaymentTransactionDetail>();

					LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
					paymentItem1.setPaymentItem(Parameter.PAYMENT_RESERVED_NAME);
					paymentItem1.setQuantity(1);
					paymentItem1.setPaymentDet(llpReservedName.getApplyLlpName());
				
					paymentItems.add(paymentItem1);

					setResponsePage(new PaymentDetailPage(llpReservedName.getRefNo(), LlpReservedNameService.class.getSimpleName(), llpReservedName, paymentItems));
				}
			};
			add(pay);
			pay.setEnabled(false);
			pay.setVisible(true);
			
			final Button paymentRecheck = new Button("paymentRecheck") {
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();

					List<LlpPaymentTransactionDetail> paymentItems = new ArrayList<LlpPaymentTransactionDetail>();

					LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
					paymentItem1.setPaymentItem(Parameter.PAYMENT_RESERVED_NAME);
					paymentItem1.setQuantity(1);
					paymentItem1.setPaymentDet(llpReservedName.getApplyLlpName());
				
					paymentItems.add(paymentItem1);

					setResponsePage(new PaymentDetailPage(llpReservedName.getRefNo(), LlpReservedNameService.class.getSimpleName(), llpReservedName, paymentItems));
				}
			};
			add(paymentRecheck);
			paymentRecheck.setVisible(false);
			if(Parameter.RESERVE_NAME_STATUS_pending_payment.equals(llpReservedName.getStatus())){
				paymentRecheck.setVisible(true);
			}
			
			
			AjaxCheckBox declareChk= new AjaxCheckBox("declareChk", new PropertyModel(llpReservedName, "declareChk")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						pay.setEnabled(true);
					} else {
						pay.setEnabled(false);
					}
					
					if(pay.isVisible())
						target.add(pay);
				}
			};
			add(declareChk);
			
			if(isProceedToLlp || isOfficer || isConvert || isForeign || !isNewReserve){
				pay.setVisible(false);
				declareChk.setVisible(false);
			}			

		
			final Button proceedToLlp = new Button("proceedToLlp") {
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();					

						llpReservedName.setStatus(Parameter.RESERVE_NAME_STATUS_approved);
						
						Calendar calExp = Calendar.getInstance();
						calExp.add(Calendar.HOUR, 24);
						
						llpReservedName.setResultDate(new Date());
						llpReservedName.setExpNameDate(calExp.getTime());
						llpReservedName.setLodgeBy(UserEnvironmentHelper.getLoginName());							
						
						llpReservedNameService.insert(llpReservedName);

					setResponsePage(new LlpRegistrationBasePage(llpReservedName.getRefNo(), null));
				}				
			};
			add(proceedToLlp);
			proceedToLlp.setVisible(false);
			if(isProceedToLlp && !isOfficer){
				proceedToLlp.setVisible(true);
			}
			
			Button generateLlpNo = new Button("generateLlpNo") {
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();	
					String llpNo="";
					try {
						if(StringUtils.isNotBlank(llpReservedName.getConversionType())){
							llpNo = llpReservedNameService.generateLLPNoForConversion(llpReservedName.getRefNo(), conversionTypeStr, llpReservedName.getRegType(), llpReservedName.getProfBodyType());
						}else{
							llpNo = llpReservedNameService.generateLLPNo(llpReservedName.getRefNo(), llpReservedName.getRegType(), llpReservedName.getProfBodyType());
						}
						
						
						if(StringUtils.isNotBlank(llpNo)){
							ssmSuccess("reservedName.page.success.genllpno", llpNo);
							setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),llpReservedName.getConversionType()));
						}
					} catch (SSMException e) {
						e.printStackTrace();
						ssmError(e);
					}
					
				}
				
				@Override
				public boolean isVisible() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();
					
//					if(Parameter.LLP_REG_TYPE_foreign.equals(llpReservedName.getRegType())){
						if(UserEnvironmentHelper.isInternalUser() && StringUtils.isNotBlank(llpReservedName.getLlpNo())){
							return false;
						}else if(UserEnvironmentHelper.isInternalUser() && StringUtils.isNotBlank(llpReservedName.getRefNo())){
							return true;
						}
//					}
					
					return false;
				}
			};
			generateLlpNo.setVisible(false);
			add(generateLlpNo);
			AttributeModifier alert=new AttributeModifier( "onclick", "return confirm('Confirm generate?');");
			generateLlpNo.add(alert);
			
			Button save = new Button("save") {//Insert new rekod utk foreign dan conversion
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();					

						llpReservedName.setStatus(Parameter.RESERVE_NAME_STATUS_approved);
						
						Calendar calExp = Calendar.getInstance();
						calExp.add(Calendar.DATE, 30);
						
						llpReservedName.setResultDate(new Date());
						llpReservedName.setExpNameDate(calExp.getTime());
						llpReservedName.setLodgeBy(UserEnvironmentHelper.getLoginName());		
						
						llpReservedNameService.insert(llpReservedName);
						PageParameters params = new PageParameters();
						params.add("applyLlpName", llpReservedName.getApplyLlpName());
						params.add("refNo", llpReservedName.getRefNo());

						//setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),llpReservedName.getConversionType()));
						setResponsePage(new ListLlpReservedNamesOfficer(params));
						//setResponsePage(ViewListLlpReservedNames.class);
				}				
			};
			add(save);
			save.setVisible(false);
			if(isOfficer && isNewReserve){
				save.setVisible(true);
			}
			
		
			Button update = new Button("update") {//viewing
				public void onSubmit() {
					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();
					llpReservedName.setUpdateBy(UserEnvironmentHelper.getLoginName());
					llpReservedNameService.update(llpReservedName);
					//setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),llpReservedName.getConversionType()));
					PageParameters params = new PageParameters();
					params.add("applyLlpName", llpReservedName.getApplyLlpName());
					params.add("refNo", llpReservedName.getRefNo());
					
//					if(isOfficer){
						setResponsePage(new ListLlpReservedNamesOfficer(params));
//					}else{
//						setResponsePage(new ViewListLlpReservedNames(params));
//					}
					
				}
			};
			add(update);
			update.setVisible(false);
			if(isOfficer && !isNewReserve){
				update.setVisible(true);
			}
//			else{
//				
//				if(!isHasLlpNo && llpReservedName.getExpNameDate()!=null && (new Date()).after(llpReservedName.getExpNameDate())){
//					update.setVisible(true);
//				}
//				
//			}
			
			
			
			final boolean officer = isOfficer;
			Button cancel = new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ViewListLlpReservedNames.class);
					
					if(officer){
						setResponsePage(AfterLoginInternal.class);						
					}
					
				}
			}.setDefaultFormProcessing(false);
			add(cancel);
			cancel.setVisible(true);
			if(isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
				cancel.setVisible(false);
			}
		
		Button back = new Button("back") {
			public void onSubmit() {
				PageParameters params = new PageParameters();
				params.add("applyLlpName", llpReservedName.getApplyLlpName());
				params.add("refNo", llpReservedName.getRefNo());
				setResponsePage(new ListLlpReservedNamesOfficer(params));
			}
		};
		add(back);
		back.setVisible(false);
		if(isOfficer&&StringUtils.isNotBlank(llpReservedName.getRefNo())){
			back.setVisible(true);
		}
		

	}
		

	}
//
//	@Override
//	public void sucessPayment(Object obj, String paymentTransId) {
//		if (obj != null) {
//			LlpReservedName llpReservedName = (LlpReservedName) obj;
//			LlpReservedNameService llpReservedNameService = (LlpReservedNameService) getService(LlpReservedNameService.class.getSimpleName());
//			llpReservedNameService.updateAfterPay(llpReservedName, paymentTransId);
//		}
//
//	}

}
