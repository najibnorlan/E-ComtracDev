package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.ParamLocale;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditLlpRegistrationPanel extends SecBasePanel {
	

	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	@SpringBean(name = "LlpReservedNameService")
	private LlpReservedNameService llpReservedNameService;
	
	final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");

	public EditLlpRegistrationPanel(String panelId) {
		super(panelId);

		//final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return llpRegistration;
			}
		}));
		init();
	}

	private void init() {
		add(new LlpRegistrationForm("llpRegForm", getDefaultModel()));
	}
	public static int test = 0;
	private class LlpRegistrationForm extends Form {
		
		public LlpRegistrationForm(String id, IModel model) {
			super(id, model);
			
			boolean isOfficer = UserEnvironmentHelper.isInternalUser();
			boolean isNewReg = true;
			boolean isDisableEdit = false; //can edit
			
			
			if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
				isNewReg = false;
			}
			if(!isNewReg && !isOfficer){ //if non ssm and already obtain llp No.
				isDisableEdit = true;
			}

//			SSMTextField llpNo = new SSMTextField("llpNo");
//			llpNo.add(new AttributeModifier("readonly", new Model("readonly")));
//			add(llpNo);


			SSMLabel regDate = new SSMLabel("regDate",llpRegistration.getRegDate());
			add(regDate);
			regDate.setVisibilityAllowed(!isNewReg);

//			SSMTextField llpName = new SSMTextField("llpName");
//			llpName.add(new AttributeModifier("readonly", new Model("readonly")));
//			llpName.setRequired(true);
//			llpName.add(StringValidator.maximumLength(100));
//			add(llpName);

			SSMTextField regAdd1 = new SSMTextField("regAdd1");
			regAdd1.setRequired(true);
			regAdd1.add(StringValidator.maximumLength(150));
			regAdd1.setReadOnly(isDisableEdit);
			regAdd1.setLabelKey("llpReg.page.regAddress");
			add(regAdd1);

			SSMTextField regAdd2 = new SSMTextField("regAdd2");
			regAdd2.add(StringValidator.maximumLength(150));
			regAdd2.setReadOnly(isDisableEdit);
			add(regAdd2);

			SSMTextField regAdd3 = new SSMTextField("regAdd3");
			regAdd3.add(StringValidator.maximumLength(150));
			regAdd3.setReadOnly(isDisableEdit);
			add(regAdd3);

			SSMDropDownChoice regState = new SSMDropDownChoice("regState", Parameter.STATE_CODE);
			regState.setReadOnly(isDisableEdit);
			regState.setLabelKey("llpReg.page.regState");
			add(regState);

			SSMDropDownChoice regCountry = new SSMDropDownChoice("regCountry", Parameter.COUNTRY_CODE, regState);
			regCountry.setRequired(true);
			regCountry.setReadOnly(true); //must always Malaysia
			regCountry.setLabelKey("llpReg.page.regCountry");
			add(regCountry);


			SSMTextField regCity = new SSMTextField("regCity");
			regCity.setRequired(true);
			regCity.add(StringValidator.maximumLength(150));
			regCity.setReadOnly(isDisableEdit);
			regCity.setLabelKey("llpReg.page.regCity");
			add(regCity);

			SSMTextField regPostcode = new SSMTextField("regPostcode");
			regPostcode.setRequired(true);
			regPostcode.add(StringValidator.maximumLength(10));
			regPostcode.add(new NumberValidator());
			regPostcode.setReadOnly(isDisableEdit);
			regPostcode.setLabelKey("llpReg.page.regPostcode");
			add(regPostcode);

			SSMTextField bussinessAdd1 = new SSMTextField("bussinessAdd1");
			bussinessAdd1.setRequired(true);
			bussinessAdd1.add(StringValidator.maximumLength(150));
			bussinessAdd1.setReadOnly(isDisableEdit);
			bussinessAdd1.setLabelKey("llpReg.page.bizAddress");
			add(bussinessAdd1);

			SSMTextField bussinessAdd2 = new SSMTextField("bussinessAdd2");
			bussinessAdd2.add(StringValidator.maximumLength(150));
			bussinessAdd2.setReadOnly(isDisableEdit);
			add(bussinessAdd2);

			SSMTextField bussinessAdd3 = new SSMTextField("bussinessAdd3");
			bussinessAdd3.add(StringValidator.maximumLength(150));
			bussinessAdd3.setReadOnly(isDisableEdit);
			add(bussinessAdd3);

			SSMDropDownChoice bussinessStateCode = new SSMDropDownChoice("bussinessStateCode", Parameter.STATE_CODE);
			bussinessStateCode.setReadOnly(isDisableEdit);
			bussinessStateCode.setLabelKey("llpReg.page.bizState");
			add(bussinessStateCode);

			SSMDropDownChoice bussinessCountryCode = new SSMDropDownChoice("bussinessCountryCode", Parameter.COUNTRY_CODE, bussinessStateCode);
			bussinessCountryCode.setRequired(true);
			bussinessCountryCode.setReadOnly(true); //must always Malaysia
			bussinessCountryCode.setLabelKey("llpReg.page.bizCountry");
			add(bussinessCountryCode);


			SSMTextField bussinessCity = new SSMTextField("bussinessCity");
			bussinessCity.setRequired(true);
			bussinessCity.add(StringValidator.maximumLength(150));
			bussinessCity.setReadOnly(isDisableEdit);
			bussinessCity.setLabelKey("llpReg.page.bizCity");
			add(bussinessCity);

			SSMTextField bussinessPostcode = new SSMTextField("bussinessPostcode");
			bussinessPostcode.setRequired(true);
			bussinessPostcode.add(StringValidator.maximumLength(10));
			bussinessPostcode.add(new NumberValidator());
			bussinessPostcode.setReadOnly(isDisableEdit);
			bussinessPostcode.setLabelKey("llpReg.page.bizPostcode");
			add(bussinessPostcode);

			SSMTextArea natureOfBusinessDesc = new SSMTextArea("natureOfBusinessDesc");
			natureOfBusinessDesc.setRequired(true);
			natureOfBusinessDesc.add(StringValidator.maximumLength(500));
			natureOfBusinessDesc.setReadOnly(isDisableEdit);
			natureOfBusinessDesc.setLabelKey("llpReg.page.natureOfBiz");
			add(natureOfBusinessDesc);

//			SSMDropDownChoice llpStatus = new SSMDropDownChoice("llpStatus", Parameter.LLP_STATUS);
//			llpStatus.add(new AttributeModifier("readonly", new Model("readonly")));
//			add(llpStatus);


			SSMTextField offTelNo = new SSMTextField("offTelNo");
			offTelNo.add(StringValidator.maximumLength(15));
			offTelNo.add(new NumberValidator());
			offTelNo.setReadOnly(isDisableEdit);
			offTelNo.setLabelKey("llpReg.page.regOfficeTel");
			add(offTelNo);

			SSMTextField hpNo = new SSMTextField("hpNo");
			hpNo.setRequired(true);
			hpNo.add(StringValidator.maximumLength(15));
			hpNo.add(new NumberValidator());
			hpNo.setReadOnly(isDisableEdit);
			hpNo.setLabelKey("llpReg.page.regHp");
			add(hpNo);

			SSMTextField faxNo = new SSMTextField("faxNo");
			faxNo.add(StringValidator.maximumLength(15));
			faxNo.add(new NumberValidator());
			faxNo.setReadOnly(isDisableEdit);
			faxNo.setLabelKey("llpReg.page.regFax");
			add(faxNo);


			final SSMRadioChoice agreementLlp = new SSMRadioChoice("agreementLlp", Parameter.YES_NO);
			agreementLlp.setRequired(true);
			agreementLlp.setReadOnly(isDisableEdit);
			agreementLlp.setLabelKey("llpReg.page.agreement");
			add(agreementLlp);
			
			final SSMDateTextField dateOfAgreement = new SSMDateTextField("agreementLlpDate");
			dateOfAgreement.setReadOnly(isDisableEdit);
			dateOfAgreement.setLabelKey("llpReg.page.agreementDate");
			

			final SSMTextField noOfPartner = new SSMTextField("noOfPartner");
			noOfPartner.add(StringValidator.maximumLength(20));
			noOfPartner.add(new NumberValidator());
			noOfPartner.setReadOnly(isDisableEdit);
			noOfPartner.setLabelKey("llpReg.page.noOfPartner");
			
			final SSMTextField totalCapitalContribution = new SSMTextField("capitalContribution");
			totalCapitalContribution.add(StringValidator.maximumLength(20));
			totalCapitalContribution.add(new NumberValidator());
			totalCapitalContribution.setReadOnly(isDisableEdit);
			totalCapitalContribution.setLabelKey("llpReg.page.capital");
			
			
			final WebMarkupContainer agreementMarkup = new WebMarkupContainer("agreementMarkup");
			agreementMarkup.setOutputMarkupPlaceholderTag(true);
			agreementMarkup.add(dateOfAgreement);
			agreementMarkup.add(noOfPartner);
			agreementMarkup.add(totalCapitalContribution);
			agreementMarkup.setVisible(false);
			add(agreementMarkup);
			
			agreementLlp.add(new AjaxFormChoiceComponentUpdatingBehavior(){
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if(Parameter.YES_NO_yes.equals(agreementLlp.getInput())){
						agreementMarkup.setVisible(true);
						noOfPartner.setRequired(true);
						totalCapitalContribution.setRequired(true);
						dateOfAgreement.setRequired(true);
						
					}else{
						agreementMarkup.setVisible(false);
						noOfPartner.setRequired(false);
						totalCapitalContribution.setRequired(false);
						dateOfAgreement.setRequired(false);
					}
					target.add(agreementMarkup);
				}
			});
			
		
			SSMDropDownChoice profBodyCode = new SSMDropDownChoice("profBodyCode", Parameter.PROF_BODY_CODE); //MAICSA, MICPA, MIA ... (child)
			profBodyCode.setReadOnly(isDisableEdit);
			profBodyCode.setLabelKey("llpReg.page.profBodyCode");
			add(profBodyCode);
			
			String profBodyTypeValue = llpRegistration.getLlpReservedName().getProfBodyType(); //CA = chartered acc, CS = cosec, LAW
			if(StringUtils.isBlank(profBodyTypeValue)){
				profBodyCode.setVisible(false);
			}else{
				profBodyCode.populateOptionsByParentCode(profBodyTypeValue);
			}

			

			SSMTextField email = new SSMTextField("email");
			email.add(StringValidator.maximumLength(150));
			email.add(EmailAddressValidator.getInstance());
			email.setReadOnly(isDisableEdit);
			email.setUpperCase(false);
			email.setLabelKey("llpReg.page.regEmail");
			add(email);

