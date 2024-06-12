package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class EditLlpUserProfilePasswordPage extends SecBasePage {
	
	private final String PASSWORD_PATTERN  = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	public EditLlpUserProfilePasswordPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				EditPasswordForm editPasswordForm = new EditPasswordForm();
				return editPasswordForm;
			}
		}));
		add(new EditLlpUserProfilePasswordForm("passwordForm", getDefaultModel()));
	}

	public final class EditPasswordForm implements Serializable {
		private String password;
		private String newPassword1;
		private String newPassword2;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getNewPassword1() {
			return newPassword1;
		}

		public void setNewPassword1(String newPassword1) {
			this.newPassword1 = newPassword1;
		}

		public String getNewPassword2() {
			return newPassword2;
		}

		public void setNewPassword2(String newPassword2) {
			this.newPassword2 = newPassword2;
		}

	}

	private final class EditLlpUserProfilePasswordForm extends Form implements Serializable {

		LlpUserProfile llpUserProfileNew = new LlpUserProfile();

		public EditLlpUserProfilePasswordForm(String id, IModel m) {
			super(id, m);
			// llpUserProfileNew = (LlpUserProfile) m.getObject();

			WebMarkupContainer changePasswordInfo = new WebMarkupContainer("changePasswordInfo");
			changePasswordInfo.setOutputMarkupId(true);
			changePasswordInfo.setVisible(false);
			add(changePasswordInfo);
			
			LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			if(Parameter.YES_NO_yes.equals(profile.getIsTempPassword())){
				changePasswordInfo.setVisible(true);
			}
			
			SSMPasswordTextField userPwd = new SSMPasswordTextField("password");
			userPwd.setRequired(true);
			userPwd.add(StringValidator.maximumLength(50));
			userPwd.setLabelKey("user.page.userCurrentPassword");
			add(userPwd);

			SSMPasswordTextField newPassword1 = new SSMPasswordTextField("newPassword1");
			newPassword1.setRequired(true);
			//newPassword1.add(StringValidator.maximumLength(50));
			newPassword1.add(new PatternValidator(PASSWORD_PATTERN));
			newPassword1.setLabelKey("user.page.userNewPassword");
			add(newPassword1);

			SSMPasswordTextField newPassword2 = new SSMPasswordTextField("newPassword2");
			newPassword2.setRequired(true);
			newPassword2.add(StringValidator.maximumLength(50));
			newPassword2.setLabelKey("user.page.userConfirmPassword");
			add(newPassword2);

			add(new Button("save") {
				public void onSubmit() {
					EditPasswordForm editPasswordForm = (EditPasswordForm) getForm().getModelObject();

					try {
						llpUserProfileService.changeUserPasswordByPublic(UserEnvironmentHelper.getLoginName(), editPasswordForm.getPassword(),
								editPasswordForm.getNewPassword1(), editPasswordForm.getNewPassword2());
					    success("Password Already Changed.");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ssmError(e.getMessage());
						return;
					}
					setResponsePage(new SuccessLlpUserProfilePage(SSMExceptionParam.USER_PROFILE_EDIT_PASSWORD));
				}
			});

			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(AfterLoginLlp.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.userprofile.password";
		return titleKey;
	}
}