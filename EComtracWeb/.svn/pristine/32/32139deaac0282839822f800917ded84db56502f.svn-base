package com.ssm.llp.base.common.dao.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.SSMAuditLogDao;
import com.ssm.llp.base.common.model.SSMAuditLogModel;

@Repository
public class SSMAuditLogDaoImpl extends SimpleBaseDaoImpl implements SSMAuditLogDao{

	@Override
	public SSMAuditLogModel findAuditLog(String clazzName,Object primaryKey, String filterField) {
		
		try {
			List<String> setFilterColl = new ArrayList<String>();
			if(StringUtils.isNotBlank(filterField)){
				setFilterColl = Arrays.asList(StringUtils.split(filterField,","));
			}
			
			List<Method> listMethodName = new ArrayList<Method>();
			List<String> listColumnName = new ArrayList<String>();
			
			Class clazz = Class.forName(clazzName);
			Method method[] = clazz.getDeclaredMethods();
			for (int j = 0; j < method.length; j++) {
				if(method[j].isAnnotationPresent(Column.class)){
//					System.out.println(method[j].getReturnType().getName());
					if(method[j].getReturnType().isInstance(new byte[0])){
						continue;
					}
					String collName = method[j].getAnnotation(Column.class).name();
					if(setFilterColl.size()>0){
						if(setFilterColl.contains(collName)){
							listColumnName.add(collName);
							listMethodName.add(method[j]);
						}
					}else{
						listColumnName.add(collName);
						listMethodName.add(method[j]);
					}
					
				}
			}
			
			
			AuditQuery q = getAuditReader().createQuery().forRevisionsOfEntity(clazz, true, true);
			q.add(AuditEntity.id().eq(primaryKey));
			
			
			List audiList = q.getResultList();
			List<List<Object>> listObjectValue = new ArrayList<List<Object>>();
			for (int i = 0; i < audiList.size(); i++) {
				Object obj = audiList.get(i);
				List<Object> objValue = new ArrayList<Object>();
				
				for (int j = 0; j < listMethodName.size(); j++) {
					try {
						objValue.add(listMethodName.get(j).invoke(obj));
					} catch (Exception e) {
						objValue.add(null);
					}
				}
				listObjectValue.add(objValue);
			}
			return new SSMAuditLogModel(listColumnName,listObjectValue);
		} catch (Exception e) {
			return new SSMAuditLogModel(new ArrayList(),new ArrayList());
		}
		
	}
	


	
}
