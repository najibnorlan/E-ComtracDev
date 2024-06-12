package com.ssm.ezbiz.robformA;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.mod1.page.NumberValidator;

@SuppressWarnings("all")
public class EditRobFormAOwnerPanel extends BaseFrame implements Serializable{
    	private static final long serialVersionUID = 1L;
	
		private int ownerIdx = -1;
		private boolean canUpdate = true;
		private RobFormA robFormA;

		// edit
		public EditRobFormAOwnerPanel(final RobFormAOwner robFormAOwner, final ModalWindow editOwnerPopUp) {
			this(robFormAOwner,editOwnerPopUp,-1);
		}
		public EditRobFormAOwnerPanel(final RobFormAOwner robFormAOwner, final ModalWindow editOwnerPopUp, final int ownerIdx) {
			this(robFormAOwner,editOwnerPopUp,ownerIdx,true);
		}
		
		public EditRobFormAOwnerPanel( final RobFormAOwner robFormAOwner, final ModalWindow editOwnerPopUp, final int ownerIdx , boolean canUpdate) {
			this.canUpdate = canUpdate;
			this.ownerIdx = ownerIdx;
			setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
	            protected Object load() {
	            	return robFormAOwner;
	            }
	        }));
			add(new RobFormAOwnerForm("RobFormAOwnerForm", getDefaultModel(), editOwnerPopUp));
		}
		
		
		/** form for processing the input. */
		private class RobFormAOwnerForm extends Form {
			public RobFormAOwnerForm(String name, IModel model, final ModalWindow editOwnerPopUp) {
				super(name, model);
				
				final RobFormAOwner owner = (RobFormAOwner) model.getObject();
				
				add(new SSMLabel("name",owner.getName()));
				add(new SSMLabel("newicno",owner.getNewicno()));
				add(new SSMLabel("dob",owner.getDob(),"dd MMM yyyy"));
				add(new SSMLabel("gender",owner.getGender(), Parameter.GENDER));
				add(new SSMLabel("race",owner.getRace(), Parameter.RACE));
				add(new SSMLabel("otherrace",owner.getOtherrace()));
				add(new SSMLabel("nationality",owner.getNationality(), Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER));
				add(new SSMLabel("countryoforiginifpr",owner.getCountryoforiginifpr(), Parameter.COUNTRY_CODE));
				
				setPrefixLabelKey("page.lbl.ezbiz.robFormA.robFormAOwner.");
				
				SSMTextField addr = new SSMTextField("addr");
				addr.setRequired(true);
				addr.setReadOnly(!canUpdate);
//				addr.setLabelKey("page.lbl.ezbiz.robFormA.robFormAOwner.addr");
				add(addr);
				
				SSMTextField addr2 = new SSMTextField("addr2");
				addr2.setReadOnly(!canUpdate);
				addr2.setNoLabel();
				add(addr2);
				
				SSMTextField addr3 = new SSMTextField("addr3");
				addr3.setReadOnly(!canUpdate);
				addr3.setNoLabel();
				add(addr3);
				
				
				
				
				SSMTextField postcode = new SSMTextField("addrPostcode");
				postcode.setRequired(true);
				postcode.add(StringValidator.maximumLength(5));
				postcode.add(new NumberValidator());
//				postcode.setLabelKey("page.lbl.ezbiz.robFormA.robFormAOwner.addrPostcode");
				postcode.setReadOnly(!canUpdate);
				add(postcode);
				
				WicketUtils.generatePostcodeTownState(this, postcode, owner, "addrPostcode","addrTown","addrState"  );
				
				
				SSMTextField telNo = new SSMTextField("telNo");
//				telNo.setLabelKey("page.lbl.ezbiz.robFormA.robFormAOwner.telNo");
				telNo.setReadOnly(!canUpdate);
				add(telNo);
				
				SSMTextField mobileNo = new SSMTextField("mobileNo");
				mobileNo.setReadOnly(!canUpdate);
//				mobileNo.setLabelKey("page.lbl.ezbiz.robFormA.robFormAOwner.mobileNo");
				add(mobileNo);
				
				add(new SSMLabel("verificationStatus", owner.getVerificationStatus(), Parameter.ROB_OWNER_VERI_STATUS));
				
				final String ownersValidation = "ownersValidation";
				String ownersFieldToValidate[] = new String[]{"addr","addrTownTmp","addrPostcode","addrStateDesc","telNo","mobileNo"};
				String ownersFieldToValidateRules[] = new String[]{"empty","empty","exactLengthNumber[5]","empty","isNotReqMinLengthNumber[10]","minLengthNumber[10]"};
				setSemanticJSValidation(this, ownersValidation, ownersFieldToValidate, ownersFieldToValidateRules);
				
				SSMAjaxButton updateOwners = new SSMAjaxButton("updateOwners", ownersValidation) {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						RobFormAOwner ownersFromForm  = (RobFormAOwner) form.getModelObject();
						
						if(ownersFromForm.getName().contains(Parameter.ROB_FORM_A_INCENTIVE_oku)) {
						String nameWithOku = ownersFromForm.getName();
						String nameWithoutOku = StringUtils.remove(nameWithOku , "  ( "+Parameter.ROB_FORM_A_INCENTIVE_oku +" )"); //buang semula wording "(OKU)"
						ownersFromForm.setName(nameWithoutOku);
						}
						
						List<RobFormAOwner> listFormRobAOwners = (List<RobFormAOwner>) getSession().getAttribute("listRobFormAOwners_");
						
						if(ownerIdx==-1){
							RobFormAOwner newBranches = new RobFormAOwner();
							copyFromTo(ownersFromForm, newBranches);
							listFormRobAOwners.add(newBranches);
						}else{
							RobFormAOwner selectedOwners = listFormRobAOwners.get(ownerIdx);
							copyFromTo(ownersFromForm, selectedOwners);
							listFormRobAOwners.set(ownerIdx, selectedOwners);
						}
						ownerIdx = -1;
						getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
						
						editOwnerPopUp.close(target);
						
					}
					
					
					void copyFromTo(RobFormAOwner robFormAOwnerFrom, RobFormAOwner robFormAOwnerTo){
						robFormAOwnerTo.setEzbizLoginName(robFormAOwnerFrom.getEzbizLoginName());
						robFormAOwnerTo.setName(robFormAOwnerFrom.getName());
						robFormAOwnerTo.setGender(robFormAOwnerFrom.getGender());
						robFormAOwnerTo.setNewicno(robFormAOwnerFrom.getNewicno());
						robFormAOwnerTo.setDob(robFormAOwnerFrom.getDob());
						robFormAOwnerTo.setAddr(robFormAOwnerFrom.getAddr());
						robFormAOwnerTo.setAddr2(robFormAOwnerFrom.getAddr2());
						robFormAOwnerTo.setAddr3(robFormAOwnerFrom.getAddr3());
						robFormAOwnerTo.setAddrPostcode(robFormAOwnerFrom.getAddrPostcode());
						robFormAOwnerTo.setAddrTown(robFormAOwnerFrom.getAddrTown());
						robFormAOwnerTo.setAddrState(robFormAOwnerFrom.getAddrState());
						robFormAOwnerTo.setEmail(robFormAOwnerFrom.getEmail());
						robFormAOwnerTo.setTelNo(robFormAOwnerFrom.getTelNo());
						robFormAOwnerTo.setMobileNo(robFormAOwnerFrom.getMobileNo());
						robFormAOwnerTo.setRace(robFormAOwnerFrom.getRace());
						robFormAOwnerTo.setOtherrace(robFormAOwnerFrom.getOtherrace());
						robFormAOwnerTo.setNationality(robFormAOwnerFrom.getNationality());
						robFormAOwnerTo.setCountryoforiginifpr(robFormAOwnerFrom.getCountryoforiginifpr());
					}
				};
				add(updateOwners);
				updateOwners.setVisible(canUpdate);
//				AjaxButton acceptOwners = new AjaxButton("acceptOwners") {
//					@Override
//					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						RobFormAOwner ownersFromForm  = (RobFormAOwner) form.getModelObject();
//						List<RobFormAOwner> listFormRobAOwners = (List<RobFormAOwner>) getSession().getAttribute("listRobFormAOwners_");
//						
//						RobFormAOwner selectedOwners = listFormRobAOwners.get(ownerIdx);
//						selectedOwners.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
//						robFormAOwnerService.update(selectedOwners);
//						
//						listFormRobAOwners.set(ownerIdx, selectedOwners);
//						
//						getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
//						
//						editOwnerPopUp.close(target);
//						
//					}
//				};
//				add(acceptOwners);
//				
//				AjaxButton rejectOwners = new AjaxButton("rejectOwners") {
//					@Override
//					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						RobFormAOwner ownersFromForm  = (RobFormAOwner) form.getModelObject();
//						List<RobFormAOwner> listFormRobAOwners = (List<RobFormAOwner>) getSession().getAttribute("listRobFormAOwners_");
//						
//						RobFormAOwner selectedOwners = listFormRobAOwners.get(ownerIdx);
//						selectedOwners.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_REJECT);
//						robFormAOwnerService.delete(selectedOwners);
//						
//						listFormRobAOwners.remove(selectedOwners);
//						
//						getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
//						
//						editOwnerPopUp.close(target);
//					}
//				};
//				add(rejectOwners);
				
				
				final AjaxButton close = new AjaxButton("close") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						editOwnerPopUp.close(target);
					}
					
				};
				add(close);
				close.setDefaultFormProcessing(false);
			}
			@Override
			protected void onError() {
				super.onError();
				
			}
		}


		@Override
		public String getPageTitle() {
			return null;
		}

	}