//			SSMDateTextField terminationDate = new SSMDateTextField("terminationDate");
//			terminationDate.add(new AttributeModifier("readonly", new Model("readonly")));
//			add(terminationDate);

			
			SSMTextField profFirmName = new SSMTextField("profFirmName");
			profFirmName.setReadOnly(isDisableEdit);
			profFirmName.setLabelKey("llpReg.page.firmName");
			add(profFirmName);
			
			
			SSMTextField profFirmNumber = new SSMTextField("profFirmNo");
			profFirmNumber.setReadOnly(isDisableEdit);
			profFirmNumber.setLabelKey("llpReg.page.firmNo");
			add(profFirmNumber);
			
			SSMTextField insuranceCompanyName = new SSMTextField("profInsuranceCompany");
			insuranceCompanyName.setReadOnly(isDisableEdit);
			insuranceCompanyName.setLabelKey("llpReg.page.insuranceCompany");
			add(insuranceCompanyName);
			
			
			SSMTextField insuranceTotalAmount = new SSMTextField("profInsuranceTotalAmt");
			insuranceTotalAmount.add(new NumberValidator());
			insuranceTotalAmount.setReadOnly(isDisableEdit);
			insuranceTotalAmount.setLabelKey("llpReg.page.insuranceTotalAmount");
			add(insuranceTotalAmount);
			
			
			SSMDateTextField policyDurationFrom = new SSMDateTextField("profInsurancePolicyFrom");
			policyDurationFrom.setReadOnly(isDisableEdit);
			add(policyDurationFrom);
			
			SSMDateTextField policyDurationTo = new SSMDateTextField("profInsurancePolicyTo");
			policyDurationTo.setReadOnly(isDisableEdit);
			add(policyDurationTo);
			
			
			
			if((StringUtils.isBlank(llpRegistration.getLlpReservedName().getProfBodyType())) //|| 
					//Parameter.PROF_BODY_TYPE_cs.equals(llpRegistration.getLlpReservedName().getProfBodyType())
					)
			{ //if general registration or profbody Company Secretary
				profFirmName.setVisible(false);
			}
			
			
			Button save = new Button("save") {
				@Override
				public void onSubmit() {
							try {
								if((Parameter.YES_NO_yes.equals(agreementLlp.getInput())) && 
										(StringUtils.isNotBlank(llpRegistration.getNoOfPartner()))){
									int pCount = Integer.parseInt((String)llpRegistration.getNoOfPartner());
									if (pCount<2){
										throw new SSMException(SSMExceptionParam.LLP_REG_PT_COUNT);
									}
								}	
								

								//Save Transaction To DB
								if(StringUtils.isBlank(llpRegistration.getLlpNo())){
									LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
									llpRegTransaction.setLlpRegistration(llpRegistration);
									llpRegTransactionService.update(llpRegTransaction);
									getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
								}
								
								
								setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.CO_PANEL));
								
								
							} catch (SSMException e) {
								ssmError(e);
							}
					}
			};
			add(save);
			if(!isOfficer && !isNewReg){
				save.setVisible(false);
			}
			
			Button reset = new Button("reset") {
				@Override
				public void onSubmit() {
						String nsRefNo = llpRegistration.getLlpReservedName().getRefNo();
						//RESET LLP REGISTRATION
						llpRegistration.setIdLlp(0);
						llpRegistration.setLlpNo(null);
						llpRegistration.setRegDate(null);
						llpRegistration.setLlpName(null);
						llpRegistration.setRegAdd1(null);
						llpRegistration.setRegAdd2(null);
						llpRegistration.setRegAdd3(null);
						llpRegistration.setRegCountry(com.ssm.llp.base.common.Parameter.COUNTRY_CODE_malaysia); //onload default Malaysia
						llpRegistration.setRegState(null);
						llpRegistration.setRegCity(null);
						llpRegistration.setRegPostcode(null);
						llpRegistration.setBussinessAdd1(null);
						llpRegistration.setBussinessAdd2(null);
						llpRegistration.setBussinessAdd3(null);
						llpRegistration.setBussinessCountryCode(com.ssm.llp.base.common.Parameter.COUNTRY_CODE_malaysia);
						llpRegistration.setBussinessStateCode(null);
						llpRegistration.setBussinessCity(null);
						llpRegistration.setBussinessPostcode(null);
						llpRegistration.setNatureOfBusinessDesc(null);
						llpRegistration.setLlpStatus(null);
						llpRegistration.setOffTelNo(null);
						llpRegistration.setHpNo(null);
						llpRegistration.setFaxNo(null);
						llpRegistration.setCapitalContribution(null);
						llpRegistration.setAgreementLlp(com.ssm.llp.base.common.Parameter.YES_NO_no);
						llpRegistration.setRegType(null);
						llpRegistration.setEmail(null);
						llpRegistration.setTerminationDate(null);
						llpRegistration.setForeignCountry(null);  
						llpRegistration.setProfBodyType(null); //ca, law, cs
						llpRegistration.setProfBodyCode(null); //maicsa, mia etc
						llpRegistration.setCreateDt(null);
						llpRegistration.setCreateBy(null);
						llpRegistration.setUpdateDt(null);
						llpRegistration.setUpdateBy(null);
						llpRegistration.setInternalRemark(null);
						llpRegistration.setLodgeBy(null);
						llpRegistration.setAgreementLlpDate(null);
						llpRegistration.setNoOfPartner(null);
						llpRegistration.setDeclareChk(Boolean.FALSE);	
						llpRegistration.setProfFirmName(null);
						llpRegistration.setProfFirmNo(null);
						llpRegistration.setProfInsuranceCompany(null);
						llpRegistration.setProfInsuranceTotalAmt(null);
						llpRegistration.setProfInsurancePolicyFrom(null);
						llpRegistration.setProfInsurancePolicyTo(null);
						
						llpRegistration.setLlpReservedName(null);
						llpRegistration.setLlpPartnerLinks(new ArrayList<LlpPartnerLink>(0));
						llpRegistration.setLlpBusinessCodeLink(new ArrayList<LlpBusinessCodeLink>(0));

						//END RESET LLP REGISTRATION
						
						LlpRegTransaction llpRegTransaction =  llpRegTransactionService.findByReserverNameRefNo(nsRefNo);
						llpRegTransaction.setStatus(Parameter.REG_TRANSACTION_STATUS_data_entry);
						
						LlpReservedName llpReservedName = llpReservedNameService.findById(nsRefNo);
	
						// prepare NS data
						llpRegistration.setLlpReservedName(llpReservedName);
						
						//map NS data and set llpRegistration
						llpRegistration.setLlpName(llpRegistration.getLlpReservedName().getApplyLlpName());
						llpRegistration.setLlpStatus(Parameter.LLP_STATUS_existing);
						llpRegistration.setRegType(llpRegistration.getLlpReservedName().getRegType()); //local, foreign, llp for prof body (sama purpose apply ns)
						llpRegistration.setProfBodyType(llpRegistration.getLlpReservedName().getProfBodyType()); //ProfBodyType=LAW, CA, CS... ProfBodyCode=MAICSA,MIA,LS...
						llpRegistration.setLodgeBy(UserEnvironmentHelper.getLoginName());
	
	
						// Prepare CO data from login
						List<LlpPartnerLink> partnerLinkList = new ArrayList<LlpPartnerLink>();
										
						LlpUserProfile llpUserProfile = ((LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
						LlpPartnerLink llpPartnerLink = new LlpPartnerLink();
						llpPartnerLink.setUserRefNo(llpUserProfile.getUserRefNo());
						llpPartnerLink.setLlpUserProfile(llpUserProfile);
						llpPartnerLink.copyDataFromProfile(llpUserProfile);
						llpPartnerLink.setType(Parameter.USER_TYPE_complianceOfficer); // CO = compliance officer
						llpPartnerLink.setLinkStatus(Parameter.PARTNER_LINK_STATUS_active); //link_status = A
						llpPartnerLink.setCapitalContribution("");
						llpPartnerLink.setAppointmentDate(new Date());
						partnerLinkList.add(llpPartnerLink);
						
						if(StringUtils.isBlank(llpUserProfile.getLicenseMbrNo())){//if not professional member.mandatory to become partner.
							LlpPartnerLink llpPartnerLinkAsPartner = new LlpPartnerLink();
							llpPartnerLinkAsPartner.setUserRefNo(llpUserProfile.getUserRefNo());
							llpPartnerLinkAsPartner.setLlpUserProfile(llpUserProfile);
							llpPartnerLinkAsPartner.copyDataFromProfile(llpUserProfile);
							llpPartnerLinkAsPartner.setType(Parameter.USER_TYPE_partner); // PT = Partner
							llpPartnerLinkAsPartner.setLinkStatus(Parameter.PARTNER_LINK_STATUS_active); //link_status = A
							llpPartnerLinkAsPartner.setCapitalContribution("");
							llpPartnerLinkAsPartner.setAppointmentDate(new Date());
							partnerLinkList.add(llpPartnerLinkAsPartner);
						}
						
						
	
						// Tie kan registration dgn partnerlink
						llpRegistration.setLlpPartnerLinks(partnerLinkList);
						getSession().setAttribute("llpRegistration_", llpRegistration); // set into session
						
						llpRegTransaction.setLlpRegistration(llpRegistration);
						llpRegTransactionService.update(llpRegTransaction);
						getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
						
						setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.LLP_PANEL));
								
					}
			};
			add(reset);
			reset.addOrReplaceOnClick(ParamLocale.ACTION_CONFIRM_RESET_FORM);
			if(!isNewReg){
				reset.setVisible(false);
			}else{
				LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
				if(llpRegTransaction!=null && llpRegTransaction.getRegTransactionId()==0){
					reset.setVisible(false);
				}
			}
			
			
			Button cancel = new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListLlpRegistration.class);
				}
			}.setDefaultFormProcessing(false);
			add(cancel);
			
			
			

		}

	}

}
