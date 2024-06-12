package com.ssm.supplyinfo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.supplyinfo.dao.SupplyInfoTransHdrDao;
import com.ssm.supplyinfo.model.SupplyInfoTransDtl;
import com.ssm.supplyinfo.model.SupplyInfoTransHdr;
import com.ssm.supplyinfo.service.SupplyInfoTransDtlService;
import com.ssm.supplyinfo.service.SupplyInfoTransHdrService;

@Service
public class SupplyInfoTransHdrServiceImpl extends BaseServiceImpl<SupplyInfoTransHdr, String>implements SupplyInfoTransHdrService, PaymentInterface {
	
	@Autowired
	SupplyInfoTransHdrDao supplyInfoTransHdrDao;
	

	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	@Qualifier("SupplyInfoTransDtlService")
	public SupplyInfoTransDtlService supplyInfoTransDtlService;
	
	

	@Autowired
	@Qualifier("LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@Override 
	public  BaseDao getDefaultDao() {
		return supplyInfoTransHdrDao;
	}


	@Override
	public SupplyInfoTransHdr findAllById(String transCode) {
		return supplyInfoTransHdrDao.findAllById(transCode);
	}


	@Override
	public SupplyInfoTransHdr genAndMergeLatestCart(String loginName, SupplyInfoTransHdr cartHdr) {
		
		SupplyInfoTransHdr latestDraff = supplyInfoTransHdrDao.findLatestDraffHdr(loginName);
		if(latestDraff==null) {
			cartHdr.setOwnerBy(UserEnvironmentHelper.getLoginName());
			supplyInfoTransHdrDao.update(cartHdr);
		}else {
			// merge kan kt sini
			latestDraff = findAllById(latestDraff.getTransCode());
			List<SupplyInfoTransDtl> list = latestDraff.getListSupplyInfoTransDtl();
			
			if(StringUtils.isNotBlank(cartHdr.getTransCode())) {
				List<SupplyInfoTransDtl> listCookies = cartHdr.getListSupplyInfoTransDtl();
				
				Set<String> hashCartCode = new HashSet<String>();
				
				for (int i = 0; i < list.size(); i++) {
					SupplyInfoTransDtl dtlTmp = list.get(i);
					hashCartCode.add(dtlTmp.getEntityType()+dtlTmp.getEntityNo()+dtlTmp.getProdCode()+dtlTmp.getProdLocale());
				}
				
				boolean hasInsert = false;
				
				for (int i = 0; i < listCookies.size(); i++) {
					SupplyInfoTransDtl dtlCookies= listCookies.get(i);
					
					if(!hashCartCode.contains(dtlCookies.getEntityType()+dtlCookies.getEntityNo()+dtlCookies.getProdCode()+dtlCookies.getProdLocale())) {
						SupplyInfoTransDtl dtl = new SupplyInfoTransDtl(dtlCookies.getEntityType(), dtlCookies.getEntityNo(), dtlCookies.getEntityName(), dtlCookies.getProdType(), dtlCookies.getProdCode(),dtlCookies.getAmt(),dtlCookies.getProdLocale());
						dtl.setHdrTransCode(latestDraff.getTransCode());
						supplyInfoTransDtlService.insert(dtl);
						hasInsert = true;
					}
					
				}
				
				if(hasInsert) {
					latestDraff = findAllById(latestDraff.getTransCode());
				}
				
				supplyInfoTransHdrDao.deleteCookiesCart(cartHdr);
				
			}
		}
		
		return latestDraff;
	}


	@Override
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		SupplyInfoTransHdr hdrTmp = (SupplyInfoTransHdr) obj;
		SupplyInfoTransHdr hdr = supplyInfoTransHdrDao.findById(hdrTmp.getTransCode());
		obj = hdr;

		System.out.println("SUCCESS PAYMENT");

		if (Parameter.SUPPLY_INFO_TRANS_STATUS_PENDING_PAYMENT.equals(hdr.getStatus())) {
			LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
			hdr.setPaymentDt(paymentTransaction.getCreateDt());

			hdr.setStatus(Parameter.SUPPLY_INFO_TRANS_STATUS_PAYMENT_SUCCESS);
			supplyInfoTransHdrDao.update(hdr);
		}
	}


	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		SupplyInfoTransHdr hdrTmp = (SupplyInfoTransHdr) obj;
		SupplyInfoTransHdr hdr = supplyInfoTransHdrDao.findById(hdrTmp.getTransCode());
		obj = hdr;

		//CALL GENERATE OUTPUT
		hdr.setStatus(Parameter.SUPPLY_INFO_TRANS_STATUS_SUCCESS);
		supplyInfoTransHdrDao.update(hdr);
	}


	@Override
	public String findEntityNameByTypeNNo(String entityType, String entityNo) {
		
		if(Parameter.ID_TYPE_newic.equals(entityType)) {
			LlpUserProfile llpUserProfile = llpUserProfileService.findLatestActiveUserByIdNo(entityNo);
			if(llpUserProfile!=null) {
				return llpUserProfile.getName();
			}
		}
		return null;
	}

}
