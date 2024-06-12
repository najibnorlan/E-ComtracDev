/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.db;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.id.UUIDHexGenerator;

import org.hibernate.impl.SessionImpl;

import org.hibernate.persister.entity.EntityPersister;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import java.io.Serializable;
import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used to log the changes made to the object
 *
 * @author hhf
 * @version 1.0
 */
public class HibernateInterceptor implements Interceptor, BeanFactoryAware {
    /** Contains String of field name which should be ignored. */
    private Set ignoredField;

    /** Contains String of field name per class which should be ignored. */
    private Map ignoredClassField;

    /** Bean aware factory */
    private BeanFactory beanFactory;

    /** Named of bean aware session factory */
    private String factoryName;

    /** Set of Hibernate Interceptor Listener. Spring Injection */
    private List hibInterceptorListeners;

    /**
     * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onLoad(
        Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
        throws CallbackException {
        return false;
    }

    /**
     * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onFlushDirty(
        Object entity, Serializable id, Object[] currentState, Object[] previousState,
        String[] propertyNames, Type[] types) throws CallbackException {
//    	System.out.println("onFlushDirty");
        /*if (entity instanceof Auditable) {
                try {
                    SessionFactory sessionFactory = (SessionFactory) beanFactory.getBean(
                            factoryName);

                    HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);

                    SessionImpl session = (SessionImpl) getSession(
                            hibernateTemplate, hibernateTemplate.isAllowCreate());
                    EntityPersister persister = session.getEntityPersister(
                            session.getEntityName(entity), entity);

                    int[] changedField = persister.findDirty(
                            currentState, previousState, null, session);

                    if ((changedField == null) || (changedField.length <= 0)) {
                        return false;
                    }

                    List fPropertyNames = new ArrayList();
                    List fCurrentState = new ArrayList();
                    List fPreviousState = new ArrayList();
                    List fType = new ArrayList();

                    filterProperties(
                        entity, propertyNames, currentState, previousState, types, changedField,
                        fPropertyNames, fCurrentState, fPreviousState, fType);

                    if (fPropertyNames.isEmpty()) {
                        return false;
                    }

                    UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
                    String logId = (String) uuidGenerator.generate(null, null);

                    for (Iterator iter = hibInterceptorListeners.iterator(); iter.hasNext();) {
                        HibernateInterceptorListerner hibListener = (HibernateInterceptorListerner) iter.next();
                        hibListener.onUpdate(
                            entity, (String[]) fPropertyNames.toArray(new String[0]),
                            fPreviousState.toArray(), fCurrentState.toArray(),
                            (Type[]) fType.toArray(new Type[0]), id, logId, hibernateTemplate);
                    }
                } catch (HibernateException e) {
                    throw new CallbackException(e);
                }
        }*/

