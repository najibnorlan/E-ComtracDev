package com.ssm.llp.page.supplyinfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;


@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class ListLlpSupplyInfoHdrPage extends SecBasePage {

	@SpringBean(name="LlpSupplyInfoHdrService")
	private LlpSupplyInfoHdrService llpSupplyInfoHdrService;
	
	@SpringBean(name="LlpSupplyInfoDtlService")
	private LlpSupplyInfoDtlService llpSupplyInfoDtlService;
	
	public ListLlpSupplyInfoHdrPage() {
		add(new LlpSupplyInfoHdrForm("llpSupplyInfoHdrForm"));
		
	}
	
	private class LlpSupplyInfoHdrForm extends Form {

		private String hdrCode;
		private String hdrStatus;
		private WebMarkupContainer wmc;
		private SSMDataView dataView;
		private SSMSortableDataProvider dp;
		
		public LlpSupplyInfoHdrForm(String name) {
			super(name);
			populateTable();
			SSMTextField hdrCodeTf = new SSMTextField("hdrCode", new PropertyModel(this, "hdrCode"));
			hdrCodeTf.setLabelKey("llpSupplyInfoHdr.page.hdrCode");
			add(hdrCodeTf);
			
			
			SSMDropDownChoice hdrStatus = new SSMDropDownChoice("hdrStatus",  new PropertyModel(this, "hdrStatus"), Parameter.SUPPLY_INFO_HDR_STATUS);
			hdrStatus.setLabelKey("llpSupplyInfoHdr.page.hdrStatus");
			setHdrStatus(Parameter.SUPPLY_INFO_HDR_STATUS_complete);
			if(UserEnvironmentHelper.isInternalUser()){
				setHdrStatus(Parameter.SUPPLY_INFO_HDR_STATUS_in_process);
			}
			add(hdrStatus);
			
			
			AjaxButton searchButton = new AjaxButton("searchButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchCriteria sc = null;
					
					sc = SearchCriteria.andIfNotNull(sc, "hdrStatus",SearchCriteria.EQUAL,getHdrStatus());
					if(StringUtils.isNotBlank(getHdrCode())){
						sc = SearchCriteria.andIfNotNull(sc, "hdrCode", SearchCriteria.LIKE, getHdrCode()+"%");
					}
					if(!UserEnvironmentHelper.isInternalUser()){//if public filter by create by
						sc = SearchCriteria.andIfNotNull(sc, "createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
					}else{
						if(sc==null){
							sc = SearchCriteria.andIfNotNull(sc, "hdrStatus",SearchCriteria.LIKE,"%");
						}
					}
					
					dp.setSc(sc);
					target.add(wmc);
					FeedbackPanel feedbackPanel =  ((ListLlpSupplyInfoHdrPage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
				}

			};
			add(searchButton);
		}

		public String getHdrCode() {
			return hdrCode;
		}

		public void setHdrCode(String hdrCode) {
			this.hdrCode = hdrCode;
		}
		
		private void populateTable() {
			
			SearchCriteria sc = null;
			if(!UserEnvironmentHelper.isInternalUser()){
				sc = new SearchCriteria("hdrStatus",SearchCriteria.EQUAL,Parameter.SUPPLY_INFO_HDR_STATUS_complete);
			}else{
				sc = new SearchCriteria("hdrStatus",SearchCriteria.EQUAL,Parameter.SUPPLY_INFO_HDR_STATUS_in_process);
			}
			
			if(!UserEnvironmentHelper.isInternalUser()){//if public filter by create by
				sc = SearchCriteria.andIfNotNull(sc, "createBy", SearchCriteria.EQUAL, UserEnvironmentHelper.getLoginName());
			}

			dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING, sc, LlpSupplyInfoHdrService.class);
			dataView = new SSMDataView<LlpSupplyInfoHdr>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpSupplyInfoHdr> item) {
					LlpSupplyInfoHdr llpSupplyInfoHdr = item.getModelObject();
					
					item.add(new SSMLabel("createDt", llpSupplyInfoHdr.getCreateDt()));
					item.add(new SSMLabel("hdrCode", llpSupplyInfoHdr.getHdrCode()));
					item.add(new SSMLabel("hdrStatus", llpSupplyInfoHdr.getHdrStatus(), Parameter.SUPPLY_INFO_HDR_STATUS));
					
					
					SSMLink detailItem = new SSMLink("detailItem", item.getDefaultModel()) {
						public void onClick() {
							LlpSupplyInfoHdr llpSupplyInfoHdr = item.getModelObject();
							
							
							List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = llpSupplyInfoDtlService.findByHdrCode(llpSupplyInfoHdr.getHdrCode());
							
							
							List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
							List<LlpPaymentTransactionDetail> listPaymentItemsCertOnly = new ArrayList<LlpPaymentTransactionDetail>();

							for (int i = 0; i < listLlpSupplyInfoDtl.size(); i++) {
								LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
								if (dtl.getIsProfileSelected()) {
									LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
									paymentItem.setPaymentItem(Parameter.PAYMENT_LLP_PROFILE);
									paymentItem.setQuantity(1);
									paymentItem.setPaymentDet(dtl.getEntityNo() + "\t" + dtl.getEntityName());
									listPaymentItems.add(paymentItem);
								}
								if (dtl.getIsCertSelected()) {
									LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
									paymentItem.setPaymentItem(Parameter.PAYMENT_LLP_CERT);
									paymentItem.setQuantity(1);
									paymentItem.setPaymentDet(dtl.getEntityNo() + "\t" + dtl.getEntityName());
									listPaymentItemsCertOnly.add(paymentItem);
								}
							}

							if (listPaymentItemsCertOnly.size() > 0) {
								listPaymentItems.addAll(listPaymentItemsCertOnly);
							}
							
							setResponsePage(new EditLlpSupplyInfoHdrPage(llpSupplyInfoHdr.getHdrCode()));
							
//							setResponsePage(new PaymentDetailPage(llpSupplyInfoHdr.getHdrCode(), LlpSupplyInfoHdrService.class.getSimpleName(), llpSupplyInfoHdr, listPaymentItems));
							
						}
					};
					item.add(detailItem);
					

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};
			dataView.setItemsPerPage(50L);

			dataView.setOutputMarkupId(true);

			PagingNavigator navigator = new PagingNavigator("navigator", dataView);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);

			add(wmc);
		}

		public String getHdrStatus() {
			return hdrStatus;
		}

		public void setHdrStatus(String hdrStatus) {
			this.hdrStatus = hdrStatus;
		}
	}
}
