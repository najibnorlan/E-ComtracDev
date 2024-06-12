package com.ssm.llp.mod1.page;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ssm.ezbiz.comtrac.RobTrainingReprintcertDetails;
import com.ssm.ezbiz.comtrac.ViewListParticipantSummary;
import com.ssm.ezbiz.otcPayment.SearchBankinSlipDetail;
import com.ssm.ezbiz.otcPayment.ListCollectionCounter.ListCounterFormModel;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.ezbiz.service.RobTrainingReprintcertService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "all" })
public class RobTrainingReprintcertList extends SecBasePage {

	@SpringBean(name = "RobTrainingParticipantService")
	private RobTrainingParticipantService robTrainingParticipantService;

	@SpringBean(name = "LlpFileDataService")
	LlpFileDataService llpFileDataService;

	@Autowired
	LlpParametersService llpParametersService;

	@Autowired
	LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "MailService")
	MailService mailService;

	String lodgerName;
	boolean isInternalUser;

	public RobTrainingReprintcertList() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchTrainingModel searchTrainingModel = new SearchTrainingModel();
				return searchTrainingModel;
			}
		}));
		add(new RobTrainingReprintcertListForm("form", (IModel<SearchTrainingModel>) getDefaultModel()));
	}

	SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public class RobTrainingReprintcertListForm extends Form implements Serializable {
		public RobTrainingReprintcertListForm(String id, IModel<SearchTrainingModel> m) {
			super(id, m);

			SearchCriteria sc = null;
			lodgerName = UserEnvironmentHelper.getLoginName();
			isInternalUser = UserEnvironmentHelper.isInternalUser();

			SSMTextField certRefNo = new SSMTextField("certRefNo");
			certRefNo.setLabelKey("page.lbl.ecomtrac.orderRefNo");
			add(certRefNo);

			SSMTextField transactionCode = new SSMTextField("transactionCode");
			transactionCode.setLabelKey("page.lbl.ecomtrac.transactionCode");
			add(transactionCode);

			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.COMTRAC_TRANSACTION_STATUS);
			status.getChoices().add(0, Parameter.PAYMENT_STATUS_ALL);
			status.setLabelKey("page.lbl.ecomtrac.paymentStatus");
			add(status);

			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchTrainingModel searchTrainingModel = (SearchTrainingModel) form.getDefaultModelObject();
					SearchCriteria sc = generateScTemplate(searchTrainingModel.getCertRefNo(),
							searchTrainingModel.getTransactionCode(), searchTrainingModel.getStatus());
					populateTable(sc, target);
				}
			};
			add(search);

			if (isInternalUser) {
				sc = new SearchCriteria("createBy", SearchCriteria.IS_NOT_NULL);
				populateTable(sc, null);
			} else {
				sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
				populateTable(sc, null);
			}
		}

	}

	public void populateTable(SearchCriteria sc, AjaxRequestTarget target) {

		WebMarkupContainer certDiv = new WebMarkupContainer("certDiv");
		certDiv.setOutputMarkupId(true);
		certDiv.setVisible(true);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING, sc,
				RobTrainingReprintcertService.class);
		final SSMDataView<RobTrainingReprintcert> dataView = new SSMDataView<RobTrainingReprintcert>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			final DecimalFormat df = new DecimalFormat("#,###,##0.00");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

			protected void populateItem(final Item<RobTrainingReprintcert> item) {
				final RobTrainingReprintcert robTrainingReprintcert = item.getModelObject();

				item.add(new SSMLabel("certRefNo", robTrainingReprintcert.getCertRefNo()));
				item.add(new SSMLabel("transactionCode", robTrainingReprintcert.getTransactionCode()));
				item.add(new SSMLabel("trainingCode", robTrainingReprintcert.getTrainingCode()));
				item.add(new SSMLabel("trainingName", robTrainingReprintcert.getTrainingName()));
				item.add(new SSMLabel("icNo", robTrainingReprintcert.getIcNo()));
				item.add(new SSMLabel("statusPayment", robTrainingReprintcert.getStatus(),
						Parameter.COMTRAC_TRANSACTION_STATUS));
				item.add(new SSMLabel("createDt", sdf.format(robTrainingReprintcert.getCreateDt())));
				item.add(new SSMLabel("lodgerId", robTrainingReprintcert.getLodgerId()));

				AjaxLink goToTransaction = new AjaxLink("goToTransaction", item.getDefaultModel()) {
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new RobTrainingReprintcertDetails(null, robTrainingReprintcert,
								robTrainingReprintcert.getStatus(), getPage(), robTrainingReprintcert.getCertRefNo()));
					}
				};
				item.add(goToTransaction);

				SSMDownloadLink downloadCert = new SSMDownloadLink("downloadCert") {
					public void onClick() {
						LlpFileData fileData = llpFileDataService.findById(robTrainingReprintcert.getFileId());
						generateDownload(robTrainingReprintcert.getIcNo() + "_CERTIFICATE.pdf", fileData.getFileData());

					}
				};
				downloadCert.setOutputMarkupId(true);
				item.add(downloadCert);

				if (robTrainingReprintcert.getFileId() == null) {
					downloadCert.setVisible(false);
				}

				LocalDate date1 = LocalDate.parse(sdf2.format(robTrainingReprintcert.getCreateDt()));
				LocalDate date2 = LocalDate.now();
				long daysDiff = ChronoUnit.DAYS.between(date1, date2);

				if (daysDiff > 30) {
					downloadCert.setEnabled(false);
				}
				if (isInternalUser) {
					downloadCert.setEnabled(true);
				}

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

		certDiv.add(dataView);
		certDiv.add(new SSMPagingNavigator("navigator", dataView));
		certDiv.add(new NavigatorLabel("navigatorLabel", dataView));

		if (target == null) {
			add(certDiv);
		} else {
			replace(certDiv);
			target.add(certDiv);
		}

	}

	public SearchCriteria generateScTemplate(String certRefNo, String transactionCode, String status) {

		SearchCriteria sc = null;
		SearchCriteria sc1 = null;

		if (isInternalUser) {

			if (certRefNo != null) {
				sc = new SearchCriteria("certRefNo", SearchCriteria.EQUAL, certRefNo);
			}
		} else {
			if (certRefNo != null) {
				sc = new SearchCriteria("certRefNo", SearchCriteria.EQUAL, certRefNo);

				sc = new SearchCriteria("certRefNo", SearchCriteria.EQUAL, certRefNo);
				sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
				sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
			}
		}

		if (isInternalUser) {

			if (sc != null) {
				if (transactionCode != null) {
					try {
						sc = sc.andIfNotNull("transactionCode", SearchCriteria.EQUAL, transactionCode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (transactionCode != null) {
					try {
						sc = new SearchCriteria("transactionCode", SearchCriteria.EQUAL, transactionCode);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} else {
			if (sc != null) {
				if (transactionCode != null) {
					try {
						sc = new SearchCriteria("transactionCode", SearchCriteria.EQUAL, transactionCode);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (transactionCode != null) {
					try {
						sc = new SearchCriteria("transactionCode", SearchCriteria.EQUAL, transactionCode);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}

		if (isInternalUser) {

			if (sc != null) {
				if (status != null) {
					if (status.equals("ALL")) {
						sc1 = new SearchCriteria("createBy", SearchCriteria.IS_NOT_NULL);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else {
						sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, status);
					}
				}
			} else {
				if (status != null) {
					if (status.equals("ALL")) {
						sc = new SearchCriteria("createBy", SearchCriteria.IS_NOT_NULL);
					} else {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
					}
				}
			}
		} else {
			if (sc != null) {
				if (status != null) {
					if (status.equals("ALL")) {
						sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, lodgerName);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_cancel)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_cert_generated)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_data_entry)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else {

					}
				}
			} else {
				if (status != null) {
					if (status.equals("ALL")) {
						sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_cancel)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_cert_generated)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_data_entry)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					} else if (status.equals(Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment)) {
						sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
						sc1 = new SearchCriteria("createBy", SearchCriteria.EQUAL, lodgerName);
						sc = new SearchCriteria(sc, SearchCriteria.AND, sc1);
					}
				}
			}
		}

		return sc;
	}

	public void generateDownload(String fileName, final byte[] byteData) {

		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(byteData);
				output.flush();
			}
		};

		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}

	public class SearchTrainingModel implements Serializable {

		private String certRefNo;
		private String transactionCode;
		private String status;

		public String getCertRefNo() {
			return certRefNo;
		}

		public void setCertRefNo(String certRefNo) {
			this.certRefNo = certRefNo;
		}

		public String getTransactionCode() {
			return transactionCode;
		}

		public void setTransactionCode(String transactionCode) {
			this.transactionCode = transactionCode;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	@Override
	public String getPageTitle() {
		return "title.page.comtrac.reprintCertOrders";
	}
}
