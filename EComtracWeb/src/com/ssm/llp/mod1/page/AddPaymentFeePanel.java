package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;


public class AddPaymentFeePanel extends SecBasePanel {
	
	public AddPaymentFeePanel(String id) {
		super(id);
		add(new AddForm("addForm"));
	}
	
	private class AddForm extends Form {
		
		private String addPaymentCode;
		
		public AddForm(String id) {
			super(id);
			add(new BookmarkablePageLink("addPaymentCode", AddPaymentCode.class));

//			SSMTextField tf = new SSMTextField("AddPaymentCode", new PropertyModel(this, "AddPaymentCode"));
//			add(tf);
			// tf.setRequired(true);
			setMarkupId("add-form");
		}
		
		
	}

}
