package com.ssm.llp.base.page;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.ssm.controller.token.MB_ErrorCode;
import com.ssm.controller.token.MB_UnauthorizedException;
import com.ssm.controller.token.TokenController;
import com.ssm.controller.token.TokenDataModel;
import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.robFormB.ViewRobFormBPage;
import com.ssm.ezbiz.robFormC.EditRobFormCPage;
import com.ssm.ezbiz.robFormC.ViewRobFormCPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.ExtUserPairingInfoService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormTransactionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.ezbiz.model.ExtUserPairingInfo;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormTransactionModel;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.GenerateLlpUserProfilePage;
import com.ssm.llp.mod1.page.ListPaymentTransactionPage;
import com.ssm.llp.mod1.page.VerificationLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;
import com.ssm.webis.param.BizOwnerInfo;
import com.ssm.webis.param.BusinessInfo;

@SuppressWarnings("serial")
public class ExtInterface extends WebPage implements Serializable {
	@SpringBean(name = "RobRenewalService")
	RobRenewalService robRenewalService;
	
	@SpringBean(name = "RobFormAService")
	RobFormAService robFormAService;
	
	@SpringBean(name = "RobFormBService")
	RobFormBService robFormBService;
	
	@SpringBean(name = "RobFormCService")
	RobFormCService robFormCService;
	
	@SpringBean(name = "RobFormTransactionService")
	RobFormTransactionService robFormTransactionService;	
	
	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	
	@SpringBean(name="ExtUserPairingInfoService")
	private ExtUserPairingInfoService extUserPairingInfoService;
	
	@SpringBean(name="LlpParametersService")
	LlpParametersService llpParametersService;
	
	public static final String FORMANEW = "FORMANEW";
	public static final String FORMA = "FORMA";
	public static final String FORMA1NEW = "FORMA1NEW";
	public static final String FORMA1 = "FORMA1";
	public static final String FORMBNEW = "FORMBNEW";
	public static final String FORMB = "FORMB";
	public static final String FORMCNEW = "FORMCNEW";
	public static final String FORMC = "FORMC";
	public static final String FORMAPVER = "FORMAPVER";
	public static final String FORMBPVER = "FORMBPVER";
	public static final String FORMCPVER = "FORMCPVER";
	public static final String LISTPYMTTRANS = "LISTPYMTTRANS";
	public static final String PAIRID = "PAIRID";
	public static final String REGUSER = "REGUSER";
	public static final String FOGPWD = "FOGPWD";
	
	public static final String ERRORPARAMETER = "Invalid Parameter";
	public static final String ERRORINVALIDUSER = "Invalid User";
	public static final String ERRORCANNOTSIGNIN = "Cannot Sign In User";
	public static final String ERRORICNOTOWNER = "IC No is not owner of the business";
	public static final String ERRORPENDINGTRAN = "You have pending transaction with reference no";
	
	
	public static final String EXT_TOKEN_DATA_MODEL = "EXT_TOKEN_DATA_MODEL";
	public static final String EXT_TOKEN = "EXT_TOKEN";
	
	
	
	private SSMMessagesFeedbackPanel feedbackPanel ;
	
	public void init() {
		feedbackPanel = new SSMMessagesFeedbackPanel("feedback");
 		feedbackPanel.setOutputMarkupId(true);
 		add(feedbackPanel);
		
	}
	
	public ExtInterface() {
		init();
	}
	

