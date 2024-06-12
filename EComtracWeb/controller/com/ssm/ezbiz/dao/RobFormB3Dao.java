/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao;

import java.util.List;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.ezbiz.model.RobFormB2;
import com.ssm.llp.ezbiz.model.RobFormB3;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface RobFormB3Dao extends BaseDao<RobFormB3, Long> {

	void deleteExceptId(String robFormBCode, long[] listB3IdNotDelete);
	
	List<RobFormB3> findByRobFormBCode(String robFormBCode);

}
