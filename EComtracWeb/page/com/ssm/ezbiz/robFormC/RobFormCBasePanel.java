package com.ssm.ezbiz.robFormC;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.ssm.llp.base.page.SecBasePanel;

public class RobFormCBasePanel extends SecBasePanel{
	
	
	EditRobFormCOwnerVerificationPanel cOwnerVerificationPanel;
	EditRobFormCSummaryPanel editRobFormCSummaryPanel;
	Panel[] segmentContainer;
	
	
	public RobFormCBasePanel(String panelId) {
		super(panelId);
	}
	public RobFormCBasePanel() {
		super();
	}
	
	public void setAllPanel(final EditRobFormCOwnerVerificationPanel cOwnerVerificationPanel) {
	
		this.cOwnerVerificationPanel = cOwnerVerificationPanel;
		//this.editRobFormCSummaryPanel = editRobFormCSummaryPanel;
		
		segmentContainer = new Panel[1];
		
		segmentContainer[0] = cOwnerVerificationPanel;
		//segmentContainer[1] = editRobFormCSummaryPanel;
		
	}
	
	public final void hideAndShowSegment(AjaxRequestTarget target, int segmentIdToShow){
		
		String js = "var toOpts = { direction: 'right' };";
		for (int i = 0; i < segmentContainer.length; i++) {
			if(i == segmentIdToShow){
				continue;
			}
			js += "$('#"+segmentContainer[i].getMarkupId()+"').hide();"; 
		}
		js += "if($('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').is(':hidden')){";
		js += "$('#"+segmentContainer[segmentIdToShow].getMarkupId()+"').toggle('slide', toOpts, 500).is(':hidden');"; 
		js += "}";
		
		String scroll = "\n$.scrollTo(document.getElementById('"+segmentContainer[segmentIdToShow].getMarkupId()+"'),100);\n";
		js += scroll;
		
		target.appendJavaScript(js);
	}
}
