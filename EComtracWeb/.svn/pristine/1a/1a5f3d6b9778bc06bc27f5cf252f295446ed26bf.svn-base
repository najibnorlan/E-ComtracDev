package com.ssm.ezbiz.robformA;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ViewRobFormAPage extends SecBasePage {

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "RobFormAService")
	private RobFormAService robFormAService;

	@SpringBean(name = "RobFormAOwnerService")
	private RobFormAOwnerService robFormAOwnerService;

	
	public ViewRobFormAPage(final String robFormARefNo, Page fromPage) {
		
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
		final WebMarkupContainer wmcAddress;
		final WebMarkupContainer wmcBranches;
		final WebMarkupContainer wmcOwners;
		final WebMarkupContainer wmcBizCodeAll;
		final WebMarkupContainer wmcFeeSummaryAll;
		final SSMLabel incentiveTypeLabel;
		final SSMLabel regFeeDiscount;
		final SSMLabel totalRegDiscount;
		final SSMLabel regFeeDurationDiscount;
		final SSMLabel regFeePerYear;
		final SSMLabel regFeeDuration;
		final SSMLabel totalRegFee;
		final SSMLabel branchFee;
		final SSMLabel branchFeeDuration;
		final SSMLabel branchFeePerYear;
		final SSMLabel totalBranchFee;
		final SSMLabel totalFee;
		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		final SSMLabel summaryError;
		final SSMTextField robFormACode;
		final RepeatingView listError;
		int currentShowId = 0;
		final RobFormA robFormA;
		final SSMTextField bizName;
		final LlpUserProfile currentLlpUserProfile;
		final Page fromPage;
		final LlpPaymentFee branchPaymentFee;
		final LlpPaymentFee formAPaymentFeeTrade;
		final LlpPaymentFee formAPaymentFeePersonal;
		final LlpPaymentFee businessInfoPaymentFee;
		public RobFormAForm(String id, IModel m, Page fPage) {
			super(id, m);
			this.fromPage = fPage;
			branchPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
			formAPaymentFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
			formAPaymentFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL);
			businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			currentLlpUserProfile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			String prefixLabelKey = "page.lbl.ezbiz.robFormA.";
			setPrefixLabelKey(prefixLabelKey);
			
			robFormA = (RobFormA) m.getObject();
			
			robFormACode = new SSMTextField("robFormACode");
			robFormACode.setReadOnly(true);
			add(robFormACode);
			
			bizName = new SSMTextField("bizName");
			bizName.setReadOnly(true);
			add(bizName);
			
			SSMTextField brNo = new SSMTextField("brNo");
			brNo.setReadOnly(true);
			add(brNo);
			if(StringUtils.isBlank(robFormA.getBrNo())){
				brNo.setVisible(false);
			}
			
			String nameTypeDesc = getCodeTypeWithValue(Parameter.ROB_NAME_TYPE, robFormA.getNameType());
			SSMTextField nameType = new SSMTextField("nameType", new PropertyModel(nameTypeDesc, ""));
			nameType.setReadOnly(true);
			add(nameType);
			
			
			String bizStartDtStr = "";
			if(robFormA.getBizStartDt()!=null){
				bizStartDtStr = sdf.format(robFormA.getBizStartDt());
			}
			SSMTextField bizStartDt = new SSMTextField("bizStartDt",new PropertyModel(bizStartDtStr, ""));
			bizStartDt.setReadOnly(true);
			add(bizStartDt);

			
			String bizRegPeriodStr = getCodeTypeWithValue(Parameter.ROB_RENEWAL_YEAR, String.valueOf(robFormA.getBizRegPeriod()));
			SSMTextField bizRegPeriod = new SSMTextField("bizRegPeriod", new PropertyModel(bizRegPeriodStr, ""));
			bizRegPeriod.setReadOnly(true);
			add(bizRegPeriod);
			
//			final SSMAjaxCheckBox isHasPartnershipAgreement= new SSMAjaxCheckBox("isHasPartnershipAgreement", new PropertyModel(robFormA, "isHasPartnershipAgreement"), true) {
//				@Override
//				protected void onUpdate(AjaxRequestTarget target) {
//				}
//			};
//			add(isHasPartnershipAgreement);
			
			
			String bizPartnershipAgreementDateStr = "";
			if(robFormA.getBizPartnershipAgreementDate()!=null){
				bizPartnershipAgreementDateStr = sdf.format(robFormA.getBizPartnershipAgreementDate());
			}
			SSMTextField bizPartnershipAgreementDate = new SSMTextField("bizPartnershipAgreementDate", new PropertyModel(bizPartnershipAgreementDateStr, ""), true);
			bizPartnershipAgreementDate.setReadOnly(true);
			add(bizPartnershipAgreementDate);
			
			String isBuyInfoDesc = getCodeTypeWithValue(Parameter.YES_NO, robFormA.isBuyInfo());
			SSMTextField isBuyInfo = new SSMTextField("isBuyInfo", new PropertyModel(isBuyInfoDesc, ""));
			isBuyInfo.setReadOnly(true);
			add(isBuyInfo);
			
			
			SSMTextField statusTf = new SSMTextField("status", Model.of(""));
			statusTf.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_FORM_A_STATUS, robFormA.getStatus()));
			statusTf.setReadOnly(true);
			add(statusTf);
			
			
			wmcAddress = new WebMarkupContainer("wmcAddress");
			wmcAddress.setPrefixLabelKey(prefixLabelKey);
			wmcAddress.setOutputMarkupId(true);
			wmcAddress.setOutputMarkupPlaceholderTag(true);
			add(wmcAddress);

			final SSMTextField bizMainAddr = new SSMTextField("bizMainAddr");
			bizMainAddr.setReadOnly(true);
			wmcAddress.add(bizMainAddr);
			final SSMTextField bizMainAddr2 = new SSMTextField("bizMainAddr2");
			bizMainAddr2.setReadOnly(true);
			bizMainAddr2.setNoLabel();
			wmcAddress.add(bizMainAddr2);
			final SSMTextField bizMainAddr3 = new SSMTextField("bizMainAddr3");
			bizMainAddr3.setReadOnly(true);
			wmcAddress.add(bizMainAddr3);
			bizMainAddr3.setNoLabel();
			final SSMTextField bizMainAddrTown= new SSMTextField("bizMainAddrTown");
			bizMainAddrTown.setReadOnly(true);
			wmcAddress.add(bizMainAddrTown);
			final SSMTextField bizMainAddrPostcode= new SSMTextField("bizMainAddrPostcode");
			bizMainAddrPostcode.setReadOnly(true);
			wmcAddress.add(bizMainAddrPostcode);
			
			String bizMainAddrStateDesc = getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormA.getBizMainAddrState());
			SSMTextField bizMainAddrState = new SSMTextField("bizMainAddrState", new PropertyModel(bizMainAddrStateDesc, ""),true);
			bizMainAddrState.setReadOnly(true);
			wmcAddress.add(bizMainAddrState);
			
			SSMTextField isIncubatorDesc = new SSMTextField("isIncubatorDesc", Model.of(""));
			isIncubatorDesc.setDefaultModelObject(getCodeTypeWithValue(Parameter.YES_NO, robFormA.isIncubator()));
			isIncubatorDesc.setReadOnly(true);
			wmcAddress.add(isIncubatorDesc);
			
			SSMDownloadLink downloadSupportingDoc = new SSMDownloadLink("downloadSupportingDoc");
			downloadSupportingDoc.setVisible(true);
			wmcAddress.add(downloadSupportingDoc); 
			
			if(Parameter.YES_NO_yes.equals(robFormA.getIsHasSupportingDoc())){
				downloadSupportingDoc.setDownloadData("SUPPORTING.pdf", "application/pdf", robFormA.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
			}
			
			final SSMTextField bizMainAddrTelNo= new SSMTextField("bizMainAddrTelNo");
			bizMainAddrTelNo.setReadOnly(true);
			wmcAddress.add(bizMainAddrTelNo);
			final SSMTextField bizMainAddrMobileNo= new SSMTextField("bizMainAddrMobileNo");
			bizMainAddrMobileNo.setReadOnly(true);
			wmcAddress.add(bizMainAddrMobileNo);
			final SSMTextField bizMainAddrEmail= new SSMTextField("bizMainAddrEmail");
			bizMainAddrEmail.setReadOnly(true);
			bizMainAddrEmail.setUpperCase(false);
			wmcAddress.add(bizMainAddrEmail);
			
			final SSMTextField bizPostAddr= new SSMTextField("bizPostAddr");
			bizPostAddr.setReadOnly(true);
			wmcAddress.add(bizPostAddr);
			final SSMTextField bizPostAddr2= new SSMTextField("bizPostAddr2");
			bizPostAddr2.setReadOnly(true);
			bizPostAddr2.setNoLabel();
			wmcAddress.add(bizPostAddr2);
			final SSMTextField bizPostAddr3= new SSMTextField("bizPostAddr3");
			bizPostAddr3.setReadOnly(true);
			bizPostAddr3.setNoLabel();
			wmcAddress.add(bizPostAddr3);
			final SSMTextField bizPostAddrTown= new SSMTextField("bizPostAddrTown");
			bizPostAddrTown.setReadOnly(true);
			wmcAddress.add(bizPostAddrTown);
			
			String bizPostAddrStateDesc = getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormA.getBizPostAddrState());
			SSMTextField bizPostAddrState = new SSMTextField("bizPostAddrState", new PropertyModel(bizPostAddrStateDesc, ""));
			bizPostAddrState.setReadOnly(true);
			wmcAddress.add(bizPostAddrState);
			
			final SSMTextField bizPostAddrPostcode = new SSMTextField("bizPostAddrPostcode");
			bizPostAddrPostcode.setReadOnly(true);
			wmcAddress.add(bizPostAddrPostcode);
			final SSMTextField bizPostAddrTelNo= new SSMTextField("bizPostAddrTelNo");
			bizPostAddrTelNo.setReadOnly(true);
			wmcAddress.add(bizPostAddrTelNo);
			final SSMTextField bizPostAddrMobileNo= new SSMTextField("bizPostAddrMobileNo");
			bizPostAddrMobileNo.setReadOnly(true);
			wmcAddress.add(bizPostAddrMobileNo);
			final SSMTextField bizPostAddrEmail= new SSMTextField("bizPostAddrEmail");
			bizPostAddrEmail.setReadOnly(true);
			bizPostAddrEmail.setUpperCase(false);
			wmcAddress.add(bizPostAddrEmail);
			
			//Branches
			final WebMarkupContainer wmcBranchesAll = new WebMarkupContainer("wmcBranchesAll");
			wmcBranchesAll.setOutputMarkupId(true);
			wmcBranchesAll.setOutputMarkupPlaceholderTag(true);
			add(wmcBranchesAll);
			
			wmcBranches = new WebMarkupContainer("wmcBranches");
			wmcBranches.setOutputMarkupId(true);
			wmcBranches.setOutputMarkupPlaceholderTag(true);
			wmcBranchesAll.add(wmcBranches);
			
			final List listBranches = robFormA.getListRobFormABranches();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
			final SSMDataView<RobFormABranches> dataView = new SSMDataView<RobFormABranches>("sortingRobFormABranch", dp) {

				protected void populateItem(final Item<RobFormABranches> item) {
					RobFormABranches robFormABranches = item.getModelObject();

					item.add(new SSMLabel("branchNo", item.getIndex()+1));
					String address = robFormABranches.getAddr();
					if(StringUtils.isNotBlank(robFormABranches.getAddr2())){
						address += "\n"+robFormABranches.getAddr2();
					}
					if(StringUtils.isNotBlank(robFormABranches.getAddr3())){
						address += "\n"+robFormABranches.getAddr3();
					}
					address += "\n"+robFormABranches.getAddrPostcode()+" "+robFormABranches.getAddrTown();
					address = address +"\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormABranches.getAddrState()) ;
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
			
			
			
			//Owners
			final WebMarkupContainer wmcOwnersAll = new WebMarkupContainer("wmcOwnersAll");
			wmcOwnersAll.setPrefixLabelKey(prefixLabelKey+"robFormAOwners.");
			wmcOwnersAll.setOutputMarkupId(true);
			wmcOwnersAll.setOutputMarkupPlaceholderTag(true);
			add(wmcOwnersAll);
			
			
			wmcOwners = new WebMarkupContainer("wmcOwners");
			wmcOwners.setOutputMarkupId(true);
			wmcOwners.setOutputMarkupPlaceholderTag(true);
			wmcOwnersAll.add(wmcOwners);
			

			final ModalWindow editOwnerPopUp = new ModalWindow("editOwnerPopUp");
			editOwnerPopUp.setHeightUnit("px"); 
			editOwnerPopUp.setInitialHeight(700);
			add(editOwnerPopUp);
			
			final SSMSessionSortableDataProvider dpOwners = new SSMSessionSortableDataProvider("", robFormA.getListRobFormAOwner());
			final SSMDataView<RobFormAOwner> dataViewOwners = new SSMDataView<RobFormAOwner>("sortingRobFormAOwners", dpOwners) {

				protected void populateItem(final Item<RobFormAOwner> item) {
					final RobFormAOwner robFormAOwnerSelected = (RobFormAOwner) item.getModelObject();

					item.add(new SSMLabel("ownerNo", item.getIndex()+1));
					item.add(new SSMLabel("name", robFormAOwnerSelected.getName()));
					item.add(new SSMLabel("idNo", robFormAOwnerSelected.getNewicno()));
					SSMLabel veriStatus = new SSMLabel("verificationStatus", robFormAOwnerSelected.getVerificationStatus(), Parameter.ROB_OWNER_VERI_STATUS);
					item.add(veriStatus);

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
					
					AjaxLink editOwner = new AjaxLink("editOwner", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							getSession().setAttribute("listRobFormAOwners_", (Serializable) listFormRobAOwners);
							
							editOwnerPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new EditRobFormAOwnerPanel(robFormAOwnerSelected, editOwnerPopUp, item.getIndex() , false);// view cannot edit record
								}
							});
							
							editOwnerPopUp.show(target);
						}
					};
					item.add(editOwner);
					
					String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.confirmAccept");
					SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							
							robFormAOwnerSelected.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_VERIFIED);
							robFormAOwnerService.update(robFormAOwnerSelected);
							robFormAService.sendEmailPartnerAccept(robFormA, robFormAOwnerSelected);
							
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
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
							List<RobFormAOwner> listFormRobAOwners = dpProvider.getListResult();
							

							listFormRobAOwners.remove(robFormAOwnerSelected);
							
							
							robFormAOwnerSelected.setVerificationStatus(Parameter.ROB_OWNER_VERI_STATUS_REJECT);
							robFormAService.sendEmailPartnerReject(robFormA, robFormAOwnerSelected);
							robFormAOwnerService.delete(robFormAOwnerSelected);
							
							dpProvider.resetView(listFormRobAOwners);
							
							target.add(wmcOwners);
							setResponsePage(new OwnersValidationListRobFormAPage());
						}
					};
					rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
					rejectOwners.setVisible(false);
					item.add(rejectOwners);
					
					
					if(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(robFormA.getStatus()) && robFormAOwnerSelected.getNewicno().equals(currentLlpUserProfile.getIdNo())){
						if(!Parameter.ROB_OWNER_VERI_STATUS_VERIFIED.equals(robFormAOwnerSelected.getVerificationStatus())){
							acceptOwners.setVisible(true);
						}
						rejectOwners.setVisible(true);
					}
					
					
					if(Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robFormAOwnerSelected.getVerificationStatus())){
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
					
					
				}

			};

			wmcOwners.add(dataViewOwners);
			wmcOwners.add(new SSMPagingNavigator("navigatorRobFormAOwners", dataViewOwners));
			wmcOwners.add(new NavigatorLabel("navigatorLabelRobFormAOwners", dataViewOwners));
			
			editOwnerPopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
					List<RobFormAOwner> listFormRobAOwners = (List<RobFormAOwner>) getSession().getAttribute("listRobFormAOwners_");
					dpProvider.resetView(listFormRobAOwners);
					
					robFormA.setNewIcNoForOwners("");
