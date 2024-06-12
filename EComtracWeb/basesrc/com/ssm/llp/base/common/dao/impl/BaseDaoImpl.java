package com.ssm.llp.base.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.ssm.llp.base.common.code.DbType;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.MultiEntitySearchCriteria;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.db.SpringHibernateTemplate;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

/**
 * Implementation of {@code BaseDao}, providing concrete methods for persisting hibernate objects to the database.
 * <p>
 * This class also provide two important static methods {@code getLoginUser()} and {@code setLoginUser()},
 * which stores the login user in a {@code ThreadLocal} variable. These two methods provide the mechanism for
 * implementing a unified security infrastructure between the web tier and the biz tiers.
 * <p>
 * Typically a Stripes interceptor or a servlet filter will be responsible for calling the {@code setLoginUser()} method.  
 *
 * @param <T> the type of the entity class
 * @param <ID> the type of Id field
 * 
 * @author yee
 */
public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID>
{
	private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	private Class<T> persistentClass;
	
//    /** Created by field constant */
//    protected static final String CREATED_BY = "createBy";
//
//    /** Created Date field constant */
//    protected static final String CREATED_AT = "createDt";
//
//    /** Update by field constant */
//    protected static final String UPDATED_BY = "updateBy";
//
//    /** Update date field constant */
//    protected static final String UPDATED_AT = "updateDt";
    
    /** Database Alias Constant */
    protected static final String ALIAS = "alias1";
    
    public String dbType;
	
    @Autowired
	@Qualifier("sessionFactory")//default session factory for LLP
	private SessionFactory sessionFactory;
	
	/** TODO DOCUMENTTHIS */
    public HibernateTemplate hibernateTemplate;
    
//	@Autowired
//	@Qualifier("sessionFactoryRoc")
//	private SessionFactory sessionFactoryRoc;
	
//	/** TODO DOCUMENTTHIS */
//    public HibernateTemplate hibernateTemplateRoc;
//    
//    @Autowired
//	@Qualifier("sessionFactoryRob")
//	private SessionFactory sessionFactoryRob;

//	/** TODO DOCUMENTTHIS */
//    public HibernateTemplate hibernateTemplateRob;
//    
//    @Autowired
//	@Qualifier("sessionFactoryUam")
//	private SessionFactory sessionFactoryUam;
	
	/** TODO DOCUMENTTHIS */
    public HibernateTemplate hibernateTemplateUam;
//    
//    @Autowired
//	@Qualifier("sessionFactoryCcs")
//	private SessionFactory sessionFactoryCcs;
//	
//	/** TODO DOCUMENTTHIS */
//    public HibernateTemplate hibernateTemplateCcs;
    

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public Session getSession()
	{
		Session session=null ;
		try {
			session =  getSessionFactory().getCurrentSession();
		} catch (Exception e) {
			if(session==null ){
				session = getSessionFactory().openSession();
			}
		}
		
		return session;
	}

	
	/**
	 * Convenient method for retrieving the principal of the authenticated user
	 * 
	 * @return the login user
	 */
	/*public static ILoginUser getLoginUser()
	{
		if (thread_loginUser.get() == null) {
	        if (SecurityContextHolder.getContext().getAuthentication() != null) {
	        	thread_loginUser.set(
	        			(ILoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	        }
        }
		return thread_loginUser.get();
	}
	
	public static void setLoginUser(ILoginUser loginUser)
	{
		thread_loginUser.set(loginUser);
	}*/

	
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new SpringHibernateTemplate(sessionFactory);
		
		
	}
//	public void setSessionFactoryRoc(SessionFactory sessionFactory) {
//		this.sessionFactoryRoc = sessionFactory;
//		this.hibernateTemplateRoc = new SpringHibernateTemplate(sessionFactoryRoc);
//	}
//	public void setSessionFactoryRob(SessionFactory sessionFactory) {
//		this.sessionFactoryRob = sessionFactory;
//		this.hibernateTemplateRob = new SpringHibernateTemplate(sessionFactoryRob);
//	}
	
