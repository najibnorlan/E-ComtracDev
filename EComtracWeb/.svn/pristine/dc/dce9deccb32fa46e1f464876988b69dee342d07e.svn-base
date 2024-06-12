package com.ssm.ezbiz.robformA;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.NumberValidator;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings("all")
public class ViewRobFormAOwnerPanel extends BaseFrame {
	
	@SpringBean(name="RobFormAOwnerService")
	RobFormAOwnerService robFormAOwnerService;
	
	@SpringBean(name="LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;

		// edit
		public ViewRobFormAOwnerPanel(final RobFormAOwner owner,  final ModalWindow viewOwnerPopUp) {			
				
			LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserIdNo(owner.getNewicno());
			
			add(new SSMLabel("name",owner.getName()));
			add(new SSMLabel("newicno",owner.getNewicno()));
			add(new SSMLabel("icColor",llpUserProfile.getIcColour(), Parameter.NRIC_COLOUR));
			add(new SSMLabel("dob",owner.getDob(),"dd MMM yyyy"));
			add(new SSMLabel("gender",owner.getGender(), Parameter.GENDER));
			add(new SSMLabel("race",owner.getRace(), Parameter.RACE));
			add(new SSMLabel("otherrace",owner.getOtherrace()));
			add(new SSMLabel("nationality",owner.getNationality(), Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER));
			add(new SSMLabel("countryoforiginifpr",owner.getCountryoforiginifpr(), Parameter.COUNTRY_CODE));
			add(new SSMLabel("telNo", owner.getTelNo() ));
			add(new SSMLabel("mobileNo", owner.getMobileNo() ));
			
			String address = owner.getAddr();
			if(StringUtils.isNotBlank(owner.getAddr2())){
				address += "\n"+owner.getAddr2();
			}
			if(StringUtils.isNotBlank(owner.getAddr3())){
				address += "\n"+owner.getAddr3();
			}
			address += "\n"+owner.getAddrPostcode()+" "+owner.getAddrTown();
			address = address +"\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, owner.getAddrState()) ;
			add(new MultiLineLabel("addr", address));
			
			
			SSMAjaxLink close2 = new SSMAjaxLink("close") {
				@Override
				public void onClick(AjaxRequestTarget arg0) {
					viewOwnerPopUp.close(arg0);
				}
			};
			add(close2);
			
/*			System.out.println("icColor>>>>>>>>>>>" + llpUserProfile.getIcColour());*/
		}

		@Override
		public String getPageTitle() {
			return null;
		}

}
