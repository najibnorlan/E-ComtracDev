package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.parser.node.GetExecutor;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;
import com.ssm.llp.mod1.service.impl.NameSearchResult;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class ListLlpReservedNames extends SecBasePage {
	@SpringBean(name = "LlpReservedNameService")
	private LlpReservedNameService llpReservedNameService;

	public ListLlpReservedNames(PageParameters params, String searchResult) {
		// super(searchResult);
		final String regType = params.get("regType") != null ? params.get("regType").toString() : null;
		final String profBodyType = params.get("profBodyType") != null ? params.get("profBodyType").toString() : null;
		final String conversionType = params.get("conversionType") != null ? params.get("conversionType").toString() : null;
		final String searchString = params.get("searchString") != null ? params.get("searchString").toString() : null;
		final String isProceedToLLP = params.get("isProceedToLLP") != null ? params.get("isProceedToLLP").toString() : null;
		
		add(new SearchLlpName("searchLlpName", searchString, regType, profBodyType, conversionType, isProceedToLLP));
		
		boolean validateNamePass = true;
		boolean hasError = false;
		if (StringUtils.isNotBlank(searchString)) {
			List<SSMException> list =	llpReservedNameService.validateNameSearch(searchString);
			for (int i = 0; i < list.size(); i++) {
				ssmError(list.get(i));
			}
			if(list.size()>0 ){
				hasError = true;
			}
			
			if(hasError && !UserEnvironmentHelper.isInternalUser()){
				validateNamePass = false;
			}
		}
		
		String cleanName = llpReservedNameService.cleanName(searchString);
		SearchCriteria sc = new SearchCriteria("nameSearch", SearchCriteria.LIKE, cleanName + "%");

		SSMSortableDataProvider dp = new SSMSortableDataProvider("applyLlpName", sc, LlpReservedNameService.class);
		final SSMDataView<LlpReservedName> dataView = new SSMDataView<LlpReservedName>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpReservedName> item) {
				LlpReservedName llpReservedName = item.getModelObject();

				//item.add(new SSMLabel("idReservedName", String.valueOf(llpReservedName.getIdReservedName())));
				item.add(new SSMLabel("refNo", llpReservedName.getRefNo()));
				item.add(new SSMLabel("applyLlpName", llpReservedName.getApplyLlpName()));
				item.add(new SSMLabel("llpNo", llpReservedName.getLlpNo()));
				//item.add(new SSMLabel("resultDate", llpReservedName.getResultDate() ));
				//item.add(new SSMLabel("expNameDate", llpReservedName.getExpNameDate() ));
				//item.add(new SSMLabel("status", llpReservedName.getStatus(), Parameter.APP_STATUS_CODE));

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		dataView.setItemsPerPage(20L);

		// add(new OrderByBorder("orderByResultRef", "refNo", dp) {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// protected void onSortChanged() {
		// dataView.setCurrentPage(0);
		// }
		// });

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpReservedNameFormModel(searchString, regType, profBodyType, conversionType, isProceedToLLP);
			}
		}));

		LlpReservedNameForm form = new LlpReservedNameForm("form", getDefaultModel(), hasError);
		add(form);
		form.setVisibilityAllowed(validateNamePass);
		
