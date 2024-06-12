package com.ssm.ezbiz.robformA;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Param;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

@SuppressWarnings({ "all" })
public class SearchRobFormA1Renewal extends SecBasePage {
	public SearchRobFormA1Renewal() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchRobFormA1RenewalModel searchRobFormA1RenewalModel = new SearchRobFormA1RenewalModel();
				return searchRobFormA1RenewalModel;
			}
		}));
		add(new SearchRobFormA1RenewalForm("form", (IModel<SearchRobFormA1RenewalModel>) getDefaultModel()));
	}

	private class SearchRobFormA1RenewalForm extends Form implements Serializable {

		public SearchRobFormA1RenewalForm(String id, IModel<SearchRobFormA1RenewalModel> m) {
			super(id, m);
			SearchRobFormA1RenewalModel searchModel = m.getObject();
//			
			SSMTextField formA1RefNo = new SSMTextField("formA1RefNo");
			formA1RefNo.setLabelKey("page.lbl.ezbiz.robFormA1RenewalSearch.formARefNo");
			add(formA1RefNo);
			
			SSMTextField brNo = new SSMTextField("brNo");
			brNo.setLabelKey("page.lbl.ezbiz.robFormA1RenewalSearch.brNo");
			add(brNo);
			
			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			createDtFrom.setLabelKey("page.lbl.ezbiz.robFormA1RenewalSearch.createDtFrom");
			add(createDtFrom);
			
			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			createDtTo.setLabelKey("page.lbl.ezbiz.robFormA1RenewalSearch.createDtTo");
			add(createDtTo);
			
			SSMTextField createBy = new SSMTextField("createBy");
			createBy.setLabelKey("page.lbl.ezbiz.robFormASearch.createBy");
			add(createBy);
			
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.ROB_RENEWAL_STATUS);
			status.setLabelKey("page.lbl.ezbiz.robFormA1RenewalSearch.status");
			add(status);
			
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchRobFormA1RenewalModel searchModelForm = (SearchRobFormA1RenewalModel) form.getDefaultModelObject();
					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(searchModelForm.getFormA1RefNo())){
						sc = new SearchCriteria("transCode", SearchCriteria.EQUAL, searchModelForm.getFormA1RefNo());
					}
					if(StringUtils.isNotBlank(searchModelForm.getBrNo())){
						if(sc==null){
							sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, searchModelForm.getBrNo()); 
						}else{
							sc = sc.andIfNotNull("brNo", SearchCriteria.EQUAL, searchModelForm.getBrNo());
						}
					}
					if(StringUtils.isNotBlank(searchModelForm.getStatus())){
						if(sc==null){
							sc = new SearchCriteria("status", SearchCriteria.EQUAL, searchModelForm.getStatus()); 
						}else{
							sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, searchModelForm.getStatus());
						}
					}
					if(searchModelForm.getCreateDtFrom()!=null){
						if(sc==null){
							sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, searchModelForm.getCreateDtFrom()); 
						}else{
							sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, searchModelForm.getCreateDtFrom());
						}
					}
					if(searchModelForm.getCreateDtTo()!=null){
						if(sc==null){
							sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, searchModelForm.getCreateDtTo()); 
						}else{
							sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, searchModelForm.getCreateDtTo());
						}
					}
					if(searchModelForm.getCreateBy()!=null){
						if(sc==null){
							sc = new SearchCriteria("createBy", SearchCriteria.LIKE, searchModelForm.getCreateBy()); 
						}else{
							sc = sc.andIfNotNull("createBy", SearchCriteria.LIKE, searchModelForm.getCreateBy());
						}
					}
					
					populateTable(sc, target);
				}
			};
			add(search);
			populateTable(null , null);
			
		}
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobRenewalService.class);
			final SSMDataView<RobRenewal> dataView = new SSMDataView<RobRenewal>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobRenewal> item) {
					final RobRenewal robRenewal = item.getModelObject();
			        
					item.add(new SSMLabel("robFormA1Code", robRenewal.getTransCode()));
					item.add(new SSMLabel("brNo", robRenewal.getBrNo()+"-"+robRenewal.getChkDigit()));
					item.add(new SSMLabel("bizName", robRenewal.getBizName()));
					item.add(new SSMLabel("status", robRenewal.getStatus(), Parameter.ROB_RENEWAL_STATUS ));
					item.add(new SSMLabel("createBy", robRenewal.getCreateBy()));
					item.add(new SSMLabel("createDt", robRenewal.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", robRenewal.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					

					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							RobRenewal robRenewal = item.getModelObject();
							setResponsePage(new EditRobRenewalPage(robRenewal));
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

			wmcSearchResult.add(dataView);
			wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
			wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}
			
		}

	}

	private class SearchRobFormA1RenewalModel {
		private String formA1RefNo;
		private String status;
		private String brNo;
		private Date createDtFrom;
		private Date createDtTo;
		private String createBy;

		public String getCreateBy() {
			return createBy;
		}

		public void setCreateBy(String createBy) {
			this.createBy = createBy;
		}

		public String getFormA1RefNo() {
			return formA1RefNo;
		}

		public void setFormA1RefNo(String formA1RefNo) {
			this.formA1RefNo = formA1RefNo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Date getCreateDtFrom() {
			return createDtFrom;
		}

		public void setCreateDtFrom(Date createDtFrom) {
			this.createDtFrom = createDtFrom;
		}

		public Date getCreateDtTo() {
			return createDtTo;
		}

		public void setCreateDtTo(Date createDtTo) {
			this.createDtTo = createDtTo;
		}

		public String getBrNo() {
			return brNo;
		}

		public void setBrNo(String brNo) {
			this.brNo = brNo;
		}
	}
}
