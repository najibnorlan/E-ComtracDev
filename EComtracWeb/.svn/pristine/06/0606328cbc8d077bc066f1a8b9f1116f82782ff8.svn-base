package com.ssm.ezbiz.robFormB;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.hibernate.LazyInitializationException;

import com.ssm.ezbiz.payment.ViewPaymentTransactionPanel;
import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.ezbiz.service.RobFormOwnerVerificationService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RobFormB4;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ViewRobFormBPage extends SecBasePage {

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	@SpringBean(name = "LlpPaymentTransactionService")
	private LlpPaymentTransactionService llpPaymentTransactionService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;
	
	@SpringBean(name = "RobFormOwnerVerificationService")
	private RobFormOwnerVerificationService robFormOwnerVerificationService;
	

	public ViewRobFormBPage(final String robFormBRefNo, Page fromPage) {

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormB robFormB = robFormBService.findAllById(robFormBRefNo);
				return robFormB;
			}
		}));
		init(fromPage);
	}

	private void init(Page fromPage) {
		add(new RobFormBForm("form", getDefaultModel(), fromPage));
	}

	private class RobFormBForm extends Form implements Serializable {

		final Page fromPage;
		final RobFormB robFormB;

		public RobFormBForm(String id, IModel m, Page fPage) {
			super(id, m);
			this.fromPage = fPage;
			robFormB = (RobFormB) m.getObject();
			add(new SSMLabel("robFormBCode",robFormB.getRobFormBCode()));
			
			add(new SSMLabel("brNo",robFormB.getBrNo()+"-"+robFormB.getCheckDigit()));
			add(new SSMLabel("bizName",robFormB.getBizName()));
			add(new SSMLabel("bizExpDt",robFormB.getBizExpDt(),"dd MMM yyyy"));
			add(new SSMLabel("status", getCodeTypeWithValue(Parameter.ROB_FORM_B_STATUS, robFormB.getStatus()) ));
			
			MultiLineLabel approveRejectNotes= new MultiLineLabel("approveRejectNotes", robFormB.getApproveRejectNotes());
			approveRejectNotes.setVisible(false);
			add(approveRejectNotes);
			if(Parameter.ROB_FORM_B_STATUS_REJECT.equals(robFormB.getStatus())) {
				approveRejectNotes.setVisible(true);
			}
			
			
			List listRobFormNotes = robFormNotesService.findByFormCode(robFormB.getRobFormBCode());
			
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
			
			if(UserEnvironmentHelper.isInternalUser() && listRobFormNotes.size()>0){
				listQueryView.setVisible(true);
			}
			
			
			
			WebMarkupContainer b1wmc = new WebMarkupContainer("b1wmc");
			b1wmc.setVisible(robFormB.getIsB1());
			add(b1wmc);
			if(robFormB.getIsB1()){
				

				b1wmc.add(new SSMLabel("b1AmmendmendDt",robFormB.getRobFormB1().getB1AmmendmendDt(), "dd MMM yyyy"));
				
				String mainAddress = robFormB.getRobFormB1().getBizMainAddr().toUpperCase();
				if(StringUtils.isNotBlank(robFormB.getRobFormB1().getBizMainAddr2())){
					mainAddress+="\n"+robFormB.getRobFormB1().getBizMainAddr2().toUpperCase();
				}
				if(StringUtils.isNotBlank(robFormB.getRobFormB1().getBizMainAddr3())){
					mainAddress+="\n"+robFormB.getRobFormB1().getBizMainAddr3().toUpperCase();
				}
				mainAddress += "\n"+robFormB.getRobFormB1().getBizMainAddrPostcode()+" "+robFormB.getRobFormB1().getBizMainAddrTown().toUpperCase();
				mainAddress += "\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormB.getRobFormB1().getBizMainAddrState()) ;
				MultiLineLabel mainAddressLabel = new MultiLineLabel("mainAddress", mainAddress);
				b1wmc.add(mainAddressLabel);
				b1wmc.add(new SSMLabel("bizMainAddrTelNo", robFormB.getRobFormB1().getBizMainAddrTelNo()));
				b1wmc.add(new SSMLabel("bizMainAddrMobileNo", robFormB.getRobFormB1().getBizMainAddrMobileNo()));
				b1wmc.add(new SSMLabel("bizMainAddrEmail", robFormB.getRobFormB1().getBizMainAddrEmail()));
				b1wmc.add(new SSMLabel("bizMainAddrUrl", robFormB.getRobFormB1().getBizMainAddrUrl()));
				
				
				String postAddress = mainAddress;// By Default Same with main
				if(StringUtils.isNotBlank(robFormB.getRobFormB1().getBizPostAddr())){
					postAddress = robFormB.getRobFormB1().getBizPostAddr().toUpperCase();
					if(StringUtils.isNotBlank(robFormB.getRobFormB1().getBizPostAddr2())){
						postAddress+="\n"+robFormB.getRobFormB1().getBizPostAddr2().toUpperCase();
					}
					if(StringUtils.isNotBlank(robFormB.getRobFormB1().getBizPostAddr3())){
						postAddress+="\n"+robFormB.getRobFormB1().getBizPostAddr3().toUpperCase();
					}
					postAddress += "\n"+robFormB.getRobFormB1().getBizPostAddrPostcode()+" "+robFormB.getRobFormB1().getBizPostAddrTown().toUpperCase();
					postAddress += "\n"+getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormB.getRobFormB1().getBizPostAddrState()).toUpperCase() ;
				}
				
				MultiLineLabel postAddressLabel = new MultiLineLabel("postAddress", postAddress);
				b1wmc.add(postAddressLabel);
				
				b1wmc.add(new SSMLabel("bizPostAddrTelNo", robFormB.getRobFormB1().getBizPostAddrTelNo()));
				b1wmc.add(new SSMLabel("bizPostAddrMobileNo", robFormB.getRobFormB1().getBizPostAddrMobileNo()));
				b1wmc.add(new SSMLabel("bizPostAddrEmail", robFormB.getRobFormB1().getBizPostAddrEmail()));
				
			}
			
			WebMarkupContainer b2wmc = new WebMarkupContainer("b2wmc");
			b2wmc.setVisible(robFormB.getIsB2());
			add(b2wmc);
			if(robFormB.getIsB2()){
				
				b2wmc.add(new SSMLabel("b2AmmendmendDt",robFormB.getRobFormB2().getB2AmmendmendDt(), "dd MMM yyyy"));
				
				MultiLineLabel businessInfo = new MultiLineLabel("businessInfo", robFormB.getRobFormB2().getBizDesc().toUpperCase());
				b2wmc.add(businessInfo);
				
				final SSMSessionSortableDataProvider dpBizCode = new SSMSessionSortableDataProvider("", robFormB.getRobFormB2().getListRobFormB2Det());
				final SSMDataView<RobFormB2Det> dataViewBizCode = new SSMDataView<RobFormB2Det>("sortingRobFormB2BizCode", dpBizCode) {

					protected void populateItem(final Item<RobFormB2Det> item) {
						RobFormB2Det robFormB2DetBizCode = item.getModelObject();

						item.add(new SSMLabel("bizCodeNo", item.getIndex()+1));
						item.add(new SSMLabel("bizCode", robFormB2DetBizCode.getBizCode()));
						item.add(new MultiLineLabel("bizCodeDesc", robFormB2DetBizCode.getBizCodeDesc()));
						item.add(new SSMLabel("ammendmentType", robFormB2DetBizCode.getAmmendmentType(), Parameter.ROB_FORM_B2_AMENDMENT_TYPE) );
						
						
						item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even" : "odd";
							}
						}));

					}

				};
				
				b2wmc.add(dataViewBizCode);
				b2wmc.add(new SSMPagingNavigator("navigatorRobFormB2BizCode", dataViewBizCode));
				b2wmc.add(new NavigatorLabel("navigatorLabelRobFormB2BizCode", dataViewBizCode));
			}
			
			WebMarkupContainer b3wmc = new WebMarkupContainer("b3wmc");
			b3wmc.setVisible(robFormB.getIsB3());
			add(b3wmc);
			if(robFormB.getIsB3()){
				b3wmc.add(new SSMLabel("b3AmmendmendDt",robFormB.getB3AmmendmendDt(), "dd MMM yyyy"));
				
				List listBranches = robFormB.getListRobFormB3();
				final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
				final SSMDataView<RobFormB3> dataView = new SSMDataView<RobFormB3>("sortingRobFormB3Branch", dp) {

					protected void populateItem(final Item<RobFormB3> item) {
						RobFormB3 robFormB3Branches = item.getModelObject();

						item.add(new SSMLabel("branchNo", item.getIndex()+1));
						String address = robFormB3Branches.getAddr();
						if(StringUtils.isNotBlank(robFormB3Branches.getAddr2())){
							address += "\n"+robFormB3Branches.getAddr2();
						}
						if(StringUtils.isNotBlank(robFormB3Branches.getAddr3())){
							address += "\n"+robFormB3Branches.getAddr3();
						}
						address += "\n"+robFormB3Branches.getAddrPostcode()+" "+robFormB3Branches.getAddrTown();
						address = address +"\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormB3Branches.getAddrState()) ;
						
						if(StringUtils.isNotBlank(robFormB3Branches.getAddrUrl())){
							address += "\n"+robFormB3Branches.getAddrUrl();
						}
						
						item.add(new MultiLineLabel("branchAddress", address));
						item.add(new SSMLabel("ammendmentType", robFormB3Branches.getAmmendmentType(), Parameter.ROB_FORM_B3_AMENDMENT_TYPE));
						
						item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even" : "odd";
							}
						}));
					}

				};
				b3wmc.add(dataView);
				b3wmc.add(new SSMPagingNavigator("navigatorRobFormB3", dataView));
				b3wmc.add(new NavigatorLabel("navigatorLabelRobFormB3", dataView));
			}
			
			
			WebMarkupContainer b4wmc = new WebMarkupContainer("b4wmc");
			b4wmc.setVisible(robFormB.getIsB4());
			add(b4wmc);
			if(robFormB.getIsB4()){
				b4wmc.add(new SSMLabel("b4AmmendmendDt",robFormB.getB4AmmendmendDt(), "dd MMM yyyy"));
				
				List listB4Owner = robFormB.getListRobFormB4();
				final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listB4Owner);
				final SSMDataView<RobFormB4> dataView = new SSMDataView<RobFormB4>("sortingRobFormB4Owners", dp) {

					protected void populateItem(final Item<RobFormB4> item) {
						
						RobFormB4 robFormB4Owners = item.getModelObject();

						item.add(new SSMLabel("ownersNo", item.getIndex()+1));
						
						
						String ownerAddr = robFormB4Owners.getAddr();
						if (StringUtils.isNotBlank(robFormB4Owners.getAddr2())) {
							ownerAddr +=  "\n"+ robFormB4Owners.getAddr2();
						}
						if (StringUtils.isNotBlank(robFormB4Owners.getAddr3())) {
							ownerAddr += "\n"+robFormB4Owners.getAddr3() ;
						}

						ownerAddr += "\n" + robFormB4Owners.getAddrPostcode() + " " + robFormB4Owners.getAddrTown().toUpperCase();
						ownerAddr += "\n" + getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormB4Owners.getAddrState());
						
						item.add(new SSMLabel("name", robFormB4Owners.getName()));
						item.add(new SSMLabel("idNo", robFormB4Owners.getNewicno()));
						item.add(new MultiLineLabel("ownersAddress", ownerAddr));
						
						
						item.add(new SSMLabel("ammendmentType", robFormB4Owners.getAmmendmentType(), Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_ALL));
						item.add(new SSMLabel("ammendmentDate", robFormB4Owners.getAmmendmentDate(), "dd MMM yyyy"));
						
						
						
						
						if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(robFormB4Owners.getAmmendmentType())){
							SSMLink downlodSupportingDoc = generateDownloadLink("downloadSupportingDoc", robFormB4Owners.getSupportingDocData(), "SupportingDoc");
							item.add(downlodSupportingDoc);
						}else {
							SSMLabel dummy = new SSMLabel("downloadSupportingDoc","");
							item.add(dummy);
							dummy.setVisible(false);
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
				b4wmc.add(dataView);
				b4wmc.add(new SSMPagingNavigator("navigatorRobFormB4", dataView));
				b4wmc.add(new NavigatorLabel("navigatorLabelRobFormB4", dataView));
			}
			
			
			final WebMarkupContainer ownerWmc = new WebMarkupContainer("ownerWmc");
			ownerWmc.setOutputMarkupId(true);
			ownerWmc.setOutputMarkupPlaceholderTag(true);
			add(ownerWmc);
			
			List listBranches = robFormB.getListRobFormOwnerVerification();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
			final SSMDataView<RobFormOwnerVerification> dataView = new SSMDataView<RobFormOwnerVerification>("sortingRobFormBOwnerVeri", dp) {

				protected void populateItem(final Item<RobFormOwnerVerification> item) {
					final RobFormOwnerVerification robFormOwnerVer = item.getModelObject();

					item.add(new SSMLabel("ownerNo", item.getIndex()+1));
					item.add(new SSMLabel("name", robFormOwnerVer.getName()));
					item.add(new SSMLabel("idNo", robFormOwnerVer.getIdNo()));
					SSMLabel veriStatus = new SSMLabel("status", robFormOwnerVer.getStatus(), Parameter.ROB_OWNER_B_C_VERI_STATUS);
					item.add(veriStatus);
					

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
					
					
					String confirmAcceptQuestion = resolve("page.lbl.ezbiz.robFormB.robFormBOwners.confirmAccept");
					SSMAjaxLink acceptOwners = new SSMAjaxLink("acceptOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							
							List<RobFormOwnerVerification> robFormOwnerVerifications = dp.getListResult();

							robFormOwnerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
							robFormOwnerVerificationService.update(robFormOwnerVer);
							robFormBService.sendEmailPartnerAccept(robFormB, robFormOwnerVer);

							dp.resetView(robFormOwnerVerifications);

							target.add(ownerWmc);
						}

					};
					
					acceptOwners.setConfirmQuestion(confirmAcceptQuestion);
					acceptOwners.setVisible(false);
					item.add(acceptOwners);

					
					String rejectOwnerQuestion = resolve("page.lbl.ezbiz.robFormB.robFormBOwners.confirmRemove");
					SSMAjaxLink rejectOwners = new SSMAjaxLink("rejectOwners") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							List<RobFormOwnerVerification> roboFormOwnerVerifications = dp.getListResult();

							robFormOwnerVer.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT);
							robFormOwnerVerificationService.update(robFormOwnerVer);
							robFormBService.sendEmailPartnerReject(robFormB, robFormOwnerVer);
							
							dp.resetView(roboFormOwnerVerifications);
							
							target.add(ownerWmc);
						}
					};
					rejectOwners.setConfirmQuestion(rejectOwnerQuestion);
					rejectOwners.setVisible(false);
					item.add(rejectOwners);
					
					
					if (!UserEnvironmentHelper.isInternalUser()) {
						LlpUserProfile currentLlpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile();
						if ( Parameter.ROB_FORM_C_STATUS_DATA_ENTRY.equals(robFormB.getStatus())  && robFormOwnerVer.getIdNo().equals(currentLlpUserProfile.getIdNo())) {
							if (!Parameter.ROB_OWNER_VERI_STATUS_VERIFIED.equals(robFormOwnerVer.getStatus())) {
								acceptOwners.setVisible(true);
							}
							rejectOwners.setVisible(true);
						}
					}

					if (Parameter.ROB_OWNER_VERI_STATUS_PENDING.equals(robFormOwnerVer.getStatus())) {
						String styleAttr = "color: red;";
						veriStatus.add(new AttributeModifier("style", styleAttr));
					}
					
				}

			};

			ownerWmc.add(dataView);
			ownerWmc.add(new SSMPagingNavigator("navigatorRobFormBOwnerVeri", dataView));
			ownerWmc.add(new NavigatorLabel("navigatorLabelRobFormBOwnerVeri", dataView));
			
			
			
			WebMarkupContainer summaryWmc = new WebMarkupContainer("summaryWmc");
			add(summaryWmc);
			
			final ViewPaymentTransactionPanel viewPaymentTransactionPanel = new ViewPaymentTransactionPanel("viewPaymentTransactionPanel", robFormB.getRobFormBCode());
			viewPaymentTransactionPanel.setOutputMarkupId(true);
			viewPaymentTransactionPanel.setOutputMarkupPlaceholderTag(true);
			summaryWmc.add(viewPaymentTransactionPanel);
			
			
			SSMAjaxButton backButton = new SSMAjaxButton("backButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						setResponsePage(fromPage);
				}
			};
			add(backButton);
			
			SignInSession signInSession = (SignInSession)getSession();
			if(null != getSession() && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
				backButton.setVisible(false);
			}else {
				backButton.setVisible(true);
			}	
			
			SSMDownloadLink downloadSupportingDoc = new SSMDownloadLink("downloadSupportingDoc");
			downloadSupportingDoc.setVisible(false);
			add(downloadSupportingDoc);
			if(Parameter.YES_NO_yes.equals(robFormB.getIsHasSupportingDoc())){
				downloadSupportingDoc.setDownloadData("SUPPORTING"+robFormB.getBrNo()+".pdf", "application/pdf", robFormB.getSupportingDocData().getFileData());
				downloadSupportingDoc.setVisible(true);
			}
			
			SSMDownloadLink downloadBisnessInfo = new SSMDownloadLink("downloadBisnessInfo");
			downloadBisnessInfo.setVisible(false);
			add(downloadBisnessInfo);
				
			
			SSMDownloadLink downloadCert = new SSMDownloadLink("downloadCert");
			downloadCert.setVisible(false);
			add(downloadCert);
			
			
			SSMDownloadLink downloadCmpNotice = new SSMDownloadLink("downloadCmpNotice");
			downloadCmpNotice.setVisible(false);
			add(downloadCmpNotice);
			
			SSMLabel downloadRule = new SSMLabel("downloadRule", new StringResourceModel("page.title.mybiz.editNoteFormA", this, null));
			add(downloadRule);
			downloadRule.setVisible(false);
						
			if(Parameter.ROB_FORM_B_STATUS_APPROVED.equals(robFormB.getStatus())){
				int downValidDays = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG, Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS));
				
				// downValidDays = downValidDays + 1;
				Calendar cal = Calendar.getInstance();
				Date generateCert = robFormB.getUpdateDt();
				
				cal.setTime(generateCert);
				
				cal.add(Calendar.DATE, downValidDays);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				String value = resolve("page.title.mybiz.editNoteFormB",  String.valueOf(downValidDays), sdf.format(cal.getTime()));
				SSMLabel ssmLabel = new SSMLabel("downloadRule", value);
				replace(ssmLabel);
				ssmLabel.setVisible(true);
				
				

				boolean isAllowDownload = false;
				
				if(UserEnvironmentHelper.isInternalUser()){
					isAllowDownload = true;
//					downloadCert.setVisible(true);
//					if(Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo()) ){
//						downloadBisnessInfo.setVisible(true);
//					}
				}else{
					if(new Date().after(cal.getTime())){
//						downloadCert.setVisible(false);
//						downloadBisnessInfo.setVisible(false);
						isAllowDownload = false;
					}else {
						isAllowDownload = true;
					}
				}
				
				if(isAllowDownload) {
					
					if(robFormB.getBusinessInfoData()!=null){
						if (Parameter.YES_NO_yes.equals(robFormB.getIsBuyInfo())) {
							downloadBisnessInfo.setDownloadData("BIZ_INFO"+robFormB.getBrNo()+".pdf", "application/pdf", robFormB.getBusinessInfoData().getFileData());
							downloadBisnessInfo.setVisible(true);
						}
					}
					if(robFormB.getCertFileData()!=null){
						downloadCert.setDownloadData("CERT_INFO"+robFormB.getBrNo()+".pdf", "application/pdf", robFormB.getCertFileData().getFileData());
						downloadCert.setVisible(true);
					}
					if(robFormB.getCmpNoticeFileData()!=null){
						downloadCmpNotice.setDownloadData("CMP_NOTE"+robFormB.getBrNo()+".pdf", "application/pdf", robFormB.getCmpNoticeFileData().getFileData());
						downloadCmpNotice.setVisible(true);
					}
				}
				
			}
			
			
			SSMAjaxButton paymentButton = new SSMAjaxButton("paymentButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					LlpPaymentFee formBPaymentFee  = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_FORM_B);
					LlpPaymentFee businessInfoPaymentFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
					
					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_B);
					paymentItem.setQuantity(1);
					paymentItem.setAmount(formBPaymentFee.getPaymentFee());
					paymentItem.setPaymentDet(robFormB.getBizName());
					listPaymentItems.add(paymentItem);

					if (robFormB.getBranchesAmt()>0) {
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_FORM_B_BRANCHES);
						paymentItem2.setQuantity(robFormB.getBalanceYear());
						paymentItem2.setPaymentDet(robFormB.getNewBranchCount()+" ");
						paymentItem2.setAmount(robFormB.getBranchesAmt());
						listPaymentItems.add(paymentItem2);
					}
