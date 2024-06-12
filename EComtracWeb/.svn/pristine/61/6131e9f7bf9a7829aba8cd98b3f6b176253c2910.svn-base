package com.ssm.ezbiz.myCardTransaction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.ezbiz.service.SSMMyKadModelService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMPasswordTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.ssmlocalsvr.SSMLocalSvrUtils;

@SuppressWarnings({ "all" })
public class ReadMyKadPage extends SecBasePage {

	@SpringBean(name = "SSMMyKadModelService")
	SSMMyKadModelService ssmMyKadModelService;
	
	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	

	public ReadMyKadPage() {
		
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SSMMyKadModel myKadModel = new SSMMyKadModel();
				myKadModel.setAdUserRequester(UserEnvironmentHelper.getUserenvironment().getLoginName());
				myKadModel.setIpAddress(getIpAddress());
				return myKadModel;
			}
		}));
		add(new ReadMyKadPageForm("form", getDefaultModel()));
	}

	private class ReadMyKadPageForm extends Form implements Serializable {
		final WebMarkupContainer wmcBasic,wmcContact,wmcEzBizRegInfo, wmcEzBizRegInfoExisting, wmcEzBizRegInfoNew, wmcEzBizRegInfoNewAddr, wmcError, wmcNotification;
		final SSMMyKadModel myKadModel;
		final SSMAjaxButton registerEzBiz;
		final SSMAjaxLink verifyThumprint, resetEzBiz, printDetail;
		final RepeatingView listError, listNotification;
		final List listIssue = new ArrayList();
		final ReadMyKadProcessingPanel processingPanel;
		
		
		
		
		public ReadMyKadPageForm(String id, IModel m) {
			super(id, m);
			setAutoCompleteForm(false);

			myKadModel = (SSMMyKadModel) m.getObject();

			
			IResource imageResource = new ContextRelativeResource("images/noimagefound.jpg");

//			if (myKadModel.getMykadPicture() != null) {
//				imageResource = new DynamicImageResource() {
//					@Override
//					protected byte[] getImageData(IResource.Attributes attributes) {
//						return myKadModel.getMykadPicture();
//					}
//				};
//			} else {
//				imageResource = new ContextRelativeResource("images/noimagefound.jpg");
//			}

			
			
			wmcBasic = new WebMarkupContainer("wmcBasic");
			wmcBasic.setOutputMarkupId(true);
			wmcBasic.setOutputMarkupPlaceholderTag(true);
			add(wmcBasic);
			
			NonCachingImage image = new NonCachingImage("wicketId", imageResource);
			image.setOutputMarkupId(true);
			image.setOutputMarkupPlaceholderTag(true);
			wmcBasic.add(image);

			wmcEzBizRegInfo = new WebMarkupContainer("wmcEzBizRegInfo");
			wmcEzBizRegInfo.setOutputMarkupId(true);
			wmcEzBizRegInfo.setOutputMarkupPlaceholderTag(true);
			wmcEzBizRegInfo.setVisible(false);
			add(wmcEzBizRegInfo);
			
			wmcEzBizRegInfoExisting = new WebMarkupContainer("wmcEzBizRegInfoExisting");
			wmcEzBizRegInfoExisting.setOutputMarkupId(true);
			wmcEzBizRegInfoExisting.setOutputMarkupPlaceholderTag(true);
			wmcEzBizRegInfoExisting.setVisible(false);
			wmcEzBizRegInfo.add(wmcEzBizRegInfoExisting);
			
			wmcEzBizRegInfoNew = new WebMarkupContainer("wmcEzBizRegInfoNew");
			wmcEzBizRegInfoNew.setOutputMarkupId(true);
			wmcEzBizRegInfoNew.setOutputMarkupPlaceholderTag(true);
			wmcEzBizRegInfoNew.setVisible(false);
			wmcEzBizRegInfo.add(wmcEzBizRegInfoNew);
			
			wmcEzBizRegInfoNewAddr = new WebMarkupContainer("wmcEzBizRegInfoNewAddr");
			wmcEzBizRegInfoNewAddr.setOutputMarkupId(true);
			wmcEzBizRegInfoNewAddr.setOutputMarkupPlaceholderTag(true);
			wmcEzBizRegInfoNewAddr.setVisible(false);
			wmcEzBizRegInfoNew.add(wmcEzBizRegInfoNewAddr);
			
			
			
			wmcContact = new WebMarkupContainer("wmcContact");
			wmcContact.setOutputMarkupId(true);
			wmcContact.setOutputMarkupPlaceholderTag(true);
			add(wmcContact);
			

			wmcError = new WebMarkupContainer("wmcError");
			wmcError.setOutputMarkupId(true);
			wmcError.setOutputMarkupPlaceholderTag(true);
			wmcError.setVisible(false);
			add(wmcError);
			
			
			listError = new RepeatingView("listError");
			listError.setOutputMarkupId(true);
			listError.setOutputMarkupPlaceholderTag(true);
			wmcError.add(listError); 
			
			
			wmcNotification = new WebMarkupContainer("wmcNotification");
			wmcNotification.setOutputMarkupId(true);
			wmcNotification.setOutputMarkupPlaceholderTag(true);
			wmcNotification.setVisible(false);
			add(wmcNotification);
			
			listNotification = new RepeatingView("listNotification");
			listNotification.setOutputMarkupId(true);
			listNotification.setOutputMarkupPlaceholderTag(true);
			wmcNotification.add(listNotification); 
			
			
			resetEzBiz = new SSMAjaxLink("resetEzBiz") {
				@Override
				public void onClick(AjaxRequestTarget target) {

					setResponsePage(new ReadMyKadPage());
				}
			};
			resetEzBiz.setOutputMarkupId(true);
			resetEzBiz.setOutputMarkupPlaceholderTag(true);
			resetEzBiz.setVisible(false);
			add(resetEzBiz);
			
			printDetail = new SSMAjaxLink("printDetail") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					
					String contextPath = getContextPath();
					
//					if(StringUtils.isNotBlank(contextPath) && !contextPath.startsWith("/")) {
//						contextPath = "/"+contextPath;
//					}
					String js = "function CallPrint(strid) {";
					
					js += "var prtContent = document.getElementById(strid);";
//					js += "var WinPrint = window.open('', '','left=0,top=0,toolbar=0,scrollbars=0,status=0');";
					js += "var WinPrint = window.open('', '','left=0,top=0,width=800,height=600,toolbar=0,scrollbars=0,status=0');";

					
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/alertify/alertify.css\">');";  
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/alertify/semantic.css\">');"; 
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/semantic/semantic.min.css\">');";  
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/styles.css\">');";  
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/animate.css\">');"; 
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/semantic/components/form.min.css\">');";
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/font-awesome/css/font-awesome.min.css\">');";
					js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/jquery/jquery-ui.css\">');";
					
					js += "WinPrint.document.write('<style>body{padding:0px 20px 0px 20px; margin: 0px}td{font-family:\"Tahoma\",\"LucidaSans\",\"Arial\",\"Helvetica\",\"Sans-serif\",\"sans\";font-size:9pt;padding:2px;}table#itemsTable{border:0.5px solid black;}table#itemsTable td{padding:4px;}table#gstSummary td{padding-right:50px;}</style>');";
					
					//Before Hide
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy  hh:mm:ssa");
					js += "document.getElementById('timeStamp').innerHTML='"+sdf.format(new Date())+" by "+ UserEnvironmentHelper.getUserenvironment().getFullName() +" ';";
					js += "$(\"#dontPrint\").hide();";
					
					
					js += "WinPrint.document.write(prtContent.innerHTML);";
					
					//Reset Back
					js += "document.getElementById('timeStamp').innerHTML='';";
					js += "$(\"#dontPrint\").show();";
					
					js += "WinPrint.document.close();";
					js += "WinPrint.focus();";
					js += "setTimeout(initprint, 1000);";
					js += "function initprint(){WinPrint.print();WinPrint.close();}";
					js += "}";
					
					
					
					
					target.prependJavaScript(js+"CallPrint('printdiv');");
				}
			};
			printDetail.setOutputMarkupId(true);
			printDetail.setOutputMarkupPlaceholderTag(true);
			printDetail.setVisible(false);
			add(printDetail);
			


			
			processingPanel = new ReadMyKadProcessingPanel("processingPanel");
			processingPanel.setOutputMarkupId(true);
			processingPanel.setOutputMarkupPlaceholderTag(true);
			add(processingPanel);
			
			

			final WebMarkupContainer wmcDownload = new WebMarkupContainer("wmcDownload");
			wmcDownload.setOutputMarkupId(true);
			wmcDownload.setOutputMarkupPlaceholderTag(true);
			wmcDownload.setVisible(false);
			add(wmcDownload);
			
			
			final HiddenField hiddenInput = new HiddenField("hiddenInput", Model.of("") );
			hiddenInput.setOutputMarkupId(true);
			add(hiddenInput);
			
			AjaxFormComponentUpdatingBehavior behavior = new AjaxFormComponentUpdatingBehavior("onchange") {
				
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
					wmcDownload.setVisible(false);
//					String jScript1 = "$('#"+processingPanel.getWmcAlertId()+"').modal('hide');";	
//					arg0.prependJavaScript(jScript1);

					String jScript = "";
					
					String hexResponseMsg = hiddenInput.getValue();
					
					
//					boolean isNormalStr = false;
//					if(hexResponseMsg.indexOf("<END2>")!=-1) {
//						isNormalStr = true;
//						hexResponseMsg = StringUtils.remove(hexResponseMsg, "<END2>");
//					}else {
//						hexResponseMsg = StringUtils.remove(hexResponseMsg, "<END>");
//					}
					
					
					listNotification.removeAll();
					
					try {
						List listResponse = WicketUtils.getSSMSvrResponse(hexResponseMsg);
//						
//						if(isNormalStr) {
//							byte byteResponse[] = Hex.decodeHex(hexResponseMsg.toCharArray());
//							String responseStr[] = new String(byteResponse).split(":");
//							listResponse.add(responseStr[0]);
//							listResponse.add(responseStr[1]);
//						}else {
//						
//							byte byteResponse[] = Hex.decodeHex(hexResponseMsg.toCharArray());
//							ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteResponse));
//							while(1==1) {
//								try {
//									listResponse.add(ois.readObject());
//								} catch (Exception e) {
//									break;
//								}
//							}
//							ois.close();
//						}
						
					
						String responseStatus = (String) listResponse.get(0);
						
						if("00".equals(responseStatus)) {
							String cmd = (String) listResponse.get(1);
							if("MYKAD:READ".equals(cmd)) {
								
								Map objectResponse = (Map) listResponse.get(2);
								SSMLocalSvrUtils.mapToObject( objectResponse, myKadModel);
								rePopulate(arg0);
								
								
//								jScript += "$('#" + processingPanel.getWmcAlertId() + "').modal({ offset: 0,closable : false }).modal('show'); ";
								jScript += "function callVerify(){ " ;		
								jScript += "document.getElementById('"+verifyThumprint.getId()+"').click();";
								jScript += " }";
								jScript += "setTimeout(callVerify, 1000);";
								
							}else if("MYKAD:VERIFY".equals(cmd)) {
								boolean isVerify = (Boolean) listResponse.get(2);
								
								if(isVerify) {
									myKadModel.setThumbPrintSuccess(Parameter.MYKAD_THUMBPRINT_STATUS_VERIFIED);
								}else {
									myKadModel.setThumbPrintSuccess(Parameter.MYKAD_THUMBPRINT_STATUS_FAILED);
								}
								rePopulate(arg0);
								jScript = "$('#"+processingPanel.getWmcAlertId()+"').modal('hide');";
								jScript += "$('#"+processingPanel.getWmcAlertId()+"').modal('hideDimmer');";
								
							}else {
								jScript = "$('#"+processingPanel.getWmcAlertId()+"').modal('hide');";
								listNotification.add(new SSMLabel(listNotification.newChildId() , "Command Issue!! - "+cmd ).addStyle("color:red"));
							}
							
						}else {
							jScript = "$('#"+processingPanel.getWmcAlertId()+"').modal('hide');";
							listNotification.add(new SSMLabel(listNotification.newChildId() , listResponse.get(1) ).addStyle("color:red"));
							if(responseStatus.indexOf("13")!=-1) {
								wmcDownload.setVisible(true);
							}
						}
						
						
					} catch (Exception e) {
						jScript = "$('#"+processingPanel.getWmcAlertId()+"').modal('hide');";
						listNotification.add(new SSMLabel(listNotification.newChildId() , "Please Try Again!!" ).addStyle("color:red"));
					}
					arg0.add(wmcDownload);
					arg0.prependJavaScript(jScript);
					generateNotification(arg0);
				}

			};
			hiddenInput.add(behavior);
			
			
			final SSMAjaxLink readMyKad = new SSMAjaxLink("readMyKad") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					
					System.out.println("read card");
					try {
						

						
						String adUser = myKadModel.getAdUserRequester();
						SSMLocalSvrUtils.clearObjectValue(myKadModel);
						myKadModel.setAdUserRequester(adUser);
						myKadModel.setIpAddress(getIpAddress());
						
						String thumbPrintPut = resolve("page.ssm.ezbiz.myCardDetailPage.insertMyKad");
						processingPanel.resetData(thumbPrintPut, "images/insertSmardCard.gif", target);
						
						ByteArrayOutputStream baosT = new ByteArrayOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(baosT);
						oos.writeObject("MYKAD:READ");
						oos.writeObject(new String[] { "CT01", "Generic EMV" });
						oos.close();
						byte byteDataLatest[] = baosT.toByteArray();		
						baosT.close();
						
						String cmdToSend = Hex.encodeHexString(byteDataLatest).toUpperCase();
						cmdToSend+="<END>";
//						String jScript = "$('#" + processingPanel.getWmcAlertId() + "').modal({ offset: 0,closable : false }).modal('show'); connect('"
//								+ cmdToSend + "','" + processingPanel.getWmcAlertId() + "');";
						
						String jScript = "showModal('"+ processingPanel.getWmcAlertId() + "'); connect('"
								+ cmdToSend + "','" + processingPanel.getWmcAlertId() + "');";
						target.appendJavaScript(jScript);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			add(readMyKad);
			
			
			registerEzBiz = new SSMAjaxButton("registerEzBiz", "ezbizRegValidation") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					listError.removeAll();
					wmcError.setVisible(false);
					try {
						LlpUserProfile llpUserProfile = ssmMyKadModelService.registerOrActivateUser(myKadModel);
						
						String msg = resolve("page.ssm.ezbiz.myCardDetailPage.successRegAndActivateUserDesc", llpUserProfile.getName(), llpUserProfile.getLoginId(), llpUserProfile.getEmail());
						
						String scriptAddt="document.getElementById('"+resetEzBiz.getId()+"').click();";
						String script = generateAjaxSuccessAlertScript(resolve("page.ssm.ezbiz.myCardDetailPage.successRegAndActivateUserTitle"), msg, scriptAddt);
						target.appendJavaScript(script);
//						
					} catch (Exception e) {
						listError.add(new SSMLabel(listError.newChildId() ,e.getMessage()));
						wmcError.setVisible(true);
					}
					target.add(wmcError);
					
				}
			};

			registerEzBiz.setOutputMarkupId(true);
			registerEzBiz.setOutputMarkupPlaceholderTag(true);
			registerEzBiz.setVisible(false);
			add(registerEzBiz);
			
			
			verifyThumprint = new SSMAjaxLink("verifyThumprint") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					
					try {
						String thumbPrintPut = resolve("page.ssm.ezbiz.myCardDetailPage.putThumbprint");
						processingPanel.resetData(thumbPrintPut, "images/verifyThumb.gif", target);
						
						ByteArrayOutputStream baosT = new ByteArrayOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(baosT);
						oos.writeObject("MYKAD:VERIFY");
						oos.writeObject(new String[] { "CT01", "Generic EMV" });
						oos.writeObject(myKadModel.getMykadRefNo());
						oos.close();
						byte byteDataLatest[] = baosT.toByteArray();		
						baosT.close();
						
						String cmdToSend = Hex.encodeHexString(byteDataLatest).toUpperCase();
						cmdToSend+="<END>";

						String jScript = "showModal('"+ processingPanel.getWmcAlertId() + "'); connect('"
								+ cmdToSend + "','" + processingPanel.getWmcAlertId() + "');";
						target.appendJavaScript(jScript);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			};
			verifyThumprint.setOutputMarkupId(true);
			verifyThumprint.setOutputMarkupPlaceholderTag(true);
			verifyThumprint.setVisible(false);
			add(verifyThumprint);
			
