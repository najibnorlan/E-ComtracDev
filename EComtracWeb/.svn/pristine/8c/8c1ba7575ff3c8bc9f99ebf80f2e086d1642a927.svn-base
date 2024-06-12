
package com.ssm.llp.mod1.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobBusinessService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpSpecialKeyword;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpSpecialKeywordService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.RocIncorporationService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.base.webis.client.LlpWebisClient;
import com.ssm.llp.mod1.dao.LlpReservedNameDao;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpRegistrationService;
import com.ssm.llp.mod1.service.LlpReservedNameService;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class LlpReservedNameServiceImpl extends BaseServiceImpl<LlpReservedName, String> implements LlpReservedNameService,PaymentInterface {

	@Autowired
	LlpReservedNameDao llpReservedNameDao;

	@Autowired
	LlpRegistrationService llpRegistrationService;

	@Autowired
	LlpSpecialKeywordService llpSpecialKeywordService;

	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	LlpUserProfileService llpUserProfileService;
	
	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;
	
	@Autowired
	RocIncorporationService rocIncorporationService;
	
	@Autowired
	RobBusinessService robBusinessService;	
	
//	@Autowired
//	RobLlpRobConversionService robLlpRobConversionService;
//	
//	@Autowired
//	RocLlpRocConversionService rocLlpRocConversionService;
//	
	private static String llpconversion = "B";

	@Override
	public BaseDao getDefaultDao() {
		return llpReservedNameDao;
	}
	
	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Override
	@Transactional
	public void update(LlpReservedName entity) {
		String nameSearch = cleanName(entity.getApplyLlpName().toUpperCase());
		entity.setApplyLlpName(entity.getApplyLlpName().toUpperCase());
		entity.setNameSearch(nameSearch);

		super.update(entity);
		
		if(StringUtils.isNotBlank(entity.getLlpNo())){
			LlpReservedName llpReservedNameLLpReg = llpReservedNameDao.findByLlpNo(entity.getLlpNo());
			if(llpReservedNameLLpReg!=null&&llpReservedNameLLpReg.getRefNo().equals(entity.getRefNo())){
				LlpRegistration llpRegistration = llpRegistrationService.findById(entity.getLlpNo());
				llpRegistration.setLlpName(entity.getApplyLlpName());
				llpRegistrationService.update(llpRegistration);
			}
			
		}
	}

	@Override
	public LlpReservedName findLatestReserveNameByLlpNo(String llpNo) {
		LlpReservedName llpReservedName = llpReservedNameDao.findByLlpNo(llpNo);
		return llpReservedName;
	}
	
	@Override
	@Transactional
	public void insert(LlpReservedName entity) {
		String name = entity.getApplyLlpName().toUpperCase();
		if(!Parameter.LLP_REG_TYPE_foreign.equals(entity.getRegType())){
			name = name + " PLT";
		}
		String nameSearch = cleanName(name);
		entity.setApplyLlpName(name);
		entity.setNameSearch(nameSearch);
		llpReservedNameDao.insert(entity);
	}
	
	
	@Override
	public List<SSMException> validateNameSearch(String applyLlpName) {
		List<SSMException> listException = new ArrayList<SSMException>();
		
		String originalName = applyLlpName;
		applyLlpName = applyLlpName.toUpperCase().trim();
		
		if (applyLlpName.endsWith("PLT")||applyLlpName.endsWith("LLP")) {
			listException.add(new SSMException("reservedName.page.plt",originalName));
		}
		
		int length = applyLlpName.length();
		//Check Symbol
		applyLlpName = applyLlpName.replaceAll("[^-)(.@'&A-Za-z0-9 ]", "");
		if(applyLlpName.length()!=length){
			String symbolDetect="{";
			Set oriCharSet = new HashSet();
			for (int i = 0; i < originalName.length(); i++) {
				oriCharSet.add(originalName.charAt(i));
			}
			Set charSet = new HashSet();
			for (int i = 0; i < applyLlpName.length(); i++) {
				charSet.add(applyLlpName.charAt(i));
			}
			oriCharSet.removeAll(charSet);
			oriCharSet.remove(" ");
			oriCharSet.remove(null);
			
			for (Iterator iterator = oriCharSet.iterator(); iterator.hasNext();) {
				Character chr = (Character) iterator.next();
				if(StringUtils.isBlank(chr.toString())){
					continue;
				}
				System.out.println(":"+chr.charValue()+":");
				symbolDetect+=chr;
				if(iterator.hasNext()){
					symbolDetect+=", ";
				}
			}
			symbolDetect+="}";
			listException.add(new SSMException("reservedName.page.symbol", originalName, symbolDetect));
		}
		
		
		//Check Le De
		applyLlpName = removeStartWithLaDe(applyLlpName);
		
		//Check state and country
		List<LlpParameters> listCountry = llpParametersService.findByActiveCodeType(Parameter.COUNTRY_CODE);
		List<LlpParameters> listState = llpParametersService.findByActiveCodeType(Parameter.STATE_CODE);
		
		for (int i = 0; i < listCountry.size(); i++) {
			LlpParameters llpParameter = listCountry.get(i);
			if(applyLlpName.startsWith(llpParameter.getCodeDesc().toUpperCase())){
				listException.add( new SSMException("reservedName.page.validate.name.countryName", originalName, llpParameter.getCodeDesc()));
			}
		}
		for (int i = 0; i < listState.size(); i++) {
			LlpParameters llpParameter = listState.get(i);
			if(applyLlpName.startsWith(llpParameter.getCodeDesc().toUpperCase())){
				listException.add( new SSMException("reservedName.page.validate.name.stateName", originalName, llpParameter.getCodeDesc()));
			}
		}
		
		//Special Gazette Offense
		List<LlpSpecialKeyword> listLlpSpecialKeyword = llpSpecialKeywordService.findByCriteria(new SearchCriteria()).getList();//find alll

		String offence ="";
		String gazette ="";
		String control ="";
		for (int i = 0; i < listLlpSpecialKeyword.size(); i++) {
			LlpSpecialKeyword llpSpecialKeyword = listLlpSpecialKeyword.get(i);
			String keyword = llpSpecialKeyword.getVchkeywords().toUpperCase();
			boolean match = false;
			if(applyLlpName.startsWith(keyword)){
				match = true;
			}else if(applyLlpName.endsWith(keyword)){
				match = true;
			}else {
				String wordSplit[] = StringUtils.splitByWholeSeparator(applyLlpName, keyword);
				if(wordSplit.length>1){
					for (int j = 0; j < wordSplit.length; j++) {
						if(wordSplit[j].startsWith(" ")||wordSplit[j].endsWith(" ")){
							match = true;
							break;
						}
					}
				}
			}
			if(match){
				if(Parameter.NAME_SEARCH_TYPE_GAZZETED.equals(llpSpecialKeyword.getChrkeywordtype())){
					gazette+=keyword+", ";
				}else if(Parameter.NAME_SEARCH_TYPE_CONTROL.equals(llpSpecialKeyword.getChrkeywordtype())){
					control+=keyword+", ";
				}else if(Parameter.NAME_SEARCH_TYPE_OFFENCE.equals(llpSpecialKeyword.getChrkeywordtype())){
					offence+=keyword+", ";
				}
			}
		}
		if(gazette.length()>0){
			gazette = "{ " + gazette.substring(0, gazette.length()-2) +" }";
			listException.add( new SSMException("reservedName.page.gazetteName", originalName, gazette ));
		}
		if(control.length()>0){
			control = "{ " + control.substring(0, control.length()-2) +" }";
			listException.add( new SSMException("reservedName.page.controlWord", originalName, control));
		}
		if(offence.length()>0){
			offence = "{ " + offence.substring(0, offence.length()-2) +" }";
			listException.add( new SSMException("reservedName.page.offensiveWord", originalName , offence));
		}
				
		if(listException.size()>0){
			return listException;
		}
		
		applyLlpName = removeEndWithWith(applyLlpName);
		applyLlpName = removeSymbols(applyLlpName);
		
		//Search llpRegistration
		SearchCriteria profBodyTypeNull = new SearchCriteria("profBodyType", SearchCriteria.IS_NULL);
		SearchCriteria profBodyTypeCS = new SearchCriteria("profBodyType", SearchCriteria.EQUAL, Parameter.PROF_BODY_TYPE_cs);
		SearchCriteria profBodyType = new SearchCriteria(profBodyTypeNull, SearchCriteria.OR, profBodyTypeCS);
		
		SearchCriteria registerName = new SearchCriteria("llpName", SearchCriteria.EQUAL, originalName+" PLT");
		registerName = new SearchCriteria(registerName, SearchCriteria.AND, profBodyType);
		Long listLlpReg = llpRegistrationService.findByCriteriaCount(registerName);
		
		if(listLlpReg.longValue()>0){
			listException.add( new SSMException("reservedName.page.registeredName", originalName));
			return listException;
		}
		
		//search from reserve name
		System.out.println("SEARCH:"+applyLlpName);
		SearchCriteria scMain = new SearchCriteria("nameSearch", SearchCriteria.EQUAL, applyLlpName);
		
		scMain = new SearchCriteria(scMain, SearchCriteria.AND, profBodyType);
		
		SearchCriteria llpNoNull = new SearchCriteria("llpNo", SearchCriteria.IS_NOT_NULL);
		SearchCriteria expNameDate = new SearchCriteria("expNameDate", SearchCriteria.GREATER, new Date());
		SearchCriteria llpNExpName = new SearchCriteria(llpNoNull, SearchCriteria.OR, expNameDate);
		
		scMain = new SearchCriteria(scMain, SearchCriteria.AND, llpNExpName);
		
		Long listLlpReservedName = llpReservedNameDao.findByCriteriaCount(scMain);
		if(listLlpReservedName.longValue()>0){
			listException.add( new SSMException("reservedName.page.reservedName", originalName));
		}
		
		
		return listException;
	}

	
	@Override
	public String cleanName(String applyLlpName)  {
		applyLlpName = removeEndWithWith(applyLlpName);
		applyLlpName = removeStartWithLaDe(applyLlpName);
		applyLlpName = removeSymbols(applyLlpName);
		return applyLlpName;
	}
	
	private String removeSymbols(String applyLlpName) {
		String allowSymbol[] = new String[]{" AND "," DAN ","&", ".", "@", "-", "(", ")", "'", " "};
		for (int i = 0; i < allowSymbol.length; i++) {
			applyLlpName = StringUtils.remove(applyLlpName, allowSymbol[i]);
		}
		
		return applyLlpName;
	}
	
	private static String removeEndWithWith(String applyLlpName)  {
		String ending[] = new String[]{"S", " (M)","(M)", " M'SIA", " MSIA", "(M'SIA)", " (M'SIA)", "(MSIA)", " (MSIA)","(MALAYSIA)", " (MALAYSIA)", "MALAYSIA"," MALAYSIA", " M", "ING", "ED", " PLT" };
		int length;
		do {
			length = applyLlpName.length();
			for (int i = 0; i < ending.length; i++) {
				if(applyLlpName.endsWith(ending[i])){
					String tmp = applyLlpName.substring(0, applyLlpName.length()-ending[i].length());
					if(!ending[i].startsWith(" ")){
						if(tmp.endsWith(" ")){
							continue;
						}
					}
//					applyLlpName = applyLlpName.substring(0, applyLlpName.length()-ending[i].length());
					applyLlpName = tmp;
				}
			}
		} while (length!=applyLlpName.length());
		return applyLlpName;
	}
	
	private String removeStartWithLaDe(String applyLlpName)  {
//		String fReplace[] = new String[]{"A ","D ","LE ","LA ","DE ","THE "};
		String fReplace[] = new String[]{"THE "};
		int length;
		do {
			length = applyLlpName.length();
			for (int i = 0; i < fReplace.length; i++) {
				if(applyLlpName.startsWith(fReplace[i])){
					applyLlpName = applyLlpName.replaceFirst(fReplace[i], "");
				}
			}
		} while (length!=applyLlpName.length());
		return applyLlpName;
	}
	
	@Override
	public void sendEmailReserverNameSuccess(LlpReservedName llpReservedName){
		LlpUserProfile userProfile =  llpUserProfileService.findById(llpReservedName.getLodgeBy());
		
		String param[] = new String[6];
		param[0] = llpReservedName.getApplyLlpName();
		param[1] = LlpDateUtils.formatDate(llpReservedName.getCreateDt());
		param[2] = llpParametersService.findByCodeTypeValue(Parameter.RESERVE_NAME_STATUS, llpReservedName.getStatus());
		param[3] = llpReservedName.getRefNo();
		param[4] = LlpDateUtils.formatDate(llpReservedName.getResultDate());
		param[5] = LlpDateUtils.formatDate(llpReservedName.getExpNameDate());
		
		String subject;
		String body;
		//General
		if(StringUtils.isBlank(llpReservedName.getProfBodyType())){
			subject = "email.notification.ns.new.subject";
			body    = "email.notification.ns.new.body";
		}else{//for professional
			subject = "email.notification.ns.prof.subject";
			body    = "email.notification.ns.prof.body";
		}
		mailService.sendMail(UserEnvironmentHelper.getUserenvironment().getEmail(), subject, llpReservedName.getRefNo(), body, param);
	}

	@Override
	@Transactional
	public void updateAfterPay(LlpReservedName llpReservedName,String paymentTransId) {
		llpReservedName.setResultDate(new Date());
		Calendar calExp = Calendar.getInstance();
		calExp.add(Calendar.DATE, 30);		
		llpReservedName.setExpNameDate(calExp.getTime());
		
		update(llpReservedName);
//		seperatte notification send to catter problem
//		sendEmailReserverNameSuccess(llpReservedName);
		
		LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
		llpPaymentTransaction.setAppRefNo(llpReservedName.getRefNo());
		llpPaymentTransactionService.update(llpPaymentTransaction);
	}
	

	@Override
	@Transactional
	public String generateLLPNo(String reserveNameRefNo, String regType,String profBodyType)throws SSMException{
		LlpReservedName llpReservedName = llpReservedNameDao.findById(reserveNameRefNo);
		if(StringUtils.isNotBlank(llpReservedName.getLlpNo())){
			throw new SSMException("reservedName.page.llpNo.already.exist");
		}
		String llpNo = llpReservedNameDao.generateLLPNo(regType, profBodyType);
		if(StringUtils.isNotBlank(llpNo)){
			if(llpReservedName != null){
				llpReservedName.setLlpNo(llpNo);
				llpReservedNameDao.update(llpReservedName);
			}
		}
		return llpNo;
	}
	
	@Override
	@Transactional
	public String generateLLPNoForConversion(String refNo, String conversionType, String regType, String profBodyType)throws SSMException{
		String llpNo = generateLLPNo(refNo, regType, profBodyType);
		
		if(StringUtils.isNotBlank(llpNo)){
			LlpReservedName llpReservedName = llpReservedNameDao.findById(refNo);
			
			try {
				String returnCode = LlpWebisClient.processConversionToLlp(conversionType, llpReservedName.getConversionNo(), llpNo, llpReservedName.getApplyLlpName(), UserEnvironmentHelper.getLoginName());
				if(!Parameter.WEBIS_SUCCESS_CODE.equals(returnCode)){
					throw new SSMException(returnCode);
				}
			} catch (Exception e) {
				throw new SSMException(e);
			}
			
		}
		
		return llpNo;
	}

	@Override
	@Transactional
	public void updateAllNameSearch() {
		List<LlpReservedName> llpName = findByCriteria(new SearchCriteria()).getList();
		for (int i = 0; i < llpName.size(); i++) {
			LlpReservedName reservedName = llpName.get(i);
			String name = cleanName(reservedName.getApplyLlpName().toUpperCase());
			reservedName.setApplyLlpName(reservedName.getApplyLlpName().toUpperCase());
			reservedName.setNameSearch(name);
			llpReservedNameDao.update(reservedName);
		}
		
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		LlpReservedName llpReservedName = (LlpReservedName) obj;
		llpReservedName.setStatus(Parameter.RESERVE_NAME_STATUS_approved);
		updateAfterPay(llpReservedName, paymentTransId);
	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		LlpReservedName llpReservedName = (LlpReservedName) obj;
		if(Parameter.RESERVE_NAME_STATUS_approved.equals(llpReservedName.getStatus()) ){
			sendEmailReserverNameSuccess(llpReservedName);
		}
		
	}


}
