package com.ssm.llp.base.page;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.ssm.common.htmleditor.HtmlEditorPanel;
import com.ssm.ezbiz.dashboard.DashboardRenewalAlert;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpLocaleMessage;
import com.ssm.llp.base.common.service.LlpAppsvrNodeService;
import com.ssm.llp.base.common.service.LlpLocaleMessageService;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class LlpLocaleMessagePage extends SecBasePage {
	@SpringBean(name="LlpLocaleMessageService")
	private LlpLocaleMessageService llpLocaleMessageService;
	
	@SpringBean(name="LlpAppsvrNodeService")
	private LlpAppsvrNodeService llpAppsvrNodeService;
	
	public LlpLocaleMessagePage() {
//		WicketApplication.getResourceLoader().exportToDb(null,"D:/workspaces/NewFramework/LLPWeb/WebContent/com/ssm/llp/base/page/WicketApplication.properties");
		
		add(new LocaleForm("localeForm"));
	}
	
	
	private class LocaleForm extends Form {

		private String keySearch;
		private String msgSearch;
		private String localeSearch;
		private WebMarkupContainer wmc;
		private SSMDataView dataView;
		private SSMSortableDataProvider dp;
		final HtmlEditorPanel htmlEditorPanel;

		public LocaleForm(String name) {
			super(name);
			populateTable();
			SSMTextField tf = new SSMTextField("keySearch", new PropertyModel(this, "keySearch"));
			tf.setUpperCase(false);
			tf.setLabelKey("llpLocale.page.key");
			add(tf);
			
			SSMTextField msgSearchTf = new SSMTextField("msgSearch", new PropertyModel(this, "msgSearch"));
			msgSearchTf.setUpperCase(false);
			msgSearchTf.setLabelKey("llpLocale.page.msg");
			add(msgSearchTf);
			
//			setLocaleSearch(Parameter.LOCALE_TYPE_default);
//			SSMDropDownChoice msgSearchChoice = new SSMDropDownChoice("localeSearch", new PropertyModel(this, "msgSearch"), Parameter.LOCALE_TYPE);
//			msgSearchChoice.setReadOnly(true);
//			msgSearchChoice.setLabelKey("llpLocale.page.locale");
//			add(msgSearchChoice);
			
			
			add(new AjaxButton("ajaxSubmit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					final List<LlpLocaleMessage> listLocaleUpdate = new ArrayList();
					
					form.visitFormComponents((IVisitor<? extends FormComponent<?>, Void>) new IVisitor<FormComponent,Void>() {
						@Override
						public void component(FormComponent arg0, IVisit<Void> arg1) {
							if(arg0.getInputName().endsWith(":localeId")){
								String tblIdArray[] = StringUtils.splitByWholeSeparator(arg0.getInputName() , "sorting:");
								String tblId = StringUtils.splitByWholeSeparator(tblIdArray[1] , ":")[0];
								
								String localeId = arg0.getInput();
								String key = ((FormComponent)arg0.getForm().get("listDataView:sorting:"+tblId+":key")).getInput();
								String msg = ((FormComponent)arg0.getForm().get("listDataView:sorting:"+tblId+":msg")).getInput();
								
								LlpLocaleMessage llpLocaleMessage = new LlpLocaleMessage();
								llpLocaleMessage.setLocaleMessageId(new Long(localeId));
								llpLocaleMessage.setKey(key);
								llpLocaleMessage.setMsg(msg);
								listLocaleUpdate.add(llpLocaleMessage);
							}
						}
	                });
					
					for (int i = 0; i < listLocaleUpdate.size(); i++) {
						LlpLocaleMessage llpLocaleMessage = listLocaleUpdate.get(i);
						LlpLocaleMessage llpLocaleMessageDB = llpLocaleMessageService.findById(llpLocaleMessage.getLocaleMessageId());
						llpLocaleMessageDB.setKey(llpLocaleMessage.getKey());
						llpLocaleMessageDB.setMsg(llpLocaleMessage.getMsg());
						llpLocaleMessageService.update(llpLocaleMessageDB);
					}
					FeedbackPanel feedbackPanel =  ((LlpLocaleMessagePage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
					WicketApplication.getResourceLoader().updateCacheMessage(listLocaleUpdate);
					ssmSuccess("llpLocale.page.success");
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
					//error if ie. have required mandatory field in popup (1)
				}

			});

			add(new AjaxButton("ajaxSubmitSearch") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					rePopulateTable(target);
					FeedbackPanel feedbackPanel =  ((LlpLocaleMessagePage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
			
			add(new AjaxButton("ajaxRefreshAllNodeLocale") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					WicketApplication.getResourceLoader().reloadAllCache();
					ssmSuccess("llpLocale.page.success.refreshAllNode");
					FeedbackPanel feedbackPanel =  ((LlpLocaleMessagePage)getPage()).getFeedbackPanel();
					target.add(feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
			
			
			htmlEditorPanel = new HtmlEditorPanel("htmlEditorPanel");
			add(htmlEditorPanel);
			
			/*
			add(new SSMAjaxButton("showHtmlEditor") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
//					DashboardRenewalAlert dashboardRenewalAlert = new DashboardRenewalAlert("additionalMsg", new ArrayList());
//					alertPanel.resetAlert("SDasdas", "Sdsads" ,dashboardRenewalAlert, target);
					
					String jScript = "showModal('"+htmlEditorPanel.getWmcEditorId()+"');";
					target.appendJavaScript(jScript);
				}
			});
			
			*/


			
			prepareForImportModal();
			
		}
		
		private void prepareForImportModal(){
			final ModalWindow modalWindowImportLocale = new ModalWindow("modalWindowImportLocale");
			add(modalWindowImportLocale);

//			modalWindowImportLocale.setCookieName(getRootForm().getId());
			
			modalWindowImportLocale.setInitialHeight(400); 
			modalWindowImportLocale.setInitialWidth(80);
			modalWindowImportLocale.setHeightUnit("px"); 
			modalWindowImportLocale.setWidthUnit("%"); 
			modalWindowImportLocale.setResizable(false); 
			
			modalWindowImportLocale.setPageCreator(new ModalWindow.PageCreator() {
				@Override
				public Page createPage() {
					return new LlpLocaleMessageFramePage(LlpLocaleMessagePage.this.getPage(), modalWindowImportLocale);
				}
			});
			modalWindowImportLocale.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					
				}
			});

			modalWindowImportLocale.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				@Override
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					return true;
				}
			});
			
			AjaxLink searchBusinessCodeBtn = new AjaxLink<Void>("importLocale") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					modalWindowImportLocale.show(target);
				}
			};
			add(searchBusinessCodeBtn);
		}
		
		private void rePopulateTable(AjaxRequestTarget target){
			
			SearchCriteria sc = null;
			if(StringUtils.isNotBlank(getKeySearch())){
				sc = SearchCriteria.andIfNotNull(sc, "key", SearchCriteria.LIKE, getKeySearch()+"%");
			}
			if(StringUtils.isNotBlank(getMsgSearch())){
				sc = SearchCriteria.andIfNotNull(sc, "msg", SearchCriteria.LIKE, "%"+getMsgSearch()+"%");
			}
//			if(StringUtils.isNotBlank(getLocaleSearch())){
//				sc = SearchCriteria.andIfNotNull(sc, "locale", SearchCriteria.EQUAL, getLocaleSearch());
//			}else{
//				sc = SearchCriteria.andCheckEmptyOrNull(sc, "locale"); 
//			}
			
			dp.setSc(sc);
			target.add(wmc);
		}
		
		private void populateTable() {
			SearchCriteria sc = null;
			
			dp = new SSMSortableDataProvider("key", sc, LlpLocaleMessageService.class);
			dataView = new SSMDataView<LlpLocaleMessage>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpLocaleMessage> item) {
					final LlpLocaleMessage localeMessage = item.getModelObject();
//					item.add(new SSMLabel("key", localeMessage.getKey()));
//					item.add(new SSMLabel("msg", localeMessage.getMsg()));
					
					HiddenField localeId = new HiddenField("localeId", new PropertyModel(localeMessage,"localeMessageId"));
					item.add(localeId);
					
					
					SSMTextField keyTf = new SSMTextField("key", new PropertyModel(localeMessage,"key"));
					keyTf.setUpperCase(false);
					item.add(keyTf);
					
					final SSMTextArea msgTf = new SSMTextArea("msg", new PropertyModel(localeMessage,"msg"));
					msgTf.setOutputMarkupId(true);
					int length = localeMessage.getMsg().length()/100;
					length+=1;
					if(length>10){
						length = 10;
					}
					if(length==1){
						length=2;
					}
					AttributeModifier rows =new AttributeModifier( "rows", String.valueOf(length));
					msgTf.add(rows);
					msgTf.setUpperCase(false);
					item.add(msgTf);
					
					SSMAjaxButton  showHtmlEditor = new SSMAjaxButton("showHtmlEditor") {
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
							
	//						DashboardRenewalAlert dashboardRenewalAlert = new DashboardRenewalAlert("additionalMsg", new ArrayList());
	//						alertPanel.resetAlert("SDasdas", "Sdsads" ,dashboardRenewalAlert, target);
							
							String htmlEscape = localeMessage.getMsg();
							StringUtils.replace(htmlEscape, "'", "\\'");
							
//							htmlEscape = StringEscapeUtils.escapeHtml(localeMessage.getMsg());
//							System.out.println("HtmlEscape:"+htmlEscape);
							String jScript = "showModal('"+htmlEditorPanel.getWmcEditorId()+"');returnId='"+msgTf.getMarkupId()+"';setHtmlText()";
							target.appendJavaScript(jScript);
						}
					};
					showHtmlEditor.setVisible(false);
					item.add(showHtmlEditor);
					
					if(dp.size()==1) {
						showHtmlEditor.setVisible(true);
					}	
						
