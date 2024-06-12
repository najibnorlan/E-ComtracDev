package com.ssm.ezbiz.counter;

import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.CollectionBalancingPage;
import com.ssm.ezbiz.otcPayment.ListOtcPaymentPage;
import com.ssm.ezbiz.otcPayment.UserCheckinPage;
import com.ssm.ezbiz.otcPayment.ViewTransactionSummary;
import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterSession;


@SuppressWarnings({ "all" })
public class ListCheckInCounter extends SecBasePage {
	
/*	@Override
	public String getPageTitle() {
		return "ezbiz.listCounterCheckIn.title";
	}*/
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService; 
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	public ListCheckInCounter(){
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel(){
			protected Object load(){
				ListCheckInCounterModel searchCheckInCounterModel = new ListCheckInCounterModel();
				return searchCheckInCounterModel;
			}
		
				}));
		add(new ListCheckInCounterForm("form", (IModel<ListCheckInCounterModel>) getDefaultModel()));

	}

	private class ListCheckInCounterForm extends Form implements Serializable {
		
		private SSMDropDownChoice branch;
		private SSMDropDownChoice floorLevel;
		
		public ListCheckInCounterForm(String id, IModel<ListCheckInCounterModel> m) {
			
			super(id,m);
			
			ListCheckInCounterModel searchModel = m.getObject();

			
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);  
			branch.setLabelKey("page.lbl.ezbiz.listCheckInCounter.branch");
/*			branch.setRequired(true);*/
			add(branch);
			
			SSMDropDownChoice floorLevel = new SSMDropDownChoice("floorLevel", Parameter.FLOOR_LVL);
			floorLevel.setLabelKey("page.lbl.ezbiz.listCheckInCounter.floorLevel");
/*			floorLevel.setRequired(true);*/
			add(floorLevel);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListCheckInCounterModel searchModelForm = (ListCheckInCounterModel) form.getDefaultModelObject();
					
					if(searchModelForm.getBranch() == null && searchModelForm.getfloorLevel() == null){
						ssmError(SSMExceptionParam.NEED_AT_LEAST_ONE_INPUT);
					}else{
						SearchCriteria sc = searchCriteriaTemplate(searchModelForm);
						populateTable(sc, target);	
					}
					FeedbackPanel feedbackPanel =  ((ListCheckInCounter)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
				
			};
			add(search);
			populateTable(null, null);
		}
		
	}
	
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("checkinDate", SortOrder.ASCENDING, sc, RobCounterSessionService.class);
		final SSMDataView<RobCounterSession> dataView = new SSMDataView<RobCounterSession>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			
		
			protected void populateItem(final Item<RobCounterSession> item) {
				final RobCounterSession robCounterSession = item.getModelObject();
		        
				item.add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate()));
				item.add(new SSMLabel("userId", robCounterSession.getUserId()));
				item.add(new SSMLabel("counterType", robCounterSession.getCounterIpAddress().getCounterType(), Parameter.PAYMENT_COUNTER_TYPE));
				item.add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress().getCounterName()));
				item.add(new SSMLabel("branch",robCounterSession.getBranch(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
				item.add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus()));
				
				
				Link checkOut = new Link("checkOut", item.getDefaultModel()){
					 @Override
				        public void onClick() {
						 Integer countTransactions = llpPaymentReceiptService
									.getCountTransactionByCounterSession(robCounterSession
											.getSessionId(), null);
						 
						 if(countTransactions == 0){
							RobCounterSession session = robCounterSessionService.findById(robCounterSession.getSessionId());
							robCounterSessionService.delete(session);
								
							setResponsePage(ListCheckInCounter.class);
						 }else{
							 robCounterSession.setIsOpen(Parameter.COLLECTION_COUNTER_IS_OPEN_no);
					         robCounterSession.setCheckoutDate(new java.util.Date());
					         robCounterSession.setCheckoutBy(UserEnvironmentHelper.getLoginName());
					         robCounterSessionService.update(robCounterSession);

					         setResponsePage(new ViewTransactionSummary(robCounterSession.getSessionId(), false));
						 }
				          
					}
				};
				item.add(checkOut);
				
				Link counterBalance = new Link("counterBalance", item.getDefaultModel()){
					 @Override
				        public void onClick() {
							robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_new);
							robCounterSession.setBalancingBy(UserEnvironmentHelper.getLoginName());
							robCounterSession.setBalancingDt(new Date());
							robCounterSessionService.update(robCounterSession);
							
							setResponsePage(new CollectionBalancingPage(robCounterSession.getSessionId()));

					}
				};
				item.add(counterBalance);
				
				if(robCounterSession.getBalancingStatus().equalsIgnoreCase(Parameter.BALANCING_STATUS_no)){
					checkOut.setVisible(true);
					counterBalance.setVisible(false);
				} else{
					counterBalance.setVisible(true);
					checkOut.setVisible(false);
				}
						

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
		if(target==null){
			add(wmcSearchResult);
		}else{
			replace(wmcSearchResult);
			target.add(wmcSearchResult);
		}
		
	}
	
	public SearchCriteria searchCriteriaTemplate(ListCheckInCounterModel searchModelForm){
		
		System.out.println("branch : " + searchModelForm.getBranch() + " | " + "floor : " + searchModelForm.getfloorLevel());
		SearchCriteria sc = null;
		
		String[] statusToCheck = {Parameter.BALANCING_STATUS_no, Parameter.BALANCING_STATUS_new};
		
/*		sc = new SearchCriteria("balancingStatus", SearchCriteria.EQUAL, Parameter.BALANCING_STATUS_no,Parameter.BALANCING_STATUS_new );*/
		
		if(searchModelForm.getBranch() != null){
			if(sc != null){
				sc = sc.andIfNotNull("branch", SearchCriteria.EQUAL, searchModelForm.getBranch());
			}else{
				sc = new SearchCriteria("branch", SearchCriteria.EQUAL, searchModelForm.getBranch());
			}
		}
		if(searchModelForm.getfloorLevel() != null){
			if(sc != null){
				sc = sc.andIfNotNull("floorLevel", SearchCriteria.EQUAL, searchModelForm.getfloorLevel());
			}else{
				sc = new SearchCriteria("floorLevel", SearchCriteria.EQUAL, searchModelForm.getfloorLevel());
			}
		}
		
		if(sc != null){
			sc = sc.andIfInNotNull("balancingStatus", statusToCheck, false);
		}else{
			sc = new SearchCriteria("balancingStatus", SearchCriteria.EQUAL, statusToCheck, false);
		}
		

		return sc;
	}

	
	private class ListCheckInCounterModel {
		private String branch;
		private String floorLevel;
		private SearchCriteria searchCriteria;
				
		public String getBranch() {
			return branch;
		}
		
		public void setBranch(String branch){
			this.branch = branch;
		}
		
		public String getfloorLevel(){
			return floorLevel;
		}
		
		private void setfloorLevel(String floorLevel){
			this.floorLevel = floorLevel;
		}
		
		public SearchCriteria getSearchCriteria() {
			return searchCriteria;
		}
		public void setSearchCriteria(SearchCriteria searchCriteria) {
			this.searchCriteria = searchCriteria;
		}
		
	}
	
	
}
