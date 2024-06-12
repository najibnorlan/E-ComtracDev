package com.ssm.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

public class SSMHibernateInterceptor extends EmptyInterceptor {

	public static final String OPERATION_TYPE_INSERT = "INSERT";

	public static final String OPERATION_TYPE_UPDATE = "UPDATE";

	public static final String OPERATION_TYPE_DELETE = "DELETE";

	public static final String OPERATION_TYPE_SELECT = "SELECT";

	/** Created by field constant */
	protected static final String CREATED_BY = "createBy";

	/** Created Date field constant */
	protected static final String CREATED_AT = "createDt";

	/** Update by field constant */
	protected static final String UPDATED_BY = "updateBy";

	/** Update date field constant */
	protected static final String UPDATED_AT = "updateDt";

	// delete
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		super.onDelete(entity, id, state, propertyNames, types);
	}

	// update
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		setUpdatedInfo(entity, id, currentState, propertyNames, types);
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	// insert
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		setCreatedInfo(entity, id, state, propertyNames, types);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * Set any created (create by, create date) fields of tables
	 *
	 * @param obj
	 *            Any valid object
	 *
	 * @return Any valid object
	 */
	protected Object setCreatedInfo(Object obj, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		if( obj.getClass().getAnnotation(Entity.class)==null ) {
			return obj;
		}
		String createBy = UserEnvironmentHelper.getLoginName() == null ? "system" : UserEnvironmentHelper.getLoginName();
		Date createDt = new Date();

		String fields[] = new String[] {CREATED_BY,CREATED_AT,CREATED_BY.toLowerCase(),CREATED_AT.toLowerCase()};
		Object fieldsValue[] = new Object[] {createBy,createDt,createBy,createDt};
		
		for (int i = 0; i < fieldsValue.length; i++) {
			try {
				PropertyUtils.setSimpleProperty(obj, fields[i], fieldsValue[i]);
			} catch (Exception e) {
			}
			
			try {
				for (int j = 0; j < propertyNames.length; j++) {
					if(fields[i].equals(propertyNames[j])) {
						state[j] = fieldsValue[i];
						break;
					}
				}
			} catch (Exception e) {
			}
		}
		
		setUpdatedInfo(obj, id, state, propertyNames, types);
		
//		try {
//			
//			PropertyUtils.setSimpleProperty(obj, CREATED_AT, createDt);
//			
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(CREATED_BY.equals(propertyNames[i])) {
//					state[i] = createBy;
//				}
//				if(CREATED_AT.equals(propertyNames[i])) {
//					state[i] = createDt;
//				}
//			}
//		} catch (Exception e) {
//
//		}

//		
//		try {
//			String createBy = PropertyUtils.getSimpleProperty(obj, CREATED_BY.toLowerCase()) == null ? UserEnvironmentHelper.getLoginName()
//					: PropertyUtils.getSimpleProperty(obj, CREATED_BY.toLowerCase()).toString();
//			Date createDt = PropertyUtils.getSimpleProperty(obj, CREATED_AT.toLowerCase()) == null ? dt
//					: (Date) PropertyUtils.getSimpleProperty(obj, CREATED_AT.toLowerCase());
//
//			PropertyUtils.setSimpleProperty(obj, CREATED_BY.toLowerCase(), createBy);
//			PropertyUtils.setSimpleProperty(obj, CREATED_AT.toLowerCase(), createDt);
//
//		} catch (Exception e) {
//
//		}
//
//		try {
//			String updateBy = PropertyUtils.getSimpleProperty(obj, UPDATED_BY.toLowerCase()) == null ? UserEnvironmentHelper.getLoginName()
//					: PropertyUtils.getSimpleProperty(obj, UPDATED_BY.toLowerCase()).toString();
//			Date updateDt = PropertyUtils.getSimpleProperty(obj, UPDATED_AT.toLowerCase()) == null ? dt
//					: (Date) PropertyUtils.getSimpleProperty(obj, UPDATED_AT.toLowerCase());
//
//			PropertyUtils.setSimpleProperty(obj, UPDATED_BY.toLowerCase(), updateBy);
//			PropertyUtils.setSimpleProperty(obj, UPDATED_AT.toLowerCase(), updateDt);
//		} catch (Exception ex) {
//
//		}
//
//		try {
//			String createBy = PropertyUtils.getSimpleProperty(obj, CREATED_BY) == null ? UserEnvironmentHelper.getLoginName()
//					: PropertyUtils.getSimpleProperty(obj, CREATED_BY).toString();
//			Date createDt = PropertyUtils.getSimpleProperty(obj, CREATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, CREATED_AT);
//
//			PropertyUtils.setSimpleProperty(obj, "vchcreateby", createBy);
//			PropertyUtils.setSimpleProperty(obj, "dtcreatedate", createDt);
//		} catch (Exception e) {
//		}
//
//		try {
//			String updateBy = PropertyUtils.getSimpleProperty(obj, UPDATED_BY) == null ? UserEnvironmentHelper.getLoginName()
//					: PropertyUtils.getSimpleProperty(obj, UPDATED_BY).toString();
//			Date updateDt = PropertyUtils.getSimpleProperty(obj, UPDATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, UPDATED_AT);
//
//			PropertyUtils.setSimpleProperty(obj, "vchupdateby", updateBy);
//			PropertyUtils.setSimpleProperty(obj, "dtupdatedate", updateDt);
//		} catch (Exception ex) {
//
//		}

		

		return obj;
	}

	/**
	 * Set any updated (update by, update date) fields of tables
	 *
	 * @param obj
	 *            Any valid object
	 *
	 * @return Any valid object
	 */
	protected Object setUpdatedInfo(Object obj, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if( obj.getClass().getAnnotation(Entity.class)==null ) {
			return obj;
		}
		Long signInSessionId = null;
		try {
			UserEnvironment uE = UserEnvironmentHelper.getUserenvironment();
			if (uE != null) {
				LlpUserLog userLog = (LlpUserLog) uE.getAttribute("UserLog");
				if (userLog != null) {
					signInSessionId = userLog.getUserLogId();
				}
			}
		} catch (Exception e) {

		}
		
		String updateBy = UserEnvironmentHelper.getLoginName() == null ? "system" : UserEnvironmentHelper.getLoginName();
		Date updateDt = new Date();

		String fields[] = new String[] {UPDATED_BY,UPDATED_AT,UPDATED_BY.toLowerCase(),UPDATED_AT.toLowerCase(),"signInSessionId"};
		Object fieldsValue[] = new Object[] {updateBy,updateDt,updateBy,updateDt,signInSessionId};
		
		for (int i = 0; i < fieldsValue.length; i++) {
			try {
				PropertyUtils.setSimpleProperty(obj, fields[i], fieldsValue[i]);
			} catch (Exception e) {
			}
			try {
				for (int j = 0; j < propertyNames.length; j++) {
					if(fields[i].equals(propertyNames[j])) {
						state[j] = fieldsValue[i];
						break;
					}
				}
			} catch (Exception e) {
			}
		}
		
//		Object ret = obj;
//
//		try {
//			PropertyUtils.setSimpleProperty(obj, UPDATED_BY, UserEnvironmentHelper.getLoginName());
//			PropertyUtils.setSimpleProperty(obj, UPDATED_AT, new Date());
//		} catch (Exception e) {
//		}
//
//		try {
//			PropertyUtils.setSimpleProperty(obj, UPDATED_BY.toLowerCase(), UserEnvironmentHelper.getLoginName());
//			PropertyUtils.setSimpleProperty(obj, UPDATED_AT.toLowerCase(), new Date());
//		} catch (Exception e) {
//		}
//
//		try {
//			PropertyUtils.setSimpleProperty(obj, "vchupdateby", UserEnvironmentHelper.getLoginName());
//			PropertyUtils.setSimpleProperty(obj, "dtupdatedate", new Date());
//		} catch (Exception e) {
//		}
//
//		try {
//			UserEnvironment uE = UserEnvironmentHelper.getUserenvironment();
//			if (uE == null) {
//				PropertyUtils.setSimpleProperty(obj, "signInSessionId", null);
//			} else {
//				LlpUserLog userLog = (LlpUserLog) uE.getAttribute("UserLog");
//				if (userLog != null) {
//					PropertyUtils.setSimpleProperty(obj, "signInSessionId", userLog.getUserLogId());
//				}
//			}
//		} catch (Exception e) {
//
//		}

		return obj;
	}

}
