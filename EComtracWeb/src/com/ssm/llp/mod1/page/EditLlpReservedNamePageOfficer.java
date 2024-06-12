package com.ssm.llp.mod1.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EditLlpReservedNamePageOfficer extends SecBasePage {
	LlpReservedNameService llpReservedNameService = (LlpReservedNameService) WicketApplication.getServiceNew(LlpReservedNameService.class
			.getSimpleName());

	public EditLlpReservedNamePageOfficer() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedName();
			}
		}));
		init();
	}

	public EditLlpReservedNamePageOfficer(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpReservedNameService.class.getSimpleName()).findById(refNo);
			}
		}));
		init();
	}

	public EditLlpReservedNamePageOfficer(PageParameters param1, PageParameters param2) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedName();
			}
		}));

		String applyLlpName = param1.get("applyLlpName").toString();
		String purposeApply = param2.get("purposeApply").toString();

		//((LlpReservedName) getDefaultModel().getObject()).setPurposeApply(purposeApply);
		((LlpReservedName) getDefaultModel().getObject()).setApplyLlpName(applyLlpName);

		System.out.println("applyLlpName" + applyLlpName);
		System.out.println("purposeApply param" + purposeApply);
		init();
	}

	private void init() {
		add(new LlpReservedNameForm("form", getDefaultModel()));
	}

	private class LlpReservedNameForm extends Form {

		public LlpReservedNameForm(String id, IModel m) {
			super(id, m);

			final LlpReservedName llpReservedName = (LlpReservedName) m.getObject();

			SSMTextField refNo = new SSMTextField("refNo");
			if (llpReservedName.getRefNo() != null) {
				refNo.setVisible(true);
				refNo.add(new AttributeModifier("readonly", new Model("readonly")));
				;
			} else {
				refNo.setVisible(false);
			}
			add(refNo);

			SSMTextField applyLlpName = new SSMTextField("applyLlpName");
			if (llpReservedName.getRefNo() != null) {
				applyLlpName.add(new AttributeModifier("readonly", new Model("readonly")));
				;
			}
			add(applyLlpName);

			SSMTextField llpNo = new SSMTextField("llpNo");
			if (llpReservedName.getRefNo() != null) {
				llpNo.setVisible(true);
				llpNo.add(new AttributeModifier("readonly", new Model("readonly")));
				;
			} else {
				llpNo.setVisible(false);
			}
			add(llpNo);

			SSMDateTextField resultDate = new SSMDateTextField("resultDate");
			if (llpReservedName.getRefNo() != null) {
				resultDate.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				resultDate.setVisible(true);
			} else {
				resultDate.setVisible(false);
			}
			add(resultDate);

			SSMDateTextField expNameDate = new SSMDateTextField("expNameDate");
			if (llpReservedName.getRefNo() != null) {
				expNameDate.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				expNameDate.setVisible(true);
			} else {
				expNameDate.setVisible(false);
			}
			add(expNameDate);

//			SSMDropDownChoice purposeApply = new SSMDropDownChoice("purposeApply", Parameter.LLP_REG_PURPOSE);
//			if (llpReservedName.getRefNo() != null) {
//				purposeApply.add(new AttributeModifier("readonly", new Model("readonly")));
//				;
//			}
//			add(purposeApply);

			SSMTextArea extraExplanation = new SSMTextArea("extraExplanation");
			extraExplanation.add(StringValidator.maximumLength(255));
			add(extraExplanation);

//			SSMTextField llpCompanyType = new SSMTextField("llpCompanyType");
//			llpCompanyType.add(new AttributeModifier("readonly", new Model("readonly")));
//			;
//			if (llpReservedName.getRefNo() != null) {
//				if (llpReservedName.getLlpNo() != null) {
//					llpCompanyType.setVisible(true);
//				} else {
//					llpCompanyType.setVisible(false);
//				}
//			} else {
//				llpCompanyType.setVisible(false);
//			}
//			add(llpCompanyType);

			SSMDropDownChoice profBodyCode = new SSMDropDownChoice("profBodyCode", Parameter.PROF_BODY_TYPE);
			add(profBodyCode);		
			
			
			SSMTextField profAuthOrg = new SSMTextField("profAuthOrg");
			add(profAuthOrg);

			SSMTextField profAuthLetterRefNo = new SSMTextField("profAuthLetterRefNo");
			profAuthLetterRefNo.add(StringValidator.maximumLength(255));
			add(profAuthLetterRefNo);

			SSMDateTextField profAuthLetterDate = new SSMDateTextField("profAuthLetterDate");
			add(profAuthLetterDate);

			SSMTextField profLetterPurpose = new SSMTextField("profLetterPurpose");
			profLetterPurpose.add(StringValidator.maximumLength(255));
			add(profLetterPurpose);

			SSMTextField profLetterSign = new SSMTextField("profLetterSign");
			profLetterSign.add(StringValidator.maximumLength(255));
			add(profLetterSign);

			SSMTextArea profRemark = new SSMTextArea("profRemark");
			profRemark.add(StringValidator.maximumLength(255));
			add(profRemark);

			SSMTextArea officerRemark = new SSMTextArea("officerRemark");
			add(officerRemark);

			SSMTextField lodgeBy = new SSMTextField("lodgeBy");
			lodgeBy.add(new AttributeModifier("readonly", new Model("readonly")));
			add(lodgeBy);

			add(new Button("update") {
				public void onSubmit() {

					LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();

					llpReservedName.setUpdateBy(UserEnvironmentHelper.getLoginName());

					getService(LlpReservedNameService.class.getSimpleName()).update(llpReservedName);

					setResponsePage(ListLlpReservedNamesOfficer.class);
				}
			});

			add(new Button("ok") {
				public void onSubmit() {
					setResponsePage(ListLlpReservedNamesOfficer.class);
				}
			}.setDefaultFormProcessing(false));

		}
	}

}
