/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;

public interface LlpSupplyInfoDtlService extends BaseService<LlpSupplyInfoDtl, Long>{

	List<LlpSupplyInfoDtl> findByHdrCode(String hdrCode);


	void updateUploadFile(long dtlId, String uploadType, byte[] uploadData);
}

