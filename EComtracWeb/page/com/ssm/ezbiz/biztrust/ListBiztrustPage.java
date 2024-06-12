package com.ssm.ezbiz.biztrust;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.hibernate.LazyInitializationException;

import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.integrasi.ws.request.GenerateQRCodeTextReq;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.CommonIntegrationService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.webis.param.BusinessInfo;

public class ListBiztrustPage  extends SecBasePage {

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	@SpringBean(name = "CommonIntegrationService")
	CommonIntegrationService commonIntegrationService;
	
	private final SSMQRResource qrCodeResource;
	private NonCachingImage qrCodeimg;
	
	
	public ListBiztrustPage() {
		
			String icNo = "";
			if(!UserEnvironmentHelper.isInternalUser()){
				LlpUserEnviroment enviroment = (LlpUserEnviroment) UserEnvironmentHelper.getUserenvironment();
				icNo = enviroment.getLlpUserProfile().getIdNo();
			}
			
			List<BusinessInfo> businessInfoList = new ArrayList();
			
			try {
				businessInfoList = robRenewalService.findListRobRenewalByIcWS(icNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			final WebMarkupContainer wmcQRCode = new WebMarkupContainer("wmcQRCode");
			wmcQRCode.setOutputMarkupId(true);
			wmcQRCode.setOutputMarkupPlaceholderTag(true);
			add(wmcQRCode);
			
			
			qrCodeResource = new SSMQRResource();
			qrCodeimg = new NonCachingImage("qrCodeimg", qrCodeResource);
			qrCodeimg.setOutputMarkupId(true);
			wmcQRCode.add(qrCodeimg);
			
			wmcQRCode.add(new SSMLabel("downloadQR","dummy"));
			
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("brNo", businessInfoList);
			final SSMDataView<BusinessInfo> dataView = new SSMDataView<BusinessInfo>("biztrustList", dp) {

				protected void populateItem(final Item<BusinessInfo> item) {
					final BusinessInfo businessInfo = item.getModelObject();
					
					item.add(new SSMLabel("idx", (item.getIndex()+1) ));
					
					item.add(new SSMLabel("brNo", businessInfo.getBrNo() + "-" + businessInfo.getChkDigit()));
					item.add(new SSMLabel("brNoNewFormat", businessInfo.getBrNoNewFormat()));
					item.add(new SSMLabel("brName", businessInfo.getBizName()));
					
					item.add(new SSMAjaxLink("viewQR") {

						@Override
						public void onClick(AjaxRequestTarget arg0) {
							
							GenerateQRCodeTextReq codeTextReq = new GenerateQRCodeTextReq();
							codeTextReq.setEntityNo(businessInfo.getBrNo()+"-"+businessInfo.getChkDigit());
							codeTextReq.setEntityNoNew(businessInfo.getBrNoNewFormat());
							codeTextReq.setEntityType("ROB");
							codeTextReq.setVersion("2");
							
							String qrText = commonIntegrationService.generateQRCodeText(codeTextReq);
							
							if(qrText!=null) {
								String entityName = businessInfo.getBizName();
								final String entityNo = businessInfo.getBrNoNewFormat()+" ("+businessInfo.getBrNo()+"-"+businessInfo.getChkDigit()+")";
								
								
								final byte byteQRImage[] = qrCodeResource.generateImage(qrText,entityNo,entityName);
								
								SSMLink  downloadQR = new SSMLink("downloadQR") {
									@Override
									public void onClick() {
	
										IResourceStream resourceStream = new AbstractResourceStreamWriter() {
											@Override
											public void write(OutputStream output) {
												try {
													output.write(byteQRImage);
												} catch (IOException e) {
													e.printStackTrace();
												}
											}
	
											@Override
											public String getContentType() {
												return "image/png";
											}
										};
	
										ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream).setFileName("QR"+entityNo + ".png");
										rs.setCacheDuration(Duration.NONE);
										getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);
									}
								};
								wmcQRCode.replace(downloadQR);
								
								String jScript = "showModal('"+wmcQRCode.getMarkupId()+"');";
								arg0.appendJavaScript(jScript);
								arg0.add(wmcQRCode);
							}else {
								String errorMWScript = generateAjaxErrorAlertScript("page.lbl.ezbiz.bizTrust.error.mwErrorTitle", "page.lbl.ezbiz.bizTrust.error.mwErrorDesc");
								arg0.appendJavaScript(errorMWScript);
							}
						}
					});

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));

				}

			};

			add(dataView);
			add(new SSMPagingNavigator("navigator", dataView));
			add(new NavigatorLabel("navigatorLabel", dataView));
			
		
	}
	
	
	@Override
	public String getPageTitle() {
		return this.getClass().getName();
	}
}