//		String resultName2 = resultName.replace(",", "");
//		if ("R".equals(resultStatus) || "E".equals(resultStatus) || "O".equals(resultStatus)
//				|| ("G".equals(resultStatus) && searchResult.replace(" ", "").equals(resultName2.replace(" ", "")))) {
//			form.setVisibilityAllowed(false);
//		}

	}

	private class LlpReservedNameFormModel implements Serializable {
		private String applyLlpName;
		private String regType;
		private String profBodyType;
		private String conversionType;
		private String isProceedToLLP;
		private boolean declareChk;

		public LlpReservedNameFormModel(String appLlpName, String regType, String profBodyType, String conversionType, String isProceedToLLP) {
			this.applyLlpName = appLlpName;
			this.regType = regType;
			this.profBodyType = profBodyType;
			this.conversionType = conversionType;
			this.isProceedToLLP = isProceedToLLP;
		}

		public String getApplyLlpName() {
			return applyLlpName;
		}

		public void setApplyLlpName(String applyLlpName) {
			this.applyLlpName = applyLlpName;
		}

		public String getRegType() {
			return regType;
		}

		public void setRegType(String regType) {
			this.regType = regType;
		}

		public String getProfBodyType() {
			return profBodyType;
		}

		public void setProfBodyType(String profBodyType) {
			this.profBodyType = profBodyType;
		}

		public String getIsProceedToLLP() {
			return isProceedToLLP;
		}

		public void setIsProceedToLLP(String isProceedToLLP) {
			this.isProceedToLLP = isProceedToLLP;
		}

		public String getConversionType() {
			return conversionType;
		}

		public void setConversionType(String conversionType) {
			this.conversionType = conversionType;
		}

		public boolean isDeclareChk() {
			return declareChk;
		}

		public void setDeclareChk(boolean declareChk) {
			this.declareChk = declareChk;
		}

	}

	private class LlpReservedNameForm extends Form {
		public LlpReservedNameForm(String id, IModel m, boolean hasError) {
			super(id, m);

			final SSMTextField applyLlpNameField = new SSMTextField("applyLlpName");
			applyLlpNameField.setRequired(true);
			applyLlpNameField.add(StringValidator.maximumLength(100));
			add(applyLlpNameField);

			final HiddenField regTypeField = new HiddenField("regType");
			add(regTypeField);

			final HiddenField profBodyTypeField = new HiddenField("profBodyType");
			add(profBodyTypeField);

			final HiddenField conversionTypeField = new HiddenField("conversionType");
			add(conversionTypeField);

			final HiddenField isProceedToLLPField = new HiddenField("isProceedToLLP");
			add(isProceedToLLPField);

			final Button reserve = new Button("reserve") {
				public void onSubmit() {
					PageParameters param = new PageParameters();

					param.add("applyLlpName", applyLlpNameField.getValue());
					param.add("regType", regTypeField.getValue());
					param.add("profBodyType", profBodyTypeField.getValue());
					param.add("conversionType", conversionTypeField.getValue());
					param.add("isProceedToLLP", isProceedToLLPField.getValue());
					setResponsePage(new EditLlpReservedNamePage(param));

				}
			};
			
			if(hasError){
				String msjConfirmProceed = resolve("reservedName.page.proceed.with.error");
				AttributeModifier alert=new AttributeModifier( "onclick", "return confirm('"+msjConfirmProceed+"');");
				reserve.add(alert);
			}
			
			add(reserve);
			reserve.setVisible(true);
			if (isProceedToLLPField.getValue().equalsIgnoreCase(Parameter.YES_NO_yes)) {
				reserve.setVisible(false);
			}


			final Button proceedName = new Button("proceedName") {
				public void onSubmit() {

					PageParameters param = new PageParameters();

					param.add("applyLlpName", applyLlpNameField.getValue());
					param.add("regType", regTypeField.getValue());
					param.add("profBodyType", profBodyTypeField.getValue());
					param.add("conversionType", conversionTypeField.getValue());
					param.add("isProceedToLLP", isProceedToLLPField.getValue());
					setResponsePage(new EditLlpReservedNamePage(param));

				}

			};
			add(proceedName);
			proceedName.setVisible(false);
			if (isProceedToLLPField.getValue().equalsIgnoreCase(Parameter.YES_NO_yes)) {
				proceedName.setVisible(true);
			}
			
			SSMDownloadLink guidelinesLink = new SSMDownloadLink("guidelinesLink", "RESERVE_NAME_GUIDELINE");
			add(guidelinesLink);
			if(!guidelinesLink.isHasFile()){
				guidelinesLink.setVisible(false);
			}else{
				reserve.setEnabled(false);
				proceedName.setEnabled(false);
			}
			
			AjaxCheckBox declareChk= new AjaxCheckBox("declareChk") {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						reserve.setEnabled(true);
						proceedName.setEnabled(true);
					} else {
						reserve.setEnabled(false);
						proceedName.setEnabled(false);
					}
					
					if(proceedName.isVisible()){
						target.add(proceedName);
					}
					if(reserve.isVisible()){
						target.add(reserve);
					}
				}
			};
			add(declareChk);
		}
	}

	public void sortingPage(List<LlpReservedName> listResult) {

	}

	private void getErrorMsg(String resultStatus, String resultName) {

		if ("O".equals(resultStatus)) {
			ssmError("reservedName.page.offensiveWord", resultName);
		}
		if ("E".equals(resultStatus)) {
			ssmError("reservedName.page.registeredName", resultName);
		}
		if ("R".equals(resultStatus)) {
			ssmError("reservedName.page.reservedName", resultName);
		}
		if ("G".equals(resultStatus)) {
			ssmError("reservedName.page.gazetteName", resultName);
		}
		if ("C".equals(resultStatus)) {
			ssmError("reservedName.page.controlWord", resultName);
		}
	}

}
