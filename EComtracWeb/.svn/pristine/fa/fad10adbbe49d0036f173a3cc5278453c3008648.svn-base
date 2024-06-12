package com.ssm.llp.base.page;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap; 

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpUserLog;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpUserLogService;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.AfterLoginInternal;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SamlHomePage extends WebPage implements Serializable, IAjaxIndicatorAware{
	
	@SpringBean(name="LlpUserLogService")
	private LlpUserLogService llpUserLogService;
	

	@SpringBean(name="LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	

	@SpringBean(name="LlpParametersService")
	private LlpParametersService llpParametersService;
	
	public SamlHomePage() {

		HttpServletRequest  request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest();
		String sessId = request.getSession().getId();
		
		ServletContext servletContext = request.getSession().getServletContext().getContext("/ezbizsaml");
		
		if(servletContext!=null){
			Map<String, String> mapSession = (Map)servletContext.getAttribute(sessId);
	        if(mapSession!=null){
	        	String username = mapSession.get("username");
	        	
	        	LlpUserProfile llpUserProfile = llpUserProfileService.findProfileInfoByUserId(username);
	        	if(llpUserProfile==null){//mean new user
	        		ssmError("NO PROFILE:"+username);
	        		if(llpUserProfile==null){
	        			return;
	        		}
	        		String uri = "https://eservices.ssm.com.my/_vti_bin/Service.svc/GetProfile/" + username;
	                try {
						URL url = new URL(uri);
//						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						// convert JSON string to Map
						ObjectMapper mapper = new ObjectMapper();
						List<Map<String, String>> list = mapper.readValue(url, new TypeReference< List <Map<String, String>>>(){} );
						Map<String, String> mapProfile = new TreeMap<String, String>();
						for (int i = 0; i < list.size(); i++) {
							Map<String, String> map = list.get(i);
							List<String> listKeyValue =  new ArrayList();
							for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
								String key = (String) iterator.next();
								String value = (String) map.get(key);
								listKeyValue.add(value);
							}
							mapProfile.put(listKeyValue.get(0), listKeyValue.get(1));
						}

						
						llpUserProfile = new LlpUserProfile();
						llpUserProfile.setUserType("CO");
						llpUserProfile.setLoginId(username.toUpperCase());
						llpUserProfile.setUserPwd("");
						llpUserProfile.setIcColour(Parameter.NRIC_COLOUR_none);
						
						llpUserProfile.setEmail(mapProfile.get("PROFILE_Email"));
						llpUserProfile.setAdd1(mapProfile.get("PROFILE_Address_1"));
						llpUserProfile.setAdd2(mapProfile.get("PROFILE_Address_2"));
						llpUserProfile.setAdd3(mapProfile.get("PROFILE_Address_3"));
						
						
						llpUserProfile.setCountry(getCodeTypeWithValue(Parameter.COUNTRY_CODE_ESERVICES_MAPPING, mapProfile.get("PROFILE_Country")));
						llpUserProfile.setState(mapProfile.get("PROFILE_State"));
						llpUserProfile.setCity(mapProfile.get("PROFILE_City"));
						llpUserProfile.setPostcode(mapProfile.get("PROFILE_Postcode"));
						llpUserProfile.setHpNo(mapProfile.get("PROFILE_Mobile_No"));
						llpUserProfile.setOffNo(mapProfile.get("PROFILE_Office_No"));
						llpUserProfile.setFaxNo(mapProfile.get("PROFILE_Fax_No"));
						llpUserProfile.setName(mapProfile.get("PROFILE_Fullname"));
						llpUserProfile.setIdType(mapProfile.get("PROFILE_Identification_Type"));
						llpUserProfile.setIdNo(mapProfile.get("PROFILE_Identification_No"));
						llpUserProfile.setNationality(mapProfile.get("PROFILE_Nationality"));
						llpUserProfile.setGender(mapProfile.get("PROFILE_Gender"));
						llpUserProfile.setRace(mapProfile.get("PROFILE_Race"));
//						userProfile.setOthersRace();
//						userProfile.setDob(mapProfile.get("PROFILE_Birthdate"));
						
						//CHECK THUMBPRINT OVER HERE
//						PROFILE_Identification_Verificatio:null
//						PROFILE_Identification_Verified:False
//						PROFILE_Identification_Verified_x0:null
//						PROFILE_Identification_Verified_x00:null
						
						llpUserProfile.setUserStatus(Parameter.USER_STATUS_pending);
						llpUserProfile.setApproveChannel("eservices");
						
//						llpUserProfileService.insert(userProfile);
					} catch (Exception e) {
						e.printStackTrace();
						return ;
					}
	        	}
	        	
	        	if(llpUserProfile==null){
	        	
		        	SignInSession signInSession = (SignInSession)getSession();
		        	signInSession.setLoginType(Parameter.LOGIN_TYPE_normal);
		        	signInSession.setNoNeedPassword(true);
	                // Sign the user in 
		        	signInSession.signIn(username.toUpperCase(), null);
		        	
//		        	LlpUserLog userLog = new LlpUserLog();
//	            	userLog.setLoginId(UserEnvironmentHelper.getUserenvironment().getLoginName());
//	            	userLog.setLoginTime(new Date());
//	            
//	                String ipAddress = getIpAddress();
//	                userLog.setIpAddress(ipAddress);
//	                
//	                llpUserLogService.insert(userLog);
//	                UserEnvironmentHelper.getUserenvironment().setAttribute("UserLog", userLog);
	                setResponsePage(AfterLoginInternal.class);
	        	}
	        }else{
	        	throw new RedirectToUrlException("/ezbizsaml");
	        }
		}
        
		
	}
	
	@Override
	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}
	
	public String getUrl(HttpServletRequest request) throws ServletException {
		String url = request.getRequestURL().toString();
		return url;
	}
	
	public String getCodeTypeWithValue(String codeType, String value) {
		String codeValue = llpParametersService.findByCodeTypeValue(codeType, value);
		if(codeValue == null){
			return value;
		}else{
			return codeValue;
		}
	}
	
	public String getIpAddress(){
		
    	HttpServletRequest request = (HttpServletRequest)getRequestCycle().getRequest().getContainerRequest(); 
    	String ipAdd = WicketUtils.getIpAddress(request, getSession());
    	
    	return ipAdd;
	}

}
