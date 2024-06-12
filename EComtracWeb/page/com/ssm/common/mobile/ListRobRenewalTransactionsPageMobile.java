package com.ssm.common.mobile;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobRenewal;

public class ListRobRenewalTransactionsPageMobile extends SecBasePageMobile {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ListRobRenewalTransactionsPageMobile(PageParameters params) {
		StringValue searchString = params.get("searchString");// .toString();
		String strParam = "";
		if (searchString != null && searchString.toString() != null) {
			strParam = searchString.toString();
		}

		
		SearchCriteria sc = new SearchCriteria("status", SearchCriteria.NOT_EQUAL, Parameter.ROB_RENEWAL_STATUS_DATA_ENTRY);
		if(!UserEnvironmentHelper.isInternalUser()){
			SearchCriteria sc1 = new SearchCriteria("status", SearchCriteria.NOT_EQUAL, Parameter.ROB_RENEWAL_STATUS_DATA_ENTRY);
			SearchCriteria sc2 = new SearchCriteria("createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
			sc = new SearchCriteria(sc1, SearchCriteria.AND, sc2);
		}
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING, sc, RobRenewalService.class);
		
		
		final SSMDataView<RobRenewal> dataView = new SSMDataView<RobRenewal>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<RobRenewal> item) {
				java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
				final RobRenewal robRenewal = item.getModelObject();

//				item.add(new SSMLabel("transCode", robRenewal.getTransCode()));
				item.add(new SSMLabel("brNo", robRenewal.getBrNo()+"-"+robRenewal.getChkDigit() ));
				item.add(new SSMLabel("bizName", robRenewal.getBizName() ));
				item.add(new SSMLabel("status", robRenewal.getStatus(), Parameter.ROB_RENEWAL_STATUS ));
				item.add(new SSMLabel("createDt", robRenewal.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
				
				
				Link link = new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						RobRenewal robRenewal = (RobRenewal) getModelObject();
						setResponsePage(new EditRobRenewalPageMobile(robRenewal));
					}
					
				};
				item.add(link);
				

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

//		add(new OrderByBorder("orderByResultRef", "transCode", dp) {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onSortChanged() {
//				dataView.setCurrentPage(0);
//			}
//		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
//		add(new RobRenewalForm("form", getDefaultModel()));
	}

	public ListRobRenewalTransactionsPageMobile(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(RobRenewalService.class.getSimpleName()).findById(refNo);
			}
		}));
		init();
	}

	private void init() {
//		add(new RobRenewalForm("form", getDefaultModel()));
	}

//	private class RobRenewalForm extends Form {
//		public RobRenewalForm(String id, IModel m) {
//			super(id, m);
//
//		}
//	}

	public void sortingPage(List<RobRenewal> listResult) {

	}
	
	public String getPageTitle() {
		String titleKey = "page.title.mybiz.renewalTransaction";
		return titleKey;
	}

}
