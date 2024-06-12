package com.ssm.llp.mod1.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
public class SearchSpecialKeyword extends SecBasePanel{
	
	public SearchSpecialKeyword(String id, PageParameters params){
		super(id);
		final String searchString = params.get("searchString") != null ? params.get("searchString").toString() : null;
		final String vchkeywords = params.get("vchkeywords") != null ? params.get("vchkeywords").toString() : null;
		System.out.println("vchkeywords");
		add(new SpecialKeywordForm("specialKeywordForm", searchString));
		
		
	}
	
	private class SpecialKeywordForm extends Form{

		private String searchString;
		
		public SpecialKeywordForm(String id, String searchString) {
			super(id);
			this.searchString = searchString;

			SSMTextField tf = new SSMTextField("searchString",  new PropertyModel(this, "searchString"));
			add(tf);
			tf.add(StringValidator.maximumLength(100));
			tf.setRequired(true);
			
		}
		public void onSubmit() {
			PageParameters params = new PageParameters();
			if (StringUtils.isNotBlank(getSearchString())) {
				params.add("searchString", getSearchString());
			}
			
			setResponsePage(ListLlpSpecialKeyword.class, params);
		}
		
		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}
		
		
	}

}
