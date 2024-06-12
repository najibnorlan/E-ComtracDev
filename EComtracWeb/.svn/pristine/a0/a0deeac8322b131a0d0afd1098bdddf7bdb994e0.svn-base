/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.dao.impl;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.base.common.util.DateUtil;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.dao.LlpPaymentTransactionDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABizCode;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;

@Repository
public class LlpPaymentTransactionDaoImpl extends BaseDaoImpl<LlpPaymentTransaction, String>  implements LlpPaymentTransactionDao{

	@Override
	public LlpPaymentTransaction findSuccessByAppRefNo(String appRefNo) {
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where appRefNo=? and status=? order by createDt asc";
		
		List<LlpPaymentTransaction> result= getHibernateTemplate().find(hql, new String[]{appRefNo, Parameter.PAYMENT_STATUS_SUCCESS});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	} 
	
	@Override
	public List<LlpPaymentTransaction> findPendingByAppRefNo(String appRefNo) {
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where appRefNo=? and status=? order by createDt asc ";
		
		return getHibernateTemplate().find(hql, new String[]{appRefNo, Parameter.PAYMENT_STATUS_PENDING});
	}
	
	@Override
	public List<LlpPaymentTransaction> findByAppRefNoStatus(String appRefNo, String status) {
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where appRefNo=? and status=? and (paymentDetail != 'OTC' OR paymentDetail IS NULL) order by createDt asc ";
		
		return getHibernateTemplate().find(hql, new String[]{appRefNo, status});
	}
	
	@Override
	public LlpPaymentTransaction findPendingByTransactionId(String transactionId){
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where transactionId=? and status=? order by createDt asc";
		
		List<LlpPaymentTransaction> result= getHibernateTemplate().find(hql, new String[]{transactionId, Parameter.PAYMENT_STATUS_PENDING});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public LlpPaymentTransaction findByAppRefNoStatusPaymentMode(String appRefNo, String status, String paymentMode){
		
		ArrayList<String> param = new ArrayList<String>();
		param.add(appRefNo);
		param.add(status);
		
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where appRefNo=? and status=?";
		
		if(paymentMode != null){
			hql += " and paymentMode = ?";
			param.add(paymentMode);
		}
		
		hql += " order by createDt asc";
		
		List<LlpPaymentTransaction> result= getHibernateTemplate().find(hql, param.toArray());
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<LlpPaymentTransaction> findDetailPaymentForGaf(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status,
			String refNo, String paymentGroupPrefix) {
		
		
		Date toDate = new Date(searchToDt.getTime() + TimeUnit.DAYS.toMillis(1));//Plus one days
		
		String hql2 = " select distinct a from "+LlpPaymentTransaction.class.getName() +" a "
				+ " left join fetch a.llpPaymentReceipt as b " 
				+ " left join fetch a.robPaymentCreditNote as c " 
				+ " left join fetch a.llpPaymentTransactionDetails as d " 
				+ " where a.requestDt>=? and a.requestDt<=? ";
		
		//sql..
//		select distinct a.* from rob_payment_transaction a 
//		left join rob_payment_receipt b on b.transaction_id=a.transaction_id 
//		left join rob_payment_credit_note c on c.cn_transaction_no=a.transaction_id
//		left join rob_payment_transaction_detail d on d.transaction_id=a.transaction_id
//		where date(a.request_dt)>='2021-01-01' and date(a.request_dt)<='2021-01-13'
//		and a.app_ref_no = 'EB-A2021010400480' --filter (ie.OKU trx)
//		order by a.transaction_id desc
		
		List listParam = new ArrayList();
		listParam.add(searchFromDt);
		listParam.add(toDate);
		
		if(StringUtils.isNotBlank(status) && !Parameter.PAYMENT_STATUS_ALL.equals(status) ){
			hql2+=" and a.status = ? ";
			listParam.add(status);
		}
		
		if(StringUtils.isNotBlank(paymentMode)){
			hql2+=" and a.paymentMode = ? ";
			listParam.add(paymentMode);
		}
		
		if(StringUtils.isNotBlank(transactionId)){
			hql2+=" and a.transactionId = ? ";
			listParam.add(transactionId);
		}
		
		if(StringUtils.isNotBlank(refNo)){
			hql2+=" and a.appRefNo = ? ";
			listParam.add(refNo);
		}
		

		if(StringUtils.isNotBlank(paymentGroupPrefix)) {
			
			String arryPaymentGrpPrefix[] = paymentGroupPrefix.split(",");
			for (int i = 0; i < arryPaymentGrpPrefix.length; i++) {
				if (i==0) {
					hql2 += " and (a.appRefNo like '"+arryPaymentGrpPrefix[i]+"%' "; //to cater paymentGroup EzBiz Online (EB,ROB).
				}else {
					hql2 += " or a.appRefNo like '"+arryPaymentGrpPrefix[i]+"%' ";
				}
			}
			hql2 +=")";
		}
		
		
		hql2+=" order by a.transactionId asc";
		System.out.println("SQL payment trx report ----> "+hql2);
		return getHibernateTemplate().find(hql2, listParam.toArray());
	}
	
	@Override
	public void cancelAllPreviosDayOTCPayment() {
		String hql = " update " + LlpPaymentTransaction.class.getName() + " set status='C'  where status = ? and paymentDetail=? and createDt<?";
		Query query = getSession().createQuery(hql);
		query.setString(0, Parameter.PAYMENT_STATUS_PENDING);
		query.setString(1, Parameter.PAYMENT_DETAIL_OTC);
		query.setDate(2, DateUtil.getCurrentDate());
		query.executeUpdate();
	}

	@Override
	public boolean hasPendingOnlineAndSuccessPaymentByAppRefNo(String appRefNo) {
		String hql = "from "+LlpPaymentTransaction.class.getName()
				+" where appRefNo=? and (status = ? or (status=? and paymentMode is null) )   ";
		
		List list = getHibernateTemplate().find(hql, new String[]{appRefNo, Parameter.PAYMENT_STATUS_SUCCESS,Parameter.PAYMENT_STATUS_PENDING});
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void cancelPendingOtcByAppRefNo(String appRefNo) {
		String hql = " update " + LlpPaymentTransaction.class.getName() + " set status='C'  where appRefNo=? and status=? and paymentDetail=? ";
		
		Session ses = getSession();//getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = ses.createQuery(hql);
		query.setString(0, appRefNo);
		query.setString(1, Parameter.PAYMENT_STATUS_PENDING);
		query.setString(2, Parameter.PAYMENT_DETAIL_OTC);
		query.executeUpdate();
		
		
	}
	
}
