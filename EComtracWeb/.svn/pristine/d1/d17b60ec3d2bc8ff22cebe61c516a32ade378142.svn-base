package com.ssm.llp.page.supplyinfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.PDFMerger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ssm.common.util.PDFUtils;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LlpSupplyInfoDetailPanel extends BasePanel {

	@SpringBean(name = "LlpSupplyInfoDtlService")
	private LlpSupplyInfoDtlService llpSupplyInfoDtlService;

	@SpringBean(name = "LlpSupplyInfoHdrService")
	private LlpSupplyInfoHdrService llpSupplyInfoHdrService;

	private WebMarkupContainer wmc;
	private SSMDataView dataView;
	private SSMSortableDataProvider dp;
	private InputForm inputFormInfoDetailPanel;

	public LlpSupplyInfoDetailPanel(String panelId, String hdrCode) {
		super(panelId);
		
		inputFormInfoDetailPanel = new InputForm("inputFormInfoItemPanel", hdrCode);
		add(inputFormInfoDetailPanel);
	}

	public void refreshContainer(AjaxRequestTarget target, String hdrCode) {
		inputFormInfoDetailPanel.refreshContainer(target, hdrCode);
	}

	/** form for processing the input. */
	private class InputForm extends Form {

		public InputForm(String name,final String hdrCode) {
			super(name);
			
			
			/*
			 * First modal window
			 */

			final ModalWindow uploadProfileOrCertPopUp = new ModalWindow("uploadProfileOrCertPopUp");
			add(uploadProfileOrCertPopUp);
			
			final LlpSupplyInfoHdr supplyInfoHdr =  llpSupplyInfoHdrService.findById(hdrCode);
			uploadProfileOrCertPopUp.setCookieName("modal-LlpSupplyInfoDetailPanel3");
			uploadProfileOrCertPopUp.setResizable(true);
			uploadProfileOrCertPopUp.setWidthUnit("px"); 
			uploadProfileOrCertPopUp.setHeightUnit("px"); 
			uploadProfileOrCertPopUp.setInitialWidth(500);
			uploadProfileOrCertPopUp.setInitialHeight(200);
			

			uploadProfileOrCertPopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					refreshContainer(target, hdrCode);
				}
			});

			uploadProfileOrCertPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				@Override
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					return true;
				}
			});
			
			
			
			
			SearchCriteria sc  = new SearchCriteria("hdrCode", SearchCriteria.EQUAL, hdrCode);
			
			dp = new SSMSortableDataProvider("dtlId", sc, LlpSupplyInfoDtlService.class);
			dataView = new SSMDataView<LlpSupplyInfoDtl>("sorting", dp) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final Item<LlpSupplyInfoDtl> item) { 
																					
					final LlpSupplyInfoDtl infoDtl = item.getModelObject();
					item.add(new SSMLabel("entityNo", infoDtl.getEntityNo()));
					item.add(new SSMLabel("entityName", infoDtl.getEntityName()));
					item.add(new SSMLabel("profileStatus", infoDtl.getProfileStatus(), Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS));
					item.add(new SSMLabel("certStatus", infoDtl.getCertStatus(), Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS));

					SSMLink downloadPerLLP = new SSMLink("downloadPerLLP", item.getDefaultModel()) {
						public void onClick() {
							
							if(infoDtl.getProfileData()!=null){
								AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {

						            @Override
						            public void write(OutputStream output) throws IOException {
						            	
						            	if(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getProfileStatus()) && Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getCertStatus()) ){
						            		//need to combine PDF
						            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
						            		PDFMergerUtility mergerUtility = new PDFMergerUtility();
						            		mergerUtility.addSource(new ByteArrayInputStream(infoDtl.getProfileData()));
						            		mergerUtility.addSource(new ByteArrayInputStream(infoDtl.getCertData()));
						            		mergerUtility.setDestinationStream(baos);
						            		try {
												mergerUtility.mergeDocuments();
											} catch (COSVisitorException e) {
												e.printStackTrace();
											}
						            		output.write(baos.toByteArray());
						            		baos.close();
						            	}else if(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getProfileStatus())){
						            		output.write(infoDtl.getProfileData());
						            	}else if(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getCertStatus())){
						            		output.write(infoDtl.getCertData());
						            	}
						                
						            }
						        };

						        ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, infoDtl.getEntityNo()+"_INFO.pdf");        
						        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
