package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;

public class SearchLlpPaymentTransaction extends SecBasePanel {

	// public SearchLlpPaymentTransaction() {
	// super();
	// add(new SearchPaymentTransactionForm("searchPaymentTransactionForm"));
	// }

	public SearchLlpPaymentTransaction(String id, final Date searchFromDt, final Date searchToDt, final String paymentMode,
			final String transactionId, final String status, final String refNo,final String paymentGroup) {
		super(id);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchPaymentModel spm = new SearchPaymentModel();
				spm.setSearchFromDt(searchFromDt);
				spm.setSearchToDt(searchToDt);
				spm.setPaymentMode(paymentMode);
				spm.setTransactionId(transactionId);
				spm.setStatus(status);
				spm.setRefNo(refNo);
				spm.setPaymentGroup(paymentGroup);

				return spm;
			}
		}));

		add(new SearchPaymentTransactionForm("searchPaymentTransactionForm", getDefaultModel()));
	}

	public SearchLlpPaymentTransaction(String id) {
		super(id);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchPaymentModel obj = (SearchPaymentModel) getDefaultModel().getObject();
				if (obj != null) {
					return obj;
				}
				SearchPaymentModel spm = new SearchPaymentModel();
				spm.setSearchFromDt(new Date());
				return spm;
			}
		}));

		add(new SearchPaymentTransactionForm("searchPaymentTransactionForm", getDefaultModel()));
	}

	private class SearchPaymentModel implements Serializable {
		private Date searchFromDt;
		private Date searchToDt;
		private String paymentMode;
		private String transactionId;
		private String refNo;
		private String status;
		private String paymentGroup;

		public SearchPaymentModel() {
			// TODO Auto-generated constructor stub
		}

		public SearchPaymentModel(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo) {
			this.searchFromDt = searchFromDt;
			this.searchToDt = searchToDt;
			this.paymentMode = paymentMode;
			this.transactionId = transactionId;
			this.status = status;
			this.refNo = refNo;
		}

		public Date getSearchFromDt() {
			return searchFromDt;
		}

		public void setSearchFromDt(Date searchFromDt) {
			this.searchFromDt = searchFromDt;
		}

		public Date getSearchToDt() {
			return searchToDt;
		}

		public void setSearchToDt(Date searchToDt) {
			this.searchToDt = searchToDt;
		}

		public String getPaymentMode() {
			return paymentMode;
		}

		public void setPaymentMode(String paymentMode) {
			this.paymentMode = paymentMode;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		public String getPaymentGroup() {
			return paymentGroup;
		}

		public void setPaymentGroup(String paymentGroup) {
			this.paymentGroup = paymentGroup;
		}
	}

	private class SearchPaymentTransactionForm extends Form {

		public SearchPaymentTransactionForm(String id, IModel m) {
			super(id, m);
			setPrefixLabelKey("searchPaymentTransaction.panel.");

			SSMDateTextField dfFrom = new SSMDateTextField("searchFromDt", "dd/MM/yyyy");
			add(dfFrom);

			SSMDateTextField dfTo = new SSMDateTextField("searchToDt", "dd/MM/yyyy");
			add(dfTo);

			SSMDropDownChoice tfPaymentMode = new SSMDropDownChoice("paymentMode", Parameter. PAYMENT_MODE_SEARCH);
			add(tfPaymentMode);
			
/*			SSMTextField tfPaymentMode = new SSMTextField("paymentMode");
			add(tfPaymentMode);*/

			SSMTextField tfTransactionId = new SSMTextField("transactionId");
			add(tfTransactionId);
			
			SSMTextField tfRefNo = new SSMTextField("refNo");
			add(tfRefNo);

			SSMDropDownChoice tfStatus = new SSMDropDownChoice("status", Parameter.PAYMENT_STATUS);
			tfStatus.getChoices().add(0, Parameter.PAYMENT_STATUS_ALL);
			add(tfStatus);
			
			SSMDropDownChoice paymentGroup = new SSMDropDownChoice("paymentGroup", Parameter.PAYMENT_GROUP);
			add(paymentGroup);
			if(!UserEnvironmentHelper.isInternalUser()){
				paymentGroup.setVisible(false);
				tfPaymentMode.setVisible(false);
			}

			setMarkupId("search-form");
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchPaymentModel searchPaymentModel = (SearchPaymentModel) form.getDefaultModelObject();
					setResponsePage(new ListPaymentTransactionPage(searchPaymentModel.getSearchFromDt(), searchPaymentModel.getSearchToDt(),
							searchPaymentModel.getPaymentMode(), searchPaymentModel.getTransactionId(), searchPaymentModel.getStatus(), searchPaymentModel.getRefNo(), searchPaymentModel.getPaymentGroup()));
				}
			};
			add(search);
		}

	}

}
