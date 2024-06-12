<%@page import="org.apache.wicket.Session"%>
<%@page import="com.ssm.llp.base.page.HomePage"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ssm.llp.base.common.Parameter"%>
<%@page import="com.ssm.llp.base.page.WicketApplication"%>
<%@page import="com.ssm.llp.base.common.service.LlpParametersService"%>
<%



String ipAddress = request.getHeader("X-Forwarded-For");

out.print("IP Forward:"+ipAddress+"<br>");
if(StringUtils.isBlank(ipAddress)){
	ipAddress=request.getRemoteAddr();
}
out.print("IP Bogel:"+request.getRemoteAddr()+"<br>");

LlpParametersService parametersService = (LlpParametersService) WicketApplication.getServiceNew(LlpParametersService.class.getSimpleName());

String isGetIpByJS = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_GET_IP_BY_JS);
if(Parameter.YES_NO_yes.equals(isGetIpByJS)){
    String listIpNotAllowed = parametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_LIST_SVR_IP_ADDR);
	if(listIpNotAllowed!=null && listIpNotAllowed.indexOf(ipAddress)!=-1){//mean use proxy or others
		String localIp = (String) session.getAttribute(HomePage.CLIENT_LOCAL_IP_ADDR);
		if(StringUtils.isNotBlank(localIp)){
			ipAddress = localIp;
		}
	}
}


String localIp = (String) session.getAttribute(HomePage.CLIENT_LOCAL_IP_ADDR);
out.print("IP From JS:"+localIp+"<br>");

out.print("IPAddress SessionID : "+request.getSession().getAttributeNames()+", IP : "+request.getSession().getAttribute(HomePage.CLIENT_LOCAL_IP_ADDR)+"<br>");
out.print("Selected IP:"+ipAddress+"<br>");

%>

<script>
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
       	console.log("IP:"+ip);
        setSumb(ip);
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

recheckSource();
</script>