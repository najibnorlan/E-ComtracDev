package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormABranchPanel;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormB3Panel extends RobFormBBasePanel{
	

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	final RobFormB3Form robFormB3Form;
	public EditRobFormB3Panel(String panelId, final RobFormB robFormB) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	RobFormB3 robFormB3 = new RobFormB3();
            	robFormB3.setB3AmmendmendDt(robFormB.getB3AmmendmendDt());
            	return robFormB3;
            }
        }));
		robFormB3Form = new RobFormB3Form("robFormB3Form", getDefaultModel(), robFormB);
		add(robFormB3Form);
	}
	
	@Override
	public void refreshPanel(AjaxRequestTarget target) {
		super.refreshPanel(target);
//		robFormB3Form.reCheckChanges(target);
	}
	private class RobFormB3Form extends Form implements Serializable {
		final RobFormB robFormB;
		final RobFormB3 robFormB3;
		final boolean isQuery;
		final SSMAjaxButton b3Next;
		
		public RobFormB3Form(String id, IModel m,final RobFormB robFormB) {
			super(id, m);
			
			setPrefixLabelKey("page.lbl.ezbiz.robFormB3.");
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			
			this.robFormB = robFormB;
			robFormB3 = (RobFormB3) m.getObject();
			
			final WebMarkupContainer wmcBranches = new WebMarkupContainer("wmcBranches");
			wmcBranches.setOutputMarkupId(true);
			wmcBranches.setOutputMarkupPlaceholderTag(true);
			add(wmcBranches);
			

			final WebMarkupContainer b3AmmendmendDtPanel = new WebMarkupContainer("b3AmmendmendDtPanel");
			b3AmmendmendDtPanel.setOutputMarkupId(true);
			b3AmmendmendDtPanel.setPrefixLabelKey("page.lbl.ezbiz.robFormB3.");
			b3AmmendmendDtPanel.setOutputMarkupPlaceholderTag(true);
			add(b3AmmendmendDtPanel);
			
			final SSMDateTextField b3AmmendmendDt = new SSMDateTextField("b3AmmendmendDt");
			b3AmmendmendDtPanel.add(b3AmmendmendDt);
			
			final EditRobFormB3DetPanel branchPanel = new EditRobFormB3DetPanel("editRobFormB3DetPanel",wmcBranches, robFormB);
			branchPanel.setOutputMarkupId(true);
			branchPanel.setOutputMarkupPlaceholderTag(true);

			if(!robFormB.getIsB3()){
				branchPanel.setVisible(false);
				b3AmmendmendDtPanel.setVisible(false);
			}
			add(branchPanel);
			
			
			final List listBranches = robFormB.getListRobFormB3();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
			final SSMDataView<RobFormB3> dataView = new SSMDataView<RobFormB3>("sortingRobFormB3Branch", dp) {

				protected void populateItem(final Item<RobFormB3> item) {
					RobFormB3 robFormB3Branches = item.getModelObject();

					item.add(new SSMLabel("branchNo", item.getIndex()+1));
					String address = robFormB3Branches.getAddr();
					if(StringUtils.isNotBlank(robFormB3Branches.getAddr2())){
						address += "\n"+robFormB3Branches.getAddr2();
					}
					if(StringUtils.isNotBlank(robFormB3Branches.getAddr3())){
						address += "\n"+robFormB3Branches.getAddr3();
					}
					address += "\n"+robFormB3Branches.getAddrPostcode()+" "+robFormB3Branches.getAddrTown();
					address = address +"\n"+getCodeTypeWithValue(Parameter.ROB_ALLOW_REG_STATE, robFormB3Branches.getAddrState()) ;
					if(StringUtils.isNotBlank(robFormB3Branches.getAddrUrl())){
						address += "\n"+robFormB3Branches.getAddrUrl();
					}
					
					item.add(new MultiLineLabel("branchAddress", address));
					item.add(new SSMLabel("ammendmentType", robFormB3Branches.getAmmendmentType(), Parameter.ROB_FORM_B3_AMENDMENT_TYPE));

					
					AjaxLink editBranch = new AjaxLink("editBranch", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							branchPanel.setModel(item.getIndex());
							target.add(branchPanel);
						}
					};
					item.add(editBranch);
					
					SSMAjaxLink undoBranch = new SSMAjaxLink("undoBranch", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
							List<RobFormB3> list = dpProvider.getListResult();
							RobFormB3 robFormB3Tmp = list.get(item.getIndex());
							if(robFormB3Tmp.getCbsBranchId()!=null){
								robFormB3Tmp.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES);
							}
							dpProvider.resetView(list);
							target.add(wmcBranches);
						}
					};
					undoBranch.setVisible(false);
					undoBranch.setConfirmQuestion(resolve("page.confirm.robFormB3.undo"));
					item.add(undoBranch);
					
					SSMAjaxLink delete = new SSMAjaxLink("deleteBranch", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							
							SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
							List<RobFormB3> list = dpProvider.getListResult();
							RobFormB3 robFormB3Tmp = list.get(item.getIndex());
							if(robFormB3Tmp.getCbsBranchId()!=null){
								robFormB3Tmp.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_CLOSE);
							}else{					
								list.remove(robFormB3Tmp);
							}
							dpProvider.resetView(list);
							target.add(wmcBranches);
						}
					};
					delete.setConfirmQuestion(resolve("page.confirm.robFormB3.deleteBranch"));
					item.add(delete);
					if(isQuery){
						delete.setVisible(false);
					}
					
					if(robFormB3Branches.getCbsBranchId()!=null){
						editBranch.setVisible(false);
						if(robFormB3Branches.getAmmendmentType().equals(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_CLOSE)){
							delete.setVisible(false);
							undoBranch.setVisible(true);
						}
						if(robFormB3Branches.getAmmendmentType().equals(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES)){
							delete.setVisible(true);
							undoBranch.setVisible(false);
						}
					}
					
					if(!robFormB.getIsB3()){
						delete.setVisible(false);
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

			wmcBranches.add(dataView);
			wmcBranches.add(new SSMPagingNavigator("navigatorRobFormB3", dataView));
			wmcBranches.add(new NavigatorLabel("navigatorLabelRobFormB3", dataView));
			
			SSMAjaxCheckBox isB3 = new SSMAjaxCheckBox("isB3", new PropertyModel(robFormB, "isB3") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					System.out.println(getValue());
					if (String.valueOf(true).equals(getValue())) {
						branchPanel.setVisible(true);
						robFormB.setIsB3(true);
					}else{
						branchPanel.setVisible(false);
						robFormB.setIsB3(false);
					}
					branchPanel.setVisible(robFormB.getIsB3());
					b3AmmendmendDtPanel.setVisible(robFormB.getIsB3());
					
					target.add(branchPanel);
					target.add(b3AmmendmendDtPanel);
					
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
					List<RobFormB3> list = dpProvider.getListResult();
					List listToRemove = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						RobFormB3 robFormB3Tmp = list.get(i);
						if(robFormB3Tmp.getAmmendmentType().equals(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NEW)){
							listToRemove.add(robFormB3Tmp);
						}else{
							robFormB3Tmp.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES);
						}
					}
					list.removeAll(listToRemove);
					dpProvider.resetView(list);
					target.add(wmcBranches);
					
				}
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
			        String confirmTitle = resolve("page.lbl.ezbiz.robFormB3.confirmResetB1Title");
			        String confirmDesc = resolve("page.lbl.ezbiz.robFormB3.confirmResetB1Desc");		
			        AjaxCallListener ajaxCallListener = generateAjaxConfirm(this,confirmTitle,confirmDesc,true);
			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			        
			    }
			};
			add(isB3);
			isB3.setEnabled(false);
			if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
				isB3.setEnabled(true);
			}

			final String b3ValidationJS = "b3Validation";
			String b3FieldToValidate[] = new String[]{"b3AmmendmendDt"};
			String b3FieldToValidateRules[] = new String[]{"empty"};
			setSemanticJSValidation(this, "b3Validation", b3FieldToValidate, b3FieldToValidateRules);
			
			
			SSMAjaxButton b3Prev = new SSMAjaxButton("b3Prev", b3ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 1);//mean show segment 3 - B1
				}
			};
			add(b3Prev);
			
			
			
			b3Next = new SSMAjaxButton("b3Next", b3ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormB3 robFormB3Tmp = (RobFormB3) form.getDefaultModelObject();
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
					List<RobFormB3> listRobFormB3 = dpProvider.getListResult();
//					robFormB.setListRobFormB3(listRobFormB3);
					robFormB.setB3AmmendmendDt(robFormB3Tmp.getB3AmmendmendDt());
					
//					if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
//						robFormBService.insertUpdateAll(robFormB);
//					}
//					if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
//						robFormBService.sendEmailToAllPartner(robFormB);
//					}
					robFormB.setListRobFormB3(listRobFormB3);
					hideAndShowSegment(target, 3);//mean show segment 3 - B3
				}
				
//				@Override
//			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
//			    {
//			        super.updateAjaxAttributes( attributes );
//			        List<RobFormOwnerVerification> listVer = robFormB.getListRobFormOwnerVerification();
//			        boolean allVerify = true;
//			        for (int i = 0; i < listVer.size(); i++) {
//						if(!Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED.equals(listVer.get(i).getStatus())){
//							allVerify = false;
//							break;
//						}
//					}
//			        if(!allVerify){
//				        AjaxCallListener ajaxCallListener = new AjaxCallListener();
//				        String text = resolve("page.confirm.robFormB3.sendNotificationEmailAlert");
//				        ajaxCallListener.onPrecondition( "return alert('" + text + "');" );
//				        attributes.getAjaxCallListeners().add( ajaxCallListener );
//			        }
//			    }
			};
			b3Next.setOutputMarkupId(true);
			add(b3Next);
			
			generateDiscardButton(this, robFormB);
		}
		
	}
}