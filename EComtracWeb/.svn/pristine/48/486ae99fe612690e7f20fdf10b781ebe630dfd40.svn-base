package com.ssm.llp.mod1.page;

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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.CmpDetail;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;

@SuppressWarnings({ "all" })
public class ListRobUserOkuSearchPage extends SecBasePage {
	@Override
	public String getPageTitle() {
		return "page.lbl.user.profile.oku.titleSearch";
	}
	
	public ListRobUserOkuSearchPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchRobUserOkuModel searchRobUserOkuModel = new SearchRobUserOkuModel();
				return searchRobUserOkuModel;
			}
		}));
		add(new SearchRobUserOkuForm("form", (IModel<SearchRobUserOkuModel>) getDefaultModel()));
	}

	private class SearchRobUserOkuForm extends Form implements Serializable {

		public SearchRobUserOkuForm(String id, IModel<SearchRobUserOkuModel> m) {
			super(id, m);
			
			SearchRobUserOkuModel searchModel =  m.getObject();
	
			setPrefixLabelKey("page.lbl.user.profile.oku.");
			
			SSMDropDownChoice okuRegStatus = new SSMDropDownChoice("okuRegStatus", Parameter.OKU_REGISTRATION_STATUS); 
			add(okuRegStatus);

			SSMDropDownChoice okuCategory = new SSMDropDownChoice("okuCategory", Parameter.OKU_CATEGORY);
			add(okuCategory);
			
			SSMDropDownChoice idType = new SSMDropDownChoice("idType", Parameter.ID_TYPE);
			add(idType);

			SSMTextField idNo = new SSMTextField("idNo");
			add(idNo);
			
			SSMTextField okuRefNo = new SSMTextField("okuRefNo");
			add(okuRefNo);
			
			SSMTextField okuCardNo = new SSMTextField("okuCardNo");
			add(okuCardNo);
			
			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			add(createDtFrom);
			
			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			add(createDtTo);
			
			
			SSMAjaxButton searchBtn = new SSMAjaxButton("searchBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchRobUserOkuModel searchModelForm = (SearchRobUserOkuModel) form.getDefaultModelObject();
					
					SearchCriteria sc = null;
					
					//table user oku-------------------
					if(StringUtils.isNotBlank(searchModelForm.getOkuRefNo())){
						sc = new SearchCriteria("okuRefNo", SearchCriteria.EQUAL, searchModelForm.getOkuRefNo()); // "okuRefNo" case sensitive, must same data object
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getOkuRegStatus())){
						if(sc==null){
							sc = new SearchCriteria("okuRegStatus", SearchCriteria.EQUAL, searchModelForm.getOkuRegStatus()); 
						}else{
							sc = sc.andIfNotNull("okuRegStatus", SearchCriteria.EQUAL, searchModelForm.getOkuRegStatus());
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getOkuCategory())){
						if(sc==null){
							sc = new SearchCriteria("okuCategory", SearchCriteria.EQUAL, searchModelForm.getOkuCategory()); 
						}else{
							sc = sc.andIfNotNull("okuCategory", SearchCriteria.EQUAL, searchModelForm.getOkuCategory());
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getOkuCardNo())){
						if(sc==null){
							sc = new SearchCriteria("okuCardNo", SearchCriteria.EQUAL, searchModelForm.getOkuCardNo()); 
						}else{
							sc = sc.andIfNotNull("okuCardNo", SearchCriteria.EQUAL, searchModelForm.getOkuCardNo());
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
					
					
					//table user profile---------------------------
					if(StringUtils.isNotBlank(searchModelForm.getIdType())){
						if(sc==null){
							sc = new SearchCriteria("userProfile.idType", SearchCriteria.EQUAL, searchModelForm.getIdType()); //BaseDaoImpl alias1 (userProfile.idType kene sama dataobject)
						}else{
							sc = sc.andIfNotNull("userProfile.idType", SearchCriteria.EQUAL, searchModelForm.getIdType());
						}
					}
					
					if(StringUtils.isNotBlank(searchModelForm.getIdNo())){
						if(sc==null){
							sc = new SearchCriteria("userProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo()); 
						}else{
							sc = sc.andIfNotNull("userProfile.idNo", SearchCriteria.EQUAL, searchModelForm.getIdNo());
						}
					}
					
					populateTable(sc, target);
				}
			};
			add(searchBtn);
			populateTable(null , null);
			
		}
		
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, RobUserOkuService.class);
			final SSMDataView<RobUserOku> dataView = new SSMDataView<RobUserOku>("sorting", dp) {
				
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobUserOku> item) {
					RobUserOku robUserOku = item.getModelObject();
					
					
//					getSession().setAttribute("robUserOku_", (Serializable) robUserOku); // set into session
//					robUserOku= (RobUserOku) getSession().getAttribute("robUserOku_");
					
					item.add(new SSMLabel("okuRefNo", robUserOku.getOkuRefNo()));
					item.add(new SSMLabel("idNo", robUserOku.getUserProfile().getIdNo()));
					item.add(new MultiLineLabel("name", robUserOku.getUserProfile().getName()));
					item.add(new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(), Parameter.OKU_REGISTRATION_STATUS));
					item.add(new SSMLabel("applicationDt", robUserOku.getApplicationDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("createDt", robUserOku.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", robUserOku.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					
					if(StringUtils.isNotBlank(robUserOku.getApproveBy())) {
						item.add(new SSMLabel("approveBy", robUserOku.getApproveBy()));
						item.add(new SSMLabel("approvalDt", robUserOku.getApprovalDt() , "dd/MM/yyyy hh:mm:ss a"));
					}else {
						item.add(new SSMLabel("approveBy", ""));
						item.add(new SSMLabel("approvalDt", new Date().toString().valueOf("")));
					}
						
					
					item.add(new Link("detailLink", item.getDefaultModel()) {
						public void onClick() {
							RobUserOku model = item.getModelObject();
								setResponsePage(new ViewOkuRegistrationPage(model.getOkuRefNo()));
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

	private class SearchRobUserOkuModel{
		
		private String okuRefNo;
		private String okuRegStatus;
		private String idType;
		private String idNo;
		private String okuCategory;
		private String okuCardNo;
		private Date createDtFrom;
		private Date createDtTo;
		
		
		public String getOkuRefNo() {
			return okuRefNo;
		}
		public void setOkuRefNo(String okuRefNo) {
			this.okuRefNo = okuRefNo;
		}
		public String getOkuRegStatus() {
			return okuRegStatus;
		}
		public void setOkuRegStatus(String okuRegStatus) {
			this.okuRegStatus = okuRegStatus;
		}
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
		public String getOkuCategory() {
			return okuCategory;
		}
		public void setOkuCategory(String okuCategory) {
			this.okuCategory = okuCategory;
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
		public String getOkuCardNo() {
			return okuCardNo;
		}
		public void setOkuCardNo(String okuCardNo) {
			this.okuCardNo = okuCardNo;
		}
		
	}
}
