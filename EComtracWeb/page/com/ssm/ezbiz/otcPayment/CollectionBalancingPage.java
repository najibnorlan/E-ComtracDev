package com.ssm.ezbiz.otcPayment;

import java.awt.TextArea;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ViewOtcPaymentPage.PaymentReceivedModel;
import com.ssm.ezbiz.service.RobCounterBalancingService;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.HomePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SecondLevelLoginPage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterBalancing;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.mod1.page.ListPaymentTransactionPage;
import com.ssm.llp.mod1.page.ViewLlpPaymentTransDetailsPage;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;
import com.sun.xml.ws.api.Component;

@SuppressWarnings({ "all" })
public class CollectionBalancingPage extends SecBasePage {

	@SpringBean(name = "LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;

	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;

	@SpringBean(name = "RobCounterBalancingService")
	RobCounterBalancingService robCounterBalancingService;
	
	@SpringBean(name = "RobCounterSessionService")
	RobCounterSessionService robCounterSessionService;

	public CollectionBalancingPage() {
		super();
	}

	private void init() {
		add(new CollectionBalancingForm("form", getDefaultModel()));
	}

	Integer countCreditNote = 0;
	Label jsScript;
	SSMAjaxButton submit;
	
	public CollectionBalancingPage(Integer counterSessionId) {
		final RobCounterSession robCounterSession = (RobCounterSession) getService(
				RobCounterSessionService.class.getSimpleName()).findById(
				counterSessionId);

		Integer countTransactions = llpPaymentReceiptService
				.getCountTransactionByCounterSession(robCounterSession
						.getSessionId(), null);
		countCreditNote = llpPaymentReceiptService
				.getCountTransactionByCounterSession(
						robCounterSession.getSessionId(),
						Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
		final Double sumTransactions = llpPaymentReceiptService
				.getTotalTransactionByCounterSession(robCounterSession
						.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						BalancingFormModel balancingFormModel = new BalancingFormModel();
						DecimalFormat df = new DecimalFormat("#,###,##0.00");

						Double zero = Double.valueOf(df.format(Double
								.valueOf("0.00")));
						Double seratus = Double.valueOf(df.format(Double
								.valueOf("100")));
						Double limapuluh = Double.valueOf(df.format(Double
								.valueOf("50")));
						Double duapuluh = Double.valueOf(df.format(Double
								.valueOf("20")));
						Double sepuluh = Double.valueOf(df.format(Double
								.valueOf("10")));
						Double lima = Double.valueOf(df.format(Double
								.valueOf("5")));
						Double satu = Double.valueOf(df.format(Double
								.valueOf("1")));
						Double limapuluhsen = Double.valueOf(df.format(Double
								.valueOf("0.50")));
						Double duapuluhsen = Double.valueOf(df.format(Double
								.valueOf("0.20")));
						Double sepuluhsen = Double.valueOf(df.format(Double
								.valueOf("0.10")));
						Double limasen = Double.valueOf(df.format(Double
								.valueOf("0.05")));
						Double satusen = Double.valueOf(df.format(Double
								.valueOf("0.01")));

						balancingFormModel.setSeratus(seratus);
						balancingFormModel.setLimapuluh(limapuluh);
						balancingFormModel.setDuapuluh(duapuluh);
						balancingFormModel.setSepuluh(sepuluh);
						balancingFormModel.setLima(lima);
						balancingFormModel.setSatu(satu);
						balancingFormModel.setLimapuluhsen(limapuluhsen);
						balancingFormModel.setDuapuluhsen(duapuluhsen);
						balancingFormModel.setSepuluhsen(sepuluhsen);
						balancingFormModel.setLimasen(limasen);
						balancingFormModel.setSatusen(satusen);

						balancingFormModel.setSeratusT(zero);
						balancingFormModel.setLimapuluhT(zero);
						balancingFormModel.setDuapuluhT(zero);
						balancingFormModel.setSepuluhT(zero);
						balancingFormModel.setLimaT(zero);
						balancingFormModel.setSatuT(zero);
						balancingFormModel.setLimapuluhsenT(zero);
						balancingFormModel.setDuapuluhsenT(zero);
						balancingFormModel.setSepuluhsenT(zero);
						balancingFormModel.setLimasenT(zero);
						balancingFormModel.setSatusenT(zero);

						balancingFormModel.setTotalAmount(zero);

						balancingFormModel
								.setRobCounterSession(robCounterSession);
						balancingFormModel.setSumTransaction(sumTransactions);

						return balancingFormModel;
					}
				}));

