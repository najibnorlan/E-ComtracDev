package com.ssm.llp.mod1.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.mod1.dao.RobUserOkuDao;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.RobUserOkuService;

@Service
public class RobUserOkuServiceImpl extends BaseServiceImpl<RobUserOku, String> implements RobUserOkuService {
	
	@Autowired
	RobUserOkuDao robUserOkuDao;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;
	
	@SpringBean(name = "LlpParametersService")
	public LlpParametersService llpParametersService;
	
	
	@Override
	public BaseDao getDefaultDao() {
		return robUserOkuDao;
	}


	
	@Override
	public RobUserOku findOkuByRefNo(String okuRefNo){
		// TODO Auto-generated method stub

		RobUserOku robUserOku = robUserOkuDao.findLatestOkuByRefNo(okuRefNo);
		return robUserOku;
	}
	
	
	@Override
	public RobUserOku findOkuByUserRefNo(String userRefNo){ //robUserProfile.userRefNo
		// TODO Auto-generated method stub

		RobUserOku robUserOku = robUserOkuDao.findLatestOkuByUserRefNo(userRefNo);
		return robUserOku;
	}
	
	public RobUserOku findOkuByIdTypeAndIdNo(String idType, String idNo) { //userProfile.idType userProfile.idNo
		RobUserOku robUserOku = robUserOkuDao.findLatestOkuByIdTypeAndIdNo(idType, idNo);
		return robUserOku;
	}

	@Override
	@Transactional
	public RobUserOku findOkuByUserRefNoWithData(String userRefNo) {
		RobUserOku robUserOku = robUserOkuDao.findLatestOkuByUserRefNoWithData(userRefNo);
		return robUserOku;
	}
	
	@Override
	public RobUserOku findOkuByUserRefNoAndIdRegUser(String userRefNo, long idRegUser){ //robUserProfile.userRefNo and idRegUser
		// TODO Auto-generated method stub

		RobUserOku robUserOku = robUserOkuDao.findLatestOkuByUserRefNoAndIdRegUser(userRefNo, idRegUser);
		return robUserOku;
	}
	
	
	@Override
	@Transactional
	public List<RobUserOku> getListRobUserOku(String userRefNo, String okuRegStatus) {
		List<RobUserOku> robUserOku = robUserOkuDao.getListRobUserOku(userRefNo, okuRegStatus);
		return robUserOku;
	}


	
	@Override
	@Transactional
	public RobUserOku insertUpdateOkuApplication(RobUserOku robUserOku, String cmd) {
		
		Calendar cal = Calendar.getInstance();
		Date dateCurrent = cal.getTime();
		
		robUserOku.setApplicationDt(dateCurrent);
		robUserOku.setOkuCategory(robUserOku.getOkuCategory());
		robUserOku.setOkuCardNo(robUserOku.getOkuCardNo());
		robUserOku.setIsHasSupportingDoc(robUserOku.getIsHasSupportingDoc());
		robUserOku.setDocDataId(robUserOku.getDocDataId());
		robUserOku.setOkuRegStatus(robUserOku.getOkuRegStatus()); //pending approval
		
		robUserOku.setRemarks("");
		robUserOku.setApproveBy("");
		robUserOku.setApprovalDt(null);

		if("insertOku".equalsIgnoreCase(cmd)) {
			insert(robUserOku);
			sendEmailOku(robUserOku,"inProcess");
			
		}else if("updateOku".equalsIgnoreCase(cmd)) {
			update(robUserOku);
			sendEmailOku(robUserOku,"inProcess");
		}
		
		return robUserOku;
	}
	
	
	@Override
	@Transactional
	public void updateOkuApproval(RobUserOku robUserOku) {
		
		Calendar cal = Calendar.getInstance();
		Date dateCurrent = cal.getTime();
		
		robUserOku.setOkuRegStatus(robUserOku.getOkuRegStatus()); 
		robUserOku.setRemarks(StringUtils.isNotBlank(robUserOku.getRemarks())?robUserOku.getRemarks():"");

		if(StringUtils.isNotBlank(robUserOku.getApproveBy())) {
			robUserOku.setApproveBy(robUserOku.getApproveBy());
			robUserOku.setApprovalDt(dateCurrent);
		}else {
			robUserOku.setApproveBy("");
			robUserOku.setApprovalDt(null);
		}
		update(robUserOku);
		sendEmailOku(robUserOku,"approval");
	}
	
	
	@Override
	@Transactional
	public void discardApplication(RobUserOku robUserOku) {
		
//		Calendar cal = Calendar.getInstance();
//		Date dateCurrent = cal.getTime();
		
		robUserOku.setOkuRegStatus(robUserOku.getOkuRegStatus()); 
		robUserOku.setRemarks(StringUtils.isNotBlank(robUserOku.getRemarks())?robUserOku.getRemarks():"");

		update(robUserOku);
		//sendEmailOku(robUserOku,"approval");
		
	}
	
	
	
	
//	@Override
//	@Transactional
//	public void updatePreviousRecordStatus(RobUserOku robUserOkuToUpdate) throws SSMException {
//		
//	//	RobUserOku robUserOkuUpdateStatus = findOkuByRefNo(robUserOkuToUpdate.getOkuRefNo());
//		RobUserOku robUserOkuUpdateStatus = findOkuByUserRefNo(robUserOkuToUpdate.getUserProfile().getUserRefNo()); //find latest record
//		
//		if(robUserOkuUpdateStatus!=null) {
//			if(!(Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOkuUpdateStatus.getOkuRegStatus())) && 
//					!(Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOkuUpdateStatus.getOkuRegStatus()))){
//				if((Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOkuUpdateStatus.getOkuRegStatus())) || 
//						(Parameter.OKU_REGISTRATION_STATUS_PENDING.equals(robUserOkuUpdateStatus.getOkuRegStatus()))) {
//					robUserOkuUpdateStatus.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_CANCEL); //update current A,P status jd cancel..C,R,CB,W status maintain. 
//					update(robUserOkuUpdateStatus);
//				}
//			}else {
//				throw new SSMException("page.lbl.user.profile.oku.error.currentStatusQueryRevoke"); //status Q,B not allowed to change.
//			}
//		}
//	}
	