//					target.add(newIcNoForOwners);
					target.add(wmcOwners);
				}
			});
			
			
			//Biz Code
			wmcBizCodeAll = new WebMarkupContainer("wmcBizCodeAll");
			wmcBizCodeAll.setPrefixLabelKey(prefixLabelKey+"robFormABizCode.");
			wmcBizCodeAll.setOutputMarkupId(true);
			wmcBizCodeAll.setOutputMarkupPlaceholderTag(true);
			add(wmcBizCodeAll);
			
			wmcBizCodeAll.add(new SSMTextArea("bizDesc"));
			
			
			final SSMSessionSortableDataProvider dpBizCode = new SSMSessionSortableDataProvider("", robFormA.getListRobFormABizCode());
			final SSMDataView<RobFormABizCode> dataViewBizCode = new SSMDataView<RobFormABizCode>("sortingRobFormABizCode", dpBizCode) {

				protected void populateItem(final Item<RobFormABizCode> item) {
					RobFormABizCode robFormABizCode = item.getModelObject();

					item.add(new SSMLabel("bizCodeNo", item.getIndex()+1));
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
			wmcBizCodeAll.add(dataViewBizCode);
			wmcBizCodeAll.add(new SSMPagingNavigator("navigatorRobFormABizCode", dataViewBizCode));
			wmcBizCodeAll.add(new NavigatorLabel("navigatorLabelRobFormABizCode", dataViewBizCode));
			
			
			//End of Biz Code
			
			
			//Fee Summary
			wmcFeeSummaryAll = new WebMarkupContainer("wmcFeeSummaryAll");
			wmcFeeSummaryAll.setPrefixLabelKey(prefixLabelKey+"feeSummary.");
			wmcFeeSummaryAll.setOutputMarkupId(true);
			wmcFeeSummaryAll.setOutputMarkupPlaceholderTag(true);
			add(wmcFeeSummaryAll);
			
			regFeeDiscount = new SSMLabel("regFeeDiscount","");
			regFeeDiscount.setOutputMarkupId(true);
			regFeeDiscount.setVisible(false);
			
			System.out.println("robFormA.getIncentive() : " + robFormA.getIncentive());
			if(robFormA.getIncentive().equals(Parameter.ROB_FORM_A_INCENTIVE_student)){
				regFeeDiscount.setVisible(true);
			}
			
			wmcFeeSummaryAll.add(regFeeDiscount);
			totalRegDiscount = new SSMLabel("totalRegDiscount","");
			wmcFeeSummaryAll.add(totalRegDiscount);
			regFeeDurationDiscount = new SSMLabel("regFeeDurationDiscount","");
			wmcFeeSummaryAll.add(regFeeDurationDiscount);
			incentiveTypeLabel = new SSMLabel("incentiveTypeLabel","");
			wmcFeeSummaryAll.add(incentiveTypeLabel);
			
			regFeePerYear = new SSMLabel("regFeePerYear","");
			wmcFeeSummaryAll.add(regFeePerYear);
			regFeeDuration = new SSMLabel("regFeeDuration","");
			wmcFeeSummaryAll.add(regFeeDuration);
			
			
			totalRegFee = new SSMLabel("totalRegFee","");
			wmcFeeSummaryAll.add(totalRegFee);
			
			branchFee = new SSMLabel("branchFee","");
//			wmcFeeSummaryAll.add(branchFee);
			branchFeeDuration  = new SSMLabel("branchFeeDuration","");
			wmcFeeSummaryAll.add(branchFeeDuration);
			branchFeePerYear = new SSMLabel("branchFeePerYear","");
			wmcFeeSummaryAll.add(branchFeePerYear);
			totalBranchFee = new SSMLabel("totalBranchFee","");
			wmcFeeSummaryAll.add(totalBranchFee);
			
			bisnessInfoFee = new SSMLabel("bisnessInfoFee","");
			wmcFeeSummaryAll.add(bisnessInfoFee);
			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity","");
			wmcFeeSummaryAll.add(bisnessInfoFeeQuantity);
			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee","");
			wmcFeeSummaryAll.add(totalBisnessInfoFee);
			
			totalFee = new SSMLabel("totalFee","");
			wmcFeeSummaryAll.add(totalFee);
			
			summaryError = new SSMLabel("summaryError","");
			summaryError.setEscapeModelStrings(true);
//			summaryError.setVisible(false);
			//ui basic red pointing prompt label transition visible
//			wmcFeeSummaryAll.add(summaryError);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			wmcFeeSummaryAll.add(listError);
			//End of Fee Summary
			
			final WebMarkupContainer wmcAddressStep = new WebMarkupContainer("wmcAddressStep");
			wmcAddressStep.setOutputMarkupId(true);
			wmcAddressStep.setOutputMarkupPlaceholderTag(true);
			wmcAddressStep.add(new AttributeModifier("class", new Model("active step")));
			add(wmcAddressStep);
			final WebMarkupContainer wmcBranchesStep = new WebMarkupContainer("wmcBranchesStep");
			wmcBranchesStep.setOutputMarkupId(true);
			wmcBranchesStep.setOutputMarkupPlaceholderTag(true);
			wmcBranchesStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcBranchesStep);
			final WebMarkupContainer wmcOwnersStep = new WebMarkupContainer("wmcOwnersStep");
			wmcOwnersStep.setOutputMarkupId(true);
			wmcOwnersStep.setOutputMarkupPlaceholderTag(true);
			wmcOwnersStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcOwnersStep);
			final WebMarkupContainer wmcBizCodeStep = new WebMarkupContainer("wmcBizCodeStep");
			wmcBizCodeStep.setOutputMarkupId(true);
			wmcBizCodeStep.setOutputMarkupPlaceholderTag(true);
			wmcBizCodeStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcBizCodeStep);
			final WebMarkupContainer wmcFeeSummaryStep = new WebMarkupContainer("wmcFeeSummaryStep");
			wmcFeeSummaryStep.setOutputMarkupId(true);
			wmcFeeSummaryStep.setOutputMarkupPlaceholderTag(true);
			wmcFeeSummaryStep.add(new AttributeModifier("class", new Model("step")));
			add(wmcFeeSummaryStep);
			
			final WebMarkupContainer segmentLeftMenu[] = new WebMarkupContainer[]{wmcAddressStep, wmcBranchesStep, wmcBizCodeStep, wmcOwnersStep,wmcFeeSummaryStep}; 
			final WebMarkupContainer segmentContainer[] = new WebMarkupContainer[]{wmcAddress, wmcBranchesAll, wmcBizCodeAll , wmcOwnersAll, wmcFeeSummaryAll}; 
			
			//Show Hide Panel
			wmcAddressStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 0, segmentLeftMenu, segmentContainer);
				}
			});
			wmcBranchesStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 1, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcOwnersStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 2, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcBizCodeStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			});
			
			wmcFeeSummaryStep.add(new AjaxEventBehavior("onclick") {
				@Override
				protected void onEvent(AjaxRequestTarget target) {
//					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			});
			
			
			//Main Address
			SSMDownloadLink downloadCert = new SSMDownloadLink("downloadCert");
			downloadCert.setVisible(false);
			wmcAddress.add(downloadCert);
			
			SSMDownloadLink downloadBusinessInfo = new SSMDownloadLink("downloadBusinessInfo");
			downloadBusinessInfo.setVisible(false);
			wmcAddress.add(downloadBusinessInfo);
			
			SSMDownloadLink downloadBorangAForm = new SSMDownloadLink("downloadBorangAForm");
			downloadBorangAForm.setVisible(false);
			wmcAddress.add(downloadBorangAForm);
			
			SSMDownloadLink downloadCmpNotice = new SSMDownloadLink("downloadCmpNotice");
			downloadCmpNotice.setVisible(false);
			wmcAddress.add(downloadCmpNotice);
			
			if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(robFormA.getStatus())){
				RobFormA robFormATmp = null;
				try {
					robFormATmp = robFormAService.findByIdWithData(robFormA.getRobFormACode());
					downloadCert.setDownloadData(robFormATmp.getBrNo() + "_CERT.pdf", "application/pdf", robFormATmp.getCertFileData().getFileData());
					downloadCert.setVisible(true);
				} catch (SSMException e) {
					e.printStackTrace();
				}
				try {
					downloadBorangAForm.setDownloadData(robFormATmp.getBrNo() + "_BORANG_A.pdf", "application/pdf", robFormATmp.getFormAData().getFileData());
					downloadBorangAForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())){
					try {
						downloadBusinessInfo.setDownloadData(robFormATmp.getBrNo() + "_BIS_INFO.pdf", "application/pdf", robFormATmp.getBusinessInfoData().getFileData());
						downloadBusinessInfo.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(StringUtils.isNotBlank(robFormA.getCompoundNo())){
					try {
						downloadCmpNotice.setDownloadData(robFormATmp.getBrNo() + "_CMP_NOTICE.pdf", "application/pdf", robFormATmp.getCmpNoticeFileData().getFileData());
						downloadCmpNotice.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
			
			
//			
//			SSMAjaxLink downloadCert = new SSMAjaxLink("downloadCert") {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//					try {
//						final RobFormA robFormATmp = robFormAService.findByIdWithData(robFormA.getRobFormACode());
//						generateDownload(robFormATmp.getBrNo() + "_CERT.pdf", robFormATmp.getCertFileData().getFileData());
//					} catch (SSMException e) {
//						ssmError(e);
//					}
//					
//				}
//			};
//			downloadCert.setDefaultFormProcessing(false);
//			downloadCert.setVisible(false);
//			wmcAddress.add(downloadCert);
//			
//			if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(robFormA.getStatus())){
//				downloadCert.setVisible(true);
//			}
			
			SSMAjaxButton mainNext = new SSMAjaxButton("mainNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 1, segmentLeftMenu ,segmentContainer);
				}
			};
			mainNext.setDefaultFormProcessing(false);
			wmcAddress.add(mainNext);
			
			//Branches
			SSMAjaxButton branchesPrevious = new SSMAjaxButton("branchesPrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 0, segmentLeftMenu, segmentContainer);
				}

			};
			branchesPrevious.setDefaultFormProcessing(false);
			wmcBranchesAll.add(branchesPrevious);
			
			SSMAjaxButton branchesNext = new SSMAjaxButton("branchesNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 2, segmentLeftMenu, segmentContainer);
				}
			};
			branchesNext.setDefaultFormProcessing(false);
			wmcBranchesAll.add(branchesNext);
			
			
			//Biz Code
			SSMAjaxButton bizCodePrevious = new SSMAjaxButton("bizCodePrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 1, segmentLeftMenu, segmentContainer);
				}
			};
			bizCodePrevious.setDefaultFormProcessing(false);
			wmcBizCodeAll.add(bizCodePrevious);
			
			SSMAjaxButton bizCodeNext = new SSMAjaxButton("bizCodeNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			};
			bizCodeNext.setDefaultFormProcessing(false);
			wmcBizCodeAll.add(bizCodeNext);
			
			//Owners
			SSMAjaxButton ownersPrevious = new SSMAjaxButton("ownersPrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 2, segmentLeftMenu ,segmentContainer);
				}
			};
			ownersPrevious.setDefaultFormProcessing(false);
			wmcOwnersAll.add(ownersPrevious);

			SSMAjaxButton ownersNext = new SSMAjaxButton("ownersNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormA robFormAForm = (RobFormA) form.getDefaultModelObject();
					recalculateFee(target, robFormAForm);
					hideAndShowSegment(target, 4, segmentLeftMenu, segmentContainer);
				}
			};
			ownersNext.setDefaultFormProcessing(false);
			wmcOwnersAll.add(ownersNext);
			
			
			//Fee Summary
			SSMAjaxButton feeSummaryPrevious = new SSMAjaxButton("feeSummaryPrevious") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 3, segmentLeftMenu, segmentContainer);
				}
			};
			feeSummaryPrevious.setDefaultFormProcessing(false);
			wmcFeeSummaryAll.add(feeSummaryPrevious);
			
			
			SSMAjaxButton exitView = new SSMAjaxButton("exitView") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					setResponsePage(fromPage);
				}
			};
			exitView.setDefaultFormProcessing(false);
			wmcFeeSummaryAll.add(exitView);
			

			SSMAjaxButton reSubmit = new SSMAjaxButton("reSubmit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormA.getNameType())){
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_TRADE);
					}else{//PERSONAL
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_PERSONAL );
					}
					
					paymentItem.setQuantity(robFormA.getBizRegPeriod());
					paymentItem.setAmount(robFormA.getRegistrationAmt());
					paymentItem.setPaymentDet(robFormA.getBizName());
					listPaymentItems.add(paymentItem);

					if (robFormA.getBranchesAmt()>0) {
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_A_BRANCHES);
						paymentItem2.setQuantity(robFormA.getBizRegPeriod());
						paymentItem2.setPaymentDet(robFormA.getListRobFormABranches().size()+" ");
						paymentItem2.setAmount(robFormA.getBranchesAmt());
						listPaymentItems.add(paymentItem2);
					}
					
					if(Parameter.YES_NO_yes.equals(robFormA.isBuyInfo())){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormA.getBusinessInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()*getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}
					
					
					setResponsePage(new PaymentDetailPage(robFormA.getRobFormACode(), RobFormAService.class.getSimpleName(), robFormA,
							listPaymentItems));
					
				}
			};
			reSubmit.setDefaultFormProcessing(false);
			reSubmit.setVisible(false);
			wmcFeeSummaryAll.add(reSubmit);
			
			if(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_OTC.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS.equals(robFormA.getStatus())){
				if(robFormA.getCreateBy().equals(UserEnvironmentHelper.getLoginName())){
					reSubmit.setVisible(true);
				}
			}
			
			
			String hideAllJs = "";
			
			for (int i = 1; i < segmentContainer.length; i++) {
				String js = "$('#"+segmentContainer[i].getMarkupId()+"').hide();"; 
				hideAllJs+=js;
			}
			Label hideAllLbl = new Label("hideAllLbl", hideAllJs);
			hideAllLbl.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		    add(hideAllLbl);
			
			
			setOutputMarkupId(true);
			setOutputMarkupPlaceholderTag(true);
		}
		

		public void generateDownload(String fileName, final byte[] byteData){
			IResourceStream resourceStream = new AbstractResourceStreamWriter() {
			      @Override 
			      public void write(OutputStream output) {
			    	  try {
						output.write(byteData);
					} catch (IOException e) {
						e.printStackTrace();
					}
			      }

			      @Override
			      public String getContentType() {                        
			        return "application/pdf";
			      }
			};
			getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream).setFileName(fileName));
			
