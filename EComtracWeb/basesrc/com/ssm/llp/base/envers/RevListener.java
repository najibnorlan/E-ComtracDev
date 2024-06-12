package com.ssm.llp.base.envers;

import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Configurable;

import com.ssm.llp.base.common.model.RevEntity;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

@Configurable
public class RevListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		RevEntity revEntity = (RevEntity) revisionEntity;
		revEntity.setUserName(UserEnvironmentHelper.getLoginName());	
	}
}