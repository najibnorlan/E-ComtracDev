package com.ssm.llp.mod1.dao.impl;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.ContactDao;
import com.ssm.llp.mod1.model.Contact;

@Repository
public class ContactDaoImpl extends BaseDaoImpl<Contact, String> implements ContactDao {
}
