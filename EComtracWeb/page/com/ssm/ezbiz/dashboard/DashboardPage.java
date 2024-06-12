package com.ssm.ezbiz.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.base.common.code.CommonConstant;
import com.ssm.ezbiz.errorlog.MyInternalErrorPage;
import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.robFormC.EditRobFormCPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormTransactionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.AlertPanel;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.GuidelinePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.ws.RobBusinessSummaryInfo;

@SuppressWarnings("serial")
public class DashboardPage extends SecBasePage {

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	

	@SpringBean(name = "RobFormTransactionService")
	private RobFormTransactionService robFormTransactionService;

	@SuppressWarnings("unchecked")
	public DashboardPage() {
		try {
			String enableDashboard = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_DASHBOARD);
			
			if( !Parameter.YES_NO_yes.equals(enableDashboard) ) {
				setResponsePage(GuidelinePage.class);
				return;
			}
			
			LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			if( !Parameter.USER_STATUS_active.equals(profile.getUserStatus())) {
				setResponsePage(new GuidelinePage());
				return;
			}
			 
			SSMAjaxLink newRegistration = new SSMAjaxLink("newRegistration") {
				@Override
				public void onClick(AjaxRequestTarget arg0) {
						setResponsePage(new EditRobFormAPage());
				}
			};
			add(newRegistration);
			
			
			SSMLabel webisError = new SSMLabel("webisError", resolve("page.title.dashboard.webisError"));
			webisError.setOutputMarkupId(true);
			add(webisError);
			
			webisError.setVisible(false);
			
			
			final AlertPanel alertPanel = new AlertPanel("alert");
			add(alertPanel);
			
			
//			long start = System.currentTimeMillis();
			final LatestTransactionPanel latestTransactionPanel = new LatestTransactionPanel("latestTransactionPanel", getPage());
			add(latestTransactionPanel);
//			System.out.println("Load Latest in : "+(System.currentTimeMillis()-start));
			
			
			WebMarkupContainer wmcListBizness = new WebMarkupContainer("wmcListBizness");
//			wmcAddress.setPrefixLabelKey("page.lbl.ezbiz.robFormA.");
			wmcListBizness.setOutputMarkupId(true);
			add(wmcListBizness);

			try {
//				start = System.currentTimeMillis();
				List<RobBusinessSummaryInfo> businessInfoListTmp = robRenewalService.findAllBizDetailByIcWS(profile.getIdNo());
//				System.out.println("Webis call in : "+(System.currentTimeMillis()-start));
				
				ListView<RobBusinessSummaryInfo> dataView = new ListView<RobBusinessSummaryInfo>("sorting", businessInfoListTmp) {
					@Override
					protected void populateItem(ListItem<RobBusinessSummaryInfo> item) {
						final RobBusinessSummaryInfo businessInfo = item.getModelObject();
						boolean isBizExpired = false;
						boolean isBizExpired3Month = false;
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date bizEndDate = businessInfo.getBizExpDate();
							Calendar cal = Calendar.getInstance();
							cal.setTime(bizEndDate);
							cal.set(Calendar.MONTH, 3);
							Date today = new Date();
							if (bizEndDate.before(today)) {// biz already expired
								isBizExpired = true;
							}else if (cal.getTime().before(today)) {// biz exp 3month
								isBizExpired3Month = true;
							}
						} catch (Exception e) {
						}

						item.add(new SSMLabel("brNo", businessInfo.getBrNo() + "-" + businessInfo.getChkDigit()));
						item.add(new SSMLabel("brName", businessInfo.getBizName()));

						SSMLabel expDateLbl = new SSMLabel("expDate", businessInfo.getBizExpDate(), "dd MMM yyyy");
						item.add(expDateLbl);

						if (isBizExpired) {
							expDateLbl.add(new AttributeAppender("style", "color:red; font-weight:bold"));
						}else if (isBizExpired3Month) {
							expDateLbl.add(new AttributeAppender("style", "color:Orange; font-weight:bold"));
						}else {
							expDateLbl.add(new AttributeAppender("style", "font-weight:bold"));
						}

						String currentMainAddress = businessInfo.getBizMainAddr().toUpperCase();
						if (StringUtils.isNotBlank(businessInfo.getBizMainAddr2())) {
							currentMainAddress += "\n" + businessInfo.getBizMainAddr2().toUpperCase();
						}
						if (StringUtils.isNotBlank(businessInfo.getBizMainAddr3())) {
							currentMainAddress += "\n" + businessInfo.getBizMainAddr3().toUpperCase();
						}
						currentMainAddress += "\n" + businessInfo.getBizMainAddrPostcode() + " " + businessInfo.getBizMainAddrTown().toUpperCase();
						currentMainAddress += "\n" + getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, businessInfo.getBizMainAddrState());

						MultiLineLabel currentMainAddressLabel = new MultiLineLabel("mainAddr", currentMainAddress);
						item.add(currentMainAddressLabel);
						
						
						final WebMarkupContainer wmcDetails = new WebMarkupContainer("wmcDetails");
						wmcDetails.setOutputMarkupId(true);
						wmcDetails.setOutputMarkupPlaceholderTag(true);
						item.add(wmcDetails);

						String[] status = { Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT, Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS };
						SearchCriteria sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, businessInfo.getBrNo());
						sc = sc.andIfInNotNull("status", status, false);

						
						
						final SSMAjaxLink editRenew = new SSMAjaxLink("editRenew") {
							@Override
							public void onClick(AjaxRequestTarget arg0) {
								String[] status = { Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT, Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS };
								SearchCriteria sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, businessInfo.getBrNo());
								sc = sc.andIfInNotNull("status", status, false);

								List<RobRenewal> renewalList = robRenewalService.findByCriteria(sc).getList();

								if (renewalList.size() > 0) {
									String confirmTitle = resolve("error.renewal.dataAlreadyExistTitle");
									String confirmDesc = resolve("error.renewal.dataAlreadyExistDesc");
									
									DashboardRenewalAlert dashboardRenewalAlert = new DashboardRenewalAlert("additionalMsg", renewalList);
									alertPanel.resetAlert(confirmTitle, confirmDesc ,dashboardRenewalAlert, arg0);
									
									String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
									arg0.appendJavaScript(jScript);
								} else {

									String confirmTitle = resolve("error.renewal.errorTitle");
									
									try {
										BusinessInfo renewalInfo = robRenewalService.findBusinessByRegNoWS( businessInfo.getBrNo(), businessInfo.getChkDigit() );
										String errorKey = "page.title.mybiz.error.invalidBiz";
										if(CommonConstant.NO.equals(renewalInfo.getCanRenew())){
											error(getLocaleMsg(errorKey));
										}else{
											setResponsePage(new EditRobRenewalPage(renewalInfo));
										}
									} catch (SSMException e) {
										DashboardRenewalAlert dashboardRenewalAlert = new DashboardRenewalAlert("additionalMsg", renewalList);
										alertPanel.resetAlert(confirmTitle, e.getMessage() ,dashboardRenewalAlert, arg0);
										
										String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
										arg0.appendJavaScript(jScript);
									}
									
									
								}
							}

						};
						

