package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormTransactionDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.SimpleBaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormTransactionModel;
import com.ssm.llp.ezbiz.model.RobRenewal;

@Repository
public class RobFormTransactionDaoImpl extends SimpleBaseDaoImpl implements RobFormTransactionDao {
	
	@Override
	public List<RobFormTransactionModel> findLatestTransactionByLoginId(String loginName) {
		
		List<RobFormTransactionModel> listLatestTransaction = new ArrayList<RobFormTransactionModel>();
		
		Calendar todayMinus7day = Calendar.getInstance();
		todayMinus7day.set(Calendar.DATE, -14);
		
		String[] statusRenewal = { Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS, Parameter.ROB_RENEWAL_STATUS_SUCCESS,Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT };
		Query q = getSession().createQuery("from "+RobRenewal.class.getName() +" where createBy=? and status IN (:statuses) and updateDt>=:updateDt order by createDt desc ");
	    q.setMaxResults(5);
	    q.setString(0, loginName);
	    q.setParameterList("statuses", statusRenewal);
	    q.setParameter("updateDt", todayMinus7day.getTime());
	    
	    List<RobRenewal> listRenewal = q.list();
	    for (int i = 0; i < listRenewal.size(); i++) {
	    	RobRenewal form = listRenewal.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getTransCode(), form.getBrNoWithCheckDigit(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_RENEWAL, form);
	    	listLatestTransaction.add(model);
		}
	    
		String[] statusFormA = { Parameter.ROB_FORM_A_STATUS_DATA_ENTRY,Parameter.ROB_FORM_A_STATUS_PENDING_PAYMENT, Parameter.ROB_FORM_A_STATUS_PAYMENT_SUCCESS, Parameter.ROB_FORM_A_STATUS_IN_PROCESS,
				Parameter.ROB_FORM_A_STATUS_QUERY, Parameter.ROB_FORM_A_STATUS_REJECT, Parameter.ROB_FORM_A_STATUS_APPROVED,
				Parameter.ROB_FORM_A_STATUS_INCENTIVE_QUERY, Parameter.ROB_FORM_A_STATUS_PENDING_VERIFICATION };
		q = getSession().createQuery("from "+RobFormA.class.getName() +" where createBy=? and status IN (:statuses) and updateDt>=:updateDt order by createDt desc ");
	    q.setMaxResults(5);
	    q.setString(0, loginName);
	    q.setParameterList("statuses", statusFormA);
	    q.setParameter("updateDt", todayMinus7day.getTime());
	    
	    List<RobFormA> listRobFormA = q.list();
	    for (int i = 0; i < listRobFormA.size(); i++) {
	    	RobFormA form = listRobFormA.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getRobFormACode(), form.getBrNo(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_A, form);
	    	listLatestTransaction.add(model);
		}
	    
	    
	    
	    String[] statusFormB = { Parameter.ROB_FORM_B_STATUS_DATA_ENTRY, Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT, Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS, Parameter.ROB_FORM_B_STATUS_IN_PROCESS,
				Parameter.ROB_FORM_B_STATUS_QUERY, Parameter.ROB_FORM_B_STATUS_REJECT, Parameter.ROB_FORM_B_STATUS_APPROVED };
	    
		q = getSession().createQuery("from "+RobFormB.class.getName() +" where createBy=? and status IN (:statuses) and updateDt>=:updateDt order by createDt desc ");
	    q.setMaxResults(5);
	    q.setString(0, loginName);
	    q.setParameterList("statuses", statusFormB);
	    q.setParameter("updateDt", todayMinus7day.getTime());
	    
	    List<RobFormB> listRobFormB = q.list();
	    for (int i = 0; i < listRobFormB.size(); i++) {
	    	RobFormB form = listRobFormB.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getRobFormBCode(), form.getBrNo(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_B, form);
	    	listLatestTransaction.add(model);
		}
	    
	    String[] statusFormC = { Parameter.ROB_FORM_C_STATUS_DATA_ENTRY, Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT, Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT, Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS, Parameter.ROB_FORM_C_STATUS_IN_PROCESS,
				Parameter.ROB_FORM_C_STATUS_QUERY, Parameter.ROB_FORM_C_STATUS_REJECT, Parameter.ROB_FORM_C_STATUS_APPROVED };
		q = getSession().createQuery("from "+RobFormC.class.getName() +" where createBy=? and status IN (:statuses) and updateDt>=:updateDt order by createDt desc ");
	    q.setMaxResults(5);
	    q.setString(0, loginName);
	    q.setParameterList("statuses", statusFormC);
	    q.setParameter("updateDt", todayMinus7day.getTime());
	    
	    List<RobFormC> listRobFormC = q.list();
	    for (int i = 0; i < listRobFormC.size(); i++) {
	    	RobFormC form = listRobFormC.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getRobFormCCode(), form.getBrNo(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_C, form);
	    	listLatestTransaction.add(model);
		}
	    
	    
	    
	    Collections.sort(listLatestTransaction);
	   
		return listLatestTransaction;
	}

