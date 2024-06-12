/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.common.dao.LlpSupplyInfoDtlDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.model.LlpSupplyInfoHdr;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.LlpSupplyInfoHdrService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class LlpSupplyInfoDtlServiceImpl extends BaseServiceImpl<LlpSupplyInfoDtl,  Long> implements LlpSupplyInfoDtlService{
	@Autowired 
	LlpSupplyInfoDtlDao llpSupplyInfoDtlDao;
	
	@Autowired 
	LlpSupplyInfoHdrService llpSupplyInfoHdrService;
	
	@Autowired 
	LlpParametersService llpParametersService;
	
	@Autowired 
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Override
	public BaseDao getDefaultDao() {
		return llpSupplyInfoDtlDao;
	}

	@Override
	public List<LlpSupplyInfoDtl> findByHdrCode(String hdrCode) {
		SearchCriteria sc = new SearchCriteria("hdrCode",SearchCriteria.EQUAL, hdrCode);
		return llpSupplyInfoDtlDao.findByCriteria(sc).getList();
	}

	@Override
	@Transactional
	public void updateUploadFile(long dtlId, String uploadType, byte[] uploadData) {
		LlpSupplyInfoDtl infoDtl = llpSupplyInfoDtlDao.findById(dtlId);
		
		if(Parameter.SUPPLY_INFO_DTL_UPLOAD_TYPE_profile.equals(uploadType)){
			infoDtl.setProfileData(uploadData);
			infoDtl.setProfileStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete);
		}
		if(Parameter.SUPPLY_INFO_DTL_UPLOAD_TYPE_cert.equals(uploadType)){
			infoDtl.setCertData(uploadData);
			infoDtl.setCertStatus(Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete);
		}
		llpSupplyInfoDtlDao.update(infoDtl);
		
		
		SearchCriteria sc1 = new SearchCriteria("hdrCode",SearchCriteria.EQUAL, infoDtl.getHdrCode());
		
		SearchCriteria sc2 = new SearchCriteria("profileStatus", SearchCriteria.EQUAL, Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process);
		SearchCriteria sc3 = new SearchCriteria("certStatus", SearchCriteria.EQUAL, Parameter.SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process);
		SearchCriteria sc4 = new SearchCriteria(sc2, SearchCriteria.OR, sc3);
		
		
		sc1 = new SearchCriteria(sc1,SearchCriteria.AND, sc4);
		
		
		List listDtl =  llpSupplyInfoDtlDao.findByCriteria(sc1).getList();
		if(listDtl.size()==0){
			
			String expiredDaysIn = llpParametersService.findByCodeTypeValue(Parameter.SUPPLY_INFO_CONFIG, Parameter.SUPPLY_INFO_CONFIG_expired_days_in);
			Calendar expCal = Calendar.getInstance();
			expCal.add(Calendar.DATE, Integer.valueOf(expiredDaysIn));
			
			LlpSupplyInfoHdr llpSupplyInfoHdr = llpSupplyInfoHdrService.findById(infoDtl.getHdrCode());
			llpSupplyInfoHdr.setHdrStatus(Parameter.SUPPLY_INFO_HDR_STATUS_complete);
			llpSupplyInfoHdr.setExpiredDt(expCal.getTime());
			llpSupplyInfoHdrService.update(llpSupplyInfoHdr);
			
			
			List listLlpSupplyInfoDtl =  findByHdrCode(infoDtl.getHdrCode());
			sendPublicEmailNotificationComplete(llpSupplyInfoHdr, listLlpSupplyInfoDtl);
		}
	}

	private void sendPublicEmailNotificationComplete(LlpSupplyInfoHdr infoHdr, List<LlpSupplyInfoDtl> listLlpSupplyInfoDtl) {
		
		String param[] = new String[4];
		param[0] = infoHdr.getHdrCode();
		param[1] = LlpDateUtils.formatDate(infoHdr.getCreateDt());
		param[3] = LlpDateUtils.formatDate(infoHdr.getExpiredDt());
		String subject = "email.notification.supplyInfo.public.complete.subject";
		String body = "email.notification.supplyInfo.public.complete.body";
		
		String profileWord = WicketApplication.resolve("email.notification.supplyInfo.public.profileWord");
		String certWord = WicketApplication.resolve("email.notification.supplyInfo.public.certWord");
		String andWord = WicketApplication.resolve("email.notification.supplyInfo.public.andWord");
		
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
		
		LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(infoHdr.getCreateBy());
		
		mailService.sendMail(llpUserProfile.getEmail(), subject, infoHdr.getHdrCode(), body, param);
	}
}
