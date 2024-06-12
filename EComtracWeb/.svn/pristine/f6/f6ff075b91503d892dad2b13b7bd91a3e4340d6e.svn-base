package com.ssm.llp.mod1.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.mod1.dao.LlpUserProfileDao;
import com.ssm.llp.mod1.model.LlpUserProfile;

@Repository
public class LlpUserProfileDaoImpl extends BaseDaoImpl<LlpUserProfile, String> implements LlpUserProfileDao {

	@Override
	public LlpUserProfile findByUserId(String userId) {
		String hql = "from " + LlpUserProfile.class.getName() + " where loginId=? ";

		List<LlpUserProfile> listResult = getHibernateTemplate().find(hql, userId);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}

	@Override
	public List<LlpUserProfile> findAllUsers() {
		List<LlpUserProfile> listLlpUserProfile = new ArrayList<LlpUserProfile>();
		String idType;
		String hql = "from " + LlpUserProfile.class.getName();
		idType = "%";
		hql = hql + " " + "where id_type like ? order by name ";

		listLlpUserProfile = getHibernateTemplate().find(hql, new Object[] { idType });
		return listLlpUserProfile;
	}

	@Override
	public boolean checkStatusIdTypeAndIdNo(String idType, String idNo) {
		// TODO Auto-generated method stub

		String hql = " from " + LlpUserProfile.class.getName() + " where idType=? and " + "idNo=? and userStatus in ('A', 'P','D')";

		List list = getHibernateTemplate().find(hql, new Object[] { idType, idNo });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfile = (LlpUserProfile) list.get(0);

			return true;
		}

