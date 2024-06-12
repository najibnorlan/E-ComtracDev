package com.ssm.llp.mod1.page;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.SecondLevelLoginPage;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.mod1.service.RobUserOkuService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ViewOkuRegistrationPage extends SecBasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobUserOkuService")
	private RobUserOkuService robUserOkuService;
	
	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	
	@SpringBean(name = "RobFormNotesService")
	private RobFormNotesService robFormNotesService;
	
	
	//by default BasePage OKU menu go to this pageView
	//public
	public ViewOkuRegistrationPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				RobUserOku robUserOku = new RobUserOku();
				LlpUserProfile llpUserProfile = new LlpUserProfile();
				try {
					if((UserEnvironmentHelper.getUserenvironment() != null) && (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment)) { //LlpUserEnviroment=external user(public), InternalUserEnviroment=internal(bo), UserEnvironmentTemp=?? 
						llpUserProfile = ((LlpUserEnviroment)UserEnvironmentHelper.getUserenvironment()).getLlpUserProfile(); //get profile from environment after public login
					}else {
						llpUserProfile = (LlpUserProfile) llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName()); //search from db if null or internal officer login etc
					}
					
					if(llpUserProfile!=null) {
						RobUserOku robUserOkuTmp = (RobUserOku) robUserOkuService.findOkuByUserRefNoWithData(llpUserProfile.getUserRefNo()); //find latest record (pintu masuk)
						if(robUserOkuTmp!=null) {
							robUserOku = (RobUserOku) robUserOkuTmp;
						}else {
							setResponsePage(EditOkuRegistrationPage.class); //for 1st time registration (public)
							//return null; //jd error null pointer..
						}
						robUserOku.setUserProfile(llpUserProfile);
						robUserOku.setIdRegUser(llpUserProfile.getIdRegUser());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return robUserOku;
			}
		}));

		init();
	}

	public ViewOkuRegistrationPage(final String okuRefNo) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return robUserOkuService.findOkuByRefNoWithData(okuRefNo);
			}
		}));
		
		init();
	}
	
	//officer internal
	public ViewOkuRegistrationPage(final RobUserOku robUserOku) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				
