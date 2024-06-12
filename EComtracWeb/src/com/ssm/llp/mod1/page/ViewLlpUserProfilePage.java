package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.EditLlpUserProfilePasswordPage.EditPasswordForm;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ViewLlpUserProfilePage extends SecBasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;
	
	public ViewLlpUserProfilePage() {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceNew(LlpUserProfileService.class.getSimpleName());
				try {
					return llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return llpUserProfileService;
			}
		}));

		init();
	}

	private void init() {
		add(new ViewLlpUserProfileForm("form", getDefaultModel()));
	}

	private class ViewLlpUserProfileForm extends Form {
		private SSMDropDownChoice country;
		private SSMDropDownChoice state;
		
		public ViewLlpUserProfileForm(String id, IModel m) {
			super(id, m);

			final LlpUserProfile llpUserProfileNew = (LlpUserProfile) m.getObject();
			
			add(new SSMLabel("userRefNo", llpUserProfileNew.getUserRefNo()));
			add(new SSMLabel("loginId", llpUserProfileNew.getLoginId()));
			add(new SSMLabel("email", llpUserProfileNew.getEmail()));
			add(new SSMLabel("name", llpUserProfileNew.getName()));
			add(new SSMLabel("idType", llpUserProfileNew.getIdType(),Parameter.ID_TYPE));
			add(new SSMLabel("idNo" , llpUserProfileNew.getIdNo()));
			add(new SSMLabel("icColour" , llpUserProfileNew.getIcColour(),Parameter.NRIC_COLOUR));
			add(new SSMLabel("nationality" , llpUserProfileNew.getNationality(), Parameter.NATIONALITY_TYPE));
			add(new SSMLabel("gender" , llpUserProfileNew.getGender(), Parameter.GENDER));
			add(new SSMLabel("dob" , llpUserProfileNew.getDob()));
			add(new SSMLabel("race" , llpUserProfileNew.getRace(),Parameter.RACE));
			
			TextField add1 = new SSMTextField("add1");
			add1.setRequired(true);
			add1.add(StringValidator.maximumLength(150));
			add(add1);

			TextField add2 = new SSMTextField("add2");
			add2.add(StringValidator.maximumLength(150));
			//add2.setRequired(true);
			add(add2);

			TextField add3 = new SSMTextField("add3");
			add3.add(StringValidator.maximumLength(150));
			add(add3);

			state = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			state.setRequired(true);
			add(state);

			country = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE, state);
			country.setRequired(true);
			add(country);

			TextField city = new SSMTextField("city");
			city.setRequired(true);
			city.add(StringValidator.maximumLength(150));
			add(city);

			TextField postcode = new SSMTextField("postcode");
			postcode.setRequired(true);
			postcode.add(StringValidator.maximumLength(10));
			add(postcode);

			TextField hpNo = new SSMTextField("hpNo");
			hpNo.setRequired(true);
			hpNo.add(StringValidator.maximumLength(15));
			add(hpNo);
			
			add(new Button("save") {
				public void onSubmit() {
					LlpUserProfile model = (LlpUserProfile) getForm().getModelObject();

					llpUserProfileService.update(model);
					
				}
			});
		}
	}
		
	public String getPageTitle() {
		String titleKey = "page.title.userprofile.view";
		return titleKey;
	}
}