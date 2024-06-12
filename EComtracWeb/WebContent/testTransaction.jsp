<%@page import="com.ssm.llp.base.sec.SecUtils"%>
<%@page import="org.apache.wicket.request.cycle.RequestCycle"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<% 


try{
	SecUtils.loginNTLM("zamzam", "Qwer!234");
}catch(Exception ex){
	out.println(ex);
}

%>