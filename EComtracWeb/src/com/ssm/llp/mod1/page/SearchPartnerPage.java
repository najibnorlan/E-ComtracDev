/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssm.llp.mod1.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpRegistrationService;
import com.ssm.llp.mod1.service.LlpUserProfileService;

/**
 * @author Matej Knopp
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SearchPartnerPage extends BaseFrame {
	private Page parentPage;
	boolean isEdit = false;
	boolean isRecordExist = false;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpRegistrationService")
	private LlpRegistrationService llpRegistrationService;

	final LlpPartnerLink llpPartnerLinkModel;

	/**
	 * 
	 * @param modalWindowPage
	 * @param window
	 */
	//add new
	public SearchPartnerPage(final Page parentPage, final ModalWindow window, PageParameters params) {
		this.parentPage = parentPage;
		isEdit = false;
		boolean isNewReg = true;
		LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
		if(StringUtils.isNotBlank(llpRegistration.getLlpNo())){ //if already get LLP No show view mode only
			isNewReg = false;
		}
		
		
		llpPartnerLinkModel = new LlpPartnerLink();
		llpPartnerLinkModel.setLlpUserProfile(new LlpUserProfile());

		if (params != null) {
			String idType = params.get("idType").toString();
			String idNo = params.get("idNo").toString();
			String namePortion = params.get("namePortion").toString();
			Date convertedDate = null;

			LlpUserProfile llpUserProfile;
			try {
				llpUserProfileService.validateBlacklistedPersonForPartner(idType,idNo); //throw if blacklist
				
				llpUserProfile = llpUserProfileService.findByIdTypeAndIdNoWithNamePortion(idType, idNo, namePortion);

				if (llpUserProfile == null) { // person not exist in db
					isRecordExist = false;

					if(Parameter.ID_TYPE_newic.equals(idType)&& (StringUtils.isNotBlank(idNo))){
						
						String year=idNo.substring(0,2);
						String month=idNo.substring(2,4);
						String dd=idNo.substring(4,6);

						int yearDOB = Integer.parseInt(year) + 1900;
						
						String dateOfBirth = dd+"/"+month+"/"+yearDOB;
					
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					    
						try {
							convertedDate = dateFormat.parse(dateOfBirth);
						} catch (ParseException e) {
							e.printStackTrace();
						}

					}
					
					llpUserProfile = new LlpUserProfile();
					llpPartnerLinkModel.getLlpUserProfile().setName(namePortion.toUpperCase());
					llpPartnerLinkModel.getLlpUserProfile().setIdType(idType);
					llpPartnerLinkModel.getLlpUserProfile().setIdNo(idNo);
					llpPartnerLinkModel.getLlpUserProfile().setDob(convertedDate);
					
					
				}

				else { // person already exist in db
					isRecordExist = true;
					llpPartnerLinkModel.setLlpUserProfile(llpUserProfile); // set user profile record into partner link (tie record)

					llpPartnerLinkModel.copyDataFromProfile(llpUserProfile); // default already have data

				}

				llpPartnerLinkModel.setType(Parameter.USER_TYPE_partner); // PT = partner
				llpPartnerLinkModel.setCapitalContribution("");
				llpPartnerLinkModel.setLinkStatus(Parameter.PARTNER_LINK_STATUS_active);
				if(!isNewReg){
					llpPartnerLinkModel.setAppointmentDate(new Date());
				}

			} catch (SSMException e) {
				ssmError(e);
				params = null; // to return to search panel with error msg
			}

		}

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpPartnerLinkModel;
			}
		}));

		if (params == null) {
			add(new InputForm("inputForm", getDefaultModel(), parentPage, window).setVisible(false));
			add(new SearchPartnerCriteriaPanel("searchPartnerCriteriaPanel", parentPage, window).setVisible(true)); // search panel
																													 
		} else {
			add(new InputForm("inputForm", getDefaultModel(), parentPage, window).setVisible(true));
			add(new SearchPartnerCriteriaPanel("searchPartnerCriteriaPanel", parentPage, window).setVisible(false)); // search panel
																														
		}
	}

	// edit
	public SearchPartnerPage(Page parentPage, final LlpPartnerLink partnerLink, ModalWindow window) {
		isEdit = true;
		llpPartnerLinkModel = partnerLink;
		
		if(StringUtils.isNotBlank(llpPartnerLinkModel.getUserRefNo())){
			isRecordExist = true;
		}
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpPartnerLinkModel;
			}
		}));

		add(new InputForm("inputForm", getDefaultModel(), parentPage, window).setVisible(true));
		add(new SearchPartnerCriteriaPanel("searchPartnerCriteriaPanel", parentPage, window).setVisible(false)); // search panel
																													

	}

	/** form for processing the input. */
	private class InputForm extends Form {
		public InputForm(String name, IModel model, final Page parentPage, final ModalWindow window) {

			super(name, model);
			final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
			boolean isOfficer = UserEnvironmentHelper.isInternalUser();
			boolean isNewReg = true;
			boolean isDisableEdit = false; //can edit
			
			
			if(StringUtils.isNotBlank(llpPartnerLinkModel.getLlpNo())){ //if already get LLP No show view mode only
				isNewReg = false;
			}
			if(!isNewReg && !isOfficer){ //if non ssm and already obtain llp No.
				isDisableEdit = true;
			}

			LlpPartnerLink link = (LlpPartnerLink) model.getObject();
			
			
			HiddenField idPartnerLinkField = new HiddenField("idPartnerLink");
			add(idPartnerLinkField);
			

			SSMTextField pName = new SSMTextField("llpUserProfile.name");
			pName.setReadOnly(isDisableEdit);
			pName.setLabelKey("llpReg.page.partner.name");

			
			SSMDropDownChoice pIdType = new SSMDropDownChoice("llpUserProfile.idType", Parameter.ID_TYPE);
			pIdType.setReadOnly(isDisableEdit);
			pIdType.setLabelKey("llpReg.page.partner.idType");

			SSMTextField pIdNo = new SSMTextField("llpUserProfile.idNo");
			pIdNo.setReadOnly(isDisableEdit);
			pIdNo.setLabelKey("llpReg.page.partner.idNo");

			
			SSMDateTextField pDob = new SSMDateTextField("llpUserProfile.dob");
			pDob.setReadOnly(isDisableEdit);
			pDob.setLabelKey("llpReg.page.partner.dob");

			SSMDropDownChoice pNationality = new SSMDropDownChoice("llpUserProfile.nationality", Parameter.NATIONALITY_TYPE);
			pNationality.setReadOnly(isDisableEdit);
			pNationality.setLabelKey("llpReg.page.partner.nationality");

			SSMDropDownChoice pRace = new SSMDropDownChoice("llpUserProfile.race", Parameter.RACE);
			pRace.setReadOnly(isDisableEdit);
			pRace.setLabelKey("llpReg.page.partner.race");
			
			SSMDateTextField pAppointmentDate = new SSMDateTextField("appointmentDate");
			pAppointmentDate.setReadOnly(isDisableEdit);
			pAppointmentDate.setLabelKey("llpReg.page.partner.appointDate");
			pAppointmentDate.setVisible(!isNewReg);
			

			SSMRadioChoice pGender = new SSMRadioChoice("llpUserProfile.gender", Parameter.GENDER);
			pGender.setReadOnly(isDisableEdit);
			pGender.setLabelKey("llpReg.page.partner.gender");

			SSMTextField pAdd1 = new SSMTextField("add1");
			pAdd1.setReadOnly(isDisableEdit);
			pAdd1.setLabelKey("llpReg.page.partner.add");

			SSMTextField pAdd2 = new SSMTextField("add2");
			pAdd2.setReadOnly(isDisableEdit);

			SSMTextField pAdd3 = new SSMTextField("add3");
			pAdd3.setReadOnly(isDisableEdit);

			SSMDropDownChoice pState = new SSMDropDownChoice("state", Parameter.STATE_CODE);
			pState.setLabelKey("llpReg.page.partner.state");
			pState.setReadOnly(isDisableEdit);
			
			SSMDropDownChoice pCountry = new SSMDropDownChoice("country", Parameter.COUNTRY_CODE, pState);
			pCountry.setLabelKey("llpReg.page.partner.country");
			pCountry.setReadOnly(isDisableEdit);

			SSMTextField pCity = new SSMTextField("city");
			pCity.setReadOnly(isDisableEdit);
			pCity.setLabelKey("llpReg.page.partner.city");

			SSMTextField pPostcode = new SSMTextField("postcode");
			pPostcode.setReadOnly(isDisableEdit);
			pPostcode.add(new NumberValidator());
			pPostcode.setLabelKey("llpReg.page.partner.postcode");

			SSMTextField pTelNo = new SSMTextField("offNo");
			pTelNo.setReadOnly(isDisableEdit);
			pTelNo.setLabelKey("llpReg.page.partner.offNo");
			pTelNo.add(new NumberValidator());
			

			SSMTextField pHpNo = new SSMTextField("hpNo");
			pHpNo.setReadOnly(isDisableEdit);
			pHpNo.setLabelKey("llpReg.page.partner.hpNo");
			pHpNo.add(new NumberValidator());

			SSMTextField pFaxNo = new SSMTextField("faxNo");
			pFaxNo.setReadOnly(isDisableEdit);
			pFaxNo.setLabelKey("llpReg.page.partner.faxNo");
			pFaxNo.add(new NumberValidator());

			SSMTextField pEmail = new SSMTextField("email");
			pEmail.setReadOnly(isDisableEdit);
			pEmail.setUpperCase(false);
			pEmail.setLabelKey("llpReg.page.partner.email");

			if (isRecordExist) {
				
				pDob.setReadOnly(true);
				pRace.setReadOnly(true);
				pGender.setReadOnly(true);
				pNationality.setReadOnly(true);
				pName.setReadOnly(true);
				
			} else {
				pDob.setRequired(true);
				pRace.setRequired(true);
				pGender.setRequired(true);
				pNationality.setRequired(true);
				pName.setRequired(true); //portion of name editable
			}

//			pIdType.add(new AttributeModifier("readonly", new Model("readonly")));
//			pIdNo.add(new AttributeModifier("readonly", new Model("readonly")));
			
			pIdType.setReadOnly(true);
			pIdNo.setReadOnly(true);

			
			pAppointmentDate.setRequired(true);
			pAdd1.setRequired(true);
			pAdd1.add(StringValidator.maximumLength(150));
			pAdd2.add(StringValidator.maximumLength(150));
			pAdd3.add(StringValidator.maximumLength(150));
			pCity.setRequired(true);
			pCity.add(StringValidator.maximumLength(150));
			pPostcode.setRequired(true);
			pPostcode.add(StringValidator.maximumLength(10));
			pCountry.setRequired(true);
			//pTelNo.setRequired(true);
			pTelNo.add(StringValidator.maximumLength(15));
			pHpNo.setRequired(true);
			pHpNo.add(StringValidator.maximumLength(15));
			pFaxNo.add(StringValidator.maximumLength(15));
			pEmail.setRequired(true);
			pEmail.add(StringValidator.maximumLength(150));
			pEmail.add(EmailAddressValidator.getInstance());

			// add
			add(pName);
			add(pIdType);
			add(pIdNo);
			add(pDob);
			add(pNationality);
			add(pRace);
			add(pGender);
			add(pAdd1);
			add(pAdd2);
			add(pAdd3);
			add(pCountry);
			add(pState);
			add(pCity);
			add(pPostcode);
			add(pTelNo);
			add(pHpNo);
			add(pFaxNo);
			add(pEmail);
			add(pAppointmentDate);
			
			
			AjaxButton addNew = new AjaxButton("addNew") { // submit button
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					try {
						
						// add new (user currently exist in db)
						LlpPartnerLink llpPartnerLinkNew = new LlpPartnerLink();
						llpPartnerLinkNew.setAdd1(llpPartnerLinkModel.getAdd1());
						llpPartnerLinkNew.setAdd2(llpPartnerLinkModel.getAdd2());
						llpPartnerLinkNew.setAdd3(llpPartnerLinkModel.getAdd3());
						llpPartnerLinkNew.setCity(llpPartnerLinkModel.getCity());
						llpPartnerLinkNew.setPostcode(llpPartnerLinkModel.getPostcode());
						llpPartnerLinkNew.setCountry(llpPartnerLinkModel.getCountry());
						llpPartnerLinkNew.setCapitalContribution(llpPartnerLinkModel.getCapitalContribution());
						llpPartnerLinkNew.setEmail(llpPartnerLinkModel.getEmail());
						llpPartnerLinkNew.setFaxNo(llpPartnerLinkModel.getFaxNo());
						llpPartnerLinkNew.setHpNo(llpPartnerLinkModel.getHpNo());
						llpPartnerLinkNew.setOffNo(llpPartnerLinkModel.getOffNo());
						llpPartnerLinkNew.setState(llpPartnerLinkModel.getState());
						llpPartnerLinkNew.setType(llpPartnerLinkModel.getType());
						llpPartnerLinkNew.setLinkStatus(llpPartnerLinkModel.getLinkStatus());
						llpPartnerLinkNew.setAppointmentDate(llpPartnerLinkModel.getAppointmentDate());
						llpPartnerLinkNew.setUserRefNo(llpPartnerLinkModel.getLlpUserProfile().getUserRefNo()); // if not exist insert null. need to update.

						llpPartnerLinkNew.setLlpNo(llpPartnerLinkModel.getLlpNo()); // if not exist insert null. need to update.

						llpPartnerLinkNew.setLlpUserProfile(llpPartnerLinkModel.getLlpUserProfile()); // tie record
																										

						// add new (user still not exist in db)
						if (!isRecordExist) {
							LlpUserProfile llpUserProfileNew = new LlpUserProfile();
							llpUserProfileNew.setDob(llpPartnerLinkModel.getLlpUserProfile().getDob());
							llpUserProfileNew.setGender(llpPartnerLinkModel.getLlpUserProfile().getGender());
							llpUserProfileNew.setIdNo(llpPartnerLinkModel.getLlpUserProfile().getIdNo());
							llpUserProfileNew.setIdType(llpPartnerLinkModel.getLlpUserProfile().getIdType());
							llpUserProfileNew.setName(llpPartnerLinkModel.getLlpUserProfile().getName());
							llpUserProfileNew.setNationality(llpPartnerLinkModel.getLlpUserProfile().getNationality());
							llpUserProfileNew.setRace(llpPartnerLinkModel.getLlpUserProfile().getRace());
							llpUserProfileNew.setUserStatus(Parameter.USER_STATUS_active); //A
							llpUserProfileNew.setUserType(Parameter.USER_TYPE_partner); //PT
							llpUserProfileNew.copyDataFromPartnerLink(llpPartnerLinkNew); // copy data such as email, address

							llpPartnerLinkNew.setLlpUserProfile(llpUserProfileNew); //override above
						}
						
						llpUserProfileService.validateInsertPersonForPartner(llpPartnerLinkNew.getLlpUserProfile(), isRecordExist);
						
						
						if (isEdit) {
							llpRegistration.getLlpPartnerLinks().remove(llpPartnerLinkModel); // remove record from listing to avoid duplicate record
						}
						
						//avoid from adding same partner
						int partnerCount = 0;
						List<LlpPartnerLink> partnerLinkList = llpRegistration.getLlpPartnerLinks();
						for (Iterator iterator = partnerLinkList.iterator(); iterator.hasNext();) {
							LlpPartnerLink llpPartnerLinkTemp = (LlpPartnerLink) iterator.next();
							if(Parameter.USER_TYPE_partner.equals(llpPartnerLinkTemp.getType())){ //Check PT only
								partnerCount++;
								if(StringUtils.equals(llpPartnerLinkTemp.getLlpUserProfile().getIdNo(), llpPartnerLinkNew.getLlpUserProfile().getIdNo())){ // to avoid add same record
									System.out.println("same Partner found!!!");
									throw new SSMException(SSMExceptionParam.LLP_REG_PT_SAME);
								}
							}
						}
						
						
						if((Parameter.YES_NO_yes.equals(llpRegistration.getAgreementLlp())) && 
								(StringUtils.isNotBlank(llpRegistration.getNoOfPartner()))){
							int declaredPartner = Integer.parseInt((String)llpRegistration.getNoOfPartner());
								if(partnerCount+1 > declaredPartner){
									throw new SSMException(SSMExceptionParam.LLP_REG_PT_TALLY); //partner added must equal to partnership agreement
								}
						}

						
						llpRegistration.getLlpPartnerLinks().add(llpPartnerLinkNew); 
						
						window.close(target);
						
					} catch (SSMException e) {
						ssmError(e);
						target.add(feedbackPanel); //error throws (2)
						
					}
					
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel); //is required validation (1)
				}

			};
			add(addNew);
			addNew.setVisible(!isEdit);
			
			AjaxButton updatePartner = new AjaxButton("updatePartner") { // submit button
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					
						System.out.println(">>"+llpPartnerLinkModel);
						System.out.println(">>"+llpPartnerLinkModel.getEmail());
						LlpPartnerLink partnerLink = llpPartnerLinkModel;
						if (!isRecordExist) {
							LlpUserProfile llpUserProfileNew = new LlpUserProfile();
							llpUserProfileNew.setDob(partnerLink.getLlpUserProfile().getDob());
							llpUserProfileNew.setGender(partnerLink.getLlpUserProfile().getGender());
							llpUserProfileNew.setIdNo(partnerLink.getLlpUserProfile().getIdNo());
							llpUserProfileNew.setIdType(partnerLink.getLlpUserProfile().getIdType());
							llpUserProfileNew.setName(partnerLink.getLlpUserProfile().getName());
							llpUserProfileNew.setNationality(partnerLink.getLlpUserProfile().getNationality());
							llpUserProfileNew.setRace(partnerLink.getLlpUserProfile().getRace());
							llpUserProfileNew.setUserStatus(Parameter.USER_STATUS_active); //A
							llpUserProfileNew.setUserType(Parameter.USER_TYPE_partner); //PT
							llpUserProfileNew.copyDataFromPartnerLink(partnerLink); // only copy address

							partnerLink.setLlpUserProfile(llpUserProfileNew); //override above
						}
						
						List<LlpPartnerLink> partnerLinkList = llpRegistration.getLlpPartnerLinks();
						for (int i = 0; i < partnerLinkList.size(); i++) {
							LlpPartnerLink llpPartnerLink = partnerLinkList.get(i);
							System.out.println(llpPartnerLink);
							System.out.println(llpPartnerLink.getEmail());
						}
						
						window.close(target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel); //is required validation (1)
				}

			};
			add(updatePartner);
			updatePartner.setVisible(isEdit);
			
			
			LlpPartnerLink llpPartnerLink = (LlpPartnerLink) getDefaultModelObject();
			if(llpPartnerLink.getIdPartnerLink()>0 && !isOfficer){
				updatePartner.setVisible(false);
			}
			
			Button cancel = new Button("cancel") {
				public void onSubmit() {
					setResponsePage(new SearchPartnerPage(parentPage, window, null));
				}
			}.setDefaultFormProcessing(false);
			add(cancel);
			
			
			//hide button "Add Partner" and "Cancel (go search page)"
			if(!isOfficer && !isNewReg){
				addNew.setVisible(false);
			}


		}

	}

	@Override
	public String getPageTitle() {
		return "search.partner.title";
	}

	public Page getParentPage() {
		return parentPage;
	}
}
