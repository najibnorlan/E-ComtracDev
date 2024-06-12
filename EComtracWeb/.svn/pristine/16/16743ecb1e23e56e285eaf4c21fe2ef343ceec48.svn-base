package com.ssm.llp.mod1.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DetailsLlpReservedName extends SecBasePage {

	public DetailsLlpReservedName() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedName();
			}
		}));
		init();
	}

	public DetailsLlpReservedName(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpReservedNameService.class.getSimpleName()).findById(refNo);
			}
		}));
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
			refNo.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(refNo);

			SSMTextField applyLlpName = new SSMTextField("applyLlpName");
			applyLlpName.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(applyLlpName);

			SSMTextField llpNo = new SSMTextField("llpNo");
			llpNo.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(llpNo);

			SSMDateTextField resultDate = new SSMDateTextField("resultDate");
			resultDate.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(resultDate);

			SSMDateTextField expNameDate = new SSMDateTextField("expNameDate");
			expNameDate.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(expNameDate);

//			SSMDropDownChoice purposeApply = new SSMDropDownChoice("purposeApply", Parameter.LLP_REG_PURPOSE);
//			purposeApply.add(new AttributeModifier("readonly", new Model("readonly")));
//			;
//			add(purposeApply);

			SSMTextArea extraExplanation = new SSMTextArea("extraExplanation");
			add(extraExplanation);
			
			SSMTextField regType = new SSMTextField("regType");
			regType.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(regType);

//			SSMTextField llpCompanyType = new SSMTextField("llpCompanyType");
//			llpCompanyType.add(new AttributeModifier("readonly", new Model("readonly")));
//			;
//			add(llpCompanyType);

			//if (Parameter.LLP_REG_PURPOSE_prof_body.equals(purposeApply.getValue())) {

//				SSMDropDownChoice profBodyCode = new SSMDropDownChoice("profBodyCode", Parameter.PROF_BODY_TYPE);
//				if (llpReservedName.getRefNo() != null) {
//					profBodyCode.add(new AttributeModifier("readonly", new Model("readonly")));
//					;
//				}
//				profBodyCode.setVisible(true);
//				profBodyCode.setRequired(true);
//				add(profBodyCode);
//				
//				SSMTextField profAuthOrg = new SSMTextField("profAuthOrg");
//				profAuthOrg.add(new AttributeModifier("readonly", new Model("readonly")));
//				profAuthOrg.setVisible(true);
//				profAuthOrg.setRequired(true);
//				add(profAuthOrg);
//
//				SSMTextField profAuthLetterRefNo = new SSMTextField("profAuthLetterRefNo");
//				profAuthLetterRefNo.add(StringValidator.maximumLength(255));
//				profAuthLetterRefNo.add(new AttributeModifier("readonly", new Model("readonly")));
//				profAuthLetterRefNo.setVisible(true);
//				profAuthLetterRefNo.setRequired(true);
//				add(profAuthLetterRefNo);
//
//				SSMDateTextField profAuthLetterDate = new SSMDateTextField("profAuthLetterDate");
//				profAuthLetterDate.add(new AttributeModifier("readonly", new Model("readonly")));
//				profAuthLetterDate.setVisible(true);
//				profAuthLetterDate.setRequired(true);
//				add(profAuthLetterDate);
//
//				SSMTextField profLetterPurpose = new SSMTextField("profLetterPurpose");
//				profLetterPurpose.add(StringValidator.maximumLength(255));
//				profLetterPurpose.add(new AttributeModifier("readonly", new Model("readonly")));
//				profLetterPurpose.setVisible(true);
//				profLetterPurpose.setRequired(true);
//				add(profLetterPurpose);
//
//				SSMTextField profLetterSign = new SSMTextField("profLetterSign");
//				profLetterSign.add(StringValidator.maximumLength(255));
//				profLetterSign.add(new AttributeModifier("readonly", new Model("readonly")));
//				profLetterSign.setVisible(true);
//				profLetterSign.setRequired(true);
//				add(profLetterSign);
//
//				SSMTextArea profRemark = new SSMTextArea("profRemark");
//				profRemark.add(StringValidator.maximumLength(255));
//				profRemark.add(new AttributeModifier("readonly", new Model("readonly")));
//				profRemark.setVisible(true);
//				add(profRemark);
			//} else {
				SSMDropDownChoice profBodyType = new SSMDropDownChoice("profBodyType", Parameter.PROF_BODY_TYPE);
				if (llpReservedName.getRefNo() != null) {
					profBodyType.add(new AttributeModifier("readonly", new Model("readonly")));
					;
				}
				profBodyType.setVisible(false);
				add(profBodyType);
				
				SSMTextField profAuthOrg = new SSMTextField("profAuthOrg");
				profAuthOrg.setVisible(false);
				profAuthOrg.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profAuthOrg);

				SSMTextField profAuthLetterRefNo = new SSMTextField("profAuthLetterRefNo");
				profAuthLetterRefNo.add(StringValidator.maximumLength(255));
				profAuthLetterRefNo.setVisible(false);
				profAuthLetterRefNo.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profAuthLetterRefNo);

				SSMDateTextField profAuthLetterDate = new SSMDateTextField("profAuthLetterDate");
				profAuthLetterDate.setVisible(false);
				profAuthLetterDate.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profAuthLetterDate);

				SSMTextField profLetterPurpose = new SSMTextField("profLetterPurpose");
				profLetterPurpose.add(StringValidator.maximumLength(255));
				profLetterPurpose.setVisible(false);
				profLetterPurpose.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profLetterPurpose);

				SSMTextField profLetterSign = new SSMTextField("profLetterSign");
				profLetterSign.add(StringValidator.maximumLength(255));
				profLetterSign.setVisible(false);
				profLetterSign.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profLetterSign);

				SSMTextArea profRemark = new SSMTextArea("profRemark");
				profRemark.add(StringValidator.maximumLength(255));
				profRemark.setVisible(false);
				profRemark.add(new AttributeModifier("readonly", new Model("readonly")));
				;
				add(profRemark);
			//}

			SSMTextField lodgeBy = new SSMTextField("lodgeBy");
			lodgeBy.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			add(lodgeBy);

			SSMTextArea officerRemark = new SSMTextArea("officerRemark");
			officerRemark.add(new AttributeModifier("readonly", new Model("readonly")));
			;
			if (llpReservedName.getRefNo() != null) {
				officerRemark.setVisible(true);
			} else {
				officerRemark.setVisible(false);
			}
			add(officerRemark);

			add(new Button("ok") {
				public void onSubmit() {
					setResponsePage(ViewListLlpReservedNames.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}

}
