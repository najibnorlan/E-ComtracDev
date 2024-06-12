package com.ssm.ezbiz.eghl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.EGHLService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings("all")
public class PaymentCheckWithEghl extends SecBasePage{ 
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "EGHLService")
	private EGHLService eghlService;
		
	public PaymentCheckWithEghl(){
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
					protected Object load() {
						PaymentCheckWithEghlModel paymentCheckWithEghlModel = new PaymentCheckWithEghlModel();
						return paymentCheckWithEghlModel;
					}

				}));
				add(new PaymentCheckWithEghlForm("form", (IModel<PaymentCheckWithEghlModel>) getDefaultModel()));

	}
	
	private class PaymentCheckWithEghlForm extends Form implements Serializable {
		
		public PaymentCheckWithEghlForm(String id , IModel<PaymentCheckWithEghlModel> m ){
			super(id, m);
			
			PaymentCheckWithEghlModel searchModel = m.getObject();
			
			SSMTextField appRefNo = new SSMTextField("appRefNo");
			appRefNo.setLabelKey("page.lbl.ezbiz.robFormASearch.formARefNo");
			add(appRefNo);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					PaymentCheckWithEghlModel searchModelForm = (PaymentCheckWithEghlModel) getForm().getDefaultModelObject();

					List<EGHLQueryResponse> listEghlQueryResponse = eghlService.getStatusByAppRefNo(searchModelForm.getAppRefNo());
					populateTable(listEghlQueryResponse, target);
				}
			};
			add(search);
			populateTable(null, null);
		}
		
		public void populateTable(List<EGHLQueryResponse> listEghlResponse, AjaxRequestTarget target) {
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listEghlResponse);
			final SSMDataView<EGHLQueryResponse> dataView = new SSMDataView<EGHLQueryResponse>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<EGHLQueryResponse> item) {
					final EGHLQueryResponse eghlModel= item.getModelObject();
					
					
					String response = "";
					Field[] field = EGHLQueryResponse.class.getDeclaredFields();
					for (int i = 0; i < field.length; i++) {
						try {
							response += field[i].getName() +" : " + field[i].get(eghlModel);
							response+="\n";
						} catch (Exception e) {
						}
					}
					
					
					item.add(new MultiLineLabel("eghlResponse", response));
					

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
		
	private class PaymentCheckWithEghlModel{
		
		private String appRefNo;

		public String getAppRefNo() {
			return appRefNo;
		}

		public void setAppRefNo(String appRefNo) {
			this.appRefNo = appRefNo;
		}
	}
	
	public String getPageTitle() {
		return "menu.myBiz.paymentWithEghl";
	}
}