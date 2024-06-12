package com.ssm.llp.base.common.service;

import java.io.Serializable;
import java.util.Collection;

import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;

public interface BaseService<T, ID extends Serializable> {

//	public static final String EZBIZ_TRANS_MANAGER="transactionManager";
	
	public abstract T findById(ID id);
		
	public abstract void insert(T entity);
	
	public abstract void update(T entity);

	public abstract void delete(T entity);
	

	/**
     * Find record(s) from target table by dynamic search criteria.
     *
     * @param sc Dynamic search criteria.
     *
     * @return Search result which is contain a list of target table data object.
     */
	public abstract SearchResult findByCriteria(SearchCriteria sc);
	
	public abstract Long findByCriteriaCount(SearchCriteria sc);
	
	/**
     * Find record(s) from target table by dynamic search criteria.
     *
     * @param sc Dynamic search criteria.
     *
     * @return Search result which is contain a list of target table data object.
     */
	public abstract SearchResult findByCriteria(MultiEntitySearchCriteria sc);
	
//	public abstract List<RefCode> getCodeType(String codeType);
	
//	public abstract List<RefCode> getCodeType(String codeType,String[] params);
	
	public abstract void insertAll(Collection<T> collection);
	
	public abstract void updateAll(Collection<T> collection);
}
