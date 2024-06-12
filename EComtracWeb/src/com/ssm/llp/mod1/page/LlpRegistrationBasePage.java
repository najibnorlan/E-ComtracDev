package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpRegistrationService;
import com.ssm.llp.mod1.service.LlpReservedNameService;

//equal to Page1.java
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LlpRegistrationBasePage extends SecBasePage {// implements PaymentInterface{
	
	boolean needPayment = true; 
	boolean isUserProfileEmail = false; //to test email
	
	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	public LlpRegistrationBasePage() { //empty constructor for payment link
		
	}

	private void init() {
		String warningSessionLabel="";
		if(getDefaultModel().getObject()!=null){
			LlpRegistration llpRegistration = (LlpRegistration) getDefaultModel().getObject();
			if(StringUtils.isBlank(llpRegistration.getLlpNo())){
				warningSessionLabel = resolve("llpRegistrationBase.page.warningSessionLabel");
			}
		}
		add(new SSMLabel("warningSessionLabel",warningSessionLabel));
		add(new LlpReservedNameForm("form", getDefaultModel()));
		
	}

	private class LlpReservedNameForm extends Form {

		public LlpReservedNameForm(String id, IModel m) {
			super(id, m);

			LlpRegistration llpReg = (LlpRegistration) m.getObject();
			
			SSMLabel llpNo = new SSMLabel("llpNo", llpReg.getLlpNo());
			add(llpNo);
			if(StringUtils.isNotBlank(llpReg.getLlpNo())){
				llpNo.setVisible(true);
			}else{
				llpNo.setVisible(false);
			}
			
			SSMLabel applyLlpName = new SSMLabel("llpReservedName.applyLlpName");
			add(applyLlpName);
			
			
			SSMLabel refNo = new SSMLabel("refNo",llpReg.getLlpReservedName().getRefNo());
			add(refNo);

			String regTypeValue = llpReg.getLlpReservedName().getRegType();
			SSMLabel regType = new SSMLabel("regType",regTypeValue,Parameter.LLP_REG_TYPE);
			add(regType);
			

			SSMLabel resultDate = new SSMLabel("resultDate",llpReg.getLlpReservedName().getResultDate());
			add(resultDate);

			SSMLabel expNameDate = new SSMLabel("expNameDate",llpReg.getLlpReservedName().getExpNameDate());
			add(expNameDate);
			
			
			//below if CA, CS, LAW ------------------------------------
			String profBodyTypeValue = llpReg.getLlpReservedName().getProfBodyType(); //CA = chartered acc, CS = cosec, LAW
			SSMLabel nsProfBody = new SSMLabel("nsProfBodyType", profBodyTypeValue, Parameter.PROF_BODY_TYPE);
			add(nsProfBody);
			
			SSMLabel profAuth = new SSMLabel("profAuthOrg",llpReg.getLlpReservedName().getProfAuthOrg());
			add(profAuth);

			SSMLabel profAuthLetterRef = new SSMLabel("profAuthLetterRefNo",llpReg.getLlpReservedName().getProfAuthLetterRefNo());
			add(profAuthLetterRef);
			
			
			SSMLabel profAuthLetterDate = new SSMLabel("profAuthLetterDate",llpReg.getLlpReservedName().getProfAuthLetterDate());
			add(profAuthLetterDate);

			
			SSMLabel profLetterPurpose = new SSMLabel("profLetterPurpose",llpReg.getLlpReservedName().getProfLetterPurpose());
			add(profLetterPurpose);

			SSMLabel profLetterSign = new SSMLabel("profLetterSign",llpReg.getLlpReservedName().getProfLetterSign());
			add(profLetterSign);
			
			SSMLabel profRemark = new SSMLabel("profRemark",llpReg.getLlpReservedName().getProfRemark());
			add(profRemark);
			
			
			if(StringUtils.isBlank(profBodyTypeValue)){
				nsProfBody.setVisible(false);
			}
			
		}

	}
	

	// constructor1 - Klik EDIT (maintenance) from LLP listing (already with llpNo) - combine NS and LLP reg
	public LlpRegistrationBasePage(final String llpNo) {
		needPayment = false; // to control payment (if edit NO payment needed)

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {

				LlpRegistrationService regService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
				LlpRegistration llpRegistration = regService.findByIdWithAllInfo(llpNo); // merge all data object into LlpRegistration

				getSession().setAttribute("llpRegistration_", llpRegistration); // set into session
																		
				return llpRegistration;
			}
		}));
		init();

		add(new LlpRegistrationTabPanel("LlpRegistrationTabPanel", 0));
	}
	

	// constructor2 - From Name Search and llpNo is null (new registration) - combine NS and LLP reg object 
	public LlpRegistrationBasePage(final String nsRefNo, final String llpNo) {

		final LlpRegistration llpRegistration = new LlpRegistration();
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				LlpRegTransaction llpRegTransaction =  llpRegTransactionService.findByReserverNameRefNo(nsRefNo);
				if(llpRegTransaction!=null ){//Mean get from prev session if exist no need to rekey-in
					LlpRegistration llpRegistrationPrevSession = llpRegTransaction.getLlpRegistration();
					llpRegistrationPrevSession.setLlpNo(null);//suppose no llp no on this part
					getSession().setAttribute("llpRegistration_", llpRegistrationPrevSession);
					getSession().setAttribute("llpRegTransaction_", llpRegTransaction);
					return llpRegistrationPrevSession;
				}else{
					llpRegTransaction = new LlpRegTransaction();
					llpRegTransaction.setNsRefNo(nsRefNo);
					llpRegTransaction.setStatus(Parameter.REG_TRANSACTION_STATUS_data_entry);
				}
				LlpReservedNameService reservedNameService = (LlpReservedNameService) getService(LlpReservedNameService.class.getSimpleName()); // from ns db

				LlpReservedName llpReservedName = reservedNameService.findById(nsRefNo);

				// prepare NS data
				llpRegistration.setLlpReservedName(llpReservedName);
				
				//map NS data and set llpRegistration
				llpRegistration.setLlpName(llpRegistration.getLlpReservedName().getApplyLlpName());
				llpRegistration.setLlpStatus(Parameter.LLP_STATUS_existing);
				llpRegistration.setRegType(llpRegistration.getLlpReservedName().getRegType()); //local, foreign, llp for prof body (sama purpose apply ns)
				llpRegistration.setProfBodyType(llpRegistration.getLlpReservedName().getProfBodyType()); //ProfBodyType=LAW, CA, CS... ProfBodyCode=MAICSA,MIA,LS...
//				llpRegistration.setRegDate(new Date()); will be set when registred
				llpRegistration.setLodgeBy(UserEnvironmentHelper.getLoginName());


				// Prepare CO data from login
				List<LlpPartnerLink> partnerLinkList = new ArrayList<LlpPartnerLink>();
								
				LlpUserProfile llpUserProfile = ((LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
				LlpPartnerLink llpPartnerLink = new LlpPartnerLink();
				llpPartnerLink.setUserRefNo(llpUserProfile.getUserRefNo());
				llpPartnerLink.setLlpUserProfile(llpUserProfile);
				llpPartnerLink.copyDataFromProfile(llpUserProfile);
				llpPartnerLink.setType(Parameter.USER_TYPE_complianceOfficer); // CO = compliance officer
				llpPartnerLink.setLinkStatus(Parameter.PARTNER_LINK_STATUS_active); //link_status = A
				llpPartnerLink.setCapitalContribution("");
				llpPartnerLink.setAppointmentDate(new Date());
				partnerLinkList.add(llpPartnerLink);
				
				if(StringUtils.isBlank(llpUserProfile.getLicenseMbrNo())){//if not professional member.mandatory to become partner.
					LlpPartnerLink llpPartnerLinkAsPartner = new LlpPartnerLink();
					llpPartnerLinkAsPartner.setUserRefNo(llpUserProfile.getUserRefNo());
					llpPartnerLinkAsPartner.setLlpUserProfile(llpUserProfile);
					llpPartnerLinkAsPartner.copyDataFromProfile(llpUserProfile);
					llpPartnerLinkAsPartner.setType(Parameter.USER_TYPE_partner); // PT = Partner
					llpPartnerLinkAsPartner.setLinkStatus(Parameter.PARTNER_LINK_STATUS_active); //link_status = A
					llpPartnerLinkAsPartner.setCapitalContribution("");
					llpPartnerLinkAsPartner.setAppointmentDate(new Date());
					partnerLinkList.add(llpPartnerLinkAsPartner);
				}
				
				

				// Tie kan registration dgn partnerlink
				llpRegistration.setLlpPartnerLinks(partnerLinkList);
				getSession().setAttribute("llpRegistration_", llpRegistration); // set into session
//				getSession().setAttribute("llpRegistration_"+nsRefNo, llpRegistration); // set into session
				
				llpRegTransaction.setLlpRegistration(llpRegistration);
				llpRegTransactionService.insert(llpRegTransaction);
				getSession().setAttribute("llpRegTransaction_", llpRegTransaction);

				return llpRegistration; // defaultModel
			}
		}));
		init();

		add(new LlpRegistrationTabPanel("LlpRegistrationTabPanel", 0));
	}
	

	// bila klik save at panel - utk save data ke dalam session
	public LlpRegistrationBasePage(int tabIdToLoad) {
		final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpRegistration;
			}
		}));
		init();

		add(new LlpRegistrationTabPanel("LlpRegistrationTabPanel", tabIdToLoad));
	}


	
	
//	
//	@Override
//	public void sucessPayment(Object obj, String paymentTransId) { //called from payment page
//		
//		if (obj != null) { //if success received data object to insert
//			
//			LlpRegistration llpRegistration = (LlpRegistration) obj;
//			LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
//			
//			try {
//				llpRegistrationService.saveInfo(llpRegistration, paymentTransId);
//				
//				} catch (SSMException e) {
//				ssmError(e);
//			}
//			
//
//		}
//
//	
//	}

}