//			AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
//				@Override
//				public void write(OutputStream output) throws IOException {
//					output.write(byteData);
//				}
//			};
//
//			ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
//			handler.setContentDisposition(ContentDisposition.ATTACHMENT);
//			getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
		
		protected void recalculateFee(AjaxRequestTarget target, RobFormA robFormAForm) {
			
			SSMSessionSortableDataProvider dpProviderBranch = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABranches>)wmcBranches.get("sortingRobFormABranch")).getDataProvider();
			List<RobFormABranches> listBranches = dpProviderBranch.getListResult();
			
			SSMSessionSortableDataProvider dpProviderOwners = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormAOwner>)wmcOwners.get("sortingRobFormAOwners")).getDataProvider();
			List<RobFormAOwner> listRobFormAOwner = dpProviderOwners.getListResult();
			
			SSMSessionSortableDataProvider dpProviderBizCode = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCodeAll.get("sortingRobFormABizCode")).getDataProvider();
			List<RobFormABizCode> listRobFormABizCode = dpProviderBizCode.getListResult();
			
			robFormAForm.setListRobFormABranches(listBranches);
			robFormAForm.setListRobFormAOwner(listRobFormAOwner);
			robFormAForm.setListRobFormABizCode(listRobFormABizCode);
			robFormA.setListRobFormABranches(listBranches);
			robFormA.setListRobFormAOwner(listRobFormAOwner);
			robFormA.setListRobFormABizCode(listRobFormABizCode);
			
			double totalFeeDouble = 0;
			double regFeePerYearDouble = formAPaymentFeePersonal.getPaymentFee(); 
			
			if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
				regFeePerYearDouble = formAPaymentFeeTrade.getPaymentFee();
			}
			
			
			double totalRegFeeDouble = regFeePerYearDouble * robFormAForm.getBizRegPeriod();
			
			double branchFeeDouble = 5;
			double branchFeePerYearDouble = branchFeeDouble * robFormAForm.getListRobFormABranches().size();
			double totalBranchFeeDouble = branchFeePerYearDouble * robFormAForm.getBizRegPeriod();
			
			double bisnessInfoFeeDouble = businessInfoPaymentFee.getPaymentFee();
			int bisnessInfoFeeQuantityInt = 0;
			double totalBisnessInfoFeeDouble = 0;
			
			double gstAmt = 0;
			
			if(Parameter.YES_NO_yes.equals(robFormAForm.isBuyInfo())){
				bisnessInfoFeeQuantityInt = 1;
				totalBisnessInfoFeeDouble = bisnessInfoFeeDouble;
				if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
					totalBisnessInfoFeeDouble += bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR);
					gstAmt += (bisnessInfoFeeDouble * getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
				}
			}
			
			double totalDiscount = 0;
			if(Parameter.ROB_FORM_A_INCENTIVE_student.equals(robFormAForm.getIncentive())){
				totalDiscount = regFeePerYearDouble;
				regFeeDiscount.setVisible(true);
			}else{
				regFeeDiscount.setVisible(false);
			}
			
			totalFeeDouble = totalRegFeeDouble +  totalBranchFeeDouble + totalBisnessInfoFeeDouble - totalDiscount;
			
			DecimalFormat df = new DecimalFormat("#0.00");
			DecimalFormat dfNegative = new DecimalFormat("-#0.00");
			
			robFormAForm.setRegistrationAmt(totalRegFeeDouble);
			robFormAForm.setBranchesAmt(totalBranchFeeDouble);
			robFormAForm.setTotalAmt(totalFeeDouble);
			
			regFeePerYear.setDefaultModelObject(df.format(regFeePerYearDouble));
			regFeeDuration.setDefaultModelObject(robFormAForm.getBizRegPeriod());
			totalRegFee.setDefaultModelObject(df.format(totalRegFeeDouble));
			
			incentiveTypeLabel.setDefaultModelObject(getCodeTypeWithValue(Parameter.ROB_FORM_A_INCENTIVE, robFormAForm.getIncentive()));
			regFeeDiscount.setDefaultModelObject(dfNegative.format(regFeePerYearDouble));
			regFeeDurationDiscount.setDefaultModelObject(robFormAForm.getBizRegPeriod());
			totalRegDiscount.setDefaultModelObject(dfNegative.format(totalDiscount));
			
			branchFee.setDefaultModelObject(df.format(branchFeeDouble));
			branchFeeDuration.setDefaultModelObject(robFormAForm.getBizRegPeriod());
			branchFeePerYear.setDefaultModelObject(df.format(branchFeePerYearDouble));
			totalBranchFee.setDefaultModelObject(df.format(totalBranchFeeDouble));
			
			bisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			bisnessInfoFeeQuantity.setDefaultModelObject(String.valueOf(bisnessInfoFeeQuantityInt));
			totalBisnessInfoFee.setDefaultModelObject(df.format(totalBisnessInfoFeeDouble));
			
			totalFee.setDefaultModelObject(df.format(totalFeeDouble));
			
			listError.removeAll();
			if( Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(robFormA.getStatus()) || Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormA.getStatus()) ){
				//Check Name
//				if(Parameter.ROB_NAME_TYPE_TRADE.equals(robFormAForm.getNameType())){
//					List<SSMException> listException = robFormAService.validateName(robFormAForm.getBizName());
//					if(listException.size()>0){
//						String errorDesc = "";
//						for (int i = 0; i < listException.size(); i++) {
//							listError.add(new SSMLabel(listError.newChildId() ,listException.get(i).getMessage()));
//							errorDesc+=listException.get(i).getMessage()+"</br>";
//						}
//					}
//					
//				}else{
//					if(!currentLlpUserProfile.getName().equals(robFormAForm.getBizName())){
//						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.personalMustUserLoggerName", robFormAForm.getBizName())));
//					}
//					if(listRobFormAOwner.size()>1){
//						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.personalCanOnlyHaveOneOwner")));
//					}
//				}
				
				
				//Check partner verification
				for (int i = 0; i < listRobFormAOwner.size(); i++) {
					RobFormAOwner formAOwner = listRobFormAOwner.get(i);
					if(Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(formAOwner.getVerificationStatus())){
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.partnert.notVerify", formAOwner.getName())));
					}
				}
				//Check Bisness Code
				if(listRobFormABizCode.size()<2){
					listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormA.bizCode.notEnough")));
				}
				
			}
			
			if(currentShowId!=4){
				String js = "$('#"+wmcFeeSummaryAll.getMarkupId()+"').hide();"; 
				target.appendJavaScript(js);
			}
			
			if(listError.size()>0){
				listError.setVisible(true);
			}else{
				listError.setVisible(false);
			}
			if(!robFormA.getCreateBy().equals(UserEnvironmentHelper.getLoginName())){//If Not Logger
				listError.setVisible(false);
			}
			target.add(wmcFeeSummaryAll);
		}

		public WebMarkupContainer getWmcAddress() {
			return wmcAddress;
		}
		
		public final void hideAndShowSegment(AjaxRequestTarget target, int segmentIdToShow, WebMarkupContainer[]  segementLeftMenu, WebMarkupContainer[] segmentContainer){
			
			for (int i = 0; i < segementLeftMenu.length; i++) {
				try {
					List<AttributeModifier> amList = segementLeftMenu[i].getBehaviors(AttributeModifier.class);
					for (int j = 0; j < amList.size(); j++) {
						if(amList.get(j).getAttribute().equals("class")){
							segementLeftMenu[i].remove(amList.get(j));
							break;
						}
					}
				} catch (Exception e) {
				}
				
				if(i == segmentIdToShow){
					segementLeftMenu[i].add(new AttributeModifier("class", new Model("active step")));
				}else{
					segementLeftMenu[i].add(new AttributeModifier("class", new Model("step")));
				}
				target.add(segementLeftMenu[i]);
			}
			
			String js = "var toOpts = { direction: 'right' };";
			for (int i = 0; i < segmentContainer.length; i++) {
				if(i == segmentIdToShow){
					continue;
				}
				js += "$('#"+segmentContainer[i].getMarkupId()+"').hide();"; 
			}
			js += "if($('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').is(':hidden')){";
			js += "$('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').toggle('slide', toOpts, 500).is(':hidden');"; 
			js += "}";
			
			String scroll = "\n$.scrollTo(document.getElementById('"+segmentContainer[segmentIdToShow].getMarkupId()+"'),100);\n";
			js += scroll;
			
			target.appendJavaScript(js);
			currentShowId = segmentIdToShow;
		}
	}
	
	
	
	private final void copyFromUserProfileToRobOwner(LlpUserProfile llpUserProfile, RobFormAOwner robFormAOwner) {
		robFormAOwner.setEzbizLoginName(llpUserProfile.getLoginId());
		robFormAOwner.setName(llpUserProfile.getName());
		robFormAOwner.setDob(llpUserProfile.getDob());
		robFormAOwner.setNewicno(llpUserProfile.getIdNo());
		robFormAOwner.setGender(llpUserProfile.getGender());
		robFormAOwner.setRace(llpUserProfile.getRace());
		robFormAOwner.setNationality(llpUserProfile.getNationality());
		robFormAOwner.setEmail(llpUserProfile.getEmail());
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.mybiz.robFormA";
	}

}
