package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EditLlpUserProfilePage extends SecBasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	

	public EditLlpUserProfilePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpUserProfile();
			}
		}));
		init();
	}

	public EditLlpUserProfilePage(final String userRefNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpUserProfileService.class.getSimpleName()).findById(userRefNo);
			}
		}));
		init();
	}

	public EditLlpUserProfilePage(final String userRefNo, String msg) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpUserProfileService.class.getSimpleName()).findById(userRefNo);
			}
		}));
		ssmSuccess(SSMExceptionParam.USER_PROFILE_SUCCESS_EDIT, userRefNo);
		init();
	}

	private void init() {
		add(new EditLlpUserProfileForm("form", getDefaultModel()));
	}

	private class EditLlpUserProfileForm extends Form implements Serializable {

		private SSMDropDownChoice country;
		private SSMDropDownChoice state;
		
		private SSMDropDownChoice bussinessCountryCode;
		private SSMDropDownChoice bussinessStateCode;

		public EditLlpUserProfileForm(String id, IModel m) {
			super(id, m);

			final LlpUserProfile llpUserProfileNew = (LlpUserProfile) m.getObject();

			TextField userRefNo = new SSMTextField("userRefNo");
			userRefNo.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				userRefNo.setVisible(false);
			} else {
				userRefNo.setVisible(true);
			}
			userRefNo.setLabelKey("user.page.userRefNo");
			add(userRefNo);

			SSMTextField loginId = new SSMTextField("loginId");
			// loginId.setRequired(true);
			loginId.add(StringValidator.maximumLength(50));
			loginId.add(new UsernameValidator());
			if (llpUserProfileNew.getUserRefNo() != null) {
				loginId.setReadOnly(true);
			}
			loginId.setLabelKey("user.page.loginId");
			add(loginId);

			SSMPasswordTextField userPwd = new SSMPasswordTextField("userPwd");
			userPwd.setRequired(true);
			userPwd.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				userPwd.setVisible(true);
			} else {
				userPwd.setVisible(false);
			}
			userPwd.setLabelKey("user.page.userPassword");
			add(userPwd);

			SSMPasswordTextField reconfirmPassword = new SSMPasswordTextField("reconfirmPassword");
			reconfirmPassword.setRequired(true);
			reconfirmPassword.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				reconfirmPassword.setVisible(true);
			} else {
				reconfirmPassword.setVisible(false);
			}
			reconfirmPassword.setLabelKey("user.page.userReconfirmPassword");
			add(reconfirmPassword);

			SSMTextField email = new SSMTextField("email");
			email.setRequired(true);
			email.add(StringValidator.maximumLength(50));
			email.add(EmailAddressValidator.getInstance());
			email.setUpperCase(false);
			email.setLabelKey("user.page.userEmail");
			add(email);

			SSMDropDownChoice icColour = new SSMDropDownChoice("icColour", Parameter.NRIC_COLOUR);
			icColour.setRequired(true);
			icColour.setLabelKey("user.page.userIdColour");
			add(icColour);
			
			SSMDropDownChoice nationality = new SSMDropDownChoice("nationality", Parameter.NATIONALITY_TYPE);
			nationality.setRequired(true);
			nationality.setLabelKey("user.page.userNationality");
			add(nationality);

			TextField add1 = new SSMTextField("add1");
			add1.add(StringValidator.maximumLength(150));
			add1.setRequired(true);
			add1.setLabelKey("user.page.userAddress");
			add(add1);

			TextField add2 = new SSMTextField("add2");
			add2.add(StringValidator.maximumLength(150));
			add(add2);

			TextField add3 = new SSMTextField("add3");
			add3.add(StringValidator.maximumLength(150));
			add(add3);

			state = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			state.setRequired(true);
			state.setLabelKey("user.page.userState");
			add(state);

			llpUserProfileNew.setCountry(Parameter.COUNTRY_CODE_malaysia);
			country = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE, state);
			country.setRequired(true);
			country.setReadOnly(true);
			country.setLabelKey("user.page.userCountry");
			add(country);

			TextField city = new SSMTextField("city");
			city.setRequired(true);
			city.add(StringValidator.maximumLength(150));
			city.setLabelKey("user.page.userCity");
			add(city);

			TextField postcode = new SSMTextField("postcode");
			postcode.setRequired(true);
			postcode.add(StringValidator.maximumLength(10));
			postcode.add(new NumberValidator());
			postcode.setLabelKey("user.page.userPostcode");
			add(postcode);

			TextField hpNo = new SSMTextField("hpNo");
			hpNo.add(StringValidator.maximumLength(15));
			hpNo.setRequired(true);
			hpNo.add(new NumberValidator());
			hpNo.setLabelKey("user.page.hpNo");
			add(hpNo);

