package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.controller.token.TokenDataModel;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateLlpUserProfilePage extends BasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	public GenerateLlpUserProfilePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				GeneratePasswordForm generatePasswordForm = new GeneratePasswordForm();
				return generatePasswordForm;
			}
		}));
		add(new GeneratePasswordLLPUserProfileForm("generateForm", getDefaultModel()));
	}

	public final class GeneratePasswordForm implements Serializable {
		private String loginId;
		private String idNo;

		// private String email;

		public String getLoginId() {
			return loginId;
		}

		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

	}

	private final class GeneratePasswordLLPUserProfileForm extends Form implements Serializable {

		public GeneratePasswordLLPUserProfileForm(String id, IModel m) {
			super(id, m);
			GeneratePasswordForm passwordForm = (GeneratePasswordForm) m.getObject();
			
			SSMTextField loginId = new SSMTextField("loginId");
			loginId.setLabelKey("user.page.loginId");
			add(loginId);
			
			SSMTextField idNo = new SSMTextField("idNo");
			idNo.setLabelKey("user.page.idNo");
			add(idNo);
			
			TokenDataModel tdm = (TokenDataModel)getSession().getAttribute(ExtInterface.EXT_TOKEN_DATA_MODEL);
			if(tdm!=null) {
				passwordForm.setIdNo(tdm.getNric());
				idNo.setReadOnly(true);
				loginId.setVisible(false);
			}
			
			add(new Button("save") {
				public void onSubmit() {
					LlpUserProfile llpUserProfile = null;
					GeneratePasswordForm editPasswordForm = (GeneratePasswordForm) getForm().getModelObject();
					try {
						llpUserProfile = llpUserProfileService.generateForgetpassword(editPasswordForm.getLoginId(), editPasswordForm.getIdNo());
					} catch (SSMException e) {
						e.printStackTrace();
						ssmError(e);
						return;
					}
//					String msg = "Your New Password already generea. Please check your Email. Thank You.";
//					setResponsePage(new SuccessLlpUserProfilePage(msg));
					//String msg = "Your Password already updated.Thank You.";
					String email = llpUserProfile.getEmail();
					String maskEmail = "";
			        Matcher m = Pattern.compile("\\w{3}(.*)@").matcher(email);
			        if (m.find()) {
			            String s = m.group(1);
			            char[] c = new char[s.length()];
			            Arrays.fill(c, '*');
			            maskEmail = email.replace(s, String.valueOf(c));
			        }
					setResponsePage(new SuccessLlpUserProfilePage(SSMExceptionParam.USER_PROFILE_GENERATE_PASSWORD,maskEmail));
					
				}
			});

			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(HomePage.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}

	private String generatePassword() {
		// TODO Auto-generated method stub
		char[] values1 = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z' };
		char[] values3 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		String out1 = "";
		String out2 = "";

		String password;
		Random rand = new Random();

		for (int i = 0; i < 5; i++) {
			int idx = rand.nextInt(values1.length);
			out1 += values1[idx];
		}

		for (int i = 0; i < 3; i++) {
			int idx = rand.nextInt(values3.length);
			out2 += values3[idx];
		}

		password = out1.concat(out2);
		System.out.println("test" + password);
		return password;

	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}