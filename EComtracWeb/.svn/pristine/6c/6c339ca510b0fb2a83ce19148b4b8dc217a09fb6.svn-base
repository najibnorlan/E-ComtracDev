package com.ssm.ezbiz.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;

@SuppressWarnings({ "all" })
public class BankInSlip extends SecBasePage {

	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	public BankInSlip() {

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						BankInSlipModel searchBankInSlipModel = new BankInSlipModel();
						searchBankInSlipModel.setBankinSlipDt(new Date());
						return searchBankInSlipModel;
					}

				}));
		add(new BankInSlipModelForm("form",
				(IModel<BankInSlipModel>) getDefaultModel()));
	}

	private class BankInSlipModelForm extends Form implements Serializable {

		private SSMDropDownChoice branch;
		private SSMDateTextField bankinSlipDt;
		String reportDate = "";

		public BankInSlipModelForm(String id, IModel<BankInSlipModel> m) {

			super(id, m);

			BankInSlipModel searchModel = m.getObject();

			SSMDropDownChoice branch = new SSMDropDownChoice("branch",
					Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.listCheckInCounter.branch");
			branch.setRequired(true);
			add(branch);

			SSMDateTextField bankinSlipDt = new SSMDateTextField("bankinSlipDt");
			bankinSlipDt.setLabelKey("page.lbl.ezbiz.bankInSlip.bankinSlipDt");
			add(bankinSlipDt);

			Button generate = new Button("generate") {
		        public void onSubmit() {
		          try {
		        	  BankInSlipModel bankInSlipForm = (BankInSlipModel) getForm().getDefaultModelObject();
		        	  
/*		        	  System.out.println("bankInSlipForm.getBranch() "+bankInSlipForm.getBranch());*/
		        	  
						SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat form2 = new SimpleDateFormat("dd-MM-yyyy");
						SimpleDateFormat pars = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date date = bankInSlipForm.getBankinSlipDt();
						SearchCriteria sc = null;
						String branch = "";
							
						if (StringUtils.isNotBlank(bankInSlipForm.getBranch())) {
							sc = new SearchCriteria("branch",SearchCriteria.EQUAL,bankInSlipForm.getBranch());

							String branchCode = bankInSlipForm.getBranch();
							String parameterName = llpParametersService.findByCodeTypeValue(Parameter.BRANCH_CODE,branchCode);

							branch = branchCode + " - " + parameterName;
						}

						if (bankInSlipForm.getBankinSlipDt() != null) {
							try {
								sc = sc.andIfNotNull("createDt",SearchCriteria.GREATER_EQUAL,pars.parse(form1.format(date)+ " 00:00:00"));
								sc = sc.andIfNotNull("createDt",SearchCriteria.LESS_EQUAL,pars.parse(form1.format(date)+ " 23:59:59"));
								
								reportDate = form2.format(bankInSlipForm.getBankinSlipDt());
							} catch (Exception ex) {
								System.out.print("Error creating query");
							}
						}
						List<RobCounterBankinSlip> branchList = robCounterBankinSlipService.findByCriteria(sc).getList();
						List<RobCounterBankinSlip> value = new ArrayList<RobCounterBankinSlip>();

						for (RobCounterBankinSlip robCounterBankinSlip : branchList) {
							value.add(robCounterBankinSlip);
/*							System.out.println("branch - " + robCounterBankinSlip.getBranch()+ " | " + "date - " + robCounterBankinSlip.getCreateDt());
							System.out.println("slipNo>>>"+robCounterBankinSlip.getBankinSlipNo());*/
						}

						Map mapData = new HashMap();
						

						mapData.put("robCounterBankinSlip", value);
						mapData.put("loginName",UserEnvironmentHelper.getLoginName());
						mapData.put("reportDate",reportDate);
						mapData.put("branch", branch);					

						if (branchList.size() > 0) {
							/*File file = new File("C:/Users/mhaziq/Desktop/SSM_Payment_Bankin_Slip.odt");*/
							byte bytePdfContent[] = LLPOdtUtils.generatePdf("ROB_PAYMENT_BANKIN_SLIP", mapData);
							generateDownload("BankInSlip.pdf", bytePdfContent);
						} else {
							/*File file = new File("C:/Users/mhaziq/Desktop/SSM_Payment_Bankin_Slip3.odt");*/
							byte bytePdfContent[] = LLPOdtUtils.generatePdf("SSM_PAYMENT_BANKIN_SLIP(1).ODT", mapData);
							generateDownload("BankInSlip.pdf", bytePdfContent);
						}
						
/*			          FeedbackPanel feedbackPanel =  ((BankInSlip)getPage()).getFeedbackPanel();
		              feedbackPanel.getFeedbackMessages().clear();
		              target.add(feedbackPanel);*/
		              
		          } catch (Exception e) {
		            System.out.println(e);
		          }
		        }
		      };
		      add(generate);	      
		     
		}
	}
	
	public void generateDownload(String fileName, final byte[] byteData) {

/*		System.out.println("file Name :::::::::: " + fileName);*/
		// System.out.println("byte length "+new String(byteData, 0));

		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(byteData);
				output.flush();
			}
		};

/*		System.out.println("AFTER WRITE OUTPUTSTREAM —---------");*/
		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(
				rstream, fileName);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}

	private class BankInSlipModel {

		private String branch;
		private Date bankinSlipDt;
		private String loginName;
		private String reportDate;

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public Date getBankinSlipDt() {
			return bankinSlipDt;
		}

		public void setBankinSlipDt(Date bankinSlipDt) {
			this.bankinSlipDt = bankinSlipDt;
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
		return "menu.myBiz.bankinSlipReport";
	}
}