						final SSMAjaxLink makeChanges = new SSMAjaxLink("makeChanges") {
							@Override
							public void onClick(AjaxRequestTarget arg0) {
								boolean isBizExpired = false;
								try {
									Date bizEndDate = businessInfo.getBizExpDate();
									if (bizEndDate.before(new Date())) {// biz
																		// already
																		// expired
										isBizExpired = true;
									}
								} catch (Exception e) {
								}

								boolean canProceed = true;
								String errorTitle = "";
								String errorDesc = "";
								if (isBizExpired) {
									canProceed = false;
									errorTitle = resolve("page.lbl.ezbiz.robFormB.canRenewBizExpiredRenewFirstTitle");
									errorDesc = resolve("page.lbl.ezbiz.robFormB.canRenewBizExpiredRenewFirstDesc");
									
									alertPanel.resetAlert(errorTitle, errorDesc ,arg0);
									String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
									arg0.appendJavaScript(jScript);
								} else {
									List<RobFormB> list = robFormBService.findPendingApplication(businessInfo.getBrNo());
									errorTitle = resolve("page.lbl.ezbiz.robFormB.pendingFormBTitle");
									errorDesc =  resolve("page.lbl.ezbiz.robFormB.pendingFormBDesc");
									
									if(list.size()>0){
										canProceed = false;
										DashboardFormBAlert dashboardFormBAlert = new DashboardFormBAlert("additionalMsg", list);
										alertPanel.resetAlert(errorTitle, errorDesc ,dashboardFormBAlert, arg0);
										
										String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
										arg0.appendJavaScript(jScript);
									}
								}

								if (canProceed) {
									setResponsePage(new EditRobFormBPage(businessInfo));
								}

							}
						};
						
						
						final SSMAjaxLink terminate = new SSMAjaxLink("terminate") {
							@Override
							public void onClick(AjaxRequestTarget arg0) {
								boolean isBizExpired = false;
								try {
									Date bizEndDate = businessInfo.getBizExpDate();
									if (bizEndDate.before(new Date())) {// biz
																		// already
																		// expired
										isBizExpired = true;
									}
								} catch (Exception e) {
								}

								boolean canProceed = true;
								String errorTitle = "";
								String errorDesc = "";
								if (isBizExpired) {
									canProceed = false;
									errorTitle = resolve("page.lbl.ezbiz.robFormC.canRenewBizExpiredRenewFirstTitle");
									errorDesc = resolve("page.lbl.ezbiz.robFormC.canRenewBizExpiredRenewFirstDesc");
									
									alertPanel.resetAlert(errorTitle, errorDesc ,arg0);
									
									String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
									arg0.appendJavaScript(jScript);
								} else {
									
									List<RobFormC> list = robFormCService.findPendingApplication(businessInfo.getBrNo());
									
									if (list.size() > 0) {
										canProceed = false;
										errorTitle = resolve("page.lbl.ezbiz.robFormC.pendingFormCTitle");
										errorDesc = resolve("page.lbl.ezbiz.robFormC.pendingFormCDesc");
										
										DashboardFormCAlert dashboardFormCAlert = new DashboardFormCAlert("additionalMsg", list);
										alertPanel.resetAlert(errorTitle, errorDesc ,dashboardFormCAlert, arg0);
										
										String jScript = "showModal('"+alertPanel.getWmcAlertId()+"');";
										arg0.appendJavaScript(jScript);
									}
								}

								if (canProceed) {
									setResponsePage(new EditRobFormCPage(businessInfo)); 
								}

							}
						};
						
						
						wmcDetails.add(new SSMLabel("brNo", businessInfo.getBrNo() + "-" + businessInfo.getChkDigit()));
						wmcDetails.add(new SSMLabel("brName", businessInfo.getBizName()));
						wmcDetails.add(new MultiLineLabel("mainAddr", currentMainAddress));
						SSMLabel expDateLbl2 = new SSMLabel("expDate", businessInfo.getBizExpDate());
						wmcDetails.add(expDateLbl2);

