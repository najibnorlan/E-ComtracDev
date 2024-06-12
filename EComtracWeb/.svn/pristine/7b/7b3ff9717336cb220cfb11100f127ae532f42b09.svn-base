package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobCounterSessionDao;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@Repository
public class RobCounterSessionDaoImpl extends BaseDaoImpl<RobCounterSession, Integer> implements RobCounterSessionDao{

	public RobCounterSession findByIpAddressStatusNDE(String ipAddress){
		
		Object[] vals = {Parameter.BALANCING_STATUS_new, Parameter.BALANCING_STATUS_no};
		
		SearchCriteria sc = new SearchCriteria("counterIpAddress.ipAddress", SearchCriteria.EQUAL, ipAddress);
		sc = sc.andIfInNotNull("balancingStatus", vals, false);
		sc.addOrderBy("createDt", SearchCriteria.DESC);
		
		System.out.print("masuk");
		List<RobCounterSession> counter = findByCriteria(sc).getList();
		if(counter.size() > 0){
			return counter.get(0);
		}
		return null;
	}
	
	public List<Object> getBranchAndFloorNoBankSlip(String branchCode, String floor, String date, String isApprove){
		
		ArrayList<String> param = new ArrayList<String>();
		
		String hql = "SELECT a.branch, a.floorLevel, SUM(c.totalAmount), count(c.receiptNo)" + 
				" FROM " + RobCounterSession.class.getName() + " a, " + LlpPaymentReceipt.class.getName() + " c" +
				" WHERE a.counterBankinSlipNo IS NULL AND c.isCancel = " + Parameter.PAYMENT_RECEIPT_ISCANCEL_no +
				" AND a.sessionId = c.counterSessionId";
		
		if(branchCode != null || floor != null || date != null){
			hql += " AND";
		}
		
		if(branchCode != null){
			hql += " a.branch = ?";
			param.add(branchCode);
			if(floor != null || date != null){
				hql += " AND";
			}
		}
		
		if(floor != null){
			hql += " a.floorLevel = ?";
			param.add(floor);
			if(date != null){
				hql += " AND";
			}
		}
		
		if(date != null){
			hql += " to_char(a.checkoutDate, '%Y-%m-%d')=?";//INFORMIX
//			hql += " DATE_FORMAT(a.checkoutDate, '%Y-%m-%d')=?";//MYSQL
			param.add(date);
		}
		
		if(isApprove != null){
			if(isApprove.equalsIgnoreCase(Parameter.YES_NO_yes)){
				hql += " AND a.balancingStatus = 'A'";
			}else{
				hql += " AND a.balancingStatus != 'A'";
			}
		}
				
		hql += " GROUP BY a.branch, a.floorLevel";
		 
		String[] paramArray = new String[ param.size() ];
		param.toArray( paramArray );
		
		List<Object> result = getHibernateTemplate().find(hql, paramArray);
		
		return result;
	}
}