//			TextField bussinessAdd1 = new SSMTextField("bussinessAdd1");
//			bussinessAdd1.add(StringValidator.maximumLength(150));
//			bussinessAdd1.setLabelKey("user.page.userBusinessAddress");
//			bussinessAdd1.setRequired(true);
//			add(bussinessAdd1);
//
//			TextField bussinessAdd2 = new SSMTextField("bussinessAdd2");
//			bussinessAdd2.add(StringValidator.maximumLength(150));
//			add(bussinessAdd2);
//
//			TextField bussinessAdd3 = new SSMTextField("bussinessAdd3");
//			bussinessAdd3.add(StringValidator.maximumLength(150));
//			add(bussinessAdd3);

//			bussinessStateCode = new SSMDropDownChoice("bussinessStateCode", Parameter.STATE_CODE);
//			bussinessStateCode.setLabelKey("user.page.userBusinessState");
//			bussinessStateCode.setRequired(true);
//			add(bussinessStateCode);
//
//			llpUserProfileNew.setBussinessCountryCode(Parameter.COUNTRY_CODE_malaysia);
//			bussinessCountryCode = new SSMDropDownChoice("bussinessCountryCode", Parameter.COUNTRY_CODE, bussinessStateCode);
//			bussinessCountryCode.setLabelKey("user.page.userBusinessCountry");
//			bussinessCountryCode.setRequired(true);
//			bussinessCountryCode.setReadOnly(true);
//			add(bussinessCountryCode);
//
//			TextField bussinessCity = new SSMTextField("bussinessCity");
//			bussinessCity.add(StringValidator.maximumLength(150));
//			bussinessCity.setLabelKey("user.page.userBusinessCity");
//			bussinessCity.setRequired(true);
//			add(bussinessCity);
//
//			TextField bussinessPostcode = new SSMTextField("bussinessPostcode");
//			bussinessPostcode.add(StringValidator.maximumLength(10));
//			bussinessPostcode.add(new NumberValidator());
//			bussinessPostcode.setLabelKey("user.page.userBusinessPostcode");
//			bussinessPostcode.setRequired(true);
//			add(bussinessPostcode);

//			TextField offNo = new SSMTextField("offNo");
//			offNo.add(StringValidator.maximumLength(15));
//			offNo.setRequired(true);
//			offNo.add(new NumberValidator());
//			offNo.setLabelKey("user.page.ofNo");
//			add(offNo);
//
//			TextField faxNo = new SSMTextField("faxNo");
//			faxNo.add(StringValidator.maximumLength(15));
//			faxNo.add(new NumberValidator());
//			faxNo.setLabelKey("user.page.faxNo");
//			add(faxNo);

