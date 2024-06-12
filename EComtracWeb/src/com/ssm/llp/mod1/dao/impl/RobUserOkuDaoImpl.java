package com.ssm.llp.mod1.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.informix.util.stringUtil;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.mod1.dao.RobUserOkuDao;
import com.ssm.llp.mod1.model.RobUserOku;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;

//utk guna fetch.LAZY kat dataobject perlu guna session.load (fix error LazyInitializationException)!!

@Repository
public class RobUserOkuDaoImpl extends BaseDaoImpl<RobUserOku, String> implements RobUserOkuDao {
	
	@Override
	public RobUserOku findLatestOkuByIdTypeAndIdNo(String idType, String idNo) { //userProfile.idType userProfile.idNo
		String hql = "from " + RobUserOku.class.getName() + " where userProfile.idType=? and userProfile.idNo=? order by srlOku desc"; //latest
		List<RobUserOku> listResult = getHibernateTemplate().find(hql, new Object[] { idType, idNo });
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
		
	@Override
	public RobUserOku findLatestOkuByUserRefNo(String userRefNo) {
		String hql = "from " + RobUserOku.class.getName() + " where userProfile.userRefNo=? order by srlOku desc"; //sebab always insert new record n cancel yg previous.
		List<RobUserOku> listResult = getHibernateTemplate().find(hql, userRefNo);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	
	@Override
	public RobUserOku findLatestOkuByUserRefNoWithData(String userRefNo) { //robUserOku initialize here..
		
		String hql = "from " + RobUserOku.class.getName() + " where userProfile.userRefNo=? order by srlOku desc"; //sebab always insert new record n cancel yg previous.
		List<RobUserOku> listResult = getHibernateTemplate().find(hql, userRefNo);
		if (listResult.size() > 0) {
			RobUserOku robUserOku = (RobUserOku) listResult.get(0);
			//utk guna fetch.LAZY kat dataobject perlu guna session.load (fix error LazyInitializationException)..
			//Hibernate.initialize(robUserOku.getUserProfile()); //tak jd, guna eager semula..
			if(robUserOku.getDocDataId()!=null) {
				robUserOku = findOkuByRefNoWithData(robUserOku.getOkuRefNo());
			}
			return robUserOku;
		}
		return null;
	 }

	
	@Override
	public RobUserOku findLatestOkuByRefNo(String okuRefNo) {
		//String hql = "from " + RobUserOku.class.getName() + " where okuRefNo=? order by updateDt desc";
		String hql = "from " + RobUserOku.class.getName() + " where okuRefNo=? order by srlOku desc"; //sebab always insert new record n cancel yg previous.

		List<RobUserOku> listResult = getHibernateTemplate().find(hql, okuRefNo);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	
	
	@Override
	public RobUserOku findLatestOkuByUserRefNoAndIdRegUser(String userRefNo, long idRegUser) {
		//String hql = "from " + RobUserOku.class.getName() + " where userProfile.userRefNo=? and idRegUser=? order by updateDt desc";
		String hql = "from " + RobUserOku.class.getName() + " where userProfile.userRefNo=? and idRegUser=? order by srlOku desc";

		List<RobUserOku> listResult = getHibernateTemplate().find(hql, new Object[] { userRefNo, idRegUser });
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	

	
	@Override
	public List<RobUserOku> getListRobUserOku(String userRefNo, String okuRegStatus) {

		List<RobUserOku> listRobUserOku = new ArrayList<RobUserOku>();

		String hql = " from " + RobUserOku.class.getName();

		if (userRefNo != null) {
			hql = hql + " " + "where userProfile.userRefNo =?";
		} else {
			userRefNo = "%";
			hql = hql + " " + "where userProfile.userRefNo like ?";
		}

		if (okuRegStatus != null) {
			hql = hql + " and " + "okuRegStatus =?";
		} else {
			okuRegStatus = "%";
			hql = hql + " and " + "okuRegStatus like ?";
		}

		System.out.println("hql:" + hql);

		listRobUserOku = getHibernateTemplate().find(hql, new Object[] { userRefNo, okuRegStatus });

		System.out.println("listRobUserOku size :" + listRobUserOku.size());

		return listRobUserOku;

	}

	@Override
	public RobUserOku findOkuByRefNoWithData(String okuRefNo) {
		Session session = getSession();
		RobUserOku robUserOku = (RobUserOku) session.load(RobUserOku.class, okuRefNo);
		Hibernate.initialize(robUserOku.getDocDataId()); //di dataobject fetch.lazy
		return robUserOku;
	}


	
}
