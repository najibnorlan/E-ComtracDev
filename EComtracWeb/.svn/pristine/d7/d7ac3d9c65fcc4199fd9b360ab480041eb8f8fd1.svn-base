package com.ssm.ezbiz.robUserActivationTray;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.ssm.ezbiz.robUserActivationTray.SearchRobUserActivationTrayPage.SearchForm;
import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

public class ProcessLockAlert extends Panel{
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;

	private WebMarkupContainer wmcAlert;
	private SSMLabel titleLbl;
	private SSMLabel msgLbl;
	private SearchForm searchForm;
	
	public ProcessLockAlert(String id, SearchForm searchForm) {
		super(id);
		
		this.searchForm=searchForm;
		
		wmcAlert = new WebMarkupContainer("wmcAlert");
		wmcAlert.setOutputMarkupId(true);
		wmcAlert.setOutputMarkupPlaceholderTag(true);
		add(wmcAlert);
		
		titleLbl = new SSMLabel("title", resolve("page.lbl.pcert.editSecretaryPcert.processAlertTitle"));
		msgLbl = new SSMLabel("msg", "msg");
		wmcAlert.add(titleLbl);
		wmcAlert.add(msgLbl);
		
		
		
		SSMAjaxLink lockAndEditButton = new SSMAjaxLink("lockAndEditButton") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				
			}
		};
		wmcAlert.add(lockAndEditButton);
		
		SSMAjaxLink unlock = new SSMAjaxLink("unlock") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				
			}
		};
		wmcAlert.add(unlock);
		
		SSMAjaxLink viewOnly = new SSMAjaxLink("viewOnly") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				
			}
		};
		wmcAlert.add(viewOnly);
		
		
	}
	
	
	public String getWmcAlertId() {
		return wmcAlert.getMarkupId();
	}

	public void resetAlert(String title, String msg, Panel addInfoPanel, AjaxRequestTarget target) {
		titleLbl.setDefaultModelObject(title);
		msgLbl.setDefaultModelObject(msg);
		wmcAlert.replace(addInfoPanel);
		
		target.add(wmcAlert);
	}

	public void resetAlert(final RobUserActivationTray robUserActivationTray,AjaxRequestTarget target) {
		
		String msg = "";
		
		
		SSMAjaxLink lockAndEditButton = new SSMAjaxLink("lockAndEditButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				String otherUserLockName = "";
				String loginUser = UserEnvironmentHelper.getLoginName();
				if(StringUtils.isBlank(robUserActivationTray.getLockBy())) {
					try {
						robUserActivationTrayService.lock(robUserActivationTray);
					} catch (HibernateOptimisticLockingFailureException e) {//mean lock by others
						
						RobUserActivationTray robUserActivationTrayLatest = robUserActivationTrayService.findById(robUserActivationTray.getAppRefNo());
						robUserActivationTray.setLockBy(robUserActivationTrayLatest.getLockBy());
						robUserActivationTray.setLockDt(robUserActivationTrayLatest.getLockDt());
						otherUserLockName = robUserActivationTrayLatest.getLockBy();
					}
				}
				if(!robUserActivationTray.getLockBy().equals(loginUser)) {
					otherUserLockName = robUserActivationTray.getLockBy();
				}
				
				if(StringUtils.isBlank(otherUserLockName)) {
					setResponsePage(new ProcessRobUserActivationTrayPage(robUserActivationTray, getPage()));
				}else {
					
					String errorScript = WicketUtils.generateAjaxErrorAlertScript(resolve("page.lbl.ezbiz.robUserActivationTray.errorTitle"), resolve("page.lbl.ezbiz.robUserActivationTray.errorDesc.transHasBeenLockedByOthers",otherUserLockName));
					target.prependJavaScript(errorScript);
					
					searchForm.resetTable(target);
				}
			}
		};
		wmcAlert.replace(lockAndEditButton);
		lockAndEditButton.setVisible(false);
		
		SSMAjaxLink unlock = new SSMAjaxLink("unlock") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				robUserActivationTrayService.unlock(robUserActivationTray);
				setResponsePage(getPage());
			}
		};
		wmcAlert.replace(unlock);
		unlock.setVisible(false);
		
		SSMAjaxLink viewOnly = new SSMAjaxLink("viewOnly") {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				setResponsePage(new ViewRobUserActivationTrayPage(robUserActivationTray , getPage()));
			}
		};
		wmcAlert.replace(viewOnly);
		
		
		String loginUser = UserEnvironmentHelper.getLoginName();
		if(StringUtils.isNotBlank(robUserActivationTray.getLockBy())) {
			if(loginUser.equals(robUserActivationTray.getLockBy())) {
				msg = resolve("page.lbl.pcert.editSecretaryPcert.processLockByYou");
				lockAndEditButton.setVisible(true);
				unlock.setVisible(true);
			}else {
				msg = resolve("page.lbl.pcert.editSecretaryPcert.processLockByOthers", robUserActivationTray.getLockBy());
				lockAndEditButton.setVisible(false);
				unlock.setVisible(false);
			}
		}else {//not locked everyone can take
			msg = resolve("page.lbl.pcert.editSecretaryPcert.processNotLock");
			lockAndEditButton.setVisible(true);
			unlock.setVisible(false);
		}
		
		
		msgLbl.setDefaultModelObject(msg);
		target.add(wmcAlert);
	}
	
}
