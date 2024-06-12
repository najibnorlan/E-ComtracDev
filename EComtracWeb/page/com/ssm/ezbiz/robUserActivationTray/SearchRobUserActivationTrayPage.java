package com.ssm.ezbiz.robUserActivationTray;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobUserActivationTrayService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

@SuppressWarnings({ "all" })
public class SearchRobUserActivationTrayPage extends SecBasePage {
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
	
	public SearchRobUserActivationTrayPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new SearchFormModel();
			}
		}));
		add(new SearchForm("form", (IModel<SearchFormModel>) getDefaultModel()));
	}

	public class SearchForm extends Form implements Serializable {

		private SimpleDateFormat fullDformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		final ProcessLockAlert processLockAlert;
		public SearchForm(String id, IModel<SearchFormModel> m) {
			super(id, m);
			SearchFormModel searchModel = m.getObject();
			setPrefixLabelKey("page.lbl.ezbiz.robUserActivationTray.");
			
			SSMTextField appRefNo = new SSMTextField("appRefNo");
			add(appRefNo);
			
			SSMTextField loginId = new SSMTextField("loginId");
			add(loginId);
			
			SSMTextField idNo = new SSMTextField("idNo");
			add(idNo);
			
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchFormModel searchModelForm = (SearchFormModel) form.getDefaultModelObject();

					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(searchModelForm.getAppRefNo())){
						sc = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, searchModelForm.getAppRefNo());
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getLoginId())){
						if(sc==null){
							sc = new SearchCriteria("llpUserProfile.loginId", SearchCriteria.EQUAL, searchModelForm.getLoginId()); 
						}else{
							sc = sc.andIfNotNull("llpUserProfile.loginId", SearchCriteria.EQUAL, searchModelForm.getLoginId());
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getIdNo())){
						if(sc==null){
							sc = new SearchCriteria("llpUserProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo()); 
						}else{
							sc = sc.andIfNotNull("llpUserProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo());
						}
					}
					
					populateTable(sc, target);
					
				}
			};
			add(search);
			
			
			processLockAlert = new ProcessLockAlert("processLockAlert", this);
			add(processLockAlert);
			
			
			
			String[] statusNeeded = {Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS,Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION};
			SearchCriteria sc = new SearchCriteria("status", SearchCriteria.IN);
			sc.setValues(statusNeeded);
			
			populateTable(sc , null);
			
		}
		public void resetTable(AjaxRequestTarget target){
			String[] statusNeeded = {Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS,Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION};
			SearchCriteria sc = new SearchCriteria("status", SearchCriteria.IN);
			sc.setValues(statusNeeded);
			
			populateTable(sc , target);
		}
		
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.ASCENDING, sc, RobUserActivationTrayService.class);
			final SSMDataView<RobUserActivationTray> dataView = new SSMDataView<RobUserActivationTray>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobUserActivationTray> item) {
					final RobUserActivationTray activationTray = item.getModelObject();
			        
					
					item.add(new SSMLabel("appRefNo", activationTray.getAppRefNo() ));
					item.add(new SSMLabel("loginId", activationTray.getLlpUserProfile().getLoginId() ));
					item.add(new SSMLabel("name", activationTray.getLlpUserProfile().getName() ));
					item.add(new SSMLabel("idNo", activationTray.getLlpUserProfile().getIdNo() ));
					item.add(new SSMLabel("status", activationTray.getStatus(), Parameter.ACTIVATION_TRAY_STATUS ));
					item.add(new SSMLabel("createDt", activationTray.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", activationTray.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					
					
					SSMAjaxLink detailLink = new SSMAjaxLink("detail"){
		    			@Override
		    			public void onClick(AjaxRequestTarget target) {
		    				if(Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS.equals(activationTray.getStatus()) || Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION.equals(activationTray.getStatus()) ) {
			    				
		    					if(Parameter.USER_STATUS_pending.equals(activationTray.getLlpUserProfile().getUserStatus())){
		    						processLockAlert.resetAlert(activationTray, target);
									String jScript = "showModal('"+processLockAlert.getWmcAlertId()+"');";
									target.appendJavaScript(jScript);
		    					}else {
		    						String msg = resolve("page.lbl.ezbiz.robUserActivationTray.discardDueToNotPending");
		    						robUserActivationTrayService.discardNonPendingApp();
		    						storeErrorMsg(msg);
		    						setResponsePage(new SearchRobUserActivationTrayPage());
		    					}
		    					
		    					
		    				}else {
		    					setResponsePage(new ViewRobUserActivationTrayPage(activationTray , getPage()));
		    				}
							
		    			}
		    		};
					item.add(detailLink);
					
					String lockByDate = "";
					if(StringUtils.isNotBlank(activationTray.getLockBy())) {
						lockByDate = activationTray.getLockBy()+"\n on "+fullDformat.format(activationTray.getLockDt());
						
						item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								String loginUser = UserEnvironmentHelper.getLoginName();
								if(loginUser.equals(activationTray.getLockBy())) {
									return "positive";
								}
								return "negative";
							}
						}));
					}else {
						
						item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return "odd";
							}
						}));
					}
					item.add(new MultiLineLabel("lockBy", lockByDate));
					
					
					String processInfo = "";
					if(StringUtils.isNotBlank(activationTray.getProcessBy())) {
						processInfo = activationTray.getProcessBy()+"\n"+  getCodeTypeWithValue(Parameter.BRANCH_CODE, activationTray.getProcessBranch());
					}
					item.add(new MultiLineLabel("processInfo", processInfo));

					
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

	private class SearchFormModel {
		private String appRefNo;
		private String loginId;
		private String idNo;
		
		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public String getAppRefNo() {
			return appRefNo;
		}
		public void setAppRefNo(String appRefNo) {
			this.appRefNo = appRefNo;
		}
		
	}
}