//			SSMAjaxButton downloadSSMClient = new SSMAjaxButton("downloadSSMClient") {
//				@Override
//				public void onSubmit() {
//					
//				}
//			};
//			add(downloadSSMClient);
			
//			SSMDownloadLink linkErrorDownload = new SSMDownloadLink("downloadSSMClient") {
//				public void onClick() {
//					if(getByteData()!=null){
//						super.onClick();
//						return;
//					}
//					try {
//						String fileName = "ERROR-LOG-EZBIZ-"+DateUtil.formatDate(new Date(),"yyyyMMdd")+".txt";
//						
//						ByteArrayOutputStream baos = new ByteArrayOutputStream();
//						PrintWriter pw = new PrintWriter(baos);
//						for (int i = 0; i < listError.size(); i++) {
//							pw.println("ErrorId : "+listError.get(i).getErrorId());
//							pw.println("UserId : "+listError.get(i).getUserId());
//							pw.println("DateTime : "+sdf.format(listError.get(i).getLogTime()));
//							pw.println(listError.get(i).getErrorMsg());
//						}
//						pw.flush();
//						
//						byte byteData[] = baos.toByteArray();
//						baos.close();
//						
//						setDownloadData(fileName, "application/txt", byteData);
//						super.onClick();
//					} catch (Exception e) {
//						ssmError(e.getMessage());
//						e.printStackTrace();
//					} 
//				}
//
//			};
//			add(linkErrorDownload);
			
			
			rePopulate(null);
		}

		private void generateNotification(AjaxRequestTarget target) {
			wmcNotification.setVisible(false);
			if(listNotification.size()>0) {
				wmcNotification.setVisible(true);
			}
			target.add(wmcNotification);
		}
		
		void rePopulate(AjaxRequestTarget target){
			
			
			if (myKadModel.getMykadPicture() != null) {
				DynamicImageResource imageResource = new DynamicImageResource() {
					@Override
					protected byte[] getImageData(IResource.Attributes attributes) {
						return myKadModel.getMykadPicture();
					}
				};
				NonCachingImage image = new NonCachingImage("wicketId", imageResource);
				wmcBasic.addOrReplace(image);
				target.add(image);
			}
			wmcEzBizRegInfo.setVisible(false);
			wmcEzBizRegInfoNew.setVisible(false);
			wmcEzBizRegInfoNewAddr.setVisible(false);
			
			wmcEzBizRegInfoExisting.setVisible(false);
			verifyThumprint.setVisible(false);
			registerEzBiz.setVisible(false);
			listError.removeAll();
			wmcError.setVisible(false);
			resetEzBiz.setVisible(false);
			
			wmcBasic.addOrReplace(new SSMLabel("name", myKadModel.getName()));
			wmcBasic.addOrReplace(new SSMLabel("originalName", myKadModel.getOriginalName()));
			wmcBasic.addOrReplace(new SSMLabel("gmpcName", myKadModel.getGmpcName()));
			wmcBasic.addOrReplace(new SSMLabel("mykadNo", myKadModel.getMykadNo()));
			wmcBasic.addOrReplace(new SSMLabel("dob", myKadModel.getDob()));
			wmcBasic.addOrReplace(new SSMLabel("birthPlace", myKadModel.getBirthPlace()));
			wmcBasic.addOrReplace(new SSMLabel("citizenship", myKadModel.getCitizenship()));
			wmcBasic.addOrReplace(new SSMLabel("race", myKadModel.getRace()));
			wmcBasic.addOrReplace(new SSMLabel("religion", myKadModel.getReligion()));
			wmcBasic.addOrReplace(new SSMLabel("gender", myKadModel.getGender(), Parameter.GENDER));
			
			
			SSMLabel thumbPrintSuccess = new SSMLabel("thumbPrintSuccess", myKadModel.getThumbPrintSuccess(), Parameter.MYKAD_THUMBPRINT_STATUS);
			if(Parameter.MYKAD_THUMBPRINT_STATUS_VERIFIED.equals(myKadModel.getThumbPrintSuccess())) {
				thumbPrintSuccess.addStyle("color:green;font-weight: bolder;");
			}else {
				thumbPrintSuccess.addStyle("color:red;font-weight: bolder;");
			}
			wmcContact.addOrReplace(thumbPrintSuccess);
			String address = myKadModel.getAddress1();
			if (StringUtils.isNotBlank(myKadModel.getAddress2())) {
				address += "\n" + myKadModel.getAddress2();
			}
			if (StringUtils.isNotBlank(myKadModel.getAddress3())) {
				address += "\n" + myKadModel.getAddress3();
			}

			address = address;
			
			wmcContact.addOrReplace(new MultiLineLabel("address", address));
			wmcContact.addOrReplace(new SSMLabel("postcode", myKadModel.getPostcode()));
			wmcContact.addOrReplace(new SSMLabel("city", myKadModel.getCity()));
			wmcContact.addOrReplace(new SSMLabel("state", myKadModel.getState()));
			
			
			if(StringUtils.isNotBlank(myKadModel.getMykadNo())) {
				resetEzBiz.setVisible(true);
				printDetail.setVisible(true);
				
				if(Parameter.MYKAD_THUMBPRINT_STATUS_VERIFIED.equals(myKadModel.getThumbPrintSuccess())) {
					
					LlpUserProfile llpUserProfile = llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, myKadModel.getMykadNo());
					
					if(llpUserProfile==null) {
						registerEzBiz.setVisible(true);
						wmcEzBizRegInfo.setVisible(true);
						ssmMyKadModelService.insert(myKadModel);
						wmcEzBizRegInfoNew.setVisible(true);
						wmcEzBizRegInfoNewAddr.setVisible(true);
						myKadModel.setEzBizAddress1(myKadModel.getAddress1());
						myKadModel.setEzBizAddress2(myKadModel.getAddress2());
						myKadModel.setEzBizAddress3(myKadModel.getAddress3());
						myKadModel.setEzBizCity(myKadModel.getCity());
						myKadModel.setEzBizPostcode(myKadModel.getPostcode());
						myKadModel.setEzBizState(myKadModel.getState());
						
					}else {
						wmcEzBizRegInfoExisting.setVisible(true);
						if(Parameter.USER_STATUS_pending.equals(llpUserProfile.getUserStatus())) {
							//activate user
							try {
								ssmMyKadModelService.registerOrActivateUser(myKadModel);
								
								String msj = resolve("page.ssm.ezbiz.myCardDetailPage.successActivateUserDesc", llpUserProfile.getName(), llpUserProfile.getLoginId(), llpUserProfile.getEmail());
								listNotification.add(new SSMLabel(listNotification.newChildId() , msj  ));
								
							} catch (Exception e) {
								e.printStackTrace();
								listNotification.add(new SSMLabel(listNotification.newChildId() , e.getMessage()  ).addStyle("color:red") );
							}
						}else if(Parameter.USER_STATUS_active.equals(llpUserProfile.getUserStatus())) {
							//user already activated
							
							String msg = resolve("page.ssm.ezbiz.myCardDetailPage.userAlreadyActivatedDesc", llpUserProfile.getName(), llpUserProfile.getLoginId(), llpUserProfile.getEmail()) ;
							
							listNotification.add(new SSMLabel(listNotification.newChildId() , msg));
						}else {
							//user suspend n inactive
							
							String msg = resolve("page.ssm.ezbiz.myCardDetailPage.userOtherStatusDesc", llpUserProfile.getName(), llpUserProfile.getLoginId(), llpUserProfile.getEmail(),llpUserProfile.getUserStatus()) ;
							
							listNotification.add(new SSMLabel(listNotification.newChildId() , msg));
						}
						
					}
					
					
				}else {
					verifyThumprint.setVisible(true);
				}
			}
			wmcEzBizRegInfoNew.addOrReplace(new SSMTextField("ezBizLoginId", new PropertyModel(myKadModel, "ezBizLoginId")));
			
			SSMTextField ezBizEmail = new SSMTextField("ezBizEmail", new PropertyModel(myKadModel, "ezBizEmail"));
			ezBizEmail.setUpperCase(false);
			wmcEzBizRegInfoNew.addOrReplace(ezBizEmail);
			
			
			wmcEzBizRegInfoNew.addOrReplace(new SSMTextField("ezBizPhoneNo", new PropertyModel(myKadModel, "ezBizPhoneNo")));
			
			SSMPasswordTextField ezBizPassword = new SSMPasswordTextField("ezBizPassword", new PropertyModel(myKadModel, "ezBizPassword"));
			wmcEzBizRegInfoNew.addOrReplace(ezBizPassword);
			
			SSMPasswordTextField ezBizPasswordTmp = new SSMPasswordTextField("ezBizPasswordTmp", new PropertyModel("", ""));
			wmcEzBizRegInfoNew.addOrReplace(ezBizPasswordTmp);
			
			
			wmcEzBizRegInfoNewAddr.addOrReplace(new SSMTextField("ezBizAddress1", new PropertyModel(myKadModel, "ezBizAddress1")));
			wmcEzBizRegInfoNewAddr.addOrReplace(new SSMTextField("ezBizAddress2", new PropertyModel(myKadModel, "ezBizAddress2")));
			wmcEzBizRegInfoNewAddr.addOrReplace(new SSMTextField("ezBizAddress3", new PropertyModel(myKadModel, "ezBizAddress3")));
			final SSMTextField ezBizPostcode = new SSMTextField("ezBizPostcode", new PropertyModel(myKadModel, "ezBizPostcode"));
			wmcEzBizRegInfoNewAddr.addOrReplace(ezBizPostcode);
			
			
