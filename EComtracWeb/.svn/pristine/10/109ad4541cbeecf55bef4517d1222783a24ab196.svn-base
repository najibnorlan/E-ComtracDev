package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
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
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.service.LlpPartnerLinkService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditPartnerRegistrationPanel extends SecBasePanel {

	// add New
	public EditPartnerRegistrationPanel(final String panelId) {
		super(panelId); // call constructor class kat parent (SecBasePanel)
		LlpPartnerLinkService llpPartnerLinkService = (LlpPartnerLinkService) getService(LlpPartnerLinkService.class.getSimpleName());
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpPartnerLink();
			}
		}));

		init();

	}


	private void init() {
		add(new pRegistrationForm("pRegForm", getDefaultModel()));
	}

	private class pRegistrationForm extends Form {

		public pRegistrationForm(String id, IModel model) {
			super(id, model);

			SSMTextField pName = new SSMTextField("llpUserProfile.name"); // "llpUserProfile.name"
																			// same
																			// in
																			// data
																			// object
																			// and
																			// in
																			// HTML
			pName.setRequired(true);
			pName.add(StringValidator.maximumLength(100));
			add(pName);

			SSMDropDownChoice pIdType = new SSMDropDownChoice("llpUserProfile.idType", Parameter.ID_TYPE);
			add(pIdType);

			SSMTextField pIdNo = new SSMTextField("llpUserProfile.idNo");
			pIdNo.setRequired(true);
			pIdNo.add(StringValidator.maximumLength(20));
			add(pIdNo);

			SSMDateTextField pDob = new SSMDateTextField("llpUserProfile.dob");
			pDob.setRequired(true);
			add(pDob);

			SSMTextField pAdd1 = new SSMTextField("add1");
			pAdd1.setRequired(true);
			pAdd1.add(StringValidator.maximumLength(150));
			add(pAdd1);

			SSMTextField pAdd2 = new SSMTextField("add2");
			pAdd2.setRequired(true);
			pAdd2.add(StringValidator.maximumLength(150));
			add(pAdd2);

			SSMTextField pAdd3 = new SSMTextField("add3");
			pAdd3.setRequired(true);
			pAdd3.add(StringValidator.maximumLength(150));
			add(pAdd3);

			SSMDropDownChoice pState = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			add(pState);

			SSMDropDownChoice pCountry = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE, pState);
			add(pCountry);

			SSMTextField pCity = new SSMTextField("city");
			pCity.setRequired(true);
			pCity.add(StringValidator.maximumLength(4));
			add(pCity);

			SSMTextField pPostcode = new SSMTextField("postcode");
			pPostcode.setRequired(true);
			pPostcode.add(StringValidator.maximumLength(10));
			add(pPostcode);

			SSMTextField pTelNo = new SSMTextField("offNo");
			pTelNo.setRequired(true);
			pTelNo.add(StringValidator.maximumLength(15));
			add(pTelNo);

			SSMTextField pHpNo = new SSMTextField("hpNo");
			pHpNo.setRequired(true);
			pHpNo.add(StringValidator.maximumLength(15));
			add(pHpNo);

			SSMTextField pFaxNo = new SSMTextField("faxNo");
			pFaxNo.setRequired(true);
			pFaxNo.add(StringValidator.maximumLength(15));
			add(pFaxNo);

			SSMTextField pEmail = new SSMTextField("llpUserProfile.email");
			pEmail.add(StringValidator.maximumLength(150));
			pEmail.add(EmailAddressValidator.getInstance());
			add(pEmail);

			SSMRadioChoice pNationality = new SSMRadioChoice("llpUserProfile.nationality", Parameter.NATIONALITY_TYPE);
			pNationality.setRequired(true);
			add(pNationality);

			SSMDropDownChoice pRace = new SSMDropDownChoice("llpUserProfile.race", Parameter.RACE);
			add(pRace);

			add(new Button("save") {
				@Override
				public void onSubmit() {
					LlpPartnerLink llpOfficer = (LlpPartnerLink) getForm().getModelObject();

					// if (llpOfficer.getIdLlp()==0) {
					getService(LlpPartnerLinkService.class.getSimpleName()).insert(llpOfficer);
					// }else{
					// getService(LlpRegistrationService.class.getSimpleName()).update(llpReg);
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
