package com.ssm.ezbiz.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.ssm.ezbiz.service.RobTrainingDashboardService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.RobTrainingDashboard;

@SuppressWarnings("serial")
public class DashboardInternalPage extends SecBasePage {

	@SpringBean(name = "RobTrainingDashboardService")
	RobTrainingDashboardService robTrainingDashboardService;

	@SuppressWarnings("unchecked")

	public DashboardInternalPage() {
		init();
	}

	public String getPageTitle() {
		String titleKey = "page.title.dashboardInternal";
		return titleKey;

	}

	public void init() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-YYYY hh:mm:ss");
		Calendar calendar = Calendar.getInstance();
		int currYear = calendar.get(Calendar.YEAR);
		int  prevYear = currYear - 1;		
		Integer[] years = {currYear,prevYear};
		
		RobTrainingDashboard robTrainingDashboard = null;
		RobTrainingDashboard robTrainingDashboard1 = null;
		RobTrainingDashboard robTrainingDashboard2 = null;
		RobTrainingDashboard robTrainingDashboard3 = null;
		RobTrainingDashboard robTrainingDashboard4 = null;
		RobTrainingDashboard robTrainingDashboard5 = null;
		
		add(new SSMLabel("currYear", currYear));
		add(new SSMLabel("prevYear", prevYear));
		
		List<RobTrainingDashboard> robTrainingDashboards = robTrainingDashboardService.findListDashboardData(years);
		if(robTrainingDashboards.size() < 8) {
			robTrainingDashboardService.updateData();
			robTrainingDashboards = robTrainingDashboardService.findListDashboardData(years);
		}
		
		for (RobTrainingDashboard listDashboard : robTrainingDashboards) {
			try {

				if("PAST_PROGRAM".equals(listDashboard.getType()) && listDashboard.getYear() == currYear) {
					robTrainingDashboard = listDashboard;
				}
				if("PAX".equals(listDashboard.getType()) && listDashboard.getYear() == currYear) {
					robTrainingDashboard1 = listDashboard;
				}
				if("TOTAL_PAX".equals(listDashboard.getType()) && listDashboard.getYear() == currYear) {
					robTrainingDashboard2 = listDashboard;
				}
				if("REVENUE".equals(listDashboard.getType()) && listDashboard.getYear() == currYear) {
					robTrainingDashboard3 = listDashboard;
				}
				if("REVENUE".equals(listDashboard.getType()) && listDashboard.getYear() == prevYear) {
					robTrainingDashboard4 = listDashboard;
				}				
				if("FUTURE_PROGRAM".equals(listDashboard.getType()) && listDashboard.getYear() == currYear) {
					robTrainingDashboard5 = listDashboard;
				}
				
				
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				
			}
		}

		   //Start dashboard Closed program
		 
			Integer countMap = (robTrainingDashboard.getParam1() + robTrainingDashboard.getParam2()
					+ robTrainingDashboard.getParam3() + robTrainingDashboard.getParam4()
					+ robTrainingDashboard.getParam5() + robTrainingDashboard.getParam6()
					+ robTrainingDashboard.getParam7() + robTrainingDashboard.getParam8()
					+ robTrainingDashboard.getParam9() + robTrainingDashboard.getParam10()
					+ robTrainingDashboard.getParam11() + robTrainingDashboard.getParam12()
					+ robTrainingDashboard5.getParam1() + robTrainingDashboard5.getParam2()
					+ robTrainingDashboard5.getParam3() + robTrainingDashboard5.getParam4()
					+ robTrainingDashboard5.getParam5() + robTrainingDashboard5.getParam6()
					+ robTrainingDashboard5.getParam7() + robTrainingDashboard5.getParam8()
					+ robTrainingDashboard5.getParam9() + robTrainingDashboard5.getParam10()
					+ robTrainingDashboard5.getParam11() + robTrainingDashboard.getParam12());

			add(new SSMLabel("param1", robTrainingDashboard.getParam1()));
			add(new SSMLabel("param2", robTrainingDashboard.getParam2()));
			add(new SSMLabel("param3", robTrainingDashboard.getParam3()));
			add(new SSMLabel("param4", robTrainingDashboard.getParam4()));
			add(new SSMLabel("param5", robTrainingDashboard.getParam5()));
			add(new SSMLabel("param6", robTrainingDashboard.getParam6()));
			add(new SSMLabel("param7", robTrainingDashboard.getParam7()));
			add(new SSMLabel("param8", robTrainingDashboard.getParam8()));
			add(new SSMLabel("param9", robTrainingDashboard.getParam9()));
			add(new SSMLabel("param10", robTrainingDashboard.getParam10()));
			add(new SSMLabel("param11", robTrainingDashboard.getParam11()));
			add(new SSMLabel("param12", robTrainingDashboard.getParam12()));
			add(new SSMLabel("countTotalProgramme", countMap));
			add(new SSMLabel("lastUpdateDate", dateFormat.format(robTrainingDashboard.getUpdateDt())));

