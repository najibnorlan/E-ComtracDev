/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.dao.RobFormOwnerVerificationDao;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormC;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;

@Repository
public class RobFormOwnerVerificationDaoImpl extends BaseDaoImpl<RobFormOwnerVerification, Long>  implements RobFormOwnerVerificationDao{

	@Override
	public RobFormOwnerVerification findByRobFormCodeAndIdNo(
			String robFormCode, String idNo) {
		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" where robFormCode=? and idNo=? ";
		
		List<RobFormOwnerVerification> result = getHibernateTemplate().find(hql, new String[]{robFormCode ,idNo});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<RobFormOwnerVerification> getListRobFormOwnerVerification(
			String robFormCRefNo) {

		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" where robFormCode=?  ";
		
		return getHibernateTemplate().find(hql, robFormCRefNo);
				
	}

	@Override
	public List<Object[]> findAllVerificationList(LlpUserProfile llpUserProfile) {
		String sql = "";
		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" ownerVer ," +RobFormB.class.getName()+ " robFormB "
				+ " where ownerVer.robFormCode=robFormB.robFormBCode "
				+ " and ownerVer.userRefNo= ? "
				+ " and ownerVer.robFormType= ? "
				+ " and ownerVer.status in (?,?,?) "
				+ " and robFormB.status = ? "
				+ " and robFormB.createBy <> ? "
				+ " order by ownerVer.robFormOwnerVerificationId desc ";
		
		List listParam = new ArrayList();
		listParam.add(llpUserProfile.getUserRefNo());
		listParam.add(Parameter.ROB_FORM_TYPE_B);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
		listParam.add(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY);
		listParam.add(llpUserProfile.getLoginId());
		
		
		return getHibernateTemplate().find(hql, listParam.toArray());
	}

	@Override
	public List<RobFormOwnerVerification> findByRobFormCode(String robFormCode) {
		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" where robFormCode=? ";
		
		return getHibernateTemplate().find(hql, new String[]{robFormCode});
	}

	@Override
	public List<Object[]> findAllVerificationFormCList(
			LlpUserProfile llpUserProfile) {
		// TODO Auto-generated method stub
		String sql = "";
		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" ownerVer ," +RobFormC.class.getName()+ " robFormC "
				+ " where ownerVer.robFormCode=robFormC.robFormCCode "
				+ " and ownerVer.userRefNo= ? "
				+ " and ownerVer.robFormType= ? "
				+ " and ownerVer.status in (?,?,?) "
				+ " and robFormC.status IN ( ?,?) "
				+ " and robFormC.createBy <> ? ";
		
		List listParam = new ArrayList();
		listParam.add(llpUserProfile.getUserRefNo());
		listParam.add(Parameter.ROB_FORM_TYPE_C);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_REJECT);
		listParam.add(Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED);
		listParam.add(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY);
		listParam.add(Parameter.ROB_FORM_C_STATUS_WITHOUT_PAYMENT);
		listParam.add(llpUserProfile.getLoginId());
		
		System.out.println("llpUserProfile.getUserRefNo()"+llpUserProfile.getUserRefNo());
		return getHibernateTemplate().find(hql, listParam.toArray());
	}

	@Override
	public RobFormOwnerVerification findByFormCodeAndUserRefNo(String robFormCode, String ezbizUserRefNo) {
		String hql = "from "+RobFormOwnerVerification.class.getName()
				+" where robFormCode=? and userRefNo=? ";
		
		List<RobFormOwnerVerification> result = getHibernateTemplate().find(hql, new String[]{robFormCode ,ezbizUserRefNo});
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
}
