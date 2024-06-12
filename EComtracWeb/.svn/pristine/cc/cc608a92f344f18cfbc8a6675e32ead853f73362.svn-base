package com.ssm.ezbiz.usageReport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.RobNameTypeStatReportService;
import com.ssm.ezbiz.usageReport.RobFormAStatistic.RobFormAStatisticForm;
import com.ssm.ezbiz.usageReport.RobFormAStatistic.StatisticModel;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;

@SuppressWarnings({"all"})
public class NameTypeReport extends SecBasePage{

	@SpringBean(name = "LlpParametersService")
	LlpParametersService llpParametersService;
	
	@SpringBean(name = "RobNameTypeStatReportService")
	RobNameTypeStatReportService robNameTypeStatReportService;
	
	public NameTypeReport(){
		final Integer curYear = Calendar.getInstance().get(Calendar.YEAR);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				StatisticModel statisticModel = new StatisticModel();
				statisticModel.setYear(curYear.toString());
				statisticModel.setStatus("All");
				statisticModel.setLastUpdate(new Date());
				return statisticModel;
			}
		}));
		add(new NameTypeReportForm("form", getDefaultModel()));
	}
	
	public class NameTypeReportForm extends Form implements Serializable{
		public NameTypeReportForm (String id, IModel m){
			super(id, m);
			
			String js = "";
			final StatisticModel statisticModel = (StatisticModel) m.getObject();
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
					StatisticModel model = (StatisticModel) form.getDefaultModelObject();
					
					generateJS(model, month, target, false);
					
				}
			};
			add(search);
			
			final SSMLabel lastUpdate = new SSMLabel("lastUpdate", statisticModel.getLastUpdate(), "dd/MM/yyyy hh:mm:ss a");
			lastUpdate.setOutputMarkupId(true);
			add(lastUpdate);
			
			SSMAjaxButton updateData = new SSMAjaxButton("updateData"){
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					StatisticModel model = (StatisticModel) form.getDefaultModelObject();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
					lastUpdate.setDefaultModelObject(sdf.format(new Date()));
					target.add(lastUpdate);
					generateJS(model, month, target, true);
					
				}
			};
			add(updateData);
			
			generateJS(statisticModel, month, null, true);
			
		}
	}
	
	Map<String, Map<String,Integer>> nameWithData = new HashMap<String, Map<String,Integer>>();
	Map<String, Integer> totalByCategory = new HashMap<String,Integer>();
	Map<String, Integer> totalByMonth = new HashMap<String,Integer>();
	
	public void generateJS(StatisticModel statisticModel, final List<LlpParameters> month, AjaxRequestTarget target, Boolean updateData){
		WebMarkupContainer wmc = new WebMarkupContainer("divStacked");
		wmc.setOutputMarkupId(true);
		WebMarkupContainer tableDiv = new WebMarkupContainer("tableDiv");
		tableDiv.setOutputMarkupId(true);
		
		List<LlpParameters> nameList = llpParametersService.findByActiveCodeType(Parameter.ROB_NAME_TYPE);
		Integer grandTotal = 0;
		String js = "";
		totalByMonth.clear();
		
		js += "var nameTypeData = {categories : ['" + nameList.get(0).getCodeDesc() + "', '" + nameList.get(1).getCodeDesc() + "'], dataset : {";
		
		Integer indJ = 1;
		for(LlpParameters j : nameList){
			Integer categoryTotal = 0;
			Integer monthTotal = 0;
			Map<String, Integer> monthWithData = new HashMap<String, Integer>();
			js += "'" + j.getCodeDesc() + "' : [";
			Integer indI = 1;
			for(LlpParameters i : month){
				Integer value = robNameTypeStatReportService.getStatisticValue(statisticModel.getYear(), i.getCode(), statisticModel.getStatus(), j.getCode(), updateData);
				js += "{ name : '" + i.getCodeDesc() + "', value : " + value + " }";
				if(indI != month.size()){
					js += ",";
				}
				indI++;
				categoryTotal += value;
				grandTotal += value;
				monthWithData.put(i.getCodeDesc(), value);
				
				if(totalByMonth.containsKey(i.getCodeDesc())){
					totalByMonth.put(i.getCodeDesc(), totalByMonth.get(i.getCodeDesc()) + value);	
				}else{
					totalByMonth.put(i.getCodeDesc(), value);
				}
				
			}
			js += "]";
			
			if(indJ != nameList.size()){
				js += ",";
			}
			indJ++;
			
			totalByCategory.put(j.getCodeDesc(), categoryTotal);
			nameWithData.put(j.getCodeDesc(), monthWithData);
		}
		js += "}};";
		js += "var chart = uv.chart ('StackedBar', nameTypeData, { meta : { caption : 'Statistic for year " + statisticModel.getYear() + "', subcaption: 'Status: " + getCodeTypeWithValue(Parameter.ROB_FORM_A_STATUS, statisticModel.getStatus()) + "', hlabel : 'Month', vlabel : 'Number of transactions', isDownloadable : true, downloadLabel : 'Muat turun' }, legend : {legendstart: 0}, graph : { orientation : 'Vertical' }, dimension : { width: 700 } });";
		
		Label jsScript = new Label("jsScript", js);
		jsScript.setEscapeModelStrings(false);
		jsScript.setOutputMarkupId(true);
		
		ListView monthList = new ListView<LlpParameters>("monthList", month) {
			public void populateItem(final ListItem<LlpParameters> item) {
				final LlpParameters month = item.getModelObject();
				item.add(new SSMLabel("month", month.getCodeDesc()));
			}
		};
		tableDiv.add(monthList);
		
		ListView monthListTotal = new ListView<LlpParameters>("monthListTotal", month) {
			public void populateItem(final ListItem<LlpParameters> item) {
				final LlpParameters month = item.getModelObject();
				item.add(new SSMLabel("monthTotal", totalByMonth.get(month.getCodeDesc())));
			}
		};
		tableDiv.add(monthListTotal);
		
		ListView statItems = new ListView<LlpParameters>("statItems", nameList) {
			public void populateItem(final ListItem<LlpParameters> item) {
				final LlpParameters statItem = item.getModelObject();
				item.add(new SSMLabel("statType", statItem.getCodeDesc()));
				item.add(new SSMLabel("categoryTotal", totalByCategory.get(statItem.getCodeDesc())));
				
				item.add(new ListView<LlpParameters>("monthData", month) {
					public void populateItem(final ListItem<LlpParameters> item2) {
						LlpParameters param = item2.getModelObject();
						item2.add(new SSMLabel("value", nameWithData.get(statItem.getCodeDesc()).get(param.getCodeDesc())));
					}
				});  
			}
		};
		
		tableDiv.add(new SSMLabel("grandTotal", grandTotal));
		
		tableDiv.add(statItems);
		
		if(target==null){
			add(jsScript);
			add(wmc);
			add(tableDiv);
		}else{
			replace(jsScript);
			replace(wmc);
			replace(tableDiv);
			target.add(wmc);
			target.add(jsScript);
			target.add(tableDiv);
		}
		
		
	}
	
	public class StatisticModel implements Serializable{
		private String year;
		private String status;
		private Date lastUpdate;

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Date getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(Date lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}
	
	@Override
	public String getPageTitle() {
		
		return "menu.myBiz.nameTypeReport";
	}
}
