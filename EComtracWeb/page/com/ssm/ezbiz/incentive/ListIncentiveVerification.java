package com.ssm.ezbiz.incentive;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAIncentivePage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.RobFormAOwnerService;
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
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "all" })
public class ListIncentiveVerification extends SecBasePage{
	
	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "RobFormAOwnerService")
	RobFormAOwnerService robFormAOwnerService;
	
		public ListIncentiveVerification() {
			setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
				protected Object load() {
					SearchListIncentiveModel incentiveModel = new SearchListIncentiveModel();
//					incentiveModel.setCreateDtFrom(new Date());
					return incentiveModel;
				}
			}));
			add(new SearchIncentiveForm("form", (IModel<SearchListIncentiveModel>) getDefaultModel()));
		}

		private class SearchIncentiveForm extends Form implements Serializable {

			public SearchIncentiveForm(String id, IModel<SearchListIncentiveModel> m) {
				super(id, m);
				SearchListIncentiveModel searchModel = m.getObject();
				SSMTextField formARefNo = new SSMTextField("formARefNo");
				formARefNo.setLabelKey("page.lbl.ezbiz.robFormASearch.formARefNo");
				add(formARefNo);
				
				SSMTextField icNo = new SSMTextField("icNo");
				icNo.setLabelKey("page.lbl.ezbiz.robFormASearch.icNo");
				add(icNo);
				
				SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
				createDtFrom.setLabelKey("page.lbl.ezbiz.robFormASearch.createDtFrom");
				add(createDtFrom);
				
				SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
				createDtTo.setLabelKey("page.lbl.ezbiz.robFormASearch.createDtTo");
				add(createDtTo);
				
				
				
				SSMAjaxButton search = new SSMAjaxButton("search") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						SearchListIncentiveModel searchModelForm = (SearchListIncentiveModel) form.getDefaultModelObject();
						
						SearchCriteria sc = null;
						if(StringUtils.isNotBlank(searchModelForm.getFormARefNo())){
							sc = new SearchCriteria("robFormACode", SearchCriteria.EQUAL, searchModelForm.getFormARefNo());
						}
						
						if(StringUtils.isNotBlank(searchModelForm.getIcNo())){
							LlpUserProfile profile = llpUserProfileService.findByIdTypeAndIdNo("01", searchModelForm.getIcNo());
							if(profile != null){
								if(sc==null){
									sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, profile.getLoginId()); 
								}else{
									sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, profile.getLoginId());
								}
							}else{
								if(sc==null){
									sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, ""); 
								}else{
									sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, "");
								}
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
							searchModelForm.getCreateDtTo().setHours(23);
							searchModelForm.getCreateDtTo().setMinutes(59);
							searchModelForm.getCreateDtTo().setSeconds(59);
							if(sc==null){
								sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, searchModelForm.getCreateDtTo()); 
							}else{
								sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, searchModelForm.getCreateDtTo());
							}
						}
						
						if(sc==null){
							sc = new SearchCriteria("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION); 
						}else{
							sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION);
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
						item.add(new SSMLabel("bizName", robFormA.getBizName()));
//						item.add(new SSMLabel("bizOwnerName", robFormAOwnerService.findByRobFormACode(robFormA.getRobFormACode()).get(0).getName()));
						item.add(new SSMLabel("status", robFormA.getStatus(), Parameter.ROB_FORM_A_STATUS ));
						item.add(new SSMLabel("createBy", robFormA.getCreateBy()));
						item.add(new SSMLabel("createDt", robFormA.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
						item.add(new SSMLabel("updateDt", robFormA.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
						
						
						item.add(new Link("detail", item.getDefaultModel()) {
							public void onClick() {
								RobFormA robFormA = item.getModelObject();
								setResponsePage(new ViewRobFormAIncentivePage(robFormA.getRobFormACode(), getPage()));
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

		private class SearchListIncentiveModel {
			private String formARefNo;
			private String icNo;
			private Date createDtFrom;
			private Date createDtTo;

			public String getFormARefNo() {
				return formARefNo;
			}

			public void setFormARefNo(String formARefNo) {
				this.formARefNo = formARefNo;
			}

			public String getIcNo() {
				return icNo;
			}

			public void setIcNo(String icNo) {
				this.icNo = icNo;
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
		}
		
		@Override
		public String getPageTitle() {
		return "Incentive Verification";
		}
	}
