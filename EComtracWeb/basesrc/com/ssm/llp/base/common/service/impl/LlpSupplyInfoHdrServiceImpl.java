/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.common.dao.LlpSupplyInfoHdrDao;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.model.LlpUserProfile;

@Service
public class LlpSupplyInfoHdrServiceImpl extends BaseServiceImpl<LlpSupplyInfoHdr,  String> implements LlpSupplyInfoHdrService, PaymentInterface{
	@Autowired 
	LlpSupplyInfoHdrDao llpSupplyInfoHdrDao;
	
	@Autowired 
	LlpSupplyInfoDtlService llpSupplyInfoDtlService;
	

	@Autowired 
	LlpParametersService llpParametersService;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;


	@Override
	public BaseDao getDefaultDao() {
		return llpSupplyInfoHdrDao;
	}

	@Override
	@Transactional
	public void submitLlpSupplyInfo(LlpSupplyInfoHdr hdr, List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl) {
		
		
		llpSupplyInfoHdrDao.insert(hdr);
		
		for (int i = 0; i < listLlpSupplyInfoDtl.size(); i++) {
			LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
			if(dtl.getIsProfileSelected()){
				dtl.setProfileStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_pending_payment);
			}
			if(dtl.getIsCertSelected()){
				dtl.setCertStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_pending_payment);
			}
			dtl.setHdrCode(hdr.getHdrCode());
			llpSupplyInfoDtlService.insert(dtl);
		}
		
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		LlpSupplyInfoHdr infoHdr = llpSupplyInfoHdrDao.findById( ((LlpSupplyInfoHdr)obj).getHdrCode());
		infoHdr.setHdrStatus(Parameter.SUPPLY_INFO_HDR_STATUS_in_process);
		llpSupplyInfoHdrDao.update(infoHdr);
		
		List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = llpSupplyInfoDtlService.findByHdrCode( ((LlpSupplyInfoHdr)obj).getHdrCode());
		for (int i = 0; i < listLlpSupplyInfoDtl.size(); i++) {
			LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
			if(dtl.getIsProfileSelected()){
				dtl.setProfileStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process);
			}
			if(dtl.getIsCertSelected()){
				dtl.setCertStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process);
			}
			llpSupplyInfoDtlService.update(dtl);
		}
	}

	private void sendPublicEmailNotification(LlpSupplyInfoHdr infoHdr, List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl) {
		
		String param[] = new String[3];
		param[0] = infoHdr.getHdrCode();
		param[1] = LlpDateUtils.formatDate(infoHdr.getCreateDt());
		
		String subject = "email.notification.supplyInfo.public.new.subject";
		String body = "email.notification.supplyInfo.public.new.body";
		
		String profileWord = WicketApplication.resolve("email.notification.supplyInfo.public.profileWord");
		String certWord = WicketApplication.resolve("email.notification.supplyInfo.public.certWord");
		String andWord = WicketApplication.resolve("email.notification.supplyInfo.public.andWord");
		
		StringBuffer bccList = new StringBuffer();
		bccList.append(llpParametersService.findByCodeTypeValue(Parameter.SUPPLY_INFO_CONFIG, Parameter.SUPPLY_INFO_CONFIG_bccList));
		
		String param2 = "";
		for (int i = 0; i < listLlpSupplyInfoDtl.size(); i++) {
			LlpSupplyInfoDtl dtl = listLlpSupplyInfoDtl.get(i);
			String jenis="";
			if(dtl.getIsProfileSelected()){
				jenis = profileWord;
			}
			if(dtl.getIsCertSelected()){
				if(StringUtils.isNotBlank(jenis)){
					jenis = jenis+" "+andWord+" ";
				}
					
				jenis = jenis + certWord;
			}
			param2 += "<tr>";
			param2 += "<td align=\"center\">"+(i+1)+"</td>";
			param2 += "<td>"+dtl.getEntityNo()+"</td>";
			param2 += "<td>"+dtl.getEntityName()+"</td>";
			param2 += "<td>"+jenis+"</td>";
			param2 += "</tr>";
		}
		param[2] = param2;
		
		mailService.sendMail(UserEnvironmentHelper.getUserenvironment().getEmail(), subject, infoHdr.getHdrCode(), body,  param);
	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		LlpSupplyInfoHdr infoHdr = llpSupplyInfoHdrDao.findById( ((LlpSupplyInfoHdr)obj).getHdrCode());
		if(Parameter.SUPPLY_INFO_HDR_STATUS_in_process.equals(infoHdr.getHdrStatus())){
			List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl = llpSupplyInfoDtlService.findByHdrCode( ((LlpSupplyInfoHdr)obj).getHdrCode());
			sendPublicEmailNotification(infoHdr,listLlpSupplyInfoDtl);
		}
	}

}
