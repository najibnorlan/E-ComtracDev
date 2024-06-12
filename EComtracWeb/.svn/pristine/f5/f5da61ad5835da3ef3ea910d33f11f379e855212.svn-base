package com.ssm.ezbiz.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.robFormB.ViewRobFormBPage;
import com.ssm.ezbiz.robFormC.EditRobFormCPage;
import com.ssm.ezbiz.robFormC.ViewRobFormCPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormTransactionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormTransactionModel;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

public class LatestTransactionPanel extends BasePanel {

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;

	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "RobFormTransactionService")
	private RobFormTransactionService robFormTransactionService;
	

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	public LatestTransactionPanel(String id, final Page parentPage) {
		super(id);
		
		List<RobFormTransactionModel> listLatestTransaction = robFormTransactionService.findLatestTransactionByLoginId(UserEnvironmentHelper.getLoginName());
		
		if(listLatestTransaction.size()==0) {
			this.setVisible(false);
		}
		
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("createDt", listLatestTransaction);
		final SSMDataView<RobFormTransactionModel> dataView = new SSMDataView<RobFormTransactionModel>("sorting", dp) {

			protected void populateItem(final Item<RobFormTransactionModel> item) {

				final RobFormTransactionModel form = item.getModelObject();

				
				item.add(new SSMLabel("formType", form.getFormType(), Parameter.ROB_FORM_TYPE ));
				item.add(new SSMLabel("appRefNo", form.getAppRefNo()));
				item.add(new MultiLineLabel("brNoNBizName", form.getBrNoNBizName()));
				item.add(new SSMLabel("updateDt", form.getUpdateDt()));
				
				
				if (Parameter.ROB_FORM_TYPE_RENEWAL.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_RENEWAL_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_A.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_A_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_B.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_B_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_C.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_C_STATUS,form.getStatus()) ));
				}
				SSMAjaxLink viewAction = new SSMAjaxLink("viewAction") {
					@Override
					public void onClick(AjaxRequestTarget arg0) {

						if (Parameter.ROB_FORM_TYPE_RENEWAL.equals(form.getFormType())) {
							setResponsePage(new EditRobRenewalPage((RobRenewal) form.getFormObject()));
						} else if (Parameter.ROB_FORM_TYPE_A.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_A_STATUS_QUERY.equals(form.getStatus())
									|| Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormAPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormAPage2(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						} else if (Parameter.ROB_FORM_TYPE_B.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_B_STATUS_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormBPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormBPage(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						} else if (Parameter.ROB_FORM_TYPE_C.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_C_STATUS_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormCPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormCPage(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						}

					}
				};
				item.add(viewAction);
				
				SSMDownloadLink downloadCert = new SSMDownloadLink("downloadCert");
				downloadCert.setVisible(false);
				item.add(downloadCert);
				
				SSMDownloadLink downloadBusinessInfo = new SSMDownloadLink("downloadBusinessInfo");
				downloadBusinessInfo.setVisible(false);
				item.add(downloadBusinessInfo);
				
				
				int downValidDays = Integer.parseInt(getCodeTypeWithValue(Parameter.ROB_RENEWAL_CONFIG, Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS));
				
				
				if (Parameter.ROB_FORM_TYPE_RENEWAL.equals(form.getFormType()) && Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(form.getStatus())) {
					RobRenewal robRenewal = (RobRenewal) form.getFormObject();
					
					boolean allowDownload = isValidForDownload(robRenewal.getUpdateDt(), downValidDays);
					
					if(allowDownload) {

						try {
							if(robRenewal.getCertFileData()==null || robRenewal.getCertFileData().getFileData().length==0) {
								robRenewal = robRenewalService.findByIdWithData(robRenewal.getTransCode());
							}else {
								if (Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())) {
									if(robRenewal.getBusinessInfoData()==null || robRenewal.getBusinessInfoData().getFileData().length==0) {
										robRenewal = robRenewalService.findByIdWithData(robRenewal.getTransCode());
									}
								}
							}
						} catch (SSMException e1) {
							e1.printStackTrace();
						}
						try {
							downloadCert.setDownloadData(robRenewal.getBrNo() + "_CERT.pdf", "application/pdf", robRenewal.getCertFileData().getFileData());
							downloadCert.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if (Parameter.YES_NO_yes.equals(robRenewal.isBuyInfo())) {
							try {
								downloadBusinessInfo.setDownloadData(robRenewal.getBrNo() + "_BIS_INFO.pdf", "application/pdf", robRenewal.getBusinessInfoData().getFileData());
								downloadBusinessInfo.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else if (Parameter.ROB_FORM_TYPE_A.equals(form.getFormType()) && Parameter.ROB_FORM_A_STATUS_APPROVED.equals(form.getStatus())) {
					RobFormA robFormA =  (RobFormA) form.getFormObject();

					boolean allowDownload = isValidForDownload(robFormA.getUpdateDt(), downValidDays);
					if(allowDownload) {
						try {
							
							if(robFormA.getCertFileData()==null || robFormA.getCertFileData().getFileData().length==0) {
								robFormA = robFormAService.findByIdWithData(robFormA.getRobFormACode());
							}else {
								if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
									if(robFormA.getBusinessInfoData()==null || robFormA.getBusinessInfoData().getFileData().length==0) {
										robFormA = robFormAService.findByIdWithData(robFormA.getRobFormACode());
									}
								}
							}
						} catch (SSMException e1) {
							e1.printStackTrace();
						}
						try {
							downloadCert.setDownloadData(robFormA.getBrNo() + "_CERT.pdf", "application/pdf", robFormA.getCertFileData().getFileData());
							downloadCert.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
							try {
								downloadBusinessInfo.setDownloadData(robFormA.getBrNo() + "_BIS_INFO.pdf", "application/pdf", robFormA.getBusinessInfoData().getFileData());
								downloadBusinessInfo.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
				} else if (Parameter.ROB_FORM_TYPE_B.equals(form.getFormType())  && Parameter.ROB_FORM_B_STATUS_APPROVED.equals(form.getStatus())) {
					RobFormB robFormB =  (RobFormB) form.getFormObject();
					boolean allowDownload = isValidForDownload(robFormB.getUpdateDt(), downValidDays);
					
					if(allowDownload) {
						try {
							if(robFormB.getCertFileData()==null || robFormB.getCertFileData().getFileData().length==0) {
								robFormB = robFormBService.findByIdWithData(robFormB.getRobFormBCode());
							}else {
								if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())) {
									if(robFormB.getBusinessInfoData()==null || robFormB.getBusinessInfoData().getFileData().length==0) {
										robFormB = robFormBService.findByIdWithData(robFormB.getRobFormBCode());
									}
								}
							}
						} catch (SSMException e1) {
							e1.printStackTrace();
						}
						try {
							downloadCert.setDownloadData(robFormB.getBrNo() + "_CERT.pdf", "application/pdf", robFormB.getCertFileData().getFileData());
							downloadCert.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo() ) && robFormB.getBusinessInfoData()!=null) {
							try {
								downloadBusinessInfo.setDownloadData(robFormB.getBrNo() + "_BIS_INFO.pdf", "application/pdf", robFormB.getBusinessInfoData().getFileData());
								downloadBusinessInfo.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				
				} else if (Parameter.ROB_FORM_TYPE_C.equals(form.getFormType())  && Parameter.ROB_FORM_C_STATUS_APPROVED.equals(form.getStatus())) {
					RobFormC robFormC =  (RobFormC) form.getFormObject();
					boolean allowDownload = isValidForDownload(robFormC.getUpdateDt(), downValidDays);
					
					
					if(allowDownload) {
						
						if (Parameter.YES_NO_yes.equals(robFormC.getIsBuyInfo() )) {
							try {
								LlpFileData llpFileData = llpFileDataService.findById(robFormC.getBusinessInfoDataId());
								if(llpFileData!=null) {		
									downloadBusinessInfo.setDownloadData(robFormC.getBrNo() + "_BIS_INFO.pdf", "application/pdf", llpFileData.getFileData());
									downloadBusinessInfo.setVisible(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				item.add(viewAction);
			}
		};
		
		dataView.setItemsPerPage(5L);
		add(dataView);
		
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}
	
	boolean isValidForDownload(Date dateUpdate, int downValidDays){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateUpdate);
		cal.add(Calendar.DATE, downValidDays);
		
		boolean allowDownload = true;
		if (new Date().after(cal.getTime())) {
			allowDownload = false;
		}
		return allowDownload;
	}

	
}