        return true;
    }
    
    protected void includeCompositeId(
        Serializable id, String[] propertyNames, Type[] types, Object[] state, List list1,
        List list2, List list3) {
//    	System.out.println("includeCompositeId");
    	/*if(id instanceof Auditable) {
    		Map map2 = new HashMap();
    		// if exception throws when try to get propertyType from the Id, just return and do nothings
	        try {
	            map2 = PropertyUtil.getPropertyType(id, false, false);
	        } catch(Exception e) {
	        	return;
	        }
	
	        Iterator keys = map2.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();

                if ("primaryKey".equals(key) || "auditDataGroup".equals(key) || "objectValue".equals(key)) {
                    continue;
                }

                String value2 = (String) map2.get(key);

                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value2)) {
                    Type type = getHibernateType(value2);

                    if (type != null) {
                    	// try to get the propertyValue based on the key, 
                    	// if exception thrown just continue to next record in the map
                    	// else add the key and type to another 2 list.
                    	// purpose is to to make sure the 3 list have same record
                        try {
                        	Object o = PropertyUtils.getSimpleProperty(id, key); 
                        	list1.add(key);
                        	list2.add(type);
                        	list3.add(o);
                        } catch(Exception e) {
                        	continue;
                        } 
                    }
                }
            }
    	}*/
    }
    
    private Map hibernateTypeMap = new HashMap();
    
    protected Type getHibernateType(final String value2) {
        Type type = (Type) hibernateTypeMap.get(value2);

        if (type != null) {
            return type;
        } else {
            if (String.class.getName().equals(value2)) {
                type = new StringType();
            } else if (Short.class.getName().equals(value2) || "short".equals(value2)) {
                type = new ShortType();
            } else if (Long.class.getName().equals(value2) || "long".equals(value2)) {
                type = new LongType();
            } else if (Integer.class.getName().equals(value2) || "int".equals(value2)) {
                type = new IntegerType();
            } else if (Timestamp.class.getName().equals(value2)) {
                type = new TimestampType();
            } else if (
                java.util.Date.class.getName().equals(value2) || java.sql.Date.class.getName().equals(value2)) {
                type = new DateType();
            } else if (BigDecimal.class.getName().equals(value2)) {
                type = new BigDecimalType();
            }

            if (type != null) {
                hibernateTypeMap.put(value2, type);

                return type;
            }

            return null;
        }
    }

    /**
     * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onSave(
        Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
        throws CallbackException {
//    	System.out.println("onsave");
        /*if (entity instanceof Auditable) {
            if (!(entity instanceof Archivable) || ((entity instanceof Archivable) && !((Archivable) entity).getIsArchive())) {
                try {
                    SessionFactory sessionFactory = (SessionFactory) beanFactory.getBean(
                            factoryName);

                    HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
                    List fPropertyNames = new ArrayList();
                    List fCurrentState = new ArrayList();
                    List fType = new ArrayList();
                    
                    /////////////////////////// bug fixed //////////////////////////
                    // +- 28062007 tsw
                    // handle the composite key if the key is extend from Auditable interface
                    List list1 = new ArrayList();
        	        List list2 = new ArrayList();
        	        List list3 = new ArrayList();
        	        includeCompositeId(id, propertyNames, types, state, list1, list2, list3);
        	
        	        String[] propertyNamesNew = new String[propertyNames.length + list1.size()];
        	        System.arraycopy(propertyNames, 0, propertyNamesNew, 0, propertyNames.length);
        	        if(!list1.isEmpty()) {
        	        	String[] list1Array = (String[])list1.toArray(new String[0]);
        	        	System.arraycopy(list1Array, 0, propertyNamesNew, propertyNames.length, list1Array.length);
        	        }
        	
        	        Type[] typesNew = new Type[types.length + list2.size()];
        	        System.arraycopy(types, 0, typesNew, 0, types.length);
        	        if(!list2.isEmpty()) {
        	        	Type[] list2Array = (Type[])list2.toArray(new Type[0]);
        	        	System.arraycopy(list2Array, 0, typesNew, types.length, list2Array.length);
        	        }
        	
        	        Object[] stateNew = new Object[state.length + list3.size()];
        	        System.arraycopy(state, 0, stateNew, 0, state.length);
        	        if(!list3.isEmpty()) {
	        	        Object[] list3Array = (Object[])list3.toArray(new Object[0]);
	        	        System.arraycopy(list3Array, 0, stateNew, state.length, list3Array.length);
        	        }
        	        
        	        filterProperties(
                            entity, propertyNamesNew, stateNew, null, typesNew, null, fPropertyNames,
                            fCurrentState, null, fType);
                    ////////////////////////////////////////////////////////////////

//                    filterProperties(
//                        entity, propertyNames, state, null, types, null, fPropertyNames,
//                        fCurrentState, null, fType);

                    if ((fPropertyNames != null) && (fPropertyNames.size() > 0)) {
                        UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
                        String logId = (String) uuidGenerator.generate(null, null);

                        for (Iterator iter = hibInterceptorListeners.iterator(); iter.hasNext();) {
                            HibernateInterceptorListerner hibListener = (HibernateInterceptorListerner) iter.next();
                            hibListener.onInsert(
                                entity, (String[]) fPropertyNames.toArray(new String[0]),
                                fCurrentState.toArray(), (Type[]) fType.toArray(new Type[0]), id,
                                logId, hibernateTemplate);
                        }
                    }
                } catch (HibernateException e) {
                    throw new CallbackException(e);
                }
            }
        }*/

        return true;
    }

    /**
     * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public void onDelete(
        Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
        throws CallbackException {
//    	System.out.println("onDelete");
    	boolean isBypassAuditLog = false;
    	/*if(UserEnvironmentHelper.getUserenvironment()!=null && UserEnvironmentHelper.getUserenvironment().getAttribute("IS_BYPASS_AUDIT_LOG")!=null){
    		isBypassAuditLog = ((Boolean)UserEnvironmentHelper.getUserenvironment().getAttribute("IS_BYPASS_AUDIT_LOG")).booleanValue();
    	}
        if (entity instanceof Auditable && !isBypassAuditLog) {
            if (!(entity instanceof Archivable) || ((entity instanceof Archivable) && !((Archivable) entity).getIsArchive())) {
                try {
                    SessionFactory sessionFactory = (SessionFactory) beanFactory.getBean(
                            factoryName);

                    HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
                    List fPropertyNames = new ArrayList();
                    List fPreviousState = new ArrayList();
                    List fType = new ArrayList();
                    
                    /////////////////////////// bug fixed //////////////////////////
                    // +- 28062007 tsw
                    // handle the composite key if the key is extend from Auditable interface
                    List list1 = new ArrayList();
        	        List list2 = new ArrayList();
        	        List list3 = new ArrayList();
        	        includeCompositeId(id, propertyNames, types, state, list1, list2, list3);
        	
        	        String[] propertyNamesNew = new String[propertyNames.length + list1.size()];
        	        System.arraycopy(propertyNames, 0, propertyNamesNew, 0, propertyNames.length);
        	        if(!list1.isEmpty()) {
        	        	String[] list1Array = (String[])list1.toArray(new String[0]);
        	        	System.arraycopy(list1Array, 0, propertyNamesNew, propertyNames.length, list1Array.length);
        	        }
        	
        	        Type[] typesNew = new Type[types.length + list2.size()];
        	        System.arraycopy(types, 0, typesNew, 0, types.length);
        	        if(!list2.isEmpty()) {
        	        	Type[] list2Array = (Type[])list2.toArray(new Type[0]);
        	        	System.arraycopy(list2Array, 0, typesNew, types.length, list2Array.length);
        	        }
        	
        	        Object[] stateNew = new Object[state.length + list3.size()];
        	        System.arraycopy(state, 0, stateNew, 0, state.length);
        	        if(!list3.isEmpty()) {
	        	        Object[] list3Array = (Object[])list3.toArray(new Object[0]);
	        	        System.arraycopy(list3Array, 0, stateNew, state.length, list3Array.length);
        	        }
        	        
        	        filterProperties(
                            entity, propertyNamesNew, null, stateNew, typesNew, null, fPropertyNames, null,
                            fPreviousState, fType);
                    ////////////////////////////////////////////////////////////////
                    
//                    filterProperties(
//                        entity, propertyNames, null, state, types, null, fPropertyNames, null,
//                        fPreviousState, fType);

                    if ((fPropertyNames != null) && (fPropertyNames.size() > 0)) {
                        UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
                        String logId = (String) uuidGenerator.generate(null, null);

                        for (Iterator iter = hibInterceptorListeners.iterator(); iter.hasNext();) {
                            HibernateInterceptorListerner hibListener = (HibernateInterceptorListerner) iter.next();
                            hibListener.onDelete(
                                entity, (String[]) fPropertyNames.toArray(new String[0]),
                                fPreviousState.toArray(), (Type[]) fType.toArray(new Type[0]), id,
                                logId, hibernateTemplate);
                        }
                    }
                } catch (HibernateException e) {
                    throw new CallbackException(e);
                }
            }
        }*/
    }

    /**
     * @see org.hibernate.Interceptor#preFlush(java.util.Iterator)
     */
    public void preFlush(Iterator entities) throws CallbackException {
        //        System.out.println("Post flush");
    }

    /**
     * @see org.hibernate.Interceptor#postFlush(java.util.Iterator)
     */
    public void postFlush(Iterator entities) throws CallbackException {
        //        System.out.println("Post flush");
    }

    /**
     * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public int[] findDirty(
        Object entity, Serializable id, Object[] currentState, Object[] previousState,
        String[] propertyNames, Type[] types) {
//    	System.out.println("findDirty");
        return null;
    }

    /**
     * Spring injection
     *
     * @param set Set
     */
    public void setIgnoredField(Set set) {
        ignoredField = set;
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware
     *      #setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory bf) {
        beanFactory = bf;
    }

    /**
     * Spring injection
     *
     * @param string String
     */
    public void setFactoryName(String string) {
        factoryName = string;
    }

    /**
     * Copied from HibernateSupport class
     *
     * @param template Hibernatetemplate
     * @param allowCreate if a new Session should be created if no thread-bound found
     *
     * @return the Hibernate Session
     *
     * @see org.springframework.orm.hibernate3.SessionFactoryUtils#getSession( SessionFactory,
     *      boolean)
     */
    protected Session getSession(HibernateTemplate template, boolean allowCreate) {
        return ((!allowCreate)
        ? SessionFactoryUtils.getSession(template.getSessionFactory(), false)
        : SessionFactoryUtils.getSession(
            template.getSessionFactory(), template.getEntityInterceptor(),
            template.getJdbcExceptionTranslator()));
    }

    /**
     * Spring injection
     *
     * @param list map
     */
    public void setIgnoredClassField(List list) {
        ignoredClassField = new HashMap();

        if (list != null) {
            Iterator iter = list.iterator();

            while (iter.hasNext()) {
                ignoredClassField.putAll((Map) iter.next());
            }
        }
    }

    /**
     * Check whether this field is ignored field
     *
     * @param objectName String
     * @param fieldName String
     *
     * @return boolean
     */
    private boolean isIgnoredClassField(String objectName, String fieldName) {
        if (ignoredClassField == null) {
            return false;
        }

        Set fields = (Set) ignoredClassField.get(objectName);

        if (fields != null) {
            return fields.contains(fieldName);
        }

        return false;
    }

    /**
     * Filter out ignored fields, ignored class fields and unchanged fields
     *
     * @param entity Entity object.
     * @param propertyNames Array of string.
     * @param newValue Array of object.
     * @param oldValue Array of object.
     * @param type Array of type.
     * @param changedField Array of integer.
     * @param fPropertyNames Set.
     * @param fNewValue Set.
     * @param fOldValue Set.
     * @param fType Set.
     */
    private void filterProperties(
        Object entity, String[] propertyNames, Object[] newValue, Object[] oldValue, Type[] type,
        int[] changedField, List fPropertyNames, List fNewValue, List fOldValue, List fType) {
        int change = 0;
        //boolean isHeaderSaved = false;
        
        for (int i = 0; i < propertyNames.length; i++) {
            if (changedField != null) {
                if (changedField.length > change) {
                    if (changedField[change] != i) {
                        continue;
                    }
                } else {
                    continue;
                }
            }

            change++;

            if (ignoredField.contains(propertyNames[i])) {
                continue;
            }

            if (isIgnoredClassField(entity.getClass().getName(), propertyNames[i])) {
                continue;
            }

            String _newValue = "";
            String _oldValue = "";
            if (newValue != null && newValue[i] !=null) {
            	_newValue = newValue[i].toString();
            	
            	if(newValue[i] instanceof Collections){
            		continue;
            	}
            	/*if(newValue[i] instanceof Auditable){
            		_newValue = ((Auditable)newValue[i]).getPrimaryKey();
            	}*/
            	if(newValue[i] instanceof Double){
            		_newValue = (new BigDecimal( ((Double)newValue[i]).doubleValue() )).toString();
            	}
            }
            if (oldValue!=null && oldValue[i] !=null) {
            	_oldValue = oldValue[i].toString();
            	/*if(oldValue[i] instanceof Auditable){
            		_oldValue = ((Auditable)oldValue[i]).getPrimaryKey();
            	}*/
            	if(oldValue[i] instanceof Double){
            		_oldValue = (new BigDecimal( ((Double)oldValue[i]).doubleValue() )).toString();
            	}
            }
            
            if (_newValue.equals(_oldValue)) {
            	continue;
            }
            
            
//            if (
//                (newValue != null) && (oldValue != null) && (newValue[i] != null) &&
//                    (oldValue[i] != null) && newValue[i] instanceof String &&
//                    oldValue[i] instanceof String) {
//                if (((String) newValue[i]).trim().equals(((String) oldValue[i]).trim())) {
//                    continue;
//                }
//            }

            fPropertyNames.add(propertyNames[i]);

            if ((fNewValue != null) && (newValue != null)) {
                fNewValue.add(newValue[i]);
            }

            if ((fOldValue != null) && (oldValue != null)) {
                fOldValue.add(oldValue[i]);
            }

            fType.add(type[i]);
        }
    }

    /**
     * TSpring Injection
     *
     * @param list Set
     */
    public void setHibInterceptorListeners(List list) {
        hibInterceptorListeners = list;
    }

    /**
     * @see org.hibernate.Interceptor#isTransient(java.lang.Object)
     */
    public Boolean isTransient(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.hibernate.Interceptor#instantiate(java.lang.String, org.hibernate.EntityMode,
     *      java.io.Serializable)
     */
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id)
        throws CallbackException {
        // Auto-generated method stub
        return null;
    }

    /**
     * @see org.hibernate.Interceptor#getEntityName(java.lang.Object)
     */
    public String getEntityName(Object object) throws CallbackException {
        // Auto-generated method stub
        return null;
    }

    /**
     * @see org.hibernate.Interceptor#getEntity(java.lang.String, java.io.Serializable)
     */
    public Object getEntity(String entityName, Serializable id)
        throws CallbackException {
        // Auto-generated method stub
        return null;
    }

    /**
     * @see org.hibernate.Interceptor#afterTransactionBegin(org.hibernate.Transaction)
     */
    public void afterTransactionBegin(Transaction tx) {
        // Auto-generated method stub
    }

    /**
     * @see org.hibernate.Interceptor#beforeTransactionCompletion(org.hibernate.Transaction)
     */
    public void beforeTransactionCompletion(Transaction tx) {
        // Auto-generated method stub
    }

    /**
     * @see org.hibernate.Interceptor#afterTransactionCompletion(org.hibernate.Transaction)
     */
    public void afterTransactionCompletion(Transaction tx) {
        // Auto-generated method stub
    }

	public void onCollectionRecreate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	public void onCollectionRemove(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	public void onCollectionUpdate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	public String onPrepareStatement(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
}
