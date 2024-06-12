package com.ssm.llp.base.page;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.wicket.component.SSMTextArea;

@SuppressWarnings("serial")
public class LlpLocaleMessageFramePage extends BaseFrame {

	
	public LlpLocaleMessageFramePage(final Page parentPage, final ModalWindow window) {
		add(new InputForm("inputForm", parentPage, window));
	}
	
	private class InputForm extends Form {
		private String importTf;
		
		public InputForm(String name, final Page parentPage, final ModalWindow window) {
			super(name);
			
			final SSMTextArea importTf = new SSMTextArea("importTf", new PropertyModel(this, "importTf"));
			importTf.setLabelKey("llpLocale.page.importLocaleMessage");
			importTf.setUpperCase(false);
//			importTf.setRequired(true);
			add(importTf);
			
			add(new AjaxButton("ajaxSubmitImport") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					String propTA = importTf.getInput();
					
					Properties prop = new Properties();
					try {
						prop.load(new StringReader(propTA));
						WicketApplication.getResourceLoader().exportToDb(prop, null);
						ssmSuccess("llpLocale.page.success");
					} catch (IOException e) {
						e.printStackTrace();
						ssmError("llpLocale.page.invalid");
					}
					
					
					FeedbackPanel feedbackPanel =  ((LlpLocaleMessageFramePage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
			
		}

		public String getImportTf() {
			return importTf;
		}

		public void setImportTf(String importTf) {
			this.importTf = importTf;
		}
	}
	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
