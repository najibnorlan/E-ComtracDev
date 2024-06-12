package com.ssm.ezbiz.robFormB;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.ExtInterface;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.ezbiz.model.RobFormB;

public class RobFormBBasePanel extends SecBasePanel{

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	EditRobFormB1Panel b1Panel;
	EditRobFormB2Panel b2Panel;
	EditRobFormB3Panel b3Panel;
	EditRobFormB4Panel b4Panel;
	EditRobFormBOwnerVerificationPanel bOwnerVerificationPanel;
	EditRobFormBSummaryPanel editRobFormBSummaryPanel;
	RobFormBBasePanel[] segmentContainer;
	
	
	public RobFormBBasePanel(String panelId) {
		super(panelId);
	}
	public RobFormBBasePanel() {
		super();
	}
	
	public void setAllPanel(final EditRobFormB1Panel b1Panel, final EditRobFormB2Panel b2Panel, final EditRobFormB3Panel b3Panel, final EditRobFormB4Panel b4Panel,
			final EditRobFormBOwnerVerificationPanel bOwnerVerificationPanel, final EditRobFormBSummaryPanel editRobFormBSummaryPanel) {
		this.b1Panel = b1Panel;
		this.b2Panel = b2Panel;
		this.b3Panel = b3Panel;
		this.b4Panel = b4Panel;
		this.bOwnerVerificationPanel = bOwnerVerificationPanel;
		this.editRobFormBSummaryPanel = editRobFormBSummaryPanel;
		
		segmentContainer = new RobFormBBasePanel[6];
		segmentContainer[0] = b1Panel;
		segmentContainer[1] = b2Panel;
		segmentContainer[2] = b3Panel;
		segmentContainer[3] = b4Panel;
		segmentContainer[4] = bOwnerVerificationPanel;
		segmentContainer[5] = editRobFormBSummaryPanel;
		
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
		
		segmentContainer[segmentIdToShow].refreshPanel(target);
		
		target.appendJavaScript(js);
	}
	
	public void refreshPanel(AjaxRequestTarget target){
//		System.out.println("Test");
	}
	
	public void generateDiscardButton(Form form,final RobFormB robFormB){
		//Main Address
		SSMAjaxButton discard = new SSMAjaxButton("discard") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_DISCARD);
				robFormBService.update(robFormB);
				
				SignInSession signInSession = (SignInSession)getSession();
				if(null != signInSession && Parameter.LOGIN_TYPE_interface.equals(signInSession.getLoginType())) {
					ssmError("Application had been discarded");
					setResponsePage(ExtInterface.class);
				}else {
					setResponsePage(TabRobFormBPage.class);
				}
				
			}
			protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
		    {
		        super.updateAjaxAttributes( attributes );
		        String confirmTitle = resolve("page.confirm.robFormB.discardApplicationConfirmTitle");
		        String confirmDesc = resolve("page.confirm.robFormB.discardApplicationConfirmTitleDesc");		
		        AjaxCallListener ajaxCallListener = generateAjaxConfirm(this, confirmTitle, confirmDesc);
		        attributes.getAjaxCallListeners().add( ajaxCallListener );
		    }
		};
		form.add(discard);
		discard.setVisible(false);
		
		if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
			discard.setVisible(true);
		}
	}
}