			//End Dahshboard Program
			
			 //Start dashboard Active program
			 
			add(new SSMLabel("paramActive1", robTrainingDashboard5.getParam1()));
			add(new SSMLabel("paramActive2", robTrainingDashboard5.getParam2()));
			add(new SSMLabel("paramActive3", robTrainingDashboard5.getParam3()));
			add(new SSMLabel("paramActive4", robTrainingDashboard5.getParam4()));
			add(new SSMLabel("paramActive5", robTrainingDashboard5.getParam5()));
			add(new SSMLabel("paramActive6", robTrainingDashboard5.getParam6()));
			add(new SSMLabel("paramActive7", robTrainingDashboard5.getParam7()));
			add(new SSMLabel("paramActive8", robTrainingDashboard5.getParam8()));
			add(new SSMLabel("paramActive9", robTrainingDashboard5.getParam9()));
			add(new SSMLabel("paramActive10", robTrainingDashboard5.getParam10()));
			add(new SSMLabel("paramActive11", robTrainingDashboard5.getParam11()));
			add(new SSMLabel("paramActive12", robTrainingDashboard5.getParam12()));

			//End Dahshboard Program
	
			//Start Dahshboard PAX
			Integer countMap5 = (robTrainingDashboard1.getParam1() + robTrainingDashboard1.getParam2()
			+ robTrainingDashboard1.getParam3() + robTrainingDashboard1.getParam4()
			+ robTrainingDashboard1.getParam5() + robTrainingDashboard1.getParam6()
			+ robTrainingDashboard1.getParam7() + robTrainingDashboard1.getParam8()
			+ robTrainingDashboard1.getParam9() + robTrainingDashboard1.getParam10()
			+ robTrainingDashboard1.getParam11() + robTrainingDashboard1.getParam12());

			add(new SSMLabel("paxparam1", robTrainingDashboard1.getParam1()));
			add(new SSMLabel("paxparam2", robTrainingDashboard1.getParam2()));
			add(new SSMLabel("paxparam3", robTrainingDashboard1.getParam3()));
			add(new SSMLabel("paxparam4", robTrainingDashboard1.getParam4()));
			add(new SSMLabel("paxparam5", robTrainingDashboard1.getParam5()));
			add(new SSMLabel("paxparam6", robTrainingDashboard1.getParam6()));
			add(new SSMLabel("paxparam7", robTrainingDashboard1.getParam7()));
			add(new SSMLabel("paxparam8", robTrainingDashboard1.getParam8()));
			add(new SSMLabel("paxparam9", robTrainingDashboard1.getParam9()));
			add(new SSMLabel("paxparam10", robTrainingDashboard1.getParam10()));
			add(new SSMLabel("paxparam11", robTrainingDashboard1.getParam11()));
			add(new SSMLabel("paxparam12", robTrainingDashboard1.getParam12()));
			add(new SSMLabel("CountTotalPax", countMap5));
			//End Dahshboard  PAX
			
			
			
			//Start Dahshboard TOTAL PAX		
//			Integer countMap5 = (robTrainingDashboard2.getParam1() + robTrainingDashboard2.getParam2()
//						+ robTrainingDashboard2.getParam3() + robTrainingDashboard2.getParam4()
//						+ robTrainingDashboard2.getParam5() + robTrainingDashboard2.getParam6()
//						+ robTrainingDashboard2.getParam7() + robTrainingDashboard2.getParam8()
//						+ robTrainingDashboard2.getParam9() + robTrainingDashboard2.getParam10()
//						+ robTrainingDashboard2.getParam11() + robTrainingDashboard2.getParam12());
//
//			add(new SSMLabel("tpaxparam1", robTrainingDashboard2.getParam1()));
//			add(new SSMLabel("tpaxparam2", robTrainingDashboard2.getParam2()));
//			add(new SSMLabel("tpaxparam3", robTrainingDashboard2.getParam3()));
//			add(new SSMLabel("tpaxparam4", robTrainingDashboard2.getParam4()));
//			add(new SSMLabel("tpaxparam5", robTrainingDashboard2.getParam5()));
//			add(new SSMLabel("tpaxparam6", robTrainingDashboard2.getParam6()));
//			add(new SSMLabel("tpaxparam7", robTrainingDashboard2.getParam7()));
//			add(new SSMLabel("tpaxparam8", robTrainingDashboard2.getParam8()));
//			add(new SSMLabel("tpaxparam9", robTrainingDashboard2.getParam9()));
//			add(new SSMLabel("tpaxparam10", robTrainingDashboard2.getParam10()));
//			add(new SSMLabel("tpaxparam11", robTrainingDashboard2.getParam11()));
//			add(new SSMLabel("tpaxparam12", robTrainingDashboard2.getParam12()));
//			add(new SSMLabel("CountTotalPax", countMap5));

