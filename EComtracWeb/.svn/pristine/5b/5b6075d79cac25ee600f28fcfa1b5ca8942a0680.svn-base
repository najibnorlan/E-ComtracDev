package com.ssm.ezbiz.robformA;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;

@SuppressWarnings({ "all" })
public class SearchRobFormA extends SecBasePage {
	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.robFormASearch";
	}
	
	public SearchRobFormA() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchRobFormAModel searchRobFormAModel = new SearchRobFormAModel();
				return searchRobFormAModel;
			}
		}));
		add(new SearchRobFormAForm("form", (IModel<SearchRobFormAModel>) getDefaultModel()));
	}

	private class SearchRobFormAForm extends Form implements Serializable {

		public SearchRobFormAForm(String id, IModel<SearchRobFormAModel> m) {
			super(id, m);
			SearchRobFormAModel searchModel = m.getObject();
//			
			setPrefixLabelKey("page.lbl.ezbiz.robFormASearch.");
			
			SSMTextField formARefNo = new SSMTextField("formARefNo");
			add(formARefNo);
			
			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			add(createDtFrom);
			
			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			add(createDtTo);
			
			SSMTextField createdBy = new SSMTextField("createBy");
			add(createdBy);
			
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.ROB_FORM_A_STATUS);
			add(status);

			SSMTextField processBy = new SSMTextField("processBy");
			processBy.setUpperCase(false);
			add(processBy);
			
			SSMTextField brNo = new SSMTextField("brNo");
			add(brNo);
			
			SSMDropDownChoice prosessingBranch = new SSMDropDownChoice("prosessingBranch", Parameter.BRANCH_CODE);
			add(prosessingBranch);
			
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchRobFormAModel searchModelForm = (SearchRobFormAModel) form.getDefaultModelObject();
					System.out.println("yezza"+searchModelForm.getStatus());
//					
//					WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
//					wmcSearchResult.setOutputMarkupId(true);
//					wmcSearchResult.setVisible(true);
//					
					
//					getPage().add(wmcSearchResult);
					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(searchModelForm.getFormARefNo())){
						sc = new SearchCriteria("robFormACode", SearchCriteria.EQUAL, searchModelForm.getFormARefNo());
					}
					if(StringUtils.isNotBlank(searchModelForm.getStatus())){
						if(sc==null){
							sc = new SearchCriteria("status", SearchCriteria.EQUAL, searchModelForm.getStatus()); 
						}else{
							sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, searchModelForm.getStatus());
						}
					}
					if(searchModelForm.getCreateDtFrom()!=null){
						Calendar calStart = Calendar.getInstance();
						calStart.setTime(searchModelForm.getCreateDtFrom());
						calStart.set(Calendar.HOUR, 0);
						calStart.set(Calendar.MINUTE, 0);
						calStart.set(Calendar.SECOND, 0);
						
						if(sc==null){
							sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, calStart.getTime()); 
						}else{
							sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, calStart.getTime());
						}
					}
					if(searchModelForm.getCreateDtTo()!=null){
						Calendar calPlus1 = Calendar.getInstance();
						calPlus1.setTime(searchModelForm.getCreateDtTo());
						calPlus1.set(Calendar.HOUR, 23);
						calPlus1.set(Calendar.MINUTE, 59);
						calPlus1.set(Calendar.SECOND, 59);
						
						if(sc==null){
							sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, calPlus1.getTime()); 
						}else{
							sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, calPlus1.getTime());
						}
					}
					if(searchModelForm.getCreateBy()!=null){
						if(sc==null){
							sc = new SearchCriteria("createBy", SearchCriteria.LIKE, searchModelForm.getCreateBy()); 
						}else{
							sc = sc.andIfNotNull("createBy", SearchCriteria.LIKE, searchModelForm.getCreateBy());
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getProcessBy())){
						if(sc==null){
							sc = new SearchCriteria("approveRejectBy", SearchCriteria.EQUAL, searchModelForm.getProcessBy()); 
						}else{
							sc = sc.andIfNotNull("approveRejectBy", SearchCriteria.EQUAL, searchModelForm.getProcessBy());
						}
					}
					if(StringUtils.isNotBlank(searchModelForm.getProsessingBranch())){
						if(sc==null){
							sc = new SearchCriteria("prosessingBranch", SearchCriteria.EQUAL, searchModelForm.getProsessingBranch()); 
						}else{
							sc = sc.andIfNotNull("prosessingBranch", SearchCriteria.EQUAL, searchModelForm.getProsessingBranch());
						}
					}
					if(StringUtils.isNotBlank(searchModelForm.getBrNo())){
						if(sc==null){
							sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, searchModelForm.getBrNo()); 
						}else{
							sc = sc.andIfNotNull("brNo", SearchCriteria.EQUAL, searchModelForm.getBrNo());
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
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormAService.class);
			final SSMDataView<RobFormA> dataView = new SSMDataView<RobFormA>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobFormA> item) {
					final RobFormA robFormA = item.getModelObject();
			        
					item.add(new SSMLabel("robFormACode", robFormA.getRobFormACode()));
					
					String bizName = robFormA.getBizName();
					if(StringUtils.isNotBlank(robFormA.getBrNo()) ) {
						bizName = robFormA.getBrNo()+"\n"+bizName;
					}
					
					item.add(new MultiLineLabel("bizName", bizName));
					item.add(new SSMLabel("status", robFormA.getStatus(), Parameter.ROB_FORM_A_STATUS ));
					item.add(new SSMLabel("createBy", robFormA.getCreateBy()));
					item.add(new SSMLabel("createDt", robFormA.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", robFormA.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					String prosessingBranchCode = robFormA.getApproveRejectBranch();
					if(StringUtils.isBlank(prosessingBranchCode)){
						prosessingBranchCode = robFormA.getProsessingBranch();
					}
					item.add(new SSMLabel("approveRejectBranch", getCodeTypeWithValue(Parameter.BRANCH_CODE, prosessingBranchCode )));
					item.add(new SSMLabel("approveRejectBy", robFormA.getApproveRejectBy()));
					item.add(new SSMLabel("approveRejectDt", robFormA.getApproveRejectDt() , "dd/MM/yyyy hh:mm:ss a"));

					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							RobFormA robFormA = item.getModelObject();
								setResponsePage(new ViewRobFormAPage2(robFormA.getRobFormACode(), getPage()));

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

	private class SearchRobFormAModel {
		private String formARefNo;
		private String status;
		private Date createDtFrom;
		private Date createDtTo;
		private String createBy;
		private String processBy;
		private String prosessingBranch;
		private String brNo;

		public String getFormARefNo() {
			return formARefNo;
		}

		public void setFormARefNo(String formARefNo) {
			this.formARefNo = formARefNo;
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

		public String getCreateBy() {
			return createBy;
		}

		public void setCreateBy(String createBy) {
			this.createBy = createBy;
		}

		public String getProcessBy() {
			return processBy;
		}

		public void setProcessBy(String processBy) {
			this.processBy = processBy;
		}


		public String getBrNo() {
			return brNo;
		}

		public void setBrNo(String brNo) {
			this.brNo = brNo;
		}

		public String getProsessingBranch() {
			return prosessingBranch;
		}

		public void setProsessingBranch(String prosessingBranch) {
			this.prosessingBranch = prosessingBranch;
		}
	}
}
