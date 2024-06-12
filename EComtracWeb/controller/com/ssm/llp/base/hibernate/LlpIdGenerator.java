package com.ssm.llp.base.hibernate;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SpringHibernateTemplate;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpRunningNo;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.llp.mod1.model.LlpRegistration;

public class LlpIdGenerator implements IdentifierGenerator,Configurable {
	protected String genCode;
	protected String tableName;
	protected String fieldCode;
	protected String moduleCode;
	protected String yearMonthDay;
	protected String lastNoPattern;
	
	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1)
			throws HibernateException {
		 
		SpringHibernateTemplate hibernateTemplate = new SpringHibernateTemplate(arg0.getFactory());
		
		String runningNo = "";
		List<LlpRunningNo> list = new ArrayList<LlpRunningNo>();
		NumberFormat noFormatter = new DecimalFormat(lastNoPattern);
		if(arg1 instanceof LlpRegistration){
			LlpRegistration llpRegistration = (LlpRegistration) arg1;
			runningNo = generateRegNo(fieldCode,genCode,moduleCode,hibernateTemplate,noFormatter,llpRegistration.getRegType(),llpRegistration.getProfBodyType());
			
		}else{
			
			if(arg1 instanceof LlpPaymentTransaction){
				LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) arg1;
				if(llpPaymentTransaction.getAppRefNo().startsWith("PC2")) {//mean Practising Cert
					if(llpPaymentTransaction.getTransactionType().equals("6")){//Staging
						fieldCode = "PX";
					} else if(llpPaymentTransaction.getTransactionType().equals("7")){//Development
						fieldCode = "PZ";
					}else {
						fieldCode = "P";
					}
				}else {//ezbiz
					if(llpPaymentTransaction.getTransactionType().equals("6")){//Staging
						fieldCode = "EX";
					} else if(llpPaymentTransaction.getTransactionType().equals("7")){//Development
						fieldCode = "EZ";
					}
				}
				
				//ELSE FielCode = E = EZBIZ
			}else if(arg1 instanceof RobCounterBankinSlip){
				RobCounterBankinSlip robCounterBankinSlip = (RobCounterBankinSlip) arg1;
				fieldCode = "E" + robCounterBankinSlip.getBranch();
			}else if(arg1 instanceof LlpPaymentReceipt){
				// bypass auto-generated ID if receipt_no set manually
				LlpPaymentReceipt llpPaymentReceipt = (LlpPaymentReceipt) arg1;
				if(llpPaymentReceipt.getReceiptNo() != null){
					return llpPaymentReceipt.getReceiptNo();  
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat(yearMonthDay);
			Date today = new Date();
			String todayStr = sdf.format(today);
			
			String hqlStr = " from "+LlpRunningNo.class.getName()
					+" where genCode=? and  fieldCode=? and moduleCode=? and yearMonthDay=? ";
				list = hibernateTemplate.find(hqlStr, new String[]{genCode, fieldCode,moduleCode,todayStr});
				LlpRunningNo llpRunningNo = new LlpRunningNo();
				
				String lastNoStr="";
				int lastNo = 1;
				if(list.size()>0){
					llpRunningNo = list.get(0); 
					lastNo = Integer.valueOf(llpRunningNo.getLastNo());
					lastNo++;
					lastNoStr = noFormatter.format(lastNo);
					llpRunningNo.setLastNo(lastNoStr);
					hibernateTemplate.update(llpRunningNo);
				}else{
					llpRunningNo.setGenCode(genCode);
					llpRunningNo.setFieldCode(fieldCode);
					llpRunningNo.setModuleCode(moduleCode);
					llpRunningNo.setYearMonthDay(todayStr);
					lastNoStr = noFormatter.format(lastNo);
					llpRunningNo.setLastNo(lastNoStr);
					hibernateTemplate.save(llpRunningNo);
				}
				
				
				runningNo=fieldCode+moduleCode+todayStr+lastNoStr;
		}
		
		
		return runningNo;
	}
	
	public static String generateRegNo (String fieldCode,String genCode,String moduleCode,HibernateTemplate hibernateTemplate,NumberFormat noFormatter,String regType,String profBodyType){
		String runningNo = "";
		runningNo += fieldCode;
		
		
		String hqlStr = " from "+LlpRunningNo.class.getName()
				+" where genCode=? and fieldCode=? ";
		List<LlpRunningNo> list = hibernateTemplate.find(hqlStr, new String[]{genCode, fieldCode});
		
		LlpRunningNo llpRunningNo = new LlpRunningNo();
		String lastNoStr="";
		int lastNo = 1;
		if(list.size()>0){
			llpRunningNo = list.get(0); 
			lastNo = Integer.valueOf(llpRunningNo.getLastNo());
			lastNo++;
			lastNoStr = noFormatter.format(lastNo);
			llpRunningNo.setLastNo(lastNoStr);
			hibernateTemplate.update(llpRunningNo);
		}else{
			llpRunningNo.setGenCode(genCode);
			llpRunningNo.setFieldCode(fieldCode);
			llpRunningNo.setModuleCode(moduleCode);
			lastNoStr = noFormatter.format(lastNo);
			llpRunningNo.setLastNo(lastNoStr);
			hibernateTemplate.save(llpRunningNo);
		}
		runningNo=fieldCode+lastNoStr+"-"+regType;
		if(StringUtils.isNotBlank(profBodyType) ){
			runningNo+=profBodyType;
		}else{
			runningNo+=Parameter.GENERAL_NAME;
		}
		return runningNo;
	}
	@Override
	public void configure(Type arg0, Properties params, Dialect arg2)
			throws MappingException {
		tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
		genCode = params.getProperty("genCode");
		fieldCode = params.getProperty("fieldCode");
		moduleCode = params.getProperty("moduleCode");
		yearMonthDay = params.getProperty("yearMonthDay");
		lastNoPattern = params.getProperty("lastNoPattern");
	}
	
}
