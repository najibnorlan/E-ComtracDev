package com.ssm.hibernate;

import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.Audited;
import org.hibernate.envers.event.AuditEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PreCollectionRemoveEvent;
import org.hibernate.event.PreCollectionUpdateEvent;

import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironment;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.ezbiz.model.RobRenewal;

public class SSMEnverListener extends AuditEventListener {
	
	/** Created by field constant */
	protected static final String CREATED_BY = "createBy";

	/** Created Date field constant */
	protected static final String CREATED_AT = "createDt";

	/** Update by field constant */
	protected static final String UPDATED_BY = "updateBy";

	/** Update date field constant */
	protected static final String UPDATED_AT = "updateDt";
	
	@Override
	public void initialize(Configuration cfg) {
		super.initialize(cfg);
	}
	
	@Override
	public void onPostUpdate(PostUpdateEvent arg0) {
//		setUpdatedInfo(arg0.getEntity());
		if( arg0.getEntity().getClass().getAnnotation(Audited.class)!=null ) {//supaya tak insert log kosong sbb extend basemodel
			super.onPostUpdate(arg0);
		}
	}
	
	@Override
	public void onPostInsert(PostInsertEvent arg0) {
//		setCreatedInfo(arg0.getEntity());
		if( arg0.getEntity().getClass().getAnnotation(Audited.class)!=null ) {//supaya tak insert log kosong sbb extend basemodel
			super.onPostInsert(arg0);
		}
	}
	@Override
	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
		super.onPreUpdateCollection(event);
//		System.out.println("PreaUpdate:"+event.getCollection());
	}
	
	@Override
	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		super.onPreRemoveCollection(event);
	}
	
	
	/**
	 * Set any created (create by, create date) fields of tables
	 *
	 * @param obj
	 *            Any valid object
	 *
	 * @return Any valid object
	 */
	protected Object setCreatedInfo(Object obj) {
		Date dt = new Date();

		try {

			String createBy = PropertyUtils.getSimpleProperty(obj, CREATED_BY) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, CREATED_BY).toString();
			Date createDt = PropertyUtils.getSimpleProperty(obj, CREATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, CREATED_AT);

			PropertyUtils.setSimpleProperty(obj, CREATED_BY, createBy);
			PropertyUtils.setSimpleProperty(obj, CREATED_AT, createDt);

		} catch (Exception e) {

		}

		try {
			String updateBy = PropertyUtils.getSimpleProperty(obj, UPDATED_BY) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, UPDATED_BY).toString();
			Date updateDt = PropertyUtils.getSimpleProperty(obj, UPDATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, UPDATED_AT);

			PropertyUtils.setSimpleProperty(obj, UPDATED_BY, updateBy);
			PropertyUtils.setSimpleProperty(obj, UPDATED_AT, updateDt);
		} catch (Exception ex) {

		}

		try {
			String createBy = PropertyUtils.getSimpleProperty(obj, CREATED_BY.toLowerCase()) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, CREATED_BY.toLowerCase()).toString();
			Date createDt = PropertyUtils.getSimpleProperty(obj, CREATED_AT.toLowerCase()) == null ? dt
					: (Date) PropertyUtils.getSimpleProperty(obj, CREATED_AT.toLowerCase());

			PropertyUtils.setSimpleProperty(obj, CREATED_BY.toLowerCase(), createBy);
			PropertyUtils.setSimpleProperty(obj, CREATED_AT.toLowerCase(), createDt);

		} catch (Exception e) {

		}

		try {
			String updateBy = PropertyUtils.getSimpleProperty(obj, UPDATED_BY.toLowerCase()) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, UPDATED_BY.toLowerCase()).toString();
			Date updateDt = PropertyUtils.getSimpleProperty(obj, UPDATED_AT.toLowerCase()) == null ? dt
					: (Date) PropertyUtils.getSimpleProperty(obj, UPDATED_AT.toLowerCase());

			PropertyUtils.setSimpleProperty(obj, UPDATED_BY.toLowerCase(), updateBy);
			PropertyUtils.setSimpleProperty(obj, UPDATED_AT.toLowerCase(), updateDt);
		} catch (Exception ex) {

		}

		try {
			String createBy = PropertyUtils.getSimpleProperty(obj, CREATED_BY) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, CREATED_BY).toString();
			Date createDt = PropertyUtils.getSimpleProperty(obj, CREATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, CREATED_AT);

			PropertyUtils.setSimpleProperty(obj, "vchcreateby", createBy);
			PropertyUtils.setSimpleProperty(obj, "dtcreatedate", createDt);
		} catch (Exception e) {
		}

		try {
			String updateBy = PropertyUtils.getSimpleProperty(obj, UPDATED_BY) == null ? UserEnvironmentHelper.getLoginName()
					: PropertyUtils.getSimpleProperty(obj, UPDATED_BY).toString();
			Date updateDt = PropertyUtils.getSimpleProperty(obj, UPDATED_AT) == null ? dt : (Date) PropertyUtils.getSimpleProperty(obj, UPDATED_AT);

			PropertyUtils.setSimpleProperty(obj, "vchupdateby", updateBy);
			PropertyUtils.setSimpleProperty(obj, "dtupdatedate", updateDt);
		} catch (Exception ex) {

		}

		try {
			UserEnvironment uE = UserEnvironmentHelper.getUserenvironment();
			if (uE == null) {
				PropertyUtils.setSimpleProperty(obj, "signInSessionId", null);
			} else {
				LlpUserLog userLog = (LlpUserLog) uE.getAttribute("UserLog");
				if (userLog != null) {
					PropertyUtils.setSimpleProperty(obj, "signInSessionId", userLog.getUserLogId());
				}
			}
		} catch (Exception e) {

		}

		return obj;
	}
	
	protected Object setUpdatedInfo(Object obj) {
		
		Object ret = obj;

		try {
			PropertyUtils.setSimpleProperty(obj, UPDATED_BY, UserEnvironmentHelper.getLoginName());
			PropertyUtils.setSimpleProperty(obj, UPDATED_AT, new Date());
		} catch (Exception e) {
		}

		try {
			PropertyUtils.setSimpleProperty(obj, UPDATED_BY.toLowerCase(), UserEnvironmentHelper.getLoginName());
			PropertyUtils.setSimpleProperty(obj, UPDATED_AT.toLowerCase(), new Date());
		} catch (Exception e) {
		}

		try {
			PropertyUtils.setSimpleProperty(obj, "vchupdateby", UserEnvironmentHelper.getLoginName());
			PropertyUtils.setSimpleProperty(obj, "dtupdatedate", new Date());
		} catch (Exception e) {
		}

		try {
			UserEnvironment uE = UserEnvironmentHelper.getUserenvironment();
			if (uE == null) {
				PropertyUtils.setSimpleProperty(obj, "signInSessionId", null);
			} else {
				LlpUserLog userLog = (LlpUserLog) uE.getAttribute("UserLog");
				if (userLog != null) {
					PropertyUtils.setSimpleProperty(obj, "signInSessionId", userLog.getUserLogId());
				}
			}
		} catch (Exception e) {

		}

		return ret;
	}
	
}
