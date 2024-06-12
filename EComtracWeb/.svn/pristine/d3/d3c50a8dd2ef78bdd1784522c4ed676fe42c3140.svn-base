package com.ssm.ezbiz.robformA;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.incentive.ListIncentiveVerification;
import com.ssm.ezbiz.mydata.MyDataRequest;
import com.ssm.ezbiz.otcPayment.ViewOtcPaymentPage.PaymentReceivedModel;
import com.ssm.ezbiz.payment.ViewPaymentTransactionPanel;
import com.ssm.ezbiz.robFormB.EditRobFormB1Panel;
import com.ssm.ezbiz.robFormB.EditRobFormBPage;
import com.ssm.ezbiz.service.PaymentService;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.EGHLRedirectPage;
import com.ssm.llp.base.page.MyDataRedirectPage;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.PaymentReceiptPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterSession;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ViewRobFormAPage2 extends SecBasePage implements Serializable{
	private static final long serialVersionUID = 1L;

	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;

	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "LlpPaymentReceiptService")
	private LlpPaymentReceiptService llpPaymentReceiptService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "LlpPaymentTransactionDetailService")
	private LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	public ViewRobFormAPage2(final String robFormARefNo, Page fromPage) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormA robFormA = robFormAService.findAllById(robFormARefNo);
				return robFormA;
			}
		}));
		init(fromPage);
	}

	private void init(Page fromPage) {
		add(new RobFormAForm("form", getDefaultModel(), fromPage));
	}

	private class RobFormAForm extends Form implements Serializable {

		final Page fromPage;
		final RobFormA robFormA;
		int bisnessInfoFeeQuantityInt = 0;
		double totalBisnessInfoFeeDouble = 0;

		double gstAmt = 0;

		double totalDiscount = 0;
		double totalRegDisc = 0;
		double totalBizInfoDiscount = 0;

		double regDiscount = 0;
		double bizInfoDiscount = 0;
		double gstAmountDiscount = 0;
		String gstCode = "";
		String regIncentive = "";
		String bizInfoIncentive = "";

		double totalRegFeeDouble = 0;
		double branchFeeDouble = 0;
		double branchFeePerYearDouble = 0;
		double totalBranchFeeDouble = 0;
		double totalFeeDouble = 0;
		double bisnessInfoFeeDouble = 0;
		double regFeePerYearDouble = 0;

		DecimalFormat df = new DecimalFormat("#0.00");
		DecimalFormat dfNegative = new DecimalFormat("-#0.00");

		LlpPaymentFee businessInfoPaymentFee = null;

		public RobFormAForm(String id, IModel m, Page fPage) {
			super(id, m);
			this.fromPage = fPage;
			robFormA = (RobFormA) m.getObject();
			final LlpUserProfile currentLlpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());

			add(new SSMLabel("robFormACode", robFormA.getRobFormACode()));
			add(new SSMLabel("nameType", robFormA.getNameType(), Parameter.ROB_NAME_TYPE));
			add(new SSMLabel("bizName", robFormA.getBizName()));
			add(new SSMLabel("bizStartDt", robFormA.getBizStartDt()));
			add(new SSMLabel("bizPartnershipAgreementDate", robFormA.getBizPartnershipAgreementDate()));
			add(new SSMLabel("bizRegPeriod", String.valueOf(robFormA.getBizRegPeriod()), Parameter.ROB_RENEWAL_YEAR));
			add(new SSMLabel("isBuyInfo", robFormA.isBuyInfo(), Parameter.YES_NO));
			add(new SSMLabel("status", robFormA.getStatus(), Parameter.ROB_FORM_A_STATUS));
			add(new SSMLabel("incubator", robFormA.isIncubator(), Parameter.YES_NO));
			add(new SSMLabel("incentiveType", robFormA.getIncentive(), Parameter.ROB_FORM_A_INCENTIVE));
			add(new SSMLabel("isOnlineSeller", robFormA.getIsOnlineSeller(), Parameter.YES_NO));
			
			

			SSMLabel brNo = new SSMLabel("brNo", robFormA.getBrNo());
			brNo.setVisible(false);
			add(brNo);
			if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(robFormA.getStatus())) {
				brNo.setVisible(true);
			}
			
			MultiLineLabel approveRejectNotes= new MultiLineLabel("approveRejectNotes", robFormA.getApproveRejectNotes());
			approveRejectNotes.setVisible(false);
			add(approveRejectNotes);
			if(Parameter.ROB_FORM_B_STATUS_REJECT.equals(robFormA.getStatus())) {
				approveRejectNotes.setVisible(true);
			}

			WebMarkupContainer wmcAddress = new WebMarkupContainer("wmcAddress");
			wmcAddress.setPrefixLabelKey("page.lbl.ezbiz.robFormA.");
			wmcAddress.setOutputMarkupId(true);
			add(wmcAddress);

			SSMLabel downloadRule = new SSMLabel("downloadRule", new StringResourceModel("page.title.mybiz.editNoteFormA", this, null));
			add(downloadRule);
			downloadRule.setVisible(false);

			String bizMainAddr = robFormA.getBizMainAddr();
			if (StringUtils.isNotBlank(robFormA.getBizMainAddr2())) {
				bizMainAddr += "\n" +robFormA.getBizMainAddr2() ;
			}
			if (StringUtils.isNotBlank(robFormA.getBizMainAddr3())) {
				bizMainAddr += "\n" + robFormA.getBizMainAddr3() ;
			}

			bizMainAddr += "\n" + robFormA.getBizMainAddrPostcode() + " " + robFormA.getBizMainAddrTown().toUpperCase();
			bizMainAddr += "\n" + getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormA.getBizMainAddrState());

			wmcAddress.add(new MultiLineLabel("bizMainAddr", bizMainAddr));
			wmcAddress.add(new SSMLabel("bizMainAddrTelNo", robFormA.getBizMainAddrTelNo()));
			wmcAddress.add(new SSMLabel("bizMainAddrMobileNo", robFormA.getBizMainAddrMobileNo()));
			wmcAddress.add(new SSMLabel("bizMainAddrEmail", robFormA.getBizMainAddrEmail()));
			wmcAddress.add(new SSMLabel("bizMainAddrUrl", robFormA.getBizMainAddrUrl()));

			String bizPostAddr = robFormA.getBizPostAddr();
			if (StringUtils.isNotBlank(robFormA.getBizPostAddr2())) {
				bizPostAddr +=  "\n"+ robFormA.getBizPostAddr2();
			}
			if (StringUtils.isNotBlank(robFormA.getBizPostAddr3())) {
				bizPostAddr += "\n"+robFormA.getBizPostAddr3() ;
			}

			bizPostAddr += "\n" + robFormA.getBizPostAddrPostcode() + " " + robFormA.getBizPostAddrTown().toUpperCase();
			bizPostAddr += "\n" + getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormA.getBizPostAddrState());

			wmcAddress.add(new MultiLineLabel("bizPostAddr", bizPostAddr));
			wmcAddress.add(new SSMLabel("bizPostAddrTelNo", robFormA.getBizPostAddrTelNo()));
			wmcAddress.add(new SSMLabel("bizPostAddrMobileNo", robFormA.getBizPostAddrMobileNo()));
			wmcAddress.add(new SSMLabel("bizPostAddrEmail", robFormA.getBizPostAddrEmail()));

			// Biz Desc
			WebMarkupContainer wmcBizCode = new WebMarkupContainer("wmcBizCode");
			wmcBizCode.setOutputMarkupId(true);
			wmcBizCode.setOutputMarkupPlaceholderTag(true);
			add(wmcBizCode);

			
			
			List listRobFormNotes = robFormNotesService.findByFormCode(robFormA.getRobFormACode());
			
			ListView<RobFormNotes> listQueryView = new ListView("listQueryView", listRobFormNotes) {
				@Override
				protected void populateItem(ListItem item) {
					RobFormNotes robormNotes = (RobFormNotes) item.getModelObject();
					item.add(new Label("queryBy", robormNotes.getQueryBy()));
					item.add(new MultiLineLabel("notes", robormNotes.getNotes()));
					item.add(new MultiLineLabel("notesAnswer", robormNotes.getNotesAnswer()));
					Date updateDt = null;
					if(robormNotes.getUpdateDt()!=null && !robormNotes.getUpdateDt().equals(robormNotes.getCreateDt())){
						updateDt = robormNotes.getUpdateDt();
					}
					item.add(new SSMLabel("createDt", robormNotes.getCreateDt(),"dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", updateDt,"dd/MM/yyyy hh:mm:ss a"));
				}
			    
			};
			add(listQueryView);
			listQueryView.setVisible(false);
			
			if(UserEnvironmentHelper.isInternalUser()&& listRobFormNotes.size()>0){
				listQueryView.setVisible(true);
			}
			

			wmcBizCode.add(new MultiLineLabel("bizDesc", robFormA.getBizDesc()));

			final SSMSessionSortableDataProvider dpBizCode = new SSMSessionSortableDataProvider("", robFormA.getListRobFormABizCode());
			final SSMDataView<RobFormABizCode> dataViewBizCode = new SSMDataView<RobFormABizCode>("sortingRobFormABizCode", dpBizCode) {

				protected void populateItem(final Item<RobFormABizCode> item) {
					RobFormABizCode robFormABizCode = item.getModelObject();

					item.add(new SSMLabel("bizCodeNo", item.getIndex() + 1));
					item.add(new SSMLabel("bizCode", robFormABizCode.getBizCode()));
					item.add(new MultiLineLabel("bizCodeDesc", robFormABizCode.getBizCodeDesc()));

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));

				}

			};
			wmcBizCode.add(dataViewBizCode);
			wmcBizCode.add(new SSMPagingNavigator("navigatorRobFormABizCode", dataViewBizCode));
			wmcBizCode.add(new NavigatorLabel("navigatorLabelRobFormABizCode", dataViewBizCode));

			// Branches
			WebMarkupContainer wmcBranches = new WebMarkupContainer("wmcBranches");
			wmcBranches.setOutputMarkupId(true);
			wmcBranches.setOutputMarkupPlaceholderTag(true);
			add(wmcBranches);

			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", robFormA.getListRobFormABranches());
			final SSMDataView<RobFormABranches> dataView = new SSMDataView<RobFormABranches>("sortingRobFormABranch", dp) {

				protected void populateItem(final Item<RobFormABranches> item) {
					RobFormABranches robFormABranches = item.getModelObject();

					item.add(new SSMLabel("branchNo", item.getIndex() + 1));
					String address = robFormABranches.getAddr();
					if (StringUtils.isNotBlank(robFormABranches.getAddr2())) {
						address += "\n" + robFormABranches.getAddr2();
					}
					if (StringUtils.isNotBlank(robFormABranches.getAddr3())) {
						address += "\n" + robFormABranches.getAddr3();
					}
					address += "\n" + robFormABranches.getAddrPostcode() + " " + robFormABranches.getAddrTown();
					address = address + "\n" + getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormABranches.getAddrState());
					
					if (StringUtils.isNotBlank(robFormABranches.getAddrUrl())) {
						address += "\n" + robFormABranches.getAddrUrl();
					}
					
					item.add(new MultiLineLabel("branchAddress", address));

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}

			};

			wmcBranches.add(dataView);
			wmcBranches.add(new SSMPagingNavigator("navigatorRobFormABranch", dataView));
			wmcBranches.add(new NavigatorLabel("navigatorLabelRobFormABranch", dataView));

			// ROB OWNER
			final WebMarkupContainer wmcOwners = new WebMarkupContainer("wmcOwners");
			wmcOwners.setOutputMarkupId(true);
			wmcOwners.setOutputMarkupPlaceholderTag(true);
			add(wmcOwners);

			final ModalWindow editOwnerPopUp = new ModalWindow("editOwnerPopUp");
			editOwnerPopUp.setHeightUnit("px");
			editOwnerPopUp.setInitialHeight(500);
			add(editOwnerPopUp);

			final SSMSessionSortableDataProvider dpOwner = new SSMSessionSortableDataProvider("", robFormA.getListRobFormAOwner());
			final SSMDataView<RobFormAOwner> dataViewOwner = new SSMDataView<RobFormAOwner>("sortingRobFormAOwner", dpOwner) {

				protected void populateItem(final Item<RobFormAOwner> item) {
					final RobFormAOwner robFormAOwner = item.getModelObject();

					item.add(new SSMLabel("ownerNo", item.getIndex() + 1));
					
					
					String ownerAddr = robFormAOwner.getAddr();
					if (StringUtils.isNotBlank(robFormAOwner.getAddr2())) {
						ownerAddr +=  "\n"+ robFormAOwner.getAddr2();
					}
					if (StringUtils.isNotBlank(robFormAOwner.getAddr3())) {
						ownerAddr += "\n"+robFormAOwner.getAddr3() ;
					}

					ownerAddr += "\n" + robFormAOwner.getAddrPostcode() + " " + robFormAOwner.getAddrTown().toUpperCase();
					ownerAddr += "\n" + getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormAOwner.getAddrState());
					item.add(new MultiLineLabel("name", robFormAOwner.getName()+"\n"+ownerAddr));
					
					item.add(new SSMLabel("icNo", robFormAOwner.getNewicno()));
					SSMLabel veriStatus = new SSMLabel("verificationStatus", robFormAOwner.getVerificationStatus(), Parameter.ROB_OWNER_VERI_STATUS);
					item.add(veriStatus);

					AjaxLink viewOwner = new AjaxLink("viewOwner", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {

							editOwnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new ViewRobFormAOwnerPanel(robFormAOwner, editOwnerPopUp);// edit
																										// record
								}
							});
							editOwnerPopUp.show(target);
						}
					};
					item.add(viewOwner);

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));

					String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.confirmAccept");
					SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>) wmcOwners
									.get("sortingRobFormAOwner")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();

							robFormAOwner.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
							robFormAOwnerService.update(robFormAOwner);
							robFormAService.sendEmailPartnerAccept(robFormA, robFormAOwner);

							dpProvider.resetView(listFormRobAOwners);

							target.add(wmcOwners);
						}
					};
					acceptOwners.setConfirmQuestion(confirmAcceptQuestion);
					acceptOwners.setVisible(false);
					item.add(acceptOwners);

					String rejectOwnerQuestion = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.confirmRemove");
					SSMAjaxLink rejectOwners = new SSMAjaxLink("rejectOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>) wmcOwners
									.get("sortingRobFormAOwner")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();

							listFormRobAOwners.remove(robFormAOwner);

							robFormAOwner.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_REJECT);
							robFormAService.sendEmailPartnerReject(robFormA, robFormAOwner);
							robFormAOwnerService.delete(robFormAOwner);

							dpProvider.resetView(listFormRobAOwners);

							target.add(wmcOwners);
							setResponsePage(new OwnersValidationListRobFormAPage());
						}
					};
					rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
					rejectOwners.setVisible(false);
					item.add(rejectOwners);

					if (!UserEnvironmentHelper.isInternalUser()) {
						if (Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(robFormA.getStatus())
								&& robFormAOwner.getNewicno().equals(currentLlpUserProfile.getIdNo())) {
							if (!Parameter.ROB_OWNER_VERI_STATUS_VERIFIED.equals(robFormAOwner.getVerificationStatus())) {
								acceptOwners.setVisible(true);
							}
							rejectOwners.setVisible(true);
						}
					}

					if (Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robFormAOwner.getVerificationStatus())) {
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
				}

			};

			wmcOwners.add(dataViewOwner);
			wmcOwners.add(new SSMPagingNavigator("navigatorRobFormAOwner", dataViewOwner));
			wmcOwners.add(new NavigatorLabel("navigatorLabelRobFormAOwner", dataViewOwner));

			// Others Info

			SSMDownloadLink downloadSupportingDoc = new SSMDownloadLink("downloadSupportingDoc");
			downloadSupportingDoc.setVisible(false);
			add(downloadSupportingDoc);

			if (Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())) {
				downloadSupportingDoc.setDownloadData("SUPPORTING.pdf", "application/pdf", robFormA.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
			}

			WebMarkupContainer wmcFeeSummary = new WebMarkupContainer("wmcFeeSummary");
			wmcFeeSummary.setOutputMarkupId(true);
			wmcFeeSummary.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummary);

			
			final ViewPaymentTransactionPanel b1Panel = new ViewPaymentTransactionPanel("viewPaymentTransactionPanel", robFormA.getRobFormACode());
			b1Panel.setOutputMarkupId(true);
			b1Panel.setOutputMarkupPlaceholderTag(true);
			wmcFeeSummary.add(b1Panel);
			
