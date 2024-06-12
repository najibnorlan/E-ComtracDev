package com.ssm.ezbiz.usageReport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.UsageReportService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;

@SuppressWarnings({"all"})
public class MonthlyReport extends SecBasePage{

	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;
	
	@SpringBean(name = "RobFormAService")
	RobFormAService robFormAService;
	
	@SpringBean(name = "UsageReportService")
	UsageReportService usageReportService;
	
	
	public MonthlyReport(){
		final Integer curYear = Calendar.getInstance().get(Calendar.YEAR);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				MonthlyReportModel reportModel = new MonthlyReportModel();
				reportModel.setYear(curYear.toString());
				reportModel.setStatus(Parameter.ROB_FORM_A_STATUS_APPROVED);
				return reportModel;
			}
		}));
		add(new MonthlyReportForm("form", getDefaultModel()));
	}
	
	public class MonthlyReportForm extends Form implements Serializable{
		public MonthlyReportForm (String id, IModel m){
			super(id, m);
			String js = "";
			MonthlyReportModel reportModel = (MonthlyReportModel) m.getObject();
			final List<LlpParameters> month = llpParametersService.findByActiveCodeType(Parameter.MONTH_LIST);
			
			SSMDropDownChoice tfStatus = new SSMDropDownChoice("status", Parameter.ROB_FORM_A_STATUS);
			tfStatus.getChoices().add(0, "All");
			tfStatus.setLabelKey("page.lbl.ezbiz.usageReport.status");
			add(tfStatus);
			
			DropDownChoice year = new DropDownChoice("year", getDropdownYear());
			year.setLabelKey("page.lbl.ezbiz.usageReport.year");
			add(year);
			
			SSMAjaxButton search = new SSMAjaxButton("search"){
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					MonthlyReportModel reportModel = (MonthlyReportModel) form.getDefaultModelObject();
					
					generateJS(reportModel, month, target);
					
				}
			};
			add(search);
			
//			generateJS(reportModel, month, null);
			
		}
	}
	
	public void generateJS(MonthlyReportModel reportModel, List<LlpParameters> month, AjaxRequestTarget target){
		WebMarkupContainer wmc = new WebMarkupContainer("divStacked");
		wmc.setOutputMarkupId(true);
		
		// generate js by business name type
		List<LlpParameters> bizType = llpParametersService.findByActiveCodeType(Parameter.ROB_BIZ_TYPE);
		String js = "";
		js = "$('.menu .item').tab();";
		js += "var nameTypeData = {categories : ['" + bizType.get(0).getCodeDesc() + "', '" + bizType.get(1).getCodeDesc() + "'], dataset : {";
		
		Integer indJ = 1;
		for(LlpParameters j : bizType){
			js += "'" + j.getCodeDesc() + "' : [";
			Integer indI = 1;
			for(LlpParameters i : month){
				js += "{ name : '" + i.getCodeDesc() + "', value : " + usageReportService.countRobByCriteria(i.getCode(), j.getCode(), null, reportModel.getStatus(), reportModel.getYear()) + " }";
				if(indI != month.size()){
					js += ",";
				}
				indI++;
			}
			js += "]";
			
			if(indJ != bizType.size()){
				js += ",";
			}
			indJ++;
		}
		js += "}};";
		js += "var chart = uv.chart ('StackedBar', nameTypeData, { meta : { caption : 'Transaction for year " + reportModel.getYear() + "', subcaption: 'By Name Type', hlabel : 'Month', vlabel : 'Number of transactions', isDownloadable : true, downloadLabel : 'Muat turun' }, graph : { orientation : 'Vertical' }, dimension : { width: 700 } });";
		
		// generate js by form type
		List<LlpParameters> formType = llpParametersService.findByActiveCodeType(Parameter.ROB_FORM_TYPE);
		js += "var byFormData = {categories : ['" + formType.get(0).getCodeDesc() + "', '" + formType.get(1).getCodeDesc() + "', '" + formType.get(2).getCodeDesc() + "', '" + formType.get(3).getCodeDesc() + "'], dataset : {";
		Integer indbyFormJ = 1;
		for(LlpParameters j : formType){
			js += "'" + j.getCodeDesc() + "' : [";
			Integer indbyFormI = 1;
			for(LlpParameters i : month){
				
				js += "{ name : '" + i.getCodeDesc() + "', value : " + usageReportService.countRobByCriteria(i.getCode(), null, j.getCode(), reportModel.getStatus(), reportModel.getYear()) + " }";
				if(indbyFormI != month.size()){
					js += ",";
				}
				indbyFormI++;
			}
			js += "]";
			
			if(indbyFormJ != formType.size()){
				js += ",";
			}
			indbyFormJ++;
		}
		js += "}};";
		js += "var chart2 = uv.chart ('StackedBar', byFormData, { meta : { position : '#uv-byForm', caption : 'Transaction for year " + reportModel.getYear() + "', subcaption: 'By Form Type', hlabel : 'Month', vlabel : 'Number of transactions', isDownloadable : true, downloadLabel : 'Muat turun' }, graph : { orientation : 'Vertical' }, dimension : { width: 700 } });";
		
		Label jsScript = new Label("jsScript", js);
		jsScript.setEscapeModelStrings(false);
		jsScript.setOutputMarkupId(true);
		if(target==null){
			add(jsScript);
			add(wmc);
		}else{
			replace(jsScript);
			replace(wmc);
			target.add(wmc);
			target.add(jsScript);
			
		}
		
	}
	
	
	public class MonthlyReportModel implements Serializable{
		private String status;
		private String year;
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
	}
	
	@Override
	public String getPageTitle() {
		return "menu.myBiz.monthlyReport";
	}
}