//	public void setSessionFactoryUam(SessionFactory sessionFactory) {
//		this.sessionFactoryUam = sessionFactory;
//		this.hibernateTemplateUam = new SpringHibernateTemplate(sessionFactoryUam);
//	}
//	public void setSessionFactoryCcs(SessionFactory sessionFactory) {
//		this.sessionFactoryCcs = sessionFactory;
//		this.hibernateTemplateCcs = new SpringHibernateTemplate(sessionFactoryCcs);
//	}

	public SessionFactory getSessionFactory(){
//		if(DbType.ROB.equals(getDbType())){
//			return sessionFactoryRob;
//		}
//		if(DbType.ROC.equals(getDbType())){
//			return sessionFactoryRoc;
//		}
//		if(DbType.UAM.equals(getDbType())){
//			return sessionFactoryUam;
//		}
//		if(DbType.CCS.equals(getDbType())){
//			return sessionFactoryCcs;
//		}
		return sessionFactory;
	}
	public HibernateTemplate getHibernateTemplate() {
//		if(DbType.ROB.equals(getDbType())){
//			return hibernateTemplateRob;
//		}
//		if(DbType.ROC.equals(getDbType())){
//			return hibernateTemplateRoc;
//		}
		if(DbType.UAM.equals(getDbType())){
			return hibernateTemplateUam;
		}
//		if(DbType.CCS.equals(getDbType())){
//			return hibernateTemplateCcs;
//		}
		return hibernateTemplate;
	}

	

	@Override
	public T findById(ID id) {
		try {
            T t = (T) getHibernateTemplate().get(getPersistentClass(), id);
//            getHibernateTemplate().initialize(t);
            return t;
        } catch (org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException e) {
            return null;
        }
	}

	@Override
	public void insert(T entity) {
		getHibernateTemplate().save(entity);
	}

	@Override
	public void update(T entity) {
		getHibernateTemplate().update(entity);
		
	}
	
	@Override
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}
	
	
    public void deleteAll(Collection<T> collection) {
        getHibernateTemplate().deleteAll(collection);
    }
    
    @Override
    public void insertAll(Collection<T> collection) {
    	for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			T entity = (T) iterator.next();
//			setCreatedInfo(entity);
			insert(entity);
		}
//		getHibernateTemplate().saveOrUpdateAll(collection);
    }
	
    @Override
    public void updateAll(Collection<T> collection) {
    	for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			T entity = (T) iterator.next();
//			setCreatedInfo(entity);
			update(entity);
		}
