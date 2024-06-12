package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robFormB.TabRobFormBPage;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;

@SuppressWarnings({ "all" })
public class ListRobUserOkuWorkingTrayPage extends SecBasePage {
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobUserOkuService")
	private RobUserOkuService robUserOkuService;
	
	@Override
	public String getPageTitle() {
		return "page.lbl.user.profile.oku.titleWorkingTray";
	}
	
	public ListRobUserOkuWorkingTrayPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchRobUserOkuModel searchRobUserOkuModel = new SearchRobUserOkuModel();
				return searchRobUserOkuModel;
			}
		}));
		add(new SearchRobUserOkuForm("form", (IModel<SearchRobUserOkuModel>) getDefaultModel()));
	}

	private class SearchRobUserOkuForm extends Form implements Serializable {
		
		SSMDropDownChoice idType, okuRegStatus;
		SSMTextField idNo;
		
		public SearchRobUserOkuForm(String id, IModel<SearchRobUserOkuModel> m) {
			super(id, m);
			
			final SearchRobUserOkuModel searchModel =  m.getObject();
			setPrefixLabelKey("page.lbl.user.profile.oku.");
			
			okuRegStatus = new SSMDropDownChoice("okuRegStatus", Parameter.OKU_REGISTRATION_STATUS); 
			add(okuRegStatus);
			
			idType = new SSMDropDownChoice("idType", Parameter.ID_TYPE);
			add(idType);

			idNo = new SSMTextField("idNo");
			add(idNo);
			
			
			//set default search 
			SearchCriteria sc1 = null;
			searchModel.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_PENDING);
			searchModel.setIdType(Parameter.ID_TYPE_newic);
			sc1 = new SearchCriteria("okuRegStatus", SearchCriteria.EQUAL, searchModel.getOkuRegStatus()); 
			sc1 = sc1.andIfNotNull("userProfile.idType", SearchCriteria.EQUAL, searchModel.getIdType()); 
			
			//always search status pending for working tray
			//sc1 = new SearchCriteria("okuRegStatus", SearchCriteria.EQUAL, searchModel.getOkuRegStatus().valueOf(Parameter.OKU_REGISTRATION_STATUS_PENDING)); 
			
			SSMAjaxButton searchBtn = new SSMAjaxButton("searchBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchRobUserOkuModel searchModelForm = (SearchRobUserOkuModel) form.getDefaultModelObject();
					
					SearchCriteria sc2 = null;
					
					if(StringUtils.isNotBlank(searchModelForm.getOkuRegStatus())){
						sc2 = new SearchCriteria("okuRegStatus", SearchCriteria.EQUAL, searchModelForm.getOkuRegStatus()); 
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getIdType())){
						if(sc2==null){
							sc2 = new SearchCriteria("userProfile.idType", SearchCriteria.EQUAL, searchModelForm.getIdType()); 	
						}else{
							sc2 = sc2.andIfNotNull("userProfile.idType", SearchCriteria.EQUAL, searchModelForm.getIdType()); 
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getIdNo())){
						if(sc2==null){
							sc2 = new SearchCriteria("userProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo()); 
						}else{
							sc2 = sc2.andIfNotNull("userProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo());
						}
					}
					
					populateTable(sc2, target); //klik button search
				}
				
			};
			add(searchBtn);
			
			populateTable(sc1, null); //default load form
			
			
			SSMAjaxButton regNewOkuBtn = new SSMAjaxButton("regNewOkuBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if (StringUtils.isNotBlank(idNo.getValue())) {
						RobUserOku robUserOku = new RobUserOku();
						LlpUserProfile	llpUserProfile = (LlpUserProfile) llpUserProfileService.findLatestActiveUserByIdNo(idNo.getValue()); //search from db
						if(llpUserProfile!=null) {
							RobUserOku robUserOkuTmp = (RobUserOku) robUserOkuService.findOkuByUserRefNoWithData(llpUserProfile.getUserRefNo()); 
								if(robUserOkuTmp!=null) {
									robUserOku = (RobUserOku) robUserOkuTmp;
									robUserOku.setUserProfile(llpUserProfile);
									robUserOku.setIdRegUser(llpUserProfile.getIdRegUser());
									setResponsePage(new ViewOkuRegistrationPage(robUserOku)); //for approval
								}else {
									robUserOku.setUserProfile(llpUserProfile);
									robUserOku.setIdRegUser(llpUserProfile.getIdRegUser());
									setResponsePage(new EditOkuRegistrationPage(robUserOku)); //new register oku
								}
						}else {
						//	ssmError(idNo.getValue()+" is not a registered ezbiz user!");
							String errorScript = WicketUtils.generateAjaxErrorAlertScript(resolve("page.lbl.user.profile.oku.ajaxPopup.userNotFound"), idNo.getValue()+" "+resolve("page.lbl.user.profile.oku.ajaxPopup.notRegisteredUser"));
			        		target.prependJavaScript(errorScript);
						}
					}else {
					//	ssmError("id number is required.");
						String errorScript = WicketUtils.generateAjaxErrorAlertScript(resolve("page.lbl.user.profile.oku.ajaxPopup.idNoBlank"), resolve("page.lbl.user.profile.oku.ajaxPopup.idNoIsRequired"));
		        		target.prependJavaScript(errorScript);
					}
				}		
			};
			add(regNewOkuBtn);
			
		}
		
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobUserOkuService.class);
			final SSMDataView<RobUserOku> dataView = new SSMDataView<RobUserOku>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobUserOku> item) {
					
					final RobUserOku robUserOku = item.getModelObject();
					
					item.add(new SSMLabel("okuRefNo", robUserOku.getOkuRefNo()));
					item.add(new SSMLabel("idNo", robUserOku.getUserProfile().getIdNo()));
					item.add(new MultiLineLabel("name", robUserOku.getUserProfile().getName()));
					item.add(new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(), Parameter.OKU_REGISTRATION_STATUS));
					item.add(new SSMLabel("applicationDt", robUserOku.getApplicationDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("okuCardNo", robUserOku.getOkuCardNo()));
				//	item.add(new SSMLabel("createDt", robUserOku.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", robUserOku.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					
					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							RobUserOku model = item.getModelObject();
								setResponsePage(new ViewOkuRegistrationPage(robUserOku.getOkuRefNo()));
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
	

	private class SearchRobUserOkuModel implements Serializable {
	    private static final long serialVersionUID = 1L;   //-1769512858853931584L;
		
		private String idType;
		private String idNo;
		private String okuRegStatus;
		
		public String getIdType() {
			return idType;
		}
		public void setIdType(String idType) {
			this.idType = idType;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public String getOkuRegStatus() {
			return okuRegStatus;
		}
		public void setOkuRegStatus(String okuRegStatus) {
			this.okuRegStatus = okuRegStatus;
		}
	}

 }
