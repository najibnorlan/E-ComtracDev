package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Param;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class SearchBankinSlip extends SecBasePage {
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService;
	
	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService;
	
	public SearchBankinSlip() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchBankinSlipModal searchBankinSlipModal  = new SearchBankinSlipModal();
				return searchBankinSlipModal;
			}
		}));
		add(new SearchBankinSlipForm("form", (IModel<SearchBankinSlipModal>) getDefaultModel()));
	}
	
	public SearchBankinSlip(String bankSlipNo) {
		
		final RobCounterBankinSlip counterBankinSlip = robCounterBankinSlipService.findByBankinSlipNo(bankSlipNo);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchBankinSlipModal searchBankinSlipModal  = new SearchBankinSlipModal();
				searchBankinSlipModal.setBranch(counterBankinSlip.getBranch());
				searchBankinSlipModal.setFloor(counterBankinSlip.getFloor());
				return searchBankinSlipModal;
			}
		}));
		add(new SearchBankinSlipForm("form", (IModel<SearchBankinSlipModal>) getDefaultModel()));
	}
	
	private class SearchBankinSlipForm extends Form implements Serializable {

		public SearchBankinSlipForm(String id, IModel<SearchBankinSlipModal> m) {
			super(id, m);
			
			SearchBankinSlipModal searchBankinSlipModal = m.getObject();
			
			SSMTextField slipNo = new SSMTextField("slipNo");
			slipNo.setLabelKey("page.lbl.ezbiz.counterbankinslip.slipNo");
			add(slipNo);
			
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.counterbankinslip.branch");
			add(branch);
			
			SSMDropDownChoice floor = new SSMDropDownChoice("floor", Parameter.FLOOR_LVL);
			floor.setLabelKey("page.lbl.ezbiz.counterbankinslip.floor");
			add(floor);
			
			SSMDateTextField dateFrom = new SSMDateTextField("dateFrom");
			dateFrom.setLabelKey("page.lbl.ezbiz.counterbankinslip.dateFrom");
			add(dateFrom);
			
			SSMDateTextField dateTo = new SSMDateTextField("dateTo");
			dateTo.setLabelKey("page.lbl.ezbiz.counterbankinslip.dateTo");
			add(dateTo);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchBankinSlipModal bankinSlipModal = (SearchBankinSlipModal) form.getDefaultModelObject();
					
					SearchCriteria sc = generateScTemplate(bankinSlipModal.getSlipNo(), bankinSlipModal.getBranch(), bankinSlipModal.getFloor(), bankinSlipModal.getDateFrom(), bankinSlipModal.getDateTo());
					populateTable(sc, target);
				}
			};
			add(search);
			populateTable(generateScTemplate(searchBankinSlipModal.getSlipNo(), searchBankinSlipModal.getBranch(), searchBankinSlipModal.getFloor(), searchBankinSlipModal.getDateFrom(), searchBankinSlipModal.getDateTo()), null);
		}
		

	}
	
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
		WebMarkupContainer dataDiv = new WebMarkupContainer("dataDiv");
		dataDiv.setOutputMarkupId(true);
		dataDiv.setVisible(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("branch", SortOrder.ASCENDING, sc, RobCounterBankinSlipService.class);
		final SSMDataView<RobCounterBankinSlip> dataView = new SSMDataView<RobCounterBankinSlip>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			final DecimalFormat df = new DecimalFormat("#,###,##0.00");

			protected void populateItem(final Item<RobCounterBankinSlip> item) {
				final RobCounterBankinSlip counterBankinSlip = item.getModelObject();
		        
				Double total = Double.valueOf(counterBankinSlip.getAmount());
				item.add(new SSMLabel("branch", counterBankinSlip.getBranch(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("floor", counterBankinSlip.getFloor(), Parameter.FLOOR_LVL));
				item.add(new SSMLabel("total",  df.format(total))); 
				item.add(new SSMLabel("bankSlipNo",  counterBankinSlip.getBankinSlipNo())); 
				item.add(new SSMLabel("createDt",  counterBankinSlip.getCreateDt(), "dd/MM/yyyy hh:mm:ss a")); 
				item.add(new SSMLabel("createBy",  counterBankinSlip.getCreateBy())); 
				item.add(new SSMLabel("index", item.getIndex()+1)); 
				
				
				SSMAjaxLink detail = new SSMAjaxLink("detail", item.getDefaultModel()){
					@Override
					public void onClick(AjaxRequestTarget target){
						Object obj = item.getModelObject();
						
						setResponsePage(new SearchBankinSlipDetail(counterBankinSlip.getBankinSlipNo()));
					}
				};
				
				detail.setOutputMarkupPlaceholderTag(true);
				item.add(detail);
				

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

		dataDiv.add(dataView);
		dataDiv.add(new SSMPagingNavigator("navigator", dataView));
		dataDiv.add(new NavigatorLabel("navigatorLabel", dataView));
		
		if(target == null){
			add(dataDiv);
		}else{
			replace(dataDiv);
			target.add(dataDiv);
		}
		

	}
	
	public SearchCriteria generateScTemplate(String slipNo, String branch, String floor, Date dateFrom, Date dateTo){
		SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SearchCriteria sc = null;
		if(slipNo != null){
			sc = new SearchCriteria("bankinSlipNo", SearchCriteria.EQUAL, slipNo);
		}
		
		if(branch != null){
			if(sc != null){
				sc = sc.andIfNotNull("branch", SearchCriteria.EQUAL, branch);
			}else{
				sc = new SearchCriteria("branch", SearchCriteria.EQUAL, branch);
			}
		}
		
		if(floor != null){
			if(sc != null){
				sc = sc.andIfNotNull("floor", SearchCriteria.EQUAL, floor);
			}else{
				sc = new SearchCriteria("floor", SearchCriteria.EQUAL, floor);
			}
		}
		
		if(dateFrom != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dateFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dateFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(dateTo != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dateTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dateTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sc;
	}
	public class SearchBankinSlipModal implements Serializable{
		private String slipNo;
		private String branch;
		private String floor;
		private Date dateFrom;
		private Date dateTo;
		
		public String getSlipNo() {
			return slipNo;
		}
		public void setSlipNo(String slipNo) {
			this.slipNo = slipNo;
		}
		public String getBranch() {
			return branch;
		}
		public void setBranch(String branch) {
			this.branch = branch;
		}
		public String getFloor() {
			return floor;
		}
		public void setFloor(String floor) {
			this.floor = floor;
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
		
	}
	@Override
	public String getPageTitle() {
		return "menu.myBiz.searchBankinSlip";
	}
}