//			LlpPaymentFee branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
//			LlpPaymentFee formAPaymentFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
//			LlpPaymentFee formAPaymentFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
//			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
//			LlpPaymentFee incentivePersonalStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT);
//			LlpPaymentFee incentiveTradeStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT);
//			LlpPaymentFee incentivePersonalOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU);
//			LlpPaymentFee incentiveTradeOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_OKU);
//			LlpPaymentFee incentiveBizInfoStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT);
//			LlpPaymentFee incentiveBizInfoOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_OKU);

//			SSMLabel regFeePerYear = new SSMLabel("regFeePerYear", "");
//			wmcFeeSummary.add(regFeePerYear);
//			SSMLabel regFeeDuration = new SSMLabel("regFeeDuration", "");
//			wmcFeeSummary.add(regFeeDuration);
//
//			SSMLabel regFeeDiscount = new SSMLabel("regFeeDiscount", "");
//			regFeeDiscount.setOutputMarkupId(true);
//			regFeeDiscount.setVisible(false);
//			wmcFeeSummary.add(regFeeDiscount);
//			SSMLabel totalRegDiscount = new SSMLabel("totalRegDiscount", "");
//			wmcFeeSummary.add(totalRegDiscount);
//			SSMLabel regFeeDurationDiscount = new SSMLabel("regFeeDurationDiscount", "");
//			wmcFeeSummary.add(regFeeDurationDiscount);
//			SSMLabel incentiveTypeLabel = new SSMLabel("incentiveTypeLabel", "");
//			wmcFeeSummary.add(incentiveTypeLabel);
//
//			SSMLabel businessInfoDiscount = new SSMLabel("businessInfoDiscount", "");
//			businessInfoDiscount.setOutputMarkupId(true);
//			businessInfoDiscount.setVisible(false);
//			wmcFeeSummary.add(businessInfoDiscount);
//			SSMLabel totalBusinessInfoDiscount = new SSMLabel("totalBusinessInfoDiscount", "");
//			wmcFeeSummary.add(totalBusinessInfoDiscount);
//			SSMLabel businessInfoQuantityDiscount = new SSMLabel("businessInfoQuantityDiscount", "");
//			wmcFeeSummary.add(businessInfoQuantityDiscount);
//
//			SSMLabel totalRegFee = new SSMLabel("totalRegFee", "");
//			wmcFeeSummary.add(totalRegFee);
//
//			SSMLabel branchFee = new SSMLabel("branchFee", "");
//			SSMLabel branchFeeDuration = new SSMLabel("branchFeeDuration", "");
//			wmcFeeSummary.add(branchFeeDuration);
//			SSMLabel branchFeePerYear = new SSMLabel("branchFeePerYear", "");
//			wmcFeeSummary.add(branchFeePerYear);
//			SSMLabel totalBranchFee = new SSMLabel("totalBranchFee", "");
//			wmcFeeSummary.add(totalBranchFee);
//
//			SSMLabel bisnessInfoFee = new SSMLabel("bisnessInfoFee", "");
//			wmcFeeSummary.add(bisnessInfoFee);
//			SSMLabel bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity", "");
//			wmcFeeSummary.add(bisnessInfoFeeQuantity);
//			SSMLabel totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee", "");
//			wmcFeeSummary.add(totalBisnessInfoFee);
//
//			SSMLabel totalFee = new SSMLabel("totalFee", "");
//			wmcFeeSummary.add(totalFee);

