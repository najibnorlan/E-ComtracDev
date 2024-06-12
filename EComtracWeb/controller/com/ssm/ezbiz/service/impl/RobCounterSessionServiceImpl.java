package com.ssm.ezbiz.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.dao.RobCounterSessionDao;
import com.ssm.ezbiz.otcPayment.ListCollectionVerification.ListCollectionVerificationModel;
import com.ssm.ezbiz.service.RobCounterSessionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobCounterCollection;
import com.ssm.llp.ezbiz.model.RobCounterSession;

@Service
public class RobCounterSessionServiceImpl extends BaseServiceImpl<RobCounterSession, Integer> implements RobCounterSessionService{

	@Autowired
	RobCounterSessionDao robCounterSessionDao;
	
	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;
	
	@Override
	public BaseDao getDefaultDao() {
		
		return robCounterSessionDao;
	}
	
	@Override
	public RobCounterSession findByIpAddressStatusNDE(String ipAddress){
		return robCounterSessionDao.findByIpAddressStatusNDE(ipAddress);
	}
	
	@Override
	public RobCounterSession findByUserIdIpAddressOpen(String userId, String ipAddress){
		SearchCriteria sc = new SearchCriteria("counterIpAddress.ipAddress", SearchCriteria.EQUAL, ipAddress);
		sc= sc.andIfNotNull("userId", SearchCriteria.EQUAL, userId);
		sc = sc.andIfNotNull("isOpen", SearchCriteria.EQUAL, 1);
		sc.addOrderBy("createDt", SearchCriteria.DESC);
		
		List<RobCounterSession> counter = findByCriteria(sc).getList();
		if(counter.size() > 0){
			return counter.get(0);
		}
		return null;
	}
	
	@Override
	public SearchCriteria searchCriteriaTemplate(String branch, Date checkoutDate, String floor, String status){
		
		SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SearchCriteria sc = null;
		
		String[] statusNeeded = {Parameter.BALANCING_STATUS_data_entry, Parameter.BALANCING_STATUS_verified, Parameter.BALANCING_STATUS_approved};
		
		if(checkoutDate != null){
			try {
				sc = new SearchCriteria("checkoutDate", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(checkoutDate) + " 00:00:00"));
				sc = sc.andIfNotNull("checkoutDate", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(checkoutDate) + " 23:59:59"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!StringUtils.isBlank(branch)){
			if(sc != null){
				sc = sc.andIfNotNull("branch", SearchCriteria.EQUAL, branch);
			}else{
				sc = new SearchCriteria("branch", SearchCriteria.EQUAL, branch);
			}
		}
		if(!StringUtils.isBlank(floor)){
			if(sc != null){
				sc = sc.andIfNotNull("floorLevel", SearchCriteria.EQUAL, floor);
			}else{
				sc = new SearchCriteria("floorLevel", SearchCriteria.EQUAL, floor);
			}
			
		}
		System.out.println("status : " + status);
		
		if(!StringUtils.isBlank(status) && !"All".equalsIgnoreCase(status)){
				if(sc != null){
					sc = sc.andIfNotNull("balancingStatus", SearchCriteria.EQUAL, status);
				}else{
					sc = new SearchCriteria("balancingStatus", SearchCriteria.EQUAL, status);
				}
		}else{
			if(sc != null){
				sc = sc.andIfInNotNull("balancingStatus", statusNeeded, false);
			}else{
				sc = new SearchCriteria("balancingStatus", SearchCriteria.IN, statusNeeded, false);
			}
		}
		
		return sc;
	}
	
	@Override
	public Double getTotalAmountByCounterList(List<RobCounterSession> counterList){
			
		Double total = 0.0;
		for(RobCounterSession i : counterList){
			Double t = llpPaymentReceiptService.getTotalTransactionByCounterSession(i.getSessionId(), Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
			total = total + t;
		}
		return total;
	}
	
	@Override
	public List<Object> getBranchAndFloorNoBankSlip(String branchCode, String floor, String date, String isApprove){
		return robCounterSessionDao.getBranchAndFloorNoBankSlip(branchCode, floor, date, isApprove);
	}

}