//								
							}
						}
					};
					item.add(downloadPerLLP);
					downloadPerLLP.setVisible(false);
					if(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getProfileStatus()) || Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getCertStatus()) ){
						downloadPerLLP.setVisible(true);
					}
					
					
					
					SSMLabel ssmLabelExpired = new SSMLabel("expiredDtLbl","");
					ssmLabelExpired.setVisible(false);
					item.add(ssmLabelExpired);
					if(supplyInfoHdr.getExpiredDt()!=null && (new Date()).after(supplyInfoHdr.getExpiredDt())){
						downloadPerLLP.setVisible(false);
						ssmLabelExpired.setVisible(true);
						if(UserEnvironmentHelper.isInternalUser()){
							downloadPerLLP.setVisible(true);
						}
					}
					
					SSMAjaxLink uploadProfile = new SSMAjaxLink<Void>("uploadProfile") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							uploadProfileOrCertPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new LlpSupplyInfoUploadPopUpPage(LlpSupplyInfoDetailPanel.this, uploadProfileOrCertPopUp, Parameter.SUPPLY_INFO_DTL_UPLOAD_TYPE_profile, infoDtl);
								}
							});
							
							uploadProfileOrCertPopUp.show(target);
						}
					};
					item.add(uploadProfile);
					uploadProfile.setVisible(false);
					uploadProfile.setLabelKey("llpSupplyInfo.page.upload");
					if( Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getProfileStatus())   ){
						uploadProfile.setLabelKey("llpSupplyInfo.page.reUpload");
					}
					if( Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process.equals(infoDtl.getProfileStatus()) || Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getProfileStatus())   ){
						if(UserEnvironmentHelper.isInternalUser()){
							uploadProfile.setVisible(true);
						}
					}
					
					
					
					SSMAjaxLink uploadCert = new SSMAjaxLink<Void>("uploadCert") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							uploadProfileOrCertPopUp.setPageCreator(new ModalWindow.PageCreator() {
								@Override
								public Page createPage() {
									return new LlpSupplyInfoUploadPopUpPage(LlpSupplyInfoDetailPanel.this, uploadProfileOrCertPopUp, Parameter.SUPPLY_INFO_DTL_UPLOAD_TYPE_cert, infoDtl);
								}
							});
							
							uploadProfileOrCertPopUp.show(target);
						}
					};
					item.add(uploadCert);
					uploadCert.setVisible(false);
					uploadCert.setLabelKey("llpSupplyInfo.page.upload");
					if( Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getCertStatus())   ){
						uploadCert.setLabelKey("llpSupplyInfo.page.reUpload");
					}
					if(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process.equals(infoDtl.getCertStatus()) || Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete.equals(infoDtl.getCertStatus()) ){
						if(UserEnvironmentHelper.isInternalUser()){
							uploadCert.setVisible(true);
						}
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

			dataView.setItemsPerPage(5L);
			
			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			
			
			wmc.add(dataView);
			wmc.add(new PagingNavigator("navigator", dataView));
			wmc.add(new NavigatorLabel("navigatorLabel", dataView));
			wmc.setOutputMarkupId(true);
			add(wmc);


		}

		public void refreshContainer(AjaxRequestTarget target, String hdrCode) {
			SearchCriteria sc  = new SearchCriteria("hdrCode", SearchCriteria.EQUAL, hdrCode);
			dp.setSc(sc);
			target.add(wmc);
		}


	}

}
