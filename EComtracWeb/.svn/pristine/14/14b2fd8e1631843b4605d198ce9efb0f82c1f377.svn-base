package com.ssm.ezbiz.cmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.CmpDetailService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.CmpDetail;
import com.ssm.llp.ezbiz.model.CmpMaster;

public class ListCmpDetailHistoryPage extends SecBasePage{
	@SpringBean(name = "CmpDetailService")
	private CmpDetailService cmpDetailService;
		
	public ListCmpDetailHistoryPage(final CmpMaster cmpMaster){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				ListCmpDetailPageFormModel listCmpPageFormModel = new ListCmpDetailPageFormModel();
				
				listCmpPageFormModel.setCmpAmt(cmpMaster.getCmpAmt());
				listCmpPageFormModel.setCmpEntityName(cmpMaster.getEntityName());
				listCmpPageFormModel.setCmpEntityNo(cmpMaster.getEntityNo());
				listCmpPageFormModel.setCmpEntityType(cmpMaster.getEntityType());
				listCmpPageFormModel.setcmpTrans(cmpMaster.getCmpTransactionCode());
				listCmpPageFormModel.setCmpNo(cmpMaster.getCmpNo());
				//listCmpPageFormModel.setCmpStatus(cmpMaster.getCmpStatus());
				//listCmpPageFormModel.setType(cmpMaster.getCmpType());
								
				return listCmpPageFormModel;
			}
		}));
		add(new ListCmpDetailPageForm("form", (IModel<ListCmpDetailPageFormModel>) getDefaultModel()));
	}
	
	private class ListCmpDetailPageForm extends Form implements Serializable {
		public ListCmpDetailPageForm(String id, IModel<ListCmpDetailPageFormModel> m) {
			super(id, m);
			ListCmpDetailPageFormModel searchModel = m.getObject();
			
			SSMTextField cmpNo = new SSMTextField("cmpNo");
			cmpNo.setLabelKey("page.lbl.ezbiz.cmp.cmpNo");
			add(cmpNo);
			
			SSMTextField cmpTrans = new SSMTextField("cmpTrans");
			cmpTrans.setLabelKey("page.lbl.ezbiz.cmp.cmpTrans");
			add(cmpTrans);
			
			SSMTextField cmpEntityType = new SSMTextField("cmpEntityType");
			cmpEntityType.setLabelKey("page.lbl.ezbiz.cmp.cmpEntityType");
			add(cmpEntityType);
			
			SSMTextField cmpEntityNo = new SSMTextField("cmpEntityNo");
			cmpEntityNo.setLabelKey("page.lbl.ezbiz.cmp.cmpEntityNo");
			add(cmpEntityNo);
			
			SSMTextField cmpEntityName = new SSMTextField("cmpEntityName");
			cmpEntityName.setLabelKey("page.lbl.ezbiz.cmp.cmpEntityName");
			add(cmpEntityName);
			
			SSMTextField cmpAmt = new SSMTextField("cmpAmt");
			cmpAmt.setLabelKey("page.lbl.ezbiz.cmp.cmpAmt");
			add(cmpAmt);
			
			SSMTextField type = new SSMTextField("type");
			type.setVisible(false);
			add(type);
			
			populateTable(searchModel);
		}
		
		public void populateTable(ListCmpDetailPageFormModel listCmpPageFormModel){
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);
			
			List<CmpDetail> cmpDetailList = new ArrayList<CmpDetail>();
			try {
				if(listCmpPageFormModel != null){
					//cmpDetailList = cmpDetailService.findCmpDetailListWS(listCmpPageFormModel.getCmpNo(), listCmpPageFormModel.getType());
				}
			} catch (Exception e) {
				e.printStackTrace();
			};
			
			SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("cmpNo", cmpDetailList);
			final SSMDataView<CmpDetail> dataView = new SSMDataView<CmpDetail>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<CmpDetail> item) {
					final CmpDetail cmpDetail = item.getModelObject();
			      
					//item.add(new SSMLabel("cmpSectionDesc", cmpDetail.getCmpSectionDesc()));
				//item.add(new SSMLabel("cmpSectionOffence", cmpDetail.getCmpSectionOffence()));
				//item.add(new SSMLabel("cmpSectionAmt", cmpDetail.getCmpSectionAmt()));				

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
		private String cmpTrans;
		private String cmpEntityType;
		private String cmpEntityNo;
		private String cmpEntityName;
		private double cmpAmt;
		private String type;
		public String getCmpNo() {
			return cmpNo;
		}
		public void setCmpNo(String cmpNo) {
			this.cmpNo = cmpNo;
		}
		public String gecmpTrans() {
			return cmpTrans;
		}
		public void setcmpTrans(String cmpTrans) {
			this.cmpTrans = cmpTrans;
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
