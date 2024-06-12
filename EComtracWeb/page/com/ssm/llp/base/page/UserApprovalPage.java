package com.ssm.llp.base.page;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.base.common.model.SysRole;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobSyncProfileAudit;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.EditLlpUserProfilePage;
import com.ssm.llp.mod1.page.ListLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({"all"})
public class UserApprovalPage extends BasePage{
	
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	public UserApprovalPage(final LlpUserProfile llpUserProfile, String msg) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpUserProfile;
			}
		}));
		if(StringUtils.isBlank(msg)) {
			ssmSuccess(SSMExceptionParam.USER_PROFILE_SUCCESS_EDIT, llpUserProfile.getUserRefNo());
		}else {
			ssmError(msg);
		}
		add(new UserApprovalForm("form", getDefaultModel(),llpUserProfile));
	}

	public UserApprovalPage(final LlpUserProfile llpUserProfile) {
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load(){
				return llpUserProfile;
			}
		}));
		
		add(new UserApprovalForm("form", getDefaultModel(),llpUserProfile));
	}
	
	private class UserApprovalForm extends Form implements Serializable{

		public UserApprovalForm(String id, final IModel m, final LlpUserProfile llpUserProfile) {
			super(id, m);
			
			SSMLabel loginId = new SSMLabel("loginId", llpUserProfile.getLoginId());
			add(loginId);
			
			SSMLabel ic = new SSMLabel("icNo", llpUserProfile.getIdNo());
			add(ic);
			
			SSMLabel hpNo = new SSMLabel("hpNo", llpUserProfile.getHpNo());
			add(hpNo);
			
			SSMLabel email = new SSMLabel("email", llpUserProfile.getEmail());
			add(email);
			
			String approveByString = "-";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			if(!StringUtils.isBlank(llpUserProfile.getApproveBy())){
				approveByString = llpUserProfile.getApproveBy() + "<br>" + sdf.format(llpUserProfile.getApproveDt());
			}
			SSMLabel approveBy = new SSMLabel("approveBy", approveByString);
			approveBy.setEscapeModelStrings(false);
			add(approveBy);
			
			SSMLabel ezbizName = new SSMLabel("ezbizName", llpUserProfile.getName());
			add(ezbizName);
			
			String userIdSuspended="";
			if(llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_suspend)) {
				userIdSuspended=" (ID "+Parameter.USER_STATUS_suspend_desc + ")" + "<br>" + resolve("page.lbl.ezbiz.userApprovalPage.IdSuspended");
			}else if(llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_deceased)) {
				userIdSuspended=" (ID "+getCodeTypeWithValue(Parameter.USER_STATUS, Parameter.USER_STATUS_deceased) + ")" + "<br>" + resolve("page.lbl.ezbiz.userApprovalPage.IdDeceased");
			}else if(llpUserProfile.getUserStatus().equals(Parameter.USER_STATUS_deactive)) {
				userIdSuspended=" (ID "+getCodeTypeWithValue(Parameter.USER_STATUS, Parameter.USER_STATUS_deactive) + ")" + "<br>" + resolve("page.lbl.ezbiz.userApprovalPage.IdDeactive");
			}else{
				llpUserProfile.setRemarks(""); //other than status suspended hide remark
			}
			SSMLabel IdSuspended = new SSMLabel("IdSuspended",userIdSuspended);
			add(IdSuspended);
			
			//llpUserProfile.setRemarks("");
			final SSMTextArea remarks = new SSMTextArea("remarks");
			remarks.setOutputMarkupId(true);
			remarks.setEnabled(true);
			add(remarks);
			
			final SSMRadioChoice userStatus = new SSMRadioChoice("userStatus",Parameter.USER_STATUS);
			if (StringUtils.isBlank(llpUserProfile.getUserRefNo())) {
				userStatus.setEnabled(false);
			} else {
				userStatus.setEnabled(true);
			}
			add(userStatus);
			
			final ModalWindow secondLevelApprovePopup = new ModalWindow("secondLevelApprovePopup");
			add(secondLevelApprovePopup);
			
			secondLevelApprovePopup.setCookieName("secondLevelApprovePopupCookies"+llpUserProfile.getUserRefNo());
			secondLevelApprovePopup.setResizable(true);
			
			secondLevelApprovePopup.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
				@Override
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {				
					return true;
				}
			});
			
			secondLevelApprovePopup.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				@Override
				public void onClose(AjaxRequestTarget target) {
				
					if(getSession().getAttribute("_approveby") != null ){
						String Pegawai = "SSM:"+(String) getSession().getAttribute("_approveby");
						getSession().removeAttribute("_approveby");
						
						
						String msg = null;
						if(StringUtils.isBlank(llpUserProfile.getRemarks())){
							msg = "failed.statusUpdate.user.profile.remarks.blank";
						}else{
							String remark = llpUserProfile.getRemarks() +"\n"+ "Verify By: "+Pegawai;
							llpUserProfile.setRemarks(remark);
							try {
								llpUserProfileService.updateStatus(llpUserProfile.getLoginId(),  llpUserProfile.getUserStatus() , llpUserProfile.getRemarks() , null);
							} catch (Exception e) {
								e.printStackTrace();
								ssmError(e.getMessage());
								return;
							}
						}
						setResponsePage(new UserApprovalPage(llpUserProfile, msg));
					}
					
				}
			});
			
			secondLevelApprovePopup.setPageCreator(new ModalWindow.PageCreator() {
				@Override
				public Page createPage() {
					return new SecondLevelLoginPage(UserApprovalPage.this.getPage(), secondLevelApprovePopup,
							llpUserProfile.getUserRefNo().toString());
				}
				
			});
			
			SSMLink back = new SSMLink("back"){
				@Override
				public void onClick(){
					setResponsePage(ListLlpUserProfilePage.class);
				}
			};
			
			add(back);
			
			SSMAjaxButton save = new SSMAjaxButton("save")  {
				public void onSubmit(AjaxRequestTarget target, Form form) {
					
					secondLevelApprovePopup.show(target);	
					
				}
			};
			save.setOutputMarkupId(true);
			add(save);
		}
		
	}

	@Override
	public String getPageTitle() {
		return "page.title.userApproval";
	}

}
