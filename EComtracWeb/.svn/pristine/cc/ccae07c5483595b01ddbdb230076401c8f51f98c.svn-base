package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormB2Panel extends RobFormBBasePanel{
	
	public EditRobFormB2Panel(String panelId, final RobFormB robFormB) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return robFormB.getRobFormB2();
            }
        }));
		add(new RobFormB2Form("robFormB2Form",getDefaultModel(),robFormB));
	}
	
	private class RobFormB2Form extends Form implements Serializable {
		final RobFormB robFormB;
		final RobFormB2 robFormB2;
		final SSMTextArea bizDesc;
		
		public RobFormB2Form(String id, IModel m, final RobFormB robFormB) {
			super(id, m);
			this.robFormB = robFormB;
			setPrefixLabelKey("page.lbl.ezbiz.robFormB2.");
			robFormB2 = (RobFormB2) m.getObject();
			
			MultiLineLabel currentbusinessInfo = new MultiLineLabel("currentbusinessInfo", robFormB.getBizProfileDetResp().getMainInfo().getBizDesc().toUpperCase());
			add(currentbusinessInfo);
			if(StringUtils.isBlank(robFormB2.getBizDesc())){
				robFormB2.setBizDesc(robFormB.getBizProfileDetResp().getMainInfo().getBizDesc().toUpperCase());
			}
			
			final WebMarkupContainer wmcB2 = new WebMarkupContainer("wmcB2");
			wmcB2.setPrefixLabelKey("page.lbl.ezbiz.robFormB2.");
			wmcB2.setOutputMarkupId(true);
			wmcB2.setOutputMarkupPlaceholderTag(true);
			add(wmcB2);
			
			if(!robFormB.getIsB2()){
				wmcB2.setVisible(false);
			}
			
			
			bizDesc = new SSMTextArea("bizDesc");
			wmcB2.add(bizDesc);
			if(StringUtils.isNotBlank(robFormB2.getBizDesc())){
				bizDesc.setDefaultModelObject(robFormB2.getBizDesc());
			}

			final SSMDateTextField b2AmmendmendDt = new SSMDateTextField("b2AmmendmendDt");
			wmcB2.add(b2AmmendmendDt);

			
			final ModalWindow editBizCodePopUp = new ModalWindow("editBizCodePopUp");
			editBizCodePopUp.setWidthUnit("px"); 
			editBizCodePopUp.setHeightUnit("px"); 
			editBizCodePopUp.setInitialWidth(800);
			editBizCodePopUp.setInitialHeight(500);

			add(editBizCodePopUp);
			
			final WebMarkupContainer wmcBizCode = new WebMarkupContainer("wmcBizCodeAll");
			wmcBizCode.setOutputMarkupId(true);
			wmcBizCode.setOutputMarkupPlaceholderTag(true);
			add(wmcBizCode);
			
			final SSMSessionSortableDataProvider dpBizCode = new SSMSessionSortableDataProvider("", robFormB2.getListRobFormB2Det());
			final SSMDataView<RobFormB2Det> dataViewBizCode = new SSMDataView<RobFormB2Det>("sortingRobFormB2BizCode", dpBizCode) {

				protected void populateItem(final Item<RobFormB2Det> item) {
					RobFormB2Det robFormB2DetBizCode = item.getModelObject();

					item.add(new SSMLabel("bizCodeNo", item.getIndex()+1));
					item.add(new SSMLabel("bizCode", robFormB2DetBizCode.getBizCode()));
					item.add(new MultiLineLabel("bizCodeDesc", robFormB2DetBizCode.getBizCodeDesc()));
					item.add(new SSMLabel("ammendmentType", robFormB2DetBizCode.getAmmendmentType(), Parameter.ROB_FORM_B2_AMENDMENT_TYPE) );
					
					SSMAjaxLink deleteBizCode = new SSMAjaxLink("deleteBizCode", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
							List<RobFormB2Det> list = dpProvider.getListResult();
							RobFormB2Det robFormB2DetBizCodeTmp = list.get(item.getIndex());
							if(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW.equals(robFormB2DetBizCodeTmp.getAmmendmentType())){
								list.remove(robFormB2DetBizCodeTmp);
							}else{
								robFormB2DetBizCodeTmp.setAmmendmentType(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_REMOVE);
							}
							dpBizCode.resetView(list);
							target.add(wmcBizCode);
						}
					};
					item.add(deleteBizCode);
					deleteBizCode.setConfirmQuestion(resolve("page.confirm.robFormB2.deleteBizCode"));
					
					SSMAjaxLink undoBranch = new SSMAjaxLink("undoBranch", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
							List<RobFormB2Det> list = dpProvider.getListResult();
							RobFormB2Det robFormB2DetBizCodeTmp = list.get(item.getIndex());
							robFormB2DetBizCodeTmp.setAmmendmentType(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NO_CHANGES);
							
							dpProvider.resetView(list);
							target.add(wmcBizCode);
						}
					};
					undoBranch.setVisible(false);
					undoBranch.setConfirmQuestion(resolve("page.confirm.robFormB2.undo"));
					item.add(undoBranch);
					
					if(!Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW.equals(robFormB2DetBizCode.getAmmendmentType())){
						if(robFormB2DetBizCode.getAmmendmentType().equals(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_REMOVE)){
							deleteBizCode.setVisible(false);
							undoBranch.setVisible(true);
						}
						if(robFormB2DetBizCode.getAmmendmentType().equals(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NO_CHANGES)){
							deleteBizCode.setVisible(true);
							undoBranch.setVisible(false);
						}
					}
					
					if(!robFormB.getIsB2()){
						deleteBizCode.setVisible(false);
						undoBranch.setVisible(false);
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
			
			wmcBizCode.add(dataViewBizCode);
			wmcBizCode.add(new SSMPagingNavigator("navigatorRobFormB2BizCode", dataViewBizCode));
			wmcBizCode.add(new NavigatorLabel("navigatorLabelRobFormB2BizCode", dataViewBizCode));
			
			
			SSMAjaxCheckBox isB2 = new SSMAjaxCheckBox("isB2", new PropertyModel(robFormB, "isB2") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						wmcB2.setVisible(true);
						robFormB.setIsB2(true);
					}else{
						wmcB2.setVisible(false);
						robFormB.setIsB2(false);
						robFormB2.setBizDesc(robFormB.getBizProfileDetResp().getMainInfo().getBizDesc().toUpperCase());
						bizDesc.setDefaultModelObject(robFormB2.getBizDesc());
					}
					target.add(wmcB2);
					
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
					List<RobFormB2Det> list = dpProvider.getListResult();
					List listToRemove = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						RobFormB2Det robFormB2DetBizCodeTmp = list.get(i);
						if(robFormB2DetBizCodeTmp.getAmmendmentType().equals(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NEW)){
							listToRemove.add(robFormB2DetBizCodeTmp);
						}else{
							robFormB2DetBizCodeTmp.setAmmendmentType(Parameter.ROB_FORM_B2_AMENDMENT_TYPE_NO_CHANGES);
						}
					}
					list.removeAll(listToRemove);
					dpProvider.resetView(list);
					target.add(wmcBizCode);
					
					
					
					
				}
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
					String confirmTitle = resolve("page.lbl.ezbiz.robFormB1.confirmResetB2Title");
			        String confirmDesc = resolve("page.lbl.ezbiz.robFormB1.confirmResetB2Desc");		
			        AjaxCallListener ajaxCallListener = generateAjaxConfirm(this ,confirmTitle,confirmDesc ,true);
			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			    }
			};
			add(isB2);
			isB2.setEnabled(false);
			if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
				isB2.setEnabled(true);
			}
			
			editBizCodePopUp.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
					List<RobFormB2Det> listRobFormB2BizCode = (List<RobFormB2Det>) getSession().getAttribute("listRobFormB2BizCode_");
