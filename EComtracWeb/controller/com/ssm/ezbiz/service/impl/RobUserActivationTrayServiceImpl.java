/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.base.common.util.DateUtil;
import com.ssm.base.model.QueueProcessBranchModel;
import com.ssm.ezbiz.dao.RobUserActivationTrayDao;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class RobUserActivationTrayServiceImpl extends BaseServiceImpl<RobUserActivationTray,  String> implements RobUserActivationTrayService{
	@Autowired 
	RobUserActivationTrayDao robUserActivationTrayDao;
	
	@Autowired 
	RobFormNotesService robFormNotesService;
	
	@Autowired 
	LlpUserProfileService llpUserProfileService;
	
	@Autowired 
	LlpFileDataService llpFileDataService;
	
	@Autowired 
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;
	
	
	public static Map<String, QueueProcessBranchModel> mapQueueByDate = new HashMap<String, QueueProcessBranchModel>();
	
	@Override
	public BaseDao getDefaultDao() {
		return robUserActivationTrayDao;
	}
	
	@Override
	public void unlock(RobUserActivationTray robUserActivationTray) {
		robUserActivationTray.setLockBy(null);
		robUserActivationTray.setLockDt(null);
		update(robUserActivationTray);
	}

	@Override
	public void lock(RobUserActivationTray robUserActivationTray) {
		String loginUser = UserEnvironmentHelper.getLoginName();
		robUserActivationTray.setLockBy(loginUser);
		robUserActivationTray.setLockDt(new Date());
		update(robUserActivationTray);
	}

	@Override
	public void processApplication(RobUserActivationTray activationTray, String status, String processNote) throws SSMException {
		
		
		boolean hasValidStatus = false;
		if (Parameter.ACTIVATION_TRAY_STATUS_APPROVED.equals(status)) {
			hasValidStatus = true;
			String remarks =  WicketApplication.resolve("page.lbl.ezbiz.robUserActivationTray.userProfileUpdateRemark", activationTray.getAppRefNo());
			llpUserProfileService.updateStatus(activationTray.getLlpUserProfile().getLoginId(),  Parameter.USER_STATUS_active  , remarks, null );
			
		}
		if (Parameter.ACTIVATION_TRAY_STATUS_REJECT.equals(status)) {
			hasValidStatus = true;
		}
		if (Parameter.ACTIVATION_TRAY_STATUS_QUERY.equals(status)) {
			hasValidStatus = true;
			
			RobFormNotes formNotes = new RobFormNotes();
			formNotes.setNotes(processNote);
			formNotes.setRobFormCode(activationTray.getAppRefNo());
			formNotes.setRobFormType(Parameter.ROB_FORM_TYPE_USER_ACTIVATION);
			formNotes.setQueryBy(UserEnvironmentHelper.getLoginName());
			robFormNotesService.insert(formNotes);
			
			

		}
		
		if(hasValidStatus) {
			activationTray.setLockBy(null);
			activationTray.setLockDt(null);

			activationTray.setStatus(status);
			
			InternalUserEnviroment internalUser = (InternalUserEnviroment) UserEnvironmentHelper.getUserenvironment();
			activationTray.setProcessBy(internalUser.getLoginName());
			activationTray.setProcessBranch(internalUser.getDefaultBranch());
			
			activationTray.setProcessNote(processNote);
			activationTray.setProcessDt(new Date());
			
			
			update(activationTray);
			
		}

	}
	
	@Override
	public void sendEmailAfterProcess(RobUserActivationTray activationTray) {
		
		if (Parameter.ACTIVATION_TRAY_STATUS_APPROVED.equals(activationTray.getStatus())) {
			mailService.sendMail( activationTray.getLlpUserProfile().getEmail(), "email.notification.robUserActivationTray.approve.subject", activationTray.getAppRefNo(), "email.notification.robUserActivationTray.approve.body", 
					activationTray.getAppRefNo(), activationTray.getLlpUserProfile().getLoginId(), activationTray.getLlpUserProfile().getName());
		}
		

		if (Parameter.ACTIVATION_TRAY_STATUS_QUERY.equals(activationTray.getStatus())) {
			mailService.sendMail( activationTray.getLlpUserProfile().getEmail(), "email.notification.robUserActivationTray.query.subject", activationTray.getAppRefNo(), "email.notification.robUserActivationTray.query.body", 
					activationTray.getAppRefNo(), activationTray.getLlpUserProfile().getLoginId(), activationTray.getLlpUserProfile().getName());
		}
		
		if (Parameter.ACTIVATION_TRAY_STATUS_REJECT.equals(activationTray.getStatus())) {
			
			mailService.sendMail( activationTray.getLlpUserProfile().getEmail(), "email.notification.robUserActivationTray.reject.subject", activationTray.getAppRefNo(), "email.notification.robUserActivationTray.reject.body", 
					activationTray.getAppRefNo(), activationTray.getLlpUserProfile().getLoginId(), activationTray.getLlpUserProfile().getName());
		}
		
			
	}

	@Override
	public void submit(RobUserActivationTray activationTray, LlpFileData myKadDoc, LlpFileData selfieDoc, LlpFileData supportingDoc) {
		
		activationTray.setStatus(Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS);
		
		llpFileDataService.insert(myKadDoc);
		activationTray.setMykadDocId(myKadDoc);
		llpFileDataService.insert(selfieDoc);
		activationTray.setSelfieDocId(selfieDoc);
		llpFileDataService.insert(supportingDoc);
		activationTray.setSupportingDocId(supportingDoc);
			
		insert(activationTray);
	}
	
	@Override
	public void insert(RobUserActivationTray entity) {
//		String branch = getProcessingBranch();
//		entity.setProcessBranch(branch);
		robUserActivationTrayDao.insert(entity);
	}
	
	
	
	public static Map<String, String> mapQuotaConfig = new HashMap<String,String>();
	public static void main(String[] args)throws Exception {
		System.out.println(DateUtil.getCurrentDate("yyyy-MM-dd"));
		mapQuotaConfig.put("KL","5");
		mapQuotaConfig.put("JB","5");
		mapQuotaConfig.put("TR","7");
		mapQuotaConfig.put("UT","7");
		
		for (int i = 0; i < 50; i++) {
			String br = getProcessingBranch();
			System.out.println((i+1)+":"+br);
		}
		
	}
	
	static synchronized String getProcessingBranch() {
//		Map<String, String> mapQuotaConfig = llpParametersService.findActiveCodeTypeAsMap(Parameter.USER_ACTIVATION_NOTICE_PROCESS_QUATA);
		
		String today = DateUtil.getCurrentDate("yyyy-MM-dd");
		QueueProcessBranchModel queueProcessBranchModel = mapQueueByDate.get(today);
		if(queueProcessBranchModel==null) {
			queueProcessBranchModel = new QueueProcessBranchModel(today);
			Map<String, Integer> mapBranchQuota = queueProcessBranchModel.getMapBranchQuota();
			Queue<String> queueIndicator = queueProcessBranchModel.getQueueIndicator();
			
			for (Iterator iterator = mapQuotaConfig.keySet().iterator(); iterator.hasNext();) {
				String branch = (String) iterator.next();
				queueIndicator.add(branch);
				mapBranchQuota.put(branch, 0);
			}
			
			mapQueueByDate.put(today,queueProcessBranchModel);
		}
		Map<String, Integer> mapBranchQuota = queueProcessBranchModel.getMapBranchQuota();
		Queue<String> queueIndicator = queueProcessBranchModel.getQueueIndicator();
		
		String branch = queueIndicator.peek();
		int currentQuota = mapBranchQuota.get(branch);
		int maxQuota = Integer.valueOf(mapQuotaConfig.get(branch));
		
		
		String curbranch = branch;
		while(currentQuota>=maxQuota) {
			queueIndicator.remove();
			queueIndicator.add(branch);
			
			branch = queueIndicator.peek();
			currentQuota = mapBranchQuota.get(branch);
			maxQuota = Integer.valueOf(mapQuotaConfig.get(branch));
			if(branch.equals(curbranch)) {
				return "KL";
			}
		}
		
		mapBranchQuota.put(branch, (currentQuota+1) );
		
		queueIndicator.remove();
		queueIndicator.add(branch);
		
		return branch+" : "+(currentQuota+1);
	}

	@Override
	public void reSubmit(RobUserActivationTray activationTray, String queryAnswer, LlpFileData myKadDoc, LlpFileData selfieDoc,
			LlpFileData supportingDoc) {
		
		RobFormNotes formNotes = activationTray.getListRobFormNotes().get(activationTray.getListRobFormNotes().size() - 1);
		formNotes.setNotesAnswer(queryAnswer);
		robFormNotesService.update(formNotes);

		activationTray.setStatus(Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION);
		activationTray.setReSubmitDt(new Date());
		
		if (myKadDoc != null && myKadDoc.getFileData()!=null) {
			llpFileDataService.insert(myKadDoc);
			activationTray.setMykadDocId(myKadDoc);
		}
		if (selfieDoc != null && selfieDoc.getFileData()!=null) {
			llpFileDataService.insert(selfieDoc);
			activationTray.setSelfieDocId(selfieDoc);
		}
		if (supportingDoc != null && supportingDoc.getFileData()!=null) {
			llpFileDataService.insert(supportingDoc);
			activationTray.setSupportingDocId(supportingDoc);
		}
		update(activationTray);
	}

	@Override
	public boolean hasPendingApplication(String userRefNo) {
		return robUserActivationTrayDao.hasPendingApplication(userRefNo);
	}

	@Override
	public void discardNonPendingApp() {
		robUserActivationTrayDao.discardNonPendingApp();
		
	}

}
