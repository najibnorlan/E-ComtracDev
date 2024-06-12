package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Param;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;

@SuppressWarnings({ "all" })
public class UserCheckinPage extends SecBasePage {
	
	@SpringBean(name = "RobCounterCollectionService")
	private RobCounterCollectionService robCounterCollectionService;
	
	@SpringBean(name = "RobCounterSessionService")
	private RobCounterSessionService robCounterSessionService;
	
	public UserCheckinPage() {
		final Boolean checkedin = checkCurrentSession();
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
		        
				String ipAddress = getIpAddress();
		        RobCounterCollection robCounterCollection = robCounterCollectionService.findByIpAddress(ipAddress);
		        
		        UserCheckinModel userCheckinModel = new UserCheckinModel();
		        userCheckinModel.setBranch(robCounterCollection.getBranch());
		        userCheckinModel.setCounterName(robCounterCollection.getCounterName());
		        userCheckinModel.setFloorNumber(robCounterCollection.getFloorLevel());
		        userCheckinModel.setUserId(UserEnvironmentHelper.getLoginName());
		        userCheckinModel.setIpAddress(robCounterCollection.getIpAddress());
		        userCheckinModel.setIsOpen(checkedin);
		        
				return userCheckinModel;
			}
		}));
		add(new UserCheckinPageForm("form", (IModel<UserCheckinModel>) getDefaultModel()));
		
	}
	
	public Boolean checkCurrentSession(){
		
		String ipAddress = null;
		String userId = null;
		Boolean checkedIn = false;
		
		RobCounterSession sessionObj = (RobCounterSession) SignInSession.get().getAttribute("_currentCheckinSession");
		if(sessionObj != null){
			userId = sessionObj.getUserId();
			ipAddress = sessionObj.getCounterIpAddress().getIpAddress();
		}else{
			userId = UserEnvironmentHelper.getLoginName();
			ipAddress = getIpAddress();
		}
		
		RobCounterSession robCounterSession = robCounterSessionService.
				findByIpAddressStatusNDE(ipAddress);
		
		if(robCounterSession != null){
			if(robCounterSession.getUserId().equalsIgnoreCase(UserEnvironmentHelper.getLoginName())){
				System.out.println("balancing : " + robCounterSession.getBalancingStatus() + " | is_open : " + robCounterSession.getIsOpen());
				
				if(robCounterSession.getBalancingStatus().equalsIgnoreCase(Parameter.BALANCING_STATUS_no)){
					SignInSession.get().setAttribute("_currentCheckinSession", robCounterSession);
					if(robCounterSession.getIsOpen() == 1){
						setResponsePage(new ListOtcPaymentPage(robCounterSession.getSessionId()));
					}else{
						setResponsePage(new ViewTransactionSummary(robCounterSession.getSessionId(), false));
					}
				}else if(robCounterSession.getBalancingStatus().equalsIgnoreCase(Parameter.BALANCING_STATUS_new)){
					SignInSession.get().setAttribute("_currentCheckinSession", robCounterSession);
					setResponsePage(new CollectionBalancingPage(robCounterSession.getSessionId()));
				}else{
					checkedIn = false;
				}
			}else{
				checkedIn = true;
			}
		}
		
		return checkedIn;
	}
	
	private class UserCheckinPageForm extends Form implements Serializable {
		public UserCheckinPageForm(String id, IModel<UserCheckinModel> m) {
			super(id, m);
			final UserCheckinModel checkinModel = m.getObject();
			
			add(new SSMLabel("userId", checkinModel.getUserId()));
			add(new SSMLabel("counterName", checkinModel.getCounterName()));
			add(new SSMLabel("branch", checkinModel.getBranch(), Parameter.BRANCH_CODE));
			add(new SSMLabel("floorLevel", checkinModel.getFloorNumber(), Parameter.FLOOR_LVL));
			Link checkin = new Link("checkin") {
				
				@Override
				public void onClick() {
					RobCounterSession robCounterSession = new RobCounterSession();
					robCounterSession.setUserId(checkinModel.getUserId());
					robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_no);
					robCounterSession.setCheckinDate(new java.util.Date());
					robCounterSession.setFloorLevel(checkinModel.getFloorNumber());
					robCounterSession.setBranch(checkinModel.getBranch());
					robCounterSession.setCounterIpAddress(robCounterCollectionService.findByIpAddress(checkinModel.getIpAddress()));
					robCounterSession.setIsOpen(Parameter.COLLECTION_COUNTER_IS_OPEN_yes);
					robCounterSessionService.insert(robCounterSession);
					
					robCounterSession = robCounterSessionService.findByUserIdIpAddressOpen(checkinModel.userId, checkinModel.getIpAddress());
					SignInSession.get().setAttribute("_currentCheckinSession", robCounterSession);
					setResponsePage(new ListOtcPaymentPage(robCounterSession.getSessionId()));
				}
				
			};
			checkin.setVisible(true);
			checkin.setOutputMarkupId(true);
			
			add(checkin);
			
			SSMLabel error = new SSMLabel("error", SSMExceptionParam.OTC_PAYMENT_COUNTER_ALREADY_CHECKIN);
			error.setVisible(false);
			add(error);
			
			if(checkinModel.getIsOpen() == true){
				checkin.setVisible(false);
				error.setVisible(true);
				
			}
			
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			
			SearchCriteria sc = null;
			if(StringUtils.isNotBlank(checkinModel.getIpAddress())){
				try{
					sc = new SearchCriteria("counterIpAddress.ipAddress", SearchCriteria.EQUAL, checkinModel.getIpAddress());
					sc = sc.andIfNotNull("checkinDate", SearchCriteria.GREATER_EQUAL, pars.parse(form.format(date) + " 00:00:00"));
					sc = sc.andIfNotNull("checkinDate", SearchCriteria.LESS_EQUAL, pars.parse(form.format(date) + " 23:59:59"));
				}catch(Exception ex){
					System.out.print("Error creating query");
				}
			}
			populateTable(sc);
			
			
		}
	}
	
	public void populateTable(SearchCriteria sc){
		WebMarkupContainer paymentList = new WebMarkupContainer("paymentList");
		paymentList.setOutputMarkupId(true);
		paymentList.setVisible(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("sessionId", SortOrder.ASCENDING, sc, RobCounterSessionService.class);
		final SSMDataView<RobCounterSession> dataView = new SSMDataView<RobCounterSession>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobCounterSession> item) {
				final RobCounterSession robCounterSession = item.getModelObject();
		        
				item.add(new SSMLabel("index" , item.getIndex()+1));
				item.add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(), "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("checkoutDate", robCounterSession.getCheckoutDate(), "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("userId", robCounterSession.getUserId()));
				item.add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus()));
				
				SSMLink reprint = new SSMLink("reprint") {
					@Override
					public void onClick(){
						setResponsePage(new CollectionBalancingSlip(robCounterSession.getSessionId()));
					}
				};
				
				reprint.setOutputMarkupId(true);
				reprint.setVisible(false);
				
				if(robCounterSession.getUserId().equals(UserEnvironmentHelper.getLoginName())){
					reprint.setVisible(true);
				}
				
				item.add(reprint);

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

		paymentList.add(dataView);
		paymentList.add(new SSMPagingNavigator("navigator", dataView));
		paymentList.add(new NavigatorLabel("navigatorLabel", dataView));
		add(paymentList);
		

	}
	
	private class UserCheckinModel implements Serializable{
		private String counterName;
		private String floorNumber;
		private String branch;
		private String userId;
		private String ipAddress;
		private Boolean isOpen;
		
		public String getCounterName() {
			return counterName;
		}
		public void setCounterName(String counterName) {
			this.counterName = counterName;
		}
		public String getFloorNumber() {
			return floorNumber;
		}
		public void setFloorNumber(String floorNumber) {
			this.floorNumber = floorNumber;
		}
		public String getBranch() {
			return branch;
		}
		public void setBranch(String branch) {
			this.branch = branch;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}
		public Boolean getIsOpen() {
			return isOpen;
		}
		public void setIsOpen(Boolean isOpen) {
			this.isOpen = isOpen;
		}
	}
	
	public String getPageTitle() {
		String titleKey = "page.title.otcPaymentPage.userCheckin";
		return titleKey;
	}
	

}
