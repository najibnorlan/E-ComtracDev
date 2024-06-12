package com.ssm.llp.mod1.page;

//import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.LlpDateUtils;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.model.LlpReservedName;
import com.ssm.llp.mod1.service.LlpRegistrationService;
import com.ssm.llp.mod1.service.LlpReservedNameService;

@SuppressWarnings( { "rawtypes", "unchecked", "serial" })
public class DetailsLlpInformation extends LlpInformationBasePage {
	@SpringBean(name = "LlpReservedNameService")
	private LlpReservedNameService llpReservedNameService;
	
	/*@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;*/
	
	
	private void init() {
		add(new LlpInformationForm("form", getDefaultModel()));
	}

	private class LlpInformationForm extends Form {
		public LlpInformationForm(String id, IModel m) {
			super(id, m);

			final LlpRegistration llpReg = (LlpRegistration) m.getObject();
			
			final SSMLabel llpNo = new SSMLabel("llpNo");
			add(llpNo);

			final SSMLabel llpName = new SSMLabel("llpName");
			add(llpName);

			SSMLabel regDate = new SSMLabel("regDate",LlpDateUtils.formatDate(llpReg.getRegDate()));
			add(regDate);
			
			SSMLabel llpStatus = new SSMLabel("llpStatus",llpReg.getLlpStatus(),Parameter.LLP_STATUS);
			add(llpStatus);
			
			SSMLabel natureOfBusinessDesc = new SSMLabel("natureOfBusinessDesc");
			add(natureOfBusinessDesc);
			
			SSMLabel regAdd1 = new SSMLabel("regAdd1");
			add(regAdd1);
			
			SSMLabel regAdd2 = new SSMLabel("regAdd2");
			add(regAdd2);
			
			SSMLabel regAdd3 = new SSMLabel("regAdd3");
			add(regAdd3);
			
			SSMLabel regCountry = new SSMLabel("regCountry",llpReg.getRegCountry(),Parameter.COUNTRY_CODE);
			add(regCountry);
			
			SSMLabel regState = new SSMLabel("regState",llpReg.getRegState(),Parameter.STATE_CODE);
			add(regState);
			
			SSMLabel regCity = new SSMLabel("regCity");
			add(regCity);
			
			SSMLabel regPostcode = new SSMLabel("regPostcode");
			add(regPostcode);
			
			SSMLabel bussinessAdd1 = new SSMLabel("bussinessAdd1");
			add(bussinessAdd1);
			
			SSMLabel bussinessAdd2 = new SSMLabel("bussinessAdd2");
			add(bussinessAdd2);
			
			SSMLabel bussinessAdd3 = new SSMLabel("bussinessAdd3");
			add(bussinessAdd3);
			
			SSMLabel bussinessCountryCode = new SSMLabel("bussinessCountryCode",llpReg.getBussinessCountryCode(),Parameter.COUNTRY_CODE);
			add(bussinessCountryCode);
			
			SSMLabel bussinessStateCode = new SSMLabel("bussinessStateCode",llpReg.getBussinessStateCode(),Parameter.STATE_CODE);
			add(bussinessStateCode);
			
			SSMLabel bussinessCity = new SSMLabel("bussinessCity");
			add(bussinessCity);
			
			SSMLabel bussinessPostcode = new SSMLabel("bussinessPostcode");
			add(bussinessPostcode);

			SSMLabel capitalContribution = new SSMLabel("capitalContribution");
			add(capitalContribution);
			
			SSMLabel dateOfChange = new SSMLabel("dateOfChange", LlpDateUtils.formatDate(llpReg.getUpdateDt()));
			add(dateOfChange);
			
			//Get reserved name
			LlpReservedName llpReservedName = llpReservedNameService.findLatestReserveNameByLlpNo(llpReg.getLlpNo());
			
			String convertName = StringUtils.isNotBlank(llpReservedName.getConversionName())?llpReservedName.getConversionName():resolve("llpInfo.page.NA");
			
			SSMLabel conversionName = new SSMLabel("conversionName",convertName);
			add(conversionName);
			
			List<LlpPartnerLink> listPartnerCoLink = llpReg.getLlpPartnerLinks();
			List<LlpPartnerLink> listPartner = new ArrayList<LlpPartnerLink>();
			List<LlpPartnerLink> listCo = new ArrayList<LlpPartnerLink>();
			
			for (Iterator iterator = listPartnerCoLink.iterator(); iterator.hasNext();) {
				LlpPartnerLink llpPartnerLink = (LlpPartnerLink) iterator.next();
				
				if(Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())){
					listCo.add(llpPartnerLink);
				}else if(Parameter.USER_TYPE_partner.equals(llpPartnerLink.getType())){
					listPartner.add(llpPartnerLink);
				}
			}
			
