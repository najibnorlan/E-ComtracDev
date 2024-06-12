/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.common.dao.LlpLocaleMessageDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpLocaleMessage;
import com.ssm.llp.base.common.service.LlpLocaleMessageService;

@Service
public class LlpLocaleMessageServiceImpl extends BaseServiceImpl<LlpLocaleMessage,  Long> implements LlpLocaleMessageService{
	
//	Hard to maintain in cluster environment
	private static Map<String, LlpLocaleMessage> mapLocaleMessage = new HashedMap();
	
	@Autowired 
	LlpLocaleMessageDao llpLocaleMessageDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpLocaleMessageDao;
	}

	void clearActiveCacheMap (String key, String localeStr){
		try {
			mapLocaleMessage.remove(key+":"+localeStr);
		} catch (Exception e) {
		}
	}
	void clearActiveCacheMap (LlpLocaleMessage llpLocaleMessage){
		try {
			mapLocaleMessage.remove(llpLocaleMessage.getKey()+":"+llpLocaleMessage.getLocale());
		} catch (Exception e) {
		}
	}
	
	LlpLocaleMessage getFromCacheMap (String key, String localeStr){
		return mapLocaleMessage.get(key+":"+localeStr);
	}
	
	void putIntoCacheMap (LlpLocaleMessage llpLocaleMessage){
		mapLocaleMessage.put(llpLocaleMessage.getKey()+":"+llpLocaleMessage.getLocale(), llpLocaleMessage);
	}
	
	void putIntoCacheMap (String key, String localeStr, LlpLocaleMessage llpLocaleMessage){
		mapLocaleMessage.put(key+":"+localeStr, llpLocaleMessage);
	}
	
	
	@Override
	public LlpLocaleMessage findByKeyNLocale(String key, String localeStr) {
		
		LlpLocaleMessage llpLocaleMessage = getFromCacheMap(key, localeStr);
		if(llpLocaleMessage==null ){
			SearchCriteria sc = new SearchCriteria("key",SearchCriteria.EQUAL, key);
			sc = SearchCriteria.andIfNotNull(sc, "locale", SearchCriteria.EQUAL, localeStr);
			sc.addOrderBy("localeMessageId", SearchCriteria.DESC);
			
			List<LlpLocaleMessage> list = findByCriteria(sc).getList();
			if(list.size()>0){
				llpLocaleMessage = list.get(0);
				putIntoCacheMap(key, localeStr, llpLocaleMessage);
			}
		}
		return llpLocaleMessage;
		
	}
	
	@Override
	public void insert(LlpLocaleMessage entity) {
		clearActiveCacheMap(entity);
		super.insert(entity);
		putIntoCacheMap(entity);
	}
	
	@Override
	public void update(LlpLocaleMessage entity) {
		clearActiveCacheMap(entity);
		super.update(entity);
		putIntoCacheMap(entity);
	}
	
	@Override
	public void delete(LlpLocaleMessage entity) {
		clearActiveCacheMap(entity);
		super.delete(entity);
	}

}
