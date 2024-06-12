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
import com.ssm.llp.ezbiz.model.RobFormB4;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface RobFormB4Dao extends BaseDao<RobFormB4, Long> {

	List<RobFormB4> findByRobFormBCode(String robFormBCode);

	void deleteExceptId(String robFormBCode, long[] listB4IdNotDelete);

}
