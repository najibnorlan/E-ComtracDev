package com.ssm.common.mobile;


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
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.param.BusinessInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobRenewalPageMobile extends SecBasePageMobile {

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;

	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	private double renewalFeeTrade = 0;
	private double renewalFeePersonal = 0;
	private double branchFee = 0;
	private DecimalFormat currencyDecFormat = new DecimalFormat("#0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public EditRobRenewalPageMobile(final BusinessInfo businesInfo) {
		branchFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES).getPaymentFee();
		renewalFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE).getPaymentFee();
		renewalFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL).getPaymentFee();

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobRenewal robRenewal = new RobRenewal();
				robRenewal.setBizName(businesInfo.getBizName());
				robRenewal.setBizType(businesInfo.getBizType());
				robRenewal.setBranchCount(businesInfo.getBranchCount());
				robRenewal.setBrNo(businesInfo.getBrNo());
				robRenewal.setChkDigit(businesInfo.getChkDigit());
				robRenewal.setCmpAmt(businesInfo.getCmpAmount());
				robRenewal.setBranchCount(businesInfo.getBranchCount());
				robRenewal.setStatus(Parameter.ROB_RENEWAL_STATUS_DATA_ENTRY);
				if(robRenewal.getCmpAmt()>0){
					robRenewal.setPaidCmp(true);
				}
				try {
					robRenewal.setExpDate(sdf.parse(businesInfo.getEndDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				robRenewal.setYearRenew(1);
				
				double total = recalculateRenewal(robRenewal);
				robRenewal.setTotalAmt(total);
				
				return robRenewal;
			}
		}));
		init();
	}

	public EditRobRenewalPageMobile(final RobRenewal renewal) {
		branchFee = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES).getPaymentFee();
		renewalFeeTrade = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE).getPaymentFee();
		renewalFeePersonal = llpPaymentFeeService.findById(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL).getPaymentFee();

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return renewal;
			}
		}));
		init();
	}

	private void init() {
		add(new RobRenewalForm("form", getDefaultModel()));
	}


	private class RobRenewalForm extends Form implements Serializable {

		public RobRenewalForm(String id, IModel m) {
			super(id, m);

			final RobRenewal robRenewal = (RobRenewal) m.getObject();

			SSMLabel brNo = new SSMLabel("brNo", robRenewal.getBrNo() + "-" + robRenewal.getChkDigit());
			add(brNo);

			SSMLabel bizName = new SSMLabel("bizName", robRenewal.getBizName());
			add(bizName);

			SSMLabel bizType = new SSMLabel("bizType", robRenewal.getBizType(), Parameter.ROB_BIZ_TYPE);
			add(bizType);
			
			SSMLabel expDate = new SSMLabel("expDate", robRenewal.getExpDate());
			add(expDate);
			
			SSMLabel updateDt = new SSMLabel("updateDt", robRenewal.getUpdateDt());
			add(updateDt);
			
			SSMLabel newExpDate = new SSMLabel("newExpDate", robRenewal.getNewExpDate());
			add(newExpDate);

			SSMLabel totalBranch = new SSMLabel("branchCount", robRenewal.getBranchCount());
			add(totalBranch);

			final SSMLabel total = new SSMLabel("totalAmt", robRenewal.getTotalAmt());
			total.setOutputMarkupId(true);
			add(total);

			SSMLabel cmpAmt = new SSMLabel("cmpAmt", robRenewal.getCmpAmt());
			add(cmpAmt);
			
			SSMLabel downloadRule = new SSMLabel("downloadRule", new StringResourceModel("page.title.mybiz.editNote", this, null));
			add(downloadRule);
			downloadRule.setVisible(false);
			
			
//			SSMRadioChoice yearRenew = new SSMRadioChoice("yearRenew", Parameter.ROB_RENEWAL_YEAR);
			SSMDropDownChoice yearRenew = new SSMDropDownChoice("yearRenew", Parameter.ROB_RENEWAL_YEAR);
			
			
			yearRenew.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getModelObject();
						robRenewal.setYearRenew(obj.getYearRenew());

						double totalFee = recalculateRenewal(robRenewal);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						arg0.add(total);
					}
				}
			});
			
