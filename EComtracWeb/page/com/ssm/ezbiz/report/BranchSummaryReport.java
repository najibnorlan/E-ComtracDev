package com.ssm.ezbiz.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.UsageReportService;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.mod1.model.BranchSummaryReportModel;

@SuppressWarnings({"all"})
public class BranchSummaryReport extends SecBasePage {

	@SpringBean(name="UsageReportService")
	UsageReportService usageReportService;

	@SpringBean(name="LlpPaymentFeeService")
	LlpPaymentFeeService llpPaymentFeeService;
	
	public BranchSummaryReport() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel(){
			protected Object load() {
				Search model = new Search();
				model.setDateFrom(new Date());
				return model;
			}
		}));
		add(new ReportForm("form", getDefaultModel()));
	}
	
	private class ReportForm extends Form implements Serializable {
		public ReportForm(String id, IModel m) {
			super(id, m);
			
			final SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setRequired(true);
			add(branch);
			
/*			final SSMDropDownChoice paymentMode = new SSMDropDownChoice("paymentMode", Parameter.PAYMENT_MODE);
			add(paymentMode);*/
			
			final SSMDateTextField dateFrom = new SSMDateTextField("dateFrom", "dd/MM/yyyy");
			dateFrom.setRequired(true);
			add(dateFrom);

			final SSMDateTextField dateTo = new SSMDateTextField("dateTo", "dd/MM/yyyy");
			dateTo.setRequired(true);
			add(dateTo);
			
			final Button btnSearch = new Button("btnSearch") {
				public void onSubmit() {
					
					String userId = UserEnvironmentHelper.getUserenvironment().getLoginName();
					Search model = (Search) getForm().getDefaultModelObject();
					Map mapData = new HashMap();
					mapData.put("userId", userId);
					mapData.put("dateFrom", model.getDateFrom());
					mapData.put("dateTo", model.getDateTo());
					/*mapData.put("paymentMode", model.getPaymentMode());*/
					mapData.put("branchCode", model.getBranch());
					
					List<LlpPaymentFee> businessFees = llpPaymentFeeService.findByCriteria(new SearchCriteria("paymentCode", SearchCriteria.NOT_LIKE, "%CMP%").andIfNotNull("status", SearchCriteria.EQUAL, "A")).getList();
					List<LlpPaymentFee> compoundFees = llpPaymentFeeService.findByCriteria(new SearchCriteria("paymentCode", SearchCriteria.LIKE, "%CMP%").andIfNotNull("status", SearchCriteria.EQUAL, "A")).getList();
					
					List<BranchSummaryReportModel> businessCollection = new ArrayList<BranchSummaryReportModel>();
					List<BranchSummaryReportModel> compoundCollection = new ArrayList<BranchSummaryReportModel>();
					
					//for business
					for(LlpPaymentFee i : businessFees){
						BranchSummaryReportModel reportModel = usageReportService.findBranchSummaryReportData(i.getPaymentCode(), model.getBranch(), model.getDateFrom(), model.getDateTo());
						
						if(reportModel != null){
							businessCollection.add(reportModel);
						}else{
							BranchSummaryReportModel bsrm = new BranchSummaryReportModel();
							bsrm.setPaymentCode(i.getPaymentCode());
							bsrm.setPaymentCodeDesc(getCodeTypeWithValue(Parameter.PAYMENT_TYPE, i.getPaymentCode()));
							bsrm.setCount(0);
							bsrm.setSum(Double.valueOf(0));
							
							businessCollection.add(bsrm);
						}
					}
					
					//for compound
					for(LlpPaymentFee i : compoundFees){
						BranchSummaryReportModel reportModel = usageReportService.findBranchSummaryReportData(i.getPaymentCode(), model.getBranch(), model.getDateFrom(), model.getDateTo());
						
						if(reportModel != null){
							compoundCollection.add(reportModel);
						}else{
							BranchSummaryReportModel bsrm = new BranchSummaryReportModel();
							bsrm.setPaymentCode(i.getPaymentCode());
							bsrm.setPaymentCodeDesc(getCodeTypeWithValue(Parameter.PAYMENT_TYPE, i.getPaymentCode()));
							bsrm.setCount(0);
							bsrm.setSum(Double.valueOf(0));
							
							compoundCollection.add(bsrm);
						}
					}
					
					mapData.put("compoundCollection", compoundCollection);
					mapData.put("businessCollection", businessCollection);
					
//					File file = new File("C:/Users/nhasrizal/Downloads/SSM_Branch_Summary_Report.odt");
					byte bytePdfContent[] = LLPOdtUtils.generatePdf("BRANCH_SUMMARY_REPORT", mapData);
//					byte bytePdfContent[] = LLPOdtUtils.generatePdf(file, mapData);
					generateDownload("Branch Summary Report.pdf", bytePdfContent);
				}
			};
			add(btnSearch);
		}
	}
	
	public class Search implements Serializable {
		private String branch;
		private Date dateFrom;
		private Date dateTo;
		private String paymentMode;

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public Date getDateFrom() {
			return dateFrom;
		}

		public void setDateFrom(Date dateFrom) {
			this.dateFrom = dateFrom;
		}

		public Date getDateTo() {
			return dateTo;
		}

		public void setDateTo(Date dateTo) {
			this.dateTo = dateTo;
		}

/*		public String getPaymentMode() {
			return paymentMode;
		}

		public void setPaymentMode(String paymentMode) {
			this.paymentMode = paymentMode;
		}*/

	}
	
	public void generateDownload(String fileName, final byte[] byteData){
		
		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(byteData);
			}
		};

		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}
	
	@Override
	public String getPageTitle() {
		return "page.lbl.BranchSummaryReport.title";
	}
	
}
