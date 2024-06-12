package com.ssm.ezbiz.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.robFormB.ViewRobFormBPage;
import com.ssm.ezbiz.robFormC.EditRobFormCPage;
import com.ssm.ezbiz.robFormC.ViewRobFormCPage;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobFormTransactionService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormTransactionModel;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.page.robRenewal.EditRobRenewalPage;

public class PendingTransactionPanel extends BasePanel {

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;

	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;

	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "RobFormTransactionService")
	private RobFormTransactionService robFormTransactionService;
	

	public PendingTransactionPanel(String id, List<RobFormTransactionModel> listPendingTransaction, final Page parentPage) {
		super(id);

		if(listPendingTransaction.size()==0) {
			this.setVisible(false);
		}
		
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("createDt", listPendingTransaction);
		final SSMDataView<RobFormTransactionModel> dataView = new SSMDataView<RobFormTransactionModel>("sorting", dp) {

			protected void populateItem(final Item<RobFormTransactionModel> item) {

				final RobFormTransactionModel form = item.getModelObject();

				
				item.add(new SSMLabel("formType", form.getFormType(), Parameter.ROB_FORM_TYPE ));
				item.add(new SSMLabel("appRefNo", form.getAppRefNo()));
				item.add(new SSMLabel("brNoNBizName", form.getBrNoNBizName()));
				item.add(new SSMLabel("updateDt", form.getUpdateDt()));
				
				
				if (Parameter.ROB_FORM_TYPE_RENEWAL.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_RENEWAL_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_A.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_A_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_B.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_B_STATUS,form.getStatus()) ));
				} else if (Parameter.ROB_FORM_TYPE_C.equals(form.getFormType())) {
					item.add(new SSMLabel("status", getCodeTypeWithValue( Parameter.ROB_FORM_C_STATUS,form.getStatus()) ));
				}

				SSMAjaxLink viewAction = new SSMAjaxLink("viewAction") {
					@Override
					public void onClick(AjaxRequestTarget arg0) {

						if (Parameter.ROB_FORM_TYPE_RENEWAL.equals(form.getFormType())) {
							setResponsePage(new EditRobRenewalPage((RobRenewal) form.getFormObject()));
						} else if (Parameter.ROB_FORM_TYPE_A.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_A_STATUS_QUERY.equals(form.getStatus())
									|| Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormAPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormAPage2(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						} else if (Parameter.ROB_FORM_TYPE_B.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_B_STATUS_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormBPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormBPage(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						} else if (Parameter.ROB_FORM_TYPE_C.equals(form.getFormType())) {
							Page actionPage = null;
							if (Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(form.getStatus())
									|| Parameter.ROB_FORM_C_STATUS_QUERY.equals(form.getStatus())) {
								actionPage = new EditRobFormCPage(form.getAppRefNo());
							} else {
								actionPage = new ViewRobFormCPage(form.getAppRefNo(), parentPage);
							}
							setResponsePage(actionPage);
						}

					}
				};
				item.add(viewAction);
				
			}
		};
		dataView.setItemsPerPage(30L);
		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
	}
	
	boolean isValidForDownload(Date dateUpdate, int downValidDays){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateUpdate);
		cal.add(Calendar.DATE, downValidDays);
		
		boolean allowDownload = true;
		if (new Date().after(cal.getTime())) {
			allowDownload = false;
		}
		return allowDownload;
	}

	
}
