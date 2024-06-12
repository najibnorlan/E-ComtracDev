package com.ssm.ezbiz.service.impl;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.PaymentService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobHealthCheckService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.ezbiz.service.RobSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.WSUamClientService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.model.RobIncentive;
import com.ssm.llp.mod1.service.RobIncentiveService;
import com.ssm.webis.client.EchoClient;
import com.ssm.webis.param.UamUserInfo;

@Service
public class RobSchedulerServiceImpl implements RobSchedulerService  {
	final String DEFAULT_MINIT_DELAY="0 0/5 * * * ?";


	@Autowired
	LlpParametersService llpParametersService;

	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	RobFormNotesService robFormNotesService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	@Qualifier("isRunScheduler")
	String isRunScheduler;

	@Autowired
	WSManagementService wSManagementService;

	@Autowired
	MailService mailService;

	@Autowired
	RobHealthCheckService healthCheckService;

	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	LlpRunningNoService llpRunningNoService;

	@Autowired
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;
	
	@Autowired
	RobFormAService robFormAService;
	
	@Autowired
	RobIncentiveService robIncentiveService;
	
	@Autowired
	RobFormBService robFormBService;
	
	@Autowired
	RobFormCService robFormCService;
	
	@Autowired
	RobRenewalService robRenewalService;
	
	
	@Autowired
	@Qualifier("WSUamClientService")
	private WSUamClientService wSUamClientService;
	

	private static ThreadLocal threadLocalForceRun = new ThreadLocal();
	
	
	public static Map<String, Date> mapMethod = new HashMap<String, Date>();
	

	/*
	 * @Autowired LlpPaymentReceipt llpPaymentReceipt;
	 */

	// fire every 15 seconds starting at 7am until 10pm, every day

	// second, minute, hour, day, month, weekday

