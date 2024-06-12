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
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.model.LlpUserProfile;

@SuppressWarnings({ "all" })
public class ListRobUserActivationTrayPage extends SecBasePage {
	
	

	@SpringBean(name = "RobUserActivationTrayService")
	private RobUserActivationTrayService robUserActivationTrayService;
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
	
	public ListRobUserActivationTrayPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new SearchFormModel();
			}
		}));
		add(new SearchForm("form", (IModel<SearchFormModel>) getDefaultModel()));
	}

	private class SearchForm extends Form implements Serializable {

		private SimpleDateFormat fullDformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//		final ProcessLockAlert processLockAlert;
		public SearchForm(String id, IModel<SearchFormModel> m) {
			super(id, m);
			SearchFormModel searchModel = m.getObject();
			setPrefixLabelKey("page.lbl.ezbiz.robUserActivationTray.");
		
			LlpUserProfile llpUserProfile = ((LlpUserEnviroment ) UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
			
			
			SSMLabel noNotice = new SSMLabel("noNotice", "");
			noNotice.setVisible(false);
			add(noNotice);
			
			SSMLabel noticeHighVolume = new SSMLabel("noticeHighVolume", "");
			noticeHighVolume.setVisible(false);
			add(noticeHighVolume);
			
			String isNoticeUp = getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_IS_USER_ACTIVATION_NOTICE_UP);
			if(Parameter.YES_NO_yes.equals(isNoticeUp)) {
				noticeHighVolume = new SSMLabel("noticeHighVolume", resolve(getPrefixLabelKey()+"noticeHighVolume"));
				replace(noticeHighVolume);
				noticeHighVolume.setVisible(true);
			}else {
				noNotice.setVisible(true);
			}
			
			
			SSMAjaxButton addNew = new SSMAjaxButton("addNew") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchFormModel searchModelForm = (SearchFormModel) form.getDefaultModelObject();
        			setResponsePage(new SubmissionRobUserActivationTrayPage());
				}
			};
			add(addNew);
			
			boolean hasPendingApplication = robUserActivationTrayService.hasPendingApplication(llpUserProfile.getUserRefNo());
			addNew.setVisible(!hasPendingApplication);
			
//			processLockAlert = new ProcessLockAlert("processLockAlert");
//			add(processLockAlert);
			
			
			SearchCriteria sc = new SearchCriteria("userRefNo", SearchCriteria.EQUAL , llpUserProfile.getUserRefNo());
			populateTable(sc , null);
			
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
//					item.add(new SSMLabel("createDt", activationTray.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", activationTray.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					
					
					SSMAjaxLink detailLink = new SSMAjaxLink("detail"){
		    			@Override
		    			public void onClick(AjaxRequestTarget target) {
		    				if(Parameter.ACTIVATION_TRAY_STATUS_QUERY.equals(activationTray.getStatus()) ) {
		    					setResponsePage(new SubmissionRobUserActivationTrayPage(activationTray) );
		    				}else {
		    					setResponsePage(new ViewRobUserActivationTrayPage(activationTray , getPage()));
		    				}
							
		    			}
		    		};
					item.add(detailLink);
					
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
