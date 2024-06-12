package com.ssm.llp.mod1.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class RegisterLlpUserProfilePage extends BasePage {

	//1 digit, 1 lower, 1 upper, from 6 to 20
	private final String PASSWORD_PATTERN  = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;// =
														// (LlpUserProfileService)
														// WicketApplication.getServiceNew(LlpUserProfileService.class.getSimpleName());
	
	@SpringBean(name = "mailService")
	MailService mailService;
	
	public RegisterLlpUserProfilePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpUserProfile();
			}
		}));
		init();
	}

	public RegisterLlpUserProfilePage(final String userRefNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpUserProfileService.class.getSimpleName()).findById(userRefNo);
			}
		}));
		init();
	}

	public RegisterLlpUserProfilePage(String idType, String idNo, String name) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpUserProfile();
			}
		}));

		((LlpUserProfile) getDefaultModel().getObject()).setIdType(idType);
		((LlpUserProfile) getDefaultModel().getObject()).setIdNo(idNo);
		((LlpUserProfile) getDefaultModel().getObject()).setName(name);
		((LlpUserProfile) getDefaultModel().getObject()).setUserType(Parameter.USER_TYPE_complianceOfficer);
		((LlpUserProfile) getDefaultModel().getObject()).setCountry(Parameter.COUNTRY_CODE_malaysia);
		
//		if(Parameter.ID_TYPE_newic.equals(idType)&& (!(StringUtils.isBlank(idNo)))){
//			
//			String year=idNo.substring(0,2);
//			String month=idNo.substring(2,4);
//			String dd=idNo.substring(4,6);
//
//			int yearDOB = Integer.parseInt(year) + 1900;
//			
//			String dateOfBirth = dd+"/"+month+"/"+yearDOB;
//		
//			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		    Date convertedDate = null;
//			try {
//				convertedDate = dateFormat.parse(dateOfBirth);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			((LlpUserProfile) getDefaultModel().getObject()).setDob(convertedDate);
//		}
		init();

		// TODO Auto-generated constructor stub
	}

	private void init() {
		add(new RegisterLlpUserProfileForm("form", getDefaultModel()));
	}

	private class RegisterLlpUserProfileForm extends Form {
		private SSMDropDownChoice state;

		public RegisterLlpUserProfileForm(String id, IModel m) {
			super(id, m);
			setAutoCompleteForm(false);
			final LlpUserProfile llpUserProfileNew = (LlpUserProfile) m.getObject();

			SSMTextField userRefNo = new SSMTextField("userRefNo");
			userRefNo.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
				userRefNo.setVisible(false);
			} else {
				userRefNo.setVisible(true);
				userRefNo.add(new AttributeModifier("readonly", new Model("readonly")));
			}
			add(userRefNo);

			SSMTextField loginId = new SSMTextField("loginId");
			loginId.setRequired(true);
			loginId.add(StringValidator.maximumLength(50));
			loginId.add(new UsernameValidator());
			loginId.setLabelKey("user.page.loginId");
			add(loginId);

			SSMPasswordTextField userPwd = new SSMPasswordTextField("userPwd");
			userPwd.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getLoginId())) {
				userPwd.setVisible(true);
			} else {
				userPwd.setVisible(false);
			}
			userPwd.setLabelKey("user.page.userPassword");
