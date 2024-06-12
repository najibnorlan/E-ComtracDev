package com.ssm.ezbiz.healthCheck;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.ezbiz.service.RobHealthCheckService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMNumberTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobHealthCheck;

@SuppressWarnings({"all"})
public class EditHealthCheckPage extends SecBasePage {

	public EditHealthCheckPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new RobHealthCheck();
			}
		}));
		add(new EditHealthCheckPageForm("form", getDefaultModel()));
	}
	
	public EditHealthCheckPage(final int id) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(RobHealthCheckService.class.getSimpleName()).findById(id); 
			}
		}));
		add(new EditHealthCheckPageForm("form", getDefaultModel()));
	}
	
	private class EditHealthCheckPageForm extends Form implements Serializable {
		public EditHealthCheckPageForm(String id, IModel m) {
			super(id, m);
			
			SSMTextField code = new SSMTextField("code");
			code.setRequired(true);
			code.setUpperCase(true);
			add(code);
			
			SSMNumberTextField time = new SSMNumberTextField("time");
			time.setRequired(true);
			time.setMinimum(1);
			time.setType(Integer.class);
			add(time);
			
			Button btnSave = new Button("btnSave") {
				public void onSubmit() {
					RobHealthCheck robHealthCheck = (RobHealthCheck) getForm().getModelObject();
					
					if(robHealthCheck.getId() == 0) {
						robHealthCheck.setStatus("FAIL");
						getService(RobHealthCheckService.class.getSimpleName()).insert(robHealthCheck);
					} else {
						getService(RobHealthCheckService.class.getSimpleName()).update(robHealthCheck);
					}
					
					setResponsePage(HealthCheckPage.class);
				}
			};
			add(btnSave);
			
			Button btnCancel = new Button("btnCancel") {
				public void onSubmit() {
					setResponsePage(HealthCheckPage.class);
				}
			}.setDefaultFormProcessing(false);
			add(btnCancel);
			
		}
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.EditHealthCheckPage";
	}
	
}
