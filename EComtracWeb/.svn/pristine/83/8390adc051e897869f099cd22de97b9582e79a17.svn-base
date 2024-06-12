package com.ssm.llp.mod1.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.impl.NameSearchResult;

public interface LlpReservedNameService extends BaseService<LlpReservedName, String> {

	LlpReservedName findLatestReserveNameByLlpNo(String llpNo);

	public String cleanName(String applyLlpName);
	
	public void updateAfterPay(LlpReservedName llpReservedName,String paymentTransId);
	
	public void sendEmailReserverNameSuccess(LlpReservedName llpReservedName);

	List<SSMException> validateNameSearch(String applyLlpName);
	
	public String generateLLPNoForConversion(String refNo, String conversionType, String regType, String profBodyType)throws SSMException;
	
	public String generateLLPNo(String reserveNameRefNo, String regType, String profBodyType)throws SSMException;
	
	public void updateAllNameSearch();
}
