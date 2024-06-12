
<%@page import="org.apache.wicket.request.cycle.IRequestCycleListener"%>
<%@page import="org.apache.wicket.request.cycle.RequestCycleListenerCollection"%>
<%@page import="org.apache.wicket.page.PageAccessSynchronizer.PageLock"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.apache.wicket.Session"%>
<%@page import="org.apache.wicket.util.time.Duration"%>
<%@page import="org.apache.wicket.page.PageAccessSynchronizer"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="com.ssm.llp.base.page.WicketApplication"%>
<%@page import="org.apache.wicket.Application"%>
<%@page import="org.apache.wicket.ThreadContext"%>
<%@page import="org.apache.wicket.protocol.http.WebSession"%>
<%
	String ipAddress = request.getRemoteAddr();
	
	out.println("IP NORMAL:"+ipAddress);
	//is client behind something?
	ipAddress = request.getHeader("X-FORWARDED-FOR");
	
	
	out.println("<br>IP X-FORWARD:"+ipAddress+"<br>");
	
	
%>
<body class="loginbody" onload="recheckSource()">
	IP By JS:<span id='ipSrc'></span>
</body>
<script type="text/javascript">	
function recheckSource() { //  onNewIp - your listener function for new IPs
    //compatibility for firefox and chrome
    var myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
    var pc = new myPeerConnection({
        iceServers: []
    }),

    noop = function() {},
    localIPs = {},
    ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g,
    key;

    function iterateIP(ip) {
    		//alert(ip);
        if(ip.length>16){
        	return;
        }
        
       // if (!localIPs[ip]){ 
         //   onNewIP(ip);
           // alert('sasa');
        //}
        localIPs[ip] = true;
        //setSumb(ip);
        document.getElementById("ipSrc").innerHTML = ip;
    }

     //create a bogus data channel
    pc.createDataChannel("");

    // create offer and set local description
    pc.createOffer(function(sdp) {
        sdp.sdp.split('\n').forEach(function(line) {
            if (line.indexOf('candidate') < 0) return;
            line.match(ipRegex).forEach(iterateIP);
        });
        
        pc.setLocalDescription(sdp, noop, noop);
    }, noop); 

    //listen for candidate events
    pc.onicecandidate = function(ice) {
        if (!ice || !ice.candidate || !ice.candidate.candidate || !ice.candidate.candidate.match(ipRegex)) return;
        ice.candidate.candidate.match(ipRegex).forEach(iterateIP);
    };
}


</script>