	//End Dahshboard TOTAL PAX
			
	//Start Dahshboard REVENUE

			Integer countMap1 = (robTrainingDashboard3.getParam1() + robTrainingDashboard3.getParam2()
			+ robTrainingDashboard3.getParam3() + robTrainingDashboard3.getParam4()
			+ robTrainingDashboard3.getParam5() + robTrainingDashboard3.getParam6()
			+ robTrainingDashboard3.getParam7() + robTrainingDashboard3.getParam8()
			+ robTrainingDashboard3.getParam9() + robTrainingDashboard3.getParam10()
			+ robTrainingDashboard3.getParam11() + robTrainingDashboard3.getParam12());

		add(new SSMLabel("rparam1", robTrainingDashboard3.getParam1()));
		add(new SSMLabel("rparam2", robTrainingDashboard3.getParam2()));
		add(new SSMLabel("rparam3", robTrainingDashboard3.getParam3()));
		add(new SSMLabel("rparam4", robTrainingDashboard3.getParam4()));
		add(new SSMLabel("rparam5", robTrainingDashboard3.getParam5()));
		add(new SSMLabel("rparam6", robTrainingDashboard3.getParam6()));
		add(new SSMLabel("rparam7", robTrainingDashboard3.getParam7()));
		add(new SSMLabel("rparam8", robTrainingDashboard3.getParam8()));
		add(new SSMLabel("rparam9", robTrainingDashboard3.getParam9()));
		add(new SSMLabel("rparam10", robTrainingDashboard3.getParam10()));
		add(new SSMLabel("rparam11", robTrainingDashboard3.getParam11()));
		add(new SSMLabel("rparam12", robTrainingDashboard3.getParam12()));
		add(new SSMLabel("countTotalRevenue", countMap1));
		
		Integer Q1 = (robTrainingDashboard3.getParam1() + robTrainingDashboard3.getParam2()
		+ robTrainingDashboard3.getParam3());
		
		Integer Q2 = (robTrainingDashboard3.getParam4() + robTrainingDashboard3.getParam5()
		+ robTrainingDashboard3.getParam6());
		
		Integer Q3 = (robTrainingDashboard3.getParam7() + robTrainingDashboard3.getParam8()
		+ robTrainingDashboard3.getParam9());
		
		Integer Q4 = (robTrainingDashboard3.getParam10() + robTrainingDashboard3.getParam11()
		+ robTrainingDashboard3.getParam12());
		
		add(new SSMLabel("countTotalQ1", Q1));
		add(new SSMLabel("countTotalQ2", Q2));
		add(new SSMLabel("countTotalQ3", Q3));
		add(new SSMLabel("countTotalQ4", Q4));

		//End Dahshboard REVENUE
	
		//Start Dahshboard REVENUE previous year	
	
		add(new SSMLabel("rpyparam1", robTrainingDashboard4.getParam1()));
		add(new SSMLabel("rpyparam2", robTrainingDashboard4.getParam2()));
		add(new SSMLabel("rpyparam3", robTrainingDashboard4.getParam3()));
		add(new SSMLabel("rpyparam4", robTrainingDashboard4.getParam4()));
		add(new SSMLabel("rpyparam5", robTrainingDashboard4.getParam5()));
		add(new SSMLabel("rpyparam6", robTrainingDashboard4.getParam6()));
		add(new SSMLabel("rpyparam7", robTrainingDashboard4.getParam7()));
		add(new SSMLabel("rpyparam8", robTrainingDashboard4.getParam8()));
		add(new SSMLabel("rpyparam9", robTrainingDashboard4.getParam9()));
		add(new SSMLabel("rpyparam10", robTrainingDashboard4.getParam10()));
		add(new SSMLabel("rpyparam11", robTrainingDashboard4.getParam11()));
		add(new SSMLabel("rpyparam12", robTrainingDashboard4.getParam12()));
		
		Integer Q1PY = (robTrainingDashboard4.getParam1() + robTrainingDashboard4.getParam2()
		+ robTrainingDashboard4.getParam3());
		
		Integer Q2PY = (robTrainingDashboard4.getParam4() + robTrainingDashboard4.getParam5()
		+ robTrainingDashboard4.getParam6());
		
		Integer Q3PY = (robTrainingDashboard4.getParam7() + robTrainingDashboard4.getParam8()
		+ robTrainingDashboard1.getParam9());
		
		Integer Q4PY = (robTrainingDashboard4.getParam10() + robTrainingDashboard4.getParam11()
		+ robTrainingDashboard1.getParam12());
		
		add(new SSMLabel("countTotalQ1PY", Q1PY));
		add(new SSMLabel("countTotalQ2PY", Q2PY));
		add(new SSMLabel("countTotalQ3PY", Q3PY));
		add(new SSMLabel("countTotalQ4PY", Q4PY));
		
	//End Dahshboard REVENUE previous year
	}
}