//			yearRenew.add(new AjaxFormChoiceComponentUpdatingBehavior() {
//				@Override
//				protected void onUpdate(AjaxRequestTarget arg0) {
//					if (StringUtils.isBlank(robRenewal.getTransCode())) {
//						RobRenewal obj = (RobRenewal) getModelObject();
//						robRenewal.setYearRenew(obj.getYearRenew());
//
//						double totalFee = recalculateRenewal(robRenewal);
//						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
//						obj.setTotalAmt(totalFee);
//						arg0.add(total);
//					}
//				}
//			});
			yearRenew.setOutputMarkupId(true);
			add(yearRenew);

			AjaxCheckBox isPaidCmp = new AjaxCheckBox("isPaidCmp", new PropertyModel<Boolean>(getDefaultModel(), "isPaidCmp")) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						RobRenewal obj = (RobRenewal) getForm().getModelObject();
						robRenewal.setPaidCmp(obj.isPaidCmp());

						double totalFee = recalculateRenewal(robRenewal);
						total.setDefaultModelObject(currencyDecFormat.format(totalFee));
						obj.setTotalAmt(totalFee);
						arg0.add(total);
					}

				}
			};

			isPaidCmp.setOutputMarkupId(true);
			add(isPaidCmp);

			final Button saveButton = new Button("save") {
				public void onSubmit() {
//					RobRenewal robRenewal = (RobRenewal) getForm().getDefaultModelObject();
					recalculateRenewal(robRenewal);

					double totalFee = 0;
					double renewalFee = 0;
					if (Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())) {
						renewalFee = renewalFeeTrade;
					} else {
						renewalFee = renewalFeePersonal;
					}
					double totalRenewalFee = robRenewal.getYearRenew() * renewalFee;
					double totalBranchFee = robRenewal.getBranchCount() * branchFee * robRenewal.getYearRenew();
					double totalCmpFee = robRenewal.getCmpAmt();


					if(StringUtils.isBlank(robRenewal.getTransCode())){
						robRenewal.setCreateBy(UserEnvironmentHelper.getLoginName());
						robRenewal.setCreateDt(new Date());
						robRenewalService.insert(robRenewal );
					}

					List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
					LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
					if(Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())){
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_TRADE);
					}else{//PERSONAL
						paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_PERSONAL);
					}
					
					paymentItem.setQuantity(robRenewal.getYearRenew());
					paymentItem.setAmount(totalRenewalFee);
					paymentItem.setPaymentDet(robRenewal.getBrNo() + "-" + robRenewal.getChkDigit() + "\t"
							+ robRenewal.getBizName());
					listPaymentItems.add(paymentItem);

					if (robRenewal.getBranchCount() > 0) {
						LlpPaymentTransactionDetail paymentItem2 = new LlpPaymentTransactionDetail();
						paymentItem2.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_BRANCHES);
						paymentItem2.setQuantity(robRenewal.getYearRenew());
						paymentItem2.setPaymentDet(robRenewal.getBranchCount()+"");
						paymentItem2.setAmount(totalBranchFee);
						listPaymentItems.add(paymentItem2);
					}

					if (robRenewal.isPaidCmp() && robRenewal.getCmpAmt() > 0) {
						LlpPaymentTransactionDetail paymentItem3 = new LlpPaymentTransactionDetail();
						paymentItem3.setPaymentItem(Parameter.PAYMENT_TYPE_ROB_RENEWAL_COMPOUND);
						paymentItem3.setQuantity(1);
						paymentItem3.setPaymentDet(robRenewal.getBrNo() + "-" + robRenewal.getChkDigit());
						paymentItem3.setAmount(totalCmpFee);
						listPaymentItems.add(paymentItem3);
					}

					setResponsePage(new PaymentDetailPageMobile(robRenewal.getTransCode(), RobRenewalService.class.getSimpleName(), robRenewal,
							listPaymentItems));
				}
			};
			add(saveButton);
			saveButton.setOutputMarkupId(true);
			saveButton.setEnabled(robRenewal.isDeclareChk());
			saveButton.setLabelKey("page.title.mybiz.payment");
			
			add(new Button("cancel") {
				public void onSubmit() {
					if (StringUtils.isBlank(robRenewal.getTransCode())) {
						setResponsePage(ListRobRenewalPageMobile.class);
					} else {
						setResponsePage(ListRobRenewalTransactionsPageMobile.class);
					}
				}
			}.setDefaultFormProcessing(false));
			
			
			SSMLink downloadCertificateButton = new SSMLink("downloadCertificateButton") {
				public void onClick() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_CERT.pdf", robRenewalFull.getCertFileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					if (StringUtils.isNotBlank(robRenewal.getTransCode())) {
						generateJsForMobile(robRenewal.getBrNo() + "_CERT.pdf", tag);
					}
				}
			};
			
			downloadCertificateButton.setVisible(false);
			add(downloadCertificateButton);
			
			SSMLink downloadCmpButton = new SSMLink("downloadCmpButton") {
				public void onClick() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_CMP.pdf", robRenewalFull.getCmpFileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					if (StringUtils.isNotBlank(robRenewal.getTransCode())) {
						generateJsForMobile(robRenewal.getBrNo() + "_CMP.pdf", tag);
					}
				}
				
			};
			
			downloadCmpButton.setVisible(false);
			add(downloadCmpButton);
			
			
			SSMLink downloadA1FormButton = new SSMLink("downloadA1FormButton") {
				public void onClick() {
					try {
						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
						generateDownload(robRenewalFull.getBrNo() + "_A1.pdf", robRenewalFull.getFormA1FileData().getFileData());
					} catch (SSMException e) {
						ssmError(e);
					}
				}
				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					if (StringUtils.isNotBlank(robRenewal.getTransCode())) {
						generateJsForMobile(robRenewal.getBrNo() + "_A1.pdf", tag);
					}
				}
			};
			
