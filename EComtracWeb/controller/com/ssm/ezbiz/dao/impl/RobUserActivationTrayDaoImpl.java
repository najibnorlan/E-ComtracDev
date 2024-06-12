/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobUserActivationTrayDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

@Repository
public class RobUserActivationTrayDaoImpl extends BaseDaoImpl<RobUserActivationTray, String> implements RobUserActivationTrayDao {

	@Override
	public boolean hasPendingApplication(String userRefNo) {
		
		String hql2 = "SELECT count(appRefNo)" + " FROM " + RobUserActivationTray.class.getName()
				+ " WHERE userRefNo=? and status in (?,?,?) ";

		List list = getHibernateTemplate().find(hql2,userRefNo,Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS, Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION,
				Parameter.ACTIVATION_TRAY_STATUS_QUERY);
		
		if((Long)list.get(0)>0) {
			return true;
		}
		

		return false;
	} 

	@Override
	public void discardNonPendingApp() {
		
		
		String pendingStatus[] = new String[] { Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS, Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION,
				Parameter.ACTIVATION_TRAY_STATUS_QUERY };
		
		String hql = "select appRefNo FROM " + RobUserActivationTray.class.getName()
				+ " WHERE status in (:status) and llpUserProfile.userStatus!=:userStatus ";
		
		Session sess = getSession();
		Query querySelect = sess.createQuery(hql);
		
		querySelect.setParameterList("status",  pendingStatus);
		querySelect.setParameter("userStatus", Parameter.USER_STATUS_pending);
		
		List<String> listAppRefNo = querySelect.list();
		
		String hqlUpdate = "update " + RobUserActivationTray.class.getName()
				+ " set status=:statusToBe, processBy=:processBy, processDt=:processDt, processNote=:processNote "
				+ " WHERE appRefNo in (:appRefNos)  ";
		
		Query queryUpdate = sess.createQuery(hqlUpdate);
		
		queryUpdate.setParameterList("appRefNos",  listAppRefNo);
		
		queryUpdate.setParameter("statusToBe", Parameter.ACTIVATION_TRAY_STATUS_DISCARD);
		queryUpdate.setParameter("processBy", "system");
		queryUpdate.setParameter("processDt", new Date());
		queryUpdate.setParameter("processNote", WicketApplication.resolve("page.lbl.ezbiz.robUserActivationTray.discardDueToNotPending"));
		
		queryUpdate.executeUpdate();
		
	}
}
