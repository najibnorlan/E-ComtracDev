package com.ssm.ezbiz.otcPayment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.GenerateBankinSlipPage.ViewBankinSlipModel;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class SearchBankinSlipDetail extends SecBasePage{

	@SpringBean(name = "RobCounterBankinSlipService")
	RobCounterBankinSlipService robCounterBankinSlipService; 
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService; 
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	public SearchBankinSlipDetail(String bankinSlipNo){
		final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		SearchCriteria sc = new SearchCriteria("counterBankinSlipNo.bankinSlipNo", SearchCriteria.EQUAL, bankinSlipNo);
		final List<RobCounterSession> sessionList = getService(RobCounterSessionService.class.getSimpleName()).findByCriteria(sc).getList();
		final RobCounterBankinSlip bankinSlip = robCounterBankinSlipService.findByBankinSlipNo(bankinSlipNo);
		
		populateData(sessionList);
		
		final SSMLabel slipNo = new SSMLabel("slipNo", bankinSlip.getBankinSlipNo());
		slipNo.setOutputMarkupId(true);
		add(slipNo);
		
		DecimalFormat decimal = new DecimalFormat("#,###,##0.00");
		add(new SSMLabel("totalAmount", decimal.format(bankinSlip.getAmount())));
		add(new SSMLabel("branch", bankinSlip.getBranch(), Parameter.BRANCH_CODE));
		add(new SSMLabel("floor", bankinSlip.getFloor(), Parameter.FLOOR_LVL));
		add(new SSMLabel("createDt", bankinSlip.getCreateDt(),  "dd/MM/yyyy hh:mm:ss a"));
		add(new SSMLabel("createBy", bankinSlip.getCreateBy()));
		
		final Link back = new Link("back") {
			@Override
			public void onClick() {
				setResponsePage(new SearchBankinSlip(bankinSlip.getBankinSlipNo()));
			}
		};
		
		back.setOutputMarkupId(true);
		
		add(back);
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
	
	@Override
	public String getPageTitle() {
		return "page.title.ezbiz.bankinSlipDetail.title";
	}
}
