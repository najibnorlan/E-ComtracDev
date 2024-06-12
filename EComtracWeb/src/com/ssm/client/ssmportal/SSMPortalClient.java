package com.ssm.client.ssmportal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ssm.client.ssmportal.SSMPortalServicesStub.BusinessEntry;
import com.ssm.client.ssmportal.SSMPortalServicesStub.CompanyEntry;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindBusinessReturnListE;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindCompanyReturnListE;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindLlp;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindLlpReturnListE;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindRob;
import com.ssm.client.ssmportal.SSMPortalServicesStub.FindRoc;
import com.ssm.client.ssmportal.SSMPortalServicesStub.GSTRegNo;
import com.ssm.client.ssmportal.SSMPortalServicesStub.LlpEntry;
import com.ssm.llp.base.common.Parameter;

public class SSMPortalClient {
	public static void main(String[] args) throws Exception{
		
		try {
			String endpoind = "http://10.7.31.38:9763/services/SSMPortalServices/";
			
//			List<SSMEntity> ssmEnList = findRob("001516361");
			List<SSMEntity> ssmEnList = findRoc(endpoind, "391355");
//			List<SSMEntity> ssmEnList = findLlp("LLP0004610-LGN");
			
			for (int i = 0; i < ssmEnList.size(); i++) {
				ssmEnList.get(i).print();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static List<SSMEntity> findEntity(String endpoint, String entityType, String entityNo)throws Exception{
		if(Parameter.ENTITY_TYPE_ROB.equals(entityType)){
			return findRob(endpoint, entityNo);
		}else if(Parameter.ENTITY_TYPE_ROC.equals(entityType)){
			return findRoc(endpoint, entityNo);
		}else if(Parameter.ENTITY_TYPE_LLP.equals(entityType)){
			return findLlp(endpoint, entityNo);
		}
		return null;
	}
	
	private static List<SSMEntity> findLlp(String endpoint, String entityNo)throws Exception{
		List<SSMEntity> ssmEntityList = new ArrayList<SSMEntity>();
		
		SSMPortalServicesStub portalServicesStub = new SSMPortalServicesStub(endpoint);
		FindLlp findLlp = new FindLlp();
		findLlp.setLlpNo(entityNo);
		FindLlpReturnListE result = portalServicesStub.findLlp(findLlp);
		if(result.getFindLlpReturnList().isLlpEntrySpecified()){
			LlpEntry[] llpEntrys = result.getFindLlpReturnList().getLlpEntry();
			for (int i = 0; i < llpEntrys.length; i++) {
				SSMEntity ssmEntity = new SSMEntity();
				ssmEntity.setEntityType(Parameter.ENTITY_TYPE_LLP);
				ssmEntity.setEntityNo(llpEntrys[i].getLlpNo());
				ssmEntity.setEntityName(llpEntrys[i].getLlpName());
				ssmEntity.setEntityStatus(llpEntrys[i].getLlpStatus());
				
				if(llpEntrys[i].getFindGSTRegNoList().isGSTRegNoSpecified()){
					GSTRegNo[] gstRegList = llpEntrys[i].getFindGSTRegNoList().getGSTRegNo();
					ssmEntity.setEntityGsts(new String[gstRegList.length]);
					for (int j = 0; j < gstRegList.length; j++) {
						ssmEntity.getEntityGsts()[j] = gstRegList[j].getVchgstnumber();
					}
				}
				ssmEntityList.add(ssmEntity);
			}
		}
		return ssmEntityList;
	}
	
	private static List<SSMEntity> findRoc(String endpoint, String entityNo)throws Exception{
		List<SSMEntity> ssmEntityList = new ArrayList<SSMEntity>();
		
		SSMPortalServicesStub portalServicesStub = new SSMPortalServicesStub(endpoint);
		FindRoc findRoc = new FindRoc();
		findRoc.setCompanyNo(entityNo);
		FindCompanyReturnListE result = portalServicesStub.findRoc(findRoc);
		if(result.getFindCompanyReturnList().isCompanyEntrySpecified()){
		
			CompanyEntry[] companyEntrys = result.getFindCompanyReturnList().getCompanyEntry();
			for (int i = 0; i < companyEntrys.length; i++) {
				SSMEntity ssmEntity = new SSMEntity();
				ssmEntity.setEntityType(Parameter.ENTITY_TYPE_ROC);
				ssmEntity.setEntityNo(companyEntrys[i].getCompanyNo());
				ssmEntity.setEntityCheckDigit(companyEntrys[i].getChkDigit());
				ssmEntity.setEntityName(companyEntrys[i].getCompanyName());
				ssmEntity.setEntityStatus(companyEntrys[i].getComStatus());
				
				if(companyEntrys[i].getFindGSTRegNoList().isGSTRegNoSpecified()){
					GSTRegNo[] gstRegList = companyEntrys[i].getFindGSTRegNoList().getGSTRegNo();
					ssmEntity.setEntityGsts(new String[gstRegList.length]);
					for (int j = 0; j < gstRegList.length; j++) {
						ssmEntity.getEntityGsts()[j] = gstRegList[j].getVchgstnumber();
					}
				}
				ssmEntityList.add(ssmEntity);
			}
		
		}
		return ssmEntityList;
	}
	
	private static List<SSMEntity> findRob(String endpoint, String entityNo)throws Exception{
		List<SSMEntity> ssmEntityList = new ArrayList<SSMEntity>();
		
		SSMPortalServicesStub portalServicesStub = new SSMPortalServicesStub(endpoint);
		FindRob findRob = new FindRob();
		findRob.setBizRegNo(entityNo);
		FindBusinessReturnListE result = portalServicesStub.findRob(findRob);
		if(result.getFindBusinessReturnList().isBusinessEntrySpecified()){
			BusinessEntry[] businessEntrys = result.getFindBusinessReturnList().getBusinessEntry();
			for (int i = 0; i < businessEntrys.length; i++) {
				SSMEntity ssmEntity = new SSMEntity();
				ssmEntity.setEntityType(Parameter.ENTITY_TYPE_ROB);
				ssmEntity.setEntityNo(businessEntrys[i].getBizRegNo());
				ssmEntity.setEntityCheckDigit(businessEntrys[i].getChkDigit());
				ssmEntity.setEntityName(businessEntrys[i].getBizName());
				ssmEntity.setEntityStatus(businessEntrys[i].getBizStatus());
				if(businessEntrys[i].getFindGSTRegNoList().isGSTRegNoSpecified()){
					GSTRegNo[] gstRegList = businessEntrys[i].getFindGSTRegNoList().getGSTRegNo();
					ssmEntity.setEntityGsts(new String[gstRegList.length]);
					for (int j = 0; j < gstRegList.length; j++) {
						ssmEntity.getEntityGsts()[j] = gstRegList[j].getVchgstnumber();
					}
				}
				ssmEntityList.add(ssmEntity);
			}
		}
		return ssmEntityList;
	}
}
