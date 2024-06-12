package com.ssm.ezbiz.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobCounterBankinSlipDao;
import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.service.RobCounterBankinSlipService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobCounterBankinSlip;
import com.ssm.textfile.RobCounterBankInSlipTextFile;
@Service
public class RobCounterBankinSlipServiceImpl extends
		BaseServiceImpl<RobCounterBankinSlip, String> implements
		RobCounterBankinSlipService {

	@Autowired
	RobCounterBankinSlipDao robCounterBankinSlipDao;
	
	@Autowired
	LlpParametersService llpParametersService;

	@Override
	public BaseDao getDefaultDao() {
		return robCounterBankinSlipDao;
	}

	@Override
	public String generateBankinSlip(
			ListCollectionVerificationModel listCollectionVerificationModel) {
		SimpleDateFormat fom = new SimpleDateFormat("yyyy");

		Integer count = 0;
		String runningNo = null;
		String branch = listCollectionVerificationModel.getBranch();
		String year = fom.format(listCollectionVerificationModel.getDate());
		String slipNumber = null;
		
		SearchCriteria sc = new SearchCriteria("branch", SearchCriteria.EQUAL, branch);
		sc = sc.andIfNotNull("yearCreated", SearchCriteria.EQUAL, year);
		
		count = findByCriteria(sc).getTotalRecordCount();
		
		if(count == 0){
			count = 1;
		}else{
			count++;	
		}
		
		runningNo = String.format("%05d", count);
		slipNumber = branch + year + runningNo;
		
		return slipNumber;
	}
	
	@Override
	public RobCounterBankinSlip findByBankinSlipNo(String slipNo) {
		
		SearchCriteria sc = new SearchCriteria("bankinSlipNo", SearchCriteria.EQUAL, slipNo);
		
		RobCounterBankinSlip robCounterBankinSlip =(RobCounterBankinSlip) findByCriteria(sc).getList().get(0);
		
		return robCounterBankinSlip;
		
	}
	
	@Override
	public byte[] generateTextFile(SearchCriteria sc) throws SSMException {

		List<RobCounterBankinSlip> listRobCounterBankinSlip = robCounterBankinSlipDao.findByCriteria(sc).getList();
		
		try {

			if (listRobCounterBankinSlip.size() > 0) {
				
				List listBankIn = new ArrayList<RobCounterBankInSlipTextFile>();
				
				int lineNo = 1;
				SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat form2 = new SimpleDateFormat("dd-MM-yyyy");
				DecimalFormat df = new DecimalFormat("####,###,###.00");
	
				// set to byte file
				StringBuffer sb = new StringBuffer();
				
				for (RobCounterBankinSlip bankIn :listRobCounterBankinSlip) {
					
					RobCounterBankInSlipTextFile robBankIn = new RobCounterBankInSlipTextFile();
					robBankIn.setCreatedDate(form2.format(bankIn.getCreateDt()));
					robBankIn.setFloor(bankIn.getFloor());
					String branchName = llpParametersService.findByCodeTypeValue(Parameter.BRANCH_CODE, bankIn.getBranch());
					robBankIn.setBranch(branchName);
					robBankIn.setBankInSlipNo(bankIn.getBankinSlipNo());
					robBankIn.setPaymentMode("CASH");
					robBankIn.setAmount(df.format(bankIn.getAmount()));
					robBankIn.setCreatedBy(bankIn.getCreateBy());
					
					listBankIn.add(robBankIn);
				}
				
				
				for (Iterator iterator = listBankIn.iterator(); iterator.hasNext();) {
					
					RobCounterBankInSlipTextFile bankIn = (RobCounterBankInSlipTextFile) iterator.next();
					sb.append(convertObjToStringPipeDelimited(bankIn));
					sb.append(System.getProperty("line.separator"));
				}
				
				return sb.toString().getBytes();
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return null;
	}
	
	public String convertObjToStringPipeDelimited(Object obj) {
		MyStyle myToStringStyle = new MyStyle();

		String str2 = ToStringBuilder.reflectionToString(obj, myToStringStyle);

		return str2 + Parameter.GAF_DELIMITER;
	}
	
	private class MyStyle extends ToStringStyle {
		public MyStyle() {
			super();
			this.setUseClassName(false);
			this.setUseIdentityHashCode(false);
			this.setUseFieldNames(false);
			this.setContentStart("");
			this.setContentEnd("");
			this.setFieldSeparator(Parameter.GAF_DELIMITER);
		}
	}

}