//				//getSession().setAttribute("robUserOku_", robUserOkuSearchPage);
//				//RobUserOku robUserOku = (RobUserOku) getSession().getAttribute("robUserOku_");
//				RobUserOku robUserOku = robUserOkuSearchPage;
//				if(robUserOku.getDocDataId()!=null) {
//				//	Hibernate.initialize(robUserOku.getDocDataId()); //di dataobject fetch.lazy
//					robUserOku = (RobUserOku) robUserOkuService.initializeOkuWithDataId(robUserOkuSearchPage); //initialize fetch.LAZY
//				}
				return robUserOku;
			}
		}));
		
		init();
	}
	
	
	//reload page
	public ViewOkuRegistrationPage(final RobUserOku robUserOku, String msg) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return robUserOku;
			}
		}));
		
		if(StringUtils.isNotBlank(msg)) {
			if(msg.contains("error")) {
				ssmError(msg);
			}else {
				ssmSuccess(msg);
			}
		}
		
		init();
	}
	



	private void init() {
		add(new ViewOkuRegistrationForm("form", getDefaultModel()));
	}


	private class ViewOkuRegistrationForm extends Form {
		public ViewOkuRegistrationForm(String id, IModel m) {
			super(id, m);
			
			//SSMLabel remarks, approveBy;
			SSMLabel okuRegStatus;
			Button editBtn, cancelBtn;
			
			final RobUserOku robUserOku = (RobUserOku) m.getObject();
			
			//from table rob_user_profile
			add(new SSMLabel("userRefNo", robUserOku.getUserProfile().getUserRefNo()));
			add(new SSMLabel("name", robUserOku.getUserProfile().getName()));
			add(new SSMLabel("idType", robUserOku.getUserProfile().getIdType(),Parameter.ID_TYPE));
			add(new SSMLabel("idNo" , robUserOku.getUserProfile().getIdNo()));
			add(new SSMLabel("nationality" , robUserOku.getUserProfile().getNationality(), Parameter.NATIONALITY_TYPE));
			add(new SSMLabel("gender" , robUserOku.getUserProfile().getGender(), Parameter.GENDER));
			add(new SSMLabel("race" , robUserOku.getUserProfile().getRace(),Parameter.RACE));
			
			//from table rob_user_oku
			add(new SSMLabel("okuRefNo", robUserOku.getOkuRefNo())); 
			add(new SSMLabel("okuCardNo", robUserOku.getOkuCardNo()));
			add(new SSMLabel("okuCategory", robUserOku.getOkuCategory(),Parameter.OKU_CATEGORY));
			
			//add(new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(),Parameter.OKU_REGISTRATION_STATUS));
			add(okuRegStatus = new SSMLabel("okuRegStatus", robUserOku.getOkuRegStatus(),Parameter.OKU_REGISTRATION_STATUS));
			if(Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:green;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_PENDING.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:blue;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:blue;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_REJECT.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:red;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:red;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_CANCEL.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}else if (Parameter.OKU_REGISTRATION_STATUS_WITHDRAW.equals(robUserOku.getOkuRegStatus())) {
				okuRegStatus.addStyle("color:orange;font-weight: bolder; border: 2px solid powderblue;");
			}
			
			add(new SSMLabel("applicationDt", robUserOku.getApplicationDt()));
			add(new SSMLabel("approvalDt", robUserOku.getApprovalDt()));
			
	/*		add(approveBy = new SSMLabel("approveBy", robUserOku.getApproveBy()));	
			add(remarks = new SSMLabel("remarks", robUserOku.getRemarks()));
			if(!UserEnvironmentHelper.isInternalUser()){
				remarks.setVisible(false);
				approveBy.setVisible(false);
			} 
	*/		
			
			setMultiPart(true); //avoid error multiple click button
			
			//download oku attachment
			SSMDownloadLink downloadSuppDocOku = new SSMDownloadLink("downloadSuppDocOku");
			downloadSuppDocOku.setVisible(false);
			if(robUserOku.getDocDataId()!=null) {
				downloadSuppDocOku.setDownloadData(robUserOku.getUserProfile().getIdNo()+"_OKU_DOCUMENT.pdf", "application/pdf", robUserOku.getDocDataId().getFileData());
				downloadSuppDocOku.setVisible(true);
			}
			add(downloadSuppDocOku);
			
			
			editBtn = new Button("editBtn") {
				public void onSubmit() {
					setResponsePage(new EditOkuRegistrationPage(robUserOku,null));
				}
			};
			add(editBtn);
			
			cancelBtn = new Button("cancelBtn") {
				public void onSubmit() {
				if(UserEnvironmentHelper.isInternalUser()){
						//setResponsePage(ListRobUserOkuSearchPage.class);
						setResponsePage(ListRobUserOkuWorkingTrayPage.class);
				}else {
						setResponsePage(GuidelinePage.class);	
				  }
				}
			}.setDefaultFormProcessing(false);
			add(cancelBtn);
			
			
			//wmcApproval (internal officer)-------------------------------------
			final WebMarkupContainer wmcApproval = new WebMarkupContainer("wmcApproval");
			final WebMarkupContainer wmcApprovalLbl = new WebMarkupContainer("wmcApprovalLbl");

			//		String prefixLabelKey = "lbl.user.profile.oku."; 
			//		wmcApproval.setPrefixLabelKey(prefixLabelKey); //add to wmc
			
			wmcApproval.setOutputMarkupPlaceholderTag(true);
			wmcApproval.setOutputMarkupId(true);
			wmcApproval.setVisible(false);
			
			wmcApprovalLbl.setOutputMarkupPlaceholderTag(true);
			wmcApprovalLbl.setOutputMarkupId(true);
			wmcApprovalLbl.setVisible(false);
			wmcApprovalLbl.add(new SSMLabel("approveBy", robUserOku.getApproveBy()));	 //internal only can view..
			wmcApprovalLbl.add(new SSMLabel("remarks", robUserOku.getRemarks()));				//this..
			
			
			if(UserEnvironmentHelper.isInternalUser()) {
				wmcApproval.setVisible(true);
				wmcApprovalLbl.setVisible(true);
				
				//test branchcode (takde info kat uam)
			/*	UamUserProfileService uamUserProfileService = (UamUserProfileService) WicketApplication.getServiceNew(UamUserProfileService.class.getSimpleName());
				String cbsUserId = StringUtils.remove(UserEnvironmentHelper.getLoginName(),"SSM:");
				String branchCode = uamUserProfileService.getDefaultBranch(cbsUserId);
				System.out.println("UserEnvironmentHelper.getLoginName: "+UserEnvironmentHelper.getLoginName());
				System.out.println("cbsUserId: "+cbsUserId);
				System.out.println("branchCode: " +branchCode); 
				
				//test branchcode (by ip segment)
				String ipAddr = getIpAddress();
				String ip3Segment = ipAddr;
				if(ipAddr.contains(".")){
					ip3Segment = ipAddr.substring(0,ipAddr.lastIndexOf(".")); }
				String branchByIp = llpParametersService.findByCodeTypeValue(Parameter.IP_SEGMENT_BY_BRANCH, ip3Segment);
				System.out.println("ipAddr:"+ipAddr);
				System.out.println("ip3Segment:"+ip3Segment);
				System.out.println("branchByIp:"+branchByIp); */
			}
			add(wmcApproval);
			add(wmcApprovalLbl);
			
			final SSMTextArea remarksRejectQueryRevoke = new SSMTextArea("remarksRejectQueryRevoke", Model.of("")); //text area input
			remarksRejectQueryRevoke.setOutputMarkupId(true);
			remarksRejectQueryRevoke.setOutputMarkupPlaceholderTag(true);
			remarksRejectQueryRevoke.setVisible(false);
			remarksRejectQueryRevoke.setRequired(true);
			wmcApproval.add(remarksRejectQueryRevoke);

			final SSMLabel remarksType = new SSMLabel("remarksType", ""); //text area label
			remarksType.setOutputMarkupId(true);
			remarksType.setOutputMarkupPlaceholderTag(true);
			remarksType.setVisible(false);
			wmcApproval.add(remarksType);
			
			
			//---2nd level approval------------------------------------------------------------------------------
			final ModalWindow secondLevelApprovePopup = new ModalWindow("secondLevelApprovePopup");
			secondLevelApprovePopup.setCookieName("secondLevelApprovePopupCookies"+robUserOku.getUserProfile().getUserRefNo());
			//secondLevelApprovePopup.setCookieName("secondLevelApprovePopupCookies"+robUserOku.getOkuRefNo());
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
						String pegawai = "SSM:"+(String) getSession().getAttribute("_approveby");
						getSession().removeAttribute("_approveby");
						
						robUserOku.setRemarks("Approved by: "+UserEnvironmentHelper.getLoginName()+"; Verified by: "+pegawai
								+"\n Reason: "+robUserOku.getRemarks());
						
						robUserOkuService.updateOkuApproval(robUserOku);
						setResponsePage(new ViewOkuRegistrationPage(robUserOku,null)); //reload
					}
					
				}
			});
			
			secondLevelApprovePopup.setPageCreator(new ModalWindow.PageCreator() {
				@Override
				public Page createPage() {
					return new SecondLevelLoginPage(ViewOkuRegistrationPage.this.getPage(), secondLevelApprovePopup,
							robUserOku.getUserProfile().getUserRefNo().toString());
							//robUserOku.getOkuRefNo().toString());
				}
				
			});
			
			wmcApproval.add(secondLevelApprovePopup);
			
			//-------------------------------------------------------------------------------------------------

			
			//button submit (query/reject/revoke/cancelQuery/cancelRevoke)
			final SSMAjaxButton submitRejectQueryRevokeBtn = new SSMAjaxButton("submitRejectQueryRevokeBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					if((Parameter.OKU_REGISTRATION_STATUS_REJECT.equals(robUserOku.getOkuRegStatus()))  || 
						(Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) ||
						(Parameter.OKU_REGISTRATION_STATUS_CANCEL.equals(robUserOku.getOkuRegStatus())) || //cancel query
						(Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE.equals(robUserOku.getOkuRegStatus()))) {  
					robUserOku.setRemarks(StringUtils.isNotBlank(remarksRejectQueryRevoke.getDefaultModelObject().toString())?
							remarksRejectQueryRevoke.getDefaultModelObject().toString():""); //officer remark
					
					}else if (Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus())){
						robUserOku.setRemarks(""); //if status query refer remark from formNotes.
						RobFormNotes formNotes = new RobFormNotes();
						formNotes.setNotes(remarksRejectQueryRevoke.getDefaultModelObject().toString());
						formNotes.setRobFormCode(robUserOku.getOkuRefNo());
						formNotes.setRobFormType("OKU_REG_QUERY");
						formNotes.setQueryBy(UserEnvironmentHelper.getLoginName());
						robFormNotesService.insert(formNotes);
						} 
					
					robUserOku.setApproveBy(UserEnvironmentHelper.getLoginName()); //officer id
					
					if((Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) ||
						(Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE.equals(robUserOku.getOkuRegStatus()))) {
						secondLevelApprovePopup.show(target); //revoke perlu 2nd layer approval
							}
					else {
					robUserOkuService.updateOkuApproval(robUserOku);
					setResponsePage(ListRobUserOkuWorkingTrayPage.class);
					}
				};
			};
			submitRejectQueryRevokeBtn.setOutputMarkupId(true);
			submitRejectQueryRevokeBtn.setOutputMarkupPlaceholderTag(true);
			submitRejectQueryRevokeBtn.setVisible(false);
			wmcApproval.add(submitRejectQueryRevokeBtn);

			
			SSMAjaxLink reject = new SSMAjaxLink("reject") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_REJECT);
					remarksRejectQueryRevoke.setVisible(true);
					remarksType.setVisible(true);
					submitRejectQueryRevokeBtn.setVisible(true);
					remarksType.setDefaultModelObject(resolve("page.lbl.user.profile.oku.remarksType.rejectReason"));
					target.add(remarksRejectQueryRevoke);
					target.add(remarksType);
					target.add(submitRejectQueryRevokeBtn);
				}
			};
			reject.setOutputMarkupId(true);
			reject.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(reject);

			
			SSMAjaxLink query = new SSMAjaxLink("query") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_QUERY);
					remarksRejectQueryRevoke.setVisible(true);
					remarksType.setVisible(true);
					submitRejectQueryRevokeBtn.setVisible(true);
					remarksType.setDefaultModelObject(resolve("page.lbl.user.profile.oku.remarksType.queryReason"));
					target.add(remarksRejectQueryRevoke);
					target.add(remarksType);
					target.add(submitRejectQueryRevokeBtn);
				}
			};
			query.setOutputMarkupId(true);
			query.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(query);
			
			
			SSMAjaxLink cancelQuery = new SSMAjaxLink("cancelQuery") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_CANCEL); //reuse
					remarksRejectQueryRevoke.setVisible(true);
					remarksType.setVisible(true);
					submitRejectQueryRevokeBtn.setVisible(true);
					remarksType.setDefaultModelObject(resolve("page.lbl.user.profile.oku.remarksType.cancelQueryReason"));
					target.add(remarksRejectQueryRevoke);
					target.add(remarksType);
					target.add(submitRejectQueryRevokeBtn);
				}
			};
			cancelQuery.setOutputMarkupId(true);
			cancelQuery.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(cancelQuery);
			
			
			SSMAjaxLink approve = new SSMAjaxLink("approve") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_APPROVE);
					robUserOku.setApproveBy(UserEnvironmentHelper.getLoginName()); //officer id
					robUserOku.setRemarks(resolve("page.lbl.user.profile.oku.applicationSuccess"));
					robUserOkuService.updateOkuApproval(robUserOku);
					setResponsePage(ListRobUserOkuWorkingTrayPage.class);
				}
			};
			approve.setOutputMarkupId(true);
			approve.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(approve);
			
			
			SSMAjaxLink revoke = new SSMAjaxLink("revoke") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_REVOKE);
					remarksRejectQueryRevoke.setVisible(true);
					remarksType.setVisible(true);
					submitRejectQueryRevokeBtn.setVisible(true);
					remarksType.setDefaultModelObject(resolve("page.lbl.user.profile.oku.remarksType.revokeReason"));
					target.add(remarksRejectQueryRevoke);
					target.add(remarksType);
					target.add(submitRejectQueryRevokeBtn);
				}
			};
			revoke.setOutputMarkupId(true);
			revoke.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(revoke);
			
			
			SSMAjaxLink cancelRevoke = new SSMAjaxLink("cancelRevoke") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					robUserOku.setOkuRegStatus(Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE);
					remarksRejectQueryRevoke.setVisible(true);
					remarksType.setVisible(true);
					submitRejectQueryRevokeBtn.setVisible(true);
					remarksType.setDefaultModelObject(resolve("page.lbl.user.profile.oku.remarksType.cancelRevokeReason"));
					target.add(remarksRejectQueryRevoke);
					target.add(remarksType);
					target.add(submitRejectQueryRevokeBtn);
				}
			};
			cancelRevoke.setOutputMarkupId(true);
			cancelRevoke.setOutputMarkupPlaceholderTag(true);
			wmcApproval.add(cancelRevoke);
		
			
			List listRobFormNotes = robFormNotesService.findByFormCode(robUserOku.getOkuRefNo());
			ListView<RobFormNotes> listQueryView = new ListView("listQueryView", listRobFormNotes) {
				@Override
				protected void populateItem(ListItem item) {
					RobFormNotes robFormNotes = (RobFormNotes) item.getModelObject();
					item.add(new Label("queryBy", robFormNotes.getQueryBy()));
					item.add(new MultiLineLabel("notes", robFormNotes.getNotes()));
					item.add(new MultiLineLabel("notesAnswer", robFormNotes.getNotesAnswer()));
					Date updateDt = null;
					if(robFormNotes.getUpdateDt()!=null && !robFormNotes.getUpdateDt().equals(robFormNotes.getCreateDt())){
						updateDt = robFormNotes.getUpdateDt();
					}
					item.add(new SSMLabel("createDt", robFormNotes.getCreateDt(),"dd/MM/yyyy hh:mm:ss a"));
					item.add(new SSMLabel("updateDt", updateDt,"dd/MM/yyyy hh:mm:ss a"));
				}
			    
			};
			add(listQueryView);
			listQueryView.setVisible(false);
			
			if(UserEnvironmentHelper.isInternalUser()){
				listQueryView.setVisible(true);
			}
			
			
			//hide n show button by status (onload page)--------------------------------------------------
			//default onload (status Pending default load)
			revoke.setVisible(false); //revoke = B
			cancelRevoke.setVisible(false); //cancel revoke = CB
			cancelQuery.setVisible(false); //cancel query = status Cancel (C)
			
			if ((Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus()))||
				(Parameter.OKU_REGISTRATION_STATUS_REJECT.equals(robUserOku.getOkuRegStatus())) ||
				(Parameter.OKU_REGISTRATION_STATUS_CANCEL.equals(robUserOku.getOkuRegStatus())) ||
				(Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus()))  ||
				(Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) ||
				(Parameter.OKU_REGISTRATION_STATUS_CANCELREVOKE.equals(robUserOku.getOkuRegStatus()))||
				(Parameter.OKU_REGISTRATION_STATUS_WITHDRAW.equals(robUserOku.getOkuRegStatus()))) {
					approve.setVisible(false);
					reject.setVisible(false);
					query.setVisible(false);
				}
			
			if (Parameter.OKU_REGISTRATION_STATUS_REVOKE.equals(robUserOku.getOkuRegStatus())) {
				editBtn.setVisible(false);
				cancelRevoke.setVisible(true);
			}
			
			if (Parameter.OKU_REGISTRATION_STATUS_QUERY.equals(robUserOku.getOkuRegStatus())){
				cancelQuery.setVisible(true);
				//edit.setVisible(false); // resubmit query btn
				//reject.setVisible(true); //??
			}
	
			if(Parameter.OKU_REGISTRATION_STATUS_APPROVE.equals(robUserOku.getOkuRegStatus())) { //approved can resubmit new application
				revoke.setVisible(true);
			}
			
			//-------------------------------------------------------------------------------------------
		}
	}
		
	public String getPageTitle() {
		String titleKey = "page.lbl.user.profile.oku.titleView";
		return titleKey;
	}
}

