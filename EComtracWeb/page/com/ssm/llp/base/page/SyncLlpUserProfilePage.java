package com.ssm.llp.base.page;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.util.operator.ShowText;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.comtrac.SelectComtracTraining.ComtracFormModel;
import com.ssm.ezbiz.otcPayment.CollectionBalancingPage;
import com.ssm.ezbiz.service.RobSyncProfileAuditService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobSyncProfileAudit;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.ListLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({ "all" })
public class SyncLlpUserProfilePage extends SecBasePage {

	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobSyncProfileAuditService")
	RobSyncProfileAuditService robSyncProfileAuditService;

	public SyncLlpUserProfilePage(LlpUserProfile llpUserProfile, String alert) {
		this(llpUserProfile);
	}

	public SyncLlpUserProfilePage(final LlpUserProfile llpUserProfile) {

		final RobSyncProfileAudit robSyncProfileAudit = new RobSyncProfileAudit();
		robSyncProfileAudit.setIcNo(llpUserProfile.getIdNo());
		robSyncProfileAudit.setUpdatedName(llpUserProfile.getName());
		try {
			String cbsName = llpUserProfileService.findCBSOwnerNameByIcWS(llpUserProfile.getIdNo());
			robSyncProfileAudit.setOriginalName(cbsName);
		} catch (SSMException e) {
			e.printStackTrace();
		}
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return robSyncProfileAudit;
			}
		}));

		add(new SyncLlpUserProfileForm("form", getDefaultModel(), llpUserProfile));

	}

	public class SyncLlpUserProfileForm extends Form implements Serializable {
		public SSMTextArea remarks;
		public SyncLlpUserProfileForm(String id, final IModel m, final LlpUserProfile llpUserProfile) {
			super(id, m);

			RobSyncProfileAudit robSyncProfileAudit = (RobSyncProfileAudit) m.getObject();

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
			if (!StringUtils.isBlank(llpUserProfile.getApproveBy())) {
				approveByString = llpUserProfile.getApproveBy() + "<br>" + sdf.format(llpUserProfile.getApproveDt());
			}
			SSMLabel approveBy = new SSMLabel("approveBy", approveByString);
			approveBy.setEscapeModelStrings(false);
			add(approveBy);

			SSMLabel ezbizName = new SSMLabel("ezbizName", llpUserProfile.getName());
			add(ezbizName);

			remarks = new SSMTextArea("remarks");
			remarks.setLabelKey("page.lbl.ezbiz.syncProfile.remarks");
//			remarks.setOutputMarkupId(true);
			remarks.isRequired();
			add(remarks);
			
			SSMAjaxFormSubmitBehavior remarkOnBlur = new SSMAjaxFormSubmitBehavior("onblur", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					RobSyncProfileAudit robSyncProfileAuditModel = (RobSyncProfileAudit) getForm().getDefaultModelObject();
					System.out.println(robSyncProfileAuditModel.getRemarks());
				}
			};
			remarks.add(remarkOnBlur);
			
			
			SSMLabel cbsName = new SSMLabel("cbsName", "");
			add(cbsName);

			final ModalWindow secondLevelApprovePopup = new ModalWindow("secondLevelApprovePopup");
			add(secondLevelApprovePopup);

			secondLevelApprovePopup.setCookieName("secondLevelApprovePopupCookies" + llpUserProfile.getUserRefNo());
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
					
					
					if (getSession().getAttribute("_approveby") != null) {
						String Pegawai = "SSM:"+(String) getSession().getAttribute("_approveby");
						getSession().removeAttribute("_approveby");

						RobSyncProfileAudit robSyncProfileAudit = (RobSyncProfileAudit) m.getObject();
						robSyncProfileAudit.setApproveBy(Pegawai);
						if(StringUtils.isBlank(robSyncProfileAudit.getRemarks())){
							storeErrorMsgKey("failed.sync.user.profile.remarks.blank");
						}else{
							try {
								llpUserProfileService.updateProfileInfoByWS(robSyncProfileAudit);
								storeSuccessMsgKey("success.sync.user.profile");
							} catch (SSMException e) {
								e.printStackTrace();
								storeErrorMsgKey("failed.sync.user.profile");
							}
						}
						setResponsePage(new ListLlpUserProfilePage());
					}

				}
			});

			secondLevelApprovePopup.setPageCreator(new ModalWindow.PageCreator() {
				@Override
				public Page createPage() {
					return new SecondLevelLoginPage(SyncLlpUserProfilePage.this.getPage(), secondLevelApprovePopup, llpUserProfile.getUserRefNo()
							.toString());
				}
			});

			
			SSMAjaxButton sync = new SSMAjaxButton("sync") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form) {
					secondLevelApprovePopup.show(target);
				}
			};
			sync.setOutputMarkupId(true);
			add(sync);

			if (StringUtils.isNotBlank(robSyncProfileAudit.getOriginalName())) {
				cbsName.add(new AttributeModifier("style", ""));
				cbsName.setDefaultModelObject(robSyncProfileAudit.getOriginalName());
			} else {

				String cbsNameString = resolve(SSMExceptionParam.USER_PROFILE_NOT_FOUND_CBS);
				cbsName.add(new AttributeAppender("style", "color:red; font-weight:bold"));
				cbsName.setDefaultModelObject(cbsNameString);

//				remarks.setEnabled(false);
				sync.setEnabled(false);
			}

			SSMLink back = new SSMLink("back") {
				@Override
				public void onClick() {
					setResponsePage(ListLlpUserProfilePage.class);
				}
			};

			add(back);
		}
		public SSMTextArea getRemarks() {
			return remarks;
		}
		public void setRemarks(SSMTextArea remarks) {
			this.remarks = remarks;
		}
	}

	@Override
	public String getPageTitle() {
		return "page.title.syncNameWithCBS";
	}
}
