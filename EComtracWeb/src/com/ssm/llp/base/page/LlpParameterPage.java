package com.ssm.llp.base.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;


@SuppressWarnings({ "rawtypes", "serial", "unchecked" }) 
public class LlpParameterPage extends SecBasePage {
	@SpringBean(name="LlpParametersService")
	private LlpParametersService llpParametersService;
	
	public LlpParameterPage() {
		add(new ParameterForm("ParameterForm"));
	}
	
	private class ParameterForm extends Form {

		private String codeType;
		private SSMDataView dataView;
		private SSMSortableDataProvider dp;
		private WebMarkupContainer wmc;
		private Button addNewButton;


		public ParameterForm(String name) {
			super(name);
			
			
			List<LlpParameters> llpParameterAllCodeType = llpParametersService.findAllCodeTypeAsParameters();
			final SSMDropDownChoice allCodeTypeChoice = new SSMDropDownChoice("codeType", new PropertyModel(this, "codeType"), llpParameterAllCodeType);
			allCodeTypeChoice.setLabelKey("llpParameters.page.codeType");
			allCodeTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				  @Override
				  protected void onUpdate(AjaxRequestTarget target) {
				    System.out.println(allCodeTypeChoice.getInput());
				    rePopulateTable(target, allCodeTypeChoice.getInput());
				  }
				});
			allCodeTypeChoice.setRequired(true);
			add(allCodeTypeChoice);
			
			addNewButton = new Button("addNew") {
				public void onSubmit() {
					String codeType = allCodeTypeChoice.getInput();
					setResponsePage(new EditLlpParameterPage(codeType)); 
				}
			};
			add(addNewButton);
			
			populateTable();
		}

		private void rePopulateTable(AjaxRequestTarget target,String codeType){
			
			SearchCriteria sc = new SearchCriteria("codeType", SearchCriteria.EQUAL, codeType);
//			sc = SearchCriteria.andIfNotNull(sc, "msg", SearchCriteria.LIKE, "%"+getMsgSearch()+"%");
			
			dp.setSc(sc);
			target.add(wmc);
		}
//		
		private void populateTable() {
			SearchCriteria sc = null;
			
			dp = new SSMSortableDataProvider("status, seq", sc, LlpParametersService.class);
			dataView = new SSMDataView<LlpParameters>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpParameters> item) {
					LlpParameters llpParameters = item.getModelObject();
					
					item.add(new SSMLabel("code", llpParameters.getCode()));
					item.add(new SSMLabel("codeDesc", llpParameters.getCodeDesc()));
					item.add(new SSMLabel("updateDt", llpParameters.getUpdateDt()));
					item.add(new SSMLabel("status", llpParameters.getStatus()));
					if(llpParameters.getSeq()==null){
						item.add(new SSMLabel("seq", ""));
					}else{
						item.add(new SSMLabel("seq", llpParameters.getSeq()));
					}
					item.add(new SSMLink("edit", item.getDefaultModel()) {
						public void onClick() {
							LlpParameters llpParameters = (LlpParameters) getModelObject();
							setResponsePage(new EditLlpParameterPage(llpParameters.getIdParameter())); 
						}
					});
					
					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}
			};
			dataView.setItemsPerPage(50L);

			dataView.setOutputMarkupId(true);

			SSMPagingNavigator navigator = new SSMPagingNavigator("navigator", dataView);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);
//			wmc.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

			add(wmc);
		}


		public String getCodeType() {
			return codeType;
		}


		public void setCodeType(String codeType) {
			this.codeType = codeType;
		}

	
	}

	
	@Override
	public String getPageTitle() {
		return "page.title.parameter";
	}
}
