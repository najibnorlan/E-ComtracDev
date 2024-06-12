package com.ssm.llp.base.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;

/**
 * Interface defining the common methods for persisting a persistent object to the database.
 *
 * @param <T> the type of the entity class
 * @param <ID> the type of Id field
 * @author yee
 */
public interface BaseDao<T, ID extends Serializable>
{

	public abstract List<T> findAll();
	
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
	
	public abstract void deleteAll(Collection<T> collection);

	public abstract void insertAll(Collection<T> collection);

	public abstract void updateAll(Collection<T> collection);
}