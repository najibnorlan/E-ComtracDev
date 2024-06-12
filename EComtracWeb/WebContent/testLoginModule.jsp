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


Connection con = null;
UserTransaction ut = null;
try{
Context ctx = new InitialContext();

DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ssm_llp");
con = (Connection) ds.getConnection();

out.println("<br>con.getAutoCommit(): " + con.getAutoCommit());
out.println("<br>con.TRANSACTION_NONE: " + con.TRANSACTION_NONE);
out.println("<br>con.TRANSACTION_READ_COMMITTED: " + con.TRANSACTION_READ_COMMITTED);
out.println("<br>con.TRANSACTION_READ_UNCOMMITTED: " + con.TRANSACTION_READ_UNCOMMITTED);
out.println("<br>con.TRANSACTION_REPEATABLE_READ: " + con.TRANSACTION_REPEATABLE_READ);
out.println("<br>con.TRANSACTION_SERIALIZABLE: " + con.TRANSACTION_SERIALIZABLE);
out.println("<br>con.getTransactionIsolation(): " + con.getTransactionIsolation());
out.println("<br>con.getTypeMap(): " + con.getTypeMap());
out.println("<br>con.getMetaData(): " + con.getMetaData());

String sql = "select * from rob_payment_transaction where date(request_dt)='2017-09-26'";

Statement st = con.createStatement();
ResultSet rs = st.executeQuery(sql);

while(rs.next()){
	out.println(rs.getString("transaction_id")+"\t"+rs.getString("status")+"</br>");
}

rs.close();
con.close();


}catch(Exception ex){
	out.println(ex);
}

%>