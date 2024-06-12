/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.db;



import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * overriden to handle the loading of object during update to log the before and after image
 *
 * @author hhf
 * @version 1.0
 */
public class SpringHibernateTemplate extends HibernateTemplate {

    /**
     * constructor
     */
    public SpringHibernateTemplate() {
        super();
    }

    /**
     * constructor
     *
     * @param sessionFactory session factory
     */
    public SpringHibernateTemplate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * constructor
     *
     * @param sessionFactory session factory
     * @param allowCreate true will create if session is null
     */
    public SpringHibernateTemplate(SessionFactory sessionFactory, boolean allowCreate) {
        super(sessionFactory, allowCreate);
    }

    /**
     * @see org.springframework.orm.hibernate3.HibernateOperations#update(java.lang.Object,
     *      org.hibernate.LockMode)
     */
    public void update(final Object entity, final LockMode lockMode) {
        execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
                    /*if (entity instanceof MltDescEnable) {
                        MltDescEnable mltDescEntity = (MltDescEnable) entity;

                        //TODO Replace with HQL bulk delete when Hibernate 3 Support bulk delete using composite key in where condition
                        List delList = session.createQuery(
                                "from MltDesc mlt where mlt.comp_id.objectName like :objectName and mlt.comp_id.priKey =:priKey")
                                              .setString(
                                "objectName", mltDescEntity.getObjectName() + "%")
                                              .setString("priKey", mltDescEntity.getPrimaryKey())
                                              .list();

                        if ((delList != null) && !delList.isEmpty()) {
                            for (Iterator iter = delList.iterator(); iter.hasNext();) {
                                session.delete(iter.next());
                            }
                        }

                        if (
                            (mltDescEntity.getMltDescData() != null) &&
                                !mltDescEntity.getMltDescData().isEmpty()) {
                            for (int i = 0; i < mltDescEntity.getMltDescData().size(); i++) {
                                session.save(mltDescEntity.getMltDescData().get(i));
                            }
                        }
                    }*/

                    /*if (entity instanceof Auditable) {
                        session.merge(entity);

                        //session.saveOrUpdateCopy(entity);
                    } else {*/
                        session.update(entity);
                    //}

                    session.lock(entity, lockMode);

                    return null;
                }
            });
    }

    /**
     * @see org.springframework.orm.hibernate3.HibernateOperations#save(java.lang.Object)
     */
    public Serializable save(final Object entity) throws DataAccessException {
        return (Serializable) execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
                    Serializable saveResult = session.save(entity);

                    /*if (entity instanceof MltDescEnable) {
                        MltDescEnable mltDescEntity = (MltDescEnable) entity;

                        if (
                            (mltDescEntity.getMltDescData() != null) &&
                                !mltDescEntity.getMltDescData().isEmpty()) {
                            Object mltDescObj = null;
                            Method method = null;
                            Object comp_id = null;

                            for (int i = 0; i < mltDescEntity.getMltDescData().size(); i++) {
                                mltDescObj = mltDescEntity.getMltDescData().get(i);

                                try {
                                    method = mltDescObj.getClass().getMethod("getComp_id", null);
                                    comp_id = method.invoke(mltDescObj, null);
                                    method = comp_id.getClass().getMethod(
                                            "setPriKey", new Class[] { String.class });
                                    method.invoke(
                                        comp_id, new Object[] { mltDescEntity.getPrimaryKey() });
                                } catch (Exception e) {
                                    if (log.isDebugEnabled()) {
                                        log.debug(e);
                                    }
                                }

                                session.save(mltDescEntity.getMltDescData().get(i));
                            }
                        }
                    }*/

                    return saveResult;
                }
            });
    }

    /**
     * @see org.springframework.orm.hibernate3.HibernateOperations#update(java.lang.Object)
     */
    public void update(final Object entity) {
        execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
                    /*if (entity instanceof MltDescEnable) {
                        MltDescEnable mltDescEntity = (MltDescEnable) entity;

                        //TODO Replace with HQL bulk delete when Hibernate 3 Support bulk delete using composite key in where condition
                        List delList = session.createQuery(
                                "from MltDesc mlt where mlt.comp_id.objectName like :objectName and mlt.comp_id.priKey =:priKey")
                                              .setString(
                                "objectName", mltDescEntity.getObjectName() + "%")
                                              .setString("priKey", mltDescEntity.getPrimaryKey())
                                              .list();

                        if ((delList != null) && !delList.isEmpty()) {
                            for (Iterator iter = delList.iterator(); iter.hasNext();) {
                                session.delete(iter.next());
                            }
                        }

                        if (
                            (mltDescEntity.getMltDescData() != null) &&
                                !mltDescEntity.getMltDescData().isEmpty()) {
                            for (int i = 0; i < mltDescEntity.getMltDescData().size(); i++) {
                                Object object = mltDescEntity.getMltDescData().get(i);

                                session.save(object);
                            }
                        }
                    }*/

                    /*if (entity instanceof Auditable) {
                        session.merge(entity);

                        //session.saveOrUpdateCopy(entity);
                    } else {*/
                        session.update(entity);
                    //}

                    return null;
                }
            });
    }

    /**
     * @see org.springframework.orm.hibernate3.HibernateOperations#delete(java.lang.Object,
     *      org.hibernate.LockMode)
     */
    public void delete(final Object entity) throws DataAccessException {
        execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
                    /*if (entity instanceof MltDescEnable) {
                        MltDescEnable mltDescEntity = (MltDescEnable) entity;

                        //TODO Replace with HQL bulk delete when Hibernate 3 Support bulk delete using composite key in where condition
                        List delList = session.createQuery(
                                "from MltDesc mlt where mlt.comp_id.objectName like :objectName and mlt.comp_id.priKey =:priKey")
                                              .setString(
                                "objectName", mltDescEntity.getObjectName() + "%")
                                              .setString("priKey", mltDescEntity.getPrimaryKey())
                                              .list();

                        if ((delList != null) && !delList.isEmpty()) {
                            for (Iterator iter = delList.iterator(); iter.hasNext();) {
                                session.delete(iter.next());
                            }
                        }
                    }*/

                    session.delete(entity);

                    return null;
                }
            });
    }

    /**
     * @see org.springframework.orm.hibernate3.HibernateOperations#deleteAll(java.util.Collection)
     */
    public void deleteAll(final Collection entities) throws DataAccessException {
        execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException {
                    Object entity = null;

                    for (Iterator it = entities.iterator(); it.hasNext();) {
                        entity = it.next();

                        /*if (entity instanceof MltDescEnable) {
                            MltDescEnable mltDescEntity = (MltDescEnable) entity;
                            String objectName = mltDescEntity.getObjectName();
                            String priKey = mltDescEntity.getPrimaryKey();
                            session.delete(
                                "from MltDesc mlt where mlt.comp_id.objectName like '" +
                                objectName + "%' and mlt.comp_id.priKey='" + priKey + "'");
                        }*/

                        session.delete(entity);
                    }

                    return null;
                }
            });
    }
}
