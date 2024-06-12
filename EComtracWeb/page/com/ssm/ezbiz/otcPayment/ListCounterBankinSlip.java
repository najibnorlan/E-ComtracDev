package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@SuppressWarnings({"all"})
public class ListCounterBankinSlip extends SecBasePage{

	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService;
	
	public ListCounterBankinSlip() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				CounterBankinSlipModel bankinSlipModel = new CounterBankinSlipModel();
				bankinSlipModel.setDate(new Date());
				return bankinSlipModel;
			}
		}));
		add(new ListCounterBankinSlipForm("form", (IModel<CounterBankinSlipModel>) getDefaultModel()));
	}
	
	public ListCounterBankinSlip(final String branch, final String floor, final Date date) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
				CounterBankinSlipModel bankinSlipModel = new CounterBankinSlipModel();
				bankinSlipModel.setDate(date);
				bankinSlipModel.setBranch(branch);
				bankinSlipModel.setFloor(floor);
				return bankinSlipModel;
			}
		}));
		add(new ListCounterBankinSlipForm("form", (IModel<CounterBankinSlipModel>) getDefaultModel()));
	}
	
	private class ListCounterBankinSlipForm extends Form implements Serializable {

		public ListCounterBankinSlipForm(String id, IModel<CounterBankinSlipModel> m) {
			super(id, m);
			
			CounterBankinSlipModel bankinSlipModel = m.getObject();
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", "BRANCH_CODE");
			branch.setLabelKey("page.lbl.ezbiz.counterbankinslip.branch");
			add(branch);
			
			SSMDropDownChoice floor = new SSMDropDownChoice("floor", "FLOOR_LVL");
			floor.setLabelKey("page.lbl.ezbiz.counterbankinslip.floor");
			add(floor);
			
			SSMDateTextField date = new SSMDateTextField("date");
			date.setLabelKey("page.lbl.ezbiz.counterbankinslip.date");
			add(date);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					CounterBankinSlipModel searchModelForm = (CounterBankinSlipModel) form.getDefaultModelObject();
					String date = null;
					if(searchModelForm.getDate() != null){
						date = df.format(searchModelForm.getDate());
					}
					
					List<Object> listToGen = robCounterSessionService.getBranchAndFloorNoBankSlip(searchModelForm.getBranch(), searchModelForm.getFloor(), date, Parameter.YES_NO_yes);
					populateTable(listToGen, searchModelForm.getDate(), target);
				}
			};
			add(search);
			
			populateTable(null, null, null);
			
		}
		

	}
	
	public void populateTable(List<Object> listToGen, final Date date, AjaxRequestTarget target){
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);
		final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		
		ListView listView = new ListView<Object>("listView", listToGen) {
			public void populateItem(final ListItem<Object> item) {
				final Object obj = item.getModelObject();
				final Object[] data = (Object[]) obj;
				
				// 0 - branch | 1 - floor | 2 - total | 3 - bil
				Double total = Double.valueOf(data[2].toString());
				item.add(new SSMLabel("branch", data[0].toString(), Parameter.BRANCH_CODE));
				item.add(new SSMLabel("floor", data[1].toString(), Parameter.FLOOR_LVL));
				item.add(new SSMLabel("total",  df.format(total))); 
				item.add(new SSMLabel("bil", data[3].toString())); 
				item.add(new SSMLabel("index", item.getIndex()+1)); 
				
				List<Object> listnotclose = robCounterSessionService.getBranchAndFloorNoBankSlip(data[0].toString(), data[1].toString(), dateformat.format(date), Parameter.YES_NO_no);
				
				SSMAjaxLink generate = new SSMAjaxLink("generate", item.getDefaultModel()){
					@Override
					public void onClick(AjaxRequestTarget target){
						Object obj = item.getModelObject();
						
						setResponsePage(new GenerateBankinSlipPage(data[0].toString(), data[1].toString(), date));
					}
				};
				
				generate.setOutputMarkupPlaceholderTag(true);
				generate.setEnabled(true);
				if(listnotclose.size() > 0){
					generate.setEnabled(false);
				}
				item.add(generate);
			}
		};
 		wmcSearchResult.add(listView);

		if(target==null){
			add(wmcSearchResult);
		}else{
			replace(wmcSearchResult);
			target.add(wmcSearchResult);
		}
	}
	
	public class CounterBankinSlipModel implements Serializable{
		private String branch;
		private String floor;
		private Date date;
		
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
	}
	
	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return "menu.myBiz.listCounterBankinSlip";
	}
	
}
