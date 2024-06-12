package com.ssm.ezbiz.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.eclipse.jdt.internal.compiler.util.Util;

import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.ezbiz.model.RobBusiness;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;

@SuppressWarnings({ "all" })
public class SearchCreditNote extends SecBasePage {
	
	@SpringBean(name = "RobPaymentCreditNoteService")
	private RobPaymentCreditNoteService robPaymentCreditNoteService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService paymentTransactionService;
	
	
	public SearchCreditNote(){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchCreditNoteModel searchCreditNoteModel = new SearchCreditNoteModel();
				searchCreditNoteModel.setDtFrom(new Date());
				return searchCreditNoteModel;
			}
		}));
		add(new SearchCreditNoteForm("form", (IModel<SearchCreditNoteModel>) getDefaultModel()));
	}
	

	private class SearchCreditNoteForm extends Form implements Serializable {
		
		public SearchCreditNoteForm(String id, IModel<SearchCreditNoteModel> m) {
			super(id, m);
			SearchCreditNoteModel searchModel = m.getObject();
			
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.ssm.ezbiz.report.creditNote.branchNo");
			branch.setRequired(true);
			add(branch);
			
			SSMDateTextField dtFrom = new SSMDateTextField("dtFrom");
			dtFrom.setLabelKey("page.ssm.ezbiz.report.creditNote.dtFrom");
			dtFrom.setRequired(true);
			add(dtFrom);
			
			SSMDateTextField dtTo = new SSMDateTextField("dtTo");
			dtTo.setLabelKey("page.ssm.ezbiz.report.creditNote.dtTo");
			add(dtTo);
			
			Button generate = new Button("generate") {
				public void onSubmit() {
					try {
						SearchCreditNoteModel searchModelForm = (SearchCreditNoteModel) getForm().getDefaultModelObject();
					
						String branch = "";
						SearchCriteria sc = null;
						String reportDate = "";
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						
						if(StringUtils.isNotBlank(searchModelForm.getBranch())){
							sc = new SearchCriteria("counterSessionId.branch", SearchCriteria.EQUAL, searchModelForm.getBranch());	

							branch = searchModelForm.getBranch();
							
						}
						
						if(searchModelForm.getDtFrom()!=null){
							if(sc==null){
								sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(form.format(searchModelForm.getDtFrom()) + " 00:00:00")); 
							}else{
								sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(form.format(searchModelForm.getDtFrom()) + " 00:00:00"));
							}
							
							reportDate = sdf.format(searchModelForm.getDtFrom());
						}
						
						if(searchModelForm.getDtTo()!=null){
							if(sc==null){
								sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, pars.parse(form.format(searchModelForm.getDtTo()) + " 23:59:59")); 
							}else{
								sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, pars.parse(form.format(searchModelForm.getDtTo()) + " 23:59:59"));
							}
							
							reportDate = reportDate + " to "+ sdf.format(searchModelForm.getDtTo());
						}
						
						List<RobPaymentCreditNote> creditNoteLs = robPaymentCreditNoteService.findByCriteria(sc).getList();
						List<RobPaymentCreditNote> listCNote = new ArrayList<RobPaymentCreditNote>();
						
						int total = 0;
						double totalAmount = 0D;
						
						for(RobPaymentCreditNote robPaymentCreditNote : creditNoteLs){
							
							total++;
							String paymentMode = "";
							String transactionId = robPaymentCreditNote.getPaymentReceiptNo().getTransactionId();
							SearchCriteria scCn = null;
							scCn = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);	
							
							List<LlpPaymentTransaction> llpTransactionLs = paymentTransactionService.findByCriteria(scCn).getList();
							
							if(llpTransactionLs.size() > 0){
								paymentMode = llpTransactionLs.get(0).getPaymentMode();
							}
							
							robPaymentCreditNote.setCnTransactionNo(paymentMode);
							listCNote.add(robPaymentCreditNote);
							totalAmount = totalAmount + robPaymentCreditNote.getAmount();
						}

						Map mapData =  new HashMap();
						
						mapData.put("total", total);
						mapData.put("listCNote", listCNote);
						mapData.put("loginName", UserEnvironmentHelper.getLoginName());
						mapData.put("reportDate", reportDate);
						mapData.put("branch", branch);
						mapData.put("totalAmount", totalAmount);
						
						String output = "";
					//	if(creditNoteLs.size() > 0){
							byte bytePdfContent[] = LLPOdtUtils.generatePdf("ROB_CREDIT_NOTE_REPORT", mapData);
//							File file = new File("D:/report/SSM_Credit_Note_Report.odt");
//							byte bytePdfContent[] = LLPOdtUtils.generatePdf(file, mapData);
							generateDownload("CreditNote.pdf", bytePdfContent);
							output = total +" records found.";
//						} else {
//							output = SSMExceptionParam.NO_RECORD_FOUND;
//							System.out.println("No record");
//						}
					//	ssmError(output);
						
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			};
			add(generate);
		}
				
		public void generateDownload(String fileName, final byte[] byteData){
			
			AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
				@Override
				public void write(OutputStream output) throws IOException {
					output.write(byteData);
					output.flush();
				}
			};

			ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
			getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
		
	}
	
	
	
	public class SearchCreditNoteModel implements Serializable {
		private Date dtFrom;
		private Date dtTo;
		private String branch;
		private String loginName;
		private String reportDate;
		
		public Date getDtFrom() {
			return dtFrom;
		}
		
		public void setDtFrom(Date dtFrom) {
			this.dtFrom = dtFrom;
		}
		
		public Date getDtTo() {
			return dtTo;
		}
		
		public void setDtTo(Date dtTo) {
			this.dtTo = dtTo;
		}
		
		public String getBranch() {
			return branch;
		}
		
		public void setBranch(String branch) {
			this.branch = branch;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getReportDate() {
			return reportDate;
		}

		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}
		
		
	}
	
	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "menu.myBiz.creditNoteReport";
	}
}
