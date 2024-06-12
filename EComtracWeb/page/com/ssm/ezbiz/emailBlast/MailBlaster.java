package com.ssm.ezbiz.emailBlast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.naming.factory.SendMailFactory;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.quartz.jobs.ee.mail.SendMailJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpSupplyInfoDtlDao;
import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMCheckBox;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.AfterLoginLlp;
import com.ssm.llp.mod1.page.SuccessLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.llp.wicket.SSMAjaxFormSubmitBehavior;

@SuppressWarnings({"all"})
public class MailBlaster extends SecBasePage {

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "MailService")
	MailService mailService;
	
	public MailBlaster() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				EmailBlastModel emailBlastModel = new EmailBlastModel();
				emailBlastModel.setBlastAll(false);
				return emailBlastModel;
			}
		}));
		add(new MailBlastForm("form", null, getDefaultModel()));
		insertTinyMce();
	}
	
	public MailBlaster(String message) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				EmailBlastModel emailBlast = new EmailBlastModel();
				emailBlast.setBlastAll(false);
				return emailBlast;
			}
		}));
		
		add(new MailBlastForm("form", message, getDefaultModel()));
		insertTinyMce();
	}
	
	public void insertTinyMce(){
		String contextPath = WicketApplication.get().getServletContext().getServletContextName();
		String js = "<script src=\"" + contextPath  + "/tinymce/tinymce.min.js\"></script>";
		
		Label jsScript = new Label("jsScript", js);
		jsScript.setEscapeModelStrings(false);
		jsScript.setOutputMarkupId(true);
		
		add(jsScript);
	}

	private class MailBlastForm extends Form implements Serializable {
		public MailBlastForm(String id, String msg, IModel m) {

			super(id, m);
			final EmailBlastModel emailBlast = (EmailBlastModel) m.getObject();
			emailBlast.setMessage(msg);
			
//			final CheckBox blastAll = new CheckBox("blastAll");
//			blastAll.setOutputMarkupId(true);
//			add(blastAll);
			
			SSMAjaxFormSubmitBehavior nameSearchOnChange = new SSMAjaxFormSubmitBehavior("onkeyup", true) {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					EmailBlastModel emailBlastModel = (EmailBlastModel) getForm().getDefaultModelObject();
					
					
				}
			};
			
//			AjaxEventBehavior nameSearchOnChange = new AjaxEventBehavior("onchange") {
//		           protected void onEvent(AjaxRequestTarget target) {
//		        	   EmailBlastModel emailBlastModel = (EmailBlastModel) getForm().getDefaultModelObject();
//		           }
//			};
			
//			List<LlpUserProfile> llpAllUsersProfile = llpUserProfileService.findProfileInfo();
			
//			final ListMultipleChoice userDropdownList = new ListMultipleChoice("userDropdownList", llpAllUsersProfile, getChoiceRenderer());
//			add(userDropdownList);
			
			final SSMTextField listUserEmail = new SSMTextField("listUserEmail");
			listUserEmail.setUpperCase(false);
			add(listUserEmail);
			
			
//			userDropdownList.add(new AjaxFormSubmitBehavior(this, "change") {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target) {
//					EmailBlastModel blasterModel = (EmailBlastModel) getForm().getDefaultModelObject();
//					ArrayList userDropdownList = (ArrayList) getComponent().getDefaultModelObject();
//					
//					if(userDropdownList.size() == 0) {
//						blasterModel.setBlastAll(true);
//					} else {
//						blasterModel.setBlastAll(false);
//					}
//					
//					target.add(blastAll);
//				}
//			});
			
			final SSMTextField composeMailTitle = new SSMTextField("composeMailTitle");
			add(composeMailTitle);

			final SSMTextArea<String> composeMailBody = new SSMTextArea("composeMailBody");
			add(composeMailBody);
			
			final SSMLabel message = new SSMLabel("message");
			message.setOutputMarkupId(true);
			message.setOutputMarkupPlaceholderTag(true);
			if(msg == null) {
				message.setVisible(false);
			}
			add(message);
			
			final SSMAjaxButton blastMail = new SSMAjaxButton("blastMail") {
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form) {
					message.setVisible(true);
					
					EmailBlastModel emailBlast = (EmailBlastModel) form.getDefaultModelObject();
					
					String composeMailTitle = emailBlast.getComposeMailTitle();
					String composeMailBody = emailBlast.getComposeMailBody();
					
					if (composeMailTitle == null) {
						emailBlast.setMessage("Subject required.");
						target.add(message);
					} else if (composeMailBody == null) {
						emailBlast.setMessage("Message required.");
						target.add(message);
					} else {
//						if(emailBlast.getBlastAll()){
//							List<LlpUserProfile> llpAllUsersProfile = llpUserProfileService.findProfileInfo();
//							for(LlpUserProfile user : llpAllUsersProfile) {
//								mailService.sendMail(user.getEmail(), composeMailTitle, user.getUserRefNo(), composeMailBody, user.getName(), user.getUserRefNo(), user.getLoginId());
//							}
//						} else {
						
//						List<LlpUserProfile> userDropdownList = emailBlast.getUserDropdownList();
						String listEmail [] = StringUtils.split(emailBlast.getListUserEmail(),",");
						
						String errorMsj = "";
						for (int i = 0; i < listEmail.length; i++) {
							try {
								mailService.sendImmediately(listEmail[i], composeMailTitle, "", composeMailBody, "");
							} catch (Exception e) {
								errorMsj="<font color='red'>Error Sending :"+listEmail[i]+"\n</font>";
							}
						}
						if(StringUtils.isBlank(errorMsj)) {
							errorMsj = "Email(s) sent.";
						}else {
							errorMsj = "<font color='red'>"+errorMsj+"</font>";
						}
						
						setResponsePage(new MailBlaster(errorMsj));
					}
				}
			};
			add(blastMail);
		}
	}

	public class EmailBlastModel implements Serializable{
		private String composeMailBody;
		private String composeMailTitle;
		private String listUserEmail;
		
		
		
		private Boolean blastAll = true;
		private List<LlpUserProfile> userDropdownList = new ArrayList<LlpUserProfile>();
		
		private String message;
		
		public List<LlpUserProfile> getUserDropdownList() {
			return userDropdownList;
		}
		
		public void setUserDropdownList(LlpUserProfile userDropdown) {
			userDropdownList.add(userDropdown);
		}
		
		public String getComposeMailBody() {
			return composeMailBody;
		}
		public void setComposeMailBody(String composeMailBody) {
			this.composeMailBody = composeMailBody;
		}
		public String getComposeMailTitle() {
			return composeMailTitle;
		}
		public void setComposeMailTitle(String composeMailTitle) {
			this.composeMailTitle = composeMailTitle;
		}
		public Boolean getBlastAll() {
			return blastAll;
		}
		public void setBlastAll(Boolean blastAll) {
			this.blastAll = blastAll;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getListUserEmail() {
			return listUserEmail;
		}

		public void setListUserEmail(String listUserEmail) {
			this.listUserEmail = listUserEmail;
		}
	}
	
	public IChoiceRenderer getChoiceRenderer() {
		IChoiceRenderer renderer = new IChoiceRenderer<Object>() {
			@Override
			public Object getDisplayValue(Object arg0) {
				if(arg0 instanceof LlpUserProfile) {
					LlpUserProfile user = (LlpUserProfile) arg0;
					String display = user.getName() + " (" + user.getEmail() + ")";
					return display;
				}
				return arg0;
			}
			@Override
			public String getIdValue(Object arg0, int arg1) {
				if(arg0 instanceof LlpUserProfile) {
					return ((LlpUserProfile)arg0).getUserRefNo();
				}
				return arg0.toString();
			}
		};
		return renderer;
	}
	
	@Override
	public String getPageTitle() {
		return "ezbiz.mailBlaster.title";
	}
}