	@Override
	@Transactional
	public void updatePreviousRecordStatus(RobUserOku userOkuCurrentStatus) throws SSMException { //status user cannot keyin (only system can update status)..
		
//	RobUserOku robUserOkuUpdateStatus = findOkuByUserRefNo(robUserOkuToUpdate.getUserProfile().getUserRefNo()); //find latest record-(error fetch.LAZY -fixed:reuse existing object session).. 
		
		if(userOkuCurrentStatus!=null) {
			if(!(Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(userOkuCurrentStatus.getOkuRegStatus())) && 
					!(Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(userOkuCurrentStatus.getOkuRegStatus()))){
				if((Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(userOkuCurrentStatus.getOkuRegStatus())) || 
						(Parameter.OKU_REGISTRATION_STATUS_PENDING.equals(userOkuCurrentStatus.getOkuRegStatus()))) {
					userOkuCurrentStatus.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_CANCEL); //update current A,P status jd cancel..C,R,CB,W status maintain. 
					update(userOkuCurrentStatus);
				}
			}else {
				throw new SSMException("page.lbl.user.profile.oku.error.currentStatusQueryRevoke"); //status Q,B not allowed to change.
			}
		}
	}
	
	

	@Override
	public void sendEmailOku(RobUserOku robUserOku, String process) {
		String emailTo=robUserOku.getUserProfile().getEmail();
		String refNo = robUserOku.getOkuRefNo();
		String bodyParam0 = robUserOku.getUserProfile().getIdNo();
		String bodyParam1 = robUserOku.getUserProfile().getName();
		String bodyParam2 = robUserOku.getOkuCardNo();
		String bodyParam3 = refNo;
		
		LlpParametersService llpParametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
		String bodyParam4 = llpParametersService.findByCodeTypeValue(Parameter.OKU_REGISTRATION_STATUS, robUserOku.getOkuRegStatus().toString()); 
		
		
		String emailSubject = "";
		String emailBody="";
		
		if("inProcess".equalsIgnoreCase(process)) {
			emailSubject="email.notification.robUserOku.inProcess.subject";
			emailBody="email.notification.robUserOku.inProcess.body";
		}else if("approval".equalsIgnoreCase(process)) {
			emailSubject="email.notification.robUserOku.approval.subject";
			emailBody="email.notification.robUserOku.approval.body";	
		}
		
		//sendMail(final String to, final String subject, String refNo, final String body, final String... bodyParam) 	
		mailService.sendMail(emailTo, emailSubject, refNo, emailBody, bodyParam0,bodyParam1,bodyParam2,bodyParam3,bodyParam4);
	
	}



	@Override
	@Transactional
	public RobUserOku findOkuByRefNoWithData(String okuRefNo) {
		return robUserOkuDao.findOkuByRefNoWithData(okuRefNo);
	}



 }
	