//			System.out.println(ezBizPostcode.getForm().getDefaultModelObject());
//			System.out.println(ezBizPostcode.getForm().getModelObject());
//			System.out.println(myKadModel);
			
			
			WicketUtils.generatePostcodeTownState(wmcEzBizRegInfoNewAddr, ezBizPostcode, myKadModel, "ezBizPostcode","ezBizCity","ezBizState" , false );
			
			
			final String ezbizRegValidationJS = "ezbizRegValidation";
			String mainFieldToValidate[] = new String[]{"ezBizLoginId","ezBizEmail","ezBizPhoneNo","ezBizPassword","ezBizPasswordTmp","ezBizAddress1","ezBizPostcode","ezBizCityTmp"};
			String mainFieldToValidateRules[] = new String[]{"usernameReg","email","minLengthNumber[10]","password","match[ezBizPassword]","empty","exactLengthNumber[5]","empty"};
			setSemanticJSValidation(this, ezbizRegValidationJS, mainFieldToValidate, mainFieldToValidateRules);
			
			if(target!=null) {
				target.add(wmcEzBizRegInfo);
				target.add(wmcEzBizRegInfoNew);
				target.add(wmcEzBizRegInfoExisting);
				target.add(registerEzBiz);
				target.add(verifyThumprint);
				target.add(wmcBasic);
				target.add(wmcContact);
				target.add(wmcError);
				target.add(resetEzBiz);
				target.add(printDetail);
				target.add(wmcNotification);
				
			}
			
//			System.out.println("Done");
		}
	}

	@Override
	public String getPageTitle() {
		return "page.title.ezbiz.myCardDetailPage";
	}

}
