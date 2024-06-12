package com.ssm.llp.mod1.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.service.RocBusinessObjectCodeService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;
import com.ssm.llp.mod1.dao.LlpBusinessCodeLinkDao;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.service.LlpBusinessCodeLinkService;

@Service
public class LlpBusinessCodeLinkServiceImpl extends BaseServiceImpl<LlpBusinessCodeLink, Long> implements LlpBusinessCodeLinkService {

	@Autowired
	LlpBusinessCodeLinkDao llpBusinessCodeLinkDao;

	@Autowired
	@Qualifier("RocBusinessObjectCodeService")
	RocBusinessObjectCodeService businessObjectCodeService;

	@Override
	public BaseDao getDefaultDao() {
		return llpBusinessCodeLinkDao;
	}

	@Override
	public LlpBusinessCodeLink findById(Long id) {
		LlpBusinessCodeLink codeLink = llpBusinessCodeLinkDao.findById(id);
		RocBusinessObjectCode objectCode = businessObjectCodeService.findById(codeLink.getBusinessCode());
		codeLink.setRocBusinessObjectCode(objectCode);

		return codeLink;
	}

	@Override
	public SearchResult findByCriteria(SearchCriteria sc) {
		SearchResult sr = llpBusinessCodeLinkDao.findByCriteria(sc);
		List<LlpBusinessCodeLink> list = sr.getList();
		for (int i = 0; i < list.size(); i++) {
			LlpBusinessCodeLink codeLink = list.get(i);
			RocBusinessObjectCode objectCode = businessObjectCodeService.findById(codeLink.getBusinessCode());
			codeLink.setRocBusinessObjectCode(objectCode);
		}
		return sr;
	}

	@Override
	public List<LlpBusinessCodeLink> findByLlpNo(String llpNo) {
		List<LlpBusinessCodeLink> list = llpBusinessCodeLinkDao.findByLlpNo(llpNo);
		for (int i = 0; i < list.size(); i++) {
			LlpBusinessCodeLink codeLink = list.get(i);
			RocBusinessObjectCode objectCode = businessObjectCodeService.findById(codeLink.getBusinessCode());
			codeLink.setRocBusinessObjectCode(objectCode);
		}
		return list;
	}

}