//			regFeePerYearDouble = formAPaymentFeePersonal.getPaymentFee();
//
//			if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
//				regFeePerYearDouble = formAPaymentFeeTrade.getPaymentFee();
//			}
//
//			totalRegFeeDouble = regFeePerYearDouble * robFormA.getBizRegPeriod();
//			branchFeeDouble = branchPaymentFee.getPaymentFee();
//			branchFeePerYearDouble = branchFeeDouble * robFormA.getListRobFormABranches().size();
//			totalBranchFeeDouble = branchFeePerYearDouble * robFormA.getBizRegPeriod();
//
//			bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
//
//			if (robFormA.getIncentive() != null && !robFormA.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no)) {
//
//				// register discount
//				if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_student)) {
//					regDiscount = incentivePersonalStudent.getPaymentFee();
//					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT;
//					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT;
//					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
//						regDiscount = incentiveTradeStudent.getPaymentFee();
//						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT;
//					}
//
//					bizInfoDiscount = incentiveBizInfoStudent.getPaymentFee();
//					gstCode = incentiveBizInfoStudent.getGstCode();
//
//				} else if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)) {
//					regDiscount = incentivePersonalOku.getPaymentFee();
//					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU;
//					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_OKU;
//					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
//						regDiscount = incentiveTradeOku.getPaymentFee();
//						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_OKU;
//					}
//
//					bizInfoDiscount = incentiveBizInfoOku.getPaymentFee();
//					gstCode = incentiveBizInfoOku.getGstCode();
//				}
//
//				totalRegDisc = regDiscount;
//
//				// biz info discount
//				if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
//					bisnessInfoFeeQuantityInt = 1;
//					totalBizInfoDiscount = bizInfoDiscount;
//					if (Parameter.PAYMENT_GST_CODE_SR.equalsIgnoreCase(gstCode)) {
//						totalBizInfoDiscount += bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
//						gstAmountDiscount += (bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
//					}
//
//					if (totalBizInfoDiscount != 0) {
//						businessInfoDiscount.setVisible(true);
//					} else {
//						businessInfoDiscount.setVisible(false);
//					}
//				}
//
//				totalDiscount = totalRegDisc + totalBizInfoDiscount;
//
//				regFeeDiscount.setVisible(true);
//			} else {
//				regFeeDiscount.setVisible(false);
//			}
//
//			if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
//				bisnessInfoFeeQuantityInt = 1;
//				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
//				if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
//					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
//					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
//				}
//			}
//
//			totalFeeDouble = totalRegFeeDouble + totalBranchFeeDouble + totalBisnessInfoFeeDouble - totalDiscount;

