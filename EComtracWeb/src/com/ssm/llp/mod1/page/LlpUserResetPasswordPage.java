package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.MD5DigestUtils;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.UserApprovalPage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({"all"})
public class LlpUserResetPasswordPage extends SecBasePage{
	
	@SpringBean(name="LlpUserProfileService")
	LlpUserProfileService llpUserProfileService; 
	
	public LlpUserResetPasswordPage(final LlpUserProfile llpUserProfile){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				String password = llpUserProfileService.generatePassword();
				llpUserProfile.setUserPwd(password);
				return llpUserProfile;
			}
		}));
		
		
		add(new ResetPasswordForm("form", getDefaultModel()));
	}
	
	public LlpUserResetPasswordPage(final LlpUserProfile llpUserProfile, String msg){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				String password = llpUserProfileService.generatePassword();
				llpUserProfile.setUserPwd(password);
				return llpUserProfile;
			}
		}));
		
		if(StringUtils.isBlank(msg)) {
			ssmSuccess(SSMExceptionParam.USER_PROFILE_SUCCESS_EDIT, llpUserProfile.getUserRefNo());
		}else {
			ssmError(msg);
		}
		
		add(new ResetPasswordForm("form", getDefaultModel()));
	}
	
	public class ResetPasswordForm extends Form implements Serializable{
		public ResetPasswordForm(String id, IModel m){
			super(id, m);
			
			LlpUserProfile llpUserProfile = (LlpUserProfile) m.getObject();
			
			SSMLabel name = new SSMLabel("name");
			add(name);
			
			SSMLabel icNo = new SSMLabel("idNo");
			add(icNo);
			
			SSMLabel email = new SSMLabel("email");
			add(email);
			
			SSMLabel hpNo = new SSMLabel("hpNo");
			add(hpNo);
			
			final SSMTextField userPwd = new SSMTextField("userPwd");
			userPwd.setRequired(true);
			userPwd.setUpperCase(false);
			userPwd.setOutputMarkupId(true);
			add(userPwd);
			
			SSMAjaxLink generatePassword = new SSMAjaxLink("generatePassword") {
				@Override
				public void onClick(AjaxRequestTarget target){
					String password = llpUserProfileService.generatePassword();
					userPwd.setDefaultModelObject(password);
					
					target.add(userPwd);
				}
			};
			
			add(generatePassword);
			
			SSMAjaxButton submit = new SSMAjaxButton("submit") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					LlpUserProfile profile = (LlpUserProfile) form.getDefaultModelObject();
					String msg = null;
					try {
						llpUserProfileService.changeUserPasswordByOfficer(profile.getLoginId(), profile.getUserPwd());
						
					} catch (SSMException e) {
						msg = e.getMessage();
					}
					
					setResponsePage(new LlpUserResetPasswordPage(profile, msg));
				}
			};
			add(submit);
			
			SSMAjaxLink cancel = new SSMAjaxLink("cancel") {
				@Override
				public void onClick(AjaxRequestTarget target){
					setResponsePage(ListLlpUserProfilePage.class);
				}
			};
			add(cancel);
			
		}
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.resetpassword";
	}
	
	
}
