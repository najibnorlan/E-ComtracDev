package com.ssm.llp.mod1.page;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditCORegistrationPanel extends SecBasePanel {


	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
	public EditCORegistrationPanel(String panelId) {
		super(panelId);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				List<LlpPartnerLink> partnerLinkList = llpRegistration.getLlpPartnerLinks();
				for (Iterator iterator = partnerLinkList.iterator(); iterator.hasNext();) {
					LlpPartnerLink llpPartnerLink = (LlpPartnerLink) iterator.next();
					if (Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())) {
						return llpPartnerLink;
					}

				}
				return null;
			}
		}));

		init();
	}

	private void init() {
		add(new CORegistrationForm("cORegForm", getDefaultModel()));
	}

	private class CORegistrationForm extends Form {
		LlpPartnerLink cOfficer = (LlpPartnerLink) getDefaultModel().getObject();
		
		public CORegistrationForm(String id, IModel model) {
			super(id, model);
			
			boolean isOfficer = UserEnvironmentHelper.isInternalUser();
			boolean isNewReg = true;
			boolean isDisableEdit = false; //can edit
			
			
			if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No, show view mode only
				isNewReg = false;
			}
			if(!isNewReg && !isOfficer){ //if non ssm and already obtain llp No.
				isDisableEdit = true;
			}
			

			//SSMTextField cOName = new SSMTextField("llpUserProfile.name"); // "llpUserProfile.name" same in data object and in HTML
			//cOName.add(new AttributeModifier("readonly", new Model("readonly")));
			SSMLabel cOName = new SSMLabel("name",cOfficer.getLlpUserProfile().getName());
			add(cOName);

			//SSMTextField cOIdNo = new SSMTextField("llpUserProfile.idNo");
			//cOIdNo.add(new AttributeModifier("readonly", new Model("readonly")));
			SSMLabel cOIdNo = new SSMLabel("idNo",cOfficer.getLlpUserProfile().getIdNo());
			add(cOIdNo);

			//SSMDateTextField cODob = new SSMDateTextField("llpUserProfile.dob");
			//cODob.add(new AttributeModifier("readonly", new Model("readonly")));
			SSMLabel cODob = new SSMLabel("dob",cOfficer.getLlpUserProfile().getDob());
			add(cODob);
			
			
			//SSMDropDownChoice cONationality = new SSMDropDownChoice("llpUserProfile.nationality", Parameter.NATIONALITY_TYPE);
			//cONationality.add(new AttributeModifier("readonly", new Model("readonly")));
			String nationalityValue = cOfficer.getLlpUserProfile().getNationality();
			SSMLabel cONationality = new SSMLabel("nationality", nationalityValue, Parameter.NATIONALITY_TYPE);
			add(cONationality);

			//SSMDropDownChoice cORace = new SSMDropDownChoice("llpUserProfile.race", Parameter.RACE);
			//cORace.add(new AttributeModifier("readonly", new Model("readonly")));
			String raceValue = cOfficer.getLlpUserProfile().getRace();
			SSMLabel cORace = new SSMLabel("race", raceValue, Parameter.RACE);
			add(cORace);
			
			//SSMRadioChoice cOGender = new SSMRadioChoice("llpUserProfile.gender", Parameter.GENDER);
			//cOGender.add(new AttributeModifier("readonly", new Model("readonly")));
			String genderValue = cOfficer.getLlpUserProfile().getGender();
			SSMLabel cOGender = new SSMLabel("gender", genderValue, Parameter.GENDER);
			add(cOGender);
			
			
			//SSMTextField cOLicenseMbrType = new SSMTextField("llpUserProfile.licenseMbrType");
			//cOLicenseMbrType.add(new AttributeModifier("readonly", new Model("readonly")));
			String licenseMbrTypeValue = cOfficer.getLlpUserProfile().getLicenseMbrType();
			SSMLabel cOLicenseMbrType = new SSMLabel("licenseMbrType", licenseMbrTypeValue, Parameter.PROF_BODY_CODE);
			cOLicenseMbrType.setVisible(true);
			add(cOLicenseMbrType);
			
			//SSMTextField cOLicenseMbrNo = new SSMTextField("llpUserProfile.licenseMbrNo");
			//cOLicenseMbrNo.add(new AttributeModifier("readonly", new Model("readonly")));
			SSMLabel cOLicenseMbrNo = new SSMLabel("licenseMbrNo", cOfficer.getLlpUserProfile().getLicenseMbrNo());
			add(cOLicenseMbrNo);
			
			if(StringUtils.isBlank(licenseMbrTypeValue)){
				cOLicenseMbrType.setVisible(false);
			}
			
			
			
			SSMDateTextField cOAppointmentDate = new SSMDateTextField("appointmentDate");
			cOAppointmentDate.setRequired(true);
			cOAppointmentDate.setReadOnly(isDisableEdit);
			cOAppointmentDate.setLabelKey("llpReg.page.co.appointmentDate");
			add(cOAppointmentDate);

			SSMTextField cOAdd1 = new SSMTextField("add1");
			cOAdd1.setRequired(true);
			cOAdd1.add(StringValidator.maximumLength(150));
			cOAdd1.setReadOnly(isDisableEdit);
			cOAdd1.setLabelKey("llpReg.page.co.add");
			add(cOAdd1);

			SSMTextField cOAdd2 = new SSMTextField("add2");
			cOAdd2.add(StringValidator.maximumLength(150));
			cOAdd2.setReadOnly(isDisableEdit);
			add(cOAdd2);

			SSMTextField cOAdd3 = new SSMTextField("add3");
			cOAdd3.add(StringValidator.maximumLength(150));
			cOAdd3.setReadOnly(isDisableEdit);
			add(cOAdd3);

			SSMDropDownChoice cOState = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			cOState.setLabelKey("llpReg.page.co.state");
			cOState.setReadOnly(isDisableEdit);
			add(cOState);

			SSMDropDownChoice cOCountry = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE, cOState);
			cOCountry.setRequired(true);
			cOCountry.setReadOnly(true); //always malaysian only
			cOCountry.setLabelKey("llpReg.page.co.country");
			add(cOCountry);

			SSMTextField cOCity = new SSMTextField("city");
			cOCity.setRequired(true);
			cOCity.add(StringValidator.maximumLength(150));
			cOCity.setReadOnly(isDisableEdit);
			cOCity.setLabelKey("llpReg.page.co.city");
			add(cOCity);

			SSMTextField cOPostcode = new SSMTextField("postcode");
			cOPostcode.setRequired(true);
			cOPostcode.add(StringValidator.maximumLength(10));
			cOPostcode.add(new NumberValidator());
			cOPostcode.setReadOnly(isDisableEdit);
			cOPostcode.setLabelKey("llpReg.page.co.postcode");
			add(cOPostcode);

			SSMTextField cOTelNo = new SSMTextField("offNo");
			cOTelNo.setRequired(true);
			cOTelNo.add(StringValidator.maximumLength(15));
			cOTelNo.add(new NumberValidator());
			cOTelNo.setReadOnly(isDisableEdit);
			cOTelNo.setLabelKey("llpReg.page.co.offPhoneNo");
			add(cOTelNo);

			SSMTextField cOHpNo = new SSMTextField("hpNo");
			cOHpNo.setRequired(true);
			cOHpNo.add(StringValidator.maximumLength(15));
			cOHpNo.add(new NumberValidator());
			cOHpNo.setReadOnly(isDisableEdit);
			cOHpNo.setLabelKey("llpReg.page.co.hpNo");
			add(cOHpNo);

			SSMTextField cOFaxNo = new SSMTextField("faxNo");
			cOFaxNo.add(StringValidator.maximumLength(15));
			cOFaxNo.add(new NumberValidator());
			cOFaxNo.setReadOnly(isDisableEdit);
			cOFaxNo.setLabelKey("llpReg.page.co.faxNo");
			add(cOFaxNo);

			SSMTextField cOEmail = new SSMTextField("email");
			cOEmail.setRequired(true);
			cOEmail.setUpperCase(false);
			cOEmail.add(StringValidator.maximumLength(150));
			cOEmail.add(EmailAddressValidator.getInstance());
			cOEmail.setReadOnly(isDisableEdit);
			cOEmail.setLabelKey("llpReg.page.co.email");
			add(cOEmail);


			
			Button save = new Button("save") {
				@Override
				public void onSubmit() {
					//Save Transaction To DB
					if(StringUtils.isBlank(llpRegistration.getLlpNo())){
						LlpRegTransaction llpRegTransaction = (LlpRegTransaction) getSession().getAttribute("llpRegTransaction_");
						llpRegTransaction.setLlpRegistration(llpRegistration);
						llpRegTransactionService.update(llpRegTransaction);
						getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
					}
					
					setResponsePage(new LlpRegistrationBasePage(LlpRegistrationTabPanel.PARTNER_LINK_PANEL));
				}
			};
			add(save);
			if(!isOfficer && !isNewReg){
				save.setVisible(false);
			}
			
			
			
			Button cancel = new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListLlpRegistration.class);
				}
			}.setDefaultFormProcessing(false);
			add(cancel);
			
			
//			if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm
//				if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
//					save.setVisible(false);
//					//cancel.setVisible(false);
//				}
//			}
			
			

		}

	}

}