//			incentiveTypeLabel.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_FORM_A_INCENTIVE, robFormA.getIncentive()));
//			regFeeDiscount.setDefaultModelObject(dfNegative.format(regDiscount));
//			regFeeDurationDiscount.setDefaultModelObject(1);
//			totalRegDiscount.setDefaultModelObject(dfNegative.format(totalRegDisc));
//
//			businessInfoDiscount.setDefaultModelObject(dfNegative.format(totalBizInfoDiscount));
//			totalBusinessInfoDiscount.setDefaultModelObject(dfNegative.format(totalBizInfoDiscount));
//			businessInfoQuantityDiscount.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
//
//			regFeePerYear.setDefaultModelObject(df.format(regFeePerYearDouble));
//			regFeeDuration.setDefaultModelObject(robFormA.getBizRegPeriod());
//			totalRegFee.setDefaultModelObject(df.format(totalRegFeeDouble));
//
//			branchFee.setDefaultModelObject(df.format(branchFeeDouble));
//			branchFeeDuration.setDefaultModelObject(robFormA.getBizRegPeriod());
//			branchFeePerYear.setDefaultModelObject(df.format(branchFeePerYearDouble));
//			totalBranchFee.setDefaultModelObject(df.format(totalBranchFeeDouble));
//
//			bisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
//			bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
//			totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));

