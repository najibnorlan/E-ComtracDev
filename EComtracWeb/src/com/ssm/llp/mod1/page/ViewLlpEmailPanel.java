package com.ssm.llp.mod1.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.model.LlpEmailLog;
import com.ssm.llp.base.common.service.LlpEmailLogService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.service.LlpReservedNameService;

public class ViewLlpEmailPanel extends SecBasePanel{
	@SpringBean(name = "MailService")
	private MailService mailService;
	
	public ViewLlpEmailPanel(String id, PageParameters param) {
		super(id);
		final Long emailId = Long.valueOf(param.get("emailId").toString());
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {				
				return (LlpEmailLog)getService(LlpEmailLogService.class.getSimpleName()).findById(emailId);
			}
		}));
		
		init();				
	}
	
	private class EmailLogForm extends Form {

		public EmailLogForm(String id, IModel m) {
			super(id,m);
			
			final LlpEmailLog llpEmailLog = (LlpEmailLog) m.getObject();
			
			if(llpEmailLog != null){				
				final SSMTextField tfEmailFrom = new SSMTextField("emailFrom");
				tfEmailFrom.setUpperCase(false);
				tfEmailFrom.setLabelKey("listLlpEmailLog.page.emailFrom");
				add(tfEmailFrom);
				
				final SSMTextField tfEmailTo = new SSMTextField("emailTo");
				tfEmailTo.setUpperCase(false);
				tfEmailTo.setLabelKey("listLlpEmailLog.page.emailTo");
				add(tfEmailTo);
				
				final SSMTextField tfEmailSubject = new SSMTextField("emailSubject");
				tfEmailSubject.setUpperCase(false);
				tfEmailSubject.setLabelKey("listLlpEmailLog.page.emailSubject");
				add(tfEmailSubject);
				
				final SSMTextArea taEmailBody = new SSMTextArea("emailBody");
				taEmailBody.setUpperCase(false);
				taEmailBody.setLabelKey("listLlpEmailLog.page.emailBody");
				add(taEmailBody);
				
				
				
				add(new AjaxButton("resendBtn") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						LlpEmailLog llpEmailLogForm = (LlpEmailLog)form.getDefaultModel().getObject();
						mailService.sendMail(llpEmailLogForm.getEmailTo(), llpEmailLogForm.getEmailSubject(), llpEmailLogForm.getRefNo(), llpEmailLogForm.getEmailBody(), null);
						ssmSuccess("listLlpEmailLog.page.send.email.success");
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						ssmError("listLlpEmailLog.page.send.email.error");
					}

				});
			}
		}
		
	}
	
	public void init(){
		add(new FeedbackPanel("feedback"));
		add(new EmailLogForm("emailLogForm", getDefaultModel()));
	}
}
