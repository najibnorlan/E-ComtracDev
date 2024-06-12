package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;

/**
 * <p>
 * User: Nick Heudecker
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SearchPartnerCriteriaPanel extends Panel {

	public SearchPartnerCriteriaPanel(String id, Page parentPage, ModalWindow window) {
		super(id);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchPartnerCriteriaPanelModel model = new SearchPartnerCriteriaPanelModel();
				return model;
			}
		}));

		add(new SearchForm("searchFormPartner", getDefaultModel(), parentPage, window));
		// add(new FeedbackPanel("feedback"));
	}

	public final class SearchPartnerCriteriaPanelModel implements Serializable {
		private String idNo;
		private String idType;
		private String namePortion;

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

		public String getIdType() {
			return idType;
		}

		public void setIdType(String idType) {
			this.idType = idType;
		}

		public String getNamePortion() {
			return namePortion;
		}

		public void setNamePortion(String namePortion) {
			this.namePortion = namePortion;
		}

	}

	public final class SearchForm extends Form implements Serializable {

		private Page parentPage;
		private ModalWindow popUpWindow;

		public SearchForm(String id, IModel<?> iModel, final Page parentPage, final ModalWindow popUpWindow) {
			super(id, iModel);
			this.parentPage = parentPage;
			this.popUpWindow = popUpWindow;

			SSMDropDownChoice idTypeDropDown = new SSMDropDownChoice("idType", Parameter.ID_TYPE);
			idTypeDropDown.setRequired(true);
			idTypeDropDown.setLabelKey("llpReg.page.partner.idType");
			add(idTypeDropDown);

			TextField idNoTf = new TextField("idNo");
			idNoTf.setLabelKey("llpReg.page.partner.idNo"); 
			idNoTf.setRequired(true);
			add(idNoTf);

			TextField namePortionTf = new TextField("namePortion");
			namePortionTf.setLabelKey("llpReg.page.partner.name");
			namePortionTf.setRequired(true);
			namePortionTf.add(StringValidator.minimumLength(3));
			add(namePortionTf);

		}

		@Override
		protected void onSubmit() {

			SearchPartnerCriteriaPanelModel model = (SearchPartnerCriteriaPanelModel) getDefaultModelObject();
			PageParameters params = new PageParameters();
			params.add("idType", model.getIdType());
			params.add("idNo", model.getIdNo());
			params.add("namePortion", model.getNamePortion());
			
			if (Parameter.ID_TYPE_newic.equals(model.getIdType()) && (model.getIdNo().length() != 12)) {
				ssmError(SSMExceptionParam.USER_PROFILE_IC_WRONG_FORMAT);
				return;
			}

			setResponsePage(new SearchPartnerPage(parentPage, popUpWindow, params));
		}

	}

}
