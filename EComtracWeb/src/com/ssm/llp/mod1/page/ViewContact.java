package com.ssm.llp.mod1.page;

import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.service.ContactService;

/**
 * <p>
 * User: Nick Heudecker
 * </p>
 * <p>
 * Date: Jan 3, 2008
 * </p>
 * <p>
 * Time: 11:09:48 AM
 * </p>
 */
public class ViewContact extends SecBasePage {

	public ViewContact(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			public Object load() {
				return getService(ContactService.class.getSimpleName()).findById(refNo);
			}
		}));
		add(new SSMLabel("refNo"));
		add(new SSMLabel("firstName"));
		add(new SSMLabel("lastName"));
		add(new SmartLinkLabel("email"));
		add(new SSMLabel("notes"));
		add(new SSMLabel("group"));

		add(new Link("edit", getDefaultModel()) {
			public void onClick() {
				Contact c = (Contact) getModelObject();
				setResponsePage(new EditContact(c.getRefNo()));
			}
		});
		add(new Link("delete", getDefaultModel()) {
			public void onClick() {
				Contact c = (Contact) getModelObject();
				getService(ContactService.class.getSimpleName()).delete(c);
				setResponsePage(ListContacts.class);
			}
		});
	}

}
