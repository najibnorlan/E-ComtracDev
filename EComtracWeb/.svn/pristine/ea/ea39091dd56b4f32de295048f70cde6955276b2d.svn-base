/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.db;

import org.hibernate.HibernateException;
import org.hibernate.type.Type;

import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;

/**
 * Listener for Hibernate Interceptor
 *
 * @author llk
 * @version 1.0
 */
public interface HibernateInterceptorListerner {
    /**
     * Invoke during Interceptor.onSave()
     *
     * @param entity Object
     * @param fieldName String[]
     * @param newValue Object[]
     * @param types Type[]
     * @param id Serializable
     * @param logId String
     * @param hibernateTemplate HibernateTemplate
     *
     * @throws HibernateException exception
     *
     * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    void onInsert(
        Object entity, String[] fieldName, Object[] newValue, Type[] types, Serializable id,
        String logId, HibernateTemplate hibernateTemplate)
        throws HibernateException;

    /**
     * Invoke during Interceptor.onSave()
     *
     * @param entity Object
     * @param fieldName String[]
     * @param oldValue Object[]
     * @param types Type[]
     * @param id Serializable
     * @param logId String
     * @param hibernateTemplate HibernateTemplate
     *
     * @throws HibernateException exception
     *
     * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    void onDelete(
        Object entity, String[] fieldName, Object[] oldValue, Type[] types, Serializable id,
        String logId, HibernateTemplate hibernateTemplate)
        throws HibernateException;

    /**
     * Invoke during Interceptor.onFlushDirty()
     *
     * @param entity Object
     * @param fieldName String[]
     * @param oldValue Object[]
     * @param newValue Object[]
     * @param types Type[]
     * @param id Serializable
     * @param logId String
     * @param hibernateTemplate HibernateTemplate
     *
     * @throws HibernateException exception
     *
     * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable,
     *      java.lang.Object[], java.lang.Object[], java.lang.String[],
     *      org.hibernate.type.Type[])
     */
    void onUpdate(
        Object entity, String[] fieldName, Object[] oldValue, Object[] newValue, Type[] types,
        Serializable id, String logId, HibernateTemplate hibernateTemplate)
        throws HibernateException;
}
