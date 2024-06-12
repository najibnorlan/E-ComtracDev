package com.ssm.ezbiz.errorlog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.SSMErrorLog;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings("all")
public class SearchErrorLogCheckPage extends SecBasePage{ 
	
	
		
	public SearchErrorLogCheckPage(){
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
					protected Object load() {
						SSMErrorLog errorLog = new SSMErrorLog();
						errorLog.setErrorId("");
						errorLog.setLogTime(null);
						errorLog.setUserId("");
						return errorLog;
					}

				}));
				add(new ErrorLogCheckForm("form", (IModel<SSMErrorLog>) getDefaultModel()));

	}
	
	private class ErrorLogCheckForm extends Form implements Serializable {
		public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss ");
		
		public ErrorLogCheckForm(String id , IModel<SSMErrorLog> m ){
			super(id, m);
//			public String errorId;
//			public String errorMsg;
//			public String userId;
//			public Date logTime;
			SSMErrorLog searchModel = m.getObject();
			
			final List<SSMErrorLog> listError = new ArrayList<SSMErrorLog>();
			
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MyInternalErrorPage.file));
				SSMErrorLog errorLog = (SSMErrorLog) ois.readObject();
				while(errorLog!=null){
					listError.add(errorLog);
					errorLog = (SSMErrorLog) ois.readObject();
				}
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			
			SSMLabel totalLogs = new SSMLabel("totalLogs", listError.size());
			add(totalLogs);
			
			SSMTextField errorId = new SSMTextField("errorId");
			errorId.setLabelKey("page.lbl.ezbiz.errorLogCheckPage.errorId");
			add(errorId);
			
			SSMTextField userId = new SSMTextField("userId");
			userId.setLabelKey("page.lbl.ezbiz.errorLogCheckPage.userId");
			userId.setUpperCase(false);
			add(userId);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					SSMErrorLog searchModelForm = (SSMErrorLog) getForm().getDefaultModelObject();

					String errorId = searchModelForm.getErrorId();
					String userId = searchModelForm.getUserId();
					
					List<SSMErrorLog> listErrorNew = new ArrayList<SSMErrorLog>(); 
					for (int i = 0; i < listError.size(); i++) {
						if(StringUtils.isNotBlank(errorId)){
							if(listError.get(i).getErrorId().equals(errorId)){
								listErrorNew.add(listError.get(i));
								break;
							}
						}else{
							if(StringUtils.isNotBlank(userId)){
								if(listError.get(i).getUserId().equals(userId)){
									listErrorNew.add(listError.get(i));
								}
							}
						}
					}
					
					populateTable(listErrorNew, target);
				}
			};
			add(search);
			
			SSMDownloadLink linkErrorDownload = new SSMDownloadLink("linkErrorDownload") {
				public void onClick() {
					if(getByteData()!=null){
						super.onClick();
						return;
					}
					try {
						String fileName = "ERROR-LOG-EZBIZ-"+DateUtil.formatDate(new Date(),"yyyyMMdd")+".txt";
						
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						PrintWriter pw = new PrintWriter(baos);
						for (int i = 0; i < listError.size(); i++) {
							pw.println("ErrorId : "+listError.get(i).getErrorId());
							pw.println("UserId : "+listError.get(i).getUserId());
							pw.println("DateTime : "+sdf.format(listError.get(i).getLogTime()));
							pw.println(listError.get(i).getErrorMsg());
						}
						pw.flush();
						
						byte byteData[] = baos.toByteArray();
						baos.close();
						
						setDownloadData(fileName, "application/txt", byteData);
						super.onClick();
					} catch (Exception e) {
						ssmError(e.getMessage());
						e.printStackTrace();
					} 
				}

			};
			add(linkErrorDownload);
			
			
			populateTable(null, null);
		}
		
		public void populateTable(List<SSMErrorLog> listEghlResponse, AjaxRequestTarget target) {
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listEghlResponse);
			final SSMDataView<SSMErrorLog> dataView = new SSMDataView<SSMErrorLog>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<SSMErrorLog> item) {
					final SSMErrorLog errorLog= item.getModelObject();
					
					
					String response = "ErrorId : "+errorLog.getErrorId()+"\n";
					response+="Username : "+errorLog.getUserId()+"\n";
					response+="DateTime : "+sdf.format(errorLog.getLogTime())+"\n\n";
					response+=errorLog.getErrorMsg()+"\n";
					
					
					item.add(new MultiLineLabel("errorLogResponse", response));
					

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};
			
			dataView.setItemsPerPage(20L);

			wmcSearchResult.add(dataView);
			wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
			wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));
						
			wmcSearchResult.setVisible(true);
			
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}

		}
	}
	
	public String getPageTitle() {
		return "menu.myBiz.paymentWithEghl";
	}
}