package com.ssm.llp.mod1.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.text.DateFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.mod1.dao.LlpRegistrationDao;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpBusinessCodeLinkService;
import com.ssm.llp.mod1.service.LlpPartnerLinkService;
import com.ssm.llp.mod1.service.LlpRegistrationService;
import com.ssm.llp.mod1.service.LlpReservedNameService;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class LlpRegistrationServiceImpl extends BaseServiceImpl<LlpRegistration, String> implements LlpRegistrationService, PaymentInterface {

	@Autowired
	LlpRegistrationDao llpRegistrationDao;

	@Autowired
	@Qualifier("LlpReservedNameService")
	LlpReservedNameService llpReservedNameService;

	@Autowired
	@Qualifier("LlpPartnerLinkService")
	LlpPartnerLinkService llpPartnerLinkService;

	@Autowired
	@Qualifier("LlpBusinessCodeLinkService")
	LlpBusinessCodeLinkService llpBusinessCodeLinkService;

	@Autowired
	@Qualifier("LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;
	
	@Autowired
	@Qualifier("LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Override
	public BaseDao getDefaultDao() {
		return llpRegistrationDao;
	}

	@Override
	public LlpRegistration findByIdWithAllInfo(String llpNo) { // from DB already obtain llpNo for edit / maintenance

		LlpRegistration llpRegistration = llpRegistrationDao.findByIdWithAllInfo(llpNo);
		LlpReservedName llpReservedName = llpReservedNameService.findLatestReserveNameByLlpNo(llpNo);
		llpRegistration.setLlpReservedName(llpReservedName);

		// Get partnerlink
		List<LlpPartnerLink> listPartnerLink = llpPartnerLinkService.findByLlpNo(llpNo);
		llpRegistration.setLlpPartnerLinks(listPartnerLink);

		// Get bizcode link
		List<LlpBusinessCodeLink> listBusinessCodeLink = llpBusinessCodeLinkService.findByLlpNo(llpNo);
		llpRegistration.setLlpBusinessCodeLink(listBusinessCodeLink);

		return llpRegistration;
	}
	
	

	@Override
	@Transactional
	public void saveInfo(LlpRegistration llpRegistration, String paymentTransId) throws SSMException {
			String logMsgKey = llpRegistration.getLlpReservedName().getRefNo()+":"+llpRegistration.getLlpName();
			System.out.println("START TO LLP REG:"+logMsgKey);
			validateRecord(llpRegistration);
			saveRecord(llpRegistration);
			LlpRegTransaction llpRegTransaction = llpRegTransactionService.findByReserverNameRefNo(llpRegistration.getLlpReservedName().getRefNo());
			if(llpRegTransaction!=null&&!Parameter.REG_TRANSACTION_STATUS_registered.equals(llpRegTransaction.getStatus())){
				llpRegTransaction.setStatus(Parameter.REG_TRANSACTION_STATUS_registered);
				llpRegTransactionService.update(llpRegTransaction);
				if(StringUtils.isBlank(paymentTransId)){
					paymentTransId = llpRegTransaction.getPaymentTransactionId();
				}
				System.out.println("UPDATE TRANSACTION:"+logMsgKey);
			}
			
			if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm personnel (if public)
				String llpNo = llpRegistration.getLlpNo();
				if(StringUtils.isBlank(llpNo)){
					throw new SSMException(SSMExceptionParam.LLP_REG_ERROR_PLZ_RESUBMIT);
				}
//				Seperate Notification Send to catter error
//				sendEmailLlpRegistrationSuccess(llpRegistration);
//				System.out.println("SEND EMAIL REG:"+logMsgKey);
							
				if (!StringUtils.isEmpty(paymentTransId)) {
					LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
	
					if (llpPaymentTransaction != null) {
						llpPaymentTransaction.setAppRefNo(llpRegistration.getLlpNo());
						llpPaymentTransactionService.update(llpPaymentTransaction);
						System.out.println("UPDATE PAYMENT:"+logMsgKey);
					}
				}
			}
	}

	public void validateRecord(LlpRegistration llpRegistration) throws SSMException {

		// only one is enough to validate
		if (StringUtils.isBlank(llpRegistration.getRegAdd1())) {
			throw new SSMException(SSMExceptionParam.LLP_REG_REGADDRESS_BLANK);
		}

		validatePartner(llpRegistration);
		
		validateBizCode(llpRegistration);

	}
	
	
	
	public void validatePartner(LlpRegistration llpRegistration) throws SSMException {
		// validation CO n partner 
				int COcount = 0;
				int partnerCount = 0;
				List<LlpPartnerLink> listPartnerLink = llpRegistration.getLlpPartnerLinks();
				Set<String> emailSet = new HashSet<String>();
				for (Iterator iterator = listPartnerLink.iterator(); iterator.hasNext();) {
					LlpPartnerLink llpPartnerLink = (LlpPartnerLink) iterator.next();	
				 if(Parameter.PARTNER_LINK_STATUS_active.equals(llpPartnerLink.getLinkStatus())){	
					if (Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())) {
						COcount++;
					} else if (Parameter.USER_TYPE_partner.equals(llpPartnerLink.getType())) {
						partnerCount++;
						if(StringUtils.isBlank(llpPartnerLink.getLlpUserProfile().getUserRefNo())){//mean new profile
							if(emailSet.contains(llpPartnerLink.getEmail())){
								throw new SSMException(SSMExceptionParam.LLP_REG_PT_EMAIL_MUST_UNIQUE);
							}
							emailSet.add(llpPartnerLink.getEmail());
						}
					}
				 }
				}

				if (COcount != 1) {
					throw new SSMException(SSMExceptionParam.LLP_REG_CO_COUNT);
				}

				if (partnerCount < 2) {
					throw new SSMException(SSMExceptionParam.LLP_REG_PT_COUNT);
				}
				
				
				if((Parameter.YES_NO_yes.equals(llpRegistration.getAgreementLlp())) && 
						(StringUtils.isNotBlank(llpRegistration.getNoOfPartner()))){
					int pCount = Integer.parseInt((String)llpRegistration.getNoOfPartner());
						if(pCount!=partnerCount){
							throw new SSMException(SSMExceptionParam.LLP_REG_PT_TALLY);
						}
				}
				
			}
	
	
	
	public void validateBizCode(LlpRegistration llpRegistration) throws SSMException{
		// validate BizCode
				int BizCodeCount = 0;
				BizCodeCount = llpRegistration.getLlpBusinessCodeLink().size();
				if (BizCodeCount > 3) {
					throw new SSMException(SSMExceptionParam.LLP_REG_BIZCODE_MAX);
				}
				else if(BizCodeCount < 1){
					throw new SSMException(SSMExceptionParam.LLP_REG_BIZCODE_MIN);
				}
	}
	
	public void validateBizCodePopup(int count) throws SSMException{
		// validate BizCode Modal Window
				if (count > 3) {
					throw new SSMException(SSMExceptionParam.LLP_REG_BIZCODE_MAX);
				}
	}
	
	
	private void saveRecord(LlpRegistration llpRegistration) throws SSMException {

		boolean isNewReg = false;
		// insert or update into table llp_registration 
		if(StringUtils.isBlank(llpRegistration.getLlpNo())){
			isNewReg = true;
			llpRegistration.setRegDate(new Date());
			insert(llpRegistration);							// new registration	(get LLP reg no after insert)
		}
		else{
			update(llpRegistration);
		}

		
		// update LLP regno into name search table
		LlpReservedName llpReservedName = llpReservedNameService.findById(llpRegistration.getLlpReservedName().getRefNo());
		llpReservedName.setLlpNo(llpRegistration.getLlpNo());
		llpReservedNameService.update(llpReservedName);

		
		// insert or update into llp_partner_link list (CO n partner)
		List<LlpPartnerLink> listPartnerLink = llpRegistration.getLlpPartnerLinks();
		for (int i = 0; i < listPartnerLink.size(); i++) {
			LlpPartnerLink llpPartnerLink = listPartnerLink.get(i);
			llpPartnerLink.setLlpNo(llpRegistration.getLlpNo());
			if(isNewReg){
				llpPartnerLink.setAppointmentDate(new Date());
			}
			if (llpPartnerLink.getIdPartnerLink() != 0) {	// Link already exist in db (update)
				llpPartnerLinkService.update(llpPartnerLink);
			} else {
				if (StringUtils.isBlank(llpPartnerLink.getUserRefNo())) { // if new user (insert new user)
					LlpUserProfile llpUserProfile = llpPartnerLink.getLlpUserProfile();
					llpUserProfileService.insert(llpUserProfile);
					llpPartnerLink.setUserRefNo(llpUserProfile.getUserRefNo()); 
				}
				//if user exist UserRefNo already insert in SearchPartnerPage
				llpPartnerLinkService.insert(llpPartnerLink); // (insert existing user)
			}
		}
		
		
		
		//insert or update llp_business_code_link
		List<LlpBusinessCodeLink> listBusinessCodeLink = llpRegistration.getLlpBusinessCodeLink();
		for (int i = 0; i < listBusinessCodeLink.size(); i++) {
			LlpBusinessCodeLink llpBusinessCodeLink = listBusinessCodeLink.get(i);
			llpBusinessCodeLink.setLlpNo(llpRegistration.getLlpNo());
			if(llpBusinessCodeLink.getIdBusinessCodeLink()!=0){ // Link already exist in db
				llpBusinessCodeLinkService.update(llpBusinessCodeLink);
			} else {
				llpBusinessCodeLinkService.insert(llpBusinessCodeLink);
			}	
		}
		
	}
	
/*	
	@Override
	public void emailToPartner(LlpRegistration l, boolean isUserProfileEmail) {
		

		String email = "";
		
		List<LlpPartnerLink> listPartnerLink = l.getLlpPartnerLinks();
		for (int i = 0; i < listPartnerLink.size(); i++) {
			LlpPartnerLink llpPartnerLink = listPartnerLink.get(i);
			
			if(isUserProfileEmail){
				email = llpPartnerLink.getLlpUserProfile().getEmail(); //email is unique
			}
			else{
				email = llpPartnerLink.getEmail(); //email is not unique
			}
			
			if(StringUtils.isNotBlank(email)){
				SimpleMailMessage smm = new SimpleMailMessage();
				smm.setSubject("/");
				String bodyEmail = "Dear Sir/Madam"+" "+llpPartnerLink.getLlpUserProfile().getName()+",\n"+
						"<br><br><br>"+
						"Please be informed that you have been appointed as partner of "+l.getLlpName()+" ( "+l.getLlpNo()+" ) in LLP System.\n"+ 
						"Your profile reference no is "+llpPartnerLink.getUserRefNo()+ ".\n"+
						"<br><br><br>"+
						"Should you have any queries or require any further information please contact us at 603-2299 5500. We welcome any comments or suggestions you may have on how we can improve our services"+"\n"+
						"<br><br>"+
						"Thank you for using our services"+"\n"+
						"<br><br><br>"+
						"Yours sincerely,"+"\n"+
						"<br>"+
						"Admin"+
						"<br><br>"+
						"(c)SSM - All rights reserved.";
				mailService.sendMail(email, "SSM-LLP User Registration", bodyEmail);
				
			}
		}
			
	}
*/	
	
	
	@Override
	public void sendEmailLlpRegistrationSuccess(LlpRegistration l){
		
		String param[] = new String[4];
		
		param[0] = l.getLlpName(); //Name of LLP : 
		param[1] = LlpDateUtils.formatDate(l.getCreateDt()); //Request Date :
		param[2] = l.getLlpNo(); //Registration No. : 
		param[3] = LlpDateUtils.formatDate(l.getRegDate()); //Registration Date :
		
		String paramNotification[] = new String[3];
		
		paramNotification[0] = l.getLlpNo();
		paramNotification[1] = l.getLlpName();
		paramNotification[2] = LlpDateUtils.formatDateFull(l.getRegDate());
		
		String subject = "";
		String body = "";
		String subjectNotification = "";
		String bodyNotification = "";
		String email = "";
						
		if(StringUtils.isBlank(l.getLlpReservedName().getProfBodyType())){ //General = blank / null
			subject = "email.notification.reg.new.subject";
			body    = "email.notification.reg.new.body";
			
			subjectNotification = "email.notification.reg.new.notification.subject";
			bodyNotification = "email.notification.reg.new.notification.body";
		}else{//for professional
			subject = "email.notification.reg.prof.subject";
			body    = "email.notification.reg.prof.body";
			
			subjectNotification = "email.notification.reg.prof.notification.subject";
			bodyNotification = "email.notification.reg.prof.notification.body";
		}		
		
		List<LlpPartnerLink> listPartnerLink = l.getLlpPartnerLinks();
		for (int i = 0; i < listPartnerLink.size(); i++) {
			LlpPartnerLink llpPartnerLink = listPartnerLink.get(i);
				email = llpPartnerLink.getEmail(); //email partnerlink is not unique
					if(StringUtils.isNotBlank(email)){
						mailService.sendMail(email, subject, l.getLlpNo(), body, param);
						mailService.sendMail(email, subjectNotification, l.getLlpNo(), bodyNotification, paramNotification);
					}
			}
		
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException{
		if (obj != null) { //if success received data object to insert
			LlpRegistration llpRegistration = (LlpRegistration) obj;
			try {
				saveInfo(llpRegistration, paymentTransId);
			} catch (SSMException e) {
				System.out.println("Reg Fail!!");
				e.printStackTrace();
				llpRegistration.setLlpNo(null);
				throw new SSMException(e.getMessage());
			}

		}
	}

	@Override
	@Transactional
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		if(!UserEnvironmentHelper.isInternalUser()){
			LlpRegistration llpRegistration = (LlpRegistration) obj;
			String llpNo = llpRegistration.getLlpNo();
			if(StringUtils.isNotBlank(llpNo)){
				sendEmailLlpRegistrationSuccess(llpRegistration);
			}
		}
		
	}


}
