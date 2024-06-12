package com.ssm.llp.mod1.service;

import java.util.List;

import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;

public interface LlpBusinessCodeLinkService extends BaseService<LlpBusinessCodeLink, Long> {

	List<LlpBusinessCodeLink> findByLlpNo(String llpNo);
}
