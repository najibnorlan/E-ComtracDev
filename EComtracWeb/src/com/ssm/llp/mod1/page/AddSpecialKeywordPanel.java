package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.ssm.llp.base.page.SecBasePanel;

@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
public class AddSpecialKeywordPanel extends SecBasePanel{
	
	public AddSpecialKeywordPanel(String id) {
		super(id);
		add(new AddSKForm("addSKForm"));
	}
	
	private class AddSKForm extends Form {
		
		private String addSKCode;
		
		public AddSKForm(String id) {
			super(id);
			add(new BookmarkablePageLink("addSKCode", EditSpecialKeywordPage.class));

			setMarkupId("add-form");
		}
		
		
	}

}