	@Override
	public List<RobFormTransactionModel> findPendingTransaction(String brNo) {

		List<RobFormTransactionModel> listLatestTransaction = new ArrayList<RobFormTransactionModel>();
		
		
		//Pending Renewal
		String[] statusRenewal = { Parameter.ROB_RENEWAL_STATUS_PENDING_PAYMENT, Parameter.ROB_RENEWAL_STATUS_PAYMENT_SUCCESS };
		Query q = getSession().createQuery("from "+RobRenewal.class.getName() +" where brNo=? and status IN (:statuses) ");
	    q.setString(0, brNo);
	    q.setParameterList("statuses", statusRenewal);
	    
	    List<RobRenewal> listRenewal = q.list();
	    for (int i = 0; i < listRenewal.size(); i++) {
	    	RobRenewal form = listRenewal.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getTransCode(), form.getBrNoWithCheckDigit(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_RENEWAL, form);
	    	listLatestTransaction.add(model);
		}
	    
	    
	    
	    
	  //Pending Form B
	    String[] statusFormB = {Parameter.ROB_FORM_B_STATUS_DATA_ENTRY, Parameter.ROB_FORM_B_STATUS_IN_PROCESS, Parameter.ROB_FORM_B_STATUS_OTC,Parameter.ROB_FORM_B_STATUS_PAYMENT_SUCCESS
				,Parameter.ROB_FORM_B_STATUS_PENDING_PAYMENT,Parameter.ROB_FORM_B_STATUS_QUERY};
	    
	    q = getSession().createQuery("from "+RobFormB.class.getName() +" where brNo=? and status IN (:statuses)  ");
	    q.setString(0, brNo);
	    q.setParameterList("statuses", statusFormB);
	    
	    List<RobFormB> listRobFormB = q.list();
	    for (int i = 0; i < listRobFormB.size(); i++) {
	    	RobFormB form = listRobFormB.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getRobFormBCode(), form.getBrNo(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_B, form);
	    	listLatestTransaction.add(model);
		}
		
		
	    //Pending Form C
	    String[] statusFormC = {Parameter.ROB_FORM_C_STATUS_DATA_ENTRY, Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT, Parameter.ROB_FORM_C_STATUS_IN_PROCESS, Parameter.ROB_FORM_C_STATUS_OTC,Parameter.ROB_FORM_C_STATUS_PAYMENT_SUCCESS
				,Parameter.ROB_FORM_C_STATUS_PENDING_PAYMENT,Parameter.ROB_FORM_C_STATUS_QUERY};
	    
	    q = getSession().createQuery("from "+RobFormC.class.getName() +" where brNo=? and status IN (:statuses) ");
	    q.setString(0, brNo);
	    q.setParameterList("statuses", statusFormC);
	    
	    List<RobFormC> listRobFormC = q.list();
	    for (int i = 0; i < listRobFormC.size(); i++) {
	    	RobFormC form = listRobFormC.get(i);
	    	RobFormTransactionModel model = new RobFormTransactionModel(form.getRobFormCCode(), form.getBrNo(), form.getBizName(),
					form.getStatus(), form.getCreateDt(), form.getUpdateDt(),
					Parameter.ROB_FORM_TYPE_C, form);
	    	listLatestTransaction.add(model);
		}
	    
	    
	    Collections.sort(listLatestTransaction);
	    
		return listLatestTransaction;
	}
	 
}

