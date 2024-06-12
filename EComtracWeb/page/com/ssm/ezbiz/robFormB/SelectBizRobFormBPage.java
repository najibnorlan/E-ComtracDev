package com.ssm.ezbiz.robFormB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.errorlog.MyInternalErrorPage;
import com.ssm.ezbiz.service.RobEBranchService;
import com.ssm.ezbiz.service.RobEBranchTransactionService;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.param.BizOwnerInfo;
import com.ssm.webis.param.BusinessInfo;

public class SelectBizRobFormBPage extends SecBasePage{
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;

	@SpringBean(name = "RobEBranchTransactionService")
	private RobEBranchTransactionService robEBranchTransactionService;
	
	@SpringBean(name = "RobEBranchService")
	private RobEBranchService robEBranchService;

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;
	
	@Override
	public String getPageTitle() {
		return "page.lbl.ezbiz.robFormB.selectBiz";
	}
	
	public SelectBizRobFormBPage() {
		add(new SelectBizRobFormBForm("selectBizRobFormBForm"));
	}
	
	
	private class SelectBizRobFormBForm extends Form {
		private String listBiz;
		private WebMarkupContainer wmc;
		private final SSMLabel selectBizLabel = new SSMLabel("selectBizLabel","");
		private Button addNewButton;
		private Map<String, BusinessInfo> mapRobBusiness = new HashMap();;
		private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		
		public SelectBizRobFormBForm(String name) {
			super(name);
			String icNo = "";
			LlpUserProfile profile = llpUserProfileService.findProfileInfoByUserId(UserEnvironmentHelper.getLoginName());
			if (profile != null) {
				icNo = profile.getIdNo();
			}
			
			final String icNoLodger=icNo;
			try {
				List<BusinessInfo> businessInfoListTmp = null;
				try {
					businessInfoListTmp = robRenewalService.findListRobRenewalByIcWS(icNo);
				} catch (SSMException e) {
					e.printStackTrace();
					ssmError(e);
					storeErrorMsg(e.getMessage());
					throw new RestartResponseAtInterceptPageException(MyInternalErrorPage.class);
				} catch (Exception e) {
					e.printStackTrace();
					ssmError(new SSMException("System Error Please try again later"));
					storeErrorMsg("System Error Please try again later");
					throw new RestartResponseAtInterceptPageException(new MyInternalErrorPage(e));
				}
				
				
				List<LlpParameters> listBizParam = new ArrayList<LlpParameters>();
				for (int i = 0; i < businessInfoListTmp.size(); i++) {
					BusinessInfo bisInfo = businessInfoListTmp.get(i);
					
					
					String brNo = bisInfo.getBrNo();
					listBizParam.add(new LlpParameters(brNo, brNo+ "-"+bisInfo.getChkDigit()+" "+bisInfo.getBizName()));
					mapRobBusiness.put(brNo, bisInfo);
				}
				
				selectBizLabel.setOutputMarkupId(true);
				add(selectBizLabel);
				
				
				final SSMAjaxButton proceedBtn = new SSMAjaxButton("proceed") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						BusinessInfo businessInfo = mapRobBusiness.get(getListBiz());
						setResponsePage(new EditRobFormBPage(businessInfo));
					}
					
					@Override
		            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		                super.updateAjaxAttributes(attributes);
		                attributes.getAjaxCallListeners().add(new AjaxCallListener()
		                    .onBefore("$('#" + getMarkupId() + "').prop('disabled',true);"));
		            }
				};
				proceedBtn.setOutputMarkupId(true);
				proceedBtn.setOutputMarkupPlaceholderTag(true);
				proceedBtn.setVisible(false);
				add(proceedBtn);
				
				final SSMDropDownChoice listBizDropDown = new SSMDropDownChoice("listBiz", new PropertyModel(this, "listBiz"), listBizParam);
				listBizDropDown.setLabelKey("selectBizRobFormBForm.page.listBiz");
				listBizDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						String brNo = listBizDropDown.getInput();
						
						BusinessInfo businessInfo = mapRobBusiness.get(brNo);
						
						boolean isBizExpired = false;
						try {
							Date bizEndDate = sdf.parse(businessInfo.getEndDate());
							
							if(bizEndDate.before(new Date())){//biz already expired
								isBizExpired = true;
							}
						} catch (Exception e) {
						}
						
						
						proceedBtn.setVisible(false);
						if(businessInfo==null){
							proceedBtn.setVisible(false);
						}else{
							boolean canProceed = true;
							if(Parameter.YES_NO_no.equals(businessInfo.getCanRenew())){
								selectBizLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormB.canRenewIntructions"));
								selectBizLabel.add(new AttributeAppender("style", "color:red; font-weight:bold"));
								canProceed = false;
							}else{
								if(isBizExpired){
									selectBizLabel.setDefaultModelObject(resolve("page.lbl.ezbiz.robFormB.canRenewBizExpiredRenewFirst"));
									selectBizLabel.add(new AttributeAppender("style", "color:red; font-weight:bold"));
									canProceed = false;
								}
							}
							List<RobFormB> list = robFormBService.findPendingApplication(brNo);
							if(list.size()>0){
								String pending = resolve("page.lbl.ezbiz.robFormB.pendingFormB");
								for (int i = 0; i < list.size(); i++) {
									pending += "<br>"+resolve("page.lbl.ezbiz.robFormB.pendingFormBItem",list.get(i).getRobFormBCode(),list.get(i).getStatus());
								}
								selectBizLabel.setDefaultModelObject(pending);
								selectBizLabel.add(new AttributeAppender("style", "color:red; font-weight:bold"));
								canProceed = false;
							}
							
							//check all owner must be a registered user
							BizOwnerInfo[] listOwner = businessInfo.getListOwner();
							if (listOwner.length>1) { //jika perkongsian
								String partnerLabel = resolve("page.lbl.ezbiz.robFormB.partnerRegister");
								for (int i = 0; i < listOwner.length; i++) {
									if(!(listOwner[i].getIcNo().equals(icNoLodger))) { //check partner only
									LlpUserProfile profile = llpUserProfileService.findLatestActiveUserByIdNo(listOwner[i].getIcNo());
									if (profile != null) {
										if (Parameter.USER_STATUS_pending.equals(profile.getUserStatus())){ //if pending verification
											partnerLabel += resolve("page.lbl.ezbiz.robFormB.partnerPendingRegister",profile.getName(),profile.getIdNo()); 
											selectBizLabel.setDefaultModelObject(partnerLabel);
											selectBizLabel.add(new AttributeAppender("style", "color:red; font-weight:bold"));
											canProceed = false;	
										}
									} else { //partner null
										partnerLabel += resolve("page.lbl.ezbiz.robFormB.partnerNotRegister",listOwner[i].getVchname(),listOwner[i].getIcNo());
										selectBizLabel.setDefaultModelObject(partnerLabel);
										selectBizLabel.add(new AttributeAppender("style", "color:red; font-weight:bold"));
										canProceed = false;	
									}
								  }
								}
							}
										
							if(canProceed){
								selectBizLabel.setDefaultModelObject("");
								proceedBtn.setVisible(true);
							}
							
						}
						target.add(proceedBtn);
						target.add(selectBizLabel);
					}
				});
				add(listBizDropDown);
				
				

				
			} catch (Exception e) {
				error(e.getLocalizedMessage());
			}
			setOutputMarkupId(true);
		}

		public String getListBiz() {
			return listBiz;
		}

		public void setListBiz(String listBiz) {
			this.listBiz = listBiz;
		}
	}

}