	public ExtInterface(String message) {
		init();
//		if(StringUtils.isNotBlank(message)) {
			ssmError(message);
//		}
			
			
	}
	
	
	public ExtInterface(PageParameters param) {
		init();
		String enableIFace = llpParametersService.findByCodeTypeValue(Parameter.EXT_CONFIG_MAMPU, Parameter.EXT_CONFIG_ENABLE_IFACE);
		if(!Parameter.YES_NO_yes.equals(enableIFace)) {
			ssmError("Interface not enabled");
			return;
		}
		
 		SignInSession signInSession = (SignInSession)getSession();
//		signInSession.invalidate();
		
//		signInSession = (SignInSession)getSession();
 		signInSession.setLoginType(Parameter.LOGIN_TYPE_interface);
 		signInSession.setLoginSource(Parameter.LOGIN_SOURCE_MSIA_BIZ);
		StringValue t=param.get("t");
		StringValue token=param.get("token");
		String operation = null;
		String refNo = null;
		String brNo = null;	
		
		//Validate token
		TokenController tokenController = new TokenController();
		TokenDataModel tdm = new TokenDataModel();
		try {
			tdm = tokenController.validateTokenAndExtractData(token.toString());
		} catch (MB_UnauthorizedException e) {
			e.printStackTrace();
			if(e.getMessage()==null) {
				if(e instanceof MB_UnauthorizedException) {
					ssmError(MB_ErrorCode.description(((MB_UnauthorizedException) e).getStatusCode()));
				}else {
					ssmError(e.getMessage());
				}
			}else {
				ssmError(e.getMessage());
			}
		}
		
		if("1".equals(t.toString())) {			
			operation = StringUtils.isNotBlank(param.get("operation").toString())?param.get("operation").toString():null;
			refNo = StringUtils.isNotBlank(param.get("refNo").toString())?param.get("refNo").toString():null;
			brNo = StringUtils.isNotBlank(param.get("brNo").toString())?param.get("brNo").toString():null;
		}else {
			operation = StringUtils.isNotBlank(tdm.getOperation())?tdm.getOperation():null;
			refNo = StringUtils.isNotBlank(tdm.getRefNo())?tdm.getRefNo():null;
			brNo = StringUtils.isNotBlank(tdm.getBrNo())?tdm.getBrNo():null;
		}			
		
		System.out.println("TOKEN >> "+token);
		System.out.println("OPERATION >> "+operation);
		System.out.println("REF NO >> "+refNo);
		System.out.println("BR NO >> "+brNo);
		System.out.println("EZBIZ USER ID >> "+tdm.getNric());
		
		try {
			if(token != null && operation != null) {
				signInSession.setAttribute(EXT_TOKEN_DATA_MODEL, tdm);
				signInSession.setAttribute(EXT_TOKEN, token.toString());
				
				if(PAIRID.equals(operation) ) {
//					UserEnvironment userEnv = (UserEnvironment) signInSession.getAttribute("UserEnvironment");
//					if(userEnv != null && !(userEnv instanceof InternalUserEnviroment) ){
//							ssmError("Already Pair");
//							return;
//					}else {
						setResponsePage(new HomePage(true));
						return;
//					}
				}else if(REGUSER.equals(operation) ){
					//check IC if already have
					LlpUserProfile uP = llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, tdm.getNric());
					
					if(uP!=null) {
						ssmError("MYKAD "+tdm.getNric()+" already register as EzBiz User");
						return;
					}else {
						setResponsePage(VerificationLlpUserProfilePage.class ) ;
						return;
					}
				}else if(FOGPWD.equals(operation)){
					setResponsePage(GenerateLlpUserProfilePage.class);
					return;
				}
				
				//Validate session
				if(tdm.getVli() != null){
					
					ExtUserPairingInfo extUserPairingInfo = extUserPairingInfoService.findByLatestSessionByEzBizId(tdm.getVli());
					
					//TODO  Validate with database, if not exist please do link ID 
//					LlpUserProfile userProfile = llpUserProfileService.findProfileInfoByUserId(userID);
					
					if(extUserPairingInfo != null) {
//						SignInSession signInSession = (SignInSession)getSession();
//						signInSession.setLoginType(Parameter.LOGIN_TYPE_interface);
//		        		signInSession.setNoNeedPassword(true);
//		        		signInSession.setSignInForm();
						if(!"A".equals(extUserPairingInfo.getStatus()) ) {
							ssmError(extUserPairingInfo.getEzbizId() + "status not active please pair again");
							return;
						}
						
						System.out.println("EZBIZ USER ID >> "+extUserPairingInfo.getEzbizId());
						
		                // Sign the user in no need password
						signInSession.setNoNeedPassword(true);
		                if (signInSession.signIn(extUserPairingInfo.getEzbizId(), null))
		                {
		                	LlpUserLog userLog = new LlpUserLog();
		                	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
		                	userLog.setLoginTime(new Date());
		                
		                	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
		                	String ipAddress = WicketUtils.getIpAddress(request, getSession());
		                    userLog.setIpAddress(ipAddress);
		                    
//		                    llpUserLogService.insert(userLog);
		                    UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
		                }else {
		                	ssmError(ERRORCANNOTSIGNIN);
		                	return;
		                }
		                
		                LlpUserProfile userProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
		                           
		                if(StringUtils.isNotBlank(brNo)) {
		                	boolean isErrIcNotOwner = false;
		                	//Check form brNo is belong to owner  
		                	if(!FORMANEW.equals(operation) && !FORMA.equals(operation)) {
		                		
		                		//bypass check for terminate business
		                		if(FORMC.equals(operation) && null!=refNo) {
		                			RobFormC robFormC = robFormCService.findById(refNo);
			                		if(robFormC != null) {
										if(Parameter.ROB_FORM_C_STATUS_APPROVED.equals(robFormC.getStatus())|| Parameter.ROB_FORM_C_STATUS_REJECT.equals(robFormC.getStatus())){
											isErrIcNotOwner = false;
										}
									}
		                		}else if(!isOwner(userProfile.getIdNo(), brNo)) {
		                			isErrIcNotOwner = true;
			                	}
			                	
			                	if(isErrIcNotOwner) {
			                		ssmError(ERRORICNOTOWNER);
				                	return;
		                		}
		                	}
		                	
		                	//Check pending trans based on brNo
			                String pendingRefNo = getPendingTransaction(brNo);
			                if(StringUtils.isNotBlank(pendingRefNo) && !pendingRefNo.equals(refNo)) {
			                	boolean isErrPending = true;
			                	//if current refno is approve or reject, bypass
			                	if(FORMA.equals(operation) && null!=refNo) {
			                		RobFormA robFormA = robFormAService.findById(refNo);
			                		if(robFormA != null) {
										if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(robFormA.getStatus())|| Parameter.ROB_FORM_A_STATUS_REJECT.equals(robFormA.getStatus())){
											isErrPending = false;
										}
									}
			                	}else if(FORMA1.equals(operation) && null!=refNo) {
			                		RobRenewal robRenewal = robRenewalService.findById(refNo);
			                		if(robRenewal != null) {
										if(Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus())){
											isErrPending = false;
										}
									}
			                	} else if(FORMB.equals(operation) && null!=refNo) {
			                		RobFormB robFormB = robFormBService.findById(refNo);
			                		if(robFormB != null) {
										if(Parameter.ROB_FORM_B_STATUS_APPROVED.equals(robFormB.getStatus())|| Parameter.ROB_FORM_B_STATUS_REJECT.equals(robFormB.getStatus())){
											isErrPending = false;
										}
									}
			                	} else if(FORMC.equals(operation) && null!=refNo) {
			                		RobFormC robFormC = robFormCService.findById(refNo);
			                		if(robFormC != null) {
										if(Parameter.ROB_FORM_C_STATUS_APPROVED.equals(robFormC.getStatus())|| Parameter.ROB_FORM_C_STATUS_REJECT.equals(robFormC.getStatus())){
											isErrPending = false;
										}
									}
			                	}
			                	
			                	if(isErrPending) {
			                		ssmError(ERRORPENDINGTRAN+" "+pendingRefNo);
				                	return;
			                	}
			                	
			                }	
		                }
		                
		              //Check operation
		                if(FORMANEW.equals(operation)) {										
//							EditRobFormAPage formANew = new EditRobFormAPage();
							setResponsePage(new EditRobFormAPage());
						}else if(FORMA.equals(operation) && null!=refNo) {
							RobFormA robFormA = robFormAService.findById(refNo);							
							
							if(robFormA != null) {
								if(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(robFormA.getStatus())|| Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())){
									setResponsePage(new EditRobFormAPage(robFormA.getRobFormACode()));
								}else{
									setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), getPage()));
								}
							}else {
								ssmError(ERRORPARAMETER);
							}
						}else if(FORMA1NEW.equals(operation) && null != brNo) {
							
							BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(brNo);
							if(null != businessInfo) {
								setResponsePage(new EditRobRenewalPage(businessInfo));
							}else {
								ssmError(ERRORPARAMETER);
							}
							
						}else if(FORMA1.equals(operation) && null != refNo && null!=brNo) {
							RobRenewal robRenewal = robRenewalService.findById(refNo);
							if(null != robRenewal) {
								setResponsePage(new EditRobRenewalPage(robRenewal));
							}else {
								ssmError(ERRORPARAMETER);
							}										
						}else if(FORMBNEW.equals(operation) && null != brNo) {
							BusinessInfo businessInfo = new BusinessInfo();
							businessInfo.setBrNo(brNo);
							setResponsePage(new EditRobFormBPage(businessInfo));					
						}else if(FORMB.equals(operation) && null != refNo && null!=brNo) {
							
							RobFormB robFormB = robFormBService.findById(refNo);
							
							if(robFormB != null) {
								if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
									setResponsePage(new EditRobFormBPage(robFormB.getRobFormBCode()));
								}else{
									setResponsePage(new ViewRobFormBPage(robFormB.getRobFormBCode(), getPage()));
								}
							}else {
								ssmError(ERRORPARAMETER);
							}
							
						}else if(FORMCNEW.equals(operation) && null != brNo) {
							BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(brNo);
							if(null != businessInfo) {
								setResponsePage(new EditRobFormCPage(businessInfo));
							}else {
								ssmError(ERRORPARAMETER);
							}
							
						}else if(FORMC.equals(operation) && null != refNo && null!=brNo) {
							RobFormC robFormC = robFormCService.findById(refNo);
							
							if(robFormC != null) {
								if(Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormC.getStatus()) || Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
									setResponsePage(new EditRobFormCPage(robFormC.getRobFormCCode()));
								}else{
									setResponsePage(new ViewRobFormCPage(robFormC.getRobFormCCode(), getPage()));
								}
							}else {
								ssmError(ERRORPARAMETER);
							}
							
						}else if(FORMAPVER.equals(operation.toString()) && null != refNo) {
							setResponsePage(new ViewRobFormAPage2(refNo, getPage()));
						}else if(FORMBPVER.equals(operation.toString()) && null != refNo) {
							setResponsePage(new ViewRobFormBPage(refNo, getPage()));
						}else if(FORMCPVER.equals(operation.toString()) && null != refNo) {
							setResponsePage(new ViewRobFormCPage(refNo, getPage()));
						}else if(LISTPYMTTRANS.equals(operation.toString())) {
							setResponsePage(new ListPaymentTransactionPage());
						}else {
							ssmError(ERRORPARAMETER);
						}
						
					}else {
						ssmError(ERRORINVALIDUSER);
					}
				}else {
					ssmError(ERRORINVALIDUSER);
				}
			}else {
				ssmError(ERRORPARAMETER);
			}
		} catch (Exception e) {
			ssmError(e.getMessage());
		}
		
		System.out.println("SS:"+feedbackPanel.getFeedbackMessages().size());
	}
	
	//TODO Check form brNo belong to IC
	public boolean isOwner(String icNo, String brNo) {
		try {
		BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(brNo);
				
		if(null != businessInfo && businessInfo.getListOwner().length > 0) {
			
			BizOwnerInfo[] businessOwner = businessInfo.getListOwner();
			for (int i = 0; i < businessOwner.length; i++) {
				if(icNo.equals(businessOwner[i].getIcNo())) {
					return true;
				}
			}
		}
		}catch (Exception e) {
			//e.printStackTrace();
			ssmError(ERRORICNOTOWNER);
		}
		
		return false;
	}
	
	//TODO Check pending trans based on brNo
    /*
     * if new renewal (A1)
     * check pending trans A1,B,C based on brNo
     * 
     * if new changes (B)
     * check pending trans A1,B,C based on brNo
     * 
     * if new terminate (C)
     * check pending trans A1,B,C based on brNo
     */
	public String getPendingTransaction(String brNo) {
		try {
			List<RobFormTransactionModel> listPendingTransaction = robFormTransactionService.findPendingTransaction(brNo);
			
			if(!listPendingTransaction.isEmpty()){
				RobFormTransactionModel robFormTransactionModel = listPendingTransaction.get(0);
				return robFormTransactionModel.getAppRefNo();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)throws Exception {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ3d3cubWFsYXlzaWFiaXoubWFtcHUuZ292Lm15IiwiaWF0IjoxNTcxNzk4MjAzLCJleHAiOjE1OTc3MTgyMDMsImV4cGlyZWQiOjE1OTc3MTgyMDMsIm5yaWMiOiI4NTA0MDIwODU5NzYiLCJ2bGkiOiJGQUhNSTQifQ.GiFkFtMmwP1CyH-M6-zMTIru9mOdb_2EiVfcLcz0NR0wxWg6_DjvJf44KSlEnMFSpnKa0aofgcYX3sDHm4K9S9p76xzJL2u_dXf7-pw2cFCYDTX2CtOksZqz3ZDGFyHueufw6leXYQC2gENAs__ci5qQq2WzMEwcRYBLwsPXa3f9wZmBgjfcBHxLcd1hVMOUpXFoeAn-MOYTnp18PK1e82TewhptQC0-xBFZg4xHcyXxYAxMWSc3oTR6YJRzhijhoYOSnE79-I8jGuk3GYb-tXqKfr5uTw9zxb_1NA5KuL6B0Z_AWzhzwnc1H7ZYkuSIpmMkdfgMYuJ0W5XOFA-MGA";
		
		
		TokenController tokenController = new TokenController();
//		String ic = tokenController.validateToken(token);
//		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		String ret = "";
		try {
			ret = tokenController.validateTokenExtractVli(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("RET = "+ret);
	}
	

}

