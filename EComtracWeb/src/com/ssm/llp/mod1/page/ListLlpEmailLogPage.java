package com.ssm.llp.mod1.page;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpEmailLog;
import com.ssm.llp.base.common.service.LlpEmailLogService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;

public class ListLlpEmailLogPage extends SecBasePage {
	
	public ListLlpEmailLogPage() {
		add(new LlpEmailLogForm("llpEmailLogForm"));
	}


	private class LlpEmailLogForm extends Form {
		private Date searchFromDt;
		private Date searchToDt;
		private String refNo;
		private String status;
		private WebMarkupContainer wmc;
		private SSMDataView dataView;
		private SSMSortableDataProvider dp;

		public LlpEmailLogForm(String id) {
			super(id);
			populateTable();
			
			SSMDateTextField dfFrom = new SSMDateTextField("searchFromDt", new PropertyModel(this, "searchFromDt"), "dd/MM/yyyy");
			dfFrom.setLabelKey("listLlpEmailLog.page.searchFromDt");
			add(dfFrom);

			SSMDateTextField dfTo = new SSMDateTextField("searchToDt", new PropertyModel(this, "searchToDt"), "dd/MM/yyyy");
			dfTo.setLabelKey("listLlpEmailLog.page.searchToDt");
			add(dfTo);

			SSMTextField tfAppRefNo = new SSMTextField("refNo", new PropertyModel(this, "refNo"));
			tfAppRefNo.setLabelKey("listLlpEmailLog.page.refNo");
			add(tfAppRefNo);

			SSMDropDownChoice tfStatus = new SSMDropDownChoice("status", new PropertyModel(this, "status"), Parameter.EMAIL_STATUS);
			tfStatus.setLabelKey("listLlpEmailLog.page.status");
			add(tfStatus);

			add(new AjaxButton("searchBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					rePopulateTable(target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
		}
		
		private void rePopulateTable(AjaxRequestTarget target){
			
			SearchCriteria sc = null;
			if (getSearchFromDt() != null) {
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("sendDate", SearchCriteria.GREATER_EQUAL, getSearchFromDt());
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("sendDate", SearchCriteria.GREATER_EQUAL, getSearchFromDt());
				}
			}

			if (getSearchToDt() != null) {
				Date toDate = new Date(getSearchToDt().getTime() + TimeUnit.DAYS.toMillis(1));
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("sendDate", SearchCriteria.LESS_EQUAL, toDate);
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("sendDate", SearchCriteria.LESS_EQUAL, toDate);
				}
			}
			
			if (StringUtils.isNotBlank((getStatus()))) {
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("status", SearchCriteria.EQUAL, getStatus());
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("status", SearchCriteria.EQUAL, getStatus());
				}
			}

			if (StringUtils.isNotBlank((getRefNo()))) {
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("refNo", SearchCriteria.EQUAL, getRefNo());
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("refNo", SearchCriteria.EQUAL, getRefNo());
				}
			}
			
			dp.setSc(sc);
			target.add(wmc);
		}
		
		private void populateTable() {
			SearchCriteria sc = null;
			
			dp = new SSMSortableDataProvider("sendDate", sc, LlpEmailLogService.class);
			dataView = new SSMDataView<LlpEmailLog>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpEmailLog> item) {
					LlpEmailLog llpEmailLog = item.getModelObject();					
					
					item.add(new SSMLabel("sendDate", llpEmailLog.getSendDate()));
					item.add(new SSMLabel("refNo", llpEmailLog.getRefNo()));
					item.add(new SSMLabel("emailTo", llpEmailLog.getEmailTo()));
					item.add(new SSMLabel("emailSubject", llpEmailLog.getEmailSubject()));
					item.add(new SSMLabel("remark", llpEmailLog.getRemark()));
					item.add(new SSMLabel("status", llpEmailLog.getStatus(), Parameter.EMAIL_STATUS));

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
					
					final ModalWindow viewLlpEmailPopUp = new ModalWindow("viewLlpEmailDiv");
					item.add(viewLlpEmailPopUp);
					
					viewLlpEmailPopUp.setCookieName("viewLlpEmailCookies"+llpEmailLog.getEmailId());
					viewLlpEmailPopUp.setInitialWidth(900);
					viewLlpEmailPopUp.setInitialHeight(600);
					viewLlpEmailPopUp.setResizable(true);

					PageParameters param = new PageParameters();
					param.set("emailId", llpEmailLog.getEmailId());
					viewLlpEmailPopUp.setContent(new ViewLlpEmailPanel(viewLlpEmailPopUp.getContentId(), param));

					viewLlpEmailPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
						@Override
						public boolean onCloseButtonClicked(AjaxRequestTarget target) {
							return true;
						}
					});
					
					item.add(new AjaxLink<Void>("viewEmail") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							viewLlpEmailPopUp.show(target);
						}
					});
				}
			};
			dataView.setItemsPerPage(20L);

			dataView.setOutputMarkupId(true);

			SSMPagingNavigator navigator = new SSMPagingNavigator("navigator", dataView);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);

			add(wmc);
		}
		
		public Date getSearchFromDt() {
			return searchFromDt;
		}

		public void setSearchFromDt(Date searchFromDt) {
			this.searchFromDt = searchFromDt;
		}

		public Date getSearchToDt() {
			return searchToDt;
		}

		public void setSearchToDt(Date searchToDt) {
			this.searchToDt = searchToDt;
		}

		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		

	}
	
	@Override
	public String getPageTitle() {
		return "page.title.email.log";
	}

}
