/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpFileUploadDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileUploadService;

@Service
public class LlpFileUploadServiceImpl extends BaseServiceImpl<LlpFileUpload,  Long> implements LlpFileUploadService{
	@Autowired 
	LlpFileUploadDao llpFileUploadDao;
	
	static Map<String, LlpFileUpload> mapCache = new HashMap<String, LlpFileUpload>();

	@Override
	public BaseDao getDefaultDao() {
		return llpFileUploadDao;
	}

	@Override
	public LlpFileUpload findByFileCode(String fileCode) {
		if(mapCache.get(fileCode)!=null) {
			return mapCache.get(fileCode);
		}
		
		SearchCriteria sc = new SearchCriteria("fileCode",SearchCriteria.EQUAL,fileCode);
		sc = sc.andIfNotNull("fileStatus", SearchCriteria.EQUAL, Parameter.FILE_STATUS_active);
		List<LlpFileUpload> list = findByCriteria(sc).getList();
		if(list.size()>0){
			mapCache.put(fileCode, list.get(0));
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public void update(LlpFileUpload entity) {
		super.update(entity);
		mapCache.remove(entity.getFileCode());
	}
	
	@Override
	public void delete(LlpFileUpload entity) {
		super.delete(entity);
		mapCache.remove(entity.getFileCode());
	}

}
