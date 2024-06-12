package com.ssm.llp.mod1.dao.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.model.LlpRunningNo;
import com.ssm.llp.base.hibernate.LlpIdGenerator;
import com.ssm.llp.mod1.dao.LlpReservedNameDao;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;

@Repository
public class LlpReservedNameDaoImpl extends BaseDaoImpl<LlpReservedName, String> implements LlpReservedNameDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public LlpReservedName findByLlpNo(String llpNo) {
		String hql = "from "+LlpReservedName.class.getName()
				+" where llpNo=? "
				+" and status='A' "
				+" order by idReservedName desc " ;
		
		List<LlpReservedName> listResult = getHibernateTemplate().find(hql,llpNo);

		if(listResult.size()>0){
			return listResult.get(0);
		}
		return null;
	}

	@Override
	public String generateLLPNo(String regType, String profBodyType) {
		String runningNo = null;
		String genCode = "LLP_REG_RUNNING_NO";
		String fieldCode = "LLP";
		String moduleCode = "";
		String lastNoPattern = "0000000";
		List<LlpRunningNo> list = new ArrayList<LlpRunningNo>();
		NumberFormat noFormatter = new DecimalFormat(lastNoPattern);
		
		
		runningNo = LlpIdGenerator.generateRegNo(fieldCode, genCode, moduleCode, getHibernateTemplate(), noFormatter, regType, profBodyType);
		
//		runningNo += fieldCode;
//		
//		String hqlStr = " from "+LlpRunningNo.class.getName()
//				+" where genCode=? and fieldCode=? ";
//		list = getHibernateTemplate().find(hqlStr, new String[]{genCode, fieldCode});
//		
//		LlpRunningNo llpRunningNo = new LlpRunningNo();
//		String lastNoStr="";
//		int lastNo = 1;
//		if(list.size()>0){
//			llpRunningNo = list.get(0); 
//			lastNo = Integer.valueOf(llpRunningNo.getLastNo());
//			lastNo++;
//			lastNoStr = noFormatter.format(lastNo);
//			llpRunningNo.setLastNo(lastNoStr);
//			getHibernateTemplate().update(llpRunningNo);
//		}else{
//			llpRunningNo.setGenCode(genCode);
//			llpRunningNo.setFieldCode(fieldCode);
//			llpRunningNo.setModuleCode(moduleCode);
//			lastNoStr = noFormatter.format(lastNo);
//			llpRunningNo.setLastNo(lastNoStr);
//			getHibernateTemplate().save(llpRunningNo);
//		}
//		runningNo=fieldCode+lastNoStr+"-"+regType;
//		if(StringUtils.isNotBlank(profBodyType) ){
//			runningNo+=profBodyType;
//		}else{
//			runningNo+=Parameter.GENERAL_NAME;
//		}
		
		return runningNo;
	}

	
}



