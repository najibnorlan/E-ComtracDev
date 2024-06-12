package com.ssm.ezbiz.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormNotesDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.ezbiz.model.RobFormNotes;

@Repository
public class RobFormNotesDaoImpl extends BaseDaoImpl<RobFormNotes, Long> implements RobFormNotesDao {

	@Override
	public List findByFormCode(String robFormBCode) {
		String hql = "from "+RobFormNotes.class.getName()
				+" where robFormCode=? order by createDt asc";
		
		return getHibernateTemplate().find(hql, robFormBCode);
	}

}
