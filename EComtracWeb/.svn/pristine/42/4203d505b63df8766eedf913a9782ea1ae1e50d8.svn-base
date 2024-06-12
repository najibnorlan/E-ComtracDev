package com.ssm.llp.base.odt;

import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;


public class CodeResolver {
	public String resolveCode(String code,String codeType){
		try {
			LlpParametersService llpParametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
			String resolve = llpParametersService.findByCodeTypeValue(codeType, code);
			if(resolve!=null){
				return resolve;
			}else{
				System.err.println("Cannot resolve:"+code+" for codeType:"+codeType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return code;
	}
	
}
