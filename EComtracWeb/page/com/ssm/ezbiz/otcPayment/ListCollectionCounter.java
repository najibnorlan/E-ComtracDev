package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.apache.jasper.tagplugins.jstl.core.Param;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.exception.SSMExceptionParam;
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
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class ListCollectionCounter extends SecBasePage {

	@SpringBean(name = "RobCounterCollectionService")
	RobCounterCollectionService robCounterCollectionService;
	
	public ListCollectionCounter(){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				ListCounterFormModel listCounterModel = new ListCounterFormModel();
				listCounterModel.setIsActive(Parameter.YES_NO_yes);
				return listCounterModel;
			}
		}));
		add(new ListCollectionCounterForm("form", (IModel<ListCounterFormModel>) getDefaultModel()));
		add(new AddCounterForm("formAdd", (IModel<ListCounterFormModel>) getDefaultModel()));
	}
	
	public ListCollectionCounter(final RobCounterCollection counterCollection){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCounterFormModel listCounterModel = new ListCounterFormModel();
				listCounterModel.setBranch(counterCollection.getBranch());
				listCounterModel.setFloor(counterCollection.getFloorLevel());
				
				SearchCriteria sc = searchCriteriaTemplate(listCounterModel);
				
				listCounterModel.setSearchCriteria(sc);
				return listCounterModel;
			}
		}));
		add(new ListCollectionCounterForm("form", (IModel<ListCounterFormModel>) getDefaultModel()));
		add(new AddCounterForm("formAdd", (IModel<ListCounterFormModel>) getDefaultModel()));
	}
	
	private class ListCollectionCounterForm extends Form implements Serializable {

		public ListCollectionCounterForm(String id, IModel<ListCounterFormModel> m) {
			super(id, m);
			
			final ListCounterFormModel listCounterModel = m.getObject();
			
			SSMAjaxLink add = new SSMAjaxLink("add") {

				@Override
				public void onClick(AjaxRequestTarget arg0) {
					setResponsePage(EditCounterCollection.class);
				}
				
			};
			add(add);
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.counteCollectionList.branch");
			add(branch);
			
			SSMDropDownChoice floor = new SSMDropDownChoice("floor", Parameter.FLOOR_LVL);
			floor.setLabelKey("page.lbl.ezbiz.counteCollectionList.floor");
			add(floor);
			
			SSMTextField ipAddress = new SSMTextField("ipAddress");
			ipAddress.setLabelKey("page.lbl.ezbiz.counteCollectionList.ipAddress");
			add(ipAddress);
			
			SSMDropDownChoice isActive = new SSMDropDownChoice("isActive", Parameter.YES_NO);
			isActive.getChoices().add(0, "All");
			isActive.setLabelKey("page.lbl.ezbiz.counteCollectionList.isActive");
			add(isActive);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListCounterFormModel searchModelForm = (ListCounterFormModel) form.getDefaultModelObject();
					System.out.println("search : " + searchModelForm.getIpAddressAdd());
					SearchCriteria sc = searchCriteriaTemplate(searchModelForm);
					
					populateTable(sc, target);
					
				}
			};
			add(search);
			
			
			populateTable(listCounterModel.getSearchCriteria(), null);
			
		}
		

	}
	
	private class AddCounterForm extends Form implements Serializable {

		public AddCounterForm(String id, IModel<ListCounterFormModel> m) {
			super(id, m);
			
			final ListCounterFormModel listCounterModel = m.getObject();
			
			SSMDropDownChoice branchAdd = new SSMDropDownChoice("branchAdd", Parameter.BRANCH_CODE);
			branchAdd.setLabelKey("page.lbl.ezbiz.counteCollectionList.branchAdd");
			branchAdd.setRequired(true);
			add(branchAdd);
			
			SSMDropDownChoice floorAdd = new SSMDropDownChoice("floorAdd", Parameter.FLOOR_LVL);
			floorAdd.setLabelKey("page.lbl.ezbiz.counteCollectionList.floorAdd");
			floorAdd.setRequired(true);
			add(floorAdd);
			
			SSMTextField ipAddressAdd = new SSMTextField("ipAddressAdd");
			ipAddressAdd.setLabelKey("page.lbl.ezbiz.counteCollectionList.ipAddressAdd");
			ipAddressAdd.setRequired(true);
			add(ipAddressAdd);
			
			SSMTextField counterNameAdd = new SSMTextField("counterNameAdd");
			counterNameAdd.setLabelKey("page.lbl.ezbiz.counteCollectionList.counterNameAdd");
			counterNameAdd.setRequired(true);
			add(counterNameAdd);
			
			 SSMDropDownChoice counterTypeAdd = new SSMDropDownChoice("counterTypeAdd", Parameter.PAYMENT_COUNTER_TYPE);
			counterTypeAdd.setLabelKey("page.lbl.ezbiz.counteCollectionList.counterTypeAdd");
			counterTypeAdd.setRequired(true);
			add(counterTypeAdd);
			
			
			
			SSMAjaxButton submit = new SSMAjaxButton("submit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListCounterFormModel model = (ListCounterFormModel) form.getDefaultModelObject();
					RobCounterCollection check = robCounterCollectionService.findByIpAddress(model.getIpAddressAdd());
					if(check == null){
						RobCounterCollection counterCollection = new RobCounterCollection();
						counterCollection.setIpAddress(model.getIpAddressAdd());
						counterCollection.setBranch(model.getBranchAdd());
						counterCollection.setFloorLevel(model.getFloorAdd());
						counterCollection.setCounterName(model.getCounterNameAdd());
						counterCollection.setCounterType(model.getCounterTypeAdd());
						counterCollection.setIsActive(Parameter.YES_NO_yes);
						counterCollection.setSoftDelete(Parameter.YES_NO_no);
						robCounterCollectionService.insert(counterCollection);
						
						setResponsePage(new ListCollectionCounter(counterCollection));
					}else{
						ssmError(SSMExceptionParam.COLLECTION_COUNTER_EXIST);
					}
					
					FeedbackPanel feedbackPanel =  ((ListCollectionCounter)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
			};
			add(submit);
			
			
		}
		

	}
	
	public SearchCriteria searchCriteriaTemplate(ListCounterFormModel searchModelForm){
		SearchCriteria sc = null;
		if(searchModelForm.getIpAddress() != null){
			sc = new SearchCriteria("ipAddress", SearchCriteria.EQUAL, searchModelForm.getIpAddress());
		}
		
		if(searchModelForm.getBranch() != null){
			if(sc != null){
				sc = sc.andIfNotNull("branch", SearchCriteria.EQUAL, searchModelForm.getBranch());
			}else{
				sc = new SearchCriteria("branch", SearchCriteria.EQUAL, searchModelForm.getBranch());
			}
		}
		
		if(searchModelForm.getFloor() != null){
			if(sc != null){
				sc = sc.andIfNotNull("floorLevel", SearchCriteria.EQUAL, searchModelForm.getFloor());
			}else{
				sc = new SearchCriteria("floorLevel", SearchCriteria.EQUAL, searchModelForm.getFloor());
			}
		}
		
		if(searchModelForm.getIsActive() != null){
			if(searchModelForm.getIsActive().equalsIgnoreCase("all")){
				if(sc != null){
					SearchCriteria sc2 = new SearchCriteria("isActive", SearchCriteria.IS_NOT_NULL);
					sc = new SearchCriteria(sc, SearchCriteria.AND, sc2);
				}else{
					sc = new SearchCriteria("isActive", SearchCriteria.IS_NOT_NULL);
				}
			}else{
				if(sc != null){
					sc = sc.andIfNotNull("isActive", SearchCriteria.EQUAL, searchModelForm.getIsActive());
				}else{
					sc = new SearchCriteria("isActive", SearchCriteria.EQUAL, searchModelForm.getIsActive());
				}
			}
		}
		
		return sc;
	}
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
		WebMarkupContainer counterList = new WebMarkupContainer("counterList");
		counterList.setOutputMarkupId(true);
		counterList.setVisible(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("branch", SortOrder.ASCENDING, sc, RobCounterCollectionService.class);
		final SSMDataView<RobCounterCollection> dataView = new SSMDataView<RobCounterCollection>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobCounterCollection> item) {
				final RobCounterCollection counterCollection = item.getModelObject();
		        final WebMarkupContainer wmcDeact = new WebMarkupContainer("wmcDeact");
		        wmcDeact.setOutputMarkupPlaceholderTag(true);
		        
		        final WebMarkupContainer wmcAct = new WebMarkupContainer("wmcAct");
		        wmcAct.setOutputMarkupPlaceholderTag(true);
		        
				item.add(new SSMLabel("index" , item.getIndex()+1));
				item.add(new SSMLabel("ipAddress", counterCollection.getIpAddress()));
				item.add(new SSMLabel("floor", counterCollection.getFloorLevel(), Parameter.FLOOR_LVL));
				item.add(new SSMLabel("branch", counterCollection.getBranch(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("counterName", counterCollection.getCounterName()));
				item.add(new SSMLabel("counterType", counterCollection.getCounterType(), Parameter.PAYMENT_COUNTER_TYPE));
				item.add(new SSMLabel("createBy", counterCollection.getCreateBy()));
				item.add(new SSMLabel("createDt", counterCollection.getCreateDt(), "dd/MM/yyyy hh:mm:ss a"));

				if(counterCollection.getIsActive().equalsIgnoreCase(Parameter.YES_NO_no)){
					wmcDeact.setVisible(true);
					wmcAct.setVisible(false);
				}else{
					wmcDeact.setVisible(false);
					wmcAct.setVisible(true);
				}
				
				SSMAjaxLink deactivated = new SSMAjaxLink("deactivated", item.getDefaultModel()){
					@Override
					public void onClick(AjaxRequestTarget target){
						RobCounterCollection counter = item.getModelObject();
						counter.setIsActive(Parameter.YES_NO_yes);
						counter.setSoftDelete(Parameter.YES_NO_no);
						robCounterCollectionService.update(counter);
						
						wmcDeact.setVisible(false);
						wmcAct.setVisible(true);
						
						target.add(wmcDeact);
						target.add(wmcAct);
					}
				};
				
				wmcDeact.add(deactivated);
				SSMAjaxLink activated = new SSMAjaxLink("activated", item.getDefaultModel()){
					@Override
					public void onClick(AjaxRequestTarget target){
						RobCounterCollection counter = item.getModelObject();
						counter.setIsActive(Parameter.YES_NO_no);
						counter.setSoftDelete(Parameter.YES_NO_yes);
						robCounterCollectionService.update(counter);
						wmcDeact.setVisible(true);
						wmcAct.setVisible(false);
						
						target.add(wmcDeact);
						target.add(wmcAct);
					}
				};
				
				wmcAct.add(activated);
				
				SSMAjaxLink edit = new SSMAjaxLink("edit", item.getDefaultModel()){
					@Override
					public void onClick(AjaxRequestTarget target){
						RobCounterCollection counter = item.getModelObject();
						setResponsePage(new EditCounterCollection(counter.getIpAddress()));
					}
				};
				
				item.add(edit);
				
				item.add(wmcAct);
				item.add(wmcDeact);
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

		counterList.add(dataView);
		counterList.add(new SSMPagingNavigator("navigator", dataView));
		counterList.add(new NavigatorLabel("navigatorLabel", dataView));
		if(target==null){
			add(counterList);
		}else{
			replace(counterList);
			target.add(counterList);
		}
		

	}
	
	public class ListCounterFormModel{
		private String ipAddress;
		private String branch;
		private String floor;
		private String isActive;
		
		private String ipAddressAdd;
		private String branchAdd;
		private String floorAdd;
		private String counterNameAdd;
		private String counterTypeAdd;
		
		private SearchCriteria searchCriteria;
		
		public String getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
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
		public String getIpAddressAdd() {
			return ipAddressAdd;
		}
		public void setIpAddressAdd(String ipAddressAdd) {
			this.ipAddressAdd = ipAddressAdd;
		}
		public String getBranchAdd() {
			return branchAdd;
		}
		public void setBranchAdd(String branchAdd) {
			this.branchAdd = branchAdd;
		}
		public String getFloorAdd() {
			return floorAdd;
		}
		public void setFloorAdd(String floorAdd) {
			this.floorAdd = floorAdd;
		}
		public String getCounterNameAdd() {
			return counterNameAdd;
		}
		public void setCounterNameAdd(String counterNameAdd) {
			this.counterNameAdd = counterNameAdd;
		}
		public String getCounterTypeAdd() {
			return counterTypeAdd;
		}
		public void setCounterTypeAdd(String counterTypeAdd) {
			this.counterTypeAdd = counterTypeAdd;
		}
		public SearchCriteria getSearchCriteria() {
			return searchCriteria;
		}
		public void setSearchCriteria(SearchCriteria searchCriteria) {
			this.searchCriteria = searchCriteria;
		}
		public String getIsActive() {
			return isActive;
		}
		public void setIsActive(String isActive) {
			this.isActive = isActive;
		}
	}
	
	public String getPageTitle() {
		String titleKey = "menu.myBiz.listCollectionCounter";
		return titleKey;
	}
}
