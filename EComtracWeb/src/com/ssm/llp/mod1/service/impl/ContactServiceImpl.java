package com.ssm.llp.mod1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.mod1.dao.ContactDao;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.service.ContactService;

@Repository
public class ContactServiceImpl extends BaseServiceImpl<Contact, String> implements ContactService {

	@Autowired
	ContactDao contactDao;

	@Override
	public BaseDao getDefaultDao() {
		return contactDao;
	}

}
