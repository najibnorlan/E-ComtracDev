package com.ssm.llp.mod1.page;

import java.io.Serializable;

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
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class VerificationLlpUserProfilePage extends BasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	public VerificationLlpUserProfilePage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new LlpUserProfile();
			}
		}));

		init();

	}
	

	private void init() {
		add(new VerificationLlpUserProfileForm("verificationForm", getDefaultModel()));
	}

	public final class VerificationUserProfileForm implements Serializable {
		private String idType;
		private String idNo;
		private String name;

		public String getIdType() {
			return idType;
		}

		public void setIdType(String idType) {
			this.idType = idType;
		}

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private final class VerificationLlpUserProfileForm extends Form implements Serializable {

		public VerificationLlpUserProfileForm(String id, IModel m) {
			super(id, m);
			LlpUserProfile userProfile = (LlpUserProfile) m.getObject();
			
			
			
			SSMDropDownChoice idType = new SSMDropDownChoice("idType", Parameter.ID_TYPE_FOR_CMP_OFFICER);
			idType.setRequired(true);
			idType.setLabelKey("user.page.userIdType");
			add(idType);

			SSMTextField idNo = new SSMTextField("idNo");
			idNo.setRequired(true);
			idNo.add(StringValidator.maximumLength(20));
			idNo.setLabelKey("user.page.userIdNo");
			add(idNo);
			
			TokenDataModel tdm = (TokenDataModel) getSession().getAttribute(ExtInterface.EXT_TOKEN_DATA_MODEL);
			if(tdm!=null) {
				userProfile.setIdNo(tdm.getNric());
				userProfile.setIdType(Parameter.ID_TYPE_newic);
				idNo.setReadOnly(true);
			}

			TextField name = new SSMTextField("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(100));
			name.setLabelKey("user.page.userFirstName");
			add(name);

			add(new Button("Find") {
				public void onSubmit() {
					LlpUserProfile llpUserProfileNew = (LlpUserProfile) getForm().getModelObject();
					try {

						if (Parameter.ID_TYPE_newic.equals(llpUserProfileNew.getIdType())
								&& (llpUserProfileNew.getIdNo().length() > 12 || llpUserProfileNew.getIdNo().length() < 12)) {
							ssmError(SSMExceptionParam.USER_PROFILE_IC_WRONG_FORMAT);
							return;
						} else {

//							Boolean checkblacklist = false;
							
//							checkblacklist = llpUserProfileService.checkBlacklist(llpUserProfileNew.getIdType(), llpUserProfileNew.getIdNo());
//							if (!checkblacklist) {
								LlpUserProfile llpUserProfile = llpUserProfileService.findByIdTypeAndIdNoWithNamePortion(
										llpUserProfileNew.getIdType(), llpUserProfileNew.getIdNo(), llpUserProfileNew.getName());

								if (llpUserProfile != null) {

									if (StringUtils.isBlank(llpUserProfile.getLoginId())) {
										String userRefNo = llpUserProfile.getUserRefNo();
										System.out.println("userRefNo: " + userRefNo);
										System.out.println("userRefNo: " + llpUserProfile.getLoginId());
										setResponsePage(new RegisterLlpUserProfilePage(userRefNo));
									} else {
										ssmError(SSMExceptionParam.USER_PROFILE_ID_NO_REGISTERED);
										return;
									}
								} else {
									setResponsePage(new RegisterLlpUserProfilePage(llpUserProfileNew.getIdType(), llpUserProfileNew.getIdNo(), llpUserProfileNew.getName()));
								}
//							}else{
//								ssmError(SSMExceptionParam.USER_PROFILE_ID_NO_BLACKLIST);
//								return;
//							}

						}

					} catch (SSMException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ssmError(e.getMessage());
						return;
					}

				}
			});

			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(HomePage.class);
				}
			}.setDefaultFormProcessing(false));
		}
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}