//					item.add(new SSMLabel("locale", localeMessage.getLocale()));
					// item.add(new SSMCheckBox("check", new PropertyModel(code,
					// "selected")));

//					item.add(new AjaxCheckBox("check", new PropertyModel(code, "selected")) {
//						@Override
//						protected void onUpdate(AjaxRequestTarget target) {
//							RocBusinessObjectCode businessObjectCode = (RocBusinessObjectCode) getParent().getDefaultModelObject();
//							if (String.valueOf(true).equals(getValue())) {
//								businessObjectCode.setSelected(true);
//								listSelected.add(businessObjectCode);
//							} else {
//								businessObjectCode.setSelected(false);
//								listSelected.remove(businessObjectCode);
//							}
//						}
//					});

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};
			dataView.setItemsPerPage(10L);

			dataView.setOutputMarkupId(true);

			SSMPagingNavigator navigator = new SSMPagingNavigator("navigator", dataView);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);
//			wmc.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

			add(wmc);
		}

		public String getKeySearch() {
			return keySearch;
		}

		public void setKeySearch(String keySearch) {
			this.keySearch = keySearch;
		}

		public String getLocaleSearch() {
			return localeSearch;
		}

		public void setLocaleSearch(String localeSearch) {
			this.localeSearch = localeSearch;
		}

		public String getMsgSearch() {
			return msgSearch;
		}

		public void setMsgSearch(String msgSearch) {
			this.msgSearch = msgSearch;
		}
	}

	@Override
	public String getPageTitle() {
		return "page.title.locale.message";
	}

}
