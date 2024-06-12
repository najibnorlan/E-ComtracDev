
<%@page import="com.ssm.llp.base.odt.LLPOdtUtils"%>
<%@page import="com.ssm.common.oo.ws.client.OOConverterClient"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ssm.llp.base.page.WicketApplication"%>

<% 
	

	if(StringUtils.isBlank(OOConverterClient.END_POINT)){
		OOConverterClient.END_POINT="http://cbsprint.ssm.com.my/SSMPrintSvr/services/OOService";
	}
		
	String odtPath = request.getParameter("odtPath");
	if(StringUtils.isNotBlank(odtPath)){
		OOConverterClient.END_POINT = odtPath;
	}
	
	
	
	System.out.println(odtPath);
	System.out.println(OOConverterClient.END_POINT);
	
	String ctxP = WicketApplication.get().getServletContext().getServletContextName();
	out.println(ctxP+"<br>");
	out.println(">>"+request.getContextPath()+"<<");
	
%>
<form action="OdtConfig.jsp" method="post">


<table>
	<tr>
	<td>
	<input type="text" name="odtPath" size="100" value="<%=OOConverterClient.END_POINT%>"/>
	</td>
	</tr>
	<tr>
	<td>
	<input type="submit" value="Submit">
	</td>
	</tr>
</table>
	
</form>