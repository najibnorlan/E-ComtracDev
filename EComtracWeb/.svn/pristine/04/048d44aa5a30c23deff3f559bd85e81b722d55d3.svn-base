package com.ssm.llp.wicket;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpLocaleMessage;
import com.ssm.llp.base.common.service.LlpAppsvrNodeService;
import com.ssm.llp.base.common.service.LlpLocaleMessageService;
import com.ssm.llp.base.common.service.SSMDistributedService;
import com.ssm.llp.base.page.WicketApplication;

public class SSMResourceLoader implements IStringResourceLoader, SSMDistributedService{
	public static Map<String, LlpLocaleMessage> mapLocalResource;
	
		
	@Override
	public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
		return resolve(key, locale);
	}

	@Override
	public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
		String value = resolve(key, locale);
		if(key.equals(value)){
			if(key.startsWith(component.getId())){
				String newKey = StringUtils.replaceOnce(key, component.getId()+".", "");
				String newValue = resolve(newKey, locale);
				if(!newValue.equals(newKey)){
					value = newValue;
				}
			}
		}
		return value;
	}
	
	private String resolve(String key, Locale locale){
		try {
			init();
			LlpLocaleMessage llpLocaleMessage = getLocaleMessage(key, locale);
			if(llpLocaleMessage==null){
				llpLocaleMessage = getLocaleMessage(key, null);
			}
			
			if(llpLocaleMessage!=null){
				return llpLocaleMessage.getMsg();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return key;
	}
	
	private LlpLocaleMessage getLocaleMessage(String key, Locale locale){
		LlpLocaleMessage llpLocaleMessage;
		if(locale!=null){
			return getLocaleMessageStr(key, locale.toString());
		}
		return getLocaleMessageStr(key, null);
	}
	
	private LlpLocaleMessage getLocaleMessageStr(String key, String localeStr){
		LlpLocaleMessage llpLocaleMessage;
		if(StringUtils.isBlank(localeStr)){
			llpLocaleMessage = mapLocalResource.get(key+":");
		}else{
			llpLocaleMessage = mapLocalResource.get(key+":"+localeStr);
		}
		
		if(llpLocaleMessage==null){
			LlpLocaleMessageService messageService = (LlpLocaleMessageService) WicketApplication.getServiceNew(LlpLocaleMessageService.class.getSimpleName());
			llpLocaleMessage = messageService.findByKeyNLocale(key, localeStr);
			if(llpLocaleMessage!=null){
				mapLocalResource.put(llpLocaleMessage.getKeyLocale(), llpLocaleMessage);
			}
		}
		return llpLocaleMessage;
	}
	
	private void init(){
		if(mapLocalResource==null){
			mapLocalResource = new HashMap<String, LlpLocaleMessage>();
			LlpLocaleMessageService messageService = (LlpLocaleMessageService) WicketApplication.getServiceNew(LlpLocaleMessageService.class.getSimpleName());
			List<LlpLocaleMessage> listLocale = messageService.findByCriteria(new SearchCriteria()).getList();
			for (int i = 0; i < listLocale.size(); i++) {
				LlpLocaleMessage llpLocaleMessage = listLocale.get(i);
				mapLocalResource.put(llpLocaleMessage.getKeyLocale(), llpLocaleMessage);
			}
		}
	}
	

	public void updateCacheMessage(List<LlpLocaleMessage> listLocaleMessage){
		init();
		for (int i = 0; i < listLocaleMessage.size(); i++) {
			LlpLocaleMessage llpLocaleMessage = listLocaleMessage.get(i);
			mapLocalResource.put(llpLocaleMessage.getKeyLocale(), llpLocaleMessage);
		}
		Localizer.get().clearCache();
		syncLocaleOnly(listLocaleMessage);
	}
	
	public void exportToDb(Properties prop,String locale) {
		init();
		LlpLocaleMessageService messageService = (LlpLocaleMessageService) WicketApplication.getServiceNew(LlpLocaleMessageService.class.getSimpleName());
		Enumeration list = prop.propertyNames();
		List<LlpLocaleMessage> listLocaleMessage = new ArrayList();
		while (list.hasMoreElements()) {
			String key = (String) list.nextElement();
			String msg = prop.getProperty(key);
			LlpLocaleMessage llpLocaleMessage = getLocaleMessageStr(key, locale);
			
			if(llpLocaleMessage!=null){
				llpLocaleMessage = messageService.findById(llpLocaleMessage.getLocaleMessageId());
				llpLocaleMessage.setMsg(msg);
				messageService.update(llpLocaleMessage);
			}else{
				llpLocaleMessage = new LlpLocaleMessage();
				llpLocaleMessage.setKey(key);
				llpLocaleMessage.setMsg(msg);
				llpLocaleMessage.setLocale(locale);
				messageService.insert(llpLocaleMessage);
			}
			listLocaleMessage.add(llpLocaleMessage);
		}
		updateCacheMessage(listLocaleMessage);
		
	} 
	
	public void exportToDb(String locale, String filePath) {
		init();
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		exportToDb(prop, locale);
		
	}

	@Override
	public void processDistributed(String cmd,Object objectData) {
		Localizer.get().clearCache();
		if("RefreshFromDB".equals(cmd)){
			mapLocalResource = null;
			init();
		}else if("SyncUpdatedLocaleOnly".equals(cmd)){
			if(mapLocalResource==null){
				init();
			}else{
				List<LlpLocaleMessage> listLocaleMessage = (List<LlpLocaleMessage>) objectData;
				LlpLocaleMessageService messageService = (LlpLocaleMessageService) WicketApplication.getServiceNew(LlpLocaleMessageService.class.getSimpleName());
				for (int i = 0; i < listLocaleMessage.size(); i++) {
					LlpLocaleMessage llpLocaleMessage = listLocaleMessage.get(i);
					llpLocaleMessage = messageService.findByKeyNLocale(llpLocaleMessage.getKey(), llpLocaleMessage.getLocale());
					mapLocalResource.put(llpLocaleMessage.getKeyLocale(), llpLocaleMessage);
				}
			}
		}
	}
	
	private void syncLocaleOnly(List listUpdatedLocale){
		LlpAppsvrNodeService llpAppsvrNodeService = (LlpAppsvrNodeService) WicketApplication.getServiceNew(LlpAppsvrNodeService.class.getSimpleName());
		llpAppsvrNodeService.distributeToAllNode(SSMResourceLoader.class.getSimpleName(), "SyncUpdatedLocaleOnly", listUpdatedLocale);
	}

	public void reloadAllCache() {
		mapLocalResource = null;
		Localizer.get().clearCache();
		init();
		
		LlpAppsvrNodeService llpAppsvrNodeService = (LlpAppsvrNodeService) WicketApplication.getServiceNew(LlpAppsvrNodeService.class.getSimpleName());
		llpAppsvrNodeService.distributeToAllNode(SSMResourceLoader.class.getSimpleName(), "RefreshFromDB", "");
	} 
	
}
