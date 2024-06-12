package com.ssm.llp.base.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
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
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.ezbiz.model.RobFormAOwner;

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
public abstract class SimpleBaseDaoImpl 
{
	private static final Logger logger = LoggerFactory.getLogger(SimpleBaseDaoImpl.class);

    /** Created by field constant */
    protected static final String CREATED_BY = "createBy";

    /** Created Date field constant */
    protected static final String CREATED_AT = "createDt";

    /** Update by field constant */
    protected static final String UPDATED_BY = "updateBy";

    /** Update date field constant */
    protected static final String UPDATED_AT = "updateDt";
    
    /** Database Alias Constant */
    protected static final String ALIAS = "alias1";
    
    public String dbType;
	
    @Autowired
	@Qualifier("sessionFactory")//default session factory for LLP
	private SessionFactory sessionFactory;
	
	/** TODO DOCUMENTTHIS */
    public HibernateTemplate hibernateTemplate;
    

//    @Autowired
//	@Qualifier("sessionFactoryUam")
//	private SessionFactory sessionFactoryUam;
	
	/** TODO DOCUMENTTHIS */
    public HibernateTemplate hibernateTemplateUam;

	@SuppressWarnings("unchecked")
	public SimpleBaseDaoImpl() {
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

	
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new SpringHibernateTemplate(sessionFactory);
	}
//	
//	public void setSessionFactoryUam(SessionFactory sessionFactory) {
//		this.sessionFactoryUam = sessionFactory;
//		this.hibernateTemplateUam = new SpringHibernateTemplate(sessionFactoryUam);
//	}

	public SessionFactory getSessionFactory(){
//		if(DbType.UAM.equals(getDbType())){
//			return sessionFactoryUam;
//		}
		return sessionFactory;
	}
	public HibernateTemplate getHibernateTemplate() {
		if(DbType.UAM.equals(getDbType())){
			return hibernateTemplateUam;
		}
		return hibernateTemplate;
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
	
	public AuditReader getAuditReader() {
		AuditReader reader = AuditReaderFactory.get(getSession());
		return reader;
	}
	
}