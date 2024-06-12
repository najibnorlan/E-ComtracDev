package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.RocBusinessObjectCodeService;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;

@SuppressWarnings("all")
public class EditRobFormBBizCodePanel extends BaseFrame implements Serializable{
	    private static final long serialVersionUID = 1L;
	
		private int bizCodeIdx = -1;
		
		@SpringBean(name = "LlpParametersService")
		private LlpParametersService llpParametersService;

		// edit
		public EditRobFormBBizCodePanel(final WebMarkupContainer wmcBizCode, final RobFormB2Det robFormB2BizCode, final ModalWindow editBizCodePopUp) {
			this(wmcBizCode, robFormB2BizCode,editBizCodePopUp,-1);
		}
		public EditRobFormBBizCodePanel(final WebMarkupContainer wmcBizCode, final RobFormB2Det robFormB2BizCode, final ModalWindow editBizCodePopUp, final int bizCodeIdx) {
			this.bizCodeIdx = bizCodeIdx;
			setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
	            protected Object load() {
	            	return new RobFormB2Det();
	            }
	        }));
			add(new RobFormB2DetForm("robFormB2DetForm", getDefaultModel(), wmcBizCode, editBizCodePopUp));
		}
		
		
		/** form for processing the input. */
		private class RobFormB2DetForm extends Form {
			
			public RobFormB2DetForm(String name, IModel model, final WebMarkupContainer wmcBizCode, final ModalWindow editBizCodePopUp) {
				super(name, model);
				
				final RobFormB2Det robFormB2BizCode = (RobFormB2Det) model.getObject();
				
//				SSMTextField bizCode = new SSMTextField("bizCode");
//				add(bizCode);
				
				SSMTextField bizCodeDesc = new SSMTextField("bizCodeDesc");
				add(bizCodeDesc);
				
				final WebMarkupContainer wmcListBizCode = new WebMarkupContainer("wmcListBizCode");
				wmcListBizCode.setOutputMarkupId(true);
				wmcListBizCode.setOutputMarkupPlaceholderTag(true);
				add(wmcListBizCode);
				
				final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("code", new ArrayList());
				final SSMDataView dataView = new SSMDataView<RobFormB2Det>("sorting", dp) {
					private static final long serialVersionUID = 1L;

					protected void populateItem(final Item<RobFormB2Det> item) {
						RobFormB2Det code = item.getModelObject();
						item.add(new SSMLabel("code", code.getBizCode()));
						item.add(new MultiLineLabel("desc", code.getBizCodeDesc()));

						item.add(new AjaxCheckBox("check", new PropertyModel(code, "selected")) {
							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								RobFormB2Det businessObjectCode = (RobFormB2Det) getParent().getDefaultModelObject();
								if (String.valueOf(true).equals(getValue())) {
									businessObjectCode.setSelected(true);
								} else {
									businessObjectCode.setSelected(false);
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

				dataView.setOutputMarkupId(true);
				
				wmcListBizCode.add(dataView);
				
				wmcListBizCode.add(new SSMPagingNavigator("navigator", dataView));
				wmcListBizCode.add(new NavigatorLabel("navigatorLabel", dataView));
				
				AjaxButton searchBizCode = new AjaxButton("searchBizCode") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						RobFormB2Det robFormABizCodeForm  = (RobFormB2Det) form.getModelObject();
						List<RobFormB2Det> list = new ArrayList<RobFormB2Det>();
						if (StringUtils.isNotBlank(robFormABizCodeForm.getBizCode())|| StringUtils.isNotBlank(robFormABizCodeForm.getBizCodeDesc())) {
							SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, "A");
							sc = sc.andIfNotNull("codeType", SearchCriteria.EQUAL, Parameter.ROB_BUSINESS_CODE);
//							if(StringUtils.isNotBlank(robFormABizCodeForm.getBizCode())){
//								sc = sc.andIfNotNull("code", SearchCriteria.LIKE,  robFormABizCodeForm.getBizCode() + "%");
//							}
							if(StringUtils.isNotBlank(robFormABizCodeForm.getBizCodeDesc())){
								System.out.println(robFormB2BizCode.getBizCodeDesc());
								sc = sc.andIfNotNull( "codeDesc", SearchCriteria.LIKE, "%" + robFormABizCodeForm.getBizCodeDesc() + "%");
							}
							
							List<LlpParameters> listDb = llpParametersService.findByCriteria(sc).getList();
							for (int i = 0; listDb!=null && i < listDb.size(); i++) {
								RobFormB2Det robFormB2DetNew = new RobFormB2Det(listDb.get(i).getCode(),listDb.get(i).getCodeDesc());
								robFormB2DetNew.setAmmendmentType(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW);
								list.add(robFormB2DetNew);
							}
							
						}
						
						dp.resetView(list);
						
						target.add(wmcListBizCode);
						
					}
				};
				add(searchBizCode);
				
				AjaxButton updateBizCode = new AjaxButton("updateBizCode") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						RobFormB2Det robFormABizCodeForm  = (RobFormB2Det) form.getModelObject();
						
						
						SSMSessionSortableDataProvider dpProviderOri = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
						List<RobFormB2Det> listRobFormB2Det = dpProviderOri.getListResult();
						
						
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcListBizCode.get("sorting")).getDataProvider();
						List<RobFormB2Det> listSelected = dpProvider.getListResult();
						for (int i = 0; i < listSelected.size(); i++) {
							if(listSelected.get(i).getSelected()){
								boolean alreadyAdd = false;
								for (int j = 0; j < listRobFormB2Det.size(); j++) {
									if(listRobFormB2Det.get(j).getBizCode().equals(listSelected.get(i).getBizCode())){
										alreadyAdd=true;
										break;
									}
								}
								
								if(!alreadyAdd){//Not Allow Duplicate
									listRobFormB2Det.add(listSelected.get(i));
								}
							}
						}
						
						getSession().setAttribute("listRobFormB2BizCode_", (Serializable)listRobFormB2Det);
						
						editBizCodePopUp.close(target);
					}
				};
				add(updateBizCode);
			}
			
			
			
		}


		@Override
		public String getPageTitle() {
			return null;
		}

	}
