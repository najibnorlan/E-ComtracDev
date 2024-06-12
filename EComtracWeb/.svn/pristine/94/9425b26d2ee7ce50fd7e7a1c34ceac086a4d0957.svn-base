package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;

/**
 * <p>
 * User: Nick Heudecker
 * </p>
 */
public class SearchPanel extends SecBasePanel {

	public SearchPanel(String id) {
		super(id);
		add(new SearchForm("searchForm"));
	}

	private class SearchForm extends Form {

		private String searchString;

		public SearchForm(String id) {
			super(id);
			add(new BookmarkablePageLink("addContact", EditContact.class));

			SSMTextField tf = new SSMTextField("searchString", new PropertyModel(this, "searchString"));
			add(tf);
			// tf.setRequired(true);
			setMarkupId("search-form");
		}

		public void onSubmit() {
			PageParameters params = new PageParameters();
			if (getSearchString() == null) {
				params.add("searchString", "");
			} else {
				params.add("searchString", getSearchString());
			}

			setResponsePage(ListContacts.class, params);
		}

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}
	}

}
