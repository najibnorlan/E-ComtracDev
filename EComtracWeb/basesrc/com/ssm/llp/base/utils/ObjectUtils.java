package com.ssm.llp.base.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import com.ssm.llp.mod1.model.LlpPartnerLink;

public class ObjectUtils {
	
	public static void main(String[] args)throws Exception {
		LlpPartnerLink link = new LlpPartnerLink();
		link.setLlpNo("HSHSDHSHDHS");
		printObject(link);
	}
	
	public static void printObject(Object obj)throws Exception{
		Method method[] = obj.getClass().getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			String field = "";
			if(method[i].getName().startsWith("get")){
				field = method[i].getName().substring(3);
			}else if(method[i].getName().startsWith("is")){
				field = method[i].getName().substring(2);
			}
			if(StringUtils.isNotBlank(field)){
				System.out.print(field+":");
				System.out.println(method[i].invoke(obj, null));
				
			}
			
		}
	}
}
