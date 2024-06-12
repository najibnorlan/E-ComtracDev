package com.ssm.ezbiz.otcPayment;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobCounterBalancingService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterBalancing;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class ViewCollectionVerificationPage extends SecBasePage{

	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;

	@SpringBean(name = "RobCounterBalancingService")
	RobCounterBalancingService robCounterBalancingService;
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService;
	
	public ViewCollectionVerificationPage(){
		super();
	}
	
	public ViewCollectionVerificationPage(Integer counterSessionId, String process){
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		Integer countTransactions = llpPaymentReceiptService
				.getCountTransactionByCounterSession(robCounterSession
						.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		final Double sumTransactions = llpPaymentReceiptService
				.getTotalTransactionByCounterSession(robCounterSession
						.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
		
		add(new SSMLabel("countTransactions", countTransactions));
		add(new SSMLabel("sumTransactions", sumTransactions));
		add(new SSMLabel("userId", robCounterSession.getUserId()));
		add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress()
				.getCounterName()));
		add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
		add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
		add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		add(new SSMLabel("checkoutDate", robCounterSession.getCheckoutDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		add(new SSMLabel("totalAmount", sumTransactions));
		
		AjaxLink verify = new AjaxLink<Void>("verify") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				robCounterSession.setVerifyDt(new Date());
				robCounterSession.setVerifyBy(UserEnvironmentHelper.getLoginName());
				robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_verified);
				robCounterSessionService.update(robCounterSession);
				try {
					setResponsePage(new ListCollectionVerification(robCounterSession));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
		add(verify);
		
		AjaxLink approve = new AjaxLink<Void>("approve") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				robCounterSession.setApproveDt(new Date());
				robCounterSession.setApproveBy(UserEnvironmentHelper.getLoginName());
				robCounterSession.setBalancingStatus(Parameter.BALANCING_STATUS_approved);
				robCounterSessionService.update(robCounterSession);
				try {
					setResponsePage(new ListCollectionVerification(robCounterSession));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
		add(approve);
		
		AjaxLink cancel = new AjaxLink<Void>("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					setResponsePage(new ListCollectionVerification(robCounterSession));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
		add(cancel);
		
		if(process.equalsIgnoreCase(Parameter.BALANCING_STATUS_approved)){
			verify.setVisible(false);
			approve.setVisible(true);
		}else{
			verify.setVisible(true);
			approve.setVisible(false);
		}
		
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId", SearchCriteria.EQUAL, robCounterSession.getSessionId());
		populateData(sc);
	}
	
	public void populateData(SearchCriteria sc){
		
		List<RobCounterBalancing> balancings = getService(RobCounterBalancingService.class.getSimpleName()).findByCriteria(sc).getList();
		
		add(new ListView<RobCounterBalancing>("balancings", balancings) {
			public void populateItem(final ListItem<RobCounterBalancing> item) {
				final RobCounterBalancing balancing = item.getModelObject();
				item.add(new SSMLabel("bankNotes", balancing.getBankNote()));
				item.add(new SSMLabel("quantity", balancing.getQuantity()));
				item.add(new SSMLabel("amount", balancing.getAmount()));
			}
		});
	}
}
