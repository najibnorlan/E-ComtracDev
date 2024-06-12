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
import com.ssm.ezbiz.service.EGHLService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;

public class EGHLRedirectPage extends WebPage implements Serializable, IAjaxIndicatorAware {
	@SpringBean(name = "EGHLService")
	private EGHLService eghlService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;

	public EGHLRedirectPage(EGHLPaymentRequest eghlSaleReq) {

		SignInSession.get().setAttribute("_eghlPaymentRequest", eghlSaleReq);
		String pgURL = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_PG_URL);

		String jsPGScript = "";
		// jsPGScript += "function "+jsPGfuntName+"(){";
		jsPGScript += "my_form=document.createElement('FORM');";
		jsPGScript += "my_form.name='myForm';";
		jsPGScript += "my_form.method='POST';";
		jsPGScript += "my_form.action='" + pgURL + "';";

		Field fields[] = eghlSaleReq.getClass().getFields();
		DecimalFormat df = new DecimalFormat("#.00");
		for (int i = 0; i < fields.length; i++) {
			try {
				String fieldName = fields[i].getName();
				Object fieldValue = fields[i].get(eghlSaleReq);
				if (fieldValue != null) {
					if (NumberUtils.isNumber(fieldValue.toString())) {
						if (Double.valueOf(fieldValue.toString()).doubleValue() == 0) {
							continue;
						}
						if (fieldName.equals("Amount")) {
							fieldValue = df.format(Double.valueOf(fieldValue.toString()));
						}
					}
					fieldValue = StringUtils.replace(fieldValue.toString(), "'", "&quot;");
					jsPGScript += "\nmy_tb=document.createElement('INPUT');\n";
					jsPGScript += "my_tb.type='HIDDEN';\n";
					jsPGScript += "my_tb.name='" + fieldName + "';\n";
					jsPGScript += "my_tb.value='" + fieldValue + "';\n";
					jsPGScript += "my_form.appendChild(my_tb);\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jsPGScript += "document.body.appendChild(my_form);\n";
		jsPGScript += "my_form.submit();\n";
		Label jsPG = new Label("redirectToPG", jsPGScript);
		jsPG.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		add(jsPG);
//		System.out.println(jsPGScript);
	}

	@Override
	public String getAjaxIndicatorMarkupId() {
		return "loadingIndicator_id";
	}

}
