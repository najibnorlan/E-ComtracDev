package com.ssm.llp.mod1.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpRegistrationService;

//tak menggunakan llp_partner_link??

public class EditLlpUserProfilePanel extends SecBasePanel {

	// add New
	public EditLlpUserProfilePanel(final String panelId) {
		super(panelId); // call constructor class kat parent (SecBasePanel)
		final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {

				LlpUserProfile llpUserProfile = ((LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
				return llpUserProfile;

			}
		}));

		init();
	}

	//
	//
	// public EditLlpUserProfilePanel(final Long idRegUser) {
	// super(panelId); //call constructor class kat parent (SecBasePanel)
	// setDefaultModel(new CompoundPropertyModel(
	// new LoadableDetachableModel() {
	// protected Object load() {
	// return getService(
	// LlpUserProfileService.class.getSimpleName()).findById(idRegUser);
	// }
	// }));
	// init();
	// }

	private void init() {
		add(new LodgerForm("lodgerForm", getDefaultModel()));
	}

	private class LodgerForm extends Form {

		public LodgerForm(String id, IModel model) {
			super(id, model);

			TextField lodgerName = new SSMTextField("name");
			// lodgerName.setRequired(true);
			// lodgerName.add(StringValidator.maximumLength(100));
			lodgerName.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerName);

			TextField lodgerIdNo = new SSMTextField("idNo");
			// lodgerIdNo.setRequired(true);
			// lodgerIdNo.add(StringValidator.maximumLength(20));
			lodgerIdNo.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerIdNo);

			SSMTextField lodgerAdd1 = new SSMTextField("add1");
			// lodgerAdd1.setRequired(true);
			// lodgerAdd1.add(StringValidator.maximumLength(150));
			lodgerAdd1.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerAdd1);

			SSMTextField lodgerAdd2 = new SSMTextField("add2");
			// lodgerAdd2.setRequired(true);
			// lodgerAdd2.add(StringValidator.maximumLength(150));
			lodgerAdd2.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerAdd2);

			SSMTextField lodgerAdd3 = new SSMTextField("add3");
			// lodgerAdd3.setRequired(true);
			// lodgerAdd3.add(StringValidator.maximumLength(150));
			lodgerAdd3.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerAdd3);

			SSMDropDownChoice lodgerCountry = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE);
			add(lodgerCountry);

			SSMDropDownChoice lodgerState = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			add(lodgerState);

			SSMTextField lodgerCity = new SSMTextField("city");
			// lodgerCity.setRequired(true);
			// lodgerCity.add(StringValidator.maximumLength(4));
			lodgerCity.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerCity);

			SSMTextField lodgerPostcode = new SSMTextField("postcode");
			// lodgerPostcode.setRequired(true);
			// lodgerPostcode.add(StringValidator.maximumLength(10));
			lodgerPostcode.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerPostcode);

			SSMTextField lodgerTelNo = new SSMTextField("offNo");
			// lodgerTelNo.setRequired(true);
			// lodgerTelNo.add(StringValidator.maximumLength(15));
			lodgerTelNo.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerTelNo);

			SSMTextField lodgerHpNo = new SSMTextField("hpNo");
			// lodgerHpNo.setRequired(true);
			// lodgerHpNo.add(StringValidator.maximumLength(15));
			lodgerHpNo.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerHpNo);

			SSMTextField lodgerFaxNo = new SSMTextField("faxNo");
			// lodgerFaxNo.setRequired(true);
			// lodgerFaxNo.add(StringValidator.maximumLength(15));
			lodgerFaxNo.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgerFaxNo);

			SSMTextField lodgerEmail = new SSMTextField("email");
			lodgerEmail.setUpperCase(false);
			lodgerEmail.add(StringValidator.maximumLength(150));
			lodgerEmail.add(EmailAddressValidator.getInstance());
			add(lodgerEmail);

			add(new Button("save") {
				@Override
				public void onSubmit() {
					LlpUserProfile llpLodger = (LlpUserProfile) getForm().getModelObject();

					// if (llpOfficer.getIdLlp()==0) {
					// getService(LlpUserProfileService.class.getSimpleName()).insert(llpLodger);
					// }else{
					getService(LlpRegistrationService.class.getSimpleName()).update(llpLodger);
					// }
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
