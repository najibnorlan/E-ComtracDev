package com.ssm.ezbiz.healthCheck;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import com.ssm.ezbiz.service.RobHealthCheckService;
import com.ssm.ezbiz.service.RobSchedulerService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.ezbiz.model.RobHealthCheck;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;

@SuppressWarnings({ "all" })
public class HealthCheckPage extends SecBasePage {

	@SpringBean(name = "RobHealthCheckService")
	private RobHealthCheckService robHealthCheckService;

	@SpringBean(name = "RobSchedulerService")
	private RobSchedulerService robSchedulerService;

	Label statusLabel;
	Label updateDt;
	List<RobHealthCheck> healthCheckSceList;
	SSMSessionSortableDataProvider dpScheduler;
	
	public HealthCheckPage() {
		
		final WebMarkupContainer appContainer = new WebMarkupContainer("appContainer");
		appContainer.setOutputMarkupId(true);
		add(appContainer);
		
		final WebMarkupContainer schedulerContainer = new WebMarkupContainer("schedulerContainer");
		schedulerContainer.setOutputMarkupId(true);
		add(schedulerContainer);
		
		
		Button runScheduler = new Button("runScheduler");
		add(runScheduler);
		runScheduler.add(new AjaxEventBehavior("click") {
			@Override
			protected void onEvent(AjaxRequestTarget arg0) {
				robSchedulerService.runAllScheduler();
				setResponsePage(HealthCheckPage.class);
			}
		});
		
		Button btnCheck = new Button("btnCheck");
		add(btnCheck);
		btnCheck.add(new AjaxEventBehavior("click") {
			@Override
			protected void onEvent(AjaxRequestTarget arg0) {
				robSchedulerService.checkHealthAll();
				setResponsePage(HealthCheckPage.class);
			}
		});
		
		final SSMSessionSortableDataProvider dpApp = new SSMSessionSortableDataProvider("", robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_app));
		final SSMDataView<RobHealthCheck> dataViewApp = new SSMDataView<RobHealthCheck>("healthCheckList", dpApp) {

			protected void populateItem(final Item<RobHealthCheck> data) {
				final RobHealthCheck healthCheck = data.getModelObject();
				
				data.add(new Label("code", data.getModelObject().getCode().replace("_", " ")));

				Label statusLabel = new Label("status", data.getModelObject().getStatus());
				if(data.getModelObject().getStatus().equals("OK")) {
					statusLabel.add(new AttributeAppender("class", " green"));
				} else {
					statusLabel.add(new AttributeAppender("class", " red"));
				}
				data.add(statusLabel);
				
				data.add(new Label("updateDt", "Last checked on " + new SimpleDateFormat("dd/MM/yyyy h:mm:ss a").format(healthCheck.getUpdateDt())));
				data.add(new Label("time", data.getModelObject().getTime().toString() + " minutes"));
				
				SSMAjaxLink recheck = new SSMAjaxLink("recheck") {
					public void onClick(AjaxRequestTarget target){
						robSchedulerService.runSchedulerByMethodName(healthCheck.getMethodName());
						
						dpApp.resetView(robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_app));
						target.add(appContainer);
					}
				};
				data.add(recheck);
			}
		};
//		appContainer.add(new AbstractAjaxTimerBehavior(Duration.seconds(5)){
//			@Override
//            protected void onTimer(AjaxRequestTarget target) {
//				dpApp.resetView(robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_app));
//				target.add(appContainer);
//            }
//		});
		appContainer.add(dataViewApp);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
		
		healthCheckSceList = robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_scheduler);
		
		dpScheduler = new SSMSessionSortableDataProvider("", healthCheckSceList);
		
		final SSMDataView<RobHealthCheck> dataViewSce = new SSMDataView<RobHealthCheck>("healthCheckSceList", dpScheduler) {

			protected void populateItem(final Item<RobHealthCheck> data) {
				final RobHealthCheck healthCheck = (RobHealthCheck) data.getModelObject();
				
				data.add(new Label("codeSce", data.getModelObject().getCode().replace("_", " ")));

				final Label statusLabel = new Label("statusSce", healthCheck.getStatus());
				statusLabel.setOutputMarkupId(true);
				if(data.getModelObject().getStatus().equals("OK")) {
					statusLabel.add(new AttributeAppender("class", " green"));
				} else {
					statusLabel.add(new AttributeAppender("class", " red"));
				}
				
				data.add(statusLabel);
				
				data.add(new Label("index", String.valueOf(data.getIndex()+1)));
				final Label updateDt = new Label("updateDtSce", sdf.format(healthCheck.getUpdateDt()));
				updateDt.setOutputMarkupId(true);
				data.add(updateDt);
				
				SSMAjaxLink recheck = new SSMAjaxLink("recheckSce") {
					public void onClick(AjaxRequestTarget target){
						robSchedulerService.runSchedulerByMethodName(healthCheck.getMethodName());
						
						dpScheduler.resetView(robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_scheduler));
						target.add(schedulerContainer);
						
					}
				};
				data.add(recheck);
			}
		};
		dataViewSce.setItemsPerPage(100);
//		schedulerContainer.add(new AbstractAjaxTimerBehavior(Duration.seconds(5)){
//			@Override
//            protected void onTimer(AjaxRequestTarget target) {
//				dpScheduler.resetView(robHealthCheckService.findByItemType(Parameter.SCHEDULER_ITEM_TYPE_scheduler));
//				target.add(schedulerContainer);
//            }
//		});
		schedulerContainer.add(dataViewSce);
		
	}
	
	@Override
	public String getPageTitle() {
		return "page.title.HealthCheckPage";
	}
	
}