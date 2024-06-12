package com.ssm.ezbiz.robFormB;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.param.BusinessInfo;
import com.ssm.ws.RobBusinessSummaryInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormBPage extends SecBasePage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;
	
	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.robFormB.selectBiz";
	}
	
	
	public EditRobFormBPage(final RobBusinessSummaryInfo businessSummaryInfo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormB robFormB = new RobFormB();
				try {
					robFormB = robFormBService.generateRobDetailFromWs(businessSummaryInfo.getBrNo());
					
				} catch (SSMException e) {
					e.printStackTrace();
					ssmError(e);
					storeErrorMsg(e.getMessage());
					throw new RestartResponseAtInterceptPageException(SelectBizRobFormBPage.class);
				} catch (Exception e) {
					e.printStackTrace();
					ssmError(new SSMException("System Error Please try again later"));
					storeErrorMsg("System Error Please try again later");
					throw new RestartResponseAtInterceptPageException(SelectBizRobFormBPage.class);
				}
				return robFormB;
			}
		}));
		init(true);
	}
	public EditRobFormBPage(final BusinessInfo businessInfo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormB robFormB = new RobFormB();
				try {
						robFormB = robFormBService.generateRobDetailFromWs(businessInfo.getBrNo());
						
				} catch (SSMException e) {
					e.printStackTrace();
					ssmError(e);
					storeErrorMsg(e.getMessage());
					throw new RestartResponseAtInterceptPageException(SelectBizRobFormBPage.class);
				} catch (Exception e) {
					e.printStackTrace();
					ssmError(new SSMException("System Error Please try again later"));
					storeErrorMsg("System Error Please try again later");
					throw new RestartResponseAtInterceptPageException(SelectBizRobFormBPage.class);
				}
				return robFormB;
			}
		}));
		init(true);
	}
	
	public EditRobFormBPage(final String robFormBCode) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobFormB robFormB = robFormBService.findAllByIdWithWS(robFormBCode);
				return robFormB;
			}
		}));
		init(false);
	}
	
	private void init(boolean isNewApp) {
		
		RobFormB robFormB = (RobFormB) getDefaultModel().getObject();
		RobFormBForm form = new RobFormBForm("form", getDefaultModel());
		add(form);
		
		String robFormBNotSyncStr = "";
		if(StringUtils.isNotBlank(robFormB.getRobFormBCode())&& !isNewApp){
			try {
				if(!robFormBService.isBizInfoHashValid(robFormB) && Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
					robFormBNotSyncStr = resolve("page.lbl.ezbiz.robFormB.notSync", robFormB.getRobFormBCode());
					form.setVisible(false);
					robFormB.setStatus(Parameter.ROB_FORM_B_STATUS_CANCEL);
					robFormB.setApproveRejectNotes("Not Sync with core system");
					robFormBService.update(robFormB);
				}
			} catch (Exception e) {
				robFormBNotSyncStr = e.getMessage();
				form.setVisible(false);
			}
			
		}
		
		SSMLabel robFormBNotSync = new SSMLabel("robFormBNotSync",robFormBNotSyncStr);
		add(robFormBNotSync);
		
	}
	
	
	private class RobFormBForm extends Form implements Serializable {
		
		final RobFormB robFormB;
		
		public RobFormBForm(String id, IModel m) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormB.");
			robFormB = (RobFormB) m.getObject();
			
			add(new SSMLabel("robFormBCode",robFormB.getRobFormBCode() ));
			add(new SSMLabel("brNo",robFormB.getBrNo()+"-"+robFormB.getCheckDigit()));
			add(new SSMLabel("bizName",robFormB.getBizName() ));
			add(new SSMLabel("bizExpDt",robFormB.getBizExpDt(), "dd MMM yyyy"  ));
			
			boolean isQuery;
			
			SSMTextArea notes = new SSMTextArea("notes",Model.of(""));
			notes.setVisible(false);
			notes.setReadOnly(true);
			add(notes);
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
				RobFormNotes formNotes = robFormB.getListRobFormNotes().get(robFormB.getListRobFormNotes().size()-1);
				notes.setDefaultModelObject(formNotes.getNotes());
				notes.setVisible(true);
				if(StringUtils.isNotBlank(formNotes.getNotesAnswer())){
					robFormB.setQueryAnswer(formNotes.getNotesAnswer());
				}
			}else{
				isQuery = false;
			}
			
			
			final EditRobFormB1Panel b1Panel = new EditRobFormB1Panel("editRobFormB1Panel", robFormB);
			b1Panel.setOutputMarkupId(true);
			b1Panel.setOutputMarkupPlaceholderTag(true);
			add(b1Panel);
			
			final EditRobFormB2Panel b2Panel = new EditRobFormB2Panel("editRobFormB2Panel", robFormB);
			b2Panel.setOutputMarkupId(true);
			b2Panel.setOutputMarkupPlaceholderTag(true);
			add(b2Panel);
			
			final EditRobFormB3Panel b3Panel = new EditRobFormB3Panel("editRobFormB3Panel", robFormB);
			b3Panel.setOutputMarkupId(true);
			b3Panel.setOutputMarkupPlaceholderTag(true);
			add(b3Panel);
			
			final EditRobFormB4Panel b4Panel = new EditRobFormB4Panel("editRobFormB4Panel", robFormB);
			b4Panel.setOutputMarkupId(true);
			b4Panel.setOutputMarkupPlaceholderTag(true);
			add(b4Panel);
			
			final EditRobFormBOwnerVerificationPanel bOwnerVerificationPanel = new EditRobFormBOwnerVerificationPanel("bOwnerVerificationPanel", robFormB);
			bOwnerVerificationPanel.setOutputMarkupId(true);
			bOwnerVerificationPanel.setOutputMarkupPlaceholderTag(true);
			add(bOwnerVerificationPanel);
			
			final EditRobFormBSummaryPanel editRobFormBSummaryPanel = new EditRobFormBSummaryPanel("editRobFormBSummaryPanel", robFormB);
			editRobFormBSummaryPanel.setOutputMarkupId(true);
			editRobFormBSummaryPanel.setOutputMarkupPlaceholderTag(true);
			add(editRobFormBSummaryPanel);
			
			b1Panel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			b2Panel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			b3Panel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			b4Panel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			bOwnerVerificationPanel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			editRobFormBSummaryPanel.setAllPanel(b1Panel,b2Panel,b3Panel,b4Panel,bOwnerVerificationPanel,editRobFormBSummaryPanel);
			
//			String hideAllJs = "$('#"+b1Panel.getMarkupId()+"').hide();"; 
			String hideAllJs =  "";
			hideAllJs +=  "$('#"+b2Panel.getMarkupId()+"').hide();"; 
			hideAllJs +=  "$('#"+b3Panel.getMarkupId()+"').hide();";
			hideAllJs +=  "$('#"+b4Panel.getMarkupId()+"').hide();";
			hideAllJs +=  "$('#"+bOwnerVerificationPanel.getMarkupId()+"').hide();"; 
			hideAllJs +=  "$('#"+editRobFormBSummaryPanel.getMarkupId()+"').hide();"; 
//		
			Label hideAllLbl = new Label("hideAllLbl", hideAllJs);
			hideAllLbl.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		    add(hideAllLbl);
			
		    
		    AjaxEventBehavior event = new AjaxEventBehavior("onload") {
		        @Override
		        protected void onEvent(final AjaxRequestTarget target) {
		            target.appendJavaScript("alert('onload');");
		        }
		    };
		    add(event);
		}
	}
}