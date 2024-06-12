package com.ssm.supplyinfo;

import java.io.Serializable;
import java.util.Calendar;
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
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;

@SuppressWarnings({ "all" })
public class SearchSupplyInfoTransaction extends SecBasePage {
	
	@Override
	public String getPageTitle() {
		return "page.lbl.supplyInfo.supplyInfoSearchTrans";
	}
	
	public SearchSupplyInfoTransaction() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchSupplyInforTransModel searchSupplyInfoTransHdrModel = new SearchSupplyInforTransModel();
				return searchSupplyInfoTransHdrModel;
			}
		}));
		add(new SearchSupplyInfoTransHdrForm("form", (IModel<SearchSupplyInforTransModel>) getDefaultModel()));
	}

	private class SearchSupplyInfoTransHdrForm extends Form implements Serializable {

		public SearchSupplyInfoTransHdrForm(String id, IModel<SearchSupplyInforTransModel> m) {
			super(id, m);
			SearchSupplyInforTransModel searchModel = m.getObject();
			
			setPrefixLabelKey("page.lbl.supplyInfo.supplyInfoTransTransSearch.");
			
			
			SSMTextField transCode = new SSMTextField("transCode");
			add(transCode);
			
			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			add(createDtFrom);
			
			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			add(createDtTo);
			
			SSMDateTextField paymentDtFrom = new SSMDateTextField("paymentDtFrom");
			add(paymentDtFrom);
			
			SSMDateTextField paymentDtTo = new SSMDateTextField("paymentDtTo");
			add(paymentDtTo);
			
			SSMTextField ownerBy = new SSMTextField("ownerBy");
			add(ownerBy);
			
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.SUPPLY_INFO_TRANS_STATUS);
			add(status);

			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchSupplyInforTransModel searchModelForm = (SearchSupplyInforTransModel) form.getDefaultModelObject();
					
					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(searchModelForm.getTransCode())){
						sc = new SearchCriteria("transCode", SearchCriteria.LIKE, searchModelForm.getTransCode()+"%");
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
					
					if(searchModelForm.getPaymentDtFrom()!=null){
						Calendar calStart = Calendar.getInstance();
						calStart.setTime(searchModelForm.getCreateDtFrom());
						calStart.set(Calendar.HOUR, 0);
						calStart.set(Calendar.MINUTE, 0);
						calStart.set(Calendar.SECOND, 0);
						
						if(sc==null){
							sc = new SearchCriteria("paymentDt", SearchCriteria.GREATER_EQUAL, calStart.getTime()); 
						}else{
							sc = sc.andIfNotNull("paymentDt", SearchCriteria.GREATER_EQUAL, calStart.getTime());
						}
					}
					if(searchModelForm.getPaymentDtTo()!=null){
						Calendar calPlus1 = Calendar.getInstance();
						calPlus1.setTime(searchModelForm.getCreateDtTo());
						calPlus1.set(Calendar.HOUR, 23);
						calPlus1.set(Calendar.MINUTE, 59);
						calPlus1.set(Calendar.SECOND, 59);
						
						if(sc==null){
							sc = new SearchCriteria("paymentDt", SearchCriteria.LESS_EQUAL, calPlus1.getTime()); 
						}else{
							sc = sc.andIfNotNull("paymentDt", SearchCriteria.LESS_EQUAL, calPlus1.getTime());
						}
					}
					
					
					if(StringUtils.isNotBlank(searchModelForm.getOwnerBy())){
						if(sc==null){
							sc = new SearchCriteria("ownerBy", SearchCriteria.EQUAL, searchModelForm.getOwnerBy()); 
						}else{
							sc = sc.andIfNotNull("ownerBy", SearchCriteria.EQUAL, searchModelForm.getOwnerBy());
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
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc, SupplyInfoTransHdrService.class);
			final SSMDataView<SupplyInfoTransHdr> dataView = new SSMDataView<SupplyInfoTransHdr>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<SupplyInfoTransHdr> item) {
					final SupplyInfoTransHdr supplyInfoTransHdr = item.getModelObject();
			        
					item.add(new SSMLabel("bil", item.getIndex()+1));
					
					
					item.add(new SSMLabel("transCode", supplyInfoTransHdr.getTransCode()));
					item.add(new SSMLabel("status", supplyInfoTransHdr.getStatus(), Parameter.SUPPLY_INFO_TRANS_STATUS));
					item.add(new SSMLabel("ownerBy", supplyInfoTransHdr.getOwnerBy()));
					item.add(new SSMLabel("createDt", supplyInfoTransHdr.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("paymentDt", supplyInfoTransHdr.getPaymentDt() , "dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", supplyInfoTransHdr.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));
					

					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							setResponsePage(new DetailOrderPage(supplyInfoTransHdr.getTransCode()));
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

	private class SearchSupplyInforTransModel {
		private String transCode;
		private String status;
		private Date createDtFrom;
		private Date createDtTo;
		private Date paymentDtFrom;
		private Date paymentDtTo;
		private String ownerBy;
		
		public String getTransCode() {
			return transCode;
		}
		public void setTransCode(String transCode) {
			this.transCode = transCode;
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
		public Date getPaymentDtFrom() {
			return paymentDtFrom;
		}
		public void setPaymentDtFrom(Date paymentDtFrom) {
			this.paymentDtFrom = paymentDtFrom;
		}
		public Date getPaymentDtTo() {
			return paymentDtTo;
		}
		public void setPaymentDtTo(Date paymentDtTo) {
			this.paymentDtTo = paymentDtTo;
		}
		public String getOwnerBy() {
			return ownerBy;
		}
		public void setOwnerBy(String ownerBy) {
			this.ownerBy = ownerBy;
		}

	}
}
