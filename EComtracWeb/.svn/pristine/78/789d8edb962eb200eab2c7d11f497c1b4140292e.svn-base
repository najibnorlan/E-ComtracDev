package com.ssm.llp.base.common.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpParametersDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;

@Service
public class LlpParametersServiceImpl extends BaseServiceImpl<LlpParameters, Long> implements LlpParametersService, Serializable{
//	Hard to maintain in cluster environment
	private static Map<String, List<LlpParameters>> mapCodeType = new HashedMap();
	
	@Autowired
	private LlpParametersDao llpParametersDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return llpParametersDao;
	}

	@Override
	public List<LlpParameters> findByActiveNonActiveCodeType(String codeType) {
//		List<LlpParameters> listResult = mapCodeType.get(codeType);
//		if(listResult==null||listResult.size()==0){
		List<LlpParameters> listResult = llpParametersDao.findByActiveNonActiveCodeType(codeType);
//			mapCodeType.put(codeType, listResult);
//		}
		
		return listResult;
	}
	
	void clearActiveCacheMap (String codeType){
		try {
			mapCodeType.remove(codeType);
		} catch (Exception e) {
		}
	}
	
	List<LlpParameters> findActiveFromCacheMap (String codeType){
		List<LlpParameters> listResult = mapCodeType.get(codeType);

		if(listResult==null || listResult.size()==0){
			listResult = llpParametersDao.findByActiveCodeType(codeType);
			mapCodeType.put(codeType, listResult);
		}
		return listResult;
	}

	@Override
	public String findByCodeTypeValue(String codeType, String code) {
		
		if(StringUtils.isBlank(code)){
			return code;
		}
		LlpParameters llpParameters = findParameter(codeType, code);
		if(llpParameters!=null){
			return llpParameters.getCodeDesc();
		}
		return null;
	}
	
//	@Override
//	public String getMappingCode (String codeFrom , String codeTypeFrom, String codeTypeDesc) {
//		if(StringUtils.isBlank(codeFrom)){
//			return codeFrom;
//		}
//		Map<String, String> mapFrom = findActiveCodeTypeAsMap(codeTypeFrom);
//		
//		
//		
//		Map<String, String> mapDesc = findActiveCodeTypeAsMap(codeTypeFrom);
//		
//		for (int i = 0; i < llpParametersList.size(); i++) {
//			if( codeDesc.toUpperCase().indexOf(llpParametersList.get(i).getCodeDesc()) !=-1 ){
//				return llpParametersList.get(i).getCode();
//			}
//		}
//		return null;
//	}

	@Override
	public List findAllCodeTypeAsParameters() {
		return llpParametersDao.findAllCodeTypeAsParameters();
	}

	@Override
	public LlpParameters findParameter(String codeType, String code) {
		
		List<LlpParameters> listParameter = findActiveFromCacheMap(codeType);
		for (int i = 0; i < listParameter.size(); i++) {
			if(code.equals(listParameter.get(i).getCode())){
				return listParameter.get(i);
			}
		}
		return null;
//		SearchCriteria sc = new SearchCriteria("codeType", SearchCriteria.EQUAL,codeType);
//		sc = sc.andIfNotNull("code", SearchCriteria.EQUAL, code);
//		sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.PARAMETER_STATUS_active);
//		List<LlpParameters> listParameter = findByCriteria(sc).getList();
//		if(listParameter.size()>0){
//			return listParameter.get(0);
//		}
//		return null;
	}

	@Override
	public List<LlpParameters> findByActiveCodeType(String codeType) {
		List<LlpParameters> listResult = findActiveFromCacheMap(codeType);
		return listResult;
	}

	@Override
	public Map<String, String> findAllCodeTypeAsMap(String codeType) {
		List<LlpParameters> listResult = findActiveFromCacheMap(codeType);
		Map map = new HashedMap();
		for (int i = 0; i < listResult.size(); i++) {
			map.put(listResult.get(i).getCode(), listResult.get(i).getCodeDesc());
		}
		
		return map;
	}
	
	@Override
	public Map<String, String> findActiveCodeTypeAsMap(String codeType) {
		List<LlpParameters> listResult = findActiveFromCacheMap(codeType);
		Map map = new HashedMap();
		for (int i = 0; i < listResult.size(); i++) {
			map.put(listResult.get(i).getCode(), listResult.get(i).getCodeDesc());
		}
		
		return map;
	}
	
	@Override
	public List<LlpParameters> findByCodeTypeStatus(String codeType, String status) {
		
		SearchCriteria sc = new SearchCriteria("codeType", SearchCriteria.EQUAL, codeType);
		sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, status);
		sc.addOrderBy("idParameter", SearchCriteria.ASC);
		
		List<LlpParameters> parameters = findByCriteria(sc).getList();
		return parameters;
	}
	
	@Override
	public List<LlpParameters> getFormTypeWithCompound(){
		
		LlpParameters parameterCmp = new LlpParameters();
		parameterCmp.setCode("CMP");
		parameterCmp.setCodeDesc(Parameter.COMPOUND);
		parameterCmp.setCodeType(Parameter.ROB_FORM_TYPE);
		List<LlpParameters> formList = findActiveFromCacheMap(Parameter.ROB_FORM_TYPE);
		formList.add(parameterCmp);
		
		return formList;
	}

	@Override
	public void deleteAllByCodeType(String codeType) {
		clearActiveCacheMap(codeType);
		llpParametersDao.deleteAllByCodeType(codeType);
	}

	@Override
	public List<LlpParameters> findByActiveCodeType(String codeType, String orderBy) {
		return llpParametersDao.findByActiveCodeType(codeType, orderBy);
	}
	
	@Override
	public void insert(LlpParameters entity) {
		clearActiveCacheMap(entity.getCodeType());
		super.insert(entity);
	}
	
	@Override
	public void update(LlpParameters entity) {
		clearActiveCacheMap(entity.getCodeType());
		super.update(entity);
	}
	
	@Override
	public void delete(LlpParameters entity) {
		clearActiveCacheMap(entity.getCodeType());
		super.delete(entity);
	}

	@Override
	public List<LlpParameters> findListTownByPostcode(String postcode , boolean isFilterAllowStateOnly) {
		Map<String, String> mapAllowState = findActiveCodeTypeAsMap(Parameter.ROB_ALLOW_REG_STATE);
		List<LlpParameters> listParameter = findByActiveCodeType(Parameter.POSTCODE_MAPPING);
		List<LlpParameters> listTown = new ArrayList<LlpParameters>();
		if(StringUtils.isNotBlank(postcode)&&postcode.length()==5){
			for (int i = 0; i < listParameter.size(); i++) {
				String postTownState = listParameter.get(i).getCode();
				if(postTownState.startsWith(postcode)){
					if(isFilterAllowStateOnly){
						String state = StringUtils.split(postTownState,":")[2];
						if(mapAllowState.containsKey(state)){
							listTown.add(listParameter.get(i));
						}
					}else{
						listTown.add(listParameter.get(i));
					}
				}
			}
		}
		return listTown;
	}
	
	
}
