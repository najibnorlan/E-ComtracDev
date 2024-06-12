package com.ssm.ezbiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobFormNotesDao;
import com.ssm.ezbiz.service.RobFormNotesService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobFormNotes;

@Service
public class RobFormNotesServiceImpl extends BaseServiceImpl<RobFormNotes, Long> implements RobFormNotesService {

	@Autowired
	RobFormNotesDao robFormNotesDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robFormNotesDao;
	}

	@Override
	public List findByFormCode(String robFormBCode) {
		return robFormNotesDao.findByFormCode(robFormBCode);
	}
}