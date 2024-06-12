package com.ssm.llp.mod1.page;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SearchLlpName extends SecBasePanel {

	public SearchLlpName(String id, String searchQuery, String regType, String profBodyType, String conversionType, String isProceedToLLP) {
		super(id);
		if(StringUtils.isNotBlank(searchQuery)){
			searchQuery=searchQuery.toUpperCase();
		}
		add(new SearchLlpNameForm( "searchLlpNameForm", searchQuery, regType , profBodyType, conversionType, isProceedToLLP));
	}
	

	public SearchLlpName(String id) {
		super(id);
		add(new SearchLlpNameForm( "searchLlpNameForm",null , null, null, null, null));
	}

	private class SearchLlpNameForm extends Form {

		private String searchString;
		private String regType; 
		private String profBodyType;
		private String isProceedToLLP;
		private String conversionType;
		
		
		public SearchLlpNameForm(String id, String searchString, String regType, String probBodyType, String conversionType, String isProceedToLLP) {
			super(id);
			this.regType = regType;
			this.profBodyType = probBodyType;
			this.searchString = searchString;
			this.isProceedToLLP = isProceedToLLP;
			this.conversionType = conversionType;

			SSMTextField tf = new SSMTextField("searchString",  new PropertyModel(this, "searchString"));
			add(tf);
			tf.add(StringValidator.maximumLength(100));
			tf.setRequired(true);
			
			
			HiddenField regTypeField = new HiddenField("regType",  new PropertyModel(this, "regType"));
			add(regTypeField);
			
			HiddenField profBodyTypeField = new HiddenField("profBodyType",  new PropertyModel(this, "profBodyType"));
			add(profBodyTypeField);
			
			HiddenField conversionTypeField = new HiddenField("conversionType",  new PropertyModel(this, "conversionType"));
			add(conversionTypeField);
			
			HiddenField isProceedToLLPField = new HiddenField("isProceedToLLP",  new PropertyModel(this, "isProceedToLLP"));
			add(isProceedToLLPField);
			
			setMarkupId("search-form");
			
			
			SSMLabel regTypeLbl = new SSMLabel("regTypeLbl", regType, Parameter.LLP_REG_TYPE);
			add(regTypeLbl);
			
			SSMLabel profBodyTypeLbl = new SSMLabel("profBodyTypeLbl", profBodyType, Parameter.PROF_BODY_TYPE);
			add(profBodyTypeLbl);
			if(StringUtils.isBlank(probBodyType)){
				profBodyTypeLbl.setVisible(false);
			}
			
			SSMLabel conversionTypeLbl = new SSMLabel("conversionTypeLbl", conversionType, Parameter.CONVERSION_TYPE);
			add(conversionTypeLbl);
			if(StringUtils.isBlank(conversionType)){
				conversionTypeLbl.setVisible(false);
			}
			
			SSMDownloadLink guidelinesLink = new SSMDownloadLink("guidelinesLink", "RESERVE_NAME_GUIDELINE");
			if(!guidelinesLink.isHasFile()){
				guidelinesLink.setVisible(false);
			}
			add(guidelinesLink);
			
		}

		public void onSubmit() {
			PageParameters params = new PageParameters();
			if(StringUtils.isNotBlank(regType)){
				params.add("regType", regType);
			}
			if(StringUtils.isNotBlank(profBodyType)){
				params.add("profBodyType", profBodyType);
			}
			if(StringUtils.isNotBlank(conversionType)){
				params.add("conversionType", conversionType);
			}
			if(StringUtils.isNotBlank(searchString)){
				searchString = searchString.trim();
				params.add("searchString",  searchString);
			}
			if(StringUtils.isNotBlank(isProceedToLLP)){
				params.add("isProceedToLLP", isProceedToLLP);
			}
			String searchStringa =  getFilterWord(searchString);
			setResponsePage(new ListLlpReservedNames(params,searchStringa));
			
			
		}
		
		private String getFilterWord(String searchString){
			String searchString1 = searchString.toUpperCase();
			String searchString2 = searchString1.trim();
			String searchString3 = searchString2.replaceAll("[^-)(.@'&A-Za-z0-9]", " ");
			String searchString3_1 = searchString3.replaceAll(" +", " ");
			String searchString3_2 = searchString3_1.replace(" ", "|");
			String searchString4 = searchString3_2;
			String searchResult = "";
			String searchResult2 = "";

			String removeDefaultString[] = { "LLP", "PLT", "PERKONGSIAN|LIABILITI|TERHAD", "LIMITED|LIABILITY|PARTNERSHIP", "PERKONGSIANLIABILITITERHAD",
					"LIMITEDLIABILITYPARTNERSHIP" };

			for (int i = 0; i < removeDefaultString.length; i++) {

				searchString4 = searchString4.replace(removeDefaultString[i], "");
			}

			searchResult = searchString4.replace("|", " ");
			searchResult2 = searchResult.replaceAll(" +", " ").trim();
			
			return searchResult2;
			
		}
	}

}
