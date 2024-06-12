/**
 * 
 */
package com.ssm.llp.base.webis.client;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.WicketApplication;

/**
 * @author zamzam
 *
 */
public class LlpWebisClient {
    public static String processConversionToLlp(String conversionType,String entityNo, String llpNo, String llpName, String userId)throws Exception{
    	LlpParametersService llpParametersService = (LlpParametersService)WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());
    	String url = "";//llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_webis_url);
    	
    	return processConversionToLlp(url, conversionType, entityNo, llpNo, llpName, userId);
    }
    
    private static String processConversionToLlp(String url, String conversionType,String entityNo, String llpNo, String llpName, String userId)throws Exception{
    	Service  service = new Service();
        Call     call    = (Call) service.createCall();
//        call.setProperty(Call.USERNAME_PROPERTY, "admin");
//    	call.setProperty(Call.PASSWORD_PROPERTY, "");
        call.setTargetEndpointAddress( new java.net.URL(url) );
        call.setOperationName( new QName("LLPService", "processConversionToLlp") );
        call.addParameter( "arg1", XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter( "arg2", XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter( "arg3", XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter( "arg4", XMLType.XSD_STRING, ParameterMode.IN);
        call.addParameter( "arg5", XMLType.XSD_STRING, ParameterMode.IN);
        call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );

        return (String) call.invoke( new Object[] { conversionType,entityNo,llpNo,llpName,userId } );
    }
    
    public static void main(String[] args) throws Exception{
    	
//    	String url = "http://webissec/SSMWEBIS_SEC/services";
    	String url = "http://10.7.34.37/SSMWEBIS_SEC_DEV/services";
    	String conversionType=Parameter.CONVERSION_TYPE_rob;
    	String entityNo ="001861101";
    	String llpNo ="LLP0000099-LGN";
    	String llpName="RA CONSULTING PLT";
    	String userId = "SSM:nazila";
    	
    	try {
    		String s = processConversionToLlp(url, conversionType, entityNo, llpNo, llpName, userId);
    		System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
		
	}
}
