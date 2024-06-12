package com.ssm.ezbiz.robformA;

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
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;

@SuppressWarnings("all")
public class EditRobFormABizCodePanel extends BaseFrame implements Serializable{

		private static final long serialVersionUID = 1L;
	
		private int bizCodeIdx = -1;
		
		@SpringBean(name = "LlpParametersService")
		private LlpParametersService llpParametersService;

		// edit
		public EditRobFormABizCodePanel(final RobFormABizCode robFormABizCode, final ModalWindow editBizCodePopUp) {
			this(robFormABizCode,editBizCodePopUp,-1);
		}
		public EditRobFormABizCodePanel(final RobFormABizCode robFormABizCode, final ModalWindow editBizCodePopUp, final int bizCodeIdx) {
			this.bizCodeIdx = bizCodeIdx;
			setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
	            protected Object load() {
	            	return new RobFormABizCode();
	            }
	        }));
			add(new RobFormABizCodeForm("RobFormABizCodeForm", getDefaultModel(), editBizCodePopUp));
		}
		
		
		/** form for processing the input. */
		private class RobFormABizCodeForm extends Form {
			
			public RobFormABizCodeForm(String name, IModel model, final ModalWindow editBizCodePopUp) {
				super(name, model);
				
				final RobFormABizCode robFormABizCode = (RobFormABizCode) model.getObject();
				
//				SSMTextField bizCode = new SSMTextField("bizCode");
//				add(bizCode);
				
				SSMTextField bizCodeDesc = new SSMTextField("bizCodeDesc");
				add(bizCodeDesc);
				
				final WebMarkupContainer wmcListBizCode = new WebMarkupContainer("wmcListBizCode");
				wmcListBizCode.setOutputMarkupId(true);
				wmcListBizCode.setOutputMarkupPlaceholderTag(true);
				add(wmcListBizCode);
				
//				code_desc
				
				final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("code", new ArrayList());
				final SSMDataView dataView = new SSMDataView<RobFormABizCode>("sorting", dp) {
					private static final long serialVersionUID = 1L;

					protected void populateItem(final Item<RobFormABizCode> item) {
						RobFormABizCode code = item.getModelObject();
						item.add(new SSMLabel("code", code.getBizCode()));
						item.add(new MultiLineLabel("desc", code.getBizCodeDesc()));
						// item.add(new SSMCheckBox("check", new PropertyModel(code,
						// "selected")));

						item.add(new AjaxCheckBox("check", new PropertyModel(code, "selected")) {
							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								RobFormABizCode businessObjectCode = (RobFormABizCode) getParent().getDefaultModelObject();
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
						RobFormABizCode robFormABizCodeForm  = (RobFormABizCode) form.getModelObject();
						List<RobFormABizCode> list = new ArrayList<RobFormABizCode>();
						if (StringUtils.isNotBlank(robFormABizCodeForm.getBizCode())|| StringUtils.isNotBlank(robFormABizCodeForm.getBizCodeDesc())) {
							SearchCriteria sc = new SearchCriteria("status", SearchCriteria.EQUAL, "A");
							sc = sc.andIfNotNull("codeType", SearchCriteria.EQUAL, Parameter.ROB_BUSINESS_CODE);
//							if(StringUtils.isNotBlank(robFormABizCodeForm.getBizCode())){
//								sc = sc.andIfNotNull("code", SearchCriteria.LIKE,  robFormABizCodeForm.getBizCode() + "%");
//							}
							if(StringUtils.isNotBlank(robFormABizCodeForm.getBizCodeDesc())){
//								System.out.println(robFormABizCode.getBizCodeDesc());
								sc = sc.andIfNotNull( "codeDesc", SearchCriteria.LIKE, "%" + robFormABizCodeForm.getBizCodeDesc() + "%");
							}
							
							List<LlpParameters> listDb = llpParametersService.findByCriteria(sc).getList();
							for (int i = 0; listDb!=null && i < listDb.size(); i++) {
								list.add(new RobFormABizCode(listDb.get(i).getCode(),listDb.get(i).getCodeDesc()));
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
						RobFormABizCode robFormABizCodeForm  = (RobFormABizCode) form.getModelObject();
						List<RobFormABizCode> listRobFormABizCode = (List<RobFormABizCode>) getSession().getAttribute("listRobFormABizCode_");
						
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcListBizCode.get("sorting")).getDataProvider();
						List<RobFormABizCode> listSelected = dpProvider.getListResult();
						for (int i = 0; i < listSelected.size(); i++) {
							if(listSelected.get(i).getSelected()){
								boolean alreadyAdd = false;
								for (int j = 0; j < listRobFormABizCode.size(); j++) {
									if(listRobFormABizCode.get(j).getBizCode().equals(listSelected.get(i).getBizCode())){
										alreadyAdd=true;
										break;
									}
								}
								
								if(!alreadyAdd){//Not Allow Duplicate
									listRobFormABizCode.add(listSelected.get(i));
								}
							}
						}
						
						
						getSession().setAttribute("listRobFormABizCode_", (Serializable) listRobFormABizCode);
						
						
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