//			SSMDropDownChoice licenseMbrType = new SSMDropDownChoice("licenseMbrType", Parameter.PROF_BODY_CODE);
//			licenseMbrType.populateOptionsByParentCode(Parameter.PROF_BODY_TYPE_cs);
//			licenseMbrType.setLabelKey("user.page.userLicenseMemberType");
//			add(licenseMbrType);
//
//			TextField licenseMbrNo = new SSMTextField("licenseMbrNo");
//			licenseMbrNo.add(StringValidator.maximumLength(20));
//			licenseMbrNo.setLabelKey("user.page.userLicenseMemberNo");
//			add(licenseMbrNo);

			TextField name = new SSMTextField("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(100));
			name.setLabelKey("user.page.userName");
			add(name);

			SSMDropDownChoice idType = new SSMDropDownChoice("idType", Parameter.ID_TYPE);
			idType.setLabelKey("user.page.userIdType");
			idType.setRequired(true);
			add(idType);

			TextField idNo = new SSMTextField("idNo");
			idNo.setRequired(true);
			idNo.add(StringValidator.maximumLength(20));
			idNo.setLabelKey("user.page.userIdNo");
			add(idNo);

			SSMDateTextField idExpiredDt = new SSMDateTextField("idExpiredDt");
			idExpiredDt.setLabelKey("user.page.userExpiredDate");
			add(idExpiredDt);

			SSMRadioChoice gender = new SSMRadioChoice("gender", Parameter.GENDER);
			gender.setLabelKey("user.page.userGender");
			add(gender);

			
			final SSMTextField othersRace = new SSMTextField("othersRace");
			othersRace.setVisible(false);
			othersRace.setLabelKey("user.page.othersRace");
			othersRace.setOutputMarkupId(true);
			othersRace.setOutputMarkupPlaceholderTag(true);
			add(othersRace);
			if("O".equals(llpUserProfileNew.getRace())){
				othersRace.setVisible(true);
			}
			
			final SSMDropDownChoice race = new SSMDropDownChoice("race", Parameter.RACE);
			race.setLabelKey("user.page.userRace");
			add(race);
			
			AjaxFormComponentUpdatingBehavior isRaceOnChange = new AjaxFormComponentUpdatingBehavior("onchange") {
	            protected void onUpdate(AjaxRequestTarget target) {
	            	System.out.println(race.getDefaultModelObject());
	            	if("O".equals(race.getDefaultModelObject())){
						othersRace.setRequired(true);
						othersRace.setVisible(true);
					}else{
						othersRace.setDefaultModelObject("");
						llpUserProfileNew.setOthersRace("");
						othersRace.setRequired(false);
						othersRace.setVisible(false);
					}
	            	target.add(othersRace);
	            }
	        };
			race.add(isRaceOnChange);

			/*SSMDropDownChoice userType = new SSMDropDownChoice("userType", Parameter.USER_TYPE);
			userType.setLabelKey("user.page.userType");
			userType.setRequired(true);
			add(userType);*/

			SSMDateTextField dob = new SSMDateTextField("dob");
			dob.setLabelKey("user.page.userDateOfBirth");
			add(dob);

			SSMRadioChoice userStatus = new SSMRadioChoice("userStatus", Parameter.USER_STATUS);
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				userStatus.setEnabled(false);
			} else {
				userStatus.setEnabled(true);
			}
			userStatus.setLabelKey("user.page.userStatus");
			userStatus.setOutputMarkupId(true);
			add(userStatus);

			// TextField ipAddress = new SSMTextField("ipAddress");
			// ipAddress.add(StringValidator.maximumLength(12));
			// if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
			// ipAddress.setVisible(false);
			// } else {
			// ipAddress.setVisible(true);
			// }
			// add(ipAddress);

			TextArea remarks = new TextArea("remarks");
			remarks.add(StringValidator.maximumLength(255));
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				remarks.setVisible(false);
			} else {
				remarks.setVisible(true);
			}
			remarks.setLabelKey("user.page.userRemarks");
			remarks.setOutputMarkupId(true);
			add(remarks);

			String turnOffUserStatus = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_TURN_OFF_USER_STATUS_UPDATE);
			if(Parameter.YES_NO_yes.equals(turnOffUserStatus)){
				remarks.setVisible(false);
			}
			
			add(new Button("save") {
				public void onSubmit() {
					LlpUserProfile llpUserProfile = (LlpUserProfile) getForm().getModelObject();
					
					if(!"O".equals(llpUserProfile.getRace())){
						llpUserProfile.setOthersRace("");
					}
					if (StringUtils.isBlank(llpUserProfile.getUserRefNo())) {
						llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
						try {
							llpUserProfileService.insertNewLlpUserProfile(llpUserProfile);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ssmError(e.getMessage());
							return;
						}
					} else {
						try {
							String turnOffUserStatus = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_TURN_OFF_USER_STATUS_UPDATE);
							if(Parameter.YES_NO_yes.equals(turnOffUserStatus)){
								llpUserProfile.setUserStatus(llpUserProfileNew.getUserStatus());
							}
							llpUserProfileService.updateLlpUserProfile(llpUserProfile, llpUserProfileNew);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ssmError(e.getMessage());
							return;
						}
					}

					String msg = null;
					setResponsePage(new EditLlpUserProfilePage(llpUserProfile.getUserRefNo(), msg));
				}
			});

			add(new Button("cancel") {
				public void onSubmit() {
					if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
						setResponsePage(HomePage.class);
					} else {
						setResponsePage(ListLlpUserProfilePage.class);
					}
				}
			}.setDefaultFormProcessing(false));

			Button print = new Button("print") {
				public void onSubmit() {
					LlpUserProfile llpUserProfile = (LlpUserProfile) getForm().getModelObject();
					setResponsePage(new LlpUserProfileEmailNotification(llpUserProfile));
				}
			};
			add(print);
			print.setVisible(true); // ada

			if (!(Parameter.USER_STATUS_active.equals(llpUserProfileNew.getUserStatus()))) {
				print.setVisible(false);
			}

		}

		@Override
		protected void onError() {
			super.onError();
			LlpUserProfile llpUserProfile = (LlpUserProfile) getDefaultModelObject();
			llpUserProfile.setCountry(Parameter.COUNTRY_CODE_malaysia);
			state.populateOptionsByParentCode(country.getInput());
			llpUserProfile.setBussinessCountryCode(Parameter.COUNTRY_CODE_malaysia);
			// bussinessStateCode.populateOptionsByParentCode(bussinessCountryCode.getInput());
		}
	}
}