/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpMaster;

public interface CmpMasterService extends BaseService<CmpMaster, Long>{
	List<CmpMaster> findCmpListWS(String icNo, String entityType, String entityNo) throws SSMException;
	List<CmpMaster> findByTransCode(String transCode) throws SSMException;
}