//			totalFee.setDefaultModelObject(df.format(totalFeeDouble));

			// DOWNLOAD
			SSMDownloadLink downloadCert = new SSMDownloadLink("downloadCert");
			downloadCert.setVisible(false);
			add(downloadCert);

			SSMDownloadLink downloadBusinessInfo = new SSMDownloadLink("downloadBusinessInfo");
			downloadBusinessInfo.setVisible(false);
			add(downloadBusinessInfo);

			SSMDownloadLink downloadBorangAForm = new SSMDownloadLink("downloadBorangAForm");
			downloadBorangAForm.setVisible(false);
			add(downloadBorangAForm);

			SSMDownloadLink downloadCmpNotice = new SSMDownloadLink("downloadCmpNotice");
			downloadCmpNotice.setVisible(false);
			add(downloadCmpNotice);

			if (Parameter.ROB_FORM_A_STATUS_APPROVED.equals(robFormA.getStatus())) {
				
				try {
					RobFormA robFormATmp = robFormAService.findByIdWithData(robFormA.getRobFormACode());
					int downValidDays = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG,
							Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS));

					// downValidDays = downValidDays + 1;
					Calendar cal = Calendar.getInstance();
					Date generateCert = robFormATmp.getUpdateDt();

					cal.setTime(generateCert);
					cal.add(Calendar.DATE, downValidDays);

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
					String value = resolve("page.title.mybiz.editNoteFormA", String.valueOf(downValidDays), sdf.format(cal.getTime()));
					SSMLabel ssmLabel = new SSMLabel("downloadRule", value);
					replace(ssmLabel);
					ssmLabel.setVisible(true);
					
					
					boolean isAllowDownload = false;
					
					if (UserEnvironmentHelper.isInternalUser()) {
//						downloadCert.setVisible(true);
//						if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
//							downloadBusinessInfo.setVisible(true);
//						}
						isAllowDownload=true;
					} else {
						if (new Date().after(cal.getTime())) {//If after 14 days
//							downloadCert.setVisible(false);
//							downloadBusinessInfo.setVisible(false);
							isAllowDownload=false;
						}else {
							isAllowDownload=true;
						}
					}
					
					
					try {
						downloadBorangAForm.setDownloadData(robFormATmp.getBrNo() + "_BORANG_A.pdf", "application/pdf", robFormATmp.getFormAData()
								.getFileData());
						downloadBorangAForm.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(isAllowDownload) {
					
						try {
							downloadCert.setDownloadData(robFormATmp.getBrNo() + "_CERT.pdf", "application/pdf", robFormATmp.getCertFileData().getFileData());
							downloadCert.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
							try {
								downloadBusinessInfo.setDownloadData(robFormATmp.getBrNo() + "_BIS_INFO.pdf", "application/pdf", robFormATmp
										.getBusinessInfoData().getFileData());
								downloadBusinessInfo.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						if (StringUtils.isNotBlank(robFormA.getCompoundNo())) {
							try {
								downloadCmpNotice.setDownloadData(robFormATmp.getBrNo() + "_CMP_NOTICE.pdf", "application/pdf", robFormATmp
										.getCmpNoticeFileData().getFileData());
								downloadCmpNotice.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
				} catch (SSMException e) {
					e.printStackTrace();
				}
				
			}

			SSMAjaxLink approve = new SSMAjaxLink("approve") {
				@Override
				public void onClick(AjaxRequestTarget target) {

					if (totalFeeDouble == 0) {

						LlpPaymentTransaction llpPaymentTransaction = new LlpPaymentTransaction();
						LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
						if (profile != null) {
							llpPaymentTransaction.setPayerName(profile.getName());
							llpPaymentTransaction.setPayerId(profile.getIdNo());
							String address = profile.getAdd1();
							if (StringUtils.isNotBlank(profile.getAdd2())) {
								address += "\n" + profile.getAdd2();
							}
							if (StringUtils.isNotBlank(profile.getAdd3())) {
								address += "\n" + profile.getAdd3();
							}
							address += "\n" + profile.getPostcode() + " " + profile.getCity();
							address = address + "\n" + getCodeTypeWithValue(Parameter.STATE_CODE, profile.getState());
							llpPaymentTransaction.setPayerAddr(address);
						}
						llpPaymentTransaction.setAppRefNo(robFormA.getRobFormACode());
						llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_incentive);

						List<LlpPaymentTransactionDetail> listPaymentItems = generatePaymentDetail();
						try {
							llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName())).processPayment(
									llpPaymentTransaction, listPaymentItems);
						} catch (SSMException e) {
							e.printStackTrace();
						}

						llpPaymentReceiptService.receivePaymentGenerateReceiptForCash(llpPaymentTransaction.getTransactionId(), null, totalFeeDouble,
								totalFeeDouble);

						robFormAService.sendEmailFormAIncentiveVerifiedNoPayment(robFormA);
					} else {

						robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT);
						robFormAService.update(robFormA);

						robFormAService.sendEmailFormAIncentiveVerifiedPayment(robFormA);
					}

					setResponsePage(ListIncentiveVerification.class);
				}
			};
			approve.setOutputMarkupId(true);
			approve.setVisible(false);
			add(approve);

			final WebMarkupContainer wmcRemarks = new WebMarkupContainer("wmcRemarks");
			wmcRemarks.setOutputMarkupPlaceholderTag(true);
			wmcRemarks.setOutputMarkupId(true);
			wmcRemarks.setVisible(false);

			add(wmcRemarks);

			final SSMTextArea remarks = new SSMTextArea("remarks", Model.of(""));
			remarks.setOutputMarkupId(true);
			remarks.setOutputMarkupPlaceholderTag(true);
			wmcRemarks.add(remarks);

			final SSMLabel remarksType = new SSMLabel("remarksType", "");
			wmcRemarks.add(remarksType);

			final SSMAjaxButton resubmit = new SSMAjaxButton("resubmit") {
				@Override
				protected void onSubmit(AjaxRequestTarget request, Form form) {

					robFormAService.update(robFormA);

					if (Parameter.ROB_FORM_A_STATUS_REJECT.equals(robFormA.getStatus())) {
						robFormAService.sendEmailFormAIncentiveReject(robFormA);
					} else if (Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY.equals(robFormA.getStatus())) {
						RobFormNotes formNotes = new RobFormNotes();
						formNotes.setNotes(remarks.getDefaultModelObject().toString());
						formNotes.setRobFormCode(robFormA.getRobFormACode());
						formNotes.setRobFormType(Parameter.ROB_FORM_TYPE_A);
						robFormNotesService.insert(formNotes);

						robFormAService.sendEmailFormAIncentiveQuery(robFormA);
					}

					setResponsePage(ListIncentiveVerification.class);
				};
			};
			resubmit.setOutputMarkupId(true);
			resubmit.setOutputMarkupPlaceholderTag(true);
			wmcRemarks.add(resubmit);

			SSMAjaxLink reject = new SSMAjaxLink("reject") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_REJECT);
					wmcRemarks.setVisible(true);
					remarksType.setDefaultModelObject("Reject");
					target.add(wmcRemarks);
				}
			};
			reject.setOutputMarkupId(true);
			reject.setOutputMarkupPlaceholderTag(true);
			reject.setVisible(false);
			add(reject);

			SSMAjaxLink query = new SSMAjaxLink("query") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robFormA.setStatus(Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY);
					wmcRemarks.setVisible(true);
					remarksType.setDefaultModelObject("Query");
					target.add(wmcRemarks);

				}
			};
			query.setOutputMarkupId(true);
			query.setVisible(false);
			query.setOutputMarkupPlaceholderTag(true);
			add(query);

			SSMAjaxLink cancel = new SSMAjaxLink("cancel") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					setResponsePage(ListIncentiveVerification.class);
				}
			};
			cancel.setOutputMarkupId(true);
			cancel.setVisible(false);
			add(cancel);

			if (fPage instanceof ListIncentiveVerification) {
				approve.setVisible(true);
				cancel.setVisible(true);
				reject.setVisible(true);
				query.setVisible(true);
			}

			SSMAjaxButton submitPayment = new SSMAjaxButton("submitPayment") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if (robFormA.getIncentive() != null && !robFormA.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no)) {
						//recalculateForStudent(); //all incentive ie.STU OKU etc recalculate here!..totalFeeDouble.
						totalFeeDouble = recalculateForIncentive(); 
						
						System.out.println("totalFeeDouble_recalculateForIncentive: "+totalFeeDouble);
						if (totalFeeDouble == 0) { //if incentive STU, totalFee not 0 here..bizInfo OKU no incentive, pay RM10 (totalFeeDouble=10)..
							List<LlpPaymentTransactionDetail> listPaymentItems = generatePaymentDetail();
							setNoPaymentTransaction(listPaymentItems);
						} 
						
					}
					List<LlpPaymentTransactionDetail> listPaymentItems = generatePaymentDetail();
					
					setResponsePage(new PaymentDetailPage(robFormA.getRobFormACode(), RobFormAService.class.getSimpleName(), robFormA,
							listPaymentItems));
				}
			};
			submitPayment.setOutputMarkupId(true);
			if (UserEnvironmentHelper.isInternalUser()) {
				submitPayment.setVisible(false);
			} else {
				if (robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT)
						|| robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_OTC)) {
					submitPayment.setVisible(true);
				} else {
					submitPayment.setVisible(false);
				}
			}
			add(submitPayment);
			submitPayment.setOutputMarkupId(true);

			SSMAjaxButton submitTransaction = new SSMAjaxButton("submitTransaction") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					List<LlpPaymentTransactionDetail> listPaymentItems = generatePaymentDetail();

					setResponsePage(new PaymentDetailPage(robFormA.getRobFormACode(), RobFormAService.class.getSimpleName(), robFormA,
							listPaymentItems));
				}
			};
			add(submitTransaction);
			submitTransaction.setOutputMarkupId(true);
			
			SSMAjaxButton editBack = new SSMAjaxButton("editBack") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						robFormAService.updateFormBackToDraff(robFormA);
						setResponsePage(new EditRobFormAPage(robFormA.getRobFormACode()));
					} catch (SSMException e) {
						ssmError(e);
						String js = "alert('"+e.getMessage()+"');";
						target.appendJavaScript(js);
					}
					
				}
			};
			editBack.setVisible(false);
			add(editBack);
			
			
			
			SSMAjaxButton buyCTCCert = new SSMAjaxButton("buyCTCCert") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					String email = UserEnvironmentHelper.getUserenvironment().getEmail();
					String username = UserEnvironmentHelper.getLoginName();
					String entityNo = StringUtils.split(robFormA.getBrNo(),"-")[0];
					String checkDigit = StringUtils.split(robFormA.getBrNo(),"-")[1];
					String entityName = robFormA.getBizName();
