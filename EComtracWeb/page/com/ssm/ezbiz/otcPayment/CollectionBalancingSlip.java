package com.ssm.ezbiz.otcPayment;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.counter.ListCheckInCounter;
import com.ssm.ezbiz.service.RobCounterBalancingService;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterBalancing;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({ "all" })
public class CollectionBalancingSlip extends SecBasePage {

	@SpringBean(name = "RobCounterCollectionService")
	RobCounterCollectionService robCounterCollectionService;
	
	public CollectionBalancingSlip() {
		super();
	}

	public CollectionBalancingSlip(Integer counterSessionId) {
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		System.out.print("branch : " + robCounterSession.getBranch());
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy"); 
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mma");
		add(new SSMLabel("userId", robCounterSession.getUserId()));
		add(new SSMLabel("balancingBy", robCounterSession.getUserId() + "/" + sdfDate.format(robCounterSession.getBalancingDt()) + "/" + sdfTime.format(robCounterSession.getBalancingDt())));
		add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress()
				.getCounterName()));
		add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
		add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
		add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		add(new SSMLabel("checkoutDate", robCounterSession.getCheckoutDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		add(new SSMLabel("balancingStatus",
				robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));

		AjaxLink done = new AjaxLink<Void>("done") {
			@Override
			public void onClick(AjaxRequestTarget target) {
		        if(isMaintenance(robCounterSession)){
		        	setResponsePage(ListCheckInCounter.class);
		        }else{
		        	setResponsePage(UserCheckinPage.class);
		        }
				    
			}
		};
		
		add(done);
		
		SearchCriteria sc = new SearchCriteria("counterSessionId.sessionId",
				SearchCriteria.EQUAL, robCounterSession.getSessionId());
		sc = sc.andIfNotNull("quantity", SearchCriteria.NOT_EQUAL, 0);
		
		populateTable(sc);
	}
	
	public Boolean isMaintenance(RobCounterSession robCounterSession){
		
		if(!robCounterSession.getUserId().equalsIgnoreCase(robCounterSession.getCheckoutBy())){
			return true;
		}
		return false;
	}

	public void populateTable(SearchCriteria sc) {

		List<RobCounterBalancing> listBalancing = getService(
				RobCounterBalancingService.class.getSimpleName()).findByCriteria(sc).getList();
		
		Double total = 0.0;
		for (RobCounterBalancing i : listBalancing) {
			total = total + i.getAmount();
		}
		
		add(new SSMLabel("totalAmount", total));
		add(new ListView<RobCounterBalancing>("listBalancing", listBalancing) {
			protected void populateItem(final ListItem<RobCounterBalancing> item) {
				final RobCounterBalancing robCounterBalancing = item
						.getModelObject();

				item.add(new SSMLabel("item", robCounterBalancing.getBankNote()));
				item.add(new SSMLabel("quantity", robCounterBalancing
						.getQuantity()));
				item.add(new SSMLabel("amount", robCounterBalancing.getAmount()));

			}
		});
	}
}
