package com.ssm.llp.mod1.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

public class ViewNameList extends LlpReservedNameBasePage {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
	public ViewNameList(PageParameters params) {
		StringValue searchString = params.get("searchString");// .toString();
		String strParam = "";
		if (searchString != null && searchString.toString() != null) {
			strParam = searchString.toString();
		}

		SearchCriteria sc = new SearchCriteria("applyLlpName", "LIKE", strParam + "%");

		SSMSortableDataProvider dp = new SSMSortableDataProvider("applyLlpName", sc, LlpReservedNameService.class);
		final DataView<LlpReservedName> dataView = new DataView<LlpReservedName>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpReservedName> item) {
				LlpReservedName llpReservedName = item.getModelObject();
				// item.add(new ActionPanel("actions", item.getModel()));
				item.add(new SSMLabel("idReservedName", String.valueOf(llpReservedName.getIdReservedName())));
				item.add(new SSMLabel("refNo", llpReservedName.getRefNo()));
				item.add(new SSMLabel("applyLlpName", llpReservedName.getApplyLlpName()));
				item.add(new SSMLabel("llpNo", llpReservedName.getLlpNo()));
				// item.add(new SSMLabel("resultDate",
				// String.(llpReservedName.getResultDate()));
				// item.add(new SSMLabel("expNameDate",
				// llpReservedName.getExpNameDate()));
				// item.add(new
				// SSMLabel("purposeApply",llpReservedName.getPurposeApply()));
				// item.add(new SSMLabel("extraExplanation",
				// llpReservedName.getExtraExplanation()));
				item.add(new SSMLabel("status", llpReservedName.getStatus()));
				// item.add(new
				// SSMLabel("llpCompanyType",llpReservedName.getLlpCompanyType()));
				// item.add(new SSMLabel("profAuthOrg",
				// llpReservedName.getProfAuthOrg()));
				// item.add(new SSMLabel("profAuthLetterRefNo",
				// llpReservedName.getProfAuthLetterRefNo()));
				// // item.add(new
				// SSMLabel("profAuthLetterDate",llpReservedName.getProfAuthLetterDate()));
				// item.add(new SSMLabel("profLetterPurpose",
				// llpReservedName.getProfLetterPurpose()));
				// item.add(new SSMLabel("profLetterSign",
				// llpReservedName.getProfLetterSign()));
				// item.add(new
				// SSMLabel("profRemark",llpReservedName.getProfRemark()));

				item.add(new Link("edit", item.getDefaultModel()) {
					public void onClick() {
						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
						setResponsePage(new EditLlpReservedNamePage(llpReservedName.getRefNo(),null));
					}
				});
				item.add(new Link("delete", item.getDefaultModel()) {
					public void onClick() {
						LlpReservedName llpReservedName = (LlpReservedName) getModelObject();
						getService(LlpReservedNameService.class.getSimpleName()).delete(llpReservedName);
						setResponsePage(ViewNameList.class);
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

		dataView.setItemsPerPage(20L);

		add(new Button("llpReservedName") {
			public void onSubmit() {
				LlpReservedName llpReservedName = (LlpReservedName) getForm().getModelObject();

				// if(){
				//
				// }
				//
				// if (llpReservedName.getIdReservedName()==0) {
				// getService(LlpReservedNameService.class.getSimpleName()).insert(llpReservedName);
				// }else{
				// getService(LlpReservedNameService.class.getSimpleName()).update(llpReservedName);
				// }
				setResponsePage(EditLlpReservedNamePage.class);
			}
		});

		add(new Button("llpRegistration") {
			public void onSubmit() {
				LlpRegistration llpRegistration = (LlpRegistration) getForm().getModelObject();
				//
				// if (llpReservedName.getIdReservedName()==0) {
				// getService(LlpReservedNameService.class.getSimpleName()).insert(llpReservedName);
				// }else{
				// getService(LlpReservedNameService.class.getSimpleName()).update(llpReservedName);
				// }
				setResponsePage(EditLlpReservedNamePage.class);
			}
		});

		add(new OrderByBorder("orderByResultRef", "refNo", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		// add(new OrderByBorder("orderByLLPName", "applyLlpName", dp)
		// {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// protected void onSortChanged()
		// {
		// dataView.setCurrentPage(0);
		// }
		// });

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}

	public void sortingPage(List<LlpReservedName> listResult) {

	}

}