//			SSMLink downloadA1FormButton = new SSMLink("downloadA1FormButton", m) {
//				public void onClick() {
//					try {
//						final RobRenewal robRenewalFull = robRenewalService.findByIdWithData(robRenewal.getTransCode());
//						generateDownload(robRenewalFull.getBrNo() + "_A1.pdf", robRenewalFull.getFormA1FileData().getFileData());
//					} catch (SSMException e) {
//						ssmError(e);
//					}
//				}
//			};
			
			downloadA1FormButton.setVisible(false);
			add(downloadA1FormButton);
			
			
			final AjaxCheckBox declareChk= new AjaxCheckBox("declareChk", new PropertyModel(robRenewal, "declareChk")) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						saveButton.setEnabled(true);
						robRenewal.setDeclareChk(true);
					} else {
						robRenewal.setDeclareChk(false);
						saveButton.setEnabled(false);
					}
					target.add(saveButton);
				}
			};
			declareChk.setVisible(true);
			add(declareChk);
			

			
			if (StringUtils.isNotBlank(robRenewal.getTransCode())) {
				yearRenew.setEnabled(false);
				isPaidCmp.setEnabled(false);
				downloadA1FormButton.setVisible(true);
				
			} 
			if(Parameter.ROB_RENEWAL_STATUS_SUCCESS.equals(robRenewal.getStatus())){
				saveButton.setVisible(false);
				downloadRule.setVisible(true);
				declareChk.setVisible(false);
				
				int downValidDays = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.ROB_RENEWAL_CONFIG, Parameter.ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS));
				Calendar cal = Calendar.getInstance();
				cal.setTime(robRenewal.getUpdateDt());
				cal.add(Calendar.DATE, downValidDays);
				if(new Date().before(cal.getTime())){
					downloadCertificateButton.setVisible(true);
				}
				
				if(StringUtils.isNotBlank(robRenewal.getCmpNo())){
					downloadCmpButton.setVisible(true);
				}		
				
			}else{
				saveButton.setVisible(true);
				if(Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS.equals(robRenewal.getStatus())){
					saveButton.setLabelKey("page.title.mybiz.resubmit");
				}
			}
			
			
			
			

			setOutputMarkupId(true);
		}
	}
	
	public void generateJsForMobile(String fileName, final ComponentTag tag){
		String onclick = (String) tag.getAttributes().get("onclick");
		try {
			String urlPath= StringUtils.splitByWholeSeparator(onclick,"window.location.href='")[1];
	        urlPath = StringUtils.splitByWholeSeparator(urlPath,"'; }")[0];
	        String basePath = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(EditRobRenewalPageMobile.class,null).toString()));
	        String fullPath = basePath+"/"+urlPath;
	        onclick="sendLinkToParent('"+fileName+"','"+fullPath+"');"+onclick;
	        tag.getAttributes().put("onclick", onclick);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}	
	public void generateDownload(String fileName, final byte[] byteData){
		
		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(byteData);
			}
		};

		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}
	public double recalculateRenewal(RobRenewal robRenewal) {
		double totalAmt = 0;
		double renewalFee = 0;
		if (Parameter.ROB_BIZ_TYPE_TRADE.equals(robRenewal.getBizType())) {
			renewalFee = renewalFeeTrade;
		} else {
			renewalFee = renewalFeePersonal;
		}
		totalAmt = (robRenewal.getYearRenew() * renewalFee)
				+ (robRenewal.getBranchCount() * branchFee * robRenewal.getYearRenew());
		if (robRenewal.isPaidCmp()) {
			totalAmt += robRenewal.getCmpAmt();
		}
		totalAmt = totalAmt * 1.00;
		robRenewal.setTotalAmt(totalAmt);
		return totalAmt;

	}

	@Override
	public String getPageTitle() {
		return "page.title.mybiz.renewalDetail";
	}

}
