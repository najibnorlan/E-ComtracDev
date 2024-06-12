package com.ssm.llp.base.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMNumberTextField;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.page.NumberValidator;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class EditLlpParameterPage extends SecBasePage {
	
	//New
	public EditLlpParameterPage(final String codeType) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				LlpParameters llpParameters = new LlpParameters();
				llpParameters.setCodeType(codeType);
				return llpParameters;
			}
		}));
		init();
	}
	
	//Update
	public EditLlpParameterPage(final int idParameter) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return getService(LlpParametersService.class.getSimpleName()).findById(idParameter);
			}
		}));
		init();
	}
	
	private void init() {
		add(new LlpParametersForm("LlpParametersForm", getDefaultModel()));
	}
	
	private class LlpParametersForm extends Form {
		
		public LlpParametersForm(String id, IModel m) {
			super(id, m);
			
			HiddenField idParameterTf = new HiddenField("idParameter");
			add(idParameterTf);
			
			SSMTextField codeTypeTf = new SSMTextField("codeType");
			codeTypeTf.setRequired(true);
			codeTypeTf.setReadOnly(true);
			codeTypeTf.setLabelKey("llpParameters.page.codeType");
			add(codeTypeTf);
			
			SSMTextField codeTf = new SSMTextField("code");
			codeTf.setRequired(true);
			codeTf.setUpperCase(false);
			codeTf.setLabelKey("llpParameters.page.code");
			add(codeTf);

			SSMTextField codeDescTf = new SSMTextField("codeDesc");
			codeDescTf.setRequired(true);
			codeDescTf.setUpperCase(false);
			codeDescTf.setLabelKey("llpParameters.page.codeDesc");
			add(codeDescTf);
			
			SSMNumberTextField seqTf = new SSMNumberTextField("seq");
			seqTf.setRequired(true);
			seqTf.setType(Integer.class);
			seqTf.setLabelKey("llpParameters.page.seq");
			add(seqTf);

			SSMDropDownChoice statusDd = new SSMDropDownChoice("status", Parameter.PARAMETER_STATUS);
			statusDd.setLabelKey("llpParameters.page.status");
			statusDd.setRequired(true);
			add(statusDd);

			
			
			add(new Button("save") {
				public void onSubmit() {
					LlpParameters llpParameters = (LlpParameters) getForm().getModelObject();

					if (llpParameters.getIdParameter()==0) {
						getService(LlpParametersService.class.getSimpleName()).insert(llpParameters);
					} else {
						getService(LlpParametersService.class.getSimpleName()).update(llpParameters);
					}
					
					setResponsePage(LlpParameterPage.class);
				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(LlpParameterPage.class);
				}
			}.setDefaultFormProcessing(false));
			
		}
	}

	
	@Override
	public String getPageTitle() {
		return "page.title.edit.parameter";
	}
	
}