						if (isBizExpired) {
							expDateLbl2.add(new AttributeAppender("style", "color:red; font-weight:bold"));
						}
						
						MultiLineLabel bizDesc = new MultiLineLabel("bizDesc", businessInfo.getBizDesc());
						wmcDetails.add(bizDesc);
						
						
						
						editRenew.setVisible(false);
						makeChanges.setVisible(false);
						terminate.setVisible(false);
						
						wmcDetails.add(editRenew);
						wmcDetails.add(makeChanges);
						wmcDetails.add(terminate);
						
						
						PendingTransactionPanel latestTransactionPanel = new PendingTransactionPanel("pendingTransactionPanel", new ArrayList(), getPage());
						wmcDetails.add(latestTransactionPanel);
						
						SSMAjaxLink showDetails = new SSMAjaxLink("showDetails") {
							@Override
							public void onClick(AjaxRequestTarget arg0) {
								String jScript = "showModal('"+wmcDetails.getMarkupId()+"');";
								arg0.appendJavaScript(jScript);
								
								List listPendingTransaction = robFormTransactionService.findPendingTransaction(businessInfo.getBrNo());
								PendingTransactionPanel latestTransactionPanel = new PendingTransactionPanel("pendingTransactionPanel", listPendingTransaction, getPage());
								wmcDetails.replace(latestTransactionPanel);
								
								
								if(listPendingTransaction.size()>0) {
									editRenew.setVisible(false);
									makeChanges.setVisible(false);
									terminate.setVisible(false);
								}else {
									editRenew.setVisible(true);
									makeChanges.setVisible(true);
									terminate.setVisible(true);
								}
								arg0.add(wmcDetails);
							}
						};
						item.add(showDetails);

					}

				};

				wmcListBizness.add(dataView);
				wmcListBizness.setVisible(true);
				webisError.setVisible(false);
			} catch (Exception e) {
				wmcListBizness.setVisible(false);
				webisError.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ssmError(e.getMessage());
			setResponsePage(new MyInternalErrorPage(e));
		}
	}

	public String getPageTitle() {
		String titleKey = "page.title.dashboard";
		return titleKey;
	}
}
