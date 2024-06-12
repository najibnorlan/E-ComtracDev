package com.ssm.ezbiz.robUserActivationTray;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class ViewRobUserActivationTrayPage extends BasePage{
	

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;
	
	
	public ViewRobUserActivationTrayPage(final RobUserActivationTray activationTray) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return activationTray;
			}
		}));
		add(new RobUserActivationTraySubmissionForm("form", getDefaultModel()));
	}

	
	public ViewRobUserActivationTrayPage(final RobUserActivationTray activationTray, Page page) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return activationTray;
			}
		}));
		add(new RobUserActivationTraySubmissionForm("form", getDefaultModel()));
		
	}


	private class RobUserActivationTraySubmissionForm extends Form {

		public RobUserActivationTraySubmissionForm(String id, IModel m) {
			super(id, m);
			setAutoCompleteForm(false);
			setPrefixLabelKey("page.lbl.ezbiz.robUserActivationTray.");
			RobUserActivationTray activationTray = (RobUserActivationTray) m.getObject();
			
			
//			viewPanel
			ViewRobUserActivationTrayPanel viewPanel = new ViewRobUserActivationTrayPanel("viewPanel", activationTray);
			add(viewPanel);
			
			SSMAjaxButton fromPageButton = new SSMAjaxButton("fromPageButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(UserEnvironmentHelper.isInternalUser()) {
						setResponsePage(SearchRobUserActivationTrayPage.class);
					}else {
						setResponsePage(ListRobUserActivationTrayPage.class);
					}
				}
				
			};
			
			add(fromPageButton);
		}
		
	}
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
}