package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.otcPayment.ViewOtcPaymentPage.PaymentReceivedModel;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class GenerateBankinSlipPage extends SecBasePage{

	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService; 
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService; 
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	public GenerateBankinSlipPage(){
		super();
	}
	
	public GenerateBankinSlipPage(final String branch, final String floor, final Date date){
		final WebMarkupContainer wmcAction = new WebMarkupContainer("wmcAction");
		wmcAction.setOutputMarkupId(true);
		wmcAction.setVisible(true);
		
		final WebMarkupContainer wmcDone = new WebMarkupContainer("wmcDone");
		wmcDone.setOutputMarkupPlaceholderTag(true);
		wmcDone.setVisible(false);
		
		final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		System.out.println("test : " + df.format(date));
		SearchCriteria sc = generateScTemplate(branch, floor, date);
		final List<RobCounterSession> sessionList = getService(RobCounterSessionService.class.getSimpleName()).findByCriteria(sc).getList();
		final Double totalAmount = robCounterSessionService.getTotalAmountByCounterList(sessionList);
		
		populateData(sessionList);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ViewBankinSlipModel viewBankinSlipModel = new ViewBankinSlipModel();
				viewBankinSlipModel.setBranch(branch);
				viewBankinSlipModel.setFloor(floor);
				try {
					viewBankinSlipModel.setDate(df.parse(df.format(date)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				viewBankinSlipModel.setTotalAmount(totalAmount);
				
				return viewBankinSlipModel;
			}
		}));
		
		final SSMLabel slipNo = new SSMLabel("slipNo", "Click Generate! to generate");
		slipNo.setOutputMarkupId(true);
		add(slipNo);
		
		final Link cancel = new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new ListCounterBankinSlip(branch, floor, date));
			}
			
		};
		cancel.setOutputMarkupId(true);
		wmcAction.add(cancel);
		
		final Link done = new Link("done") {
			@Override
			public void onClick() {
				setResponsePage(new ListCounterBankinSlip(branch, floor, date));
			}
		};
		
		done.setOutputMarkupId(true);
		wmcDone.add(done);
		
		final SSMAjaxLink generate = new SSMAjaxLink("generate") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				RobCounterBankinSlip counterBankinSlip = new RobCounterBankinSlip();
				counterBankinSlip.setAmount(totalAmount);
				counterBankinSlip.setYearCreated(df.format(new Date()));
				counterBankinSlip.setBranch(branch);
				counterBankinSlip.setFloor(floor);
				robCounterBankinSlipService.insert(counterBankinSlip);
				
				for(RobCounterSession i : sessionList){
					i.setCounterBankinSlipNo(counterBankinSlip);
					robCounterSessionService.update(i);
				}
				
				slipNo.setDefaultModelObject(counterBankinSlip.getBankinSlipNo());
				
				cancel.setVisible(false);
				wmcAction.setVisible(false);
				wmcDone.setVisible(true);
				
				target.add(slipNo);
				target.add(cancel);
				target.add(wmcAction);
				target.add(wmcDone);
				
			}
			
		};
		
		generate.setOutputMarkupId(true);
		wmcAction.add(generate);
		
		add(wmcAction);
		add(wmcDone);
		
		DecimalFormat decimal = new DecimalFormat("#,###,##0.00");
		ViewBankinSlipModel model = (ViewBankinSlipModel) getDefaultModel().getObject();
		add(new SSMLabel("totalAmount", decimal.format(totalAmount)));
		add(new SSMLabel("branch", branch, Parameter.BRANCH_CODE));
		add(new SSMLabel("floor", floor, Parameter.FLOOR_LVL));
		add(new SSMLabel("collectionDate", model.getDate()));
	}
	
	public void populateData(List<RobCounterSession> counterList){
		
		add(new ListView<RobCounterSession>("counterList", counterList) {
			public void populateItem(final ListItem<RobCounterSession> item) {
				final RobCounterSession robCounterSession = item.getModelObject();
				Double sumTransactions = llpPaymentReceiptService
						.getTotalTransactionByCounterSession(robCounterSession
								.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		        
				item.add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress().getCounterName()));
				item.add(new SSMLabel("userId", robCounterSession.getUserId()));
				item.add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));
				item.add(new SSMLabel("checkoutDt", robCounterSession.getCheckoutDate() , "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("checkinDt",robCounterSession.getCheckinDate() , "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("floor", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
				item.add(new SSMLabel("sumTransactions", sumTransactions));
				
				SSMLabel verifyBy = new SSMLabel("verifyBy", robCounterSession.getVerifyBy());
				SSMLabel verifyDt = new SSMLabel("verifyDt", robCounterSession.getVerifyDt(), "dd/MM/yyyy hh:mm:ss a");
				SSMLabel approveBy = new SSMLabel("approveBy", robCounterSession.getApproveBy());
				SSMLabel approveDt = new SSMLabel("approveDt", robCounterSession.getApproveDt(), "dd/MM/yyyy hh:mm:ss a");
				
				item.add(verifyBy);
				item.add(verifyDt);
				item.add(approveBy);
				item.add(approveDt);
			}
		});
	}
	
	public SearchCriteria generateScTemplate(String branch, String floor, Date date){
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SearchCriteria sc = null;
		if(branch != null){
			sc = new SearchCriteria("branch", SearchCriteria.EQUAL, branch);
		}
		
		if(floor != null){
			if(sc != null){
				sc = sc.andIfNotNull("floorLevel", SearchCriteria.EQUAL, floor);
			}else{
				sc = new SearchCriteria("floorLevel", SearchCriteria.EQUAL, floor);
			}
		}
		
		if(date != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("checkoutDate", SearchCriteria.GREATER_EQUAL,  pars.parse(form.format(date) + " 00:00:00"));
					sc = sc.andIfNotNull("checkoutDate", SearchCriteria.LESS_EQUAL,  pars.parse(form.format(date) + " 23:59:59"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}else{
				try {
					sc = new SearchCriteria("checkoutDate", SearchCriteria.GREATER_EQUAL, pars.parse(form.format(date) + " 00:00:00"));
					sc = sc.andIfNotNull("checkoutDate", SearchCriteria.LESS_EQUAL, pars.parse(form.format(date) + " 23:59:59"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		sc = sc.andIfNotNull("balancingStatus", SearchCriteria.EQUAL, Parameter.BALANCING_STATUS_approved);
		
		SearchCriteria sc2 = new SearchCriteria("counterBankinSlipNo", SearchCriteria.IS_NULL);
		SearchCriteria sc3 = new SearchCriteria(sc, SearchCriteria.AND, sc2);
		
		
		return sc3;
	}
	
	
	public class ViewBankinSlipModel implements Serializable{
		
		private String branch;
		private String floor;
		private Date date;
		private Double totalAmount;
		private String slipNo;
		
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
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getSlipNo() {
			return slipNo;
		}
		public void setSlipNo(String slipNo) {
			this.slipNo = slipNo;
		}
	}
	
	@Override
	public String getPageTitle() {
		return "menu.myBiz.listCounterBankinSlip";
	}
	
}