		return false;

	}

	@Override
	public LlpUserProfile findByIdTypeAndIdNo(String idType, String idNo) {
		// TODO Auto-generated method stub
		String hql = "from " + LlpUserProfile.class.getName() + " where idType=? and idNo=? ";

		List<LlpUserProfile> listResult = getHibernateTemplate().find(hql, new Object[] { idType, idNo });
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}

	@Override
	public boolean validateIdNoUponUpdate(LlpUserProfile llpUserProfile) {
		// TODO Auto-generated method stub

		String hql = " from " + LlpUserProfile.class.getName() + " where idType=? and "
				+ "idNo=? and userStatus in ('A', 'P','D') and userRefNo not in (?)";

		List list = getHibernateTemplate().find(hql,
				new Object[] { llpUserProfile.getIdType(), llpUserProfile.getIdNo(), llpUserProfile.getUserRefNo() });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
			return true;
		}

		return false;

	}

	@Override
	public boolean checkEmail(String email) {
		// TODO Auto-generated method stub
		String hql = " from " + LlpUserProfile.class.getName() + " where  " + "email=? and userStatus in ('A', 'P','D') ";

		List list = getHibernateTemplate().find(hql, new Object[] { email });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
			return true;
		}

		return false;
	}

	@Override
	public boolean checkLoginId(String loginId, String userRefNo) {
		// TODO Auto-generated method stub
		String hql = " from " + LlpUserProfile.class.getName() + " where  " + "loginId=? and userStatus in ('A', 'P','D') ";

		List list = getHibernateTemplate().find(hql, new Object[] { loginId });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
			return true;
		}

		return false;
	}

	@Override
	public boolean checkEmailUpdate(String userRefNo, String email) {
		// TODO Auto-generated method stub

		//
		// if(("").equals(userRefNo)){
		// hql = " from " + LlpUserProfile.class.getName() + " where  "
		// + "email=? and userStatus in ('A', 'P','D') ";
		//
		// List list = getHibernateTemplate().find(hql, new Object[] {email});
		//
		//
		// if (list.size() > 0) {
		// LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
		// return true;
		// }
		// }else{
		//
		String hql = " from " + LlpUserProfile.class.getName() + " where  " + "email=? and userStatus in ('A', 'P','D') and userRefNo not in (?)";
		List list = getHibernateTemplate().find(hql, new Object[] { email, userRefNo });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
			return true;
		}
		// }

		return false;
	}

	@Override
	public List<LlpUserProfile> getListLlpUserProfile(String idType, String idNo, String name, String userStatus) {

		List<LlpUserProfile> listLlpUserProfile = new ArrayList<LlpUserProfile>();

		String hql = " from " + LlpUserProfile.class.getName();

		if (idType != null) {
			hql = hql + " " + "where id_type =?";
		} else {
			idType = "%";
			hql = hql + " " + "where id_type like ?";
		}

		if (idNo != null) {
			hql = hql + " and " + "id_no =?";
		} else {
			idNo = "%";
			hql = hql + " and " + "id_no like ?";
		}

		if (name != null) {
			hql = hql + " and " + "name =?";
		} else {
			name = "%";
			hql = hql + " and " + "name like ?";
		}

		if (userStatus != null) {
			hql = hql + " and " + "user_status =?";
		} else {
			userStatus = "%";
			hql = hql + " and " + "user_status like ?";
		}
		System.out.println("hql:" + hql);

		listLlpUserProfile = getHibernateTemplate().find(hql, new Object[] { idType, idNo, name, userStatus });
		System.out.println("idType,idNo,name,userStatus" + idType + idNo + name + userStatus);

		System.out.println("listLlpUserProfile size :" + listLlpUserProfile.size());

		return listLlpUserProfile;

	}

	//
	// public SearchResult findByCriteriaUserProfile(SearchCriteria criteria) {
	// return findByCriteriaUserProfile(criteria,
	// getPersistentClass().getName());
	// }
	//
	// public SearchResult findByCriteriaUserProfile(SearchCriteria criteria,
	// String className) {
	// List parameters = new ArrayList();
	//
	// String criteriaStr = scToString(criteria, parameters, ALIAS);
	//
	// return findByCriteriaUserProfile(
	// "count(*)", "", className + " " + ALIAS, criteriaStr,
	// getOrderBySql(criteria, ALIAS),
	// parameters, criteria.getStartAtIndex(), criteria.getMaxRecord());
	// }

	/**
	 * Find the SearchResult with the given count sql and select sql
	 *
	 * @param selectCountSql
	 *            select count sql
	 * @param selectSql
	 *            selected fields
	 * @param fromSql
	 *            from sql
	 * @param criteriaStr
	 *            where sql
	 * @param orderBySql
	 *            order by sql
	 * @param parameters
	 *            parameters list
	 * @param startAtIndex
	 *            returned result start index
	 * @param maxRecord
	 *            retured result max number of records
	 *
	 * @return TODO DOCUMENTTHIS
	 */
	// private SearchResult findByCriteriaUserProfile(
	// String selectCountSql, String selectSql, String fromSql, String
	// criteriaStr,
	// String orderBySql, List parameters, int startAtIndex, int maxRecord) {
	// SearchResult sr = new SearchResult();
	// StringBuffer countSql = new StringBuffer();
	// StringBuffer fullSql = new StringBuffer();
	//
	// countSql.append("select ");
	// countSql.append(selectCountSql);
	// countSql.append(" from ");
	// countSql.append(fromSql);
	//
	// if (StringUtils.isNotBlank(selectSql)) {
	// fullSql.append("select ");
	// fullSql.append(selectSql);
	// }
	//
	// fullSql.append(" from ");
	// fullSql.append(fromSql);
	//
	// if (StringUtils.isNotBlank(criteriaStr)) {
	// countSql.append(" where ");
	// countSql.append(criteriaStr);
	// fullSql.append(" where ");
	// fullSql.append(criteriaStr);
	// }
	//
	// if (StringUtils.isNotBlank(orderBySql)) {
	// fullSql.append(" order by ");
	// fullSql.append(orderBySql);
	// }
	//
	// // System.out.println("BaseDaoImpl -> Full: " + fullSql);
	// // System.out.println("BaseDaoImpl -> Count: " + countSql);
	//
	// List<T> list = null;
	// Long count = new Long(0);
	//
	// try {
	// list = findFromCache(countSql.toString(), parameters.toArray());
	//
	// if ((list != null) && !list.isEmpty()) {
	// count = (Long) list.get(0);
	// }
	//
	// list = null;
	//
	// if (count.intValue() > 0) {
	// list = findFromCache(
	// fullSql.toString(), parameters.toArray(), maxRecord, startAtIndex);
	// sr.setList(list);
	// sr.setTotalRecordCount(count.intValue());
	//
	// }
	// } catch (DataAccessException e) {
	// throw e;
	// }
	//
	// return sr;
	// }
	/**
	 * Return the order by sql
	 *
	 * @param criteria
	 *            SearchCriteria
	 * @param alias
	 *            TODO DOCUMENTTHIS
	 *
	 * @return Order By Sql String
	 */
	// private String getOrderBySql(SearchCriteria criteria, String alias) {
	// Map orderByMap = criteria.getOrderByMap();
	//
	// if ((orderByMap != null) && (orderByMap.size() > 0)) {
	// StringBuffer orderBySql = new StringBuffer();
	//
	// for (Iterator iterator = orderByMap.keySet().iterator();
	// iterator.hasNext();) {
	// String key = (String) iterator.next();
	//
	// if (StringUtils.isNotBlank(key)) {
	// if (StringUtils.isNotBlank(alias)) {
	// orderBySql.append(alias);
	// orderBySql.append(".");
	// }
	//
	// orderBySql.append(key);
	// orderBySql.append(" ");
	// orderBySql.append(orderByMap.get(key));
	//
	// orderBySql.append(", ");
	// }
	// }
	//
	// if (orderBySql.toString().endsWith(", ")) {
	// orderBySql.delete(orderBySql.length() - 2, orderBySql.length());
	// }
	//
	// return orderBySql.toString();
	// }
	//
	// return "";
	// }

	@Override
	public boolean validateIdNoUpdate(LlpUserProfile llpUserProfile) {
		// TODO Auto-generated method stub

		String hql = " from " + LlpUserProfile.class.getName() + " where idType=? and "
				+ "idNo=? and userStatus in ('A', 'P','D') and userRefNo not in (?)";

		List list = getHibernateTemplate().find(hql,
				new Object[] { llpUserProfile.getIdType(), llpUserProfile.getIdNo(), llpUserProfile.getUserRefNo() });

		if (list.size() > 0) {
			LlpUserProfile llpUserProfileNew = (LlpUserProfile) list.get(0);
			return true;
		}

		return false;

	}

	@Override
	public LlpUserProfile findProfileInfoByUserIdNo(String idNo) {
		String hql = "from " + LlpUserProfile.class.getName() + " where idNo=? ";

		List<LlpUserProfile> listResult = getHibernateTemplate().find(hql, idNo);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}

	@Override
	public boolean isPasswordUseByOthers(String userPwd, String loginId) {
		String hql = " from " + LlpUserProfile.class.getName() + " where  " + "userPwd=? and loginId != ? ";
		List list = getHibernateTemplate().find(hql, new Object[] { userPwd, loginId });
		if (list.size() > 10) {
			return true;
		}

		return false;
	}

	@Override
	public LlpUserProfile findLatestActiveUserByIdNo(String idNo) {
		String hql = "from " + LlpUserProfile.class.getName() + " where idNo=? order by updateDt desc ";

		List<LlpUserProfile> listResult = getHibernateTemplate().find(hql, idNo);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	
	@Override
	public LlpUserProfile findLatestProfileByUserRefNo(String userRefNo) {
		String hql = "from " + LlpUserProfile.class.getName() + " where userRefNo=? order by updateDt desc ";

		List<LlpUserProfile> listResult = getHibernateTemplate().find(hql, userRefNo);
		if (listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
	}
	
	@Override
	public boolean checkIsAllowedStatusByUserRefNo(String userRefNo, String status) {
		
			String hql = " from " + LlpUserProfile.class.getName() + " where userRefNo=? and " + "userStatus in (?) ";

			List list = getHibernateTemplate().find(hql, new Object[] { userRefNo, status });

			if (list.size() > 0) {
				LlpUserProfile llpUserProfile = (LlpUserProfile) list.get(0);

				return true;
			}

			return false;

		}

}
