package com.ssm.ezbiz.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.UsageReportDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobRenewal;
import com.ssm.llp.mod1.model.BranchSummaryReportModel;

@Repository
public class UsageReportDaoImpl extends BaseDaoImpl<UsageReportDao, String> implements UsageReportDao{
	@Override
	public Integer countRobByCriteria(String month, String bizType, String formType, String status, String year){
		
		ArrayList<String> param = new ArrayList<String>();
		String className = "";
		String statusToCheck = "";
		String dateToCheck = "createDt";
		
		if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(status) || Parameter.ROB_FORM_A_STATUS_REJECT.equals(status)){
			dateToCheck = "approveRejectDt";
		}
		
		if(formType != null){
			
			statusToCheck = getStatusToCheck(formType, status);
			
			if(StringUtils.isBlank(statusToCheck)){
				return 0;
			}
			
			if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_A)){
				className = RobFormA.class.getName();
			}else if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_B)){
				className = RobFormB.class.getName();
			}else if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_C)){
				className = RobFormC.class.getName();
			}else if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_RENEWAL)){
				className = RobRenewal.class.getName();
			}else if(formType.equalsIgnoreCase("CMP")){
				className = CmpTransaction.class.getName();
			}
			
		}else{
			className = RobFormA.class.getName();
			statusToCheck = "'" + status + "'";
		}
		
		String hql = "FROM "+ className;
		
		if(year != null || month != null || status != null || bizType != null){
			hql += " WHERE";
		}
		
		if(bizType != null){
			hql += " nameType=?";
			param.add(bizType);
			if(year != null || month != null || status != null){
				hql += " AND";
			}
		}
		
		if(year != null){
			hql += " to_char(" + dateToCheck + ", '%Y')=?";
//			hql += " DATE_FORMAT(" + dateToCheck + ", '%Y')=?";//MYSQL
			param.add(year);
			if(month != null || status != null){
				hql += " AND";
			}
		}
		if(month != null){
			hql += " to_char(" + dateToCheck + ", '%m')=?";
//			hql += " DATE_FORMAT(" + dateToCheck + ", '%m')=?";//MYSQL
			param.add(month);
			if(status != null && !status.equalsIgnoreCase("all") && !StringUtils.isBlank(statusToCheck)){
				hql += " AND";
			}
		}
		if(status != null && !status.equalsIgnoreCase("all") && !StringUtils.isBlank(statusToCheck)){
			hql += " status IN (" + statusToCheck + ")";
		}
		String[] paramArray = new String[ param.size() ];
		param.toArray( paramArray );
		
		List result = getHibernateTemplate().find(hql, paramArray);
		
		return result.size();
	}
	
	@Override
	public BranchSummaryReportModel findBranchSummaryReportData(String paymentCode, String branch, Date dateFrom, Date dateTo) {
		List<Object> params = new ArrayList<Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateFromString = df.format(dateFrom);
		String dateToString = df.format(dateTo);
		
		params.add(branch);
		params.add(dateFromString);
		params.add(dateToString);
		/*params.add(paymentMode);*/
		params.add(paymentCode);
		
		String hql = "select "
					+ "b.paymentItem, " 
					+ "(select distinct codeDesc from LlpParameters where codeType = 'PAYMENT_TYPE' and code = b.paymentItem) as paymentCodeDesc, "
					+ "count(*) as count, " 
					+ "sum(b.amount) as sum  " 
					+ "from LlpPaymentTransaction a, LlpPaymentTransactionDetail b, LlpPaymentReceipt c, RobCounterSession d "
					+ "where b.transactionId = a.transactionId "
					+ "and c.transactionId = a.transactionId "
					+ "and d.sessionId = c.counterSessionId "
					+ "and d.branch = ? "
//					+ "and DATE_FORMAT(c.createDt, '%Y-%m-%d') between ? and ? "//MYSQL
					+ "and to_char(c.createDt, '%Y-%m-%d') between ? and ? "
					+ "and a.paymentMode = 'CASH'"
					+ "and c.isCancel = 0 "
					+ "and b.paymentItem = ? "
					+ "group by b.paymentItem "
					+ "order by b.paymentItem";
		
		List<Object[]> result = getHibernateTemplate().find(hql, params.toArray());
		
		BranchSummaryReportModel bsrm = null;
		if(result.size() > 0){
			Object[] data = (Object[]) result.get(0);
			bsrm = new BranchSummaryReportModel();
			bsrm.setPaymentCode((String) data[0]);
			bsrm.setPaymentCodeDesc((String) data[1]);
				
			Long countL = (Long) data[2];
			bsrm.setCount(countL.intValue());
				
			bsrm.setSum((Double) data[3]);
		}
		
		return bsrm;
	}

	public String getStatusToCheck(String formType, String status){
		
		String statusToCheck = "";
		if(Parameter.ROB_FORM_A_STATUS_DATA_ENTRY.equals(status)){
			statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_DATA_ENTRY + "'";
			if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_A)){
				statusToCheck += ", '" + Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION + "'";
				statusToCheck += ", '" + Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY + "'";
			}
		}else if(Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT.equals(status)){
			statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT + "'";
			statusToCheck += ", '" + Parameter.ROB_FORM_A_STATUS_OTC + "'";
		}else if(Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS.equals(status)){
			statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS + "'";
		}else if(Parameter.ROB_FORM_A_STATUS_IN_PROCESS.equals(status)){
			if(!formType.equalsIgnoreCase("CMP")){
				statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_IN_PROCESS + "'";
			}
		}else if(Parameter.ROB_FORM_A_STATUS_APPROVED.equals(status)){
			if(!formType.equalsIgnoreCase("CMP")){
				statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_APPROVED + "'";
			}
			if(formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_RENEWAL)){
				statusToCheck = "'" + Parameter.ROB_RENEWAL_STATUS_SUCCESS + "'";
			}
		}else if(Parameter.ROB_FORM_A_STATUS_REJECT.equals(status)){
			if(!formType.equalsIgnoreCase("CMP") && !formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_RENEWAL)){
				statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_REJECT + "'";
			}
		}else if(Parameter.ROB_FORM_A_STATUS_QUERY.equals(status)){
			if(!formType.equalsIgnoreCase("CMP") && !formType.equalsIgnoreCase(Parameter.ROB_FORM_TYPE_RENEWAL)){
				statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_QUERY + "'";
			}
		}else if(Parameter.ROB_FORM_A_STATUS_CANCEL.equals(status)){
			if(!formType.equalsIgnoreCase("CMP")){
				statusToCheck = "'" + Parameter.ROB_FORM_A_STATUS_CANCEL + "'";
			}
		}
		
		return statusToCheck;
	}
	@Override
	public List<Object> getMonthYearCreateDt() {
		final String hql = "select distinct month(create_dt) as m, year(create_dt) as y from rob_form_a order by year(create_dt), month(create_dt)";
		return getHibernateTemplate().execute(new HibernateCallback<List>() {
			@Override
	        public List doInHibernate(Session s) throws HibernateException, SQLException {
	            SQLQuery sql=s.createSQLQuery(hql);
	            sql.addScalar("m");
	            sql.addScalar("y");
	            return sql.list();
	        }
	    });
	}
}
