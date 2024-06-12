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
import org.apache.wicket.model.IModel;
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

public class ListLlpReservedNamesOfficer extends SecBasePage {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ListLlpReservedNamesOfficer(PageParameters params) {
		final String applyLlpName = params.get("applyLlpName") != null ? params.get("applyLlpName").toString() : null;
		final String refNo = params.get("refNo") != null ? params.get("refNo").toString() : null;
		
		add(new SearchLlpReservedNameOfficer("searchPanelLlpReserve", params));
		
		
		SearchCriteria sc = null;
		if(StringUtils.isNotBlank(applyLlpName)){
			sc = SearchCriteria.andIfNotNull(sc , "applyLlpName", SearchCriteria.LIKE, applyLlpName + "%"); 
		}
		if(StringUtils.isNotBlank(refNo)){
			sc = SearchCriteria.andIfNotNull(sc, "refNo", SearchCriteria.EQUAL, refNo);
		}
		if(!UserEnvironmentHelper.isInternalUser()){ //if non ssm
			sc = SearchCriteria.andIfNotNull(sc, "lodgeBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
		}

		SSMSortableDataProvider dp = new SSMSortableDataProvider("applyLlpName", sc, LlpReservedNameService.class);
		final SSMDataView<LlpReservedName> dataView = new SSMDataView<LlpReservedName>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpReservedName> item) {
				final LlpReservedName llpReservedName = item.getModelObject();

				//item.add(new SSMLabel("idReservedName", String.valueOf(llpReservedName.getIdReservedName())));
				item.add(new SSMLabel("refNo", llpReservedName.getRefNo()));
				item.add(new SSMLabel("applyLlpName", llpReservedName.getApplyLlpName()));
				item.add(new SSMLabel("llpNo", llpReservedName.getLlpNo()));
				item.add(new SSMLabel("resultDate", llpReservedName.getResultDate()));
				item.add(new SSMLabel("expNameDate",llpReservedName.getExpNameDate()));
				item.add(new SSMLabel("status", llpReservedName.getStatus(), Parameter.RESERVE_NAME_STATUS));

				item.add(new Link("edit", item.getDefaultModel()) {
					public void onClick() {
						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
						setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),llpReservedName.getConversionType()));
					}

				});

//				item.add(new Link("delete", item.getDefaultModel()) {
//					public void onClick() {
//						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
//						getService(LlpReservedNameService.class.getSimpleName()).delete(llpReservedName);
//						setResponsePage(ListLlpReservedNamesOfficer.class);
//					}
//
//				});

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

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

	private class LlpReservedNameForm extends Form {
		public LlpReservedNameForm(String id, IModel m) {
			super(id, m);

		}
	}

	public void sortingPage(List<LlpReservedName> listResult) {

	}
	
	public String getPageTitle() {
		String titleKey = "page.title.reservename.list";
		return titleKey;
	}

}
