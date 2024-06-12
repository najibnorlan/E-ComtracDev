package com.ssm.llp.page.supplyinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormAOwner;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpReservedNameService;


@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LlpSupplyInfoLlpSearchPanel extends BasePanel {
	
	private LlpSupplyInfoItemPanel llpSupplyInfoItemPanel;
	public LlpSupplyInfoLlpSearchPanel(String panelId, LlpSupplyInfoItemPanel llpSupplyInfoItemPanel) {
		super(panelId);
		this.llpSupplyInfoItemPanel = llpSupplyInfoItemPanel;
		add(new InputForm("inputForm"));
	}
	
	/** form for processing the input. */
	private class InputForm extends Form {

		private String entityType;
		private String entityNo;
		private String entityName;
		// holds NameWrapper elements
		private WebMarkupContainer wmc;
		private SSMDataView dataView;
		private SSMSessionSortableDataProvider dp;

		private List<RocBusinessObjectCode> listSelected = new ArrayList<RocBusinessObjectCode>();

		public InputForm(String name) {
			super(name);
			populateTable();
			final SSMTextField tf = new SSMTextField("entityNo", new PropertyModel(this, "entityNo"));
			add(tf);
			
			final SSMTextField tf2 = new SSMTextField("entityName", new PropertyModel(this, "entityName"));
			add(tf2);
			
			add(new AjaxButton("ajaxSubmitSearch") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					System.out.println("Search:"+tf.getInput());
					SearchCriteria sc = null;
					if(StringUtils.isNotBlank(getEntityNo())){
						sc = new SearchCriteria("llpNo", SearchCriteria.EQUAL, getEntityNo());
					}
					if(StringUtils.isNotBlank(getEntityName())){
						if(sc==null){
							sc = new SearchCriteria("applyLlpName", SearchCriteria.LIKE, getEntityName()+"%");
						}else{
							sc = sc.andIfNotNull("applyLlpName", SearchCriteria.LIKE, getEntityName()+"%");
						}
					}
					if(sc!=null){
						sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.RESERVE_NAME_STATUS_approved);
						SearchCriteria notNullLlpNoSC = new SearchCriteria("llpNo", SearchCriteria.IS_NOT_NULL);
						sc = new SearchCriteria(sc,SearchCriteria.AND,notNullLlpNoSC);
					}
					
//					dp.setSc(sc);
					List list = dp.getListResult();
					
					LlpSupplyInfoDtl dtl = new LlpSupplyInfoDtl();
					dtl.setEntityType("ROB");
					dtl.setEntityName("Abu Bakar");
					dtl.setEntityNo("000123");
					
					list.add(dtl);
					dp.resetView(list);
					target.add(wmc);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
		}

		private  void populateTable() {
			SearchCriteria sc = null;

			
//			final SSMSessionSortableDataProvider dpOwner = new SSMSessionSortableDataProvider("", robFormA.getListRobFormAOwner());
//			final SSMDataView<RobFormAOwner> dataViewOwner = new SSMDataView<RobFormAOwner>("sortingRobFormAOwner", dpOwner) {
//				
//			}
			dp = new SSMSessionSortableDataProvider("", new ArrayList());
			dataView = new SSMDataView<LlpSupplyInfoDtl>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpSupplyInfoDtl> item) {
					LlpSupplyInfoDtl llpSupplyDtl = item.getModelObject();
					item.add(new SSMLabel("entityType", llpSupplyDtl.getEntityType()));
					item.add(new SSMLabel("entityNo", llpSupplyDtl.getEntityNo()));
					item.add(new SSMLabel("entityName", llpSupplyDtl.getEntityName()));
					// item.add(new SSMCheckBox("check", new PropertyModel(code,
					// "selected")));
					
					item.add(new AjaxButton("buyProfile") {
						
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
							System.out.println("buyProfile");
							LlpReservedName llpReservedName = (LlpReservedName) getParent().getDefaultModelObject();
							List llpSupplyInfoDetList = (List) getSession().getAttribute("llpSupplyInfoDetList_");
							if(llpSupplyInfoDetList==null){
								llpSupplyInfoDetList = new ArrayList();
							}
							if(llpSupplyInfoDetList.size()>=5){
								target.appendJavaScript("alert('Maximum can choose only 5 LLP');");
							}else{
								boolean alreadyExist = false;
								for (int i = 0; i < llpSupplyInfoDetList.size(); i++) {
									LlpSupplyInfoDtl dtl = (LlpSupplyInfoDtl) llpSupplyInfoDetList.get(i);
									if(dtl.getEntityNo().equals(llpReservedName.getLlpNo())){
										dtl.setIsProfileSelected(true);
										alreadyExist=true;
									}
								}
								if(!alreadyExist){
									LlpSupplyInfoDtl dtl = new LlpSupplyInfoDtl();
									dtl.setEntityNo(llpReservedName.getLlpNo());
									dtl.setEntityName(llpReservedName.getApplyLlpName());
									dtl.setIsProfileSelected(true);
									llpSupplyInfoDetList.add(dtl);
								}
								getSession().setAttribute("llpSupplyInfoDetList_",(Serializable)llpSupplyInfoDetList);
								
								llpSupplyInfoItemPanel.refreshContainer(target);
							}
							
						}
					});
					
					item.add(new AjaxButton("buyCert") {
						
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
							System.out.println("buyCert");
							LlpReservedName llpReservedName = (LlpReservedName) getParent().getDefaultModelObject();
							List llpSupplyInfoDetList = (List) getSession().getAttribute("llpSupplyInfoDetList_");
							if(llpSupplyInfoDetList==null){
								llpSupplyInfoDetList = new ArrayList();
							}
							if(llpSupplyInfoDetList.size()>=5){
								target.appendJavaScript("alert('Maximum can choose only 5 LLP');");
							}else{
								boolean alreadyExist = false;
								for (int i = 0; i < llpSupplyInfoDetList.size(); i++) {
									LlpSupplyInfoDtl dtl = (LlpSupplyInfoDtl) llpSupplyInfoDetList.get(i);
									if(dtl.getEntityNo().equals(llpReservedName.getLlpNo())){
										dtl.setIsCertSelected(true);
										alreadyExist=true;
									}
								}
								if(!alreadyExist){
									LlpSupplyInfoDtl dtl = new LlpSupplyInfoDtl();
									dtl.setEntityNo(llpReservedName.getLlpNo());
									dtl.setEntityName(llpReservedName.getApplyLlpName());
									dtl.setIsCertSelected(true);
									llpSupplyInfoDetList.add(dtl);
								}
								getSession().setAttribute("llpSupplyInfoDetList_",(Serializable)llpSupplyInfoDetList);
								
								llpSupplyInfoItemPanel.refreshContainer(target);
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

			// add(dataView);
			dataView.setOutputMarkupId(true);
			dataView.setItemsPerPage(5L);

			PagingNavigator navigator = new PagingNavigator("navigator", dataView);
			// add(navigator);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			// add(navigatorLabel);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);
			add(wmc);
		}


		public String getEntityType() {
			return entityType;
		}

		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}

		public String getEntityNo() {
			return entityNo;
		}

		public void setEntityNo(String entityNo) {
			this.entityNo = entityNo;
		}

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

		
	}
}


