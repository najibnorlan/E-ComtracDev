package com.ssm.llp.base.page;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.eghl.EGHLPaymentRequest;
import com.ssm.ezbiz.mydata.MyDataRequest;
import com.ssm.ezbiz.service.EGHLService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;

public class MyDataRedirectPage extends WebPage implements Serializable, IAjaxIndicatorAware {
	
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	public static final String CTC_CERT = "3";
	public static final String EZBIZ_CLIENT_ID = "2";

	public MyDataRedirectPage(String email, String username, String productId, String entityNo, String checkDigit, String entityName) {
		String pgURL = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_MYDATA_URL);
		String callBack = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_MYDATA_CALLBACK_URL);

		String jsPGScript = "";
		// jsPGScript += "function "+jsPGfuntName+"(){";
		jsPGScript += "my_form=document.createElement('FORM');\n";
		jsPGScript += "my_form.name='myForm';\n";
		jsPGScript += "my_form.method='POST';\n";
		jsPGScript += "my_form.action='" + pgURL + "';\n";

		jsPGScript = addField("email",email, jsPGScript);
		jsPGScript = addField("clientId", EZBIZ_CLIENT_ID , jsPGScript);
		jsPGScript = addField("username",username, jsPGScript);
		jsPGScript = addField("requestId","EB"+System.currentTimeMillis(), jsPGScript);
		jsPGScript = addField("redirectUrl",callBack, jsPGScript);
		
		jsPGScript = addField("docReq[0][entityNo]", entityNo, jsPGScript);
		jsPGScript = addField("docReq[0][checkDigit]",checkDigit, jsPGScript);
		jsPGScript = addField("docReq[0][entityName]",entityName, jsPGScript);
		jsPGScript = addField("docReq[0][productId]",productId, jsPGScript);
		jsPGScript = addField("docReq[0][language]","MY", jsPGScript);
		jsPGScript = addField("docReq[0][isCertified]","true", jsPGScript);
		
		
		jsPGScript += "document.body.appendChild(my_form);\n";
		jsPGScript += "my_form.submit();\n";
		Label jsPG = new Label("redirectToPG", jsPGScript);
		jsPG.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		add(jsPG);
//		System.out.println(jsPGScript);
		System.out.println("");
	}


	private String addField(String fieldName, String fieldValue, String jsPGScript) {
		jsPGScript += "\nmy_tb=document.createElement('INPUT');\n";
		jsPGScript += "my_tb.type='HIDDEN';\n";
		jsPGScript += "my_tb.name='" + fieldName + "';\n";
		jsPGScript += "my_tb.value='" + fieldValue + "';\n";
		jsPGScript += "my_form.appendChild(my_tb);\n";
		
		return jsPGScript;
	}


	@Override
	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}

}
