package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.service.ContactService;

public class ViewContactPage extends SecBasePage {

	public ViewContactPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new Contact();
			}
		}));
		init();
	}

	public ViewContactPage(final Long contactId) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(ContactService.class.getSimpleName()).findById(contactId);
			}
		}));
		init();
	}

	private void init() {
		add(new ContactForm("form", getDefaultModel()));
	}

	private class ContactForm extends Form {

		public ContactForm(String id, IModel m) {
			super(id, m);

			SSMTextField firstName = new SSMTextField("firstName");
			firstName.setRequired(true);
			firstName.add(StringValidator.maximumLength(15));
			add(firstName);

			SSMTextField lastName = new SSMTextField("lastName");
			lastName.setRequired(true);
			lastName.add(StringValidator.maximumLength(20));
			add(lastName);

			SSMTextField email = new SSMTextField("email");
			email.add(StringValidator.maximumLength(150));
			email.add(EmailAddressValidator.getInstance());
			email.setVisible(false);
			email.setUpperCase(false);
			add(email);

			TextArea notes = new TextArea("notes");
			notes.add(StringValidator.maximumLength(500));
			add(notes);

			DropDownChoice group = new DropDownChoice("group");
			group.setChoices(new AbstractReadOnlyModel() {
				public Object getObject() {
					List<String> l = new ArrayList<String>(3);
					l.add("Friend");
					l.add("Co-Worker");
					l.add("Nemesis");
					return l;
				}
			});
			add(group);

			add(new Button("save") {
				public void onSubmit() {
					Contact c = (Contact) getForm().getModelObject();

					if (c.getId() == 0) {
						getService(ContactService.class.getSimpleName()).insert(c);
					} else {
						getService(ContactService.class.getSimpleName()).update(c);
					}
					setResponsePage(ListContacts.class);
				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListContacts.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}
}
