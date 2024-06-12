package com.ssm.ezbiz.robFormB;

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

import com.ssm.ezbiz.service.RobFormBService;
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
import com.ssm.llp.ezbiz.model.RobFormB;

@SuppressWarnings({ "all" })
public class SearchRobFormB extends SecBasePage {
	public SearchRobFormB() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchRobFormBModel searchRobFormBModel = new SearchRobFormBModel();
				return searchRobFormBModel;
			}
		}));
		add(new SearchRobFormBForm("form", (IModel<SearchRobFormBModel>) getDefaultModel()));
	}
	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.robFormBSearch";
	}
	private class SearchRobFormBForm extends Form implements Serializable {

		public SearchRobFormBForm(String id, IModel<SearchRobFormBModel> m) {
			super(id, m);
			SearchRobFormBModel searchModel = m.getObject();
			setPrefixLabelKey("page.lbl.ezbiz.robFormBSearch.");
			
			SSMTextField formBRefNo = new SSMTextField("formBRefNo");
			add(formBRefNo);
			
			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			add(createDtFrom);
			
			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			add(createDtTo);
			
			SSMTextField createBy = new SSMTextField("createBy");
			add(createBy);
			
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.ROB_FORM_B_STATUS);
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
					SearchRobFormBModel searchModelForm = (SearchRobFormBModel) form.getDefaultModelObject();

					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(searchModelForm.getFormBRefNo())){
						sc = new SearchCriteria("robFormBCode", SearchCriteria.EQUAL, searchModelForm.getFormBRefNo());
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
						String brNo = StringUtils.substringBefore(searchModelForm.getBrNo(), "-");
						if(sc==null){
							sc = new SearchCriteria("brNo", SearchCriteria.EQUAL, brNo); 
						}else{
							sc = sc.andIfNotNull("brNo", SearchCriteria.EQUAL, brNo);
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
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobFormBService.class);
			final SSMDataView<RobFormB> dataView = new SSMDataView<RobFormB>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobFormB> item) {
					final RobFormB robFormB = item.getModelObject();
			        
					item.add(new SSMLabel("robFormBCode", robFormB.getRobFormBCode()));
					
					
					String bizName = robFormB.getBizName();
					if(StringUtils.isNotBlank(robFormB.getBrNo()) ) {
						bizName = robFormB.getBrNo()+"-"+robFormB.getCheckDigit()+"\n"+bizName;
					}
					
					item.add(new MultiLineLabel("bizName", bizName));
					item.add(new SSMLabel("status", robFormB.getStatus(), Parameter.ROB_FORM_B_STATUS ));
					item.add(new SSMLabel("createBy", robFormB.getCreateBy()));
					item.add(new SSMLabel("createDt", robFormB.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", robFormB.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					
					String prosessingBranchCode = robFormB.getApproveRejectBranch();
					if(StringUtils.isBlank(prosessingBranchCode)){
						prosessingBranchCode = robFormB.getProsessingBranch();
					}
					item.add(new SSMLabel("approveRejectBranch", getCodeTypeWithValue(Parameter.BRANCH_CODE, prosessingBranchCode )));
					item.add(new SSMLabel("approveRejectBy", robFormB.getApproveRejectBy()));
					item.add(new SSMLabel("approveRejectDt", robFormB.getApproveRejectDt() , "dd/MM/yyyy hh:mm:ss a"));

					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							RobFormB robFormB = item.getModelObject();
							if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())|| Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
								//setResponsePage(new EditRobFormBPage(robFormB.getRobFormBCode()));
							}else{
								//setResponsePage(new ViewRobFormBPage(robFormB.getRobFormBCode(), getPage()));
							}
							setResponsePage(new ViewRobFormBPage(robFormB.getRobFormBCode(), getPage()));
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

	private class SearchRobFormBModel {
		private String formBRefNo;
		private String status;
		private Date createDtFrom;
		private Date createDtTo;
		private String createBy;
		private String processBy;
		private String prosessingBranch;
		private String brNo;

		public String getCreateBy() {
			return createBy;
		}

		public void setCreateBy(String createBy) {
			this.createBy = createBy;
		}

		public String getFormBRefNo() {
			return formBRefNo;
		}

		public void setFormBRefNo(String formBRefNo) {
			this.formBRefNo = formBRefNo;
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