//			userPwd.setLabel(Model.of("user.page.userPassword"));
			userPwd.add(new PatternValidator(PASSWORD_PATTERN));
			add(userPwd);

			SSMPasswordTextField reconfirmPassword = new SSMPasswordTextField("reconfirmPassword");
			reconfirmPassword.add(StringValidator.maximumLength(50));
			if (StringUtils.isBlank(llpUserProfileNew.getLoginId())) {
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
//			if (llpUserProfileNew.getUserRefNo() != null) {
//				email.add(new AttributeModifier("readonly", new Model("readonly")));
//			}
			add(email);

			
			SSMTextField reTypeEmail = new SSMTextField("reTypeEmail");
			reTypeEmail.setRequired(true);
			reTypeEmail.setUpperCase(false);
			reTypeEmail.add(StringValidator.maximumLength(50));
			reTypeEmail.add(EmailAddressValidator.getInstance());
			reTypeEmail.setLabelKey("user.page.userRetypeEmail");
//			if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
//				reTypeEmail.setVisible(true);
//			} else {
//				reTypeEmail.setVisible(false);
//			}
			add(reTypeEmail);
			
			
			SSMTextField add1 = new SSMTextField("add1");
			add1.add(StringValidator.maximumLength(150));
			add1.setRequired(true);
			add1.setLabelKey("user.page.userAddress");
			add(add1);

			SSMTextField add2 = new SSMTextField("add2");
			add2.add(StringValidator.maximumLength(150));
			//add2.setRequired(true);
			add(add2);

			SSMTextField add3 = new SSMTextField("add3");
			//add3.setRequired(true);
			add3.add(StringValidator.maximumLength(150));
			add(add3);

//			final SSMDropDownChoice bizMainAddrState = new SSMDropDownChoice("bizMainAddrState", Parameter.CBS_ROB_STATE);
			state = new SSMDropDownChoice("state", Parameter.STATE_CODE, Parameter.COUNTRY_CODE_malaysia);
			state.setRequired(true);
			state.setLabelKey("user.page.userState");
//			state.setReadOnly(true);
			add(state);
			
//			if(StringUtils.isBlank(llpUserProfileNew.getCountry())){
//				llpUserProfileNew.setCountry(Parameter.COUNTRY_CODE_malaysia);
//			}
			
			String countryDesc = getCodeTypeWithValue(Parameter.COUNTRY_CODE, Parameter.COUNTRY_CODE_malaysia);
			SSMTextField country = new SSMTextField("country", new PropertyModel(countryDesc, ""),true);
			country.setLabelKey("user.page.userCountry");
			country.setReadOnly(true);
			add(country);

			SSMTextField city = new SSMTextField("city");
			city.setRequired(true);
			city.add(StringValidator.maximumLength(150));
			city.setLabelKey("user.page.userCity");
			add(city);

			SSMTextField postcode = new SSMTextField("postcode");
			postcode.setRequired(true);
			postcode.add(StringValidator.maximumLength(10));
			postcode.add(new NumberValidator());
			postcode.setLabelKey("user.page.userPostcode");
			add(postcode);

			SSMTextField hpNo = new SSMTextField("hpNo");
			hpNo.add(StringValidator.maximumLength(15));
			hpNo.setRequired(true);
			hpNo.add(new NumberValidator());
			hpNo.setLabelKey("user.page.hpNo");
			add(hpNo);

			SSMTextField faxNo = new SSMTextField("faxNo");
			faxNo.add(StringValidator.maximumLength(15));//
			faxNo.setLabelKey("user.page.faxNo");
			faxNo.add(new NumberValidator());
			add(faxNo);

			SSMTextField name = new SSMTextField("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(100));
			name.setLabelKey("user.page.userName");
			add(name);

			
			String idTypeDesc = getCodeTypeWithValue(Parameter.ID_TYPE_FOR_CMP_OFFICER, llpUserProfileNew.getIdType());
			SSMTextField idType = new SSMTextField("idType",new PropertyModel(idTypeDesc, ""));
			idType.setReadOnly(true);
			add(idType);
			

			SSMDropDownChoice icColour = new SSMDropDownChoice("icColour", Parameter.NRIC_COLOUR);
			icColour.setRequired(true);
			icColour.setLabelKey("user.page.userIdColour");
			add(icColour);
			
			SSMDropDownChoice nationality = new SSMDropDownChoice("nationality", Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER);
			nationality.setRequired(true);
			nationality.setLabelKey("user.page.userNationality");
			add(nationality);

			SSMTextField idNo = new SSMTextField("idNo");
			idNo.setRequired(true);
			idNo.add(StringValidator.maximumLength(20));
			idNo.add(new AttributeModifier("readonly", new Model("readonly")));
			idNo.setLabelKey("user.page.userIdNo");
			add(idNo);

			SSMDateTextField idExpiredDt = new SSMDateTextField("idExpiredDt");
			idExpiredDt.setLabelKey("user.page.userExpiredDate");
			add(idExpiredDt);

			SSMRadioChoice gender = new SSMRadioChoice("gender", Parameter.GENDER);
			gender.setLabelKey("user.page.userGender");
			gender.setRequired(true);
			add(gender);


			final SSMTextField othersRace = new SSMTextField("othersRace");
			othersRace.setVisible(false);
			othersRace.setLabelKey("user.page.othersRace");
			othersRace.setOutputMarkupId(true);
			othersRace.setOutputMarkupPlaceholderTag(true);
			add(othersRace);
			
			final SSMDropDownChoice race = new SSMDropDownChoice("race", Parameter.RACE);
			race.setLabelKey("user.page.userRace");
			race.setRequired(true);
			add(race);
			
			AjaxFormComponentUpdatingBehavior isRaceOnChange = new AjaxFormComponentUpdatingBehavior("onchange") {
	            protected void onUpdate(AjaxRequestTarget target) {
//	            	System.out.println(race.getDefaultModelObject());
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
			
			SSMDateTextField dob = new SSMDateTextField("dob");
			dob.setRequired(true);
			dob.setLabelKey("user.page.userDateOfBirth");
			add(dob);

			SSMRadioChoice userStatus = new SSMRadioChoice("userStatus", Parameter.USER_STATUS);
			userStatus.setLabelKey("user.page.userStatus");
			if (StringUtils.isBlank(llpUserProfileNew.getLoginId())) {
				userStatus.setEnabled(false);
			} else {
				userStatus.setEnabled(true);
			}
			add(userStatus);

			SSMTextField ipAddress = new SSMTextField("ipAddress");
			ipAddress.setLabelKey("user.page.userIpAddress");
			ipAddress.add(StringValidator.maximumLength(12));
			if (StringUtils.isBlank(llpUserProfileNew.getLoginId())) {
				ipAddress.setVisible(false);
			} else {
				ipAddress.setVisible(true);
			}
			add(ipAddress);

			TextArea remarks = new TextArea("remarks");
			remarks.setLabelKey("user.page.userRemarks");
			remarks.add(StringValidator.maximumLength(255));
			if (StringUtils.isBlank(llpUserProfileNew.getLoginId())) {
				remarks.setVisible(false);
			} else {
				remarks.setVisible(true);
			}
			add(remarks);

			final Button save =new Button("save") {
				public void onSubmit() {
					LlpUserProfile llpUserProfile = (LlpUserProfile) getForm().getModelObject();
					if(!"O".equals(llpUserProfile.getRace())){
						llpUserProfile.setOthersRace("");
					}
					llpUserProfile.setIdType(llpUserProfileNew.getIdType());
					llpUserProfile.setIdNo(llpUserProfileNew.getIdNo());
					llpUserProfile.setUserType(Parameter.USER_TYPE_complianceOfficer);
					if (StringUtils.isBlank(llpUserProfile.getUserRefNo())) {
						llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
						try {
							llpUserProfileService.insertNewLlpUserProfile(llpUserProfile);
							mailService.sendMail(llpUserProfile.getEmail(), "email.notification.user.register.subject", llpUserProfile.getUserRefNo(),"email.notification.user.register.body",llpUserProfile.getName(),llpUserProfile.getUserRefNo(),llpUserProfile.getLoginId());
						} catch (Exception e) {
							e.printStackTrace();
							ssmError(e.getMessage());
							return;
						}
					} else {
						try {
							llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
							llpUserProfileService.updateLlpUserProfile(llpUserProfile, llpUserProfileNew);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ssmError(e.getMessage());
							return;
						}
					}
					
					//String msg = "Your Registration already Done. For ID Activation, Please Come To the nearest SSM's office.Thank You";
					setResponsePage(new SuccessLlpUserProfilePage(SSMExceptionParam.USER_PROFILE_SUCCESS_REGISTER, llpUserProfile.getLoginId(), llpUserProfile.getEmail() ));
					//setResponsePage(new SuccessLlpUserProfilePage());
				}
			};
			save.setLabelKey("user.page.userSave");
			save.setEnabled(false);
			add(save);
			
			Button cancel = new Button("cancel") {
				public void onSubmit() {
					if (StringUtils.isBlank(llpUserProfileNew.getUserRefNo())) {
						setResponsePage(HomePage.class);
					} else {
						setResponsePage(ListLlpUserProfilePage.class);
					}
				}
			}.setDefaultFormProcessing(false);
			cancel.setLabelKey("user.page.userCancel");
			add(cancel);
			
			
			AjaxCheckBox declareChk= new AjaxCheckBox("declareChk", new PropertyModel(llpUserProfileNew, "declareChk")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						save.setEnabled(true);
					} else {
						save.setEnabled(false);
					}
					
					if(save.isVisible())
						target.add(save);
				}
			};
			add(declareChk);
//			
//			PageParameters portalTermParams = new PageParameters();
//			portalTermParams.set("fileCode", "PORTAL_TERM_CONDITION");
//			BookmarkablePageLink portalTermLink = new BookmarkablePageLink("portalTermLink", DownloadPage.class,  portalTermParams);
//			add(portalTermLink);
//			
//			PageParameters protocolLodgementParams = new PageParameters();
//			protocolLodgementParams.set("fileCode", "PROTOCOL_ELODGEMENT");
//			BookmarkablePageLink protocalElodgementLink = new BookmarkablePageLink("protocalElodgementLink", DownloadPage.class,  protocolLodgementParams);
//			add(protocalElodgementLink);
			
			
			SSMDownloadLink portalTermLink = new SSMDownloadLink("portalTermLink", "PORTAL_TERM_CONDITION");
			add(portalTermLink);
			
			SSMDownloadLink protocalElodgementLink = new SSMDownloadLink("protocalElodgementLink", "PROTOCOL_ELODGEMENT");
			add(protocalElodgementLink);
			
		}
		
		@Override
		protected void onError() {
			super.onError();
			LlpUserProfile llpUserProfile = (LlpUserProfile) getDefaultModelObject();
			llpUserProfile.setCountry(Parameter.COUNTRY_CODE_malaysia);
//			llpUserProfile.setBussinessCountryCode(Parameter.COUNTRY_CODE_malaysia);
		}
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}