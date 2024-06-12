package com.ssm.llp.mod1.page;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

public class ViewListLlpReservedNames extends SecBasePage {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ViewListLlpReservedNames(PageParameters params) {
		StringValue searchString = params.get("searchString");// .toString();
		String strParam = "";
		if (searchString != null && searchString.toString() != null) {
			strParam = searchString.toString();
		}

		SearchCriteria sc1 = new SearchCriteria("applyLlpName", SearchCriteria.LIKE, strParam + "%");
		SearchCriteria sc2 = new SearchCriteria("lodgeBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
		SearchCriteria sc = new SearchCriteria(sc1, SearchCriteria.AND, sc2);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("idReservedName", sc, LlpReservedNameService.class);
		final SSMDataView<LlpReservedName> dataView = new SSMDataView<LlpReservedName>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpReservedName> item) {
				java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
				final LlpReservedName llpReservedName = item.getModelObject();

//				item.add(new SSMLabel("idReservedName", String.valueOf(llpReservedName.getIdReservedName())));
				item.add(new SSMLabel("refNo", llpReservedName.getRefNo()));
				item.add(new SSMLabel("applyLlpName", llpReservedName.getApplyLlpName()));
				item.add(new SSMLabel("llpNo", llpReservedName.getLlpNo()));
				item.add(new SSMLabel("resultDate", llpReservedName.getResultDate()));
				item.add(new SSMLabel("expNameDate", llpReservedName.getExpNameDate()));
				item.add(new SSMLabel("status", llpReservedName.getStatus(), Parameter.RESERVE_NAME_STATUS));

				item.add(new Link("detail", item.getDefaultModel()) {
					public void onClick() {
						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
						setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),llpReservedName.getConversionType()));
					}

				});

				item.add(new Link("llpRegistration", item.getDefaultModel()) {
					public void onClick() {
						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
						setResponsePage(new LlpRegistrationBasePage(llpReservedName.getRefNo(), null));
					}

					public boolean isVisible() {
						java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());
						
						if(StringUtils.isNotBlank(llpReservedName.getLlpNo())){
							return false;
						}
						if(Parameter.RESERVE_NAME_STATUS_pending_payment.equals(llpReservedName.getStatus())){
							return false;
						}
						
						if ((llpReservedName.getExpNameDate().after(currentDate))) {
							return true;
						}
						
						
						return false;
					};
				});

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

		add(new OrderByBorder("orderByResultRef", "refNo", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
		add(new LlpReservedNameForm("form", getDefaultModel()));
	}

	public ViewListLlpReservedNames(final String refNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpReservedNameService.class.getSimpleName()).findById(refNo);
			}
		}));
		init();
	}

	private void init() {
		add(new LlpReservedNameForm("form", getDefaultModel()));
	}

	private class LlpReservedNameForm extends Form {
		public LlpReservedNameForm(String id, IModel m) {
			super(id, m);

		}
	}

	public void sortingPage(List<LlpReservedName> listResult) {

	}
	
	public String getPageTitle() {
		String titleKey = "page.title.reservename.userreservedname";
		return titleKey;
	}

}
