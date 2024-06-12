package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

public class EditLlpRegistrationPage extends SecBasePanel {

	// add New
	public EditLlpRegistrationPage(final String panelId) {
		super(panelId);
		LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpRegistration();
			}
		}));
		init();

	}

	public EditLlpRegistrationPage(final String panelId, final String llpNo) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return getService(LlpRegistrationService.class.getSimpleName()).findById(llpNo);// find
																								// by
																								// ID
			}
		}));
		init();
	}

	private void init() {
		add(new LlpRegistrationForm("llpRegForm", getDefaultModel()));
	}

	private class LlpRegistrationForm extends Form {

		public LlpRegistrationForm(String id, IModel model) {
			super(id, model);

			TextField llpNo = new SSMTextField("llpNo");
			llpNo.setRequired(true);
			llpNo.add(StringValidator.maximumLength(25));
			add(llpNo);

			SSMDateTextField regDate = new SSMDateTextField("regDate");
			regDate.setRequired(true);
			// regDate.add(StringValidator.maximumLength(20));
			add(regDate);

			SSMTextField llpName = new SSMTextField("llpName");
			llpName.setRequired(true);
			llpName.add(StringValidator.maximumLength(100));
			add(llpName);

			SSMTextField regAdd1 = new SSMTextField("regAdd1");
			regAdd1.setRequired(true);
			regAdd1.add(StringValidator.maximumLength(150));
			add(regAdd1);

			SSMTextField regAdd2 = new SSMTextField("regAdd2");
			regAdd2.setRequired(true);
			regAdd2.add(StringValidator.maximumLength(150));
			add(regAdd2);

			SSMTextField regAdd3 = new SSMTextField("regAdd3");
			regAdd3.setRequired(true);
			regAdd3.add(StringValidator.maximumLength(150));
			add(regAdd3);

			SSMDropDownChoice regCountry = new SSMDropDownChoice("regCountry", Parameter.COUNTRY_CODE);
			add(regCountry);

			// SSMTextField regCountry = new SSMTextField("regCountry");
			// regCountry.setRequired(true);
			// regCountry.add(StringValidator.maximumLength(4));
			// add(regCountry);

			SSMDropDownChoice regState = new SSMDropDownChoice("regState", Parameter.STATE_CODE);
			add(regState);

			// SSMTextField regState = new SSMTextField("regState");
			// regState.setRequired(true);
			// regState.add(StringValidator.maximumLength(4));
			// add(regState);

			SSMTextField regCity = new SSMTextField("regCity");
			regCity.setRequired(true);
			regCity.add(StringValidator.maximumLength(4));
			add(regCity);

			SSMTextField regPostcode = new SSMTextField("regPostcode");
			regPostcode.setRequired(true);
			regPostcode.add(StringValidator.maximumLength(10));
			add(regPostcode);

			SSMTextField bussinessAdd1 = new SSMTextField("bussinessAdd1");
			bussinessAdd1.setRequired(true);
			bussinessAdd1.add(StringValidator.maximumLength(150));
			add(bussinessAdd1);

			SSMTextField bussinessAdd2 = new SSMTextField("bussinessAdd2");
			bussinessAdd2.setRequired(true);
			bussinessAdd2.add(StringValidator.maximumLength(150));
			add(bussinessAdd2);

			SSMTextField bussinessAdd3 = new SSMTextField("bussinessAdd3");
			bussinessAdd3.setRequired(true);
			bussinessAdd3.add(StringValidator.maximumLength(150));
			add(bussinessAdd3);

			SSMDropDownChoice bussinessCountryCode = new SSMDropDownChoice("bussinessCountryCode", Parameter.COUNTRY_CODE);
			add(bussinessCountryCode);

			// SSMTextField bussinessCountryCode = new
			// SSMTextField("bussinessCountryCode");
			// bussinessCountryCode.setRequired(true);
			// bussinessCountryCode.add(StringValidator.maximumLength(4));
			// add(bussinessCountryCode);

			SSMDropDownChoice bussinessStateCode = new SSMDropDownChoice("bussinessStateCode", Parameter.STATE_CODE);
			add(bussinessStateCode);

			// SSMTextField bussinessStateCode = new
			// SSMTextField("bussinessStateCode");
			// bussinessStateCode.setRequired(true);
			// bussinessStateCode.add(StringValidator.maximumLength(4));
			// add(bussinessStateCode);

			SSMTextField bussinessCity = new SSMTextField("bussinessCity");
			bussinessCity.setRequired(true);
			bussinessCity.add(StringValidator.maximumLength(4));
			add(bussinessCity);

			SSMTextField bussinessPostcode = new SSMTextField("bussinessPostcode");
			bussinessPostcode.setRequired(true);
			bussinessPostcode.add(StringValidator.maximumLength(10));
			add(bussinessPostcode);

			TextArea natureOfBusinessDesc = new TextArea("natureOfBusinessDesc");
			natureOfBusinessDesc.setRequired(true);
			natureOfBusinessDesc.add(StringValidator.maximumLength(500));
			add(natureOfBusinessDesc);

			SSMDropDownChoice llpStatus = new SSMDropDownChoice("llpStatus", Parameter.LLP_STATUS);
			add(llpStatus);

			// SSMTextField llpStatus = new SSMTextField("llpStatus");
			// llpStatus.setRequired(true);
			// llpStatus.add(StringValidator.maximumLength(4));
			// add(llpStatus);

			SSMTextField offTelNo = new SSMTextField("offTelNo");
			offTelNo.setRequired(true);
			offTelNo.add(StringValidator.maximumLength(15));
			add(offTelNo);

			SSMTextField hpNo = new SSMTextField("hpNo");
			hpNo.setRequired(true);
			hpNo.add(StringValidator.maximumLength(15));
			add(hpNo);

			SSMTextField faxNo = new SSMTextField("faxNo");
			faxNo.setRequired(true);
			faxNo.add(StringValidator.maximumLength(15));
			add(faxNo);

			SSMTextField capitalContribution = new SSMTextField("capitalContribution");
			capitalContribution.setRequired(true);
			capitalContribution.add(StringValidator.maximumLength(20));
			add(capitalContribution);

			SSMRadioChoice agreementLlp = new SSMRadioChoice("agreementLlp", Parameter.YES_NO);
			add(agreementLlp);

			// DropDownChoice agreementLlP = new DropDownChoice("agreementLlp");
			// agreementLlP.setChoices(new AbstractReadOnlyModel() {
			// public Object getObject() {
			// List<String> l = new ArrayList<String>(2);
			// l.add("YES");
			// l.add("NO");
			// return l;
			// }
			// });
			// add(agreementLlP);

			SSMDropDownChoice regType = new SSMDropDownChoice("regType", Parameter.LLP_REG_TYPE);
			add(regType);

			// SSMTextField regType = new SSMTextField("regType");
			// regType.setRequired(true);
			// regType.add(StringValidator.maximumLength(4));
			// add(regType);

			SSMTextField email = new SSMTextField("email");
			email.add(StringValidator.maximumLength(150));
			email.add(EmailAddressValidator.getInstance());
			email.setUpperCase(false);
			add(email);

			SSMDateTextField terminationDate = new SSMDateTextField("terminationDate");
			terminationDate.setRequired(true);
			// terminationDate.add(StringValidator.maximumLength(10));
			add(terminationDate);

			SSMDropDownChoice foreignCountry = new SSMDropDownChoice("foreignCountry", Parameter.COUNTRY_CODE);
			add(foreignCountry);

			// SSMTextField foreignCountry = new SSMTextField("foreignCountry");
			// foreignCountry.setRequired(true);
			// foreignCountry.add(StringValidator.maximumLength(4));
			// add(foreignCountry);

			add(new Button("save") {
				@Override
				public void onSubmit() {
					LlpRegistration llpReg = (LlpRegistration) getForm().getModelObject();

					if (llpReg.getIdLlp() == 0) {
						getService(LlpRegistrationService.class.getSimpleName()).insert(llpReg);
					} else {
						getService(LlpRegistrationService.class.getSimpleName()).update(llpReg);
					}
					setResponsePage(ListLlpRegistration.class);
				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListLlpRegistration.class);
				}
			}.setDefaultFormProcessing(false));

		}

	}

}
