package com.ssm.llp.base.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.model.LlpEmailLog;
import com.ssm.llp.base.common.service.LlpEmailLogService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.page.WicketApplication;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.HostInfo;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;

@Service
public class MailServiceImpl extends BaseServiceImpl<Object, String> implements MailService {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage simpleMailMessage;
	
	@Autowired
	private LlpEmailLogService llpEmailLogService;
	
	@Autowired
	@Qualifier("LlpParametersService")
	private LlpParametersService llpParametersService;
	

    @Autowired
    private Configuration freemarkerConfig;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
	
	@Transactional
	@Override
	public void sendMail(final String to, final String subject, String refNo, final String body, final String... bodyParam)  {
		if(StringUtils.isBlank(to)) {
			System.err.println(subject+":"+refNo+" email null");
			return;
		}
		try {
			prepareEmailQueue(to, new StringBuffer(),subject, refNo, body, bodyParam, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	@Override
	public void sendEmailCalender(Date eventDtFrom, Date eventDtTo, final String to, final String subject, String refNo, final String body, final String... bodyParam)  {
		if(StringUtils.isBlank(to)) {
			System.err.println(subject+":"+refNo+" email null");
			return;
		}
		try {
			prepareEmailQueue(to, new StringBuffer(),subject, refNo, body, bodyParam, eventDtFrom, eventDtTo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendImmediately(final String to, final String subject, String refNo, final String body, final String... bodyParam) throws Exception {
		
		LlpEmailLog llpEmailLog = prepareEmailQueue(to, new StringBuffer(),subject, refNo, body, bodyParam, null, null);
		try {
			resendEmail(llpEmailLog);
			llpEmailLog.setStatus(Parameter.EMAIL_STATUS_success);
			llpEmailLogService.update(llpEmailLog);
		} catch (Exception e) {
			llpEmailLog.setRemark(e.getMessage());
			llpEmailLog.setStatus(Parameter.EMAIL_STATUS_fail);
			llpEmailLogService.update(llpEmailLog);
			throw e;
		}
    		
	}
	
	@Override
	public void sendEmailCalenderImmediately(Date eventDtFrom, Date eventDtTo, final String to, final String subject, String refNo, final String body, final String... bodyParam) throws Exception {
		
		LlpEmailLog llpEmailLog = prepareEmailQueue(to, new StringBuffer(),subject, refNo, body, bodyParam, eventDtFrom, eventDtTo);
		try {
			resendEmail(llpEmailLog);
			llpEmailLog.setStatus(Parameter.EMAIL_STATUS_success);
			llpEmailLogService.update(llpEmailLog);
		} catch (Exception e) {
			llpEmailLog.setRemark(e.getMessage());
			llpEmailLog.setStatus(Parameter.EMAIL_STATUS_fail);
			llpEmailLogService.update(llpEmailLog);
			throw e;
		}
	}
	
	@Transactional
	public LlpEmailLog prepareEmailQueue(final String to, final StringBuffer bccList, final String subject, String refNo, final String body, final String bodyParam[], Date eventDtFrom, Date eventDtTo) throws Exception {
		String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_deployment_type);
		String subjectForNonProduction = "";
		String bodyForNonProduction = "";
				
		
		if(!Parameter.DEPLOYMENT_TYPE_production.equals(depType)){
			subjectForNonProduction = " ( "+ depType +" TESTING ONLY "+depType+") ";
			bodyForNonProduction = " <BR/>( "+ depType +" TESTING ONLY "+depType+")<BR/> ";
		}
		
		String subjectRes;
		if(StringUtils.isNotBlank(refNo)) {//mean subject not resolve
			String resolveSubjectKey = WicketApplication.resolve(subject);
			if(StringUtils.isBlank(resolveSubjectKey)){
				throw new Exception("Email not send Subject Blank:"+subject);
			}
			subjectRes = subjectForNonProduction+" "+resolveSubjectKey+" ("+refNo+")";
		}else {//mean subject already resolve
			subjectRes = subject;
		}
		String bodyRes = bodyForNonProduction + WicketApplication.resolve(body, bodyParam) + bodyForNonProduction;
		String fromRes = simpleMailMessage.getFrom();
		
		
		if(Parameter.DEPLOYMENT_TYPE_production.equals(depType)){
			bodyRes = StringUtils.replace(bodyRes, "ezbizdev.ssm.com.my", "ezbiz.ssm.com.my");
		}
		
		
		
		
		if(bccList!=null && StringUtils.isNotBlank(bccList.toString().trim())){
			bccList.append(","+fromRes);
		}else{
			bccList.append(fromRes);
		}
		
		LlpEmailLog llpEmailLog = insertLog(fromRes, to, subjectRes, bodyRes, refNo, null, Parameter.EMAIL_STATUS_pending, bccList.toString(), eventDtFrom, eventDtTo);	
		return llpEmailLog;
	}
	
public void resendEmail(final LlpEmailLog llpEmailLog)throws Exception{
	
		String depType = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_deployment_type);
		if(!Parameter.DEPLOYMENT_TYPE_production.equals(depType)){
			if(llpEmailLog.getEmailTo().indexOf("@ssm.com.my")==-1 ){//only send if ssmm email
				String listExtEmail = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_ALLOW_EXT_EMAIL_LIST);
				List<String> list = Arrays.asList(listExtEmail.split(","));
				if(!list.contains(llpEmailLog.getEmailTo())) {
					System.err.println("Not Send Email for Non Production:for email id "+llpEmailLog.getEmailId()+" to "+llpEmailLog.getEmailTo());
					llpEmailLog.setRemark("Not Send Email for Non Production");
					return;
				}
			}
		}
		
//		String smtpHost="173.194.68.26";
//		int smtpPort=25;//
//		String from="ezbiz@ssm.gov.my";
//		String to = llpEmailLog.getEmailTo();
//		String subject = llpEmailLog.getEmailSubject();
//	    String content = llpEmailLog.getEmailBody();
//	    
//	    System.out.println(from);
//	    System.out.println(to);
//	    java.util.Properties props = new java.util.Properties();
//	    props.put("mail.smtp.host", smtpHost);
//	    props.put("mail.smtp.port", "" + smtpPort);
//	    props.put("mail.debug", "true" );
//	    Session session = Session.getDefaultInstance(props, null);
//
//	    Message msg = new MimeMessage(session);
//	    msg.setFrom(new InternetAddress(from));
//	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//	    msg.setSubject(subject);
//	    msg.setText(content);
//
//	    Transport.send(msg);

		try {
			
			if(llpEmailLog.getEventDtFrom()==null) {
				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {
	//					mimeMessage
	//							.addHeader("Content-Transfer-Encoding", "base64");
	//					mimeMessage.setHeader("Content-Disposition", "attachment");
						
						MimeMessageHelper helper = new MimeMessageHelper(
								mimeMessage, true, "UTF-8");
						helper.setFrom(llpEmailLog.getEmailFrom());
						helper.setTo(llpEmailLog.getEmailTo());
						if (llpEmailLog.getBccList() != null
								&& StringUtils.isNotBlank(llpEmailLog.getBccList())) {
	//						helper.setBcc("ezbiz@ssm.gov.my");
						}
	
						helper.setSubject(llpEmailLog.getEmailSubject());
						helper.setText(llpEmailLog.getEmailBody(), true);
						
						
					}
				};
				mailSender.send(preparator);
			}else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

				String eventStartDtStr = sdf.format(llpEmailLog.getEventDtFrom());
				String eventEndDtStr = eventStartDtStr;
				if(llpEmailLog.getEventDtTo()!=null) {
					eventEndDtStr = sdf.format(llpEmailLog.getEventDtTo());
				}
				
				String subject = llpEmailLog.getEmailSubject();
				String content = llpEmailLog.getEmailBody();
				String emailFromWithName = llpEmailLog.getEmailFrom();
				String emailTo = llpEmailLog.getEmailTo();
				
				MimeMessage message = mailSender.createMimeMessage();
		        final MimeMultipart mixedMultipart = new MimeMultipart("mixed");
		        
				
				message.addHeaderLine("method=REQUEST");
		        message.addHeaderLine("charset=UTF-8");
		        message.addHeaderLine("component=VEVENT");
		        
		        
		        final InternetAddress toAddress = new InternetAddress(emailTo);
		        message.setContent(mixedMultipart);
				message.setRecipient(Message.RecipientType.TO, toAddress);
				message.setFrom(emailFromWithName);
				message.setSubject(subject);
				

				InternetAddress senderAdd = (InternetAddress) message.getFrom()[0];
				
				message.setReplyTo(new javax.mail.Address[]
						{
						    new javax.mail.internet.InternetAddress("donotreply@ssm.com.my")
						});
				message.setDescription("");
		        
				String contentCal = content.trim();//"Please renew your business.";
				contentCal = StringUtils.replace(contentCal, "\n", "\\n");
				
				MimeBodyPart bodyPartEvent = new MimeBodyPart();
				final DataSource source = new ByteArrayDataSource(generateICalData(subject, contentCal, eventStartDtStr,  eventEndDtStr, emailTo, emailTo,  senderAdd.getPersonal(), senderAdd.getAddress()), "text/calendar; charset=UTF-8");
				bodyPartEvent.setDataHandler(new DataHandler(source));
				bodyPartEvent.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");
				// Fill the message
				bodyPartEvent.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
				bodyPartEvent.setHeader("Content-ID", "calendar_message");
		        
				
				
				MimeBodyPart bodyPart2 = new MimeBodyPart();
				bodyPart2.setContent(content, "text/html; charset=UTF-8");
				
				mixedMultipart.addBodyPart(bodyPart2);
				
				mixedMultipart.addBodyPart(bodyPartEvent);
				
				Transport.send(message);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void resendEmail2(final LlpEmailLog llpEmailLog)throws Exception{
		
		
//		String smtpHost="173.194.68.26";
//		int smtpPort=25;//
//		String from="ezbiz@ssm.gov.my";
//		String to = llpEmailLog.getEmailTo();
//		String subject = llpEmailLog.getEmailSubject();
//	    String content = llpEmailLog.getEmailBody();
//	    
//	    System.out.println(from);
//	    System.out.println(to);
//	    java.util.Properties props = new java.util.Properties();
//	    props.put("mail.smtp.host", smtpHost);
//	    props.put("mail.smtp.port", "" + smtpPort);
//	    props.put("mail.debug", "true" );
//	    Session session = Session.getDefaultInstance(props, null);
//
//	    Message msg = new MimeMessage(session);
//	    msg.setFrom(new InternetAddress(from));
//	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//	    msg.setSubject(subject);
//	    msg.setText(content);
//
//	    Transport.send(msg);

		try {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
//					mimeMessage
//							.addHeader("Content-Transfer-Encoding", "base64");
//					mimeMessage.setHeader("Content-Disposition", "attachment");
					
					MimeMessageHelper helper = new MimeMessageHelper(
							mimeMessage, true, "UTF-8");
					helper.setFrom(llpEmailLog.getEmailFrom());
					helper.setTo(llpEmailLog.getEmailTo());
					if (llpEmailLog.getBccList() != null
							&& StringUtils.isNotBlank(llpEmailLog.getBccList())) {
//						helper.setBcc("ezbiz@ssm.gov.my");
					}

					helper.setSubject(llpEmailLog.getEmailSubject());
					helper.setText(llpEmailLog.getEmailBody(), true);
				}
			};
			mailSender.send(preparator);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public LlpEmailLog insertLog(String from, String to, String subject, String body, String refNo, String remark, String status, String bccList,Date eventDtFrom, Date eventDtTo){
		LlpEmailLog llpEmailLog = new LlpEmailLog();
		llpEmailLog.setEmailTo(to);
		llpEmailLog.setEmailFrom(from);
		llpEmailLog.setEmailSubject(subject);
		llpEmailLog.setEmailBody(body);
		llpEmailLog.setRefNo(refNo);
		llpEmailLog.setRemark(remark);
		llpEmailLog.setStatus(status);
		llpEmailLog.setSendDate(new Date());
		llpEmailLog.setBccList(bccList);
		llpEmailLog.setEventDtFrom(eventDtFrom);
		llpEmailLog.setEventDtTo(eventDtTo);
		llpEmailLogService.insert(llpEmailLog);
		return llpEmailLog;
		
	}
	
public static String generateICalData(String subject, String content, String eventStartDt, String eventEndDt, String toName, String toAddr, String fromName, String fromAddr) {
		
		// Generate a UID for the event..
		HostInfo hostInfo = new SimpleHostInfo("ssm.com.my");
		UidGenerator ug = new FixedUidGenerator(hostInfo, "1");
		
		if(StringUtils.isBlank(fromName)) {
			fromName = fromAddr;
		}
		
		String str = "BEGIN:VCALENDAR\r\n"
				+ "METHOD:REQUEST\r\n"
				+ "PRODID:Microsoft Exchange Server 2010\r\n"
				+ "VERSION:2.0\r\n"
				+ "BEGIN:VTIMEZONE\r\n"
				+ "TZID:Singapore Standard Time\r\n"
				+ "BEGIN:STANDARD\r\n"
				+ "DTSTART:16010101T000000\r\n"
				+ "TZOFFSETFROM:+0800\r\n"
				+ "TZOFFSETTO:+0800\r\n"
				+ "END:STANDARD\r\n"
				+ "BEGIN:DAYLIGHT\r\n"
				+ "DTSTART:16010101T000000\r\n"
				+ "TZOFFSETFROM:+0800\r\n"
				+ "TZOFFSETTO:+0800\r\n"
				+ "END:DAYLIGHT\r\n"
				+ "END:VTIMEZONE\r\n"
				+ "BEGIN:VEVENT\r\n"
				+ "ORGANIZER;CN="+fromName+":mailto:"+fromAddr+"\r\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN="+toName+":mailto:"+toAddr+"\r\n"
				+ "DESCRIPTION;LANGUAGE=en-US:"+content+"\r\n"
				+ ug.generateUid()+"\r\n"
//				+"UID:040000008200E00074C5B7101A82E00800000000002FF466CE3AC5010000000000000000100000004377FE5C37984842BF9440448399EB02\r\n"
				+ "SUMMARY;LANGUAGE=en-US:"+subject+"\r\n"
				+ "DTSTART;VALUE=DATE:"+eventStartDt+"\r\n"
				+ "DTEND;VALUE=DATE:"+eventEndDt+"\r\n"
				+ "CLASS:PUBLIC\r\n"
				+ "PRIORITY:5\r\n"
//				+ "DTSTAMP:20210922T064144Z\r\n"
				+ "TRANSP:OPAQUE\r\n"
				+ "STATUS:CONFIRMED\r\n"
				+ "SEQUENCE:0\r\n"
				+ "LOCATION;LANGUAGE=en-US:ezbiz.ssm.com.my\r\n"
				+ "X-MICROSOFT-CDO-APPT-SEQUENCE:0\r\n"
				+ "X-MICROSOFT-CDO-OWNERAPPTID:-701065243\r\n"
				+ "X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE\r\n"
				+ "X-MICROSOFT-CDO-INTENDEDSTATUS:FREE\r\n"
				+ "X-MICROSOFT-CDO-ALLDAYEVENT:TRUE\r\n"
				+ "X-MICROSOFT-CDO-IMPORTANCE:1\r\n"
				+ "X-MICROSOFT-CDO-INSTTYPE:0\r\n"
				+ "X-MICROSOFT-DONOTFORWARDMEETING:FALSE\r\n"
				+ "X-MICROSOFT-DISALLOW-COUNTER:FALSE\r\n"
				+ "X-MICROSOFT-LOCATIONS:[ { \"DisplayName\" : \"ezbiz.ssm.com.my\"\\, \"LocationAnn\r\n"
				+ " otation\" : \"\"\\, \"LocationSource\" : 0\\, \"Unresolved\" : true\\, \"LocationUri\"\r\n"
				+ "  : \"\" } ]\r\n"
				+ "BEGIN:VALARM\r\n"
				+ "DESCRIPTION:REMINDER\r\n"
				+ "TRIGGER;RELATED=START:-PT18H\r\n"
				+ "ACTION:DISPLAY\r\n"
				+ "END:VALARM\r\n"
				+ "BEGIN:VALARM\r\n"
				+ "ACTION:DISPLAY\r\n"
				+ "DESCRIPTION:This is an event reminder\r\n"
				+ "TRIGGER:-P6DT15H0M0S\r\n"
				+ "END:VALARM\r\n"
				+ "END:VEVENT\r\n"
				+ "END:VCALENDAR\r\n"
				+ "";
		
		return str;
	}

	@Override
	public BaseDao getDefaultDao() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void testSend() {
		
//		testTemplate.html
		

        Map model = new HashMap();
        model.put("name", "Memorynotfound.com");
        model.put("location", "Belgium");
        model.put("signature", "https://memorynotfound.com");
        
        try {
			
		
        System.out.println("freemarkerConfig:"+freemarkerConfig);
        
        
        String emailTemplatePath = "emailTest/";
        String attachmentPath = "templates/";
        
        
        Template t = freemarkerConfig.getTemplate(emailTemplatePath+"testTemplate.html");
        
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,"UTF-8");
        
        helper.addAttachment("logo.png", new ClassPathResource(attachmentPath+"logo.png"));//templates
        System.out.println(">>"+new ClassPathResource(attachmentPath+"logo.png").exists());

        helper.setTo("zamzam@ssm.com.my");
        helper.setText(html, true);
        helper.setSubject("Test");
        helper.setFrom("htmlsender@ssm.com.my");

        mailSender.send(message);
        
        System.out.println(html);
        } catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public static void main(String[] args)throws Exception {
		

//			String smtpHost="173.194.68.26";
//			int smtpPort=25;//
//			String from="ezbiz@ssm.gov.my";
//			String to="mzamzam82@gmail.com";
//			String subject="test baru esmtp yihaa"+System.currentTimeMillis();
//		    String content="sdhshadsah";
		    
			String smtpHost="smtp01.ssm.com.my";
			int smtpPort=25;
			
			
			
			String from="ezbiz@ssm.com.my";
			String to="mzamzam82@gmail.com";
			String subject="Test from : "+smtpHost+" : "+System.currentTimeMillis();
		    String content="sdhshadsah";
		    
//			<!--Resource name="mail/Session"
//		              auth="Container"
//		              type="javax.mail.Session"
//		              mail.debug="true"              
//		              mail.transport.protocol="smtp"
//		              mail.smtp.host="mail.ssm.com.my"
//		              mail.smtp.port="25"
//		              mail.smtp.user="ezbiz"
//		              mail.smtp.auth="false"
//		              password="93023123"
//		              /-->
//			<!--

		    java.util.Properties props = new java.util.Properties();
		    props.put("mail.smtp.host", smtpHost);
		    props.put("mail.smtp.port", "" + smtpPort);
		    props.put("mail.debug", "true" );
		    Session session = Session.getDefaultInstance(props, null);

		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(from));
		    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		    msg.setSubject(subject);
		    msg.setText(content);

		    Transport.send(msg);
		    System.out.println("Done");

//		  public static void main(String[] args) throws Exception {
//		    send("hostname", 25, "j@s.com", "s@s.com", "re: dinner", "body");
//		  }
	}

}
