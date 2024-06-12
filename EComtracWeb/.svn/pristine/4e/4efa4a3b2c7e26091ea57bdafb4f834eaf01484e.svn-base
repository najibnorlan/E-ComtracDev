package com.ssm.ezbiz.myCardTransaction;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.ezbiz.comtrac.NameListAttendees;
import com.ssm.ezbiz.comtrac.ViewAttendeesInfo;
import com.ssm.ezbiz.report.SearchPaymentReceipt;
import com.ssm.ezbiz.service.SSMMyKadModelService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.ViewPaymentReceiptPanel2;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;

@SuppressWarnings({"all"})
public class SearchMyCardList extends SecBasePage{
	
	public SearchMyCardList() {
		
		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						SearchMyCardListModel searchMyCardListModel = new SearchMyCardListModel();
						return searchMyCardListModel;
					}

				}));
		add(new SearchMyCardListForm("form", (IModel<SearchMyCardListModel>) getDefaultModel()));
	}
	
	private class SearchMyCardListForm extends Form implements Serializable{
		
		private SSMLabel mykadNo;
		private SSMLabel name;
		
		public SearchMyCardListForm(String id, IModel<SearchMyCardListModel> m){
			
			super(id,m);
			
			final SearchMyCardListModel searchmodel = m.getObject();
			
			SSMTextField mykadNo = new SSMTextField("mykadNo");
			mykadNo.setLabelKey("page.ssm.ezbiz.searchMyCardList.myKadNo");
			add(mykadNo);
			
			SSMTextField name = new SSMTextField("name");
			name.setLabelKey("page.ssm.ezbiz.searchMyCardList.name");
			add(name);
			
			SSMTextField createdBy = new SSMTextField("createdBy");
			createdBy.setLabelKey("page.lbl.ezbiz.robFormASearch.createBy");
			createdBy.setUpperCase(false);
			add(createdBy);
			
			SSMDateTextField createDt = new SSMDateTextField("createDt");
			createDt.setLabelKey("page.ssm.ezbiz.searchMyCardList.createDt");
			add(createDt);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					try{
					
						SearchMyCardListModel searchModelForm = (SearchMyCardListModel) getForm().getDefaultModelObject();
					
					SearchCriteria sc = null;	
					String mykadNo = searchModelForm.getMykadNo();
					String name = searchModelForm.getName();
					String createdBy = searchModelForm.getCreatedBy();
					Date createDt = searchModelForm.getCreateDt();
					
					sc = searchCriteriaTemplate(mykadNo, name, createdBy,createDt);
					
					populateTable(sc , target);
					
					} catch (Exception e) {						
			            System.out.println(e); 
			          }
				}
			};
			
			add(search);
			populateTable(null, null);
		}
		
		
	}
	
	public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
		
		WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
		wmcSearchResult.setOutputMarkupId(true);
		wmcSearchResult.setVisible(true);
		
		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING, sc, SSMMyKadModelService.class);
		
		final SSMDataView<SSMMyKadModel> dataView = new SSMDataView<SSMMyKadModel>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<SSMMyKadModel> item) {
				final SSMMyKadModel ssmMyKadList= item.getModelObject();
				
				item.add(new SSMLabel("index", ssmMyKadList.getSsmMykadId()));
				item.add(new SSMLabel("name", ssmMyKadList.getName()));
				item.add(new SSMLabel("createBy", ssmMyKadList.getCreateBy()));
				item.add(new SSMLabel("adUserRequester", ssmMyKadList.getAdUserRequester()));
				item.add(new SSMLabel("createDt", ssmMyKadList.getCreateDt(),  "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("thumbPrintSuccess", ssmMyKadList.getThumbPrintSuccess(),Parameter.YES_NO));
				
				AjaxLink viewUserDetails = new AjaxLink("viewUserDetails", item.getDefaultModel()) {
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new MyCardDetailPage(ssmMyKadList.getSsmMykadId()));
					}
				};
				viewUserDetails.setOutputMarkupPlaceholderTag(true);
				item.add(viewUserDetails);	

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
					
		wmcSearchResult.setVisible(true);
		
		if(target==null){
			add(wmcSearchResult);
		}else{
			replace(wmcSearchResult);
			target.add(wmcSearchResult);
		}
	}
	
	public SearchCriteria searchCriteriaTemplate(String mykadNo , String name, String createdBy ,Date createDt){
		
		SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat form2 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SearchCriteria sc = null;
		
		if(StringUtils.isNotBlank(mykadNo)){
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("mykadNo", SearchCriteria.EQUAL, mykadNo);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("mykadNo", SearchCriteria.EQUAL, mykadNo);
			}
		}
		
		if(StringUtils.isNotBlank(name)){		
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("name", SearchCriteria.LIKE, name);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("name", SearchCriteria.LIKE, name);
			}
		}
		
		if(StringUtils.isNotBlank(createdBy)){		
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("createBy", SearchCriteria.LIKE, createdBy);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("createBy", SearchCriteria.LIKE, createdBy);
			}
		}
				
		if(createDt != null){
			try{
				sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(form1.format(createDt) + " 00:00:00"));
				sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, pars.parse(form1.format(createDt) + " 23:59:59"));
			
			}catch (Exception ex) {
				System.out.print("Error creating query1");
			}
		}
		
		return sc;
	}
	
	
	private class SearchMyCardListModel{
		
		private String mykadNo;
		private String name;
		private String createdBy;
		private Date createDt;
		
		public String getMykadNo() {
			return mykadNo;
		}
		
		public void setMykadNo(String mykadNo) {
			this.mykadNo = mykadNo;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public Date getCreateDt() {
			return createDt;
		}

		public void setCreateDt(Date createDt) {
			this.createDt = createDt;
		}
		
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.ezbiz.myCardTransaction";
	}

}
