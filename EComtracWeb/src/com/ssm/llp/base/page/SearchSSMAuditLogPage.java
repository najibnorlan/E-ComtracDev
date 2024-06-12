package com.ssm.llp.base.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.SSMAuditLogModel;
import com.ssm.llp.base.common.service.SSMAuditLogService;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.ws.RobBusinessSummaryInfo;

@SuppressWarnings({ "rawtypes", "unchecked" ,"serial" })
public class SearchSSMAuditLogPage  extends SecBasePage {

	@SpringBean(name = "SSMAuditLogService")
	private SSMAuditLogService ssmAuditLogService;
	
	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.SSMAuditLogPage";
	}
	
	public SearchSSMAuditLogPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchSSMAuditLogModel searchSSMAuditLogModel = new SearchSSMAuditLogModel();
				return searchSSMAuditLogModel;
			}
		}));
		add(new SearchSSMAuditLogForm("form", (IModel<SearchSSMAuditLogModel>) getDefaultModel()));
	}

	private class SearchSSMAuditLogForm extends Form implements Serializable {
		public SearchSSMAuditLogForm(String id, IModel<SearchSSMAuditLogModel> m) {
			super(id, m);
			setPrefixLabelKey(getPageTitle()+".");
			
			
			SSMDropDownChoice clazzName = new SSMDropDownChoice("clazzName", Parameter.AUD_CLASSNAME);
			add(clazzName);
			
			
			SSMTextField primaryKey = new SSMTextField("primaryKey");
			primaryKey.setUpperCase(false);
			add(primaryKey);
			
			SSMTextField filterField = new SSMTextField("filterField");
			filterField.setUpperCase(false);
			add(filterField);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					SearchSSMAuditLogModel searchModelForm = (SearchSSMAuditLogModel) getForm().getDefaultModelObject();
					populateTable(searchModelForm.getClazzName(), searchModelForm.getPrimaryKey(), searchModelForm.getFilterField(), target);
				}
			};
			add(search);
			
			
			populateTable(null, null , null, null);
		}
		
		
		public void populateTable(String clazzName,String primaryKey, final String filterField, AjaxRequestTarget target) {
			
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			SSMAuditLogModel modelLog = ssmAuditLogService.findAuditLog(clazzName ,primaryKey, filterField);
			
			SSMSessionSortableDataProvider dpHeader = new SSMSessionSortableDataProvider("", modelLog.getListColumnName());
			final SSMDataView<String> dataViewHeader = new SSMDataView<String>("sortingHeader", dpHeader) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<String> item) {
					final String collName = item.getModelObject();
					item.add(new SSMLabel("column", collName));
				}
			};
			dataViewHeader.setItemsPerPage(200L);
			wmcSearchResult.add(dataViewHeader);
			
			
			
			
			SSMSessionSortableDataProvider dpRow = new SSMSessionSortableDataProvider("", modelLog.getListObjectValue());
			final SSMDataView<List<Object>> dataViewRow = new SSMDataView<List<Object>>("sortingRows", dpRow) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<List<Object>> item) {
					final List listObjValue= item.getModelObject();
					ListView<Object> objValues = new ListView<Object>("objValues", listObjValue) {
						@Override
						protected void populateItem(ListItem<Object> arg0) {
							arg0.add(new SSMLabel("column", arg0.getModelObject() ));
						}
							
					};
					item.add(objValues);
				}
			};
			
			dataViewRow.setItemsPerPage(500L);
			wmcSearchResult.add(dataViewRow);
			
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}

		}
		
		
	}
	
	private class SearchSSMAuditLogModel {
		private String clazzName;
		private String primaryKey;
		private String filterField;
		
		public String getClazzName() {
			return clazzName;
		}
		public void setClazzName(String clazzName) {
			this.clazzName = clazzName;
		}
		public String getPrimaryKey() {
			return primaryKey;
		}
		public void setPrimaryKey(String primaryKey) {
			this.primaryKey = primaryKey;
		}
		public String getFilterField() {
			return filterField;
		}
		public void setFilterField(String filterField) {
			this.filterField = filterField;
		}
	}
}
