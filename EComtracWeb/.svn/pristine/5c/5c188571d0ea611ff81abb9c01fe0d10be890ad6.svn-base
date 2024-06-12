package com.ssm.llp.mod1.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.service.ContactService;

/**
 * <p>
 * User: Nick Heudecker
 * </p>
 */
public class ListContacts extends ContactBasePage {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ListContacts(PageParameters params) {
		StringValue searchString = params.get("searchString");// .toString();
		String strParam = "";
		if (searchString != null && searchString.toString() != null) {
			strParam = searchString.toString();
		}
		// IModel contactsModel = new LoadableDetachableModel() {
		// protected Object load() {
		// return
		// getService(ContactService.class.getSimpleName()).findByCriteria(new
		// SearchCriteria("firstName","LIKE",searchString+"%")).getList();
		// }
		// };

		SearchCriteria sc = new SearchCriteria("firstName", "LIKE", strParam + "%");
		SSMSortableDataProvider dp = new SSMSortableDataProvider("firstName", sc, ContactService.class);
		final SSMDataView<Contact> dataView = new SSMDataView<Contact>("sorting", dp) {

			protected void populateItem(final Item<Contact> item) {
				Contact contact = item.getModelObject();
				// item.add(new ActionPanel("actions", item.getModel()));
				item.add(new SSMLabel("contactid", String.valueOf(contact.getId())));
				item.add(new SSMLabel("refNo", String.valueOf(contact.getRefNo())));
				item.add(new SSMLabel("firstname", contact.getFirstName()));
				item.add(new SSMLabel("lastname", contact.getLastName()));

				item.add(new SmartLinkLabel("email", contact.getEmail()));

				item.add(new SSMLabel("group", contact.getGroup(), Parameter.CONTACT_GROUP));

				item.add(new SSMLink("edit", item.getDefaultModel()) {
					public void onClick() {
						Contact c = (Contact) getModelObject();
						setResponsePage(new EditContact(c.getRefNo()));
					}
				});
				item.add(new SSMLink("delete", item.getDefaultModel(), true) {
					public void onClick() {
						Contact c = (Contact) getModelObject();
						getService(ContactService.class.getSimpleName()).delete(c);
						setResponsePage(ListContacts.class);
					}
				});

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		add(new OrderByBorder("orderByFirstName", "firstName", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByLastName", "lastName", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	public void sortingPage(List<Contact> listResult) {

	}

}
