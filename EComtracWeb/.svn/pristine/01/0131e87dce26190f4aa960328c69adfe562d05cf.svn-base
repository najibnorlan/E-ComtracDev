package com.ssm.llp.base.common.dao;

import java.util.List;

import com.ssm.llp.base.common.model.LlpParameters;

public interface LlpParametersDao extends BaseDao<LlpParameters, Long>{

	List<LlpParameters> findByActiveNonActiveCodeType(String codeType);

	List<LlpParameters> findAllCodeTypeAsParameters();

	List<LlpParameters> findByActiveCodeType(String codeType);

	void deleteAllByCodeType(String codeType);

	List<LlpParameters> findByActiveCodeType(String codeType, String orderBy);

}
