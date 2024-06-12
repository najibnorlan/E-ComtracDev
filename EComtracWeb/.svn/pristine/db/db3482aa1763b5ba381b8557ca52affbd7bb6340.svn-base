package com.ssm.ezbiz.cmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.CmpDetailService;
import com.ssm.ezbiz.service.CmpMasterService;
import com.ssm.ezbiz.service.CmpTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.page.PaymentDetailPage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.CmpInfo;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

public class ListCmpPage extends SecBasePage{
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "CmpMasterService")
	private CmpMasterService cmpMasterService;
	
	@SpringBean(name = "CmpTransactionService")
	private CmpTransactionService cmpTransactionService;
	
	@SpringBean(name = "CmpDetailService")
	private CmpDetailService cmpDetailService;
	
	@SuppressWarnings("unchecked")
	public ListCmpPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCmpPageFormModel listCmpPageFormModel = new ListCmpPageFormModel();
				
				String icNo = "";
				if(!UserEnvironmentHelper.isInternalUser()){
					LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
					if(profile!=null){
						icNo = profile.getIdNo();
					}
				}
				listCmpPageFormModel.setCmpIcNo(icNo);
				
				return listCmpPageFormModel;
			}
		}));
		add(new ListCmpPageForm("form", (IModel<ListCmpPageFormModel>) getDefaultModel(),null));
	}
	
	@SuppressWarnings("unchecked")
	public ListCmpPage(final PageParameters param) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCmpPageFormModel listCmpPageFormModel = new ListCmpPageFormModel();
				
				String icNo = "";
				if(!UserEnvironmentHelper.isInternalUser()){
					LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
					if(profile!=null){
						icNo = profile.getIdNo();
					}
				}
				listCmpPageFormModel.setCmpIcNo(icNo);
				
				if(param.get("entityNo") != null){
					listCmpPageFormModel.setCmpEntityNo(param.get("entityNo").toString());
				}
				
				if(param.get("entityType") != null){
					listCmpPageFormModel.setCmpEntityType(param.get("entityType").toString());
				}
								
				return listCmpPageFormModel;
			}
		}));
		add(new ListCmpPageForm("form", (IModel<ListCmpPageFormModel>) getDefaultModel(), param));
	}
	
	private class ListCmpPageForm extends Form implements Serializable {
		public ListCmpPageForm(String id, IModel<ListCmpPageFormModel> m, PageParameters param) {
			super(id, m);
			ListCmpPageFormModel searchModel = m.getObject();
			
			SSMTextField cmpIcNo = new SSMTextField("cmpIcNo");
			cmpIcNo.setLabelKey("page.lbl.ezbiz.cmpIcNo");
			cmpIcNo.setReadOnly(true);
			add(cmpIcNo);
			
			SSMDropDownChoice cmpEntityType = new SSMDropDownChoice("cmpEntityType", Parameter.ENTITY_TYPE);
			cmpEntityType.setLabelKey("page.lbl.ezbiz.cmpEntityType");
			add(cmpEntityType);
			
			SSMTextField cmpEntityNo = new SSMTextField("cmpEntityNo");
			cmpEntityNo.setLabelKey("page.lbl.ezbiz.cmpEntityNo");
			add(cmpEntityNo);
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form<?> form) {
					ListCmpPageFormModel searchModelForm = (ListCmpPageFormModel) form.getDefaultModelObject();
					
					populateTable(searchModelForm,target);
					
				}
			};
			add(search);
			
			if(param != null){
				populateTable(searchModel,null);
			}else{
				populateTable(null,null);
			}
		}
		
		public void populateTable(final ListCmpPageFormModel listCmpPageFormModel, AjaxRequestTarget target){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			List<CmpMaster> cmpMasterList = new ArrayList<CmpMaster>();
			try {
				if(listCmpPageFormModel != null){
					cmpMasterList = cmpMasterService.findCmpListWS(listCmpPageFormModel.getCmpIcNo(), listCmpPageFormModel.getCmpEntityType(), listCmpPageFormModel.getCmpEntityNo());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("cmpNo", cmpMasterList);
			final SSMDataView<CmpMaster> dataView = new SSMDataView<CmpMaster>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				protected void populateItem(final Item<CmpMaster> item) {
					final CmpMaster cmpMaster = item.getModelObject();
			        
					item.add(new SSMLabel("cmpNo", cmpMaster.getCmpNo()));
					item.add(new SSMLabel("cmpStatus", cmpMaster.getCmpStatus(), Parameter.CMP_STATUS));
					item.add(new SSMLabel("cmpEntityType", cmpMaster.getEntityType(), Parameter.CMP_ENTITY_TYPE));
					item.add(new SSMLabel("cmpEntityNo", cmpMaster.getEntityNo()));
					item.add(new SSMLabel("cmpEntityName", cmpMaster.getEntityName()));
					//item.add(new SSMLabel("regNo", cmpMaster.getEntityNoCheckDigit()));	
					
					SSMLabel regNo = new SSMLabel("regNo", cmpMaster.getEntityNoCheckDigit());
					regNo.setVisible(false);
					item.add(regNo);
					
					
				//	item.add(new SSMLabel("regName", cmpMaster.getEntity_name_reg()));	
					
					SSMLabel regName = new SSMLabel("regName", cmpMaster.getEntity_name_reg());
					regName.setVisible(false);
					item.add(regName);
					
					Link payCompound = new Link("payCompound", item.getDefaultModel()) {
						public void onClick() {
							try {								
								//Insert cmptransaction
								CmpTransaction cmpTransaction = new CmpTransaction();
								cmpTransaction.setStatus(Parameter.CMP_PAYMENT_DATA_ENTRY);
								cmpTransaction.setTotalAmt(cmpMaster.getCmpAmt());
								cmpTransaction.setPaymentDt(new Date());
								cmpTransactionService.insert(cmpTransaction);
								
								cmpMaster.setCmpTransactionCode(cmpTransaction.getCmpTransactionCode());
								cmpMasterService.insert(cmpMaster);
								
								//Populate payment items
								List<LlpPaymentTransactionDetail> listPaymentItems = new ArrayList<LlpPaymentTransactionDetail>();
								LlpPaymentTransactionDetail paymentItem = new LlpPaymentTransactionDetail();
								
								if(Parameter.COMPOUND_TYPE_ROB.equals(cmpMaster.getCmpType())){
									paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_COMPOUND_ROB);
								}else{
									paymentItem.setPaymentItem(Parameter.PAYMENT_TYPE_COMPOUND_ROC);
								}
								
								paymentItem.setQuantity(1);
								paymentItem.setAmount(cmpMaster.getCmpAmt());
								paymentItem.setPaymentDet(cmpMaster.getCmpNo() + "-" + cmpMaster.getEntityNo());
								listPaymentItems.add(paymentItem);
								
								
								List cmpMasterList = new ArrayList<CmpMaster>();
								cmpMasterList.add(cmpMaster);
								
								CmpInfo cmpInfo = new CmpInfo();
								cmpInfo.setCmpTransaction(cmpTransaction);
								cmpInfo.setListCmpMaster(cmpMasterList);
								
								setResponsePage(new PaymentDetailPage(cmpTransaction.getCmpTransactionCode(), CmpTransactionService.class.getSimpleName(), cmpInfo,
										listPaymentItems));
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					item.add(payCompound);
					
					Link detailCompound = new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							CmpMaster cmpMaster = item.getModelObject();
							setResponsePage(new ListCmpDetailPage(cmpMaster,listCmpPageFormModel.getCmpIcNo(),listCmpPageFormModel.getCmpEntityType(),listCmpPageFormModel.getCmpEntityNo()));
						}
					};
					item.add(detailCompound);
					
//					SSMTextField referSSMLabel  = new SSMTextField("referSSMLabel");
//					referSSMLabel.setLabelKey("page.lbl.ezbiz.cmp.referSSM");
//					referSSMLabel.setReadOnly(true);
//					add(referSSMLabel);

					if("02".equals(cmpMaster.getCmpStatus()) && cmpMaster.getCmpAmt()>0 ){
						payCompound.setVisible(true);
//						detailCompound.setVisible(true);
//						referSSMLabel.setVisible(false);
					}else{
						payCompound.setVisible(false);
//						detailCompound.setVisible(false);
//						referSSMLabel.setVisible(true);
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

			dataView.setItemsPerPage(20L);

			wmcSearchResult.add(dataView);
			wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
			wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));
			if(target==null){
				add(wmcSearchResult);
			}else{
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}
		}
		
	}
	
	private class ListCmpPageFormModel implements Serializable{
		private String cmpIcNo;
		private String cmpNo;
		private String cmpStatus;
		private String cmpEntityType;
		private String cmpEntityNo;
		private String cmpEntityName;
		private String cmpAmt;
//		private String cmpType;
		public String getCmpIcNo() {
			return cmpIcNo;
		}
		public void setCmpIcNo(String cmpIcNo) {
			this.cmpIcNo = cmpIcNo;
		}
		public String getCmpNo() {
			return cmpNo;
		}
		public void setCmpNo(String cmpNo) {
			this.cmpNo = cmpNo;
		}
		public String getCmpStatus() {
			return cmpStatus;
		}
		public void setCmpStatus(String cmpStatus) {
			this.cmpStatus = cmpStatus;
		}
		public String getCmpEntityType() {
			return cmpEntityType;
		}
		public void setCmpEntityType(String cmpEntityType) {
			this.cmpEntityType = cmpEntityType;
		}
		public String getCmpEntityNo() {
			return cmpEntityNo;
		}
		public void setCmpEntityNo(String cmpEntityNo) {
			this.cmpEntityNo = cmpEntityNo;
		}
		public String getCmpEntityName() {
			return cmpEntityName;
		}
		public void setCmpEntityName(String cmpEntityName) {
			this.cmpEntityName = cmpEntityName;
		}
		public String getCmpAmt() {
			return cmpAmt;
		}
		public void setCmpAmt(String cmpAmt) {
			this.cmpAmt = cmpAmt;
		}
//		public String getCmpType() {
//			return cmpType;
//		}
//		public void setCmpType(String cmpType) {
//			this.cmpType = cmpType;
//		}
		
	}
}
