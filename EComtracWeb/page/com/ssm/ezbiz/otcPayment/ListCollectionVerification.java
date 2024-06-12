package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;

@SuppressWarnings({"all"})
public class ListCollectionVerification extends SecBasePage{
	
	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService; 
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService; 
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService; 
	
	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "menu.myBiz.collectionVerification";
	}
	
	public ListCollectionVerification() throws ParseException {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				ListCollectionVerificationModel listCollectionVerificationModel = new ListCollectionVerificationModel();
				listCollectionVerificationModel.setDate(new Date());
				listCollectionVerificationModel.setSearchCriteria(null);
				return listCollectionVerificationModel;
			}
		}));
		add(new CollectionVerificationPageForm("form", (IModel<ListCollectionVerificationModel>) getDefaultModel()));
	}
	
	public ListCollectionVerification(final RobCounterSession robCounterSession) throws ParseException {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCollectionVerificationModel listCollectionVerificationModel = new ListCollectionVerificationModel();
				listCollectionVerificationModel.setBranch(robCounterSession.getBranch());
				listCollectionVerificationModel.setFloor(robCounterSession.getFloorLevel());
				listCollectionVerificationModel.setDate(robCounterSession.getCheckoutDate());
				
				SearchCriteria sc = robCounterSessionService.searchCriteriaTemplate(listCollectionVerificationModel.getBranch(), listCollectionVerificationModel.getDate(), listCollectionVerificationModel.getFloor(), listCollectionVerificationModel.getStatus());
				
				listCollectionVerificationModel.setSearchCriteria(sc);
				
				return listCollectionVerificationModel;
			}
		}));
		add(new CollectionVerificationPageForm("form", (IModel<ListCollectionVerificationModel>) getDefaultModel()));
	}
	
	public ListCollectionVerification(final ListCollectionVerificationModel listCollectionVerificationModel) throws ParseException {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchCriteria sc = robCounterSessionService.searchCriteriaTemplate(listCollectionVerificationModel.getBranch(), listCollectionVerificationModel.getDate(), listCollectionVerificationModel.getFloor(), listCollectionVerificationModel.getStatus());
				listCollectionVerificationModel.setSearchCriteria(sc);
				return listCollectionVerificationModel;
			}
		}));
		add(new CollectionVerificationPageForm("form", (IModel<ListCollectionVerificationModel>) getDefaultModel()));
	}
	
	private class CollectionVerificationPageForm extends Form implements Serializable {

		public CollectionVerificationPageForm(String id, IModel<ListCollectionVerificationModel> m) throws ParseException {
			super(id, m);
			
			final ListCollectionVerificationModel listCollectionVerificationModel = m.getObject();
			
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.collectionVerification.branch");
			add(branch);
			
			SSMDropDownChoice floor = new SSMDropDownChoice("floor", Parameter.FLOOR_LVL);
			floor.setLabelKey("page.lbl.ezbiz.collectionVerification.floor");
			add(floor);
			
			SSMDateTextField date = new SSMDateTextField("date");
			date.setLabelKey("page.lbl.ezbiz.collectionVerification.date");
			add(date);
			
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.BALANCING_STATUS);
			status.setLabelKey("page.lbl.ezbiz.collectionVerification.status");
			status.getChoices().add(0, "All");
			add(status);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListCollectionVerificationModel searchModelForm = (ListCollectionVerificationModel) form.getDefaultModelObject();
					
					SearchCriteria sc = robCounterSessionService.searchCriteriaTemplate(searchModelForm.getBranch(), searchModelForm.getDate(), searchModelForm.getFloor(), searchModelForm.getStatus());
					
					populateTable(sc, target);
					
				}
			};
			add(search);
			
			populateTable(listCollectionVerificationModel.getSearchCriteria() , null);
			
		}
		

	}
	
	Double grandTotal= null;
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);
		grandTotal = 0.0;
		final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		
		final SSMLabel total = new SSMLabel("grandTotal", df.format(grandTotal));
		total.setOutputMarkupId(true);
		total.setOutputMarkupPlaceholderTag(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("counterIpAddress.counterName", SortOrder.ASCENDING, sc, RobCounterSessionService.class);
		final SSMDataView<RobCounterSession> dataView = new SSMDataView<RobCounterSession>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			
			protected void populateItem(final Item<RobCounterSession> item) {
				final RobCounterSession robCounterSession = item.getModelObject();
//		        String bankslip = "-";
//		        if(robCounterSession.getCounterBankinSlipNo() != null){
//		        	bankslip = robCounterSession.getCounterBankinSlipNo().getBankinSlipNo();
//		        }
		        
		        Double sumTransactions = llpPaymentReceiptService
						.getTotalTransactionByCounterSession(robCounterSession
								.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		        
		        grandTotal += sumTransactions;
		        total.setDefaultModelObject(df.format(grandTotal));
//		        System.out.println("grandtotal " + (item.getIndex()+1) + " : " + grandTotal);
				item.add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress().getCounterName()));
				item.add(new SSMLabel("userId", robCounterSession.getUserId()));
				item.add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));
				item.add(new SSMLabel("checkoutDt", robCounterSession.getCheckoutDate() , "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("checkinDt",robCounterSession.getCheckinDate() , "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("floor", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
//				item.add(new SSMLabel("bankslip", bankslip));
				item.add(new SSMLabel("sumTransactions", df.format(sumTransactions)));
				
				SSMLabel verifyBy = new SSMLabel("verifyBy", robCounterSession.getVerifyBy());
				SSMLabel verifyDt = new SSMLabel("verifyDt", robCounterSession.getVerifyDt(), "dd/MM/yyyy hh:mm:ss a");
				SSMLabel approveBy = new SSMLabel("approveBy", robCounterSession.getApproveBy());
				SSMLabel approveDt = new SSMLabel("approveDt", robCounterSession.getApproveDt(), "dd/MM/yyyy hh:mm:ss a");
				
				item.add(verifyBy);
				item.add(verifyDt);
				item.add(approveBy);
				item.add(approveDt);
				
				Link verify = new Link("verify", item.getDefaultModel()) {
					public void onClick() {
						RobCounterSession robCounterSession = item.getModelObject();
						setResponsePage(new ViewCollectionVerificationPage(robCounterSession.getSessionId(), Parameter.BALANCING_STATUS_verified));
					}
				};
				item.add(verify);
				
				Link approve = new Link("approve", item.getDefaultModel()) {
					public void onClick() {
						RobCounterSession robCounterSession = item.getModelObject();
						setResponsePage(new ViewCollectionVerificationPage(robCounterSession.getSessionId(), Parameter.BALANCING_STATUS_approved));
					}
				};
				item.add(approve);
				
				if(robCounterSession.getBalancingStatus().equalsIgnoreCase(Parameter.BALANCING_STATUS_data_entry)){
					verify.setVisible(true);
					approve.setVisible(false);
					verifyBy.setVisible(false);
					verifyDt.setVisible(false);
					approveBy.setVisible(false);
					approveDt.setVisible(false);
				}else if(robCounterSession.getBalancingStatus().equalsIgnoreCase(Parameter.BALANCING_STATUS_verified)){
					verify.setVisible(false);
					approve.setVisible(true);
					verifyBy.setVisible(true);
					verifyDt.setVisible(true);
					approveBy.setVisible(false);
					approveDt.setVisible(false);
				}else{
					verify.setVisible(false);
					approve.setVisible(false);
					verifyBy.setVisible(true);
					verifyDt.setVisible(true);
					approveBy.setVisible(true);
					approveDt.setVisible(true);
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
		wmcSearchResult.add(total);
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
	
	
	public class ListCollectionVerificationModel implements Serializable{
		private String branch;
		private String floor;
		private Date date;
		private String status;
		private SearchCriteria searchCriteria;
		private String slipNumber;
		
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
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public SearchCriteria getSearchCriteria() {
			return searchCriteria;
		}
		public void setSearchCriteria(SearchCriteria searchCriteria) {
			this.searchCriteria = searchCriteria;
		}
		public String getSlipNumber() {
			return slipNumber;
		}
		public void setSlipNumber(String slipNumber) {
			this.slipNumber = slipNumber;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
		
	}
}
