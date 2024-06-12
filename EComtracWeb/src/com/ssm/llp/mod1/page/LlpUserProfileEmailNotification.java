package com.ssm.llp.mod1.page;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;


public class LlpUserProfileEmailNotification extends SecBasePage{
	public LlpUserProfileEmailNotification() {
		super();
		
	}
	
	public LlpUserProfileEmailNotification(final LlpUserProfile llpUserProfile) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			public Object load() {
				return getService(LlpUserProfileService.class.getSimpleName()).findById(llpUserProfile.getUserRefNo());
			}
		}));
		add(new SSMLabel("userRefNo"));
		add(new SSMLabel("name"));
		
		// TODO Auto-generated constructor stub
	}




}