		init();
		
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		add(new SSMLabel("countCreditNote", countCreditNote));
		add(new SSMLabel("countTransactions", countTransactions));
		add(new SSMLabel("userId", robCounterSession.getUserId()));
		add(new SSMLabel("counterName", robCounterSession.getCounterIpAddress()
				.getCounterName()));
		add(new SSMLabel("branch", robCounterSession.getBranch(), Parameter.BRANCH_CODE));
		add(new SSMLabel("floorLevel", robCounterSession.getFloorLevel(), Parameter.FLOOR_LVL));
		add(new SSMLabel("checkinDate", robCounterSession.getCheckinDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		add(new SSMLabel("balancingStatus", robCounterSession.getBalancingStatus(), Parameter.BALANCING_STATUS));
		add(new SSMLabel("checkoutDate", robCounterSession.getCheckoutDate(),
				"dd-MM-yyyy hh:mm:ss a"));
		
		final ModalWindow viewBalancingAmountDiv = new ModalWindow("viewBalancingAmountDiv");
		add(viewBalancingAmountDiv);
		
		viewBalancingAmountDiv.setCookieName("viewBalancingAmountCookies"+robCounterSession.getSessionId());
		viewBalancingAmountDiv.setResizable(true);

		viewBalancingAmountDiv.setPageCreator(new ModalWindow.PageCreator() {
			@Override
			public Page createPage() {
				return new SecondLevelLoginPage(CollectionBalancingPage.this.getPage(), viewBalancingAmountDiv,
						robCounterSession.getSessionId().toString());
			}
		});

		viewBalancingAmountDiv.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		add(new AjaxLink<Void>("viewTotalBalancing") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				viewBalancingAmountDiv.show(target);
			}
		});

	}

	private class CollectionBalancingForm extends Form implements Serializable {
		public CollectionBalancingForm(String id, IModel m) {
			super(id, m);
			final BalancingFormModel balancingFormModel = (BalancingFormModel) m.getObject();
			final RobCounterSession robCounterSession = balancingFormModel.getRobCounterSession();
			final DecimalFormat df = new DecimalFormat("#,###,##0.00");
			final SSMLabel totalAmount = new SSMLabel("totalAmount", balancingFormModel.getTotalAmount());
			final Double sumCreditNote = llpPaymentReceiptService
					.getTotalTransactionByCounterSession(robCounterSession
							.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_yes);
			
			totalAmount.setOutputMarkupId(true);
			add(totalAmount);
			
			jsScript = new Label("jsScript", "");
			jsScript.setEscapeModelStrings(false);
			jsScript.setOutputMarkupId(true);
			
			add(jsScript);
			
			add(new SSMLabel("sumCreditNote", df.format(sumCreditNote)));
			add(new SSMLabel("countCN", countCreditNote));
			
			final SSMLabel seratus = new SSMLabel("seratus", balancingFormModel.getSeratus());
			add(seratus);
			final SSMTextField seratusQ = new SSMTextField("seratusQ", Model.of(balancingFormModel.getSeratusQ()));
			add(seratusQ);
			final SSMLabel seratusT = new SSMLabel("seratusT", balancingFormModel.getSeratusT());
			seratusT.setOutputMarkupId(true);
			add(seratusT);
			
			final SSMLabel limapuluh = new SSMLabel("limapuluh", balancingFormModel.getLimapuluh());
			add(limapuluh);
			final SSMTextField limapuluhQ = new SSMTextField("limapuluhQ", Model.of(balancingFormModel.getLimapuluhQ()));
			add(limapuluhQ);
			final SSMLabel limapuluhT = new SSMLabel("limapuluhT", balancingFormModel.getLimapuluhT());
			limapuluhT.setOutputMarkupId(true);
			add(limapuluhT);
			
			final SSMLabel duapuluh = new SSMLabel("duapuluh", balancingFormModel.getDuapuluh());
			add(duapuluh);
			final SSMTextField duapuluhQ = new SSMTextField("duapuluhQ", Model.of(balancingFormModel.getDuapuluhQ()));
			add(duapuluhQ);
			final SSMLabel duapuluhT = new SSMLabel("duapuluhT", balancingFormModel.getDuapuluhT());
			duapuluhT.setOutputMarkupId(true);
			add(duapuluhT);
			
			final SSMLabel sepuluh = new SSMLabel("sepuluh", balancingFormModel.getSepuluh());
			add(sepuluh);
			final SSMTextField sepuluhQ = new SSMTextField("sepuluhQ", Model.of(balancingFormModel.getSepuluhQ()));
			add(sepuluhQ);
			final SSMLabel sepuluhT = new SSMLabel("sepuluhT", balancingFormModel.getSepuluhT());
			sepuluhT.setOutputMarkupId(true);
			add(sepuluhT);
			
			final SSMLabel lima = new SSMLabel("lima", balancingFormModel.getLima());
			add(lima);
			final SSMTextField limaQ = new SSMTextField("limaQ", Model.of(balancingFormModel.getLimaQ()));
			add(limaQ);
			final SSMLabel limaT = new SSMLabel("limaT", balancingFormModel.getLimaT());
			limaT.setOutputMarkupId(true);
			add(limaT);

			final SSMLabel satu = new SSMLabel("satu", balancingFormModel.getSatu());
			add(satu);
			final SSMTextField satuQ = new SSMTextField("satuQ", Model.of(balancingFormModel.getSatuQ()));
			add(satuQ);
			final SSMLabel satuT = new SSMLabel("satuT", balancingFormModel.getSatuT());
			satuT.setOutputMarkupId(true);
			add(satuT);
			
			final SSMLabel limapuluhsen = new SSMLabel("limapuluhsen", balancingFormModel.getLimapuluhsen());
			add(limapuluhsen);
			final SSMTextField limapuluhsenQ = new SSMTextField("limapuluhsenQ", Model.of(balancingFormModel.getLimapuluhsenQ()));
			add(limapuluhsenQ);
			final SSMLabel limapuluhsenT = new SSMLabel("limapuluhsenT", balancingFormModel.getLimapuluhsenT());
			limapuluhsenT.setOutputMarkupId(true);
			add(limapuluhsenT);
			
			final SSMLabel duapuluhsen = new SSMLabel("duapuluhsen", balancingFormModel.getDuapuluhsen());
			add(duapuluhsen);
			final SSMTextField duapuluhsenQ = new SSMTextField("duapuluhsenQ", Model.of(balancingFormModel.getDuapuluhsenQ()));
			add(duapuluhsenQ);
			final SSMLabel duapuluhsenT = new SSMLabel("duapuluhsenT", balancingFormModel.getDuapuluhsenT());
			duapuluhsenT.setOutputMarkupId(true);
			add(duapuluhsenT);
			
			final SSMLabel sepuluhsen = new SSMLabel("sepuluhsen", balancingFormModel.getSepuluhsen());
			add(sepuluhsen);
			final SSMTextField sepuluhsenQ = new SSMTextField("sepuluhsenQ", Model.of(balancingFormModel.getSepuluhsenQ()));
			add(sepuluhsenQ);
			final SSMLabel sepuluhsenT = new SSMLabel("sepuluhsenT", balancingFormModel.getSepuluhsenT());
			sepuluhsenT.setOutputMarkupId(true);
			add(sepuluhsenT);
			
			final SSMLabel limasen = new SSMLabel("limasen", balancingFormModel.getLimasen());
			add(limasen);
			final SSMTextField limasenQ = new SSMTextField("limasenQ", Model.of(balancingFormModel.getLimasenQ()));
			add(limasenQ);
			final SSMLabel limasenT = new SSMLabel("limasenT", balancingFormModel.getLimasenT());
			limasenT.setOutputMarkupId(true);
			add(limasenT);
			
			final SSMLabel satusen = new SSMLabel("satusen", balancingFormModel.getSatusen());
			add(satusen);
			final SSMTextField satusenQ = new SSMTextField("satusenQ", Model.of(balancingFormModel.getSatusenQ()));
			add(satusenQ);
			final SSMLabel satusenT = new SSMLabel("satusenT", balancingFormModel.getSatusenT());
			satusenT.setOutputMarkupId(true);
			add(satusenT);
			
			
			SSMAjaxFormSubmitBehavior seratusChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(seratusQ.getDefaultModelObject() != null){
						q = Integer.valueOf(seratusQ.getDefaultModelObject().toString());
					}
					
					if(seratus.getDefaultModelObject() != null){
						n = Double.valueOf(seratus.getDefaultModelObject().toString());
					}
					
					seratusQ.setDefaultModelObject(q);
					seratusT.setDefaultModelObject(df.format(q * n));
					
					target.add(seratusT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior limapuluhChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(limapuluhQ.getDefaultModelObject() != null){
						q = Integer.valueOf(limapuluhQ.getDefaultModelObject().toString());
					}
					
					if(limapuluh.getDefaultModelObject() != null){
						n = Double.valueOf(limapuluh.getDefaultModelObject().toString());
					}
					
					limapuluhQ.setDefaultModelObject(q);
					limapuluhT.setDefaultModelObject(df.format(q * n));
					
					target.add(limapuluhT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior duapuluhChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(duapuluhQ.getDefaultModelObject() != null){
						q = Integer.valueOf(duapuluhQ.getDefaultModelObject().toString());
					}
					
					if(duapuluh.getDefaultModelObject() != null){
						n = Double.valueOf(duapuluh.getDefaultModelObject().toString());
					}
					
					duapuluhQ.setDefaultModelObject(q);
					duapuluhT.setDefaultModelObject(df.format(q * n));
					
					target.add(duapuluhT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior sepuluhChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(sepuluhQ.getDefaultModelObject() != null){
						q = Integer.valueOf(sepuluhQ.getDefaultModelObject().toString());
					}
					
					if(sepuluh.getDefaultModelObject() != null){
						n = Double.valueOf(sepuluh.getDefaultModelObject().toString());
					}
					
					sepuluhQ.setDefaultModelObject(q);
					sepuluhT.setDefaultModelObject(df.format(q * n));
					
					target.add(sepuluhT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior limaChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(limaQ.getDefaultModelObject() != null){
						q = Integer.valueOf(limaQ.getDefaultModelObject().toString());
					}
					
					if(lima.getDefaultModelObject() != null){
						n = Double.valueOf(lima.getDefaultModelObject().toString());
					}
					
					limaQ.setDefaultModelObject(q);
					limaT.setDefaultModelObject(df.format(q * n));
					
					target.add(limaT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior satuChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(satuQ.getDefaultModelObject() != null){
						q = Integer.valueOf(satuQ.getDefaultModelObject().toString());
					}
					
					if(satu.getDefaultModelObject() != null){
						n = Double.valueOf(satu.getDefaultModelObject().toString());
					}
					
					satuQ.setDefaultModelObject(q);
					satuT.setDefaultModelObject(df.format(q * n));
					
					target.add(satuT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior limapuluhsenChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(limapuluhsenQ.getDefaultModelObject() != null){
						q = Integer.valueOf(limapuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(limapuluhsen.getDefaultModelObject() != null){
						n = Double.valueOf(limapuluhsen.getDefaultModelObject().toString());
					}
					
					limapuluhsenQ.setDefaultModelObject(q);
					limapuluhsenT.setDefaultModelObject(df.format(q * n));
					
					target.add(limapuluhsenT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior duapuluhsenChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(duapuluhsenQ.getDefaultModelObject() != null){
						q = Integer.valueOf(duapuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(duapuluhsen.getDefaultModelObject() != null){
						n = Double.valueOf(duapuluhsen.getDefaultModelObject().toString());
					}
					
					duapuluhsenQ.setDefaultModelObject(q);
					duapuluhsenT.setDefaultModelObject(df.format(q * n));
					
					target.add(duapuluhsenT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior sepuluhsenChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(sepuluhsenQ.getDefaultModelObject() != null){
						q = Integer.valueOf(sepuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(sepuluhsen.getDefaultModelObject() != null){
						n = Double.valueOf(sepuluhsen.getDefaultModelObject().toString());
					}
					
					sepuluhsenQ.setDefaultModelObject(q);
					sepuluhsenT.setDefaultModelObject(df.format(q * n));
					
					target.add(sepuluhsenT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior limasenChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(limasenQ.getDefaultModelObject() != null){
						q = Integer.valueOf(limasenQ.getDefaultModelObject().toString());
					}
					
					if(limasen.getDefaultModelObject() != null){
						n = Double.valueOf(limasen.getDefaultModelObject().toString());
					}
					
					limasenQ.setDefaultModelObject(q);
					limasenT.setDefaultModelObject(df.format(q * n));
					
					target.add(limasenT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			SSMAjaxFormSubmitBehavior satusenChange = new SSMAjaxFormSubmitBehavior("keyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					Integer q = 0;
					Double n = Double.valueOf(0);
					if(satusenQ.getDefaultModelObject() != null){
						q = Integer.valueOf(satusenQ.getDefaultModelObject().toString());
					}
					
					if(satusen.getDefaultModelObject() != null){
						n = Double.valueOf(satusen.getDefaultModelObject().toString());
					}
					
					satusenQ.setDefaultModelObject(q);
					satusenT.setDefaultModelObject(df.format(q * n));
					
					target.add(satusenT);
					recalculate(target, seratusQ, limapuluhQ, duapuluhQ, sepuluhQ, limaQ, satuQ, limapuluhsenQ, duapuluhsenQ, sepuluhsenQ, limasenQ, satusenQ, totalAmount);
					
				}
			};
			
			
			seratusQ.add(seratusChange);
			limapuluhQ.add(limapuluhChange);
			duapuluhQ.add(duapuluhChange);
			sepuluhQ.add(sepuluhChange);
			limaQ.add(limaChange);
			satuQ.add(satuChange);
			limapuluhsenQ.add(limapuluhsenChange);
			duapuluhsenQ.add(duapuluhsenChange);
			sepuluhsenQ.add(sepuluhsenChange);
			limasenQ.add(limasenChange);
			satusenQ.add(satusenChange);
			
			
			
			submit = new SSMAjaxButton("submit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					 Integer seratus = 0;
					 Integer limapuluh = 0;
					 Integer duapuluh = 0;
					 Integer sepuluh = 0;
					 Integer lima = 0;
					 Integer satu = 0;
					 Integer limapuluhsen = 0;
					 Integer duapuluhsen = 0;
					 Integer sepuluhsen = 0;
					 Integer limasen = 0;
					 Integer satusen =  0;
					
					if(seratusQ.getDefaultModelObject() != null){
						seratus = Integer.parseInt(seratusQ.getDefaultModelObject().toString());
					}
					
					if(limapuluhQ.getDefaultModelObject() != null){
						limapuluh = Integer.parseInt(limapuluhQ.getDefaultModelObject().toString());
					}
					
					if(duapuluhQ.getDefaultModelObject() != null){
						duapuluh = Integer.parseInt(duapuluhQ.getDefaultModelObject().toString());
					}
					
					if(sepuluhQ.getDefaultModelObject() != null){
						sepuluh = Integer.parseInt(sepuluhQ.getDefaultModelObject().toString());
					}
					
					if(limaQ.getDefaultModelObject() != null){
						lima = Integer.parseInt(limaQ.getDefaultModelObject().toString());
					}
					
					if(satuQ.getDefaultModelObject() != null){
						satu = Integer.parseInt(satuQ.getDefaultModelObject().toString());
					}
					
					if(limapuluhsenQ.getDefaultModelObject() != null){
						limapuluhsen = Integer.parseInt(limapuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(duapuluhsenQ.getDefaultModelObject() != null){
						duapuluhsen = Integer.parseInt(duapuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(sepuluhsenQ.getDefaultModelObject() != null){
						sepuluhsen = Integer.parseInt(sepuluhsenQ.getDefaultModelObject().toString());
					}
					
					if(limasenQ.getDefaultModelObject() != null){
						limasen = Integer.parseInt(limasenQ.getDefaultModelObject().toString());
					}
					
					if(satusenQ.getDefaultModelObject() != null){
						satusen = Integer.parseInt(satusenQ.getDefaultModelObject().toString());
					}
					
					 BigDecimal seratusT = BigDecimal.valueOf(100.00).multiply(new BigDecimal(seratus));
					 BigDecimal limapuluhT = BigDecimal.valueOf(50.00).multiply(new BigDecimal(limapuluh));
					 BigDecimal duapuluhT = BigDecimal.valueOf(20.00).multiply(new BigDecimal(duapuluh));
					 BigDecimal sepuluhT = BigDecimal.valueOf(10.00).multiply(new BigDecimal(sepuluh));
					 BigDecimal limaT = BigDecimal.valueOf(5.00).multiply(new BigDecimal(lima));
					 BigDecimal satuT = BigDecimal.valueOf(1.00).multiply(new BigDecimal(satu));
					 BigDecimal limapuluhsenT = BigDecimal.valueOf(0.5).multiply(new BigDecimal(limapuluhsen));
					 BigDecimal duapuluhsenT = BigDecimal.valueOf(0.2).multiply(new BigDecimal(duapuluhsen));
					 BigDecimal sepuluhsenT = BigDecimal.valueOf(0.1).multiply(new BigDecimal(sepuluhsen));
					 BigDecimal limasenT = BigDecimal.valueOf(0.05).multiply(new BigDecimal(limasen));
					 BigDecimal satusenT = BigDecimal.valueOf(0.01).multiply(new BigDecimal(satusen));
					
					 BigDecimal total = seratusT.add(limapuluhT).add(sepuluhT).add(duapuluhT).add(limaT).add(satuT).add(limapuluhsenT).add(duapuluhsenT).add(sepuluhsenT).add(limasenT).add(satusenT);
					 
					 System.out.println("sum transaction : " + balancingFormModel.getSumTransaction() + " | " + "balancing total : " + total.doubleValue());
					if(!balancingFormModel.getSumTransaction().equals(total.doubleValue())){
						ssmError(SSMExceptionParam.OTC_PAYMENT_BALANCING_NOT_MATCH);
						String js = "$(\"html, body\").animate({ scrollTop: 0 }, 600);";
						jsScript.setDefaultModelObject(js);
						
						target.add(jsScript);
						
					}else{
							jsScript.setDefaultModelObject("");
							
							
							List<RobCounterBalancing> listBalancing = new ArrayList<RobCounterBalancing>();
							
							RobCounterBalancing balance100 = new RobCounterBalancing();
							balance100.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance100.setBankNote(100.00);
							balance100.setAmount(seratusT.doubleValue());
							balance100.setQuantity(seratus);
							listBalancing.add(balance100);
							
							RobCounterBalancing balance50 = new RobCounterBalancing();
							balance50.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance50.setBankNote(50.00);
							balance50.setAmount(limapuluhT.doubleValue());
							balance50.setQuantity(limapuluh);
							listBalancing.add(balance50);
						
							RobCounterBalancing balance20 = new RobCounterBalancing();
							balance20.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance20.setBankNote(20.00);
							balance20.setAmount(duapuluhT.doubleValue());
							balance20.setQuantity(duapuluh);
							listBalancing.add(balance20);
							
							RobCounterBalancing balance10 = new RobCounterBalancing();
							balance10.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance10.setBankNote(10.00);
							balance10.setAmount(sepuluhT.doubleValue());
							balance10.setQuantity(sepuluh);
							listBalancing.add(balance10);
							
							RobCounterBalancing balance5 = new RobCounterBalancing();
							balance5.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance5.setBankNote(5.00);
							balance5.setAmount(limaT.doubleValue());
							balance5.setQuantity(lima);
							listBalancing.add(balance5);
							
							RobCounterBalancing balance1 = new RobCounterBalancing();
							balance1.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance1.setBankNote(1.00);
							balance1.setAmount(satuT.doubleValue());
							balance1.setQuantity(satu);
							listBalancing.add(balance1);
							
							RobCounterBalancing balance05 = new RobCounterBalancing();
							balance05.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance05.setBankNote(0.50);
							balance05.setAmount(limapuluhsenT.doubleValue());
							balance05.setQuantity(limapuluhsen);
							listBalancing.add(balance05);
							
							RobCounterBalancing balance02 = new RobCounterBalancing();
							balance02.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance02.setBankNote(0.20);
							balance02.setAmount(duapuluhsenT.doubleValue());
							balance02.setQuantity(duapuluhsen);
							listBalancing.add(balance02);
							
							RobCounterBalancing balance01 = new RobCounterBalancing();
							balance01.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance01.setBankNote(0.10);
							balance01.setAmount(sepuluhsenT.doubleValue());
							balance01.setQuantity(sepuluhsen);
							listBalancing.add(balance01);
							
							RobCounterBalancing balance005 = new RobCounterBalancing();
							balance005.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance005.setBankNote(0.05);
							balance005.setAmount(limasenT.doubleValue());
							balance005.setQuantity(limasen);
							listBalancing.add(balance005);
							
							RobCounterBalancing balance001 = new RobCounterBalancing();
							balance001.setCounterSessionId(balancingFormModel.getRobCounterSession());
							balance001.setBankNote(0.01);
							balance001.setAmount(satusenT.doubleValue());
							balance001.setQuantity(satusen);
							listBalancing.add(balance001);
							
							
							robCounterBalancingService.generateBalancingSlip(listBalancing,robCounterSession);
							
							setResponsePage(new CollectionBalancingSlip(robCounterSession.getSessionId()));
					}
					
					FeedbackPanel feedbackPanel =  ((CollectionBalancingPage)getPage()).getFeedbackPanel();
		        	feedbackPanel.getFeedbackMessages().clear();
		        	target.add(feedbackPanel);
				}
			};
			add(submit);
			
			AjaxEventBehavior submitOnclick = new AjaxEventBehavior("onclick") {
			    @Override
			    protected void onEvent(AjaxRequestTarget target) {
			    	submit.setEnabled(false);
					target.add(submit);
			   }
			};
			submit.add(submitOnclick);
			
		}
	}

	public void recalculate(AjaxRequestTarget target, SSMTextField seratusQ, SSMTextField limapuluhQ, SSMTextField duapuluhQ, 
			SSMTextField sepuluhQ, SSMTextField limaQ, SSMTextField satuQ, SSMTextField limapuluhsenQ, SSMTextField duapuluhsenQ, 
			SSMTextField sepuluhsenQ, SSMTextField limasenQ, SSMTextField satusenQ, SSMLabel totalAmount) {
		
		 submit.setEnabled(true);
		 Integer seratus = 0;
		 Integer limapuluh = 0;
		 Integer duapuluh = 0;
		 Integer sepuluh = 0;
		 Integer lima = 0;
		 Integer satu = 0;
		 Integer limapuluhsen = 0;
		 Integer duapuluhsen = 0;
		 Integer sepuluhsen = 0;
		 Integer limasen = 0;
		 Integer satusen =  0;
		 
		 final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		
		if(seratusQ.getDefaultModelObject() != null){
			seratus = Integer.parseInt(seratusQ.getDefaultModelObject().toString());
		}
		
		if(limapuluhQ.getDefaultModelObject() != null){
			limapuluh = Integer.parseInt(limapuluhQ.getDefaultModelObject().toString());
		}
		
		if(duapuluhQ.getDefaultModelObject() != null){
			duapuluh = Integer.parseInt(duapuluhQ.getDefaultModelObject().toString());
		}
		
		if(sepuluhQ.getDefaultModelObject() != null){
			sepuluh = Integer.parseInt(sepuluhQ.getDefaultModelObject().toString());
		}
		
		if(limaQ.getDefaultModelObject() != null){
			lima = Integer.parseInt(limaQ.getDefaultModelObject().toString());
		}
		
		if(satuQ.getDefaultModelObject() != null){
			satu = Integer.parseInt(satuQ.getDefaultModelObject().toString());
		}
		
		if(limapuluhsenQ.getDefaultModelObject() != null){
			limapuluhsen = Integer.parseInt(limapuluhsenQ.getDefaultModelObject().toString());
		}
		
		if(duapuluhsenQ.getDefaultModelObject() != null){
			duapuluhsen = Integer.parseInt(duapuluhsenQ.getDefaultModelObject().toString());
		}
		
		if(sepuluhsenQ.getDefaultModelObject() != null){
			sepuluhsen = Integer.parseInt(sepuluhsenQ.getDefaultModelObject().toString());
		}
		
		if(limasenQ.getDefaultModelObject() != null){
			limasen = Integer.parseInt(limasenQ.getDefaultModelObject().toString());
		}
		
		if(satusenQ.getDefaultModelObject() != null){
			satusen = Integer.parseInt(satusenQ.getDefaultModelObject().toString());
		}
		
		 Double seratusT = 100.00 * seratus;
		 Double limapuluhT = 50.00 * limapuluh;
		 Double duapuluhT = 20.00 * duapuluh;
		 Double sepuluhT = 10.00 * sepuluh;
		 Double limaT = 5.00 * lima;
		 Double satuT = 1.00 * satu;
		 Double limapuluhsenT = 0.5 * limapuluhsen;
		 Double duapuluhsenT = 0.2 * duapuluhsen;
		 Double sepuluhsenT = 0.1 * sepuluhsen;
		 Double limasenT = 0.05 * limasen;
		 Double satusenT = 0.01 * satusen;
		 
		 Double total = seratusT + limapuluhT + sepuluhT + duapuluhT + limaT + satuT + limapuluhsenT + duapuluhsenT + sepuluhsenT + limasenT + satusenT;
		 System.out.println(df.format(total));
		totalAmount.setDefaultModelObject(df.format(total));
		target.add(totalAmount);
		target.add(submit);
	}

	private class BalancingFormModel implements Serializable {
		private Double seratus;
		private Double limapuluh;
		private Double duapuluh;
		private Double sepuluh;
		private Double lima;
		private Double satu;
		private Double limapuluhsen;
		private Double duapuluhsen;
		private Double sepuluhsen;
		private Double limasen;
		private Double satusen;

		private Integer seratusQ;
		private Integer limapuluhQ;
		private Integer duapuluhQ;
		private Integer sepuluhQ;
		private Integer limaQ;
		private Integer satuQ;
		private Integer limapuluhsenQ;
		private Integer duapuluhsenQ;
		private Integer sepuluhsenQ;
		private Integer limasenQ;
		private Integer satusenQ;

		private Double seratusT;
		private Double limapuluhT;
		private Double duapuluhT;
		private Double sepuluhT;
		private Double limaT;
		private Double satuT;
		private Double limapuluhsenT;
		private Double duapuluhsenT;
		private Double sepuluhsenT;
		private Double limasenT;
		private Double satusenT;

		private Double totalAmount;

		private RobCounterSession robCounterSession;
		private Double sumTransaction;

		public Double getSeratus() {
			return seratus;
		}

		public void setSeratus(Double seratus) {
			this.seratus = seratus;
		}

		public Double getLimapuluh() {
			return limapuluh;
		}

		public void setLimapuluh(Double limapuluh) {
			this.limapuluh = limapuluh;
		}

		public Double getDuapuluh() {
			return duapuluh;
		}

		public void setDuapuluh(Double duapuluh) {
			this.duapuluh = duapuluh;
		}

		public Double getSepuluh() {
			return sepuluh;
		}

		public void setSepuluh(Double sepuluh) {
			this.sepuluh = sepuluh;
		}

		public Double getLima() {
			return lima;
		}

		public void setLima(Double lima) {
			this.lima = lima;
		}

		public Double getSatu() {
			return satu;
		}

		public void setSatu(Double satu) {
			this.satu = satu;
		}

		public Double getLimapuluhsen() {
			return limapuluhsen;
		}

		public void setLimapuluhsen(Double limapuluhsen) {
			this.limapuluhsen = limapuluhsen;
		}

		public Double getDuapuluhsen() {
			return duapuluhsen;
		}

		public void setDuapuluhsen(Double duapuluhsen) {
			this.duapuluhsen = duapuluhsen;
		}

		public Double getSepuluhsen() {
			return sepuluhsen;
		}

		public void setSepuluhsen(Double sepuluhsen) {
			this.sepuluhsen = sepuluhsen;
		}

		public Double getLimasen() {
			return limasen;
		}

		public void setLimasen(Double limasen) {
			this.limasen = limasen;
		}

		public Double getSatusen() {
			return satusen;
		}

		public void setSatusen(Double satusen) {
			this.satusen = satusen;
		}

		public Integer getSeratusQ() {
			return seratusQ;
		}

		public void setSeratusQ(Integer seratusQ) {
			this.seratusQ = seratusQ;
		}

		public Integer getLimapuluhQ() {
			return limapuluhQ;
		}

		public void setLimapuluhQ(Integer limapuluhQ) {
			this.limapuluhQ = limapuluhQ;
		}

		public Integer getDuapuluhQ() {
			return duapuluhQ;
		}

		public void setDuapuluhQ(Integer duapuluhQ) {
			this.duapuluhQ = duapuluhQ;
		}

		public Integer getSepuluhQ() {
			return sepuluhQ;
		}

		public void setSepuluhQ(Integer sepuluhQ) {
			this.sepuluhQ = sepuluhQ;
		}

		public Integer getLimaQ() {
			return limaQ;
		}

		public void setLimaQ(Integer limaQ) {
			this.limaQ = limaQ;
		}

		public Integer getSatuQ() {
			return satuQ;
		}

		public void setSatuQ(Integer satuQ) {
			this.satuQ = satuQ;
		}

		public Integer getLimapuluhsenQ() {
			return limapuluhsenQ;
		}

		public void setLimapuluhsenQ(Integer limapuluhsenQ) {
			this.limapuluhsenQ = limapuluhsenQ;
		}

		public Integer getDuapuluhsenQ() {
			return duapuluhsenQ;
		}

		public void setDuapuluhsenQ(Integer duapuluhsenQ) {
			this.duapuluhsenQ = duapuluhsenQ;
		}

		public Integer getSepuluhsenQ() {
			return sepuluhsenQ;
		}

		public void setSepuluhsenQ(Integer sepuluhsenQ) {
			this.sepuluhsenQ = sepuluhsenQ;
		}

		public Integer getLimasenQ() {
			return limasenQ;
		}

		public void setLimasenQ(Integer limasenQ) {
			this.limasenQ = limasenQ;
		}

		public Integer getSatusenQ() {
			return satusenQ;
		}

		public void setSatusenQ(Integer satusenQ) {
			this.satusenQ = satusenQ;
		}

		public Double getSeratusT() {
			return seratusT;
		}

		public void setSeratusT(Double seratusT) {
			this.seratusT = seratusT;
		}

		public Double getLimapuluhT() {
			return limapuluhT;
		}

		public void setLimapuluhT(Double limapuluhT) {
			this.limapuluhT = limapuluhT;
		}

		public Double getDuapuluhT() {
			return duapuluhT;
		}

		public void setDuapuluhT(Double duapuluhT) {
			this.duapuluhT = duapuluhT;
		}

		public Double getSepuluhT() {
			return sepuluhT;
		}

		public void setSepuluhT(Double sepuluhT) {
			this.sepuluhT = sepuluhT;
		}

		public Double getLimaT() {
			return limaT;
		}

		public void setLimaT(Double limaT) {
			this.limaT = limaT;
		}

		public Double getSatuT() {
			return satuT;
		}

		public void setSatuT(Double satuT) {
			this.satuT = satuT;
		}

		public Double getLimapuluhsenT() {
			return limapuluhsenT;
		}

		public void setLimapuluhsenT(Double limapuluhsenT) {
			this.limapuluhsenT = limapuluhsenT;
		}

		public Double getDuapuluhsenT() {
			return duapuluhsenT;
		}

		public void setDuapuluhsenT(Double duapuluhsenT) {
			this.duapuluhsenT = duapuluhsenT;
		}

		public Double getSepuluhsenT() {
			return sepuluhsenT;
		}

		public void setSepuluhsenT(Double sepuluhsenT) {
			this.sepuluhsenT = sepuluhsenT;
		}

		public Double getLimasenT() {
			return limasenT;
		}

		public void setLimasenT(Double limasenT) {
			this.limasenT = limasenT;
		}

		public Double getSatusenT() {
			return satusenT;
		}

		public void setSatusenT(Double satusenT) {
			this.satusenT = satusenT;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public RobCounterSession getRobCounterSession() {
			return robCounterSession;
		}

		public void setRobCounterSession(RobCounterSession robCounterSession) {
			this.robCounterSession = robCounterSession;
		}

		public Double getSumTransaction() {
			return sumTransaction;
		}

		public void setSumTransaction(Double sumTransaction) {
			this.sumTransaction = sumTransaction;
		}

	}

}
