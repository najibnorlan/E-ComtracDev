package com.ssm.llp.mod1.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpSpecialKeyword;
import com.ssm.llp.base.common.service.LlpSpecialKeywordService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "unchecked", "serial", "rawtypes", "unused" })
public class EditSpecialKeywordPage extends SecBasePage{


	boolean isNewKeyword = false;
	
	public EditSpecialKeywordPage(PageParameters params){
		
		final long id_keyword = params.get("id_keyword") != null ? params.get("id_keyword").toLong() : null;
		final String searchString = params.get("searchString") != null ? params.get("searchString").toString() : null;
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return getService(LlpSpecialKeywordService.class.getSimpleName()).findById(id_keyword);

				
			}
		}));
		
		add(new EditSpecialKeywordForm("editSpecialKeywordForm", getDefaultModel(),searchString));
	
	}
	
	public EditSpecialKeywordPage(){
		isNewKeyword = true;
		final LlpSpecialKeyword llpSpecialKeyword = new LlpSpecialKeyword();
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return llpSpecialKeyword;
			}
		}));
		add(new EditSpecialKeywordForm("editSpecialKeywordForm", getDefaultModel(), ""));
	
	}
	
	private class EditSpecialKeywordForm extends Form{
		private String vchkeywords;
		private String chrkeywordtype;
		private String searchString;
		
		public EditSpecialKeywordForm(String id, IModel m, String searchString){
			super(id, m);
			this.vchkeywords = vchkeywords;
			this.chrkeywordtype = chrkeywordtype;
			this.searchString =searchString;
			
			SSMTextField kw = new SSMTextField("vchkeywords");
			kw.setRequired(true);
			kw.add(StringValidator.maximumLength(100));
			add(kw);
			
			SSMDropDownChoice kt = new SSMDropDownChoice("chrkeywordtype", Parameter.KEYWORD);
			add(kt);	
			
			add(new Button("save") {
				@Override
				public void onSubmit() {
					LlpSpecialKeyword llpSK = (LlpSpecialKeyword) getForm().getModelObject();
					PageParameters params = new PageParameters();
                    
					
					
					if(isNewKeyword){
				        params.add("searchString", llpSK.getVchkeywords());
                	getService(LlpSpecialKeywordService.class.getSimpleName()).insert(llpSK);
                	
					}else{
						params.add("searchString", getSearchString());
						getService(LlpSpecialKeywordService.class.getSimpleName()).update(llpSK);
				}
					setResponsePage(new ListLlpSpecialKeyword(params));
				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(ListLlpSpecialKeyword.class);
				}
			}.setDefaultFormProcessing(false));
			
		}

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}

		public String getVchkeywords() {
			return vchkeywords;
		}

		public void setVchkeywords(String vchkeywords) {
			this.vchkeywords = vchkeywords;
		}

		public String getChrkeywordType() {
			return chrkeywordtype;
		}

		public void setChrkeywordType(String chrkeywordtype) {
			this.chrkeywordtype = chrkeywordtype;
		}
		
		
	}
}
