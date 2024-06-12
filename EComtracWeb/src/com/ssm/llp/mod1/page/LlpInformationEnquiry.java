package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LlpInformationEnquiry extends SecBasePanel {

	public LlpInformationEnquiry(String id) {
		super(id);
		add(new LlpInfoForm("llpInfoForm"));
	}

	private class LlpInfoForm extends Form {
		private String SearchByLlpName;
		private String SearchByLlpNo;

		public LlpInfoForm(String id) {
			super(id);

			/*
			 * TextField LlpName = new SSMTextField("SearchByLlpName", new
			 * PropertyModel(this, "SearchByLlpName")); add(LlpName);
			 */

			SSMTextField LlpNo = new SSMTextField("SearchByLlpNo", new PropertyModel(this, "SearchByLlpNo"));
			add(LlpNo);

			setMarkupId("search-form");
		}

		public void onSubmit() {
			PageParameters params = new PageParameters();
			if (getSearchByLlpName() == null) {
				params.add("SearchByLlpName", "");
			} else {
				params.add("SearchByLlpName", getSearchByLlpName());
			}
			if (getSearchByLlpNo() == null) {
				params.add("SearchByLlpNo", "");
			} else {
				params.add("SearchByLlpNo", getSearchByLlpNo());
			}
			setResponsePage(ListLlpInformationEnquiry.class, params);

		}

		public String getSearchByLlpName() {
			return SearchByLlpName;
		}

		public void setSearchByLlpName(String searchByLlpName) {
			SearchByLlpName = searchByLlpName;
		}

		public String getSearchByLlpNo() {
			return SearchByLlpNo;
		}

		public void setSearchByLlpNo(String searchByLlpNo) {
			SearchByLlpNo = searchByLlpNo;
		}

	}

}