//					
					dpProvider.resetView(listRobFormB2BizCode);
					
					target.add(wmcBizCode);
				}
			});
			
			SSMAjaxButton showBizCodePanel = new SSMAjaxButton("showBizCodePanel") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormABizCode>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
					List<RobFormB2Det> listRobFormB2BizCode = dpProvider.getListResult();
					editBizCodePopUp.setPageCreator(new ModalWindow.PageCreator() {
						@Override
						public Page createPage() {
							return new EditRobFormBBizCodePanel(  wmcBizCode , null, editBizCodePopUp);// edit record
						}
					});
					
					getSession().setAttribute("listRobFormB2BizCode_", (Serializable) listRobFormB2BizCode);
					editBizCodePopUp.show(target);
						
				}
			};
			showBizCodePanel.setDefaultFormProcessing(false);
			wmcB2.add(showBizCodePanel);
			
			final String b2ValidationJS = "b2Validation";
			String bizCodeFieldToValidate[] = new String[]{"b2AmmendmendDt","bizDesc"};
			String bizCodeFieldToValidateRules[] = new String[]{"empty","empty"};
			setSemanticJSValidation(this, "b2Validation", bizCodeFieldToValidate, bizCodeFieldToValidateRules);
			
			SSMAjaxButton b2Next = new SSMAjaxButton("b2Next", b2ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB2Det>)wmcBizCode.get("sortingRobFormB2BizCode")).getDataProvider();
					List<RobFormB2Det> list = dpProvider.getListResult();
					robFormB.getRobFormB2().setListRobFormB2Det(list);
					hideAndShowSegment(target, 2);//mean show segment 3 - B3
				}
			};
			add(b2Next);
			
			//Main Address
			SSMAjaxButton b2Prev = new SSMAjaxButton("b2Prev", b2ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 0);//mean show segment 3 - B1
				}
			};
			add(b2Prev);
			
			generateDiscardButton(this, robFormB);
		}
	}
}