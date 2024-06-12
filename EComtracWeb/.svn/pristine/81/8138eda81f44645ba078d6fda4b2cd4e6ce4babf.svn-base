package com.ssm.ezbiz.report;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.informix.util.stringUtil;
import com.ssm.ezbiz.counter.ListCheckInCounter;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.ViewPaymentReceiptPanel2;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;

@SuppressWarnings({"all"})
public class SearchPaymentReceipt extends SecBasePage{
	
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	public SearchPaymentReceipt() {

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						SearchPaymentReceiptModel searchPaymentReceiptModel = new SearchPaymentReceiptModel();
						searchPaymentReceiptModel.setDtFrom(new Date());
						return searchPaymentReceiptModel;
					}

				}));
		add(new SearchPaymentReceiptForm("form", (IModel<SearchPaymentReceiptModel>) getDefaultModel()));
	}
	
	private class SearchPaymentReceiptForm extends Form implements Serializable{
		
		private SSMDateTextField dtFrom;
		private SSMDateTextField dtTo;
		private SSMLabel transactionId;
		private SSMLabel receiptNo;
	
		public SearchPaymentReceiptForm(String id, IModel<SearchPaymentReceiptModel> m){
			
			super(id,m);
			
			SearchPaymentReceiptModel searchmodel = m.getObject(); 
			
			SSMDateTextField dtFrom = new SSMDateTextField("dtFrom");
			dtFrom.setLabelKey("page.ssm.ezbiz.searchPaymentReceipt.dtFrom");
			add(dtFrom);
			
			SSMDateTextField dtTo = new SSMDateTextField("dtTo");
			dtTo.setLabelKey("page.ssm.ezbiz.searchPaymentReceipt.dtTo");
			add(dtTo);
			
			SSMTextField receiptNo = new SSMTextField("receiptNo");
			receiptNo.setLabelKey("page.ssm.ezbiz.searchPaymentReceipt.receiptNo");
			add(receiptNo);
			
			SSMTextField transactionId = new SSMTextField("transactionId");
			transactionId.setLabelKey("page.ssm.ezbiz.searchPaymentReceipt.transactionId");
			add(transactionId);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					try{
						SearchPaymentReceiptModel searchModelForm = (SearchPaymentReceiptModel) getForm().getDefaultModelObject();
						
						SearchCriteria sc = null;
						Date dtFrom = searchModelForm.getDtFrom();
						Date dtTo = searchModelForm.getDtTo();
						String receiptNo = searchModelForm.getReceiptNo();
						String transactionId =searchModelForm.getTransactionId();
						
						sc = searchCriteriaTemplate(dtFrom, dtTo, receiptNo, transactionId);
						
						List<LlpPaymentReceipt> branchList = llpPaymentReceiptService.findByCriteria(sc).getList();
						
						populateTable(sc , target);

					} catch (Exception e) {						
			            System.out.println(e); 
			          }
					FeedbackPanel feedbackPanel =  ((SearchPaymentReceipt)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
			};
			
			add(search);
			populateTable(null, null);
			
		}
		
		public void populateTable(SearchCriteria sc, AjaxRequestTarget target){
			
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING, sc, LlpPaymentReceiptService.class);
			
			final SSMDataView<LlpPaymentReceipt> dataView = new SSMDataView<LlpPaymentReceipt>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpPaymentReceipt> item) {
					final LlpPaymentReceipt llpPaymentReceipt = item.getModelObject();
					
					DecimalFormat df = new DecimalFormat("####,###,###.00");
					
					String amount = df.format(llpPaymentReceipt.getTotalAmount());
			        
					item.add(new SSMLabel("receiptDate", llpPaymentReceipt.getCreateDt(), "dd/MM/yyyy"));
					item.add(new SSMLabel("receiptNo", llpPaymentReceipt.getReceiptNo()));
					item.add(new SSMLabel("transactionId", llpPaymentReceipt.getTransactionId()));
					item.add(new SSMLabel("totalAmount", amount));
					item.add(new SSMLabel("isCancel",llpPaymentReceipt.getIsCancel().toString(), Parameter.PAYMENT_RECEIPT_ISCANCEL));
					item.add(new SSMLabel("taxInvoiceNo", llpPaymentReceipt.getTaxInvoiceNo())); 
					
					final ModalWindow viewReceiptPopUp = new ModalWindow("viewReceiptDiv");
					item.add(viewReceiptPopUp);
					
					viewReceiptPopUp.setCookieName("viewReceiptCookies"+llpPaymentReceipt.getTransactionId());
					viewReceiptPopUp.setResizable(true);

					PageParameters param = new PageParameters();
					param.set("transId", llpPaymentReceipt.getTransactionId());
					viewReceiptPopUp.setContent(getRecieptPanel(viewReceiptPopUp.getContentId(), param));

					viewReceiptPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
						@Override
						public boolean onCloseButtonClicked(AjaxRequestTarget target) {
							return true;
						}
					});
					
					item.add(new AjaxLink<Void>("viewReceipt") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							viewReceiptPopUp.show(target);
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
						
			wmcSearchResult.setVisible(true);
			
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}
		}
	}
	
	
	public SearchCriteria searchCriteriaTemplate(Date dtFrom, Date dtTo , String receiptNo , String transactionId){
		
		SearchCriteria sc = null;
		SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat form2 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(dtFrom != null){
			try{
				if(sc == null){
					sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL , pars.parse(form1.format(dtFrom)+ " 00:00:00"));
				}else{
					sc = sc.andIfNotNull("createDt",SearchCriteria.GREATER_EQUAL , pars.parse(form1.format(dtFrom)+ " 00:00:00"));
				}

			}catch (Exception ex) {
				System.out.print("Error creating query1");
			}
		}
		
		if(dtTo != null){
			try{
				if(sc == null){
					sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, pars.parse(form1.format(dtTo)+ " 23:59:59"));
				}else{
					sc = sc.andIfNotNull("createDt",SearchCriteria.LESS_EQUAL , pars.parse(form1.format(dtTo)+ " 23:59:59"));
				}
						
			}catch (Exception ex) {
				System.out.print("Error creating query2");
			}
		}
		
		if(StringUtils.isNotBlank(receiptNo)){		
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("receiptNo", SearchCriteria.EQUAL, receiptNo);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("receiptNo", SearchCriteria.EQUAL, receiptNo);
			}
		}
		
		if(StringUtils.isNotBlank(transactionId)){		
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);
			}
}
		return sc;
	}
	
	private class SearchPaymentReceiptModel {
		
		private Date dtFrom;
		private Date dtTo;
		private String transactionId;
		private String receiptNo; 
		
		public Date getDtFrom() {
			return dtFrom;
		}
		
		public void setDtFrom(Date dtFrom) {
			this.dtFrom = dtFrom;
		}
		
		public Date getDtTo() {
			return dtTo;
		}
		
		public void setDtTo(Date dtTo) {
			this.dtTo = dtTo;
		}
		
		public String getTransactionId() {
			return transactionId;
		}
		
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
		
		public String getReceiptNo() {
			return receiptNo;
		}
		
		public void setReceiptNo(String receiptNo) {
			this.receiptNo = receiptNo;
		}
	}
	
	@Override
	public String getPageTitle() {
		return "menu.myBiz.searchPaymentReceipt";
	}

}