//		getHibernateTemplate().saveOrUpdateAll(collection);
    }
    
    /**
     * Search database by criteria template
     *
     * @param criteria SearchCriteria
     *
     * @return SearchResult SearchResult
     */
    public SearchResult findByCriteria(SearchCriteria criteria) {
        return findByCriteria(criteria, getPersistentClass().getName());
    }
    
   
    
    /**
     * Search database by criteria template
     *
     * @param criteria SearchCriteria
     * @param className String
     *
     * @return SearchResult SearchResult
     */
    public SearchResult findByCriteria(SearchCriteria criteria, String className) {
        List parameters = new ArrayList();        

        String criteriaStr = scToString(criteria, parameters, ALIAS);

        return findByCriteria(
            "count(*)", "", className + " " + ALIAS, criteriaStr, getOrderBySql(criteria, ALIAS),
            parameters, criteria.getStartAtIndex(), criteria.getMaxRecord());
    }
    
    /**
     * Search database by criteria template
     *
     * @param criteria SearchCriteria
     *
     * @return SearchResult SearchResult
     */
    public Long findByCriteriaCount(SearchCriteria criteria) {
    	List parameters = new ArrayList();   
        String criteriaStr = scToString(criteria, parameters, ALIAS);
        String className = getPersistentClass().getName();
        
        String selectCountSql = "count(*)";
        String fromSql = className + " " + ALIAS;
        StringBuffer countSql = new StringBuffer();

        countSql.append("select ");
        countSql.append(selectCountSql);
        countSql.append(" from ");
        countSql.append(fromSql);
        
        if (StringUtils.isNotBlank(criteriaStr)) {
            countSql.append(" where ");
            countSql.append(criteriaStr);
        }
        
        List<T> list = null;
        Long count = new Long(0);

        try {
            list = findFromCache(countSql.toString(), parameters.toArray()); //list dataview mula2 count kat sini.. 

            if ((list != null) && !list.isEmpty()) {
                count = (Long) list.get(0);
            }
        } catch (DataAccessException e) {
            throw e;
        }
        return count;
    }
    /**
     * Find the SearchResult with the given count sql and select sql
     *
     * @param selectCountSql select count sql
     * @param selectSql selected fields
     * @param fromSql from sql
     * @param criteriaStr where sql
     * @param orderBySql order by sql
     * @param parameters parameters list
     * @param startAtIndex returned result start index
     * @param maxRecord retured result max number of records
     *
     * @return TODO DOCUMENTTHIS
     */
    private SearchResult findByCriteria(
        String selectCountSql, String selectSql, String fromSql, String criteriaStr,
        String orderBySql, List parameters, int startAtIndex, int maxRecord) {
    	SearchResult sr = new SearchResult();
        StringBuffer countSql = new StringBuffer();
        StringBuffer fullSql = new StringBuffer();

        countSql.append("select ");
        countSql.append(selectCountSql);
        countSql.append(" from ");
        countSql.append(fromSql);

        if (StringUtils.isNotBlank(selectSql)) {
            fullSql.append("select ");
            fullSql.append(selectSql);
        }

        fullSql.append(" from ");
        fullSql.append(fromSql);

        if (StringUtils.isNotBlank(criteriaStr)) {
            countSql.append(" where ");
            countSql.append(criteriaStr);
            fullSql.append(" where ");
            fullSql.append(criteriaStr);
        }

        if (StringUtils.isNotBlank(orderBySql)) {
            fullSql.append(" order by ");
            fullSql.append(orderBySql);
        }
        
        List<T> list = null;
        Long count = new Long(0);

        try {
            list = findFromCache(countSql.toString(), parameters.toArray());

            if ((list != null) && !list.isEmpty()) {
                count = (Long) list.get(0);
            }

            list = null;

            if (count.intValue() > 0) {
            	long start = System.currentTimeMillis();
                list = findFromCache(
                        fullSql.toString(), parameters.toArray(), maxRecord, startAtIndex);
                sr.setList(list);
                sr.setTotalRecordCount(count.intValue());
                
            }
        } catch (DataAccessException e) {
            throw e;
        }

        return sr;
    }
    
    /**
     * Return the order by sql
     *
     * @param criteria SearchCriteria
     * @param alias TODO DOCUMENTTHIS
     *
     * @return Order By Sql String
     */
    private String getOrderBySql(SearchCriteria criteria, String alias) {
        Map orderByMap = criteria.getOrderByMap();

        if ((orderByMap != null) && (orderByMap.size() > 0)) {
            StringBuffer orderBySql = new StringBuffer();

            for (Iterator iterator = orderByMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();

                if (StringUtils.isNotBlank(key)) {
                    if (StringUtils.isNotBlank(alias)) {
                        orderBySql.append(alias);
                        orderBySql.append(".");
                    }

                    orderBySql.append(key);
                    orderBySql.append(" ");
                    orderBySql.append(orderByMap.get(key));

                    orderBySql.append(", ");
                }
            }

            if (orderBySql.toString().endsWith(", ")) {
                orderBySql.delete(orderBySql.length() - 2, orderBySql.length());
            }

            return orderBySql.toString();
        }

        return "";
    }
    
    /**
     * Invoke scToString with default ALIAS name
     *
     * @param sc SearchCriteria
     * @param param List of a parameters
     *
     * @return
     */
    protected String scToString(SearchCriteria sc, List param) {
        return scToString(sc, param, ALIAS);
    }

    /**
     * Convert SearchCriteria objects to String
     *
     * @param sc SearchCriteria
     * @param param Parameters
     * @param alias TODO DOCUMENTTHIS
     *
     * @return String
     */
    protected String scToString(SearchCriteria sc, List param, String alias) {
        if ((sc.getFieldName() == null) && !sc.isNested()) {
            return null;
        }

        StringBuffer cond = new StringBuffer();

        if (sc.isNested()) {
            SearchCriteria sc1 = sc.getSearchCriteria1();
            SearchCriteria sc2 = sc.getSearchCriteria2();
            cond.append("(");
            cond.append(scToString(sc1, param, alias));
            cond.append(" ");
            cond.append(sc.getJointType());
            cond.append(" ");
            cond.append(scToString(sc2, param, alias));
            cond.append(") ");

            return cond.toString();
        } else {
            if (StringUtils.isNotBlank(sc.getFunction())) {
                cond.append(sc.getFunction());
                cond.append("(");

                if (StringUtils.isNotBlank(alias)) {
                    cond.append(alias);
                    cond.append(".");
                }

                cond.append(sc.getFieldName());
                cond.append(") ");
            } else {
                if (StringUtils.isNotBlank(alias)) {
                    cond.append(alias);
                    cond.append(".");
                }

                cond.append(sc.getFieldName());
                cond.append(" ");
            }

            if (sc.getOperator().equals(SearchCriteria.IN)) {
                cond.append(sc.getOperator());

                Object[] values = sc.getValues();
                cond.append(" (");

                for (int i = 0; i < values.length; i++) {
                    if (sc.isNumeric()) {
                        cond.append(values[i]);
                    } else {
                        cond.append("'" + values[i] + "'");
                    }

                    if (i != (values.length - 1)) {
                        cond.append(",");
                    }
                }

                cond.append(")");
            } else if (sc.getOperator().equals(SearchCriteria.NOT_IN)) {
                cond.append(sc.getOperator());

                Object[] values = sc.getValues();
                cond.append(" (");

                for (int i = 0; i < values.length; i++) {
                    if (sc.isNumeric()) {
                        cond.append(values[i]);
                    } else {
                        cond.append("'" + values[i] + "'");
                    }

                    if (i != (values.length - 1)) {
                        cond.append(",");
                    }
                }

                cond.append(")");
            } else if (sc.getOperator().equals(SearchCriteria.BETWEEN)) {
                cond.append(sc.getOperator());
                cond.append(" ? ");
                cond.append(SearchCriteria.AND);
                cond.append(" ? ");
                param.add(sc.getValues()[0]);
                param.add(sc.getValues()[1]);
            } else if (sc.getOperator().equals(SearchCriteria.IS_NULL)) {
                cond.append(" ");
                cond.append(SearchCriteria.IS_NULL);
            } else if (sc.getOperator().equals(SearchCriteria.IS_NOT_NULL)) {
                cond.append(" ");
                cond.append(SearchCriteria.IS_NOT_NULL);
            } else {
                cond.append(sc.getOperator());
                cond.append(" ");

                if (StringUtils.isNotBlank(sc.getValueFunction())) {
                    cond.append(sc.getFunction());
                    cond.append("(");
                    cond.append("?");
                    cond.append(") ");
                } else {
                    cond.append("?");
                }

                param.add(sc.getValue());
            }

            return cond.toString();
        }
    }
    
    /**
     * Implementation of cache find
     *
     * @param queryString String
     * @param values Object[]
     *
     * @return List
     */
    public List findFromCache(final String queryString, final Object[] values) {
        return findFromCache(queryString, values, 0, 0);
    }

    /**
     * Implementation of cache find
     *
     * @param queryString String
     * @param values Object[]
     * @param maxRecord int
     * @param startAtIndex int
     *
     * @return List
     */
    public List findFromCache(
        final String queryString, final Object[] values, final int maxRecord, final int startAtIndex) {
        return getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
//                	int prevIso = Connection.TRANSACTION_READ_COMMITTED;
//                	try {
//                		prevIso = session.connection().getTransactionIsolation();
//                    	session.connection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
                	
                    Query queryObject = session.createQuery(queryString);//.setCacheable(true);
                    
                    if (values != null) {
                        for (int i = 0; i < values.length; i++) {
                            Object value = values[i];
                            queryObject.setParameter(i, value);
                        }
                    }

                    if (maxRecord > 0) {
                        queryObject.setMaxResults(maxRecord);
                    }

                    if (startAtIndex > 0) {
                        queryObject.setFirstResult(startAtIndex);
                    }

                    List list = queryObject.list();
//                    try {
//                    	session.connection().setTransactionIsolation(prevIso);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
                    return list;
                }
            });
    }

    /**
     * Implementation of cache find
     *
     * @param queryString String
     *
     * @return List
     */
    public List findFromCache(final String queryString) {
        return findFromCache(queryString, null);
    }

    /**
     * Implementation of cache find
     *
     * @param queryString String
     * @param param Object
     *
     * @return List
     */
    public List findFromCache(final String queryString, Object param) {
        return findFromCache(queryString, new Object[] { param });
    }

	@Override
	public List<T> findAll() {
		SearchResult sr = findByCriteria(new SearchCriteria());
		return sr.getList();
	}
	
	/**
     * Search Result by multi entity criteria.
     *
     * @param meCriteria MultiEntity Criteria
     *
     * @return SearchResult
     */
    public SearchResult findByCriteria(MultiEntitySearchCriteria meCriteria) {
        List parameters = new ArrayList();
        SearchResult sr = new SearchResult();

        StringBuffer selectFieldSql = new StringBuffer();
        StringBuffer selectFieldCountSql = new StringBuffer();

        if (meCriteria.isDistinctReturnFields()) {
            selectFieldSql.append("distinct ");
        }

        if ((meCriteria.getReturnFields() != null) && !meCriteria.getReturnFields().isEmpty()) {
            selectFieldSql.append(StringUtils.join(meCriteria.getReturnFields().iterator(), ", "));
            
            if(meCriteria.getReturnFields().size() > 1) {
            	selectFieldCountSql.append("count(*)");
            } else {
				selectFieldCountSql.append("count(" + selectFieldSql + ")");
			}
        } else {
            selectFieldSql.append("*");
        	selectFieldCountSql.append("count(*)");
        }

        StringBuffer fromSql = new StringBuffer();
        fromSql.append(meCriteria.getMainClassName());
        fromSql.append(" ");
        fromSql.append(meCriteria.getMainAliasName());

        if (
            (meCriteria.getAssociatedClasses() != null) &&
                !meCriteria.getAssociatedClasses().isEmpty()) {
            fromSql.append(" ");
            fromSql.append(meCriteria.getAssociatedClassesAsString());
        }

        String criteriaStr = scToString(meCriteria.getSearchCriteria(), parameters, "");

        return findByCriteria(selectFieldCountSql.toString(), selectFieldSql.toString(), fromSql.toString(),
            criteriaStr, getOrderBySql(meCriteria.getSearchCriteria(), ""), parameters,
            meCriteria.getSearchCriteria().getStartAtIndex(),
            meCriteria.getSearchCriteria().getMaxRecord());
    }

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}
	/*
	public List findByUsingDirtyRead(String queryStr,Object... objParams){
		
		List  listResult = new ArrayList();
		
		if(objParams!=null && objParams.length ==1){
			if(objParams[0] instanceof List){
				objParams = ((List)(objParams[0])).toArray();
			}
		}
//		int prevIso = Connection.TRANSACTION_READ_COMMITTED;
		
		Session session = getSession();
//		try {
//        	session.connection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Query query = session.createQuery(queryStr);
		for (int i = 0; objParams!=null && i < objParams.length; i++) {
			query.setParameter(i, objParams[i]);
		}
		
		listResult = query.list();
		
//		try {
//        	session.connection().setTransactionIsolation(prevIso);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		return listResult;
	}
	*/
	
//	public List find(String queryString, final Object... values) {
//		
//		Query query = createQuery(queryString);	
//		System.out.println(query.getQueryString());
//		if (values != null) {
//			for (int i = 0; i < values.length; i++) {
//				System.out.println("SS:"+values[i]);
//				query.setParameter(i+1, values[i]);
//			}
//		}
//		return query.list();
//	}
//	
//	public Query createQuery(String queryString){
//		
//		if(queryString.indexOf("?")!=-1 && queryString.indexOf("?0")==-1) {
//			String refineQuery = "";
//			String queryPart[] = StringUtils.splitPreserveAllTokens(queryString, "?");
//			for (int i = 0; i < queryPart.length; i++) {
//				refineQuery+=queryPart[i];
//				if(i < queryPart.length-1) {
//					refineQuery+="?"+(i+1);
//				}
//			}
//			queryString = refineQuery;
//		}
//		
//		return getSession().createQuery(queryString);
//	}
	
	public AuditReader getAuditReader() {
		AuditReader reader = AuditReaderFactory.get(getSession());
		return reader;
	}
	
}