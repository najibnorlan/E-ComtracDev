package com.ssm.ssmlocalsvr;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class SSMLocalSvrUtils {
	
	
	public static Map mapToObject(Map mapObj, Object obj ) {

		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
//			
			if(mapObj.containsKey(fieldName)) {
				Object value = mapObj.get(fieldName);
				try {
	//				fields[i].set(obj, value);
					BeanUtils.setProperty(obj,fieldName,value);
				} catch (Exception e) {
					// Try Get by getter
					e.printStackTrace();
					String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					try {
						obj.getClass().getMethod(methodName).invoke(value);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}

		return mapObj;
	}
	
	public static void clearObjectValue(Object obj ) {

		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
//			
			try {
				BeanUtils.setProperty(obj,fieldName,null);
			} catch (Exception e) {
				// Try Get by getter
				e.printStackTrace();
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					obj.getClass().getMethod(methodName).invoke(null);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

	}
}
