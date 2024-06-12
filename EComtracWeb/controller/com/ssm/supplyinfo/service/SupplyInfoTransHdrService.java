/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.supplyinfo.service;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;

/**
 * TODO DOCUMENTTHIS
 *
 * @author zamzam
 * @version 1.0
  */
public interface SupplyInfoTransHdrService extends BaseService<SupplyInfoTransHdr, String> {

	SupplyInfoTransHdr findAllById(String transCode);

	SupplyInfoTransHdr genAndMergeLatestCart(String loginName, SupplyInfoTransHdr cartHdr);

	String findEntityNameByTypeNNo(String entityType, String entityNo);
}
