package com.ssm.llp.base.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.ezbiz.model.RobRenewal;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID>{
	
	
	
//	@Autowired CodeProviderService codeProviderService;
	
	public abstract BaseDao getDefaultDao();
	
//	public String[] getListCodeType(){
//		return null;
//	}
	
	@Override
	@Transactional
	public void delete(T entity) {
		getDefaultDao().delete(entity);
//		refreshCodeType();
	}

	@Override
	public SearchResult findByCriteria(SearchCriteria sc) {
		return getDefaultDao().findByCriteria(sc);
	}
	
	@Override
	public SearchResult findByCriteria(MultiEntitySearchCriteria sc) {
		return getDefaultDao().findByCriteria(sc);
	}
	
	@Override
	public Long findByCriteriaCount(SearchCriteria sc) {
		return getDefaultDao().findByCriteriaCount(sc);
	}
	
	@Override
	public T findById(ID id) {
		return (T) getDefaultDao().findById(id);
	}

	@Override
	@Transactional
	public void insert(T entity) {
		getDefaultDao().insert(entity);
//		refreshCodeType();
	}
	
	@Override
	@Transactional
	public void update(T entity) {
		getDefaultDao().update(entity);
//		refreshCodeType();
	}
	
//	@Override
//	public List<RefCode> getCodeType(String codeType) {
//		return getCodeType(codeType, null);
//	}
//	
//	@Override
//	public List<RefCode> getCodeType(String codeType, String[] params) {
//		return null;
//	}
//	
//	
//	private void refreshCodeType(){
//		String serviceName = getClass().getSimpleName();
//		serviceName = serviceName.substring(0,serviceName.length() - "Impl".length());
//		String listCodeType[] = getListCodeType();
//		if(listCodeType!=null && listCodeType.length>0){
//			for (int i = 0; i < listCodeType.length; i++) {
//				codeProviderService.refreshCode(serviceName, listCodeType[i]);
//			}
//		}
//		
//	}
	
	@Override
	@Transactional
	public void insertAll(Collection<T> collection) {
		getDefaultDao().insertAll(collection);
	}
	

	@Override
	@Transactional
	public void updateAll(Collection<T> collection) {
		getDefaultDao().updateAll(collection);
	}
}