	public void println(String str) {
		try {
			String isPrintDetail = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_PRINT_DETAIL_SCHEDULER);
			if (Parameter.YES_NO_yes.equals(isPrintDetail)) {
				System.out.println(str);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void runSchedulerByMethodName(String methodName) {
		try {
			threadLocalForceRun.set(new Boolean(true));
			
			if(methodName.indexOf(":")==-1){
				return;
			}
			String clazzName = StringUtils.split(methodName,":")[0];
			methodName = StringUtils.split(methodName,":")[1];
			Object baseService = WicketApplication.getBean(clazzName);
			
			Method method = baseService.getClass().getDeclaredMethod(methodName);
			method.invoke(baseService, new Object[] {});
			
			System.out.println("Success Execute:"+methodName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			threadLocalForceRun.remove();
		}
	}

	@Override
	public void runAllScheduler() {
//		try {
//			sendingEmailStatusPending();
//			checkRobFormBStatus();
//			checkRobFormCStatus();
////			processA1StatusPaymentSuccess();
////			processAStatusPaymentSuccess();
//			processBStatusPaymentSuccess();
//			processCmpStatusPendingPayment();
////			processA1StatusPendingPayment();
////			processAStatusPendingPayment();
//			processBStatusPendingPayment();
////			cancelA1FormExceed1DayDE();
//			processCStatusPaymentSuccess();
//			processCStatusWithoutPayment();
//			processCStatusPendingPayment();
//			cancelComtracDataEntry();
//			cancelComtracPendingPayment();
//			processComtracPendingPayment();
//			generatePaymentReceiptNumber();
////			cancelA1PendingPayment();
//		} catch (Exception ex) {
//			println("Error run all scheduler");
//		}

	}

	
	public boolean checkIsRunScheduler() {
		UserEnvironmentHelper.setUserEnvironment(null);

//		System.out.println("InitScheduler :");
		boolean isRunSchedulerBool = true;
		if (Parameter.YES_NO_no.equals(isRunScheduler)) {
			isRunSchedulerBool = false;
			if (threadLocalForceRun.get() != null && ((Boolean) threadLocalForceRun.get())) {
				isRunSchedulerBool = true;
			}
		}
		if (isRunSchedulerBool) {
			String methodName = "no method name";
			try {
				int stackInd = 2;
				String clazzName = StringUtils.removeEnd(Thread.currentThread().getStackTrace()[stackInd].getClassName(),"Impl");
				if(clazzName.indexOf("RobSchedulerService")!=-1) {
					stackInd=3;
					clazzName = StringUtils.removeEnd(Thread.currentThread().getStackTrace()[stackInd].getClassName(),"Impl");
				}
				
				
				clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
				methodName = clazzName+":"+ Thread.currentThread().getStackTrace()[stackInd].getMethodName();
				
				
//				if(methodName.indexOf("__ A W_")!=-1) {
//					methodName = StringUtils.replace(methodName, "__ A W_", "");
//				}
				System.out.println("Checking For :"+methodName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(mapMethod.get(methodName)!=null){
				Date dtRun = mapMethod.get(methodName);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				
//				Duration duration = new Duration( dtRun.getTime(), System.currentTimeMillis());
				System.out.println("Scheduler already running : "+methodName +" in "+sdf.format(dtRun));
				isRunSchedulerBool= false;  
			}else{
				mapMethod.put(methodName, new Date());
				System.out.println("RunningScheduler:" + methodName + "\t" + new Date());
			}
		}else {
//			System.out.println("Scheduler not running ");
		}
		return isRunSchedulerBool;
 
	}
 

//	22/01/2020 12:34:37 [system:RobSchedulerServiceImpl.java:__AW_checkIsRunScheduler:206] : Scheduler already running : RobSchedulerService:__AW_checkRobFormBStatus at 22/01/2020 12:32:01 for 2hours 35seconds
//	22/01/2020 12:34:37 [system:RobSchedulerServiceImpl.java:runSchedulerByMethodName:139] : Success Execute:checkRobFormBStatus


//	22/01/2020 12:03:21 [system:RobSchedulerServiceImpl.java:__AW_checkIsRunScheduler:200] : Scheduler already running :RobSchedulerService:checkIsRunScheduler at 1/22/20 12:00 PM for 2hours 59seconds
//	22/01/2020 12:03:21 [system:RobSchedulerServiceImpl.java:runSchedulerByMethodName:139] : Success Execute:checkRobFormAStatus


	
	

	public void updateHealthCheckStatus(String healthCheckStatus) {
		try {
			
			int stackInd = 2;
			String clazzName = StringUtils.removeEnd(Thread.currentThread().getStackTrace()[stackInd].getClassName(),"Impl");
			if(clazzName.indexOf("RobSchedulerService")!=-1) {
				stackInd=3;
				clazzName = StringUtils.removeEnd(Thread.currentThread().getStackTrace()[stackInd].getClassName(),"Impl");
			}
			
			clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
			String methodName = clazzName+":"+ Thread.currentThread().getStackTrace()[2].getMethodName();
			
			if(methodName.indexOf("updateHealthCheckStatus")!=-1) {
				methodName = clazzName+":"+ Thread.currentThread().getStackTrace()[3].getMethodName();
			}
//			
//			if(methodName.indexOf("__ A W_")!=-1) {
//				methodName = StringUtils.replace(methodName, "__ A W_", "");
//			}
//			CANCEL ALL O T C PAYMENT NITELY
			
			mapMethod.remove(methodName);
			System.out.println("Done:"+methodName);
			healthCheckService.updateHealthCheckStatus(methodName, healthCheckStatus);
		} catch (Exception e) {
			println(e.getMessage());
		}
	}


	@Override
	public void checkHealthAll() {
		checkWebServicesHealth();
		checkDatabaseHealth();
		checkMailHealth();
	}

//	@Scheduled(cron = DEFAULT_MINIT_DELAY)
	public void checkWebServicesHealth() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;

			try {
				EchoClient.echo(wSManagementService.getWsUrl("EchoClient.echo"), "");
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			} catch (Exception e) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(e.getMessage());
			}

			updateHealthCheckStatus(healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Scheduled(fixedDelay = DATABASE_HEALTH_CHECK_DELAY)
	public void checkDatabaseHealth() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				EchoClient.echo(wSManagementService.getWsUrl("EchoClient.echo"), "");
				healthCheckStatus = Parameter.HEALTH_CHECK_ok;
			} catch (Exception e) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(e.getMessage());
			}
			updateHealthCheckStatus(healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Scheduled(fixedDelay = MAIL_HEALTH_CHECK_DELAY)
	public void checkMailHealth() {
		try {
			if (!checkIsRunScheduler()) {
				return;
			}
			String healthCheckStatus = Parameter.HEALTH_CHECK_fail;
			try {
				InetAddress inet = InetAddress.getByName("mail.ssm.com.my");
				long start = System.currentTimeMillis();
				if (inet.isReachable(5000)) {
					long ms = System.currentTimeMillis() - start;
					healthCheckStatus = Parameter.HEALTH_CHECK_ok;
				} else {
					healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				}
			} catch (Exception e) {
				healthCheckStatus = Parameter.HEALTH_CHECK_fail;
				println(e.getMessage());
			}
			updateHealthCheckStatus(healthCheckStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	//FOR FORM A, FORM B and FORM C Only
	public void updateRobFormStatus(Object robForm, Object wsRespObject) throws Exception{

		String status = (String) getSimpleProperty(wsRespObject, "status");
		if(StringUtils.isBlank(status)){
			return;
		}
		
		String brNo = (String) getSimpleProperty(wsRespObject, "brNo");
		String cmpNo = (String) getSimpleProperty(wsRespObject, "compoundNo");
		String statusNotes = (String) getSimpleProperty(wsRespObject, "statusNotes");
		
		Date processDate = (Date) getSimpleProperty(wsRespObject, "processDate");
		
		String processBy = "";
		if(StringUtils.isNotBlank((String) getSimpleProperty(wsRespObject, "processBy"))){
			processBy = "SSM:"+(String) getSimpleProperty(wsRespObject, "processBy");
		}
		String processBranch = (String) getSimpleProperty(wsRespObject, "processBranch");
		String incentiveType = (String) getSimpleProperty(wsRespObject, "incentive");
		
		String robFormType = "";
		String robFormRefNo = "";
		String robFormQStatus = "";
		String cbsFormQStatus = "";
		
		String robFormApproveStatus = "";
		String cbsFormApproveStatus = "";
		
		String robFormRejectStatus = "";
		String cbsFormRejectStatus = "";
		

		String robFormInProcessStatus = "";
		String cbsFormInProcessStatus = "";
		String robFormCurrentStatus = "";
		
		if(robForm instanceof RobFormA){
			robFormType = Parameter.ROB_FORM_TYPE_A;
			robFormRefNo = ((RobFormA)robForm).getRobFormACode();
			robFormCurrentStatus = ((RobFormA)robForm).getStatus();
			robFormQStatus = Parameter.ROB_FORM_A_STATUS_QUERY;
			cbsFormQStatus = Parameter.CBS_ROB_FORM_A_STATUS_QUERY;
			robFormApproveStatus = Parameter.ROB_FORM_A_STATUS_APPROVED;
			cbsFormApproveStatus = Parameter.CBS_ROB_FORM_A_STATUS_APPROVED;
			robFormRejectStatus = Parameter.ROB_FORM_A_STATUS_REJECT;
			cbsFormRejectStatus = Parameter.CBS_ROB_FORM_A_STATUS_REJECT;
			robFormInProcessStatus = Parameter.ROB_FORM_A_STATUS_IN_PROCESS;
			cbsFormInProcessStatus = Parameter.CBS_ROB_FORM_A_STATUS_NEW;
		}
		if(robForm instanceof RobFormB){
			robFormType = Parameter.ROB_FORM_TYPE_B;
			robFormRefNo = ((RobFormB)robForm).getRobFormBCode();
			robFormCurrentStatus = ((RobFormB)robForm).getStatus();
			robFormQStatus = Parameter.ROB_FORM_B_STATUS_QUERY;
			cbsFormQStatus = Parameter.CBS_ROB_FORM_B_STATUS_QUERY;
			robFormApproveStatus = Parameter.ROB_FORM_B_STATUS_APPROVED;
			cbsFormApproveStatus = Parameter.CBS_ROB_FORM_B_STATUS_APPROVED;
			robFormRejectStatus = Parameter.ROB_FORM_B_STATUS_REJECT;
			cbsFormRejectStatus = Parameter.CBS_ROB_FORM_B_STATUS_REJECT;
			robFormInProcessStatus = Parameter.ROB_FORM_B_STATUS_IN_PROCESS;
			cbsFormInProcessStatus = Parameter.CBS_ROB_FORM_B_STATUS_NEW;
		}
		if(robForm instanceof RobFormC){
			robFormType = Parameter.ROB_FORM_TYPE_C;
			robFormRefNo = ((RobFormC)robForm).getRobFormCCode();
			robFormCurrentStatus = ((RobFormC)robForm).getStatus();
			robFormQStatus = Parameter.ROB_FORM_C_STATUS_QUERY;
			cbsFormQStatus = Parameter.CBS_ROB_FORM_C_STATUS_QUERY;
			robFormApproveStatus = Parameter.ROB_FORM_C_STATUS_APPROVED;
			cbsFormApproveStatus = Parameter.CBS_ROB_FORM_C_STATUS_APPROVED;
			robFormRejectStatus = Parameter.ROB_FORM_C_STATUS_REJECT;
			cbsFormRejectStatus = Parameter.CBS_ROB_FORM_C_STATUS_REJECT;
			robFormInProcessStatus = Parameter.ROB_FORM_C_STATUS_IN_PROCESS;
			cbsFormInProcessStatus = Parameter.CBS_ROB_FORM_C_STATUS_VERIFY;
		}
		
		
		if(StringUtils.isBlank(robFormRefNo)){
			System.err.println("Invalid Object");
			return;
		}
		
		if (cbsFormQStatus.equals(status) && StringUtils.isBlank(statusNotes)) {
			String defaultQueryMsj = WicketApplication.resolve("default.queryMsg");
			statusNotes = defaultQueryMsj;
		}else {
			try {
				statusNotes  = new String(statusNotes.getBytes("ISO-8859-1"), "UTF-8");//Try Convert Status Notes
			} catch (Exception e) {
			} 
		}
		
	//	System.out.println("--------------- "+robFormRefNo+" ---------------");
		if (cbsFormQStatus.equals(status)) {
			RobFormNotes formNotes = new RobFormNotes();
			formNotes.setNotes(statusNotes);
			formNotes.setRobFormCode(robFormRefNo);
			formNotes.setRobFormType(robFormType);
			formNotes.setQueryBy(processBy);
			
			if(StringUtils.isNotBlank(processBranch)){
				String contact = llpParametersService.findByCodeTypeValue(Parameter.BRANCH_CONTACT, processBranch);
				String branchName = StringUtils.split(contact,"|")[0];
				String phoneNo = StringUtils.split(contact,"|")[1];
				String userId = StringUtils.remove(processBy , "SSM:");
				if(StringUtils.isNotBlank(userId)){
					UamUserInfo uamUserInfo = wSUamClientService.findCBSProfileWithEzbizRole(userId);
					if(uamUserInfo!=null) {
						String contactInfo = WicketApplication.resolve("default.queryMsgContact",uamUserInfo.getVchusername(),phoneNo,branchName);
						statusNotes += "\n" + contactInfo;
					}
					
					formNotes.setNotes(statusNotes);
					
				}
				
			}
			
			robFormNotesService.insert(formNotes);

			setSimpleProperty(robForm, "approveRejectBranch", processBranch);
			setSimpleProperty(robForm, "approveRejectDt", processDate);
			setSimpleProperty(robForm, "approveRejectBy", processBy);
			setSimpleProperty(robForm, "status", robFormQStatus);

			
			if(robForm instanceof RobFormA){
				robFormAService.update((RobFormA) robForm);
				robFormAService.sendEmailFormAInQuery((RobFormA) robForm);
				
				//status cbs query
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equalsIgnoreCase(incentiveType)) { //if incentive oku
					List <RobIncentive> listRobIncentive = robIncentiveService.findListIncentiveByRobFormCode(((RobFormA) robForm).getRobFormACode()); 
					for (int i = 0; i < listRobIncentive.size(); i++) {
						RobIncentive robIncentive = listRobIncentive.get(i);
						robIncentive.setProcessingBranch(processBranch);
						robIncentive.setUpdateBy(processBy);
						robIncentive.setIncentiveApprovalDt(processDate);
						robIncentive.setApplicationStatus(robFormQStatus);
						//robIncentive.setBrNo(StringUtils.split(brNo, "-")[0]); //brNo BusinessFormAStatusResp (webis) come with "-"..
						//robIncentive.setCheckDigit(StringUtils.split(brNo, "-")[1]);
						robIncentiveService.update(robIncentive);
					}
				}
			}
			if(robForm instanceof RobFormB){
				robFormBService.update((RobFormB) robForm);
				robFormBService.sendEmailFormBInQuery((RobFormB) robForm);
			}
			if(robForm instanceof RobFormC){
				robFormCService.update((RobFormC) robForm);
				robFormCService.sendEmailFormCInQuery((RobFormC) robForm);
			}
			
		}
		
		if (cbsFormRejectStatus.equals(status)) {
			setSimpleProperty(robForm, "status", robFormRejectStatus);
			setSimpleProperty(robForm, "approveRejectBranch", processBranch);
			setSimpleProperty(robForm, "approveRejectDt", processDate);
			setSimpleProperty(robForm, "approveRejectBy", processBy);
			setSimpleProperty(robForm, "approveRejectNotes", statusNotes);
			
			if(robForm instanceof RobFormA){
				robFormAService.update((RobFormA) robForm);
				robFormAService.sendEmailFormAInReject((RobFormA) robForm);
				
				//status cbs reject
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equalsIgnoreCase(incentiveType)) { //if incentive oku
					List <RobIncentive> listRobIncentive = robIncentiveService.findListIncentiveByRobFormCode(((RobFormA) robForm).getRobFormACode()); 
					for (int i = 0; i < listRobIncentive.size(); i++) {
						RobIncentive robIncentive = listRobIncentive.get(i);
						robIncentive.setProcessingBranch(processBranch);
						robIncentive.setUpdateBy(processBy);
						robIncentive.setIncentiveApprovalDt(processDate);
						robIncentive.setApplicationStatus(robFormRejectStatus);
						//robIncentive.setBrNo(StringUtils.split(brNo, "-")[0]); //brNo BusinessFormAStatusResp (webis) come with "-"..
						//robIncentive.setCheckDigit(StringUtils.split(brNo, "-")[1]);
						robIncentiveService.update(robIncentive);
					}
				}
			}
			if(robForm instanceof RobFormB){
				robFormBService.update((RobFormB) robForm);
				robFormBService.sendEmailFormBInReject((RobFormB) robForm);
			}
			if(robForm instanceof RobFormC){
				robFormCService.update((RobFormC) robForm);
				robFormCService.sendEmailFormCInReject((RobFormC) robForm);
			}
		}

		if (cbsFormApproveStatus.equals(status)) {
			
			setSimpleProperty(robForm, "status", robFormApproveStatus);
			setSimpleProperty(robForm, "approveRejectBranch", processBranch);
			setSimpleProperty(robForm, "approveRejectDt", processDate);
			setSimpleProperty(robForm, "approveRejectBy", processBy);
			setSimpleProperty(robForm, "approveRejectNotes", statusNotes);
			
			if(robForm instanceof RobFormA){
				RobFormA robFormA = (RobFormA) robForm;
				robFormA.setCompoundNo(cmpNo);
				robFormA.setBrNo(brNo);
				
				robFormAService.update(robFormA);
				try {
					robFormA = robFormAService.generateBorangAForm(robFormA);
				} catch (Exception e) {
					println(e.getMessage());
				}
				
				try {
					robFormA = robFormAService.getFormACertDataFromWS(robFormA);
				} catch (Exception e) {
					println(e.getMessage());
				}
				
				try {
					if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
						robFormA = robFormAService.getFormABusinessInfoDataFromWS(robFormA);
					}
				} catch (Exception e) {
					println(e.getMessage());
				}
				robFormAService.sendEmailFormAInApproved(robFormA);
				
				//cbs status Approved
				if(Parameter.ROB_FORM_A_INCENTIVE_oku.equalsIgnoreCase(incentiveType)) { //if incentive oku
					List <RobIncentive> listRobIncentive = robIncentiveService.findListIncentiveByRobFormCode(robFormA.getRobFormACode()); 
					for (int i = 0; i < listRobIncentive.size(); i++) {
						RobIncentive robIncentive = listRobIncentive.get(i);
						robIncentive.setProcessingBranch(processBranch);
						robIncentive.setUpdateBy(processBy);
						robIncentive.setIncentiveApprovalDt(processDate);
						robIncentive.setApplicationStatus(robFormApproveStatus);
						robIncentive.setBrNo(StringUtils.split(robFormA.getBrNo(), "-")[0]); //brNo BusinessFormAStatusResp (webis) come with "-"..
						robIncentive.setCheckDigit(StringUtils.split(robFormA.getBrNo(), "-")[1]);
						robIncentiveService.update(robIncentive);
					}
				}
			}
			if(robForm instanceof RobFormB){
				robFormBService.updateStatusApproved((RobFormB) robForm, processBy, processBranch,  processDate, statusNotes);
				robFormBService.sendEmailFormBInApproved((RobFormB) robForm);
			}
			if(robForm instanceof RobFormC){
				robFormCService.update((RobFormC) robForm);
				robFormCService.sendEmailFormCInApproved((RobFormC) robForm); //fix send approve email
			}
			
		}
		if (!robFormInProcessStatus.equals(robFormCurrentStatus) && cbsFormInProcessStatus.equals(status)) {
			setSimpleProperty(robForm, "status", robFormInProcessStatus);
			setSimpleProperty(robForm, "submitDt", new Date());
			
			if(robForm instanceof RobFormA){
				robFormAService.update((RobFormA) robForm);
				robFormAService.sendEmailFormAInProcess((RobFormA) robForm);
				
				//jika masih inProcess takyah update rob_incentive..
			/*	if(Parameter.ROB_FORM_A_INCENTIVE_oku.equalsIgnoreCase(incentiveType)) { //if incentive oku
					List <RobIncentive> listRobIncentive = robIncentiveService.findListIncentiveByRobFormCode(((RobFormA) robForm).getRobFormACode()); 
					for (int i = 0; i < listRobIncentive.size(); i++) {
						RobIncentive robIncentive = listRobIncentive.get(i);
						//robIncentive.setProcessingBranch(processBranch);
						//robIncentive.setUpdateBy(processBy);
						//robIncentive.setIncentiveApprovalDt(processDate);
						robIncentive.setApplicationStatus(robFormInProcessStatus);
						//robIncentive.setBrNo(StringUtils.split(brNo, "-")[0]); //brNo BusinessFormAStatusResp (webis) come with "-"..
						//robIncentive.setCheckDigit(StringUtils.split(brNo, "-")[1]);
						robIncentiveService.update(robIncentive);
					}
				} */
			}
			if(robForm instanceof RobFormB){
				robFormBService.update((RobFormB) robForm);
				robFormBService.sendEmailFormBInProcess((RobFormB) robForm);
			}
			if(robForm instanceof RobFormC){
				robFormCService.update((RobFormC) robForm);
				robFormCService.sendEmailFormCInProcess((RobFormC) robForm);
			}
			
		}
	}
	private Object getSimpleProperty(Object obj, String fieldName){
		try {
			return PropertyUtils.getSimpleProperty(obj, fieldName);
		} catch (Exception e) {
		}
		return null;
	}
	void setSimpleProperty(Object obj, String fieldName, Object value){
		try {
			if(value==null){
				return;
			}
			if(value instanceof String && StringUtils.isBlank((String) value)){
				return;
			}
			PropertyUtils.setSimpleProperty(obj, fieldName, value);
		} catch (Exception e) {
			
		}
	}
	

}
