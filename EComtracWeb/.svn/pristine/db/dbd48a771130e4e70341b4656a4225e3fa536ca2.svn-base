package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.service.ContactService;

/**
 * Page used to create and edit contacts.
 * 
 * <p>
 * User: Nick Heudecker
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditContact extends BasePage  {

	public EditContact() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new Contact();
			}
		}));
		init();
	}

	public EditContact(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(ContactService.class.getSimpleName()).findById(refNo);
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

			SSMTextField refNo = new SSMTextField("refNo");
			refNo.add(new AttributeModifier("readonly", new Model("readonly")));
			add(refNo);

			SSMTextField firstName = new SSMTextField("firstName");
			firstName.setRequired(true);
			firstName.add(StringValidator.maximumLength(15));
			add(firstName);

			SSMTextField lastName = new SSMTextField("lastName");
			lastName.setRequired(true);
			lastName.setLabelKey("contact.page.lastName");
			lastName.add(StringValidator.maximumLength(20));
			add(lastName);

			SSMTextField email = new SSMTextField("email");
			email.add(StringValidator.maximumLength(150));
			email.add(EmailAddressValidator.getInstance());
			email.setVisible(false);
			add(email);

			TextArea notes = new TextArea("notes");
			notes.add(StringValidator.maximumLength(500));
			add(notes);

			SSMDropDownChoice group = new SSMDropDownChoice("group", Parameter.CONTACT_GROUP);
			add(group);

			add(new Button("save") {
				public void onSubmit() {
					Contact c = (Contact) getForm().getModelObject();

					if (StringUtils.isBlank(c.getRefNo())) {
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
			add(new Button("pay") {
				public void onSubmit() {
					Contact c = (Contact) getForm().getModelObject();

//					List<LlpPaymentTransactionDetail> paymentItems = new ArrayList<LlpPaymentTransactionDetail>();
//
//					LlpPaymentTransactionDetail paymentItem1 = new LlpPaymentTransactionDetail();
//					paymentItem1.setPaymentItem("A");
//					paymentItem1.setQuantity(1);
//					paymentItem1.setAmount(100);
//
//					paymentItems.add(paymentItem1);
//
//					LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
//					paymentItem2.setPaymentItem("B");
//					paymentItem2.setQuantity(1);
//					paymentItem2.setAmount(200);
//
//					paymentItems.add(paymentItem2);
//
//					setResponsePage(new PaymentDetailPage(EditContact.class, c, paymentItems));
				}
			});
		}
	}

//	@Override
//	public void sucessPayment(Object obj, String paymentTransId) {
//		if (obj != null) {
//			Contact c = (Contact) obj;
//
//			if (c.getId() == 0) {
//				getService(ContactService.class.getSimpleName()).insert(c);
//			} else {
//				getService(ContactService.class.getSimpleName()).update(c);
//			}
//		}
//		setResponsePage(ListContacts.class);
//	}

	@Override
	public String getPageTitle() {
		return null;
	}
}
