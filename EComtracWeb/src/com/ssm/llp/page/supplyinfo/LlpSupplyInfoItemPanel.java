package com.ssm.llp.page.supplyinfo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpReservedName;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LlpSupplyInfoItemPanel extends BasePanel {

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpSupplyInfoHdrService")
	private LlpSupplyInfoHdrService llpSupplyInfoHdrService;

	private SSMLabel totalProfileAmtLbl;
	private SSMLabel totalCertAmtLbl;
	private SSMLabel totalAmtLbl;

	private Double totalProfileAmt = new Double(0);
	private Double totalCertAmt = new Double(0);
	private Double totalAmt = new Double(0);

	private double profileAmt;
	private double certAmt;

	private WebMarkupContainer wmc;
	private SSMDataView dataView;
	private SSMSessionSortableDataProvider dp;
	private InputForm inputFormInfoItemPanel;

	public LlpSupplyInfoItemPanel(String panelId) {
		super(panelId);

		profileAmt = llpPaymentFeeService.findById(Parameter.PAYMENT_LLP_PROFILE).getPaymentFee();
		certAmt = llpPaymentFeeService.findById(Parameter.PAYMENT_LLP_CERT).getPaymentFee();

		inputFormInfoItemPanel = new InputForm("inputFormInfoItemPanel");
		add(inputFormInfoItemPanel);
	}

	public void refreshContainer(AjaxRequestTarget target) {
		inputFormInfoItemPanel.refreshContainer(target);
	}

	/** form for processing the input. */
	private class InputForm extends Form {

		public InputForm(String name) {
			super(name);
			List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = (List) getSession().getAttribute("llpSupplyInfoDetList_");

			dp = new SSMSessionSortableDataProvider(listLlpSupplyInfoDtl);
			dataView = new SSMDataView<LlpSupplyInfoDtl>("sorting", dp) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final Item<LlpSupplyInfoDtl> item) { // populate
																					// list
																					// of
																					// partner
					LlpSupplyInfoDtl infoDtl = item.getModelObject();
					item.add(new SSMLabel("entityNo", infoDtl.getEntityNo()));
					item.add(new SSMLabel("entityName", infoDtl.getEntityName()));

					item.add(new AjaxCheckBox("isProfileSelected", new PropertyModel(infoDtl, "isProfileSelected")) {
						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							LlpSupplyInfoDtl infoDtl = (LlpSupplyInfoDtl) getParent().getDefaultModelObject();
							if (String.valueOf(true).equals(getValue())) {
								infoDtl.setIsProfileSelected(true);
							} else {
								infoDtl.setIsProfileSelected(false);
							}
							refreshContainer(target);
						}
					});
					item.add(new AjaxCheckBox("isCertChkbox", new PropertyModel(infoDtl, "isCertSelected")) {
						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							LlpSupplyInfoDtl infoDtl = (LlpSupplyInfoDtl) getParent().getDefaultModelObject();
							if (String.valueOf(true).equals(getValue())) {
								infoDtl.setIsCertSelected(true);
							} else {
								infoDtl.setIsCertSelected(false);
							}
							refreshContainer(target);
						}
					});

					AjaxLink deleteLink = new AjaxLink("delete", item.getDefaultModel()) {
						@Override
						public void onClick(AjaxRequestTarget target) {
							LlpSupplyInfoDtl selectedDtl = (LlpSupplyInfoDtl) getParent().getDefaultModelObject();
							List<LlpSupplyInfoDtl> llpSupplyInfoDetList = (List) getSession().getAttribute("llpSupplyInfoDetList_");
							llpSupplyInfoDetList.remove(selectedDtl);
							refreshContainer(target);
						}
					};

					// SSMLink deleteLink = new SSMLink("delete",
					// item.getDefaultModel(),true) {
					// public void onClick() {
					// System.out.println("Delete");
					//
					// }
					// };
					item.add(deleteLink);

					// AjaxLink editLink = new AjaxLink("ajaxEdit",
					// item.getDefaultModel()) {
					// @Override
					// public void onClick(AjaxRequestTarget target) {
					// System.out.println("Edit");
					// }
					// };
					// item.add(editLink); //add into form

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};

			dataView.setItemsPerPage(5L);
			// add(dataView);
			// add(new PagingNavigator("navigator", dataView));
			// add(new NavigatorLabel("navigatorLabel", dataView));

			totalProfileAmtLbl = new SSMLabel("totalProfileAmt", totalProfileAmt);
			totalProfileAmtLbl.setOutputMarkupId(true);
			totalCertAmtLbl = new SSMLabel("totalCertAmt", totalCertAmt);
			totalAmtLbl = new SSMLabel("totalAmt", totalAmt);
			
			
			
			AjaxButton pay = new AjaxButton("pay") {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = (List) getSession().getAttribute("llpSupplyInfoDetList_");
					if (listLlpSupplyInfoDtl.size() > 0) {
						System.out.println("PAY INFO");
						recalculateAmt(listLlpSupplyInfoDtl);
						
						LlpSupplyInfoHdr hdr = new LlpSupplyInfoHdr();
						hdr.setHdrStatus(Parameter.SUPPLY_INFO_HDR_STATUS_pending_payment);//PendingPayment
						hdr.setTotalProfileAmt(totalProfileAmt);
						hdr.setTotalCertAmt(totalCertAmt);
						hdr.setTotalAmt(totalAmt);
						
						llpSupplyInfoHdrService.submitLlpSupplyInfo(hdr, listLlpSupplyInfoDtl);

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
						
						
						setResponsePage(new PaymentDetailPage(hdr.getHdrCode(), LlpSupplyInfoHdrService.class.getSimpleName(), hdr, listPaymentItems));
					}

				}
			};

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(totalProfileAmtLbl);
			wmc.add(totalCertAmtLbl);
			wmc.add(totalAmtLbl);
			wmc.add(pay);

			wmc.add(dataView);
			wmc.add(new PagingNavigator("navigator", dataView));
			wmc.add(new NavigatorLabel("navigatorLabel", dataView));
			wmc.setOutputMarkupId(true);
			add(wmc);

			recalculateAmt(listLlpSupplyInfoDtl);

		}

		public void refreshContainer(AjaxRequestTarget target) {
			List<LlpSupplyInfoDtl> llpSupplyInfoDetList = (List) getSession().getAttribute("llpSupplyInfoDetList_");
			dp.resetView(llpSupplyInfoDetList);
			recalculateAmt(llpSupplyInfoDetList);
			target.add(wmc);
			getSession().setAttribute("llpSupplyInfoDetList_",(Serializable) llpSupplyInfoDetList);
		}

		public void recalculateAmt(List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl) {
			totalProfileAmt = 0.0;
			totalCertAmt = 0.0;
			totalAmt = 0.0;
			
			for (int i = 0;listLlpSupplyInfoDtl!=null &&  i < listLlpSupplyInfoDtl.size(); i++) {
				LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
				if (dtl.getIsProfileSelected()) {
					totalProfileAmt += profileAmt;
				}
				if (dtl.getIsCertSelected()) {
					totalCertAmt += certAmt;
				}
			}

			DecimalFormat df = new DecimalFormat("#0.00");

			totalAmt = totalProfileAmt + totalCertAmt;

			totalProfileAmtLbl.setDefaultModelObject(df.format(totalProfileAmt));
			totalCertAmtLbl.setDefaultModelObject(df.format(totalCertAmt));
			totalAmtLbl.setDefaultModelObject(df.format(totalAmt));

		}

	}

}
