/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.CmpDetail;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface RobUserActivationTrayDao extends BaseDao<RobUserActivationTray, String> {

	boolean hasPendingApplication(String userRefNo);

	void discardNonPendingApp();
}