			//Get partner		
			SSMSessionSortableDataProvider dpPartner = new SSMSessionSortableDataProvider(listPartner);
			final SSMDataView<LlpPartnerLink> dataViewPartner = new SSMDataView<LlpPartnerLink>("partnerList", dpPartner) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpPartnerLink> item) {
					final LlpPartnerLink llpPartnerLink = item.getModelObject();

					item.add(new SSMLabel("partnerName", llpPartnerLink.getLlpUserProfile().getName()));
					item.add(new SSMLabel("partnerIdNo", llpPartnerLink.getLlpUserProfile().getIdNo()));
					item.add(new SSMLabel("partnerNationality", llpPartnerLink.getLlpUserProfile().getNationality(), Parameter.NATIONALITY_TYPE));
					
					String fullAdd = llpPartnerLink.getAdd1();
					
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getAdd2())?fullAdd+" "+llpPartnerLink.getAdd2():fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getAdd3())?fullAdd+" "+llpPartnerLink.getAdd3()+"\n":fullAdd;		
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getPostcode())?fullAdd+llpPartnerLink.getPostcode():fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getCity())?fullAdd+" "+llpPartnerLink.getCity()+"\n":fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getState())?fullAdd+getCodeTypeWithValue(Parameter.STATE_CODE, llpPartnerLink.getState()):fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getCountry())?fullAdd+" "+getCodeTypeWithValue(Parameter.COUNTRY_CODE,llpPartnerLink.getCountry()):fullAdd;
					
					item.add(new SSMLabel("partnerFullAdd", fullAdd));
					String appointDt = llpPartnerLink.getAppointmentDate()!=null?LlpDateUtils.formatDate(llpPartnerLink.getAppointmentDate()):resolve("llpInfo.page.NA");
					item.add(new SSMLabel("partnerApptDt", appointDt));
					String resignDt = llpPartnerLink.getResignDate()!=null?LlpDateUtils.formatDate(llpPartnerLink.getResignDate()):resolve("llpInfo.page.NA");
					item.add(new SSMLabel("partnerResDt", resignDt));

				}
			};
			add(dataViewPartner);
			
			//Get CO
			SSMSessionSortableDataProvider dpCo = new SSMSessionSortableDataProvider(listCo);
			final SSMDataView<LlpPartnerLink> dataViewCo = new SSMDataView<LlpPartnerLink>("coList", dpCo) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<LlpPartnerLink> item) {
					final LlpPartnerLink llpPartnerLink = item.getModelObject();

					item.add(new SSMLabel("coName", llpPartnerLink.getLlpUserProfile().getName()));
					item.add(new SSMLabel("coIdNo", llpPartnerLink.getLlpUserProfile().getIdNo()));
					item.add(new SSMLabel("coNationality", llpPartnerLink.getLlpUserProfile().getNationality(), Parameter.NATIONALITY_TYPE));
					
					String fullAdd = llpPartnerLink.getAdd1();
					
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getAdd2())?fullAdd+" "+llpPartnerLink.getAdd2():fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getAdd3())?fullAdd+" "+llpPartnerLink.getAdd3()+"\n":fullAdd;		
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getPostcode())?fullAdd+llpPartnerLink.getPostcode():fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getCity())?fullAdd+" "+llpPartnerLink.getCity()+"\n":fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getState())?fullAdd+getCodeTypeWithValue(Parameter.STATE_CODE, llpPartnerLink.getState()):fullAdd;
					fullAdd = StringUtils.isNotBlank(llpPartnerLink.getCountry())?fullAdd+" "+getCodeTypeWithValue(Parameter.COUNTRY_CODE,llpPartnerLink.getCountry()):fullAdd;
					
					item.add(new SSMLabel("coFullAdd", fullAdd));
					String appointDt = llpPartnerLink.getAppointmentDate()!=null?LlpDateUtils.formatDate(llpPartnerLink.getAppointmentDate()):resolve("llpInfo.page.NA");
					item.add(new SSMLabel("coApptDt", appointDt));
					String resignDt = llpPartnerLink.getResignDate()!=null?LlpDateUtils.formatDate(llpPartnerLink.getResignDate()):resolve("llpInfo.page.NA");
					item.add(new SSMLabel("coResDt", resignDt));

				}
			};
			add(dataViewCo);
			
			Link downloadPdf = new Link("downloadPdf") {
				public void onClick() {
					OutputStream outputStream = null;
					Map mapData =  new HashMap();
					mapData.put("llpName",llpReg.getLlpName());
					mapData.put("llpNo",llpReg.getLlpNo());
					
					String regDateYear="";
					String regDateMonth="";
					String regDateDay="";
					String regDateMonthDesc ="";
					String dCurrentDate = "";
					int yCurrentDate = 0;
					String monthCurrentDate="";
					String yCurrent = "";
					System.out.println("registrationDate:"+llpReg.getRegDate());
					if(llpReg.getRegDate()!=null){
						String  registrationDate = String.valueOf(llpReg.getRegDate());
						
						regDateYear=registrationDate.substring(0,4);
						regDateMonth=registrationDate.substring(5,7);
						regDateDay=registrationDate.substring(8,10);
						int regMonth =  Integer.parseInt(regDateMonth);
						regDateMonthDesc =  getMonthName(regMonth);
						
						//Date Current
						Calendar cal = Calendar.getInstance();
						Date dateCurrent = cal.getTime();
						dCurrentDate = new SimpleDateFormat("dd").format(new Date(dateCurrent.getDay()));
						int mCurrentDate = dateCurrent.getMonth() + 1;
						yCurrentDate = dateCurrent.getYear() + 1900;
						monthCurrentDate = getMonthName(mCurrentDate);
					
						yCurrent = String.valueOf(yCurrentDate);
						System.out.println("yCurrentDate"+yCurrentDate);
						
					}
					System.out.println("yCurrentDate"+yCurrentDate);
					mapData.put("regDateDay",regDateDay);
					mapData.put("regDateMonth", regDateMonthDesc);
					mapData.put("regDateYear",regDateYear);
					mapData.put("dCurrentDate",dCurrentDate);
					mapData.put("yCurrentDate",yCurrent);
					mapData.put("monthCurrentDate",monthCurrentDate);
					try {
					byte bytePdfContent[] = LLPOdtUtils.generatePdf("LLP_CERT_BM", mapData);
					if ( bytePdfContent != null) {
					WebResponse response = (WebResponse) getResponse();
					response.setHeader("Content-disposition", "attachment; filename=llpcertBMOriginal.pdf");
					response.setContentType("application/pdf");
					outputStream = response.getOutputStream();
					outputStream.write(bytePdfContent);
					outputStream.flush();
					}
					} catch (Exception e) {
						// TODO: handle exception
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			};
			add(downloadPdf);
			
			
			Link downloadInfo = new Link("downloadInfo") {
				public void onClick() {
					OutputStream outputStream = null;
					Map mapData =  new HashMap();
					
					/*String status = llpReg.getLlpStatus();
					String state = llpReg.getRegState();
					String bizState = llpReg.getBussinessStateCode();
					String country = llpReg.getRegCountry();
					String bizCountry = llpReg.getBussinessCountryCode();
					
					status = llpParametersService.findByCodeTypeValue(Parameter.LLP_STATUS, status);
					state = llpParametersService.findByCodeTypeValue(Parameter.STATE_CODE, state);
					bizState = llpParametersService.findByCodeTypeValue(Parameter.STATE_CODE, bizState);
					country = llpParametersService.findByCodeTypeValue(Parameter.COUNTRY_CODE, country);
					bizCountry = llpParametersService.findByCodeTypeValue(Parameter.COUNTRY_CODE, bizCountry);
					
					llpReg.setLlpStatus(status);
					llpReg.setRegState(state);
					llpReg.setBussinessStateCode(bizState);
					llpReg.setRegCountry(country);
					llpReg.setBussinessCountryCode(bizCountry);*/
					
					mapData.put("llpReg",llpReg);
					
					List<LlpPartnerLink> listPartnerCoLink = llpReg.getLlpPartnerLinks();
					List<LlpPartnerLink> listPartner = new ArrayList<LlpPartnerLink>();
					List<LlpPartnerLink> listCo = new ArrayList<LlpPartnerLink>();
					
					for (Iterator iterator = listPartnerCoLink.iterator(); iterator.hasNext();) {
						LlpPartnerLink llpPartnerLink = (LlpPartnerLink) iterator.next();
						
						/*String partnerLinkState = llpPartnerLink.getState();
						String partnerLinkCountry = llpPartnerLink.getCountry();
						
						partnerLinkState = llpParametersService.findByCodeTypeValue(Parameter.STATE_CODE, partnerLinkState);
						partnerLinkCountry = llpParametersService.findByCodeTypeValue(Parameter.COUNTRY_CODE, partnerLinkCountry);
						
						llpPartnerLink.setState(partnerLinkState);
						llpPartnerLink.setCountry(partnerLinkCountry);*/
						
						if(Parameter.USER_TYPE_complianceOfficer.equals(llpPartnerLink.getType())){
							listCo.add(llpPartnerLink);
						}else if(Parameter.USER_TYPE_partner.equals(llpPartnerLink.getType())){
							listPartner.add(llpPartnerLink);
						}
					}
					
					mapData.put("listCo", listCo);
					mapData.put("listPartner", listPartner);
					try {
						
						//File fileTemplate = new File("C:/workspace/LLP/LLPWeb/resources/llpInfo.odt");
						byte bytePdfContent[] = LLPOdtUtils.generatePdf("LLP_PROFILE_BM", mapData);
						if ( bytePdfContent != null) {
						WebResponse response = (WebResponse) getResponse();
						response.setHeader("Content-disposition", "attachment; filename=llpInfo.pdf");
						response.setContentType("application/pdf");
						outputStream = response.getOutputStream();
						outputStream.write(bytePdfContent);
						outputStream.flush();
						}
					} catch (Exception e) {
						// TODO: handle exception
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			};
			add(downloadInfo);
			
		}
	}

	public DetailsLlpInformation(final String llpNo) {

		setDefaultModel(new CompoundPropertyModel(
				new LoadableDetachableModel() {
					protected Object load() {
						LlpRegistrationService regService = (LlpRegistrationService) getService(LlpRegistrationService.class
								.getSimpleName());
						LlpRegistration llpRegistration = regService
								.findByIdWithAllInfo(llpNo); // merge all data
						getSession().setAttribute("llpRegistration_",llpRegistration); // set into session
						return llpRegistration;
					}
				}));

		init();

		
	}
	
	public static String getMonthName(int monthName) {
		String malayMonthName = "";
		if (monthName == 1) {
			malayMonthName = "Januari";
		} else if (monthName == 2) {
			malayMonthName = "Februari";
		} else if (monthName == 3) {
			malayMonthName = "Mac";
		} else if (monthName == 4) {
			malayMonthName = "April";
		} else if (monthName == 5) {
			malayMonthName = "Mei";
		} else if (monthName == 6) {
			malayMonthName = "Jun";
		} else if (monthName == 7) {
			malayMonthName = "Julai";
		} else if (monthName == 8) {
			malayMonthName = "Ogos";
		} else if (monthName == 9) {
			malayMonthName = "September";
		} else if (monthName == 10) {
			malayMonthName = "Oktober";
		} else if (monthName == 11) {
			malayMonthName = "November";
		} else if (monthName == 12) {
			malayMonthName = "Disember";
		} else {
			malayMonthName = "Bulan";
		}
		return malayMonthName;
	}

}
