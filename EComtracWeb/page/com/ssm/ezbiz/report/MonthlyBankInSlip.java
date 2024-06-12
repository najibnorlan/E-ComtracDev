package com.ssm.ezbiz.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.eclipse.jdt.internal.compiler.util.Util;

import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobFormB;

@SuppressWarnings({ "all" })
public class MonthlyBankInSlip extends SecBasePage {

	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	public MonthlyBankInSlip() {

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						BankInSlipModel searchBankInSlipModel = new BankInSlipModel();
						searchBankInSlipModel.setDtFrom(new Date());
						searchBankInSlipModel.setDtTo(new Date());
						return searchBankInSlipModel;
					}

				}));
		add(new BankInSlipModelForm("form", (IModel<BankInSlipModel>) getDefaultModel()));
	}

	private class BankInSlipModelForm extends Form implements Serializable {

		private SSMDropDownChoice branch;
		private SSMDateTextField dtFrom;
		private SSMDateTextField dtTo;
		
		private String ssmBranch = "";
		private Date dateFrom;
		private Date dateTo;
		String reportDate = "";
		private String branchName = "";
		private SearchCriteria sc2;

		public BankInSlipModelForm(String id, IModel<BankInSlipModel> m) {

			super(id, m);

			BankInSlipModel searchModel = m.getObject();

			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.listCheckInCounter.branch");
			//branch.setRequired(true);
			add(branch);

			SSMDateTextField dtTo = new SSMDateTextField("dtTo");
			dtTo.setLabelKey("page.ssm.ezbiz.report.dtTo");
			add(dtTo);
			
			SSMDateTextField dtFrom = new SSMDateTextField("dtFrom");
			dtFrom.setLabelKey("page.ssm.ezbiz.report.dtFrom");
			//dtFrom.setRequired(true);
			add(dtFrom);		
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form) {
					
		          try {
		        	  BankInSlipModel bankInSlipForm = (BankInSlipModel) getForm().getDefaultModelObject();
		        	  
						
						SearchCriteria sc = null;
						String branch = bankInSlipForm.getBranch();
						Date dtFrom = bankInSlipForm.getDtFrom();
						Date dtTo = bankInSlipForm.getDtTo();
							
						if(!StringUtils.isNotBlank(branch)){
							branch = "";
						}
						
						sc = getSearchCriteria(branch, dtFrom, dtTo);
						
						List<RobCounterBankinSlip> branchList = robCounterBankinSlipService.findByCriteria(sc).getList();
						
						populateTable(sc, target); 
		              
		          } catch (Exception e) {
		            System.out.println(e);
		          }
		        }
		      };
		      add(search);	
		     
		      populateTable(null , null);
		     
		}
		
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			sc2 =sc;
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobCounterBankinSlipService.class);
			
			final SSMDataView<RobCounterBankinSlip> dataView = new SSMDataView<RobCounterBankinSlip>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobCounterBankinSlip> item) {
					final RobCounterBankinSlip robCounterBankinSlip = item.getModelObject();
					
					DecimalFormat df = new DecimalFormat("####,###,###.00");
					
					String amount = df.format(robCounterBankinSlip.getAmount());
			        
					item.add(new SSMLabel("transactionDate", robCounterBankinSlip.getCreateDt(), "dd/MM/yyyy"));
					item.add(new SSMLabel("floorLevel", robCounterBankinSlip.getFloor()));
					item.add(new SSMLabel("branch", robCounterBankinSlip.getBranch(), Parameter.BRANCH_CODE ));
					item.add(new SSMLabel("bankInSlipNo", robCounterBankinSlip.getBankinSlipNo()));
					item.add(new SSMLabel("amount", amount));
					item.add(new SSMLabel("createdBy", robCounterBankinSlip.getCreateBy()));
					
				
					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};

			dataView.setItemsPerPage(20L);

			wmcSearchResult.add(dataView);
			wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
			wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));
			
			SSMDownloadLink linkGafDownload = new SSMDownloadLink("linkGafDownload") {
				public void onClick() {
					if(getByteData()!=null){
						super.onClick();
						return;
					}
					try {
						
						
						String fileName = "MONTHLY-BANKIN-SLIP-"+DateUtil.formatDate(new Date(),"yyyyMMdd")+".txt";
						
						byte byteTextFile[] = ((RobCounterBankinSlipService) getService(RobCounterBankinSlipService.class.getSimpleName())).generateTextFile(sc2);
						setDownloadData(fileName, "application/txt", byteTextFile);
						super.onClick();
					} catch (Exception e) {
						ssmError(e.getMessage());
						e.printStackTrace();
					} 
				}

			};
			
			wmcSearchResult.add(linkGafDownload);
			wmcSearchResult.setVisible(true);
			
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}
			
		}
	}
	
	private final SearchCriteria getSearchCriteria(String branch, Date dtFrom, Date dtTo) {
		SearchCriteria sc = null;
		
		SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat form2 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		if (StringUtils.isNotBlank(branch)) {
			sc = new SearchCriteria("branch",SearchCriteria.EQUAL,branch);
		} 

		if (dtFrom != null) {
			try {
				if(sc==null){
					sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(form1.format(dtFrom)+ " 00:00:00"));
				} else {
					sc = sc.andIfNotNull("createDt",SearchCriteria.GREATER_EQUAL,pars.parse(form1.format(dtFrom)+ " 00:00:00"));
				}
				
			} catch (Exception ex) {
				System.out.print("Error creating query");
			}
		}
		
		if (dtTo != null) {
			try {
				if(sc==null){
					sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, pars.parse(form1.format(dtTo)+ " 23:59:59"));
				} else {
					sc = sc.andIfNotNull("createDt",SearchCriteria.LESS_EQUAL,pars.parse(form1.format(dtTo)+ " 23:59:59"));
				}
				
			} catch (Exception ex) {
				System.out.print("Error creating query");
			}
		} 
		
		return sc;
	}

	private class BankInSlipModel {

		private String branch;
		private Date dtFrom;
		private Date dtTo;
		private String loginName;
		private String reportDate;

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
		
	}
	
	@Override
	public String getPageTitle() {
		return "menu.myBiz.monthlyBankinSlipReport";
	}
}
