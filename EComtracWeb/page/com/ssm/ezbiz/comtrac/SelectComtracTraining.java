package com.ssm.ezbiz.comtrac;

import java.awt.TextField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.guice.GuiceFieldValueFactory.MoreThanOneBindingException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Hibernate;

import com.ssm.ezbiz.robformA.EditRobFormABizCodePanel;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "all" })
public class SelectComtracTraining extends SecBasePage {

	@SpringBean(name = "RobTrainingConfigService")
	RobTrainingConfigService robTrainingConfigService;

	@SpringBean(name = "RobTrainingTransactionService")
	RobTrainingTransactionService robTrainingTransactionService;

	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;

	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.selectComtracTraining.comtracLbl";
	}

	RobTrainingTransaction trainingTransaction;
	WebMarkupContainer wmcParticipant;
	WebMarkupContainer errorWmcSeat;
	WebMarkupContainer seatWmc;
	WebMarkupContainer errorWmcDate;
	RobTrainingConfig robTrainingConfig;
	ComtracFormModel formModel;
	SSMAjaxButton submitPayment;
	SSMAjaxButton submit;
	SSMAjaxButton showParticipantFormPanel;
	SSMAjaxButton saveAsDraft;
	Integer available = 0;

	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat deciFormat = new DecimalFormat("####,###,##0.00");

	public static Map<Integer, Integer> balanceSeat = new HashMap<Integer, Integer>();

	public SelectComtracTraining() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ComtracFormModel comtracFormModel = new ComtracFormModel();
				comtracFormModel.setRobTrainingTransaction(new RobTrainingTransaction());
				comtracFormModel.getRobTrainingTransaction().setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_data_entry);
				return comtracFormModel;
			}
		}));
		add(new SelectComtracTrainingForm("selectComtracTrainingForm", getDefaultModel()));
	}

	public SelectComtracTraining(final String trainingTrans) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {

				RobTrainingTransaction robTrainingTransaction = robTrainingTransactionService
						.findByTransactionCodeWithParticipant(trainingTrans);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				available = robTrainingTransaction.getTrainingId().getMaxPax()
						- robTrainingTransaction.getTrainingId().getCurrentPax();

				if (available < 0) {
					available = 0;
				}
				ComtracFormModel comtracFormModel = new ComtracFormModel();
				comtracFormModel.setRobTrainingTransaction(robTrainingTransaction);
				comtracFormModel.setRobTrainings(robTrainingTransaction.getTrainingId());
				comtracFormModel.setSpecialFee(robTrainingTransaction.getTrainingId().getSpecialFee());
				comtracFormModel.setStandardFee(robTrainingTransaction.getTrainingId().getStandardFee());
				comtracFormModel.setTrainingCode(robTrainingTransaction.getTrainingId().getTrainingCode());
				comtracFormModel.setTrainingDesc(robTrainingTransaction.getTrainingId().getTrainingDesc());
				comtracFormModel.setTrainingName(robTrainingTransaction.getTrainingId().getTrainingName());
				comtracFormModel.setTrainingEndDt(df.format(robTrainingTransaction.getTrainingId().getTrainingEndDt()));
				comtracFormModel
						.setTrainingStartDt(df.format(robTrainingTransaction.getTrainingId().getTrainingStartDt()));
				comtracFormModel.setRegClosingDt(df.format(robTrainingTransaction.getTrainingId().getRegClosingDt()));
				comtracFormModel.setTrainingStartTime(robTrainingTransaction.getTrainingId().getTrainingStartTime());
				comtracFormModel.setTrainingEndTime(robTrainingTransaction.getTrainingId().getTrainingEndTime());
				comtracFormModel.setCpePoint(robTrainingTransaction.getTrainingId().getCpePoint());
				comtracFormModel.setTrainingVenue(robTrainingTransaction.getTrainingId().getTrainingVenue());
				comtracFormModel.setAvailableSeat(available);
				if (UserEnvironmentHelper.isInternalUser()) {
					comtracFormModel.setReceiptNo(robTrainingTransaction.getReceiptNo());
					comtracFormModel.setLouLoaRefNo(robTrainingTransaction.getLouLoaRefNo());
					comtracFormModel.setInvoiceNo(robTrainingTransaction.getInvoiceNo());
					comtracFormModel.setPaymentChannel(robTrainingTransaction.getPaymentChannel());
					comtracFormModel.setRegistrationChannel(robTrainingTransaction.getRegistrationChannel());
					comtracFormModel.setRemarks(robTrainingTransaction.getRemarks());
				}
				return comtracFormModel;
			}
		}));
		add(new SelectComtracTrainingForm("selectComtracTrainingForm", getDefaultModel()));
	}

	private Integer getAvailableSeat(RobTrainingConfig robTrainingConfig) {
		Integer avaiSeat = balanceSeat.get(robTrainingConfig.getTrainingId());
		if (avaiSeat == null) {
			return resetAvailableSeat(robTrainingConfig);
		} else {
			return avaiSeat;
		}
	}

	public static synchronized Integer resetAvailableSeat(RobTrainingConfig robTrainingConfig) {
		Integer available = robTrainingConfig.getMaxPax() - robTrainingConfig.getCurrentPax();
		if (available < 0) {
			available = 0;
		}
		balanceSeat.put(robTrainingConfig.getTrainingId(), available);
		return available;
	}

	private class SelectComtracTrainingForm extends Form implements Serializable {

		private SelectComtracTrainingForm(String id, IModel m) {
			super(id, m);

			formModel = (ComtracFormModel) m.getObject();
			trainingTransaction = formModel.getRobTrainingTransaction();

			if (formModel.getRobTrainings() != null) {
				robTrainingConfig = formModel.getRobTrainings();
			}

			final WebMarkupContainer trainingInfo = new WebMarkupContainer("trainingInfo");
			trainingInfo.setOutputMarkupPlaceholderTag(true);
			trainingInfo.setOutputMarkupId(true);

			errorWmcSeat = new WebMarkupContainer("errorWmcSeat");
			errorWmcSeat.setOutputMarkupPlaceholderTag(true);
			errorWmcSeat.setOutputMarkupId(true);
			errorWmcSeat.setVisible(false);

			errorWmcDate = new WebMarkupContainer("errorWmcDate");
			errorWmcDate.setOutputMarkupPlaceholderTag(true);
			errorWmcDate.setOutputMarkupId(true);
			errorWmcDate.setVisible(false);

			trainingInfo.add(errorWmcSeat);
			trainingInfo.add(errorWmcDate);

			if (formModel.getRobTrainingTransaction().getTransactionCode() != null) {
				trainingInfo.setVisible(true);
			} else {
				trainingInfo.setVisible(false);
			}

			add(trainingInfo);

			wmcParticipant = new WebMarkupContainer("wmcParticipant");
			wmcParticipant.setOutputMarkupId(true);
			wmcParticipant.setVisible(true);
			trainingInfo.add(wmcParticipant);

			final WebMarkupContainer internalUse = new WebMarkupContainer("internalUse");
			internalUse.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(internalUse);

			seatWmc = new WebMarkupContainer("seatWmc");
			seatWmc.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(seatWmc);

			final WebMarkupContainer buttonMarkup = new WebMarkupContainer("buttonMarkup");
			buttonMarkup.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(buttonMarkup);

			if (!UserEnvironmentHelper.isInternalUser()) {
				internalUse.setVisible(false);
			} else {
				internalUse.setVisible(true);
			}

			List<RobTrainingConfig> listTraining = robTrainingConfigService.getAvailableTraining();

			final DropDownChoice robTrainings = new DropDownChoice("robTrainings", listTraining);
			robTrainings.setLabelKey("selectComtracTraining.page.robTrainings");
			add(robTrainings);

			final SSMLabel trainingCode = new SSMLabel("trainingCode");
			trainingCode.setOutputMarkupId(true);
			trainingCode.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingCode);

			final SSMLabel availableSeat = new SSMLabel("availableSeat");
			availableSeat.setOutputMarkupId(true);
			availableSeat.setOutputMarkupPlaceholderTag(true);
			seatWmc.add(availableSeat);
			trainingInfo.add(seatWmc);
			seatWmc.add(new AbstractAjaxTimerBehavior(Duration.seconds(5)) {
				@Override
				protected void onTimer(AjaxRequestTarget target) {
					if (robTrainingConfig != null) {
//						Boolean isUpdate = updateSeat.get(robTrainingConfig.getTrainingId());
//						if(isUpdate==null){
//							isUpdate= true;
//							updateSeat.put(robTrainingConfig.getTrainingId().longValue(), isUpdate);
//						}
//						
//						if(!isUpdate){
//							return;
//						}
//						isUpdate= false;

//						RobTrainingConfig tempObject = robTrainingConfigService.findById(robTrainingConfig.getTrainingId());
//						Integer available = tempObject.getMaxPax() - tempObject.getCurrentPax();
//						if(available < 0){
//							available = 0;
//						}
						Integer available = getAvailableSeat(robTrainingConfig);

						availableSeat.setDefaultModelObject(available);

						errorWmcDate.setVisible(false);
						errorWmcSeat.setVisible(false);
						showParticipantFormPanel.setEnabled(true);

						if (UserEnvironmentHelper.isInternalUser()) {
							if (formModel.getPaymentChannel() != null && formModel.getRegistrationChannel() != null) {
								showParticipantFormPanel.setEnabled(true);
							} else {
								showParticipantFormPanel.setEnabled(false);
							}
						} else {
							if (available > 0 && getZeroTimeDate(new Date())
									.before(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
								showParticipantFormPanel.setEnabled(true);
							} else {
								if (available <= 0) {
									errorWmcDate.setVisible(false);
									errorWmcSeat.setVisible(true);
								} else if (getZeroTimeDate(new Date())
										.after(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
									errorWmcDate.setVisible(true);
									errorWmcSeat.setVisible(false);
								}
								showParticipantFormPanel.setEnabled(false);
							}
						}
						target.add(errorWmcDate);
						target.add(errorWmcSeat);

						target.add(seatWmc);
						target.add(showParticipantFormPanel);
					}
				}

			});

			final SSMLabel trainingName = new SSMLabel("trainingName");
			trainingName.setOutputMarkupId(true);
			trainingName.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingName);

			final SSMLabel trainingDesc = new SSMLabel("trainingDesc");
			trainingDesc.setOutputMarkupId(true);
			trainingDesc.setOutputMarkupPlaceholderTag(true);
			trainingDesc.setEscapeModelStrings(false);
			trainingInfo.add(trainingDesc);

			final SSMLabel trainingDt = new SSMLabel("trainingDt", "");
			trainingDt.setOutputMarkupId(true);
			trainingDt.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingDt);

			if (formModel.getTrainingStartDt() != null) {
				String trainDt = df.format(robTrainingConfig.getTrainingStartDt());
				if (!robTrainingConfig.getTrainingEndDt().equals(robTrainingConfig.getTrainingStartDt())) {
					trainDt += " - " + df.format(robTrainingConfig.getTrainingEndDt());
				}
				trainingDt.setDefaultModelObject(trainDt);
			}
			final SSMLabel regClosingDt = new SSMLabel("regClosingDt");
			regClosingDt.setOutputMarkupId(true);
			regClosingDt.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(regClosingDt);

			final SSMLabel trainingStartTime = new SSMLabel("trainingStartTime");
			trainingStartTime.setOutputMarkupId(true);
			trainingStartTime.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingStartTime);

			final SSMLabel trainingEndTime = new SSMLabel("trainingEndTime");
			trainingEndTime.setOutputMarkupId(true);
			trainingEndTime.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingEndTime);

			final SSMLabel cpePoint = new SSMLabel("cpePoint");
			cpePoint.setOutputMarkupId(true);
			cpePoint.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(cpePoint);

			final SSMLabel trainingVenue = new SSMLabel("trainingVenue");
			trainingVenue.setOutputMarkupId(true);
			trainingVenue.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(trainingVenue);

			final SSMLabel standardFee = new SSMLabel("standardFee");
			standardFee.setOutputMarkupId(true);
			standardFee.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(standardFee);

			final SSMLabel specialFee = new SSMLabel("specialFee");
			specialFee.setOutputMarkupId(true);
			specialFee.setOutputMarkupPlaceholderTag(true);
			trainingInfo.add(specialFee);

			SSMTextField receiptNo = new SSMTextField("receiptNo");
			receiptNo.setLabelKey("selectComtracTraining.page.receiptNo");
			receiptNo.setOutputMarkupId(true);
			internalUse.add(receiptNo);

			SSMTextField invoiceNo = new SSMTextField("invoiceNo");
			invoiceNo.setLabelKey("selectComtracTraining.page.invoiceNo");
			invoiceNo.setOutputMarkupId(true);
			internalUse.add(invoiceNo);

			SSMTextField louLoaRefNo = new SSMTextField("louLoaRefNo");
			louLoaRefNo.setLabelKey("selectComtracTraining.page.louLoaRefNo");
			louLoaRefNo.setOutputMarkupId(true);
			internalUse.add(louLoaRefNo);

			SSMTextArea remarks = new SSMTextArea("remarks");
			remarks.setLabelKey("selectComtracTraining.page.remarks");
			remarks.setOutputMarkupId(true);
			internalUse.add(remarks);

			SSMDropDownChoice paymentChannel = new SSMDropDownChoice("paymentChannel",
					Parameter.COMTRAC_PAYMENT_CHANNEL);
			paymentChannel.setOutputMarkupId(true);
			paymentChannel.setLabelKey("selectComtracTraining.page.paymentChannel");
			paymentChannel.setRequired(true);
			internalUse.add(paymentChannel);

			SSMDropDownChoice registrationChannel = new SSMDropDownChoice("registrationChannel",
					Parameter.COMTRAC_REGISTRATION_CHANNEL);
			registrationChannel.setOutputMarkupId(true);
			registrationChannel.setLabelKey("selectComtracTraining.page.registrationChannel");
			registrationChannel.setRequired(true);
			internalUse.add(registrationChannel);

			resetTimer(null, trainingTransaction);

			IChoiceRenderer renderer = new IChoiceRenderer<Object>() {
				@Override
				public Object getDisplayValue(Object arg0) {
					RobTrainingConfig config = (RobTrainingConfig) arg0;
					return config.getTrainingCode() + " : " + config.getTrainingName();
				}

				@Override
				public String getIdValue(Object arg0, int arg1) {
					RobTrainingConfig config = (RobTrainingConfig) arg0;
					return config.getTrainingId().toString();
				}
			};
			robTrainings.setChoiceRenderer(renderer);

			SSMAjaxFormSubmitBehavior choicesOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {

					ComtracFormModel formModelForm = (ComtracFormModel) getForm().getDefaultModelObject();
					formModel = copyFromForm(formModelForm, formModel);

					if (formModel.getRobTrainings() != null) {
						robTrainingConfig = robTrainingConfigService
								.findById(formModel.getRobTrainings().getTrainingId());

						available = robTrainingConfig.getMaxPax() - robTrainingConfig.getCurrentPax();
//						
						String trainDt = df.format(robTrainingConfig.getTrainingStartDt());
						if (!robTrainingConfig.getTrainingEndDt().equals(robTrainingConfig.getTrainingStartDt())) {
							trainDt += " - " + df.format(robTrainingConfig.getTrainingEndDt());
						}

						String trainingStrtDate = df.format(robTrainingConfig.getTrainingStartDt());
						String trainingEndDate = df.format(robTrainingConfig.getTrainingEndDt());
						String regClosingDate = df.format(robTrainingConfig.getRegClosingDt());
						String stdFee = deciFormat.format(robTrainingConfig.getStandardFee());
						String splFee = deciFormat.format(robTrainingConfig.getSpecialFee());

						trainingCode.setDefaultModelObject(robTrainingConfig.getTrainingCode());
						trainingName.setDefaultModelObject(robTrainingConfig.getTrainingName());
						trainingDesc.setDefaultModelObject(robTrainingConfig.getTrainingDesc());
						trainingDt.setDefaultModelObject(trainDt);
						regClosingDt.setDefaultModelObject(regClosingDate);
						trainingStartTime.setDefaultModelObject(robTrainingConfig.getTrainingStartTime());
						trainingEndTime.setDefaultModelObject(robTrainingConfig.getTrainingEndTime());
						cpePoint.setDefaultModelObject(robTrainingConfig.getCpePoint());
						trainingVenue.setDefaultModelObject(robTrainingConfig.getTrainingVenue());
						standardFee.setDefaultModelObject(stdFee);
						specialFee.setDefaultModelObject(splFee);
						availableSeat.setDefaultModelObject(available);

						trainingInfo.setVisible(true);

						errorWmcDate.setVisible(false);
						errorWmcSeat.setVisible(false);
						showParticipantFormPanel.setEnabled(true);

						if (UserEnvironmentHelper.isInternalUser()) {
							if (formModel.getPaymentChannel() != null && formModel.getRegistrationChannel() != null) {
								showParticipantFormPanel.setEnabled(true);
							} else {
								showParticipantFormPanel.setEnabled(false);
							}
						} else {
							if (available > 0 && getZeroTimeDate(new Date())
									.before(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
								showParticipantFormPanel.setEnabled(true);
							} else {
								if (available <= 0) {
									errorWmcDate.setVisible(false);
									errorWmcSeat.setVisible(true);
								} else if (getZeroTimeDate(new Date())
										.after(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
									errorWmcDate.setVisible(true);
									errorWmcSeat.setVisible(false);
								}
								showParticipantFormPanel.setEnabled(false);
							}
						}

						target.add(errorWmcDate);
						target.add(errorWmcSeat);
						target.add(showParticipantFormPanel);
						target.add(trainingCode);
						target.add(trainingName);
						target.add(trainingInfo);
						target.add(trainingDesc);
						target.add(trainingDt);
						target.add(regClosingDt);
						target.add(trainingStartTime);
						target.add(trainingEndTime);
						target.add(cpePoint);
						target.add(trainingVenue);
						target.add(standardFee);
						target.add(specialFee);
						target.add(availableSeat);

						if (trainingTransaction.getTransactionCode() != null) {
							trainingTransaction = recalculate(formModel, robTrainingConfig, target);

							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobTrainingParticipant>) wmcParticipant
									.get("sortingParticipant")).getDataProvider();
							List<RobTrainingParticipant> listParticipant = trainingTransaction
									.getRobTrainingParticipantList();
							dpProvider.resetView(listParticipant);
						}

					} else {
						trainingInfo.setVisible(false);
						target.add(trainingInfo);
					}
				}
			};
			robTrainings.add(choicesOnchange);

			final ModalWindow addParticipantPopup = new ModalWindow("addParticipantPopup");
			addParticipantPopup.setWidthUnit("px");
			addParticipantPopup.setHeightUnit("px");
			addParticipantPopup.setInitialWidth(800);
			addParticipantPopup.setInitialHeight(650);

			trainingInfo.add(addParticipantPopup);

			addParticipantPopup.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {

					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobTrainingParticipant>) wmcParticipant
							.get("sortingParticipant")).getDataProvider();
					trainingTransaction = recalculate(formModel, robTrainingConfig, target);
					dpProvider.resetView(trainingTransaction.getRobTrainingParticipantList());

					target.add(wmcParticipant);
				}
			});

			final SSMSessionSortableDataProvider dpParticipant = new SSMSessionSortableDataProvider("",
					trainingTransaction.getRobTrainingParticipantList());
			final SSMDataView<RobTrainingParticipant> dataViewParticipant = new SSMDataView<RobTrainingParticipant>(
					"sortingParticipant", dpParticipant) {

				protected void populateItem(final Item<RobTrainingParticipant> item) {
					final RobTrainingParticipant participant = item.getModelObject();

					item.add(new SSMLabel("participantNo", item.getIndex() + 1));
					item.add(new SSMLabel("name", participant.getName()));
					item.add(new SSMLabel("icNo", participant.getIcNo()));
					item.add(new SSMLabel("amount", participant.getAmount()));

					SSMAjaxLink deleteParticipant = new SSMAjaxLink("deleteParticipant", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {

							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobTrainingParticipant>) wmcParticipant
									.get("sortingParticipant")).getDataProvider();
							List<RobTrainingParticipant> list = dpProvider.getListResult();
							list.remove(item.getIndex());
							trainingTransaction = recalculate(formModel, robTrainingConfig, target);

							RobTrainingConfig tempObject = robTrainingConfigService
									.findById(robTrainingConfig.getTrainingId());
							Integer currentpax = tempObject.getCurrentPax();
							tempObject.setCurrentPax(currentpax - 1);
							robTrainingConfigService.update(tempObject);

							dpParticipant.resetView(trainingTransaction.getRobTrainingParticipantList());
							target.add(wmcParticipant);
						}
					};

					SSMAjaxLink edit = new SSMAjaxLink("edit", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobTrainingParticipant>) wmcParticipant
									.get("sortingParticipant")).getDataProvider();
							List<RobTrainingParticipant> listParticipant = dpProvider.getListResult();
							getSession().setAttribute("listParticipant_", (Serializable) listParticipant);

							addParticipantPopup.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new AddTrainingParticipantForm(participant, robTrainingConfig,
											addParticipantPopup, item.getIndex());// edit record
								}
							});
							getSession().setAttribute("listParticipant_", (Serializable) listParticipant);
							addParticipantPopup.show(target);
						}
					};

					item.add(edit);

					item.add(deleteParticipant);
					deleteParticipant.setConfirmQuestion(resolve("page.confirm.deleteParticipant"));

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));

				}

			};
			wmcParticipant.add(dataViewParticipant);
			wmcParticipant.add(new SSMPagingNavigator("navigatorParticipant", dataViewParticipant));
			wmcParticipant.add(new NavigatorLabel("navigatorLabelParticipant", dataViewParticipant));

			showParticipantFormPanel = new SSMAjaxButton("showParticipantFormPanel") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobTrainingParticipant>) wmcParticipant
							.get("sortingParticipant")).getDataProvider();
					List<RobTrainingParticipant> listParticipant = dpProvider.getListResult();
					addParticipantPopup.setPageCreator(new ModalWindow.PageCreator() {
						@Override
						public Page createPage() {
							return new AddTrainingParticipantForm(null, robTrainingConfig, addParticipantPopup);// edit
																												// record
						}
					});

					getSession().setAttribute("listParticipant_", (Serializable) listParticipant);
					addParticipantPopup.show(target);

				}
			};
			showParticipantFormPanel.setDefaultFormProcessing(false);
			showParticipantFormPanel.setOutputMarkupId(true);
			wmcParticipant.add(showParticipantFormPanel);

			errorWmcDate.setVisible(false);
			errorWmcSeat.setVisible(false);
			showParticipantFormPanel.setEnabled(true);

			if (UserEnvironmentHelper.isInternalUser()) {
				if (formModel.getPaymentChannel() != null && formModel.getRegistrationChannel() != null) {
					showParticipantFormPanel.setEnabled(true);
				} else {
					showParticipantFormPanel.setEnabled(false);
				}
			} else {
				if (available > 0
						&& getZeroTimeDate(new Date()).before(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
					showParticipantFormPanel.setEnabled(true);
				} else {
					if (available <= 0) {
						errorWmcDate.setVisible(false);
						errorWmcSeat.setVisible(true);
					} else if (getZeroTimeDate(new Date())
							.after(getZeroTimeDate(robTrainingConfig.getRegClosingDt()))) {
						errorWmcDate.setVisible(true);
						errorWmcSeat.setVisible(false);
					}
					showParticipantFormPanel.setEnabled(false);
				}
			}

			SSMAjaxFormSubmitBehavior paymentChannelOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					ComtracFormModel formModelForm = (ComtracFormModel) getForm().getDefaultModelObject();
					formModel = copyFromForm(formModelForm, formModel);
					if (formModel.getPaymentChannel() != null && formModel.getRegistrationChannel() != null) {
						showParticipantFormPanel.setEnabled(true);
					} else {
						showParticipantFormPanel.setEnabled(false);
					}
					target.add(showParticipantFormPanel);
				}
			};
			paymentChannel.add(paymentChannelOnchange);

			SSMAjaxFormSubmitBehavior registrationChannelOnchange = new SSMAjaxFormSubmitBehavior("onchange", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					ComtracFormModel formModelForm = (ComtracFormModel) getForm().getDefaultModelObject();
					formModel = copyFromForm(formModelForm, formModel);
					if (formModel.getPaymentChannel() != null && formModel.getRegistrationChannel() != null) {
						showParticipantFormPanel.setEnabled(true);
					} else {
						showParticipantFormPanel.setEnabled(false);
					}
					target.add(showParticipantFormPanel);
				}
			};
			registrationChannel.add(registrationChannelOnchange);

			saveAsDraft = new SSMAjaxButton("saveAsDraft") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ComtracFormModel formModelForm = (ComtracFormModel) getForm().getDefaultModelObject();
					formModel = copyFromForm(formModelForm, formModel);
					formModel.getRobTrainingTransaction().setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_data_entry);

					trainingTransaction = recalculate(formModel, robTrainingConfig, target);
					String alert = generateNotyAlert(resolve("page.lbl.ezbiz.comtrac.successSaveDraft",
							trainingTransaction.getTransactionCode()), Parameter.ALERT_TYPE_success, target);

					setResponsePage(new TabComtracPage(alert, target));
				}
			};
			saveAsDraft.setOutputMarkupId(true);
			saveAsDraft.setVisible(false);
			buttonMarkup.add(saveAsDraft);
			wmcParticipant.add(buttonMarkup);

			submitPayment = new SSMAjaxButton("submitPayment") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					RobTrainingTransaction trainingTransaction = recalculate(formModel, robTrainingConfig, target);

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();

					for (RobTrainingParticipant participant : trainingTransaction.getRobTrainingParticipantList()) {
						LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
						if (Parameter.COMTRAC_FEE_TYPE_standard.equals(participant.getFeeType())) {
							paymentItem.setPaymentItem(trainingTransaction.getTrainingId().getTrainingCode() + "_"
									+ Parameter.COMTRAC_FEE_TYPE_standard);
						} else {
							paymentItem.setPaymentItem(trainingTransaction.getTrainingId().getTrainingCode() + "_"
									+ Parameter.COMTRAC_FEE_TYPE_special);
						}
						paymentItem.setQuantity(1);
						paymentItem.setPaymentDet(participant.getName() + " (" + participant.getIcNo() + ")");
						paymentItem.setAmount(participant.getAmount());
						paymentItem.setGstAmt(participant.getGstAmount());
						paymentItem.setGstCode(participant.getGstCode());
						listPaymentItems.add(paymentItem);
					}

					setResponsePage(new PaymentDetailPage(trainingTransaction.getTransactionCode(),
							RobTrainingTransactionService.class.getSimpleName(), trainingTransaction,
							listPaymentItems));

				}
			};
			submitPayment.setOutputMarkupId(true);
			buttonMarkup.add(submitPayment);
			wmcParticipant.add(buttonMarkup);

			submit = new SSMAjaxButton("submit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ComtracFormModel formModelForm = (ComtracFormModel) getForm().getDefaultModelObject();
					formModel = copyFromForm(formModelForm, formModel);
					formModel.getRobTrainingTransaction()
							.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);

					RobTrainingTransaction trainingTransaction = recalculate(formModel, robTrainingConfig, target);
					String alert = generateNotyAlert(
							resolve("page.lbl.ezbiz.comtrac.success", trainingTransaction.getTransactionCode()),
							Parameter.ALERT_TYPE_success, target);

					setResponsePage(new TabComtracPage(alert, target));
				}
			};
			submit.setOutputMarkupId(true);
			submit.setConfirmQuestion("page.lbl.ezbiz.comtrac.confirmToSubmit");
			buttonMarkup.add(submit);
			wmcParticipant.add(buttonMarkup);

			if (formModel.getRobTrainingTransaction() != null) {
				if (formModel.getRobTrainingTransaction().getRobTrainingParticipantList() != null
						&& formModel.getRobTrainingTransaction().getRobTrainingParticipantList().size() > 0) {
					submitPayment.setEnabled(true);
					submit.setEnabled(true);
//					saveAsDraft.setEnabled(true);
				} else {
					submitPayment.setEnabled(false);
					submit.setEnabled(false);
//					saveAsDraft.setEnabled(false);
				}
			}

			if (UserEnvironmentHelper.isInternalUser()) {
				submitPayment.setVisible(false);
//				saveAsDraft.setVisible(true);
				submit.setVisible(true);
			} else {
				submitPayment.setVisible(true);
//				saveAsDraft.setVisible(true);
				submit.setVisible(false);
			}

		}
	}

	public void resetTimer(AjaxRequestTarget target, RobTrainingTransaction robTrainingTransaction) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		String note = "";

		WebMarkupContainer wmcTimer = new WebMarkupContainer("wmcTimer");
		wmcTimer.setOutputMarkupId(true);
		wmcTimer.setOutputMarkupPlaceholderTag(true);
		wmcTimer.setVisible(false);

		if (robTrainingTransaction.getTransactionCode() != null) {
			Calendar calTrans = Calendar.getInstance();
			calTrans.setTime(robTrainingTransaction.getUpdateDt());
			calTrans.add(Calendar.MINUTE, 15);

			if (UserEnvironmentHelper.isInternalUser()) {
				note = "Please send your application before <b>" + dateFormat.format(calTrans.getTime())
						+ "</b> or system will automatically cancel your transaction and you will lose your seat(s).";
			} else {
				note = "Please make your payment before <b>" + dateFormat.format(calTrans.getTime())
						+ "</b> or system will automatically cancel your transaction and you will lose your seat(s).";
			}
			wmcTimer.setVisible(true);
		}

		SSMLabel warningNote = new SSMLabel("warningNote", note);
		warningNote.setEscapeModelStrings(false);
		wmcTimer.add(warningNote);

		if (target == null) {
			add(wmcTimer);
		} else {
			replace(wmcTimer);
			target.add(wmcTimer);
		}
	}

	protected RobTrainingTransaction recalculate(ComtracFormModel comtracModel, RobTrainingConfig robTrainingConfig,
			AjaxRequestTarget target) {
		RobTrainingTransaction robTrainingTransaction = comtracModel.getRobTrainingTransaction();
		List<RobTrainingParticipant> listParticipant = reUpdateListParticipant(robTrainingTransaction);
		String lodgerName = UserEnvironmentHelper.getLoginName();

		if (listParticipant.size() > 0) {
			submitPayment.setEnabled(true);
			submit.setEnabled(true);
//			saveAsDraft.setEnabled(true);
			target.add(submitPayment);
			target.add(submit);
		} else {
			submitPayment.setEnabled(false);
			submit.setEnabled(false);
//			saveAsDraft.setEnabled(false);
			target.add(submitPayment);
			target.add(submit);
		}
		LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(lodgerName);

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalGstAmount = BigDecimal.ZERO;
		for (RobTrainingParticipant participant : listParticipant) {
			if (Parameter.COMTRAC_FEE_TYPE_standard.equals(participant.getFeeType())) {
				participant.setAmount(robTrainingConfig.getStandardFee());
				participant.setGstAmount(robTrainingConfig.getStandardFeeGst());
			} else {
				participant.setAmount(robTrainingConfig.getSpecialFee());
				participant.setGstAmount(robTrainingConfig.getSpecialFeeGst());
			}
			totalAmount = totalAmount.add(new BigDecimal(participant.getAmount()));
			totalGstAmount = totalGstAmount.add(new BigDecimal(participant.getGstAmount()));
		}

		robTrainingTransaction.setTrainingId(robTrainingConfig);
		robTrainingTransaction.setRobTrainingParticipantList(listParticipant);
		robTrainingTransaction.setTotalPax(listParticipant.size());
		robTrainingTransaction.setAmount(totalAmount.doubleValue());
		robTrainingTransaction.setLodgerName(lodgerName);
		robTrainingTransaction.setGstAmount(totalGstAmount.doubleValue());

		if (UserEnvironmentHelper.isInternalUser()) {
			robTrainingTransaction.setPaymentChannel(comtracModel.getPaymentChannel());
			robTrainingTransaction.setRegistrationChannel(comtracModel.getRegistrationChannel());
			robTrainingTransaction.setReceiptNo(comtracModel.getReceiptNo());
			robTrainingTransaction.setInvoiceNo(comtracModel.getInvoiceNo());
			robTrainingTransaction.setLouLoaRefNo(comtracModel.getLouLoaRefNo());
			robTrainingTransaction.setRemarks(comtracModel.getRemarks());
			robTrainingTransaction.setLodgerId("SSM STAF");
		} else {
			robTrainingTransaction.setLodgerId(llpUserProfile.getIdNo());
			robTrainingTransaction.setPaymentChannel(Parameter.COMTRAC_PAYMENT_CHANNEL_ezbiz);
			robTrainingTransaction.setRegistrationChannel(Parameter.COMTRAC_REGISTRATION_CHANNEL_ezbiz);
		}

		robTrainingTransactionService.updateInsertAll(robTrainingTransaction);

		resetTimer(target, trainingTransaction);

		return robTrainingTransaction;

	}

	private List<RobTrainingParticipant> reUpdateListParticipant(RobTrainingTransaction robTrainingTransaction) {
		List<RobTrainingParticipant> listParticipant = (List<RobTrainingParticipant>) getSession()
				.getAttribute("listParticipant_");
		if (listParticipant == null) {
			getSession().setAttribute("listParticipant_",
					(Serializable) robTrainingTransaction.getRobTrainingParticipantList());
			listParticipant.addAll(robTrainingTransaction.getRobTrainingParticipantList());
		}

		if (robTrainingTransaction.getRobTrainingParticipantList() != null) {
			for (RobTrainingParticipant participantDb : robTrainingTransaction.getRobTrainingParticipantList()) {
				if (!listParticipant.contains(participantDb)) {
					listParticipant.add(participantDb);
				}
			}
		}

		return listParticipant;

	}

	public static Calendar getZeroTimeDate(Date fecha) {
		Date res = fecha;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(fecha);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	public ComtracFormModel copyFromForm(ComtracFormModel from, ComtracFormModel to) {

		to.setRobTrainings(from.getRobTrainings());
		to.setInvoiceNo(from.getInvoiceNo());
		to.setRemarks(from.getRemarks());
		to.setReceiptNo(from.getReceiptNo());
		to.setLouLoaRefNo(from.getLouLoaRefNo());
		to.setPaymentChannel(from.getPaymentChannel());
		to.setRegistrationChannel(from.getRegistrationChannel());

		return to;
	}

	public class ComtracFormModel implements Serializable {

		private RobTrainingConfig robTrainings;
		private RobTrainingTransaction robTrainingTransaction;
		private String trainingCode;
		private String trainingName;
		private String trainingDesc;
		private String trainingStartDt;
		private String trainingEndDt;
		private String regClosingDt;
		private String trainingStartTime;
		private String trainingEndTime;
		private Integer cpePoint;
		private String trainingVenue;
		private Double standardFee;
		private Double specialFee;
		private Integer availableSeat;

		private String receiptNo;
		private String invoiceNo;
		private String louLoaRefNo;
		private String paymentChannel;
		private String registrationChannel;
		private String remarks;

		public RobTrainingConfig getRobTrainings() {
			return robTrainings;
		}

		public void setRobTrainings(RobTrainingConfig robTrainings) {
			this.robTrainings = robTrainings;
		}

		public String getTrainingCode() {
			return trainingCode;
		}

		public void setTrainingCode(String trainingCode) {
			this.trainingCode = trainingCode;
		}

		public String getTrainingName() {
			return trainingName;
		}

		public void setTrainingName(String trainingName) {
			this.trainingName = trainingName;
		}

		public String getTrainingDesc() {
			return trainingDesc;
		}

		public void setTrainingDesc(String trainingDesc) {
			this.trainingDesc = trainingDesc;
		}

		public String getTrainingStartDt() {
			return trainingStartDt;
		}

		public void setTrainingStartDt(String trainingStartDt) {
			this.trainingStartDt = trainingStartDt;
		}

		public String getTrainingEndDt() {
			return trainingEndDt;
		}

		public void setTrainingEndDt(String date) {
			this.trainingEndDt = date;
		}

		public String getRegClosingDt() {
			return regClosingDt;
		}

		public void setRegClosingDt(String regClosingDt) {
			this.regClosingDt = regClosingDt;
		}

		public String getTrainingStartTime() {
			return trainingStartTime;
		}

		public void setTrainingStartTime(String trainingStartTime) {
			this.trainingStartTime = trainingStartTime;
		}

		public String getTrainingEndTime() {
			return trainingEndTime;
		}

		public void setTrainingEndTime(String trainingEndTime) {
			this.trainingEndTime = trainingEndTime;
		}

		public Integer getCpePoint() {
			return cpePoint;
		}

		public void setCpePoint(Integer cpePoint) {
			this.cpePoint = cpePoint;
		}

		public String getTrainingVenue() {
			return trainingVenue;
		}

		public void setTrainingVenue(String trainingVenue) {
			this.trainingVenue = trainingVenue;
		}

		public Double getStandardFee() {
			return standardFee;
		}

		public void setStandardFee(Double standardFee) {
			this.standardFee = standardFee;
		}

		public Double getSpecialFee() {
			return specialFee;
		}

		public void setSpecialFee(Double specialFee) {
			this.specialFee = specialFee;
		}

		public String getReceiptNo() {
			return receiptNo;
		}

		public void setReceiptNo(String receiptNo) {
			this.receiptNo = receiptNo;
		}

		public String getInvoiceNo() {
			return invoiceNo;
		}

		public void setInvoiceNo(String invoiceNo) {
			this.invoiceNo = invoiceNo;
		}

		public String getLouLoaRefNo() {
			return louLoaRefNo;
		}

		public void setLouLoaRefNo(String louLoaRefNo) {
			this.louLoaRefNo = louLoaRefNo;
		}

		public String getPaymentChannel() {
			return paymentChannel;
		}

		public void setPaymentChannel(String paymentChannel) {
			this.paymentChannel = paymentChannel;
		}

		public String getRegistrationChannel() {
			return registrationChannel;
		}

		public void setRegistrationChannel(String registrationChannel) {
			this.registrationChannel = registrationChannel;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public RobTrainingTransaction getRobTrainingTransaction() {
			return robTrainingTransaction;
		}

		public void setRobTrainingTransaction(RobTrainingTransaction robTrainingTransaction) {
			this.robTrainingTransaction = robTrainingTransaction;
		}

		public Integer getAvailableSeat() {
			return availableSeat;
		}

		public void setAvailableSeat(Integer availableSeat) {
			this.availableSeat = availableSeat;
		}
	}
}