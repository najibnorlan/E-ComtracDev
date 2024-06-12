package com.ssm.llp.base.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.WSManagementService;

@Service
public class WSManagementServiceImpl implements WSManagementService {

	@Autowired
	LlpParametersService llpParametersService;

	@Override
	public String getWsUrl(String methodName) {
		if (!methodName.equals("SSMInfosClient.generateInfo")) {
//			 return "http://localhost:8082/SSMWEBIS_SEC/services";
//			 return "http://ezbizwebisdevsdc.ssm.com.my/EZBIZ/services";
//			 return "http://ezbizdev.ssm.com.my/SSMWEBIS_SEC/services";
//			return 	"http://10.7.34.221/services";
		}

		String url = llpParametersService.findByCodeTypeValue(Parameter.WS_URL, methodName);
		if (StringUtils.isBlank(url)) {
			url = llpParametersService.findByCodeTypeValue(Parameter.WS_URL, Parameter.WS_URL_DEFAULT);
			if (StringUtils.isNotBlank(url)) {
				LlpParameters param = new LlpParameters();
				param.setCode(methodName);
				param.setCodeType(Parameter.WS_URL);
				param.setCodeDesc(url);
				param.setStatus(Parameter.PARAMETER_STATUS_active);
				param.setSeq(0);
				llpParametersService.insert(param);
			}
		}
		return url;
	}
}