//					String entityNo = "IP0300375";
//					String checkDigit = "H";
//					String entityName = "GEMILANG PASIR TRADING";
					
					setResponsePage(new MyDataRedirectPage(email,username,MyDataRequest.CTC_CERT,entityNo,checkDigit,entityName ));
				}
			};
			add(buyCTCCert);
			buyCTCCert.setOutputMarkupId(true);
			buyCTCCert.setVisible(false);
			
		
			
			if (UserEnvironmentHelper.isInternalUser()) {
				submitPayment.setVisible(false);
			} else {
				if (robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT)
						|| robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_OTC)) {
					submitPayment.setVisible(true);
					editBack.setVisible(true);
				} else {
					submitPayment.setVisible(false);
				}
			}
			
			if (robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS)) {
				submitTransaction.setVisible(true);
			} else {
				submitTransaction.setVisible(false);
			}
			
			if (robFormA.getStatus().equals(Parameter.ROB_FORM_A_STATUS_APPROVED)) {
				buyCTCCert.setVisible(true);
				if(!Parameter.YES_NO_yes.equals(getCodeTypeWithValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ENABLE_CTC_MYDATA) )) {
					buyCTCCert.setVisible(false);
				}
			} else {
				buyCTCCert.setVisible(false);
			}
			
			
		}

		public List<LlpPaymentTransactionDetail> generatePaymentDetail() {
			
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
			LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
			if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
				paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
			} else {// PERSONAL
				paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			}

			paymentItem.setQuantity(robFormA.getBizRegPeriod());
			paymentItem.setAmount(totalRegFeeDouble);
			paymentItem.setPaymentDet(robFormA.getBizName());
			listPaymentItems.add(paymentItem);

			if (robFormA.getBranchesAmt() > 0) {
				LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
				paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
				paymentItem2.setQuantity(robFormA.getBizRegPeriod());
				paymentItem2.setPaymentDet(robFormA.getListRobFormABranches().size() + " ");
				paymentItem2.setAmount(robFormA.getBranchesAmt());
				listPaymentItems.add(paymentItem2);
			}

			if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
				LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
				paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
				paymentItemBisInfo.setQuantity(1);
				paymentItemBisInfo.setPaymentDet("");
				paymentItemBisInfo.setAmount(totalBisnessInfoFeeDouble);
				paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
				if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
					paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee() * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}

				listPaymentItems.add(paymentItemBisInfo);
			}

			if (robFormA.getIncentive() != null && !robFormA.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no)) {
				if (regDiscount != 0) {
					LlpPaymentTransactionDetail paymentRegDisc = new LlpPaymentTransactionDetail();
					paymentRegDisc.setPaymentItem(regIncentive);
					paymentRegDisc.setQuantity(robFormA.getBizRegPeriod());
					paymentRegDisc.setAmount(Double.valueOf(dfNegative.format(regDiscount)));
					paymentRegDisc.setPaymentDet("REGISTRATION OF BUSINESS");
					listPaymentItems.add(paymentRegDisc);
				}

				if (totalBizInfoDiscount != 0) {	//bizInfo disc STU=10, OKU=0..
					LlpPaymentTransactionDetail bizInfoDisc = new LlpPaymentTransactionDetail();
					bizInfoDisc.setPaymentItem(bizInfoIncentive);
					bizInfoDisc.setQuantity(1);
					bizInfoDisc.setPaymentDet("BUSINESS INFO");
					bizInfoDisc.setAmount(Double.valueOf(dfNegative.format(totalBizInfoDiscount)));
					if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
						bizInfoDisc.setGstAmt(Double.valueOf(dfNegative.format(gstAmountDiscount)));
					}

					listPaymentItems.add(bizInfoDisc);
				}
			}
			
			//tambah utk fix gaf <null> pada kod gst (SR/OR)
			for (Iterator iterator = listPaymentItems.iterator(); iterator.hasNext();) {
				LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator.next();
				LlpPaymentFee fee = ((LlpPaymentFeeService) getService(LlpPaymentFeeService.class.getSimpleName()))
						.findById(llpPaymentTransactionDetail.getPaymentItem());
				
				llpPaymentTransactionDetail.setGstCode(fee.getGstCode());
			}

			return listPaymentItems;
		}

	/*	private void recalculateForStudent() {
			LlpPaymentFee branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
			LlpPaymentFee formAPaymentFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
			LlpPaymentFee formAPaymentFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			LlpPaymentFee incentivePersonalStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT);
			LlpPaymentFee incentiveTradeStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT);
			LlpPaymentFee incentivePersonalOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU);
			LlpPaymentFee incentiveTradeOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_OKU);
			LlpPaymentFee incentiveBizInfoStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT);
			LlpPaymentFee incentiveBizInfoOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_OKU);


			regFeePerYearDouble = formAPaymentFeePersonal.getPaymentFee();

			if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
				regFeePerYearDouble = formAPaymentFeeTrade.getPaymentFee();
			}

			totalRegFeeDouble = regFeePerYearDouble * robFormA.getBizRegPeriod();
			branchFeeDouble = branchPaymentFee.getPaymentFee();
			branchFeePerYearDouble = branchFeeDouble * robFormA.getListRobFormABranches().size();
			totalBranchFeeDouble = branchFeePerYearDouble * robFormA.getBizRegPeriod();

			bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();

			if (robFormA.getIncentive() != null && !robFormA.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no)) {

				// register discount
				if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_student)) {
					regDiscount = incentivePersonalStudent.getPaymentFee();
					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT;
					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT;
					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
						regDiscount = incentiveTradeStudent.getPaymentFee();
						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT;
					}

					bizInfoDiscount = incentiveBizInfoStudent.getPaymentFee();
					gstCode = incentiveBizInfoStudent.getGstCode();

				} else if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)) {
					regDiscount = incentivePersonalOku.getPaymentFee();
					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU;
					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_OKU;
					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
						regDiscount = incentiveTradeOku.getPaymentFee();
						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_OKU;
					}

					bizInfoDiscount = incentiveBizInfoOku.getPaymentFee();
					gstCode = incentiveBizInfoOku.getGstCode();
				}

				totalRegDisc = regDiscount;

				// biz info discount
				if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
					bisnessInfoFeeQuantityInt = 1;
					totalBizInfoDiscount = bizInfoDiscount;
					if (Parameter.PAYMENT_GST_CODE_SR.equalsIgnoreCase(gstCode)) {
						totalBizInfoDiscount += bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
						gstAmountDiscount += (bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
					}
				}

				totalDiscount = totalRegDisc + totalBizInfoDiscount;
			} 
			if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
				bisnessInfoFeeQuantityInt = 1;
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
			}

			totalFeeDouble = totalRegFeeDouble + totalBranchFeeDouble + totalBisnessInfoFeeDouble - totalDiscount;

		} */
		
		private double recalculateForIncentive() {
			LlpPaymentFee branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
			LlpPaymentFee formAPaymentFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
			LlpPaymentFee formAPaymentFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			LlpPaymentFee incentivePersonalStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT);
			LlpPaymentFee incentiveTradeStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT);
			LlpPaymentFee incentivePersonalOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU);
			LlpPaymentFee incentiveTradeOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_TRADE_OKU);
			LlpPaymentFee incentiveBizInfoStudent = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT);
			LlpPaymentFee incentiveBizInfoOku = llpPaymentFeeService.findById(Parameter.PAYMENT_INCENTIVE_BINFO_OKU);


			regFeePerYearDouble = formAPaymentFeePersonal.getPaymentFee();

			if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
				regFeePerYearDouble = formAPaymentFeeTrade.getPaymentFee();
			}

			totalRegFeeDouble = regFeePerYearDouble * robFormA.getBizRegPeriod();
			branchFeeDouble = branchPaymentFee.getPaymentFee();
			branchFeePerYearDouble = branchFeeDouble * robFormA.getListRobFormABranches().size();
			totalBranchFeeDouble = branchFeePerYearDouble * robFormA.getBizRegPeriod();

			bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();

			if (robFormA.getIncentive() != null && !robFormA.getIncentive().equalsIgnoreCase(Parameter.YES_NO_no)) {

				// register discount
				if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_student)) {
					regDiscount = incentivePersonalStudent.getPaymentFee();
					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_STUDENT;
					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_STUDENT;
					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
						regDiscount = incentiveTradeStudent.getPaymentFee();
						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_STUDENT;
					}

					bizInfoDiscount = incentiveBizInfoStudent.getPaymentFee();
					gstCode = incentiveBizInfoStudent.getGstCode();

				} else if (robFormA.getIncentive().equalsIgnoreCase(Parameter.ROB_FORM_A_INCENTIVE_oku)) {
					regDiscount = incentivePersonalOku.getPaymentFee();
					regIncentive = Parameter.PAYMENT_INCENTIVE_PERSONAL_OKU;
					bizInfoIncentive = Parameter.PAYMENT_INCENTIVE_BINFO_OKU;
					if (Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())) {
						regDiscount = incentiveTradeOku.getPaymentFee();
						regIncentive = Parameter.PAYMENT_INCENTIVE_TRADE_OKU;
					}

					bizInfoDiscount = incentiveBizInfoOku.getPaymentFee();
					gstCode = incentiveBizInfoOku.getGstCode();
				}

				totalRegDisc = regDiscount;

				// biz info discount
				if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
					bisnessInfoFeeQuantityInt = 1;
					totalBizInfoDiscount = bizInfoDiscount;
					if (Parameter.PAYMENT_GST_CODE_SR.equalsIgnoreCase(gstCode)) {
						totalBizInfoDiscount += bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
						gstAmountDiscount += (bizInfoDiscount * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
					}
				}

				totalDiscount = totalRegDisc + totalBizInfoDiscount;
			} 
			if (Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())) {
				bisnessInfoFeeQuantityInt = 1;
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if (Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())) {
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
			}

			return totalFeeDouble = totalRegFeeDouble + totalBranchFeeDouble + totalBisnessInfoFeeDouble - totalDiscount;

		}
		
		private void setNoPaymentTransaction(List<LlpPaymentTransactionDetail> listPaymentItems) {
			//set robPaymentTransaction and robPaymentTransactionDetail..
			
			LlpPaymentTransaction llpPaymentTransaction = new LlpPaymentTransaction();
			LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(robFormA.getCreateBy());
			if (profile != null) {
				llpPaymentTransaction.setPayerName(profile.getName());
				llpPaymentTransaction.setPayerId(profile.getIdNo());
				String address = profile.getAdd1();
				if (StringUtils.isNotBlank(profile.getAdd2())) {
					address += "\n" + profile.getAdd2();
				}
				if (StringUtils.isNotBlank(profile.getAdd3())) {
					address += "\n" + profile.getAdd3();
				}
				address += "\n" + profile.getPostcode() + " " + profile.getCity();
				address = address + "\n" + getCodeTypeWithValue(Parameter.STATE_CODE, profile.getState());
				llpPaymentTransaction.setPayerAddr(address);
			}
			llpPaymentTransaction.setAppRefNo(robFormA.getRobFormACode());
			llpPaymentTransaction.setPaymentMode(Parameter.PAYMENT_MODE_incentive);
			
			try {
				llpPaymentTransaction = ((PaymentService) getService(PaymentService.class.getSimpleName())).processPayment(
						llpPaymentTransaction, listPaymentItems);
			} catch (SSMException e) {
				e.printStackTrace();
			}

			llpPaymentReceiptService.receivePaymentGenerateReceiptForCash(llpPaymentTransaction.getTransactionId(), null, totalFeeDouble,
					totalFeeDouble);
			
		}
	}

}
