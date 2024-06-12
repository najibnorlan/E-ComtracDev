package com.ssm.llp.mod1.page;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
public class ListLlpInformationEnquiry extends LlpProductBasePage {
	public ListLlpInformationEnquiry(PageParameters params) {
		StringValue SearchByLlpNo = params.get("SearchByLlpNo");
		StringValue SearchByLlpName = params.get("SearchByLlpName");
		String strParam = "";
		String strParam2 = "";
		if (SearchByLlpNo != null && SearchByLlpNo.toString() != null) {
			strParam = SearchByLlpNo.toString();
		}

		if (SearchByLlpName != null && SearchByLlpName.toString() != null) {
			strParam2 = SearchByLlpName.toString();
		}

		SearchCriteria sc1 = new SearchCriteria("llpNo", SearchCriteria.EQUAL, strParam);
		SearchCriteria sc2 = new SearchCriteria("llpName", "LIKE", strParam2 + "%");
		SearchCriteria sc = null;

		if (SearchByLlpName != null && StringUtils.isNotBlank(SearchByLlpNo.toString())) {
			sc = SearchCriteria.andIfNotNull(sc, "llpNo", SearchCriteria.EQUAL, SearchByLlpNo.toString());
		}
		if (SearchByLlpName != null && StringUtils.isNotBlank(SearchByLlpName.toString())) {
			sc = SearchCriteria.andIfNotNull(sc, "llpName", SearchCriteria.LIKE, SearchByLlpName.toString() + "%");
		}

		SSMSortableDataProvider dp = new SSMSortableDataProvider("llpNo", sc, LlpRegistrationService.class);
		final DataView<LlpRegistration> dataView = new DataView<LlpRegistration>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpRegistration> item) {
				LlpRegistration llpReg = item.getModelObject();

				item.add(new SSMLabel("idLlp", String.valueOf(llpReg.getIdLlp())));
				item.add(new SSMLabel("llpNo", llpReg.getLlpNo()));
				item.add(new SSMLabel("llpName", llpReg.getLlpName()));
				item.add(new SSMLabel("llpStatus", llpReg.getLlpStatus(), Parameter.LLP_STATUS));
				//item.add(new SSMLabel("llpStatus", llpReg.getLlpStatus()));			      
				item.add(new Link("Detail", item.getDefaultModel()) {
					public void onClick() {
						LlpRegistration l = (LlpRegistration) getModelObject();
						setResponsePage(new DetailsLlpInformation(l.getLlpNo()));	
					}
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

		add(new OrderByBorder("orderByLlpName", "llpName", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByLlpStatus", "llpStatus", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	public void sortingPage(List<LlpRegistration> listResult) {

	}
}
