package com.ssm.llp.mod1.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;

public class SearchLlpRegistration extends SecBasePanel {

	public SearchLlpRegistration(String id) {
		super(id);
		add(new SearchForm("searchForm"));
	}

	private class SearchForm extends Form {

		private String searchString;
		private String llpNo;

		public SearchForm(String id) {
			super(id);

			SSMTextField tf = new SSMTextField("searchString", new PropertyModel(this, "searchString"));
			add(tf);
			SSMTextField llpNo = new SSMTextField("llpNo", new PropertyModel(this, "llpNo"));
			add(llpNo);
			// tf.setRequired(true);
			setMarkupId("search-form");
		}

		public void onSubmit() {
			PageParameters params = new PageParameters();
			if (StringUtils.isNotBlank(getSearchString())) {
				params.add("searchString", getSearchString());
			}
			if (StringUtils.isNotBlank(getLlpNo())) {
				params.add("llpNo", getLlpNo());
			}
			
			setResponsePage(ListLlpRegistration.class, params);
		}

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}

		public String getLlpNo() {
			return llpNo;
		}

		public void setLlpNo(String llpNo) {
			this.llpNo = llpNo;
		}
	}

}
