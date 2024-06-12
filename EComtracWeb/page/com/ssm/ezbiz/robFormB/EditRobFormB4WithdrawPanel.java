package com.ssm.ezbiz.robFormB;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormB4;	

@SuppressWarnings("all")
public class EditRobFormB4WithdrawPanel extends SSMPopUpPanel {
	
	public EditRobFormB4WithdrawPanel(String panelId) {
		super(panelId , "page.lbl.ezbiz.robFormB4."+panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new RobFormB4();
            }
        }));
		add(new EditRobFormB4WithdrawPanelForm("EditRobFormB4WithdrawPanelForm",getDefaultModel(), -1));
	}
	private class EditRobFormB4WithdrawPanelForm extends Form  {
		private int ownersIdx = -1;
		
		public EditRobFormB4WithdrawPanelForm(String id, IModel m , int ownIdx) {
			super(id, m);
			this.ownersIdx=ownIdx;
			setAutoCompleteForm(false);
			setPrefixLabelKey("page.lbl.ezbiz.robFormB4.");
			
			
			final SSMTextField stateDesc = new SSMTextField( "addrStateDesc", new PropertyModel("" , ""));
			stateDesc.setOutputMarkupId(true);
			stateDesc.setOutputMarkupPlaceholderTag(true);
			stateDesc.setReadOnly(true);
			add(stateDesc);
			
		}
	}
}
