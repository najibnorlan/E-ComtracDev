package com.ssm.ezbiz.robFormB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

import com.ssm.ezbiz.robformA.EditRobFormABranchPanel;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB3;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormBOwnerVerificationPanel extends RobFormBBasePanel{
	
	final RobFormBOwnerVerificationPanelForm bOwnerVerificationPanelForm;
	public EditRobFormBOwnerVerificationPanel(String panelId, final RobFormB robFormB) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new RobFormB3();
            }
        }));
		bOwnerVerificationPanelForm = new RobFormBOwnerVerificationPanelForm("robFormBOwnerVerificationPanelForm", getDefaultModel(), robFormB);
		add(bOwnerVerificationPanelForm);
	}
	@Override
	public void refreshPanel(AjaxRequestTarget target) {
		super.refreshPanel(target);
		bOwnerVerificationPanelForm.refreshPanel(target);
	}
	
	private class RobFormBOwnerVerificationPanelForm extends Form implements Serializable {
		final RobFormB robFormB;
		final boolean isQuery;
		final WebMarkupContainer wmcPartnerVerification;
		
		public RobFormBOwnerVerificationPanelForm(String id, IModel m, RobFormB robFormB) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormB3.");
			
			if(Parameter.ROB_FORM_A_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			this.robFormB = robFormB;
			
			wmcPartnerVerification = new WebMarkupContainer("wmcPartnerVerification");
			wmcPartnerVerification.setOutputMarkupId(true);
			wmcPartnerVerification.setOutputMarkupPlaceholderTag(true);
			add(wmcPartnerVerification);
			
			
			final List listBranches = robFormB.getListRobFormOwnerVerification();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listBranches);
			final SSMDataView<RobFormOwnerVerification> dataView = new SSMDataView<RobFormOwnerVerification>("sortingRobFormBOwnerVeri", dp) {

				protected void populateItem(final Item<RobFormOwnerVerification> item) {
					RobFormOwnerVerification robFormOwnerVer = item.getModelObject();

					item.add(new SSMLabel("ownerNo", item.getIndex()+1));
//					item.add(new SSMLabel("name", robFormOwnerVer.getName()));
					
//					String fullAddress = "";
//					if(robFormOwnerVer.getCbsOwnerInfo() != null) {
//						if(StringUtils.isNotBlank(robFormOwnerVer.getCbsOwnerInfo().getAddr())){
//							fullAddress = robFormOwnerVer.getCbsOwnerInfo().getAddr().toUpperCase();
//						}
//						if(StringUtils.isNotBlank(robFormOwnerVer.getCbsOwnerInfo().getAddr2())){
//							fullAddress+="\n"+robFormOwnerVer.getCbsOwnerInfo().getAddr2().toUpperCase();
//						}
//						if(StringUtils.isNotBlank(robFormOwnerVer.getCbsOwnerInfo().getAddr3())){
//							fullAddress+="\n"+robFormOwnerVer.getCbsOwnerInfo().getAddr3().toUpperCase();
//						}
//						
//						fullAddress += "\n"+robFormOwnerVer.getCbsOwnerInfo().getAddrPostcode()+" ";
//						if(StringUtils.isNotBlank(robFormOwnerVer.getCbsOwnerInfo().getAddrTown())){
//							fullAddress+=robFormOwnerVer.getCbsOwnerInfo().getAddrTown().toUpperCase();
//						}
//								
//						fullAddress += "\n"+getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormOwnerVer.getCbsOwnerInfo().getAddrState()).toUpperCase() ;
//					}
					
					
//					MultiLineLabel nameWAddr = new MultiLineLabel("name", robFormOwnerVer.getName()+"\n"+fullAddress );
					MultiLineLabel nameWAddr = new MultiLineLabel("name", robFormOwnerVer.getName());
					item.add(nameWAddr);
					
					
					
					item.add(new SSMLabel("idNo", robFormOwnerVer.getIdNo()));
					SSMLabel veriStatus = new SSMLabel("status", robFormOwnerVer.getStatus(), Parameter.ROB_OWNER_B_C_VERI_STATUS);
					item.add(veriStatus);
//					SSMLabel emailStatus = new SSMLabel("emailStatus", robFormOwnerVer.getEmailStatus(), Parameter.ROB_OWNER_VER_EMAIL_STATUS);
//					item.add(emailStatus);

					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}

			};

			wmcPartnerVerification.add(dataView);
			wmcPartnerVerification.add(new SSMPagingNavigator("navigatorRobFormBOwnerVeri", dataView));
			wmcPartnerVerification.add(new NavigatorLabel("navigatorLabelRobFormBOwnerVeri", dataView));
			
			SSMAjaxButton sendNotification = new SSMAjaxButton("sendNotification") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
				}
			};
//			add(sendNotification);
			
			SSMAjaxButton bOwnerVerPrev = new SSMAjaxButton("bOwnerVerPrev") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 3);//mean show segment 3 - B1
				}
			};
			add(bOwnerVerPrev);
			
			
			SSMAjaxButton bOwnerVerNext = new SSMAjaxButton("bOwnerVerNext") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 5);//mean show segment 3 - B3
				}
			};
			add(bOwnerVerNext);
			
			generateDiscardButton(this, robFormB);
		}
		void refreshPanel(AjaxRequestTarget target){
			SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormOwnerVerification>)wmcPartnerVerification.get("sortingRobFormBOwnerVeri")).getDataProvider();
			List<RobFormOwnerVerification> list = dpProvider.getListResult();
			dpProvider.resetView(list);
			target.add(wmcPartnerVerification);
		}
		
	}
}