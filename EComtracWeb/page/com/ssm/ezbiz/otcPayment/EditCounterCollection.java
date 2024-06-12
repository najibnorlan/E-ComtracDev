package com.ssm.ezbiz.otcPayment;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ListCollectionCounter.ListCounterFormModel;
import com.ssm.ezbiz.service.RobCounterCollectionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobCounterCollection;

@SuppressWarnings({"all"})
public class EditCounterCollection extends SecBasePage{
	
	@SpringBean(name = "RobCounterCollectionService")
	RobCounterCollectionService robCounterCollectionService;
	
	public EditCounterCollection(){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobCounterCollection counterCollection = new RobCounterCollection();
				
				return counterCollection;
			}
		}));
		add(new EditCounterForm("form", getDefaultModel()));
	}
	
	public EditCounterCollection(final String ipAddress){
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobCounterCollection counterCollection = robCounterCollectionService.findByIpAddress(ipAddress);
				
				return counterCollection;
			}
		}));
		add(new EditCounterForm("form", getDefaultModel()));
	}
	
	private class EditCounterForm extends Form implements Serializable {

		public EditCounterForm(String id, IModel m) {
			super(id, m);
			final RobCounterCollection counterCollection = (RobCounterCollection) m.getObject();
			
			SSMLabel counterIp = new SSMLabel("counterIp", counterCollection.getIpAddress());
			counterIp.setOutputMarkupId(true);
			add(counterIp);
			
			SSMDropDownChoice branch = new SSMDropDownChoice("branch", Parameter.BRANCH_CODE);
			branch.setLabelKey("page.lbl.ezbiz.counteCollectionList.branch");
			branch.setRequired(true);
			add(branch);
			
			SSMDropDownChoice floorLevel = new SSMDropDownChoice("floorLevel", Parameter.FLOOR_LVL);
			floorLevel.setLabelKey("page.lbl.ezbiz.counteCollectionList.floorLevel");
			floorLevel.setRequired(true);
			add(floorLevel);
			
			SSMTextField counterName = new SSMTextField("counterName");
			counterName.setLabelKey("page.lbl.ezbiz.counteCollectionList.counterName");
			counterName.setRequired(true);
			add(counterName);
			
			 SSMDropDownChoice counterType = new SSMDropDownChoice("counterType", Parameter.PAYMENT_COUNTER_TYPE);
			 counterType.setLabelKey("page.lbl.ezbiz.counteCollectionList.counterType");
			 counterType.setRequired(true);
			 add(counterType);
			 
			 SSMTextField ipAddress = new SSMTextField("ipAddress");
			 ipAddress.setLabelKey("page.lbl.ezbiz.counteCollectionList.ipAddress");
			 ipAddress.setRequired(true);
			 ipAddress.setOutputMarkupId(true);
			 add(ipAddress);
			 
			 if(StringUtils.isBlank(counterCollection.getIpAddress())){
				 ipAddress.setVisible(true);
				 counterIp.setVisible(false);
			 }else{
				 ipAddress.setVisible(false);
				 counterIp.setVisible(true);
			 }
			 
			Button submit = new Button("submit") {
				public void onSubmit() {
					RobCounterCollection model = (RobCounterCollection) getForm().getDefaultModelObject();

					RobCounterCollection checkCounterName = validateCounter(model);
					RobCounterCollection checkIpAddress = robCounterCollectionService.findByIpAddress(model.getIpAddress());
					
					if(checkIpAddress == null){
						if (checkCounterName == null) {
							if (StringUtils.isBlank(counterCollection
									.getIpAddress())) {
								model.setSoftDelete(Parameter.YES_NO_no);
								model.setIsActive(Parameter.YES_NO_yes);
								robCounterCollectionService.insert(model);
							} else {
								robCounterCollectionService.update(model);
							}
							setResponsePage(ListCollectionCounter.class);
						}else{
							ssmError(SSMExceptionParam.COLLECTION_COUNTER_SAME_NAME, getCodeTypeWithValue(Parameter.BRANCH_CODE, checkCounterName.getBranch()), getCodeTypeWithValue(Parameter.FLOOR_LVL, checkCounterName.getFloorLevel()));
						}
					}else{
						ssmError(SSMExceptionParam.COLLECTION_COUNTER_EXIST, getCodeTypeWithValue(Parameter.BRANCH_CODE, checkIpAddress.getBranch()), getCodeTypeWithValue(Parameter.FLOOR_LVL, checkIpAddress.getFloorLevel()));
					}
				}
			};
			add(submit);
			
			SSMAjaxLink cancel = new SSMAjaxLink("cancel") {
				@Override
				public void onClick(AjaxRequestTarget target){
					setResponsePage(ListCollectionCounter.class);
				}
			};
			
			add(cancel);
		}
		
	}
	
	public RobCounterCollection validateCounter(RobCounterCollection robCounterCollection){
		SearchCriteria sc = new SearchCriteria("branch", SearchCriteria.EQUAL, robCounterCollection.getBranch());
		sc = sc.andIfNotNull("floorLevel", SearchCriteria.EQUAL, robCounterCollection.getFloorLevel());
		sc = sc.andIfNotNull("counterName", SearchCriteria.EQUAL, robCounterCollection.getCounterName());
		
		List<RobCounterCollection> cs = robCounterCollectionService.findByCriteria(sc).getList();
		if(cs.size() > 0){
			return cs.get(0);
		}
		
		return null;
	}
			
	@Override
	public String getPageTitle() {
		return "menu.myBiz.editCollectionCounter";
	}
}
