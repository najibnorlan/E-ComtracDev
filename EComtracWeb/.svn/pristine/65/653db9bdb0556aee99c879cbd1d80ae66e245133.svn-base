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

import com.ssm.common.util.StringUtil;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpSpecialKeyword;
import com.ssm.llp.base.common.service.LlpSpecialKeywordService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class ListLlpSpecialKeyword extends SecBasePage{
	
	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ListLlpSpecialKeyword(PageParameters params) {
		final String searchString = params.get("searchString") != null ? params.get("searchString").toString() : null;
		String searchString1 = "";
		if(StringUtils.isNotBlank(searchString)){
			
			searchString1 = searchString.toUpperCase();
		}
		add(new SearchSpecialKeyword("searchPanelLlpSpecialKeyword", params));
		add(new AddSpecialKeywordPanel("addSpecialKeywordPanel"));

		SearchCriteria sc = null;
		if(StringUtils.isNotBlank(searchString)){
			sc = SearchCriteria.andIfNotNull(sc , "vchkeywords", SearchCriteria.LIKE, searchString1 + "%"); 
		}
 
		SSMSortableDataProvider dp = new SSMSortableDataProvider("vchkeywords", sc, LlpSpecialKeywordService.class);
		final SSMDataView<LlpSpecialKeyword> dataView = new SSMDataView<LlpSpecialKeyword>("sortingSP", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpSpecialKeyword> item) {
				final LlpSpecialKeyword llpSpecialKeyword = item.getModelObject();

				item.add(new SSMLabel("vchkeywords", llpSpecialKeyword.getVchkeywords()));
				item.add(new SSMLabel("chrkeywordtype", llpSpecialKeyword.getChrkeywordtype()));
				item.add(new SSMLabel("vchcreateby", llpSpecialKeyword.getVchcreateby()));
				item.add(new SSMLabel("dtcreatedate", llpSpecialKeyword.getDtcreatedate()));
				
				item.add(new Link("edit", item.getDefaultModel()) {
					public void onClick() {
						LlpSpecialKeyword llpSpecialKeyword = (LlpSpecialKeyword) getModelObject();
						PageParameters params = new PageParameters();
						
						params.add("id_keyword", llpSpecialKeyword.getId_keyword());
						params.add("vchkeywords", llpSpecialKeyword.getVchkeywords());
						params.add("chrkeywordtype", llpSpecialKeyword.getChrkeywordtype());
						params.add("searchString", searchString);
						setResponsePage(new EditSpecialKeywordPage(params));
					}

				});

				item.add(new Link("delete", item.getDefaultModel()) {
					public void onClick() {
						LlpSpecialKeyword llpSpecialKeyword = (LlpSpecialKeyword) getModelObject();
						PageParameters params = new PageParameters();
						
						params.add("searchString", searchString);
						getService(LlpSpecialKeywordService.class.getSimpleName()).delete(llpSpecialKeyword);
						
						setResponsePage(new ListLlpSpecialKeyword(params));
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
		
		add(new OrderByBorder("orderByResultVchkeywords", "vchkeywords", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
		add(new LlpSpecialKeywordForm("form", getDefaultModel()));
	}

	private class LlpSpecialKeywordForm extends Form {
		public LlpSpecialKeywordForm(String id, IModel m) {
			super(id, m);

		}
	}

	public void sortingPage(List<LlpSpecialKeyword> listResult) {

	}
	
}
