package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.HomePage;

public class SuccessLlpUserProfilePage extends BasePage {

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public SuccessLlpUserProfilePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				newLlpUserProfileForm newLlpUserProfileForm = new newLlpUserProfileForm();
				return newLlpUserProfileForm;
			}
		}));
		add(new SuccessLlpUserProfileForm("successForm", getDefaultModel()));
	}

	public SuccessLlpUserProfilePage(final String msg) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				newLlpUserProfileForm newLlpUserProfileForm = new newLlpUserProfileForm();
				newLlpUserProfileForm.setMessage(msg);
				return newLlpUserProfileForm;
			}
		}));
		add(new SuccessLlpUserProfileForm("successForm", getDefaultModel()));
//		success(msg);
		ssmSuccess(msg);

	}
	
	public SuccessLlpUserProfilePage(final String msg, String... param) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				newLlpUserProfileForm newLlpUserProfileForm = new newLlpUserProfileForm();
				newLlpUserProfileForm.setMessage(msg);
				return newLlpUserProfileForm;
			}
		}));
		add(new SuccessLlpUserProfileForm("successForm", getDefaultModel()));
//		success(msg);
		ssmSuccess(msg, param);

	}

	private final class SuccessLlpUserProfileForm extends Form implements Serializable {

		public SuccessLlpUserProfileForm(String id, IModel m) {
			super(id, m);

			// add(new SSMLabel("message"));

//			add(new Button("ok") {
//				public void onSubmit() {
//					setResponsePage(HomePage.class);
//				}
//			}.setDefaultFormProcessing(false));
		}
	}

	public final class newLlpUserProfileForm implements Serializable {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