//					
					
					if (robFormB.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentCmp = new LlpPaymentTransactionDetail();
						paymentCmp.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_COMPOUND);
						paymentCmp.setQuantity(1);
						paymentCmp.setPaymentDet(robFormB.getBrNo() + "-" + robFormB.getCheckDigit());
						paymentCmp.setAmount(robFormB.getCmpAmt());
						listPaymentItems.add(paymentCmp);
					}
					
					
					if(robFormB.getBizInfoAmt()>0){
						LlpPaymentTransactionDetail paymentItemBisInfo = new LlpPaymentTransactionDetail();
						paymentItemBisInfo.setPaymentItem(Parameter.PAYMENT_TYPE_BUSINESS_INFO);
						paymentItemBisInfo.setQuantity(1);
						paymentItemBisInfo.setPaymentDet("");
						paymentItemBisInfo.setAmount(robFormB.getBizInfoAmt());
						paymentItemBisInfo.setGstCode(businessInfoPaymentFee.getGstCode());
						if(Parameter.PAYMENT_GST_CODE_SR.equals(businessInfoPaymentFee.getGstCode())){
							paymentItemBisInfo.setGstAmt(businessInfoPaymentFee.getPaymentFee()*getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
						}
						
						listPaymentItems.add(paymentItemBisInfo);
					}

					setResponsePage(new PaymentDetailPage(robFormB.getRobFormBCode(), RobFormBService.class.getSimpleName(), robFormB,
							listPaymentItems));
				}
			};
			paymentButton.setVisible(false);
			add(paymentButton);
			
			SSMAjaxButton editBack = new SSMAjaxButton("editBack") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						robFormBService.updateFormBackToDraff(robFormB);
						setResponsePage(new EditRobFormBPage(robFormB.getRobFormBCode()));
					} catch (SSMException e) {
						ssmError(e);
						e.printStackTrace();
						String js = "alert('"+e.getMessage()+"');";
						target.appendJavaScript(js);
					}
				}
			};
			editBack.setVisible(false);
			add(editBack);
			
			if(Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_OTC.equals(robFormB.getStatus())){
				paymentButton.setVisible(true);
				boolean hasPendingOnline = llpPaymentTransactionService.hasPendingOnlineAndSuccessPaymentByAppRefNo(robFormB.getRobFormBCode());
				if(!hasPendingOnline){
					editBack.setVisible(true);
				}
			}
			
		}
		
		
	}

}
