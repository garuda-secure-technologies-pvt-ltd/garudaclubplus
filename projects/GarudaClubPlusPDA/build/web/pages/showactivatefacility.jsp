<%@page import="com.openbravo.pos.ticket.ActivateInfo,java.util.ArrayList,java.util.HashMap " %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>
        <title>ActivateFacility</title>
    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus" class="logo" /><br/><br/>
        </center>
        <center>


            <%     ActivateInfo ainfo = (ActivateInfo) request.getAttribute("atinfo");

        String[] suc = ainfo.getSucc();
        String[] fai = ainfo.getFail();
       

        HashMap<String, String> map = (HashMap<String, String>) request.getSession().getAttribute("map");
        HashMap<String,String> succe=ainfo.getSuccess();
        
        if (suc[0] != null && fai[0] == null) {
            out.println("<font color='red'>ur request is sent to manager approval for <br/>");
            for (String success : suc) {
                if (success != null) {
                    
                    out.println(map.get(success) + "<br/>");
                }
            }
            out.println("facility<br/>");
        } else if (fai[0] != null && suc[0] == null) {
            out.println("<font color='red'>u already sent a request for  <br/>");
            for (String failure : fai) {
                if (failure != null) {
                    out.println(map.get(failure) + "<br/>");
                }
            }
            out.println("facility<br/>");
        } else if (suc[0] != null && fai[0] != null) {
            out.println("<font color='red'>ur request is sent to manager approval for <br/>");
            for (String success : suc) {
                if (success != null) {
                    out.println(map.get(success) + "<br/>");
                }
            }
            out.println("facility<br/>");
            out.println("<font color='red'>u already sent a request for   <br/>");
            for (String failure : fai) {
                if (failure != null) {
                    out.println(map.get(failure) + "<br/>");
                }
            }
            out.println("facility<br/>");
        }
%>
            <div >
                <br/> 
                <a href="showOtherFacility.do"><img src="images/goback.png" onmouseover="this.src='images/goback_mo.png';" onmouseout="this.src='images/goback.png';" alt="GoBack" /></a>
            </div>
        </center>

    </body>
</html>
