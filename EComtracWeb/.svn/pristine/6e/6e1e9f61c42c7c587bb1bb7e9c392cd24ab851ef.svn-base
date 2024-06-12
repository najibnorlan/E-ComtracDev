package com.ssm.llp.base.common.service;

import com.ssm.llp.base.common.model.LlpEmailLog;
import java.util.Date;

public interface MailService {
	public void sendMail(String to, String subject, String refNo, String body, String... bodyParam) ;
	
	public void sendImmediately(String to, String subject, String refNo, String body, String... bodyParam) throws Exception;
	
//	public void sendMail(String to, StringBuffer bccList, String subject, String refNo, String body, Boolean sendImmediately, String... bodyParam);
	
	public void resendEmail(LlpEmailLog llpEmailLog) throws Exception;

	void testSend();
	
	public void sendEmailCalender(Date eventDtFrom, Date eventDtTo, String to, String subject, String refNo, String body, String... bodyParam);
	
	void sendEmailCalenderImmediately(Date eventDtFrom, Date eventDtTo, String to, String subject, String refNo, String body, String... bodyParam)
			throws Exception;
}
