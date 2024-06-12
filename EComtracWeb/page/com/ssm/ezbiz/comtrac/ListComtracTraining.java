package com.ssm.ezbiz.comtrac;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.ezbiz.otcPayment.SearchBankinSlipDetail;
import com.ssm.ezbiz.otcPayment.ListCollectionCounter.ListCounterFormModel;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingFinalListingService;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListing;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

@SuppressWarnings({ "all" })
public class ListComtracTraining extends SecBasePage {

	@SpringBean(name = "RobTrainingParticipantService")
	private RobTrainingParticipantService robTrainingParticipantService;

	@SpringBean(name = "RobTrainingFinalListingService")
	private RobTrainingFinalListingService robTrainingFinalListingService;

	@SpringBean(name = "RobTrainingConfigService")
	private RobTrainingConfigService robTrainingConfigService;

	public ListComtracTraining() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchTrainingModel searchTrainingModel = new SearchTrainingModel();
				return searchTrainingModel;
			}
		}));
		add(new ListComtracTrainingForm("form", (IModel<SearchTrainingModel>) getDefaultModel()));
	}

	SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public class ListComtracTrainingForm extends Form implements Serializable {
		public ListComtracTrainingForm(String id, IModel<SearchTrainingModel> m) {
			super(id, m);

			SSMTextField trainingCode = new SSMTextField("trainingCode");
			trainingCode.setLabelKey("page.lbl.ezbiz.comtracList.trainingCode");
			add(trainingCode);

			SSMDateTextField dtFrom = new SSMDateTextField("dtFrom");
			dtFrom.setLabelKey("page.lbl.ezbiz.comtracList.dtFrom");
			add(dtFrom);

			SSMDateTextField dtTo = new SSMDateTextField("dtTo");
			dtTo.setLabelKey("page.lbl.ezbiz.comtracList.dtTo");
			add(dtTo);

			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SearchTrainingModel searchTrainingModel = (SearchTrainingModel) form.getDefaultModelObject();
					SearchCriteria sc = generateScTemplate(searchTrainingModel.getTrainingCode(),
							searchTrainingModel.getDtFrom(), searchTrainingModel.dtTo);
					populateTable(sc, target);
				}
			};
			add(search);

			SSMLink addTraining = new SSMLink("addTraining") {
				@Override
				public void onClick() {
					setResponsePage(EditComtracTrainingPage.class);
				}
			};
			add(addTraining);

			SearchCriteria criteria = null;
			try {
				criteria = new SearchCriteria("trainingStartDt", SearchCriteria.GREATER_EQUAL,
						pars.parse(fom.format(new Date()) + " 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			populateTable(criteria, null);
		}

	}

	public void populateTable(SearchCriteria sc, AjaxRequestTarget target) {

		WebMarkupContainer trainingDiv = new WebMarkupContainer("trainingDiv");
		trainingDiv.setOutputMarkupId(true);
		trainingDiv.setVisible(true);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("trainingId", SortOrder.DESCENDING, sc,
				RobTrainingConfigService.class);
		final SSMDataView<RobTrainingConfig> dataView = new SSMDataView<RobTrainingConfig>("sorting", dp) {
			private static final long serialVersionUID = 1L;
			final DecimalFormat df = new DecimalFormat("#,###,##0.00");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			protected void populateItem(final Item<RobTrainingConfig> item) {
				final RobTrainingConfig robTrainingConfig = item.getModelObject();

				RobTrainingFinalListing existingFinalListing = robTrainingFinalListingService
						.findByTrainingCode(robTrainingConfig.getTrainingCode());

				String trainingDt = sdf.format(robTrainingConfig.getTrainingStartDt());
				String status = "OPEN";
				if (!robTrainingConfig.getTrainingEndDt().equals(robTrainingConfig.getTrainingStartDt())) {
					trainingDt += " - " + sdf.format(robTrainingConfig.getTrainingEndDt());
				}

				Date closingDt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(robTrainingConfig.getRegClosingDt());
				c.add(Calendar.DATE, 1);
				closingDt = c.getTime();

				if (new Date().after(closingDt)) {
					status = "CLOSED";
				} else if (robTrainingConfig.getCurrentPax().equals(robTrainingConfig.getMaxPax())) {
					status = "MAX PARTICIPANT";
				} else if (!robTrainingConfig.getIsActive()) {
					status = "NOT ACTIVE";
				}

				Label statusLabel = new Label("status", status);
				if (status.equals("OPEN")) {
					statusLabel.add(new AttributeAppender("class", " green"));
				} else {
					statusLabel.add(new AttributeAppender("class", " red"));
				}
				item.add(statusLabel);
				item.add(new SSMLabel("trainingCode", robTrainingConfig.getTrainingCode()));
				item.add(new SSMLabel("trainingName", robTrainingConfig.getTrainingName()));
				item.add(new SSMLabel("trainingDt", trainingDt));
				item.add(new SSMLabel("regClosingDt", robTrainingConfig.getRegClosingDt(), "dd/MM/yyyy"));
				item.add(new SSMLabel("standardFee", df.format(robTrainingConfig.getStandardFee())));
				item.add(new SSMLabel("specialFee", df.format(robTrainingConfig.getSpecialFee())));
				item.add(new SSMLabel("maxPax", robTrainingConfig.getMaxPax()));
				item.add(new SSMLabel("currPax", robTrainingConfig.getCurrentPax()));
				item.add(new SSMLabel("trainingListSeq", robTrainingConfig.getTrainingListSeq()));

				SSMAjaxLink edit = new SSMAjaxLink("edit", item.getDefaultModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {

						setResponsePage(new EditComtracTrainingPage(robTrainingConfig));
					}
				};

				edit.setOutputMarkupPlaceholderTag(true);
				item.add(edit);
//				

				SSMAjaxLink nameList = new SSMAjaxLink("nameList", item.getDefaultModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new NameListAttendees(robTrainingConfig));
					}
				};
				nameList.setOutputMarkupPlaceholderTag(true);
				item.add(nameList);

				SSMAjaxLink finalListing = new SSMAjaxLink("finalListing", item.getDefaultModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {

						if (existingFinalListing == null) {

							RobTrainingFinalListing robTrainingFinalListingNew = new RobTrainingFinalListing();
							robTrainingFinalListingNew.setStatus(Parameter.COMTRAC_FINAL_LISTING_STATUS_pending);
							robTrainingFinalListingNew.setTrainingCode(robTrainingConfig.getTrainingCode());
							robTrainingFinalListingService.updateInsertAll(robTrainingFinalListingNew);
							setResponsePage(new RegisterFinalListing(robTrainingConfig));
						} else {

							setResponsePage(new RegisterFinalListing(robTrainingConfig));
						}
					}
				};
				finalListing.setOutputMarkupPlaceholderTag(true);
				item.add(finalListing);
				Label linkLabel = new Label("linkLabel", Model.of("Final Listing"));
				finalListing.add(linkLabel);

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

				if (existingFinalListing != null) {

					finalListing.setEnabled(false);
					linkLabel.setDefaultModel(Model.of("SUBMITTED"));
				}

			}
		};

		dataView.setItemsPerPage(20L);

		trainingDiv.add(dataView);
		trainingDiv.add(new SSMPagingNavigator("navigator", dataView));
		trainingDiv.add(new NavigatorLabel("navigatorLabel", dataView));

		if (target == null) {
			add(trainingDiv);
		} else {
			replace(trainingDiv);
			target.add(trainingDiv);
		}

	}

	public SearchCriteria generateScTemplate(String trainingCode, Date dateFrom, Date dateTo) {

		SearchCriteria sc = null;
		if (trainingCode != null) {
			sc = new SearchCriteria("trainingCode", SearchCriteria.EQUAL, trainingCode);
		}

		if (dateFrom != null) {
			if (sc != null) {
				try {
					sc = sc.andIfNotNull("trainingStartDt", SearchCriteria.GREATER_EQUAL,
							pars.parse(fom.format(dateFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					sc = new SearchCriteria("trainingStartDt", SearchCriteria.GREATER_EQUAL,
							pars.parse(fom.format(dateFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (dateTo != null) {
			if (sc != null) {
				try {
					sc = sc.andIfNotNull("trainingStartDt", SearchCriteria.LESS_EQUAL,
							pars.parse(fom.format(dateTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				try {
					sc = new SearchCriteria("trainingStartDt", SearchCriteria.LESS_EQUAL,
							pars.parse(fom.format(dateTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
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
		private String trainingCode;
		private Date dtFrom;
		private Date dtTo;

		public String getTrainingCode() {
			return trainingCode;
		}

		public void setTrainingCode(String trainingCode) {
			this.trainingCode = trainingCode;
		}

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
	}

	@Override
	public String getPageTitle() {
		return "menu.myBiz.listComtracTraining";
	}
}
