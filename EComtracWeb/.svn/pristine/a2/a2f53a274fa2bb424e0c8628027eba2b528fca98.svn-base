package com.ssm.llp.mod1.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMTextField;

public class SearchLlpReservedNameOfficer extends SecBasePanel {

	public SearchLlpReservedNameOfficer(String id, PageParameters params) {
		super(id);
		
		final String applyLlpName = params.get("applyLlpName") != null ? params.get("applyLlpName").toString() : null;
		final String refNo = params.get("refNo") != null ? params.get("refNo").toString() : null;
		add(new SearchForm("searchForm", applyLlpName ,refNo));
	}

	private class SearchForm extends Form {

		private String applyLlpName;
		private String refNo;

		public SearchForm(String id, String applyLlpName, String refNo) {
			super(id);
			this.applyLlpName = applyLlpName;
			this.refNo = refNo;
			
			SSMTextField tf = new SSMTextField("applyLlpName", new PropertyModel(this, "applyLlpName"));
			add(tf);
			
			
			SSMTextField llpNo = new SSMTextField("refNo", new PropertyModel(this, "refNo"));
			add(llpNo);
			// tf.setRequired(true);
			setMarkupId("search-form");
		}

		public void onSubmit() {
			PageParameters params = new PageParameters();
			if (StringUtils.isNotBlank(getApplyLlpName())) {
				params.add("applyLlpName", getApplyLlpName());
			}
			if (StringUtils.isNotBlank(getRefNo())) {
				params.add("refNo", getRefNo());
			}
			
			setResponsePage(ListLlpReservedNamesOfficer.class, params);
		}


		

		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		public String getApplyLlpName() {
			return applyLlpName;
		}

		public void setApplyLlpName(String applyLlpName) {
			this.applyLlpName = applyLlpName;
		}
	}

}
