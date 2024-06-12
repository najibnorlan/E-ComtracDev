package com.ssm.ezbiz.robFormB;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB3;

@SuppressWarnings("all")
public class EditRobFormB3DetPanel extends SecBasePanel {
	private int branchIdx = -1;
	private final WebMarkupContainer wmcBranches;
	private RobFormB robFormB;

	public EditRobFormB3DetPanel(String panelId, WebMarkupContainer wmcBranches, RobFormB robFormB) {
		super(panelId);
		this.robFormB = robFormB;
		this.wmcBranches = wmcBranches;
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new RobFormB3();
            }
        }));
		add(new RobFormB3DetForm("robFormB3DetForm",getDefaultModel()));
	}
	public void setModel(final int branchIdx){
		this.branchIdx = branchIdx;
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
            	
            	if(branchIdx==-1 || dpProvider.getListResult().size()==0){
            		return new RobFormB3();
            	}
            	
            	RobFormB3 branchSelected = (RobFormB3) dpProvider.getListResult().get(branchIdx);
            	RobFormB3 newBranches = new RobFormB3();
            	newBranches.setRobFormB3Id(branchSelected.getRobFormB3Id());
				newBranches.setAddr(branchSelected.getAddr());
				newBranches.setAddr2(branchSelected.getAddr2());
				newBranches.setAddr3(branchSelected.getAddr3());
				newBranches.setAddrPostcode(branchSelected.getAddrPostcode());
				newBranches.setAddrTown(branchSelected.getAddrTown());
				newBranches.setAddrState(branchSelected.getAddrState());
				newBranches.setAddrUrl(branchSelected.getAddrUrl());
            	return newBranches;
            }
        }));
		RobFormB3DetForm aBranchesForm = new RobFormB3DetForm("robFormB3DetForm",getDefaultModel());
		replace(aBranchesForm);
	}
	
	private class RobFormB3DetForm extends Form  {
		final boolean isQuery;
		public SSMAjaxButton updateBranch;
		public RobFormB3DetForm(String id, IModel m) {
			super(id, m);
			setAutoCompleteForm(false);
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			
			setPrefixLabelKey("page.lbl.ezbiz.robFormB3.");
			final RobFormB3 branch = (RobFormB3) m.getObject();
			
			add(new SSMTextField("addr"));
			
			SSMTextField addr2 = new SSMTextField("addr2");
			addr2.setNoLabel();
			add(addr2);
			
			SSMTextField addr3 = new SSMTextField("addr3");
			addr3.setNoLabel();
			add(addr3);
			
//			add(new SSMTextField("addrTown"));
			SSMTextField addrPostcode = new SSMTextField("addrPostcode");
			add(addrPostcode);
			
//			SSMDropDownChoice addrState = new SSMDropDownChoice("addrState", Parameter.ROB_ALLOW_REG_STATE);
//			add(addrState);
			
			SSMTextField addrUrl= new SSMTextField("addrUrl");
			addrUrl.setUpperCase(false);
			add(addrUrl);
			
			WicketUtils.generatePostcodeTownState(this, addrPostcode, branch, "addrPostcode", "addrTown","addrState" , true );
			
			setOutputMarkupId(true);
			
			
			final String branchesAddBranchJS = "branchesAddBranch";
			String branchesAddBranchField[] = new String[]{"addr","addrTown","addrPostcode","addrTownTmp","addrStateDesc","addrUrl"};
			String branchesAddBranchFieldRules[] = new String[]{"empty","empty","exactLengthNumber[5]","empty","empty","isNotReqUrl"};
			setSemanticJSValidation(this, branchesAddBranchJS, branchesAddBranchField,branchesAddBranchFieldRules);
			
			updateBranch = new SSMAjaxButton("updateBranch", branchesAddBranchJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormB3 branchFromForm  = (RobFormB3) form.getDefaultModelObject();
					
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB3>)wmcBranches.get("sortingRobFormB3Branch")).getDataProvider();
					List<RobFormB3> list = dpProvider.getListResult();
					
					String town="",state=""; 
					
					SSMDropDownChoice townDD = (SSMDropDownChoice) form.get("addrTownTmp");
					String postcodeTownState = townDD.getDefaultModelObject().toString();
					if(StringUtils.isNotBlank(postcodeTownState)&&postcodeTownState.indexOf(":")>2){
						town = StringUtils.split(postcodeTownState,":")[1];
						state = StringUtils.split(postcodeTownState,":")[2];
					}
					
					String errorJs="";
					RobFormB3 newBranches = new RobFormB3();
					newBranches.setAddr(branchFromForm.getAddr());
					newBranches.setAddr2(branchFromForm.getAddr2());
					newBranches.setAddr3(branchFromForm.getAddr3());
					newBranches.setAddrPostcode(branchFromForm.getAddrPostcode());
					newBranches.setAddrTown(town);
					newBranches.setAddrState(state);
					newBranches.setAddrUrl(branchFromForm.getAddrUrl());
					newBranches.setAmmendmentType(Parameter.ROB_FORM_B3_AMENDMENT_TYPE_NEW);

					if(robFormB.getIsB1()){
						if(newBranches.compareToB1MainAddr(robFormB.getRobFormB1())){
							errorJs = "alert('"+resolve("page.lbl.ezbiz.robFormA.sameMainAddress")+"');";
						}
					}else{
						if(newBranches.compareToMainAddr(robFormB.getBizProfileDetResp().getMainInfo())){
							errorJs = "alert('"+resolve("page.lbl.ezbiz.robFormA.sameMainAddress")+"');";
						}
					}
					if(branchIdx==-1){
						for (int i = 0; i < list.size(); i++) {
							if(list.get(i).compareTo(newBranches)){
								errorJs = "alert('"+resolve("page.lbl.ezbiz.robFormA.sameBranchAddress")+"');";
							}
						}
						if(StringUtils.isBlank(errorJs)){
							list.add(newBranches);
						}
					}else{
						for (int i = 0; i < list.size() && i!=branchIdx; i++) {
							if(list.get(i).compareTo(newBranches)){
								errorJs = "alert('"+resolve("page.lbl.ezbiz.robFormA.sameBranchAddress")+"');";
							}
						}
						
						if(StringUtils.isBlank(errorJs)){
							RobFormB3 selectedBranch = list.get(branchIdx);
							selectedBranch.setAddr(branchFromForm.getAddr());
							selectedBranch.setAddr2(branchFromForm.getAddr2());
							selectedBranch.setAddr3(branchFromForm.getAddr3());
							selectedBranch.setAddrPostcode(branchFromForm.getAddrPostcode());
							selectedBranch.setAddrTown(town);
							selectedBranch.setAddrState(state);
							selectedBranch.setAddrUrl(branchFromForm.getAddrUrl());
							list.set(branchIdx, selectedBranch);
						}
					}
					
					branchIdx = -1;
					
					form.setDefaultModelObject(new RobFormB3());
					townDD.resetChild(new ArrayList());
					form.get("addrStateDesc").setDefaultModelObject("");
					
					
					if(isQuery){
						updateBranch.setVisible(false);
					}
					target.add(this.getParent().getParent());
					
					dpProvider.resetView(list);
					target.add(wmcBranches);
					if(StringUtils.isNotBlank(errorJs)){
						target.appendJavaScript(errorJs);
					}
				}
			};
			add(updateBranch);
			if(isQuery&&branch.getRobFormB3Id()==0){
				updateBranch.setVisible(false);
			}else{
				updateBranch.setVisible(true);
			}
			
		}

	}
}
