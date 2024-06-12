package com.ssm.ezbiz.cmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.CmpDetailService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.CmpDetail;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.llp.mod1.page.ListLlpReservedNamesOfficer;

public class ListCmpDetailPage extends SecBasePage{
	@SpringBean(name = "CmpDetailService")
	private CmpDetailService cmpDetailService;
		
	public ListCmpDetailPage(final CmpMaster cmpMaster, String icNo, String entityType, String EntityNo){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCmpDetailPageFormModel listCmpPageFormModel = new ListCmpDetailPageFormModel();
				
				listCmpPageFormModel.setCmpAmt(cmpMaster.getCmpAmt());
				listCmpPageFormModel.setCmpEntityName(cmpMaster.getEntityName());
				listCmpPageFormModel.setCmpEntityNo(cmpMaster.getEntityNo());
				listCmpPageFormModel.setCmpEntityType(cmpMaster.getEntityType());
				listCmpPageFormModel.setCmpMasterKeyCode(cmpMaster.getCmpMasterId());
				listCmpPageFormModel.setCmpNo(cmpMaster.getCmpNo());
				listCmpPageFormModel.setCmpStatus(cmpMaster.getCmpStatus());
				listCmpPageFormModel.setType(cmpMaster.getCmpType());
				listCmpPageFormModel.setRegNo(cmpMaster.getEntityNoCheckDigit());
				listCmpPageFormModel.setRegName(cmpMaster.getEntity_name_reg());
								
				return listCmpPageFormModel;
			}
		}));
		add(new ListCmpDetailPageForm("form", (IModel<ListCmpDetailPageFormModel>) getDefaultModel(), icNo,entityType,EntityNo));
	}
	
	private class ListCmpDetailPageForm extends Form implements Serializable {
		public ListCmpDetailPageForm(String id, IModel<ListCmpDetailPageFormModel> m, final String icNo, final String entityType, final String entityNo) {
			super(id, m);
			final ListCmpDetailPageFormModel searchModel = m.getObject();
			
			SSMTextField cmpNo = new SSMTextField("cmpNo");
			cmpNo.setLabelKey("page.lbl.ezbiz.cmp.cmpNo");
			add(cmpNo);
			
			String cmpStatusDesc = getCodeTypeWithValue(Parameter.CMP_STATUS, searchModel.getCmpStatus());
			SSMTextField cmpStatus = new SSMTextField("cmpStatus", new PropertyModel(cmpStatusDesc, ""));
			cmpStatus.setLabelKey("page.lbl.ezbiz.cmp.cmpStatus");
			add(cmpStatus);
			
			String cmpEntityTypeDesc = getCodeTypeWithValue(Parameter.CMP_ENTITY_TYPE, searchModel.getCmpEntityType());
			SSMTextField cmpEntityType = new SSMTextField("cmpEntityType", new PropertyModel(cmpEntityTypeDesc, ""));
			cmpEntityType.setLabelKey("page.lbl.ezbiz.cmp.entityType");
			add(cmpEntityType);
			
			SSMTextField cmpEntityNo = new SSMTextField("cmpEntityNo");
			cmpEntityNo.setLabelKey("page.lbl.ezbiz.cmp.entityNo");
			add(cmpEntityNo);
			
			SSMTextField cmpAmt = new SSMTextField("cmpAmt");
			cmpAmt.setLabelKey("page.lbl.ezbiz.cmp.cmpAmt");
			cmpAmt.setOutputMarkupId(true);
			add(cmpAmt);
			
			SSMTextField type = new SSMTextField("type");
			type.setVisible(false);
			add(type);
			
			SSMTextField regNo = new SSMTextField("regNo");
			//type.setVisible(false);
			regNo.setLabelKey("page.lbl.ezbiz.cmp.regNo");
			add(regNo);
			
			SSMTextField regName = new SSMTextField("regName");
			regName.setLabelKey("page.lbl.ezbiz.cmp.regName");
			//type.setVisible(false);
			add(regName);
			
			if( "05".equals(searchModel.getCmpStatus()) || "09".equals(searchModel.getCmpStatus()) ||"15".equals(searchModel.getCmpStatus())  ) {
				cmpAmt.setVisible(false);
			}
			
			SSMAjaxButton back = new SSMAjaxButton("back") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form<?> form) {
					PageParameters params = new PageParameters();
					
					if(StringUtils.isNotEmpty(icNo)){
						params.add("icNo", icNo);
					}
					
					if(StringUtils.isNotEmpty(entityNo)){
						params.add("entityNo", entityNo);
					}
					
					if(StringUtils.isNotEmpty(entityType)){
						params.add("entityType", entityType);
					}
					
					setResponsePage(new ListCmpPage(params));
				}
			};
			add(back);
			
			populateTable(searchModel);
		}
		
		public void populateTable(ListCmpDetailPageFormModel listCmpPageFormModel){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			List<CmpDetail> cmpDetailList = new ArrayList<CmpDetail>();
			try {
				if(listCmpPageFormModel != null){
					cmpDetailList = cmpDetailService.findCmpDetailListWS(listCmpPageFormModel.getCmpNo(), listCmpPageFormModel.getType());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("cmpNo", cmpDetailList);
			final SSMDataView<CmpDetail> dataView = new SSMDataView<CmpDetail>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<CmpDetail> item) {
					final CmpDetail cmpDetail = item.getModelObject();
			        
					item.add(new SSMLabel("cmpSectionCode", cmpDetail.getCmpSectionCode()));
					item.add(new SSMLabel("cmpSectionOffence", cmpDetail.getCmpSectionOffence()));
					item.add(new SSMLabel("cmpSectionAmt", cmpDetail.getCmpSectionAmt()));				

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
			add(wmcSearchResult);
		}
		
	}
	
	private class ListCmpDetailPageFormModel {
		private long cmpMasterKeyCode;
		private String cmpNo;
		private String cmpStatus;
		private String cmpEntityType;
		private String cmpEntityNo;
		private String cmpEntityName;
		private double cmpAmt;
		private String type;
		
		private String regNo;
		private String regName;
		
		
		
		public String getRegNo() {
			return regNo;
		}
		public void setRegNo(String regNo) {
			this.regNo = regNo;
		}
		public String getRegName() {
			return regName;
		}
		public void setRegName(String regName) {
			this.regName = regName;
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
		public double getCmpAmt() {
			return cmpAmt;
		}
		public void setCmpAmt(double cmpAmt) {
			this.cmpAmt = cmpAmt;
		}
		public long getCmpMasterKeyCode() {
			return cmpMasterKeyCode;
		}
		public void setCmpMasterKeyCode(long cmpMasterKeyCode) {
			this.cmpMasterKeyCode = cmpMasterKeyCode;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
}
