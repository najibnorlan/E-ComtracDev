package com.ssm.ezbiz.robFormC;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.base.common.code.CommonConstant;
import com.ssm.ezbiz.service.RobFormCService;
import com.ssm.ezbiz.service.RobRenewalService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.webis.param.BusinessInfo;

public class SearchBusinessForFormCPage extends SecBasePage {

	@SpringBean(name = "RobFormCService")
	private RobFormCService robFormCService;
	
	
	@SpringBean(name = "RobRenewalService")
	private RobRenewalService robRenewalService;
	
	
	public SearchBusinessForFormCPage() {
		add(new SearchForm("searchPanel"));
	}
	
	private class SearchForm extends Form {

		private String brNo;
		private String chkDigit;

		public SearchForm(String id) {
			super(id);

			SSMTextField brNo = new SSMTextField("brNo", new PropertyModel(this, "brNo"));
			add(brNo);
			
			SSMTextField chkDigit = new SSMTextField("chkDigit", new PropertyModel(this, "chkDigit"));
			add(chkDigit);
			
			// tf.setRequired(true);
			setMarkupId("search-form");
		}

		public void onSubmit() {
			try {
				BusinessInfo businessInfo = robRenewalService.findBusinessByRegNoWS(getBrNo(),getChkDigit());
				String errorKey = "page.title.mybiz.error.invalidBiz";
				if(businessInfo==null){
					error(getLocaleMsg(errorKey));
				} else if(CommonConstant.NO.equals(businessInfo.getCanRenew())){
					error(getLocaleMsg(errorKey));
				}else{
					setResponsePage(new EditRobFormCPage(businessInfo));
				}
			} catch (SSMException e) {
				error(e);
			}
			
		}

		public String getBrNo() {
			return brNo;
		}

		public void setBrNo(String brNo) {
			this.brNo = brNo;
		}

		public String getChkDigit() {
			return chkDigit;
		}

		public void setChkDigit(String chkDigit) {
			this.chkDigit = chkDigit;
		}

	}

	@Override
	public String getPageTitle() {
		return "page.title.mybiz.3rdPartyRobFormC";
	}
}
