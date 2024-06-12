package com.ssm.llp.base.common.service;

import java.util.List;
import java.util.Map;

import com.ssm.llp.base.common.model.LlpParameters;

public interface LlpParametersService extends BaseService<LlpParameters, Long>{
	public List<LlpParameters> findByActiveNonActiveCodeType(String codeType);
	
	public List<LlpParameters> findByActiveCodeType(String codeType);
	
	public List<LlpParameters> findByActiveCodeType(String codeType, String orderBy);

	public String findByCodeTypeValue(String codeType, String code);

	public List<LlpParameters> findListTownByPostcode(String postcode, boolean isFilterAllowStateOnly);
	
	public List<LlpParameters> findAllCodeTypeAsParameters();
	
	public Map<String,String> findAllCodeTypeAsMap(String codeType);

	public LlpParameters findParameter(String codeType, String code);
	
	public List<LlpParameters> findByCodeTypeStatus(String codeType, String status);

	public List<LlpParameters> getFormTypeWithCompound();

	public void deleteAllByCodeType(String codeType);

	

//	String getMappingCode(String codeFrom, String codeTypeFrom, String codeTypeDesc);

	Map<String, String> findActiveCodeTypeAsMap(String codeType);

}
