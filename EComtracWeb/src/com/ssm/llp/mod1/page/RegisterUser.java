package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.ssm.llp.base.page.SecBasePanel;

public class RegisterUser extends SecBasePanel {

	public RegisterUser(String id) {
		super(id);
		add(new RegisterForm("registerForm"));
	}

	private class RegisterForm extends Form {

		public RegisterForm(String id) {
			super(id);
			add(new BookmarkablePageLink("newRegistration", RegisterLlpUserProfilePage.class));
			setMarkupId("register-form");
		}
	}
}